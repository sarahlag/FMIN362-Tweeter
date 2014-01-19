var tweets_json;

var availableTags = new Array();
var availableUsers = new Array();

var init_table;

var geocoder;
var map;

/* ====================	*/
/* Utils				*/
/* ====================	*/

function checkPhoto()
{
	var str=document.getElementById('formfield-photourl').value.toUpperCase();
	if ( !( /\.JPG$/.test(str) || /\.JPEG$/.test(str) || /\.PNG$/.test(str) || /\.GIF$/.test(str) ))
		alert('Vous devez selectionner une image !');
}

function addParam(url)
{
	var nb_affichage = readCookie("nb_affichage");
	var num_page = readCookie("num_page");
	var criteria = readCookie("criteria");
		
	url += "?";
	url += "by="+nb_affichage;
	url += "&p="+num_page;
	url += "&c="+criteria;
	return url;
}

function split(val) {
	return val.split(/,\s*/);
}

function extractLast(term) {
	return split(term).pop();
}

function setRadioChecked()
{
	var nb_affichage = readCookie("nb_affichage");
	
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
/* Modification Wall    */
/* ====================	*/

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
/* On Load				*/
/* ====================	*/

function setLvlAffichage()
{
	is_admin = readCookie('is_admin');
	username = readCookie('username');
	
	if (username === "anon")
		$(".lvlUser").css("display", "none");
	if (is_admin !== "true")
		$(".lvlAdmin").css("display", "none");
}

function WallLoaded()
{
	var nb_affichage = readCookie('nb_affichage');
	var num_page = readCookie('num_page');
	var criteria = readCookie('criteria');
	
	if (nb_affichage === '')
	{
		nb_affichage = 0;
		writeCookie("nb_affichage", 0);
	}
	if (num_page === '')
	{
		num_page = 1;
		writeCookie("num_page", 1);
	}
	if (criteria === '')
	{
		criteria = "current";
		writeCookie("criteria", "current");
	}
	
	is_admin = readCookie('is_admin');
	username = readCookie('username');
	if (username === '')
		username = "anon";
	document.getElementById('p-username').innerHTML = "TWEETER (@"+username+")";
	init_table = document.getElementById('tableTweets').innerHTML;
	
	getTweets();
}


