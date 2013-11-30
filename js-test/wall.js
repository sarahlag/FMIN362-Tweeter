var xmlHttpRequest;

function FetchTweets()
{
	xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest == null)
		return;
	xmlHttpRequest.open("GET", "tweets.json", true);
	xmlHttpRequest.overrideMimeType("application/json");
	xmlHttpRequest.onreadystatechange = StateChangeForJSON;
	xmlHttpRequest.send(null);
}

function StateChangeForJSON()
{
	var listtweets = "";
	if(xmlHttpRequest.readyState == xmlHttpRequest.DONE) // && xmlHttpRequest.status == 200)
	{
		var json = JSON.parse(xmlHttpRequest.responseText);
		//console.log(json[0].username);
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

