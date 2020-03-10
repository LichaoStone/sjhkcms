$(function(){
	$('#inputExcel').click(function(e) {
		var f = $('#fileApp').val();
		if(f==''){
			$('#top-message').text('请选择文件!').fadeIn('slow').delay(5000).fadeOut('slow');
			return;
		}
		$('#uploadImg').show();
        $(this).prop('disabled', true).removeClass('bt-primary')
		.addClass('bt-secondary').text('上传中...');
        uploadFileFn();
	});
	
	$('#toAddDict').click(function(e){	
	  var retVal =openShowModal(window.ctx+ '/dict/toDict.do', 980, 600);
		if (retVal) {
			location.reload();
		}
	});
	
	                   
	$('#toDelDict').click(function(e){
		var $ptId = $('input:checked[name="arcID"]');
		if($ptId.length <=0){
			alert('请选择要删除的选项');
			return;
		}
		if(!window.confirm("您确定要删除您选择的选项！")){
			return false;
		}
		var ptIds = [];
		for (i = 0; i < $ptId.length; i++) {
				ptIds.push($($ptId[i]).val());
		}
		var ptId = ptIds.join(",");
		var url =window.ctx+'/dict/delDict.do?ids='+ptId;
		$.post(url, function(feedback){
			alert(feedback.message);
			if(feedback.successful){
				location.reload();
			}
		})
	})
	
	$('#queryDict').click(function(e){
		initPage();
		$('#queryForm').submit();
	});	
})

function uploadFileFn() {
	$.ajaxFileUpload({
				url : window.ctx + '/dict/getReadReport.do', 
				dataType : 'json',
				secureuri : false,
				fileElementId : 'fileApp', // 文件选择框的id属性
				success : function(data, status, e) {
					alert(data.msg);
					$('#fileApp').val('');
					$('#inputExcel').prop('disabled', false)
							.removeClass('bt-secondary').addClass('bt-primary')
							.text(data.msg)
					$('#uploadImg').hide();
					location.reload();
				},
				error : function(data, status, e) {
					alert("上传出错");
					$('#inputExcel').prop('disabled', false)
							.addClass('bt-primary')
					$('#uploadImg').hide();
				}
			});
}
	

function updateDictStatus($this, id, status){
	var url = '';
	if(status =='0'){
		url = window.ctx+ '/dict/dictReverse.do?id='+id;
		if(!window.confirm("您确定要启用此字典数据！")){
			return false;
		}
	}else if(status == '1'){
		url = window.ctx+ '/dict/dictClose.do?id='+id;
		if(!window.confirm("您确定要停用此字典数据！")){
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

/* 删除 */
function delDict($this, id){
	var state = confirm('确定要删除该信息吗？');
	if (state) {
		$.ajax({
             type: "POST",
             url: window.ctx+"/dict/delDict.do?ids="+id,
             success: function(feedback){
                 alert(feedback.message);
				if(feedback.successful){
					location.reload();
				}
             }
         });
	}
}

function checkFile($this, name) {
	if (/^.+\.(xls)$/i.test(name.toLowerCase())
			|| /^.+\.(xlsx)$/i.test(name.toLowerCase())) {
		return;
	} else {
		$('#top-message').text('你导入的文件格式不正确,请上传Excel文件!').fadeIn('slow').delay(5000).fadeOut('slow');
		$($this).val('');
	}
}

function toDictDetail(obj) {	
    openShowModal(window.ctx+'/dict/dictDetail.do?id='+obj, 900, 450);
}

function toUpdateDict($this, id){
	var retVal =openShowModal(window.ctx+ '/dict/toDict.do?id='+id, 980, 600);
	if (retVal) {
		location.reload();
	}
}
	