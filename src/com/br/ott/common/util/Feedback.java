/*
 * @# Feedback.java Jul 25, 2012 5:29:27 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.common.util;

/*
 * @author pananz
 * TODO
 * 页面反馈信息类
 */
public final class Feedback {

	private boolean successful;
	private String message;
	private String type;
	private Object obj;
	private Object obj2;
	public Feedback(boolean successful, String message) {
		this.successful = successful;
		this.message = message;
	}

	public static Feedback success(String message) {
		return new Feedback(true, message);
	}

	public static Feedback fail(String message) {
		return new Feedback(false, message);
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public Feedback setType(String type) {
		this.type = type;
		return this;
	}

	public Object getObj() {
		return obj;
	}

	public Feedback setObj(Object obj) {
		this.obj = obj;
		return this;
	}

	public Object getObj2() {
		return obj2;
	}

	public Feedback setObj2(Object obj2) {
		this.obj2 = obj2;
		return this;
	}
	
}
