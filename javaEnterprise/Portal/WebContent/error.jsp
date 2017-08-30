<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
		<link rel="stylesheet" type="text/css" href="/css/tabla.css"/>
		<link rel="stylesheet" type="text/css" href="/css/estilo.css"/>
		<title>
			<xsl:value-of select="Titulo"/>
		</title>
	</head>
	<body>
		<div id="filtros" class="divtbl">
			<table>
				<tbody>
					<tr>
						<td class="td-h1">Error <%= request.getAttribute("codError")%>
						</td>
					</tr>
					<tr>
						<td class="td-c1">
							<%= request.getAttribute("dscError")%>
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