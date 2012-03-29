<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                 xmlns="http://www.w3.org/1999/xhtml">
<xsl:output method="html" encoding="utf-8" version="" indent="yes" standalone="no" media-type="text/html" omit-xml-declaration="no" doctype-system="about:legacy-compat" />
        
<!-- start xslt -->
<xsl:template match="endpoints_catalog">
  <div class="ui-widget-content ui-corner-all"  style="width:90%;align:center">
	  <xsl:text>&#10;</xsl:text>
	  <p>
	  <!--  table -->
	  		<table>
			<xsl:attribute name="id"><xsl:value-of select="@id" disable-output-escaping="yes"/></xsl:attribute>
			<xsl:attribute name="class">display</xsl:attribute>
			<xsl:attribute name="class">endpoints</xsl:attribute>
			<xsl:attribute name="cellpadding">0</xsl:attribute>
			<xsl:attribute name="border">0</xsl:attribute>
			<xsl:attribute name="width">100%</xsl:attribute>
			<xsl:attribute name="cellspacing">0</xsl:attribute>

		  <xsl:text>&#10;</xsl:text>
		  <caption><h3><xsl:value-of select="@name"/></h3><xsl:call-template name="print_href"/></caption>
		  <xsl:text>&#10;</xsl:text>
		  <thead> 
		 	<th width='25%'>Group</th>
		 	<th width='15%'>Subgroup</th>
		 	<th width='51%'>Name</th>
		 	<th width='3%'></th>
		 	<th width='3%'>OECD No.</th>
		 	<th width='3%'>ECHA No.</th>

		   </thead>  
		   <xsl:text>&#10;</xsl:text>
		  <tbody> 	
		  <xsl:text>&#10;</xsl:text>
		  <xsl:apply-templates/>
		  </tbody>
		  <xsl:text>&#10;</xsl:text>
		  </table>
		  <xsl:text>&#10;</xsl:text>
    </p>
    </div>
    <xsl:text>&#10;</xsl:text>

</xsl:template>


<xsl:template match="catalog">

	  <xsl:text>&#10;</xsl:text>
	  <p>
	  <!--  table -->
	  		<table>
			<xsl:attribute name="class">display</xsl:attribute>
			<xsl:attribute name="class">endpoints</xsl:attribute>
			<xsl:attribute name="cellpadding">0</xsl:attribute>
			<xsl:attribute name="border">0</xsl:attribute>
			<xsl:attribute name="width">100%</xsl:attribute>
			<xsl:attribute name="cellspacing">0</xsl:attribute>

		  <xsl:text>&#10;</xsl:text>
		  <caption><h3>Endpoints/ Test guidelines list comparison</h3>
		  <div>COUNCIL REGULATION (EC) No 440/2008-30 May 2008 (<u>EC</u>)</div> 
		  <div>OECD GUIDELINES FOR TESTING OF CHEMICALS-Sep 2009 (<u>OECD</u>)</div> 
		  <div>QMRF Endpoints list (<u>QMRF</u>)</div>
		  </caption>
		  <xsl:text>&#10;</xsl:text>
		  <thead> 
		 	<th width='25%'>Group</th>
		 	<th width='15%'>Subgroup</th>
		 	<th width='51%'>Name</th>
		 	<th width='3%'>List</th>
		 	<th width='3%'>OECD No.</th>
		 	<th width='3%'>ECHA No.</th>
		   </thead>  
		   <xsl:text>&#10;</xsl:text>
		  <tbody> 	
		  <xsl:text>&#10;</xsl:text>
		  <xsl:apply-templates/>
		  </tbody>
		  <xsl:text>&#10;</xsl:text>
		  </table>
		  <xsl:text>&#10;</xsl:text>
    </p>

    <xsl:text>&#10;</xsl:text>
    
</xsl:template>

<xsl:template match="acatalog">
	  <xsl:text>&#10;</xsl:text>
		  <xsl:apply-templates/>
    <xsl:text>&#10;</xsl:text>
    
</xsl:template>
        
<!--catalogs -->
<xsl:template match="/">
  <html>
  <head>
  	<title><xsl:value-of select="@name"/></title>
  	<xsl:text>&#10;</xsl:text>
     <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
     <xsl:text>&#10;</xsl:text>
	<!-- jQuery -->
	<script type="text/javascript" charset="utf8" src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.7.1.min.js"></script>
	<xsl:text>&#10;</xsl:text>
	<!-- jQuery UI -->
	<script type="text/javascript" charset="utf8" src="http://ajax.microsoft.com/ajax/jquery.ui/1.8.5/jquery-ui.js"></script>
	<xsl:text>&#10;</xsl:text>
	<link type="text/css" rel="Stylesheet" href="http://ajax.microsoft.com/ajax/jquery.ui/1.8.5/themes/ui-lightness/jquery-ui.css" />
	<xsl:text>&#10;</xsl:text>
	<style type="text/css" media="screen">
			.dataTables_info { padding-top: 0; }
			.dataTables_paginate { padding-top: 0; }
			.css_right { float: right; }
		</style>	
	<xsl:text>&#10;</xsl:text>		
	<!-- DataTables -->
	<script type="text/javascript" charset="utf8" src="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.8.2/jquery.dataTables.min.js"></script>
	<xsl:text>&#10;</xsl:text>
    <link rel="stylesheet" type="text/css" href="http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.8.2/css/jquery.dataTables.css"/>
		<script type="text/javascript" charset="utf-8">
			$(document).ready(function() {
				$('.endpoints').dataTable({
					"bJQueryUI": true
					<!-- "sPaginationType": "full_numbers"  -->				
				});
		
			} );
		</script>
		<xsl:text>&#10;</xsl:text>
  </head>
  <xsl:text>&#10;</xsl:text>
  <body>
    	<xsl:apply-templates/>
	
  </body>
  </html> 
</xsl:template>

<xsl:template match="endpoint">
	<tr>
 	<td><xsl:value-of select="@group" /></td>
 	<td><i><xsl:value-of select="@subgroup" /></i></td>
 	<td><b><xsl:value-of select="@name" /></b></td>
 	<td><xsl:call-template name="print_catalog_header"/></td>
 	<td><xsl:value-of select="@oecdNo" /></td>
 	<td><xsl:value-of select="@echaNo" /></td>
 	</tr>
 	<xsl:text>&#10;</xsl:text>
</xsl:template>

<xsl:template name="print_catalog_header">
 <xsl:choose>
    <xsl:when test="../@url != ''">
	 		<a>
			<xsl:attribute name="href">
			<xsl:value-of select="../@url" disable-output-escaping="yes"/></xsl:attribute>
			<xsl:attribute name="title"><xsl:value-of select="../@name" disable-output-escaping="yes"/></xsl:attribute>
			<xsl:attribute name="target">_blank</xsl:attribute>
			<xsl:value-of select="../@id" disable-output-escaping="yes"/>
			</a> 
    </xsl:when>
    <xsl:otherwise></xsl:otherwise> <!-- default value -->
  </xsl:choose>

 </xsl:template>
 
<xsl:template name="print_href">
 <xsl:choose>
    <xsl:when test="@url != ''">
	 		<a>
			<xsl:attribute name="href">
			<xsl:value-of select="@url" disable-output-escaping="yes"/>
			</xsl:attribute>
			<xsl:attribute name="target">_blank</xsl:attribute>
			<xsl:value-of select="@url" disable-output-escaping="yes"/>
			</a> 
    </xsl:when>
    <xsl:otherwise></xsl:otherwise> <!-- default value -->
  </xsl:choose>

 </xsl:template>
<!-- end xslt -->
 </xsl:stylesheet>


	