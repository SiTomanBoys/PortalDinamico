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
				<xsl:sequence select="portal:editor(Cabecera/DatosConf/editor,Cabecera/pagina,Cabecera/Parametros/idSession)"/>
				<form name="formulario" method="POST" action="addMenu">
					<input name="accion" type="hidden"/>
					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />
					<p>
						<div class="divtbl">
							<table>
								<tr>
									<td class="td-h1" >Nombre:</td>
									<td class="td-c1">
										<input type="text" name="nombre"/>
									</td>
								</tr>
								<tr>
									<td class="td-h1">Dentro de:</td>
									<td class="td-c2">
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
								<tr>
									<td class="td-h1">Url:</td>
									<td class="td-c1">
										<select name="id_url">
											<option value="">Ninguno</option>
											<xsl:for-each select="Cuerpo/listaUrl/fila">
												<option value="{id_url}">
													<xsl:if test="id_url = /Documento/Cuerpo/menu/id_url">
														<xsl:attribute name="selected">selected</xsl:attribute>
													</xsl:if>
													<xsl:value-of select ="url"/>
												</option>
											</xsl:for-each>
										</select>
									</td>
								</tr>
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
														<a href="#" onclick="Agregar();">
															<span>Agregar</span>
														</a>
													</li>
													<li>
														<a href="#" onclick="Volver();">
															<span>Volver</span>
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