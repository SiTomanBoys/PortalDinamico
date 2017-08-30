<?xml version="1.0" encoding="UTF-8"?>
<!-- Toda hoja de transformacion comineza con este tag -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Indicamos que nuestro output sera un tipo HTML -->
	<xsl:output method = "html" />
	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->
	<xsl:template match="/Documento">
		<html>
			<head>
				<title>
					<xsl:value-of select="Titulo" />
				</title>
				<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" />
				<meta http-equiv='cache-control' content='no-cache' />
				<meta http-equiv='pragma' content='no-cache' />
				<meta http-equiv='expires' content='1' />
				<link type="text/css" href="/css/estilo.css" rel="stylesheet" />
			</head>	
			<body>
				<div id="central_home">
					<div id="central_inner">
						<ul>
                    Bienvenido al portal dinamico<br/>
							<br/>
							<li>Seleccione una <strong>opcion</strong> del menu izquierdo.</li>
							<li> Para consultas o dudas, escribanos a <a href="mailto:contacto@d-portal.cl">contacto@d-portal.cl</a>
							</li>
						</ul>
					</div>
				</div>        
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>