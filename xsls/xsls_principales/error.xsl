<?xml version="1.0" encoding="UTF-8"?>
<!-- Toda hoja de transformacion comineza con este tag -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Indicamos que nuestro output sera un tipo HTML -->
	<xsl:output method = "html" />
	<xsl:param name="Titulo" />
	<xsl:param name="codError" />
	<xsl:param name="dscError" />
	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->
	<xsl:template match="/Documento"> 		&lt;!DOCTYPE html&gt; 		<!--[if lt IE 7 ]> <html lang="en" class="ie6 ielt8"> <![endif]-->
		<!--[if IE 7 ]>    <html lang="en" class="ie7 ielt8"> <![endif]-->
		<!--[if IE 8 ]>    <html lang="en" class="ie8"> <![endif]-->
		<!--[if (gte IE 9)|!(IE)]><!-->
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
				<link rel="stylesheet" type="text/css" href="/css/tabla.css"/>
				<link rel="stylesheet" type="text/css" href="/css/estilo.css"/>
				<title>
					<xsl:value-of select="$Titulo"/>
				</title>
			</head>
			<body>
				<div id="filtros" class="divtbl">
					<table>
						<tbody>
							<tr>
								<td class="td-h1">Error <xsl:value-of select="$codError"/>
								</td>
							</tr>
							<tr>
								<td class="td-c1">
									<xsl:value-of select="$dscError"/>
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
												<a href="/Portal/logout" target="_top">
													<span>Aceptar</span>
												</a>
											</li>
										</ul>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>