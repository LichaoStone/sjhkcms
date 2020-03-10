$(function(){
	
	$('#delPartner').click(function(e){
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
		var url = window.ctx+ '/partner/delPartner.do?id='+ptId;
		$.post(url, function(feedback){
			alert(feedback.message);
			if(feedback.successful){
				location.reload();
			}
		})
	})
	
	$('#queryPartner').click(function(e){
		initPage();
		$('#queryForm').submit();
	});
})

function toUpdatePartner($this, id){
	location.href = window.ctx+ '/partner/toPartner.do?id='+id;
}

function toPartnerInfo($this, id){
	openShowModal(window.ctx+'/partner/partnerInfo.do?id='+id, 1120, 700);
}

function toProduct($this, partId){
	openShowModal(window.ctx+'/common/partnerProduct.do?partId='+partId, 1120, 700);
}