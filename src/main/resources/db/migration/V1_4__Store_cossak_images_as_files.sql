ALTER TABLE cossak_images
DROP COLUMN IF EXISTS original_image,
DROP COLUMN IF EXISTS translated_image;

ALTER TABLE cossak_images
ALTER COLUMN original_image_name TYPE varchar(255),
ALTER COLUMN translated_image_name TYPE varchar(255);

ALTER TABLE cossak_images
ADD CONSTRAINT uk_cossak_images_original_image_name UNIQUE (original_image_name),
ADD CONSTRAINT uk_cossak_images_translated_image_name UNIQUE (translated_image_name);
