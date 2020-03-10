$(function(){	
	$('#loginName').focus();
	if(top.location!==self.location){   
	  	top.location=self.location;   
	}
	//new EmailTip('#loginName', '#loginName');
	
	// 按"回车"跳入"密码"区
	$('#loginName').keypress(function(e) {
		if(e.keyCode == 13) { // the 'enter' key is pressed
			$('#pass').focus();
			return false;
		}
	});
		
	// 按"回车"确定
	$('#pass').keypress(function(e) {
		if(e.keyCode == 13) { // the 'enter' key is pressed
			$('#vdcode').focus();
			return false;
		}
	});
	
	// 按"回车"确定
	$('#vdcode').keypress(function(e) {
		if(e.keyCode == 13) { // the 'enter' key is pressed
			$('#loginSubmit').click();
			return false;
		}
	});
	
	// 单击提交提交表单事件
	var isSaving = false;
	$('#loginSubmit').click(function (e) { 
		var validing = false;
		if ($('#loginName').val().length <= 0) {
			ddb.alert('请输入账号。', function(){
				$('#loginName').focus();
			});
			return false;
		}
		if ($('#pass').val().length <= 0) {
			ddb.alert('请输入密码。', function(){
				$('#pass').focus();
			});
			return false;
		}
		
		if ($('#vdcode').val().length <= 0) {
			ddb.alert('请输入验证码。', function(){
				$('#vdcode').focus();
			});
			return false;
		}
		
		validing = true;
		if (isSaving) {
			return false;
		}
		if (validing) {
			encryption();
			isSaving = true;
			var url= window.ctx+ '/shiro/login.htm';
			$("#form1").attr('action', url).submit();
		}
	});
	
	$('#vdcodeImg').click(function(e){
		$(this).attr('src',window.ctx+'/common/getVeCode.htm?t='+ new Date());
	});
});

function encryption(inputCtrl) {
	var textval = $("#pass").val();
	var hash = hex_md5(textval);
	$("#password").val(hash);
	$("#pass").val('');
}