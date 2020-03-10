// 移动
function moveOption(obj1, obj2) {
	for (var i = obj1.options.length - 1; i >= 0; i--) {
		if (obj1.options[i].selected) {
			var opt = new Option(obj1.options[i].text, obj1.options[i].value);
			opt.selected = true;
			obj2.options.add(opt);
			obj1.remove(i);
		}
	}
}
/**
 * 保存
 * 
 * @param {}
 *            obj
 */
 var isSaving = false;
function saveAuthority(obj) {
	if (isSaving) {
		return false;
	}
	var groupIds = [];
    var $selected = $('select#right option');
	$.each($selected, function(i, $right){
		groupIds.push($($right).val());
	})
    var selVal = groupIds.join(',');
		
    isSaving = true;
	var url = window.ctx+'/role/addGroupToRole.do?groupIds=' + selVal;
	var data = $('#myform').serializeArray();
	$.post(url, data, function(feedback) {
		alert(feedback.message);
		if (feedback.successful) {
			location.href=window.ctx+'/role/findRoleList.do';
		}else{
			isSaving = false;
		}
	});
}
