<!-- Toda hoja de transformacion comineza con este tag -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Indicamos que nuestro output sera un tipo HTML -->
	<xsl:output method = "html" />
	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->
	<xsl:template match="/Documento">
		<html>
			<head>
				<link rel="stylesheet" type="text/css" href="/css/estilo.css?2"/>
				<script src="/js/funciones.js"/>
				<script src="/js/jquery-1.9.1.js"/>
				<script src="/js/jquery.base64.js"/>
				<script src="/js/jquery-ui-1.10.3.custom.js"/>
				<script language="JavaScript">
					<![CDATA[
   				function Buscar()
  				{
  					document.formulario.accion.value="buscar";
  					document.formulario.submit();
  				}

 				function modificar()
 				{
 					document.formulario.action="updXSL";
					document.formulario.accion.value="modificar";
					document.formulario.contenido.value = Base64.encode(document.formulario.contenido.value);
 					document.formulario.submit();
 				}
				
				]]>
				function preparar()
				{
					<xsl:if test="Cuerpo/updXSL"> 
						alert('<xsl:value-of select="Cuerpo/updXSL/respuesta/mensaje"/>');
					</xsl:if>
					document.formulario.contenido.value = Base64.decode(document.formulario.contenido.value);
				}
				
				</script>
			</head>
			<body onload="preparar()">
				<textarea id="xml-response" style="display:none;" name="xml-response">
					<xsl:copy-of select="/*"/>
				</textarea>
				<div class="copiar-xml">
					<a href="#" onclick="CopyToClipboard(document.getElementById('xml-response').value);return false;">Copiar XML</a>
				</div>
				<form name="formulario" method="POST">
					<input name="accion" type="hidden"/>
					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />

					<table width="100%" height="90%">
						<tr height="10%">
							<td width="10%">ID XSL:</td>
							<td>
								<xsl:value-of select="Cuerpo/pagXSL/idXSL"/>
								<input type="hidden" name="id_xsl" value="{Cuerpo/pagXSL/idXSL}"/>
							</td>
						</tr>
						<tr height="80%">
							<td>Contenido:</td>
							<td>
								<textarea name="contenido" style="width:100%; height:100%;">
									<xsl:value-of select="Cuerpo/pagXSL/contenido"/>
								</textarea>
							</td>
						</tr>
						<tr height="10%">
							<td>Metodo Ejb:</td>
							<td>
								<input type="text" name="metodo_ejb" value="{Cuerpo/pagXSL/nombreEjb}"/>
							</td>
						</tr>
					</table>
					<div class="btn_guardar">
						<a href="#" onclick="modificar();">Modificar</a>
					</div>

				</form>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>