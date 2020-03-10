package com.br.ott.common.util.orm.mybatis;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.builder.xml.dynamic.ForEachSqlNode;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.webmacro.PropertyException;

import com.br.ott.common.util.Pagination;
import com.br.ott.common.util.ReflectUtil;
import com.br.ott.common.util.StringUtil;

/* 
 *  
 *  文件名：PaginationPlugin.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  创建人：pananz
 *  创建时间：2013-1-22 - 下午1:38:14
*/
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PaginationPlugin implements Interceptor {
	/**< plugins >   
	< plugin   interceptor = "com.ott.common.orm.interceptor.PaginationInterceptor" >   
	< property   name = "dialect"   value = "mysql" />   
	< property   name = "pageSqlId"   value = ".*listPage.*" />   
	</ plugin >   
	</ plugins >   */
	private String DIALECT = ""; //数据库方言
	private String _SQL_PATTERN = ""; //mapper.xml中需要拦截的ID(正则匹配) 

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (invocation.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectUtil.getValueByFieldName(statementHandler,
					"delegate");
			MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getValueByFieldName(delegate,
					"mappedStatement");
			if (mappedStatement.getId().matches(_SQL_PATTERN)) { //拦截需要分页的SQL   
				BoundSql boundSql = delegate.getBoundSql();
				Object parameterObject = boundSql.getParameterObject();
				if (parameterObject == null) {
					throw new NullPointerException("parameterObject尚未实例化！");
				} else {
					String sql = boundSql.getSql();
					Connection connection = (Connection) invocation.getArgs()[0];
					getCount(sql, connection, mappedStatement, parameterObject, boundSql);
					Pagination pagination = convertParameter(parameterObject);
					ReflectUtil.setValueByFieldName(boundSql, "sql", generatePageSql(sql, pagination)); //将分页sql语句反射回BoundSql.   
				}
			}
		}
		return invocation.proceed();
	}

	/**
	 * 创建人：pananz
	 * 创建时间：2013-1-30 - 下午3:18:36
	 * @param sql
	 * @param connection
	 * @param mappedStatement
	 * @param parameterObject
	 * @param boundSql
	 * @return
	 * @throws SQLException
	 * 返回类型：int
	 * @exception throws
	 */
	private static void getCount(String sql, Connection connection, MappedStatement mappedStatement,
			Object parameterObject, BoundSql boundSql) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), sql,
					boundSql.getParameterMappings(), parameterObject);
			setParameters(ps, mappedStatement, countBS, parameterObject);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	/**
	 * 按数据库类型设置分页语句
	 * 创建人：pananz
	 * 创建时间：2013-1-30 - 下午3:17:53
	 * @param sql
	 * @param pagination
	 * @return
	 * 返回类型：String
	 * @exception throws
	 */
	private String generatePageSql(String sql, Pagination pagination) {
		if (pagination != null && !StringUtil.isEmpty(DIALECT)) {
			StringBuffer pageSql = new StringBuffer();
			if ("mysql".equals(DIALECT)) {
				pageSql.append(sql);
				pageSql.append(" limit " + pagination.getCurrentResult() + "," + pagination.getShowCount());
			} else if ("oracle".equals(DIALECT)) {
				pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
				pageSql.append(sql);
				pageSql.append(") as tmp_tb where ROWNUM<=");
				pageSql.append(pagination.getCurrentResult() + pagination.getShowCount());
				pageSql.append(") where row_id>");
				pageSql.append(pagination.getCurrentResult());
			}
			return pageSql.toString();
		} else {
			return sql;
		}
	}

	@Override
	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	@Override
	public void setProperties(Properties p) {
		DIALECT = p.getProperty("dialect");
		if (StringUtil.isEmpty(DIALECT)) {
			try {
				throw new PropertyException("dialect property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
		_SQL_PATTERN = p.getProperty("pageSqlId");
		if (StringUtil.isEmpty(_SQL_PATTERN)) {
			try {
				throw new PropertyException("pageSqlId property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置条件查询参数
	 * 创建人：pananz
	 * 创建时间：2013-1-30 - 下午3:16:59
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 * 返回类型：void
	 * @exception throws
	 */
	private static void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)
							&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value).getValue(
									propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					@SuppressWarnings("unchecked")
					TypeHandler<Object> typeHandler = (TypeHandler<Object>) parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName
								+ " of statement " + mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}

	private static Pagination convertParameter(Object parameterObject) throws Exception {
		Pagination pagination = null;
		if (parameterObject instanceof Pagination) { //参数就是Pagination实体   
			pagination = (Pagination) parameterObject;
			pagination.setEntityOrField(true);
		} else { //参数为某个实体，该实体拥有Pagination属性   
			Field pageField = ReflectUtil.getFieldByFieldName(parameterObject, "pagination");
			if (pageField != null) {
				pagination = (Pagination) ReflectUtil.getValueByFieldName(parameterObject, "pagination");
				if (pagination == null)
					pagination = new Pagination();
				pagination.setEntityOrField(false);
				//通过反射，对实体对象设置分页对象   
				ReflectUtil.setValueByFieldName(parameterObject, "pagination", pagination);
			} else {
				throw new NoSuchFieldException(parameterObject.getClass().getName() + "不存在 pagination 属性！");
			}
		}
		return pagination;
	}

}
