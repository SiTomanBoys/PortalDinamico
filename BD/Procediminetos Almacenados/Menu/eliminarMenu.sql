USE `portal`;
DROP procedure IF EXISTS `eliminarMenu`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`portal`@`%` PROCEDURE `eliminarMenu`(in idMenu int)
BEGIN
	DECLARE posicion INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		ROLLBACK;
		SELECT '1' as estado from dual;
	END;
    
    START TRANSACTION;
    
    SELECT 
    orden INTO posicion
    FROM
    menu
    WHERE
    id_menu = idMenu;
    
    DELETE FROM menu WHERE id_menu = idMenu;
    
    UPDATE menu SET 
    orden = orden - 1
	WHERE 
    orden > posicion;
    
    COMMIT;
    SELECT '0' as estado from dual;
END$$

DELIMITER ;

