$(function(){
	$('#queryTariff').click(function(e) {
		initPage();
		$('#queryForm').submit();
	});
	
})


function updateTariffStatus($this, id, status){
	var url = '';
	if(status =='0'){
		url = window.ctx+ '/select/tariffReverse.do?id='+id;
		if(!window.confirm("您确定要启用此资费！")){
			return false;
		}
	}else if(status == '1'){
		url = window.ctx+ '/select/tariffClose.do?id='+id;
		if(!window.confirm("您确定要停用此资费！")){
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

