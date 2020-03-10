/*
 * @# ProductRecommendService.java Aug 13, 2012 11:14:55 AM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.common.net;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 字符的解码工具类.
 * 
 * @author huang weijian
 */
public final class Decoder {

	private static Logger log = Logger.getLogger(Decoder.class);

	private Decoder() {
		// empty!
	}

	/**
	 * 进行字符串的URL编码转换，使用UTF-8编码.
	 * 
	 * @param str
	 *            要进行编码转换的字符串
	 * @return 转换后的字符串, 若转化异常则返回原字符串
	 */
	public static String decode(String str) {
		return decode(str, "utf-8");
	}

	/**
	 * 进行字符串的URL编码转换.
	 * 
	 * @param str
	 *            要进行编码转换的字符串
	 * @param encoding
	 *            转换的编码
	 * @return 转换后的字符串, 若转化异常则返回原字符串
	 */
	public static String decode(String str, String encoding) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		try {
			return URLDecoder.decode(str, encoding).trim();
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	/**
	 * 进行字符串的URL编码转换，使用UTF-8编码.
	 * 
	 * @param strs
	 *            要进行编码转换的字符串数组
	 * @return 转换后的对象
	 */
	public static String[] decode(String[] strs) {
		if (ArrayUtils.isEmpty(strs)) {
			return strs;
		}
		for (int i = 0, len = strs.length; i < len; i++) {
			strs[i] = decode(strs[i]);
		}
		return strs;
	}

	/**
	 * 进行字符串的URL编码转换，使用UTF-8编码.
	 * 
	 * @param collection
	 *            要进行编码转换的字符串集合
	 * @return 转换后的对象
	 */
	public static List<String> decode(List<String> list) {
		if (CollectionUtils.isEmpty(list)) {
			return list;
		}
		for (int i = 0, len = list.size(); i < len; i++) {
			list.set(i, decode(list.get(i)));
		}
		return list;
	}

	/**
	 * 进行字符串的URL编码转换，使用UTF-8编码.
	 * 
	 * @param obj
	 *            要对其中的字符型属性进行编码转换的对象
	 * @return 转换后的对象
	 */
	public static Object decode(Object obj) {
		return decode(obj, false);
	}

	/**
	 * 进行字符串的URL编码转换，使用UTF-8编码. 
	 * 
	 * 注: 只支持二级的继承.
	 * 
	 * @param obj
	 *            要对其中的字符型属性进行编码转换的对象
	 * @param decodeSuperClass
	 *            是否转换父类的编码?
	 * @return 转换后的对象
	 */
	public static Object decode(Object obj, boolean decodeSuperClass) {
		if (null == obj) {
			return obj;
		}
		if (!decodeSuperClass) {
			return decode(obj, obj.getClass());
		}
		return decode(obj, obj.getClass().getSuperclass());
	}

	private static Object decode(Object obj, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		if (ArrayUtils.isEmpty(fields)) {
			return obj;
		}
		for (Field field : fields) {
			if (Modifier.isFinal(field.getModifiers())) {
				continue;
			}
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			try {
				Object value = field.get(obj);
				if (isNotStringClass(value)) {
					continue;
				}
				field.set(obj, decode(value.toString()));
			} catch (IllegalArgumentException e) {
				log.warn("decoder转化错误", e);
			} catch (IllegalAccessException e) {
				log.warn("decoder转化错误", e);
			}
		}
		return obj;
	}

	private static boolean isNotStringClass(Object value) {
		return null == value || value.getClass() != String.class;
	}

	public static Map<Object , Object> decode(Map<Object , Object> map) {
		if (MapUtils.isEmpty(map)) {
			return map;
		}
		Set<Object> keys = map.keySet();
		if (CollectionUtils.isEmpty(keys)) {
			return map;
		}
		for (Object key : keys) {
			Object value = map.get(key);
			if (isNotStringClass(value)) {
				continue;
			}
			map.put(key, decode(value.toString()));
		}
		return map;
	}
}
