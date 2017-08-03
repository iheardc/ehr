<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	
	
    <xsl:output method="xml"/>

	<xsl:template match="/">
		<html>
		<body>
			<table border="1">
			<xsl:for-each select="/metadata">
			<tr bgcolor="#3399ff">
				<xsl:if test="Title != ''">
					<th style="text-align:left">Title</th>
				</xsl:if>	
				<xsl:if test="Creator != ''">
					<th style="text-align:left">Creator</th>
				</xsl:if>
				<xsl:if test="Subject != ''">
					<th style="text-align:left">Subject</th>
				</xsl:if>
				<xsl:if test="Description != ''">
					<th style="text-align:left">Description</th>
				</xsl:if>	
				<xsl:if test="Publisher != ''">
					<th style="text-align:left">Publisher</th>
				</xsl:if>
				<xsl:if test="Contributor != ''">
					<th style="text-align:left">Contributor</th>
				</xsl:if>
				<xsl:if test="Type != ''">
					<th style="text-align:left">Type</th>
				</xsl:if>	
				<xsl:if test="Format != ''">
					<th style="text-align:left">Format</th>
				</xsl:if>
				<xsl:if test="Identifier != ''">
					<th style="text-align:left">Identifier</th>
				</xsl:if>
				<xsl:if test="Source != ''">
					<th style="text-align:left">Source</th>
				</xsl:if>	
				<xsl:if test="Language != ''">
					<th style="text-align:left">Language</th>
				</xsl:if>
				<xsl:if test="Relation != ''">
					<th style="text-align:left">Relation</th>
				</xsl:if>	
				<xsl:if test="Coverage != ''">
					<th style="text-align:left">Coverage</th>
				</xsl:if>	
				<xsl:if test="Date != ''">
					<th style="text-align:left">Date</th>
				</xsl:if>
				<xsl:if test="Rights != ''">
					<th style="text-align:left">Rights</th>
				</xsl:if>							
			</tr>
			
				<tr>
				<xsl:if test="Title != ''">
					<td><xsl:value-of select="Title"/></td>
				</xsl:if>	
				<xsl:if test="Creator != ''">
					<td><xsl:value-of select="Creator"/></td>
				</xsl:if>
				<xsl:if test="Subject != ''">
					<td><xsl:value-of select="Subject"/></td>
				</xsl:if>
				<xsl:if test="Description != ''">
					<td><xsl:value-of select="Description"/></td>
				</xsl:if>	
				<xsl:if test="Publisher != ''">
					<td><xsl:value-of select="Publisher"/></td>
				</xsl:if>
				<xsl:if test="Contributor != ''">
					<td><xsl:value-of select="Contributor"/></td>
				</xsl:if>
				<xsl:if test="Type != ''">
					<td><xsl:value-of select="Type"/></td>
				</xsl:if>	
				<xsl:if test="Format != ''">
					<td><xsl:value-of select="Format"/></td>
				</xsl:if>
				<xsl:if test="Identifier != ''">
					<td><xsl:value-of select="Identifier"/></td>
				</xsl:if>
				<xsl:if test="Source != ''">
					<td><xsl:value-of select="Source"/></td>
				</xsl:if>	
				<xsl:if test="Language != ''">
					<td><xsl:value-of select="Language"/></td>
				</xsl:if>
				<xsl:if test="Relation != ''">
					<td><xsl:value-of select="Relation"/></td>
				</xsl:if>	
				<xsl:if test="Coverage != ''">
					<td><xsl:value-of select="Coverage"/></td>
				</xsl:if>	
				<xsl:if test="Date != ''">
					<td><xsl:value-of select="Date"/></td>
				</xsl:if>
				<xsl:if test="Rights != ''">
					<td><xsl:value-of select="Rights"/></td>
				</xsl:if>
				
				</tr>
				</xsl:for-each>
				</table>
				<br/>
			</body>
		</html>
	</xsl:template>


</xsl:stylesheet>
