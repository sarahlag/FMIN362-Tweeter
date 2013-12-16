var xmlHttpRequest;
var reader;
var reader_result;

/* ====================	*/
/* Utils		*/
/* ====================	*/

function checkPhoto()
{
	var str=document.getElementById('formfield-photourl').value.toUpperCase();
	if ( !( /\.JPG$/.test(str) || /\.JPEG$/.test(str) || /\.PNG$/.test(str) || /\.GIF$/.test(str) ))
		alert('Vous devez selectionner une image !');
	else
	{
		var file = document.getElementById('formfield-photourl').files[0];
		dataToBinary(file);
	}
}

function resetForm()
{
        console.log('clearing form');
        document.getElementById('form-tweet').reset();
}

/* ====================	*/
/* AJAX			*/
/* ====================	*/

function write_multipart_text(boundary, name, value, end)
{
	var text = "";
	text += '--' + boundary + '\r\n' + 'Content-Disposition: form-data; name="';
	text += name;
	text += '"\r\n\r\n';
	text += value;
	text += '\r\n';
	text += '--' + boundary;
	text += end;
	return text;
}

function dataToBinary(file){
    reader = new FileReader(); 
    reader.onload = function() {
	reader_result = reader.result;
    };
    reader.readAsBinaryString(file);
}

function write_multipart_image(boundary, name, photofile, end)
{
	alert(reader_result);
	var text = "";
	//text += '--' + boundary + '\r\n' + 'Content-Disposition: form-data; name="';
	text += 'Content-Disposition: form-data; name="';
	text += name;
	text += '"; filename="';
	text += photofile.value;
	text += '"\r\nContent-Type: ';
	text += photofile.files[0].type + '\r\n' + 'Content-Transfer-Encoding: binary';
	text += '\r\n\r\n';
	text += reader_result; // binary data - écrite lors onchange de input file
	text += '\r\n';
	text += '--' + boundary;
	text += end;
	return text;
}

function write_multipart_request(xmlHttpRequest)
{
	var boundary = 'xxx' + Math.floor(Math.random()*32768) + Math.floor(Math.random()*32768) + Math.floor(Math.random()*32768);
	xmlHttpRequest.setRequestHeader( "Content-Type", 'multipart/form-data; boundary=' + boundary );
	var body = '';
	var photofile = document.getElementById('formfield-photourl');
	//body += '--' + boundary; // ???
	body += write_multipart_text(boundary, 'username', document.getElementById('formfield-username').value, "\r\n");
	body += write_multipart_image(boundary, 'photofile', photofile, "--\r\n");
	//body += write_multipart_text(boundary, 'url', document.getElementById('formfield-photourl').value, "--\r\n");
	return body;
}

function getTweets()
{
	xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest === null)
		return;
	xmlHttpRequest.open("GET", "http://localhost:9000/FMIN362-Tweeter/resources/tweets/get", true);
	xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState === xmlHttpRequest.DONE && xmlHttpRequest.status === 200)      // completed && OK
                listTweets(xmlHttpRequest.responseText);
        }
	xmlHttpRequest.send("");
}

function postTweet() // post tweet multipart, version je-me-complique-trop-la-vie
{
        xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest == null)
		return;
            
        xmlHttpRequest.open("POST", "http://localhost:9000/FMIN362-Tweeter/resources/tweets/post", true);

	var data = write_multipart_request(xmlHttpRequest);
	alert(data);
        xmlHttpRequest.onreadystatechange = function() {
		console.log(xmlHttpRequest.readyState + " " + xmlHttpRequest.status);
            if (xmlHttpRequest.readyState === xmlHttpRequest.DONE && xmlHttpRequest.status === 0) {     // completed && OK
                listTweets(xmlHttpRequest.responseText);
		resetForm();
	    }

        }
        xmlHttpRequest.send(data);
}

/* ====================	*/
/* Modification Wall    */
/* ====================	*/

function listTweets(data)
{
	console.log('listing!');
	var listtweets = "";
	var json = JSON.parse(data);
   	var output;
   	for (var i=json.length-1; i>=0; i--){
		output = '<tr>';
		output += '<th><a href="'+json[i].photo_url+'"> <img src="'+json[i].photo_url+'" class="icon" /></a></th>';
		output += '<th>'+json[i].username+'</th>';
		output += '<th>'+json[i].comment+'</th>';
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

