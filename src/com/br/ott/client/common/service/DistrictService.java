package com.br.ott.client.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.common.entity.District;
import com.br.ott.client.common.mapper.DistrictMapper;

/* 
 *  
 *  文件名：DistrictService.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：陈登民
 *  创建时间：2013-1-18 - 下午2:38:40
 */
@Service
public class DistrictService {
	
	@Autowired
	private DistrictMapper districtMapper;
	
	/**
	 * 所有地区
	 * 创建人：陈登民
	 * 创建时间：2013-1-18 - 下午2:41:37
	 * @param maxLevel 最大级别
	 * @return
	 * 返回类型：List<District>
	 * @exception throws
	 */
	public List<District> getAllDistrict(int maxLevel){
		return districtMapper.getAllDistrict(maxLevel);
	}
	
	/**
	 * 父地区下面的所有子地区
	 * 创建人：陈登民
	 * 创建时间：2013-1-18 - 下午6:24:51
	 * @param upid
	 * @return
	 * 返回类型：List<District>
	 * @exception throws
	 */
	public List<District> getDistrictByUpid(int upid){
		return districtMapper.getDistrictByUpid(upid);
	}
}
