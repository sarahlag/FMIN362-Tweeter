var xmlHttpRequest;

/* ====================	*/
/* Utils		*/
/* ====================	*/

function checkPhoto()
{
	str=document.getElementById('formfield-photourl').value.toUpperCase();
	if ( !( /\.JPG$/.test(str) || /\.JPEG$/.test(str) || /\.PNG$/.test(str) || /\.GIF$/.test(str) ))
		alert('Vous devez selectionner une image !');
}

function clearPage()
{
        console.log('clearing document');
        window.location.reload();
        document.getElementById('form-tweet').reset();
}

/* ====================	*/
/* AJAX			*/
/* ====================	*/

function getTweets()
{
	xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest === null)
		return;
	xmlHttpRequest.open("GET", "resources/tweets/get", true);
	xmlHttpRequest.overrideMimeType("application/json");
	xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState === xmlHttpRequest.DONE && xmlHttpRequest.status === 200)      // completed && OK
                listTweets(xmlHttpRequest.responseText);
        }
	xmlHttpRequest.send("null");
}

function postTweet()
{
        xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest == null)
		return;
            
        xmlHttpRequest.open("POST", "resources/tweets/post", true);
        xmlHttpRequest.setRequestHeader("Content-Type", "multipart/form-data");
        xmlHttpRequest.setRequestHeader("Cache-Control", "no-cache");

        var file = document.getElementById('formfield-photourl');

        xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState === xmlHttpRequest.DONE && xmlHttpRequest.status === 200)      // completed && OK
                clearPage();
        }
        xmlHttpRequest.send(file);
}

/* ====================	*/
/* Modification Wall    */
/* ====================	*/

function listTweets(data)
{
	var listtweets = "";
	var json = JSON.parse(data);
   	var output;
   	for (var i=0; i<json.length; i++){
		output = '<tr>';
		output += '<th>'+json[i].username+'</th>';
		output += '<th>'+json[i].comment+'</th>';
		output += '<th><a href="'+json[i].photo_url+'"> <img src="'+json[i].photo_url+'" class="icon" /></a></th>';
		output += '<th>'+json[i].photo_date+'</th>';
		output += '<th>'+json[i].photo_place+'</th>';
		output += '<th>'+json[i].tags+'</th>';
		output+='</tr>'
		listtweets += output;
   	}
	document.getElementById('tableTweets').innerHTML += listtweets;
}

/* ====================	*/
/* On Load		*/
/* ====================	*/

getTweets();
