package com.br.ott.client.select.mapper;

import java.util.List;

import com.br.ott.client.select.entity.ViewLog;

public interface ViewLogMapper {
	/**
	*按分页查询
	*/
	List<ViewLog> findViewLogByPage(ViewLog mViewLog);
	/**
	*按统计总记录数
	*/
	int getCountViewLog(ViewLog mViewLog);
	
	/**
	*按ID查询
	*/
	ViewLog findViewLogById(String id);
	

	/**
	*按条件查询
	*/
	List<ViewLog> findViewLogByCond(ViewLog mViewLog);
	

}