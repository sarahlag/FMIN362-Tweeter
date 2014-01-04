
$(document).ready(function($) {
	
	$("input[type=submit], button").button();
	
	$("#btn-login").click(function(event) {
		var username_value = $('#formfield-login-username').val();
		var passwd = $('#formfield-login-passwd').val();
		var data = "username="+username_value
					+"&passwd="+passwd;
		
		$.ajax({
			type : 'POST',
			url : "http://localhost:9000/FMIN362-Tweeter/resources/users/login",
			data : data,
			success : function(resp) {
				writeCookie('username', username_value);
				location.href="wall.html";
			},
			error : function(resp) {
					clearErrorMsg();
					printErrorMsg("Une erreur est survenue : "+resp.responseText);
			}
					
		});
	});
	
	$("#btn-register").click(function(event) {
		var username_value = $('#formfield-register-username').val();
		var passwd = $('#formfield-register-passwd').val();
		var passwd_verif = $('#formfield-register-passwd-verify').val();
		
		if (passwd != passwd_verif)
		{
			clearErrorMsg();
			printErrorMsg("Les deux mots de passe ne correspondent pas.");
			return;
		}
		
		var data = "username="+username_value
					+"&passwd="+passwd;
		
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
				printErrorMsg("Une erreur est survenue : "+resp.responseText);
			}
		});
	});
	
});


