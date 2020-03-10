/*
 * @# PartnerService.java Aug 1, 2012 11:36:57 AM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.client.partner.service;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.partner.entity.Partner;
import com.br.ott.client.partner.mapper.PartnerMapper;
import com.br.ott.client.shiro.entity.UserInfo;
import com.br.ott.common.util.Constants.PartnerState;
import com.br.ott.common.util.PagedModelList;

/*
 * @author pananz
 * TODO
 * 合作伙伴业务接口
 */

@Service
public class PartnerService {

	@Autowired
	private PartnerMapper partnerMapper;

	/**
	 * 增加合作伙伴信息
	 * 
	 * @param partner
	 */
	public void addPartner(Partner partner, UserInfo userInfo) {
		partnerMapper.addPartner(partner);
	}

	/**
	 * 修改合作伙伴信息
	 * 
	 * @param partner
	 */
	public void modifyPartner(Partner partner, UserInfo userInfo) {
		partnerMapper.modifyPartner(partner);
	}

	/**
	 * 修改合作伙伴信息状态
	 * 
	 * @param state
	 * @param id
	 */
	public void modifyPartnerStatus(String state, String id) {
		partnerMapper.modifyPartnerStatus(state, id);
	}

	/**
	 * 删除合作伙伴信息
	 * 
	 * @param ids
	 */
	public void delPartner(String[] ids) {
		partnerMapper.delPartner(ids);
	}

	/**
	 * 强制下架
	 * 
	 * @param ids
	 */
	public void modifyPartnerStatus(String[] ids) {
		for (String id : ids) {
			modifyPartnerStatus(PartnerState.PARTNER_FORCED_SHELF, id);
		}
	}

	/**
	 * 按ID查询合作伙伴信息
	 * 
	 * @param id
	 * @return
	 */
	public Partner getPartnerById(String id) {
		return partnerMapper.getPartnerById(id);
	}

	public Partner getPartnerId(String id) {
		return partnerMapper.getPartnerId(id);
	}

	/**
	 * 按分页查询合作伙伴信息
	 * 
	 * @param merchant
	 * @param skip第几页
	 * @return
	 */
	public PagedModelList<Partner> findPartner(Partner merchant, int skip,
			int row) {
		int totalCount = partnerMapper.getCountPartner(merchant);
		PagedModelList<Partner> pml = new PagedModelList<Partner>(skip, row,
				totalCount);
		pml.addModels(partnerMapper.findPartner(merchant, new RowBounds(
				(skip - 1) * row, row)));
		return pml;
	}

	/**
	 * 是否存在该SPID的合作伙伴
	 * 
	 * @param spid
	 * @return
	 */
	public boolean getPartnerBySPid(String spid) {
		int count = partnerMapper.getPartnerBySPid(spid);
		if (count > 0) {
			return true;
		}
		return false;
	}
}
