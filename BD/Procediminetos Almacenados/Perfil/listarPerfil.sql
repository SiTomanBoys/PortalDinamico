USE `portal`;
DROP procedure IF EXISTS `listarPerfil`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`portal`@`%` PROCEDURE `listarPerfil` (in idPerfil int, in nombre varchar(50), in dsc varchar(100),in pagina int, in numReg int,out totReg int)
BEGIN
	DECLARE posicion INT DEFAULT 0;
    SELECT 
	count(*) into totReg
	FROM
    perfil
    WHERE
    (idPerfil is null or idPerfil = id_perfil) AND
    (nombre is null or perfil LIKE concat('%',concat(nombre,'%'))) AND
	(dsc is null or descripcion LIKE concat('%',concat(dsc,'%')))
	;

	if pagina is null then
		SELECT
    	1 INTO pagina
    	FROM DUAL;
	end if;
    
    if numReg is null then
		SELECT
    	100 INTO numReg
    	FROM DUAL;
	end if;

	SELECT
    numReg*(pagina-1) INTO posicion
    FROM DUAL;
	
	SELECT 
	*
	FROM
    perfil
    WHERE
    (idPerfil is null or idPerfil = id_perfil) AND
    (nombre is null or perfil LIKE concat('%',concat(nombre,'%'))) AND
	(dsc is null or descripcion LIKE concat('%',concat(dsc,'%')))
	LIMIT numReg OFFSET posicion
	;	

END$$

DELIMITER ;

