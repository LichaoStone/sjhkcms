$(function() {
	$('#queryGroup').click(function(e){
		initPage();
		$('#queryForm').submit();
	});
})

/* 删除 */
function toDel(obj) {
	var form = document.queryForm;
	var state = confirm('确定要删除该信息吗？');
	if (state) {
		$.ajax({
             type: "POST",
             url: window.ctx+"/role/delRole.do?oid="+obj,
             success: function(feedback){
                 alert(feedback.message);
				if(feedback.successful){
					location.href=window.ctx+'/role/findRoleList.do';
				}
             }
         });
	}
}

