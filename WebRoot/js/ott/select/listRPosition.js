$(function(){
	$('#queryProgram').click(function(e) {
		initPage();
		$('#queryForm').submit();
	});
	
})

function changeUpSequence($this,sort,prType,id){
	var url = '';
	if(!window.confirm("您确定要上移该排序？")){
		return false;
	}
	url = window.ctx+ '/recommend/changeUpSequence.do?id='+id + "&sort="+sort + "&prType="+prType;
	$.post(url, function(feedback){
		if(feedback.successful){
			location.reload();
		}
	})
}

function changeDownSequence($this,sort,prType,id){
	var url = '';
	if(!window.confirm("您确定要下移该排序？")){
		return false;
	}
	url = window.ctx+ '/recommend/changeDownSequence.do?id='+id + "&sort="+sort + "&prType="+prType;
	$.post(url, function(feedback){
		if(feedback.successful){
			location.reload();
		}
	})
}

function del($this, id) {
	if (window.confirm("您确定要删除此推荐资产信息！")) {
		var url = window.ctx + '/recommend/del.do?id=' + id;
		$.post(url, function(feedback) {
			alert(feedback.message);
			if (feedback.successful) {
				location.reload();
			}
		})
	}
}
