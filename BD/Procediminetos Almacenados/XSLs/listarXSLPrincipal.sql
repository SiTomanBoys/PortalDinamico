USE `portal`;
DROP procedure IF EXISTS `listarXSLPrincipal`;

DELIMITER $$
USE `portal`$$
CREATE PROCEDURE `listarXSLPrincipal` (in idXSL int, in nom varchar(50), in idIdioma int)
BEGIN
	select 
	x.id_xsl,
	x.nombre,
	x.contenido,
	i.descripcion as idioma
	from xsls_principales x inner join idiomas i
	on (x.id_idioma = i.id_idioma)
	where ( idXSL is null or idXSL = x.id_xsl )
	and ( nom is null  or x.nombre = nom)
	and ( idIdioma is null  or i.id_idioma = idIdioma);
END$$

DELIMITER ;

