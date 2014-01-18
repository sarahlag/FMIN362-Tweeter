var nb_affichage;
var num_page;
var criteria;

var username;
var is_admin;

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
	if (typeof criteria == 'undefined')
		criteria = "current";
		
	url += "?";
	url += "by="+nb_affichage;
	url += "&p="+num_page;
	url += "&c="+criteria;
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
	var xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest === null)
		return;
	xmlHttpRequest.open("GET", url, true);
	xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState === xmlHttpRequest.DONE && xmlHttpRequest.status === 200){      // completed && OK
                listTweets(JSON.parse(xmlHttpRequest.responseText));
                clearMsg();
            }
            else if (xmlHttpRequest.readyState === xmlHttpRequest.DONE) // status == 4**, erreur
            	printMsg("Erreur: n'a pas pu récupérer la liste des tweets.");
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
				+"<th class='lvlUser'>Admin</th>"
			+"</tr>";
   	var output;
   	var img_url;
   	for (var i=json.length-1; i>=0; i--){
   		img_url = "http://localhost:9000/FMIN362-Tweeter/resources/tweets/"+json[i].photo_url;
		output = '<tr>';
		output += '<td><a href="'+img_url+'"> <img src="'+img_url+'" class="icon" /></a></td>';
		output += '<td>'+json[i].username+'</td>';
		output += '<td>'+json[i].comment+'</td>';
		output += '<td>'+json[i].photo_date+'</td>';
		output += '<td>'+json[i].photo_place+'</td>';
		output += '<td>';
		for (var j=0; j<json[i].tags.length-1; j++)
			output += json[i].tags[j].tagname+', ';
		output += json[i].tags[j].tagname+'</td>';
		
		if (username === json[i].username || is_admin !== "false")
			output += '<td id="td-'+json[i].id+'"><button id="btn-delete-'+json[i].id+'" class="btn-delete" type="button">x</button></td>';
		//output += '<td class="lvlUser" id="td-'+json[i].id+'"></td>';

		// <button id="btn-delete-'+json[i].id+'" class="btn-delete" type="button"> </button>
		//var $button = $('<button/>').attr({ type: 'button', id:'btn-delete-'+json[i].id}).addClass("btn-delete");
		//$("#col-admin").append($button);
		
		output += '</tr>';
		listtweets += output;		
   	}
	document.getElementById('tableTweets').innerHTML = listtweets;
	document.getElementById('label-page').innerHTML = 'Page '+num_page;
	setRadioChecked();
}

function printMsg(msg)
{
	document.getElementById('div-messages').innerHTML += '<label class="errmsg">'+msg+'</label> </br>';
}

function clearMsg()
{
	document.getElementById('div-messages').innerHTML = "";
}

/* ====================	*/
/* Session Cookies		*/
/* ====================	*/

function writeCookie(name, value) 
{
    document.cookie = name + "=" + encodeURIComponent(value); // + "; path=/";
}

function readCookie (cookie_name)
{
	var cookie_string = document.cookie;
	if (cookie_string.length === 0)
		return '';
	var i = cookie_string.indexOf(cookie_name);
	if (i<0)
		return '';
	var cookie_value = cookie_string.slice(i+cookie_name.length+1);
	var j = cookie_value.indexOf(";");
	if (j>=0)
		cookie_value = cookie_value.substring(0,j);
	return decodeURIComponent ( cookie_value ) ;
}

/* ====================	*/
/* Buttons				*/
/* ====================	*/


/* ====================	*/
/* On Load				*/
/* ====================	*/

function WallLoaded()
{
	is_admin = readCookie('is_admin');
	username = readCookie('username');
	if (typeof username == 'undefined' ||  username === '')
		username = "anon";
	document.getElementById('p-username').innerHTML = "@"+username;
	getTweets();
}


