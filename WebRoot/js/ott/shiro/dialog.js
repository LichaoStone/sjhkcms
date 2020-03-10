;(function($, object){
	var _isIE6 = !('minWidth' in $('html')[0].style);
	var _isIE8 = !!window.ActiveXObject && document.documentMode == 8;
	var _callback = null;
	var _timeOut = null;
	var loadOne = true;
	
	var __Iframe = function(config){
		
		if($('#iframe_dialog').length > 0) {
			__CloseIframe();
		}
		
		_callback = config.callback;
		var regExp = /^(http:|https:|ftp:)\/\/.*/;
		var isRemote = regExp.test(config.url);
		
		var init = {title: config.title, scroll: config.scroll};
		
		var loadTemplate = __getTemplate(0);
		var template = __getTemplate(2, init);
		
		$('body').append(loadTemplate);
		__setCenter('#dialog_loading');
		
		$('body').append(template);
		$('#iframe_dialog').load(function(){
			if(loadOne) {
				if(_isIE6){
					barWidth = 4;
					barHeight = $('.iframe-title-box').height() + 4;
				} else {
					barWidth = $('.iframe-left').width() + $('.iframe-right').width();
					barHeight = $('.iframe-title-box').height() + $('.iframe-bottom').height();
				}
				if(isRemote) {
					var title = '\u6d88\u606f\u7a97\u53e3', 
						width = 9999999,
						height = 9999999;	
				}else{
					var title = $(this).contents().find('title').html(),
						width = $(this).contents().outerWidth() + barWidth,
						height = $(this).contents().outerHeight() + barHeight;
						if(_isIE8) width += 4;
				}
				
				config.title = (typeof(config.title) == 'string') ? config.title : title;
				config.width = typeof(config.width) == 'number' ? config.width : width;
				config.height = typeof(config.height) == 'number' ? config.height : height;
				
				if(config.width > document.documentElement.clientWidth) {
					config.width = document.documentElement.clientWidth - 20;
				}
				
				if(config.height > document.documentElement.clientHeight) {
					config.height = document.documentElement.clientHeight - 20;
				}
				
				$('#iframe_title').html(config.title)
				$('#iframe_box').width(config.width);
				$('#iframe_box').height(config.height);
				$('.iframe-content').height(config.height - barHeight);
				$(this).css({width: '100%',height: '100%'});
				
				if('number' == typeof(config.top) || 'number' == typeof(config.left) || 'number' == typeof(config.right) || 'number' == typeof(config.bottom)) {
					if('number' == typeof(config.top)) {
						$('#iframe_box').css('top', config.top);
					}
					if('number' == typeof(config.left)) {
						$('#iframe_box').css('left', config.left);
					}
					if('number' == typeof(config.right)) {
						$('#iframe_box').css('right', config.right);
					}
					if('number' == typeof(config.bottom)) {
						$('#iframe_box').css('bottom', config.bottom);
					}
				} else {
					__setCenter('#iframe_box');
				}
				
				if(config.lock) {
					createCover('iframe_lock_cover', 2004);
				}
				$('#iframe_box').css('visibility','visible');
				$('#dialog_loading').remove();
				
				if(config.move){
					if(config.lock) {
						__MousemoveDialog('#iframe_box', '.iframe-title-box');
					}
				}else{
					$('#iframe_dialog').resize(function(){
						__setCenter('#iframe_box');
					});
				}
		
				$('#iframe_close').click(function(){
					__CloseIframe();
				});
				
				
				loadOne = false;
			}
		});
		
		
		
		$('#iframe_dialog').attr('src',config.url);
	}
	
	
	var __CloseIframe = function(){
		if("function" == typeof(_callback) && object.data != null){
			_callback(ddb.data);
			_callback = null;
			object.data = null;
		}
		loadOne = true;
		if($("#iframe_box").length > 0){
			$("#iframe_box").remove();
		}
		if($('#iframe_lock_cover').length > 0){
			$("#iframe_lock_cover").remove();
		}
	}
	
	var __Dialog = function(config) {
		
		var template = '',
			init = {
				'id': 'dialog',
				'className': 'dialog-info ',
				'btn': '<a href="javascript:;" class="dialog-button" id="dialog_ok">\u786e\u5b9a</a>',
				'message': config.content
			};
		switch(config.type) {
			case 1:
				init.title = '\u63d0\u95ee\u6d88\u606f';
				init.className += 'dialog-confirm';
				init.btn += '<a href="javascript:;" class="dialog-button" id="dialog_cancel">\u53d6\u6d88</a>';
				break;
			case 2:
				init.title = '\u6210\u529f\u63d0\u793a';
				init.className += 'dialog-success';
				break;
			default:
				init.title = '\u9519\u8bef\u63d0\u9192';
				init.className += 'dialog-alert';
		};
		template = __getTemplate(1, init);
		$('body').append(template);
		
		createCover('dialog_lock_cover', 2014);

		__MousemoveDialog('#dialog_box');
		$('#dialog_close, #dialog_cancel').click(function(){
			$('#dialog_lock_cover').remove();
			$('#dialog_box').remove();
			return false;
		})
		
		$('#dialog_ok').click(function(){
			$('#dialog_lock_cover').remove();
			$('#dialog_box').remove();
			if("function" == typeof(config.callback)){
				config.callback();
			}
			return false;
		}).keydown(function(e){
			var keycode = e.which;
			return 13 == keycode ? ($(this).click(), !1) : void 0
			return false;
		});
		
		$('#dialog_box').css('visibility','visible');
		
		__setCenter('#dialog_box');
		$('#dialog_ok').focus();
	}

	
	var __Message = function(config){
		if($('#message_box').length > 0) {
			clearTimeout(_timeOut);
			$('#message_box').remove();
		}
		var init = {
			'id': 'message',
			'className': 'message-info',
			'btn': null,
			'message': config.content,
			'title': config.title
		};
		
		var template = __getTemplate(1, init);
		
		$('body').append(template);
		
		$('#message_box').width(config.width);
		$('#message_box').height(config.height);
		
		if(_isIE6){
			$('.message-content').height(config.height);
		}else{
			barHeight = $('.dialog-top').height() + $('.dialog-bottom').height();
			$('.message-content').height(config.height - barHeight);
		}
		
		var attachNum = 8;
		$('#message_box').css({
			top: document.documentElement.clientHeight,
			left: parseInt((document.documentElement.clientWidth - config.width) - attachNum),
			visibility: 'visible'
		});
		
		var top = parseInt((document.documentElement.clientHeight - config.height) - attachNum);
		$('#message_box').animate({top: top}, 'slow');
		
		$('#message_close').click(function(){
			__CloseMessage();
		});
		
		$(window).resize(function(){
			var top = parseInt((document.documentElement.clientHeight - config.height) - attachNum),
				left = parseInt((document.documentElement.clientWidth - config.width) - attachNum);
			$('#message_box').css({
				top: top,
				left: left
			});
		});
		
		if(config.time != null) {
			_timeOut = setTimeout(function(){
				__CloseMessage();
			}, config.time * 1000);
		}
	};
	
	var __CloseMessage = function(){
		clearTimeout(_timeOut);
		$('#message_box').fadeOut('slow', function(){
			$('#message_box').remove();
		});
		return false;
	};
	
	var __Image = function(url, content){

		var loadTemplate = __getTemplate(0);
		
		$('body').append(loadTemplate);
		__setCenter('#dialog_loading');
		
		Picture = new Image();
		
		Picture.onerror = function(){
			$('#dialog_loading').remove();
			ddb.alert('图片载入失败。');
		}
		
		Picture.onload = function(){
			$('#dialog_loading').remove();
			init = {url:url};
			if('string' == typeof(content)) init.content = content;
			
			var template = __getTemplate(3, init);
			$('body').append(template);
			
			var size = __SetImageSize(Picture.width, Picture.height);
			var width = size[0];
			var height = size[1];
			
			createCover('picture_lock_cover', 2004);
			
			var initSize = 200;
			if(_isIE6){
				$('#picture_box').width(initSize);
				$('#picture_object').width(initSize);
				
			}else{	
				$('#picture_box').width(initSize + $('.picture-left').width() + $('.picture-right').width());
				$('#picture_object').width(initSize);
			}
			
			__setCenter('#picture_box');
			$('#picture_box').css('visibility','visible');
			var isInfo = 'string' != typeof(content) ? false : true;
			showImage(width, height, true, isInfo);
			
			$(window).resize(function(){
				var size = __SetImageSize(Picture.width, Picture.height);
				showImage(size[0], size[1], false, isInfo);
			});
			
			$('#picture_close, #picture_lock_cover').click(function(){
				$('#picture_box').remove();
				$('#picture_lock_cover').fadeOut('fast');
			});
		}

		Picture.src = url;
		
	};
	
	var showImage = function(width, height, isAnimate, isInfo){
		if(_isIE6){
			if(isAnimate){
				$('#picture_box').animate({
					width: width,
					height: height,
					left: (document.documentElement.clientWidth - width) / 2,
					top: (document.documentElement.clientHeight - height) / 2
				}, 'slow', 'swing', function(){
					if(isInfo) {
						$('.picture-info').show();
					}
				});
				$('#picture_object').animate({
					width: width,
					height: height
				}, 'slow');
			} else {
				$('#picture_box').css({
					width: width,
					height: height,
					left: (document.documentElement.clientWidth - width) / 2,
					top: (document.documentElement.clientHeight - height) / 2
				});
				$('#picture_object').css({
					width: width,
					height: height
				});
			}
		} else{
			var w = width + 24;
				h = height + 25;
			if(isAnimate){
				$('#picture_box').animate({
					width: w,
					height: h,
					left: (document.documentElement.clientWidth - w) / 2,
					top: (document.documentElement.clientHeight - h) / 2
				}, 'slow', 'swing', function(){
					$('#picture_close').fadeIn();
					if(isInfo) {
						$('.picture-info').slideDown();
					}
				});
				$('#picture_object').animate({
					width: width,
					height: height
				}, 'slow');
			} else {
				$('#picture_box').css({
					width: w,
					height: h,
					left: (document.documentElement.clientWidth - w) / 2,
					top: (document.documentElement.clientHeight - h) / 2
				});
				$('#picture_object').css({
					width: width,
					height: height
				});			
			}
		}
	}
	
	var __SetImageSize = function(width, height){
		if(width >= document.documentElement.clientWidth - 35){
			var multiple = width / (document.documentElement.clientWidth - 35);
			width = Math.round(width / multiple);
			height = Math.round(height / multiple);
		}
		if(height >= document.documentElement.clientHeight - 50){
			var multiple = height / (document.documentElement.clientHeight - 50);
			height = Math.round(height / multiple);
			width = Math.round(width / multiple);
		}
		return [width, height];
	}
	
	var __MousemoveDialog = function(dialogBox, moveBar){
		
		moveBar = typeof(moveBar) == 'undefined' ? dialogBox : moveBar;

		if($('.iframe-content').length > 0) {
			var w = $('.iframe-content').width();
			var h = $('.iframe-content').height();
			$('.iframe-content').append('<div id="iframe_transparent" style="display:none;background:#fff;position:absolute;top:0;left:0;filter:alpha(opacity=1);-moz-opacity:0.1;-khtml-opacity:0.1;-webkit-opacity:0.1;opacity:0.1;width:'+w+'px;height:'+h+'px"></div>');
		}

		$(moveBar).mousedown(function(e) {
			var isMove = true;
			var offset = $(dialogBox).offset();
				absX = e.pageX - offset.left,
				absY = e.pageY - offset.top;
				$('#iframe_transparent').show();
				$("body").addClass('disable-select');
				$(this).bind("selectstart", function(){return false});
			$(document).mousemove(function(e) {
				$(dialogBox).stop();
				$(moveBar).css("cursor", "move");
				var left = e.pageX - absX - $(document).scrollLeft(),
					top = e.pageY - absY - $(document).scrollTop();
				$(dialogBox).css({
					left: left + "px",
					top: top + "px"
				});
			}).mouseup(function(){
				isMove = false;
				$(moveBar).css("cursor", "default");
				$("body").removeClass('disable-select');
				$(this).unbind('selectstart');
				$(this).unbind("mousemove");
				$('#iframe_transparent').hide();
			});
		});
	};
	
	var createCover = function(id, zIndex) {
		$("body").append('<div id="' + id + '" style="display:none"></div>');
		$("#"+id).css({
			position: "absolute",
			top: "0px",
			left: "0px",
			right: "0px",
			bottom: "0px",
			"z-index": zIndex,
			opacity: 0.3,
			background: "#000"
		}).show();
		
		$(window).resize(function(){
			$("#"+id).css({
				width: $(document).width(),
				height: $(document).height()
			});
		});
	}
	
	var __setCenter = function(box){
		var width = $(box).outerWidth();
		var height = $(box).outerHeight();
		
		var top = parseInt((document.documentElement.clientHeight - height) / 2),
			left = parseInt((document.documentElement.clientWidth - width) / 2);
		
		$(box).css({top: top,left: left});
	}
	
	var __getTemplate = function(id, init){
		var template = '';
		if(id==1){
			if(_isIE6){
				template = 
'<div id="' + init.id + '_box" class="' + init.id + '-IE6" style="visibility:hidden">'
+	'<div class="' + init.id + '-content">'
+    '<div class="dialog-title-bar">'
+        '<span class="dialog-title">' + init.title + '</span>'
+        '<span class="dialog-close" id="' + init.id + '_close"></span>'
+    '</div>'
+    '<div class="' + init.className + '">' + init.message + '</div>';
				if(init.btn != null) {
					template += '<div class="' + init.id + '-button-bar">' + init.btn + '</div>'
				}
				template +=
	'</div>'
+'</div>';
			} else {
				template = 
'<div id="' + init.id + '_box" style="visibility:hidden">'
+	'<table width="100%" border="0" cellpadding="0" cellspacing="0">'
+      '<tr>'
+        '<td class="dialog-top-left"></td>'
+        '<td class="dialog-top"></td>'
+        '<td class="dialog-top-right"></td>'
+      '</tr>'
+      '<tr>'
+        '<td class="dialog-left"></td>'
+        '<td class="' + init.id + '-content" valign="top">'
+        	'<div class="dialog-title-bar">'
+            	'<span class="dialog-title">' + init.title + '</span>'
+                '<span class="dialog-close" id="' + init.id + '_close"></span>'
+            '</div>'
+            '<div class="' + init.className + '">' + init.message + '</div>';
			if(init.btn != null) {
				template += '<div class="' + init.id + '-button-bar">' + init.btn + '</div>';
			}
			template +=
        '</td>'
+        '<td class="dialog-right"></td>'
+      '</tr>'
+      '<tr>'
+        '<td class="dialog-bottom-left"></td>'
+        '<td class="dialog-bottom"></td>'
+        '<td class="dialog-bottom-right"></td>'
+      '</tr>'
+'</table>'
+'</div>';
			}
		}else if(id==2) {
			if(_isIE6){
				template =
'<div id="iframe_box" class="iframe-IE6" style="visibility:hidden">'
+	'<div class="iframe-title-box"><span id="iframe_title">' + init.title + '</span><span class="iframe-close" id="iframe_close"></span></div>'
+    '<div class="iframe-content">'
+		'<iframe name="iframe_dialog" id="iframe_dialog" scrolling="' + init.scroll + '" frameborder="0" style="border:0px none;"></iframe>'
+    '</div>'
+'</div>';
			} else{
				template =
'<div id="iframe_box" style="visibility:hidden">'
+	'<table width="100%" border="0" cellspacing="0" cellpadding="0">'
+      '<tr>'
+        '<td class="iframe-top-left"></td>'
+        '<td class="iframe-title-box"><span id="iframe_title">' + init.title + '</span><span class="iframe-close" id="iframe_close"></span></td>'
+        '<td class="iframe-top-right"></td>'
+      '</tr>'
+    '</table>'
+	'<table width="100%" border="0" cellspacing="0" cellpadding="0">'
+      '<tr>'
+      	'<td class="iframe-left"></td>'
+        '<td class="iframe-content">'
+        	'<iframe name="iframe_dialog" id="iframe_dialog" scrolling="' + init.scroll + '" frameborder="0" style="border:0px none;"></iframe>'
+        '</td>'
+        '<td class="iframe-right"></td>'
+      '</tr>'
+    '</table>'
+	'<table width="100%" border="0" cellspacing="0" cellpadding="0">'
+      '<tr>'
+        '<td class="iframe-bottom-left"></td>'
+        '<td class="iframe-bottom">&nbsp;</td>'
+        '<td class="iframe-bottom-right"></td>'
+      '</tr>'
+    '</table>'
+'</div>';
			}
		} else if(id==3) {
			if(_isIE6){
				template =
'<div id="picture_box" class="picture-IE6"  style="visibility:hidden;">'
+	'<div class="picture-content">'
+        '<a href="' + init.url + '" target="_blank"><img id="picture_object" src="' + init.url + '" /></a>';
				if('string' == typeof(init.content)){
					template += '<div class="picture-info" style="display:none;"><span>' + init.content + '</span></div>';
				}
				template +=
    '</div>'
+'</div>';
			} else {
				template =
'<div id="picture_box" style="visibility:hidden;">'
+	'<table width="100%" border="0" cellpadding="0" cellspacing="0">'
+      '<tr>'
+        '<td class="picture-top-left"></td>'
+        '<td class="picture-top"></td>'
+        '<td class="picture-top-right"></td>'
+      '</tr>'
+      '<tr>'
+        '<td class="picture-left"></td>'
+        '<td class="picture-content">'
+        	'<a href="' + init.url + '" target="_blank"><img id="picture_object" src="' + init.url + '"/></a>'
+            '<a href="javascript:;" id="picture_close" class="picture-close" style="display:none;"></a>';
				if('string' == typeof(init.content)){
					template += '<div class="picture-info" style="display:none;"><span>' + init.content + '</span></div>';
				}
				template +=
        '</td>'
+        '<td class="picture-right"></td>'
+      '</tr>'
+      '<tr>'
+        '<td class="picture-bottom-left"></td>'
+        '<td class="picture-bottom"></td>'
+        '<td class="picture-bottom-right"></td>'
+      '</tr>'
+'</table>'
+'</div>';
			}
		} else {
			template = '<div id="dialog_loading"><span></span></div>';
		}
		return template;
	}
	
	$.extend(object, {
		'alert' : function(content, callback){
			var config = {'content': content, 'callback': callback, 'type': 0};
			__Dialog(config);
		},
		'confirm': function(content, callback){
			var config = {'content': content, 'callback': callback, 'type': 1};
			__Dialog(config);
		},
		'success' : function(content, callback){
			var config = {'content': content, 'callback': callback, 'type': 2};
			__Dialog(config);
		},
		'message': function(content, clearTime, config){
			config = 'object' != typeof(config) ? {title: null, width: null, height: null} : config;
			config.width = 'number' != typeof(config.width) ? 270 : config.width;
			config.height = 'number' != typeof(config.height) ? 180 : config.height;
			config.title = 'string' != typeof(config.title) ? '系统消息' : config.title;
			config.content = 'string' != typeof(content) ? '' : content;
			config.time = 'number' != typeof(clearTime) ? null : clearTime;
			__Message(config);
		},
		'dialog': function(url, callback, config) {
			config = 'object' != typeof(config) ? {
				title: null,
				callback: null,
				width: null,
				height: null,
				scroll: 'auto',
				top: null,
				left: null,
				bottom: null,
				right: null,
				move: true,
				lock: true
			} : config;
			config.move = 'boolean' != typeof(config.move) ? true : config.move;
			config.lock = 'boolean' != typeof(config.lock) ? true : config.lock;
			config.url = 'string' != typeof(url) ? '' : url;
			config.callback = callback;
			
			if(config.scroll != 'auto') {
				if(typeof(config.scroll) == 'boolean'){
					if(config.scroll){
						config.scroll = 'yes';
					} else {
						config.scroll = 'no';
					}
				}else{
					config.scroll = 'auto';
				}
			}
			__Iframe(config);
		},
		'close': function(){
			__CloseIframe();
		},
		'reload': function(){
			//按框架层级配置
			iframeIndex.mainFrame.location.reload();
			__CloseIframe();
		},
		'image': function(url, content) {
			__Image(url, content);
		},
		'data': null
	});
})(jQuery, ddb = {});