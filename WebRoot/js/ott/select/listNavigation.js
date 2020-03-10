$(function(){
	$('#toAddChannelType').click(function(e){
		var retVal = openShowModal(window.ctx+ '/type/toNavigation.do', 500, 300);
		retVal && location.reload();
	});
	
	$('#queryProgram').click(function(e) {
		initPage();
		$('#queryForm').submit();
	});
	
	$('#list-table td input[name="queue"]').focus(function(e){
		$('#list-table td').find("img").hide();
		$(this).parent("td").find("img").show();
	})
	
	$('#list-table td input[name="queue"]').change(function(e){
		  var $this=$(this);
		  var reCat = new RegExp("^[0-9]*$"); 
		  var queue=$this.val();
		  var oldQueue=$this.attr('data-queue');
		  if(reCat.test(queue) && queue.length < 5 && parseInt(queue) > 0){
			  var url =  window.ctx+ '/type/changeDNSequence.do';
			  var id=$this.attr('data-id');
			  var prama={
					  'id':id,
					  'queue':queue,
					  'oldQueue':oldQueue
			  }
			  $.post(url, prama,function(feedback){
				  if (feedback.successful) {
						location.reload();
				   }else{
					   alert(feedback.message);
				   }
			  })
		  }else{
			  alert("请输入大于0小于10000的正整数！");
			  $this.val(oldQueue);
		  }
	});
	
})

function updateTypeStatus($this, id, status){
	var url = '';
	if(status =='0'){
		url = window.ctx+ '/type/dramaNavigationReverse.do?id='+id;
		if(!window.confirm("您确定要启用此频道类型！")){
			return false;
		}
	}else if(status == '1'){
		url = window.ctx+ '/type/dramaNavigationClose.do?id='+id;
		if(!window.confirm("您确定要停用此频道类型！")){
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

function delCType($this, id){
	 var url = window.ctx+ '/type/delDramaNavigation.do?id='+id;
	if(!window.confirm("您确定要删除此频道类型下的频道！")){
		return false;
	}
	$.post(url, function(feedback){
		alert(feedback.message);
		if(feedback.successful){
			location.reload();
		}
	})
}

function toUpdateType($this, id){
	location.href=window.ctx+ '/type/toNavigation.do?id='+id;
}

function changeUpSequence($this,sequence,parentId,id){
	var url = '';
	if(!window.confirm("您确定要上移该排序？")){
		return false;
	}
	url = window.ctx+ '/type/changeUpSequence.do?id='+id + "&sequence="+sequence + "&parentId="+parentId;
	$.post(url, function(feedback){
		if(feedback.successful){
			location.reload();
		}
	})
}

function changeDownSequence($this,sequence,parentId,id){
	var url = '';
	if(!window.confirm("您确定要下移该排序？")){
		return false;
	}
	url = window.ctx+ '/type/changeDownSequence.do?id='+id + "&sequence="+sequence + "&parentId="+parentId;
	$.post(url, function(feedback){
		if(feedback.successful){
			location.reload();
		}
	})
}

