package com.br.ott.client.select.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.select.entity.Tariff;
import com.br.ott.client.select.mapper.TariffMapper;

/* 
 *  
 *  文件名：TariffService.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-11 - 上午11:25:57
 */
@Service
public class TariffService {

	@Autowired
	private TariffMapper tariffMapper;

	/**
	 * 增加资费信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:15:10
	 * 
	 * @param tariff
	 *            返回类型：void
	 * @exception throws
	 */
	public void addTariff(Tariff tariff) {
		tariffMapper.addTariff(tariff);
	}

	/**
	 * 修改资费信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:15:19
	 * 
	 * @param tariff
	 *            返回类型：void
	 * @exception throws
	 */
	public void updateTariff(Tariff tariff) {
		tariffMapper.updateTariff(tariff);
	}

	/**
	 * 删除资费信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:15:26
	 * 
	 * @param id
	 * @return 返回类型：Tariff
	 * @exception throws
	 */
	public Tariff getTariffById(String id) {
		return tariffMapper.getTariffById(id);
	}

	/**
	 * 按分页查询资费信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:15:36
	 * 
	 * @param tariff
	 * @return 返回类型：List<Tariff>
	 * @exception throws
	 */
	public List<Tariff> findTariffByPage(Tariff tariff) {
		int totalResult=tariffMapper.getCountTariff(tariff);
		tariff.setTotalResult(totalResult);
		return tariffMapper.findTariffByPage(tariff);
	}

	/**
	 * 按条件查询资费信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:15:46
	 * 
	 * @param tariff
	 * @return 返回类型：List<Tariff>
	 * @exception throws
	 */
	public List<Tariff> findTariffByCond(Tariff tariff) {
		return tariffMapper.findTariffByCond(tariff);
	}

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
	public void updateTariffStatus(String status, String id) {
		tariffMapper.updateTariffStatus(status, id);
	}

	/**
	 * 校验名称是否已使用
	 * 
	 * 创建人：pananz 创建时间：2015-3-11 - 上午11:41:31
	 * 
	 * @param name
	 * @return 返回类型：boolean
	 * @exception throws
	 */
	public boolean checkTariffName(String name) {
		Tariff tariff = new Tariff();
		tariff.setTariffName(name);
		List<Tariff> list = findTariffByCond(tariff);
		if (CollectionUtils.isNotEmpty(list)) {
			return false;
		}
		return true;
	}
}
