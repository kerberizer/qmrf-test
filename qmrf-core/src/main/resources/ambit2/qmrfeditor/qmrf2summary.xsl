<?xml version="1.0" encoding="UTF-8"?>
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
 
    <div id='tabs-1'>
    <div  class='summary'>
    	<xsl:apply-templates select="QSAR_identifier"/>	
    </div>
    </div>
  
    <div id='tabs-2'>
    <div  class='summary'>
    	<xsl:apply-templates select="QSAR_General_information"/>	
    </div>
    </div>
        
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
  <h2><xsl:value-of select="@name" /></h2>
   <div class='summary'><p>
  <xsl:apply-templates select="QSAR_title"/>
  </p></div>

     <div class='summary'><p>
  <xsl:apply-templates select="QSAR_models"/>
  </p></div>
       <div class='summary'><p>
  <xsl:apply-templates select="QSAR_software"/>

  </p></div>

</xsl:template>

 <xsl:template match="QSAR_title">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>		
</xsl:template>

 <xsl:template match="QSAR_models">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>		
</xsl:template>


<xsl:template match="QSAR_software">
  <strong><xsl:value-of select="@name" /></strong>
<div class='summary'><p>
   <xsl:apply-templates select="software_ref"/>
   </p></div>
</xsl:template>


<!-- 2.General  -->
<!-- 2. General information  -->
<xsl:template match="QSAR_General_information">
  <h2><xsl:value-of select="@name" /></h2>
   <div class='summary'><p>
  <xsl:apply-templates select="qmrf_date"/>
  </p></div>
  

   <div class='summary'><p>
  <xsl:apply-templates select="qmrf_authors"/>
     </p></div>
     
   <div class='summary'><p>
  <xsl:apply-templates select="qmrf_date_revision"/>
    </p> </div>
    <div class='summary'><p>
  <xsl:apply-templates select="qmrf_revision"/>
   </p>  </div>
   <div>		<p>
  <xsl:apply-templates select="model_authors"/>
    </p></div>
    <div class='summary'><p>
  <xsl:apply-templates select="model_date"/>
    </p> </div>
     <div class='summary'><p>
  <xsl:apply-templates select="references"/>
   </p> </div>		
     <div class='summary'><p>
  <xsl:apply-templates select="info_availability"/>
   </p> </div>	   
    <div class='summary'><p>
  <xsl:apply-templates select="related_models"/>
   </p> </div>	    

 </xsl:template> 
 
 <xsl:template match="qmrf_date">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>		
</xsl:template>


<xsl:template match="qmrf_authors">
  <strong><xsl:value-of select="@name" /></strong>
   <table width='100%'>
  <tr><th width='20%'>Name</th><th width='20%'>Affiliation</th><th width='20%'>Contact</th><th width='20%'>e-mail</th><th>WWW</th></tr>
   <xsl:apply-templates select="author_ref"/>
   </table>
</xsl:template>

<xsl:template match="model_authors">

  <strong><xsl:value-of select="@name" /></strong>
   <table width='100%'>
   <tr><th width='20%'>Name</th><th width='20%'>Affiliation</th><th width='20%'>Contact</th><th width='20%'>e-mail</th><th>WWW</th></tr>
   <xsl:apply-templates select="author_ref"/>
    </table>
</xsl:template>


<xsl:template match="references">
  <strong><xsl:value-of select="@name" /></strong>
   <table width='100%'>
   <xsl:apply-templates select="publication_ref"/>
   </table>
</xsl:template>


<xsl:template match="qmrf_date_revision">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>		
</xsl:template>


<xsl:template match="qmrf_revision">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>		
</xsl:template>

<xsl:template match="model_date">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>		
</xsl:template>

<xsl:template match="info_availability">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="."  disable-output-escaping="yes"/>		
</xsl:template>



<xsl:template match="related_models">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="."  disable-output-escaping="yes"/>		
</xsl:template>

<!-- 3. Endpoint  -->
<xsl:template match="QSAR_Endpoint">
  <h2><xsl:value-of select="@name" /></h2>
   <div class='summary'><p>
  <xsl:apply-templates select="model_species"/>
  </p></div>
   <div class='summary'><p>
  <xsl:apply-templates select="model_endpoint"/>
     </p></div>
   <div class='summary'><p>
  <xsl:apply-templates select="endpoint_comments"/>
    </p> </div>
    <div class='summary'><p>
  <xsl:apply-templates select="endpoint_units"/>
   </p>  </div>
   <div>		<p>
  <xsl:apply-templates select="endpoint_variable"/>
    </p></div>
    <div class='summary'><p>
  <xsl:apply-templates select="endpoint_protocol"/>
    </p> </div>
     <div class='summary'><p>
  <xsl:apply-templates select="endpoint_data_quality"/>
   </p> </div>		
</xsl:template>

<xsl:template match="model_species">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>		
</xsl:template>

<xsl:template match="model_endpoint">
  <strong><xsl:value-of select="@name" /></strong>
   <xsl:text> </xsl:text>
   <xsl:apply-templates select="endpoint_ref"/>
</xsl:template>

<xsl:template match="endpoint_units">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>		
</xsl:template>


<xsl:template match="endpoint_comments">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>		
</xsl:template>

<xsl:template match="endpoint_units">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>		
</xsl:template>

<xsl:template match="endpoint_protocol">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="."  disable-output-escaping="yes"/>		
</xsl:template>

<xsl:template match="endpoint_variable">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="."  disable-output-escaping="yes"/>		
</xsl:template>

<xsl:template match="endpoint_data_quality">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="."  disable-output-escaping="yes"/>		
</xsl:template>

<!-- 4. Algorithm -->
<xsl:template match="QSAR_Algorithm">
  <h2><xsl:value-of select="@name" /></h2>
  <div class='summary'><p>
  <xsl:apply-templates select="algorithm_type"/>
  </p></div>
   <div class='summary'><p>
     <xsl:apply-templates select="algorithm_explicit"/>
   </p></div>
     <div class='summary'><p>
  <xsl:apply-templates select="equation"/>
  </p></div> 
    <div class='summary'><p>
  <xsl:apply-templates select="algorithms_descriptors"/>
  </p></div> 
    <div class='summary'><p>
  <xsl:apply-templates select="descriptors_selection"/>
  </p></div> 
      <div class='summary'><p>
  <xsl:apply-templates select="descriptors_generation"/>
  </p></div> 
    <div class='summary'><p>
  <xsl:apply-templates select="descriptors_generation_software"/>
  </p></div> 
  <div class='summary'><p>
  <xsl:apply-templates select="descriptors_chemicals_ratio"/>
  </p></div> 
</xsl:template>


<xsl:template match="algorithm_type">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>		

</xsl:template>

<xsl:template match="algorithm_explicit">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
  <xsl:apply-templates select="algorithm_ref"/>
</xsl:template>

<xsl:template match="algorithms_descriptors">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
  <xsl:apply-templates select="descriptor_ref"/>
</xsl:template>

<xsl:template match="descriptors_generation_software">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
  <xsl:apply-templates select="software_ref"/>
</xsl:template>


<xsl:template match="equation">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	

</xsl:template>

<xsl:template match="descriptors_generation">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	

</xsl:template>

<xsl:template match="descriptors_chemicals_ratio">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	

</xsl:template>


<!-- 5. App domain -->
<xsl:template match="QSAR_Applicability_domain">
  <h2><xsl:value-of select="@name" /></h2>
  <div class='summary'><p>
  <xsl:apply-templates select="app_domain_description"/>
  </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="app_domain_method"/>
   </p></div>
     <div class='summary'><p>
  <xsl:apply-templates select="app_domain_software"/>
   </p></div>
    <div class='summary'><p>
  <xsl:apply-templates select="applicability_limits"/>
   </p></div>
   
</xsl:template>

<xsl:template match="app_domain_method">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>
 
<xsl:template match="app_domain_description">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>

<xsl:template match="app_domain_software">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
  <xsl:apply-templates select="software_ref"/>
</xsl:template>

<xsl:template match="applicability_limits">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>

<!--  6. Robustness -->

<xsl:template match="QSAR_Robustness">
  <h2><xsl:value-of select="@name" /></h2>
  <div class='summary'><p>
  <xsl:apply-templates select="goodness_of_fit"/>
  </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="loo"/>
  </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="lmo"/>
  </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="yscrambling"/>
  </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="bootstrap"/>
  </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="other_statistics"/>
  </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="other_info"/>
  </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="preprocessing"/>
  </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="training_set_data"/>
   </p></div>
</xsl:template>


<xsl:template match="goodness_of_fit">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>

<xsl:template match="loo">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>
<xsl:template match="lmo">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>
<xsl:template match="yscrambling">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>
<xsl:template match="bootstrap">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>
<xsl:template match="other_statistics">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>
<xsl:template match="other_info">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>

<xsl:template match="preprocessing">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>


<!--  7. Predictivity -->

<xsl:template match="QSAR_Predictivity ">
  <h2><xsl:value-of select="@name" /></h2>
  
    <div class='summary'><p>
  <xsl:apply-templates select="validation_set_availability"/>
    </p></div>
    <div class='summary'><p>
  <xsl:apply-templates select="validation_set_data"/>
    </p></div>
        <div class='summary'><p>
  <xsl:apply-templates select="validation_set_descriptors"/>
    </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="validation_dependent_var_availability"/>
    </p></div>
      <div class='summary'><p>
  <xsl:apply-templates select="validation_other_info"/>
    </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="experimental_design"/>
    </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="validation_predictivity "/>
    </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="validation_assessment"/>
    </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="validation_comments"/>
    </p></div>


</xsl:template>

<xsl:template match="validation_predictivity">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>

<xsl:template match="validation_assessment">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>


<xsl:template match="validation_comments">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>

<xsl:template match="validation_other_info">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>

<xsl:template match="experimental_design">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>


<xsl:template match="validation_set_descriptors">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>


<xsl:template match="validation_dependent_var_availability">
  <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>


<!-- 8. Interpretation -->

<xsl:template match="QSAR_Interpretation">
  <h2><xsl:value-of select="@name" /></h2>
  <div class='summary'><p>
  <xsl:apply-templates select="mechanistic_basis"/>
     </p></div>
  <div class='summary'><p>
  <xsl:apply-templates select="mechanistic_basis_comments"/>
     </p></div>
            <div class='summary'><p>
  <xsl:apply-templates select="mechanistic_basis_info"/>
     </p></div>
</xsl:template>

<xsl:template match="mechanistic_basis">
    <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>

<xsl:template match="mechanistic_basis_comments">
    <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>

<xsl:template match="mechanistic_basis_info">
    <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>
 
<xsl:template match="endpoint_ref"> 
	<xsl:value-of select="id(@idref)/@name"/>
</xsl:template>

<!-- 9. Interpretation -->

<xsl:template match="QSAR_Miscelaneous">
  <h2><xsl:value-of select="@name" /></h2>
  <div class='summary'><p>
  <xsl:apply-templates select="comments"/>
     </p></div>  
  <div class='summary'><p>
  <xsl:apply-templates select="bibliography"/>
     </p></div>  
</xsl:template>

<xsl:template match="comments">
    <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>


<xsl:template match="bibliography">
  <strong><xsl:value-of select="@name" /></strong>
   <table width='100%'>
   <xsl:apply-templates select="publication_ref"/>
   </table>
</xsl:template>

<!--  -->

<xsl:template match="training_set_data"> 
    <strong><xsl:value-of select="training_set_availability/@name"/></strong>
    <xsl:value-of select="training_set_availability/@answer"/>
	<strong><xsl:value-of select="@name" /></strong>
	<ul>
	<li><b>Chemname:</b><xsl:text> </xsl:text> <xsl:value-of select="@chemname"/></li>				
	<li><b>SMILES:</b><xsl:text> </xsl:text><xsl:value-of select="@smiles"/></li>				
	<li><b>CAS RN:</b><xsl:text> </xsl:text><xsl:text> </xsl:text><xsl:value-of select="@cas"/></li>	
	<li><b>InChI:</b><xsl:text> </xsl:text><xsl:value-of select="@inchi"/></li>	
	<li><b>MOL file:</b><xsl:text> </xsl:text><xsl:value-of select="@mol"/></li>	
	<li><b>Formula:</b><xsl:text> </xsl:text><xsl:value-of select="@formula"/></li>	
	</ul>
</xsl:template> 	

<xsl:template match="validation_set_availability/@name">
    <strong><xsl:value-of select="@name" /></strong>
  <xsl:value-of select="." disable-output-escaping="yes"/>	
</xsl:template>

<xsl:template match="validation_set_data"> 
    <strong><xsl:value-of select="@name"/></strong>
	<ul>
	<li><b>Chemname:</b><xsl:text> </xsl:text><xsl:value-of select="@chemname"/>	</li>			
	<li><b>SMILES:</b><xsl:text> </xsl:text><xsl:value-of select="@smiles"/>	</li>				
	<li><b>CAS RN:</b><xsl:text> </xsl:text><xsl:value-of select="@cas"/></li>	
	<li><b>InChI:</b><xsl:text> </xsl:text><xsl:value-of select="@inchi"/></li>	
	<li><b>MOL file:</b><xsl:text> </xsl:text><xsl:value-of select="@mol"/>	</li>
	<li><b>Formula:</b><xsl:text> </xsl:text><xsl:value-of select="@formula"/>	</li>
	</ul>
</xsl:template> 

<xsl:template match="author_ref"> 
  
   <tr><th>
	<xsl:value-of select="id(@idref)/@name" disable-output-escaping="yes"/>
	</th><td>
	<xsl:value-of select="id(@idref)/@affiliation" disable-output-escaping="yes"/>	
	</td><td>
	<xsl:value-of select="id(@idref)/@contact" disable-output-escaping="yes"/>		
	</td><td>
	<xsl:value-of select="id(@idref)/@email" disable-output-escaping="yes"/>		
	</td><td>
	<xsl:call-template name="print_href"/>
	</td>
	</tr>
</xsl:template>	

<xsl:template match="publication_ref"> 
  <tr><th>
	<xsl:value-of select="id(@idref)/@title" disable-output-escaping="yes"/>
	</th><td width='20%'>
	<xsl:call-template name="print_href"/>
	</td>
	</tr>	
</xsl:template>

<xsl:template match="algorithm_ref"> 
	<xsl:value-of select="id(@idref)/@definition" disable-output-escaping="yes"/>
	<br/>	
	<xsl:value-of select="id(@idref)/@description" disable-output-escaping="yes"/>	
</xsl:template>


<xsl:template match="software_ref"> 
	<xsl:text>Name: </xsl:text>
	<xsl:value-of select="id(@idref)/@name" disable-output-escaping="yes"/>
	<xsl:text> Description: </xsl:text>
	<xsl:value-of select="id(@idref)/@description" disable-output-escaping="yes"/>
	<xsl:text> Contact: </xsl:text>
	<xsl:value-of select="id(@idref)/@contact" disable-output-escaping="yes"/>
	<xsl:text> </xsl:text>
    <xsl:call-template name="print_href"/>
    <br/>
	<!-- 
	crashes if using these 
		<td> 
		<xsl:value-of select="id(@idref)/@contact" disable-output-escaping="yes"/>
	</td>	
	<td> 
		<xsl:value-of select="id(@idref)/@description" disable-output-escaping="yes"/>
	</td>
	<td> 
		<xsl:value-of select="id(@idref)/@contact" disable-output-escaping="yes"/>
	</td>	
	
	<td>
	<xsl:call-template name="print_href"/>
	</td>	
crashes if using id(@idref)/@version  - apparently in some xml files the version field is not used at all
		 -->	
</xsl:template>

<xsl:template name="print_href">
	 		<a>
			<xsl:attribute name="href">
			<xsl:value-of select="id(@idref)/@url" disable-output-escaping="yes"/>
			</xsl:attribute>
			<xsl:attribute name="target">_blank</xsl:attribute>
			<xsl:value-of select="id(@idref)/@url" disable-output-escaping="yes"/>
			</a> 
 </xsl:template>

<!-- end xslt -->
 </xsl:stylesheet>


	