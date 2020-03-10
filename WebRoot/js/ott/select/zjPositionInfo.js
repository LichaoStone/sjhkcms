
$(function(){
	
	var validator = $("#form1").validate({
		onsubmit : false, 
		rules: {
			'sort': {
				required: true,
				number: true
			}
		},
		messages: { // 出错提示信息
			'sort': {
				required: "请填写排序值",
				number: "排序只能是数字"
			}
		},
		errorPlacement: $.handleError,
		success: $.handleSuccess,
		invalidHandler: $.invalidAlertHandler
	});
	
	var isSaving = false;
	// 单击保存按钮提交表单事件
	$('#saveSource').click(function (e) { 
		if (isSaving) {
			return false;
		}
		if (validator.form()) {
			var url = '';
			if($('#id').val() == ''){
				 url= window.ctx+ '/recommend/addRecommendPst.do';
			}else{
				 url= window.ctx+ '/recommend/updateRecommendPst.do';
			}
			var $this = $(this);
			$this.removeClass('bt-primary').addClass('bt-secondary').text('保存中...').prop('disabled', true);
			
			var data = $('#form1').serializeArray();
			$.post(url, data, function(feedback) {
				alert(feedback.message);
				if(feedback.successful) {
					location.href=window.ctx+'/recommend/findRecommendPst.do?prType='+$('#prType').val()
				}else{
					$this.removeClass('bt-secondary').addClass('bt-primary').text('保存').prop('disabled', false);
					isSaving = false;
				}
			});
		}
	});
})

$('#addPosition').click(function(e) {
		$('#positionTestList').show();
	})
var test = 0;
$('#checkOk').click(function(e){
		var $partner = $("input[name=checkMac]:checked");
		if($partner.val() == null){
			alert("请选择一个资产");
			return false;
		}else{
			var pd = true;
			var list = $('#form1').serializeArray()
			if(typeof($('#sort').val()) != "undefined"){
				for(var i in list){
					for(var j in list[i]){
						if($partner.attr('macs')==list[i][j]){
							alert("资产选择重复");
							pd = false;
						}
					}
					
				}
			}
			var test1;
			var url = window.ctx+ '/recommend/getMaxSort.do?prType='+$('#prType').val();
			if(pd){
				$.get(url, function(result){
					test1 = parseInt(result)+parseInt(test);
					var macList=$partner.attr('macs');
					var ids=$partner.attr('ids');
					$.each(macList.split(','), function(index, name) {
						var $trs = $('#macTb').find('tr');
						var html = '<tr><td width="10%" class="pn-flabel pn-flabel-h">资产名称：'
								+ '</td>'
								+ '<td class="pn-fcontent" colspan="1"><input type="text" name="name" id="name11"  class="mac_class" value="'
								+ name
								+ '"/>'+'<td width="10%" class="pn-flabel pn-flabel-h">排序值：'+'<input type="hidden" id="dramaId" name="dramaId"  value="'
								+ ids
								+'" />'
								+ '</td>'+'<td  class="pn-fcontent" colspan="1" ><input  type="text" class="mac_class" name="sort" id="sort"  value="'
								+test1
								+ '"/>'
								+ '<a href="javascript:void(0);" onclick="removeThisMac(this)" class="operation-icon reset" title="清除"></a></td></tr>';
						$('#macTb').append(html);
						test = parseInt(test) +1;
					})
				  });
				$('#positionTestList').hide();
			}
		}
	});
function removeThisMac($this){
	$($this).parent().parent().remove();
}
