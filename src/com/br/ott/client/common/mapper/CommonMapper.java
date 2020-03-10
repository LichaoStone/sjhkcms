package com.br.ott.client.common.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CommonMapper {
	List<Map<String, Object>> exportExcel(@Param("columns")String columns,@Param("table")String table,@Param("where")String where);
}
