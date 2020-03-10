package com.br.ott.client.select.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.ott.base.BaseController;
import com.br.ott.client.select.service.ViewLogService;
import com.br.ott.client.select.entity.ViewLog;

/** 
 * 创建时间：2016-11-21
 */
@Controller
@RequestMapping(value="/viewLog")
public class ViewLogController extends BaseController {
	
	@Autowired
	private ViewLogService viewLogService;
	
	
	/**
	*按分页查询
	 */
	@RequestMapping(value="findViewLog")
	public String findViewLog(HttpServletRequest request,ViewLog viewLog,Model model) throws Exception{
		viewLog.setCurrentPage(getPageNo(request));
		viewLog.setShowCount(getPageRow(request));
		
		List<ViewLog> list = viewLogService.findViewLogByPage(viewLog);
		model.addAttribute("viewLogs", list);
		model.addAttribute("viewLog", viewLog);
		list = null;
		return "select/listViewLog";
	}
	
}
