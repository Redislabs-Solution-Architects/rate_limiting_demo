
	stompClient = null;
	connect();
	
	setInterval(function() {
	  checkQuota($('#username').text());
	}, 3000);

	$("#saveBtn").click(function() {
		
		$.ajax({
			   url: '/api/prospects',
			   error: function (jqXHR, exception) {
			         //$('#tableId tbody').append('<tr><td></td><td></td><td></td><td></td><td></td><td>Quota exceeded: Request limit reached</td><td></td><td></td></tr>');
			         $('#errMsg').html('Quota exceeded: Request limit reached');
			         $('#errDiv').show();
			   },
			   dataType: "json",
			   success: function(data, textStatus, xhr) {
				   $('#tableId tbody > tr').remove();
				   var ss = JSON.stringify(data)
				   var res = $.parseJSON(ss);
				   
				   if(xhr.status == 200) {
					   $('#errMsg').html('');
			           $('#errDiv').hide();
					   $.each(res, function(k, v) {
						 //$('#tableId tr:last').after('<tr><td>' + v.id + '</td><td>' + v.contactNo + '</td><td>' + v.address + '</td><td>' + v.email + '</td><td>' + v.creditScore + '</td><td>' + v.requirement + '</td></tr>');
						   $('#tableId tbody').append('<tr><td>' + v.id + '</td><td>' + v.contactNo
							   + '</td><td>' + v.address + '</td><td>' + v.email + '</td><td>' + v.creditScore
							   + '</td><td>' + v.liability + '</td><td>' + v.defaults + '</td><td>' + v.requirement + '</td></tr>');

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
	
			
	function connect() {
	    stompClient = Stomp.client('ws://localhost:8080/status');
	    stompClient.connect({}, function (frame) {
	        stompClient.subscribe('/topic/messages', function (response) {
	            message = JSON.parse(response.body).quota;
	            $("#availableQuota").text(message);
	        });
	    });
	}
	    
	function checkQuota(username) {
		stompClient.send("/app/quota", {}, username);
	}

	
	