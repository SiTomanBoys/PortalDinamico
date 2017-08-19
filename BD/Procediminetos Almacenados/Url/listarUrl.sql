USE `portal`;
DROP procedure IF EXISTS `listarUrl`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`portal`@`%` PROCEDURE `listarUrl`(IN `idUrl` INT, IN `nombre` VARCHAR(50),in pagina int, in numReg int,out totReg int)
BEGIN
	
	DECLARE posicion INT DEFAULT 0;
    SELECT 
	count(*) into totReg
	FROM
    urls
    WHERE
    (idUrl is null or idUrl = id_url) AND
    (nombre is null or url LIKE concat('%',concat(nombre,'%')));
	
    SELECT
    numReg*(pagina-1) INTO posicion
    FROM DUAL;
    
    SELECT
    *
    FROM
    urls
    WHERE
    (idUrl is null or idUrl = id_url) AND
    (nombre is null or url LIKE concat('%',concat(nombre,'%')))
    LIMIT numReg OFFSET posicion
    ;
END$$

DELIMITER ;

