
$(function(){
	
	var validator = $("#form1").validate({
		onsubmit : false, 
		rules: {
			'tariffName': {
				required: true,
				maxlength: 16,
				remote: {
					 		url: window.ctx+'/select/checkTariffName.do',
					 		data: {
					 			'oldName': function(){
					 				return $('#oldName').val()
					 			}
					 		}
						 }
			}, 
			'tariffValue': {
				required: true,
				number: true
			},
			'tariffType': {
				required: true
			},
			'billId': {
				required: true
			}
		},
		messages: { // 出错提示信息
			'tariffName': {
				required:"请填写资费名称",
				maxlength:"资费名称字符长度不能超过{0}",
				remote: '此资费名称已存在，请重新输入一个'
			},
			'tariffValue': {
				required: "请填写资费值",
				number: "请填写数字"
			},
			'tariffType': "请选择资费类型",
			'billId':"请选择计费项"
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
			var url = '';
			if($('#id').val() == ''){
				 url= window.ctx+ '/select/addTariff.do';
			}else{
				 url= window.ctx+ '/select/updateTariff.do';
			}
			var $this = $(this);
			$this.removeClass('bt-primary').addClass('bt-secondary').text('保存中...').prop('disabled', true);
			
			var data = $('#form1').serializeArray();
			$.post(url, data, function(feedback) {
				alert(feedback.message);
				if(feedback.successful) {
					location.href=window.ctx+'/select/findTariff.do'
				}else{
					$this.removeClass('bt-secondary').addClass('bt-primary').text('保存').prop('disabled', false);
					isSaving = false;
				}
			});
		}
	});
})