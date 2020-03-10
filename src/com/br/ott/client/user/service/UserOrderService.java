package com.br.ott.client.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.user.entity.UserOrder;
import com.br.ott.client.user.mapper.UserOrderMapper;

/* 
 *  
 *  文件名：UserOrderService.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-13 - 下午4:48:34
 */
@Service
public class UserOrderService {

	@Autowired
	private UserOrderMapper userOrderMapper;

	/**
	 * 增加点播订单信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午4:44:09
	 * 
	 * @param userOrder
	 *            返回类型：void
	 * @exception throws
	 */
	public void addUserOrder(UserOrder userOrder) {
		userOrderMapper.addUserOrder(userOrder);
	}

	/**
	 * 按ID修改点播订单状态
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午4:45:48
	 * 
	 * @param status
	 * @param id
	 *            返回类型：void
	 * @exception throws
	 */
	public void updateStatusById(String status, String id) {
		userOrderMapper.updateStatusById(status, id);
	}

	/**
	 * 按流水单修改点播订单状态
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午4:46:04
	 * 
	 * @param status
	 * @param account
	 *            返回类型：void
	 * @exception throws
	 */
	public void updateStatusByAccountAndPid(String status, String account,
			String programId) {
		userOrderMapper.updateStatusByAccountAndPid(status, account, programId);
	}

	/**
	 * 按分页查询点播订单信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午4:46:29
	 * 
	 * @param userOrder
	 * @return 返回类型：List<UserOrder>
	 * @exception throws
	 */
	public List<UserOrder> findUserOrderByPage(UserOrder userOrder) {
		int totalResult=userOrderMapper.getCountUserOrder(userOrder);
		userOrder.setTotalResult(totalResult);
		return userOrderMapper.findUserOrderByPage(userOrder);
	}

	/**
	 * 按条件查询点播订单信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午4:46:47
	 * 
	 * @param userOrder
	 * @return 返回类型：List<UserOrder>
	 * @exception throws
	 */
	public List<UserOrder> findUserOrderByCond(UserOrder userOrder) {
		return userOrderMapper.findUserOrderByCond(userOrder);
	}

	/**
	 * 按ID查询点播订单信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午4:47:27
	 * 
	 * @param id
	 * @return 返回类型：UserOrder
	 * @exception throws
	 */
	public UserOrder getUserOrderById(String id) {
		return userOrderMapper.getUserOrderById(id);
	}

	/**
	 * 按流水单号查询点播订单信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午4:47:44
	 * 
	 * @param orderId
	 * @return 返回类型：UserOrder
	 * @exception throws
	 */
	public UserOrder getUserOrderByOrderId(String orderId) {
		return userOrderMapper.getUserOrderByOrderId(orderId);
	}
}
