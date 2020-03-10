package com.br.ott.client.select.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.select.entity.DramaType;
import com.br.ott.client.select.mapper.DramaTypeMapper;

/**
 * 文件名：DramaTypeService.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-19
 */
@Service
public class DramaTypeService {

	@Autowired
	private DramaTypeMapper dramaTypeMapper;

	public List<DramaType> findDramaTypeByDramaId(String dramaId) {
		return dramaTypeMapper.findDramaTypeByDramaId(dramaId);
	}

	public List<DramaType> findNavByDramaId(String dramaId) {
		return dramaTypeMapper.findNavByDramaId(dramaId);
	}

	public void addDramaTypeList(String dramaId, String dtList, String oldType) {
		List<String> newList = Arrays.asList(dtList.split(","));
		List<String> oldList = Arrays.asList(oldType.split(","));
		if (!oldList.containsAll(newList)) {
			dramaTypeMapper.delDramaTypeByDramaId(dramaId);
			if (CollectionUtils.isNotEmpty(newList)) {
				for (String navId : newList) {
					DramaType dt = new DramaType(dramaId, navId);
					dramaTypeMapper.addDramaType(dt);
				}
			}
		}
		newList = null;
		oldList = null;
	}

}
