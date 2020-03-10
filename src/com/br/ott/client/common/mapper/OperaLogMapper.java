package com.br.ott.client.common.mapper;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.br.ott.client.common.entity.OperaLog;

/**
 * 操作记录
 * @author 陈登民
 *
 */
public interface OperaLogMapper {
	void addOperaLog(OperaLog operaLog);
	
	List<OperaLog> findOperaLogs(OperaLog log, RowBounds rowBounds);
	int findCountOperaLogs(OperaLog log);
}
