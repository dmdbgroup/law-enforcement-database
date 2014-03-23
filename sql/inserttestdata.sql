SET SQL_SAFE_UPDATES=0;
delete from user;
delete from case_note;
delete from poi_note;
delete from  is_linked_to;
delete from  poi;
delete from type;
delete from `case`;

insert into user values ("cyrill", "12345");
insert into user values ("andreas", "asdf");

insert into `case` (open, title, description, location, time) values ( FALSE, "case 1", "description 1", "location 1", "2011-12-18 13:17:17");
insert into `case` (open, title, description, location, time) values ( TRUE, "case 2", "description 2", "location 2", "2013-12-18 13:17:17");

insert into poi (name, birthdate) values ( "alex", "2013-12-19");
insert into poi (name, birthdate) values ( "jimmy", "2013-12-20");

insert into type (name) values ( "murder");
insert into type (name) values ( "robbery");

insert into is_linked_to (case_id, poi_id, type_id, time, end_time)
	select c.id, p.id, t.id, "2011-12-18 13:17:17", "2011-12-21 13:17:17"
	from `case` c, poi p, `type` t
	where c.title = "case 1" and p.name = "alex" and t.name = "murder";
insert into is_linked_to (case_id, poi_id, type_id, time) 
	select c.id, p.id, t.id, "2013-12-18 13:17:17"
	from `case` c, poi p, `type` t
	where c.title = "case 2" and p.name = "alex" and t.name = "robbery";

insert into case_note (case_id, text)
	select c.id, "weapon used: knife"
	from `case` c
	where c.title = "case 2";

insert into poi_note (poi_id, text)
	select p.id, "wanted: bounty of 500'000"
	from poi p
	where p.name = "alex";