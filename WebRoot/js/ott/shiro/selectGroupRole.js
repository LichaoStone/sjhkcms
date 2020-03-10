            //移动  
         function moveOption(obj1, obj2){  
            for(var i = obj1.options.length - 1 ; i >= 0 ; i--){  
               if(obj1.options[i].selected){  
                   var opt = new Option(obj1.options[i].text,obj1.options[i].value);  
                   opt.selected = true;  
                   obj2.options.add(opt);  
                   //  obj2.options.selected = true; 
                   obj1.remove(i);  
               }  
           }  
         }  
		/**
		 * 保存
		 * @param {} obj
		 */
         var isSaving = false;
	  function saveAuthority(obj){
		  	if (isSaving) {
				return false;
			}
		    var roleIds = [];
		    var $selected = $('select#right option');
  			$.each($selected, function(i, $right){
  				roleIds.push($($right).val());
  			})
		    var selVal = roleIds.join(',');
  			isSaving=true;
			var url = '../group/addRoleToGroup.do?roleIds=' + selVal;
			var data = $('#myform').serializeArray();
			$.post(url, data, function() {
				alert("组权限操作成功！");
				location.href=window.ctx+'/group/findGroupList.do';
			});
	  }
	  
	  
