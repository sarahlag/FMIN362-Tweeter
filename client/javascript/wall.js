
function listTweets(json)
{
	var num_page = readCookie('num_page');
	var criteria = readCookie('criteria');
	tweets_json = json;
	document.getElementById('tableTweets').innerHTML = init_table;
	
   	for (var i=json.length-1; i>=0; i--){
   		$('#tableTweets tr:last').clone(true).insertAfter('#tableTweets tr:last');
   		
   		var tags = '';
   		var img_url = "http://localhost:9000/FMIN362-Tweeter/resources/tweets/"+json[i].photo_url;
   		
   		$('#tableTweets tr:last #td-photo').html('<a href="'+img_url+'"> <img src="'+img_url+'" class="icon" /></a>');
   		$('#tableTweets tr:last #td-username').html(json[i].username);
   		$('#tableTweets tr:last #td-tweet').html(json[i].comment);
   		$('#tableTweets tr:last #td-date').html(json[i].photo_date);
   		$('#tableTweets tr:last #td-loc').html(json[i].photo_place);
   		for (var j=0; j<json[i].tags.length-1; j++)
   			tags += json[i].tags[j].tagname+', ';
   		if (json[i].tags.length > 0)
			tags += json[i].tags[j].tagname;
   		$('#tableTweets tr:last #td-tags').html(tags);
   		if (username === json[i].username || is_admin !== "false")
   		{
   			var $btn_del = $('<button />').attr({ type: 'button', id:'btn-delete-'+json[i].id });
   			$btn_del.button({ icons : { primary : "ui-icon-closethick" }, text : false });
   			$btn_del.click(function(event) {   
   				var data = new FormData();
   				data.append('username',  readCookie('username'));
   				data.append('id',  $(this).attr('id').substr(11));
   				
   				$.ajax({
   					type : 'POST',
   					url : "http://localhost:9000/FMIN362-Tweeter/resources/tweets/delete",
   					processData : false,
   					contentType : false,
   					data : data,
   					success : function(resp) {
   						getTweets();
   					},
   					error : function(resp) {
   						clearMsg();
   						printMsg("Une erreur est survenue : "+resp.responseText);
   					}
   				});
   			});
   			
   			$('#tableTweets tr:last #td-btn').html($btn_del);
   		}
   		else
   			$('#tableTweets tr:last #td-btn').html('');
   		
   	}
   	$('#tableTweets tr:eq(1)').remove();
	$("#label-page").html("Page "+num_page);
	if (criteria !== "" && criteria !== "current")
		$("#label-search").html("Recherche: "+criteria);
	else
		$("#label-search").html("");
	
	setRadioChecked();
}

function getTweets()
{
	var url = "http://localhost:9000/FMIN362-Tweeter/resources/tweets/get";
	url = addParam(url);
	var xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest === null)
		return;
	xmlHttpRequest.open("GET", url, true);
	xmlHttpRequest.setRequestHeader("Accept", "application/json"); 
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

/* ================== */
/* on load	          */
/* ================== */

$(document).ready(function($) { 
	$("body").ajaxError(function(event, jqXHR, ajaxSettings, thrownError) {
		alert("ERROR : " + thrownError);
		location.reload();
	});
	
	//Initialisation de la map
	google.maps.event.addDomListener(window, 'load', function() {
		geocoder = new google.maps.Geocoder();
		var latlng = new google.maps.LatLng(48.865166, 2.351704);
		var mapOptions = {
			zoom : 3,
			center : latlng
		};
		map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
	});
		
	WallLoaded();
	setLvlAffichage();
	
	$("input[type=submit], button").button();
	
	$("#tabs").tabs({
	      collapsible: true,
	      active: false
	});
	
	$("#formfield-pdate").datepicker({
		changeMonth : true,
		changeYear : true,
		maxDate: "+0"
	}).datepicker('setDate', new Date()); // pour mettre la date du jour par défaut
	
			
	/* ================== */
	/* affichage	     */
	/* ================== */
	
	$("input[type='radio']").change(function(event) {
		writeCookie("num_page", 1);
		writeCookie("nb_affichage", $(this).val());
		getTweets();
	});

	
	$("#btn-next").button({ icons : { primary : "ui-icon-circle-triangle-e" }, text : false });
	$("#btn-next").click(function(event) {
		var nb_affichage = readCookie('nb_affichage');
		var num_page = readCookie('num_page');
		if (nb_affichage == 0)
			return;
		num_page ++;
		writeCookie("num_page", num_page);
		getTweets();
	});
	
	$("#btn-prev").button({ icons : { primary : "ui-icon-circle-triangle-w" }, text : false });
	$("#btn-prev").click(function(event) {
		var nb_affichage = readCookie('nb_affichage');
		var num_page = readCookie('num_page');
		if (nb_affichage == 0)
			return;
		if(num_page <= 1) {
			writeCookie("num_page", 1);
		} else {
			num_page--;
			writeCookie("num_page", num_page);
			getTweets();
		}
	});
	
	/* ================== */
	/* recherche	     */
	/* ================== */
	
	$("#btn-search").click(function(event) {
		var criteria = readCookie('criteria');
		
		var users = $("input[name=searchUsers]").val();
		var tags = $("input[name=searchTags]").val();
		if (users !== "" || tags !== "")
		{	
			if (users !== "")
				criteria = "users:"+users;
			if (tags !== "")
			{
				if (users !== "")
					criteria += "+tags:"+tags;
				else
					criteria = "tags:"+tags;
			}
			writeCookie("num_page", 1);
			writeCookie("criteria", criteria);
			
			getTweets();
		}
	});
	
	/* ================== */
	/* boutons		      */
	/* ================== */
		
	$("#btn-return").button({ icons : { primary : "ui-icon-home" }, text : false 	});
	$("#btn-return").click(function(event) {
		writeCookie("num_page", 1);
		writeCookie("criteria", '');
		getTweets();
	});
	
	$("#btn-deconnect").button({ icons : { primary : "ui-icon-extlink" }, text : false 	});
	$("#btn-deconnect").click(function(event) {
		writeCookie('is_admin', "false");
		writeCookie('username', 'anon');
		writeCookie("num_page", 1);
		writeCookie("criteria", '');
		writeCookie("nb_affichage", 0);
		location.href="index.html";
	});
	
	$("#btn-posttweet").click(function(event) {
		var data = new FormData();
		data.append('username',  readCookie('username'));
		data.append('photofile', $('#formfield-photourl')[0].files[0]);
		data.append('comment',   $('#formfield-comment').val());
		data.append('photodate', $('#formfield-pdate').val());
		data.append('photoloc',  $('#formfield-ploc').val());
		data.append('tags', 	 $('#formfield-tags').val());

		$.ajax({
			type : 'POST',
			url : "http://localhost:9000/FMIN362-Tweeter/resources/tweets/post",
			processData : false,
			contentType : false,
			data : data,
			success : function(resp) {
				listTweets(resp); // listTweets dans wall.js
				clearMsg();
			},
			error : printMsg("Une erreur est survenue. Le tweet n'a pas pu être posté.")
		});
	});
	
	$("#btn-changemdp").click(function(event) {
		var passwd = $('#oldpasswd').val();
		var newpasswd = $('#newpasswd').val();
		var newpasswd_verif = $('#newpasswd_verif').val();
		
		if (newpasswd !== newpasswd_verif)
		{
			clearMsg();
			printMsg("Erreur : les deux mots de passe ne correspondent pas.");
			return;
		}
		
		var data = "username="+username
					+"&passwd="+passwd
					+"&newpasswd="+newpasswd
					+"&newpasswd_verif="+newpasswd_verif;
		
		$.ajax({
			type : 'POST',
			url : "http://localhost:9000/FMIN362-Tweeter/resources/users/login",
			data : data,
			success : function(resp) {
				clearMsg();
				printMsg("Votre mot de passe a bien été modifié");
			},
			error : function(resp) {
					clearMsg();
					printMsg("Une erreur est survenue : "+resp.responseText);
			}	
		});
	});
	
	$("#btn-delete-users").click(function(event) {
		var data = new FormData();
		data.append('username',  readCookie('username'));
		data.append('names', 	 $("#formfield-delete-users").val());

		$.ajax({
			type : 'POST',
			url : "http://localhost:9000/FMIN362-Tweeter/resources/users/delete",
			processData : false,
			contentType : false,
			data : data,
			success : function(resp) {
				getTweets();
			},
			error : function(resp) {
				clearMsg();
				printMsg("Une erreur est survenue : "+resp.responseText);
			}
		});
	});
	
	$("#btn-delete-tags").click(function(event) {
		var data = new FormData();
		data.append('username', readCookie('username'));
		data.append('names', 	 $("#formfield-delete-tags").val());
					
		$.ajax({
			type : 'POST',
			url : "http://localhost:9000/FMIN362-Tweeter/resources/tags/delete",
			processData : false,
			contentType : false,
			data : data,
			success : getTweets(),
			error : function(resp) {
				clearMsg();
				printMsg("Une erreur est survenue : "+resp.responseText);
			}	
		});
	});
	
	$("#btn-ren-tags").click(function(event) {
		var data = new FormData();
		data.append('username',  readCookie('username'));
		data.append('oldtag', 	 $("#formfield-ren-tagold").val());
		data.append('newtag', 	 $("#formfield-ren-tagnew").val());

		$.ajax({
			type : 'POST',
			url : "http://localhost:9000/FMIN362-Tweeter/resources/tags/rename",
			processData : false,
			contentType : false,
			data : data,
			success : getTweets(),
			error : function(resp) {
				clearMsg();
				printMsg("Une erreur est survenue : "+resp.responseText);
			}	
		});
	});
	
	$("#btn-test").click(function(event) {
	var data = new FormData();
	data.append('from_username',  readCookie('username'));
	data.append('id',  '16');
	
	data.append('username',  'mikasa'); // no reaction
	data.append('photofile', $('#formfield-photourl')[0].files[0]); // ok
	data.append('comment',   'hey'); 
	data.append('photodate', '');
	data.append('photoloc',  '');
	data.append('tags', 	 'hello');

	$.ajax({
		type : 'POST',
		url : "http://localhost:9000/FMIN362-Tweeter/resources/tweets/update",
		processData : false,
		contentType : false,
		data : data,
		success : getTweets(),
		error : printMsg("Une erreur est survenue. Le tweet n'a pas pu être modifié.")
	});
	});
	
	/* ================== */
	/* show map		      */
	/* ================== */
	
	$("#dialogMap").dialog({
		autoOpen : false,
		width: 600
	});
	
	$("#showMap").click(function(event) {
		$("#dialogMap").dialog("open");
		google.maps.event.trigger(map, "resize");
		showMap();
	});
	
	$("#dialogInfo").dialog({
		autoOpen : false,
		height: 500
	});
	
	function showTweetInfo(marker, tweet)
	{
		google.maps.event.addListener(marker, 'click', function() {
			var tags = '';
			var img_url = "http://localhost:9000/FMIN362-Tweeter/resources/tweets/"+tweet.photo_url;
			$('#dialogInfo').html('');
			$('#dialogInfo').append('<a href="'+img_url+'"> <img src="'+img_url+'" class="bigIcon" /></a>');
			$('#dialogInfo').append('<p><b>Auteur : </b>'+tweet.username+'</p>');
			$('#dialogInfo').append('<p><b>Commentaire : </b>'+tweet.comment+'</p>');
			$('#dialogInfo').append('<p><b>Date : </b>'+tweet.photo_date+'</p>');
			$('#dialogInfo').append('<p><b>Lieu : </b>'+tweet.photo_place+'</p>');
			for (var j=0; j<tweet.tags.length-1; j++)
				tags += tweet.tags[j].tagname+', ';
			if (tweet.tags.length > 0)
				tags += tweet.tags[j].tagname;
			$('#dialogInfo').append('<p><b>Tags : </b>'+tags+'</p>');
			
			$("#dialogInfo").dialog("open");
		});
	}
	
	function showMap() {
		if (tweets_json.length > 11)
			alert("Trop de tweets. Veuillez réduire le nombre.");
		else
		{
		//Récupération des tweets
		$(tweets_json).each(function(i) {
			//Initialisation du geocoder
			geocoder = new google.maps.Geocoder();
			//Récupération des infos du tweet
			var tweet = this;
			var id = this.id;
			var address = this.photo_place;
			var date = this.photo_date;
			var comment = this.comment;
			//Géolocalisation du tweet
			geocoder.geocode({
				'address' : address
			}, function(results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
					//On recentre la carte sur la position du tweet
					map.setCenter(results[0].geometry.location);
					//Ajout d'un marker correspondant au tweet
					var marker = new google.maps.Marker({
						map : map,
						title : date + " " + comment,
						draggable : true,
						animation : google.maps.Animation.DROP,
						position : results[0].geometry.location
					});
					//Ajout d'un listener sur le marker pour afficher les infos du tweet
					showTweetInfo(marker, tweet);
				} 
				else if (status == google.maps.GeocoderStatus.ZERO_RESULTS)
					alert('[id='+id+'] '+address+' est une adresse invalide');
				else {
					alert('Geocode was not successful for the following reason: ' + status);
				}
				
			});
			});
		}
	}
	
	/* ================== */
	/* autocompletion     */
	/* ================== */

	$.ajax({
		type : 'GET',
		url : "http://localhost:9000/FMIN362-Tweeter/resources/tags/get",
		//contentType : "application/json", // marche pas avec ça
		success : function(data) {
			$(data).each(function(i) {
				availableTags[i] = this.tagname;
			});
			clearMsg();
		},
		error : printMsg("Erreur: n'a pas pu récuperer la liste des tags.")
	});

	$.ajax({
		type : 'GET',
		url : "http://localhost:9000/FMIN362-Tweeter/resources/users/get",
		//contentType : "application/json; charset=UTF-8",
		success : function(data) {
			$(data).each(function(i) {
				availableUsers[i] = this.username;
			});
			clearMsg();
		},
		error : printMsg("Erreur: n'a pas pu récuperer la liste des utilisateurs.")
	});
	
	$(".tags")
	// don't navigate away from the field on tab when selecting an item
	.bind("keydown", function(event) {
		if (event.keyCode === $.ui.keyCode.TAB && $(this).data("ui-autocomplete").menu.active) {
			event.preventDefault();
		}
	}).autocomplete({
		minLength : 0,
		source : function(request, response) {
			// delegate back to autocomplete, but extract the last term
			response($.ui.autocomplete.filter(availableTags, extractLast(request.term)));
		},
		focus : function() {
			// prevent value inserted on focus
			return false;
		},
		select : function(event, ui) {
			var terms = split(this.value);
			// remove the current input
			terms.pop();
			// add the selected item
			terms.push(ui.item.value);
			// add placeholder to get the comma-and-space at the end
			terms.push("");
			this.value = terms.join(",");
			return false;
		}
	});

	$(".users")
	// don't navigate away from the field on tab when selecting an item
	.bind("keydown", function(event) {
		if (event.keyCode === $.ui.keyCode.TAB && $(this).data("ui-autocomplete").menu.active) {
			event.preventDefault();
		}
	}).autocomplete({
		minLength : 0,
		source : function(request, response) {
			// delegate back to autocomplete, but extract the last term
			response($.ui.autocomplete.filter(availableUsers, extractLast(request.term)));
		},
		focus : function() {
			// prevent value inserted on focus
			return false;
		},
		select : function(event, ui) {
			var terms = split(this.value);
			// remove the current input
			terms.pop();
			// add the selected item
			terms.push(ui.item.value);
			// add placeholder to get the comma-and-space at the end
			terms.push("");
			this.value = terms.join(",");
			return false;
		}
	});
	
});

