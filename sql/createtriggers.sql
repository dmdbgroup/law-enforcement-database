-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DROP TRIGGER IF EXISTS delete_type;

DELIMITER $$

CREATE TRIGGER delete_type AFTER DELETE ON is_linked_to
FOR EACH ROW 
BEGIN
	declare idd INT;
	IF ((SELECT COUNT(il.type_id) FROM is_linked_to AS il WHERE il.type_id = OLD.type_id) = 0) THEN
		SET idd = old.type_id;
	ELSE
		SET idd = null;
	END IF;
	DELETE FROM type WHERE id = old.type_id;
END