package com.br.ott.client.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;

import com.br.ott.client.select.entity.DramaPosition;

/**
 * 文件名：DramaRecCache.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-29
 */
public class DramaPositionCache {
	private static final Logger logger = Logger
			.getLogger(DramaPositionCache.class);
	public static List<DramaPosition> dpList = new ArrayList<DramaPosition>();

	/**
	 * 
	 * 
	 * 创建人：pananz 创建时间：2016-9-12
	 * 
	 * @param @param list 返回类型：void
	 * @exception throws
	 */
	public static void relocadPosition(List<DramaPosition> list) {
		logger.debug("内容的dpList更新");
		dpList = list;
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
	public static DramaPosition getDramaPositionById(String id) {
		for (DramaPosition nav : dpList) {
			if (id.equals(nav.getId())) {
				return nav;
			}
		}
		return null;
	}

	/**
	 * 取得推荐位名称
	 * 
	 * 创建人：pananz 创建时间：2016-9-29
	 * 
	 * @param @param id
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	public static String getNameById(String id) {
		String name = "";
		for (DramaPosition nav : dpList) {
			if (id.equals(nav.getId())) {
				name = nav.getName();
				break;
			}
		}
		return name;
	}
	
	/**
	 * 
	 * 
	 * 创建人：lizhenghg 创建时间：2016-9-12
	 * 
	 * @param code
	 * @return 返回类型：String
	 * @exception throws
	 */
	public static String getDPIDByCode(String code) {
		if(StringUtils.isEmpty(code))
			return null;
		Iterator<DramaPosition> itr = dpList.iterator();
		DramaPosition dramaPosition = null;
		String id = null;
		while(itr.hasNext()){
			dramaPosition = itr.next();
			if(code.equals(dramaPosition.getCode())){
				id = dramaPosition.getId();
				break;
			}
		}
		return id;
	}


}
