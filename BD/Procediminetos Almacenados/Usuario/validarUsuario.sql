USE `portal`;
DROP procedure IF EXISTS `validarUsuario`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`root`@`%` PROCEDURE `validarUsuario`(in user varchar(50), in pass varchar(100))
BEGIN
DECLARE existe BIGINT DEFAULT 0;

	select count(*) into existe 
    from usuario
	where 
    nombre_usuario = user and
    pass_usuario = pass;
    
    IF existe > 0 THEN
		select 
		'1' as estado,
        u.id_usuario,
        u.nombre_usuario,
        u.pass_usuario,
        p.id_perfil,
        p.perfil,
        p.descripcion
        from usuario u inner join perfil p
        on (u.id_perfil = p.id_perfil)
        where
		u.nombre_usuario = user and
		u.pass_usuario = pass;
	ELSE
		select 
		'0' as estado 
        from dual;
	END IF;
END$$

DELIMITER ;