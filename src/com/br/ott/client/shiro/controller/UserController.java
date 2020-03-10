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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.base.BaseController;
import com.br.ott.client.SessionUtil;
import com.br.ott.client.common.service.OperaLogService;
import com.br.ott.client.partner.entity.Partner;
import com.br.ott.client.partner.service.PartnerService;
import com.br.ott.client.shiro.entity.GroupUser;
import com.br.ott.client.shiro.entity.Groups;
import com.br.ott.client.shiro.entity.UserInfo;
import com.br.ott.client.shiro.service.GroupService;
import com.br.ott.client.shiro.service.GroupUserService;
import com.br.ott.client.shiro.service.UserService;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.MD5Util;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.StringUtil;

/**
 * 用户管理
 * 
 * @author zhuw
 * @version 1.0 2012-7-25
 */
@Controller
@RequestMapping(value = "user")
public class UserController extends BaseController {
	private static final Logger logger = Logger.getLogger(UserController.class);

	private static final String USER_NAME = "用户管理";

	@Autowired
	private OperaLogService operaLogService;

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private GroupUserService guService;

	@Autowired
	private PartnerService partnerService;

	@RequestMapping(value = "findUsersList", method = RequestMethod.GET)
	public String findUsersList(HttpServletRequest request, Model model,
			UserInfo user) {
		if (StringUtil.isEmpty(user.getStatus())) {
			user.setStatus("1");
		}
		PagedModelList<UserInfo> pagedModelList = userService.findAllUserList(
				getPageNo(request), getPageRow(request), user);
		model.addAttribute("pagedModelList", pagedModelList);
		model.addAttribute("users", user);
		pagedModelList = null;
		return "shiro/listUser";
	}

	/**
	 * 去添加页面
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "toUser", method = RequestMethod.GET)
	public String toAddUser(HttpServletRequest request, Model model) {
		UserInfo userInfo = new UserInfo();
		String id = request.getParameter("uid");
		if (!StringUtil.isEmpty(id)) {
			userInfo = userService.getUserById(id);
		}
		model.addAttribute("userInfo", userInfo);
		List<Groups> mgroup = groupService.findAllGroupList();
		model.addAttribute("mgroup", mgroup);
		mgroup = null;

		if (StringUtil.isNotEmpty(userInfo.getAreaid())) {
			// 绑定所需要城市信息
			bindCitysAllToModel(model, userInfo.getAreaid());
		} else {
			bindCitysAllToModel(model, "");
		}
		
		return "shiro/userInfo";
	}

	/**
	 * 保存用户
	 * 
	 * @author zhuw
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveUser", method = RequestMethod.POST)
	public @ResponseBody
	Feedback saveUser(HttpServletRequest request, UserInfo userInfo) {
		try {
			if (!StringUtil.isEmpty(userInfo.getUserId())) {
				userService.modifyUser(userInfo);
				// 添加日志
				operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request,
						USER_NAME, "修改记录,用户名=" + userInfo.getUserName());
			} else {
				userInfo.setPassword(MD5Util.getMD5(userInfo.getPassword()));
				userService.addUser(userInfo);
				// 添加日志
				operaLogService.addOperaLog(OPERA_TYPE_ADD, request, USER_NAME,
						"添加记录,用户名=" + userInfo.getUserName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("用户保存失败");
		}
		return Feedback.success("用户保存成功");
	}

	/**
	 * 查找所有组
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "findAllGroup", method = RequestMethod.GET)
	public String findAllGroup(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		model.addAttribute("userId", id);
		List<Groups> groupList = groupService.findAllGroupByUserId(id); // 系统中所有组
		model.addAttribute("groupList", groupList);
		groupList = null;
		List<GroupUser> ruList = guService.findGroupUserByUserId(id); // 当前用户已有组
		List<String> list = new ArrayList<String>();
		for (GroupUser groupUser : ruList) {
			list.add(groupUser.getGroupid());
		}
		model.addAttribute("oldGroupId", StringUtils.join(list, ","));
		model.addAttribute("ruList", ruList);
		ruList = null;
		return "shiro/selectUserGroup";
	}

	/**
	 * 给用户分组
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "addUserToGroup", method = RequestMethod.POST)
	public String addUserToGroup(HttpServletRequest request, Model model) {
		String groupId = request.getParameter("groupId");
		String userId = request.getParameter("userId");
		String oldGroupId = request.getParameter("oldGroupId");
		String msg = userService.addUserToGroup(userId, groupId, oldGroupId);
		operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, USER_NAME,
				"用户分组记录:修改所属组信息," + msg);
		return "redirect:/user/findUsersList.do";
	}

	/**
	 * 禁用用户
	 * 
	 * @author zhuw
	 */
	@RequestMapping(value = "delUser", method = RequestMethod.POST)
	public @ResponseBody
	Feedback delUser(@RequestParam("uid") String id, HttpServletRequest request) {
		UserInfo userInfo = userService.getUserById(id);
		try {
			userService.delUser(new String[] { id });
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_DELETE, request, USER_NAME,
					"禁用用户,用户名=" + userInfo.getUserName());
		} catch (Exception e) {
			logger.debug(e);
			e.printStackTrace();
			return Feedback.fail("禁用失败!");
		}
		return Feedback.success("禁用成功!");
	}

	/**
	 * 去修改页面
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "toViewUser", method = RequestMethod.GET)
	public String toViewUser(HttpServletRequest request, Model model) {
		UserInfo userInfo = new UserInfo();
		String id = request.getParameter("uid");
		if (!StringUtil.isEmpty(id)) {
			userInfo = userService.getUserById(id);
		}
		model.addAttribute("userInfo", userInfo);
		Partner partner = partnerService.getPartnerId(userInfo.getPartnerid());
		model.addAttribute("partner", partner);

		List<Groups> mgroup = groupService.findAllGroupList();
		model.addAttribute("mgroup", mgroup);
		mgroup = null;
		bindCitysAllToModel(model, userInfo.getAreaid());

		return "shiro/viewUser";
	}

	/**
	 * 用户名的唯一性
	 * 
	 * @param comm
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "checkUserNameIsUse", method = RequestMethod.GET)
	public void checkUserNameIsUse(HttpServletResponse response,
			HttpServletRequest request) {
		String userName = request.getParameter("userName");
		String oldName = request.getParameter("oldName");

		if (StringUtil.isEmpty(userName)) {
			writeAjaxResult(response, String.valueOf(true));
		}
		if (StringUtil.isNotEmpty(oldName) && userName.equals(oldName)) {
			writeAjaxResult(response, String.valueOf(true));
		}

		boolean result = userService.checkUserNameIsUse(userName);
		writeAjaxResult(response, String.valueOf(result));

	}

	/**
	 * 手机账号的唯一性 创建人：pananz 创建时间：2013-3-26 - 下午3:58:18
	 * 
	 * @param response
	 * @param request
	 *            返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "checkPhoneIsUse", method = RequestMethod.GET)
	public void checkPhoneIsUse(HttpServletResponse response,
			HttpServletRequest request) {
		String phone = request.getParameter("phone");
		String oldPhone = request.getParameter("oldPhone");
		if (StringUtil.isEmpty(phone)) {
			writeAjaxResult(response, String.valueOf(true));
		}
		if (StringUtil.isNotEmpty(oldPhone) && phone.equals(oldPhone)) {
			writeAjaxResult(response, String.valueOf(true));
		}

		boolean result = userService.checkPhoneIsUse(phone);
		writeAjaxResult(response, String.valueOf(result));

	}

	/**
	 * 去密码修改页面
	 * 
	 * @param model
	 * @return string
	 */
	@RequestMapping(value = "toEditPwd", method = RequestMethod.GET)
	public String toEditPwd() {
		return "shiro/editPassword";
	}

	/**
	 * 密码修改 创建人：莫家森 创建时间： 2013-11-11 - 上午11:45
	 * 
	 * @param model
	 * @return string
	 */
	@RequestMapping(value = "editPwd", method = RequestMethod.POST)
	public @ResponseBody
	Feedback editPwd(HttpServletRequest request) {
		try {
			UserInfo userInfo = SessionUtil.getCurrentUser();
			String newPass = request.getParameter("newpwd");

			userService.editPwd(userInfo.getUserId(), newPass);
		} catch (Exception e) {
			logger.error("密码修改失败：" + e.getMessage());
			return Feedback.fail("密码修改失败！");
		}
		return Feedback.success("密码修改成功！");
	}

	/**
	 * 修改密码时，判断原密码是否正确 创建人：莫家森 创建时间：2013-11-15 - 下午15:39
	 * 
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/checkPwdIsRight")
	public void checkPwdIsRight(HttpServletResponse response,
			@RequestParam("oldpwd") String password) {
		UserInfo userInfo = SessionUtil.getCurrentUser();
		String oriPassword = userInfo.getPassword();
		boolean bool = false;

		if (StringUtil.isEmpty(password) || password.equals(oriPassword)) {
			bool = true;// 密码正确
		}
		writeAjaxResult(response, String.valueOf(bool));
	}

	/**
	 * 点击获取登录用户相关model信息并跳转到个人信息修改页面
	 * 
	 * @author morrison
	 * @time 2013-11-09 - 15:37
	 * @param model
	 * @return string
	 */
	@RequestMapping(value = "toUpdate", method = RequestMethod.GET)
	public String toUpdate(Model model) {
		UserInfo userInfo = SessionUtil.getCurrentUser();
		model.addAttribute("userInfo", userInfo);
		return "/shiro/modifyUserBaseInfo";
	}

	/**
	 * 修改当前登录用户的信息并保存 创建人：莫家森 创建时间： 2013-11-11 - 上午10:24
	 * 
	 * @param request
	 * @param model
	 * @return json
	 */
	@RequestMapping(value = "modifyBaseInfo", method = RequestMethod.POST)
	public @ResponseBody
	Feedback modifyBaseInfo(UserInfo userInfo, HttpServletRequest request) {
		try {
			UserInfo temp = userService.getUserById(userInfo.getUserId());
			temp.setLoginname(userInfo.getLoginname());
			temp.setPhone(userInfo.getPhone());

			userService.modifyBaseUserInfo(temp);
			SessionUtil.setCurrentUser(temp);
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, "个人信息修改",
					"修改记录,真实姓名=" + userInfo.getLoginname());
		} catch (Exception e) {
			logger.error("个人信息修改失败:" + e.getMessage());
			return Feedback.fail("个人信息修改失败");
		}
		return Feedback.success("个人信息修改成功");
	}

	/**
	 * 转到密码重置页面 创建人：pananz 创建时间：2013-11-18 - 上午11:20:45
	 * 
	 * @param request
	 * @param model
	 * @return 返回类型：String
	 * @exception throws
	 */
	@RequestMapping(value = "toResetPwd", method = RequestMethod.GET)
	public String toPwdInfo(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		UserInfo userInfo = userService.getUserById(id);
		model.addAttribute("userInfo", userInfo);
		return "shiro/pwdInfo";
	}

	/**
	 * 密码重置 创建人：pananz 创建时间：2013-11-18 - 上午11:21:06
	 * 
	 * @param request
	 * @return 返回类型：Feedback
	 * @exception throws
	 */
	@RequestMapping(value = "resetPwd")
	public @ResponseBody
	Feedback resetPwd(HttpServletRequest request, UserInfo userInfo) {
		try {
			String pasword = MD5Util.getMD5(userInfo.getPassword());
			userService.editPwd(userInfo.getUserId(), pasword);
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, USER_NAME,
					"修改用户密码记录,用户名=" + userInfo.getUserName());
			return Feedback.success("重置密码成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("重置密码失败");
		}
	}
}
