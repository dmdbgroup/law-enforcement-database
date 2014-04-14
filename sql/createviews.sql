drop view if exists alltypes;
drop view if exists alllinks;
drop view if exists allcasenotes;
drop view if exists allpoi;
drop view if exists oldestcases;
drop view if exists newestcases;
drop view if exists allcases;
drop view if exists allunsuspectedpois;


create view allunsuspectedpois (id, firstname, surname, birthdate)
	as select p1.id, p1.firstname, p1.surname, p1.birthdate from poi p1
	where p1.id not in 
	(select p2.id from poi as p2 join is_linked_to as il on p2.id = il.poi_id 
	where il.case_id = case_id group by p2.id);

create view allcases (id, title, description, time, address, open, creator) 
	as select id, title, description, time, address, open, creator from `case`;

create view newestcases ( id, title, description, time, address, open, creator) 
	as select * from allcases where open = TRUE
	order by time desc
	limit 10;

create view oldestcases ( id, title, description, time, address, open, creator) 
	as select * from allcases where open = TRUE
	order by time
	limit 10;

create view allpoi ( id, firstname, surname, birthdate )
	as select id, firstname, surname, birthdate from poi;

create view allcasenotes ( id, case_id, text )
	as select id, case_id, text from case_note;

create view alllinks ( case_id, poi_id, type_id, time, end_time )
	as select case_id, poi_id, type_id, time, end_time from is_linked_to;

create view alltypes ( id, name )
	as select id, name from type;