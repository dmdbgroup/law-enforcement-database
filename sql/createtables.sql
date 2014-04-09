drop table if exists user;
drop table if exists case_note;
drop table if exists poi_note;
drop table if exists is_linked_to;
drop table if exists `case`;
drop table if exists address;
drop table if exists poi;
drop table if exists type;


create table user ( name nvarchar(255) not null primary key, 
	password nvarchar(255) not null);

create table address ( id int auto_increment primary key,
	country nvarchar(255) not null,
	zipcode int not null,
	city nvarchar(255) not null,
	street nvarchar(255) not null,
	streetno int not null);

create table `case` ( id int auto_increment primary key, 
	address_id int,
	open boolean not null, 
	title nvarchar(255) not null, 
	description nvarchar(2000) not null, 
	time datetime not null,
	creator nvarchar(255) not null,
	foreign key(address_id) references address(id) on delete cascade,
	foreign key(creator) references `user`(name) on delete cascade);

create table poi ( id int auto_increment primary key, 
	name nvarchar(255) not null, 
	birthdate date not null);

create table type ( id int auto_increment primary key, 
	name nvarchar(255) not null);

create table is_linked_to ( case_id int, 
	poi_id int, 
	type_id int, 
	time datetime not null, 
	end_time datetime, 
	foreign key (case_id) references `case`(id) on delete cascade, 
	foreign key (poi_id) references poi(id) on delete cascade, 
	foreign key (type_id) references type(id) on delete cascade);

create table case_note ( id int auto_increment primary key, 
	case_id int, 
	text nvarchar(2000) not null, 
	foreign key (case_id) references `case`(id) on delete cascade);

create table poi_note ( id int auto_increment primary key, 
	poi_id int, 
	text nvarchar(2000) not null, 
	foreign key (poi_id) references poi(id) on delete cascade);