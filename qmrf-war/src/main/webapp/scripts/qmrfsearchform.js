function validateQMRFSearchForm() {
	$("#protocolquery").validate({
		
		rules : {
			'search': {
				required : true,
				minlength: 2
			},		
			'option': {
				required : true
			},
			'pagesize': {
				number	 : true,
				range: [1, 10000]
			}
			
		},
		messages : {
			'search'  : {
				required: "Please enter a search query"
			},
			'option' : "Please select an option",
			'pagesize'  : {
				required: "Please provide number of hits."
			}		
		}
	});
}


function qHelp(option) {
	var help = {
		'title' : 'Enter partial or full QMRF document title',
		'text'  : 'Enter a phrase in free text. There are no special operators.',
		'textboolean'  : 'Enter search string in implied Boolean logic. By default the words are combined with "or". More rules: + the word must be present; - the word must not be present; ( ) group expressions',
		'endpoint'  : 'Enter endpoint name',
		'author'  : 'Enter QMRF author name',
		'qmrfnumber'  : 'Enter QMRF number'
	};
	$( "#qhelp" ).html(help[option]);
	$( "#qhelp" ).show();
}
