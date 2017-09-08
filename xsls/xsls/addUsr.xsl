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
						window.location.href = "lstUsr?idSession=<xsl:value-of select='Cabecera/Parametros/idSession'/>";
					}
					function Agregar()
					{
						if(document.formulario.nombre_usuario.value =="")
						{	
							alert("Debe ingresar un usuario");
							return;
						}
						if(document.formulario.contra_usuario.value =="")
						{	
							alert("Debe ingresar una contrasena");
							return;
						}
						if(document.formulario.perfil.value =="-1")
						{	
							alert("Debe seleccionar un perfil");
							return;
						}
						document.formulario.accion.value="agregar";
						document.formulario.submit();
					}
				</script>
			</head>
			<body>
				<xsl:sequence select="portal:editor(Cabecera/DatosConf/editor,Cabecera/pagina,Cabecera/Parametros/idSession)"/>
				<form name="formulario" method="POST" action="addUsr">
					<input name="accion" type="hidden"/>
					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />
					<p>
						<div class="divtbl">
							<table>
								<tr>
									<td class="td-h1" >Nombre Usuario:</td>
									<td class="td-c1">
										<input type="text" name="nombre_usuario"/>
									</td>
								</tr>
								<tr>
									<td class="td-h1" >Contrasena:</td>
									<td class="td-c0">
										<input type="password" name="contra_usuario"/>
									</td>
								</tr>
								<tr>
									<td class="td-h1" >Perfil</td>
									<td class="td-c1">
										<select name="perfil">
											<option value="-1">Todos</option>
											<xsl:for-each select="Cuerpo/listaPerfil/fila">
												<option value="{id_perfil}"><xsl:value-of select="perfil"/></option>
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