package com.br.ott.client.select.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.common.NavigationCache;
import com.br.ott.client.select.entity.DramaNavigation;
import com.br.ott.client.select.mapper.DramaNavigationMapper;

/* 
 *  
 *  文件名：ChannelTypeService.java
 *  创建人：pananz
 *  创建时间：2014-7-4 - 下午7:41:36
 */
@Service
public class DramaNavigationService {

	@Autowired
	private DramaNavigationMapper dramaNavigationMapper;

	/**
	 * 获取栏目页面
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param dramaNavigation
	 * @param @return 返回类型：List<DramaNavigation>
	 * @exception throws
	 */
	public List<DramaNavigation> findDramaNavigationByPage(
			DramaNavigation dramaNavigation) {
		int totalResult = dramaNavigationMapper
				.getCountDramaNavigation(dramaNavigation);
		dramaNavigation.setTotalResult(totalResult);
		return dramaNavigationMapper.findDramaNavigationByPage(dramaNavigation);
	}

	/**
	 * 添加栏目信息
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param dramaNavigation 返回类型：void
	 * @exception throws
	 */
	public void addNavigationType(DramaNavigation dramaNavigation) {
		dramaNavigationMapper.addDramaNavigation(dramaNavigation);
		reloadNavigation();
	}

	public DramaNavigation getDramaNavigationById(String id) {
		return dramaNavigationMapper.getDramaNavigationById(id);
	}

	/**
	 * 根据名字查询栏目验证
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param operators
	 * @param @param name
	 * @param @return 返回类型：boolean
	 * @exception throws
	 */
	public boolean findDramaNavigationByTypeAndName(String operators,
			String name) {
		List<DramaNavigation> list = dramaNavigationMapper
				.findDramaNavigationByTypeAndName(operators, name);
		if (CollectionUtils.isNotEmpty(list)) {
			list = null;
			return false;
		}
		return true;
	}

	/**
	 * 编辑栏目信息
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param dramaNavigation 返回类型：void
	 * @exception throws
	 */
	public void updateDramaNavigation(DramaNavigation dramaNavigation) {
		dramaNavigationMapper.updateDramaNavigation(dramaNavigation);
		reloadNavigation();
	}

	/**
	 * 栏目启用
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param string
	 * @param @param id 返回类型：void
	 * @exception throws
	 */
	public void updateDramaNavigationStatus(String status, String id) {
		dramaNavigationMapper.updateDramaNavigationStatus(status, id);
		reloadNavigation();
	}

	/**
	 * 删除
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-12
	 * 
	 * @param @param id 返回类型：void
	 * @exception throws
	 */
	public void delDramaNavigationById(String id) {
		dramaNavigationMapper.delNavigationById(id);
		reloadNavigation();
	}

	/**
	 * 按条件查询
	 * 
	 * 创建人：pananz 创建时间：2016-9-12
	 * 
	 * @param @param navigation
	 * @param @return 返回类型：List<DramaNavigation>
	 * @exception throws
	 */
	public List<DramaNavigation> findDramaNavigationByCond(
			DramaNavigation navigation) {
		return dramaNavigationMapper.findDramaNavigationByCond(navigation);
	}
	
	public void reloadNavigation(){
		DramaNavigation navigation= new DramaNavigation();
		navigation.setStatus("1");
		List<DramaNavigation> list = dramaNavigationMapper.findDramaNavigationByCond(navigation);
		NavigationCache.relocadNavigation(list);
		list = null;
	}

	/**
	 * 校验栏目名称
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-12
		 * 
		 * @param @param sequence
		 * @param @return
		 * 返回类型：boolean
		 * @exception throws
	 */
	public boolean checkNavigationByName(String name, String parentId) {
		DramaNavigation navigation = new DramaNavigation();
		navigation.setName(name);
		navigation.setParentId(parentId);
		List<DramaNavigation> list = findDramaNavigationByCond(navigation);
		if (CollectionUtils.isNotEmpty(list)) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 校验编码
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-12
		 * 
		 * @param @param name
		 * @param @return
		 * 返回类型：boolean
		 * @exception throws
	 */
	public boolean checkNavigationByCode(String code) {
		DramaNavigation navigation = new DramaNavigation();
		navigation.setCode(code);
		List<DramaNavigation> list = findDramaNavigationByCond(navigation);
		if (CollectionUtils.isNotEmpty(list)) {
			return false;
		}
		return true;
	}
	/**
	 * 查询最大排序值并加一
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-22
	 * 
	 * @param @param parentId
	 * @param @return
	 * 返回类型：DramaNavigation
	 * @exception throws
	 */
	public int getMaxSequence(String parentId) {
		return dramaNavigationMapper.getMaxSequence(parentId);
	}
	
	/**
	 * 改变推荐顺序
	 * @author lizhenghg
	 * @param dn
	 * @param oldQueue
	 * @param queue
	 */
	public void changeDNSequence(DramaNavigation dn, String oldQueue, String queue){
		int old = Integer.parseInt(oldQueue);
		int now = Integer.parseInt(queue);
		dn.setSequence(now);
		dn.setOldSequence(old);
		if (old > now) {// 从大到小,小于当前要修改的排序值queue要加1
			dramaNavigationMapper.updateDNAddSequence(dn);
		} else {// 从小到大,queue的排序值要减1
			dramaNavigationMapper.updateDNSubSequence(dn);
		}
		dramaNavigationMapper.updateDNSequence(queue, dn.getId());
	}
	
	/**
	 * 获取比当前排序值大的排序值
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-22
		 * 
		 * @param @param sequence
		 * @param @return
		 * 返回类型：List<DramaNavigation>
		 * @exception throws
	 */
	public List<DramaNavigation> findDramaNavigationBySequenceUp(DramaNavigation dramaNavigation) {
		return dramaNavigationMapper.findDramaNavigationBySequenceUp(dramaNavigation);
	}
	
	/**
	 * 根据当前排序值修改上一个排序值
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-9-22
		 * 
		 * @param @param seq
		 * @param @param sequence
		 * 返回类型：void
		 * @exception throws
	 */
	public void updateDramaNavigationBySequence(DramaNavigation dramaNavigation) {
		dramaNavigationMapper.updateDramaNavigationBySequence(dramaNavigation);
		reloadNavigation();
	}

	public List<DramaNavigation> findDramaNavigationBySequenceDown(
			DramaNavigation dramaNavigation) {
		return dramaNavigationMapper.findDramaNavigationBySequenceDown(dramaNavigation);
	}
	/**
	 * 修改推荐排序值
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-10-10
		 * 
		 * @param @param dramaNavigation
		 * @param @return
		 * 返回类型：List<DramaNavigation>
		 * @exception throws
	 */
	public List<DramaNavigation> findDramaNavigationBySortUp(
			DramaNavigation dramaNavigation) {
		return dramaNavigationMapper.findDramaNavigationBySortUp(dramaNavigation);
	}

	public void updateDramaNavigationBySort(DramaNavigation dramaNavigation) {
		dramaNavigationMapper.updateDramaNavigationBySort(dramaNavigation);
		reloadNavigation();
	}

	public List<DramaNavigation> findDramaNavigationBySortDown(
			DramaNavigation dramaNavigation) {
		return dramaNavigationMapper.findDramaNavigationBySortDown(dramaNavigation);
	}

	public void updateDramaNavigationRecommend(String recommend, String id) {
		dramaNavigationMapper.updateDramaNavigationRecommend(recommend,id);
		reloadNavigation();
	}

	/**
	 * 查询最大排序值并加1
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-10-11
		 * 
		 * @param @return
		 * 返回类型：DramaNavigation
		 * @exception throws
	 */
	public DramaNavigation findDramaNavigationMaxSort() {
		return dramaNavigationMapper.findDramaNavigationMaxSort();
	}

	/**
	 * 为DramaNavigation对象设置isdl_display值
     * 
     * 创建人：lizhenghg 创建时间：2016-10-11
	 * 
	 * @param DramaNavigation
	 * 返回类型：void
	 * @exception throws
	 */
	public void setIsdl_display(DramaNavigation dramaNavigation){
		List<DramaNavigation> navList = this.dramaNavigationMapper.findDramaNavigationById(dramaNavigation);
		if(CollectionUtils.isNotEmpty(navList)){
			dramaNavigation.setIsdl_display(navList.get(0).getIsdl_display());
		}
	}
}
