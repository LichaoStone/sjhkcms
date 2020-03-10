      
            //移动  
         function moveOption(obj1, obj2){  
            for(var i = obj1.options.length - 1 ; i >= 0 ; i--){  
               if(obj1.options[i].selected){  
                   var opt = new Option(obj1.options[i].text,obj1.options[i].value);  
                   opt.selected = true;  
                   obj2.options.add(opt);  
                   obj1.remove(i);  
               }  
           }  
         }  
		/**
		 * 保存
		 * @param {} obj
		 */
	  function saveAuthority(obj){
			var groupArr = [];
		    var $selected = $('select#right option');
  			$.each($selected, function(i, $right){   
  				groupArr.push($($right).val());
  			})
		    var groupId = groupArr.join(',');
			var url = window.ctx+'/user/addUserToGroup.do?groupId=' + groupId;
			var data = $('#myform').serializeArray();
			$.post(url, data, function() {
				alert("用户分组操作成功！");
				location.href=window.ctx+'/user/findUsersList.do'
			});
	  }
