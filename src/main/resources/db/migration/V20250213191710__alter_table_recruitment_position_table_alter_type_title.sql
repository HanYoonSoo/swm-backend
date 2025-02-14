create type recruitment_position_title as enum ('BACKEND', 'FRONTEND', 'ETC');

alter table study_recruitment_position
    alter column title type recruitment_position_title USING (title::text::recruitment_position_title);
