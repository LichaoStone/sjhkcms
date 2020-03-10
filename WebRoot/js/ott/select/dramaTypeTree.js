$(function() {
	
    //	左边树型内容显示
	$('#drama_tree').tinytree({
		url : window.ctx+'/select/buildDramaTypeTree.do?dramaId='+$('#dramaId').val(),
		autoload : true,
		checkboxSupport : true,
		onError : function(xhr,status,e) {alert(e.message);},
		showChildren : true,
		openAllNodes : true,
		clickNodeToCheckRadioOrCheckbox : true,
		alsoCheckParent : false,
		alsoCheckChildren : false //是否同时勾选子节点
	});

	$('#saveType').click(function(e){
		var $nodes = $('#drama_tree')[0].tinytree.getSelecetedNodes();
		var dramaId = $('#dramaId').val();
		var dType = $('#dType').val();
		var resIds = new Array();
		$.each($nodes, function(i, node) {
			resIds.push(node.nodeId);
		});
		var url = window.ctx + '/select/addDramaTypeList.do?dramaId='+dramaId+'&dType='+dType+'&dtList='+resIds.join(",");
		$.post(url, function(feedback) {
			alert(feedback.message);
			if (feedback.successful) {
				location.href = window.ctx+'/select/findDProgram.do';
			} 
		});
	});
	
});