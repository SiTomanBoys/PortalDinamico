USE `portal`;
DROP procedure IF EXISTS `modificarMenu`;

DELIMITER $$
USE `portal`$$
CREATE PROCEDURE `modificarMenu` (in idMenu int, in nom varchar(50), in idPadre int, in idUrl int)
BEGIN
	DECLARE posicion INT DEFAULT 0;
    DECLARE posicionActual INT DEFAULT 0;
    DECLARE nvl INT DEFAULT 0;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		ROLLBACK;
		SELECT '1' as estado from dual;
	END;
    
    START TRANSACTION;
    
    IF idPadre = 0 THEN
		SELECT orden INTO posicion 
		FROM
		menu 
        WHERE 
        id_menu = idMenu;

		UPDATE
        menu
        SET
        orden = orden - 1
        WHERE
        orden > posicion;
        
        SELECT max(orden) INTO posicion 
		FROM
		menu;
        SET posicion = posicion + 1;
        
		UPDATE
		menu
		SET
		nombre = nom,
		id_padre = idPadre,
        orden = posicion,
        nivel = 0
		WHERE
		id_menu = idMenu;
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
        
        SELECT orden INTO posicionActual 
		FROM
		menu 
        WHERE 
        id_menu = idMenu;
        
        IF posicionActual > posicion THEN
			UPDATE menu SET orden = orden + 1 
            WHERE orden > posicion AND
            orden < posicionActual;
            SET posicion = posicion + 1;
		ELSE
			UPDATE menu SET orden = orden - 1 
            WHERE orden <= posicion AND
            orden > posicionActual;
        END IF;
        
        
        SET nvl = nvl + 1;
        
		UPDATE
		menu
		SET
		nombre = nom,
		id_padre = idPadre,
        orden = posicion,
        nivel = nvl,
        id_url = idUrl
		WHERE
		id_menu = idMenu;
        
    END IF;
    
    COMMIT;
    SELECT '0' as estado from dual;
END$$

DELIMITER ;

