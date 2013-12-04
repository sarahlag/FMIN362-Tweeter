var xmlHttpRequest;

// pour le cross-domain


/* ====================	*/
/* AJAX			*/
/* ====================	*/

function fetchTweets()
{
	xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest === null)
		return;
	xmlHttpRequest.open("GET", "tweets.json", true);
	xmlHttpRequest.overrideMimeType("application/json");
	xmlHttpRequest.onreadystatechange = listTweets;
	xmlHttpRequest.send(null);
}

function listTweets()
{
	var listtweets = "";
        console.log('readyState == '+xmlHttpRequest.readyState+', status == '+xmlHttpRequest.status)
	if(xmlHttpRequest.readyState === xmlHttpRequest.DONE && xmlHttpRequest.status === 0)
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

function postTweet()
{
	/*document.getElementById('form-tweet').form.reset();
	username = document.getElementById('formfield-username').value;
	comment = document.getElementById('formfield-comment').value;
	photourl = document.getElementById('formfield-photourl').value;
	pdate = document.getElementById('formfield-pdate').value;
	ploc = document.getElementById('formfield-ploc').value;
	tags = document.getElementById('formfield-tags').value;

	xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest == null)
		return;

	xmlHttpRequest.open("POST", "resources/tweets/post_urlencoded", true);
	xmlHttpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xmlHttpRequest.send("u="+username+"&c="+comment+"&url="+photourl+"&pdate="+pdate+"&ploc="+ploc+"&tags="+tags);*/
	window.location.reload();
	document.getElementById('form-tweet').reset();
}

/* ====================	*/
/* Utils		*/
/* ====================	*/

function checkPhoto()
{
	str=document.getElementById('formfield-photourl').value.toUpperCase();
	if ( !( /\.JPG$/.test(str) || /\.JPEG$/.test(str) || /\.PNG$/.test(str) || /\.GIF$/.test(str) ))
		alert('Vous devez selectionner une image !');
}

/* ====================	*/
/* On Load		*/
/* ====================	*/

fetchTweets();

