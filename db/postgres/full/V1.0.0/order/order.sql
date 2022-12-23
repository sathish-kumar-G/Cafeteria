
-- Order Table
-- Create Sequence for Order table
drop sequence if exists cafeteria_management_system.order_seq;

create sequence cafeteria_management_system.order_seq
           increment 1
           minvalue 1
           maxvalue 999999999999
           start 1
           cache 1;


-- Create Table Order
drop table if exists cafeteria_management_system.order;

create table cafeteria_management_system.order(
      order_id int8 not null default NEXTVAL('cafeteria_management_system.order_seq'),
      order_status varchar(250),
      order_amount int8,
      user_id int8,
     constraint user_id_fkk foreign key(user_id)
      references cafeteria_management_system.user(user_id)
      match simple on update no action on delete no action,
      constraint order_pk primary key(order_id)
);

-- update the sequence after all the sample data inserted
select SETVAL('cafeteria_management_system.order_seq',
(select max(order_id)+1 from cafeteria_management_system.order),false);


-- Order Food Item Map Table
-- Create Sequence for Order Food Item Map 
drop sequence if exists cafeteria_management_system.order_food_item_map_seq;

create sequence cafeteria_management_system.order_food_item_map_seq
             increment 1
             minvalue 1
             maxvalue 99999999999
             start 1
             cache 1;
            
-- Create Table Order Food Item Map
drop table if exists cafeteria_management_system.order_food_item_map;

create table cafeteria_management_system.order_food_item_map(
      order_food_item_map_id int8 not null default NEXTVAL('cafeteria_management_system.order_food_item_map_seq'),
      order_id int8,
      food_item_id int8,
      quantity int8,
      constraint order_id_fk foreign key(order_id)
      references cafeteria_management_system.order(order_id)
      match simple on update no action on delete no action,
      constraint food_id_fk foreign key(food_item_id)
      references cafeteria_management_system.food_item(food_item_id)
      match simple on update no action on delete no action,
      constraint order_food_item_id_pk primary key(order_food_item_map_id)
);

-- update the sequence after all the sample data inserted
select SETVAL('cafeteria_management_system.order_food_item_map',
(select max(order_food_item_map_id)+1 from cafeteria_management_system.order_food_item_map),false);


-- Create OrderAddressMap Table
-- Create Sequence for Order Address Map
drop sequence if exists cafeteria_management_system.order_address_map_seq;

create sequence cafeteria_management_system.order_address_map_seq
                 increment 1
                 minvalue 1
                 maxvalue 999999999999
                 start 1
                 cache 1;
                
--Create Table Order Address map
drop table if exists cafeteria_management_system.order_address_map;

create table cafeteria_management_system.order_address_map(
     order_address_map_id int8 not null default NEXTVAL('cafeteria_management_system.order_address_map_seq'),
     order_id int8,
     order_date date,
     address_door_no varchar(100),
     address_street varchar(200),
     address_district varchar(200),
     address_state varchar(200),
     phone_number varchar(15),
     constraint order_id_fkk foreign key(order_id)
     references cafeteria_management_system.order(order_id)
     match simple on update no action on delete no action,
     constraint order_address_map_id_pk primary key(order_address_map_id)
);


-- update the sequence after all the sample data inserted
select SETVAL('cafeteria_management_system.order_address_map',
(select max(order_address_map_id)+1 from cafeteria_management_system.order_address_map),false); 
