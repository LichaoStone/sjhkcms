package com.br.ott.client.select.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.select.entity.ViewLog;
import com.br.ott.client.select.mapper.ViewLogMapper;

@Service
public class ViewLogService {
	@Autowired
	private ViewLogMapper mViewLogMapper;
	
	/**分页查询
	*/
	public List<ViewLog> findViewLogByPage(ViewLog mViewLog) {
		mViewLog.setTotalResult(mViewLogMapper.getCountViewLog(mViewLog));
		return mViewLogMapper.findViewLogByPage(mViewLog);
	}
	/**根据ID查询
	*/
	public ViewLog findViewLogById(String id){
		return mViewLogMapper.findViewLogById(id);
	}
	
	/**查询全部
	*/
	public List<ViewLog> findViewLogByCond(ViewLog mViewLog){
		return mViewLogMapper.findViewLogByCond(mViewLog);
	}

}