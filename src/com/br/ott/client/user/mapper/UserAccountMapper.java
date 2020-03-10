package com.br.ott.client.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.br.ott.client.user.entity.UserAccount;

public interface UserAccountMapper {
	/**
	 * 查找用户
	 * @param user 查询条件
	 * @return
	 */
	List<UserAccount> findUsers(UserAccount user, RowBounds rowBounds);

	/**
	 * 添加用户
	 * @param user
	 */
	void addUser(UserAccount user);

	/**
	 * 根据条件查询满足条件的总记录数
	 * @param user
	 * @return
	 */
	int findCountUsers(UserAccount user);

	/**
	 * 删除用户
	 * @param id
	 */
	void delUser(@Param("id")
	String id);

	/**
	 * 查找用户详情
	 * @param id
	 * @return
	 */
	UserAccount getUserAccount(@Param("id")
	String id);

	/**
	 * 按IPTV帐号查询用户信息
	 * @param userId
	 * @return
	 */
	UserAccount getUserAccountByUserId(String userId);

	/**
	 * 修改用户信息
	 * @param account
	 */
	void modifyUserAccount(UserAccount account);

	/**
	 * 修改儿童锁密码
	 * @param account
	 */
	void updateChildPwd(UserAccount account);

	/**
	 * 验证儿童锁的密码
	 * @param account
	 * @return
	 */
	List<UserAccount> getCheckPwd(UserAccount account);

	/**
	 * 用户修改为平台会员
	 * @param isMember
	 * @param userId
	 */
	void modifyToMember(@Param("isMember")
	String isMember, @Param("userId")
	String userId);

	/**
	 * 条件查询用户账号信息
	 * 创建人：pananz
	 * 创建时间：2014-7-4 - 上午10:16:42
	 * @param account
	 * @return
	 * 返回类型：List<UserAccount>
	 * @exception throws
	 */
	List<UserAccount> findUserAccountByCond(UserAccount account);

	UserAccount checkLogin(@Param("account")
	String account, @Param("pwd")
	String pwd);
}
