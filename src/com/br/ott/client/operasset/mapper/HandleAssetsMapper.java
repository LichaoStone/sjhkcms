package com.br.ott.client.operasset.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.br.ott.client.operasset.entity.Assets;
import com.br.ott.client.select.entity.DramaAlbum;
import com.br.ott.client.select.entity.DramaSource;

public interface HandleAssetsMapper {
	
	void addJointinfo_handlelog(Map<String, String> map);
	
	void addProgram(Map<String, Object> map);
	
	void addProgramType(Map<String, String> map);
	
	void addJointInfo_category(Map<String, String> map);
	
	List<Map<String, Object>> queryCategoryForList(Map<String, String> map);
	
	void addJointInfo(Map<String, String> map);
	
	List<Map<String, String>> queryJointInfoForList();
	
	void updateJointInfo(Map<String, String> paramsMap);
	
	List<Map<String, String>> queryPendingAssetForList();
	
	List<Map<String, Object>> queryJointInfoByName(@Param("assetName") String assetName);
	
	List<Map<String, Object>> queryChildByIdAndType(@Param("elementID") String elementID, @Param("eleType") String eleType);
	
	List<Map<String, String>> queryParentByIdAndType(@Param("parentID") String parentID, @Param("parentType") String parentType);
	
	List<Map<String, String>> queryPictureOrMoviesByIdAndType(@Param("elementID") String elementID, @Param("elementType") String elementType); 
	
	List<Map<String, Object>> queryMovieByIdAndType(@Param("elementID") String elementID, @Param("elementType") String elementType);
	
	void addDrama_source(Map<String, String> paramsMap);
	
	DramaSource queryDramaSource(Map<String, String> paramsMap);
	
	void addJointinfo_source(Map<String, String> paramsMap);
	
	void addJointinfo_source2(Map<String, String> paramsMap);
			
	void addDrama_type(@Param("dramaId") String dramaId, @Param("navId") String navId);
			
	void updateChildrenAssets(Map<String, String> paramsMap);
	
	void updateDramaProgram(Map<String, String> paramsMap);
				
	void updateDrama_source(Map<String, String> paramsMap);
		
	void updateJointinfo_source(Map<String, String> paramsMap);
	
	void updateJointinfo_source2(Map<String, String> paramsMap);
	
	List<Map<String, String>> queryCategoryInfo(@Param("crNavId") String crNavId, @Param("elementType") String elementType);

	List<Map<String, String>> queryCategoryInfo2(@Param("crNavId") String crNavId, @Param("elementType") String elementType);
	
	List<Map<String, String>> queryNodifyAssetForList(Map<String, Object> result);
	
	List<Map<String, String>> queryPictureByList(@Param("parentID") String parentID, @Param("parentType") String parentType, @Param("elementID") String elementID, @Param("elementType") String elementType);
	
	List<Map<String, String>> queryMovieByList(@Param("elementID") String elementID, @Param("elementType") String elementType, @Param("parentID") String parentID, @Param("parentType") String parentType);
	
	void updateJointInfoForSendStatus(Map<String, String> paramsMap);
		
	List<Map<String, String>> queryCategoryForList(@Param("parentCode") String parentCode);
	
	void addOtherCategory(Map<String, String> paramsMap);
	
	void addDrama_navigation(Map<String, Object> paramsMap);
	
	void addCategory(Map<String, Object> paramsMap);
	
	List<Map<String, String>> queryPictureByIdAndType(@Param("elementID") String elementID, @Param("elementType") String elementType);
	
	List<Map<String, Object>> queryProgramByIdAndType(@Param("elementID") String elementID, @Param("elementType") String elementType);
	
	void addJointInfoType(@Param("localNavId") String localNavId, @Param("elementID") String elementID, @Param("elementCode") String elementCode, @Param("elementType") String elementType);
	
	void addAssets(Map<String, String> paramsMap);
	
	void addDrama_children(Map<String, Object> paramsMap);
	
	List<Map<String, String>> queryPendParseFileForList(Map<String, Object> paramsMap);
	
	List<Map<String, Object>> queryAllMoviesByList(@Param("elementID") String elementID, @Param("elementType") String elementType);
	
	void updateDramaSource(Map<String, String> paramsMap);
	
	List<Map<String, Object>> queryAllPendPic();
	
	void updateJointinfoSource(Map<String, Object> paramsMap);
	
	List<Map<String, String>> queryDramaTypeInfo(@Param("dramaId") String dramaId, @Param("navId") String navId);
	
	List<Map<String, String>> querytest();
	
	void updateCategoryInfo(Map<String, String> paramsMap);
	
	void updateCategoryInfo2(Map<String, String> paramsMap);
	
	void updateCategoryInfos(Map<String, String> paramsMap);
	
	void updateCategoryInfo2s(Map<String, String> paramsMap);
					
	List<Assets> findAssetsByPage(Assets assets);
	
	int getCountHandleAssets(Assets assets);
	
	Assets getAssetsByCorrelateID(@Param("correlateID") String correlateID);
	
	List<Map<String, String>> queryJointInfoTypeInfo(@Param("elementID") String elementID, @Param("elementType") String elementType);
	
	void updateJointInfoType(@Param("dramaId") String dramaId, @Param("elementID") String elementID, @Param("elementType") String elementType);
	
	List<Map<String, Object>> queryAllDispatchPics();
	
	void updateJointinfoSource2(Map<String, Object> paramsMap);
	
	void updateDramaNavigation(@Param("localNavId")String localNavId, @Param("status")String status);
	
	List<Map<String, Object>> queryOtherCategory();
	
	void updateOtherCategory(Map<String, Object> paramsMap);
	
	void updateDramaPid(@Param("status")String status, @Param("dramaId") String dramaId);
	
	void addJointinfoPath(@Param("elementID")String elementID, @Param("elementCode")String elementCode, @Param("elementType")String elementType, @Param("localFileName")String localFileName, @Param("correlateID")String correlateID);

	List<DramaAlbum> findDramaAlbums(@Param("albumId") String albumId, @Param("dramaId") String dramaId);
}