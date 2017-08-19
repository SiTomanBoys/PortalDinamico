USE `portal`;
DROP procedure IF EXISTS `agregarUrl`;

DELIMITER $$
USE `portal`$$
CREATE PROCEDURE `agregarUrl` (in nom varchar(100))
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		ROLLBACK;
		SELECT '1' as estado from dual;

	END; 
    START TRANSACTION;
    
    INSERT INTO urls(url) VALUES(nom);
    
    COMMIT;
    SELECT '0' as estado from dual;
END$$

DELIMITER ;