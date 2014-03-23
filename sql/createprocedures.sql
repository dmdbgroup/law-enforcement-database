drop procedure if exists search_cases_by_type_of_conviction;
drop procedure if exists search_cases_by_date_of_conviction;
drop procedure if exists search_poi_by_name;
drop procedure if exists search_cases_by_status;

create procedure search_cases_by_status ( in is_open boolean ) select * from allcases where open = is_open;
create procedure search_poi_by_name ( in contains nvarchar(255) ) select * from allpoi where name like concat('%', contains, '%');
create procedure search_cases_by_date_of_conviction ( in date date ) select * from allcases where date(time) = date;
create procedure search_cases_by_type_of_conviction ( in type_name nvarchar(255) ) 
	select distinct c.title, c.description, c.location, c.time, c.open 
	from (`case` c inner join is_linked_to l on l.case_id = c.id) inner join type t on l.type_id = t.id
	where t.name = type_name;