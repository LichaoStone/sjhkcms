package com.br.ott.common.util.ip.iprange;

/* 
 *  
 *  文件名：IpCityRow.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：陈登民
 *  创建时间：2012-12-17 - 下午5:09:19
 */
public class IpCityRow extends RangeRow<Long, String> {
	private static final long serialVersionUID = 8411106544005822554L;

	private String city;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
