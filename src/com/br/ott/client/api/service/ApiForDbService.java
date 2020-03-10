package com.br.ott.client.api.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.Global;
import com.br.ott.client.api.mapper.ApiForDbMapper;
import com.br.ott.client.common.DramaPositionCache;
import com.br.ott.client.common.NavigationCache;
import com.br.ott.client.common.RecDramaCache;
import com.br.ott.client.common.service.CommonService;
import com.br.ott.client.select.entity.DramaAlbum;
import com.br.ott.client.select.entity.DramaChildren;
import com.br.ott.client.select.entity.DramaNavigation;
import com.br.ott.client.select.entity.DramaPosition;
import com.br.ott.client.select.entity.DramaProgram;
import com.br.ott.client.select.entity.DramaSource;
import com.br.ott.client.select.entity.DramaType;
import com.br.ott.client.select.mapper.DramaAlbumMapper;
import com.br.ott.client.select.mapper.DramaChildrenMapper;
import com.br.ott.client.select.mapper.DramaChildrenMapper2;
import com.br.ott.client.select.mapper.DramaPositionMapper;
import com.br.ott.client.select.mapper.DramaProgramMapper;
import com.br.ott.client.select.mapper.DramaProgramMapper2;
import com.br.ott.client.select.mapper.DramaSourceMapper;
import com.br.ott.client.select.mapper.DramaTypeMapper2;
import com.br.ott.client.select.service.DramaProgramService;
import com.br.ott.common.exception.OTTException;
import com.br.ott.common.exception.SystemExceptionCode.ApiCode;
import com.br.ott.common.util.Constants;
import com.br.ott.common.util.StringUtil;

@Service
public class ApiForDbService {

	@Autowired
	public DramaProgramMapper dramaProgramMapper;
	
	@Autowired
	public DramaProgramMapper2 dramaProgramMapper2;

	@Autowired
	public DramaProgramService dramaProgramService;

	@Autowired
	private DramaChildrenMapper dramaChildrenMapper;
	
	@Autowired
	private DramaChildrenMapper2 dramaChildrenMapper2;
	
	@Autowired
	private DramaSourceMapper dramaSourceMapper;
	
	@Autowired
	private DramaAlbumMapper dramaAlbumMapper;
	
	@Autowired
	private ApiForDbMapper apiForDbMapper;
	
	@Autowired
	private DramaTypeMapper2 dramaTypeMapper2;
	
	@Autowired
	private DramaPositionMapper  dramaPositionMapper;
	
	/**
	 * 获取一级推荐栏目接口
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-11
	 * 
	 * @param List
	 * @return 返回类型：String
	 * @exception throws
	 */
	public Object findRecType(HttpServletRequest request) {
		List<JSONObject> navList = NavigationCache.getNavigationJSON2();
		String sign = (String)request.getAttribute("sign");
		if (CollectionUtils.isEmpty(navList)) {
			if (sign != null) {
				return new JSONArray();
			} else {
				JSONObject result = new JSONObject();
				result.put("returnCode", "0");
				result.put("result", new ArrayList<Map<String, Object>>());
				return result;
			}                
		}
		List<JSONObject> childrenList = new ArrayList<JSONObject>();
		//只有当点播首页面信息组合接口调用时才得到childrenList的值
		if (sign != null) {
			Iterator<JSONObject> itor = navList.iterator();
			JSONObject json = null;
			while (itor.hasNext()) {
				json = itor.next();
				childrenList = NavigationCache.getNavigationJSON((String)json.get("id"));
				json.put("navChildren", childrenList);
				childrenList = null;
			}
			return JSONArray.fromObject(navList);
		} else {
			JSONObject result = new JSONObject();
			result.put("returnCode", "0");
			result.put("result", navList);
			return result;
		}
	}

	/**
	 * 获取推荐资产信息接口
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-12
	 * 
	 * @return 返回类型：String
	 * @throws OTTException 
	 * @exception throws
	 */
	public JSONObject findRecDrama(HttpServletRequest request, JSONObject content) throws OTTException {
		String positionCode = getStringFromJson(content, "positionCode");
		String limit = content.get("limit") == null ? "15" : content.get("limit").toString();
		String prType = DramaPositionCache.getDPIDByCode(positionCode);
		
		List<DramaProgram> tempList = RecDramaCache.findDramaProgramByCode(prType, limit);
		List<Object> result = new ArrayList<Object>();
		if (CollectionUtils.isNotEmpty(tempList)) {
			Iterator<DramaProgram> itor = tempList.iterator();
			DramaProgram program = null;
			JSONObject jsonObject = null;
			while (itor.hasNext()) {
				program = itor.next();
				jsonObject = new JSONObject();
				jsonObject.put("id", program.getId());
				jsonObject.put("name", program.getName());
				jsonObject.put("recDImg", program.getRealRecDImg());
				jsonObject.put("recXImg", program.getRealRecXImg());
				jsonObject.put("coverDImg", program.getRealCoverDImg());
				jsonObject.put("coverXImg", program.getRealCoverXImg());
				jsonObject.put("level1NavName", program.getLevel1NavName());
				jsonObject.put("sort", program.getSort());
				jsonObject.put("pId", program.getpId());
				jsonObject.put("categoryId", "sj_dbsytj");
				result.add(jsonObject);
				program = null;
				jsonObject = null;
			}
			tempList = null;
		}
		JSONObject json = new JSONObject();
		if (request.getAttribute("sign") != null) {
			JSONObject jsonVar = new JSONObject();
			jsonVar.put("returnCode", "0");
			json.put("tj", result);
			jsonVar.put("result", json);
			result = null;
			json = null;
			return jsonVar;
		} else {
			json.put("returnCode", "0");
			json.put("result", result);
			return json;
		}
	}

	public JSONObject findRecDrama2(HttpServletRequest request, JSONObject content) throws OTTException {
		JSONObject jsonObject2 = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		String limit = content.get("limit") == null ? "15" : content.get("limit").toString();
		DramaPosition dramaPosition = new DramaPosition();
		dramaPosition.setType("1");
		dramaPosition.setOrderColumn("sort asc");
		List<DramaPosition> dpList = dramaPositionMapper.findDramaPositionByCond(dramaPosition);
		String value = "";
		for (int i = 0; i < dpList.size(); i++) {
			dramaPosition = dpList.get(i);
			value = NavigationCache.getIdByName(dramaPosition.getName());
			List<JSONObject> list = NavigationCache.getNavigationJSON(value);
			List<DramaProgram> tempList = RecDramaCache.findDramaProgramByCode(dramaPosition.getId(), limit);
			
			if (CollectionUtils.isNotEmpty(tempList)) {
				tempList = toResolveParamsByList(tempList, 2);
				jsonObject2.put("name", dramaPosition.getName());
				jsonObject2.put("id", value);
				jsonObject2.put("childern", tempList);
				jsonObject2.put("navChildren", list);
				jsonArray.add(jsonObject2);
				tempList = null;
			}
		}
		JSONObject result = new JSONObject();
		result.put("returnCode", "0");
		result.put("result", jsonArray);
		return result;
	}
	
	/**
	 * 资产列表查询接口
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-12
	 * 
	 * @return 返回类型：String
	 * @throws IOException 
	 * @exception throws
	 */
	@SuppressWarnings("unchecked")
	public JSONObject findDramaByPage(JSONObject content) throws IOException {
		int page = content.get("page") == null ? 1 : Integer.parseInt(content.get("page").toString());
		int limit = content.get("limit") == null ? Constants.PAGE_DATA : Integer.parseInt(content.get("limit").toString());
		String typeId = content.get("typeId") == null ? "" : content.get("typeId").toString();
		String name = content.get("name") == null ? "" : content.get("name").toString();
		String status = content.get("status") == null ? "1" : content.get("status").toString();
		DramaProgram dp = new DramaProgram();
		dp.setCurrentPage(page);
		dp.setShowCount(limit);
		if (!"".equals(typeId)) {
			dp.setTtype(typeId);
		}
		dp.setName(name);
		dp.setStatus(status);
		List<DramaProgram> dpList = this.dramaProgramService.findDramaProgramByPage2(dp);  //使用了mybatis的二级缓存
		//假如存在search="true"参数，进行下面的去掉重复name属性的对象，合辑会出现多个名字相同的资产
		String search = (String)content.get("search");
		if("true".equals(search)) {
			dpList = (List<DramaProgram>) CommonService.removeDuplicateObject(dpList, "Name");
		}
		List<JSONObject> result = toResolveParamsForDramaProgram(dpList);
		JSONObject json = new JSONObject();	
		json.put("returnCode", "0");
		json.put("typeId", typeId);
		json.put("page", page);
		json.put("totalPages", dp.getTotalPage());
		json.put("totalResult", dp.getTotalResult());
		json.put("result", result);
		json.put("categoryId", "21100001000000091480920561911000");
		
		dpList = null;
		result = null;
		content = null;
		return json;
	}

	/**
	 * 资产详细信息接口
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-13
	 * 
	 * @return 返回类型：String
	 * @throws OTTException 
	 * @exception throws
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getDramaInfo(HttpServletRequest request) throws OTTException {
		String dramaId = getStringFromReq(request, "dramaId");
		String pId = getStringFromReq(request, "pId");  //剧集类型:连播剧 1，单播剧0,合辑 -1
		
		JSONObject result = new JSONObject();
		DramaProgram dramaProgram = this.dramaProgramMapper2.getDramaProgramByIdAndStatus(dramaId, "1"); //使用mybatis的二级缓存

		if (null != dramaProgram) {
			String[] typeArr = dramaProgram.getTypeName().split("\\/");
			String typeName = "";
			for (int i = 0,n = typeArr.length; i < n; i++) {
				if (typeArr[i].split("-").length > 1) {
					if ("".equals(typeName))
						typeName += typeArr[i].split("-")[1];
					else
						typeName += ("|" + typeArr[i].split("-")[1]);
				}
			}
			String typeId = dramaProgram.getDType();
			
			JSONObject temp = new JSONObject();
			List<DramaChildren> childrenList = null;
			List<DramaProgram> programList = null;
			
			//pId:剧集类型:连播剧1，单播剧0，合辑-1，合辑资产2
			if ("0,1".indexOf(pId) > -1) {
				DramaChildren dramaChildren = new DramaChildren();
				dramaChildren.setDramaId(dramaId);
				dramaChildren.setStatus("1");
				childrenList = this.dramaChildrenMapper2.findDramaChildren(dramaChildren); //使用mybatis二级缓存
				childrenList = toResolveParamsByList(childrenList, 2);
			}
			if ("-1".equals(pId)) {
				DramaProgram dp = new DramaProgram();
				dp.setStatus("1");
				dp.setAlbumId(dramaProgram.getId());
				dp.setOrderColumn("da.queue desc");
				programList = this.dramaProgramMapper2.findDramaProgram2(dp);     //使用mybatis二级缓存
				programList = toResolveParamsByList(programList, 1);	
			}
			temp.put("id", dramaProgram.getId());
			temp.put("name", dramaProgram.getName());
			temp.put("pId", dramaProgram.getpId());
			temp.put("actor", dramaProgram.getActor());
			temp.put("remark", dramaProgram.getRemark());
			temp.put("supercid", dramaProgram.getAssetId());
			temp.put("coverXImg", dramaProgram.getRealCoverXImg());
			temp.put("coverDImg", dramaProgram.getRealCoverDImg());
			temp.put("recXImg", dramaProgram.getRealRecXImg());
			temp.put("recDImg", dramaProgram.getRealRecDImg());		
			temp.put("typeName", typeName);
			temp.put("typeId", typeId);
			temp.put("children", "0,1".indexOf(pId) > -1 ? childrenList : ("-1".equals(pId) ? programList : new ArrayList<Object>()));
			result.put("dramaInfo", temp);
			programList = null;
			childrenList = null;
			temp = null;
		} else {
			result.put("dramaInfo", "{}");
		}
		return result;
	}

	/**
	 * 分解获取一级栏目、资产列表查询(DramaProgram列表、DramaChildren列表、DramaNavigation列表)
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-14
	 * 
	 * @return 返回类型：List
	 * @exception throws
	 */
	//当target为1时，表示合辑，要分解的是详情接口里剧集children的参数，只保留dramaId、pId、图片、name
	//当target为2时，表示连播剧或者电影，当target为3时，表示导航
	//target为1时集合里面的对象为DramaProgram；为2时对象为DramaChildren或者DramaProgram；为3时集合里面的对象为DramaNavigation
	@SuppressWarnings("rawtypes")
	public List toResolveParamsByList(List<?> list ,int target) {
		ArrayList<Object> result = new ArrayList<Object>();
		if (CollectionUtils.isNotEmpty(list)) {
			Object object = null;
			JSONObject info = null;
			Iterator<?> itor = list.iterator();
			while (itor.hasNext()) {
				object = itor.next();
				info = new JSONObject();
				if (1 == target) {
					info.put("id", ((DramaProgram)object).getId());
					info.put("pId", ((DramaProgram)object).getpId());
					info.put("actor", ((DramaProgram)object).getActor());
					info.put("title", ((DramaProgram)object).getName());
				} else if (2 == target) {
					if (object instanceof DramaChildren) {
						info.put("id", ((DramaChildren)object).getId());
						info.put("title", ((DramaChildren)object).getTitle());
					} else {
						info.put("id", ((DramaProgram)object).getId());
						info.put("name", ((DramaProgram)object).getName());
						info.put("pId", ((DramaProgram)object).getpId());
						info.put("actor", ((DramaProgram)object).getActor());
						info.put("remark", ((DramaProgram)object).getRemark());
						info.put("supercid", ((DramaProgram)object).getAssetId());
					}
				} else {
					info.put("id", ((DramaNavigation)object).getId());
					info.put("name", ((DramaNavigation)object).getName());
					info.put("parentId", ((DramaNavigation)object).getParentId());
					info.put("imgurl", ((DramaNavigation)object).getRealImgurl());
					info.put("categoryId", ((DramaNavigation)object).getCategoryId());
				}
				if (object instanceof DramaProgram) {
					info.put("coverXImg", ((DramaProgram)object).getRealCoverXImg());
					info.put("coverDImg", ((DramaProgram)object).getRealCoverDImg());
					info.put("recXImg", ((DramaProgram)object).getRealRecXImg());
					info.put("recDImg", ((DramaProgram)object).getRealRecDImg());
				}
				result.add(info);
				info = null;
			}
		}
		return result;
	}
	
	
	/**
	 * 专门处理集合元素为DramaProgram的集合，去掉展示端不需要的参数
	 * @author lizhenghg  @time 2018-01-23
	 * @param dpList
	 * @return List<DramaProgram>
	 */
	public List<JSONObject> toResolveParamsForDramaProgram(List<DramaProgram> dpList) {
		if (CollectionUtils.isEmpty(dpList)) {
			return new ArrayList<JSONObject>();
		}
		List<JSONObject> result = new ArrayList<JSONObject>();
		Iterator<DramaProgram> itor = dpList.iterator();
		DramaProgram dp = null;
		while (itor.hasNext()) {
			JSONObject temp = new JSONObject();
			dp = itor.next();
			temp.put("id", dp.getId());
			temp.put("name", dp.getName());
			temp.put("pId", dp.getpId());
			temp.put("actor", dp.getActor());
			temp.put("remark", dp.getRemark());
			temp.put("supercid", dp.getAssetId());
			temp.put("coverXImg", dp.getRealCoverXImg());
			temp.put("coverDImg", dp.getRealCoverDImg());
			temp.put("recXImg", dp.getRealRecXImg());
			temp.put("recDImg", dp.getRecDImg());
			result.add(temp);
			temp = null;
		}
		return result;
	}
	
	/**
	 * 获取当参数isdl_display为空时除了isdl_display为1的全部栏目接口
	 * 
	 * 创建人：lizhenghg 创建时间：2016-10-13
	 * 
	 * @param id
	 * @return 返回类型：List
	 * 
	 */
	public List<DramaNavigation> getNavigationList(String dramaId, String isdl_display){
		List<DramaNavigation> navList = null;
		if ("0".equals(dramaId)) {
			navList = NavigationCache.getNavigationList1(dramaId);
		} else {
			navList = NavigationCache.getNavigationList(dramaId);
		}
		List<DramaNavigation> result = new ArrayList<DramaNavigation>();
		if(CollectionUtils.isEmpty(navList))
			return result;
		Iterator<DramaNavigation> it = navList.iterator();
		DramaNavigation dramaNavigation = null;
		while (it.hasNext()) {
			dramaNavigation = it.next();
			if ("0".equals(dramaNavigation.getIsdl_display()) || "n".equals(isdl_display)) {
				result.add(dramaNavigation);
			}
		}
		navList = null;
		return result;
	}
	
	/**
	 * 保存用户收藏/最近收看资产接口
	 * 
	 * 创建人：lizhenghg 创建时间：2016-11-3
	 * 
	 * @param Map map
	 * @return 返回类型：void
	 * 
	 * @exception try{}catch
	 */
	public void addUserViewLog(Map<String, Object> map){
		this.apiForDbMapper.addUserViewLog(map);
	}
	
	/**
	 * 获取用户收藏/最近收看资产接口
	 * 
	 * 创建人：lizhenghg 创建时间：2016-11-3
	 * 
	 * @return 返回类型：String
	 * @throws OTTException 
	 * 
	 * @exception try{}catch
	 */
	public List<Map<String, Object>> findZClogForColAndRec(JSONObject content) throws OTTException{
		String phone = getStringFromJson(content, "phone");
		String dramaChannel = getStringFromJson(content, "dramaChannel");
		int page = content.get("page") == null ? 1 : Integer.parseInt(content.get("page").toString());
		int limit = content.get("limit") == null ? Constants.PAGE_DATA : Integer.parseInt(content.get("limit").toString());
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("dramaChannel", dramaChannel);
		paramsMap.put("phone", phone);
		paramsMap.put("dramaType", "0");
		result = this.apiForDbMapper.findZCByViewLogIds(paramsMap, new RowBounds((page - 1) * limit, limit));
		if (CollectionUtils.isNotEmpty(result)) {
			Iterator<Map<String, Object>> it = result.iterator();
			Map<String, Object> linfo = null;
			while (it.hasNext()) {
				linfo = it.next();
				String coverDImg = (String)linfo.get("coverDImg");
				String coverXImg = (String)linfo.get("coverXImg");
				String recDImg = (String)linfo.get("recDImg");
				String recXImg = (String)linfo.get("recXImg");

				if(StringUtil.isEmpty(coverDImg)) {
					linfo.put("coverDImg", "");
				}else {
					if (coverDImg.indexOf("http") > -1) {
						linfo.put("coverDImg", coverDImg);
					}else{
						linfo.put("coverDImg", Global.SERVER_SOURCE_URL + (String)linfo.get("coverDImg"));
					}
				}
				if(StringUtil.isEmpty(coverXImg)) {
					linfo.put("coverXImg", "");
				}else {
					if (coverXImg.indexOf("http") > -1) {
						linfo.put("coverXImg", coverXImg);
					}else{
						linfo.put("coverXImg", Global.SERVER_SOURCE_URL + (String)linfo.get("coverXImg"));
					}
				}
				if(StringUtil.isEmpty(recDImg)) {
					linfo.put("recDImg", "");
				}else {
					if (recDImg.indexOf("http") > -1) {
						linfo.put("recDImg", recDImg);
					}else{
						linfo.put("recDImg", Global.SERVER_SOURCE_URL + (String)linfo.get("recDImg"));
					}
				}
				if(StringUtil.isEmpty(recXImg)) {
					linfo.put("recXImg", "");
				}else {
					if (recDImg.indexOf("http") > -1) {
						linfo.put("recXImg", recXImg);
					}else{
						linfo.put("recXImg", Global.SERVER_SOURCE_URL + (String)linfo.get("recXImg"));
					}
				}
				linfo.put("categoryId", "21100001000000091480920561911000");
				linfo = null;
			}
		}
		return result;
	}
	
	/**
	 * 每天24:00批量处理一次用户观看资产状态
	 * 
	 * 创建人：lizhenghg 创建时间：2016-11-3
	 * 
	 * @return 返回类型：void
	 * 
	 * @exception try{}catch
	 */
	public void updateViewLogStatusForPL(){
		this.apiForDbMapper.updateViewLogStatusForPL();
	}
	
	/**
	 * 更新用户观看资产状态
	 * 
	 * 创建人：lizhenghg 创建时间：2016-11-3
	 * 
	 * @return 返回类型：void
	 * @throws OTTException 
	 * 
	 * @exception try{}catch
	 */
	public void updateViewLogStatus(Map<String, Object> map) throws OTTException{
		getStringFromMap(map, "phone");
		getStringFromMap(map, "dramaId");
		getStringFromMap(map, "dramaChannel");
		this.apiForDbMapper.updateViewLogStatus(map);
	}
	
	/**
	 * 更新资产点击量
	 * 
	 * 创建人：lizhenghg 创建时间：2016-11-14
	 * 
	 * @return 返回类型：void
	 * 
	 * @exception try{}catch
	 */
	public void updateProgramClickNum(String dramaId){
		apiForDbMapper.updateProgramClickNum(dramaId);
	}
	
	/**
	 * 获取资产列表
	 * 
	 * 创建人：lizhenghg 创建时间：2016-11-14
	 * 
	 * @return 返回类型：List
	 * 
	 * @exception try{}catch
	 */
	public List<DramaProgram> getDramaByPage(Map<String, Object> paramsMap, JSONObject content, String header) {
		int page = header == null ? (paramsMap.get("page") == null ? 1 : Integer.parseInt(paramsMap.get("page").toString())) : (content.get("page") == null ? 1 : Integer.parseInt(content.get("page").toString()));
		int limit = header == null ? (paramsMap.get("limit") == null ? Constants.PAGE_DATA: Integer.parseInt(paramsMap.get("limit").toString())) : (content.get("limit") == null ? Constants.PAGE_DATA: Integer.parseInt(content.get("limit").toString()));
		String typeId = header == null ? (paramsMap.get("typeId") == null ? "" : paramsMap.get("typeId").toString()) : (content.get("typeId") == null ? "" : content.get("typeId").toString());
		String jp = header == null ? (paramsMap.get("jp") == null ? "" : paramsMap.get("jp").toString()) : (content.get("jp") == null ? "" : content.get("jp").toString());
		String status = header == null ? (paramsMap.get("status") == null ? "1" : paramsMap.get("status").toString()) : (content.get("status") == null ? "1" : content.get("status").toString());
		DramaProgram dp = new DramaProgram();
		dp.setCurrentPage(page);
		dp.setShowCount(limit);
		if(!"".equals(typeId)) dp.setParentId(typeId);
		dp.setPcode(jp);
		dp.setStatus(status);
		List<DramaProgram> dpList = this.dramaProgramService.findDramaProgramByPage(dp);
		return dpList;
	}
	
	/**
	 * 根据点击量获取推荐资产信息接口
	 * 
	 * 创建人：lizhenghg 创建时间：2016-11-15
	 * 
	 * @return 返回类型：String
	 * @exception throws
	 */
	public JSONObject getRecDramaForClickNum(String dramaId, HttpServletRequest request){
		int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		int limit = request.getParameter("limit") == null ? Constants.PAGE_DATA : Integer.parseInt(request.getParameter("limit"));
		String status = request.getParameter("status") == null ? "1" : request.getParameter("status");	
		
		DramaType dt = this.dramaTypeMapper2.findDramaTypeByDramaId(dramaId);  //使用mybatis的二级缓存
		List<DramaType> dtLst = null;
		if (dt != null) {
			dtLst = this.dramaTypeMapper2.findDramaTypeByNavId(dt.getNavId()); //使用mybatis的二级缓存
		}
		if (CollectionUtils.isEmpty(dtLst)) {
			return null;
		}
		List<String> ids = new ArrayList<String>();
		for (DramaType dramaType : dtLst) {
			ids.add(dramaType.getDramaId());
		}
		dtLst = null;
		DramaProgram dramaProgram = new DramaProgram();
		dramaProgram.setStatus(status);
		dramaProgram.setCurrentPage(page);
		dramaProgram.setShowCount(limit);
		dramaProgram.setContainId(StringUtils.join(ids, ","));
		List<DramaProgram> dpList = this.dramaProgramMapper.findDramaProgramByPage2(dramaProgram);
		
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		if (CollectionUtils.isNotEmpty(dpList)) {
			Iterator<DramaProgram> it = dpList.iterator();
			DramaProgram dp_1 = null;
			Map<String, String> dp_2 = null;
			while (it.hasNext()) {
				dp_1 = it.next();
				dp_2 = new HashMap<String, String>();
				dp_2.put("dramaId", dp_1.getId());
				dp_2.put("pId", dp_1.getpId());
				dp_2.put("name", dp_1.getName());
				dp_2.put("coverDImg", dp_1.getRealCoverDImg());
				dp_2.put("coverXImg", dp_1.getRealCoverXImg());
				dp_2.put("recDImg", dp_1.getRealRecDImg());
				dp_2.put("recXImg", dp_1.getRealRecXImg());
				result.add(dp_2);
				dp_2 = null;
				dp_1 = null;
			}
		}
		JSONObject json = new JSONObject();
		json.put("tj", result);
		result = null;
		dpList = null;
		ids = null;
		return json;
	}
	
	/**
	 * 判断某资产是否已经收藏接口
	 * 创建人：lizhenghg 创建时间：2016-11-22
	 * @param paramsMap
	 * @return 返回类型：List
	 * @throws OTTException 
	 * @exception try{}catch
	 */
	public List<Map<String, Object>> findZCInfoForCol(Map<String, Object> paramsMap) throws OTTException{
		getStringFromMap(paramsMap, "phone");
		getStringFromMap(paramsMap, "dramaId");			
		getStringFromMap(paramsMap, "dramaChannel");			
		return this.apiForDbMapper.findZCInfoForCol(paramsMap);
	}
	
	/**
	 * 更新最近观看/收藏资产信息接口
	 * 创建人：lizhenghg 创建时间：2016-11-22
	 * @param paramsMap
	 * @return 返回类型：void
	 * @exception try{}catch
	 */
	public void updateZCInfo(Map<String, Object> map){
		this.apiForDbMapper.updateZCInfo(map);
	}
	
	/**
	 * 取消用户收藏资产接口(C端接口)
	 * 创建人：lizhenghg 创建时间：2016-11-16
	 * @param paramsMap
	 * @return 返回类型：void
	 * @throws OTTException 
	 */
	public void cancelZClogForColAndRec(Map<String, Object> paramsMap) throws OTTException {
		paramsMap.put("dramaType", "0");
	    paramsMap.put("status", "ok");
		List<Map<String, Object>> list = findZCInfoForCol(paramsMap);
		if (CollectionUtils.isNotEmpty(list)) {
			Map<String, Object> map = list.get(0);
			if ("1".equals((String)map.get("status"))) {
				this.apiForDbMapper.updateViewLogStatus(paramsMap);
			}
			list = null;
		}
	}
	
	/**
	 * 处理资产详情
	 * @author lizhenghg   2017-7-21
	 */
	public JSONObject dealDramaDetail(HttpServletRequest req) throws OTTException, Exception{
		String dramaId = getStringFromReq(req, "dramaId");
		String sign = req.getParameter("sign");	
		JSONObject result = new JSONObject();
		JSONObject temp = new JSONObject();
		if (!"3".equals(sign)) {
			temp = getDramaInfo(req); 	//sign不存在或1时：请求剧集和简介；sign=2时：请求推荐、剧集、简介(已废弃)；sign=3时，只请求推荐（终端IOS：sign为1时终端只解析简介）
		}
		if (StringUtils.isNotBlank(sign) && "2,3".indexOf(sign) > -1) {
			temp = getRecDramaForClickNum(dramaId, req);     //推荐必须有值，没值要补充
		}
		result.put("returnCode", "0");
		result.put("result", temp);
		return result;
	}
	
	/**
	 * 获取播放URL
	 * @author lizhenghg  2017-7-23
	 * @param request
	 * @param content
	 * @param header
	 * @return JSONObject
	 */
	public JSONArray getUrlAddress(JSONObject content) throws OTTException{
		String dramaId = getStringFromJson(content, "dramaId");
		String pId = getStringFromJson(content, "pId");  //剧集类型:连播剧 1，单播剧0,合辑 -1,合辑资产2,连播剧子集3
		String title = getStringFromJson(content, "title");
		String operator = getStringFromJson(content, "operator");
	
		String source = "sjiptv_zx".equals(operator) ? "0" : ("sjiptv_hw".equals(operator) ? "1" : "2");
		String albumId = null;
		DramaSource ds = null;
		if("-1".equals(pId)){       //当为合辑时，传过来的dramaId为合辑id，title为默认值1
			DramaAlbum dramaAlbum = new DramaAlbum();
			dramaAlbum.setAlbumId(Integer.parseInt(dramaId));
			dramaAlbum.setQueue(0);
			List<DramaAlbum> daList = this.dramaAlbumMapper.findDramaAlbumForAPI(dramaAlbum);
			if(CollectionUtils.isNotEmpty(daList)){
				albumId = String.valueOf(daList.get(0).getDramaId());
				ds = this.dramaSourceMapper.findDramaSourceForAPI(albumId, title, "2", source);				
			}
			daList = null;
			dramaAlbum = null;
		}else if("1".equals(pId)){  //当为连播剧时，传过来的dramaId为父剧id，title值终端给
			DramaChildren children = new DramaChildren();
			children.setDramaId(dramaId);
			children.setTitle(title);
			List<DramaChildren> dcList = this.dramaChildrenMapper.findDramaChildren(children);
			if(CollectionUtils.isNotEmpty(dcList)){
				albumId = dcList.get(0).getId();
				ds = this.dramaSourceMapper.findDramaSourceForAPI(dcList.get(0).getId(), title, "3", source);
			}
			dcList = null;
			children = null;
		}else{  //单播剧0,合辑资产2,连播剧子集3
			ds = this.dramaSourceMapper.findDramaSourceForAPI(dramaId, title, pId, source);
		}
		
		JSONArray result = null;
		if(ds == null) return result = new JSONArray();
		result = new JSONArray();
		JSONObject linfo = new JSONObject();
		linfo.put("playUrl", ds.getPlayUrl());
		linfo.put("operator", operator);
		if("-1,1".indexOf(pId) > -1){
			linfo.put("dramaId", albumId);
		}else{
			linfo.put("dramaId", dramaId);
		}
		result.add(linfo);
		linfo = null;
		return result;
	}
	
	/**
	 * IPTV首页全部点播信息组合接口
	 * 创建人：lizhenghg 创建时间：2016-11-14
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public JSONObject IPTVDramaIndex(){
		JSONObject jparent = new JSONObject();
		List<DramaProgram> childern = new ArrayList<DramaProgram>();
		List<Map<String, Object>> parent = new ArrayList<Map<String, Object>>(); 
		List<JSONObject> list = null;
		Map<String, Object> map = null;
		
		String key = null;
		String value = null;
		
		for (int i = 0; i < 3; i++) {
			map = new HashMap<String, Object>();
			if (i == 0) {
				key = "大片";
				map.put("categoryId", "21100001000000091480920561911000");
			} else if (i == 1) {
				key = "动漫";
				map.put("categoryId", "21100001000000091480923295948000");
			} else {
				key = "剧场";
				map.put("categoryId", "21100001000000091480920940818000");
			}
			value = NavigationCache.getIdByName(key);
			list = NavigationCache.getNavigationJSON(value);
			childern = RecDramaCache.getDramaProgramsById(value);
			if (CollectionUtils.isNotEmpty(childern)) {
				childern = toResolveParamsByList(childern, 2);
			}
			map.put("name", key);
			map.put("id", value);
			map.put("childern", childern);
			map.put("navChildren", list);
			parent.add(map);
			list = null;
			childern = null;
		}
		jparent.put("returnCode", "0");
		jparent.put("result", parent);
		parent = null;
		
		return jparent;
	}
	
	/**
	 * 获取栏目信息接口
	 * 创建人：lizhenghg 创建时间：2016-10-12
	 * @param id
	 * @param isdl_display
	 * @return
	 * @throws OTTException 
	 */
	public JSONObject findType(JSONObject content) {
		String id = (String)content.get("id");
		if (StringUtils.isEmpty(id)) {
			id = "0";
		} 
		List<JSONObject> list = null;
		if ("0".equals(id)) {
			list = NavigationCache.getNavigationJSON2();
		} else {
			list = NavigationCache.getNavigationJSON(id);
		}
		JSONObject result = new JSONObject();
		result.put("returnCode", "0");
		result.put("result", list);
		list = null;
		return result;
	}
	
	/**
	 * 添加用户收藏/最近收看资产接口(C端接口)
	 * 创建人：lizhenghg 创建时间：2016-11-4
	 * @param request
	 * @param paramsMap
	 * @return
	 * @throws OTTException
	 */
	public void addZClogForColAndRec(Map<String, Object> paramsMap) throws OTTException{
		paramsMap.put("dramaType", "0");
		paramsMap.put("status", "ok");
		List<Map<String, Object>> list = findZCInfoForCol(paramsMap);
		if (CollectionUtils.isNotEmpty(list))
			updateZCInfo(paramsMap);
		else
			addUserViewLog(paramsMap);
		list = null;
	}
	
	/**
	 * 根据资产id获取该资产所对应的spuercid
	 * 创建人：lizhenghg 创建时间：2017-09-14
	 * @param request
	 * @param content
	 * @param header
	 * @return String
	 * @throws OTTException
	 */
	public String getSpuercidByID(JSONObject content) throws OTTException {
		String dramaId = getStringFromJson(content, "dramaId");
		DramaProgram dramaProgram = dramaProgramService.getDramaProgramById(dramaId);
		JSONObject jsonObject = new JSONObject();
		if(dramaProgram != null) {
			jsonObject.put("supercid", dramaProgram.getAssetId());
			dramaProgram = null;
		}
		return jsonObject.toString();
	}
	
	/**
	 * 判断某JSONObject是否为空
	 * @author lizhenghg 2017-08-09
	 * @param object
	 * @return
	 */
	public boolean isNotNull(JSONObject object){
		if (object != null && object.size() > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断HttpServletRequest中是否存在某参数并返回该参数的值
	 * @author lizhenghg  2017-08-09
	 * @param request
	 * @param key
	 * @return
	 * @throws OTTException
	 */
	public String getStringFromReq(HttpServletRequest request, String key) throws OTTException{
		String content = request.getParameter(key);
		if(null == content || "".equals(content))
			throw new OTTException(ApiCode.NULL_PARAM, "参数" + key + "缺失");
		return content;
	}
	
	/**
	 * 判断传入参数是否缺失
	 * 创建人：lizhenghg 创建时间：2016-11-4 
	 * @param Map<String, Object> map, String str
	 *
	 */
	public Object getStringFromMap(Map<String, Object> map, String str) throws OTTException{
		if(org.springframework.util.CollectionUtils.isEmpty(map))
			throw new OTTException(ApiCode.NULL_PARAM, "全部参数缺失");
		if(StringUtils.isBlank(str))
			throw new OTTException(ApiCode.NULL_PARAM, "参数" + str + "缺失");
		Object value = map.get(str);
		if(null == value)
			throw new OTTException(ApiCode.NULL_PARAM, "参数" + str + "缺失");
		return value;
	}
	
	/**
	 * 判断传入参数是否缺失
	 * 创建人：lizhenghg 创建时间：2016-11-4 
	 * @param Map<String, Object> map, String str
	 *
	 */
	public String getStringFromJson(JSONObject object, String str) throws OTTException{
		if(object == null || object.size() == 0)
			throw new OTTException(ApiCode.NULL_PARAM, "全部参数缺失");
		if(StringUtils.isBlank(str))
			throw new OTTException(ApiCode.NULL_PARAM, "参数" + str + "缺失");
		String value = (String)object.get(str);
		if(StringUtil.isEmpty(value))
			throw new OTTException(ApiCode.NULL_PARAM, "参数" + str + "缺失");
		return value;
	}
}