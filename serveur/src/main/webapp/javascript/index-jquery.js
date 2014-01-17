
$(document).ready(function($) {
	
	$("input[type=submit], button").button();
	//$("input[type=reset]").button();
	//$("#radio").buttonset();
	
	$("#btn-login").click(function(event) {
		var username_value = $('#formfield-login-username').val();
		var data = "username="+username_value
					+"&password="+$('#formfield-login-password').val();

		$.ajax({
			type : 'POST',
			url : "http://localhost:9000/FMIN362-Tweeter/resources/users/login",
			data : data,
			success : function(resp) {
				writeCookie('username', username_value);
				alert('u : '+ username_value + ', cookie : '+readCookie('username'));
				//location.href="wall.html";
			},
			error : function(resp) {
					clearErrorMsg();
					printErrorMsg("Une erreur est survenue. Veuillez vérifier vos informations de login.");
			}
					
		});
	});
	
	$("#btn-register").click(function(event) {
		var username_value = $('#formfield-register-username').val();
		var data = "username="+username_value
					+"&password="+username_value
					+"&password-verif="+$('#formfield-register-password-verify').val();
		
		$.ajax({
			type : 'POST',
			url : "http://localhost:9000/FMIN362-Tweeter/resources/users/register",
			data : data,
			success : function(resp) {
				writeCookie("username", username_value);
				location.href="wall.html";
			},
			error : function(resp) {
				clearErrorMsg();
				printErrorMsg("Une erreur est survenue. Impossible d'enregister le nouvel utilisateur.");
			}
		});
	});
	
});


