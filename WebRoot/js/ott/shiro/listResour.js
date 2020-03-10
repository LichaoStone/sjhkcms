
/* 删除 */
function toDel(obj) {
	var form = document.queryForm;
	var state = confirm('确定要删除该信息吗？');
	if (state) {
		$.ajax({
             type: "POST",
             url: "../resour/delResour.do?oid="+obj,
             success: function(feedback){
                 alert(feedback.message);
				if(feedback.successful){
					location.reload();
				}
             }
         });
	}
}

$(function() {
	$('#queryRole').click(function(e){
		initPage();
		$('#form2').submit();
	});
})