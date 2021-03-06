<!-- Toda hoja de transformacion comineza con este tag -->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:portal="http://d-portal.cl">
	<xsl:include href="[[raiz_xsl]]/funciones.xsl"/>
	<!-- Indicamos que nuestro output sera un tipo HTML -->
	<xsl:output method = "html" />
	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->
	<xsl:template match="/Documento">
	
		<html>
			<head>
				<link rel="stylesheet" type="text/css" href="/css/estilo.css?1"/>
				<link rel="stylesheet" type="text/css" href="/css/tabla.css"/>
				<script src="/js/funciones.js?1"/>
				<script language="JavaScript">
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
				<xsl:sequence select="portal:editor(Cabecera/DatosConf/editor,Cabecera/pagina,Cabecera/Parametros/idSession)"/>
				<form name="formulario" method="POST" action="lstXSL">
					<input name="accion" type="hidden"/>
					<input name="pagina" value="1" type="hidden"/>
					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />
					<p>
						<div id="filtros" class="divtbl">
							<table>
								<tbody>
									<tr>
										<td class="td-h1">ID XSL:</td>
										<td class="td-c1">
											<input type="text" name="id_xsl" />
										</td>
										<td class="td-h1">URL:</td>
										<td class="td-c1">
											<input type="text" name="url" />
										</td>
									</tr>
									<tr>
										<td class="td-h1">ID Idioma</td>
										<td class="td-c2">
											<input type="text" name="id_idioma" />
										</td>
										<td class="td-h1"/>
										<td class="td-c2"/>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="divbtn">
							<table>
								<tbody>
									<tr>
										<td colspan="4" >
											<div id="paging">
												<ul>
													<li>
														<a href="#" onclick="Buscar();">
															<span>Buscar</span>
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
					<p>
						<div class="divtbl">
							<table>
								<thead>
									<tr>
										<th>URL</th>
										<th>Nombre Ejb</th>
										<th>Idioma</th>
									</tr>
								</thead>
								<tbody>
									<xsl:choose>
										<xsl:when test="Cuerpo/listaXSL/@cantidad != '0'">
											<xsl:for-each select="Cuerpo/listaXSL/fila">
												<tr class="td-c{(position() mod 2)}">
													<td >
														<a href="#" onclick="modificar('{id_xsl}');">
															<xsl:value-of select="url" />
														</a>
													</td>
													<td>
														<xsl:value-of select="nombre_ejb" />
													</td>
													<td>
														<xsl:value-of select="idioma" />
													</td>
												</tr>
											</xsl:for-each>
										</xsl:when>
										<xsl:otherwise>
											<tr>
												<td colspan="2">No Se Encontraron Registros</td>
											</tr>
										</xsl:otherwise>
									</xsl:choose>
								</tbody>	
								<xsl:if test="Cuerpo/listaXSL/@cantidad != '0'">
									<xsl:value-of select="portal:paginacion(Cuerpo/TOTAL_REGISTROS, Cabecera/DatosConf/registrosPorPagina, Cabecera/Parametros/pagina)"/>
								</xsl:if> 							
							</table>
						</div>
					</p>
				</form>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>