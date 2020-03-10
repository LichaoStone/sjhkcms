$(function(){
	$('#queryProgram').click(function(e) {
		initPage();
		$('#queryForm').submit();
	});
	
	$('#childReverse').click(function(e){
		var $ptId = $('input:checked[name="arcID"]');
		if($ptId.length <=0){
			alert('请选择要启用的选项');
			return;
		}
		if(!window.confirm("您确定要启用您选择的选项！")){
			return false;
		}
		var ptIds = [];
		for (i = 0; i < $ptId.length; i++) {
				ptIds.push($($ptId[i]).val());
		}
		var ptId = ptIds.join(",");
		var url =window.ctx+'/select/childListReverse.do?ids='+ptId;
		$.post(url, function(feedback){
			alert(feedback.message);
			if(feedback.successful){
				location.reload();
			}
		})
	})
	
	$('#childClose').click(function(e){
		var $ptId = $('input:checked[name="arcID"]');
		if($ptId.length <=0){
			alert('请选择要停用的选项');
			return;
		}
		if(!window.confirm("您确定要停用您选择的选项！")){
			return false;
		}
		var ptIds = [];
		for (i = 0; i < $ptId.length; i++) {
				ptIds.push($($ptId[i]).val());
		}
		var ptId = ptIds.join(",");
		var url =window.ctx+'/select/childListClose.do?ids='+ptId;
		$.post(url, function(feedback){
			alert(feedback.message);
			if(feedback.successful){
				location.reload();
			}
		})
	})
	
})


function updateChildStatus($this, id, status){
	var url = '';
	if(status =='0'){
		url = window.ctx+ '/select/childReverse.do?id='+id;
		if(!window.confirm("您确定要启用此剧集！")){
			return false;
		}
	}else if(status == '1'){
		url = window.ctx+ '/select/childClose.do?id='+id;
		if(!window.confirm("您确定要停用此剧集！")){
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