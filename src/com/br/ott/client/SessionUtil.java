package com.br.ott.client;

import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import com.br.ott.Global;
import com.br.ott.client.shiro.entity.Role;
import com.br.ott.client.shiro.entity.UserInfo;
import com.br.ott.common.util.Constants;

/**
 * 全局公用缓存(包括Session存取)
 * 
 * @author 陈登民
 * 
 */
public class SessionUtil {

	/**
	 * 获取当前登录用户
	 * 
	 * @author zhuw
	 * @return
	 */
	public static final UserInfo getCurrentUser() {
		Subject currentUser = SecurityUtils.getSubject();
		UserInfo user = null;
		if (null != currentUser) {
			Session session = currentUser.getSession();
			if (null != session) {
				user = (UserInfo) session.getAttribute(Global.CURRENT_USER);
				if (null != user) {
					return user;
				}
			}
		}
		return user;
	}

	/**
	 * 更新当前登录用户
	 * 
	 * @author zhuw
	 * @return
	 */
	public static final void setCurrentUser(UserInfo info) {
		Subject currentUser = SecurityUtils.getSubject();
		if (null != currentUser) {
			Session session = currentUser.getSession();
			if (null != session) {
				session.setAttribute(Global.CURRENT_USER, info);
			}
		}
	}

	/**
	 * 获取当前登录用户角色
	 * 
	 * @author zhuw
	 * @return
	 */
	public static final List<Role> getCurrentUserRole() {
		@SuppressWarnings({ "unchecked" })
		List<Role> roles = (List<Role>) SecurityUtils.getSubject().getSession()
				.getAttribute(Constants.USER_ROLE);
		return roles;
	}

	public static final void setCurrentUserRole(List<Role> roles) {
		Session session = SecurityUtils.getSubject().getSession();
		if (null != session) {
			session.setAttribute(Constants.USER_ROLE, roles);
		}
	}

	/**
	 * 取得用户操作仓库（地市） 创建人：pananz 创建时间：2013-3-25 - 下午4:28:17
	 * 
	 * @return 返回类型：List<String>
	 * @exception throws
	 */
	public static final List<String> getCurrentUserArea() {
		@SuppressWarnings("unchecked")
		List<String> nodes = (List<String>) SecurityUtils.getSubject()
				.getSession().getAttribute(Constants.USER_NODE);
		return nodes;
	}

	/**
	 * 设置用户操作仓库（地市） 创建人：pananz 创建时间：2013-3-25 - 下午4:28:53
	 * 
	 * @param nodes
	 *            返回类型：void
	 * @exception throws
	 */
	public static final void setCurrentUserArea(List<String> nodes) {
		Session session = SecurityUtils.getSubject().getSession();
		if (null != session) {
			session.setAttribute(Constants.USER_NODE, nodes);
		}
	}

	/**
	 * 获取登录的用户名 创建人：陈登民 创建时间：2012-12-28 - 上午9:59:22
	 * 
	 * @return 返回类型：String
	 * @exception throws
	 */
	public static final String getLoginName() {
		UserInfo info = getCurrentUser();
		if (info != null) {
			return info.getUserName();
		} else {
			return "";
		}
	}

	/**
	 * 取得登陆用户对应运营商
	 * 
	 * 创建人：pananz 创建时间：2016-8-19
	 * 
	 * @param @return 返回类型：String
	 * @exception throws
	 */
	public static final String getOperators() {
		UserInfo info = getCurrentUser();
		if (info != null) {
			return info.getOperators();
		} else {
			return "";
		}
	}
}
