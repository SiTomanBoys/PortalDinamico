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
						window.location.href = "lstPerfil?idSession=<xsl:value-of select='Cabecera/Parametros/idSession'/>";
					}
					function Modificar()
					{
						if(document.formulario.perfil.value =="")
						{	
							alert("Debe ingresar un perfil");
							return;
						}
						if(document.formulario.descripcion.value =="-1")
						{	
							alert("Debe ingresar una descripcion");
							return;
						}
						document.formulario.accion.value="modificar";
						document.formulario.submit();
					}
				</script>
			</head>
			<body>
				<xsl:sequence select="portal:editor(Cabecera/DatosConf/editor,Cabecera/pagina,Cabecera/Parametros/idSession)"/>
				<form name="formulario" method="POST" action="updMenu">
					<input name="accion" type="hidden"/>
					<input name="id_perfil" type="hidden" value = "{Cabecera/Parametros/upd_id_perfil}"/>
					<input name="idSession" type="hidden" value="{Cabecera/Parametros/idSession}" />
					<p>
						<div class="divtbl">
							<table>
								<tr>
									<td class="td-h1" >Nombre Perfil:</td>
									<td class="td-c1">
										<input type="text" name="perfil" value="{Cuerpo/perfil/perfil}"/>
									</td>
								</tr>
								<tr>
									<td class="td-h1" >Descripcion</td>
									<td class="td-c0">
										<input type="text" name="perfil" value="{Cuerpo/perfil/descripcion}"/>
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
														<a href="#" onclick="Modificar();">
															<span>Modificar</span>
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