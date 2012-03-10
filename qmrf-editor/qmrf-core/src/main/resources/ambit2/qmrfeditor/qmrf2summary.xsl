<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                 xmlns="http://www.w3.org/1999/xhtml">
<!-- start xslt -->
<xsl:template match="/">
  <xsl:apply-templates/> 
</xsl:template>

<xsl:template match="QMRF">
  <xsl:apply-templates select="QMRF_chapters"/>
  
</xsl:template>

<xsl:template match="QMRF_chapters">
    
    <div id='tabs-3'>
    <div  class='summary'>
    	<xsl:apply-templates select="QSAR_Endpoint"/>	
    </div>
    </div>
   
    <div id='tabs-4'>
    <div  class='summary'>
    	<xsl:apply-templates select="QSAR_Algorithm"/>	
    </div>
    </div>
       
    <div id='tabs-5'>
    <div  class='summary'>
    	<xsl:apply-templates select="QSAR_Applicability_domain"/>	
    </div>
    </div>       
 
       
    <div id='tabs-6'>
    <div  class='summary'>
    	<xsl:apply-templates select="QSAR_Robustness"/>	
    </div>
    </div>    
    
    <div id='tabs-7'>
    <div  class='summary'>
    	<xsl:apply-templates select="QSAR_Predictivity"/>	
    </div>
    </div>      
           
    <div id='tabs-8'>
    <div  class='summary'>
    	<xsl:apply-templates select="QSAR_Interpretation"/>	
    </div>
    </div>
    
    <div id='tabs-9'>
    <div  class='summary'>
    	<xsl:apply-templates select="QSAR_Miscelaneous"/>	
    </div>
    </div>
        
</xsl:template>

<xsl:template match="QSAR_identifier">
  <xsl:apply-templates select="QSAR_title"/>	
</xsl:template>

<xsl:template match="QSAR_title">
  
  <xsl:value-of select="."/>
  		
</xsl:template>
<!-- 3. Endpoint  -->
<xsl:template match="QSAR_Endpoint">
  <label><xsl:value-of select="@name" /></label>
   <br/><br/>
  <xsl:apply-templates select="model_species"/>
   <br/><br/>
  <xsl:apply-templates select="model_endpoint"/>
   <br/><br/>
  <xsl:apply-templates select="endpoint_comments"/>
   <br/><br/>
  <xsl:apply-templates select="endpoint_units"/>
   <br/><br/>		
  <xsl:apply-templates select="endpoint_variable"/>
   <br/><br/>
  <xsl:apply-templates select="endpoint_protocol"/>
   <br/><br/>
  <xsl:apply-templates select="endpoint_data_quality"/>		
</xsl:template>

<xsl:template match="model_species">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>		
</xsl:template>

<xsl:template match="model_endpoint">
  <label><xsl:value-of select="@name" /></label>
   <xsl:apply-templates select="endpoint_ref"/>
</xsl:template>

<xsl:template match="endpoint_units">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>		
</xsl:template>


<xsl:template match="endpoint_comments">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>		
</xsl:template>

<xsl:template match="endpoint_units">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>		
</xsl:template>

<xsl:template match="endpoint_protocol">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>		
</xsl:template>

<xsl:template match="endpoint_data_quality">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>		
</xsl:template>

<!-- 4. Algorithm -->
<xsl:template match="QSAR_Algorithm">
  <label><xsl:value-of select="@name" /></label>
  <br/><br/>
  <xsl:apply-templates select="algorithm_type"/>
   <br/><br/>
  <xsl:apply-templates select="descriptors_generation"/>
   
</xsl:template>

<xsl:template match="algorithm_type">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>		
  <br/>
</xsl:template>

<xsl:template match="descriptors_generation">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>

<!-- 5. App domain -->
<xsl:template match="QSAR_Applicability_domain">
  <label><xsl:value-of select="@name" /></label>
  <br/><br/>
  <xsl:apply-templates select="app_domain_description"/>
  <br/><br/>
  <xsl:apply-templates select="app_domain_method"/>
   
</xsl:template>

<xsl:template match="app_domain_method">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>
 
<xsl:template match="app_domain_description">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>

<!--  6. Robustness -->

<xsl:template match="QSAR_Robustness">
  <label><xsl:value-of select="@name" /></label>
  <br/>
  <xsl:apply-templates select="goodness_of_fit"/>
  <br/>
  <xsl:apply-templates select="loo"/>
  <br/>
  <xsl:apply-templates select="lmo"/>
  <br/>
  <xsl:apply-templates select="yscrambling"/>
  <br/>
  <xsl:apply-templates select="bootstrap"/>
  <br/>
  <xsl:apply-templates select="other_statistics"/>
  <br/>
  <xsl:apply-templates select="other_info"/>
  <br/>
  <xsl:apply-templates select="preprocessing"/>
  <br/>
  <xsl:apply-templates select="training_set_data"/>
   
</xsl:template>


<xsl:template match="goodness_of_fit">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>

<xsl:template match="loo">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>
<xsl:template match="lmo">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>
<xsl:template match="yscrambling">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>
<xsl:template match="bootstrap">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>
<xsl:template match="other_statistics">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>
<xsl:template match="other_info">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>

<xsl:template match="preprocessing">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>


<!--  7. Predictivity -->

<xsl:template match="QSAR_Predictivity ">
  <label><xsl:value-of select="@name" /></label>
  <br/>
  <xsl:apply-templates select="experimental_design"/>
  <br/>
  <xsl:apply-templates select="validation_predictivity "/>
  <br/>
  <xsl:apply-templates select="validation_assessment"/>
  <br/>
  <xsl:apply-templates select="validation_comments"/>
  <br/>
  <xsl:apply-templates select="validation_other_info"/>
  <br/>
  <xsl:apply-templates select="validation_set_data"/>

   
</xsl:template>

<xsl:template match="validation_predictivity">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>

<xsl:template match="validation_assessment">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>


<xsl:template match="validation_comments">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>

<xsl:template match="validation_other_info">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>

<xsl:template match="experimental_design">
  <label><xsl:value-of select="@name" /></label>
  <xsl:value-of select="."/>	
  <br/>	
</xsl:template>

<!-- 8. Interpretation -->

<xsl:template match="QSAR_Interpretation">
  <label><xsl:value-of select="@name" /></label>
  <xsl:apply-templates select="mechanistic_basis"/>
</xsl:template>

<xsl:template match="mechanistic_basis">
  <xsl:value-of select="."/>		
</xsl:template>

 <xsl:template name="print_href">
	 		<a>
			<xsl:attribute name="href">
			<xsl:value-of select="@url" />
			</xsl:attribute>
			<xsl:value-of select="@url"/>
			</a> 
 </xsl:template>
 
<xsl:template match="endpoint_ref"> 
	<xsl:value-of select="id(@idref)/@name"/>
</xsl:template>

<!-- 9. Interpretation -->

<xsl:template match="QSAR_Miscelaneous">
  <label><xsl:value-of select="@name" /></label>
  <xsl:apply-templates select="comments"/>
</xsl:template>

<xsl:template match="comments">
  <xsl:value-of select="."/>		
</xsl:template>

<!--  -->

<xsl:template match="training_set_data"> 
    <label><xsl:value-of select="training_set_availability/@name"/></label>
    <xsl:value-of select="training_set_availability/@answer"/>
	<label><xsl:value-of select="@name" /></label>
	<ul>
	<li><b>Chemname:</b><xsl:text> </xsl:text> <xsl:value-of select="@chemname"/></li>				
	<li><b>SMILES:</b><xsl:text> </xsl:text><xsl:value-of select="@smiles"/></li>				
	<li><b>CAS RN:</b><xsl:text> </xsl:text><xsl:text> </xsl:text><xsl:value-of select="@cas"/></li>	
	<li><b>InChI:</b><xsl:text> </xsl:text><xsl:value-of select="@inchi"/></li>	
	<li><b>MOL file:</b><xsl:text> </xsl:text><xsl:value-of select="@mol"/></li>	
	<li><b>Formula:</b><xsl:text> </xsl:text><xsl:value-of select="@formula"/></li>	
	</ul>
</xsl:template> 	

<xsl:template match="validation_set_data"> 
    <label><xsl:value-of select="validation_set_availability/@name"/></label>
    <xsl:value-of select="validation_set_availability/@answer"/>
	 <label><xsl:value-of select="@name" /></label>
	<ul>
	<li><b>Chemname:</b><xsl:text> </xsl:text><xsl:value-of select="@chemname"/>	</li>			
	<li><b>SMILES:</b><xsl:text> </xsl:text><xsl:value-of select="@smiles"/>	</li>				
	<li><b>CAS RN:</b><xsl:text> </xsl:text><xsl:value-of select="@cas"/></li>	
	<li><b>InChI:</b><xsl:text> </xsl:text><xsl:value-of select="@inchi"/></li>	
	<li><b>MOL file:</b><xsl:text> </xsl:text><xsl:value-of select="@mol"/>	</li>
	<li><b>Formula:</b><xsl:text> </xsl:text><xsl:value-of select="@formula"/>	</li>
	</ul>
</xsl:template> 	
<!-- start xslt -->
 </xsl:stylesheet>


	