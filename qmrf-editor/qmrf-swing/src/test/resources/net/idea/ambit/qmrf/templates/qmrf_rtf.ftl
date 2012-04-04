<#--
If you indent your templates for readability, make sure you trim them using the <#t> directive,
since RTF is white-space sensitive (except for newlines).
-->
<#import "rtf.ftl" as rtf>
<#escape x as RtfConverter(x)>
<@rtf.document>
 <@rtf.H1>${qmrf.title}</@rtf.H1><@rtf.newline/><#t>
  Author ${qmrf.author}<@rtf.newline/><#t>
  Version ${qmrf.version}<@rtf.newline/><#t>
  email ${qmrf.email}<@rtf.newline/><#t>
  Contact ${qmrf.contact}<@rtf.newline/><#t>
  <@rtf.newline/><#t>
<#list qmrf.chapters as chapter><#t>
	<@rtf.H2>${chapter.chapter}.${chapter.title}</@rtf.H2><@rtf.newline/><#t>
	<#list chapter.subchapters.iterator as subchapter><#t>
	  	<@rtf.big>${subchapter.chapter}.${subchapter.title}</@rtf.big><@rtf.newline/><#t>
	  	 ${subchapter.text}<@rtf.newline/><#t>
	     <#if subchapter.attributes.answer??><#t>
	     	<@rtf.bold>${subchapter.attributes.answer}</@rtf.bold><@rtf.newline/><#t>
	     </#if><#t>
	     <#if subchapter.options??><#t>
		     <#if subchapter.attributes.chemname??><#t>
		     	<@rtf.bullet>Chemical name ${subchapter.attributes.chemname}</@rtf.bullet><@rtf.newline/><#t>
		     </#if><#t>	     
		     <#if subchapter.attributes.cas??><#t>
		     	<@rtf.bullet>CAS ${subchapter.attributes.cas}</@rtf.bullet><@rtf.newline/><#t>
		     </#if><#t>	     
		     <#if subchapter.attributes.smiles??><#t>
		     	<@rtf.bullet>SMILES ${subchapter.attributes.smiles}</@rtf.bullet><@rtf.newline/><#t>
		     </#if><#t>	     
		     <#if subchapter.attributes.inchi??><#t>
		     	<@rtf.bullet>InChI ${subchapter.attributes.inchi}</@rtf.bullet><@rtf.newline/><#t>
		     </#if><#t>	     
		     <#if subchapter.attributes.mol??><#t>
		     	<@rtf.bullet>MOL file ${subchapter.attributes.mol}</@rtf.bullet><@rtf.newline/><#t>
		     </#if><#t>	     	
		     <#if subchapter.attributes.formula??><#t>
		     	<@rtf.bullet>Formula ${subchapter.attributes.formula}</@rtf.bullet><@rtf.newline/><#t>
		     </#if><#t>
	     </#if><#t>
	     <#if subchapter.attachments??><#t>
	     	<#list subchapter.attachments as attachmentType><#t>
	     	    <@rtf.bold>${attachmentType.title}</@rtf.bold><@rtf.newline/><#t>
	     		<#list attachmentType as attachment><#t>
	     		   <@rtf.bullet><#t>
	     		     ${attachment.description} (${attachment.filetype}) <@rtf.underline>${attachment.url}</@rtf.underline><#t>				   	     		   
					</@rtf.bullet><@rtf.newline/><#t>	     		   
	     		 </#list><#t>
	     	</#list><#t>
	     	<@rtf.newline/>
	     </#if><#t>	 
	      <#if subchapter.catalogReference??><#t>
		    	<#list subchapter.catalogReference.iterator as entry><#t>
					<#if entry.attributes.title??><@rtf.bold>Title:</@rtf.bold> ${entry.attributes.title}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.group??>${entry.attributes.group} </#if><#t>
		    		<#if entry.attributes.subgroup??>${entry.attributes.subgroup} </#if><#t>
					<#if entry.attributes.name??><@rtf.bold>${entry.attributes.name}</@rtf.bold><@rtf.newline/></#if><#t>
		    		<#if entry.attributes.units??><@rtf.bold>Units:</@rtf.bold> ${entry.attributes.units}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.description??><@rtf.bold>Description:</@rtf.bold> ${entry.attributes.description}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.affiliation??><@rtf.bold>Affiliation:</@rtf.bold> ${entry.attributes.affiliation}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.email??><@rtf.bold>Email: </@rtf.bold> ${entry.attributes.email}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.definition??> ${entry.attributes.definition}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.contact??><@rtf.bold>Contact:</@rtf.bold> ${entry.attributes.contact}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.url??><@rtf.bold>WWW:</@rtf.bold> ${entry.attributes.url}<@rtf.newline/></#if><#t>
		    		<@rtf.newline/>
   				</#list><#t>
		 </#if><#t>    	     
	</#list><#t>	
</#list><#t>
</@rtf.document>
</#escape>