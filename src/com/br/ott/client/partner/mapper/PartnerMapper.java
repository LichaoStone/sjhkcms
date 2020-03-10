/*
 * @# PartnerMapper.java Aug 1, 2012 11:15:05 AM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.client.partner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.br.ott.client.partner.entity.Partner;

/*
 * @author pananz
 * TODO
 */
public interface PartnerMapper {

	/**
	 * 增加合作伙伴信息
	 * @param partner
	 */
	void addPartner(Partner partner);

	/**
	 * 修改合作伙伴信息
	 * @param partner
	 */
	void modifyPartner(Partner partner);

	/**
	 * 修改合作伙伴状态
	 * @param state
	 * @param id
	 */
	void modifyPartnerStatus(@Param("status")
	String status, @Param("id")
	String id);

	/**
	 * 删除合作伙伴
	 * @param ids
	 */
	void delPartner(String[] ids);

	/**
	 * 按ID查询合作伙伴信息
	 * @param id
	 * @return
	 */
	Partner getPartnerById(String id);

	Partner getPartnerId(String id);

	/**
	 * 按分页查询合作伙伴信息
	 * @param partner
	 * @param rowBounds
	 * @return
	 */
	List<Partner> findPartner(Partner partner, RowBounds rowBounds);

	/**
	 * 查询合作伙伴总记录数
	 * @param partner
	 * @return
	 */
	int getCountPartner(Partner partner);

	/**
	 * 修改合作伙伴合同编号
	 * 创建人：pananz
	 * 创建时间：2013-3-14 - 上午10:04:28
	 * @param contractNumber
	 * @param id
	 * 返回类型：void
	 * @exception throws
	 */
	void modifyPartnerContract(@Param("contractNumber")
	String contractNumber, @Param("id")
	String id);
	
	int getPartnerBySPid(@Param("SPid")String SPid);
}
