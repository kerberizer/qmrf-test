/**
 * jOpenTox JavaScript Library v0.0.1 (alpha)
 * 
 * Copyright 2012, IDEAconsult Ltd.
 * http://www.ideaconsult.net/
 * 
 * TODO: Licence.
 */

function runTask(modelURI, datasetURI, resultDOM, statusDOM, imgRunning, imgReady, imgError) {
	
	var request = new XMLHttpRequest();
	var reqBody = 'dataset_uri=' + datasetURI;
	
	// 'true' is for async
	request.open('POST', modelURI, true);
	
	request.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	request.setRequestHeader('Accept', 'text/uri-list');
	
	request.onreadystatechange = function() {
		if (request.readyState != 4) { return false; }
		switch (request.status) {
			case 200:
				document.getElementById(resultDOM).innerHTML = request.responseText;
				document.getElementById(statusDOM).src = imgReady;
				document.getElementById(resultDOM).style.display = 'inline';
				document.getElementById(statusDOM).style.display = 'inline';
				break;
			case 201: // fall down
			case 202:
				checkTask(request.responseText, resultDOM, statusDOM, imgReady, imgError);
				document.getElementById(statusDOM).src = imgRunning;
				document.getElementById(statusDOM).style.display = 'inline';
				break;
			case 400:
				document.getElementById(resultDOM).innerHTML = request.statusText;
				document.getElementById(statusDOM).src = imgError;
				document.getElementById(resultDOM).style.display = 'inline';
				document.getElementById(statusDOM).style.display = 'inline';
				break;
			default:
				alert('Unexpected HTTP response code returned.');
				break;
			}
	};
	
	request.send(reqBody);
}

function checkTask(taskURI, resultDOM, statusDOM, imgReady, imgError) {
	
	var request = new XMLHttpRequest();
	
	// 'true' is for async
	request.open('GET', taskURI, true);
	
	request.setRequestHeader('Accept', 'text/uri-list');
	
	request.onreadystatechange = function() {
		if (request.readyState != 4) { return false; }
		switch (request.status) {
			case 200:
				document.getElementById(resultDOM).innerHTML = request.responseText;
				document.getElementById(statusDOM).src = imgReady;
				document.getElementById(resultDOM).style.display = 'inline';
				document.getElementById(statusDOM).style.display = 'inline';
				break;
			case 201: // fall down
			case 202:
				var taskTimer = window.setTimeout(function() {
					checkTask(taskURI, resultDOM, statusDOM, imgReady, imgError);
				}, 3000);
				break;
			case 400:
				document.getElementById(resultDOM).innerHTML = request.statusText;
				document.getElementById(statusDOM).src = imgError;
				document.getElementById(resultDOM).style.display = 'inline';
				document.getElementById(statusDOM).style.display = 'inline';
				break;
			default:
				alert('Unexpected HTTP response code returned.');
				break;
		}
	};
	
	request.send(null);
}