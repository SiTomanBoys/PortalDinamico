<?xml version="1.0" encoding="UTF-8"?>
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
				<link rel="shortcut icon" href="../img/icon.ico"/>
			</head>
			<frameset rows="100,*,30" cols="*" frameborder="NO" border="0" framespacing="0">
				<frame src="cabecera" name="cabeceraServlet" scrolling="NO" noresize="noresize" />
				<frameset name="frameset2" id="frameset2" rows="*" cols="240,20,*" framespacing="0" frameborder="NO" border="0">
					<frame src="menu" name="menu" scrolling="AUTO" noresize="noresize" />
					<frame src="../abrircerrarmenu.html" name="openclosemenu" id="openclosemenu" scrolling="NO" noresize="noresize" />
					<frame src="central" name="central" />
				</frameset>
				<frame src="pie" name="pie" scrolling="NO" noresize="noresize" />
			</frameset>
			<noframes>
				<body/>
			</noframes>
		</html>
	</xsl:template>
</xsl:stylesheet>