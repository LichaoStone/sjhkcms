$(function() {
	var validator = $("#fom").validate({
		onsubmit : false,
		rules : {
			'loginname' : {
				required : true
			},
			'phone' : {
				required : true,
				phoneCN : true,
				remote: {
			 		url: window.ctx+ '/user/checkPhoneIsUse.do',
			 		data: {
			 			'oldPhone': function(){
			 				return $('#oldPhone').val()
			 			}
			 		}
		   		}
			}
		},
		messages : { // 出错提示信息
			'loginname' : "真实姓名不能为空!",
			'phone' : {
				required : "联系方式不能为空!",
				remote:"此联系方式已经注册过，请重新输入一个"
			}
		},
		errorPlacement : $.handleError,
		success : $.handleSuccess,
		invalidHandler : $.invalidAlertHandler
	});
	
	$('#editUser').click(function(e) {
		if (validator.form()) {
			var url = window.ctx+ '/user/modifyBaseInfo.do';
			var data = $('#fom').serializeArray();
			
			$.post(url, data, function(feedback) {
				alert(feedback.message);
				location.href = location.href;
			});
		}
	});
});
