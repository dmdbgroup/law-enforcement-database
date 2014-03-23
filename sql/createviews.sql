drop view if exists allpoi;
drop view if exists oldestcases;
drop view if exists newestcases;
drop view if exists allcases;

create view allcases (title, description, location, time, open) 
	as select title, description, location, time, open from `case`;

create view newestcases ( title, description, location, time, open) 
	as select * from allcases where open = TRUE
	order by time desc
	limit 1;

create view oldestcases ( title, description, location, time, open) 
	as select * from allcases where open = TRUE
	order by time
	limit 1;

create view allpoi ( name, birthdate )
	as select name, birthdate from poi;