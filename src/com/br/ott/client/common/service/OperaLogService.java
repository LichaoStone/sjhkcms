package com.br.ott.client.common.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.Global;
import com.br.ott.client.SessionUtil;
import com.br.ott.client.common.entity.OperaLog;
import com.br.ott.client.common.mapper.OperaLogMapper;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.ip.FindclientIP;

/* 
 *  系统日志服务类
 *  文件名：OperaLogService.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：陈登民
 *  创建时间：2012-12-25 - 下午6:01:10
 */
@Service
public class OperaLogService {
	@Autowired
	private OperaLogMapper operaLogMapper;

	/**
	 * 添加系统操作日志 创建人：陈登民 创建时间：2012-12-28 - 下午1:31:58
	 * 
	 * @param type
	 *            操作类型，从BaseController中的常量获取
	 * @param request
	 * @param busiName
	 *            业务名称
	 * @param content
	 *            操作内容 返回类型：void
	 * @exception throws
	 */
	public void addOperaLog(String type, HttpServletRequest request,
			String busiName, String content) {
		OperaLog log = new OperaLog();
		log.setCreateUser(SessionUtil.getLoginName());
		log.setBusiname(busiName);
		log.setContent(content.length() > 512 ? content.substring(0, 512)
				+ "..." : content);
		log.setCreateIp(FindclientIP.getIpAddr(request));
		log.setOperType(type);

		Object reqTimeObj = request
				.getAttribute(Global.SESSION_REQ_SYSTEM_TIME);
		if (null != reqTimeObj) {
			try {
				Long reqTime = (Long) reqTimeObj;
				if (reqTime != 0) {
					long start = reqTime.longValue();
					long end = System.currentTimeMillis();
					long time = end - start;
					log.setCost((int) time);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		operaLogMapper.addOperaLog(log);
		log = null;
	}

	public PagedModelList<OperaLog> findOperaLogs(int pageNo, int row,
			OperaLog log) {
		int count = operaLogMapper.findCountOperaLogs(log);
		RowBounds rowBounds = new RowBounds((pageNo - 1) * row, row);
		List<OperaLog> users = operaLogMapper.findOperaLogs(log, rowBounds);

		PagedModelList<OperaLog> modelList = new PagedModelList<OperaLog>(
				pageNo, row, count);
		modelList.addModels(users);
		users = null;
		return modelList;
	}
}
