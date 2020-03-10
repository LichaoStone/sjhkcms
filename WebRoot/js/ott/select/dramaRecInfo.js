$(function(){
	
	var validator = $("#form1").validate({
		onsubmit : false, 
		rules: {
			'cList': {
				required: true
			}
		},
		messages: { // 出错提示信息
			'cList': "请选择推荐位"
		},
		errorPlacement: $.handleError,
		success: $.handleSuccess,
		invalidHandler: $.invalidAlertHandler
	});
	var isSaving = false;
	// 单击保存按钮提交表单事件
	$('#saveDramaRec').click(function (e) { 
		if (isSaving) {
			return false;
		}
		var nodes = checkNodeFn();
		$('#cList').val(nodes.join(','));
		if (validator.form()) {
			isSaving = true;
			var url= window.ctx+ '/recommend/addDramaRecList.do';
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


function checkNodeFn() {
	var $nodes = $('input:checked[name="nodes"]');
	var array = [];
	if ($nodes && $nodes.length > 0) {
		for (var i = 0; i < $nodes.length; i++) {
			array.push($($nodes[i]).val());
		}
	}
	return array;
}
