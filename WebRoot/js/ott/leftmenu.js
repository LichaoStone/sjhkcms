
$(function(e){
//	initSelect();
});
//初始选择第一个竖菜单
function initSelect(){
	$('#childMenu >div').children().hide();
	var $first = $('#topMenu >a').first();
	if($first.attr('menuStyle') == 'mm2'){
		$first.removeClass().addClass('mmac2');
	}else if($first.attr('menuStyle') == 'mm3'){
		$first.removeClass().addClass('mmac3');
	}else{
		$first.removeClass().addClass('mmac');
	}
	var $second =$('#mainct >div').first();
	$second.show();
}
//横菜单显示/遮盖
function isShowMenu(menuId){
	var $this = $('#'+menuId);
	var $parent = $('#sun'+menuId);
//	$('.sitem').css("display", "none");
//	$parent.siblings().addClass('bitem2');
	
	if($this.is(':hidden')){
		$parent.removeClass().addClass('bitem');
	}else{
		$parent.removeClass().addClass('bitem2');
	}
	$this.slideToggle(200);
}

function getFirstA(){
	var $first = $('#topMenu >a').first();
	return $first.attr('menuId');
}
//var curitem = getFirstA();
//竖菜单显示/遮盖
function ShowMainMenu(n){
//	if(curitem ==n ) return false;
	var $thisDiv = $('#'+n);
//	var $oldDiv = $('#'+curitem);
	
//	var $thisA = $('#link_'+n);
//	var $oldA = $('#link_'+curitem);
	if($thisDiv){
//		$oldDiv.hide();
		$('div[id^="ct_"]').hide();
		$thisDiv.show();
//		if($oldA.hasClass('mmac2')){
//			$oldA.removeClass().addClass('mm2');
//		}else if($oldA.hasClass('mmac3')){
//			$oldA.removeClass().addClass('mm3');
//		}else{
//			$oldA.removeClass().addClass('mm');
//		}
//		if($thisA.hasClass('mm2')){
//			$thisA.removeClass().addClass('mmac2');
//		}else if($thisA.hasClass('mm3')){
//			$thisA.removeClass().addClass('mmac3');
//		}else{
//			$thisA.removeClass().addClass('mmac');
//		}
//		curitem = n;
	}
}
//二级导航菜单点击后突出显示(字体加粗并变为蓝色) <add by yangdd>
function changeCurrent(link, menuName) {
//	top.showThisMsg(menuName);
	var $current = $(link);
	var $allLink = $("#mainct a[href][class!='url-disabled']");
	$allLink.hover(function() {
		$(this).css('color','#ff0000');
	}, function() {
		if(this != link) {
			$(this).css('color','#000000');
		} else {
			$(this).css('color', '#138cc5');
		}
	});
	$allLink.css('color','#000000')
			.css('font-weight', 'normal');
	$current.css('color', '#138cc5')
			.css('font-weight', 'bold');
}
