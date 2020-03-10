package com.br.ott.common.annotation;

import java.util.Date;

/* 
 *  用于注解字段的测试实体类
 *  文件名：A.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：陈登民
 *  创建时间：2012-12-25 - 下午4:54:26
 */
public class A {
	/**
	 * @FieldName("年龄") 使用该注解，在使用对象比较时，才能返回该注解的值，否则返回字段名age
	 */
	@FieldName("年龄")
	private int age;
	
	@FieldName("名称")
	private String username;
	
	@FieldName("昵称")
	private String nickname;
	
	@FieldName("出生日期")
	private Date date;
	
	private String temp;
	
	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
