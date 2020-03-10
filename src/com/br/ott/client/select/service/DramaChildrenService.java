package com.br.ott.client.select.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.select.entity.DramaChildren;
import com.br.ott.client.select.mapper.DramaChildrenMapper;

/**
 * 文件名：DramaChildrenService.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-20
 */
@Service
public class DramaChildrenService {

	@Autowired
	private DramaChildrenMapper dramaChildrenMapper;

	/**
	 * 增加子资产信息
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param dramaChildren 返回类型：void
	 * @exception throws
	 */
	public void addDramaChildren(DramaChildren dramaChildren) {
		dramaChildrenMapper.addDramaChildren(dramaChildren);
	}

	/**
	 * 按ID查询子资产信息
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param id
	 * @param @return 返回类型：DramaChildren
	 * @exception throws
	 */
	public DramaChildren getDramaChildrenById(String id) {
		return dramaChildrenMapper.getDramaChildrenById(id);
	}

	/**
	 * 按条件查询子资产信息
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param dramaChildren
	 * @param @return 返回类型：List<DramaChildren>
	 * @exception throws
	 */
	public List<DramaChildren> findDramaChildrenByCond(
			DramaChildren dramaChildren) {
		return dramaChildrenMapper.findDramaChildrenByCond(dramaChildren);
	}

	/**
	 * 按条件分页查询子资产信息
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param dramaChildren
	 * @param @return 返回类型：List<DramaChildren>
	 * @exception throws
	 */
	public List<DramaChildren> findDramaChildrenByPage(
			DramaChildren dramaChildren) {
		int total = dramaChildrenMapper.getCountDramaChildren(dramaChildren);
		dramaChildren.setTotalResult(total);
		return dramaChildrenMapper.findDramaChildrenByPage(dramaChildren);
	}

	/**
	 * 更新子资产信息
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param dramaChildren 返回类型：void
	 * @exception throws
	 */
	public void updateDramaChildren(DramaChildren dramaChildren) {
		dramaChildrenMapper.updateDramaChildren(dramaChildren);
	}

	/**
	 * 信息子资产状态
	 * 
	 * 创建人：pananz 创建时间：2016-9-20
	 * 
	 * @param @param status
	 * @param @param id 返回类型：void
	 * @exception throws
	 */
	public void updateChildrenStatus(String status, String id) {
		dramaChildrenMapper.updateChildrenStatus(status, id);
	}
	
	public void updateChildrenStatus(String status, String[] ids) {
		for(String id:ids){
			dramaChildrenMapper.updateChildrenStatus(status, id);
		}
	}
}
