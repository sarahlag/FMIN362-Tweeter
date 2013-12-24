
$(document).ready(function($) {
	
	//$("input[type=submit], button").button();
	//$("input[type=reset]").button();
	//$("#radio").buttonset();
	
	$("#formfield-pdate").datepicker({
		changeMonth : true,
		changeYear : true,
		maxDate: "+0"
	}).datepicker('setDate', new Date()); // pour mettre la date du jour par défaut

	$("#btn-posttweet").click(function(event) {
		var data = new FormData();
		data.append('photofile', $('#formfield-photourl')[0].files[0]);
		data.append('username',  $('#formfield-username').val());
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
				clearErrorMsg();
			},
			error : printErrorMsg("Une erreur est survenue. Le tweet n'a pas pu être posté.")
		});
	});

	/* ================== */
	/* autocompletion     */
	/* ================== */

	var availableTags = new Array();
	var availableUsers = new Array();

	$.ajax({
		type : 'GET',
		url : "http://localhost:9000/FMIN362-Tweeter/resources/tags/get",
		//contentType : "application/json", // marche pas avec ça
		success : function(data) {
			$(data).each(function(i) {
				availableTags[i] = this.tagname;
			});
			clearErrorMsg();
		},
		error : printErrorMsg("Erreur: n'a pas pu récuperer la liste des tags.")
	});

	$.ajax({
		type : 'GET',
		url : "http://localhost:9000/FMIN362-Tweeter/resources/users/get",
		//contentType : "application/json; charset=UTF-8",
		success : function(data) {
			$(data).each(function(i) {
				availableUsers[i] = this.username;
			});
			clearErrorMsg();
		},
		error : printErrorMsg("Erreur: n'a pas pu récuperer la liste des utilisateurs.")
	});
	
	function split(val) {
		return val.split(/,\s*/);
	}

	function extractLast(term) {
		return split(term).pop();
	}

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


