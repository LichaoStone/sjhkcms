$(function() {
			var validator = $("#fom").validate({
						onsubmit : false,
						rules : {
							'roleName' : {
								required : true,
								remote : {
									url : window.ctx+'/role/checkRname.do',
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
									url : window.ctx+'/role/checkEname.do',
									data : {
										'oldEnName' : function() {
											return $("#oldEnName").val();
										}
									}
								}
							}
						},
						messages : { // 出错提示信息
							'roleName' : {
								required : "角色名称不能为空!",
								remote : '此角色名称已被使用，请重新输入'
							},
							'enname' : {
								required : "角色编码不能为空!",
								remote : '此角色编码已被使用，请重新输入'
							}
						},
						errorPlacement : $.handleError,
						success : $.handleSuccess,
						invalidHandler : $.invalidAlertHandler
					});

			var isSaving = false;
			$('#saveRole').click(function(e) {
				if (isSaving) {
					return false;
				}
				if (validator.form()) {
					var url = window.ctx+'/role/saveRole.do';
					var data = $('#fom').serializeArray();
					isSaving = true;
					$.post(url, data, function(feedback) {
						alert(feedback.message);
						if (feedback.successful) {
							location.href = window.ctx+'/role/findRoleList.do';
						}else{
							isSaving = false;
						}
					});
				}
			});

		});
