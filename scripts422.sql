CREATE TABLE car
(
    id    SERIAL PRIMARY KEY,
    brand VARCHAR(20)   NOT NULL,
    model VARCHAR(20)   NOT NULL,
    price NUMERIC(8, 2) NOT NULL

);
CREATE TABLE person
(
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(30) NOT NULL,
    age                 INTEGER     NOT NULL,
    has_drivers_license BOOLEAN     NOT NULL DEFAULT FALSE,
    car_id              BIGINT,
    CONSTRAINT fk_person_car    -- fk внешний ключ
        FOREIGN KEY (car_id)    --Внешний ключ для заполнения car_id
            REFERENCES car (id) --Ссылается на car.id
            --UNIQUE -- для OneToOne, оставлю здесь, чтоб потом не искать
            ON DELETE SET NULL  -- при удалении выбранной машины заменяет её на null
            ON UPDATE CASCADE   -- при изменении car.id изменяется и person.car_id

);
ALTER TABLE car
    ADD CONSTRAINT chk_car_price
        CHECK (price >= 0),
    ADD CONSTRAINT uq_car_brand_model
        UNIQUE (brand, model);

ALTER TABLE person
    ADD CONSTRAINT chk_person_age
        CHECK (age >= 0 AND age <= 110),
    ADD CONSTRAINT chk_person_car_requires_license
        CHECK (
            (has_drivers_license = TRUE AND car_id IS NOT NULL)
                OR
            (has_drivers_license = FALSE AND car_id IS NULL)
            );