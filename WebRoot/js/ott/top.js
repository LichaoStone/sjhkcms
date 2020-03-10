jQuery(function(){
	$('a').bind("focus", function(){$(this).blur()});
	$('#nav li a').click(function(){
		$('#nav .current').removeClass('current');
		$(this).addClass('current');
		var menuId= $(this).attr("menuId");
		window.parent.document.getElementById("menu").contentWindow
			.ShowMainMenu(menuId);
		return false;
	});			
});

function changePwd(){
	openShowModal(window.ctx+ '/user/toEditPwd.do', 600, 300);
}

function greetings(){
	var day = new Date;
	var hr = day.getHours();
	if(hr > -1 && hr < 6) return '夜深了';
	if(hr > 5 && hr < 11) return '早上好';
	if(hr > 10 && hr < 15) return '中午好';
	if(hr > 14 && hr < 19) return '下午好';
	if(hr > 18 && hr < 24) return '晚上好';
}

function showMenu(id){
	window.parent.document.getElementById("menu").contentWindow.ShowMainMenu("ct_"+id);
}