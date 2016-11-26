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
					function Volver()
					{
						window.location.href = "lstMenu?idSession=<xsl:value-of select='Cabecera/Parametros/idSession'/>";
					}
					function Agregar()
					{
						if(document.formulario.nombre.value !="")
						{
							document.formulario.accion.value="agregar";
							document.formulario.submit();
						}
						else
						{
							alert("Debe Ingresar Un Nombre");
						}
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
				<form name="formulario" method="POST" action="addMenu">
					<input name="accion" type="hidden"/>
					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />
					<table>
						<tr>
							<td>Nombre:</td>
							<td>
								<input type="text" name="nombre"/>
							</td>
						</tr>
						<tr>
							<td>Dentro de:</td>
							<td>
								<select name="id_menu">
									<option value="">Ninguno</option>
									<xsl:for-each select="Cuerpo/listaMenu/fila">
										<xsl:if test="id_url = 0">
											<option value="{id_menu}">
												<xsl:value-of select ="nombre"/>
											</option>
										</xsl:if>	
									</xsl:for-each>
								</select>
							</td>
						</tr>
					</table>

					<div id="botonera">
						<input type="button" name="agregar" value="Agregar" onclick="Agregar();" />&#160;
						<input type="button" name="volver" value="Volver" onclick="Volver();" />
					</div>
				</form>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>