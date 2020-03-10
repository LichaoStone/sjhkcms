$(function(){
	$('#queryProgram').click(function(e) {
		initPage();
		$('#queryForm').submit();
	});
})


function updateSPStatus($this, id, status){
	var url = '';
	if(status =='0'){
		url = window.ctx+ '/select/sProgramClose.do?id='+id;
		if(!window.confirm("您确定要启用此节目！")){
			return false;
		}
	}else if(status == '1'){
		url = window.ctx+ '/select/sProgramReverse.do?id='+id;
		if(!window.confirm("您确定要停用此节目！")){
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