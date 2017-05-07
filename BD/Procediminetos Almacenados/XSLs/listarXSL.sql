USE `portal`;
DROP procedure IF EXISTS `listarXSL`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`portal`@`%` PROCEDURE `listarXSL`(in idXSL int, in p_url varchar(100), in idIdioma int, in verCont int, in pagina int, in numReg int,out totReg int)
BEGIN
	
	select 
	count(*) into totReg
	from xsl x inner join urls u 
	on (u.id_url = x.id_url) inner join idiomas i
	on (x.id_idioma = i.id_idioma)
	where ( idXSL is null or idXSL = x.id_xsl )
	and ( p_url is null or u.url = p_url )
	and ( idIdioma is null  or i.id_idioma = idIdioma);
	
	IF verCont is null THEN
		select 
		tmp.id_xsl,
		tmp.nombre_ejb,
		tmp.id_url,
		tmp.contenido,
		tmp.url,
		tmp.idioma
		from
		(
			select 
			x.id_xsl,
			x.nombre_ejb,
			u.id_url,
			x.contenido as contenido,
			u.url,
			i.descripcion as idioma,
			@rownum := @rownum + 1 as rownum
			from xsl x inner join urls u 
			on (u.id_url = x.id_url) inner join idiomas i
			on (x.id_idioma = i.id_idioma)
			where ( idXSL is null or idXSL = x.id_xsl )
			and ( p_url is null or u.url = p_url )
			and ( idIdioma is null  or i.id_idioma = idIdioma)
		) tmp
		where 
		(
			pagina is null or
			(
				tmp.rownum > ((pagina-1) * numReg) and
				tmp.rownum < ((pagina-1) * numReg) + (numReg + 1)
			)
		);
	ELSE
		select 
		tmp.id_xsl,
		tmp.nombre_ejb,
		tmp.id_url,
		tmp.url,
		tmp.idioma
		from
		(
			select 
			x.id_xsl,
			x.nombre_ejb,
			u.id_url,
			u.url,
			i.descripcion as idioma,
			@rownum := @rownum + 1 as rownum
			from 
			(SELECT @rownum := 0) r,
			xsl x inner join urls u 
			on (u.id_url = x.id_url) inner join idiomas i
			on (x.id_idioma = i.id_idioma)
			where ( idXSL is null or idXSL = x.id_xsl )
			and ( p_url is null or u.url = p_url )
			and ( idIdioma is null  or i.id_idioma = idIdioma)
		) tmp
		where 
		(
			pagina is null or
			(
				tmp.rownum > ((pagina-1) * numReg) and
				tmp.rownum < ((pagina-1) * numReg) + (numReg + 1)
			)
		);
    END IF;
END$$

DELIMITER ;

