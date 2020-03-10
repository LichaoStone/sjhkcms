$(function() {
//	$(document).bind("contextmenu",function(e){   
//          return false;   
//    });
	var param = {
		'on':true,
		'page':1
	};
	ajaxRequest(param);
	
    //查询提交
	$('#query_partner').click(function(e){
		param = {
			'on':true,
			'page':1,
			'partnerName':$('#partnerName').val(),
			'partnerPhone':$('#partnerPhone').val(),
			'partnerType':$('#partnerType').val()
		}
		ajaxRequest(param);
	});
	
});

function ajaxRequest(param){
		$("#pagination").myPagination({
		      currPage: 1,
		      ajax: {
		              on: true,
		              callback: 'ajaxCallBack',
		              url: window.ctx+"/common/findPartner.do",
		              dataType: 'json', 
		              param: param	//{on:true,page:1,pageSize:10,arg1:'1',arg2:'2'}参数列表，其中  on 必须开启，page 参数必须存在，其他的都是自定义参数，如果是多条件查询，可以序列化表单，然后增加 page 参数
		            }
		 });
	}

function ajaxCallBack(data) {
         var html = "";	//视图数据
         if(data.result.length==0){
         	html +='<tr align="center" bgcolor="#FFFFFF" height="26"><td colspan="7"><h1>暂无相关数据内容</h1></td></tr>';
         	  $('#partner_data').html(html);
         	  return;
         }
         $.each(data.result, function(index,partner) {
				 html +='<tr align="left" bgcolor="#FFFFFF" height="26">';
				 html +='<td ><input type="radio" name="checkPartner" class="np" pid="'+partner.id+'" pname="'+partner.partnerName+'" /></td>';
			     html +='<td >'+partner.partnerName+'</td>';
			     html +='<td >'+partner.partnerTypeName+'</td>';
			     html +='<td >'+partner.linkman+'</td>';
			     html +='<td >'+partner.partnerPhone+'</td>';
			     html +='<td >'+partner.statusName+'</td>';
			     html +='<td >'+partner.createTime+'</td></tr>';
         });
         $('#partner_data').html(html);
         return false;
}