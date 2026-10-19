--liquibase formatted sql

--changeset ds:1
CREATE TABLE IF NOT EXISTS faculty
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL,
    CONSTRAINT uq_faculty_name_color UNIQUE (name, color)
);

CREATE TABLE IF NOT EXISTS student
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    age        INT          NOT NULL DEFAULT 20,
    faculty_id BIGINT,
    CONSTRAINT uq_student_name UNIQUE (name),
    CONSTRAINT chk_student_age CHECK (age >= 16),
    CONSTRAINT fk_student_faculty
        FOREIGN KEY (faculty_id)
            REFERENCES faculty (id)
            ON DELETE SET NULL
            ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS avatar
(
    id         BIGSERIAL PRIMARY KEY,
    file_path  VARCHAR(255),
    file_size  BIGINT,
    media_type VARCHAR(255),
    preview    BYTEA,
    student_id BIGINT UNIQUE,
    CONSTRAINT fk_avatar_student FOREIGN KEY (student_id)
        REFERENCES student (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

--changeset ds:2
CREATE INDEX IF NOT EXISTS idx_student_name ON student (name);
CREATE INDEX IF NOT EXISTS idx_faculty_name_color ON faculty (name, color);


--при изменении --changeset нужно очистить таблицу "DELETE FROM databasechangelog WHERE id = '1' AND author = 'ds';" или "DROP TABLE DATABASECHANGELOG;"
-- при MD5Sum Check Failed нежно прописать в .properties spring.liquibase.clear-checksums=true на 1 запуск. он ТОЛЬКО обнуляет контрольные суммы