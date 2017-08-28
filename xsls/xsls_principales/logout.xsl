<!-- Toda hoja de transformacion comineza con este tag -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Indicamos que nuestro output sera un tipo HTML -->
	<xsl:output method = "html" />
	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->
	<xsl:template match="/Documento">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
				<title><xsl:value-of select="Titulo" /></title>
				<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE"/>
				<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"/>
				<META HTTP-EQUIV="EXPIRES" CONTENT="1"/>
			</head>
			<body onload="window.location.href='{URL}'">
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>