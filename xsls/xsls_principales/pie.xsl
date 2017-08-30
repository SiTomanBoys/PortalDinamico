<?xml version="1.0" encoding="UTF-8"?>
<!-- Toda hoja de transformacion comineza con este tag -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Indicamos que nuestro output sera un tipo HTML -->
	<xsl:output method = "html" />
	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->
	<xsl:template match="/Documento">
		<html>
			<head>
			<title><xsl:value-of select="Titulo" /></title>
            	<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" />
				<meta http-equiv='cache-control' content='no-cache' />
				<meta http-equiv='pragma' content='no-cache' />
				<meta http-equiv='expires' content='1' />
				<link type="text/css" href="/css/estilo.css" rel="stylesheet" />
				<style>
					.footer {
						background-color: #C1C1C1;
						color: #000000;
						border-top-style: solid;
						border-top-width: 1px;
						border-top-color: #999999;
					}
				</style>
			</head>
			
            <body>
				<table cellpadding="0" cellspacing="0" border="0" width="100%" class="footer" height="32">
					<tr>
						<td align="center">d-portal | &#169;2017. Todos los derechos reservados</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>