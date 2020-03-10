/*
 * @# PaginationInterceptor.java Jul 30, 2012 2:12:38 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.common.util;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import com.br.ott.Global;
import com.br.ott.common.exception.OTTException;
import com.br.ott.common.exception.OTTRuntimeException;
import com.br.ott.common.exception.SystemExceptionCode.SysCommonCode;

/**
 * 发送电子邮件工具类
 * @author pananz
 */
public class SendEmailUtils {

	private static Logger log = Logger.getLogger(SendEmailUtils.class);

	/**
	 * 发送电子邮件
	 * @param to 接收者邮箱地址
	 * @param emailSubject 邮件主题
	 * @param emailContent 邮件内容
	 * @param contentType 邮件内容类型
	 */
	/*public static void send(String to, String emailSubject, String emailContent, String contentType)
			throws OTTException {

		HtmlEmail htmlEmail = new HtmlEmail();

		try {
			// 设置接收邮件的邮件服务器
			htmlEmail.setHostName(Global.EMAIL_HOSTNAME);
			// 用户名密码验证
			htmlEmail
					.setAuthenticator(new DefaultAuthenticator(Global.SEND_EMAIL_USERNAME, Global.SEND_EMAIL_PASSWORD));
			htmlEmail.addTo(to);
			htmlEmail.setFrom(Global.SEND_EMAIL_ADRRESS);
			htmlEmail.setSubject(emailSubject);
			htmlEmail.setMsg(emailContent);
			htmlEmail.setContent(emailContent, contentType);
			htmlEmail.send();
		} catch (EmailException e) {
			log.error("发送电子邮件出错！" + e.getMessage(), e);
			throw new OTTException(SysCommonCode.UNKNOWN_ERROR, e);
		} catch (OTTRuntimeException e) {
			log.error("发送电子邮件出错！" + e.getMessage(), e);
			throw e;
		}
	}*/

	/**
	 * 发送HTML格式电子邮件
	 * @param to 接收者邮箱地址
	 * @param emailSubject 邮件主题
	 * @param emailContent 邮件内容
	 */
	/*public static void sendHtml(String to, String emailSubject, String emailContent) throws OTTException {
		send(to, emailSubject, emailContent, "text/html;charset=utf-8");
	}*/

	/**
	 * 发送文本格式电子邮件
	 * @param to 接收者邮箱地址
	 * @param emailSubject 邮件主题
	 * @param emailContent 邮件内容
	 */
	/*public static void sendText(String to, String emailSubject, String emailContent) throws OTTException {
		send(to, emailSubject, emailContent, "text/plain;charset=utf-8");
	}*/
}
