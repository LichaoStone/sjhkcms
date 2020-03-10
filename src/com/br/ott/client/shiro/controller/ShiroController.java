/* 
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Founder. You shall not disclose such Confidential Information 
 * and shall use it only in accordance with the terms of the agreements 
 * you entered into with Founder. 
 * 
 */

package com.br.ott.client.shiro.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.br.ott.base.BaseController;
import com.br.ott.client.shiro.entity.Resources;
import com.br.ott.common.jackson.TinyTreeBean;
import com.br.ott.common.util.Constants;
import com.br.ott.common.util.StringUtil;
import com.br.ott.common.util.ip.FindclientIP;

/**
 * 登录 Controller This class is used for ...
 * 
 * @author zhuw
 * @version 1.0 2012-7-25
 */
@Controller
@RequestMapping(value = "shiro")
public class ShiroController extends BaseController {

	private static Logger log = Logger.getLogger(ShiroController.class);
	private static final String ROOT_MENU_ID = "0";

	/**
	 * 登录
	 * 
	 * @param loginName
	 * @param password
	 * @param keeptime
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "login")
	public String login(String loginName, String password, String keeptime,
			Model model, HttpServletRequest request) {
		try {
			String vdcode = request.getParameter("vdcode");
			String rand = (String) request.getSession().getAttribute("rand");
			if (StringUtil.isNotEmpty(vdcode) && StringUtil.isNotEmpty(rand)) {
				if (!vdcode.toLowerCase().equals(rand.toLowerCase())) {
					model.addAttribute("message", "验证码不正确！");
					return "shiro/login";
				}
			}
			return checkUserLogin(loginName, password, keeptime, model, request);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return "redirect:/app";
		}
	}

	/**
	 * 校验用户登录
	 * 
	 * @param loginName
	 * @param password
	 * @param keeptime
	 * @param model
	 * @param request
	 * @return
	 */
	private String checkUserLogin(String loginName, String password,
			String keeptime, Model model, HttpServletRequest request) {
		Subject currentUser = SecurityUtils.getSubject(); // 获取当前Subject,Subject对象代表当前用户
		// if (!currentUser.isAuthenticated()) { // 判断用户是否登录
		// (在此期间需要使用有效的凭据登录系统，否则值为false.)
		// 提交身份和证明
		UsernamePasswordToken token = new UsernamePasswordToken(loginName,
				password, Boolean.valueOf(keeptime));
		token.setRememberMe(true);
		try {
			// 提交身份和证明
			currentUser.login(token);
			/**
			 * // 判断用户是否拥有特定的角色 if (currentUser.hasRole("admin")) {
			 * log.info("May the Schwartz be with you!"); // 显示‘Create User’按钮
			 * // result.append(
			 * "/orgemp/** = authc,roles[11],roles[12]...roles[N]"); // //
			 * 允许多个角色访问同一个URL } else { log.info("Hello, mere mortal."); // 按钮置灰
			 * } // 判断用户是否对特定某实体有操作权限 if
			 * (currentUser.isPermitted("lightsaber:weild")) {
			 * log.info("You may use a lightsaber ring. Use it wisely."); } else
			 * { log.info(
			 * "Sorry, lightsaber rings are for schwartz masters only."); } //
			 * 判断用户是否有访问特定类型实例的权限 if
			 * (currentUser.isPermitted("winnebago:drive:eagle5")) { log.info(
			 * "You are permitted to 'drive' the 'winnebago' with license plate (id) 'eagle5'. "
			 * + "Here are the keys - have fun!"); } else { log.info(
			 * "Sorry, you aren't allowed to drive the 'eagle5' winnebago!"); }
			 * 
			 */
			return "redirect:/portal";
		} catch (UnknownAccountException uae) {
			log.info("There is no user with username of "
					+ token.getPrincipal());
			model.addAttribute("message", "用户名或密码错误！");
			return "shiro/login";
		} catch (IncorrectCredentialsException ice) {
			log.info("Password for account " + token.getPrincipal()
					+ " was incorrect!");
			model.addAttribute("message", "证书不正确！ 稍后再试");
			return "shiro/login";
		} catch (LockedAccountException lae) {
			log.info("The account for username " + token.getPrincipal()
					+ " is locked.  "
					+ "Please contact your administrator to unlock it.");
			model.addAttribute("message", "帐户锁定异常！ 稍后再试");
			return "shiro/login";
		} catch (AuthenticationException ae) {
			ae.printStackTrace();
			model.addAttribute("message", "系统异常！ 稍后再试");
			return "shiro/login";
		}
	}

	/**
	 * 退出
	 * 
	 * @author zhuw
	 * @param request
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {
			// 释放所有已知的身份信息
			subject.logout();
		}
		return "redirect:/app";
	}

	/**
	 * 转到管理主页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "home")
	public String home(Model model, HttpServletRequest request) {
		Subject currentUser = SecurityUtils.getSubject();
		@SuppressWarnings("unchecked")
		List<Resources> list = (List<Resources>) currentUser.getSession()
				.getAttribute(Constants.USER_RESOURCE);
		if (CollectionUtils.isNotEmpty(list)) {
			List<TinyTreeBean> menus = buildMenuPrivilegeTree(list);
			sortMenu(menus);
			SecurityUtils.getSubject().getSession()
					.setAttribute(Constants.USER_MENU_PRI, menus);
		}
		list = null;
		return "index/index";
	}

	/**
	 * 页面顶部
	 * 
	 * @return
	 */
	@RequestMapping(value = "top")
	public String top() {
		return "index/top";
	}

	/**
	 * 页面底部
	 * 
	 * @return
	 */
	@RequestMapping(value = "footer")
	public String footer() {
		return "index/footer";
	}

	/**
	 * 页面中部
	 * 
	 * @return
	 */
	@RequestMapping(value = "drag")
	public String drag() {
		return "index/drag";
	}

	private List<String> getAllowIPList() {
		List<String> list = new ArrayList<String>();
		list.add("117.158.207.130");
		list.add("117.158.207.131");
		list.add("117.158.207.132");
		list.add("117.158.207.133");
		list.add("117.158.207.134");
		list.add("117.158.207.135");
		list.add("117.158.207.136");
		list.add("117.158.207.137");
		list.add("117.158.207.138");
		list.add("117.158.207.139");

		list.add("171.8.0.33");
		list.add("171.8.0.34");
		list.add("125.46.82.100");
		list.add("171.8.148.227");
		// list.add("125.46.19.22");
		// list.add("125.46.19.23");
		// list.add("125.46.19.24");
		list.add("test192.168.111.222");
		return list;
	}

	/**
	 * 跳转到首页
	 * 
	 * @return
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request) {
		// 获取用户访问IP地址，对指定IP进行授权访问控制，前期我们测试需要，可以留个口子，请求方式get带上我们指定的IP参数，也允许放行
		String testreqIP = request.getParameter("testreqIP");
		HttpSession session = request.getSession(true);
		if (StringUtil.isNotEmpty(testreqIP)) {
			session.setAttribute("testreqIP", testreqIP);
		} else {
			testreqIP = (String) request.getSession().getAttribute("testreqIP");
		}
		String reallyreqIP = FindclientIP.getIpAddr(request);

		log.debug("---------------reallyreqIP-----------" + reallyreqIP);
		// 判断reallyreqIP是否在允许访问IP串中，如在，跳转登录。如不存在跳转到404错误页面
//		List<String> list = getAllowIPList();
//		if (list.contains(testreqIP) || list.contains(reallyreqIP)) {
//			log.debug("跳转到登录页面...");
//			return "shiro/login";
//		} else {
//			log.error("非法请求IP：" + reallyreqIP);
//			return "shiro/error";
//		}
		 return "shiro/login";
	}

	/**
	 * 登录成功后加载该用户可操作权限树
	 * 
	 * @author zhuw
	 * @return
	 */

	@RequestMapping(value = "getIndexMenu", method = RequestMethod.GET)
	public String getIndexMenu() {
		return "index/index_menu";
	}

	/**
	 * 构造用户资源成树型展示
	 * 
	 * @param entMenuPrivileges
	 * @return
	 */
	private List<TinyTreeBean> buildMenuPrivilegeTree(
			List<Resources> entMenuPrivileges) {
		TinyTreeBean root = new TinyTreeBean(ROOT_MENU_ID, null);
		Map<String, TinyTreeBean> menuIdTreeMap = new HashMap<String, TinyTreeBean>();
		menuIdTreeMap.put(ROOT_MENU_ID, root);
		for (Resources each : entMenuPrivileges) {
			TinyTreeBean node = new TinyTreeBean(each.getId(), each.getName());
			node.setUrl(each.getLink());
			node.setHide(each.changeIsOpen());
			node.selectStyle();
			if (StringUtil.isEmpty(each.getOrderid())) {// 因要比较大小，当ORDERID为空时，设置一个靠后的排序方式
				node.setOrderId(10000);
			} else {
				node.setOrderId(Integer.parseInt(each.getOrderid()));
			}
			menuIdTreeMap.put(each.getId(), node);
		}
		for (Resources each : entMenuPrivileges) {
			TinyTreeBean parent = menuIdTreeMap.get(each.getParentId());
			if (parent == null) {
				continue;
			}
			parent.addChild(menuIdTreeMap.get(each.getId()));
		}
		return menuIdTreeMap.get(ROOT_MENU_ID).getChildren();
	}

	/**
	 * 菜单按ORDERID排序
	 * 
	 * @param menus
	 */
	@SuppressWarnings("unchecked")
	private void sortMenu(List<TinyTreeBean> menus) {
		if (CollectionUtils.isEmpty(menus))
			return;
		BeanComparator menuBC = new BeanComparator("orderId");// 排序
		Collections.sort(menus, menuBC);
		for (TinyTreeBean bean : menus) {
			sortMenu(bean.getChildren());
		}
	}
}
