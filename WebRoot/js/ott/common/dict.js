$(function() {
	
    //	左边树型内容显示
	$('#dict_tree_left').tinytree({
		url : window.ctx+'/common/buildTreeDict.do',
		autoload : true,
		onError : function(xhr,status,e) {alert(e.message);},
		showChildren : true,
		openAllNodes : true,
		root : -1,
		radioSupport : true,
		clickNodeToCheckRadioOrCheckbox : true,
		onNodeClick : returnSelectedDict,
		onRadionClick : function(radio, nodeId, nodeName) {
			return returnSelectedDict(nodeId, nodeName);
		}
	});

});

function returnSelectedDict(nodeId, nodeName) {
	$('#dicType').val(nodeId);
	$('#typeName').val(nodeName);
	$('#dictList').hide();
}

function _closeDictOption(){
	$('#dictList').hide();
}