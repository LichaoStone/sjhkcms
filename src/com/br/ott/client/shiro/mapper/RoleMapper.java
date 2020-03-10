package com.br.ott.client.shiro.mapper;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.br.ott.client.shiro.entity.Role;

/**
 * 持久层处理接口
 * This class is used for ... 
 * @author zhuw
 * @version 1.0 2012-8-3
 */
public interface RoleMapper {
	/**
	 * 添加 Role
	 * @param eNote [ENote对象]
	 */
	public void addRole(Role role);

	/**
	 * 修改
	 * @author zhuw
	 * @param role
	 */
	public void modifyRole(Role role);

	/**
	 * 根据ID修改
	 * @author zhuw
	 * @param id
	 * @return
	 */
	public String modifyRoleById(String id);

	/**
	 * 删除
	 * @author zhuw
	 * @param ids
	 */
	public void delRole(String ids);

	/**
	 * 分页查询
	 * @author zhuw
	 * @param role
	 * @param rowBounds
	 * @return
	 */
	public List<Role> findAllRole(Role role, RowBounds rowBounds);

	/**
	 * 查询总记录数
	 * @author zhuw
	 * @param role
	 * @return
	 */
	public int getCountRole(Role role);

	public Role getRoleById(String id);

	public Role getResourByRoleId(String id);

	public List<Role> getRoleList();

	public List<Role> getRoleByIdList(String id);

	public List<Role> findAllRoleByGroupId(String id);

	public Role getRole(String rolename);

	/**
	 * 按组集合查询角色信息
	 * @param groupId
	 * @return
	 */
	List<Role> findRoleByGroupIds(List<String> groupIds);
	
	public List<Role> checkName(String rname);
	
	public List<Role> checkEname(String enName);
	
	
}
