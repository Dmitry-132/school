ALTER TABLE student --В одном ALTER TABLE можно использовать только один тип операции
    ADD CONSTRAINT uq_student_name --CONSTRAINT ,,, - имя ограничения,первые символы - префиксы, маркируют ограничения по типам
        UNIQUE (name),
    ADD CONSTRAINT chk_student_age --в SQL	стиль наименования snake_case
        CHECK (age >= 16);

ALTER TABLE student
    ALTER COLUMN name SET NOT NULL,
    ALTER COLUMN age SET DEFAULT 20;

ALTER TABLE faculty
    ADD CONSTRAINT uq_faculty_name_color
        UNIQUE (name, color);
