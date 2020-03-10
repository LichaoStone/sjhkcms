package com.br.ott.client.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;


import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;

import com.br.ott.client.select.entity.DramaNavigation;
import com.br.ott.common.util.StringUtil;

/**
 * 文件名：NavigationCache.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-12
 */
public class NavigationCache {

	private static final Logger logger = Logger.getLogger(NavigationCache.class);
	
	public static List<DramaNavigation> navList = new ArrayList<DramaNavigation>();

	//parentId = 0时，isdl_display = 0或者n时的数据集合
	public static List<DramaNavigation> navList2 = new ArrayList<DramaNavigation>();
	
	//parentId != 0时，isdl_display = 0或者n时的数据集合
	public static List<DramaNavigation> navList3 = new ArrayList<DramaNavigation>();
	
	//categoryId的集合
	private static HashMap<String, String> cMap = new HashMap<String, String>();
	
	//获取二级栏目parentId != 0，isdl_display = 0或者n，按照sequence升序，只获取id\name\parentId\imgurl\categoryId
	private static HashMap<String, List<JSONObject>> jsonObject1 = new HashMap<String, List<JSONObject>>();
	
	//获取一级栏目parent = 0，recommend = 1，isdl_display = 0，按照sequence升序，只获取id\name\parentId\imgurl\categoryId
	private static List<JSONObject> jsonObject2 = new ArrayList<JSONObject>();
	
	static {
		cMap.put("1", "21100001000000091480920561911000");
		cMap.put("2", "21100001000000091480920889394000");
		cMap.put("3", "21100001000000091480920940818000");
		cMap.put("4", "21100001000000091480923252091000");
		cMap.put("5", "21100001000000091480923295948000");
		cMap.put("6", "21100001000000091480923319258000");
		cMap.put("7", "2110MAMS000000091489737112125000");
		cMap.put("8", "2110MAMS000000091489737072561000");
		cMap.put("9", "21100001000000091480923476115000");
		cMap.put("10", "21100001000000091480924075298000");
		cMap.put("11", "21100001000000091480924121702000");
		cMap.put("178", "21100001000000091480924146575000");
	}
	
	/**
	 * 
	 * 
	 * 创建人：pananz 创建时间：2016-9-12
	 * 
	 * @param @param list 返回类型：void
	 * @exception throws
	 */
	@SuppressWarnings("unchecked")
	public static void relocadNavigation(List<DramaNavigation> list) {
		logger.debug("内容的navList更新");
		navList = list;
		if (CollectionUtils.isNotEmpty(navList)) {
			Iterator<DramaNavigation> it = navList.iterator();
			navList2 = new ArrayList<DramaNavigation>();
			navList3 = new ArrayList<DramaNavigation>();
			DramaNavigation dramaNavigation = null;
			while (it.hasNext()) {
				dramaNavigation = it.next();
				if ("1".equals(dramaNavigation.getRecommend()) && 
						("0".equals(dramaNavigation.getIsdl_display()) || "n".equals(dramaNavigation.getIsdl_display()))) {
					if ("0".equals(dramaNavigation.getParentId())) {
						navList2.add(dramaNavigation);
					} else {
						navList3.add(dramaNavigation);
					}
				}
			}
			if (CollectionUtils.isNotEmpty(navList2)) {
				jsonObject2 = new ArrayList<JSONObject>();
				BeanComparator bc = new BeanComparator("sequence"); // 排序，根据list中每个object的sequence字段，升序排序object
				Collections.sort(navList2, bc); // 升序
				for (DramaNavigation dn : navList2) {
					dn.setCategoryId(cMap.get(dn.getId()));
					JSONObject json = new JSONObject();
					json.put("id", dn.getId());
					json.put("name", dn.getName());
					json.put("imgurl", dn.getRealImgurl());
					json.put("parentId", dn.getParentId());
					json.put("categoryId", dn.getCategoryId());
					jsonObject2.add(json);
				}
			}
			if (CollectionUtils.isNotEmpty(navList3)) {
				jsonObject1 = new HashMap<String, List<JSONObject>>();
				BeanComparator bc = new BeanComparator("sequence"); // 排序，根据list中每个object的sequence字段，升序排序object
				Collections.sort(navList3, bc); // 升序
				for (Entry<String, String> entry : cMap.entrySet()) {
					List<JSONObject> jsonList = new ArrayList<JSONObject>();
					for (DramaNavigation dn : navList3) {
						if (entry.getKey().equals(dn.getParentId())) {
							dn.setCategoryId(entry.getValue());
							JSONObject json = new JSONObject();
							json.put("id", dn.getId());
							json.put("name", dn.getName());
							json.put("parentId", dn.getParentId());
							json.put("imgurl", dn.getRealImgurl());
							json.put("categoryId", dn.getCategoryId());
							jsonList.add(json);
						}
					}
					jsonObject1.put(entry.getKey(), jsonList);
				}
			}
		}
	}

	/**
	 * 
	 * 
	 * 创建人：lizhenghg 创建时间：2017-8-30
	 * 
	 * @param @param parentId
	 * 该方法只处理parentId != 0 ，isdl_display = 0或者n时的情况
	 * 1 大片;2 好莱坞;3 剧场;4 综艺;5 动漫;6 演艺;7 情感;8 讲堂;9 纪录;10 自制;11 4K专区;178 栏目
	 * @param @return 返回类型：List<DramaNavigation>
	 * @exception throws
	 */
	public static List<DramaNavigation> getNavigationList(String parentId) {
		if (StringUtil.isEmpty(parentId) || CollectionUtils.isEmpty(navList3)) {
			return new ArrayList<DramaNavigation>();
		}
		List<DramaNavigation> list = new ArrayList<DramaNavigation>();
		for (DramaNavigation dict : navList3) {
			if (parentId.equals(dict.getParentId())) {
				dict.setCategoryId(cMap.get(parentId));
				list.add(dict);
			}
		}
		return list;
	}
	
	
	/**
	 * 
	 * 
	 * 创建人：lizhenghg 创建时间：2017-8-30
	 * 
	 * @param @param parentId
	 * 该方法只处理parentId != 0 ，isdl_display = 0或者n时的情况
	 * 1 大片;2 好莱坞;3 剧场;4 综艺;5 动漫;6 演艺;7 情感;8 讲堂;9 纪录;10 自制;11 4K专区;178 栏目
	 * @param @return 返回类型：List<DramaNavigation>
	 * @exception throws
	 */
	public static List<JSONObject> getNavigationJSON(String parentId) {
		if (StringUtil.isEmpty(parentId) || CollectionUtils.isEmpty(navList3)) {
			return new ArrayList<JSONObject>();
		}
		List<JSONObject> list = new ArrayList<JSONObject>();
		if (jsonObject1.size() > 0) {
			list.addAll(jsonObject1.get(parentId));
		}
		return list;
	}
	
	/**
	 * 
	 * 
	 * 创建人：lizhenghg 创建时间：2017-8-30
	 * 
	 * @param @param parentId
	 * 该方法只处理parentId = 0 ，recommend = 1，isdl_display = 0或者n时的情况
	 * 1 大片;2 好莱坞;3 剧场;4 综艺;5 动漫;6 演艺;7 情感;8 讲堂;9 纪录;10 自制;11 4K专区;178 栏目
	 * @param @return 返回类型：List<DramaNavigation>
	 * @exception throws
	 */
	public static List<JSONObject> getNavigationJSON2() {
		List<JSONObject> result = new ArrayList<JSONObject>();
		if (jsonObject2.size() > 0) {
			result.addAll(jsonObject2);
		}
		return result;
	}
	
	/**
	 * 创建人：lizhenghg 创建时间：2017-8-30
	 * 该方法只处理parentId = 0，isdl_display = 0或者n时的数据
	 * 1 大片;2 好莱坞;3 剧场;4 综艺;5 动漫;6 演艺;7 情感;8 讲堂;9 纪录;10 自制;11 4K专区;178 栏目
	 * @param @param parentId
	 * @param @return 返回类型：List<DramaNavigation>
	 * @exception throws
	 */
	
	public static List<DramaNavigation> getNavigationList1(String parentId) {
		if (StringUtil.isEmpty(parentId) || !"0".equals(parentId)) {
			return new ArrayList<DramaNavigation>();
		}
		List<DramaNavigation> list = new ArrayList<DramaNavigation>();
		list.addAll(navList2);
		return list;
	}

	/**
	 * 
	 * 
	 * 创建人：pananz 创建时间：2016-9-12
	 * 
	 * @param @param id
	 * @param @return 返回类型：DramaNavigation
	 * @exception throws
	 */
	public static DramaNavigation getNavigationById(String id) {
		for (DramaNavigation nav : navList) {
			if (id.equals(nav.getId())) {
				return nav;
			}
		}
		return null;
	}

	public static String getNameById(String id) {
		String name = "";
		for (DramaNavigation nav : navList) {
			if (id.equals(nav.getId())) {
				name = nav.getName();
				break;
			}
		}
		return name;
	}

	public static String getParentNameById(String id) {
		String name = "";
		for (DramaNavigation nav : navList) {
			if (id.equals(nav.getId())) {
				name = nav.getParentName();
				break;
			}
		}
		return name;
	}
	
	/**
	 * 通过DramaNavigation的code获取其id
	 * 创建人：lizhenghg 创建时间：2016-10-12
	 * @param code
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	public static String getIdByCode(String code){
		if(StringUtils.isEmpty(code))
			return null;
		String id = null;
		DramaNavigation dramaNavigation = null;
		Iterator<DramaNavigation> it = navList.iterator();
		while(it.hasNext()){
			dramaNavigation = it.next();
			if(code.equals(dramaNavigation.getCode())){
				id = dramaNavigation.getId();
				break;
			}
		}
		return id;
	}
	
	/**
	 * 通过DramaNavigation的name获取其id(局限于获取一级菜单)
	 * 创建人：lizhenghg 创建时间：2016-11-20
	 * @param name
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	public static String getIdByName(String name){
		if(StringUtils.isEmpty(name))
			return null;
		String id = null;
		DramaNavigation dramaNavigation = null;
		Iterator<DramaNavigation> it = navList.iterator();
		while(it.hasNext()){
			dramaNavigation = it.next();
			if(name.equals(dramaNavigation.getName()) && "0".equals(dramaNavigation.getParentId())){
				id = dramaNavigation.getId();
				break;
			}
		}
		return id;
	}
}