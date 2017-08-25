<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet [
<!ENTITY nbsp "&#160;">
<!ENTITY bull "&#8226;">
<!ENTITY deg "&#176;">
<!ENTITY copy "&#169;">
<!ENTITY reg "&#174;">
<!ENTITY trade "&#8482;">
<!ENTITY mdash "&#8212;">
<!ENTITY ldquo "&#8220;">
<!ENTITY rdquo "&#8221;">
<!ENTITY pound "&#163;">
<!ENTITY yen "&#165;">
<!ENTITY euro "&#8364;">
<!ENTITY aacute "&#225;">
<!ENTITY Aacute "&#193;">
<!ENTITY eacute "&#233;">
<!ENTITY Eacute "&#201;">
<!ENTITY iacute "&#237;">
<!ENTITY Iacute "&#205;">
<!ENTITY oacute "&#243;">
<!ENTITY Oacute "&#211;">
<!ENTITY uacute "&#250;">
<!ENTITY Uacute "&#218;">
<!ENTITY ntilde "&#241;">
<!ENTITY Ntilde "&#209;">
<!ENTITY iquest "&#191;">
<!ENTITY br "<![CDATA[<xsl:text disable-output-escaping='yes'>&lt;br&gt;</xsl:text>]]>
">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:portal="http://d-portal.cl" version="2.0">
	<xsl:variable name="xml-response" select="/Documento"/>
	<xsl:function name="portal:editor">
		<xsl:param name="modo" />
		<xsl:param name="idXsl" />
		<xsl:if test="$modo = 'online'">
			<![CDATA[
			<textarea id="xml-response" style="display:none;" name="xml-response">]]><xsl:copy-of select="$xml-response"/><![CDATA[</textarea>
			<span class="spanbtn copiar-xml">
				<a href="#" onclick="CopyToClipboard(document.getElementById('xml-response').value);return false;">Copiar XML</a>
			</span>
			<span class="spanbtn copiar-xml">
				<a href="/MantenedorXSL/updXSL?id_xsl=]]><xsl:value-of select="$idXsl"/><![CDATA[" target="_blank">Editar XSL</a>
			</span>
			]]>
		</xsl:if>
	</xsl:function>
	<xsl:function name="portal:paginacion">
		<xsl:param name="totalRegistros" />
		<xsl:param name="registrosPorPagina" />
		<xsl:param name="pagina" />
		<xsl:variable name="contador">
			<xsl:number value="ceiling($totalRegistros div $registrosPorPagina)"/>
		</xsl:variable>
		<xsl:if test="$contador &gt; 1">
			<![CDATA[
			<tfoot>
				<tr>
					<td colspan="3" >
						<div id="paging">
							<ul class="ul-der">
								<li>]]>
									<xsl:choose>
										<xsl:when test="$contador &lt; 6">
											<xsl:for-each select="1 to $contador">
													<![CDATA[
													<a href="#" onclick="paginar(']]><xsl:value-of select="position()"/><![CDATA[','buscar');">
														<span>]]><xsl:value-of select="position()"/><![CDATA[</span>
													</a>]]>
											</xsl:for-each>
										</xsl:when>
										<xsl:otherwise>
											<xsl:variable name="pag">
												<xsl:number value="$pagina"/>
											</xsl:variable>
											<xsl:variable name="pagSig">
												<xsl:number value="$pag + 1"/>
											</xsl:variable>
											<xsl:choose>
												<xsl:when test = "$pag = 1">
													<xsl:for-each select="1 to 4">
														<![CDATA[
													<a href="#" onclick="paginar(']]><xsl:value-of select="position()"/><![CDATA[','buscar');">
														<span>]]><xsl:value-of select="position()"/><![CDATA[</span>
													</a>]]>
													</xsl:for-each>
													<![CDATA[
													<a href="#" onclick="paginar(']]><xsl:value-of select="$pagSig"/><![CDATA[','buscar');">
														<span>]]><xsl:value-of select="'&gt;'"/><![CDATA[</span>
													</a>
													]]>
												</xsl:when>
												<xsl:when test="$pag &gt; 1 and $pag &lt; $contador - 2 ">
													<xsl:variable name="pagAnt">
														<xsl:number value="$pag - 1"/>
													</xsl:variable>
													<![CDATA[
													<a href="#" onclick="paginar(']]><xsl:value-of select="$pagAnt"/><![CDATA[','buscar');">
														<span>]]><xsl:value-of select="'&lt;'"/><![CDATA[</span>
													</a>]]>
													<xsl:for-each select="1 to 3">
														<xsl:variable name ="pagAct" select="$pag + position() - 1"/>
														<![CDATA[
														<a href="#" onclick="paginar(']]><xsl:value-of select="$pagAct"/><![CDATA[','buscar');">
															<span>]]><xsl:value-of select="$pagAct"/><![CDATA[</span>
														</a>]]>
														
													</xsl:for-each>
													<![CDATA[
													<a href="#" onclick="paginar(']]><xsl:value-of select="$pagSig"/><![CDATA[','buscar');">
														<span>]]><xsl:value-of select="'&gt;'"/><![CDATA[</span>
													</a>]]>
												</xsl:when>
												<xsl:otherwise>
													<xsl:variable name="pagAnt">
														<xsl:number value="$pag - 1"/>
													</xsl:variable>
													<![CDATA[
													<a href="#" onclick="paginar(']]><xsl:value-of select="$pagAnt"/><![CDATA[','buscar');">
														<span>]]><xsl:value-of select="'&lt;'"/><![CDATA[</span>
													</a>]]>
													<xsl:for-each select="1 to 4">
														<xsl:variable name ="pagAct" select="$contador + position() - 4"/>
														<![CDATA[
														<a href="#" onclick="paginar(']]><xsl:value-of select="$pagAct"/><![CDATA[','buscar');">
															<span>]]><xsl:value-of select="$pagAct"/><![CDATA[</span>
														</a>]]>
													</xsl:for-each>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>
									<![CDATA[
								</li>
							</ul>
						</div>
					</td>
				</tr>
			</tfoot>
			]]>
		</xsl:if>
	</xsl:function>
</xsl:stylesheet>
