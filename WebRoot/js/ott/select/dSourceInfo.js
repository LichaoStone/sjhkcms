
$(function(){
	
	var validator = $("#form1").validate({
		onsubmit : false, 
		rules: {
			'bitrate': {
				required: true,
				maxlength: 5
			}, 
			'playUrl': {
				required: true,
				maxlength: 200
			},
			'ftpUrl': {
				maxlength: 128
			}
		},
		messages: { // 出错提示信息
			'bitrate': {
				required:"请填写码率",
				maxlength:"码率字符长度不能超过{0}"
			},
			'playUrl': {
				required:"请填写播放源地址",
				maxlength:"播放源地址字符长度不能超过{0}"
			},
			'ftpUrl': {
				maxlength:"ftp地址字符长度不能超过{0}"
			}
		},
		errorPlacement: $.handleError,
		success: $.handleSuccess,
		invalidHandler: $.invalidAlertHandler
	});
	
	var isSaving = false;
	// 单击保存按钮提交表单事件
	$('#saveSource').click(function (e) { 
		if (isSaving) {
			return false;
		}
		if (validator.form()) {
			var url = '';
			if($('#id').val() == ''){
				 url= window.ctx+ '/select/addDramaSource.do';
			}else{
				 url= window.ctx+ '/select/updateDramaSource.do';
			}
			var $this = $(this);
			$this.removeClass('bt-primary').addClass('bt-secondary').text('保存中...').prop('disabled', true);
			
			var data = $('#form1').serializeArray();
			$.post(url, data, function(feedback) {
				alert(feedback.message);
				if(feedback.successful) {
					location.href=window.ctx+'/select/findDramaSource.do?childId='+$('#childId').val()
				}else{
					$this.removeClass('bt-secondary').addClass('bt-primary').text('保存').prop('disabled', false);
					isSaving = false;
				}
			});
		}
	});
})