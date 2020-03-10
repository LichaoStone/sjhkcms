package com.br.ott.client.shiro.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.br.ott.client.shiro.entity.UserInfo;

/**
 * 持久层处理接口 This class is used for ...
 * 
 * @author zhuw
 * @version 1.0 2012-8-3
 */
public interface UsersMapper {

	/**
	 * 分页查询
	 * 
	 * @author zhuw
	 * @param role
	 * @param rowBounds
	 * @return
	 */
	public List<UserInfo> findAllUser(UserInfo userInfo, RowBounds rowBounds);

	/**
	 * 查询总记录数
	 * 
	 * @author zhuw
	 * @param role
	 * @return
	 */
	public int getCountUser(UserInfo userInfo);

	public List<UserInfo> getUser(String username);

	public List<UserInfo> getUserList();

	public String getPassword(String username);

	public UserInfo getLogin(@Param("loginName") String loginName,
			@Param("password") String password);

	public List<UserInfo> findAllUserByGroupId(String groupid);

	/**
	 * 添加一个用户
	 * 
	 */
	public void addUser(UserInfo user);

	/**
	 * 修改
	 * 
	 * @author zhuw
	 * @param role
	 */
	public void modifyUser(UserInfo user);

	public void delUser(String id);

	public UserInfo getUserById(String id);

	/**
	 * 按用户名查询信息 创建人：pananz 创建时间：2013-3-26 - 下午4:27:32
	 * 
	 * @param userName
	 * @return 返回类型：List<UserInfo>
	 * @exception throws
	 */
	public List<UserInfo> findUserByUserName(String userName);

	/**
	 * 按手机查询信息 创建人：pananz 创建时间：2013-3-26 - 下午4:27:43
	 * 
	 * @param phone
	 * @return 返回类型：List<UserInfo>
	 * @exception throws
	 */
	public List<UserInfo> findUserByPhone(String phone);

	/**
	 * 按partnerId查询用户
	 * 
	 * @param partnerId
	 * @return
	 */
	UserInfo getUserInfoByPartnerId(String partnerid);

	/**
	 * 修改密码
	 * 
	 * @param userInfo
	 */
	void editPwd(@Param("userId") String userId,
			@Param("password") String password);
}
