package com.br.ott.client.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ott.client.select.entity.DramaProgram;
import com.br.ott.client.select.entity.DramaSource;
import com.br.ott.client.select.mapper.DramaProgramMapper;
import com.br.ott.client.select.mapper.DramaSourceMapper;
import com.br.ott.client.user.entity.UserAccount;
import com.br.ott.client.user.entity.UserOrder;
import com.br.ott.client.user.mapper.UserAccountMapper;
import com.br.ott.client.user.mapper.UserOrderMapper;
import com.br.ott.common.exception.OTTException;
import com.br.ott.common.exception.SystemExceptionCode.ApiCode;
import com.br.ott.common.util.StringUtil;
import com.br.ott.common.util.id.CreateIdentityId;

/* 
 *  
 *  文件名：UserOrderApiService.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2015-3-16 - 上午10:13:20
 */
@Service
public class UserOrderApiService {

	private static Logger log = Logger.getLogger(UserOrderApiService.class);
	
	@Autowired
	private UserOrderMapper userOrderMapper;

	@Autowired
	private DramaSourceMapper dramaSourceMapper;
	
	@Autowired
	private DramaProgramMapper dramaProgramMapper;
	
	@Autowired
	private UserAccountMapper userAccountMapper;

	private boolean checkAccount(String account) {
		UserAccount userAccount = userAccountMapper
				.getUserAccountByUserId(account);
		if (userAccount == null) {
			log.debug("此账号不存在");
			return true;
		}
		if ("-1".equals(userAccount.getStatus())) {
			log.debug("账号已停机");
			return true;
		}
		return false;
	}

	/**
	 * 点播订购
	 * 
	 * 创建人：pananz 创建时间：2015-3-16 - 上午10:15:15
	 * 
	 * @param request
	 * @param json
	 * @return
	 * @throws OTTException
	 *             返回类型：String
	 * @exception throws
	 */
	public String orderProgram(HttpServletRequest request, JSONObject json)
			throws OTTException {
		String account = "";
		String mac = "";
		String programId = "";
		String type = "";
		String num = "";
		try {
			account = StringUtil.getJsonStr(json, "account");
			mac = StringUtil.getJsonStr(json, "mac");
			type = StringUtil.getJsonStr(json, "type");
			num = StringUtil.getJsonStr(json, "num");
			programId = StringUtil.getJsonStr(json, "programId");
		} catch (Exception e) {
			e.printStackTrace();
			throw new OTTException(ApiCode.DECODE_JSON_ERROR);
		}
		log.debug("请求参数account: " + account + " ,programId: " + programId
				+ " ,type: " + type + " ,mac: " + mac);
		if (checkAccount(account)) {
			return "{\"result\":\"0\", \"msg\":\"账号不存在\"}";
		}
		String programName = "";
		float price = 0.0f;
		String playUrl = "";
		// 校验订购的节目是否存在
		if ("1".equals(type)) {
			 DramaProgram program = dramaProgramMapper
						 .getDramaProgramById(programId);
			if (program == null) {
				log.debug("您要预定的节目不存在");
				return "{\"result\":\"2\", \"msg\":\"您要预定的节目不存在\"}";
			}
			programName = program.getName();
			price = program.getPrice();
			playUrl = program.getPlayUrl();
		} else if ("2".equals(type)) {
			DramaSource dramaSource=new DramaSource();
			dramaSource.setChildId(num);
			List<DramaSource> dsList = dramaSourceMapper.findDramaSourceByCond(dramaSource);
			if (CollectionUtils.isEmpty(dsList)) {
				log.debug("您要预定的节目不存在2");
				return "{\"result\":\"2\", \"msg\":\"您要预定的节目不存在\"}";
			}
		}

		UserOrder userOrder = new UserOrder();
		userOrder.setProgramId(programId);
		userOrder.setType(type);
		userOrder.setAccount(account);
		userOrder.setStatus("1");
		List<UserOrder> list = userOrderMapper.findUserOrderByCond(userOrder);
		if (CollectionUtils.isNotEmpty(list)) {
			log.debug("已预定过该节目");
			list = null;
			return "{\"result\":\"3\", \"msg\":\"已预定过该节目\"}";
		}
		userOrder.setMac(mac);
		userOrder.setPrice(price);
		userOrder.setProgramName(programName);
		userOrder.setOrderId(CreateIdentityId.getInstance().createId2());
		try {
			userOrderMapper.addUserOrder(userOrder);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("预定此节目异常");
			return "{\"result\":\"4\", \"msg\":\"预定此节目异常\"}";
		}
		log.debug("成功预定此节目");
		return "{\"result\":\"1\", \"msg\":\"成功预定此节目\", \"playUrl\":\""
				+ playUrl + "\"}";
	}

	/**
	 * 节目鉴权
	 * 
	 * 创建人：pananz 创建时间：2015-3-19 - 下午4:56:11
	 * 
	 * @param request
	 * @param json
	 * @return
	 * @throws OTTException
	 *             返回类型：String
	 * @exception throws
	 */
	public String authentication(HttpServletRequest request, JSONObject json)
			throws OTTException {
		String account = "";
		String programId = "";
		String type = "";
		String num = "";
		try {
			account = StringUtil.getJsonStr(json, "account");
			programId = StringUtil.getJsonStr(json, "programId");
			type = StringUtil.getJsonStr(json, "type");
			num = StringUtil.getJsonStr(json, "num");
		} catch (Exception e) {
			e.printStackTrace();
			throw new OTTException(ApiCode.DECODE_JSON_ERROR);
		}
		log.debug("请求参数account: " + account + " ,programId: " + programId
				+ " ,type: " + type);

		if (checkAccount(account)) {
			return "{\"result\":\"0\", \"msg\":\"账号不存在\"}";
		}
		// 校验订购的节目是否存在
		String playUrl = "";
		if ("1".equals(type)) {
			 DramaProgram program = dramaProgramMapper
					 .getDramaProgramById(programId);
			if (program == null) {
				log.debug("鉴权的节目不存在");
				return "{\"result\":\"2\", \"msg\":\"鉴权的节目不存在\"}";
			}
			playUrl = program.getPlayUrl();
		} else if ("2".equals(type)) {// 资产点播
			DramaSource dramaSource=new DramaSource();
			dramaSource.setChildId(num);
			List<DramaSource> dsList = dramaSourceMapper.findDramaSourceByCond(dramaSource);
			if (CollectionUtils.isEmpty(dsList)) {
				log.debug("鉴权的节目不存在2");
				return "{\"result\":\"2\", \"msg\":\"鉴权的节目不存在\"}";
			}
		}
		UserOrder userOrder = new UserOrder();
		userOrder.setProgramId(programId);
		userOrder.setType(type);
		userOrder.setAccount(account);
		userOrder.setStatus("1");
		List<UserOrder> list = userOrderMapper.findUserOrderByCond(userOrder);
		if (CollectionUtils.isEmpty(list)) {
			log.debug("未预定过该节目");
			return "{\"result\":\"3\", \"msg\":\"未预定过该节目\"}";
		}
		list = null;
		log.debug("此节目鉴权通过");
		return "{\"result\":\"1\", \"msg\":\"此节目鉴权通过\", \"playUrl\":\""
				+ playUrl + "\"}";
	}
}
