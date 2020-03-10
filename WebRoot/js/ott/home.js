$(function(){
		
	//左侧菜单开关
	$("#togglemenu").click(function(){
		if($("body").attr("class")=="showmenu"){
			$("body").attr("class","hidemenu");
			$(this).html("显示菜单");
		}else{
			$("body").attr("class","showmenu");
			$(this).html("隐藏菜单");
		}
	});
});


function showThisMsg(menuName){
	$('#nav').empty().addClass('menu-tip').append(menuName);
}



