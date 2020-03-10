$(function(){
	
	//汉字转为首拼音字母
	var oldName = $('#oldName').val();
	$('#name').blur(function(e){
		if (oldName != $(this).val()) {
			makePyToThis2($(this).val(), 'code');
			oldName = $(this).val();
		}
	});
	
	var validator = $("#form1").validate({
			onsubmit : false, 
			rules: {
				'code': {
					required: true
				},
				'sort': {
					required: true,
					digits:true
				}
			},
			messages: { // 出错提示信息
					'code': {
						required:"请填写编码"
							},
					'sort':{
						required:"请填写推荐排序值",
						digits:"推荐排序值只能是整数值"
					}
				},
			errorPlacement: $.handleError,
			success: $.handleSuccess,
			invalidHandler: $.invalidAlertHandler
		});
		
	var isSaving = false;
	// 单击保存按钮提交表单事件
	$('#saveType').click(function (e) { 
		if (isSaving) {
			return false;
		}
		if (validator.form()) {
			var url = '';
			isSaving = true;
			if($('#id').val() == ''){
				 url= window.ctx+ '/position/addDramaPosition.do';
			}else{
				 url= window.ctx+ '/position/updateDramaPosition.do';
			}
			var $this = $(this);
			$this.removeClass('bt-primary').addClass('bt-secondary').text('保存中...').prop('disabled', true);
			var data = $('#form1').serializeArray();
				$.post(url, data, function(feedback) {
					alert(feedback.message);
					if(feedback.successful) {
						location.href=window.ctx+'/position/findPosition.do'
					}else{
						$this.removeClass('bt-secondary').addClass('bt-primary').text('保存').prop('disabled', false);
						isSaving = false;
					}
				});
		}
	});
	
})
