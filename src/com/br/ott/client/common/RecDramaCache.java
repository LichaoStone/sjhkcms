package com.br.ott.client.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.br.ott.client.select.entity.DramaProgram;
import com.br.ott.common.util.BaseComparator;

/**
 * 文件名：RecDramaCache.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：lizhenghg 创建时间：2016-10-12
 */

@SuppressWarnings("rawtypes")
public class RecDramaCache {

	private static final Logger logger = Logger
			.getLogger(RecDramaCache.class);
	/**
	 * dramaList表示存在于所有推荐位的资产集合
	 */
	public static List<DramaProgram> dramaList = new ArrayList<DramaProgram>();
	
	/**
	 * dramaIdsAndSortsList主要存储三个字段，被推荐的资产id：dramaId、存在哪些推荐位中：prTypes、在该推荐位中的排位：
	 * sorts 举例：dramaId、prType、sorts 7460 2 11 || 10429 4,5 5,1 || 10891 4,2,5 10,2,5
	 */
	public static List<Map<String, Object>> dramaIdsAndSortsList = new ArrayList<Map<String, Object>>();
	
	//获取特定栏目下面资产的集合
	public static Map<String, List<DramaProgram>> dpMap = new HashMap<String, List<DramaProgram>>();
	/**
	 * 
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-12
	 * 
	 * @param @param list 返回类型：void
	 * @exception throws
	 */
	public static void reloadDramaProgram(List<DramaProgram> list) {
		logger.debug("内容的dramaList更新");
		dramaList = list;
	}

	public static List<DramaProgram> getDramaProgramsById(String dramaId) {
		List<DramaProgram> dpList = new ArrayList<DramaProgram>();
		if (dpMap.containsKey(dramaId)) {
			dpList.addAll(dpMap.get(dramaId));
		}
		return dpList;
	}
	
	/**
	 * 
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-12
	 * 
	 * @param @param list 返回类型：void
	 * @exception throws
	 */
	public static void reloadDramaIdsAndSorts(List<Map<String, Object>> list){
		logger.debug("内容的dramaIdsAndSortsList更新");
		dramaIdsAndSortsList = list;
	}
	
	/**
	 * 
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-12
	 * 
	 * @param @param id
	 * @param @return 返回类型：DramaProgram
	 * @exception throws
	 */
	public static DramaProgram getDramaProgramById(String id) {
		for (DramaProgram nav : dramaList) {
			if (id.equals(nav.getId())) {
				return nav;
			}
		}
		return null;
	}
	
	/**
	 * 加载指定推荐栏目的资产
	 * 创建人：lizhenghg 创建时间：2016-10-12
	 * @param map
	 */
	public static void reloadRecDramaPrograms(Map<String, List<DramaProgram>> map){
		logger.debug("内容的dpMap更新");
		dpMap = map;
	}
	
	/**
	 * 根据推荐位id按照sort降序排列推荐资产
	 * @author lizhenghg  2017-08-16
	 * @param prType
	 * @return List<DramaProgram>
	 */
	
	@SuppressWarnings("unchecked")
	public static List<DramaProgram> findDramaProgramByCode(String prType, String limit){
		List<Map<String, Object>> linfo = new ArrayList<Map<String, Object>>();
		List<DramaProgram> dramaProgramList = new ArrayList<DramaProgram>();
		if (CollectionUtils.isNotEmpty(dramaIdsAndSortsList)) {
			Iterator<Map<String, Object>> it = dramaIdsAndSortsList.iterator();
			while (it.hasNext()) {
				Map<String, Object> dsMap = it.next();
				String[] prTypes = ((String) dsMap.get("prTypes")).split(",");
				String[] sorts = ((String) dsMap.get("sorts")).split(",");
				if (prTypes != null && prTypes.length > 0) {
					HashMap<String, Object> paramsMap = null;
					for (int i = 0; i < prTypes.length; i++) {
						if (prTypes[i].equals(prType)) {
							paramsMap = new HashMap<String, Object>();
							paramsMap.put("dramaId", dsMap.get("dramaId").toString());
							paramsMap.put("prType", prTypes[i]);
							paramsMap.put("sort", sorts[i]);
							linfo.add(paramsMap);
						}
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(dramaList) && CollectionUtils.isNotEmpty(linfo)) {
			Iterator<DramaProgram> it = dramaList.iterator();
			DramaProgram dp = null;
			while (it.hasNext()) {
				dp = it.next();
				for (int i = 0,n = linfo.size(); i < n; i++) {
					if (dp.getId().equals(linfo.get(i).get("dramaId").toString())) {
						dp.setSort(linfo.get(i).get("sort").toString());
						dramaProgramList.add(dp);
					}
				}		
			}
			if (CollectionUtils.isNotEmpty(dramaProgramList)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("sort", "desc");                                        //可升可降
				Collections.sort(dramaProgramList, new BaseComparator(map));    //降序    排序，根据dramaProgramList中每个object的sort字段，降序排序object
				int limitCounter = Integer.valueOf(limit);
				if (dramaProgramList.size() > limitCounter) {
					dramaProgramList = dramaProgramList.subList(0, limitCounter);
				}
				map = null;
			}
			linfo = null;
		}
		return dramaProgramList;
	}
}