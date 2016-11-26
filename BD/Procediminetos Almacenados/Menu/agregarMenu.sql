USE `portal`;
DROP procedure IF EXISTS `agregarMenu`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`portal`@`%` PROCEDURE `agregarMenu`(in nom varchar(50), in idPadre int)
BEGIN
	DECLARE posicion INT DEFAULT 0;
    DECLARE nvl INT DEFAULT 0;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		ROLLBACK;
		SELECT '1' as estado from dual;

	END; 
	
	START TRANSACTION;
    
	IF idPadre = 0 THEN 
		SELECT max(orden) INTO posicion 
		FROM
		menu;
		SET posicion = posicion + 1;
        INSERT INTO menu(id_url,nivel,nombre,orden,id_padre) VALUES (0,0,nom,posicion,idPadre);
	ELSE
		SELECT nivel INTO nvl 
		FROM
		menu
        WHERE 
        id_menu = idPadre;
        
        SELECT max(orden) INTO posicion 
        FROM
        menu
        WHERE
        id_padre = idPadre;
        
        IF posicion is null THEN
			SELECT orden INTO posicion
            FROM
            menu
            WHERE
            id_menu = idPadre;
		END IF;
        
		SET posicion = posicion + 1;
        SET nvl = nvl + 1;
        UPDATE menu SET orden = orden + 1 WHERE orden >= posicion;
        INSERT INTO menu(id_url,nivel,nombre,orden,id_padre) VALUES (0,nvl,nom,posicion,idPadre);
	END IF;
    
    COMMIT;
    SELECT '0' as estado from dual;
END$$

DELIMITER ;

