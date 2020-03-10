package com.br.ott.client.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.user.entity.UserOrder;

/* 
 *  
 *  文件名：UserOrderMapper.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-13 - 下午4:29:41
 */
public interface UserOrderMapper {

	/**
	 * 增加点播订单信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午4:44:09
	 * 
	 * @param userOrder
	 *            返回类型：void
	 * @exception throws
	 */
	void addUserOrder(UserOrder userOrder);

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
	void updateStatusById(@Param("status") String status, @Param("id") String id);

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
	void updateStatusByAccountAndPid(@Param("status") String status,
			@Param("account") String account,
			@Param("programId") String programId);

	/**
	 * 按分页查询点播订单信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午4:46:29
	 * 
	 * @param userOrder
	 * @return 返回类型：List<UserOrder>
	 * @exception throws
	 */
	List<UserOrder> findUserOrderByPage(UserOrder userOrder);
	int getCountUserOrder(UserOrder userOrder);
	
	/**
	 * 按条件查询点播订单信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午4:46:47
	 * 
	 * @param userOrder
	 * @return 返回类型：List<UserOrder>
	 * @exception throws
	 */
	List<UserOrder> findUserOrderByCond(UserOrder userOrder);

	/**
	 * 按ID查询点播订单信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午4:47:27
	 * 
	 * @param id
	 * @return 返回类型：UserOrder
	 * @exception throws
	 */
	UserOrder getUserOrderById(String id);

	/**
	 * 按流水单号查询点播订单信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午4:47:44
	 * 
	 * @param orderId
	 * @return 返回类型：UserOrder
	 * @exception throws
	 */
	UserOrder getUserOrderByOrderId(String orderId);

}
