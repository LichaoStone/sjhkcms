
$(function(){
	
	var validator = $("#form1").validate({
		onsubmit : false, 
		rules: {
			'sort': {
				required: true,
				number: true
			}
		},
		messages: { // 出错提示信息
			'sort': {
				required: "请填写排序值",
				number: "排序只能是数字"
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
				 url= window.ctx+ '/recommend/addRecommendPst.do';
			}else{
				 url= window.ctx+ '/recommend/updateRecommendPst.do';
			}
			var $this = $(this);
			$this.removeClass('bt-primary').addClass('bt-secondary').text('保存中...').prop('disabled', true);
			
			var data = $('#form1').serializeArray();
			$.post(url, data, function(feedback) {
				alert(feedback.message);
				if(feedback.successful) {
					location.href=window.ctx+'/recommend/toRecommendPst.do?dramaId='+$('#dramaId').val()+'&dramaName='+$('#dramaName').val()
				}else{
					$this.removeClass('bt-secondary').addClass('bt-primary').text('保存').prop('disabled', false);
					isSaving = false;
				}
			});
		}
	});
})