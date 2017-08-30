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

			</head>
                      
			<body>
				<form>
					<div class="logocabecera">
                          </div>
					<div class="userdatamenu" style="float:right;margin-right:50px; margin-top:10px;">
						<br /> <a href='logout' target="_top">Cerrar sesión</a>
						<br />
					</div>
				</form>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>