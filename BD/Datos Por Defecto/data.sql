-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 11-07-2017 a las 00:11:00
-- Versión del servidor: 5.7.18-0ubuntu0.16.04.1
-- Versión de PHP: 7.0.18-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `portal`
--
CREATE DATABASE IF NOT EXISTS `portal` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `portal`;

--
-- Volcado de datos para la tabla `idiomas`
--

INSERT INTO `idiomas` (`id_idioma`, `descripcion`) VALUES
(1, 'Default'),
(2, 'Ingles'),
(3, 'Español');

--
-- Volcado de datos para la tabla `menu`
--

INSERT INTO `menu` (`id_menu`, `id_url`, `nivel`, `nombre`, `orden`, `id_padre`) VALUES
(1, 0, 0, 'Administración de Menu', 1, 0),
(2, 2, 1, 'Consulta y Mantenimiento de Menu', 2, 1),
(3, 0, 0, 'Administración de XSL', 5, 0),
(4, 3, 1, 'Consulta y Mantenimiento de XSL', 6, 2);

--
-- Volcado de datos para la tabla `perfil`
--

INSERT INTO `perfil` (`id_perfil`, `perfil`, `descripcion`) VALUES
(1, 'SUPERADMINISTRADOR', 'Administrador de todo el sistema');

--
-- Volcado de datos para la tabla `rel_urls_xsl`
--

INSERT INTO `rel_urls_xsl` (`id_url`, `id_xsl`) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

--
-- Volcado de datos para la tabla `urls`
--

INSERT INTO `urls` (`id_url`, `url`) VALUES
(1, '/MantenedorXSL/updXSL'),
(2, '/MantenedorMenu/lstMenu'),
(3, '/MantenedorXSL/lstXSL'),
(4, '/MantenedorMenu/addMenu'),
(5, '/MantenedorMenu/updMenu');

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `nombre_usuario`, `pass_usuario`, `id_perfil`) VALUES
(1, 'anibal', '1234', 1);

--
-- Volcado de datos para la tabla `xsl`
--

INSERT INTO `xsl` (`id_xsl`, `contenido`, `nombre_ejb`, `id_idioma`, `id_url`) VALUES
(1, '<!-- Toda hoja de transformacion comineza con este tag -->\n<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">\n	<!-- Indicamos que nuestro output sera un tipo HTML -->\n	<xsl:output method = "html" />\n	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->\n	<xsl:template match="/Documento">\n		<html>\n			<head>\n				<link rel="stylesheet" type="text/css" href="/css/estilo.css?1"/>\n				<link rel="stylesheet" type="text/css" href="/css/tabla.css"/>\n				<script src="/js/funciones.js"/>\n				<script src="/js/jquery-1.9.1.js"/>\n				<script src="/js/jquery.base64.js"/>\n				<script src="/js/jquery-ui-1.10.3.custom.js"/>\n				<script language="JavaScript">\n					<![CDATA[\n   				function Buscar()\n  				{\n  					document.formulario.accion.value="buscar";\n  					document.formulario.submit();\n  				}\n\n 				function modificar()\n 				{\n 					document.formulario.action="updXSL";\n					document.formulario.accion.value="modificar";\n					document.formulario.contenido.value = Base64.encode(document.formulario.contenido.value);\n 					document.formulario.submit();\n 				}\n				\n				]]>\n				function preparar()\n				{\n					<xsl:if test="Cuerpo/updXSL"> \n						alert(\'<xsl:value-of select="Cuerpo/updXSL/respuesta/mensaje"/>\');\n					</xsl:if>\n					document.formulario.contenido.value = Base64.decode(document.formulario.contenido.value);\n				}\n				\n				</script>\n			</head>\n			<body onload="preparar()">\n				<textarea id="xml-response" style="display:none;" name="xml-response">\n					<xsl:copy-of select="/*"/>\n				</textarea>\n				<span class="spanbtn copiar-xml">\n					<a href="#" onclick="CopyToClipboard(document.getElementById(\'xml-response\').value);return false;">Copiar XML</a>\n				</span>\n				<form name="formulario" method="POST">\n					<input name="accion" type="hidden"/>\n					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />\n					<p>\n						<div class="divtbl">\n							<table width="100%" height="80%">\n								<tbody>\n									<tr height="10%">\n										<td class="td-h1" width="10%">ID XSL:</td>\n										<td class="td-c1">\n											<xsl:value-of select="Cuerpo/pagXSL/idXSL"/>\n											<input type="hidden" name="id_xsl" value="{Cuerpo/pagXSL/idXSL}"/>\n										</td>\n									</tr>\n									<tr height="80%">\n										<td class="td-h1">Contenido:</td>\n										<td class="td-c2">\n											<textarea name="contenido" style="width:100%; height:100%;">\n												<xsl:value-of select="Cuerpo/pagXSL/contenido"/>\n											</textarea>\n										</td>\n									</tr>\n									<tr height="10%">\n										<td class="td-h1">Metodo Ejb:</td>\n										<td class="td-c1">\n											<input type="text" name="metodo_ejb" value="{Cuerpo/pagXSL/nombreEjb}"/>\n										</td>\n									</tr>\n								</tbody>\n							</table>\n						</div>\n					</p>\n					<p>\n						<div class="divbtn">\n							<table>\n								<tbody>\n									<tr>\n										<td colspan="4" >\n											<div id="paging">\n												<ul>\n													<li>\n														<a href="#" onclick="modificar();">\n															<span>Modificar</span>\n														</a>\n													</li>\n												</ul>\n											</div>\n										</td>\n									</tr>\n								</tbody>\n							</table>\n						</div>\n					</p>\n				</form>\n			</body>\n		</html>\n	</xsl:template>\n</xsl:stylesheet>', 'Ejb3XSL.updXSL', 1, 1),
(2, '<!-- Toda hoja de transformacion comineza con este tag -->\n<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">\n	<!-- Indicamos que nuestro output sera un tipo HTML -->\n	<xsl:output method = "html" />\n	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->\n	<xsl:template match="/Documento">\n		<html>\n			<head>\n				<link rel="stylesheet" type="text/css" href="/css/estilo.css?1"/>\n				<link rel="stylesheet" type="text/css" href="/css/tabla.css"/>\n				<script src="/js/funciones.js"/>\n				<script>\n					function Buscar()\n					{\n						document.formulario.accion.value="buscar";\n						document.formulario.submit();\n					}\n					function Eliminar()\n					{\n						if(document.formulario.del_id_menu.value!="")\n						{\n							document.formulario.accion.value="eliminar";\n							if(confirm("Esta Seguro Que Desea Eliminar?"))\n							{\n								document.formulario.submit();\n							}\n						}\n						else\n							alert("Debe Seleccionar Una Opcion.");\n					}\n					function Modificar(valor)\n					{\n						document.formulario.upd_id_menu.value=valor;\n						document.formulario.action="updMenu";\n						document.formulario.submit();\n					}\n					function Agregar()\n					{\n						document.formulario.action="addMenu";\n						document.formulario.submit();\n					}\n				</script>\n			</head>\n			<body>\n				<textarea id="xml-response" style="display:none;" name="xml-response">\n					<xsl:copy-of select="/*"/>\n				</textarea>\n				<span class="spanbtn copiar-xml">\n					<a href="#" onclick="CopyToClipboard(document.getElementById(\'xml-response\').value);return false;">Copiar XML</a>\n				</span>\n				<form name="formulario" method="POST" action="lstMenu">\n				<input name="accion" type="hidden"/>\n				<input name="upd_id_menu" type="hidden"/>\n				<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />\n					<p>\n						<div id="filtros" class="divtbl">\n							<table>\n								<tbody>\n									<tr>\n										<td class="td-h1">ID Menu:</td>\n										<td class="td-c1">\n											<input type="text" name="id_menu" />\n										</td>\n										<td class="td-h1">Nombre:</td>\n										<td class="td-c1">\n											<input type="text" name="nombre" />\n										</td>\n									</tr>\n								</tbody>\n							</table>\n						</div>\n						<div class="divbtn">\n							<table>\n								<tbody>\n									<tr>\n										<td colspan="4" >\n											<div id="paging">\n												<ul>\n													<li>\n														<a href="#" onclick="Buscar();">\n															<span>Buscar</span>\n														</a>\n													</li>\n												</ul>\n											</div>\n										</td>\n									</tr>\n								</tbody>\n							</table>\n						</div>\n					</p>\n					<p>\n						<div class="divtbl">\n							<table>\n								<thead>\n									<tr>\n										<th></th>\n										<th>ID Menu</th>\n										<th>Nombre</th>\n									</tr>\n								</thead>\n								<tbody>\n									<xsl:choose>\n										<xsl:when test="Cuerpo/listaMenu/@cantidad != \'0\'">\n											<xsl:for-each select="Cuerpo/listaMenu/fila">\n												<tr class="td-c{(position() mod 2)}">\n													<td><input type="radio" name="del_id_menu" value="{id_menu}"/></td>\n													<td><xsl:value-of select="id_menu" /></td>\n													<td><a onclick="Modificar({id_menu})" href="#"><xsl:value-of select="nombre" /></a></td>\n												</tr>\n											</xsl:for-each>\n										</xsl:when>\n										<xsl:otherwise>\n											<tr>\n											<td colspan="2">No Se Encontraron Registros</td>\n											</tr>\n										</xsl:otherwise>\n									</xsl:choose>\n								</tbody>\n							</table>\n						</div>\n					</p>\n					<p>\n						<div class="divbtn">\n							<table>\n								<tbody>\n									<tr>\n										<td colspan="4" >\n											<div id="paging">\n												<ul>\n													<li>\n														<a href="#" onclick="Agregar();">\n															<span>Agregar</span>\n														</a>\n													</li>\n													<li>\n														<a href="#" onclick="Eliminar();">\n															<span>Eliminar</span>\n														</a>\n													</li>\n												</ul>\n											</div>\n										</td>\n									</tr>\n								</tbody>\n							</table>\n						</div>\n					</p>\n				</form>\n			</body>\n		</html>\n	</xsl:template>\n</xsl:stylesheet>', 'Ejb3Menu.lstMenu', 1, 2),
(3, '<!-- Toda hoja de transformacion comineza con este tag -->\n<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:portal="http://d-portal.cl">\n	<!-- Indicamos que nuestro output sera un tipo HTML -->\n	\n	<xsl:output method = "html" />\n	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->\n	<xsl:template match="/Documento">\n	\n		<html>\n			<head>\n				<link rel="stylesheet" type="text/css" href="/css/estilo.css?1"/>\n				<link rel="stylesheet" type="text/css" href="/css/tabla.css"/>\n				<script src="/js/funciones.js?1"/>\n				<script language="JavaScript">\n  				function Buscar()\n 				{\n 					document.formulario.accion.value="buscar";\n 					document.formulario.submit();\n 				}\n\n				function modificar(valor)\n				{\n					document.formulario.action="updXSL";\n					document.formulario.id_xsl.value=valor;\n					document.formulario.submit();\n				}\n				</script>\n			</head>\n			<body>\n				<textarea id="xml-response" style="display:none;" name="xml-response">\n					<xsl:copy-of select="/*"/>\n				</textarea>\n				<span class="spanbtn copiar-xml">\n					<a href="#" onclick="CopyToClipboard(document.getElementById(\'xml-response\').value);return false;">Copiar XML</a>\n				</span>\n				<form name="formulario" method="POST" action="lstXSL">\n					<input name="accion" type="hidden"/>\n					<input name="pagina" value="1" type="hidden"/>\n					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />\n					<p>\n						<div id="filtros" class="divtbl">\n							<table>\n								<tbody>\n									<tr>\n										<td class="td-h1">ID XSL:</td>\n										<td class="td-c1">\n											<input type="text" name="id_xsl" />\n										</td>\n										<td class="td-h1">URL:</td>\n										<td class="td-c1">\n											<input type="text" name="url" />\n										</td>\n									</tr>\n									<tr>\n										<td class="td-h1">ID Idioma</td>\n										<td class="td-c2">\n											<input type="text" name="id_idioma" />\n										</td>\n										<td class="td-h1"/>\n										<td class="td-c2"/>\n									</tr>\n								</tbody>\n							</table>\n						</div>\n						<div class="divbtn">\n							<table>\n								<tbody>\n									<tr>\n										<td colspan="4" >\n											<div id="paging">\n												<ul>\n													<li>\n														<a href="#" onclick="Buscar();">\n															<span>Buscar</span>\n														</a>\n													</li>\n												</ul>\n											</div>\n										</td>\n									</tr>\n								</tbody>\n							</table>\n						</div>\n					</p>\n					<p>\n						<div class="divtbl">\n							<table>\n								<thead>\n									<tr>\n										<th>URL</th>\n										<th>Nombre Ejb</th>\n										<th>Idioma</th>\n									</tr>\n								</thead>\n								<tbody>\n									<xsl:choose>\n										<xsl:when test="Cuerpo/listaXSL/@cantidad != \'0\'">\n											<xsl:for-each select="Cuerpo/listaXSL/fila">\n												<tr class="td-c{(position() mod 2)}">\n													<td >\n														<a href="#" onclick="modificar(\'{id_xsl}\');">\n															<xsl:value-of select="url" />\n														</a>\n													</td>\n													<td>\n														<xsl:value-of select="nombre_ejb" />\n													</td>\n													<td>\n														<xsl:value-of select="idioma" />\n													</td>\n												</tr>\n											</xsl:for-each>\n										</xsl:when>\n										<xsl:otherwise>\n											<tr>\n												<td colspan="2">No Se Encontraron Registros</td>\n											</tr>\n										</xsl:otherwise>\n									</xsl:choose>\n								</tbody>	\n								<xsl:if test="Cuerpo/listaXSL/@cantidad != \'0\'">\n									<xsl:variable name="contador">\n										<xsl:number value="ceiling(Cuerpo/TOTAL_REGISTROS div Cabecera/DatosConf/registrosPorPagina)"/>\n									</xsl:variable>\n									<xsl:if test="$contador &gt; 1">\n										<tfoot>\n											<tr>\n												<td colspan="3" >\n													<div id="paging">\n														<ul class="ul-der">\n															<li>\n																<xsl:choose>\n																	<xsl:when test="$contador &lt; 6">\n																		<xsl:for-each select="1 to $contador">\n																				<a href="#" onclick="paginar(\'{position()}\',\'buscar\');">\n																					<span><xsl:value-of select="position()"/></span>\n																				</a>\n																		</xsl:for-each>\n																	</xsl:when>\n																	<xsl:otherwise>\n																		<xsl:variable name="pag">\n																			<xsl:number value="Cabecera/Parametros/pagina"/>\n																		</xsl:variable>\n																		<xsl:variable name="pagSig">\n																			<xsl:number value="$pag + 1"/>\n																		</xsl:variable>\n																		<xsl:choose>\n																			<xsl:when test = "$pag = 1">\n																				<xsl:for-each select="1 to 4">\n																					<a href="#" onclick="paginar(\'{position()}\',\'buscar\');">\n																						<span><xsl:value-of select="position()"/></span>\n																					</a>\n																				</xsl:for-each>\n																				<a href="#" onclick="paginar(\'{$pagSig}\',\'buscar\');">\n																					<span><xsl:value-of select="\'&gt;\'"/></span>\n																				</a>\n																			</xsl:when>\n																			<xsl:when test="$pag &gt; 1 and $pag &lt; $contador - 2 ">\n																				<xsl:variable name="pagAnt">\n																					<xsl:number value="$pag - 1"/>\n																				</xsl:variable>\n																				<a href="#" onclick="paginar(\'{$pagAnt}\',\'buscar\');">\n																					<span><xsl:value-of select="\'&lt;\'"/></span>\n																				</a>\n																				<xsl:for-each select="1 to 3">\n																					<xsl:variable name ="pagAct" select="$pag + position() - 1"/>\n																					<a href="#" onclick="paginar(\'{$pagAct}\',\'buscar\');">\n																						<span><xsl:value-of select="$pagAct"/></span>\n																					</a>\n																				</xsl:for-each>\n																				<a href="#" onclick="paginar(\'{$pagSig}\',\'buscar\');">\n																					<span><xsl:value-of select="\'&gt;\'"/></span>\n																				</a>\n																			</xsl:when>\n																			<xsl:otherwise>\n																				<xsl:variable name="pagAnt">\n																					<xsl:number value="$pag - 1"/>\n																				</xsl:variable>\n																				<a href="#" onclick="paginar(\'{$pagAnt}\',\'buscar\');">\n																					<span><xsl:value-of select="\'&lt;\'"/></span>\n																				</a>\n																				<xsl:for-each select="1 to 4">\n																					<xsl:variable name ="pagAct" select="$contador + position() - 4"/>\n																					<a href="#" onclick="paginar(\'{$pagAct}\',\'buscar\');">\n																						<span><xsl:value-of select="$pagAct"/></span>\n																					</a>\n																				</xsl:for-each>\n																			</xsl:otherwise>\n																		</xsl:choose>\n																	</xsl:otherwise>\n																</xsl:choose>\n															</li>\n														</ul>\n													</div>\n												</td>\n											</tr>\n										</tfoot>\n									</xsl:if>\n								</xsl:if> 							\n							</table>\n						</div>\n					</p>\n				</form>\n			</body>\n		</html>\n	</xsl:template>\n</xsl:stylesheet>', 'Ejb3XSL.lstXSL', 1, 3),
(4, '<!-- Toda hoja de transformacion comineza con este tag -->\n<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">\n	<!-- Indicamos que nuestro output sera un tipo HTML -->\n	<xsl:output method = "html" />\n	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->\n	<xsl:template match="/Documento">\n		<html>\n			<head>\n				<link rel="stylesheet" type="text/css" href="/css/estilo.css?1"/>\n				<link rel="stylesheet" type="text/css" href="/css/tabla.css"/>\n				<script src="/js/funciones.js"/>\n				<script>\n					function Volver()\n					{\n						window.location.href = "lstMenu?idSession=<xsl:value-of select=\'Cabecera/Parametros/idSession\'/>";\n					}\n					function Agregar()\n					{\n						if(document.formulario.nombre.value !="")\n						{\n							document.formulario.accion.value="agregar";\n							document.formulario.submit();\n						}\n						else\n						{\n							alert("Debe Ingresar Un Nombre");\n						}\n					}\n				</script>\n			</head>\n			<body>\n				<textarea id="xml-response" style="display:none;" name="xml-response">\n					<xsl:copy-of select="/*"/>\n				</textarea>\n				<span class="spanbtn copiar-xml">\n					<a href="#" onclick="CopyToClipboard(document.getElementById(\'xml-response\').value);return false;">Copiar XML</a>\n				</span>\n				<form name="formulario" method="POST" action="addMenu">\n					<input name="accion" type="hidden"/>\n					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />\n					<p>\n						<div class="divtbl">\n							<table>\n								<tr>\n									<td class="td-h1" >Nombre:</td>\n									<td class="td-c1">\n										<input type="text" name="nombre"/>\n									</td>\n								</tr>\n								<tr>\n									<td class="td-h1">Dentro de:</td>\n									<td class="td-c2">\n										<select name="id_menu">\n											<option value="">Ninguno</option>\n											<xsl:for-each select="Cuerpo/listaMenu/fila">\n												<xsl:if test="id_url = 0">\n													<option value="{id_menu}">\n														<xsl:value-of select ="nombre"/>\n													</option>\n												</xsl:if>	\n											</xsl:for-each>\n										</select>\n									</td>\n								</tr>\n							</table>\n						</div>\n					</p>\n					<p>\n						<div class="divbtn">\n							<table>\n								<tbody>\n									<tr>\n										<td colspan="4" >\n											<div id="paging">\n												<ul>\n													<li>\n														<a href="#" onclick="Agregar();">\n															<span>Agregar</span>\n														</a>\n													</li>\n													<li>\n														<a href="#" onclick="Volver();">\n															<span>Volver</span>\n														</a>\n													</li>\n												</ul>\n											</div>\n										</td>\n									</tr>\n								</tbody>\n							</table>\n						</div>\n					</p>\n				</form>\n			</body>\n		</html>\n	</xsl:template>\n</xsl:stylesheet>', 'Ejb3Menu.addMenu', 1, 4),
(5, '<!-- Toda hoja de transformacion comineza con este tag -->\n<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">\n	<!-- Indicamos que nuestro output sera un tipo HTML -->\n	<xsl:output method = "html" />\n	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->\n	<xsl:template match="/Documento">\n		<html>\n			<head>\n				<link rel="stylesheet" type="text/css" href="/css/estilo.css?1"/>\n				<link rel="stylesheet" type="text/css" href="/css/tabla.css"/>\n				<script src="/js/funciones.js"/>\n				<script>\n					function Volver()\n					{\n						window.location.href = "lstMenu?idSession=<xsl:value-of select=\'Cabecera/Parametros/idSession\'/>";\n					}\n					function Modificar()\n					{\n						if(document.formulario.nombre.value !="")\n						{\n							document.formulario.accion.value="modificar";\n							document.formulario.submit();\n						}\n						else\n						{\n							alert("Debe Ingresar Un Nombre");\n						}\n					}\n				</script>\n			</head>\n			<body>\n				<textarea id="xml-response" style="display:none;" name="xml-response">\n					<xsl:copy-of select="/*"/>\n				</textarea>\n				<span class="spanbtn copiar-xml">\n					<a href="#" onclick="CopyToClipboard(document.getElementById(\'xml-response\').value);return false;">Copiar XML</a>\n				</span>\n				<form name="formulario" method="POST" action="updMenu">\n					<input name="accion" type="hidden"/>\n					<input name="upd_id_menu" type="hidden" value = "{Cabecera/Parametros/upd_id_menu}"/>\n					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />\n					<p>\n						<div class="divtbl">\n							<table>\n								<tr>\n									<td class="td-h1">Nombre:</td>\n									<td class="td-c1">\n										<input type="text" name="nombre" value="{Cuerpo/menu/nombre}" />\n									</td>\n								</tr>\n								<tr>\n									<td class="td-h1">Dentro de:</td>\n									<td class="td-c2">\n										<select name="id_padre">\n											<option value="">Ninguno</option>\n											<xsl:for-each select="Cuerpo/listaMenu/fila">\n												<xsl:if test="id_url = 0">\n													<option value="{id_menu}">\n														<xsl:if test="id_menu = /Documento/Cuerpo/menu/id_padre">\n															<xsl:attribute name="selected">selected</xsl:attribute>\n														</xsl:if>\n														<xsl:value-of select ="nombre"/>\n													</option>\n												</xsl:if>	\n											</xsl:for-each>\n										</select>\n									</td>\n								</tr>\n							</table>\n						</div>\n					</p>\n					<p>\n						<div class="divbtn">\n							<table>\n								<tbody>\n									<tr>\n										<td colspan="4" >\n											<div id="paging">\n												<ul>\n													<li>\n														<a href="#" onclick="Modificar();">\n															<span>Modificar</span>\n														</a>\n													</li>\n													<li>\n														<a href="#" onclick="Volver();">\n															<span>Volver</span>\n														</a>\n													</li>\n												</ul>\n											</div>\n										</td>\n									</tr>\n								</tbody>\n							</table>\n						</div>\n					</p>\n				</form>\n			</body>\n		</html>\n	</xsl:template>\n</xsl:stylesheet>', 'Ejb3Menu.updMenu', 1, 5);

--
-- Volcado de datos para la tabla `xsls_principales`
--

INSERT INTO `xsls_principales` (`id_xsl`, `contenido`, `nombre`, `id_idioma`) VALUES
(1, '<?xml version="1.0" encoding="UTF-8"?>\r\n<!-- Toda hoja de transformacion comineza con este tag -->\r\n<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">\r\n	<!-- Indicamos que nuestro output sera un tipo HTML -->\r\n	<xsl:output method = "html" />\r\n	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->\r\n	<xsl:template match="/Documento">\r\n		<html>\r\n			<head>\r\n				<link rel="stylesheet" type="text/css" href="/css/menu.css"/>\r\n				<link rel="stylesheet" type="text/css" href="/css/estilo.css"/>\r\n				<script language="JavaScript">\r\n				function expandir(id) \r\n				{\r\n					if (document.getElementById(id).className=="mabierto"){\r\n						document.getElementById(id).className="mcerrado";\r\n						document.getElementsByTagName(\'li\')[id].className="mabierto";\r\n					}else{\r\n						document.getElementById(id).className="mabierto";\r\n						document.getElementsByTagName(\'li\')[id].className="mcerrado";\r\n					}\r\n				}\r\n 				function CopyToClipboard(text)\r\n 				{\r\n 					window.prompt("Copiar Xml al portapapeles: Ctrl+C, Enter", text);\r\n 				}\r\n				</script>\r\n			</head>\r\n			<body>\r\n				<ul class="menu">\r\n					<xsl:copy-of select="Menu/*"/>\r\n				</ul>\r\n			</body>\r\n		</html>\r\n	</xsl:template>\r\n</xsl:stylesheet>', 'Menu', 1),
(2, '<?xml version="1.0" encoding="UTF-8"?>\r\n<!-- Toda hoja de transformacion comineza con este tag -->\r\n<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">\r\n	<!-- Indicamos que nuestro output sera un tipo HTML -->\r\n	<xsl:output method = "html" />\r\n	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->\r\n	<xsl:template match="/Documento">\r\n		<html>\r\n			<head>\r\n				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>\r\n				<title><xsl:value-of select="Titulo" /></title>\r\n				<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE"/>\r\n				<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"/>\r\n				<META HTTP-EQUIV="EXPIRES" CONTENT="1"/>\r\n				<link rel="shortcut icon" href="../img/icon.ico"/>\r\n			</head>\r\n			<frameset rows="100,*,30" cols="*" frameborder="NO" border="0" framespacing="0">\r\n				<frame src="cabecera" name="cabeceraServlet" scrolling="NO" noresize="noresize" />\r\n				<frameset name="frameset2" id="frameset2" rows="*" cols="240,20,*" framespacing="0" frameborder="NO" border="0">\r\n					<frame src="menu" name="menu" scrolling="AUTO" noresize="noresize" />\r\n					<frame src="../abrircerrarmenu.html" name="openclosemenu" id="openclosemenu" scrolling="NO" noresize="noresize" />\r\n					<frame src="central" name="central" />\r\n				</frameset>\r\n				<frame src="pie" name="pie" scrolling="NO" noresize="noresize" />\r\n			</frameset>\r\n			<noframes>\r\n				<body/>\r\n			</noframes>\r\n		</html>\r\n	</xsl:template>\r\n</xsl:stylesheet>', 'frameset', 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
