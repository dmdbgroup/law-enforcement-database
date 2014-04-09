drop view if exists alltypes;
drop view if exists alllinks;
drop view if exists allcasenotes;
drop view if exists alladdresses;
drop view if exists allpoi;
drop view if exists oldestcases;
drop view if exists newestcases;
drop view if exists allcases;

create view allcases (id, title, description, time, address_id, open, creator) 
	as select id, title, description, time, address_id, open, creator from `case`;

create view newestcases ( id, title, description, time, address_id, open, creator) 
	as select * from allcases where open = TRUE
	order by time desc
	limit 1;

create view oldestcases ( id, title, description, time, address_id, open, creator) 
	as select * from allcases where open = TRUE
	order by time
	limit 1;

create view allpoi ( id, name, birthdate )
	as select id, name, birthdate from poi;

create view alladdresses ( id, country, zipcode, city, street, streetno )
	as select id, country, zipcode, city, street, streetno from address;

create view allcasenotes ( id, case_id, text )
	as select id, case_id, text from case_note;

create view alllinks ( case_id, poi_id, type_id, time, end_time )
	as select case_id, poi_id, type_id, time, end_time from is_linked_to;

create view alltypes ( id, name )
	as select id, name from type;