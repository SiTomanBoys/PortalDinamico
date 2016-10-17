USE `portal`;
DROP procedure IF EXISTS `listarMenu`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`root`@`%` PROCEDURE `listarMenu`(in idMenu int, in nom varchar(50),in nvl int)
BEGIN

	select 
	m.id_menu,
	m.nombre,
	m.id_url,
	m.nivel,
	u.url
	from menu m left join urls u 
	on (m.id_url = u.id_url)
	where
	(idMenu is null or idMenu = m.id_menu) and
	(nom is null or m.nombre like concat(nom,'%')) and
	(nvl is null or nvl = m.nivel)
	order by m.orden;	

END$$

DELIMITER ;

