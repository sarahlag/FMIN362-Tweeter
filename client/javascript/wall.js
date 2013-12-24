var xmlHttpRequest;

/* ====================	*/
/* Utils		*/
/* ====================	*/

function checkPhoto()
{
	var str=document.getElementById('formfield-photourl').value.toUpperCase();
	if ( !( /\.JPG$/.test(str) || /\.JPEG$/.test(str) || /\.PNG$/.test(str) || /\.GIF$/.test(str) ))
		alert('Vous devez selectionner une image !');
}

/* ====================	*/
/* AJAX			*/
/* ====================	*/

function getTweets()
{
	xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest === null)
		return;
	xmlHttpRequest.open("GET", "http://localhost:9000/FMIN362-Tweeter/resources/tweets/get", true);
	xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState === xmlHttpRequest.DONE && xmlHttpRequest.status === 200)      // completed && OK
                listTweets(JSON.parse(xmlHttpRequest.responseText));
        };
	xmlHttpRequest.send("");
}


/* ====================	*/
/* Modification Wall    */
/* ====================	*/

function listTweets(json)
{
	var listtweets = "<tr>"
				+"<th>Photo</th>"
				+"<th>Username</th>"
				+"<th>Tweet</th>"
				+"<th>Date</th>"
				+"<th>Location</th>"
				+"<th>Tags</th>"
			+"</tr>";
   	var output;
   	for (var i=json.length-1; i>=0; i--){
		output = '<tr>';
		output += '<th><a href="'+json[i].photo_url+'"> <img src="'+json[i].photo_url+'" class="icon" /></a></th>';
		output += '<th>'+json[i].username+'</th>';
		output += '<th>'+json[i].comment+'</th>';
		output += '<th>'+json[i].photo_date+'</th>';
		output += '<th>'+json[i].photo_place+'</th>';
		output += '<th>';
		for (var j=0; j<json[i].tags.length-1; j++)
			output += json[i].tags[j].tagname+',';
		output += json[i].tags[j].tagname+'</th>';
		output += '</tr>';
		listtweets += output;		
   	}
	document.getElementById('tableTweets').innerHTML = listtweets;
}

function printErrorMsg(id, msg)
{
	document.getElementById(id).innerHTML = msg;
}

/* ====================	*/
/* On Load		*/
/* ====================	*/

getTweets();

