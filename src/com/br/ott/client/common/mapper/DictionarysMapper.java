package com.br.ott.client.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.br.ott.client.common.entity.Dictionary;

/**
 * 持久层处理接口
 * 
 * @author cailz
 * @version 2012-8-7
 */
public interface DictionarysMapper {

	/**
	 * 增加系统字典
	 * 
	 * @param dictionarys
	 */
	public void addDictionarys(Dictionary dictionary);

	/**
	 * 分页查询
	 * 
	 * @param dictionarys
	 * @param rowBounds
	 * @return
	 */
	public List<Dictionary> findAllDictionarys(Dictionary dictionary,
			RowBounds rowBounds);

	/**
	 * 删除系统字典
	 * 
	 * @param ids
	 */
	public void delDictionarys(String ids);

	/**
	 * 查看系统字典详情
	 * 
	 * @param dictionarys
	 */
	public Dictionary findByDictById(String id);

	/**
	 * 查询总的记录条数
	 * 
	 * @param dictionary
	 * @return
	 */
	int findCountDictionarys(Dictionary dictionary);

	/**
	 * 字典分类
	 */
	public List<Dictionary> getDictclassification();

	/**
	 * 删除check选中的数据
	 * 
	 * @param checkIds
	 */
	public void delDictionaryType(List<String> ids);

	/**
	 * 验证输入的常量名是否存在
	 * 
	 * @param name
	 * @return
	 */
	public List<Dictionary> findDictByName(String name);

	/**
	 * 查询字典 创建人：pananz 创建时间：2014-9-30 - 上午11:40:05
	 * 
	 * @param dict
	 * @return 返回类型：List<Dictionary>
	 * @exception throws
	 */
	public List<Dictionary> findDictByCond(Dictionary dict);

	/**
	 * 验证输入的常量类型值是否存在
	 * 
	 * @param dicType
	 * @return
	 */
	public List<Dictionary> findDictByValue(String dicValue);

	/**
	 * 得到所有的数据，用于重新加载缓存
	 * 
	 * @return
	 */
	public List<Dictionary> findAllDictionarys(Dictionary dict);

	/**
	 * 最大值的二级数据dicValue值
	 */
	public int findMaxKey();

	/**
	 * 修改字典数据子类数据状态
	 */
	public void modifyChildStatus(@Param("status") String status,
			@Param("dicType") String dicType);
	/**
	 * 修改字典数据状态
	 */
	public void modifyDictStatus(@Param("status") String status,
			@Param("id") String id);

	public int[] insertDict(List<Dictionary> list);

	/**
	 * 修改字典数据 创建人：pananz 创建时间：2014-10-9 - 下午5:27:42
	 * 
	 * @param dictionary
	 *            返回类型：void
	 * @exception throws
	 */
	void updateDictionary(Dictionary dictionary);
	
	void delDictionaryList(List<String> ids);

}
