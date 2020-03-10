package com.br.ott.client.shiro.mapper;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.br.ott.client.shiro.entity.Groups;

/**
 * 持久层处理接口
 * This class is used for ... 
 * @author zhuw
 * @version 1.0 2012-8-3
 */
public interface GroupMapper {
	/**
	 * 查询总记录数
	 * @author zhuw
	 * @param groups
	 * @return
	 */
	public int getCountGroup(Groups groups);
	/**
	 * 分页查询
	 * @author zhuw
	 * @param groups
	 * @param rowBounds
	 * @return
	 */
	public List<Groups> findAllGroup(Groups groups, RowBounds rowBounds);
	
    /**
     * 添加一个分组
     * @param eNote [ENote对象]
     */
    public void addGroup(Groups groups);
    
    public Groups getGroupById(String id);
    
    public List<Groups> checkName(String rname);
    
    public List<Groups> checkenName(String enName);
    
    public Groups getRoleByGroupId(String id);
    
    /**
     * 修改
     * @author zhuw
     * @param role
     */
    public void modifyGroup(Groups groups);
    
    /**
     * 删除
     * @author zhuw
     * @param ids
     */
    public void delGroup(String ids);
    
    /**
     * 查询所有
     * @author zhuw
     * @return
     */
    public List<Groups> getAllGroup();
    
    public List<Groups> getRoleByIdList(String id);
    
    public List<Groups> getGroupByIdList(String id);
    
    public List<Groups> getGroupByUserId(String id);
    
    public Groups getGroup(String groupName);
    
    public Groups findGroupByEnName(String name);
}
