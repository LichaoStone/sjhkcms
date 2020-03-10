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

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.br.ott.Global;
import com.br.ott.client.SessionUtil;
import com.br.ott.client.shiro.entity.Groups;
import com.br.ott.client.shiro.entity.Resources;
import com.br.ott.client.shiro.entity.Role;
import com.br.ott.client.shiro.entity.UserInfo;
import com.br.ott.client.shiro.mapper.UsersMapper;
import com.br.ott.common.util.Constants;

/**
 * Shiro 权限的认证类 This class is used for ...
 * 
 * @author zhuw
 * @version 1.0 2012-7-25
 */
public class ShiroRealm extends AuthorizingRealm {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(ShiroRealm.class);

	@Autowired
	private UsersMapper usersMapper;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ResourcesService resourcesService;

	/**
	
	public ShiroRealm() {
		super();
		// TODO Auto-generated constructor stub
		//设置认证token的实现类
		setAuthenticationTokenClass(UsernamePasswordToken.class);
		//设置加密算法
		setCredentialsMatcher(new HashedCredentialsMatcher(Sha1Hash.ALGORITHM_NAME));
	}
	 * 
	 */

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (principals == null) {
			throw new AuthorizationException("PrincipalCollection 方法参数不能为null.");
		}
		/* 这里编写授权代码 */
		//Set<Groups> userGroups = userInfo.getGroups();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		UserInfo userInfo = SessionUtil.getCurrentUser();
		if (userInfo != null) {
			List<Role> roles = SessionUtil.getCurrentUserRole();
			if (CollectionUtils.isNotEmpty(roles)) {
				for (Role role : roles) {
					info.addRole(role.getRoleName());
					info.addRole(role.getEnname());
				}
			}
			return info;
		}
		return null;
	}

	/**
	 * 认证回调函数,登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		/* 这里编写认证代码 */
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String userName = token.getUsername();
		if (userName != null && !"".equals(userName)) {
			UserInfo userInfo = usersMapper.getLogin(token.getUsername(), String.valueOf(token.getPassword()));
			if (userInfo != null) {
				Set<Groups> userGroups = userInfo.getGroups();
				List<Role> roles = roleService.findRoleByGroupIds(userGroups);
				List<Resources> resourceList = resourcesService.findResourcesByRoleIds(roles);
				
				SecurityUtils.getSubject().getSession().setAttribute(Constants.USER_ROLE, roles);
				SecurityUtils.getSubject().getSession().setAttribute(Constants.USER_RESOURCE, resourceList);
				userInfo.setSuperman(isSuperman(roles));
				
				SecurityUtils.getSubject().getSession().setAttribute(Global.CURRENT_USER, userInfo);
				return new SimpleAuthenticationInfo(userInfo.getUserId(), userInfo.getPassword(), getName());
			} else {
				return null;
			}
		}
		return null;

	}

	public boolean isSuperman(List<Role> roles){
		boolean flag=false;
		for(Role role: roles){
			if("ADMIN".equals(role.getEnname())){
				flag= true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 更新用户授权信息缓存
	 * 
	 * @author zhuw
	 * @param principal
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 
	 * 清除所有用户授权信息缓存.
	 */

	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

}
