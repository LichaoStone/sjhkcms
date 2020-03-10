/*
 * @# OTTRuntimeException.java Aug 23, 2012 2:19:21 PM
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
public class OTTRuntimeException extends RuntimeException {
	private static ReadProperties prop = new ReadProperties("exception-msg.properties");
	private static final long serialVersionUID = 7490642728319542370L;
	private static Logger log = Logger.getLogger("ott.exception");

	private String code;
	private String message;
	private Throwable cause;

	public OTTRuntimeException(String code, String msg) {
		this(code, msg, null);
	}

	public OTTRuntimeException(String code) {
		this(code, getShow(code), null);
	}

	public OTTRuntimeException(String code, Throwable cause) {
		this(code, getShow(code), cause);
	}

	public OTTRuntimeException(String code, String message, Throwable cause) {
		super(String.format("错误编码是：%s，错误提示是：%s", code, message), cause);
		this.code = code;
		this.message = message;
		this.cause = cause;
		log.error(getLogMessage(), cause);
	}

	public String getLogMessage() {
		if (this.cause == null) {
			return String.format("错误编码是：%s，错误提示是：%s，异常类型是：%s", code, message, this.getClass().getName());
		} else {
			return String.format("错误编码是：%s，错误提示是：%s，异常类型是：%s，异常源消息：%s", code, message, cause.getClass().getName(),
					cause.getMessage());
		}
	}

	protected static String getShow(String code) {
		try {
			return prop.getPara(code);
		} catch (Exception e) {
			return SysCommonCode.EXCEPTION_MSG_READER_ERROR + ":[" + "异常消息读取失败" + "]";
		}
	}
}
