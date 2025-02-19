package com.jj.swm.domain.study.repository;

import com.jj.swm.domain.study.entity.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcStudyTagRepositoryImpl implements JdbcStudyTagRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(Study study, List<String> tags) {
        String sql = "insert into study_tag(study_id, name) VALUES(?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                String tag = tags.get(i);

                ps.setLong(1, study.getId());
                ps.setString(2, tag);
            }

            @Override
            public int getBatchSize() {
                return tags.size();
            }
        });
    }
}
