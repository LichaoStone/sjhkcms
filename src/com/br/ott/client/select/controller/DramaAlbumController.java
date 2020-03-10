package com.br.ott.client.select.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.ott.base.BaseController;
import com.br.ott.client.select.service.DramaAlbumService;
import com.br.ott.client.select.entity.DramaAlbum;
import com.br.ott.common.util.StringUtil;
import com.br.ott.common.util.Feedback;

/** 
 * 创建时间：2017-03-06
 */
@Controller
@RequestMapping(value="/dramaAlbum")
public class DramaAlbumController extends BaseController {
	private final Logger logger = Logger.getLogger(DramaAlbumController.class);
	
	@Autowired
	private DramaAlbumService dramaAlbumService;
	
	
	/**
	*按分页查询
	 */
	@RequestMapping(value="findDramaAlbum")
	public String findDramaAlbum(HttpServletRequest request,DramaAlbum dramaAlbum,Model model) throws Exception{
		dramaAlbum.setCurrentPage(getPageNo(request));
		dramaAlbum.setShowCount(getPageRow(request));
		
		List<DramaAlbum> list = dramaAlbumService.findDramaAlbumByPage(dramaAlbum);
		model.addAttribute("dramaAlbums", list);
		model.addAttribute("dramaAlbum", dramaAlbum);
		list = null;
		return "dramaAlbum/listDramaAlbum";
	}
	
	/**跳转到编辑页面
	 */
	@RequestMapping(value="toDramaAlbum")
	public String toDramaAlbum(HttpServletRequest request,Model model) throws Exception{
		String id = request.getParameter("id");
		DramaAlbum dramaAlbum= new DramaAlbum();
		if(!StringUtil.isEmpty(id)) {
			dramaAlbum = dramaAlbumService.getDramaAlbumById(id);
		} 
		model.addAttribute("dramaAlbum",  dramaAlbum);
		return "dramaAlbum/dramaAlbumInfo";
	}
	
	/**保存
	 */
	@RequestMapping(value="saveDramaAlbum")
	public @ResponseBody
	Feedback saveDramaAlbum(HttpServletRequest request,DramaAlbum dramaAlbum,Model model){
		logger.debug(getClass().getName()+" save()");
		try{
			String id=request.getParameter("id");
			if(StringUtil.isEmpty(id)){
				dramaAlbumService.insertDramaAlbum(dramaAlbum);
			} else {
				dramaAlbumService.updateDramaAlbum(dramaAlbum);
			}
			return Feedback.success("保存成功");
		}catch(Exception e) {
			return Feedback.success("保存失败");
		}
	}
	
	/**删除
	 */
	@RequestMapping(value="deleteDramaAlbumById")
	public @ResponseBody
	Feedback deleteDramaAlbumById(HttpServletRequest request,Model model){
		logger.debug(getClass().getName()+" delete()");
		try{
			String id = request.getParameter("id");
			dramaAlbumService.deleteDramaAlbumById(id);
			return Feedback.success("删除成功");
		}catch(Exception e) {
			return Feedback.success("删除失败");
		}
	}
}
