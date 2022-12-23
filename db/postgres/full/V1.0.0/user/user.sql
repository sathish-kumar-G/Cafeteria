-- Create Schema
CREATE SCHEMA IF NOT EXISTS cafeteria_management_system;

-- Create Sequence for User
CREATE SEQUENCE cafeteria_management_system.user_seq
     INCREMENT 1
	 MINVALUE 1
	 MAXVALUE 99999999999999
	 START 1
	 CACHE 1;
	 
-- Create User Table
DROP TABLE IF EXISTS cafeteria_management_system.user;

CREATE TABLE cafeteria_management_system.user(
    user_id int8 NOT NULL default NEXTVAL('cafeteria_management_system.user_seq'),
	first_name varchar(50),
	last_name varchar(50),
	email varchar(250),
	password varchar(250),
	CONSTRAINT user_pk PRIMARY KEY(user_id)
);

-- Update the Sequence Number After all the sample data insert
SELECT SETVAL('cafeteria_management_system.user_seq',(
     SELECT MAX(user_id)+1 FROM cafeteria_management_system.user),FALSE);
	 
-- Create Sequence, Table, Update Sequence
DROP SEQUENCE IF EXISTS cafeteria_management_system.role_seq;

CREATE SEQUENCE cafeteria_management_system.role_seq
     INCREMENT 1
	 MINVALUE 1
	 MAXVALUE 9999999999
	 START 1
	 CACHE 1;

DROP TABLE IF EXISTS cafeteria_management_system.role;

CREATE TABLE cafeteria_management_system.role(
   role_id int8 NOT NULL default NEXTVAL('cafeteria_management_system.role_seq'),
	role_name varchar(50),
	role_desc varchar(300),
	CONSTRAINT role_pk PRIMARY KEY(role_id)
);

SELECT SETVAL('cafeteria_management_system.role_seq',(
     SELECT MAX(role_id)+1 FROM cafeteria_management_system.role),FALSE);
	 
	 
-- Create user_role_map Table

DROP SEQUENCE IF EXISTS cafeteria_management_system.user_role_map_seq;

CREATE SEQUENCE cafeteria_management_system.user_role_map_seq
     INCREMENT 1
	 MINVALUE 1
	 MAXVALUE 9999999999
	 START 1
	 CACHE 1;
	 
DROP TABLE IF EXISTS cafeteria_management_system.user_role_map;

CREATE TABLE cafeteria_management_system.user_role_map(
     user_role_map_id int8 NOT NULL default 
	       NEXTVAL('cafeteria_management_system.user_role_map_seq'),
	user_id int8,
	role_id int8,
	CONSTRAINT user_role_map_fk1 FOREIGN KEY(user_id)
	REFERENCES cafeteria_management_system.user(user_id)
	MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT user_role_map_fk2 FOREIGN KEY(role_id)
	REFERENCES cafeteria_management_system.role(role_id)
	MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT user_role_map_pk PRIMARY KEY(user_role_map_id)
);

SELECT SETVAL('cafeteria_management_system.user_role_map_seq',(
  SELECT MAX(user_role_map_id)+1 FROM cafeteria_management_system.user_role_map),
			    FALSE);
				
				
--SELECT * FROM cafeteria_management_system.user_role_map;

INSERT INTO cafeteria_management_system.role VALUES
       (1,'admin','Admin of the Cafeteria'),
       (2,'staff','staff of the Cafeteria'),
       (3,'customer','customer of the Cafeteria');
       

