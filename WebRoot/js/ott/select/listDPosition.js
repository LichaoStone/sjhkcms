$(function() {
			$('#delRecommend').click(function(e) {
				var $ptId = $('input:checked[name="arcID"]');
				if ($ptId.length <= 0) {
					alert('请选择要删除的选项');
					return;
				}
				if (!window.confirm("您确定要删除您选择的选项！")) {
					return false;
				}
				var ptIds = [];
				for (i = 0; i < $ptId.length; i++) {
					ptIds.push($($ptId[i]).val());
				}
				var ptId = ptIds.join(",");
				var url = window.ctx + '/recommend/delRecommend.do?id='
						+ ptId;
				$.post(url, function(feedback) {
							alert(feedback.message);
							if (feedback.successful) {
								location.reload();
							}
						})
			});

			$('#queryProductRec').click(function(e) {
				initPage();
				$('#queryForm').submit();
			});
		})



function toAddRecommend($th, prType) {
	
	location.href=window.ctx+ '/recommend/toDRecommend.do?prType=' + prType + '&dramaName=' + $('#dramaName').val()+'&dramaId='+$('#dramaId').val();

}