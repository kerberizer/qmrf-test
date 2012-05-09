<#--
If you indent your templates for readability, make sure you trim them using the <#t> directive,
since RTF is white-space sensitive (except for newlines).
-->
<#import "rtf.ftl" as rtf>
<#import "logo.ftl" as logo>
<#import "table.ftl" as top>
<#escape x as RtfConverter(x)>
<@rtf.document><#t>
<@top.top_pic><@logo.logo/></@top.top_pic><#t>
<@top.top_first><@rtf.bold><@rtf.italic>QMRF identifier (JRC Inventory): </@rtf.italic><@rtf.big>${qmrf.number}</@rtf.big></@rtf.bold></@top.top_first><#t>
<@top.top_middle><@rtf.bold><@rtf.italic>QMRF Title: </@rtf.italic><@rtf.big>${qmrf.qmrftitle}</@rtf.big></@rtf.bold></@top.top_middle><#t>
<@top.top_middle><@rtf.bold><@rtf.italic>Printing Date: </@rtf.italic>${.now?date}</@rtf.bold></@top.top_middle><#t>
<@top.top_last></@top.top_last><#t>
\pard\par<#t>
<#list qmrf.chapters as chapter><#t>
	<@rtf.H2><@rtf.bold>${chapter.chapter}.${chapter.title}</@rtf.bold></@rtf.H2><@rtf.newline/><#t>
	<#list chapter.subchapters.iterator as subchapter><#t>
	  	<@rtf.big><@rtf.bold>${subchapter.chapter}.${subchapter.title}</@rtf.bold></@rtf.big><@rtf.newline/><#t>
	      <@rtf.newline/><#t>	  	
	  	 <#if subchapter.text?? && !(''=subchapter.text)>
	  	 	<@rtf.justified>${subchapter.text}</@rtf.justified><@rtf.newline/><#t>
	  	 </#if>
	     <#if subchapter.attributes.answer??><#t>
	     	<@rtf.left>${subchapter.attributes.answer}</@rtf.left><@rtf.newline/><#t>
	     </#if><#t>
	     <#if subchapter.options??><#t>
	     	 <@rtf.left>
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
		     </@rtf.left>
	     </#if><#t>
	     <#if subchapter.attachments??><#t>
	     	<#list subchapter.attachments as attachmentType><#t>
	     		<#list attachmentType as attachment><#t>
	     			 <@rtf.left>
	     		     <@rtf.bold>${attachmentType.title}: </@rtf.bold> 
	     		     ${attachment.description} (${attachment.filetype})<@rtf.newline/><#t>
	     		     <@rtf.href>${attachment.url}</@rtf.href><@rtf.newline/><#t>
	     		     </@rtf.left><#t>				   	     		   
	     		 </#list><#t>
	     	</#list><#t>
	     </#if><#t>	 
	      <#if subchapter.catalogReference??><#t>
	      		<@rtf.left><#t>
		    	<#list subchapter.catalogReference.iterator as entry><#t>
					<#if entry.attributes.title?? && !(''=entry.attributes.title)><@rtf.bold>Title:</@rtf.bold> ${entry.attributes.title}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.group?? && !(''=entry.attributes.group)>${entry.attributes.group} </#if><#t>
		    		<#if entry.attributes.subgroup?? && !(''=entry.attributes.subgroup)>${entry.attributes.subgroup} </#if><#t>
					<#if entry.attributes.name?? && !(''=entry.attributes.name)><@rtf.bold>${entry.attributes.name}</@rtf.bold><@rtf.newline/></#if><#t>
		    		<#if entry.attributes.units?? && !(''=entry.attributes.units)><@rtf.bold>Units:</@rtf.bold> ${entry.attributes.units}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.description?? && !(''=entry.attributes.description)><@rtf.bold>Description:</@rtf.bold> ${entry.attributes.description}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.affiliation?? && !(''=entry.attributes.affiliation)><@rtf.bold>Affiliation:</@rtf.bold> ${entry.attributes.affiliation}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.email?? && !(''=entry.attributes.email)><@rtf.bold>Email: </@rtf.bold> ${entry.attributes.email}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.definition?? && !(''=entry.attributes.definition)>${entry.attributes.definition}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.contact?? && !(''=entry.attributes.contact)><@rtf.bold>Contact:</@rtf.bold> ${entry.attributes.contact}<@rtf.newline/></#if><#t>
		    		<#if entry.attributes.url??  && !(''=entry.attributes.url)><@rtf.href>${entry.attributes.url}</@rtf.href><@rtf.newline/></#if><#t>
   					<@rtf.newline/><#t>
   				</#list><#t>
   				</@rtf.left><#t>
		 </#if><#t>   
	</#list><#t>	
</#list><#t>
</@rtf.document>
</#escape>