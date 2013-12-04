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
	xmlHttpRequest.send(null);
}

function postTweet()
{
        xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest == null)
		return;
            
        xmlHttpRequest.open("POST", "resources/tweets/post_multipart", true);
        xmlHttpRequest.setRequestHeader("Content-Type", "multipart/form-data");
        //xmlHttpRequest.setRequestHeader("Content-Disposition", "form-data");
        xmlHttpRequest.setRequestHeader("Cache-Control", "no-cache");

        var file = document.getElementById('formfield-photourl');

        xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState === xmlHttpRequest.DONE && xmlHttpRequest.status === 200)      // completed && OK
                clearPage();
        }
        xmlHttpRequest.send(file);
}

/*function postTweet_urlformencoded()
{
	document.getElementById('form-tweet').form.reset();
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
	xmlHttpRequest.send("u="+username+"&c="+comment+"&url="+photourl+"&pdate="+pdate+"&ploc="+ploc+"&tags="+tags);
	window.location.reload();
	document.getElementById('form-tweet').reset();
}*/

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
		output += '<th>'+json[i].photo_url+'</th>';
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
