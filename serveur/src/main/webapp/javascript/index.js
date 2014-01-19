
$(document).ready(function($) {
	
	$("input[type=submit], button").button();
	
	$("#btn-login").click(function(event) {
		var username_value = $('#formfield-login-username').val();
		var passwd = $('#formfield-login-passwd').val();
		var data = "username="+username_value
					+"&passwd="+passwd;
		
		$.ajax({
			type : 'POST',
			url : "/FMIN362-Tweeter/resources/users/login",
			data : data,
			success : function(resp) {
				if (resp === "Login successful as admin")
					writeCookie('is_admin', "true");
				else
					writeCookie('is_admin', "false");
				writeCookie('username', username_value);
				location.href="wall.html";
			},
			error : function(resp) {
					clearMsg();
					printMsg("Une erreur est survenue : "+resp.responseText);
			}
					
		});
	});
	
	$("#btn-register").click(function(event) {
		var username_value = $('#formfield-register-username').val();
		var passwd = $('#formfield-register-passwd').val();
		var passwd_verif = $('#formfield-register-passwd-verify').val();
		
		if (passwd != passwd_verif)
		{
			clearMsg();
			printMsg("Les deux mots de passe ne correspondent pas.");
			return;
		}
		
		var data = "username="+username_value
					+"&passwd="+passwd;
		
		$.ajax({
			type : 'POST',
			url : "/FMIN362-Tweeter/resources/users/register",
			data : data,
			success : function(resp) {
				writeCookie("is_admin", "false");
				writeCookie("username", username_value);
				location.href="wall.html";
			},
			error : function(resp) {
				clearMsg();
				printMsg("Une erreur est survenue : "+resp.responseText);
			}
		});
	});
	
});


