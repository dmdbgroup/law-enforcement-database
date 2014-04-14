drop procedure if exists search_cases_by_similar_description;
drop procedure if exists search_cases_by_similar_title;
drop procedure if exists search_cases_by_similar_type_of_conviction;
drop procedure if exists add_poi;
drop procedure if exists delete_poi;
drop procedure if exists get_notes_for_poi;
drop procedure if exists delete_case;
drop procedure if exists add_case_note;
drop procedure if exists add_poi_note;
drop procedure if exists add_case;
drop procedure if exists toggle_case_open;
drop procedure if exists get_link;
drop procedure if exists get_type;
drop procedure if exists get_links_for_poi;
drop procedure if exists get_links_for_case;
drop procedure if exists get_notes_for_case;
drop procedure if exists get_case;
drop procedure if exists get_poi;
drop procedure if exists get_id_from_type;
drop procedure if exists get_all_cases;
drop procedure if exists get_all_poi;
drop procedure if exists get_all_unsuspected_pois;
drop procedure if exists get_most_recent_cases;
drop procedure if exists get_oldest_unsolved_cases;
drop procedure if exists get_all_categories;
drop procedure if exists search_cases_by_type_of_conviction;
drop procedure if exists search_cases_by_date_of_conviction;
drop procedure if exists search_poi_by_name;
drop procedure if exists search_cases_by_status;
drop procedure if exists user_exists;
drop procedure if exists username_exists;


create procedure add_poi_note ( in poi_id int, in text nvarchar(2000) ) insert into poi_note ( poi_id, text ) values ( poi_id, text );
create procedure search_cases_by_status ( in is_open boolean ) select * from allcases where open = is_open;
create procedure search_poi_by_name ( in contains nvarchar(255) ) select * from allpoi where name like concat('%', contains, '%');
create procedure search_cases_by_date_of_conviction ( in date date ) select * from allcases where date(time) = date;
create procedure search_cases_by_type_of_conviction ( in type_name int ) 
	select distinct c.id, c.title, c.description, c.time, c.address, c.open, c.creator
	from (`case` c inner join is_linked_to l on l.case_id = c.id) inner join type t on l.type_id = t.id
	where t.id = type_name;
create procedure get_poi( in poi_id int ) select * from allpoi where id = poi_id;
create procedure get_case( in case_id int ) select * from allcases where id = case_id;
create procedure get_id_from_type(in name nvarchar(255)) select id from type t where t.name = name;
create procedure get_all_cases () select * from allcases;
create procedure get_all_poi () select * from allpoi;
create procedure get_most_recent_cases() select * from newestcases;
create procedure get_oldest_unsolved_cases() select * from oldestcases;
create procedure get_all_categories() select * from alltypes;
create procedure get_all_unsuspected_pois(in case_id int) select * from allunsuspectedpois;
create procedure get_notes_for_case( in case_id int ) select * from allcasenotes where allcasenotes.case_id = case_id;
create procedure get_links_for_case( in case_id int ) select * from alllinks where alllinks.case_id = case_id;
create procedure get_links_for_poi( in poi_id int ) select * from alllinks where alllinks.poi_id = poi_id;
create procedure get_type( in type_id int ) select * from alltypes where alltypes.id = type_id;
create procedure get_link( in case_id int, in poi_id int, in type_id int ) 
	select * from alllinks 
	where alllinks.case_id = case_id and alllinks.poi_id = poi_id and alllinks.type_id = type_id;
create procedure toggle_case_open( in case_id int ) update `case` set open= !open where id = case_id;
create procedure add_case( in title nvarchar(255), in description nvarchar(2000), in time Time, in address nvarchar(255), in creator nvarchar(255), in open boolean )
	insert into `case` ( title, description, time, address, creator, open ) values ( title, description, time, address, creator, open );
create procedure add_case_note( in case_id int, in text nvarchar(2000) )
	insert into case_note ( case_id, text ) values ( case_id, text );
create procedure delete_case( in case_id int ) delete from `case` where id = case_id;
create procedure get_notes_for_poi( in poi_id int ) select * from poi_note where poi_id = poi_id;
create procedure delete_poi( in poi_id int ) delete from poi where id = poi_id;
create procedure add_poi( in firstname nvarchar(255), in surname nvarchar(255), in birthdate Date )
	insert into poi ( firstname, surname, birthdate ) values ( firstname, surname, birthdate );
create procedure search_cases_by_similar_type_of_conviction ( in contains nvarchar(255) ) 
	select distinct c.id, c.title, c.description, c.time, c.address, c.open, c.creator
	from (`case` c inner join is_linked_to l on l.case_id = c.id) inner join type t on l.type_id = t.id
	where t.name like concat('%', contains, '%');
create procedure user_exists( in user_name nvarchar(255), in pass_word nvarchar(255)) 
	select * from user where name = user_name and password = pass_word;
create procedure username_exists(in user_name nvarchar(255))
	select * from user where name = user_name;
create procedure search_cases_by_similar_title( in contains nvarchar(255) ) select * from allcases where title like concat('%', contains, '%');
create procedure search_cases_by_similar_description( in contains nvarchar(255) ) select * from allcases where description like concat('%', contains, '%');