
$(function(){
	
	var validator = $("#form1").validate({
		onsubmit : false, 
		rules: {
			'msg': {
				required: true,
				maxlength:50
			}
		},
		messages: { // 出错提示信息
			'msg': {
				required:"请填写下线原因",
				maxlength:"下线原因字符长度不能超过{0}"
			}
		},
		errorPlacement: $.handleError,
		success: $.handleSuccess,
		invalidHandler: $.invalidAlertHandler
	});
	
	var isSaving = false;
	// 单击保存按钮提交表单事件
	$('#saveTariff').click(function (e) { 
		if (isSaving) {
			return false;
		}
		if (validator.form()) {
			var url = url= window.ctx+ '/select/dProgramClose.do';
			var $this = $(this);
			$this.removeClass('bt-primary').addClass('bt-secondary').text('保存中...').prop('disabled', true);
			var data = $('#form1').serializeArray();
			$.post(url, data, function(feedback) {
				alert(feedback.message);
				if(feedback.successful) {
					location.href=window.ctx+'/select/findProgram.do'
				}else{
					$this.removeClass('bt-secondary').addClass('bt-primary').text('保存').prop('disabled', false);
					isSaving = false;
				}
			});
		}
	});
})