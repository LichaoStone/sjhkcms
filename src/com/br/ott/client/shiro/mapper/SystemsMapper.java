package com.br.ott.client.shiro.mapper;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.br.ott.client.shiro.entity.Systems;

/**
 * 持久层处理接口
 * This class is used for ... 
 * @author zhuw
 * @version 1.0 2012-8-3
 */
public interface SystemsMapper {
    /**
     * 添加
     * @param eNote
     */
    public void addSystem(Systems systems);
    
    /**
     * 修改
     * @author zhuw
     * @param id
     */
  //  public void modifySystem(String id);
    
    /**
     * 删除
     * @author zhuw
     * @param id
     */
    public void delSystem(String id);
    
    /**
     * 修改
     * @author zhuw
     * @param systems
     */
    public void modifySystem(Systems systems);
    
    /**
     * 查询所有
     * @author zhuw
     * @return
     */
    public List<Systems> getAllSystem();
    
    /**
     * 查询总条数
     * @author zhuw
     * @param systems
     * @return
     */
    public int getCountSystem(Systems systems);
    
    /**
     * 分页查询
     * @author zhuw
     * @param role
     * @param rowBounds
     * @return
     */
    public List<Systems> findAllSystem(Systems systems, RowBounds rowBounds);
    
    /**
     * 根据ID查找
     * @author zhuw
     * @param id
     * @return
     */
    public Systems getSystemById(String id);
    
    /**
     * 查询所有
     * @author zhuw
     * @return
     */
    public List<Systems> getSystemList();
    
}
