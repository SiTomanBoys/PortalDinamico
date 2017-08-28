<?xml version="1.0" encoding="UTF-8"?>
<!-- Toda hoja de transformacion comineza con este tag -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Indicamos que nuestro output sera un tipo HTML -->
	<xsl:output method = "html" />
	<!-- Usamos Xpath para comentar que queremos parsear todo el xml -->
	<xsl:template match="/Documento">
		&lt;!DOCTYPE html&gt;
		<!--[if lt IE 7 ]> <html lang="en" class="ie6 ielt8"> <![endif]-->
		<!--[if IE 7 ]>    <html lang="en" class="ie7 ielt8"> <![endif]-->
		<!--[if IE 8 ]>    <html lang="en" class="ie8"> <![endif]-->
		<!--[if (gte IE 9)|!(IE)]><!-->
		<html lang="en">
			<!--<![endif]-->
			<head>
				<meta charset="utf-8"/>
				<title>Un Login para el Portal</title>
				<link rel="stylesheet" type="text/css" href="css/LoginCSS.css" />
			</head>
			<body>
				<div class="container">
					<section id="content">
						<form method="POST" action="login">
							<h1>El Terrible Login</h1>
							<div>
								<label align="left" id="lblLogin">Usuario :</label>
								<input type="text" placeholder="usuario" required="" id="usuario" name="txtUsuario" />
								<br/>
							</div>
							<div>
								<label align="left" id="lblLogin">Contraseña :</label>
								<input type="password" placeholder="Password" required="" id="password" name="txtPassword"/>
								<br/>
							</div>
							<div>
								<input align="right" type="submit" value="Entra Aquí 1313" />
							</div>
						</form>
						<!-- form -->
					</section>
					<!-- content -->
				</div>
				<!-- container -->
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>