package com.br.ott.client.shiro.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.shiro.entity.GroupUser;

/**
 * 持久层处理接口 This class is used for ...
 * 
 * @author zhuw
 * @version 1.0 2012-8-3
 */

public interface GroupUserMapper {
	/**
	 * 添加 Role
	 * 
	 * @param eNote
	 *            [ENote对象]
	 */
	public void addUserToGroup(GroupUser guUser);

	public void delUserToGroup(@Param("ids") String ids,
			@Param("gid") String gid);

	public void delGroupToUser(@Param("ids") String ids,
			@Param("uId") String uId);

	/**
	 * 按用户ID称删除用户组关联信息 创建人：pananz 创建时间：2013-10-13 - 下午7:26:34
	 * 
	 * @param userId
	 *            返回类型：void
	 * @exception throws
	 */
	void delGroupUserByUserId(String userId);

	/**
	 * 按组查询关联用户信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 上午10:51:20
	 * 
	 * @param groupId
	 * @return 返回类型：List<GroupUser>
	 * @exception throws
	 */
	List<GroupUser> findGroupUserByGroupId(String groupId);

	/**
	 * 按用户查询关联信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 上午10:51:38
	 * 
	 * @param userId
	 * @return 返回类型：List<GroupUser>
	 * @exception throws
	 */
	List<GroupUser> findGroupUserByUserId(String userId);

	/**
	 * 按组与用户查询关联信息
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 上午11:30:49
	 * 
	 * @param groupId
	 * @param userId
	 * @return 返回类型：GroupUser
	 * @exception throws
	 */
	GroupUser getGroupUserByGIdAndUId(@Param("groupid") String groupid,
			@Param("userid") String userid);
}
