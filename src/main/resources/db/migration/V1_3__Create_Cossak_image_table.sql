ALTER table cossak_images
    add column user_id bigint not null default 1;

ALTER TABLE cossak_images
ADD CONSTRAINT fk_cossak_images_to_users FOREIGN KEY (user_id)
REFERENCES users(id)
ON DELETE cascade