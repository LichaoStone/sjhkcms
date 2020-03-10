package com.br.ott.client.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.ott.client.common.DictCache;
import com.br.ott.client.common.NavigationCache;
import com.br.ott.client.common.entity.AssetFile;
import com.br.ott.client.common.entity.AssetInfo;
import com.br.ott.client.common.entity.AssetSource;
import com.br.ott.client.common.entity.Dictionary;
import com.br.ott.client.select.entity.AssetLog;
import com.br.ott.client.select.entity.DramaChildren;
import com.br.ott.client.select.entity.DramaNavigation;
import com.br.ott.client.select.entity.DramaProgram;
import com.br.ott.client.select.entity.DramaSource;
import com.br.ott.client.select.entity.DramaType;
import com.br.ott.client.select.mapper.AssetLogMapper;
import com.br.ott.client.select.mapper.DramaChildrenMapper;
import com.br.ott.client.select.mapper.DramaProgramMapper;
import com.br.ott.client.select.mapper.DramaSourceMapper;
import com.br.ott.client.select.mapper.DramaTypeMapper;
import com.br.ott.common.exception.OTTException;
import com.br.ott.common.exception.SystemExceptionCode.SyncCode;
import com.br.ott.common.util.JsonUtils;
import com.br.ott.common.util.StringUtil;
import com.br.ott.common.util.Constants.DicType;

/**
 * 文件名：AssetLogService.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-12
 */
@Service
public class AssetLogService {
	private static Logger log = Logger.getLogger(AssetLogService.class);
	@Autowired
	private AssetLogMapper assetLogMapper;
	@Autowired
	private DramaProgramMapper dramaProgramMapper;
	@Autowired
	private DramaSourceMapper dramaSourceMapper;
	@Autowired
	private DramaTypeMapper dramaTypeMapper;
	@Autowired
	private DramaChildrenMapper dramaChildrenMapper;

	/**
	 * 同步华数资产信息到平台
	 * 
	 * 创建人：pananz 创建时间：2016-9-13
	 * 
	 * @param @param request
	 * @param @return
	 * @param @throws OTTException 返回类型：String
	 * @exception throws
	 */

	@Transactional
	public String syncTvodsInfo(HttpServletRequest request) throws OTTException {
		String jsonString = StringUtil.requestGetStreamToString(request);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("files", AssetSource.class);
		map.put("file", AssetFile.class);

		AssetInfo assetInfo = (AssetInfo) JsonUtils.getDTO(jsonString,
				AssetInfo.class, map);
		if (assetInfo == null) {
			log.error("参数格式解析异常");
			addAssetLog(null, jsonString,
					"{\"result\":\"1\", \"msg\":\"参数格式解析异常\"}", "1");
			return "{\"result\":\"1\", \"msg\":\"参数格式解析异常\"}";
		}
		if (StringUtil.isEmpty(assetInfo.getAssetTitle())) {
			log.error("节目名称不能为空");
			addAssetLog(assetInfo, jsonString,
					"{\"result\":\"1\", \"msg\":\"节目名称不能为空\"}", "1");
			return "{\"result\":\"1\", \"msg\":\"节目名称不能为空\"}";
		}

		try {
			DramaProgram dramaProgram = new DramaProgram();
			dramaProgram.setAssetId(assetInfo.getAssetId());
			List<DramaProgram> list = dramaProgramMapper
					.findDramaProgramByCond(dramaProgram);
			if ("OFF".equals(assetInfo.getAction())) {// 下线
				if (CollectionUtils.isEmpty(list)) {
					log.error("无此下线节目信息");
					addAssetLog(assetInfo, jsonString,
							"{\"result\":\"1\", \"msg\":\"无此下线节目信息\"}", "1");
					return "{\"result\":\"1\", \"msg\":\"无此下线节目信息\"}";
				} else {
					DramaProgram pro = list.get(0);
					dramaProgramMapper.updateDramaProgramStatus("2",
							pro.getId());
					addAssetLog(assetInfo, jsonString,
							"{\"result\":\"0\", \"msg\":\"节目下线同步成功\"}", "1");
					return "{\"result\":\"0\", \"msg\":\"节目下线同步成功\"}";
				}
			}
			DramaProgram dp = new DramaProgram();
			dp.setAssetId(assetInfo.getAssetId());
			dp.setActor(assetInfo.getAssetActor());
			dp.setcProvider("HS");
			dp.setKeyword(assetInfo.getKeywords());
			dp.setLanguage(assetInfo.getAssetLanguage());
			dp.setName(assetInfo.getAssetTitle());
			dp.setOperator("华数系统同步");
			dp.setOrigin(assetInfo.getAssetArea());
			dp.setPlayyear(assetInfo.getYear());
			dp.setDirector(assetInfo.getAssetDirector());
			dp.setRemark(assetInfo.getAssetDescription());
			dp.setSummary(assetInfo.getAssetSummary());
			dp.setpId(assetInfo.getAssetStatus());
			dp.setLinkUrl(assetInfo.getLinkUrl());
			dp.setStatus("0");
			String assetType = assetInfo.getAssetType();
			dp.setOldType(assetType);
			dp.setIsLoad("0");
			// 取得简拼
			dp.setPcode(StringUtil.getPinYinHeadChar(assetInfo.getAssetTitle())
					.toUpperCase());
			// 类型名称转ID映射处理
			if (StringUtil.isNotEmpty(assetInfo.getAssetMode())) {
				List<Dictionary> ptypes = DictCache.getDictList(DicType.JMLX);
				for (Dictionary dict : ptypes) {
					if (dict.getDicName().equals(assetInfo.getAssetMode())) {
						log.debug("一级类型对应" + dict.getDicName());
						dp.setPtype(dict.getDicValue());
						break;
					}
				}
			}

			if (CollectionUtils.isNotEmpty(list)) {
				if ("1".equals(assetInfo.getAssetStatus())) {// 跟播资产
					DramaProgram program = list.get(0);
					dp.setId(program.getId());

					List<AssetSource> files = assetInfo.getFiles();
					if (CollectionUtils.isNotEmpty(files)) {
						for (AssetSource as : files) {
							DramaChildren child = new DramaChildren();
							child.setDramaId(dp.getId());
							child.setTitle(as.getEpisode());
							List<DramaChildren> dcList = dramaChildrenMapper
									.findDramaChildrenByCond(child);
							if (CollectionUtils.isNotEmpty(dcList)) {
								log.error(assetInfo.getAssetTitle() + "已存在此剧集"
										+ as.getEpisode());
								continue;
							}

							DramaChildren dc = new DramaChildren();
							dc.setTitle(as.getEpisode());
							dc.setDramaId(dp.getId());
							dc.setStatus("0");
							dc.setOperator("华数系统同步");
							dramaChildrenMapper.addDramaChildren(dc);

							List<AssetFile> afList = as.getFile();
							for (AssetFile af : afList) {
								DramaSource dramaSource = new DramaSource();
								dramaSource.setChildId(dc.getId());
								dramaSource.setPlayUrl(af.getPlayUrl());
								dramaSource.setBitrate(af.getAssetBitrate());
								dramaSource.setFtpUrl(af.getFtpUrl());
								dramaSource.setMd5(af.getMd5());
								dramaSource.setFileId(af.getFileId());
								dramaSource.setStatus("0");
								dramaSource.setBitrateTypeStr();
								dramaSourceMapper.addDramaSource(dramaSource);
							}
						}
					}
				} else {
					DramaProgram program = list.get(0);
					dp.setId(program.getId());
					if (!"1".equals(program.getStatus())) {
						dramaProgramMapper.updateDramaProgram(dp);
					} else {
						addAssetLog(
								assetInfo,
								jsonString,
								"{\"result\":\"1\", \"msg\":\"此节目信息已经过融媒平台审核上线\"}",
								"1");
						return "{\"result\":\"1\", \"msg\":\"此节目信息已经过融媒平台审核上线\"}";
					}
				}

			} else {
				dp.setRecDImg(assetInfo.getRecommendDImg());
				dp.setRecXImg(assetInfo.getRecommendXImg());
				dp.setCoverDImg(assetInfo.getCoverDImg());
				dp.setCoverXImg(assetInfo.getCoverXImg());

				dramaProgramMapper.addDramaProgram(dp);
				List<AssetSource> files = assetInfo.getFiles();
				log.debug("files size is" + files.size());
				if (CollectionUtils.isNotEmpty(files)) {
					for (AssetSource as : files) {
						DramaChildren dc = new DramaChildren();
						dc.setTitle(String.valueOf(as.getEpisode()));
						dc.setDramaId(dp.getId());
						dc.setStatus("0");
						dc.setOperator("华数系统同步");
						dramaChildrenMapper.addDramaChildren(dc);

						List<AssetFile> afList = as.getFile();
						for (AssetFile af : afList) {
							DramaSource dramaSource = new DramaSource();
							dramaSource.setChildId(dc.getId());
							dramaSource.setPlayUrl(af.getPlayUrl());
							dramaSource.setBitrate(String.valueOf(af
									.getAssetBitrate()));
							dramaSource.setFtpUrl(af.getFtpUrl());
							dramaSource.setMd5(af.getMd5());
							dramaSource
									.setFileId(String.valueOf(af.getFileId()));
							dramaSource.setStatus("0");
							dramaSource.setBitrateTypeStr();
							dramaSourceMapper.addDramaSource(dramaSource);
						}
					}
				}
			}
			// 可属于多级类型，通过“-”区分一二级，通过“|”分割多个类型 如：“电影-欧美大片|少儿-动画电影”

			if (StringUtil.isNotEmpty(assetType)) {
				String[] typeArray = assetType.split("\\|");

				for (String type : typeArray) {
					List<DramaNavigation> ttypes = NavigationCache.navList;
					for (DramaNavigation dn : ttypes) {
						String at = dn.getParentName() + "-" + dn.getName();
						if (at.equals(type)) {
							log.debug("二级类型对应" + type);
							DramaType dramaType = new DramaType();
							dramaType.setDramaId(dp.getId());
							dramaType.setNavId(dn.getId());
							dramaTypeMapper.addDramaType(dramaType);
						}
					}
				}
			}
			addAssetLog(assetInfo, jsonString,
					"{\"result\":\"0\", \"msg\":\"节目上线同步成功\"}", "0");
			return "{\"result\":\"0\", \"msg\":\"节目上线同步成功\"}";
		} catch (Exception e) {
			log.error("节目同步数据操作异常" + e.getMessage());
			addAssetLog(assetInfo, jsonString,
					"{\"result\":\"1\", \"msg\":\"节目同步数据操作异常" + e.getMessage()
							+ "\"}", "1");
			throw new OTTException(SyncCode.SYNC_TVOD_ERROR, e);

		}
	}

	private void addAssetLog(AssetInfo assetInfo, String jsonStr,
			String returnInfo, String result) {
		AssetLog assetLog = new AssetLog();
		if (assetInfo != null) {
			assetLog.setAssetId(assetInfo.getAssetId());
			assetLog.setAssetTitle(assetInfo.getAssetTitle());
			assetLog.setAction(assetInfo.getAction());
		}
		assetLog.setAssetInfo(jsonStr);
		assetLog.setReturnInfo(returnInfo);
		assetLog.setResult(result);
		assetLogMapper.addAssetLog(assetLog);
	}
}
