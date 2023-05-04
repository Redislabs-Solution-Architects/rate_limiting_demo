
	$("#saveBtn").click(function() {
		$('#tableId tbody > tr').remove();
		$.ajax({
			   url: '/api/prospects',
			   error: function (jqXHR, exception) {
			         $('#tableId tbody').append('<tr><td></td><td></td><td></td><td></td><td>Quota exceeded: Request limit reached</td><td></td></tr>');
			         
			   },
			   dataType: "json",
			   success: function(data, textStatus, xhr) {
				   var ss = JSON.stringify(data)
				   var res = $.parseJSON(ss);
				   
				   if(xhr.status == 200) {
					   $.each(res, function(k, v) {
						   //$('#tableId tr:last').after('<tr><td>' + v.id + '</td><td>' + v.contactNo + '</td><td>' + v.address + '</td><td>' + v.email + '</td><td>' + v.creditScore + '</td><td>' + v.requirement + '</td></tr>');
					   	 $('#tableId tbody').append('<tr><td>' + v.id + '</td><td>' + v.contactNo + '</td><td>' + v.address + '</td><td>' + v.email + '</td><td>' + v.creditScore + '</td><td>' + v.requirement + '</td></tr>');

					   });
				   }
			   },
			   complete: function(xhr, textStatus) {
			       if (xhr.status == 999) {
			    	   window.location.replace("login");
			       }
			   },
			   method: "GET",
			   contentType:"application/json"
			});
	});
	
	