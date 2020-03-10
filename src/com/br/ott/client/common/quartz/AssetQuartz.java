package com.br.ott.client.common.quartz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.br.ott.Global;
import com.br.ott.client.api.service.ApiForDbService;
import com.br.ott.client.common.DictCache;
import com.br.ott.client.common.NavigationCache;
import com.br.ott.client.common.entity.AssetFile;
import com.br.ott.client.common.entity.AssetInfo;
import com.br.ott.client.common.entity.AssetSource;
import com.br.ott.client.common.entity.Dictionary;
import com.br.ott.client.operasset.service.HandleAssetsService;
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
import com.br.ott.client.select.service.DramaNavigationService;
import com.br.ott.client.select.service.DramaPositionService;
import com.br.ott.client.select.service.DramaProgramService;
import com.br.ott.common.exception.OTTException;
import com.br.ott.common.exception.SystemExceptionCode.SyncCode;
import com.br.ott.common.util.DateUtil;
import com.br.ott.common.util.FileUtil;
import com.br.ott.common.util.FtpFileUtil;
import com.br.ott.common.util.JsonUtils;
import com.br.ott.common.util.StringUtil;
import com.br.ott.common.util.UploadApiUtil;
import com.br.ott.common.util.Constants.DicType;
import com.br.ott.common.util.plugin.cache.impl.EnhancedCachingManagerImpl;
import com.br.ott.common.util.spring.SpringContextHolder;

/**
 * 文件名：AssetQuartz.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights reserved
 * 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-9-12
 */
public class AssetQuartz {
	private static final Logger logger = Logger.getLogger(AssetQuartz.class);

	/**
	 * 每间隔5分钟更新一次数据
	 * 
	 * 创建人：pananz 创建时间：2016-9-12
	 * 
	 * @param 返回类型
	 *            ：void
	 * @exception throws
	 */
	public void checkNavigation() {
		DramaNavigationService navigationService = SpringContextHolder
				.getBean(DramaNavigationService.class);
		navigationService.reloadNavigation();

		DramaPositionService positionService = SpringContextHolder
				.getBean(DramaPositionService.class);
		positionService.reloadPosition();
	}

	/**
	 * 读取华数全量资产文件
	 * 
	 * 创建人：pananz 创建时间：2016-9-28
	 * 
	 * @param 返回类型
	 *            ：void
	 * @exception throws
	 */
	public void doAssetFile() {
		File dir = new File(Global.ASSET_DATA);
		if (dir.exists() && dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files.length == 0) {
				return;
			}

			for (File fDir : files) {
				String fileName = fDir.getName();
				// 不是文件或文件名以_bak结束的，去除
				if (fDir.isDirectory()) {
					if (fileName.endsWith("_bak") || fileName.equals("temp")) {
						logger.debug("此文件夹名以_bak结束或是临时目录文件的");
						continue;
					}
					File[] cfiles = fDir.listFiles();
					if (cfiles.length == 0) {
						logger.debug("此文件夹下没JSON文件");
						return;
					}
					for (File f : cfiles) {
						try {
							readAssetDir(f);
						} catch (Exception e) {
							logger.error("读取文件出错:" + e.getMessage());
							// 增加文件迁移到临时目录
							try {
								File destFile = new File(Global.ASSET_DATA
										+ "/temp/" + fileName);
								FileUtils.copyFile(f, destFile);
							} catch (IOException e1) {
								logger.error("迁移读取异常文件出错:" + e1.getMessage());
							}
							continue;
						}
					}
					// 增加重命名文件夹
					File dest = new File(fDir.getParent() + "/" + fileName
							+ "_" + DateUtil.getCurrentYMD() + "_bak");
					fDir.renameTo(dest);
					logger.debug(fileName + "文件夹重命名....");
				} else {
					String ext = FilenameUtils.getExtension(fileName);
					if ("json".equals(ext)) {
						try {
							readAssetDir(fDir);
						} catch (Exception e) {
							logger.error("读取文件出错:" + e.getMessage());
							// 增加文件迁移到临时目录
							try {
								File destFile = new File(Global.ASSET_DATA
										+ "/temp/" + fileName);
								FileUtils.copyFile(fDir, destFile);
							} catch (IOException e1) {
								logger.error("迁移读取异常文件出错:" + e1.getMessage());
							}
						}
					} else {
						logger.debug("此是文件不是以json后缀文件的");
					}
				}
			}
		}
	}

	/**
	 * 读取文件夹下的文件
	 * 
	 * 创建人：pananz 创建时间：2016-9-28
	 * 
	 * @param @param f 返回类型：void
	 * @exception throws
	 */
	private void readAssetDir(File f) {
		String fileName = f.getName();
		String ext = FilenameUtils.getExtension(fileName);
		logger.debug("文件:" + f.getAbsolutePath() + "==" + fileName + "后缀="
				+ ext);
		if ("json".equals(ext)) {
			InputStreamReader read = null;
			BufferedReader reader = null;
			try {
				read = new InputStreamReader(new FileInputStream(f), "utf-8");// 考虑到编码格式
				reader = new BufferedReader(read);
				StringBuffer sb = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				boolean flag = false;
				try {
					readAssetFile(sb.toString());
					flag = true;
				} catch (OTTException e) {
					logger.error("读取JSON文件异常：" + e.getMessage());
					return;
				}

				if (flag) {
					File dest = new File(f.getParent() + "/"
							+ fileName.substring(0, fileName.indexOf("json"))
							+ "bak");
					// 修改文件后缀
					f.renameTo(dest);
					logger.debug("修改文件后缀,结束本次数据写入:" + fileName);
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				logger.error("编码格式不支持" + e.getMessage());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				logger.error("找不到文件");
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("读取文件异常" + e.getMessage());
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (read != null) {
					try {
						read.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 增加资产同步日志
	 * 
	 * 创建人：pananz 创建时间：2016-9-28
	 * 
	 * @param @param assetInfo
	 * @param @param jsonStr
	 * @param @param returnInfo
	 * @param @param result
	 * @param @param assetLogMapper 返回类型：void
	 * @exception throws
	 */
	private void addAssetLog(AssetInfo assetInfo, String jsonStr,
			String returnInfo, String result, AssetLogMapper assetLogMapper) {
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

	/**
	 * 读取资产文件内容
	 * 
	 * 创建人：pananz 创建时间：2016-9-28
	 * 
	 * @param @param jsonString
	 * @param @return
	 * @param @throws OTTException 返回类型：String
	 * @exception throws
	 */
	public String readAssetFile(String jsonString) throws OTTException {
		AssetLogMapper assetLogMapper = SpringContextHolder
				.getBean(AssetLogMapper.class);
		if (StringUtil.isEmpty(jsonString)) {
			logger.error("参数格式解析异常");
			addAssetLog(null, jsonString,
					"{\"result\":\"1\", \"msg\":\"参数格式解析异常\"}", "1",
					assetLogMapper);
			return "{\"result\":\"1\", \"msg\":\"json内容为空\"}";
		}

		DramaProgramMapper dramaProgramMapper = SpringContextHolder
				.getBean(DramaProgramMapper.class);
		DramaSourceMapper dramaSourceMapper = SpringContextHolder
				.getBean(DramaSourceMapper.class);
		DramaTypeMapper dramaTypeMapper = SpringContextHolder
				.getBean(DramaTypeMapper.class);
		DramaChildrenMapper dramaChildrenMapper = SpringContextHolder
				.getBean(DramaChildrenMapper.class);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("files", AssetSource.class);
		map.put("file", AssetFile.class);

		AssetInfo assetInfo = (AssetInfo) JsonUtils.getDTO(jsonString,
				AssetInfo.class, map);
		if (assetInfo == null) {
			logger.error("参数格式解析异常");
			addAssetLog(null, jsonString,
					"{\"result\":\"1\", \"msg\":\"参数格式解析异常\"}", "1",
					assetLogMapper);
			return "{\"result\":\"1\", \"msg\":\"参数格式解析异常\"}";
		}
		if (StringUtil.isEmpty(assetInfo.getAssetTitle())) {
			logger.error("节目名称不能为空");
			addAssetLog(assetInfo, jsonString,
					"{\"result\":\"1\", \"msg\":\"节目名称不能为空\"}", "1",
					assetLogMapper);
			return "{\"result\":\"1\", \"msg\":\"节目名称不能为空\"}";
		}

		try {
			DramaProgram dramaProgram = new DramaProgram();
			dramaProgram.setAssetId(assetInfo.getAssetId());
			List<DramaProgram> list = dramaProgramMapper
					.findDramaProgramByCond(dramaProgram);
			if ("OFF".equals(assetInfo.getAction())) {// 下线
				if (CollectionUtils.isEmpty(list)) {
					logger.error("无此下线节目信息");
					addAssetLog(assetInfo, jsonString,
							"{\"result\":\"1\", \"msg\":\"无此下线节目信息\"}", "1",
							assetLogMapper);
					return "{\"result\":\"1\", \"msg\":\"无此下线节目信息\"}";
				} else {
					DramaProgram pro = list.get(0);
					dramaProgramMapper.updateDramaProgramStatus("2",
							pro.getId());
					addAssetLog(assetInfo, jsonString,
							"{\"result\":\"0\", \"msg\":\"节目下线同步成功\"}", "1",
							assetLogMapper);
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
			dp.setPcode(StringUtil.getPinYinHeadChar(assetInfo.getAssetTitle()).toUpperCase());
			// 类型名称转ID映射处理
			if (StringUtil.isNotEmpty(assetInfo.getAssetMode())) {
				List<Dictionary> ptypes = DictCache.getDictList(DicType.JMLX);
				for (Dictionary dict : ptypes) {
					if (dict.getDicName().equals(assetInfo.getAssetMode())) {
						logger.debug("一级类型对应" + dict.getDicName());
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
							child.setTitle(as.getEpisode());
							child.setDramaId(dp.getId());
							List<DramaChildren> dcList = dramaChildrenMapper
									.findDramaChildrenByCond(child);
							if (CollectionUtils.isNotEmpty(dcList)) {
								logger.error(assetInfo.getAssetTitle()
										+ "已存在此剧集" + as.getEpisode());
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
								dramaSource.setStatus("1");
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
								"1", assetLogMapper);
						return "{\"result\":\"1\", \"msg\":\"此节目信息已经过融媒平台审核上线\"}";
					}
				}

			} else {

				dp.setRecDImg(assetInfo.getRecommendDImg());
				dp.setRecXImg(assetInfo.getRecommendXImg());
				dp.setCoverDImg(assetInfo.getCoverDImg());
				dp.setCoverXImg(assetInfo.getCoverXImg());
				dp.setIsLoad("0");
				dramaProgramMapper.addDramaProgram(dp);
				List<AssetSource> files = assetInfo.getFiles();
				logger.debug("files size is" + files.size());
				if (CollectionUtils.isNotEmpty(files)) {
					for (AssetSource as : files) {
						DramaChildren dc = new DramaChildren();
						dc.setTitle(String.valueOf(as.getEpisode()));
						dc.setDramaId(dp.getId());
						dc.setStatus("1");
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
							dramaSource.setStatus("1");
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
							logger.debug("二级类型对应" + type);
							DramaType dramaType = new DramaType();
							dramaType.setDramaId(dp.getId());
							dramaType.setNavId(dn.getId());
							dramaTypeMapper.addDramaType(dramaType);
						}
					}
				}
			}
			addAssetLog(assetInfo, jsonString,
					"{\"result\":\"0\", \"msg\":\"节目上线同步成功\"}", "0",
					assetLogMapper);
			return "{\"result\":\"0\", \"msg\":\"节目上线同步成功\"}";
		} catch (Exception e) {
			logger.error("节目同步数据操作异常" + e.getMessage());
			addAssetLog(assetInfo, jsonString,
					"{\"result\":\"1\", \"msg\":\"节目同步数据操作异常" + e.getMessage()
							+ "\"}", "1", assetLogMapper);
			throw new OTTException(SyncCode.SYNC_TVOD_ERROR, e);
		}
	}

	/**
	 * 下载资产图片
	 * 
	 * 创建人：pananz 创建时间：2016-9-22
	 * 
	 * @param 返回类型
	 *            ：void
	 * @exception throws
	 */
	public void checkLoadAssetImage() {
		logger.debug("开始扫描要下载图片的资产");
		DramaProgramMapper dramaProgramMapper = SpringContextHolder
				.getBean(DramaProgramMapper.class);
		DramaProgram dramaProgram = new DramaProgram();
		dramaProgram.setStatus("0");
		dramaProgram.setIsLoad("0");
		List<DramaProgram> dpList = dramaProgramMapper
				.findDramaProgramByCond(dramaProgram);
		if (CollectionUtils.isNotEmpty(dpList)) {
			for (DramaProgram dp : dpList) {
				try {
					String coverDImg = dp.getCoverDImg();
					if (StringUtil.isNotEmpty(dp.getCoverDImg())) {
						coverDImg = getImageInfo(dp.getCoverDImg(), 0);
					}
					// String coverXImg = getImageInfo(dp.getCoverXImg());
					// String recDImg = getImageInfo(dp.getRecDImg());
					// String recXImg = getImageInfo(dp.getRecXImg());

					if (StringUtil.isNotEmpty(coverDImg)) {
						dp.setCoverDImg(coverDImg);
						dp.setIsLoad("1");
					} else {// 注入失败
						dp.setCoverDImg(dp.getCoverDImg());
						dp.setIsLoad("2");
					}
					dp.setCoverXImg(dp.getCoverXImg());
					dp.setRecDImg(dp.getCoverXImg());
					dp.setRecXImg(dp.getRecDImg());
					dramaProgramMapper.updateDramaImage(dp);
				} catch (OTTException e) {
					logger.error("下载图片异常=======" + e.getMessage());
					continue;
				}
			}
		}
		dpList = null;
	}

	/**
	 * 3次下载不成功，不再下载
	 * 
	 * 创建人：pananz 创建时间：2016-9-23
	 * 
	 * @param @param imgUrl
	 * @param @param skip
	 * @param @return
	 * @param @throws OTTException 返回类型：String
	 * @exception throws
	 */
	private String getImageInfo(String imgUrl, int skip) throws OTTException {
		skip++;
		String fileUrl = "";
		try {
			String savePath = Global.SOURCE_PATH + Global.EPG_IMG
					+ File.separator + "temp" + File.separator
					+ DateUtil.getCurrentDate();
			if (!FileUtil.isDirExist(savePath)) {
				logger.debug("生成图片文件目录：" + savePath);
				FileUtil.createDir(savePath);
			}

			File file = null;
			if (imgUrl.indexOf("http") > -1) {
				String filePath = savePath + File.separator
						+ FileUtil.genFileName(imgUrl);
				boolean bool = UploadApiUtil.httpDownload(imgUrl, filePath);
				logger.debug("下载情况:" + bool);
				if (bool) {
					file = new File(filePath);
				} else {
					logger.error("下载图片异常结果:" + bool);
				}
			} else if (imgUrl.indexOf("ftp") > -1) {
				String[] ftpInfo = imgUrl.split(":");
				String ftp_account = ftpInfo[1];

				String ftp_passwd = ftpInfo[2].substring(0,
						ftpInfo[2].lastIndexOf("@"));
				String ftp_hostname = ftpInfo[2].substring(
						ftpInfo[2].lastIndexOf("@") + 1, ftpInfo[2].length());

				String ftp_port = ftpInfo[3].substring(0,
						ftpInfo[3].indexOf("/"));
				String filePath = ftpInfo[3].substring(ftpInfo[3].indexOf("/"),
						ftpInfo[3].length());
				String remotePath = filePath.substring(0,
						filePath.lastIndexOf("/"));
				String fileName = filePath.substring(
						filePath.lastIndexOf("/") + 1, filePath.length());

				logger.debug("remotePath: " + remotePath);

				FtpFileUtil ffu = new FtpFileUtil(ftp_hostname,
						Integer.valueOf(ftp_port), ftp_account, ftp_passwd);

				file = ffu.downloadSingleFile(savePath, remotePath, fileName);
			} else {
				logger.error("图片地址非http或ftp ");
				fileUrl = "";
			}
			if (file == null) {
				logger.error("抓取图片失败" + imgUrl);
				fileUrl = "";
			} else {
				Map<String, String> params = new HashMap<String, String>();
				String secondsDir = Global.EPG_IMG + "/"
						+ DateUtil.getCurrentDate();
				params.put("id", "");
				params.put("fileName", file.getName());
				params.put("secondsDir", secondsDir);
				params.put("changeName", "0");
				HashMap<String, Object> map = UploadApiUtil.uploadFile2(
						Global.SOURCE_UPLOAD_SOURCE_URL, file, params);
				String returnMsg = (String) map.get("returnMsg");
				if (!StringUtil.isEmpty(returnMsg)) {
					logger.error("同步图片失败" + imgUrl);
					fileUrl = "";
				}
				fileUrl = (String) map.get("fileUrl");
			}
		} catch (OTTException e) {
			logger.error("抓取图片异常" + imgUrl);
			fileUrl = "";
		}
		if (StringUtil.isEmpty(fileUrl)) {
			if (skip >= 3) {
				return "";
			} else {
				return getImageInfo(imgUrl, skip);
			}
		} else {
			return fileUrl;
		}
	}

	/**
	 * 每隔5分钟加载一次推荐资产信息到缓存
	 * 
	 * 创建人：lizhengh 创建时间：2016-10-12
	 * 
	 * @param 返回类型
	 *            ：void
	 * @exception throws
	 */
	public void doUpRecDrama() {
		DramaProgramService dramaProgramService = (DramaProgramService) SpringContextHolder
				.getBean(DramaProgramService.class);
		dramaProgramService.reloadDramaProgram();
	}
	
	/**
	 * 每天24:00处理一次用户观看资产状态
	 * 
	 * 创建人：lizhengh 创建时间：2016-11-3
	 * 
	 * @param 返回类型
	 *            ：void
	 * @exception throws
	 */
	public void doUpViewLogStatus(){
		ApiForDbService apiForDbService = (ApiForDbService) SpringContextHolder.getBean(ApiForDbService.class);
		apiForDbService.updateViewLogStatusForPL();
	}
	
	/**
	 * 每20分处理一次内容提供商百途(csp)提供的文件路径下载情况(点播)
	 * 创建人：lizhenghg 创建时间：2016-12-20
	 * @param 返回类型：void
	 * @throws DocumentException 
	 * @throws RemoteException 
	 * @exception throws
	 */
	public void doCreateFile() throws RemoteException, DocumentException{
		HandleAssetsService handleAssetsService = (HandleAssetsService) SpringContextHolder.getBean(HandleAssetsService.class);
		handleAssetsService.toCreateFile();
	}
	
	/**
	 * 每2小时处理一次内容提供商百途(csp)提供的图片下载情况(点播)
	 * 创建人：lizhenghg 创建时间：2016-12-20
	 * @param 返回类型：void
	 * @throws DocumentException 
	 * @throws RemoteException 
	 * @exception throws
	 */
	public void doCreateFilePic(){
		HandleAssetsService handleAssetsService = (HandleAssetsService) SpringContextHolder.getBean(HandleAssetsService.class);
		handleAssetsService.toParseAssets();
	}
	
	/**
	 * 每30分钟处理一次内容提供商百视通(csp)提供的图片分发 创建人：lizhenghg 创建时间：2017-04-25
	 * 
	 * @param 返回类型
	 *            ：void
	 * @throws DocumentException
	 * @throws RemoteException
	 * @exception throws
	 */
	public void doDispatchPic() {
		HandleAssetsService handleAssetsService = (HandleAssetsService) SpringContextHolder.getBean(HandleAssetsService.class);
		handleAssetsService.toDispatchPic();
	}
	
	/**
	 * 每5分钟处理一次mybatis的二级缓存
	 * @author lizhenghg  time：2018-01-29
	 */
	public void doMybatisCache() {
		try {
			EnhancedCachingManagerImpl enhancedCachingManager = EnhancedCachingManagerImpl.getInstance();
			Map<String, Set<String>> observers = enhancedCachingManager.getObservers();
	        if (observers != null && observers.size() > 0) {
	        	Set<String> statement = new HashSet<String>();
	        	for (Entry<String, Set<String>> str : observers.entrySet()) {
	        		statement.add(str.getKey());
	        	}
	        	enhancedCachingManager.clearRelatedCaches(statement);
	        }
		} catch (Exception  ex) {
			ex.printStackTrace();
			logger.error(AssetQuartz.class.getName() + "方法doMybatisCache执行异常：" + ex.getMessage());
		}
	}
}