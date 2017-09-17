USE `portal`;
DROP procedure IF EXISTS `modificarPerfil`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`portal`@`%` PROCEDURE `modificarPerfil`(in idPerfil int,in nom varchar(50),in dsc varchar(100))
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		ROLLBACK;
		SELECT '1' as estado from dual;

	END; 
    START TRANSACTION;
    UPDATE
	perfil
	SET
	perfil=nom,
	descripcion=dsc
	WHERE
	id_perfil = idPerfil;
    COMMIT;
    SELECT '0' as estado from dual;
END$$

DELIMITER ;

