$(function(){
	$('#queryProgram').click(function(e) {
		initPage();
		$('#queryForm').submit();
	});
	
	$('#toBatchOnline,#toBatchOffline').click(function(e){
		var msg = null;
		var sign = null;
		var theResponse = null;
		if($(this).attr("id") == "toBatchOnline"){
			msg = "您确定要批量上线您选择的选项?"
			sign = 1;
		}
		else{
			msg = "您确定要批量下线您选择的选项?";
			sign = 2;
			theResponse = window.prompt("请在此输入资产下线原因","");
			if(theResponse==""||theResponse.length==0){
				alert("没有输入原因");
				return;
			}
		}
		var $ptId = $('input:checked[name="arcID"]');
		if($ptId.length <=0){
			alert('请选择要操作的资产选项');
			return;
		}
		if(!window.confirm(msg)){
			return false;
		}
		var ptIds = [];
		for (i = 0; i < $ptId.length; i++) {
			if(sign == 1){
				if($($ptId[i]).siblings("input[type='hidden']").eq(0).val() == '1'){
					alert("所选批量上线资产中包含已经上线资产");
					return;
				}
			}
			if(sign == 2){
				if($($ptId[i]).siblings("input[type='hidden']").eq(0).val() != '1'){
					alert("所选批量下线资产中包含已经下线资产");
					return;
				}
			}
			ptIds.push($($ptId[i]).val());
		}
		var ptId = ptIds.join(",");
		var url = ctx + '/select/batchOnlineForDP.do?ids=' + ptId + "&sign=" + sign + "&theResponse=" + theResponse;
		$.post(url, function(feedback){
			alert(feedback.message);
			if(feedback.successful){
				location.reload();
			}
		})
	})
})
function dProgramDetail($this, id){
	openShowModal(window.ctx+'/select/toDProgramDetail.do?id='+id, 1120, 700);
}

function updateDPStatus($this, id, status){
	var url = '';
	if(status =='0'){
		url = window.ctx+ '/select/dProgramReverse.do?id='+id;
		if(!window.confirm("您确定要上线此资产！")){
			return false;
		}
	}else if(status == '2'){
		url = window.ctx+ '/select/dProgramReady.do?id='+id;
		if(!window.confirm("您确定要准备上线此资产！")){
			return false;
		}
	}
	else{
		return;
	}
	$.post(url, function(feedback){
		alert(feedback.message);
		if(feedback.successful){
			location.reload();
		}
	})
}