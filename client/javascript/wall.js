var xmlHttpRequest;

var nb_affichage;
var num_page;

/* ====================	*/
/* Utils		*/
/* ====================	*/

function checkPhoto()
{
	var str=document.getElementById('formfield-photourl').value.toUpperCase();
	if ( !( /\.JPG$/.test(str) || /\.JPEG$/.test(str) || /\.PNG$/.test(str) || /\.GIF$/.test(str) ))
		alert('Vous devez selectionner une image !');
}

function addParam(url)
{
	if (typeof nb_affichage == 'undefined')
		nb_affichage = 0;
	if (typeof num_page == 'undefined')
		num_page = 1;
		
	url += "?";
	url += "by="+nb_affichage;
	url += "&p="+num_page;
	return url;
}

function setRadioChecked()
{
	if (nb_affichage == 0)
		document.getElementById('sh0').checked = true;
	else
		document.getElementById('sh0').checked = false;
	
	if (nb_affichage == 5)
		document.getElementById('sh5').checked = true;
	else
		document.getElementById('sh5').checked = false;
	
	if (nb_affichage == 10)
		document.getElementById('sh10').checked = true;
	else
		document.getElementById('sh10').checked = false;
}

/* ====================	*/
/* AJAX			*/
/* ====================	*/

function getTweets()
{
	var url = "http://localhost:9000/FMIN362-Tweeter/resources/tweets/get";
	url = addParam(url);
	xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest === null)
		return;
	xmlHttpRequest.open("GET", url, true);
	xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState === xmlHttpRequest.DONE && xmlHttpRequest.status === 200){      // completed && OK
                listTweets(JSON.parse(xmlHttpRequest.responseText));
                clearErrorMsg();
            }
            else if (xmlHttpRequest.readyState === xmlHttpRequest.DONE) // status == 4**, erreur
            	printErrorMsg("Erreur: n'a pas pu récupérer la liste des tweets.");
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
	document.getElementById('label-page').innerHTML = 'Page '+num_page;
	setRadioChecked();
}

function printErrorMsg(msg)
{
	document.getElementById('div-messages').innerHTML += '<label class="errmsg">'+msg+'</label> </br>';
}

function clearErrorMsg()
{
	document.getElementById('div-messages').innerHTML = "";
}

/* ====================	*/
/* On Load		*/
/* ====================	*/

getTweets();

