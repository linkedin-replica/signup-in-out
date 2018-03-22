-- use created database
USE linkedin;

-- create stored procedures for insertions

DROP PROCEDURE IF EXISTS `insert_user`;
DELIMITER $$
 CREATE PROCEDURE `insert_user`(
 	IN email_val varchar(50),
 	IN password_val varchar(512)
)
   BEGIN
  	INSERT INTO users (email, password)
	VALUES (email_val, password_val);
   END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS `delete_users`;

DELIMITER $$
 CREATE PROCEDURE `delete_users`()
   BEGIN
     DELETE FROM users;
   END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `delete_user`;

DELIMITER $$
 CREATE PROCEDURE `delete_user`(user_email varchar(50))
    BEGIN
      DELETE FROM users WHERE email = user_email;
    END$$
DELIMITER ;

DROP procedure IF EXISTS `search_for_user`;

DELIMITER $$
 CREATE PROCEDURE `search_for_user`(user_email varchar(50))
   BEGIN
     SELECT * FROM users WHERE email = user_email;
   END$$
DELIMITER ;

DROP procedure IF EXISTS `get_user`;

DELIMITER $$
 CREATE PROCEDURE `get_user`(user_id int)
   BEGIN
      SELECT * FROM users WHERE id = user_id;
   END$$
DELIMITER ;

