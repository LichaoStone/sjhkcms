

/**
 * 针对jquery.validate的一个美化信息提示控件.
 * 
 * @author alvin hwang
 */
(function( $, undefined ) {
	
	var sequence = 0;
	
	$.handleError = function(error, element) {
		var xOffset = 0; // x distance from mouse
    	var yOffset = 8; // y distance from mouse  
    	if ($.browser.msie && $.browser.version < 8) {
    		yOffset = 12;
    	}
    
		var tipHolder = document.createElement('p');
		var $tipHolder = $(tipHolder);
		
		if ($.browser.msie && $.browser.version < 7) {
    		$tipHolder.width(120);
    	}
    	
		var tipHolderId = 'validate-error-tip-' + (sequence++);
		$tipHolder.addClass('validate-error-tip').attr('id', tipHolderId);
		
		var $error = $(error);
		$error.attr('validateErrorTipId', tipHolderId);
		
		var tipImg = document.createElement('img');
		$(tipImg).addClass('validate-error-tip-img').attr('src', window.ctx+'/images/vtip-arrow.png');
		
		$tipHolder.append(tipImg).append(error).insertAfter(element);
		var elemOffset = element.offset();
		var top = elemOffset.top + element.height() + yOffset;
		var left = elemOffset.left + element.width() / 2 + xOffset;
		$tipHolder.css("top", top + "px").css("left", left + "px")
		
		element.hover(function(e) {
			if ($.trim($tipHolder.text())) {
				$tipHolder.show();
			}
		}, function(e) {
			$tipHolder.hide();
		}).focus(function(e) {
			if ($.trim($tipHolder.text())) {
				$tipHolder.show();
			}
		}).blur(function(e) {
			$tipHolder.hide();
		});
	};

	$.handleSuccess = function(label) {
		$('#' + $(label).attr('validateErrorTipId')).hide();
	};
	
	$.invalidAlertHandler = function(e, validator) {
		var errors = validator.numberOfInvalids();
		if (errors) {
			var message = '页面内有' + errors + '个信息录入不正确, 请查看.\n'
			$(validator.errorList).map(function(index, error) {
				message += (index > 0 ? '\n' : '') + (index + 1) + '. ' + error.message;
			});
			window.alert(message);
		}
	};
})(jQuery);

$.validator.addMethod('lessThanEqual', function(value, element, param) {
	var target = $(param).unbind(".validate-lessThanEqual").bind("blur.validate-lessThanEqual", function() {
			$(element).valid();
		});
	if(!value||!target.val()){
		return true;
	}
	return parseFloat(value) <= parseFloat(target.val());
}, '');
	
$.validator.addMethod('mobileCN', function(value, element) {
    return this.optional(element) || /^0*(13|15|18)\d{9}$/.test(value);
}, '请输入正确手机格式,如18800000088');
	
$.validator.addMethod('unique', function(value, element, param) {
	var original = $(element).attr('data-original');
	if (value === original) {
		return true;
	}
    return $.validator.methods.remote.call(this, value, element, param);
}, '已存在该值, 请重新输入新值');

$.validator.addMethod('equalValue', function(value, element, param) {
    return $.validator.methods.remote.call(this, value, element, param);
}, '验证码有误，请重新输入验证码');

$.validator.addMethod('phoneCN', function(value, element) {
    return this.optional(element) || /^(\d{3,4}-)?(\d{3,4})?\d{7,8}(-\d{3,4})?$/.test(value) || /^0*(13|15|18)\d{9}$/.test(value);
}, '请填写正确电话格式，如020-88888888或089888888888或88888888或18800000088"');

$.validator.addMethod('account', function(value, element, param) {
    return this.optional(element) || /^[a-z-./_A-Z0-9]+$/i.test(value);
}, '{0}只能是字母、数字、下划线、连字符、或圆点');

$.validator.addMethod('postCN', function(value, element){
	return this.optional(element) || /^[0-9]{6}$/.test(value);
}, '请填写正确邮编格式，如510036或510035"');

$.validator.addMethod('faxCN', function(value, element){
	return this.optional(element) || /^(\d{3,4}-)?(\d{3,4})?\d{7,8}(-\d{3,4})?$/.test(value);
}, '请填写正确传真格式，如88888888或02088888888或88888888-808"');

$.validator.addMethod('radioFileType', function(value, element, param) {
	var target = $(param).unbind(".validate-radioFileType").bind("blur.validate-radioFileType", function() {
			$(element).valid();
		});
	if(!value||!target.val()){
		return true;
	}
	var suffix = typeof target.val() == "string" ? target.val().replace(/,/g, '|') : "apk|rar|zip";
	return this.optional(element) || value.match(new RegExp(".(" + suffix + ")$", "i"));
}, '请选择正确文件格式');

$.validator.addMethod('noChinese', function(value, element, param) {
	var flag= false;
	for(var i=0; i< value.length; i++){
		if(value.charCodeAt(i)> 255){
			flag = true;
			break;
		}
	}
    return this.optional(element) || !flag;
}, '{0}不能出现汉字');

$.validator.addMethod('checkFileName', function(value, element, param) {
	var pos = value.lastIndexOf("/");
	if(pos == -1){
　 		pos = value.lastIndexOf("\\");
	}
	var fileName = value.substr(pos +1);
	var flag= false;
	for(var i=0; i< fileName.length; i++){
		if(fileName.charCodeAt(i)> 255){
			flag = true;
			break;
		}
	}
    return this.optional(element) || !flag;
}, '{0}不能出现汉字');

	
/*
 * jQuery validate.password plug-in 1.0
 *
 * http://bassistance.de/jquery-plugins/jquery-plugin-validate.password/
 *
 * Copyright (c) 2009 Jörn Zaefferer
 *
 * $Id$
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 */
(function($) {
	
	var LOWER = /[a-z]/,
		UPPER = /[A-Z]/,
		DIGIT = /[0-9]/,
		DIGITS = /[0-9].*[0-9]/,
		SPECIAL = /[^a-zA-Z0-9]/,
		SAME = /^(.)\1+$/;
		
	function rating(rate, message) {
		return {
			rate: rate,
			messageKey: message
		};
	}
	
	function uncapitalize(str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	$.validator.passwordRating = function(password, username) {
		if (!password || password.length < 6)
			return rating(0, "too-short");
		if (!username && password.toLowerCase().match(username.toLowerCase()))
			return rating(0, "similar-to-username");
		if (SAME.test(password))
			return rating(1, "very-weak");
		
		var lower = LOWER.test(password),
			upper = UPPER.test(uncapitalize(password)),
			digit = DIGIT.test(password),
			digits = DIGITS.test(password),
			special = SPECIAL.test(password);
		
		if (lower && upper && digit || lower && digits || upper && digits || special)
			return rating(4, "strong");
		if (lower && upper || lower && digit || upper && digit)
			return rating(3, "good");
		return rating(2, "weak");
	}
	
	$.validator.passwordRating.messages = {
		"similar-to-username": "与帐号过于相似",
		"too-short": "长度过短",
		"very-weak": "非常弱",
		"weak": "弱",
		"good": "良好",
		"strong": "强"
	}
	
	$.validator.addMethod("password", function(value, element, param) {
		var undefined;
		// use untrimmed value
		var password = element.value,
		// get username for comparison, if specified
			username = [], force = true;
		 if (typeof param == 'boolean') {
			username = [];
		} else if (typeof param == 'string') {
			username = param;
		} else {
			username = param['username'];
			force = param['force'] === undefined ? true : param['force'];
		}
		username = $(username);
			
		var rating = $.validator.passwordRating(password, username.val());
		// update message for this field
		
		var meter = $(".password-meter", element.form);
		
		meter.find(".password-meter-bar").removeClass().addClass("password-meter-bar").addClass("password-meter-" + rating.messageKey);
		meter.find(".password-meter-message")
		.removeClass()
		.addClass("password-meter-message")
		.addClass("password-meter-message-" + rating.messageKey)
		.text($.validator.passwordRating.messages[rating.messageKey]);
		// display process bar instead of error message
		
		return force ? rating.rate > 2 : true;
	}, "密码强度不够");
	// manually add class rule, to make username param optional
	$.validator.classRuleSettings.password = { password: true };
	
})(jQuery);

