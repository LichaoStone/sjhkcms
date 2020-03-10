package com.br.ott.client.select.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.select.entity.Tariff;

/* 
 *  
 *  文件名：TariffMapper.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-11 - 上午11:12:43
 */
public interface TariffMapper {

	/**
	 * 增加资费信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:15:10
	 * 
	 * @param tariff
	 *            返回类型：void
	 * @exception throws
	 */
	void addTariff(Tariff tariff);

	/**
	 * 修改资费信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:15:19
	 * 
	 * @param tariff
	 *            返回类型：void
	 * @exception throws
	 */
	void updateTariff(Tariff tariff);

	/**
	 * 删除资费信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:15:26
	 * 
	 * @param id
	 * @return 返回类型：Tariff
	 * @exception throws
	 */
	Tariff getTariffById(String id);

	/**
	 * 按分页查询资费信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:15:36
	 * 
	 * @param tariff
	 * @return 返回类型：List<Tariff>
	 * @exception throws
	 */
	List<Tariff> findTariffByPage(Tariff tariff);
	int getCountTariff(Tariff tariff);
	
	/**
	 * 按条件查询资费信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:15:46
	 * 
	 * @param tariff
	 * @return 返回类型：List<Tariff>
	 * @exception throws
	 */
	List<Tariff> findTariffByCond(Tariff tariff);

	/**
	 * 修改资费状态
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:15:54
	 * 
	 * @param status
	 * @param id
	 *            返回类型：void
	 * @exception throws
	 */
	void updateTariffStatus(@Param("status") String status,
			@Param("id") String id);
}
