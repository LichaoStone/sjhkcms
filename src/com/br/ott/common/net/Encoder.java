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
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 字符的编码工具类.
 * 
 * @author huang weijian
 */
public final class Encoder {

	private static Logger log = Logger.getLogger(Encoder.class);

	private Encoder() {
		// empty!
	}

	/**
	 * 进行字符串的URL编码，使用UTF-8编码.
	 * 
	 * @param str
	 *            要进行编码的字符串
	 * @return 后的字符串, 若转化异常则返回原字符串
	 */
	public static String encode(String str) {
		return encode(str, "utf-8");
	}

	/**
	 * 进行字符串的URL编码.
	 * 
	 * @param str
	 *            要进行编码的字符串
	 * @param encoding
	 *            编码
	 * @return 转换后的字符串, 若转化异常则返回原字符串
	 */
	public static String encode(String str, String encoding) {
		if (StringUtils.isBlank(str)) {
			return str;
		}
		try {
			return URLEncoder.encode(str, encoding);
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	/**
	 * 进行字符串的URL编码，使用UTF-8编码.
	 * 
	 * @param strs
	 *            要进行编码的字符串数组
	 * @return 编码后的数组
	 */
	public static String[] encode(String[] strs) {
		if (ArrayUtils.isEmpty(strs)) {
			return strs;
		}
		for (int i = 0, len = strs.length; i < len; i++) {
			strs[i] = encode(strs[i]);
		}
		return strs;
	}

	/**
	 * 进行字符串的URL编码，使用UTF-8编码.
	 * 
	 * @param obj
	 *            要对其中的字符型属性进行编码的对象
	 * @return 编码后的对象
	 */
	public static Object encode(Object obj) {
		return encode(obj, false);
	}

	/**
	 * 进行字符串的URL编码，使用UTF-8编码.
	 * 
	 * 注: 只支持二级的继承.
	 * 
	 * @param obj
	 *            要对其中的字符型属性进行编码的对象
	 * @param encodeSuperClass
	 *            是否转换父类的编码?
	 * @return 转换后的对象
	 */
	public static Object encode(Object obj, boolean encodeSuperClass) {
		if (null == obj) {
			return obj;
		}
		if (!encodeSuperClass) {
			return encode(obj, obj.getClass());
		}
		return encode(obj, obj.getClass().getSuperclass());
	}

	private static Object encode(Object obj, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		if (ArrayUtils.isEmpty(fields)) {
			return obj;
		}
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
				field.set(obj, encode(value.toString()));
			} catch (IllegalArgumentException e) {
				log.warn("encoder转化错误", e);
			} catch (IllegalAccessException e) {
				log.warn("encoder转化错误", e);
			}
		}
		return obj;
	}

	private static boolean isNotStringClass(Object value) {
		return null == value || value.getClass() != String.class;
	}

	public static Map<Object, Object> encode(Map<Object, Object> map) {
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
			map.put(key, encode(value.toString()));
		}
		return map;
	}

	/**
	 * Html 转码.
	 */
	public static String htmlEscape(String html) {
		return StringEscapeUtils.escapeHtml(html);
	}

	/**
	 * Html 解码.
	 */
	public static String htmlUnescape(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml(htmlEscaped);
	}

	/**
	 * Xml 转码.
	 */
	public static String xmlEscape(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * Xml 解码.
	 */
	public static String xmlUnescape(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}
}
