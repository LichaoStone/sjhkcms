/*
 * @# PartnerController.java Aug 1, 2012 2:12:47 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.client.partner.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.br.ott.Global;
import com.br.ott.base.BaseController;
import com.br.ott.client.SessionUtil;
import com.br.ott.client.common.DictCache;
import com.br.ott.client.common.entity.Dictionary;
import com.br.ott.client.common.service.OperaLogService;
import com.br.ott.client.partner.entity.Partner;
import com.br.ott.client.partner.service.PartnerService;
import com.br.ott.common.exception.OTTException;
import com.br.ott.common.exception.OTTRuntimeException;
import com.br.ott.common.exception.SystemExceptionCode.UploadCode;
import com.br.ott.common.util.Constants.DicType;
import com.br.ott.common.util.Constants.PartnerState;
import com.br.ott.common.util.Constants.State;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.ObjectUtil;
import com.br.ott.common.util.PagedModelList;
import com.br.ott.common.util.StringUtil;
import com.br.ott.common.util.UploadFile;
import com.br.ott.common.util.id.CreateIdentityId;

/*
 * @author pananz
 * TODO
 * 合作伙伴控制器
 */
@Controller
@RequestMapping(value = "/partner")
public class PartnerController extends BaseController {
	private Logger log = Logger.getLogger(PartnerController.class);
	private static final String basePath = Global.PARTNER_IMG_URL;
	private static final String BUSI_NAME = "合作伙伴管理";
	private static final long PARTNER_MAX_IMG = 2097152;

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private OperaLogService operaLogService;

	/**
	 * 分页查询合作伙伴信息
	 * 
	 * @param page第几页
	 * @param partner查询条件
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "findPartner", method = RequestMethod.GET)
	public String findPartner(HttpServletRequest request, Partner partner,
			Model model) {
		partner.setNoShelf(PartnerState.PARTNER_FORCED_SHELF);
		PagedModelList<Partner> pml = partnerService.findPartner(partner,
				getPageNo(request), getPageRow(request));
		model.addAttribute("pml", pml);
		model.addAttribute("partner", partner);
		pml = null;
		return "partner/listPartner";
	}

	/**
	 * 转到合作伙伴编辑页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "toPartner", method = RequestMethod.GET)
	public String toPartner(HttpServletRequest request, Model model) {
		Partner partner = new Partner();
		String id = request.getParameter("id");
		if (!StringUtil.isEmpty(id)) {
			partner = partnerService.getPartnerById(id);
		}
		model.addAttribute("sourceUrl", Global.SERVER_SOURCE_URL);
		bindCitysAllToModel(model, partner != null ? partner.getPartnerCity()
				: null);
		Map<String, Dictionary> mechantTypes = DictCache
				.getDictValueList(DicType.MERCHANT_TYPE);
		model.addAttribute("mechantTypes", mechantTypes);
		model.addAttribute("partner", partner);
		return "partner/partnerInfo";
	}

	/**
	 * 查询合伙伙伴详情
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "partnerInfo", method = RequestMethod.GET)
	public String partnerInfo(@RequestParam("id") String id, Model model) {
		if (!StringUtil.isEmpty(id)) {
			Partner partner = partnerService.getPartnerById(id);
			model.addAttribute("sourceUrl", Global.SERVER_SOURCE_URL);
			model.addAttribute("partner", partner);
		}
		return "common/partnerInfo";
	}

	/**
	 * 增加合作伙伴
	 * 
	 * @param partner
	 * @return
	 */
	@RequestMapping(value = "addPartner", method = RequestMethod.POST)
	public String addPartner(MultipartHttpServletRequest file,
			HttpServletRequest request, Partner partner, Model model) {
		try {
			String id = CreateIdentityId.getInstance().createId();
			partner.setId(id);
			partner.setStatus(PartnerState.PARTNER_APPLY);
			setPartFile(file, request, partner);
			partnerService.addPartner(partner, SessionUtil.getCurrentUser());
			operaLogService.addOperaLog(OPERA_TYPE_ADD, request, BUSI_NAME,
					"添加合作伙伴，合作伙伴名称：" + partner.getPartnerName());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return newErrorModelView(model, e.getMessage());
		}
		return "redirect:/partner/findPartner.do";
	}

	/**
	 * 设置是否上传图片
	 * 
	 * @param file
	 * @param request
	 * @param partner
	 */
	private void setPartFile(MultipartHttpServletRequest file,
			HttpServletRequest request, Partner partner) throws Exception {
		MultipartFile merchantImgFile = file.getFile("merchantImgFile");
		MultipartFile identitycardImgFile = file.getFile("identitycardImgFile");
		MultipartFile businesslicenseFile = file.getFile("businesslicenseFile");
		MultipartFile otherImgFile = file.getFile("otherImgFile");
		checkPartFileSize(merchantImgFile, identitycardImgFile,
				businesslicenseFile, otherImgFile);
		try {
			String merchantImg = UploadFile.uploadFileToServer(merchantImgFile,
					partner.getId(), basePath, "");
			String identitycardImg = UploadFile.uploadFileToServer(
					identitycardImgFile, partner.getId(), "",
					basePath);
			String businesslicense = UploadFile.uploadFileToServer(
					businesslicenseFile, partner.getId(), "",
					basePath);
			String otherImg = UploadFile.uploadFileToServer(otherImgFile,
					partner.getId(), basePath, "");

			if (!StringUtil.isEmpty(merchantImg))
				partner.setMerchantImg(merchantImg);
			if (!StringUtil.isEmpty(identitycardImg))
				partner.setIdentitycardImg(identitycardImg);
			if (!StringUtil.isEmpty(businesslicense))
				partner.setBusinesslicense(businesslicense);
			if (!StringUtil.isEmpty(otherImg))
				partner.setOtherImg(otherImg);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new OTTException(UploadCode.FILE_IO_UPLOAD_ERROR, e);
		}
	}

	private void checkPartFileSize(MultipartFile merchantImgFile,
			MultipartFile identitycardImgFile,
			MultipartFile businesslicenseFile, MultipartFile otherImgFile)
			throws Exception {
		if (null != merchantImgFile) {
			if (merchantImgFile.getSize() > PARTNER_MAX_IMG) {
				throw new OTTRuntimeException(UploadCode.PARTNER_OVER_MAX_SIZE);
			}
		}
		if (null != identitycardImgFile) {
			if (identitycardImgFile.getSize() > PARTNER_MAX_IMG) {
				throw new OTTRuntimeException(UploadCode.PARTNER_OVER_MAX_SIZE2);
			}
		}
		if (null != businesslicenseFile) {
			if (businesslicenseFile.getSize() > PARTNER_MAX_IMG) {
				throw new OTTRuntimeException(UploadCode.PARTNER_OVER_MAX_SIZE3);
			}
		}
		if (null != otherImgFile) {
			if (otherImgFile.getSize() > PARTNER_MAX_IMG) {
				throw new OTTRuntimeException(UploadCode.PARTNER_OVER_MAX_SIZE4);
			}
		}
	}

	/**
	 * 修改合作伙伴
	 * 
	 * @param request
	 * @param partner
	 * @return
	 */
	@RequestMapping(value = "modifyPartner", method = RequestMethod.POST)
	public String modifyPartner(MultipartHttpServletRequest file,
			HttpServletRequest request, Partner partner, Model model) {
		try {
			setPartFile(file, request, partner);
			partner.setStatus(PartnerState.PARTNER_APPLY);
			Partner old = partnerService.getPartnerById(partner.getId());
			Partner clone = (Partner) ObjectUtil.clone(partner);
			String diffStr = "";
			try {
				diffStr = ObjectUtil.getDiffColumnDescript(old, clone);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			partnerService.modifyPartner(partner, SessionUtil.getCurrentUser());
			if (StringUtil.isNotEmpty(diffStr)) {
				operaLogService.addOperaLog(OPERA_TYPE_MODIFY, request,
						BUSI_NAME, diffStr);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return newErrorModelView(model, e.getMessage());
		}
		return "redirect:/partner/findPartner.do";
	}

	/**
	 * 注销合作伙伴
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "modifyPartnerClose", method = RequestMethod.POST)
	public @ResponseBody
	Feedback modifyPartnerClose(@RequestParam("id") String id,
			HttpServletRequest request) {
		try {
			partnerService.modifyPartnerStatus(State.INVALID, id);
			operaLogService.addOperaLog(OPERA_TYPE_DELETE, request, BUSI_NAME,
					"注销合作伙伴，合作伙伴编号：" + id);
			return Feedback.success("合作伙伴注销成功");
		} catch (RuntimeException e) {
			return Feedback.fail("合作伙伴注销失败");
		}
	}

	/**
	 * 启用合作伙伴
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "modifyPartnerReverse", method = RequestMethod.POST)
	public @ResponseBody
	Feedback modifyPartnerReverse(@RequestParam("id") String id,
			HttpServletRequest request) {
		try {
			partnerService.modifyPartnerStatus(State.VALID, id);
			operaLogService.addOperaLog(OPERA_TYPE_DELETE, request, BUSI_NAME,
					"启用合作伙伴，合作伙伴编号：" + id);
			return Feedback.success("合作伙伴启用成功");
		} catch (RuntimeException e) {
			return Feedback.fail("合作伙伴启用失败");
		}
	}

	/**
	 * 删除合作伙伴(逻辑删除)
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delPartner", method = RequestMethod.POST)
	public @ResponseBody
	Feedback delPartner(@RequestParam("id") String id,
			HttpServletRequest request) {
		if (StringUtil.isEmpty(id)) {
			return Feedback.fail("合作伙伴删除失败,请选择要删除的合作伙伴");
		}
		try {
			partnerService.modifyPartnerStatus(id.split(","));
			operaLogService.addOperaLog(OPERA_TYPE_DELETE, request, BUSI_NAME,
					"删除合作伙伴，合作伙伴编号：" + id);
			return Feedback.success("合作伙伴删除成功");
		} catch (RuntimeException e) {
			return Feedback.fail("合作伙伴删除失败");
		}
	}
}
