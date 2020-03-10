/* 
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Founder. You shall not disclose such Confidential Information 
 * and shall use it only in accordance with the terms of the agreements 
 * you entered into with Founder. 
 * 
 */

package com.br.ott.client.shiro.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.br.ott.client.shiro.entity.GroupUser;
import com.br.ott.client.shiro.entity.Groups;
import com.br.ott.client.shiro.entity.UserInfo;
import com.br.ott.client.shiro.mapper.GroupMapper;
import com.br.ott.client.shiro.mapper.GroupUserMapper;
import com.br.ott.client.shiro.mapper.UsersMapper;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.StringUtil;

@Component
public class UserService {

	@Autowired
	private UsersMapper userDao;

	@Autowired
	private GroupUserMapper groupUserMapper;

	@Autowired
	private GroupMapper groupMapper;

	/**
	 * 分页查询
	 * 
	 * @author zhuw
	 * @param role
	 * @param skip
	 * @return
	 */

	public PagedModelList<UserInfo> findAllUserList(int pageNo, int pageRow,
			UserInfo userInfo) {
		int count = userDao.getCountUser(userInfo);
		PagedModelList<UserInfo> modelList = new PagedModelList<UserInfo>(
				pageNo, pageRow, count);
		List<UserInfo> users = userDao.findAllUser(userInfo, new RowBounds(
				(pageNo - 1) * pageRow, pageRow));
		modelList.addModels(users);
		return modelList;
	}

	/**
	 * 查询所有
	 * 
	 * @author zhuw
	 * @return
	 */
	public List<UserInfo> findAllUserList() {
		return userDao.getUserList();
	}

	/**
	 * 设置用户组 创建人：pananz 创建时间：2013-3-26 - 下午4:51:49
	 * 
	 * @param id
	 * @param uId
	 * @param gId
	 * @return 返回类型：String
	 * @exception throws
	 */
	@Transactional
	public String addUserToGroup(String userId, String groupId, String oldGroup) {
		GroupUser gUseru = new GroupUser();
		String[] oldGroups = oldGroup.split(",");
		String[] groupIds = groupId.split(",");
		List<String> sbf = new ArrayList<String>();
		for (int i = 0; i < oldGroups.length; i++) {
			boolean f1 = true;
			for (int j = 0; j < groupIds.length; j++) {
				if (oldGroups[i].equals(groupIds[j])) {
					f1 = false;
				}
			}
			if (f1) {
				sbf.add(oldGroups[i]);
			}
		}
		StringBuffer delGroup = new StringBuffer();
		StringBuffer addGroup = new StringBuffer();
		// 删除旧的，并做记录
		for (String delId : sbf) {
			Groups group = groupMapper.getGroupById(delId);
			groupUserMapper.delGroupToUser(delId, userId);
			if (group != null) {
				delGroup.append(group.getName()).append(",");
			}
		}

		// 增加新的，并做记录
		if (StringUtil.isNotEmpty(groupId)) {
			for (String gid : groupIds) {
				GroupUser gu = groupUserMapper.getGroupUserByGIdAndUId(gid,
						userId);
				if (gu == null) {
					gUseru.setUserid(userId);
					gUseru.setGroupid(gid);
					groupUserMapper.addUserToGroup(gUseru);
					Groups groups = groupMapper.getGroupById(gid);
					if (groups != null) {
						addGroup.append(groups.getName()).append(",");
					}
				}
			}
		}
		StringBuffer sBuffer = new StringBuffer();
		if (StringUtil.isNotEmpty(delGroup.toString())) {
			sBuffer.append("移除:" + delGroup.toString() + "   ;");
		}
		if (StringUtil.isNotEmpty(addGroup.toString())) {
			sBuffer.append("新增:" + addGroup);
		}
		return sBuffer.toString();
	}

	/**
	 * 添加
	 * 
	 * @author zhuw 修改人：pananz 修改时间：2013-3-25 - 下午3:43:16
	 * @param userInfo
	 *            返回类型：void
	 * @exception throws
	 */
	@Transactional
	public void addUser(UserInfo userInfo) {
		userDao.addUser(userInfo);

		GroupUser gUseru = new GroupUser();
		gUseru.setUserid(userInfo.getUserId());
		gUseru.setGroupid(userInfo.getGroupId());
		groupUserMapper.addUserToGroup(gUseru);
	}

	/**
	 * 修改
	 * 
	 * @author zhuw 修改人：pananz 修改时间：2013-3-25 - 下午3:43:16
	 * @param userInfo
	 *            返回类型：void
	 * @exception throws
	 */
	@Transactional
	public void modifyUser(UserInfo userInfo) {
		userDao.modifyUser(userInfo);

		GroupUser gUseru = new GroupUser();
		gUseru.setUserid(userInfo.getUserId());
		gUseru.setGroupid(userInfo.getGroupId());
		// 先删除旧的再添加新的
		groupUserMapper.delGroupUserByUserId(userInfo.getUserId());
		groupUserMapper.addUserToGroup(gUseru);
	}

	/**
	 * 查询所有
	 * 
	 * @author zhuw
	 * @param username
	 * @return
	 */
	public List<UserInfo> getUser(String username) {
		return userDao.getUser(username);
	}

	/**
	 * 登录验证
	 * 
	 * @author zhuw
	 * @param loginName
	 * @param password
	 * @return
	 */
	public UserInfo getLogin(String loginName, String password) {
		return userDao.getLogin(loginName, password);
	}

	/**
	 * 
	 * @author zhuw
	 * @param username
	 * @return
	 */
	public String getPassword(String username) {
		return userDao.getPassword(username);
	}

	/**
	 * 根据组ID查找系统中可用用户
	 * 
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public List<UserInfo> findAllUserByGroupId(String id) {
		return userDao.findAllUserByGroupId(id);

	}

	/**
	 * 根据ID 删除用户
	 * 
	 * @author zhuw
	 * @param ids
	 */
	public void delUser(String[] ids) {
		for (String id : ids) {
			userDao.delUser(id);
		}
	}

	/**
	 * 密码修改
	 * 
	 * @author morrison
	 * @time 2013-11-11 - 11:54
	 * @param userInfo
	 * @return
	 */
	public void editPwd(String userId, String password) {
		userDao.editPwd(userId, password);
	}

	public void modifyBaseUserInfo(UserInfo userInfo) {
		userDao.modifyUser(userInfo);
	}

	public UserInfo getUserById(String id) {
		return userDao.getUserById(id);
	}

	public boolean checkUserNameIsUse(String userName) {
		List<UserInfo> users = userDao.findUserByUserName(userName);
		if (CollectionUtils.isNotEmpty(users)) {
			return false;
		}
		return true;
	}

	public boolean checkPhoneIsUse(String phone) {
		List<UserInfo> users = userDao.findUserByPhone(phone);
		if (CollectionUtils.isNotEmpty(users)) {
			return false;
		}
		return true;
	}
}
