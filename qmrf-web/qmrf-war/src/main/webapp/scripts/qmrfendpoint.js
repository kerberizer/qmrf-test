
function endpointAutocomplete(root,qmrf_uri) {
	$( "#endpoint" ).each(function() {
		var autoCompleteElement = this;
		var formElementName = $(this).attr('name');
		var hiddenElementID  = formElementName + '_autocomplete_hidden';
		$(this).attr('name', formElementName + 'Name');
		$(this).before("<input type='text' size='15' required style='border:none;.ui-autocomplete overflow-y: auto;' readonly name=" + formElementName + " id=" + hiddenElementID + " />");
		$(this).autocomplete({
			source:root+'/catalog?media=application/json', 
			select: function(event, ui) {
				var selectedObj = ui.item;
				$(autoCompleteElement).val(selectedObj.name);
				$('#'+hiddenElementID).val(selectedObj.code);
				$('#endpointParentCode').val(selectedObj.parentCode);
				$('#endpointParentName').val(selectedObj.parentName);			
				return false;
			}
		});
	});

	$.getJSON(qmrf_uri,
    function(data){
      $.each(data, function(i,item){
       	$('#endpoint_autocomplete_hidden').val(item.code);
       	$('#endpoint').val(item.name);
       	$('#endpointParentCode').val(item.parentCode);
       	$('#endpointParentName').val(item.parentName);
      });
    });							

}