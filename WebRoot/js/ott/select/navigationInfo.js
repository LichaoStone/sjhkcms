$(function(){
	if('1'==$("#xz").val()){
		$.get(window.ctx + "/type/changeParentId.do", 
		function(res) {
			$("#sequence").val(res.sequence);
		},"json");
	}
	var validator = $("#form1").validate({
			onsubmit : false, 
			rules: {
				'parentId': {
					required: true
				},
				'name': {
					required: true,
					remote:{
						url : window.ctx + '/type/checkNavigationByName.do',
						data : { 	
							'oldName' : function() {
								return $("#oldName").val();
							},
							'parentId':function(){
								return $('#parentId').val();
							}
						}
					}
				},
				'code': {
					required: true,
					remote:{
						url : window.ctx + '/type/checkNavigationByCode.do',
						data : { 	
							'oldCode' : function() {
								return $("#oldCode").val();
							}
						}
					}
				},
				'sequence': {
					required: true,
					digits:true
				},
				'sort': {
					required: true,
					digits:true
				},
				'isdl_display':{
					required: true,
					digits: true
				}
			},
			messages: { // 出错提示信息
				'parentId': {
					required:"请选择所属栏目"
						},
				'name': {
					required:"请填写名称",
					remote:"名称已存在,请重新输入"
						},
				'code': {
					remote: "编码已存在,请重新输入",
					required:"请填写编码"
						},
				'sequence':{
					required:"请填写排序值",
					digits:"排序值只能是整数值"
				},
				'sort':{
					required:"请填写推荐排序值",
					digits:"推荐排序值只能是整数值"
				},
				'isdl_display':{
					required:"请填写是否独立展示",
					digits:"独立展示值只能是整数值"
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
				 url= window.ctx+ '/type/addDramaNavigation.do';
			}else{
				 url= window.ctx+ '/type/updateDramaNavigation.do';
			}
			var $this = $(this);
			$this.removeClass('bt-primary').addClass('bt-secondary').text('保存中...').prop('disabled', true);
			var data = $('#form1').serializeArray();
				$.post(url, data, function(feedback) {
					if(feedback.successful) {
						if($('#tj').val()=='1'){
							location.href=window.ctx+'/type/findNgByPId.do?parentId='+$('#parentId').val()
						}else{
							location.href=window.ctx+'/type/findNavigation.do?parentId='+$('#parentId').val()
						}
						
					}else{
						$this.removeClass('bt-secondary').addClass('bt-primary').text('保存').prop('disabled', false);
						isSaving = false;
					}
				});
		}
	});
	
	//汉字转为首拼音字母
	var oldName = $('#oldName').val();
	$('#name').blur(function(e){
		if (oldName != $(this).val()) {
			makePyToThis($(this).val(), 'code');
			oldName = $(this).val();
		}
	});
	
	$('#parentId').change(function(){
		var parentId=$(this).children('option:selected').val();
		$.get(window.ctx + "/type/changeParentId.do?parentId="+parentId , 
				function(data) {
					$("#sequence").val(data.sequence);
				},"json");
	});
	
	$('#imgurlFile').change(
			function(e) {
				new uploadPreview(this, {
					DivShow : "preview",
					ImgShow : "imghead"
				}).show();
			});
	
	$('#saveImgurl').click(function(e) {
		ajaxFileUpload("imgurlFile", $(this), "imgurl")
	});
	
	// 上传文件
	function ajaxFileUpload(fileId, $this, imgId) {
		var f = $('#' + fileId).val();
		if (f == '') {
			$('#top-message').text('请选择文件!').fadeIn('slow').delay(5000).fadeOut(
					'slow');
			return;
		}
		$('#uploadImg').show();
		$this.prop('disabled', true).removeClass('bt-primary').addClass(
				'bt-secondary').text('上传中...');
		$.ajaxFileUpload({
			url : window.ctx + '/common/uploadFile.do?fileId=' + fileId, // 需要链接到服务器地址
			fileElementId : fileId, // 文件选择框的id属性
			dataType : 'text',
			success : function(data, status, e) {
				var fb = $.parseJSON(data);
				$('#uploadImg').hide();
				if (fb.result == '1') {
					$('#' + imgId).val(fb.msg);
					$this.prop('disabled', false).removeClass('bt-primary')
							.addClass('bt-secondary').text('上传完成');
				} else {
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
				$this.prop('disabled', false).removeClass('bt-primary').addClass(
						'bt-secondary').text('上传');
			}
		});
	}
})
