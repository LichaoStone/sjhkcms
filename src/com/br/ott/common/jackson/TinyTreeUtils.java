/*
 * @# PartnerController.java Aug 1, 2012 2:12:47 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.common.jackson;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * jquery tinytree控件前/后台交互的数据转化工具类.
 * 
 * 主要用于转换列表(List)数据变成前后需要JSON格式的数据.
 * 
 * @author huang weijian
 */
public final class TinyTreeUtils {

	private TinyTreeUtils() {
		// empty!
	}
	
	/**
	 * 一个根节点的树结构.
	 * 
	 * @param node
	 *            根节点.
	 * @return json格式的字符串.
	 */
	public static String toJson(final TinyTreeBean node) {
		List<TinyTreeBean> nodes = new ArrayList<TinyTreeBean>();
		nodes.add(node);
		return toJson(nodes);
	}

	/**
	 * 多个根节点的树结构.
	 * 
	 * @param nodes
	 *            根节点.
	 * @return json格式的字符串.
	 */
	public static String toJson(final List<TinyTreeBean> nodes) {
		if (CollectionUtils.isEmpty(nodes)) {
			return StringUtils.EMPTY;
		}
		int len = nodes.size();
		String[] nodeJsons = new String[len];
		for (int index = 0; index < len; index++) {
			nodeJsons[index] = generateNodeJson(nodes.get(index));
		}
		StringBuffer json = new StringBuffer();
		json.append("[").append(StringUtils.join(nodeJsons, ",")).append("]");
		return json.toString();
	}

	private static String generateNodeJson(TinyTreeBean node) {
		StringBuffer json = new StringBuffer();
		json.append("{");
		json.append("\"id\":\"").append(escapeValue(node.getId())).append("\"");
		json.append(",\"name\":\"").append(escapeValue(node.getName())).append("\"");
		if (StringUtils.isNotBlank(node.getUrl())) {
			json.append(",\"url\":\"").append(escapeValue(node.getUrl())).append("\"");
		}
		json.append(node.isLeaf() ? ",\"leaf\":true" : "");
		json.append(node.isChecked() ? ",\"checked\":true" : "");
		json.append(node.isDisabled() ? ",\"disabled\":true" : "");
		json.append(node.isHide() ? ",\"hide\":true" : "");
		if (CollectionUtils.isNotEmpty(node.getChildren())) {
			json.append(",\"children\":").append(toJson(node.getChildren()));
		}
		json.append("}");
		return json.toString();
	}

	private static String escapeValue(String value) {
		return StringUtils.isBlank(value) ? "" : value.replaceAll("\"", "\\\\\"");
	}
}
