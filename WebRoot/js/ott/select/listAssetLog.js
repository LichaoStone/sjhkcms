$(function(){
	$('#queryProgram').click(function(e) {
		initPage();
		$('#queryForm').submit();
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
		var url =window.ctx+'/log/delAssetLog.do?ids='+ptId;
		$.post(url, function(feedback){
			alert(feedback.message);
			if(feedback.successful){
				location.reload();
			}
		})
	})
	
})

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
