$(function(){
	$('#queryProgram').click(function(e) {
		initPage();
		$('#queryForm').submit();
	});
})

function updateTypeStatus($this, id, status){
	var url = '';
	if(status =='0'){
		url = window.ctx+ '/position/dramaPositionReverse.do?id='+id;
		if(!window.confirm("您确定要启用此推荐位！")){
			return false;
		}
	}else if(status == '1'){
		url = window.ctx+ '/position/dramaPositionClose.do?id='+id;
		if(!window.confirm("您确定要停用此推荐位！")){
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
