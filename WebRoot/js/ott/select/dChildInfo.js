
$(function(){
	
	var validator = $("#form1").validate({
		onsubmit : false, 
		rules: {
			'title': {
				required: true,
				maxlength: 16
			}, 
			'timeLongth': {
				number: true
			},
			'dramaDesc':{
				maxlength: 200
			}
		},
		messages: { // 出错提示信息
			'title': {
				required:"请填写剧集名称",
				maxlength:"剧集名称字符长度不能超过{0}"
			},
			'timeLongth': {
				number: "播放时长只能是数字"
			},'title': {
				maxlength:"剧集描述字符长度不能超过{0}"
			}
		},
		errorPlacement: $.handleError,
		success: $.handleSuccess,
		invalidHandler: $.invalidAlertHandler
	});
	
	var isSaving = false;
	// 单击保存按钮提交表单事件
	$('#saveChild').click(function (e) { 
		if (isSaving) {
			return false;
		}
		if (validator.form()) {
			var url = '';
			if($('#id').val() == ''){
				 url= window.ctx+ '/select/addDramaChild.do';
			}else{
				 url= window.ctx+ '/select/updateDramaChild.do';
			}
			var $this = $(this);
			$this.removeClass('bt-primary').addClass('bt-secondary').text('保存中...').prop('disabled', true);
			
			var data = $('#form1').serializeArray();
			$.post(url, data, function(feedback) {
				alert(feedback.message);
				if(feedback.successful) {
					location.href=window.ctx+'/select/findDramaChild.do?dramaId='+$('#dramaId').val()+"&dramaName="+$('#dramaName').val()
				}else{
					$this.removeClass('bt-secondary').addClass('bt-primary').text('保存').prop('disabled', false);
					isSaving = false;
				}
			});
		}
	});
})