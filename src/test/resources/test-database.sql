DROP TABLE IF EXISTS permission;
CREATE TABLE permission
(
   id           INTEGER AUTO_INCREMENT PRIMARY KEY,
   create_date  TIMESTAMP DEFAULT now(),
   modify_date  TIMESTAMP DEFAULT now(),
   name         VARCHAR(250),
   object_type  VARCHAR(50),
   object_label VARCHAR(250),
   description  VARCHAR(500)
);

ALTER TABLE permission ADD UNIQUE permission_u_name (name);
ALTER TABLE permission ADD INDEX permission_i_object_label_object_type (object_label, object_type);



DROP TABLE IF EXISTS role;
CREATE TABLE role
(
   id          INTEGER AUTO_INCREMENT PRIMARY KEY,
   create_date TIMESTAMP DEFAULT now(),
   modify_date TIMESTAMP DEFAULT now(),
   name        VARCHAR(100),
   description VARCHAR(500)
);

ALTER TABLE role ADD UNIQUE role_u_name (name);



DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
   id          INTEGER AUTO_INCREMENT PRIMARY KEY,
   create_date TIMESTAMP DEFAULT now(),
   modify_date TIMESTAMP DEFAULT now(),
   email       VARCHAR(200),
   full_name   VARCHAR(100)
);

ALTER TABLE `user` ADD UNIQUE user_u_email(email);



DROP TABLE IF EXISTS user_role_int;
CREATE TABLE user_role_int
(
   id          INTEGER AUTO_INCREMENT PRIMARY KEY,
   create_date TIMESTAMP DEFAULT now(),
   modify_date TIMESTAMP DEFAULT now(),
   user_id     INTEGER NOT NULL,
   role_id     INTEGER NOT NULL
);

ALTER TABLE user_role_int ADD UNIQUE user_role_int_u_user_id_role_id (user_id, role_id);



DROP TABLE IF EXISTS role_permission_int;
CREATE TABLE role_permission_int
(
   id            INTEGER AUTO_INCREMENT PRIMARY KEY,
   create_date   TIMESTAMP DEFAULT now(),
   modify_date   TIMESTAMP DEFAULT now(),
   role_id       INTEGER NOT NULL,
   permission_id INTEGER NOT NULL
);

ALTER TABLE role_permission_int ADD UNIQUE role_permission_int_u_role_id_permission_id (role_id, permission_id);
ALTER TABLE role_permission_int ADD index role_permission_int_i_permission_id (permission_id);


DROP TABLE IF EXISTS user_permission_int;
CREATE TABLE user_permission_int
(
   id            INTEGER AUTO_INCREMENT PRIMARY KEY,
   create_date   TIMESTAMP DEFAULT now(),
   modify_date   TIMESTAMP DEFAULT now(),
   user_id       INTEGER NOT NULL,
   permission_id INTEGER NOT NULL
);

ALTER TABLE user_permission_int ADD UNIQUE user_permission_int_u_user_id_permission_id (user_id, permission_id);
ALTER TABLE user_permission_int ADD index user_permission_int_i_permission_id (permission_id);
