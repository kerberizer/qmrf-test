function getMyAccount() {
	var facet = {};	
    $.ajax({
        dataType: "json",
        url: "/qmrf/myaccount?media=application/json",
        success: function(data, status, xhr) {
        	$.each(data["user"],function(index, entry) {
        		$("#username").text(entry["username"]);
        		$("#useruri").prop("href",entry["uri"]);
        		$("#useruri").text(entry["title"] + " " + entry["firstname"] + " " + entry["lastname"]);
        		$("#email").prop("value",entry["email"]);
        		$("#title").prop("value",entry["title"]);
        		$("#firstname").prop("value",entry["firstname"]);
        		$("#lastname").prop("value",entry["lastname"]);

        		$("#homepage").prop("value",entry["homepage"]);
        		$("#keywords").prop("value",entry["keywords"]);
        		$("#reviewer").attr("checked",entry["reviewer"]);
        	        		
        		var protocolURI = "/qmrf/user/" + entry.id + "/protocol?headless=true&details=false&media=text/html";
        		var alertURI = "/qmrf/user/" + entry.id + "/alert?headless=true&details=false&media=text/html";
        		
        		$("#protocoluri").prop("href",protocolURI);
        		$("#alerturi").prop("href",alertURI);
        		//$("#QMRF_documents").text(protocolURI);
        		//$("#alerts").text(alertURI);
        		
        		//reload tabs
        		$(function() {$( ".tabs" ).tabs({cache: true});});
        	});
        },
        error: function(xhr, status, err) {
        },
        complete: function(xhr, status) {
        }
     });
    return facet;
}