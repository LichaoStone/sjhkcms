package com.br.ott.common.util.plugin.cache;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.br.ott.common.util.plugin.cache.impl.EnhancedCachingManagerImpl;

/**
 * 
 * MyBatis 全局二级缓存拦截器
 * 该拦截器负责处理拦截请求，执行相对应的Cache、CacheKey和statementId等操作
 * @author lizhenghg
 * @date   2018-01-29
 */

@Intercepts(value = {
		@Signature(args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}, method = "query", type = Executor.class),
		@Signature(args = {MappedStatement.class, Object.class}, method = "update", type = Executor.class),
		@Signature(args = {boolean.class}, method = "commit", type = Executor.class),
		@Signature(args = {boolean.class}, method = "rollback", type = Executor.class),
		@Signature(args = {boolean.class}, method = "close", type = Executor.class)
})
public class EnhancedCachingExecutor implements Interceptor {
	private CacheKeysPool queryCacheOnCommit = new CacheKeysPool();
	private Set<String> updateStatementOnCommit = new HashSet<String>();
	EnhancedCachingManager cachingManager = EnhancedCachingManagerImpl.getInstance();
	
	public Object intercept(Invocation invocation) throws Exception {
		String name = invocation.getMethod().getName();
		Object result =null;
		if ("query".equals(name)) {
			result = this.processQuery(invocation);
		} else if("update".equals(name)) {
			result = this.processUpdate(invocation);
		} else if("commit".equals(name)) {
			result = this.processCommit(invocation);
		} else if("rollback".equals(name)) {
			result = this.processRollback(invocation);
		} else if("close".equals(name)) {
			result = this.processClose(invocation);
		}
		return result;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	protected Object processQuery(Invocation invocation) throws Exception {
		Object result = invocation.proceed();
		if (cachingManager.isCacheEnabled()) {
			Object[] args = invocation.getArgs();
			MappedStatement mappedStatement = (MappedStatement)args[0];
			//如果本条statementId表示的查询语句配置了 flushCache=true，则清空querCacheOnCommit缓存
			if (mappedStatement.isFlushCacheRequired()) {
				queryCacheOnCommit.clear();
			}
			//如果本条statementId表示的查询语句配置了使用缓存，并且二级缓存不为空，则将StatementId 和对应的二级缓存对象映射关系添加到全局缓存映射管理器中
			if (mappedStatement.isUseCache() && mappedStatement.getCache() != null) {
				cachingManager.appendStatementCacheMap(mappedStatement.getId(), mappedStatement.getCache());
			}
			Object parameter = args[1];
			RowBounds rowBounds = (RowBounds)args[2];
			Executor executor = (Executor)invocation.getTarget();
			//BoundSql boundSql = mappedStatement.getBoundSql(parameter);
			//记录本次查询所产生的CacheKey
			CacheKey cacheKey= executor.createCacheKey(mappedStatement, parameter, rowBounds);
			queryCacheOnCommit.putElement(mappedStatement.getId(), cacheKey);
		}
		return result;
	}
	
	protected Object processUpdate(Invocation invocation) throws Exception {
		Object result = invocation.proceed();
		MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
		updateStatementOnCommit.add(mappedStatement.getId());
		return result;
	}

	protected Object processCommit(Invocation invocation) throws Exception {
		Object result  = invocation.proceed();
		refreshCache();
		return result;
	}

	protected Object processRollback(Invocation invocation) throws Exception {
	    Object result = invocation.proceed();
	    clearSessionData();
		return result;
	}

	protected Object processClose(Invocation invocation) throws Exception {
		Object result = invocation.proceed();
		boolean forceRollback = (Boolean) invocation.getArgs()[0];
		if (forceRollback) {
			clearSessionData();
		} else {
			refreshCache();
		}
		return result;
	}
	
	/**
	 * 
	 * 当SqlSession 执行了commit()、rollback()、close()方法，
	 * Session级别的查询语句产生的CacheKey集合以及  执行的更新语句集合应该被清空
	 */
	private synchronized void clearSessionData() {
		queryCacheOnCommit.clear();
	    updateStatementOnCommit.clear();
	}
	
	/**
	 * 刷新会话缓存，有三件事要做:
     * 1、将此会话范围查询日志添加到全局缓存管理器
     * 2、根据"依赖"文件中配置的更新语句清除相关的缓存
     * 3、清除会话数据
	 */
	private synchronized void refreshCache() {
		cachingManager.refreshCacheKey(queryCacheOnCommit);
		cachingManager.clearRelatedCaches(updateStatementOnCommit);
		clearSessionData();
	}
	
	/**
	 * 
	 * 
	 * Executor插件配置信息加载点
	 * properties中有 "dependency" 属性来指示 配置的缓存依赖配置信息，读取文件，初始化EnhancedCacheManager
	 */
	public void setProperties(Properties properties) {
		if (!cachingManager.isInitialized()) {
			cachingManager.initialize(properties);
		}
	}
}
