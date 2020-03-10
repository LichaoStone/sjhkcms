$(function() {
	var validator = $("#fom").validate({
				onsubmit : false,
				rules : {
					'userName' : {
						required : true,
						remote: {
					 		url: window.ctx+'/user/checkUserNameIsUse.do',
					 		data: {
					 			'oldName': function(){
					 				return $('#oldName').val()
					 			}
					 		}
						 }
					},
					'password' : {
						maxlength : 15,
						minlength : 6,
						required : true
					},
					'loginname' : {
						required : true
					},
					'areaid' : {
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
					'userName' : {
						required: "用户名不能为空!",
						remote: '此用户名已经注册过，请重新输入一个'
					},
					'password' : {
						required : "密码不能为空!",
						minlength : "密码长度不能少于{0}!",
						maxlength : "密码长度不能超过{0}!"
					},
					'loginname' : "真实姓名不能为空!",
					'areaid' : "地区不能为空!",
					'phone' : {
						required : "联系方式不能为空!",
						phoneCN : "你输入的联系方式格式不正确，请重新输入！\n",
						remote:"此联系方式已经注册过，请重新输入一个"
					}
				},
				errorPlacement : $.handleError,
				success : $.handleSuccess,
				invalidHandler : $.invalidAlertHandler
			});
	var isSaving = false;
	$('#saveUser').click(function(e) {
		if (isSaving) {
			return false;
		}
				if (validator.form()) {
					var url = window.ctx+'/user/saveUser.do';
					var data = $('#fom').serializeArray();
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

	// 选择商家
	$('#selectPartner').click(function(e) {
		$('#partnerList').show();
	});
	
	
	$('#checkOk').click(function(e){
		var $partner = $("input[name=checkPartner]:checked");
		if($partner.val() == null){
			alert("请选择一个合作商");
			return false;
		}else{
			$('#mId').val($partner.attr('pid'));
			$('#partnerName').val($partner.attr('pname'));
			$('#partnerList').hide();
		}
	});
});

function _closePartnerOption(){
	$('#partnerList').hide();
}


