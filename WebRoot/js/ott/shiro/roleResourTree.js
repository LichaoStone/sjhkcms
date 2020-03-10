$(function() {
	
    //	左边树型内容显示
	$('#resource_tree').tinytree({
		url : window.ctx+'/role/buildResourceTree.do?roleId='+$('#roleId').val(),
		autoload : true,
		checkboxSupport : true,
		onError : function(xhr,status,e) {alert(e.message);},
		showChildren : true,
		openAllNodes : true,
		clickNodeToCheckRadioOrCheckbox : true,
		alsoCheckParent : true,
		alsoCheckChildren : true //是否同时勾选子节点
	});

	$('#roleResource_save').click(function(e){
		var $nodes = $('#resource_tree')[0].tinytree.getSelecetedNodes();
		var roleId = $('#roleId').val();
		var resIds = new Array();
		$.each($nodes, function(i, node) {
			resIds.push(node.nodeId);
		});
		var url = window.ctx + '/role/addRoleResource.do?roleId='+roleId+'&resIds='+resIds.join(",");
		$.post(url, function(feedback) {
			alert(feedback.message);
			if (feedback.successful) {
				location.href = window.ctx+'/role/findRoleList.do';
			} 
		});
	});
	
});