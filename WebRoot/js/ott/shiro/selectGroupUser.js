      
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
      var isSaving = false;
	  function saveAuthority(obj){
		    var userIds = [];
		    var $selected = $('select#right option');
  			$.each($selected, function(i, $right){
  				userIds.push($($right).val());
  			})
  			if (isSaving) {
				return false;
			}
			isSaving = true;
		    var selVal = userIds.join(',');
			var url = window.ctx+'/group/addUserToGroup.do?userIds=' + selVal;
			var data = $('#myform').serializeArray();
			$.post(url, data, function(feedback) {
				alert(feedback.message);
				if (feedback.successful) {
					location.href=window.ctx+'/group/findGroupList.do';
				}else{
					isSaving = false;
				}
			});
	  }
