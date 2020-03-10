package com.br.ott.client.select.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.select.entity.DramaRecommend;
import com.br.ott.client.select.mapper.DramaRecommendMapper;

/**
 * 文件名：RecommendPositionMapper.java 版权：BoRuiCubeNet. Copyright 2014-2015,All
 * rights reserved 公司名称：青岛博瑞立方科技有限公司 创建人：xiaojxiang 创建时间：2016-9-19
 */
@Service
public class DramaRecommendService {
	@Autowired
	private DramaRecommendMapper dramaRecommendMapper;

	/**
	 * 按分页查询推荐位列表
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-19
	 * 
	 * @param @param recommendPosition
	 * @param @return 返回类型：List<RecommendPosition>
	 * @exception throws
	 */
	public List<DramaRecommend> findRecommendPstByPage(
			DramaRecommend recommendPosition) {
		int totalResult = dramaRecommendMapper
				.getCountRecommendPst(recommendPosition);
		recommendPosition.setTotalResult(totalResult);
		return dramaRecommendMapper.findRecommendPstByPage(recommendPosition);
	}

	public DramaRecommend getRecommendPstById(String id) {
		return dramaRecommendMapper.getRecommendPstById(id);
	}

	public void updateRecommendPst(DramaRecommend recommendPosition) {
		dramaRecommendMapper.updateRecommendPst(recommendPosition);
	}

	public void delRecommend(String[] ids) {
		dramaRecommendMapper.delRecommend(ids);
	}

	public void addRecommendPst(DramaRecommend dramaRecommend) {
		dramaRecommendMapper.addRecommendPst(dramaRecommend);
	}

	public DramaRecommend getMaxSortByPrType(String prType) {
		return dramaRecommendMapper.getMaxSortByPrType(prType);
	}

	public List<DramaRecommend> findDramaRecByDramaId(String dramaId) {
		return dramaRecommendMapper.findDramaRecByDramaId(dramaId);
	}

	public List<DramaRecommend> findDramaRecByPrType(String prType) {
		return dramaRecommendMapper.findDramaRecByPrType(prType);
	}
	
	/**
	 * 更新资产推荐位关联表信息
	 * 
	 * 创建人：pananz 创建时间：2016-9-29
	 * 
	 * @param @param dr
	 * @param @param oldType 返回类型：void
	 * @exception throws
	 */
	public void addDramaRecList(DramaRecommend dr, String oldType) {
		if (!oldType.equals(dr.getcList())) {
			List<String> newList = Arrays.asList(dr.getcList().split(","));
			dramaRecommendMapper.delDramaRecByDramaId(dr.getDramaId());
			DramaRecommend dramaRecommend = new DramaRecommend();
			if (CollectionUtils.isNotEmpty(newList)) {
				for (String prType : newList) {
					dr.setPrType(prType);
					dramaRecommend.setPrType(prType);
					List<DramaRecommend> list = dramaRecommendMapper
							.findDramaRecommendBySortUp(dramaRecommend);
					if (CollectionUtils.isNotEmpty(list)) {
						DramaRecommend rec = list.get(0);
						if (rec != null) {
							dr.setSort(rec.getSort() + 1);
						}
					}
					dramaRecommendMapper.addRecommendPst(dr);
				}
			}
		}
	}

	/**
	 * 获取比当前排序大的最小值
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param dramaRecommend
	 * @param @return 返回类型：List<DramaRecommend>
	 * @exception throws
	 */
	public List<DramaRecommend> findDramaRecommendBySortUp(
			DramaRecommend dramaRecommend) {
		return dramaRecommendMapper.findDramaRecommendBySortUp(dramaRecommend);
	}

	/**
	 * 更新排序值
	 * 
	 * 创建人：xiaojxiang 创建时间：2016-9-29
	 * 
	 * @param @param dramaRecommend 返回类型：void
	 * @exception throws
	 */
	public void updateDramaRecommendBySort(DramaRecommend dramaRecommend) {
		dramaRecommendMapper.updateDramaRecommendBySort(dramaRecommend);
	}

	public List<DramaRecommend> findDramaRecommendBySortDown(
			DramaRecommend dramaRecommend) {
		return dramaRecommendMapper
				.findDramaRecommendBySortDown(dramaRecommend);
	}

	/**
	 * 删除推荐资产
		 * 
		 * 创建人：xiaojxiang 创建时间：2016-10-18
		 * 
		 * @param @param id
		 * 返回类型：void
		 * @exception throws
	 */
	public void delRecommendPstById(String id) {
		dramaRecommendMapper.delRecommendPstById(id);
	}
}
