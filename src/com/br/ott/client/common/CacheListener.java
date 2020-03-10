package com.br.ott.client.common;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.br.ott.client.common.entity.Dictionary;
import com.br.ott.client.common.mapper.DictionarysMapper;
import com.br.ott.client.common.service.DistrictService;
import com.br.ott.client.select.service.DramaNavigationService;
import com.br.ott.client.select.service.DramaPositionService;
import com.br.ott.client.select.service.DramaProgramService;
import com.br.ott.common.util.spring.SpringUtils;
public class CacheListener implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(CacheListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.warn("服务器关闭..... ");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.warn("服务器启动..... 开始查找缓存数据.");
		loadDictCache(event.getServletContext());

		loadDistrictCache(event.getServletContext());

		reloadNavigationCache(event.getServletContext());
		
		reloadPoistionCache(event.getServletContext());
		
		reloadRecDramaCache(event.getServletContext());
		
	}
	
	/**
	 * 加载推荐资产信息    创建人：lizhenghg  创建时间：2016-10-12
	 * 
	 * @exception throws
	 */
	private void reloadRecDramaCache(ServletContext sc) {
		try {
			DramaProgramService dramaProgramService = SpringUtils.getBean(sc,
					"dramaProgramService", DramaProgramService.class);
			dramaProgramService.reloadDramaProgram();
		} catch (Exception e) {
			logger.error("加载推荐资产异常.........");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 加载点播推荐位
	 * 
	 * 创建人：pananz 创建时间：2016-9-12
	 * 
	 * @param @param sc 返回类型：void
	 * @exception throws
	 */
	private void reloadPoistionCache(ServletContext sc) {
		try {
			DramaPositionService dramaPositionService = SpringUtils.getBean(sc,
					"dramaPositionService", DramaPositionService.class);
			dramaPositionService.reloadPosition();
		} catch (Exception e) {
			logger.error("加载点播推荐位数据异常.........");
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载资产栏目
	 * 
	 * 创建人：pananz 创建时间：2016-9-12
	 * 
	 * @param @param sc 返回类型：void
	 * @exception throws
	 */
	private void reloadNavigationCache(ServletContext sc) {
		try {
			DramaNavigationService dramaNavigationService = SpringUtils.getBean(sc,
					"dramaNavigationService", DramaNavigationService.class);
			dramaNavigationService.reloadNavigation();
		} catch (Exception e) {
			logger.error("加载资产栏目数据异常.........");
			e.printStackTrace();
		}
	}

	/**
	 * 加载区域信息 创建人：陈登民 创建时间：2013-1-18 - 下午2:56:28 返回类型：void
	 * 
	 * @exception throws
	 */
	private void loadDistrictCache(ServletContext sc) {
		try {
			DistrictService districtService = SpringUtils.getBean(sc,
					"districtService", DistrictService.class);
			DistrictCache.reload(districtService.getAllDistrict(2));
		} catch (Exception e) {
			logger.error("加载区域数据异常.........");
			e.printStackTrace();
		}
	}

	/**
	 * 加载字典与节点 创建人：pananz 创建时间：2012-12-27 - 下午6:00:53 返回类型：void
	 * 
	 * @exception throws
	 */
	private void loadDictCache(ServletContext sc) {
		try {
			DictionarysMapper dictionarysMapper = SpringUtils.getBean(sc,
					"dictionarysMapper", DictionarysMapper.class);
			Dictionary cictionary = new Dictionary();
			cictionary.setStatus("1");
			List<Dictionary> dicts = dictionarysMapper
					.findAllDictionarys(cictionary);
			DictCache.reload(dicts);
			dicts = null;
		} catch (Exception e) {
			logger.error("加载字典数据异常.........");
			e.printStackTrace();
		}
	}
}
