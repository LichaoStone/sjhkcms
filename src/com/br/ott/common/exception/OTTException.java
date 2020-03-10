/*
 * @# OTTException.java Aug 23, 2012 1:57:45 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.common.exception;

import org.apache.log4j.Logger;

import com.br.ott.common.exception.SystemExceptionCode.SysCommonCode;
import com.br.ott.common.util.ReadProperties;

/*
 * @author pananz
 * TODO
 */
public class OTTException extends Exception {
	private static ReadProperties prop = new ReadProperties("exception-msg.properties");

	private static final long serialVersionUID = 5963823262602684523L;
	private static Logger log = Logger.getLogger("ott.exception");

	private String code;
	private String message;
	private Throwable cause;

	public String getCode() {
		return code;
	}
	
	public String getMessage(){
		return message;
	}

	public OTTException(String code) {
		this(code, getContent(code));
	}

	public OTTException(String code, String message) {
		this(code, message, null);
	}

	public OTTException(String code, Throwable cause) {
		this(code, getContent(code), cause);
	}

	public OTTException(String code, String message, Throwable cause) {
		super(String.format("错误编码：%s，信息：%s", code, message), cause);
		this.code = code;
		this.message = message;
		this.cause = cause;
		log.error(getLogMessage(), cause);
	}

	public static String getContent(String code) {
		try {
			return prop.getPara(code);
		} catch (Exception e) {
			return SysCommonCode.EXCEPTION_MSG_READER_ERROR + ":[" + "异常消息读取失败" + "]";
		}
	}

	public String getLogMessage() {
		if (this.cause == null) {
			return String.format("错误编码：%s，信息：%s，异常类型：%s", code, message, this.getClass().getName());
		} else {
			return String.format("错误编码：%s，信息：%s，异常类型：%s，异常源消息：%s", code, message, cause.getClass().getName(), cause
					.getMessage());
		}
	}
}
