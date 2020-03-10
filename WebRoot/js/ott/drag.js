jQuery(function(){
	$('#drag').attr('class', 'drag-close');
	$('#drag').click(function(){
		toggleDrag();
		return false;					  
	});
});
function toggleDrag(){
	var obj = $('#bodyFrame', parent.document);
	var drag = $('#drag');
	if(obj.attr('cols') == "165,6,*"){
		obj.attr('cols','0,6,*');
		drag.attr('class', 'drag-open');
	}else{
		obj.attr('cols','165,6,*');
		drag.attr('class', 'drag-close');
	}
}
document.oncontextmenu=new Function("event.returnValue=false;"); 
document.onselectstart=new Function("event.returnValue=false;");