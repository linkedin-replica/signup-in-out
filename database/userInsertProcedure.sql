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