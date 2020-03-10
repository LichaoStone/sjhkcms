$(document).ready(function(){
	$("#toBatchcfile,#toBatchdfile").click(function(){
		var $assetIds = $('input:checked[name="arcID"]');
		if($assetIds.length <= 0){
			alert("请选择要操作的资产选项");
			return;
		}
		var sign = null;
		var warnMsg = null;
		var errorMsg = null;
		if($(this).attr("id") == "toBatchcfile"){
			sign = 0;
			warnMsg = "您确定要批量生成您选择的选项?";
			errorMsg = "请选择生成失败的资产";
		}else{
			sign = 1;
			warnMsg = "您确定要批量处理您选择的选项?";
			errorMsg = "请选择处理失败的资产";
		}
		if(!window.confirm(warnMsg))
			return false;
		var assetlist = [];
		var content = null;
		for(var i = 0; i < $assetIds.length; i++){
			if($($assetIds[i]).siblings("input[type='hidden']").eq(sign).val() != '1'){
				alert(errorMsg);
				return;
			}
			content = $($assetIds[i]).siblings("input[type='hidden']").eq(2).val() + "|" + $($assetIds[i]).val();
			assetlist.push(content);
		}
		if(assetlist.length > 0)
			assetlist = assetlist.join(";");
		var url = null;
		if(sign == 0)
			url = window.ctx + "/oper/toCreateFileForBatch.action?assetlist=" + assetlist;
		else
			url = window.ctx + "/oper/toDealFileForBatch.action?assetlist=" + assetlist;
		$.post(url, function(feedback){
			alert(feedback.message);
			if(feedback.successful){
				location.reload();
			}
		})
	})
})
function toCreateOrDealFile(obj, sign){
	var warnMsg = null;
	var errorMsg = null;
	var url = null;
	if(sign == 0){
		warnMsg = "您确定要生成该文件吗";
		errorMsg = "请选择生成失败的资产";
		url = ctx + "/oper/toCreateFileForBatch.action?assetlist=";
	}else{
		warnMsg = "您确定要处理该文件吗";
		errorMsg = "请选择处理失败的资产";
		url = ctx + "/oper/toDealFileForBatch.action?assetlist=";
	}
	if(!confirm(warnMsg))
		return;
	var $obj = $(obj);
	if($obj.parent("td").siblings("td").eq(0).find("input[type='hidden']").eq(0).val() != '1'){
		alert(errorMsg);
		return;
	}
	var assetlist = $obj.parent("td").siblings("td").eq(0).find("input[type='hidden']").eq(2).val() + "|" + $obj.parent("td").siblings("td").eq(0).find("input[type='checkbox']").eq(sign).val();
	url = url + assetlist;
	$.post(url, function(feedback){
		alert(feedback.message);
		if(feedback.successful){
			location.reload();
		}
	})
}

