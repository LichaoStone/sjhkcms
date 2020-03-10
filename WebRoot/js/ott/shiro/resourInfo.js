
$(function() {
//	$('#systemid').change(function(e) {
//		var systemId = $(this).val();
//		$.get(window.ctx+"/resour/getResources.do?systemId=" + systemId, "", function(
//						res) {
//					$('#parentId option').remove();
//					$('#parentId')
//							.append("<option value='0'>=\u8bf7\u9009\u62e9=</option>");
//					$.each(res, function(index, c) {
//								$('#parentId').append("<option value='" + c.id
//										+ "'>" + c.name + "</option>");
//							})
//				});
//	})
	
			var validator = $("#fom").validate({
						onsubmit : false,
						rules : {
							'name' : {
								required : true,
								remote : {
									url : window.ctx+'/resour/checkName.do',
									data : {
										'oldName' : function() {
											return $("#oldName").val();
										}
									}
								}
							},
							'enname' : {
								required : true,
								remote : {
									url : window.ctx+'/resour/checkEnName.do',
									data : {
										'oldEnName' : function() {
											return $("#oldEnName").val();
										}
									}
								}
							},
							'link' : {
								required : true
							},
							'systemid' : {
								required : true
							}
						},
						messages : { // 出错提示信息
							'name' : {
								required : "资源名称不能为空!",
								remote : '此资源名称已被使用，请重新输入'
							},
							'enname' : {
								required : "资源编码不能为空!",
								remote : '此资源编码已被使用，请重新输入'
							},
							'link' : "资源连接不能为空!",
							'systemid' : "请选择所属系统!"
						},
						errorPlacement : $.handleError,
						success : $.handleSuccess,
						invalidHandler : $.invalidAlertHandler
					});
					
			var isSaving = false;
			$('#saveResour').click(function(e) {
				if (isSaving) {
					return false;
				}
				if (validator.form()) {
					var url = window.ctx+'/resour/saveResour.do';
					var data = $('#fom').serializeArray();
					isSaving = true;
					$.post(url, data, function(feedback) {
					    alert(feedback.message);
						if (feedback.successful) {
						    location.href=window.ctx+'/resour/findResourList.do';
						}else{
							isSaving = false;
						}
					});
				}
			});
		});
		
		

function changeSystem(obj, dom){
	if (obj.value == '') {
		jQuery('#' + dom + ' option').remove();
		jQuery('#' + dom)
				.append("<option value=''>=\u8bf7\u9009\u62e9=</option>");
		return;
	}
	var param={
		'systemId': obj.value
	}
	$.get(window.ctx + "/resour/getResources.do" , param,
			function(res) {
				if (res == null) {
					return;
				}
				jQuery('#' + dom + ' option').remove();
				jQuery('#' + dom)
						.append("<option value=''>=\u8bf7\u9009\u62e9=</option>");
				for (var c in res) {
					var value="";
					if(res[c].parentId=="0"){
						value="-"+res[c].name
					}else{
						value="---"+res[c].name
					}
					jQuery("#" + dom).append("<option data='"+res[c].name+"' value='" + res[c].id
							+ "'>" + value + "</option>");
				}
			});
	return false;
}
