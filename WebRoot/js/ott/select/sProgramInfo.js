$(function(){
	
	var validator = $("#form1").validate({
		onsubmit : false, 
		rules: {
			'name': {
				required: true,
				remote:{
					url: window.ctx+'/select/checkSPName.do',
					data : {
						'oldName' : function() {
							return $("#oldName").val();
						}
					}
				}
			},
			'timeLongth':{
				digits: true
			},
			'playUrl':{
				required: true
			},
			'limitTime':{
				required: true,
				digits: true
			},
			'price':{
				required: true
			}
		},
		messages: { // 出错提示信息
			'name': {
				required:'请填写节目名称',
				remote: '此名称节目已录入，请输入新名称'
			},
			'timeLongth':{
				digits: '播放时长只能是整数值'
			},
			'playUrl':{
				required: '播放地址不能为空'
			},
			'limitTime':{
				required: '请填写免费时长',
				digits:'免费时长只能是整数值'
			},
			'price':{
				required:'请选择点播价格'
			}
		},
		errorPlacement: $.handleError,
		success: $.handleSuccess,
		invalidHandler: $.invalidAlertHandler
	});
	// 单击保存按钮提交表单事件
	var isSaving = false;
	$('#saveProgram').click(function (e) { 
		if (isSaving) {
			return false;
		}
		if (validator.form()) {
			isSaving = true;
			var url= window.ctx+ '/select/addSProgram.do';
			if($('#id').val() !=''){
				url= window.ctx+ '/select/updateSProgram.do';
			}
			var nodes = checkNodeFn();
			$('#ttype').val(nodes.join(','));
			
			var $this = $(this);
			$this.removeClass('bt-primary').addClass('bt-secondary').text('保存中...').prop('disabled', true);
			var data = $('#form1').serializeArray();
			$.post(url, data, function(feedback) {
				alert(feedback.message);
				if(feedback.successful) {
					location.href=window.ctx+'/select/findSProgram.do'
				}else{
					$this.removeClass('bt-secondary').addClass('bt-primary').text('保存').prop('disabled', false);
					isSaving = false;
				}
			});
		}
	});
	
	
	$('#saveLogoImg').click(function(e){
		ajaxFileUpload("logoFile", $(this), "logoUrl")
	});
	$('#saveWlogoImg').click(function(e){
		ajaxFileUpload("wlogoFile", $(this), "wlogoUrl")
	});
	$('#saveHlogoImg').click(function(e){
		ajaxFileUpload("hlogoFile", $(this), "hlogoUrl")
	});
	
	$('#logoFile').change(function(e){
		new uploadPreview(this,{  DivShow: "preview", ImgShow: "imghead" }).show();
	});
	$('#wlogoFile').change(function(e){
		new uploadPreview(this,{  DivShow: "preview2", ImgShow: "imghead2" }).show();
	});
	$('#hlogoFile').change(function(e){
		new uploadPreview(this,{  DivShow: "preview3", ImgShow: "imghead3" }).show();
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
// 上传文件
function ajaxFileUpload(fileId, $this, imgId) {
	var f = $('#'+fileId).val();
	if (f == '') {
		$('#top-message').text('请选择文件!').fadeIn('slow').delay(5000)
				.fadeOut('slow');
		return;
	}
	$('#uploadImg').show();
	$this.prop('disabled', true).removeClass('bt-primary')
			.addClass('bt-secondary').text('上传中...');
	$.ajaxFileUpload({
				url : window.ctx
						+ '/programs/uploadProgramsFile.do?channelId=1000000&fileId='
						+ fileId, // 需要链接到服务器地址
				fileElementId : fileId, // 文件选择框的id属性
				dataType : 'text',
				success : function(data, status, e) {
					var fb =$.parseJSON(data);
					$('#uploadImg').hide();
					if(fb.result=='1') {
						$('#'+imgId).val(fb.msg);
						$this.prop('disabled', true).removeClass('bt-primary')
							.addClass('bt-secondary').text('上传完成');
					}else{
						$('#top-message').text(fb.msg).fadeIn('slow').delay(5000)
							.fadeOut('slow');
						$this.prop('disabled', false).removeClass('bt-primary')
							.addClass('bt-secondary').text('上传');
					}
				},
				error : function(data, status, e) {
					$('#top-message').text("图片上传出错").fadeIn('slow').delay(5000)
							.fadeOut('slow')
					$('#uploadImg').hide();
					$this.prop('disabled', false).removeClass('bt-primary')
							.addClass('bt-secondary').text('上传');
				}
	});
}