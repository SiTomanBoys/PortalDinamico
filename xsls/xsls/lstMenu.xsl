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
					function Buscar()
					{
						document.formulario.accion.value="buscar";
						document.formulario.submit();
					}
					function Eliminar()
					{
						if(document.formulario.del_id_menu.value!="")
						{
							document.formulario.accion.value="eliminar";
							if(confirm("Esta Seguro Que Desea Eliminar?"))
							{
								document.formulario.submit();
							}
						}
						else
							alert("Debe Seleccionar Una Opcion.");
					}
					function Modificar(valor)
					{
						document.formulario.upd_id_menu.value=valor;
						document.formulario.action="updMenu";
						document.formulario.submit();
					}
					function Agregar()
					{
						document.formulario.action="addMenu";
						document.formulario.submit();
					}
				</script>
			</head>
			<body>
				<xsl:sequence select="portal:editor(Cabecera/DatosConf/editor,Cabecera/pagina,Cabecera/Parametros/idSession)"/>
				<form name="formulario" method="POST" action="lstMenu">
				<input name="accion" type="hidden"/>
				<input name="upd_id_menu" type="hidden"/>
				<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />
					<p>
						<div id="filtros" class="divtbl">
							<table>
								<tbody>
									<tr>
										<td class="td-h1">ID Menu:</td>
										<td class="td-c1">
											<input type="text" name="id_menu" />
										</td>
										<td class="td-h1">Nombre:</td>
										<td class="td-c1">
											<input type="text" name="nombre" />
										</td>
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
										<th></th>
										<th>ID Menu</th>
										<th>Nombre</th>
									</tr>
								</thead>
								<tbody>
									<xsl:choose>
										<xsl:when test="Cuerpo/listaMenu/@cantidad != '0'">
											<xsl:for-each select="Cuerpo/listaMenu/fila">
												<tr class="td-c{(position() mod 2)}">
													<td><input type="radio" name="del_id_menu" value="{id_menu}"/></td>
													<td><xsl:value-of select="id_menu" /></td>
													<td><a onclick="Modificar({id_menu})" href="#"><xsl:value-of select="nombre" /></a></td>
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
														<a href="#" onclick="Eliminar();">
															<span>Eliminar</span>
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