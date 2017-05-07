<!-- Toda hoja de transformacion comineza con este tag -->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:portal="http://d-portal.cl">
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
				<textarea id="xml-response" style="display:none;" name="xml-response">
					<xsl:copy-of select="/*"/>
				</textarea>
				<span class="spanbtn copiar-xml">
					<a href="#" onclick="CopyToClipboard(document.getElementById('xml-response').value);return false;">Copiar XML</a>
				</span>
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
									<xsl:variable name="contador">
										<xsl:number value="ceiling(Cuerpo/TOTAL_REGISTROS div Cabecera/DatosConf/registrosPorPagina)"/>
									</xsl:variable>
									<xsl:if test="$contador &gt; 1">
										<tfoot>
											<tr>
												<td colspan="3" >
													<div id="paging">
														<ul class="ul-der">
															<li>
																<xsl:choose>
																	<xsl:when test="$contador &lt; 6">
																		<xsl:for-each select="1 to $contador">
																				<a href="#" onclick="paginar('{position()}','buscar');">
																					<span><xsl:value-of select="position()"/></span>
																				</a>
																		</xsl:for-each>
																	</xsl:when>
																	<xsl:otherwise>
																		<xsl:variable name="pag">
																			<xsl:number value="Cabecera/Parametros/pagina"/>
																		</xsl:variable>
																		<xsl:variable name="pagSig">
																			<xsl:number value="$pag + 1"/>
																		</xsl:variable>
																		<xsl:choose>
																			<xsl:when test = "$pag = 1">
																				<xsl:for-each select="1 to 4">
																					<a href="#" onclick="paginar('{position()}','buscar');">
																						<span><xsl:value-of select="position()"/></span>
																					</a>
																				</xsl:for-each>
																				<a href="#" onclick="paginar('{$pagSig}','buscar');">
																					<span><xsl:value-of select="'&gt;'"/></span>
																				</a>
																			</xsl:when>
																			<xsl:when test="$pag &gt; 1 and $pag &lt; $contador - 2 ">
																				<xsl:variable name="pagAnt">
																					<xsl:number value="$pag - 1"/>
																				</xsl:variable>
																				<a href="#" onclick="paginar('{$pagAnt}','buscar');">
																					<span><xsl:value-of select="'&lt;'"/></span>
																				</a>
																				<xsl:for-each select="1 to 3">
																					<xsl:variable name ="pagAct" select="$pag + position() - 1"/>
																					<a href="#" onclick="paginar('{$pagAct}','buscar');">
																						<span><xsl:value-of select="$pagAct"/></span>
																					</a>
																				</xsl:for-each>
																				<a href="#" onclick="paginar('{$pagSig}','buscar');">
																					<span><xsl:value-of select="'&gt;'"/></span>
																				</a>
																			</xsl:when>
																			<xsl:otherwise>
																				<xsl:variable name="pagAnt">
																					<xsl:number value="$pag - 1"/>
																				</xsl:variable>
																				<a href="#" onclick="paginar('{$pagAnt}','buscar');">
																					<span><xsl:value-of select="'&lt;'"/></span>
																				</a>
																				<xsl:for-each select="1 to 4">
																					<xsl:variable name ="pagAct" select="$contador + position() - 4"/>
																					<a href="#" onclick="paginar('{$pagAct}','buscar');">
																						<span><xsl:value-of select="$pagAct"/></span>
																					</a>
																				</xsl:for-each>
																			</xsl:otherwise>
																		</xsl:choose>
																	</xsl:otherwise>
																</xsl:choose>
															</li>
														</ul>
													</div>
												</td>
											</tr>
										</tfoot>
									</xsl:if>
								</xsl:if> 							
							</table>
						</div>
					</p>
				</form>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>