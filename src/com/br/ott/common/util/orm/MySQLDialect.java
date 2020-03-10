/*
 * @# PaginationInterceptor.java Jul 30, 2012 2:12:38 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.common.util.orm;

/**
 * Oracle数据库的分页实现.
 * 
 * @author alvin hwang
 */
public class MySQLDialect implements Dialect {

	@Override
	public boolean isPaginationSupportable() {
		return true;
	}

	@Override
	public String getPaginationSql(String sql, int offset, int limit) {
		if (offset < 0 || limit <= 0) {
			return sql;
		}
		String trimmedSql = sql.trim();
		StringBuffer sb = new StringBuffer(trimmedSql.length() + 25);
		sb.append(trimmedSql).append(" limit ").append(offset).append(",")
				.append(limit);
		return sb.toString();
	}
	
	@Override
	public String getCountSql(String sql) {
		return "select count(*) from (" + sql + ") as countsql";
	}
}
