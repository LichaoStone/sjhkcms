package com.br.ott.client.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.br.ott.client.common.entity.Dictionary;
import com.br.ott.common.util.Constants;
import com.br.ott.common.util.StringUtil;

/**
 * 字典缓存类
 * 
 * @author 陈登民
 * 
 */
public class DictCache {
	private static final Logger logger = Logger.getLogger(DictCache.class);
	private static Map<String, Map<String, Dictionary>> dictMap = null;
	private static Map<String, Dictionary> dictTop = null;
	public static List<Dictionary> dictList = new ArrayList<Dictionary>();

	/**
	 * 重新加载ehcache字典内容 创建人：pananz 创建时间：2012-12-11 - 下午5:17:50
	 * 
	 * @param dicts
	 *            返回类型：void
	 * @exception throws
	 */
	public static void reload(List<Dictionary> list) {
		logger.info("sysCache内容的dicts更新");
		dictList = list;
	}

	/**
	 * 获取字典值
	 * 
	 * @param dictType
	 *            字典类型，取Constants.DictType的常量值
	 * @param id
	 *            字典的ID
	 * @return
	 */
	public static Dictionary getDictValue(String dictType, String dicValue) {
		if (StringUtil.isEmpty(dictType))
			return null;
		List<Dictionary> dicts = dictList;
		if (CollectionUtils.isEmpty(dicts))
			return null;
		Map<String, Dictionary> map = new LinkedHashMap<String, Dictionary>();
		for (Dictionary dict : dicts) {
			if (dictType.equals(dict.getDicType())) {
				map.put(dict.getDicValue(), dict);
			}
		}
		return map.get(dicValue);
	}

	/**
	 * 获取字典值列表
	 * 
	 * @param dictType
	 *            字典类型，取Constants.DictType的常量值
	 * @param id
	 *            字典的ID
	 * @return
	 */
	public static Map<String, Dictionary> getDictValueList(String dictType) {
		if (StringUtil.isEmpty(dictType))
			return null;
		List<Dictionary> dicts = dictList;
		if (CollectionUtils.isEmpty(dicts))
			return null;
		Map<String, Dictionary> map = new LinkedHashMap<String, Dictionary>();
		for (Dictionary dict : dicts) {
			if (dictType.equals(dict.getDicType())) {
				map.put(dict.getDicValue(), dict);
			}
		}
		return map;
	}

	public static List<Dictionary> getDictList(String dictType) {
		if (StringUtil.isEmpty(dictType))
			return null;
		List<Dictionary> dicts = dictList;
		List<Dictionary> list = new ArrayList<Dictionary>();
		for (Dictionary dict : dicts) {
			if (dictType.equals(dict.getDicType())) {
				list.add(dict);
			}
		}
		return list;
	}

	/**
	 * 保存字典信息
	 * 
	 * @param dicts
	 */
	public static void saveDicts(List<Dictionary> dicts) {
		if (dicts == null) {
			return;
		}
		dictMap = new HashMap<String, Map<String, Dictionary>>();
		Map<String, Dictionary> mapDict = null;
		for (Dictionary dict : dicts) {
			if (Constants.DicType.TOP_TYPE.equals(dict.getDicType())) {
				if (dictTop == null) {
					dictTop = new LinkedHashMap<String, Dictionary>();
				}
				dictTop.put(dict.getDicValue(), dict);
				continue;
			}
			if (dict.getDicValue() == null) {
				continue;
			}
			mapDict = dictMap.get(dict.getDicType());// 先获取某一类型的
			if (mapDict == null) {
				mapDict = new HashMap<String, Dictionary>();
			}
			mapDict.put(dict.getDicValue(), dict);
			dictMap.put(dict.getDicType(), mapDict);
		}
	}

}
