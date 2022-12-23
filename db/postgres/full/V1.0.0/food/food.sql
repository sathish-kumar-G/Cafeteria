-- Food Menu Table
-- create sequence for food menu
drop sequence if exists cafeteria_management_system.food_menu_seq;

create sequence cafeteria_management_system.food_menu_seq
    increment 1
    minvalue 1
    maxvalue 9999999999999
    start 1
    cache 1;
   
   
-- create table food menu
drop table if exists cafeteria_management_system.food_menu;

create table cafeteria_management_system.food_menu(
     food_menu_id int8 not null default NEXTVAL('cafeteria_management_system.food_menu_seq'),
     food_menu_name varchar(250),
     food_menu_type varchar(250),
     food_menu_available_date varchar(400),
     constraint food_menu_pk primary key(food_menu_id)
);

-- update the sequence number after all sample data inserted
select setval('cafeteria_management_system.food_menu_seq',
             (select max(food_menu_id)+1 from cafeteria_management_system.food_menu),false); 
    
-- select * from cafeteria_management_system.food_menu;
            
-- Food Item Table
-- create sequence for food item
drop sequence if exists cafeteria_management_system.food_item_seq;

create sequence cafeteria_management_system.food_item_seq
           increment 1
           minvalue 1
           maxvalue 99999999999999
           start 1
           cache 1;
          
-- Food Item Table
-- create sequence for food item
drop sequence if exists cafeteria_management_system.food_item_seq;

create sequence cafeteria_management_system.food_item_seq
           increment 1
           minvalue 1
           maxvalue 99999999999999
           start 1
           cache 1;
          
-- create table food item
drop table if exists cafeteria_management_system.food_item;

create table cafeteria_management_system.food_item(
     food_item_id int8 not null default NEXTVAL('cafeteria_management_system.food_item_seq'),
     food_name varchar(250),
     food_price int8,
     constraint food_item_pk primary key(food_item_id)
);
           
-- update the sequence after the all sample data inserted
select SETVAl('cafeteria_management_system.food_item_seq',
                 (select max(food_item_id)+1 from cafeteria_management_system.food_item),false);
                 
-- select * from cafeteria_management_system.food_item;
                
                
-- Food Menu Item Map Table
-- create sequence for food menu item map
drop sequence if exists cafeteria_management_system.food_menu_item_map_seq;

create sequence cafeteria_management_system.food_menu_item_map_seq
             increment 1
             minvalue 1
             maxvalue 9999999999999
             start 1
             cache 1;
            
-- Food Menu Item Map Table
-- create sequence for food menu item map
drop sequence if exists cafeteria_management_system.food_menu_item_map_seq;

create sequence cafeteria_management_system.food_menu_item_map_seq
             increment 1
             minvalue 1
             maxvalue 9999999999999
             start 1
             cache 1;
            
-- create table food menu item map
drop table if exists cafeteria_management_system.food_menu_item_map;

create table cafeteria_management_system.food_menu_item_map(
          food_menu_item_map_id int8 not null default NEXTVAL('cafeteria_management_system.food_menu_item_map_seq'),
          food_menu_id int8,
          food_item_id int8,
          food_count int8,
          constraint food_menu_id_fk foreign key(food_menu_id)
          references cafeteria_management_system.food_menu(food_menu_id)
          match simple on update no action on delete no action,
          constraint food_item_id_fk foreign key(food_item_id)
          references cafeteria_management_system.food_item(food_item_id)
          match simple on update no action on delete no action,
          constraint food_menu_item_map_pk primary key(food_menu_item_map_id)
          );
         
-- update the sequence after all the sample data inserted
select SETVAL('cafeteria_management_system.food_menu_item_map_seq',
(select max(food_menu_item_map_id)+1 from cafeteria_management_system.food_menu_item_map),false);
                
-- select * from cafeteria_management_system.food_menu_item_map;

