package com.jj.swm.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jj.swm.global.security.AllowedPaths;
import com.jj.swm.global.security.custom.CustomAccessDeniedHandler;
import com.jj.swm.global.security.custom.CustomAuthenticationEntryPoint;
import com.jj.swm.global.security.custom.CustomOAuth2UserService;
import com.jj.swm.global.security.jwt.JwtAuthenticationFilter;
import com.jj.swm.global.security.jwt.JwtExceptionFilter;
import com.jj.swm.global.security.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oauth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/user").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/user").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/user").authenticated()
                        .requestMatchers(AllowedPaths.getAllowedPaths()).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/studyroom/user/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/studyroom/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/study/user/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/study/**", "/api/v1/comment/**").permitAll()
                        .requestMatchers("/api/v1/files/**").authenticated()
                        .anyRequest().authenticated()

                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .headers(headerConfig ->
                        headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionConfigurer ->
                        sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsConfigurer ->
                        corsConfigurer.configurationSource(corsConfigurationSource()))
                .oauth2Login(oauth -> {
                            oauth.authorizedClientService(authorizedClientService);
                            oauth.userInfoEndpoint(c -> c.userService(oauth2UserService))
                                    .successHandler(oAuth2SuccessHandler);
                        }
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(objectMapper()), JwtAuthenticationFilter.class)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper()))
                        .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper()))
                );

        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedOriginPatterns(Collections.singletonList("*")); // ⭐️ 허용할 origin
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Bean
    public static RoleHierarchy roleHierarchy(){
        return RoleHierarchyImpl.fromHierarchy("""
                ROLE_ADMIN > ROLE_ROOM_ADMIN
                ROLE_ROOM_ADMIN > ROLE_USER
                """);
    }
}
