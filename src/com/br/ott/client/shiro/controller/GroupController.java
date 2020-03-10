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

import org.apache.commons.collections.CollectionUtils;
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
import com.br.ott.client.common.service.OperaLogService;
import com.br.ott.client.shiro.entity.GroupRole;
import com.br.ott.client.shiro.entity.GroupUser;
import com.br.ott.client.shiro.entity.Groups;
import com.br.ott.client.shiro.entity.Role;
import com.br.ott.client.shiro.entity.UserInfo;
import com.br.ott.client.shiro.service.GroupRoleService;
import com.br.ott.client.shiro.service.GroupService;
import com.br.ott.client.shiro.service.GroupUserService;
import com.br.ott.client.shiro.service.RoleService;
import com.br.ott.client.shiro.service.UserService;
import com.br.ott.common.util.Constants.GroupState;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.ObjectUtil;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.StringUtil;

/**
 * 分组 This class is used for ...
 * 
 * @author zhuw
 * @version 1.0 2012-8-2
 */
@Controller
@RequestMapping(value = "group")
public class GroupController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(GroupController.class);

	private static final String GROUP_NAME = "组管理";
	/**
	 * 分组服务接口
	 */
	@Autowired
	private GroupService groupService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private GroupRoleService grService;

	@Autowired
	private GroupUserService gUserService;

	@Autowired
	private UserService userService;

	@Autowired
	private OperaLogService operaLogService;

	/**
	 * 获得分组列表
	 * 
	 * @author zhuw
	 */
	@RequestMapping(value = "findGroupList", method = RequestMethod.GET)
	public String findGroupList(HttpServletRequest request, Model model,
			Groups groups) {
		if (StringUtil.isEmpty(groups.getStatus())) {
			groups.setStatus(GroupState.STATE1);
		}
		PagedModelList<Groups> pml = groupService.findAllGroup(groups,
				getPageNo(request), getPageRow(request));
		model.addAttribute("pml", pml);
		model.addAttribute("groups", groups);
		pml = null;
		return "shiro/listGroup";
	}

	/**
	 * 去编辑页面
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "toGroup", method = RequestMethod.GET)
	public String toGroup(HttpServletRequest request, Model model) {
		String id = request.getParameter("oid");
		Groups group = new Groups();
		if (!StringUtil.isEmpty(id)) {
			group = groupService.getGroupById(id);
		}
		model.addAttribute("group", group);
		group = null;

		List<Groups> mgroup = groupService.findAllGroupList();
		model.addAttribute("mgroup", mgroup);
		mgroup = null;
		return "shiro/groupInfo";
	}

	/**
	 * 检验组名是否已存在
	 * 
	 * @author zhuw
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "checkName", method = RequestMethod.GET)
	public void checkName(HttpServletRequest request,
			HttpServletResponse response) {
		String rname = request.getParameter("name");
		String oldName = request.getParameter("oldName");
		if (StringUtil.isEmpty(rname)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		if (rname.equals(oldName)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		boolean bool = groupService.getGroupByName(rname);
		writeAjaxResult(response, String.valueOf(bool));
	}

	@RequestMapping(value = "checkEname", method = RequestMethod.GET)
	public void checkEname(HttpServletRequest request,
			HttpServletResponse response) {
		String enName = request.getParameter("enName");
		String oldEnName = request.getParameter("oldEnName");
		if (StringUtil.isEmpty(enName)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		if (enName.equals(oldEnName)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		boolean bool = groupService.getGroupByenName(enName);
		writeAjaxResult(response, String.valueOf(bool));
	}

	/**
	 * 保存分组记录
	 * 
	 * @author zhuw
	 */
	@RequestMapping(value = "saveGroup", method = RequestMethod.POST)
	public @ResponseBody
	Feedback saveGroup(HttpServletRequest request, Groups group) {
		try {
			if (!StringUtil.isEmpty(group.getId())) {
				Groups gp = (Groups) ObjectUtil.clone(group);
				groupService.modifyGroup(group);
				String diffStr = ObjectUtil.getDiffColumnDescript(group, gp);
				logger.debug("变化值-->" + diffStr);
				if (null != diffStr) {
					// 写入系统操作日志
					operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request,
							GROUP_NAME, diffStr);
				}
				return Feedback.success("修改组记录成功!");
			} else {
				groupService.addGroup(group);
				// 写入系统操作日志
				operaLogService.addOperaLog(OPERA_TYPE_ADD, request,
						GROUP_NAME, group.getName());
				return Feedback.success("添加组记录成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return Feedback.fail("保存组记录失败!");
		}
	}

	/**
	 * 去查看页面
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "toViewGroup", method = RequestMethod.GET)
	public String toViewGroup(HttpServletRequest request, Model model) {
		String id = request.getParameter("oid");
		if (!StringUtil.isEmpty(id)) {
			Groups group = groupService.getGroupById(id);
			model.addAttribute("group", group);
			group = null;
		}

		List<Groups> mgroup = groupService.findAllGroupList();
		model.addAttribute("mgroup", mgroup);
		mgroup = null;
		return "shiro/viewGroup";
	}

	/**
	 * 删除分组 在删除前查找该组下是否有用户
	 * 
	 * @author zhuw
	 */
	@RequestMapping(value = "delGroup", method = RequestMethod.POST)
	public @ResponseBody
	Feedback delGroup(@RequestParam("oid") String id, HttpServletRequest request) {
		List<GroupUser> users = gUserService.findGroupUserByGroupId(id);
		if (!CollectionUtils.isEmpty(users)) {
			return Feedback.fail("该组下有用户,不能删除!");
		}
		users = null;
		try {
			groupService.delGroup(new String[] { id });
			Groups group = groupService.getGroupById(id);
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_DELETE, request, GROUP_NAME,
					"删除该记录,组名=" + group.getName());
		} catch (Exception e) {
			logger.error(e);
			return Feedback.fail("删除失败!");
		}

		return Feedback.success("删除成功!");
	}

	/**
	 * 查找所有角色
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "findAllRole", method = RequestMethod.GET)
	public String findAllRole(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		model.addAttribute("id", id);
		List<Role> roleList = roleService.findAllRoleByGroupId(id); // 系统中可选权限
		model.addAttribute("roleList", roleList);
		roleList = null;
		List<GroupRole> rgList = grService.findGroupRoleByGroupId(id); // 当前用户已有权限
		List<String> grList = new ArrayList<String>();
		for (GroupRole groupRole : rgList) {
			grList.add(groupRole.getRoleid());
		}
		model.addAttribute("oldId", StringUtils.join(grList, ","));
		model.addAttribute("rgList", rgList);
		rgList = null;
		return "shiro/selectGroupRole";
	}

	/**
	 * 往组里添加角色
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "addRoleToGroup", method = RequestMethod.POST)
	public @ResponseBody
	Feedback addRoleToGroup(HttpServletRequest request, Model model) {
		try {
			String id = request.getParameter("id");
			String roleIds = request.getParameter("roleIds");
			String oldId = request.getParameter("oldId");
			
			String[] oldArr = oldId.split(",");
			String[] roleArr = roleIds.split(",");
			List<String> sbf = new ArrayList<String>();
			for (int i = 0; i < oldArr.length; i++) {
				boolean f1 = true;
				for (int j = 0; j < roleArr.length; j++) {
					if (oldArr[i].equals(roleArr[j])) {
						f1 = false;
					}
				}
				if (f1) {
					sbf.add(oldArr[i]);
				}
			}
			grService.delGroupRoleByGid(sbf, id); // 执行权限移除

			// 写入系统操作日志
			StringBuffer delGroup = new StringBuffer();
			StringBuffer addGroup = new StringBuffer();
			for (String delID : sbf) {
				Role role = roleService.getRoleById(delID);
				if (role != null) {
					delGroup.append(role.getRoleName()).append(",");
				}
			}
			if (StringUtil.isNotEmpty(roleIds)) {
				GroupRole gRole = new GroupRole();
				for (String roleId : roleArr) {
					GroupRole gr = grService
							.getGroupRoleByRidAndGid(id, roleId);
					if (gr == null) {
						gRole.setGroupid(id);
						gRole.setRoleid(roleId);
						grService.addRoleToGroup(gRole);
						// 写入系统操作日志
						Role role = roleService.getRoleById(roleId);
						if (role != null) {
							addGroup.append(role.getRoleName()).append(",");
						}
					}
				}
			}
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, GROUP_NAME,
					"组记录:修改组权限,移除:" + delGroup + "   ;新增:" + addGroup);
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("角色分组失败");
		}
		return Feedback.success("角色分组成功");
	}

	/**
	 * 查找所有用户
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "findAllUser", method = RequestMethod.GET)
	public String findAllUser(HttpServletRequest request, Model model) {
		String id = request.getParameter("id");
		model.addAttribute("id", id);
		List<UserInfo> userInfos = userService.findAllUserByGroupId(id); // 系统中可选用户
		model.addAttribute("userInfos", userInfos);
		userInfos = null;
		List<GroupUser> guList = gUserService.findGroupUserByGroupId(id);// 当前用户
		List<String> grList = new ArrayList<String>();
		for (GroupUser groupUser : guList) {
			grList.add(groupUser.getUserid());
		}
		model.addAttribute("oldId", StringUtils.join(grList, ","));
		model.addAttribute("guList", guList);
		guList = null;
		return "shiro/selectGroupUser";
	}

	/**
	 * 往组里添加用户
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "addUserToGroup", method = RequestMethod.POST)
	public @ResponseBody
	Feedback addUserToGroup(HttpServletRequest request, Model model) {
		try {
			String userIds = request.getParameter("userIds");
			String gid = request.getParameter("id");
			String oldId = request.getParameter("oldId");
			String[] usersArr = oldId.split(",");
			GroupUser gUser = new GroupUser();
			String[] userIdArr = userIds.split(",");

			StringBuffer sbf = new StringBuffer();
			for (int i = 0; i < usersArr.length; i++) {
				boolean f1 = true;
				for (int j = 0; j < userIdArr.length; j++) {
					if (usersArr[i].equals(userIdArr[j])) {
						f1 = false;
					}
				}
				if (f1) {
					sbf.append(usersArr[i]).append(",");
				}
			}
			String[] stry = sbf.toString().split(",");
			gUserService.delUserToGroup(stry, gid); // 执行用户移除

			// 写入系统操作日志
			StringBuffer delUser = new StringBuffer();
			StringBuffer addUser = new StringBuffer();
			for (String delID : stry) {
				UserInfo userInfo = userService.getUserById(delID);
				if (userInfo != null) {
					delUser.append(userInfo.getUserName()).append(",");
				}
			}
			if (StringUtil.isNotEmpty(userIds)) {
				for (String uId : userIdArr) {
					GroupUser gu = gUserService.getGroupUserByGIdAndUId(gid,
							uId);
					if (gu == null) {
						gUser.setUserid(uId);
						gUser.setGroupid(gid);
						gUserService.addUserToGroup(gUser);

						// 写入系统操作日志
						UserInfo userInfo = userService.getUserById(uId);
						if (userInfo != null) {
							addUser.append(userInfo.getUserName()).append(",");
						}
					}
				}
			}
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, GROUP_NAME,
					"组记录:修改组用户,移除:" + delUser + "   ;新增:" + addUser);
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("用户分组失败");
		}
		return Feedback.success("用户分组成功");
	}

}
