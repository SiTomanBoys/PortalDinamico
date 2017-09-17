USE `portal`;
DROP procedure IF EXISTS `agregarPerfil`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`portal`@`%` PROCEDURE `agregarPerfil`(in nom varchar(50),in dsc varchar(100))
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		ROLLBACK;
		SELECT '1' as estado from dual;

	END; 
    START TRANSACTION;
    
    INSERT INTO perfil(perfil,descripcion) VALUES(nom,dsc);
    COMMIT;
    SELECT '0' as estado from dual;
END$$

DELIMITER ;
