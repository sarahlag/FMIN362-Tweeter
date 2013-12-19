
$(document).ready(function($) {

	$("#formfield-pdate").datepicker({
		changeMonth : true,
		changeYear : true,
		maxDate: "+0"
	});

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
				listTweets(resp);
			}
		});
	});

});


