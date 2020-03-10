$(function(){

	var validator = $("#dictForm").validate({
		onsubmit : true, 
		rules: {
	       'dicName': {
				required: true,
				remote: {
				 	url: window.ctx+'/dict/findDictByName.do',
				 	data:{
				 		'dicType' : function() {
							return $("#dicType").val();
						},
						'oldName' : function() {
							return $("#oldName").val();
						}
				 	}
				 }		 
			},
			'dicValue':{
				 required: true,
				 remote: {
				 	url: window.ctx+'/dict/findDictByValue.do',
				 	data:{
				 		'dicType' : function() {
							return $("#dicType").val();
						},
						'oldValue' : function() {
							return $("#oldValue").val();
						}
				 	}
				 }	
			},
			'orderId':{
			   digits:true
			}
		},
		messages: { // 出错提示信息
			'dicName': {
				required:'请填写字典名称',
				remote: '此字典名称已经在所属字典下使用过，重新输入一个'
			},
			'dicValue':{
			    required:'请填写字典值',
				remote: '此字典值已经在所属字典下使用过，重新输入一个'
			},
			'orderId':{
			    digits:'填写排序，只能为数字'
			}
		},
		errorPlacement: $.handleError,
		success: $.handleSuccess,
		invalidHandler: $.invalidAlertHandler
	});
	//保存字典
	$('#saveDict').click(function (e) { 
		if (validator.form()) {
			var url = window.ctx+'/dict/addDict.do';
			if ($('#id').val() != '') {
				url = window.ctx + '/dict/updateDict.do';
			}
			if($('#dicType').val()==''){
				$('#dicType').val('0');
			}
			var data = $('#dictForm').serializeArray();
			$.post(url, data, function(feedback) {
		             alert(feedback.message);
					if(feedback.successful) {
					   	location.href=window.ctx+'/dict/findAll.do'
					}
			});
		}
	});
	
	$('#selectDict').click(function(e){
		$('#dictList').show();
	});

})
	
