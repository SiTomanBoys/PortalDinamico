<!-- Toda hoja de transformacion comineza con este tag -->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:portal="http://d-portal.cl">
	<xsl:include href="[[raiz_xsl]]/funciones.xsl"/>
	<!-- Indicamos que nuestro output sera un tipo HTML -->
	<xsl:output method = "html" />
	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->
	<xsl:template match="/Documento">
	
		<html>
			<head>
				<link rel="stylesheet" type="text/css" href="/css/estilo.css"/>
				<link rel="stylesheet" type="text/css" href="/css/tabla.css"/>
				<script src="/js/funciones.js"/>
				<script language="JavaScript">
	  				function Buscar()
	 				{
	 					document.formulario.accion.value="buscar";
	 					document.formulario.submit();
	 				}
	
					function modificar(valor)
					{
						document.formulario.action="updUsr";
						document.formulario.upd_id_usuario.value=valor;
						document.formulario.submit();
					}
					function Eliminar()
					{
						if(document.formulario.del_id_usuario.value!="")
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
					function Agregar()
					{
						document.formulario.action="addUsr";
						document.formulario.submit();
					}
				</script>
			</head>
			<body>
				<xsl:sequence select="portal:editor(Cabecera/DatosConf/editor,Cabecera/pagina,Cabecera/Parametros/idSession)"/>
				<form name="formulario" method="POST" action="lstUsr">
					<input name="accion" type="hidden"/>
					<input name="upd_id_usuario" type="hidden"/>
					<input name="pagina" value="1" type="hidden"/>
					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />
					<p>
						<div id="filtros" class="divtbl">
							<table>
								<tbody>
									<tr>
										<td class="td-h1">ID Usuario:</td>
										<td class="td-c1">
											<input type="text" name="id_usuario" />
										</td>
										<td class="td-h1">Nombre:</td>
										<td class="td-c1">
											<input type="text" name="nombre" />
										</td>
									</tr>
									<tr>
										<td class="td-h1">Perfil:</td>
										<td class="td-c0">
											<select name="perfil">
												<option value="">Todos</option>
												<xsl:for-each select="Cuerpo/listaPerfil/fila">
													<option value="{id_perfil}"><xsl:value-of select="perfil"/></option>
												</xsl:for-each>
											</select>
										</td>
										<td class="td-h1"></td>
										<td class="td-c0">
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
							<table id="tablaContenido">
								<thead>
									<tr>
										<th></th>
										<th>ID Usuario</th>
										<th>Nombre</th>
										<th>Perfil</th>
									</tr>
								</thead>
								<tbody>
									<xsl:choose>
										<xsl:when test="Cuerpo/listaUsuario/@cantidad != '0'">
											<xsl:for-each select="Cuerpo/listaUsuario/fila">
												<tr class="td-c{(position() mod 2)}">
													<td>
														<input type="radio" name="del_id_usuario" value="{id_usuario}"/>
													</td>
													<td>
														<xsl:value-of select="id_usuario" />
													</td>
													<td >
														<a href="#" onclick="modificar('{id_usuario}');">
															<xsl:value-of select="nombre_usuario" />
														</a>
													</td>
													<td>
														<xsl:value-of select="perfil" />
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
 
								<xsl:if test="Cuerpo/listaUsuario/@cantidad != '0'">
									<xsl:value-of select="portal:paginacion(Cuerpo/TOTAL_REGISTROS, Cabecera/DatosConf/registrosPorPagina, Cabecera/Parametros/pagina)"/>
								</xsl:if>							
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