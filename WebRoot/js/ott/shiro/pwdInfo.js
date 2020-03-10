$(function() {
	var validator = $("#form1").validate({
				onsubmit : false,
				rules : {
					'password' : {
						maxlength : 15,
						minlength : 6,
						password: {
							username : "#password",
							force : false
						},
						required : true
					},
					'password_confirm' : {
						required : true,
						equalTo: "#password"
					}
				},
				messages : { // 出错提示信息
					'password' : {
						required : "用户密码不能为空!",
						minlength : "用户密码长度不能少于{0}!",
						maxlength : "用户密码长度不能超过{0}!"
					},
					'password_confirm' : {
						required:"确认密码不能为空!",
						equalTo:"确认密码与用户密码不一致"
					}
				},
				errorPlacement : $.handleError,
				success : $.handleSuccess,
				invalidHandler : $.invalidAlertHandler
			});
			
	var isSaving = false;
	$('#savePwd').click(function(e) {
		if (isSaving) {
			return false;
		}
		if (validator.form()) {
			var url = window.ctx+'/user/resetPwd.do';
			var data = $('#form1').serializeArray();
			isSaving = true;
			$.post(url, data, function(feedback) {
				alert(feedback.message);
				if (feedback.successful) {
					location.href=window.ctx+'/user/findUsersList.do'
				}else{
					isSaving = false;
				}
			});
		}
	});

});

