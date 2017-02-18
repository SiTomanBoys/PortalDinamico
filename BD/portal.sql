-- phpMyAdmin SQL Dump
-- version 4.1.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 18, 2017 at 02:07 AM
-- Server version: 5.5.54-0ubuntu0.12.04.1
-- PHP Version: 5.3.10-1ubuntu3.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `zadmin_portal`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`portal`@`%` PROCEDURE `agregarMenu`(in nom varchar(50), in idPadre int)
BEGIN
	DECLARE posicion INT DEFAULT 0;
    DECLARE nvl INT DEFAULT 0;
	/*DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
		ROLLBACK;
		SELECT '1' as estado from dual;

	END; 
	*/
	START TRANSACTION;
    
	IF idPadre = 0 THEN 
		SELECT max(orden) INTO posicion 
		FROM
		menu;
		SET posicion = posicion + 1;
        INSERT INTO menu(id_url,nivel,nombre,orden,id_padre) VALUES (0,0,nom,posicion,idPadre);
	ELSE
		SELECT nivel INTO nvl 
		FROM
		menu
        WHERE 
        id_menu = idPadre;
        
        SELECT max(orden) INTO posicion 
        FROM
        menu
        WHERE
        id_padre = idPadre;
        
        IF posicion is null THEN
			SELECT orden INTO posicion
            FROM
            menu
            WHERE
            id_menu = idPadre;
		END IF;
        
		SET posicion = posicion + 1;
        SET nvl = nvl + 1;
        UPDATE menu SET orden = orden + 1 WHERE orden >= posicion;
        INSERT INTO menu(id_url,nivel,nombre,orden,id_padre) VALUES (0,nvl,nom,posicion,idPadre);
	END IF;
    
    COMMIT;
    SELECT '0' as estado from dual;
END$$

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
		x.contenido as contenido,
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
    contenido = contenido_xsl, 
	nombre_ejb = nombreEjb
    where
    id_xsl = idXSL;
    
    SELECT '0' as estado from dual;
    
    COMMIT;
    
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
-- Table structure for table `idiomas`
--

CREATE TABLE IF NOT EXISTS `idiomas` (
  `id_idioma` int(11) NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) NOT NULL,
  PRIMARY KEY (`id_idioma`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `idiomas`
--

INSERT INTO `idiomas` (`id_idioma`, `descripcion`) VALUES
(1, 'Default'),
(2, 'Ingles'),
(3, 'Español');

-- --------------------------------------------------------

--
-- Table structure for table `menu`
--

CREATE TABLE IF NOT EXISTS `menu` (
  `id_menu` int(11) NOT NULL AUTO_INCREMENT,
  `id_url` bigint(11) NOT NULL DEFAULT '0',
  `nivel` bigint(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `orden` int(11) NOT NULL,
  `id_padre` int(11) NOT NULL,
  PRIMARY KEY (`id_menu`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `menu`
--

INSERT INTO `menu` (`id_menu`, `id_url`, `nivel`, `nombre`, `orden`, `id_padre`) VALUES
(1, 0, 0, 'Administración de Menu', 1, 0),
(2, 2, 1, 'Consulta y Mantenimiento de Menu', 2, 1),
(3, 0, 0, 'Administración de XSL', 5, 0),
(4, 3, 1, 'Consulta y Mantenimiento de XSL', 6, 2);

-- --------------------------------------------------------

--
-- Table structure for table `perfil`
--

CREATE TABLE IF NOT EXISTS `perfil` (
  `id_perfil` bigint(20) NOT NULL AUTO_INCREMENT,
  `perfil` varchar(50) NOT NULL,
  `descripcion` varchar(100) NOT NULL,
  PRIMARY KEY (`id_perfil`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `perfil`
--

INSERT INTO `perfil` (`id_perfil`, `perfil`, `descripcion`) VALUES
(1, 'SUPERADMINISTRADOR', 'Administrador de todo el sistema');

-- --------------------------------------------------------

--
-- Table structure for table `rel_urls_xsl`
--

CREATE TABLE IF NOT EXISTS `rel_urls_xsl` (
  `id_url` int(11) NOT NULL,
  `id_xsl` int(11) NOT NULL,
  PRIMARY KEY (`id_url`,`id_xsl`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rel_urls_xsl`
--

INSERT INTO `rel_urls_xsl` (`id_url`, `id_xsl`) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4);

-- --------------------------------------------------------

--
-- Table structure for table `urls`
--

CREATE TABLE IF NOT EXISTS `urls` (
  `id_url` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(100) NOT NULL,
  PRIMARY KEY (`id_url`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `urls`
--

INSERT INTO `urls` (`id_url`, `url`) VALUES
(1, '/MantenedorXSL/updXSL'),
(2, '/MantenedorMenu/lstMenu'),
(3, '/MantenedorXSL/lstXSL'),
(4, '/MantenedorMenu/addMenu');

-- --------------------------------------------------------

--
-- Table structure for table `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre_usuario` varchar(50) NOT NULL,
  `pass_usuario` varchar(100) NOT NULL,
  `id_perfil` bigint(20) NOT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `nombre_usuario`, `pass_usuario`, `id_perfil`) VALUES
(1, 'anibal', '1234', 1);

-- --------------------------------------------------------

--
-- Table structure for table `xsl`
--

CREATE TABLE IF NOT EXISTS `xsl` (
  `id_xsl` int(11) NOT NULL AUTO_INCREMENT,
  `contenido` text,
  `nombre_ejb` varchar(100) DEFAULT NULL,
  `id_idioma` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_xsl`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `xsl`
--

INSERT INTO `xsl` (`id_xsl`, `contenido`, `nombre_ejb`, `id_idioma`) VALUES
(1, '<!-- Toda hoja de transformacion comineza con este tag -->\r\n<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">\r\n	<!-- Indicamos que nuestro output sera un tipo HTML -->\r\n	<xsl:output method = "html" />\r\n	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->\r\n	<xsl:template match="/Documento">\r\n		<html>\r\n			<head>\r\n				<link rel="stylesheet" type="text/css" href="/css/estilo.css?2"/>\r\n				<script src="/js/funciones.js"/>\r\n				<script src="/js/jquery-1.9.1.js"/>\r\n				<script src="/js/jquery.base64.js"/>\r\n				<script src="/js/jquery-ui-1.10.3.custom.js"/>\r\n				<script language="JavaScript">\r\n					<![CDATA[\r\n   				function Buscar()\r\n  				{\r\n  					document.formulario.accion.value="buscar";\r\n  					document.formulario.submit();\r\n  				}\r\n\r\n 				function modificar()\r\n 				{\r\n 					document.formulario.action="updXSL";\r\n					document.formulario.accion.value="modificar";\r\n					document.formulario.contenido.value = Base64.encode(document.formulario.contenido.value);\r\n 					document.formulario.submit();\r\n 				}\r\n				\r\n				]]>\r\n				function preparar()\r\n				{\r\n					<xsl:if test="Cuerpo/updXSL"> \r\n						alert(''<xsl:value-of select="Cuerpo/updXSL/respuesta/mensaje"/>'');\r\n					</xsl:if>\r\n					document.formulario.contenido.value = Base64.decode(document.formulario.contenido.value);\r\n				}\r\n				\r\n				</script>\r\n			</head>\r\n			<body onload="preparar()">\r\n				<textarea id="xml-response" style="display:none;" name="xml-response">\r\n					<xsl:copy-of select="/*"/>\r\n				</textarea>\r\n				<div class="copiar-xml">\r\n					<a href="#" onclick="CopyToClipboard(document.getElementById(''xml-response'').value);return false;">Copiar XML</a>\r\n				</div>\r\n				<form name="formulario" method="POST">\r\n					<input name="accion" type="hidden"/>\r\n					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />\r\n\r\n					<table width="100%" height="90%">\r\n						<tr height="10%">\r\n							<td width="10%">ID XSL:</td>\r\n							<td>\r\n								<xsl:value-of select="Cuerpo/pagXSL/idXSL"/>\r\n								<input type="hidden" name="id_xsl" value="{Cuerpo/pagXSL/idXSL}"/>\r\n							</td>\r\n						</tr>\r\n						<tr height="80%">\r\n							<td>Contenido:</td>\r\n							<td>\r\n								<textarea name="contenido" style="width:100%; height:100%;">\r\n									<xsl:value-of select="Cuerpo/pagXSL/contenido"/>\r\n								</textarea>\r\n							</td>\r\n						</tr>\r\n						<tr height="10%">\r\n							<td>Metodo Ejb:</td>\r\n							<td>\r\n								<input type="text" name="metodo_ejb" value="{Cuerpo/pagXSL/nombreEjb}"/>\r\n							</td>\r\n						</tr>\r\n					</table>\r\n					<div class="btn_guardar">\r\n						<a href="#" onclick="modificar();">Modificar</a>\r\n					</div>\r\n\r\n				</form>\r\n			</body>\r\n		</html>\r\n	</xsl:template>\r\n</xsl:stylesheet>', 'Ejb3XSL.updXSL', 1),
(2, '<!-- Toda hoja de transformacion comineza con este tag -->\n<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">\n	<!-- Indicamos que nuestro output sera un tipo HTML -->\n	<xsl:output method = "html" />\n	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->\n	<xsl:template match="/Documento">\n		<html>\n			<head>\n				<link rel="stylesheet" type="text/css" href="/css/estilo.css"/>\n				<script src="/js/funciones.js"/>\n				<script>\n					function Buscar()\n					{\n						document.formulario.accion.value="buscar";\n						document.formulario.submit();\n					}\n					function Agregar()\n					{\n						document.formulario.action="addMenu";\n						document.formulario.submit();\n					}\n				</script>\n			</head>\n			<body>\n				<textarea id="xml-response" style="display:none;" name="xml-response">\n					<xsl:copy-of select="/*"/>\n				</textarea>\n				<div class="copiar-xml">\n					<a href="#" onclick="CopyToClipboard(document.getElementById(''xml-response'').value);return false;">Copiar XML</a>\n				</div>\n				<form name="formulario" method="POST" action="lstMenu">\n				<input name="accion" type="hidden"/>\n				<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />\n					<div id="filtros">\n						<table>\n							<tr>\n								<td>ID Menu:</td>\n								<td>\n									<input type="text" name="id_menu" />\n								</td>\n								<td>Nombre:</td>\n								<td>\n									<input type="text" name="nombre" />\n								</td>\n							</tr>\n							<tr>\n								<td>\n									<input type="button" name="buscar" value="Buscar" onclick="Buscar();" />\n								</td>\n							</tr>\n						</table>\n					</div>\n					\n					<table>\n						<tr>\n							<td>ID Menu</td>\n							<td>Nombre</td>\n						</tr>\n						<xsl:choose>\n							<xsl:when test="Cuerpo/listaMenu/@cantidad != ''0''">\n								<xsl:for-each select="Cuerpo/listaMenu/fila">\n									<tr>\n										<td><xsl:value-of select="id_menu" /></td>\n										<td><xsl:value-of select="nombre" /></td>\n									</tr>\n								</xsl:for-each>\n							</xsl:when>\n							<xsl:otherwise>\n								<tr>\n								<td colspan="2">No Se Encontraron Registros</td>\n								</tr>\n							</xsl:otherwise>\n						</xsl:choose>\n					</table>\n					<table>\n						<tr>\n							<td>\n								<input type="button" name="agregar" value="Agregar" onclick="Agregar();" />\n							</td>\n						</tr>\n					</table>\n					\n				</form>\n			</body>\n		</html>\n	</xsl:template>\n</xsl:stylesheet>', 'Ejb3Menu.lstMenu', 1),
(3, '<!-- Toda hoja de transformacion comineza con este tag -->\n<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">\n	<!-- Indicamos que nuestro output sera un tipo HTML -->\n	<xsl:output method = "html" />\n	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->\n	<xsl:template match="/Documento">\n		<html>\n			<head>\n				<link rel="stylesheet" type="text/css" href="/css/estilo.css"/>\n				<script src="/js/funciones.js"/>\n				<script language="JavaScript">\n  				function Buscar()\n 				{\n 					document.formulario.accion.value="buscar";\n 					document.formulario.submit();\n 				}\n\n				function modificar(valor)\n				{\n					document.formulario.action="updXSL";\n					document.formulario.id_xsl.value=valor;\n					document.formulario.submit();\n				}\n				</script>\n			</head>\n			<body>\n				<textarea id="xml-response" style="display:none;" name="xml-response">\n					<xsl:copy-of select="/*"/>\n				</textarea>\n				<div class="copiar-xml">\n					<a href="#" onclick="CopyToClipboard(document.getElementById(''xml-response'').value);return false;">Copiar XML</a>\n				</div>\n				<form name="formulario" method="POST" action="lstXSL">\n					<input name="accion" type="hidden"/>\n					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />\n					<div id="filtros">\n						<table>\n							<tr>\n								<td>ID XSL:</td>\n								<td>\n									<input type="text" name="id_xsl" />\n								</td>\n								<td>URL:</td>\n								<td>\n									<input type="text" name="url" />\n								</td>\n							</tr>\n							<tr>\n								<td>ID Idioma</td>\n								<td>\n									<input type="text" name="id_idioma" />\n								</td>\n								<td/>\n								<td/>\n							</tr>\n							<tr>\n								<td>\n									<input type="button" name="buscar" value="Buscar" onclick="Buscar();" />\n								</td>\n							</tr>\n						</table>\n					</div>\n\n					<table>\n						<tr>\n							<td>URL</td>\n							<td>Nombre Ejb</td>\n							<td>Idioma</td>\n						</tr>\n						<xsl:choose>\n							<xsl:when test="Cuerpo/listaXSL/@cantidad != ''0''">\n								<xsl:for-each select="Cuerpo/listaXSL/fila">\n									<tr>\n										<td >\n											<a href="#" onclick="modificar(''{id_xsl}'');">\n												<xsl:value-of select="url" />\n											</a>\n										</td>\n										<td>\n											<xsl:value-of select="nombre_ejb" />\n										</td>\n										<td>\n											<xsl:value-of select="idioma" />\n										</td>\n									</tr>\n								</xsl:for-each>\n							</xsl:when>\n							<xsl:otherwise>\n								<tr>\n									<td colspan="2">No Se Encontraron Registros</td>\n								</tr>\n							</xsl:otherwise>\n						</xsl:choose>\n					</table>\n				</form>\n			</body>\n		</html>\n	</xsl:template>\n</xsl:stylesheet>', 'Ejb3XSL.lstXSL', 1),
(4, '<!-- Toda hoja de transformacion comineza con este tag -->\r\n<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">\r\n	<!-- Indicamos que nuestro output sera un tipo HTML -->\r\n	<xsl:output method = "html" />\r\n	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->\r\n	<xsl:template match="/Documento">\r\n		<html>\r\n			<head>\r\n				<link rel="stylesheet" type="text/css" href="/css/estilo.css"/>\r\n				<script src="/js/funciones.js"/>\r\n				<script>\r\n					function Volver()\r\n					{\r\n						window.location.href = "lstMenu?idSession=<xsl:value-of select=''Cabecera/Parametros/idSession''/>";\r\n					}\r\n					function Agregar()\r\n					{\r\n						if(document.formulario.nombre.value !="")\r\n						{\r\n							document.formulario.accion.value="agregar";\r\n							document.formulario.submit();\r\n						}\r\n						else\r\n						{\r\n							alert("Debe Ingresar Un Nombre");\r\n						}\r\n					}\r\n				</script>\r\n			</head>\r\n			<body>\r\n				<textarea id="xml-response" style="display:none;" name="xml-response">\r\n					<xsl:copy-of select="/*"/>\r\n				</textarea>\r\n				<div class="copiar-xml">\r\n					<a href="#" onclick="CopyToClipboard(document.getElementById(''xml-response'').value);return false;">Copiar XML</a>\r\n				</div>\r\n				<form name="formulario" method="POST" action="addMenu">\r\n					<input name="accion" type="hidden"/>\r\n					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />\r\n					<table>\r\n						<tr>\r\n							<td>Nombre:</td>\r\n							<td>\r\n								<input type="text" name="nombre"/>\r\n							</td>\r\n						</tr>\r\n						<tr>\r\n							<td>Dentro de:</td>\r\n							<td>\r\n								<select name="id_menu">\r\n									<option value="">Ninguno</option>\r\n									<xsl:for-each select="Cuerpo/listaMenu/fila">\r\n										<xsl:if test="id_url = 0">\r\n											<option value="{id_menu}">\r\n												<xsl:value-of select ="nombre"/>\r\n											</option>\r\n										</xsl:if>	\r\n									</xsl:for-each>\r\n								</select>\r\n							</td>\r\n						</tr>\r\n					</table>\r\n\r\n					<div id="botonera">\r\n						<input type="button" name="agregar" value="Agregar" onclick="Agregar();" />&#160;\r\n						<input type="button" name="volver" value="Volver" onclick="Volver();" />\r\n					</div>\r\n				</form>\r\n			</body>\r\n		</html>\r\n	</xsl:template>\r\n</xsl:stylesheet>', 'Ejb3Menu.addMenu', 1);

-- --------------------------------------------------------

--
-- Table structure for table `xsls_principales`
--

CREATE TABLE IF NOT EXISTS `xsls_principales` (
  `id_xsl` int(11) NOT NULL AUTO_INCREMENT,
  `contenido` longtext NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `id_idioma` int(11) NOT NULL,
  PRIMARY KEY (`id_xsl`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `xsls_principales`
--

INSERT INTO `xsls_principales` (`id_xsl`, `contenido`, `nombre`, `id_idioma`) VALUES
(1, '<?xml version="1.0" encoding="UTF-8"?>\r\n<!-- Toda hoja de transformacion comineza con este tag -->\r\n<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">\r\n	<!-- Indicamos que nuestro output sera un tipo HTML -->\r\n	<xsl:output method = "html" />\r\n	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->\r\n	<xsl:template match="/Documento">\r\n		<html>\r\n			<head>\r\n				<link rel="stylesheet" type="text/css" href="/css/menu.css"/>\r\n				<link rel="stylesheet" type="text/css" href="/css/estilo.css"/>\r\n				<script language="JavaScript">\r\n				function expandir(id) \r\n				{\r\n					if (document.getElementById(id).className=="mabierto"){\r\n						document.getElementById(id).className="mcerrado";\r\n						document.getElementsByTagName(''li'')[id].className="mabierto";\r\n					}else{\r\n						document.getElementById(id).className="mabierto";\r\n						document.getElementsByTagName(''li'')[id].className="mcerrado";\r\n					}\r\n				}\r\n 				function CopyToClipboard(text)\r\n 				{\r\n 					window.prompt("Copiar Xml al portapapeles: Ctrl+C, Enter", text);\r\n 				}\r\n				</script>\r\n			</head>\r\n			<body>\r\n				<ul class="menu">\r\n					<xsl:copy-of select="Menu/*"/>\r\n				</ul>\r\n			</body>\r\n		</html>\r\n	</xsl:template>\r\n</xsl:stylesheet>', 'Menu', 1),
(2, '<?xml version="1.0" encoding="UTF-8"?>\r\n<!-- Toda hoja de transformacion comineza con este tag -->\r\n<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">\r\n	<!-- Indicamos que nuestro output sera un tipo HTML -->\r\n	<xsl:output method = "html" />\r\n	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->\r\n	<xsl:template match="/Documento">\r\n		<html>\r\n			<head>\r\n				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>\r\n				<title><xsl:value-of select="Titulo" /></title>\r\n				<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE"/>\r\n				<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"/>\r\n				<META HTTP-EQUIV="EXPIRES" CONTENT="1"/>\r\n				<link rel="shortcut icon" href="../img/icon.ico"/>\r\n			</head>\r\n			<frameset rows="100,*,30" cols="*" frameborder="NO" border="0" framespacing="0">\r\n				<frame src="cabecera" name="cabeceraServlet" scrolling="NO" noresize="noresize" />\r\n				<frameset name="frameset2" id="frameset2" rows="*" cols="240,20,*" framespacing="0" frameborder="NO" border="0">\r\n					<frame src="menu" name="menu" scrolling="AUTO" noresize="noresize" />\r\n					<frame src="../abrircerrarmenu.html" name="openclosemenu" id="openclosemenu" scrolling="NO" noresize="noresize" />\r\n					<frame src="central" name="central" />\r\n				</frameset>\r\n				<frame src="pie" name="pie" scrolling="NO" noresize="noresize" />\r\n			</frameset>\r\n			<noframes>\r\n				<body/>\r\n			</noframes>\r\n		</html>\r\n	</xsl:template>\r\n</xsl:stylesheet>', 'frameset', 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
