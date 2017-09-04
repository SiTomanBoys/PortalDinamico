USE `portal`;
DROP procedure IF EXISTS `eliminarUrl`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`portal`@`%` PROCEDURE `eliminarUrl` (in idUrl int)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		ROLLBACK;
		SELECT '1' as estado from dual;

	END; 
    START TRANSACTION;
    
    DELETE FROM urls WHERE id_url = idUrl;
    
    COMMIT;
    SELECT '0' as estado from dual;
END$$

DELIMITER ;

