CREATE TABLE cossak_images (
    id bigserial PRIMARY KEY,
    original_image bytea,
    original_image_name varchar(50),
    translated_image bytea,
    translated_image_name varchar(50),
    file_type varchar(10),
    text varchar(1000),
    creation_date date
)