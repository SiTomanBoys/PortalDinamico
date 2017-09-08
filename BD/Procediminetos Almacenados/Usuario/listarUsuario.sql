USE `portal`;
DROP procedure IF EXISTS `listarUsuario`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`portal`@`%` PROCEDURE `listarUsuario` (in idUsuario int,in nombre varchar(50),in idPerfil int,in pagina int, in numReg int,out totReg int)
BEGIN
	DECLARE posicion INT DEFAULT 0;
    SELECT 
	count(*) into totReg
	FROM
    usuario u, perfil p
    WHERE
    p.id_perfil = u.id_perfil and
	(idUsuario is null or idUsuario = id_usuario) AND
    (idPerfil is null or idPerfil = p.id_perfil) AND
    (nombre is null or nombre_usuario LIKE concat('%',concat(nombre,'%')))
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
	id_usuario,
	p.id_perfil,
	nombre_usuario,
	perfil
	FROM
	usuario u, perfil p
    WHERE
    p.id_perfil = u.id_perfil and
	(idUsuario is null or idUsuario = id_usuario) AND
    (idPerfil is null or idPerfil = p.id_perfil) AND
    (nombre is null or nombre_usuario LIKE concat('%',concat(nombre,'%')))
	;
	
END$$

DELIMITER ;

