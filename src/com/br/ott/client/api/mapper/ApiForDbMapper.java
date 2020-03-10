package com.br.ott.client.api.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.br.ott.client.select.entity.DramaProgram;


public interface ApiForDbMapper {
	
	void addUserViewLog(Map<String, Object> map);
	
	List<Map<String, Object>> findZClogForColAndRec(@Param("phone") String phone, @Param("dramaChannel") String dramaChannel, @Param("dramaType") String dramaType);
	
	List<Map<String, Object>> findZCByViewLogIds(Map<String, Object> map, RowBounds rowBounds);
	
	void updateViewLogStatusForPL();
	
	public void updateViewLogStatus(Map<String, Object> map);
	
	void updateProgramClickNum(@Param("dramaId") String dramaId);
	
	List<DramaProgram> getRecDramas(Map<String, Object> map, RowBounds rowBounds);
	
	List<Map<String, Object>> getDramaIdsForList(@Param("dramaId") String dramaId);
	
	List<Map<String, Object>> findZCInfoForCol(Map<String, Object> map);
	
	void updateZCInfo(Map<String, Object> map);
	
}
