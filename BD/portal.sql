-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 13-11-2016 a las 12:27:21
-- Versión del servidor: 5.5.50
-- Versión de PHP: 5.3.10-1ubuntu3.24

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `portal`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`portal`@`%` PROCEDURE `listarMenu`(in idMenu int, in nom varchar(50),in nvl int)
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

CREATE DEFINER=`portal`@`%` PROCEDURE `listarXSL`(in idXSL int, in p_url varchar(100), in idIdioma int, in verCont int)
BEGIN
	IF verCont is null THEN
		select 
		x.id_xsl,
		x.nombre_ejb,
		u.id_url,
		x.contenido,
		u.url,
		i.descripcion as idioma
		from xsl x inner join rel_urls_xsl rl 
		on (x.id_xsl = rl.id_xsl) inner join urls u 
		on (u.id_url = rl.id_url) inner join idiomas i
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
		from xsl x inner join rel_urls_xsl rl 
		on (x.id_xsl = rl.id_xsl) inner join urls u 
		on (u.id_url = rl.id_url) inner join idiomas i
		on (x.id_idioma = i.id_idioma)
		where ( idXSL is null or idXSL = x.id_xsl )
		and ( p_url is null or u.url = p_url )
		and ( idIdioma is null  or i.id_idioma = idIdioma);
    END IF;
END$$

CREATE DEFINER=`portal`@`%` PROCEDURE `listarXSLPrincipal`(in idXSL int, in nom varchar(50), in idIdioma int)
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

CREATE DEFINER=`portal`@`%` PROCEDURE `lstMenu`(IN idpadre INT, IN nvel INT, IN link VARCHAR(200))
BEGIN
	SELECT 	m.id_menu,m.nombre,m.id_padre,m.id_url,m.nivel,u.url,m.posicion
	FROM menu m LEFT JOIN urls u 
	ON (m.id_url=u.id_url)
	WHERE
	(idpadre IS NULL OR idpadre = m.id_padre) AND
	(nvel IS NULL OR nvel = m.nivel) AND
	(link IS NULL OR link = u.url)
	ORDER BY m.posicion;
END$$

CREATE DEFINER=`portal`@`%` PROCEDURE `validarUsuario`(in user varchar(50), in pass varchar(100))
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

CREATE DEFINER=`portal`@`%` PROCEDURE `ValidaUsuario`(in  P_Name varchar(50) ,
                             in  p_Password varchar(30),
                             out p_validacion   varchar(30)
                             )
BEGIN
  declare   NullUsser  int         default 0  ;
  declare   T_Name     varchar(30) default '' ;
  declare   T_Password varchar(30) default '' ;


  select count(NombreUsuario)  into NullUsser 
		 from    Usuarios 
		 where    NombreUsuario= P_Name  ;
  select NombreUsuario into T_Name
         from    Usuarios 
         where    NombreUsuario= P_Name ;

select   PassUsuario into T_Password
         from    Usuarios 
         where    NombreUsuario= P_Name ;


  IF NullUsser   = '0' THEN
    SET p_validacion   = 'No existe Usuario  ';
  ELSE
             IF T_Password = p_Password THEN
                   SET p_validacion   = 'ok ';
             ELSE
                   SET p_validacion = 'Contraseña incorrecta ';
             END IF;
  END IF;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `idiomas`
--

CREATE TABLE IF NOT EXISTS `idiomas` (
  `id_idioma` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) NOT NULL,
  PRIMARY KEY (`id_idioma`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `menu`
--

CREATE TABLE IF NOT EXISTS `menu` (
  `id_menu` int(11) NOT NULL AUTO_INCREMENT,
  `id_url` bigint(11) NOT NULL DEFAULT '0',
  `nivel` bigint(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `orden` int(11) NOT NULL,
  PRIMARY KEY (`id_menu`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `perfil`
--

CREATE TABLE IF NOT EXISTS `perfil` (
  `id_perfil` bigint(20) NOT NULL AUTO_INCREMENT,
  `perfil` varchar(50) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  PRIMARY KEY (`id_perfil`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rel_urls_xsl`
--

CREATE TABLE IF NOT EXISTS `rel_urls_xsl` (
  `id_url` int(11) NOT NULL,
  `id_xsl` int(11) NOT NULL,
  PRIMARY KEY (`id_url`,`id_xsl`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `urls`
--

CREATE TABLE IF NOT EXISTS `urls` (
  `id_url` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(100) NOT NULL,
  PRIMARY KEY (`id_url`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre_usuario` varchar(50) NOT NULL,
  `pass_usuario` varchar(100) NOT NULL,
  `id_perfil` bigint(20) NOT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `xsl`
--

CREATE TABLE IF NOT EXISTS `xsl` (
  `id_xsl` int(11) NOT NULL AUTO_INCREMENT,
  `contenido` text,
  `nombre_ejb` varchar(100) DEFAULT NULL,
  `id_idioma` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_xsl`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `xsls_principales`
--

CREATE TABLE IF NOT EXISTS `xsls_principales` (
  `id_xsl` int(11) NOT NULL AUTO_INCREMENT,
  `contenido` longtext NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `id_idioma` int(11) NOT NULL,
  PRIMARY KEY (`id_xsl`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
