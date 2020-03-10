package com.br.ott.client.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.ott.base.BaseController;
import com.br.ott.client.user.entity.UserOrder;
import com.br.ott.client.user.service.UserOrderService;

/* 
 *  
 *  文件名：UserOrderController.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-13 - 下午4:51:37
 */
@Controller
@RequestMapping(value = "/order")
public class UserOrderController extends BaseController {

	@Autowired
	private UserOrderService userOrderService;

	/**
	 * 按分页查询点播订单
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午5:17:06
	 * 
	 * @param request
	 * @param model
	 * @param userOrder
	 * @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "findUserOrder")
	public String findUserOrder(HttpServletRequest request, Model model,
			UserOrder userOrder) {
		// 将所请求参数绑定Model上
		userOrder.setCurrentPage(getPageNo(request));
		userOrder.setShowCount(getPageRow(request));
		List<UserOrder> list = userOrderService.findUserOrderByPage(userOrder);
		model.addAttribute("list", list);
		model.addAttribute("userOrder", userOrder);
		return "user/listUserOrder";
	}

	/**
	 * 点播订购详情
	 * 
	 * 创建人：pananz 创建时间：2015-3-13 - 下午5:17:38
	 * 
	 * @param request
	 * @param model
	 * @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "toUserOrder")
	public String toUserOrder(HttpServletRequest request, Model model) {
		// 将所请求参数绑定Model上
		UserOrder userOrder = userOrderService.getUserOrderById(request
				.getParameter("id"));
		model.addAttribute("userOrder", userOrder);
		return "user/userOrderInfo";
	}
}
