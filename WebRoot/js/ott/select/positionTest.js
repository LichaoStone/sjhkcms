$(function() {
	var param = {
		'on':true,
		'page':1,
		'name':$('#name').val(),
		'prType':$('#prType').val(),
		'parentId':$('select#parentId option:selected').val(),
		'ttype':$('select#ttype option:selected').val(),
		'ptype':$('select#ptype option:selected').val()
	};
	ajaxRequest(param);
	
    //查询提交
	$('#querySoft').click(function(e){
		var nodes=checkNodeFn()
		param = {
			'on':true,
			'page':1,
			'name':$('#name').val(),
			'prType':$('#prType').val(),
			'selectId':nodes.join(','),
			'parentId':$('select#parentId option:selected').val(),
			'ttype':$('select#ttype option:selected').val(),
			'ptype':$('select#ptype option:selected').val()
		}
		ajaxRequest(param);
	});
	
	
});

function checkNodeFn() {
	var $nodes = $('input:hidden[name="dramaId"]');
	var array = [];
	if ($nodes && $nodes.length > 0) {
		for (var i = 0; i < $nodes.length; i++) {
			array.push($($nodes[i]).val());
		}
	}
	return array;
}

function _closeSoftOption(){
	$('#positionTestList').hide();
}

function ajaxRequest(param){
		$("#pagination").myPagination({
		      currPage: 1,
		      ajax: {
		              on: true,
		              callback: 'ajaxCallBack',
		              url: window.ctx+"/recommend/findPositionTest.do",
		              dataType: 'json', 
		              param: param	//{on:true,page:1,pageSize:10,arg1:'1',arg2:'2'}参数列表，其中  on 必须开启，page 参数必须存在，其他的都是自定义参数，如果是多条件查询，可以序列化表单，然后增加 page 参数
		            }
		 });
	}

function ajaxCallBack(data) {
         var html = "";	//视图数据
         if(data.result.length==0){
         	html +='<tr align="center" bgcolor="#FFFFFF" height="26"><td colspan="8"><h1>暂无相关数据内容</h1></td></tr>';
         	  $('#soft_data').html(html);
         	  $("#pagination").html('');
         	  return;
         }
         $.each(data.result, function(index,soft) {
				 html +='<tr align="left" bgcolor="#FFFFFF" height="26">';
				 html +='<td ><input type="radio" name="checkMac" class="np" macs="'+soft.name+'" ids="'+soft.id+'" /></td>';
			     html +='<td >'+soft.name+'</td>';
			     html +='<td >'+soft.typeName+'</td>';
			     html +='<td >'+soft.providerName+'</td>';
			     html +='<td >'+soft.ptypeName+'</td>';
			     html +='</tr>';
         });
         $('#soft_data').html(html);
         return false;
}