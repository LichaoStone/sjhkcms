package com.br.ott.common.util.plugin.cache.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;

import com.br.ott.common.util.StringUtil;
import com.br.ott.common.util.plugin.cache.CacheKeysPool;
import com.br.ott.common.util.plugin.cache.EnhancedCachingManager;

/**
 * MyBatis 全局二级缓存管理器实现类
 * 负责管理MyBatis内部的所有二级缓存Cache
 * 该管理器维护着MyBatis所有的查询所产生的CacheKey集合，当有update操作执行时，会根据此update操作对应的StatementId，查看此StatementId是否指定了要刷新的查询缓存
 * @author lizhenghg
 * @date   2018-01-29
 */
public class EnhancedCachingManagerImpl implements EnhancedCachingManager{
	
	//每一个statementId 更新依赖的statementId集合
	private Map<String, Set<String>> observers = new ConcurrentHashMap<String,Set<String>>();
	
	//全局性的  statemntId与CacheKey集合
	private CacheKeysPool sharedCacheKeysPool = new CacheKeysPool();
	
	public CacheKeysPool getCacheKeysPool() {
		return this.sharedCacheKeysPool;
	}
	public Map<String, Cache> getCaches() {
		return this.holds;
	}
	
	//记录每一个statementId 对应的Cache 对象
	private Map<String, Cache> holds = new ConcurrentHashMap<String,Cache>();
	
	private boolean initialized = false;
	private boolean cacheEnabled = false;
	
	private static EnhancedCachingManagerImpl enhancedCacheManager;

	private EnhancedCachingManagerImpl() {}
	
	public static EnhancedCachingManagerImpl getInstance() {
		return enhancedCacheManager == null ? (enhancedCacheManager = new EnhancedCachingManagerImpl()) : enhancedCacheManager;
	}
	
	public Map<String,Set<String>> getObservers() {
		return observers;
	}
	
	public void refreshCacheKey(CacheKeysPool keysPool) {
		sharedCacheKeysPool.putAll(keysPool);
	}
	
	public void clearRelatedCaches(final Set<String> set) {
		if (CollectionUtils.isNotEmpty(set)) {
			for (String observable : set) {
				Set<String> relatedStatements = observers.get(observable);
				if (CollectionUtils.isNotEmpty(relatedStatements)) {
					for (String statementId : relatedStatements) {
						Cache cache = holds.get(statementId);
						Set<Object> cacheKeys = sharedCacheKeysPool.get(statementId);
						if (CollectionUtils.isNotEmpty(cacheKeys)) {
							for (Object cacheKey: cacheKeys) {
								cache.removeObject(cacheKey);
							}
						}
					}
					sharedCacheKeysPool.remove(observable);
				}
			}
		}
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public void initialize(Properties properties) {
		String dependency = properties.getProperty("dependency");
		if (StringUtil.isNotEmpty(dependency)) {
			InputStream inputStream = null;
			try {
				inputStream = Resources.getResourceAsStream(dependency);
				XPathParser parser = new XPathParser(inputStream);
				List<XNode> statements = parser.evalNodes("/dependencies/statements/statement");
				if (CollectionUtils.isNotEmpty(statements)) {
					for (XNode node :statements) {
						Set<String> temp = new HashSet<String>();
						List<XNode> obs = node.evalNodes("observer");
						for (XNode observer:obs) {
							temp.add(observer.getStringAttribute("id"));
						}
						this.observers.put(node.getStringAttribute("id"), temp);
					}
					initialized = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		//cacheEnabled
		String cacheEnabled = properties.getProperty("cacheEnabled", "true");
		if ("true".equals(cacheEnabled)) {
			this.cacheEnabled = true;
		}
	}
	
	public void appendStatementCacheMap(String statementId, Cache cache) {
		if (holds.containsKey(statementId) && holds.get(statementId) != null) {
			return;
		}
		holds.put(statementId, cache);
	}
	
	public boolean isCacheEnabled() {
		return cacheEnabled;
	}
}