drop procedure if exists toggle_case_open;
drop procedure if exists get_link;
drop procedure if exists get_type;
drop procedure if exists get_links_for_case;
drop procedure if exists get_notes_for_case;
drop procedure if exists get_case;
drop procedure if exists get_address;
drop procedure if exists get_poi;
drop procedure if exists search_cases_by_type_of_conviction;
drop procedure if exists search_cases_by_date_of_conviction;
drop procedure if exists search_poi_by_name;
drop procedure if exists search_cases_by_status;

create procedure search_cases_by_status ( in is_open boolean ) select * from allcases where open = is_open;
create procedure search_poi_by_name ( in contains nvarchar(255) ) select * from allpoi where name like concat('%', contains, '%');
create procedure search_cases_by_date_of_conviction ( in date date ) select * from allcases where date(time) = date;
create procedure search_cases_by_type_of_conviction ( in type_name nvarchar(255) ) 
	select distinct c.id, c.title, c.description, c.location, c.time, c.address_id, c.open 
	from (`case` c inner join is_linked_to l on l.case_id = c.id) inner join type t on l.type_id = t.id
	where t.name = type_name;
create procedure get_poi( in poi_id int ) select * from allpoi where id = poi_id;
create procedure get_address( in address_id int ) select * from alladdresses where id = address_id;
create procedure get_case( in case_id int ) select * from allcases where id = case_id;
create procedure get_notes_for_case( in case_id int ) select * from allcasenotes where allcasenotes.case_id = case_id;
create procedure get_links_for_case( in case_id int ) select * from alllinks where alllinks.case_id = case_id;
create procedure get_type( in type_id int ) select * from alltypes where alltypes.id = type_id;
create procedure get_link( in case_id int, in poi_id int, in type_id int ) 
	select * from alllinks 
	where alllinks.case_id = case_id and alllinks.poi_id = poi_id and alllinks.type_id = type_id;
create procedure toggle_case_open( in case_id int ) update `case` set open= !open where id = case_id;