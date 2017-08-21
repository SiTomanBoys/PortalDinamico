<!-- Toda hoja de transformacion comineza con este tag -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:portal="http://d-portal.cl">
	<xsl:include href="[[raiz_xsl]]/funciones.xsl"/>
	<!-- Indicamos que nuestro output sera un tipo HTML -->
	<xsl:output method = "html" />
	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->
	<xsl:template match="/Documento">
		<html>
			<head>
				<link rel="stylesheet" type="text/css" href="/css/estilo.css?1"/>
				<link rel="stylesheet" type="text/css" href="/css/tabla.css"/>
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
				<xsl:sequence select="portal:editor(Cabecera/DatosConf/editor)"/>
				<form name="formulario" method="POST">
					<input name="accion" type="hidden"/>
					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />
					<p>
						<div class="divtbl">
							<table width="100%" height="80%">
								<tbody>
									<tr height="10%">
										<td class="td-h1" width="10%">ID XSL:</td>
										<td class="td-c1">
											<xsl:value-of select="Cuerpo/pagXSL/idXSL"/>
											<input type="hidden" name="id_xsl" value="{Cuerpo/pagXSL/idXSL}"/>
										</td>
									</tr>
									<tr height="80%">
										<td class="td-h1">Contenido:</td>
										<td class="td-c2">
											<textarea name="contenido" style="width:100%; height:100%;">
												<xsl:value-of select="Cuerpo/pagXSL/contenido"/>
											</textarea>
										</td>
									</tr>
									<tr height="10%">
										<td class="td-h1">Metodo Ejb:</td>
										<td class="td-c1">
											<input type="text" name="metodo_ejb" value="{Cuerpo/pagXSL/nombreEjb}"/>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</p>
					<p>
						<div class="divbtn">
							<table>
								<tbody>
									<tr>
										<td colspan="4" >
											<div id="paging">
												<ul>
													<li>
														<a href="#" onclick="modificar();">
															<span>Modificar</span>
														</a>
													</li>
												</ul>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</p>
				</form>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>