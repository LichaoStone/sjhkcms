package com.br.ott.client.select.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.br.ott.Global;
import com.br.ott.client.common.DictCache;
import com.br.ott.client.common.NavigationCache;
import com.br.ott.client.common.RecDramaCache;
import com.br.ott.client.common.entity.Dictionary;
import com.br.ott.client.common.service.CommonService;
import com.br.ott.client.common.service.OperaLogService;
import com.br.ott.client.select.entity.DramaNavigation;
import com.br.ott.client.select.entity.DramaProgram;
import com.br.ott.client.select.entity.DramaType;
import com.br.ott.client.select.mapper.DramaProgramMapper;
import com.br.ott.client.select.mapper.DramaProgramMapper2;
import com.br.ott.client.select.mapper.DramaTypeMapper;
import com.br.ott.client.shiro.entity.UserInfo;
import com.br.ott.common.util.ExcelUtil;
import com.br.ott.common.util.SSOUtil;
import com.br.ott.common.util.StringUtil;

/* 
 *  资产点播节目业务接口
 *  文件名：DramaProgramService.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-9 - 下午5:36:12
 */
@Service
@SuppressWarnings("unused")
public class DramaProgramService {
	private static final Logger logger = Logger
			.getLogger(DramaProgramService.class);

	@Autowired
	private DramaProgramMapper dramaProgramMapper;
	
	@Autowired
	private DramaProgramMapper2 dramaProgramMapper2;
	
	@Autowired
	private DramaTypeMapper dramaTypeMapper;
	
	@Autowired
	private OperaLogService operaLogService;

	private static final String BUSI_NAME_BATCH = "批量导入资产信息";
	protected final static String OPERA_TYPE_BATCH_ADD = "4";

	/**
	 * 
	 * 增加点播节目单 创建人：pananz 创建时间：2015-3-9 - 下午5:19:00
	 * 
	 * @param DramaProgram
	 *            返回类型：void
	 * @exception throws
	 */
	public void addDramaProgram(DramaProgram dramaProgram) {
		dramaProgramMapper.addDramaProgram(dramaProgram);
		if (StringUtil.isNotEmpty(dramaProgram.getTtype())) {
			List<String> newList = Arrays.asList(dramaProgram.getTtype().split(
					","));
			if (CollectionUtils.isNotEmpty(newList)) {
				for (String navId : newList) {
					DramaType dt = new DramaType(dramaProgram.getId(), navId);
					dramaTypeMapper.addDramaType(dt);
				}
			}
			newList = null;
		}
	}

	/**
	 * 修改点播节目单
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 下午5:19:04
	 * 
	 * @param DramaProgram
	 *            返回类型：void
	 * @exception throws
	 */
	public void updateDramaProgram(DramaProgram dramaProgram, String oldType) {
		dramaProgramMapper.updateDramaProgram(dramaProgram);
		if (StringUtil.isNotEmpty(dramaProgram.getTtype())) {
			List<String> newList = Arrays.asList(dramaProgram.getTtype().split(
					","));
			List<String> oldList = Arrays.asList(oldType.split(","));
			if (!oldList.containsAll(newList)) {
				dramaTypeMapper.delDramaTypeByDramaId(dramaProgram.getId());
				if (CollectionUtils.isNotEmpty(newList)) {
					for (String navId : newList) {
						DramaType dt = new DramaType(dramaProgram.getId(),
								navId);
						dramaTypeMapper.addDramaType(dt);
					}
				}
			}
			newList = null;
			oldList = null;
		}
	}

	/**
	 * 按ID查询点播节目单
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 下午5:19:08
	 * 
	 * @param id
	 * @return 返回类型：DramaProgram
	 * @exception throws
	 */
	public DramaProgram getDramaProgramById(String id) {
		return dramaProgramMapper.getDramaProgramById(id);
	}

	/**
	 * 按分页查询点播节目单
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 下午5:19:12
	 * 
	 * @param DramaProgram
	 * @return 返回类型：List<DramaProgram>
	 * @exception throws
	 */
	public List<DramaProgram> findDramaProgramByPage(DramaProgram dramaProgram) {
		int totalResult = dramaProgramMapper.getCountDramaProgram(dramaProgram);
		dramaProgram.setTotalResult(totalResult);
		return dramaProgramMapper.findDramaProgramByPage(dramaProgram);
	}

	/**
	 * 按分页查询点播节目单
	 * 
	 * 创建人：lizhenghg 创建时间：2018-01-05 - 下午5:19:12
	 * 
	 * @param DramaProgram
	 * @return 返回类型：List<DramaProgram>
	 * @exception throws
	 */
	public List<DramaProgram> findDramaProgramByPage2(DramaProgram dramaProgram) {
		int totalResult = this.dramaProgramMapper2.getCountDramaProgram(dramaProgram);  //使用mybatis的二级缓存
		dramaProgram.setTotalResult(totalResult);
		int page = dramaProgram.getCurrentPage();
		int limit = dramaProgram.getShowCount();
		RowBounds rowBounds = new RowBounds((page - 1) * limit, limit);
		return this.dramaProgramMapper2.findDramaProgram(dramaProgram, rowBounds);                 //使用mybatis的二级缓存
	}
	
	
	/**
	 * 按条件查询点播节目单
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 下午5:19:15
	 * 
	 * @param DramaProgram
	 * @return 返回类型：List<DramaProgram>
	 * @exception throws
	 */
	public List<DramaProgram> findDramaProgramByCond(DramaProgram dramaProgram) {
		return dramaProgramMapper.findDramaProgramByCond(dramaProgram);
	}

	/**
	 * 修改点播节目单状态
	 * 
	 * 创建人：pananz 创建时间：2015-3-9 - 下午5:19:19
	 * 
	 * @param status
	 * @param id
	 *            返回类型：void
	 * @exception throws
	 */
	public void updateDramaProgramStatus(String status, String id) {
		dramaProgramMapper.updateDramaProgramStatus(status, id);
	}

	/**
	 * 下线资产
	 * 
	 * 创建人：pananz 创建时间：2016-9-13
	 * 
	 * @param @param status
	 * @param @param id
	 * @param @param assetId
	 * @param @param msg 返回类型：void
	 * @exception throws
	 */
	public void closeDramaProgram(String status, String id, String assetId,
			String msg) {
		dramaProgramMapper.updateDramaProgramStatus(status, id);
		// 同步信息到运营商
		// String params = "{\"assetId\":\"" + assetId + "\", \"msg\":\"" + msg
		// + "\"}";
		// String jsonStr = SSOUtil.postRequestByUTF8(Global.HS_ASSET_OFF_URL,
		// params);
		// logger.debug("result:" + jsonStr);
		// if (StringUtil.isEmpty(jsonStr)) {
		// logger.error("下线同步无信息返回");
		// }
	}

	public boolean checkDPName(String name) {
		DramaProgram sp = new DramaProgram();
		sp.setName(name);
		List<DramaProgram> list = findDramaProgramByCond(sp);
		if (CollectionUtils.isNotEmpty(list)) {
			list = null;
			return false;
		}
		return true;
	}

	public boolean checkAssetId(String assetId) {
		DramaProgram sp = new DramaProgram();
		sp.setAssetId(assetId);
		List<DramaProgram> list = findDramaProgramByCond(sp);
		if (CollectionUtils.isNotEmpty(list)) {
			list = null;
			return false;
		}
		return true;
	}

	/**
	 * 读取资产excel文件，然后进行导入处理
	 * 
	 * @author lizhenghg 创建时间：2016-09-28
	 * 
	 * @param request
	 *            ,file
	 * @return String
	 * @throws
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String insertDPForExecl(MultipartFile file,
			HttpServletRequest request) {

		List<DramaProgram> dpList = parseExcel(file, request);

		if (CollectionUtils.isEmpty(dpList))
			return "excel内容不能为空";

		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		UserInfo user = (UserInfo) session.getAttribute(Global.CURRENT_USER);
		List<DramaNavigation> DNList = NavigationCache.navList;

		int flag = 1;
		int success = 0;
		int except = 0;
		boolean flags = false;
		StringBuffer sb = new StringBuffer(64);
		StringBuffer sbExcept = new StringBuffer(64);
		StringBuffer logContent = new StringBuffer(512); //分配512个字符，1024个字节，1k内存的空间，默认为16个字符，只要StringBuffer到达它的最大容量它就不得不创建一个新的字符数组然后重新将旧字符和新字符都拷贝一遍，给StringBuffer设置一个合理的初始化容量值是错不了的，这样会带来立竿见影的性能增益
		logContent.append("导入的资产数据如下：\n");
		for (DramaProgram dp : dpList) {
			flag++;
			try {
				dp.setOperator(user.getUserName());
				dp.setIsLoad("0");
				dramaProgramMapper.addDramaProgram(dp);
				String dramaId = dp.getId();
				// 更新数据表ott_drama_type
				List<String> navIdList = new ArrayList<String>();
				if (!CollectionUtils.isEmpty(dp.getDramaTypeNames())) {
					for (String nav : dp.getDramaTypeNames()) {
						String navName = nav;
						for (DramaNavigation dn : DNList) {
							if (dn.getName().equals(navName)) {
								navIdList.add(dn.getId());
							}
						}
					}
				}
				// 把每个DramaProgram对象所对应的所属栏目的id存放在List集合里，插入数据表ott_drama_type
				Map info = new HashMap();
				info.put("tablename", "ott_drama_type");
				info.put("dramaId", dramaId);
				info.put("navIdList", navIdList);
				if (navIdList.size() > 0)
					this.dramaProgramMapper.insertDramaTypeByMap(info);
				logContent.append("资产名：" + dp.getName() + ",导演："
						+ dp.getDirector() + ",关键字：" + dp.getKeyword() + "操作者："
						+ dp.getOperator());
				success++;
				flags = true;
			} catch (Exception e) {
				e.printStackTrace();
				except++;
				sbExcept.append(flag + ",");
			}
		}
		if (flags) {
			// 写入系统操作日志
			operaLogService.addOperaLog(
					OPERA_TYPE_BATCH_ADD,
					request,
					BUSI_NAME_BATCH,
					logContent.toString().substring(0,
							logContent.toString().length() - 1));
		}
		sb.append("共" + dpList.size() + "条数据,");
		if (success > 0) {
			sb.append("成功导入" + success + "条数据,");
		}
		if (except > 0) {
			sb.append("导入失败" + except + "条,第" + sbExcept.toString() + "条数据异常");
		}
		return sb.toString();
	}

	/**
	 * 读取excel文件
	 * 
	 * @author lizhenghg 创建时间：2016-09-28
	 * 
	 * @param MultipartFile
	 *            file, HttpServletRequest request
	 * @return List<DramaProgram>
	 * @throws
	 */
	public List<DramaProgram> parseExcel(MultipartFile file,
			HttpServletRequest request) {
		List<DramaProgram> dpList = new ArrayList<DramaProgram>();
		Map<String, String> linfo = new HashMap<String, String>();
		String cellInfo = null;
		try {
			Workbook wk = WorkbookFactory.create(file.getInputStream());
			// 获取第一个sheet
			Sheet sheet = wk.getSheetAt(0);
			for (int i = 1; i < sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (null == row)
					continue;
				if (row.getCell(0) == null
						|| (row.getCell(0) != null && "".equals(row.getCell(0)
								.toString())))
					continue;
				for (int j = 0; j < row.getLastCellNum(); j++) {
					Cell cell = row.getCell(j);
					if (cell == null)
						linfo.put(String.valueOf(j), "");
					else {
						cellInfo = ExcelUtil.ConvertCellStr(cell);
						linfo.put(String.valueOf(j), cellInfo);
					}
				}
				// 对相应的map进行处理
				DramaProgram dp = getDramaPro(linfo);
				dpList.add(dp);
			}
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dpList;
	}

	/**
	 * 通过枚举，把相应的字段添加到指定的对象属性上
	 * 
	 * @author lizhenghg 创建时间：2016-09-28
	 * 
	 * @param Map
	 *            <String,String>
	 * @return String
	 * @throws
	 */
	public DramaProgram getDramaPro(Map<String, String> map) {
		DramaProgram dp = new DramaProgram();
		Set<String> keySet = map.keySet();
		List<Dictionary> dictList = DictCache.dictList;

		if (map == null || map.size() == 0)
			return dp;

		for (String key : keySet) {

			switch (Integer.parseInt(key)) {
			case 0:
				dp.setName(map.get(key));

				String ptype = map.get("1") == null ? "" : map.get("1");
				String cProvider = map.get("3") == null ? "" : map.get("3");
				String language = map.get("6") == null ? "" : map.get("6");
				String origin = map.get("7") == null ? "" : map.get("7");

				if (dictList != null && dictList.size() > 0) {
					for (Dictionary dictionary : dictList) {
						if (dictionary.getDicName().equals(ptype)) {
							dp.setPtype(dictionary.getDicValue());
						}
						if (dictionary.getDicName().equals(cProvider)) {
							dp.setcProvider(dictionary.getDicValue());
						}
						if (dictionary.getDicName().equals(language)) {
							dp.setLanguage(dictionary.getDicValue());
						}
						if (dictionary.getDicName().equals(origin)) {
							dp.setOrigin(dictionary.getDicValue());
						}
					}
				}
				break;
			case 2:
				Set<String> result = new HashSet<String>();
				String[] parantDramaType = map.get(key).split("\\|");
				if (null != parantDramaType && parantDramaType.length > 0) {
					for (int i = 0; i < parantDramaType.length; i++) {
						String[] sonDramaType = parantDramaType[i].split("-");
						if (sonDramaType != null && sonDramaType.length > 0) {
							for (int j = 1; j < sonDramaType.length; j++) {
								result.add(sonDramaType[j]);
							}
						}
					}
				}
				dp.setDramaTypeNames(result);
				break;
			case 4:
				dp.setActor(map.get(key));
				break;
			case 5:
				dp.setDirector(map.get(key));
				break;
			case 8:
				dp.setKeyword(map.get(key));
				break;
			case 9:
				dp.setAssetId(map.get(key));
				break;
			case 10:
				dp.setOldType(map.get(key));
				break;
			case 11:
				dp.setPcode(map.get(key));
				break;
			case 12:
				dp.setCompere(map.get(key));
				break;
			case 13:
				dp.setPlayyear(map.get(key));
				break;
			case 14:
				dp.setSummary(map.get(key));
				break;
			case 15:
				if ("否".equals(map.get(key)))
					dp.setpId("0");
				else
					dp.setpId("1");
				break;
			case 16:
				dp.setLinkUrl(map.get(key));
				break;
			case 17:
				dp.setRemark(map.get(key));
				break;
			}
		}
		return dp;
	}

	/**
	 * 判断输入的资产名称是否已经存在
	 * 
	 * @author lizhenghg 创建时间：2016-09-28
	 * 
	 * @param DPName
	 * @return String
	 * @throws
	 */
	public boolean findDPByName(String DPName) {
		List<DramaProgram> dpList = this.dramaProgramMapper
				.findDPListByName(DPName);
		if (CollectionUtils.isEmpty(dpList))
			return false;
		return true;
	}

	/**
	 * 批量上线/下线资产
	 * 
	 * @author lizhenghg 创建时间：2016-10-09
	 * 
	 * @param paramsMap
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	public void updateDPStatusForBatch(Map paramsMap) {
		this.dramaProgramMapper.updateDPStatusForBatch(paramsMap);
	}

	/**
	 * 每隔5分钟加载一次推荐资产信息
	 * 
	 * @author lizhenghg 创建时间：2016-10-12
	 * 
	 * @param void
	 * @return void
	 * @throws
	 */
	public void reloadDramaProgram() {
		List<Map<String, Object>> dramidList = this.dramaProgramMapper.findDramaIdsAndSorts();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("dramidList", dramidList);
		List<DramaProgram> dramaList = null;
		if (CollectionUtils.isNotEmpty(dramidList)) dramaList = this.dramaProgramMapper.findRecDramaByCloud(paramsMap);
		
		//获取特定的栏目下面的子资产
		String dpNavId = NavigationCache.getIdByName("大片");
		String dmNavId = NavigationCache.getIdByName("动漫");
		String jcNavId = NavigationCache.getIdByName("剧场");
		
		DramaProgram dramaProgram = new DramaProgram();
		dramaProgram.setStatus("1");
		dramaProgram.setCurrentPage(1);
		dramaProgram.setShowCount(6);
		
		dramaProgram.setParentId(dpNavId);
		List<DramaProgram> dpList = findDramaProgramByPage(dramaProgram);
		dramaProgram.setParentId(dmNavId);
		List<DramaProgram> dmList = findDramaProgramByPage(dramaProgram);
		dramaProgram.setParentId(jcNavId);
		List<DramaProgram> jcList = findDramaProgramByPage(dramaProgram);
		
		Map<String, List<DramaProgram>> dpMap = new HashMap<String, List<DramaProgram>>();
		
		//去掉资产名称重复的资产
		dpList = removeDuplicateObject(dpList, 6, dpNavId);
		dmList = removeDuplicateObject(dmList, 6, dmNavId);
		jcList = removeDuplicateObject(jcList, 6, jcNavId);
 
		dpMap.put(dpNavId, dpList);
		dpMap.put(dmNavId, dmList);
		dpMap.put(jcNavId, jcList);
		
		RecDramaCache.reloadDramaProgram(dramaList);
		RecDramaCache.reloadDramaIdsAndSorts(dramidList);
		RecDramaCache.reloadRecDramaPrograms(dpMap);
		dpList = null;
		dmList = null;
		jcList = null;
		dramidList = null;
		dramaList = null;
	}
	
	/**
	 * 去掉集合中名称重复的资产，并返回指定的资产个数
	 * @author lizhenghg  2017-08-09
	 * @param dpList 集合
	 * @param size 返回资产个数
	 * @param parentId 选择资产依据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DramaProgram> removeDuplicateObject(List<DramaProgram> dpList, int size, String parentId){
		dpList = (List<DramaProgram>) CommonService.removeDuplicateObject(dpList, "Name");
		if(dpList.size() < size){
			List<String> nameList = new ArrayList<String>();
			for(DramaProgram dp : dpList) nameList.add(dp.getName());
			DramaProgram dramaProgram = new DramaProgram();
			dramaProgram.setStatus("1");
			dramaProgram.setCurrentPage(1);
			dramaProgram.setShowCount(30);
			dramaProgram.setParentId(parentId);
			List<DramaProgram> list = findDramaProgramByPage(dramaProgram);
			Iterator<DramaProgram> itor = list.iterator();
			
			while(itor.hasNext()){
				dramaProgram = itor.next();
				if(nameList.contains(dramaProgram.getName()) || StringUtils.isEmpty(dramaProgram.getName())) continue;
				dpList.add(dramaProgram);
				if(dpList.size() == size) break;
			}
			nameList = null;
			list = null;
		}
		return dpList;
	}
	
	
	public void resetDramaProgramOfJP() {
		List<DramaProgram> list = dramaProgramMapper
				.findDramaProgramByCond(new DramaProgram());
		for (DramaProgram dp : list) {
			try {
				String jp = StringUtil.getPinYinHeadChar(dp.getName())
						.toUpperCase();
				logger.debug(dp.getName() + "--->" + jp);
				dramaProgramMapper
						.updateDramaProgramJP(
								jp.length() > 16 ? jp.substring(0, 16) : jp,
								dp.getId());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
}
