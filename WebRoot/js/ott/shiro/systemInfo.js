$(function(){
	var validator = $("#fom").validate({
		onsubmit : false, 
		rules: {
			'name': {
				required: true
			}, 
			'ename': {
				required: true
			},
			'contextPath': {
				required: true
			},
			'tablePrefix': {
				required: true
			}
		},
		messages: { // 出错提示信息
			'name': "系统名称不能为空!",
			'ename': "系统编码不能为空!",
			'contextPath': "系统上下文不能为空!",
			'tablePrefix': "系统表名前缀不能为空!"
		},
		errorPlacement: $.handleError,
		success: $.handleSuccess,
		invalidHandler: $.invalidAlertHandler
	});
	var isSaving = false;
	$('#saveSystem').click(function (e) {
		if (isSaving) {
			return false;
		}
		if (validator.form()) {
			isSaving= true;
			var url = window.ctx+'/system/saveSystem.do';
			var data = $('#fom').serializeArray();
			$.post(url, data, function(feedback) {
			        alert(feedback.message);
					if (feedback.successful) {
						location.href = window.ctx+'/system/findSystemList.do';
					}else{
						isSaving = false;
					}
			});
		}
	});
	
});
