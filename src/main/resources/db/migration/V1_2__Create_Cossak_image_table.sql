
CREATE TABLE users (
   id bigserial PRIMARY KEY,
   username varchar(100),
   password varchar(100),
   role varchar(10)
)