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
import com.br.ott.client.shiro.entity.Groups;
import com.br.ott.client.shiro.entity.Resources;
import com.br.ott.client.shiro.entity.Role;
import com.br.ott.client.shiro.entity.RoleAuth;
import com.br.ott.client.shiro.service.GroupRoleService;
import com.br.ott.client.shiro.service.GroupService;
import com.br.ott.client.shiro.service.ResourcesService;
import com.br.ott.client.shiro.service.RoleAuthService;
import com.br.ott.client.shiro.service.RoleService;
import com.br.ott.common.util.Constants.RoleState;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.StringUtil;

/**
 * 角色 This class is used for ...
 * 
 * @author zhuw
 * @version 1.0 2012-8-2
 */
@Controller
@RequestMapping(value = "role")
public class RoleController extends BaseController {

	private static final Logger logger = Logger.getLogger(RoleController.class);

	private static final String ROLE_NAME = "角色管理";

	/**
	 * 角色服务接口
	 */
	@Autowired
	private RoleService roleService;

	/**
	 * 资源服务接口
	 */
	@Autowired
	private ResourcesService resourService;

	/**
	 * 分组服务接口
	 */
	@Autowired
	private GroupService groupService;

	@Autowired
	private GroupRoleService grService;

	@Autowired
	private RoleAuthService roleAuthService;

	@Autowired
	private OperaLogService operaLogService;

	/**
	 * 获得角色列表
	 * 
	 * @author zhuw
	 */
	@RequestMapping(value = "findRoleList", method = RequestMethod.GET)
	public String findRoleList(HttpServletRequest request, Model model,
			Role role) {
		if (StringUtil.isEmpty(role.getStatus())) {
			role.setStatus(RoleState.STATE1);
		}
		PagedModelList<Role> pml = roleService.findAllRole(role,
				getPageNo(request), getPageRow(request));
		model.addAttribute("pml", pml);
		model.addAttribute("role", role);
		pml = null;
		return "shiro/listRole";
	}

	/**
	 * 去角色编辑页面
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "toRole", method = RequestMethod.GET)
	public String toRole(HttpServletRequest request, Model model) {
		Role role = new Role();
		String id = request.getParameter("oid");
		if (!StringUtil.isEmpty(id)) {
			role = roleService.getRoleById(id);
		}
		model.addAttribute("role", role);
		return "shiro/roleInfo";
	}

	/**
	 * 检验角色名是否已存在
	 * 
	 * @author zhuw
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "checkRname", method = RequestMethod.GET)
	public void checkRname(HttpServletRequest request,
			HttpServletResponse response) {
		String rname = request.getParameter("roleName");
		String oldName = request.getParameter("oldName");
		if (StringUtil.isEmpty(rname)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		if (rname.equals(oldName)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}

		boolean bool = roleService.getRoleByName(rname);
		writeAjaxResult(response, String.valueOf(bool));
	}

	/**
	 * 校验英文名是否存在 TODO 创建人：pananz 创建时间：2014-11-21 - 上午10:33:21
	 * 
	 * @param request
	 * @param response
	 *            返回类型：void
	 * @exception throws
	 */
	@RequestMapping(value = "checkEname", method = RequestMethod.GET)
	public void checkEname(HttpServletRequest request,
			HttpServletResponse response) {
		String enName = request.getParameter("enname");
		String oldEnName = request.getParameter("oldEnName");
		if (StringUtil.isEmpty(enName)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		if (enName.equals(oldEnName)) {
			writeAjaxResult(response, String.valueOf(true));
			return;
		}
		boolean bool = roleService.getRoleByenName(enName);
		writeAjaxResult(response, String.valueOf(bool));
	}

	/**
	 * 保存角色记录
	 * 
	 * @author zhuw
	 */
	@RequestMapping(value = "saveRole", method = RequestMethod.POST)
	public @ResponseBody
	Feedback saveRole(HttpServletRequest request, Role role) {
		try {
			if (!StringUtil.isEmpty(role.getOid())) {
				roleService.modifyRole(role);
				return Feedback.success("修改角色记录成功!");
			} else {
				roleService.addRole(role);
				return Feedback.success("添加角色记录成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e);
			return Feedback.fail("保存角色记录失败!");
		}
	}

	/**
	 * 去查看页面
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "toViewRole", method = RequestMethod.GET)
	public String toViewRole(HttpServletRequest request, Model model) {
		String id = request.getParameter("oid");
		if (!StringUtil.isEmpty(id)) {
			Role role = roleService.getRoleById(id);
			model.addAttribute("role", role);
		}
		return "shiro/viewRole";
	}

	/**
	 * 删除指定id的角色
	 * 
	 * @author zhuw
	 */
	@RequestMapping(value = "delRole", method = RequestMethod.POST)
	public @ResponseBody
	Feedback delRole(@RequestParam("oid") String id, HttpServletRequest request) {
		List<GroupRole> gRole = grService.findGroupRoleByRoleId(id);
		if (!CollectionUtils.isEmpty(gRole)) {
			return Feedback.fail("该角色下有分组,不能删除!");
		}
		try {
			Role role = roleService.getRoleById(id);
			roleService.delRole(new String[] { id });
			// 写入系统操作日志
			operaLogService.addOperaLog(OPERA_TYPE_DELETE, request, ROLE_NAME,
					"删除该记录,角色名=" + role.getRoleName());
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e);
			return Feedback.fail("删除失败!");
		}
		return Feedback.success("删除成功!");
	}

	/**
	 * 转到角色菜单页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "findRoleResourByRoleId", method = RequestMethod.GET)
	public String findRoleResourByRoleId(HttpServletRequest request, Model model) {
		String roleId = request.getParameter("roleId");
		model.addAttribute("roleId", roleId);
		return "shiro/roleResourTree";
	}

	/**
	 * 查看角色菜单权限树
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "buildResourceTree", method = RequestMethod.POST)
	public void buildResourceTree(HttpServletRequest request,
			HttpServletResponse response) {
		writeAjaxResult(response,
				resourService.buildTreeResource(request.getParameter("roleId")));
	}

	/**
	 * 给角色授予资源
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "addRoleResource", method = RequestMethod.POST)
	public @ResponseBody
	Feedback addRoleResource(HttpServletRequest request) {
		String resIds = request.getParameter("resIds");
		String roleId = request.getParameter("roleId");
		String[] res = resIds.split(",");
		try {
			roleAuthService.addAuthToRole(res, roleId);
			return Feedback.success("角色授予资源成功");
		} catch (RuntimeException e) {
			e.printStackTrace();
			logger.debug(e);
			return Feedback.fail("角色授予资源失败");
		}
	}

	/**
	 * 给角色分配资源
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "addResourToRole", method = RequestMethod.POST)
	public String addResourToRole(HttpServletRequest request, Model model) {
		String id = request.getParameter("resId");
		String roleId = request.getParameter("id");
		String oldId = request.getParameter("sb");
		RoleAuth rAuth = new RoleAuth();
		String[] oldIds = oldId.split(",");
		String[] str = id.split(",");
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < oldIds.length; i++) {
			boolean f1 = true;
			for (int j = 0; j < str.length; j++) {
				if (oldIds[i].equals(str[j])) {
					f1 = false;
				}
			}
			if (f1) {
				sbf.append(oldIds[i]).append(",");
			}
		}
		String[] stry = sbf.toString().split(",");
		roleAuthService.delResourToRole(stry, roleId); // 执行权限移除
		// 写入系统操作日志
		StringBuffer delRes = new StringBuffer();
		StringBuffer addRes = new StringBuffer();
		for (String delId : stry) {
			Resources resour = resourService.getResourById(delId);
			// 写入系统操作日志
			if (resour != null) {
				delRes.append(resour.getName()).append(",");
			}
		}
		for (String strId : str) {
			RoleAuth ra = roleAuthService.findRoleAuth(roleId, strId);
			if (null == ra) {
				rAuth.setResourceId(strId);
				rAuth.setRoleId(roleId);
				rAuth.setActions("");
				roleAuthService.addAuthToRole(rAuth);

				// 写入系统操作日志
				Resources resour = resourService.getResourById(strId);
				if (resour != null) {
					addRes.append(resour.getName()).append(",");
				}
			}
		}
		operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, ROLE_NAME,
				"权限记录:修改权限资源信息,移除:" + delRes + "   ;新增:" + addRes);
		return "redirect:/role/findRoleList.do";
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
		model.addAttribute("id", id);
		List<Groups> groupList = groupService.findAllGroupByRoleId(id); // 系统可选分组
		model.addAttribute("groupList", groupList);
		groupList = null;
		// 当前角色已存在的分组
		List<GroupRole> roleGroupList = grService.findGroupRoleByRoleId(id);
		List<String> grList = new ArrayList<String>();
		for (GroupRole groupRole : roleGroupList) {
			grList.add(groupRole.getGroupid());
		}
		model.addAttribute("oldId", StringUtils.join(grList, ","));
		model.addAttribute("roleGroupList", roleGroupList);
		roleGroupList = null;
		return "shiro/selectRoleGroup";
	}

	/**
	 * 给角色分组
	 * 
	 * @author zhuw
	 * @return
	 */
	@RequestMapping(value = "addGroupToRole", method = RequestMethod.POST)
	public @ResponseBody
	Feedback addGroupToRole(HttpServletRequest request, Model model) {
		try {
			String groupIds = request.getParameter("groupIds");
			String roleId = request.getParameter("roleId");
			String oldId = request.getParameter("oldId");
			String[] oldArr = oldId.split(",");
			String[] groupArr = groupIds.split(",");
			List<String> sbf = new ArrayList<String>();
			for (int i = 0; i < oldArr.length; i++) {
				boolean f1 = true;
				for (int j = 0; j < groupArr.length; j++) {
					if (oldArr[i].equals(groupArr[j])) {
						f1 = false;
					}
				}
				if (f1) {
					sbf.add(oldArr[i]);
				}
			}
			grService.delGroupRoleByRid(sbf, roleId); // 执行权限移除

			// 写入系统操作日志
			StringBuffer delgp = new StringBuffer();
			StringBuffer addgp = new StringBuffer();
			for (String delId : sbf) {
				Groups group = groupService.getGroupById(delId);
				// 写入系统操作日志
				if (group != null) {
					delgp.append(group.getName()).append(",");
				}
			}
			GroupRole gRole = new GroupRole();
			if(StringUtil.isNotEmpty(groupIds)){
				for (String groupId : groupArr) {
					GroupRole gr = grService.getGroupRoleByRidAndGid(groupId,
							roleId);
					if (gr == null) {
						gRole.setGroupid(groupId);
						gRole.setRoleid(roleId);
						grService.addRoleToGroup(gRole);
						
						// 写入系统操作日志
						Groups groups = groupService.getGroupById(groupId);
						if (groups != null) {
							addgp.append(groups.getName()).append(",");
						}
					}
				}
			}
			operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request, ROLE_NAME,
					"权限记录:修改权限组,移除:" + delgp + "   ;新增:" + addgp);
		} catch (Exception e) {
			e.printStackTrace();
			return Feedback.fail("角色失败成功");
		}
		return Feedback.success("角色分组成功");
	}
}
