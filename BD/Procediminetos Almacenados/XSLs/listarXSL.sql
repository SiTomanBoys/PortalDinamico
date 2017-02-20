USE `portal`;
DROP procedure IF EXISTS `listarXSL`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`portal`@`%` PROCEDURE `listarXSL`(in idXSL int, in p_url varchar(100), in idIdioma int, in verCont int)
BEGIN
	IF verCont is null THEN
		select 
		x.id_xsl,
		x.nombre_ejb,
		u.id_url,
		x.contenido as contenido,
		u.url,
		i.descripcion as idioma
		from xsl x inner join urls u 
		on (u.id_url = x.id_url) inner join idiomas i
		on (x.id_idioma = i.id_idioma)
		where ( idXSL is null or idXSL = x.id_xsl )
		and ( p_url is null or u.url = p_url )
		and ( idIdioma is null  or i.id_idioma = idIdioma);
	ELSE
    
		select 
		x.id_xsl,
		x.nombre_ejb,
		u.id_url,
		u.url,
		i.descripcion as idioma
		from xsl x inner join urls u 
		on (u.id_url = x.id_url) inner join idiomas i
		on (x.id_idioma = i.id_idioma)
		where ( idXSL is null or idXSL = x.id_xsl )
		and ( p_url is null or u.url = p_url )
		and ( idIdioma is null  or i.id_idioma = idIdioma);
    END IF;
END$$

DELIMITER ;

