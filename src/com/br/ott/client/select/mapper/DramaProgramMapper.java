package com.br.ott.client.select.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.select.entity.DramaProgram;

/* 
 *  
 *  文件名：DramaProgramMapper.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-9 - 下午5:16:39
 */
public interface DramaProgramMapper {

	/**
	 * 
	 * 增加资产点播节目单 创建人：pananz 创建时间：2015-3-9 - 下午5:19:00
	 * 
	 * @param dramaProgram
	 *            返回类型：void
	 * @exception throws
	 */
	void addDramaProgram(DramaProgram dramaProgram);

	/**
	 * 修改资产点播节目单
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 下午5:19:04
	 * 
	 * @param dramaProgram
	 *            返回类型：void
	 * @exception throws
	 */
	void updateDramaProgram(DramaProgram dramaProgram);

	/**
	 * 按ID查询资产点播节目单
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 下午5:19:08
	 * 
	 * @param id
	 * @return 返回类型：dramaProgram
	 * @exception throws
	 */
	DramaProgram getDramaProgramById(String id);

	/**
	 * 按ID和STATUS查询资产点播节目单
	 * 
	 * 创建人：lizhenghg 创建时间：2016-11-28 - 上午10:07:08
	 * 
	 * @param id
	 * @return 返回类型：dramaProgram
	 * @exception throws
	 */
	DramaProgram getDramaProgramByIdAndStatus(@Param("id")String id, @Param("status")String status);
	
	/**
	 * 按分页查询资产点播节目单
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 下午5:19:12
	 * 
	 * @param dramaProgram
	 * @return 返回类型：List<DramaProgram>
	 * @exception throws
	 */
	List<DramaProgram> findDramaProgramByPage(DramaProgram dramaProgram);

	/**
	 * 按分页查询资产点播节目单
	 * 
	 * 创建人：lizhenghg 创建时间：2018-1-5 - 下午5:19:12
	 * 
	 * @param dramaProgram
	 * @return 返回类型：List<DramaProgram>
	 * @exception throws
	 */
	List<DramaProgram> findDramaProgramByPage2(DramaProgram dramaProgram);
	
	int getCountDramaProgram(DramaProgram dramaProgram);
	

	/**
	 * 按条件查询资产点播节目单
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 下午5:19:15
	 * 
	 * @param dramaProgram
	 * @return 返回类型：List<DramaProgram>
	 * @exception throws
	 */
	List<DramaProgram> findDramaProgramByCond(DramaProgram dramaProgram);

	/**
	 * 修改资产点播节目单状态
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 下午5:19:19
	 * 
	 * @param status
	 * @param id
	 *            返回类型：void
	 * @exception throws
	 */
	void updateDramaProgramStatus(@Param("status") String status,
			@Param("id") String id);

	void updateProgramClickNum(@Param("id") String id);	
	/**
	 * 更新图片资源
	 * 
	 * 创建人：pananz 创建时间：2016-9-22
	 * 
	 * @param @param dramaProgram 返回类型：void
	 * @exception throws
	 */
	void updateDramaImage(DramaProgram dramaProgram);

	/**
	 * 通过DramaProgramName获取记录
	 * 
	 * 创建人：lizhenghg 创建时间：2016-9-28
	 * 
	 * @param DramaProgramName
	 *            返回类型：boolean
	 * @exception throws
	 */
	public List<DramaProgram> findDPListByName(String DPName);

	/**
	 * 通过dramId和navId集合插入数据到ott_drama_type
	 * 
	 * 创建人：lizhenghg 创建时间：2016-9-28
	 * 
	 * @param Map
	 *            返回类型：void
	 * @exception throws
	 */
	@SuppressWarnings("rawtypes")
	public void insertDramaTypeByMap(Map map);

	/**
	 * 批量上线/下载资产
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-09
	 * 
	 * @param Map
	 *            返回类型：void
	 * @exception throws
	 */
	@SuppressWarnings("rawtypes")
	public void updateDPStatusForBatch(Map map);

	/**
	 * 获取推荐资产信息(每隔5分钟加载一次推荐资产信息到缓存)
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-12
	 * 
	 * @param void 返回类型：List<DramaProgram>
	 * @exception throws
	 */
	@SuppressWarnings("rawtypes")
	public List<DramaProgram> findRecDramaByCloud(Map map);

	/**
	 * 获取推荐资产信息id集合
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-12
	 * 
	 * @param void 返回类型：List
	 * @exception throws
	 */
	public List<Map<String, Object>> findDramaIdsAndSorts();

	/**
	 * 更新资产简拼信息
	 * 
	 * 创建人：pananz 创建时间：2016-10-24
	 * 
	 * @param @param pcode
	 * @param @param id 返回类型：void
	 * @exception throws
	 */
	void updateDramaProgramJP(@Param("pcode") String pcode,
			@Param("id") String id);
	
	/**
	 * 手机IPTV获取资产列表(非分页)
	 * @author lizhenghg  创建时间：2017-7-22
	 * @return List<DramaProgram>
	 */
	List<DramaProgram> findDramaProgramForAPI(DramaProgram dramaProgram);
	
	
	/**
	 * 更新资产图片路径
	 * 
	 * @author lizhenghg 创建时间：2017-5-11
	 * @param @param dp
	 * @exception throws
	 */
	void updateDramaImg(DramaProgram dp);
}
