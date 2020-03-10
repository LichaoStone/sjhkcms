/*
 * @# Dialect.java Jul 30, 2012 2:37:54 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.common.util.orm;

/*
 * @author pananz
 * TODO
 */
public interface Dialect {

	/**
	 * 数据库是否支持分页.
	 * 
	 * @return 返回true, 若数据库支持分页, 否则返回false.
	 */
	boolean isPaginationSupportable();

	/**
	 * 构造分页的sql语句.
	 * 
	 * @param sql
	 *            分页查询的原始语句.
	 * @param offset
	 *            跳过的记录数.
	 * @param limit
	 *            总共显示的记录数.
	 * @return 用于分页的sql语句.
	 */
	String getPaginationSql(String sql, int offset, int limit);

	/**
	 * 构造求总数的sql语句.
	 * 
	 * @param sql
	 *            要求总数的sql语句.
	 * @return 求总数的sql语句.
	 */
	String getCountSql(String sql);
}
