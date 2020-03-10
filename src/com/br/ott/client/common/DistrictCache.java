package com.br.ott.client.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.br.ott.client.common.entity.District;

/* 
 *  地区缓存
 *  文件名：CityCache.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  创建人：陈登民
 *  创建时间：2013-1-17 - 下午3:59:03
 */
public class DistrictCache {
	private static final int level1 = 1;
	private static final int level2 = 2;
	private static final int level3 = 3;

	private static final Logger LOGGER = Logger.getLogger(DistrictCache.class);
	private static List<District> areaPro = new ArrayList<District>();// 省份
	private static List<District> areaCity = new ArrayList<District>();// 地市
	private static List<District> areaDist = new ArrayList<District>();// 区域

	public static void reload(List<District> districts) {
		for (District district : districts) {
			if (level1 == district.getLevel()) {
				areaPro.add(district);
			} else if (level2 == district.getLevel()) {
				areaCity.add(district);
			} else if (level3 == district.getLevel()) {
				areaDist.add(district);
			}
		}
		LOGGER.debug("level1:" + areaPro.size() + " level2" + areaCity.size());
		districts = null;
	}

	/**
	 * 获取所有省份 创建人：陈登民 创建时间：2013-1-21 - 下午2:04:27
	 * 
	 * @return 返回类型：List<District>
	 * @exception throws
	 */
	public static List<District> getProvinces() {
		return areaPro;
	}

	/**
	 * 获取指定省份的所有城市 创建人：陈登民 创建时间：2013-1-21 - 下午2:04:34
	 * 
	 * @param proId
	 * @return 返回类型：List<District>
	 * @exception throws
	 */
	public static List<District> getCitys(int proId) {
		List<District> nodes = areaCity;
		List<District> citys = new ArrayList<District>();
		for (District dis : nodes) {
			if (proId == dis.getUpid()) {
				citys.add(dis);
			}
		}
		return citys;
	}

	public static List<String> getCityCodes(int proId) {
		List<District> nodes = areaCity;
		List<String> citys = new ArrayList<String>();
		for (District dis : nodes) {
			if (proId == dis.getUpid()) {
				citys.add(dis.getAreacode());
			}
		}
		return citys;
	}

	/**
	 * 获取指定城市的所有区域 创建人：陈登民 创建时间：2013-1-21 - 下午2:04:47
	 * 
	 * @param cityId
	 * @return 返回类型：List<District>
	 * @exception throws
	 */
	public static List<District> getDistricts(int cityId) {
		List<District> nodes = areaDist;
		List<District> citys = new ArrayList<District>();
		for (District dis : nodes) {
			if (cityId == dis.getUpid()) {
				citys.add(dis);
			}
		}
		return citys;
	}

	/**
	 * 通过区号查找城市 创建人：陈登民 创建时间：2013-1-21 - 下午2:05:02
	 * 
	 * @param areaCode
	 * @return 返回类型：District
	 * @exception throws
	 */
	public static District getCityByAreaCode(String areaCode) {
		List<District> nodes = areaCity;
		for (District dis : nodes) {
			if (areaCode.equals(dis.getAreacode())) {
				return dis;
			}
		}
		return null;
	}

	public static Map<String, String> getProvincesStr() {
		List<District> districts = getProvinces();
		Map<String, String> map = new HashMap<String, String>();
		if (null == districts)
			return map;
		for (District district : districts) {
			map.put(String.valueOf(district.getId()), district.getName());
		}
		return map;
	}

}
