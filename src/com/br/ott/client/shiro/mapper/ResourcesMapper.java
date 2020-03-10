package com.br.ott.client.shiro.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.br.ott.client.shiro.entity.Resources;

/**
 * 持久层处理接口
 * This class is used for ... 
 * @author zhuw
 * @version 1.0 2012-8-3
 */
public interface ResourcesMapper {
	/**
	 * 添加一个Role
	 * @param eNote [ENote对象]
	 */
	public void addResour(Resources res);

	/**
	 * 查询总记录数
	 * @author zhuw
	 * @param groups
	 * @return
	 */
	public int getCountResour(Resources res);

	/**
	 * 分页查询
	 * @author zhuw
	 * @param groups
	 * @param rowBounds
	 * @return
	 */
	public List<Resources> findAllResour(Resources res, RowBounds rowBounds);

	/**
	 * 修改
	 * @author zhuw
	 * @param role
	 */
	public void modifyResour(Resources res);

	/**
	 * 删除
	 * @author zhuw
	 * @param ids
	 */
	public void delResour(String ids);

	/**
	 * <查询所有>
	 * @return [参数说明]
	 * @return List<ENoteUser> [用户对象]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public List<Resources> getAllResour();

	public Resources getResourById(String id);

	public List<Resources> getChekEnname(String rname);

	public List<Resources> checkName(String rname);

	public List<Resources> findAllResourByRoleId(String id);

	public List<Resources> findAllResourList();

	public List<Resources> findAllResourByIdList(@Param("id")
	String id, @Param("resId")
	String resId);

	public String getResourId(String systemId);

	/**
	 * 按角色集合查询资源信息
	 * @param roleIds
	 * @return
	 */
	List<Resources> findResourcesByRoleIds(List<String> roleIds);
}
