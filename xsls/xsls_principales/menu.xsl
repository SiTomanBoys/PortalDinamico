<!-- Toda hoja de transformacion comineza con este tag -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Indicamos que nuestro output sera un tipo HTML -->
	<xsl:output method = "html" />
	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->
	<xsl:template match="/Documento">
		<html>
			<head>
				<link rel="stylesheet" type="text/css" href="/css/menu.css"/>
				<link rel="stylesheet" type="text/css" href="/css/estilo.css"/>
				<script language="JavaScript">
				function expandir(id) 
				{
					if (document.getElementById(id).className=="mabierto"){
						document.getElementById(id).className="mcerrado";
						document.getElementsByTagName('li')[id].className="mabierto";
					}else{
						document.getElementById(id).className="mabierto";
						document.getElementsByTagName('li')[id].className="mcerrado";
					}
				}
 				function CopyToClipboard(text)
 				{
 					window.prompt("Copiar Xml al portapapeles: Ctrl+C, Enter", text);
 				}
				</script>
			</head>
			<body>
				<ul class="menu">
					<xsl:copy-of select="Menu/*"/>
				</ul>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>