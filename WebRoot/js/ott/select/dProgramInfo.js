$(function() {

	var validator = $("#form1").validate({
		onsubmit : false,
		rules : {
			'name' : {
				required : true,
				maxlength : 100
			},
			'ptype' : {
				required : true
			},
			'cProvider' : {
				required : true
			},
			'keyword':{
				maxlength : 100
			},
			'summary':{
				maxlength : 1000
			},
			'actor':{
				maxlength : 200
			},
			'pcode':{
				required : true,
				maxlength : 16
			}
			
		},
		messages : { // 出错提示信息
			'name' : {
				required : '请填写资产名称',
				maxlength : '输入资产名称字符长度不能超过{0}'
			},
			'ptype' : {
				required : '请选择资产类型',
			},
			'cProvider' : {
				required : '请选择内容商'
			},
			'keyword':{
				maxlength : '输入关键字字符长度不能超过{0}'
			},
			'summary':{
				maxlength : '输入看点字符长度不能超过{0}'
			},
			'actor':{
				maxlength : '输入主演字符长度不能超过{0}'
			},
			'pcode':{
				required : '请填写资产简拼',
				maxlength : '输入资产简拼字符长度不能超过{0}'
			}
		},
		errorPlacement : $.handleError,
		success : $.handleSuccess,
		invalidHandler : $.invalidAlertHandler
	});
	// 单击保存按钮提交表单事件
	var isSaving = false;
	$('#saveProgram').click(
			function(e) {
				if (isSaving) {
					return false;
				}
				if (validator.form()) {
					isSaving = true;
					var url = window.ctx + '/select/addDProgram.do';
					if ($('#id').val() != '') {
						url = window.ctx + '/select/updateDProgram.do';
					}
					var $this = $(this);
					$this.removeClass('bt-primary').addClass('bt-secondary')
							.text('保存中...').prop('disabled', true);
					var data = $('#form1').serializeArray();
					$.post(url, data, function(feedback) {
						alert(feedback.message);
						if (feedback.successful) {
							location.href = window.ctx
									+ '/select/findDProgram.do'
						} else {
							$this.removeClass('bt-secondary').addClass(
									'bt-primary').text('保存').prop('disabled',
									false);
							isSaving = false;
						}
					});
				}
			});

	$('#saveCoverXImg').click(function(e) {
		ajaxFileUpload("coverXImgFile", $(this), "coverXImg")
	});
	$('#saveCoverDImg').click(function(e) {
		ajaxFileUpload("coverDImgFile", $(this), "coverDImg")
	});
	$('#saveRecXImg').click(function(e) {
		ajaxFileUpload("recXImgFile", $(this), "recXImg")
	});
	$('#saveRecDImg').click(function(e) {
		ajaxFileUpload("recDImgFile", $(this), "recDImg")
	});

	$('#coverXImgFile').change(
			function(e) {
				new uploadPreview(this, {
					DivShow : "preview",
					ImgShow : "imghead"
				}).show();
			});
	$('#coverDImgFile').change(
			function(e) {
				new uploadPreview(this, {
					DivShow : "preview2",
					ImgShow : "imghead2"
				}).show();
			});
	$('#recXImgFile').change(
			function(e) {
				new uploadPreview(this, {
					DivShow : "preview3",
					ImgShow : "imghead3"
				}).show();
			});
	$('#recDImgFile').change(
			function(e) {
				new uploadPreview(this, {
					DivShow : "preview4",
					ImgShow : "imghead4"
				}).show();
			});
	
	$('#selectDType').click(function(e){
		var $typeId = $('#typeName');
		var left = $typeId.offset().left;
		var top = $typeId.offset().top + $typeId.height() -5;
		$('#dTypeList').css({
			left : left,
			top : top
		}).show();
		$('#drama_tree')[0].tinytree.reload();
	});
	
	$('#drama_tree').tinytree({
		url : window.ctx+'/select/buildDramaTypeTree.do?dramaId='+$('#id').val(),
		autoload : false,
		checkboxSupport : true,
		onError : function(xhr,status,e) {alert(e.message);},
		showChildren : false,
		openAllNodes : false,
		clickNodeToCheckRadioOrCheckbox : true,
		alsoCheckParent : false,
		alsoCheckChildren : false //是否同时勾选子节点
	});

	$('#saveType').click(function(e){
		var $nodes = $('#drama_tree')[0].tinytree.getSelecetedNodes();
		var dTypeArr = new Array();
		var typeNameArr = new Array();
		$.each($nodes, function(i, node) {
			dTypeArr.push(node.nodeId);
			typeNameArr.push(node.nodeName);
		});
		$('#ttype').val(dTypeArr.join(","));
		$('#typeName').val(typeNameArr.join(","));
		$('#dTypeList').hide();
	});
	
	//汉字转为首拼音字母
	var oldName = $('#oldName').val();
	$('#name').blur(function(e){
		if (oldName != $(this).val()) {
			makePyToThis($(this).val(), 'pcode');
			oldName = $(this).val();
		}
	});
	
})

function _closeDtypeOption(){
	$('#dTypeList').hide();
}
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
					.fadeOut('slow');
			$('#uploadImg').hide();
			$this.prop('disabled', false).removeClass('bt-primary').addClass(
					'bt-secondary').text('上传');
		}
	});
}