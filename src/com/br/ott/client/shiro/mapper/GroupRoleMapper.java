package com.br.ott.client.shiro.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.shiro.entity.GroupRole;

/**
 * 持久层处理接口 This class is used for ...
 * 
 * @author zhuw
 * @version 1.0 2012-8-3
 */

public interface GroupRoleMapper {
	/**
	 * 添加 Role
	 * 
	 * @param eNote
	 *            [ENote对象]
	 */
	public void addRoleToGroup(GroupRole gr);

	public void delGroupRole(@Param("groupid") String ids,
			@Param("roleid") String roleid);

	/**
	 * 按组查询组角色关联信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 上午11:45:44
	 * 
	 * @param groupid
	 * @return 返回类型：List<GroupRole>
	 * @exception throws
	 */
	List<GroupRole> findGroupRoleByGroupId(String groupid);

	/**
	 * 按角色查询组角色关联信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 上午11:46:07
	 * 
	 * @param roleid
	 * @return 返回类型：List<GroupRole>
	 * @exception throws
	 */
	List<GroupRole> findGroupRoleByRoleId(String roleid);

	/**
	 * 按组与角色查询组角色关联信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 上午11:46:16
	 * 
	 * @param groupid
	 * @param roleid
	 * @return 返回类型：GroupRole
	 * @exception throws
	 */
	GroupRole getGroupRoleByRidAndGid(@Param("groupid") String groupid,
			@Param("roleid") String roleid);
}
