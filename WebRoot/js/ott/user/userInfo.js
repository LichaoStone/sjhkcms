
$(function(){
	
	var validator = $("#form0").validate({
		onsubmit : false, 
		rules: {
			'userId': {
				required: true,
				maxlength: 16,
				remote: {
					 		url: window.ctx+'/userinfo/checkUserId.do'
						 }
			}, 
			'trueName': {
				required: true
			},
			'nickName': {
				required: true
			},
			'regTime': {
				required: true
			},
			'phone': {
				mobileCN: true
			},
			'email': {
				email: true
			}
		},
		messages: { // 出错提示信息
			'userId': {
				required:"请填写用户账号",
				maxlength:"用户账号字符长度不能超过{0}",
				remote: '此用户账号已存在，请重新输入一个'
			},
			'trueName': {
				required: "请填写用户名"
			},
			'nickName': "请填写昵称",
			'regTime':"请选择注册时间",
			'phone':"手机格式不正确",
			'email':"邮箱格式不正确"
		},
		errorPlacement: $.handleError,
		success: $.handleSuccess,
		invalidHandler: $.invalidAlertHandler
	});
	
	var isSaving = false;
	// 单击保存按钮提交表单事件
	$('#saveUser').click(function (e) { 
		if (isSaving) {
			return false;
		}
		if (validator.form()) {
			var url= window.ctx+ '/userinfo/addUser.do';
			if($('#id').val()!=""){
				url =  window.ctx+ '/userinfo/modifyUser.do';
			}
			var $this = $(this);
			$this.removeClass('bt-primary').addClass('bt-secondary').text('保存中...').prop('disabled', true);
			var data = $('#form0').serializeArray();
			$.post(url, data, function(feedback) {
				alert(feedback.message);
				if(feedback.successful) {
					location.href=window.ctx+'/userinfo/findUsers.do'
				}else{
					$this.removeClass('bt-secondary').addClass('bt-primary').text('保存').prop('disabled', false);
					isSaving = false;
				}
			});
		}
	});
})