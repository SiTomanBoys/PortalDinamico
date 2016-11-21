<!-- Toda hoja de transformacion comineza con este tag -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Indicamos que nuestro output sera un tipo HTML -->
	<xsl:output method = "html" />
	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->
	<xsl:template match="/Documento">
		<html>
			<head>
				<link rel="stylesheet" type="text/css" href="/css/estilo.css"/>
				<script src="/js/funciones.js"/>
				<script>
					function Buscar()
					{
						document.formulario.accion.value="buscar";
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
				<form name="formulario" method="POST" action="lstMenu">
				<input name="accion" type="hidden"/>
				<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />
					<div id="filtros">
						<table>
							<tr>
								<td>ID Menu:</td>
								<td>
									<input type="text" name="id_menu" />
								</td>
								<td>Nombre:</td>
								<td>
									<input type="text" name="nombre" />
								</td>
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
							<td>ID Menu</td>
							<td>Nombre</td>
						</tr>
						<xsl:choose>
							<xsl:when test="Cuerpo/listaMenu/@cantidad != '0'">
								<xsl:for-each select="Cuerpo/listaMenu/fila">
									<tr>
										<td><xsl:value-of select="id_menu" /></td>
										<td><xsl:value-of select="nombre" /></td>
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