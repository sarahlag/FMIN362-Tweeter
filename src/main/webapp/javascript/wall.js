var xmlHttpRequest;

function FetchTweets()
{
	xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest === null)
		return;
	xmlHttpRequest.open("GET", "resources/tweets/get", true);
	xmlHttpRequest.overrideMimeType("application/json");
	xmlHttpRequest.onreadystatechange = ListTweets;
	xmlHttpRequest.send(null);
}

function ListTweets()
{
	var listtweets = "";
        console.log('readyState == '+xmlHttpRequest.readyState+', status == '+xmlHttpRequest.status)
	if(xmlHttpRequest.readyState === xmlHttpRequest.DONE && xmlHttpRequest.status === 200)
	{
		var json = JSON.parse(xmlHttpRequest.responseText);
   		var output;
   		for (var i=0; i<json.length; i++){
			output = '<tr>';
			output += '<th>'+json[i].username+'</th>';
			output += '<th>'+json[i].comment+'</th>';
			output += '<th>'+json[i].photo_url+'</th>';
			output += '<th>'+json[i].photo_date+'</th>';
			output += '<th>'+json[i].photo_place+'</th>';
			output += '<th>'+json[i].tags+'</th>';
			output+='</tr>'
			listtweets += output;
   		}
		document.getElementById('tableTweets').innerHTML += listtweets;
	}
}

FetchTweets();
