$(function(){
	$('#queryProgram').click(function(e) {
		initPage();
		$('#queryForm').submit();
	});
	$("#inputExcel").on("click", function(){
		var fileName = $("input[name='fileApp']").val();
		if(fileName==""){
			alert("请选择要上传的文件");
			return;
		}
		$('#uploadImg').show();
        $(this).prop('disabled', true).removeClass('bt-primary')
		.addClass('bt-secondary').text('上传中...');
        uploadFileFn();
	});
	
	$('#resetJP').click(function(e) {
		var url = url = window.ctx+ '/select/resetProgramJP.do';
		if(!window.confirm("您确定要重置资产简拼！")){
			return false;
		}
		 $(this).prop('disabled', true).removeClass('bt-primary')
			.addClass('bt-secondary').text('重置中...');
		 $('#uploadImg').show();
		$.post(url, function(feedback){
			alert(feedback.message);
			if(feedback.successful){
				location.reload();
			}
		})
	});
})

function checkFile($this, name) {
	if (/^.+\.(xls)$/i.test(name.toLowerCase())
			|| /^.+\.(xlsx)$/i.test(name.toLowerCase())) {
		$('#inputExcel').attr("disabled",false);
		return;
	} else {
		$('#top-message').text('你导入的文件格式不正确,请上传Excel文件!').fadeIn('slow').delay(5000).fadeOut('slow');
		$($this).val('');
	}
}

function uploadFileFn() {
	$.ajaxFileUpload({
		url : window.ctx + '/select/getDPInfosByImportExecl.do', 
		type : 'post',
		dataType : 'text',
		secureuri : false,
		fileElementId : 'fileApp', // 文件选择框的id属性
		success : function(data, status, e) {
			$('#fileApp').val('');
			$('#inputExcel').removeClass('bt-secondary').addClass('bt-primary').text("上传完成");
			$('#uploadImg').fadeOut(3000);
			alert("上传完成");
			location.reload();
		},
		error : function(data, status, e) {
			var info = $.parseJSON(data);
			alert(info.msg);
			$('#inputExcel').prop('disabled', false).addClass('bt-primary').text("导入");
			$('#uploadImg').hide();
		}
	});
}


function dProgramDetail($this, id){
	openShowModal(window.ctx+'/select/toDProgramDetail.do?id='+id, 1120, 700);
}

function updateDPStatus($this, id, status){
	var url = '';
	 if(status == '2'){
		url = window.ctx+ '/select/dProgramReady.do?id='+id;
		if(!window.confirm("您确定要准备上线此资产！")){
			return false;
		}
	}else{
		return;
	}
	$.post(url, function(feedback){
		alert(feedback.message);
		if(feedback.successful){
			location.reload();
		}
	})
}