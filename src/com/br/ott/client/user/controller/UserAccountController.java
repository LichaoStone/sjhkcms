package com.br.ott.client.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.base.BaseController;
import com.br.ott.client.common.entity.District;
import com.br.ott.client.user.entity.UserAccount;
import com.br.ott.client.user.service.UserAccountService;
import com.br.ott.common.util.MD5Util;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.StringUtil;

/**
 * 用户管理模块
 * 
 * @author 陈登民
 * 
 */
@Controller
@RequestMapping(value = "/userinfo")
public class UserAccountController extends BaseController {
	private static final Logger logger = Logger
			.getLogger(UserAccountController.class);

	@Autowired
	private UserAccountService userService;

	/**
	 * 用户列表（包括多条件查询）
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/findUsers")
	public String findUsers(HttpServletRequest request, Model model) {
		// 将所请求参数绑定Model上
		model = bindRequestToModel(request, model);
		
		logger.debug("查询管理员列表开始,当前页码："+getPageNo(request));
		UserAccount user = new UserAccount();
		user.setNickName(getParameter(request,"nickName"));
		user.setPhone(getParameter(request,"phone"));
		user.setEmail(getParameter(request,"email"));
		user.setOrderColumn(getOrderColumn(request));
		user.setStatus(getParameter(request,"status"));

		PagedModelList<UserAccount> pagedModelList = userService.findUsers(
				getPageNo(request), user);
		model.addAttribute("pagedModelList", pagedModelList);
		return "user/listUser";
	}

	/**
	 * 去到添加用户页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "toAddUser")
	public String toAddUser() {
		logger.info("go to addUserInfo.vm");
		return "user/addUserInfo";
	}

	/**
	 * 添加一个用户信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "addUser", method = RequestMethod.POST)
	public String addUser(HttpServletRequest request) {
		logger.info("添加用户信息开始!");
		UserAccount user = (UserAccount) resolveBeanFromRequest(request,
				UserAccount.class);
		if (user != null) {
			// 账号ID
			String id = user.getId();
			if (!StringUtil.isEmpty(id)) {
				// 用户名生成规则： 将账号ID取MD5值
				user.setTrueName(MD5Util.getMD5(id));
			}
		}
		userService.addUser(user);
		logger.info("添加用户信息成功!");
		return "init";
	}

	/**
	 * 查看用户详情/编辑用户信息初始化数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "toDetailUser")
	public String toDetailUser(String id, boolean isEdit, Model model) {
		if (isEdit) {
			logger.debug("编辑用户信息、ID：" + id);
		} else {
			logger.debug("查看用户详情、ID：" + id);
		}
		UserAccount account = userService.getUserAccount(id);
		model.addAttribute("u", account);
		if(account != null){
			// 绑定所需要城市信息
			bindCitysAllToModel(model, account.getCity());
		}
		if (isEdit) {
			return "user/editUser";
		} else {
			return "user/detailUser";
		}
	}
	
	/**
	 * 切换省份
	 * 
	 * @return
	 */
	@RequestMapping(value = "changeCity")
	public @ResponseBody List<District> changeCity(HttpServletRequest request){
		String proId = request.getParameter("proId");
		logger.debug("切换省份，获取城市,省份ID："+proId);
		return getCitys(proId);
	}

	/**
	 * 修改用户信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "modifyUser", method = RequestMethod.POST)
	public String modifyUser(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		logger.info("修改用户信息开始!");
		UserAccount user = (UserAccount) resolveBeanFromRequest(request,
				UserAccount.class);
		if (user != null) {
			// 账号ID
			String id = user.getId();
			if (!StringUtil.isEmpty(id)) {
				userService.modifyUserAccount(user);
				logger.info("修改用户信息成功!");
				writeAjaxResult(response, RETURN_OK);
				return null;
			}
		}
		return newErrorModelView(model, "用户不存在,无法修改信息.");
	}

	/**
	 * 删除一个用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delUser")
	public String delUser(HttpServletResponse response, String id) {
		logger.debug("开始删除:id-" + id);
		if (StringUtil.isEmpty(id)) {
			writeAjaxResult(response, "0|该用户不存在!");
			return null;
		}
		try{
			userService.delUser(id);
			writeAjaxResult(response, RETURN_OK);
		}catch (Exception e) {
			writeAjaxResult(response, "系统错误");
		}
		return null;
	}
}
