$(function(){
	var validator = $("#fom").validate({
		onsubmit : false, 
		rules: {
			'name': {
				required: true,
				remote : {
					url : window.ctx+'/group/checkName.do',
					data : {
						'oldName' : function() {
							return $("#oldName").val();
						}
					}
				}
			}, 
			'enName': {
				required: true,
				remote : {
					url : window.ctx+'/group/checkEname.do',
					data : {
						'oldEnName' : function() {
							return $("#oldEnName").val();
						}
					}
				}
			}
		},
		messages: { // 出错提示信息
			'name' : {
					required : "组名称不能为空!",
					remote : '此组名称已被使用，请重新输入'
			},
			'enName' : {
					required : "组编码不能为空!",
					remote : '此组编码已被使用，请重新输入'
			}
		},
		errorPlacement: $.handleError,
		success: $.handleSuccess,
		invalidHandler: $.invalidAlertHandler
	});
	
	var isSaving = false;
	$('#saveGroup').click(function (e) {
		if (isSaving) {
			return false;
		}
		if (validator.form()) {
			var url = window.ctx+'/group/saveGroup.do';
			var data = $('#fom').serializeArray();
			isSaving = true;
			$.post(url, data, function(feedback) {
				alert(feedback.message);
				if(feedback.successful) {
					location.href=window.ctx+'/group/findGroupList.do'
				}else{
					isSaving = false;
				}
			});
		}
	});
});
