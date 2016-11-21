USE `portal`;
DROP procedure IF EXISTS `modificarXSL`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`portal`@`%` PROCEDURE `modificarXSL`(in idXSL int, in contenido_xsl text, in nombreEjb varchar(100), in idIdioma int)
BEGIN

	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
    
		ROLLBACK;
		SELECT '1' as estado from dual;
	
	END; 

	START TRANSACTION;
	UPDATE 
    xsl 
    SET 
    contenido = unhex(contenido_xsl), 
	nombre_ejb = nombreEjb
    where
    id_xsl = idXSL;
    
    SELECT '0' as estado from dual;
    
    COMMIT;
    
END$$

DELIMITER ;

