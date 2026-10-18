--liquibase formatted sql

--changeset ds:1
CREATE TABLE IF NOT EXISTS student
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    age        INT          NOT NULL,
    faculty_id BIGINT
);

CREATE TABLE IF NOT EXISTS faculty
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS avatar
(
    id         BIGSERIAL PRIMARY KEY,
    file_path  VARCHAR(255),
    file_size  BIGINT,
    media_type VARCHAR(255),
    preview    BYTEA,
    student_id BIGINT UNIQUE
);

--changeset ds:2
CREATE INDEX IF NOT EXISTS idx_student_name ON student (name);
CREATE INDEX IF NOT EXISTS idx_faculty_name_color ON faculty (name, color);


--при изменении --changeset нужно очистить таблицу "DELETE FROM databasechangelog WHERE id = '1' AND author = 'ds';" или "DROP TABLE DATABASECHANGELOG;"
-- при MD5Sum Check Failed нежно прописать в .properties spring.liquibase.clear-checksums=true на 1 запуск. он ТОЛЬКО обнуляет контрольные суммы