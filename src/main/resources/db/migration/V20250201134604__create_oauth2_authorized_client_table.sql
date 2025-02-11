CREATE TABLE oauth2_authorized_client (
    client_registration_id varchar(100) NOT NULL,
    principal_name varchar(200) NOT NULL,
    access_token_type varchar(100) NOT NULL,
    access_token_value BYTEA NOT NULL,
    access_token_issued_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    access_token_expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    access_token_scopes varchar(1000) DEFAULT NULL,
    refresh_token_value BYTEA DEFAULT NULL,
    refresh_token_issued_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (client_registration_id, principal_name)
);
