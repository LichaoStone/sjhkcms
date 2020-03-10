/*
 * @# PaginationInterceptor.java Jul 30, 2012 2:12:38 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.common.util.orm;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 通过系统参数检测不同的数据库.
 * 
 * @author alvin hwang
 */
public final class DialectFactory {

	/**
	 * 系统参数值.
	 */
	public static final String CONFIG_DBTYPE = "DB_TYPE";
	private static final String DB_TYPE = System.getProperty(CONFIG_DBTYPE);

	private static final Dialect DEFAULT_DIALECT = new OralceDialect();
	private static final Map<String, Dialect> DIALECTS = new HashMap<String, Dialect>();
	static {
		/**
		 * Oracle数据库名.
		 */
		DIALECTS.put("oracle", new OralceDialect());
		/**
		 * MySQL数据库名.
		 */
		DIALECTS.put("mysql", new MySQLDialect());
	}

	private DialectFactory() {
		// empty!
	}
	
	/**
	 * 通过运行参数判断数据库方言.
	 * 
	 * @return 数据库方言
	 */
	public static Dialect getDialect() {
		return getDialect(DB_TYPE);
	}

	/**
	 * 通过指定的参数判断数据库方言.
	 * 
	 * @param dbType
	 *            数据库类型, 如: mysql, oracle, postgresql等.
	 * @return 数据库方言
	 * @since 1.0.2
	 */
	public static Dialect getDialect(String dbType) {
		if (StringUtils.isBlank(dbType)) {
			return DEFAULT_DIALECT;
		}
		Dialect dialect = DIALECTS.get(dbType.toLowerCase(Locale.ENGLISH));
		return dialect == null ? DEFAULT_DIALECT : dialect;
	}
}
