package com.jj.swm.domain.study.core.repository.jdbc.impl;

import com.jj.swm.domain.study.core.entity.Study;
import com.jj.swm.domain.study.core.repository.jdbc.JdbcStudyImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class JdbcStudyImageRepositoryImpl implements JdbcStudyImageRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(Study study, List<String> imageUrlList) {
        String sql = "insert into study_image(study_id, image_url) VALUES(?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                String imageUrl = imageUrlList.get(i);

                ps.setLong(1, study.getId());
                ps.setString(2, imageUrl);
            }

            @Override
            public int getBatchSize() {
                return imageUrlList.size();
            }
        });
    }
}
