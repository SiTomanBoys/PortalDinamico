<?xml version="1.0" encoding="UTF-8"?>
 <!-- Toda hoja de transformacion comineza con este tag -->
 <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 	<!-- Indicamos que nuestro output sera un tipo HTML -->
 	<xsl:output method = "html" />
 	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->
 	<xsl:template match="/Documento">
 		<html>
 			<head>
 				<link rel="stylesheet" type="text/css" href="/css/estilo.css"/>
 				<script language="JavaScript">
  				function CopyToClipboard(text)
  				{
  					window.prompt("Copiar Xml al portapapeles: Ctrl+C, Enter", text);
  				}
  				function Buscar()
 				{
 					document.formulario.accion.value="buscar";
 					document.formulario.submit();
 				}
				
				function modificar(valor)
				{
					document.formulario.action="updXSL";
					document.formulario.id_xsl.value=valor;
					document.formulario.submit();
				}
 				</script>
 			</head>
 			<body>
 				<textarea id="xml-response" style="display:none;" name="xml-response">
 					<xsl:copy-of select="/*"/>
 				</textarea>
 				<div class="copiar-xml">
 					<a href="#" onclick="CopyToClipboard(document.getElementById('xml-response').value);return false;">Copiar XML</a>
 				</div>
 				<form name="formulario">
 				<input name="accion" type="hidden"/>
 				<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />
 					<div id="filtros">
 						<table>
 							<tr>
 								<td>ID XSL:</td>
 								<td>
 									<input type="text" name="id_xsl" />
 								</td>
 								<td>URL:</td>
 								<td>
 									<input type="text" name="url" />
 								</td>
 							</tr>
 							<tr>
 								<td>ID Idioma</td>
 								<td>
 									<input type="text" name="id_idioma" />
 								</td>
 								<td></td>
 								<td></td>
 							</tr>
 							<tr>
 								<td>
 									<input type="button" name="buscar" value="Buscar" onclick="Buscar();" />
 								</td>
 							</tr>
 						</table>
 					</div>
 					
 					<table>
 						<tr>
 							<td>URL</td>
 							<td>Nombre Ejb</td>
 							<td>Idioma</td>
 						</tr>
 						<xsl:choose>
 							<xsl:when test="Cuerpo/listaXSL/@cantidad != '0'">
 								<xsl:for-each select="Cuerpo/listaXSL/fila">
 									<tr>
										<td ><a href="javascript:modificar('{id_xsl}');"><xsl:value-of select="url" /></a></td>
 										<td><xsl:value-of select="nombre_ejb" /></td>
 										<td><xsl:value-of select="idioma" /></td>
 									</tr>
 								</xsl:for-each>
 							</xsl:when>
 							<xsl:otherwise>
 								<tr>
 								<td colspan="2">No Se Encontraron Registros</td>
 								</tr>
 							</xsl:otherwise>
 						</xsl:choose>
 					</table>
 				</form>
 			</body>
 		</html>
 	</xsl:template>
 </xsl:stylesheet>