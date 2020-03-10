/*
 * @# Partner.java Aug 1, 2012 11:07:27 AM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.client.partner.entity;

import java.util.Map;

import com.br.ott.client.common.DictCache;
import com.br.ott.client.common.DistrictCache;
import com.br.ott.client.common.entity.Dictionary;
import com.br.ott.client.common.entity.District;
import com.br.ott.common.util.Constants.DicType;
import com.br.ott.common.util.Constants.PartnerState;
import com.br.ott.common.util.StringUtil;

/*
 * @author pananz
 * TODO
 * 合作伙伴
 */
public class Partner {
	// 合作伙伴编号
	private String id;
	// 合作伙伴名称
	private String partnerName;
	// 合作伙伴类型：（1,运营商，2平台商，3企业内容提供商，4.个人内容提供者，5渠道商，6广告商，7品牌/版权提供商）
	private String partnerType;
	// 合作伙伴地址
	private String partnerAddress;
	// 取字典表相应键
	private String partnerCity;
	// 手机号码
	private String partnerPhone;
	// 其他联系方式
	private String otherContact;
	// 联系人
	private String linkman;
	// 合作伙伴描述
	private String partnerDesc;
	// 合作伙伴图片
	private String merchantImg;
	// 创建合作伙伴时间
	private String createTime;
	// 合作伙伴信息状态：1有效0无效 default '1'
	private String status;
	// 身份证
	private String identitycardImg;
	// 营业执照
	private String businesslicense;
	// 其他图片
	private String otherImg;
	// 准入或上线
	private String readyOrPass;
	// 第三方平台注册id
	private String SPid;

	// 公司法人代表
	private String legal;

	private String fullName;
	// 公司简称
	private String simpleName;
	// 业务负责人
	private String competent;
	// 业务负责人电话
	private String competentPhone;
	// 业务负责人邮箱
	private String competentEmail;
	// 注册资金
	private String funds;
	// 客服联系人
	private String contact;
	// 客服联系人电话
	private String contactPhone;
	// 客服联系人邮箱
	private String contactEmail;
	// 合同编号(以电信为准)
	private String contractNumber;

	/**
	 * 要排序的字段
	 */
	private String orderColumn;

	/**
	 * 强制删除标识
	 */
	private String noShelf;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
	}

	public String getPartnerAddress() {
		return partnerAddress;
	}

	public void setPartnerAddress(String partnerAddress) {
		this.partnerAddress = partnerAddress;
	}

	public String getPartnerCity() {
		return partnerCity;
	}

	public void setPartnerCity(String partnerCity) {
		this.partnerCity = partnerCity;
	}

	public String getPartnerPhone() {
		return partnerPhone;
	}

	public void setPartnerPhone(String partnerPhone) {
		this.partnerPhone = partnerPhone;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getPartnerDesc() {
		return partnerDesc;
	}

	public void setPartnerDesc(String partnerDesc) {
		this.partnerDesc = partnerDesc;
	}

	public String getMerchantImg() {
		return merchantImg;
	}

	public void setMerchantImg(String merchantImg) {
		this.merchantImg = merchantImg;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusName() {
		Map<String, String> map = PartnerState.getPartnerStatus();
		return map.get(this.status);
	}

	public String getIdentitycardImg() {
		return identitycardImg;
	}

	public void setIdentitycardImg(String identitycardImg) {
		this.identitycardImg = identitycardImg;
	}

	public String getBusinesslicense() {
		return businesslicense;
	}

	public void setBusinesslicense(String businesslicense) {
		this.businesslicense = businesslicense;
	}

	public String getOtherImg() {
		return otherImg;
	}

	public void setOtherImg(String otherImg) {
		this.otherImg = otherImg;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getNoShelf() {
		return noShelf;
	}

	public void setNoShelf(String noShelf) {
		this.noShelf = noShelf;
	}

	public String getOtherContact() {
		return otherContact;
	}

	public void setOtherContact(String otherContact) {
		this.otherContact = otherContact;
	}

	public String getReadyOrPass() {
		return readyOrPass;
	}

	public void setReadyOrPass(String readyOrPass) {
		this.readyOrPass = readyOrPass;
	}

	public String getSPid() {
		return SPid;
	}

	public void setSPid(String sPid) {
		SPid = sPid;
	}

	public String getPartnerTypeName() {
		if (StringUtil.isEmpty(this.partnerType)) {
			return "";
		} else {
			Dictionary dictionary = DictCache.getDictValue(
					DicType.MERCHANT_TYPE, partnerType);
			return null == dictionary ? "" : dictionary.getDicName();
		}
	}

	public String getPartnerCityName() {
		if (StringUtil.isEmpty(this.partnerCity)) {
			return "";
		} else {
			// Dictionary dictionary = DictCache.getCity(this.partnerCity);
			// return null == dictionary ? "" : dictionary.getDicName();
			District district = DistrictCache.getCityByAreaCode(partnerCity);
			return null == district ? "" : district.getName();
		}
	}

	/**
	 * 合作伙伴是否可以删除(处于失效，待审核， 管理员审核异常， 商务审核异常）
	 * 
	 * @return
	 */
	public boolean isDelPartner() {
		if (StringUtil.isEmpty(this.status)) {
			return false;
		}
		if (PartnerState.PARTNER_INVALID.equals(this.status)
				|| PartnerState.PARTNER_APPLY.equals(this.status)
				|| PartnerState.PARTNER_EXP_MANAGER.equals(this.status)
				|| PartnerState.PARTNER_EXP_BUS.equals(this.status)) {
			return false;
		}
		return true;
	}

	public String getLegal() {
		return legal;
	}

	public void setLegal(String legal) {
		this.legal = legal;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getCompetent() {
		return competent;
	}

	public void setCompetent(String competent) {
		this.competent = competent;
	}

	public String getFunds() {
		return funds;
	}

	public void setFunds(String funds) {
		this.funds = funds;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getCompetentPhone() {
		return competentPhone;
	}

	public void setCompetentPhone(String competentPhone) {
		this.competentPhone = competentPhone;
	}

	public String getCompetentEmail() {
		return competentEmail;
	}

	public void setCompetentEmail(String competentEmail) {
		this.competentEmail = competentEmail;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

}
