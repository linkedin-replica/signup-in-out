-- create database
CREATE DATABASE IF NOT EXISTS linkedin;

-- use created database
USE linkedin;

-- create users table
CREATE TABLE IF NOT EXISTS users(
  id int  AUTO_INCREMENT PRIMARY KEY,
  email varchar(50) UNIQUE,
  password  varchar(512)
);

