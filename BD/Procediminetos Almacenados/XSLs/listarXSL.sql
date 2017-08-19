USE `portal`;
DROP procedure IF EXISTS `listarXSL`;

DELIMITER $$
USE `portal`$$
CREATE DEFINER=`portal`@`%` PROCEDURE `listarXSL`(in idXSL int, in p_url varchar(100), in idIdioma int, in verCont int, in pagina int, in numReg int,out totReg int)
BEGIN
	DECLARE posicion INT DEFAULT 0;
	select 
	count(*) into totReg
	from xsl x inner join urls u 
	on (u.id_url = x.id_url) inner join idiomas i
	on (x.id_idioma = i.id_idioma)
	where ( idXSL is null or idXSL = x.id_xsl )
	and ( p_url is null or u.url = p_url )
	and ( idIdioma is null  or i.id_idioma = idIdioma);

	if pagina is null then
		SELECT
    	1 INTO pagina
    	FROM DUAL;
	end if;
    
    if numReg is null then
		SELECT
    	100 INTO numReg
    	FROM DUAL;
	end if;
	
    SELECT
	(numReg*(pagina-1)) INTO posicion
	FROM DUAL;
	
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
		and ( idIdioma is null  or i.id_idioma = idIdioma)
		LIMIT numReg OFFSET posicion;
	ELSE
		
		select 
		x.id_xsl,
		x.nombre_ejb,
		u.id_url,
		u.url,
		i.descripcion as idioma
		from 
		xsl x inner join urls u 
		on (u.id_url = x.id_url) inner join idiomas i
		on (x.id_idioma = i.id_idioma)
		where ( idXSL is null or idXSL = x.id_xsl )
		and ( p_url is null or u.url = p_url )
		and ( idIdioma is null  or i.id_idioma = idIdioma)
		LIMIT numReg OFFSET posicion;
    END IF;
END$$

DELIMITER ;

