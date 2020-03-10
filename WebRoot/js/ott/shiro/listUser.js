
$(function() {
	//条件查询
	$('#queryUser').click(function(e) {
				initPage();
				$('#queryForm').submit();
			});
})

function del(obj) {
	var form = document.queryForm;
	var state = confirm('确定要禁用此账号!');
	if (state) {
		$.ajax({
             type: "POST",
             url: "../user/delUser.do?uid="+obj,
             success: function(feedback){
                 alert(feedback.message);
				 if(feedback.successful){
					 location.reload();
				 }
             }
         });
	}
}

