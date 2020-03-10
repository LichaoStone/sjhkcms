package com.br.ott.common.util.ip.iprange;

import java.io.Serializable;

/**
 *  IP段信息
 *  文件名：RangeRow.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：陈登民
 *  创建时间：2012-12-17 - 下午5:03:35
 */
public class RangeRow<T extends Comparable<?>, V> implements Serializable {
	private static final long serialVersionUID = -289623726849550889L;
	private T begin;
	private T end;
	private V value;
	private Object attachment;

	public T getBegin() {
		return begin;
	}

	public void setBegin(T begin) {
		this.begin = begin;
	}

	public T getEnd() {
		return end;
	}

	public void setEnd(T end) {
		this.end = end;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public Object getAttachment() {
		return attachment;
	}

	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}

}