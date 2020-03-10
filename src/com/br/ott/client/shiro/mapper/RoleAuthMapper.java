package com.br.ott.client.shiro.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.shiro.entity.RoleAuth;

/**
 * 角色权限持久层处理接口
 * This class is used for ... 
 * @author zhuw
 * @version 1.0 2012-8-3
 */
public interface RoleAuthMapper {

	/**
	 * 添加一个Role
	 * @param eNote [ENote对象]
	 */
	void addAuthToRole(RoleAuth gr);

	/**
	 * 按角色ID及资源ID查找角色权限
	 * @param roleId
	 * @param resId
	 * @return
	 */
	RoleAuth findRoleAuth(@Param("roleId")
	String roleId, @Param("resId")
	String resId);

	/**
	 * 按角色ID及资源ID删除角色权限
	 * @param resourceId
	 * @param roleId
	 */
	void delResourToRole(@Param("resourceId")
	String resourceId, @Param("roleId")
	String roleId);

	/**
	 * 按角色ID查找角色权限
	 * @param roleId
	 * @return
	 */
	List<RoleAuth> findRoleAuthByRoleId(@Param("roleId")
	String roleId);

	/**
	 * 按角色ID删除角色权限
	 * @param roleId
	 */
	void deleteRoleAuthByRoleId(@Param("roleId")
	String roleId);
}
