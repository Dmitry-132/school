--liquibase formatted sql

--changeset ds:1
CREATE INDEX IF NOT EXISTS idx_student_name ON student (name);
CREATE INDEX IF NOT EXISTS idx_faculty_name_color ON faculty (name, color);