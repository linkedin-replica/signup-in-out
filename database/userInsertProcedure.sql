-- use created database
USE linkedin;

-- create stored procedures for insertions

DROP PROCEDURE IF EXISTS Insert_User;
DELIMITER //
 CREATE PROCEDURE Insert_User(
 	IN email_val varchar(50),
 	IN password_val varchar(512)
)
   BEGIN
  	INSERT INTO users (email, password)
	VALUES (email_val, password_val);
   END //
 DELIMITER ;


 DROP PROCEDURE IF EXISTS `Dlelete_User`;

 DELIMITER $$
 CREATE PROCEDURE `Dlelete_User`(user_id int)
   BEGIN
     DELETE FROM users WHERE id = user_id;
   END$$
 DELIMITER ;

  DROP PROCEDURE IF EXISTS `Delete_Users`;

  DELIMITER $$
  CREATE PROCEDURE `Delete_Users`()
    BEGIN
      DELETE FROM users;
    END$$
  DELIMITER ;


 DROP procedure IF EXISTS `Search_For_User`;
 DELIMITER $$
 CREATE PROCEDURE `Search_For_User`(user_email varchar(50))
   BEGIN
     SELECT * FROM users WHERE email = user_email;
   END$$
 DELIMITER ;

 DROP procedure IF EXISTS `Get_User`;
 DELIMITER $$
 CREATE PROCEDURE `Get_User`(user_id int)
   BEGIN
      SELECT * FROM users WHERE id = user_id;
   END$$
 DELIMITER ;

 DROP procedure IF EXISTS `Get_Users`;
 DELIMITER $$
 CREATE PROCEDURE `Get_Users`()
   BEGIN
      SELECT * FROM users;
   END$$
 DELIMITER ;

