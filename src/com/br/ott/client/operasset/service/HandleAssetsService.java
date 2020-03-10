package com.br.ott.client.operasset.service;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.net.TelnetInputStream;
import sun.net.ftp.FtpClient;

import com.br.ott.Global;
import com.br.ott.client.api.webservice.baitu.client.CSPResponseSoapBindingStub;
import com.br.ott.client.api.webservice.baitu.client.ItvServiceImplResultService;
import com.br.ott.client.api.webservice.baitu.client.ItvServiceImplResultServiceLocator;
import com.br.ott.client.api.webservice.baitu.client.CSPResult;
import com.br.ott.client.operasset.entity.Assets;
import com.br.ott.client.operasset.mapper.HandleAssetsMapper;
import com.br.ott.client.select.entity.DramaAlbum;
import com.br.ott.client.select.entity.DramaNavigation;
import com.br.ott.client.select.entity.DramaProgram;
import com.br.ott.client.select.entity.DramaType;
import com.br.ott.client.select.mapper.DramaAlbumMapper;
import com.br.ott.client.select.mapper.DramaNavigationMapper;
import com.br.ott.client.select.mapper.DramaProgramMapper;
import com.br.ott.client.select.mapper.DramaTypeMapper;
import com.br.ott.common.util.DateUtil;
import com.br.ott.common.util.Feedback;
import com.br.ott.common.util.StringUtil;
import com.br.ott.common.util.UploadFile;

/**
 * 文件名：HandleAssetsService.java，跟内容提供商百途资产对接类 版权：BoRuiCubeNet. Copyright
 * 2014-2015,All rights reserved 公司名称：青岛博瑞立方科技有限公司 创建人：lizhenghg 创建时间：2016-12-20
 */
@Service
public class HandleAssetsService {

	@Autowired
	private HandleAssetsMapper handleAssetsMapper;
	@Autowired
	private DramaProgramMapper dramaProgramMapper;
	@Autowired
	private DramaNavigationMapper dramaNavigationMapper;
	@Autowired
	private DramaTypeMapper dramaTypeMapper;
	@Autowired
	private DramaAlbumMapper dramaAlbumMapper;
	
	private static Logger logger = Logger.getLogger(HandleAssetsService.class);

	/**
	 * 每20分处理一次内容提供商百途(csp)提供的文件路径下载情况
	 * 
	 * 创建人：lizhenghg 创建时间：2016-12-20
	 * 
	 * @return 返回类型：void
	 * @throws DocumentException 
	 * @throws RemoteException 
	 * 
	 * @exception try
	 *                {}catch
	 */
	public void toCreateFile(){
		List<Map<String, String>> jointinfoList = this.handleAssetsMapper.queryJointInfoForList();
		if (CollectionUtils.isNotEmpty(jointinfoList)) {
			Map<String, String> paramsMap = null;
			Map<String, String> map = null;
			boolean flag = false;
			String correlateID = null;
			String cmdFileURL = null;
			for (int i = 0, n = jointinfoList.size(); i < n; i++) {
				paramsMap = jointinfoList.get(i);
				correlateID = (String) paramsMap.get("correlateID");
				cmdFileURL = (String) paramsMap.get("cmdFileURL");
				map = getFtpInfo(correlateID, cmdFileURL, Global.BT_ASSET_DOWNLOADPATH);  //   /home/soft/resources
				flag = toCreateFile(map);
				if (flag){
					paramsMap.put("isCreateFile", "0"); // 表示本地已经成功生成文件
					map.put("status", "0");
				}else{
					paramsMap.put("isCreateFile", "1"); // 表示本地生成文件失败
					map.put("status", "-1");
				}
				map.put("tableName", "ott_jointinfo");
				map.put("handleType", "1");
				paramsMap.put("modifyFileName", "ok");
				paramsMap.put("fileName", map.get("fileName"));
				paramsMap.put("createTime", "ok");
				this.handleAssetsMapper.updateJointInfo(paramsMap);
				this.handleAssetsMapper.addJointinfo_handlelog(map);
			}
			//解析xml并返回解析结果
			parseFile(jointinfoList);
			jointinfoList = null;
		}
	}

	/**
	 * 获取用户名、密码、ftp服务器hostname、ftp端口、ftp服务器上的相对路径、下载后保存到本地的路径
	 * 
	 * @param cmdFileURL
	 * @return Map<String, String>
	 */
	public Map<String, String> getFtpInfo(String correlateID, String cmdFileURL, String downloadPath) {
		String username = null; // 用户名
		String password = null; // 密码
		String basePath = null; // ftp主机+端口号
		String host = null;     //ftp主机
		String remotePath = ""; // ftp服务器上的相对路径
		String port = null; // 端口号
		String fileName = null; // 文件名
		
		if(cmdFileURL.indexOf("://") != -1)
			cmdFileURL = cmdFileURL.substring(cmdFileURL.indexOf("://") + 3);
		//先获取用户名和密码
		username = cmdFileURL.substring(0, cmdFileURL.indexOf(":"));
		password = cmdFileURL.substring(cmdFileURL.indexOf(":") + 1, cmdFileURL.indexOf("@"));
		//获取ftp主机+端口号
		basePath = cmdFileURL.substring(cmdFileURL.indexOf("@") + 1, cmdFileURL.indexOf("/"));
		if(basePath.indexOf(":") == -1){
			host = basePath;
			port = "";
		}else{
			host = basePath.substring(0, basePath.indexOf(":"));
			port = basePath.substring(basePath.indexOf(":") + 1);
		}
		//获取相对路径和文件名
		remotePath = cmdFileURL.substring(cmdFileURL.indexOf("/") + 1);
		if(remotePath.indexOf("/") == 0)
			remotePath = remotePath.substring(1);
		fileName = remotePath.substring(remotePath.lastIndexOf("/") + 1);
		remotePath = remotePath.substring(0, remotePath.lastIndexOf("/") + 1);
		if(remotePath.indexOf("//") != -1)
			remotePath = remotePath.replaceAll("//", "/");
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("username", username);
		paramsMap.put("password", password);
		paramsMap.put("host", host);
		paramsMap.put("port", port);
		paramsMap.put("remotePath", remotePath);
		paramsMap.put("downloadPath", downloadPath);
		paramsMap.put("correlateID", correlateID);
		paramsMap.put("fileName", fileName);
		return paramsMap;
	}

	/**
	 * 根据ftp路径获取文件并把文件生成在本地
	 * 
	 * @param paramsMap
	 *            return void
	 */
	public boolean toCreateFile(Map<String, String> paramsMap) {		
		String username = paramsMap.get("username") == null ? "" : (String) paramsMap.get("username");
		String password = paramsMap.get("password") == null ? "" : (String) paramsMap.get("password");
		String host = paramsMap.get("host") == null ? "" : (String) paramsMap.get("host");
		String port = paramsMap.get("port") == null ? "" : (String) paramsMap.get("port");
		String remotePath = paramsMap.get("remotePath") == null ? "" : (String) paramsMap.get("remotePath");
		String downloadPath = paramsMap.get("downloadPath") == null ? "" : (String) paramsMap.get("downloadPath");  // /home/soft/resources
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());   //2016-12-30
		File f = null;
		if(StringUtils.isNotEmpty(((String) paramsMap.get("parsePic")))){
			f = new File(downloadPath + Global.CSP_EPG_IMG + File.separator + date); // f = /home/soft/resources/epgs/2016-12-30
			downloadPath = downloadPath + Global.CSP_EPG_IMG + File.separator + date;
		}else{
			f = new File(downloadPath + Global.CSP_FILE_PATH + File.separator + date);  // f = /home/soft/resources/files/2016-12-30
			downloadPath = downloadPath + Global.CSP_FILE_PATH + File.separator + date;
		}
		if(!f.exists() && !f.isDirectory()) f.mkdir();
		String fileName = paramsMap.get("fileName") == null ? "" : (String) paramsMap.get("fileName");  //  04_VOD_20161208141914_000033645.xml
		String timeSequence = null;
		String postfix_fileName = null;
		if(StringUtils.isNotEmpty(((String) paramsMap.get("parsePic")))) postfix_fileName = fileName.substring(fileName.indexOf("."));
		
		FtpClient ftpClient = null;
		OutputStream output = null;
		TelnetInputStream input = null;
		try {
			if ("".equals(port)) {
				ftpClient = new FtpClient(host);
			} else {
				ftpClient = new FtpClient(host, Integer.parseInt(port));
			}
			ftpClient.login(username, password); // 登录
			ftpClient.binary();
			if (!"".equals(remotePath)) // 转移到FTP服务器目录，假如该目录不存在会报异常
				ftpClient.cd(remotePath);
			input = ftpClient.get(fileName);  //当该文件不存在时候会报异常
			if(input == null) return false;
			File localFile = null;
			if(StringUtils.isEmpty(postfix_fileName))
				localFile = new File(downloadPath + "/" + fileName);  //  /home/soft/resources/files/2016-12-30/04_VOD_20161208141914_000033645.xml
			else
				localFile = new File(downloadPath + "/" + paramsMap.get("picId") + "_" + (timeSequence = DateUtil.getTimeSequence()) + postfix_fileName); // /home/soft/resources/epgs/2016-12-30/7679_20170207084737090050.jpg
			output = new FileOutputStream(localFile);
			byte[] bytes = new byte[1024];
			int len = 0;
			while((len = input.read(bytes)) != (-1)){  //把内容读取到byte数组中，每次最多读1024个字节到byte数组中
				output.write(bytes, 0, len);
			}
			paramsMap.put("status", "0");
			if(postfix_fileName == null)
				paramsMap.put("fileName", Global.CSP_FILE_PATH+ "/" + date + "/" + fileName);  // /files/2016-12-30/04_VOD_20161208141914_000033645.xml
			else
				paramsMap.put("fileName", Global.CSP_EPG_IMG + "/" + date + "/" + paramsMap.get("picId") + "_" + timeSequence + postfix_fileName);  // /epgs/2016-12-30/7679_20170207084737090050.jpg
			return true;
		} catch (Exception e) {
			logger.error(HandleAssetsService.class.getName() + "执行方法toCreateFile报异常：" + e.getMessage());
			e.printStackTrace();
			paramsMap.put("status", "-1");
			paramsMap.put("error", "执行方法toCreateFile报异常：" + e.getMessage());
			return false;
		} finally{
			try{
				if(output != null)
					output.close();
				if(input != null)
					input.close();
				if(null != ftpClient)
					ftpClient.closeServer();
			}catch(Exception e){}
		}
	}

	/**    
     * 取得相对于当前连接目录的某个目录下所有文件列表    
     * @author lizhenghg 创建时间：2016-12-20
     * @param path,ftpClient
     * @return List<String>
     */     
    @SuppressWarnings("deprecation")
	public List<String> getFileList(String path, FtpClient ftpClient){      
        List<String> list = new ArrayList<String>();      
        DataInputStream input;      
        try {      
        	input = new DataInputStream(ftpClient.nameList(path));      
            String filename = "";      
            while((filename = input.readLine()) != null){      
                list.add(filename);      
            }      
        } catch (IOException e) {      
            e.printStackTrace();      
        }      
        return list;      
    }      
    
    /**
	 * 判断传入字符串是否整数
	 * @author lizhenghg 创建时间：2016-12-20
	 * @param content
	 * @return 返回类型：boolean
	 */
	public static boolean isNumber(String content) {
		boolean flag = true;
		if(StringUtils.isBlank(content))
			return (flag = false);
		char[] ch = content.toCharArray();
		for (int i = 0, n = ch.length; i < n; i++) {
			if (!Character.isDigit(ch[i])) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	/**
	 * 
	 * 创建人：lizhenghg 创建时间：2016-12-20
	 * 
	 * @return 返回类型：void
	 * @throws DocumentException
	 * 
	 * @exception try
	 *                {}catch
	 */
	@SuppressWarnings("unchecked")
	public void parseFile(List<Map<String, String>> assetsList){
		String path = Global.BT_ASSET_DOWNLOADPATH;
		String filePath = null;
		File file = null;
		Document doc = null;
		Element object = null;
		Element mapping = null;
		SAXReader reader = new SAXReader();

		if (CollectionUtils.isNotEmpty(assetsList)) {
			Map<String, Object> p = new HashMap<String, Object>(); 
			p.put("assetsList", assetsList);
			assetsList = this.handleAssetsMapper.queryPendParseFileForList(p);
			Map<String, String> info = null;
			if(CollectionUtils.isEmpty(assetsList))
				return;
			for (int i = 0, n = assetsList.size(); i < n; i++) {
				info = assetsList.get(i);
				if(!("0".equals(info.get("isCreateFile"))))
					continue;
				filePath = path + (String) info.get("localFileName");   //  /home/soft/resource/files/2016-12-30/04_VOD_20161208141914_000033645.xml
				file = new File(filePath);
				
				//先判断该xml是否为空
				try {
					doc = reader.read(file);
				} catch (DocumentException e) {
					info.put("isDealFile", "1");
					info.put("fileName", info.get("localFileName"));
					info.put("status", "-2");
					info.put("error", "DocumentException：" + e.getMessage());
					this.handleAssetsMapper.updateJointInfo(info);
					this.handleAssetsMapper.addJointinfo_handlelog(info);
					continue;
				}
				
				// 获取Object里面<Property></Property>元素里面的全部内容
				Map<String, String> map = new HashMap<String, String>();

				// Object里面<Property></Property>元素里面的全部内容载体
				List<Element> propertyList = null;

				// 负责跳出下面Object的for循环的标志
				boolean isJump = false;

				//判断成功解析与否的标识
				boolean isSuccess = true;
				
				try{
					// 处理全部Object元素
					for (Iterator<Element> iter = doc.selectNodes("//Object").iterator(); iter.hasNext();) {
						object = (Element) iter.next();
					    String elementType = object.attributeValue("ElementType");
					    String elementID = object.attributeValue("ID");
					    String elementCode = object.attributeValue("Code");
					    String action = object.attributeValue("Action");
					    // 资产类型，点播目前处理：Series、Program、Movie、Picture、Category，暂时不处理Package、HTMLContent、PhysicalChannel类型
					    if (!("Series".equals(elementType) || "Program".equals(elementType) || "Movie".equals(elementType) || "Picture".equals(elementType) || "Category".equals(elementType))) {
					    	//dealError(info, "", "类型中包含Package、HTMLContent、PhysicalChannel", "1", elementID, elementType);
					    	continue;
					    }
						// 获取Object里面<Property></Property>元素里面的全部内容
					    propertyList = object.elements("Property");
					    map.clear();
					    for (int j = 0, m = propertyList.size(); j < m; j++) {
					    	map.put(propertyList.get(j).attributeValue("Name"), propertyList.get(j).getText().trim());
					    }
					    // 新增操作开始
					    if ("REGIST".equals(action)) {
					    	String sign = addAssets(object, doc, elementID, elementCode, elementType, info, map);
						    if("-1".equals(sign)){
							    isJump = true;
							    break;
						    }
						    if("1".equals(sign)){
						    	isSuccess = false;
							    continue;
						    }
						} //新增操作结束

					    // 更新、删除操作开始
					    if ("UPDATE".equals(action) || "DELETE".equals(action)) {
					    	boolean flag = updateOrDeleteAssets(action, elementID, elementCode, elementType, info, map);
					    	if(!flag){
					    		isSuccess = false;
					    		continue;
					    	}
					    } //更新删除操作结束
				    }
					if (isJump) {   // 判断@1，当出现@1这种情况时，该xml处理失败，后期人工处理
						continue;
					}
					
				    // 处理全部Mapping元素,该Mapping元素的操作全部为Action=REGIST
					if(doc.selectNodes("//Mapping") != null && doc.selectNodes("//Mapping").size() > 0){
						dealMapping(isSuccess, info, doc, mapping);
					}
					//在assetsList不为空情况下全部成功走通，说明该文件解析成功，标志该文件为解析成功
					if(CollectionUtils.isNotEmpty(assetsList)){
						if(isSuccess){
							info.put("isDealFile", "0");     //是否已处理本地文件0表示已经成功处理;1表示处理失败;2表示尚未处理
							info.put("fileName", info.get("localFileName"));
							info.put("status", "0");
							this.handleAssetsMapper.addJointinfo_handlelog(info);
						}else{
							info.put("isDealFile", "1");
						}
						this.handleAssetsMapper.updateJointInfo(info);
					}
				}catch(Exception e){
					info.put("isDealFile", "1");
					info.put("fileName", info.get("localFileName"));
					info.put("status", "-2");
					info.put("error", e.getMessage());
					this.handleAssetsMapper.updateJointInfo(info);
					this.handleAssetsMapper.addJointinfo_handlelog(info);
					continue;
				}
			}
			p = null;
			info = null;
		}
		//把对xml处理结果反馈给内容提供商百途
		notifyInfo(assetsList);
	}
	
	/**
	 * 把对xml处理的结果反馈给内容提供商百途
	 * 
	 * 创建人：lizhenghg 创建时间：2016-12-20
	 * 
	 * @return 返回类型：void
	 * @throws RemoteException 
	 * @throws DocumentException
	 * 
	 * @exception try
	 *                {}catch
	 */
	
	public void notifyInfo(List<Map<String, String>> result){
		if(CollectionUtils.isNotEmpty(result)){
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("correlateIDList", result);
			result = this.handleAssetsMapper.queryNodifyAssetForList(paramsMap);
			Iterator<Map<String, String>> iter = null;
			if(CollectionUtils.isNotEmpty(result))
				iter = result.iterator();
			else
				return;
			ItvServiceImplResultService locator = new ItvServiceImplResultServiceLocator();
			CSPResponseSoapBindingStub stub = null;
			CSPResult resp = null;
			try {
				stub = (CSPResponseSoapBindingStub)locator.getCSPResponse();
			} catch (ServiceException e) {
				logger.error("文件处理情况通讯失败：" + e.getMessage());
				e.printStackTrace();
			}
			Map<String, String> map = null;
			try{
				while(iter.hasNext()){
					map = iter.next();
					resp = (CSPResult)stub.resultNotify(map.get("cspid"), map.get("lspid"), map.get("correlateID"), ((map.get("isDealFile").equals("1")) ? -1 : 0), map.get("cmdFileURL"));
				    map.put("result", String.valueOf(resp.getResult()));
				    map.put("errorDescription", resp.getErrorDescription());
				    this.handleAssetsMapper.updateJointInfoForSendStatus(map);
				}
			}catch(Exception e){
				logger.error("文件处理情况通讯调用resultNotify方法失败：" + e.getMessage());
				e.printStackTrace();
			}
			result = null;
		}
	}
	
	public void setParams(PreparedStatement pstmt, Object...object) throws SQLException{
		if(object == null || object.length == 0)
			return;
		for(int i = 0, l = object.length; i < l; i++){
			pstmt.setObject(i + 1, object[i]);
		}
	}
	
	public void dealError(Map<String, String> info, String tableName, String error, String handleType, String elementID, String elementType){
		info.put("isDealFile", "1");
		info.put("fileName", info.get("localFileName"));
		info.put("tableName", tableName);
		info.put("status", "-2");
		info.put("error", error);
		info.put("handleType", handleType);
		info.put("elementID", elementID);
		info.put("elementType", elementType);
		this.handleAssetsMapper.updateJointInfo(info);
		this.handleAssetsMapper.addJointinfo_handlelog(info);
	}
	
	public String addAssets(Element object, Document doc, String elementID, String elementCode, String elementType, Map<String, String> info, Map<String, String> map){
		//处理类型为Category的资产
		if("Category".equals(elementType)){
			List<Map<String, String>> category1 = this.handleAssetsMapper.queryCategoryInfo(elementID, elementType);    //3级分类
			List<Map<String, String>> category2 = this.handleAssetsMapper.queryCategoryInfo2(elementID, elementType);   //1、2级分类
			//假如已经存在该1、2级分类，修改下名称
			if(CollectionUtils.isNotEmpty(category2)){
				String localNavId = category2.get(0).get("localNavId");
				DramaNavigation dramaNavigation = new DramaNavigation();
				dramaNavigation.setId(localNavId);
				dramaNavigation.setName(map.get("Name"));
				this.dramaNavigationMapper.updateDramaNavigation(dramaNavigation);
				dramaNavigation = null;
				category2 = null;
				return "0";
			}
			
			//假如已经存在该3级分类，则作更新处理
			if(CollectionUtils.isNotEmpty(category1)){
				HashMap<String, String> content = new HashMap<String, String>();
				content.put("dramaId", category1.get(0).get("albumId"));
				content.put("Name", map.get("Name"));
				content.put("Status", map.get("Status"));
				content.put("Description", map.get("Description"));
				this.handleAssetsMapper.updateDramaProgram(content);
				content = null;
				return "0";
			}
			String parentCode = object.attributeValue("ParentCode");
			String name = map.get("Name");
			List<Map<String, String>> list = handleAssetsMapper.queryCategoryForList(parentCode);
			String localNavId = null;
			if(CollectionUtils.isNotEmpty(list)){
				Map<String, String> paramsMap = list.get(0);  //localNavId,crNavId,navRank
				localNavId = paramsMap.get("localNavId");
				String rank = paramsMap.get("navRank");
				map.put("elementID", elementID);
				map.put("elementType", elementType);
				//证明该分类为我们这边的三级分类，我们要把该分类转换为合辑
				if("4".equals(rank)){
					map.put("parentCrNrNavId", parentCode);
					DramaProgram dp = new DramaProgram();
					dp.setName(name);
					dp.setcProvider("BT");
					dp.setAssetId(elementID);
					dp.setpId("-1");
					dp.setPtype("JC");
					dp.setPcode(name);
					dp.setRemark(map.get("Description"));
					dp.setIsAlbum("1");
					dp.setStatus("1");
					dp.setTtype(localNavId);
					
					this.dramaProgramMapper.addDramaProgram(dp);
					if (StringUtil.isNotEmpty(dp.getTtype())) {
						List<String> newList = Arrays.asList(dp.getTtype().split(","));
						if (CollectionUtils.isNotEmpty(newList)) {
							for (String navId : newList) {
								DramaType dt = new DramaType(dp.getId(), navId);
								this.dramaTypeMapper.addDramaType(dt);
							}
							newList = null;
						}
					}
					
					map.put("albumId", dp.getId());
					this.handleAssetsMapper.addOtherCategory(map);
					this.handleAssetsMapper.addJointInfoType(dp.getId(), elementID, elementCode, elementType);
					//记录下该C2资产对应的工单号和对应的本地文件路径
					this.handleAssetsMapper.addJointinfoPath(elementID, elementCode, elementType, info.get("localFileName"), info.get("correlateID"));
				}else{
					//证明该分类为我们这边的2级分类，先插入数据到ott_drama_navigation
					int sequence = this.dramaNavigationMapper.getMaxSequence(localNavId);
					HashMap<String, Object> params = new HashMap<String, Object>();
					
					params.put("sequence", String.valueOf(sequence));
					params.put("recommend", "1");
					params.put("parentId", localNavId);
					params.put("operators", "山东网台");
					params.put("code", StringUtil.getPinYinHeadChar(name));
					params.put("status", "1");
					params.put("Name", name);
					this.handleAssetsMapper.addDrama_navigation(params);
					
					//再把记录插入到ott_jointinfo_category
					params.put("localNavId", String.valueOf(params.get("navId")));
					params.put("navRank", "4");
					params.put("elementID", elementID);
					params.put("elementType", elementType);
					this.handleAssetsMapper.addCategory(params);
					this.handleAssetsMapper.addJointInfoType(localNavId, elementID, elementCode, elementType);
					//记录下该C2资产对应的工单号和对应的本地文件路径
					this.handleAssetsMapper.addJointinfoPath(elementID, elementCode, elementType, info.get("localFileName"), info.get("correlateID"));
					params = null;
				}
				list = null;
				category1 = null;
				category2 = null;
			}else{
				//先判断是否一级
				if(parentCode.equals(Global.BT_SECOND_ID)){
					int sequence = this.dramaNavigationMapper.getMaxSequence(localNavId);
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("sequence", String.valueOf(sequence));
					params.put("recommend", "1");
					params.put("parentId", "0");
					params.put("operators", "山东网台");
					params.put("code", StringUtil.getPinYinHeadChar(name));
					params.put("status", "1");
					params.put("Name", name);
					this.handleAssetsMapper.addDrama_navigation(params);
					//再把记录插入到ott_jointinfo_category
					String navId = String.valueOf(params.get("navId"));
					
					params.put("localNavId", navId);
					params.put("navRank", "3");
					params.put("elementID", elementID);
					params.put("elementType", elementType);
					this.handleAssetsMapper.addCategory(params);
					
					//插入ott_jointinfo_type表
					this.handleAssetsMapper.addJointInfoType(navId, elementID, elementCode, elementType);
					//记录下该C2资产对应的工单号和对应的本地文件路径
					this.handleAssetsMapper.addJointinfoPath(elementID, elementCode, elementType, info.get("localFileName"), info.get("correlateID"));
					params = null;
				}else{
					dealError(info, "ott_jointinfo_category", "不存在crNavId为：" + elementID + "的分类栏目", "1", elementID, elementType);
					info.put("isDealFile", "1");
					this.handleAssetsMapper.updateJointInfo(info);
					return "-1"; //@1
				}
			}
		}
		
		// 处理类型为Series、Program的资产
		if ("Series".equals(elementType) || "Program".equals(elementType)) {
			// 先获取到Series或者Program的资产名。
			String assetName = ((String) map.get("Name")).replaceAll("《", "").replaceAll("》", "").replaceAll("<", "").replaceAll(">", "");
			String seriesFlag = null; // 判断是否影片还是单集
			if ("Program".equals(elementType)) // 是否影片还是单集，0表示影片1表示单集
				seriesFlag = (String) map.get("SeriesFlag");
			else
				seriesFlag = "1";
			List<Map<String, String>> linfo = null;
			// 当资产类型为Program，seriesFlag为单集(就是连播剧子集时)
			if ("Program".equals(elementType) && "1".equals(seriesFlag)) {
				// 判断当前子连续剧是否已经存在
				List<Map<String, Object>> childrenLiCst = this.handleAssetsMapper.queryChildByIdAndType(elementID, elementType);
				if (CollectionUtils.isNotEmpty(childrenLiCst)) {
					//假如已经存在子连续剧，进行更新
					map.put("elementID", elementID);
					map.put("elementType", elementType);
					if(StringUtils.isNotBlank(map.get("Status"))){
						map.put("status", map.get("Status"));
						this.handleAssetsMapper.updateChildrenAssets(map);
					}
					childrenLiCst = null;
					return "0";
				}
				if(CollectionUtils.isNotEmpty(this.handleAssetsMapper.queryJointInfoTypeInfo(elementID, elementType))){
					return "0";
				} else {
					//把dramaId、crDramaId、elementType插入ott_jointinfo_type，dramaId默认为0
					this.handleAssetsMapper.addJointInfoType("0", elementID, elementCode, elementType);
				}
			} else {
				// linfo不为空，证明本地数据库已经存在该记录
				linfo = this.handleAssetsMapper.queryParentByIdAndType(elementID, elementType);
				if (CollectionUtils.isNotEmpty(linfo)) {
					//假如电影、父剧已经存在，进行更新处理
					map.put("elementID", elementID);
					map.put("elementType", elementType);
					map.put("dramaId", linfo.get(0).get("dramaId"));
					this.handleAssetsMapper.updateDramaProgram(map);
					linfo = null;
					return "0";
				}
			}
			
			Map<String, Object> contantMap = new HashMap<String, Object>();
			int dramaId = 0;
			if(("Program".equals(elementType) && "0".equals(seriesFlag)) || "Series".equals(elementType)){
				contantMap.put("name", assetName);
				contantMap.put("cProvider", "BT");
				contantMap.put("assetId", elementID);
				contantMap.put("pId", seriesFlag);
				contantMap.put("ptype", "JC");
				contantMap.put("pcode", StringUtil.getPinYinHeadChar(assetName));
				contantMap.put("language", map.get("Language"));
				contantMap.put("playyear", map.get("ReleaseYear"));
				contantMap.put("director", map.get("Director"));
				contantMap.put("actor", map.get("ActorDisplay"));
				contantMap.put("compere", map.get("Compere"));
				contantMap.put("remark", map.get("Description"));
				contantMap.put("status", "1");  //状态：1上线，0待上线，2下线,-1逻辑删除,3不通过
				this.handleAssetsMapper.addProgram(contantMap);
				dramaId =  (int)(((long)((Long)contantMap.get("prId"))));
				this.handleAssetsMapper.addJointInfoType(String.valueOf(dramaId), elementID, elementCode, elementType);
			}
			contantMap.clear();
			
			//影片
			if("Program".equals(elementType) && "0".equals(map.get("SeriesFlag"))){
				contantMap.put("dramaId", String.valueOf(dramaId));contantMap.put("title", "1");contantMap.put("status", "1");contantMap.put("crChildDramaId", elementID);contantMap.put("crChildDramaType", elementType);
			}
			//连续剧父剧
			if("Series".equals(elementType)){
				contantMap.put("dramaId", String.valueOf(dramaId));contantMap.put("title", "0");contantMap.put("status", "0");contantMap.put("crChildDramaId", elementID);contantMap.put("crChildDramaType", elementType);
			}
			//连续剧子剧集(会出现子剧集跟父剧不在同一个xml情况)，先插入ott_drama_children，dramaId跟title先假设为0，后期找到相应的编排工单时再更新处理
			if("Program".equals(elementType) && "1".equals(map.get("SeriesFlag"))){
				contantMap.put("dramaId", "0");contantMap.put("title", "0");contantMap.put("status", "1");contantMap.put("crChildDramaId", elementID);contantMap.put("crChildDramaType", elementType);
			}
			contantMap.put("operator", "百途系统同步");
			this.handleAssetsMapper.addDrama_children(contantMap);
			//记录下该C2资产对应的工单号和对应的本地文件路径
			this.handleAssetsMapper.addJointinfoPath(elementID, elementCode, elementType, info.get("localFileName"), info.get("correlateID"));
			contantMap = null;
		}		
		// 新增类型为Picture、Movies的资产
		if ("Movie".equals(elementType) || "Picture".equals(elementType)) {
			List<Map<String, String>> list = this.handleAssetsMapper.queryPictureOrMoviesByIdAndType(elementID, elementType);
			if(CollectionUtils.isNotEmpty(list)){
				//假如已经存在，进行更新处理
				map.put("elementID", elementID);
				map.put("elementType", elementType);
				if("Movie".equals(elementType)){
					String movieBitrate = null;
					if(StringUtils.isNotBlank(movieBitrate = map.get("BitRateType"))){
						if("500".equals(movieBitrate))                   //我们码率1、急速;2、高清;3、蓝光;4、4k，百途：写死500
							map.put("movieBitrateType", "1");
						else
							map.put("movieBitrateType", "2");
						map.put("movieBitrate", movieBitrate);
					}
					map.remove("Type");
				}
				
				this.handleAssetsMapper.updateJointinfo_source(map);
				
				if("Movie".equals(elementType)){
					List<Map<String, Object>> result = this.handleAssetsMapper.queryAllMoviesByList(elementID, elementType);
					if(CollectionUtils.isNotEmpty(result)){
						for(int t = 0,n=result.size(); t < n; t++){
							if(result.get(t) == null)
								continue;
							map.put("crParentId", ((String)((Map<String, Object>)result.get(t)).get("crParentId")));
							map.put("crParentType", ((String)((Map<String, Object>)result.get(t)).get("crParentType")));
							this.handleAssetsMapper.updateDramaSource(map);
						}
						result = null;
					}
				}
				list = null;
				return "0";
			}
			map.put("crDramaId", elementID);
			map.put("elementType", elementType);
			if(map.get("FileURL") != null)
				map.put("fileURL", map.get("FileURL"));
			
			if("Movie".equals(elementType)){
				String movieBitrate = null;
				if(StringUtils.isNotBlank(movieBitrate = map.get("BitRateType"))){
					if("500".equals(movieBitrate))                   //我们码率1、急速;2、高清;3、蓝光;4、4k，百途：写死500
						map.put("movieBitrateType", "1");
					else
						map.put("movieBitrateType", "2");
					map.put("movieBitrate", movieBitrate);
				}
				map.remove("Type");
			}
			this.handleAssetsMapper.addJointinfo_source2(map);
		}
		return "0";
	}
	
	public boolean updateOrDeleteAssets(String action, String elementID, String elementCode, String elementType, Map<String, String> info, Map<String, String>map){
		String status = null;             //"0"表示失效；"1"表示生效
		if("UPDATE".equals(action)){
			status = map.get("Status") == null ? "1" : ("0".equals(map.get("Status")) ? "0" : "1");
		}else{
			status = "0";
		}
		map.put("Status", status);
		//类型为Category的资产
		if("Category".equals(elementType)){
			List<Map<String, String>> category1 = this.handleAssetsMapper.queryCategoryInfo(elementID, elementType);   //3级分类
			List<Map<String, String>> category2 = this.handleAssetsMapper.queryCategoryInfo2(elementID, elementType);  //1、2级分类
			//假如不存在该分类
			if(CollectionUtils.isEmpty(category1) && CollectionUtils.isEmpty(category2))
				return true;
			map.put("elementID", elementID);
			map.put("elementType", elementType);
			String localNavId = null;
			//category1不为空：处理3级分类，不用处理ott_drama_navigation，只需要处理合辑资产
			if(CollectionUtils.isNotEmpty(category1)){
				if("UPDATE".equals(action)){
					this.handleAssetsMapper.updateCategoryInfo(map);
				}else{
					this.handleAssetsMapper.updateCategoryInfos(map);
				}
				String albumId = category1.get(0).get("albumId");
				this.dramaProgramMapper.updateDramaProgramStatus(status, albumId);
				category1 = null;
			}else{
				localNavId = category2.get(0).get("localNavId");
				if("UPDATE".equals(action)){
					this.handleAssetsMapper.updateCategoryInfo2(map);
				}else{
					this.handleAssetsMapper.updateCategoryInfo2s(map);
				}
				this.handleAssetsMapper.updateDramaNavigation(localNavId, status);
				category2 = null;	
			}			
		}
		
		// 类型为Series、Program的资产
		if ("Series".equals(elementType) || "Program".equals(elementType)) {
			String seriesFlag = null;          // 判断是否影片还是单集
			if ("Program".equals(elementType)) // 是否影片还是单集，0表示影片1表示单集
				seriesFlag = (String) map.get("SeriesFlag");
			else
				seriesFlag = "1";
			// 当资产类型为Program，seriesFlag为单集时，通过ott_drama_children表判断是否存在
			if ("Program".equals(elementType) && "1".equals(seriesFlag)) {
				List<Map<String, Object>> childrenLiCst = this.handleAssetsMapper.queryChildByIdAndType(elementID, elementType);
				if (CollectionUtils.isEmpty(childrenLiCst)) {
					return true;
				}
				//更新当前资产
				map.put("elementID", elementID);
				map.put("elementType", elementType);
				if(StringUtils.isNotBlank(map.get("Status"))){
					map.put("status", map.get("Status"));
					this.handleAssetsMapper.updateChildrenAssets(map);
				}
				childrenLiCst = null;
			}else{
				//当前资产为Program或者Series
				List<Map<String, String>> linfo = this.handleAssetsMapper.queryParentByIdAndType(elementID, elementType);
				if(CollectionUtils.isEmpty(linfo)){
					return true;
				}
				map.put("dramaId", linfo.get(0).get("dramaId"));
				//更新ott_drama_program
				this.handleAssetsMapper.updateDramaProgram(map);
				linfo = null;
			}
		}	
		// 类型为Picture、Movies的资产
		if ("Movie".equals(elementType) || "Picture".equals(elementType)) {
			//先判断Picture、Movies资产是否存在
			@SuppressWarnings("rawtypes")
			List linfo = null;
			if("Picture".equals(elementType))
				linfo = this.handleAssetsMapper.queryPictureByIdAndType(elementID, elementType);
			if ("Movie".equals(elementType))
				linfo = this.handleAssetsMapper.queryPictureOrMoviesByIdAndType(elementID, elementType);
			if(CollectionUtils.isEmpty(linfo)){
				return true;
			}
			//对Picture、Movies进行更新/删除处理
			map.put("elementID", elementID);
			map.put("elementType", elementType);
			//先更新/删除ott_jointinfo_source表
			if("UPDATE".equals(action)){
				if("Movie".equals(elementType)){
					String movieBitrate = null;
					if(StringUtils.isNotBlank(movieBitrate = map.get("BitRateType"))){
						if("500".equals(movieBitrate)) {                  //我们码率1、急速;2、高清;3、蓝光;4、4k，百途：写死500
							map.put("movieBitrateType", "1");
						}
						else{
							map.put("movieBitrateType", "2");
						}
						map.put("movieBitrate", movieBitrate);
					}
					map.remove("Type");
				}
				this.handleAssetsMapper.updateJointinfo_source(map);
			}else{
				this.handleAssetsMapper.updateJointinfo_source2(map);
			}
			//再处理其他相关联的表
			if("Movie".equals(elementType)){
				List<Map<String, Object>> result = this.handleAssetsMapper.queryAllMoviesByList(elementID, elementType);
				if(CollectionUtils.isNotEmpty(result)){
					for(int i = 0,n=result.size(); i < n; i++){
						if(result.get(i) == null)
							continue;
						map.put("crParentId", ((String)((Map<String, Object>)result.get(i)).get("crParentId")));
						map.put("crParentType", ((String)((Map<String, Object>)result.get(i)).get("crParentType")));
					    this.handleAssetsMapper.updateDramaSource(map);
					}
					result = null;
				}
			}
			linfo = null;
		}
		return true;	
	}
	
	public void dealMapping(boolean isSuccess, Map<String, String> info, Document doc, Element mapping){	
		for (@SuppressWarnings("unchecked")Iterator<Element> itor = doc.selectNodes("//Mapping").iterator(); itor.hasNext();) {
			mapping = itor.next();
			String elementType = mapping.attributeValue("ElementType");
			String elementID = mapping.attributeValue("ElementID");
			String parentType = mapping.attributeValue("ParentType");
			String parentID = mapping.attributeValue("ParentID");
			String action = mapping.attributeValue("Action");
			String navId = null;
			String dramaId = null;
			if(!"REGIST".equals(action))
				continue;
			
			// 获取Mapping里面<Property></Property>元素里面的全部内容
			Map<String, String> paramMap = new HashMap<String, String>();
			@SuppressWarnings("unchecked")
			List<Element> property = mapping.elements("Property");
			if(CollectionUtils.isNotEmpty(property)){
				for (Element element : property) {
					paramMap.put(element.attributeValue("Name"), element.getText().trim());
				}
			}
			
			//处理Category与Program/Series的映射关系
			if("Category".equals(parentType)){
				//先获取分类信息，主要获取本地的菜单id.先获取5级菜单id
				List<Map<String, String>> category1 = this.handleAssetsMapper.queryCategoryInfo(parentID, parentType); //3级分类
				List<Map<String, String>> category2 = null;    //1、2级分类
				if(CollectionUtils.isEmpty(category1))
					category2 = this.handleAssetsMapper.queryCategoryInfo2(parentID, parentType);
				if(CollectionUtils.isEmpty(category1) && CollectionUtils.isEmpty(category2)){
					dealError(info, "ott_drama_type", "本地数据库不存在资产id为：" + parentID + "资产类型为：" + parentType + "的分类导航信息", "1", parentID, parentType);
					isSuccess = false;
					continue;
				}
				if(CollectionUtils.isNotEmpty(category1))
					navId = category1.get(0).get("localNavId");   //获取2级分类对应的NavId
				else                                            
					navId = category2.get(0).get("localNavId");   //获取1或者2级分类对应的NavId
				
				if("Program".equals(elementType) || "Series".equals(elementType)){      //在分类下增加节目、连续剧，假如该分类为3级分类，那么不执行插入ott_drama_type操作，但是要update该资产的pId，和执行插入ott_drama_album操作
					//先判断该资产是不是已经存在了
					List<Map<String, String>> dramaIdInfo = this.handleAssetsMapper.queryParentByIdAndType(elementID, elementType);
					if(CollectionUtils.isEmpty(dramaIdInfo)){
						dealError(info, "ott_drama_type", "本地数据库不存在资产id为：" + elementID + "资产类型为：" + elementType + "的信息", "1", elementID, elementType);
						isSuccess = false;
						continue;
					}
					//获取资产id对应的本地资产id
					dramaId = dramaIdInfo.get(0).get("dramaId");
					//对资产进行分类之前先判断是否已经分类，3级分类下配置的资产不分类
					List<Map<String, String>> list = this.handleAssetsMapper.queryDramaTypeInfo(dramaId, navId);
					if(CollectionUtils.isEmpty(list)){
						if(CollectionUtils.isNotEmpty(category2) && CollectionUtils.isEmpty(category1)){
							this.handleAssetsMapper.addDrama_type(dramaId, navId);
						}
					}
					//假如是3级分类下配置资产，那么不执行插入ott_drama_type的操作，但是要update该资产的pId和执行插入ott_drama_album操作
					if(CollectionUtils.isNotEmpty(category1)){
						//更新资产的pId
						HashMap<String, String> content = new HashMap<String, String>();
						content.put("Pid", "2");         //剧集类型:连播剧 1，单播剧0,合辑 -1,合辑资产2
						content.put("dramaId", dramaId);
						this.handleAssetsMapper.updateDramaProgram(content);
						content = null;
						
						//先判断该资产是否已经插入ott_drama_album，有的话更新下queue，没有的话执行插入
						String albumId = category1.get(0).get("albumId");
						String queue = paramMap.get("Sequence");
						//已经插入ott_drama_album，更新下quene，然后跳出该Mapping
						if(this.handleAssetsMapper.findDramaAlbums(albumId, dramaId) != null){
							this.dramaAlbumMapper.updateDASequence(queue, dramaId);
							property = null;
							category1 = null;
							dramaIdInfo = null;
							list = null;
							continue;
						}
						
						//插入ott_drama_album
						DramaAlbum dramaAlbum = new DramaAlbum();
						if(albumId != null)
							dramaAlbum.setAlbumId(Integer.parseInt(albumId));
						
						property = null;
						if(queue != null)
							dramaAlbum.setQueue(Integer.parseInt(queue));
						dramaAlbum.setDramaId(Integer.parseInt(dramaId));
						this.dramaAlbumMapper.insertDramaAlbum(dramaAlbum);
						
					}
					dramaIdInfo = null;
					list = null;
					category1 = null;
					category2 = null;
				}
			}
			//处理Series与Program的关系，也就是连播剧剧头与子连播剧的关系
			if("Series".equals(parentType) && "Program".equals(elementType)){
				//先判断该父资产是否已经存在，假如不存在，报异常
				List<Map<String, String>> parentIdList = null;
				if(CollectionUtils.isEmpty((parentIdList = this.handleAssetsMapper.queryParentByIdAndType(parentID, parentType)))){
					dealError(info, "ott_jointinfo_type", "本地数据库不存在资产id为：" + parentID + "资产类型为：" + parentType + "的信息(处理Series与Program关系时出现Series不存在情况)", "1", parentID, parentType);
					isSuccess = false;
					continue;
				}
				
				if(CollectionUtils.isEmpty(this.handleAssetsMapper.queryJointInfoTypeInfo(elementID, elementType))){
					dealError(info, "ott_jointinfo_type", "本地数据库不存在资产id为：" + elementID + "资产类型为：" + elementType + "的信息(处理Series与Program关系时出现Program不存在情况)", "1", elementID, elementType);
					isSuccess = false;
					continue;
				}
				
				//更新ott_drama_children、ott_jointinfo_type
				Map<String, String> contantMap = new HashMap<String, String>();
				String dramId = parentIdList.get(0).get("dramaId");
				String title = paramMap.get("Sequence");
				contantMap.put("dramaId", dramId);
				contantMap.put("title", title);
				contantMap.put("status", "1");          //0表示停用;1表示使用
				contantMap.put("elementID", elementID);
				contantMap.put("elementType", elementType);
				this.handleAssetsMapper.updateChildrenAssets(contantMap);

				//更新ott_jointinfo_type表的dramaId
				this.handleAssetsMapper.updateJointInfoType(dramId, elementID, elementType);				
				parentIdList = null;
				property = null;
			}
			
			//处理Picture与Category/Program/Series的关系
			if("Picture".equals(parentType)){
				//先判断当前资产是否存在，存在的话是否已经被分配好了
				List<Map<String, String>> picutreList = null;
				//只处理Category/Program/Series
				//先判断Category/Program/Series是否存在
				if("Program".equals(elementType) || "Series".equals(elementType)){
					if(CollectionUtils.isEmpty(this.handleAssetsMapper.queryParentByIdAndType(elementID, elementType))){
						dealError(info, "ott_jointinfo_type", "资产id为：" + elementID + "资产类型为：" + elementType + "的资产不存在", "1", elementID, elementType);
						isSuccess = false;
						continue;
					}
				}
				if("Category".equals(elementType)){
					List<Map<String, String>> l1 = this.handleAssetsMapper.queryCategoryForList(elementID);
					List<Map<String, String>> l2 = this.handleAssetsMapper.queryCategoryInfo(elementID, elementType);
					if(CollectionUtils.isEmpty(l1) && CollectionUtils.isEmpty(l2)){
						dealError(info, "ott_jointinfo_category,ott_jointinfo_othercategory", "资产id为：" + elementID + "资产类型为：" + elementType + "的资产不存在", "1", elementID, elementType);
						isSuccess = false;
						continue;
					}
				}
				//判断该图片是否已经存在，是否已经分配好了
				if("Category".equals(elementType) || "Program".equals(elementType) || "Series".equals(elementType)){
					picutreList = this.handleAssetsMapper.queryPictureByList(parentID, parentType, elementID, elementType);
					//该图片资产已经被分配好，重新生成一遍图片，并让该图片的分发状态为未分发
					if(CollectionUtils.isNotEmpty(picutreList)){
						HashMap<String, Object> contentMap = new HashMap<String, Object>();
						contentMap.put("isCreateFile", "2");
						contentMap.put("isDealFile", "2");
						contentMap.put("isDispatch", "0");
						contentMap.put("crDramaId", parentID);
						contentMap.put("elementType", parentType);
						this.handleAssetsMapper.updateJointinfoSource(contentMap);
						contentMap = null;
						continue;
					}
					//再判断该图片资产是否已经存在
					picutreList = this.handleAssetsMapper.queryPictureByIdAndType(parentID, parentType);
					if(CollectionUtils.isEmpty(picutreList)){
						dealError(info, "ott_jointinfo_source", "本地数据库不存在资产id为：" + parentID + "资产类型为：" + parentType + "的资产信息", "1", parentID, parentType);
						isSuccess = false;
						continue;
					}
					
					Map<String, String> paramsMap = picutreList.get(0);
					paramsMap.put("crParentId", elementID);
					paramsMap.put("crParentType", elementType);
					//插入ott_jointinfo_source表记录
					String type = paramMap.get("Type");
					paramsMap.put("type", type);
					this.handleAssetsMapper.addJointinfo_source(paramsMap);
					picutreList = null;
					property = null;
				}
			}
			if("Movie".equals(elementType)){
				if("Program".equals(parentType) || "Series".equals(parentType)){
					//先判断Program、Series是否存在
					List<Map<String, String>> list = this.handleAssetsMapper.queryParentByIdAndType(parentID, parentType);
					if(CollectionUtils.isEmpty(list)){
						dealError(info, "ott_drama_program", "本地数据库不存在资产id为：" + parentID + "资产类型为：" + parentType + "的资产信息", "1", parentID, parentType);
						isSuccess = false;
						continue;
					}
					
					//判断该Movies是否存在
					List<Map<String, String>> moivesList = this.handleAssetsMapper.queryPictureOrMoviesByIdAndType(elementID, elementType);
					if(CollectionUtils.isEmpty(moivesList)){
						dealError(info, "ott_jointinfo_source", "本地数据库不存在资产id为：" + elementID + "资产类型为：" + elementType + "的资产信息", "1", elementID, elementType);
						isSuccess = false;
						continue;
					}
					
					//判断该Movie是否已经被分配
					if(CollectionUtils.isNotEmpty(handleAssetsMapper.queryMovieByList(elementID, elementType, parentID, parentType)))
						continue;
					
					//获取ott_drama_children的id
					List<Map<String, Object>> childrenList = this.handleAssetsMapper.queryChildByIdAndType(parentID, parentType);
					String childId = null;
					if(CollectionUtils.isNotEmpty(childrenList))
						childId = String.valueOf(((Integer)childrenList.get(0).get("id")));
					String fileURL = moivesList.get(0).get("fileURL");
					
					Map<String, String> paramsMap = new HashMap<String, String>();
					paramsMap.put("childId", childId);
					paramsMap.put("ftpUrl", fileURL);
					paramsMap.put("playUrl", Global.C2_ZHONGXING_VODPLAYURL.replaceAll("elementCode", elementID));   //百途对应的CDN平台商有中兴跟华为，中兴的获取MovieId，华为的获取ProgramId
					paramsMap.put("bitrate", moivesList.get(0).get("movieBitrate"));
					paramsMap.put("bitrateType", moivesList.get(0).get("movieBitrateType"));
					paramsMap.put("status", "1");
					paramsMap.put("source", "0"); //0表示中兴;1表示华为
					//先判断ott_drama_source表是否已经存在该中兴记录
					if(this.handleAssetsMapper.queryDramaSource(paramsMap) == null)
						this.handleAssetsMapper.addDrama_source(paramsMap);              //插入中兴播放记录
					
					paramsMap.put("playUrl", parentID);
					paramsMap.put("source", "1");
					if(this.handleAssetsMapper.queryDramaSource(paramsMap) == null)
						this.handleAssetsMapper.addDrama_source(paramsMap);              //插入华为播放记录
					
					//插入ott_jointinfo_source表记录
					Map<String, String> contentMap = new HashMap<String, String>();
					contentMap = moivesList.get(0);
					contentMap.put("crParentId", parentID);
					contentMap.put("crParentType", parentType);
					this.handleAssetsMapper.addJointinfo_source(contentMap);
					paramsMap = null;
					contentMap = null;
					moivesList = null;
					childrenList = null;
				}
			}
		}
	}
	
	/**
	 * 与内容提供商百途信息对接接口(采用apache-axis1.4框架)
	 * 
	 * 创建人：lizhenghg 创建时间：2016-12-20
	 * 
	 * @param String CSPID, String LSPID, String CorrelateID, String CmdFileURL
	 * @return 返回类型：execCmd
	 * @exception try{}catch
	 */
	public void addAssetInfo(Map<String, String> paramsMap){
		this.handleAssetsMapper.addAssets(paramsMap);
	}
	
	/**
	 * 每20分钟处理一次内容提供商百途(csp)提供的文件的图片下载情况
	 * 
	 * 创建人：lizhenghg 创建时间：2017-01-10
	 * 
	 * @return 返回类型：void
	 * @throws DocumentException 
	 * @throws RemoteException 
	 * 
	 * @exception try
	 *                {}catch
	 */
	public void toParseAssets(){
		List<Map<String, Object>> queryAllPendPic = this.handleAssetsMapper.queryAllPendPic();
		if (CollectionUtils.isNotEmpty(queryAllPendPic)) {
			Map<String, Object> paramsMap = null;
			Map<String, String> map = null;
			boolean flag = false;
			String cmdFileURL = null;
			for (int i = 0, n = queryAllPendPic.size(); i < n; i++) {
				paramsMap = queryAllPendPic.get(i);
				cmdFileURL = (String) paramsMap.get("fileURL");  //ftp://mac2movie:mac2movie@172.25.37.235:5710/pic//SDGDZX/20170105/20170105225100774.jpg
				map = getFtpInfo(null, cmdFileURL, Global.BT_ASSET_DOWNLOADPATH);  //Global.BT_ASSETPIC_DOWNLOADPATH为：/home/soft/resources
				map.put("parsePic", "ok");
				map.put("picId", String.valueOf(((Integer)paramsMap.get("id"))));
				flag = toCreateFile(map);
				if (flag){
					paramsMap.put("isCreateFile", "0"); // 表示本地已经成功生成文件
					paramsMap.put("isDealFile", "0");
					paramsMap.put("localFilePath", map.get("fileName"));
				}else{
					paramsMap.put("isCreateFile", "1"); // 表示本地生成文件失败
					paramsMap.put("isDealFile", "1");
				}
				this.handleAssetsMapper.updateJointinfoSource(paramsMap);
			}
		}
	}
	
	/**
	 * 每30分钟处理一次内容提供商百视通(csp)提供的图片分发
	 * 
	 * 创建人：lizhenghg 创建时间：2017-01-10
	 * 
	 * @return 返回类型：void
	 * @throws DocumentException 
	 * @throws RemoteException 
	 * 
	 * @exception try
	 *                {}catch
	 */
	public void toDispatchPic(){
		List<Map<String, Object>> dispatchPics = this.handleAssetsMapper.queryAllDispatchPics();
		if(CollectionUtils.isNotEmpty(dispatchPics)){
			Iterator<Map<String, Object>> it = dispatchPics.iterator();
			Map<String, Object> params = null;
			File file = null;
			String imgUrl = null;
			String secondsDir = Global.EPG_IMG + File.separator + DateUtil.getCurrentDate();   // /epg/2017-06-02
			DramaProgram dp = new DramaProgram();
			try {
				while(it.hasNext()){
					params = it.next();
					if(StringUtils.isBlank((String)params.get("dramaId")) || "0".equals((String)params.get("dramaId")))
						continue;
					file = new File(Global.BT_ASSET_DOWNLOADPATH + params.get("localFilePath").toString());
					if(file.exists() && file.isFile())
						imgUrl = UploadFile.uploadFileToServer(file, "", secondsDir, "");
					else
						continue;
					dp.setId(params.get("dramaId").toString());
					if("1".equals(params.get("type").toString())){                                 //type值为1，图形为大横图，表示我们的推荐大小图
						dp.setRecXImg(imgUrl);
					}
					if("2".equals(params.get("type").toString()) || "0".equals(params.get("type").toString())){                                 //type值为2，图形为小竖图，表示我们的封面大小图
						dp.setCoverXImg(imgUrl);
					}
					if(StringUtil.isNotEmpty(dp.getRecXImg()) || StringUtil.isNotEmpty(dp.getCoverXImg()))
						dramaProgramMapper.updateDramaImg(dp);
					handleAssetsMapper.updateJointinfoSource2(params);
					dp.setRecXImg("");
					dp.setCoverXImg("");
				}
			} catch (Exception e) {
				logger.error("图片分发出现异常：" + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	/**查询资产列表
	 * @author lizhenghg 2016-12-12
	 * @param request
	 * @param asset
	 * @param model
	 * @return List<Assets>
	 */
	public List<Assets> findAssetsByPage(Assets assets){
		int totalResult = handleAssetsMapper.getCountHandleAssets(assets);
		assets.setTotalResult(totalResult);
		return handleAssetsMapper.findAssetsByPage(assets);
	}
	
	/**查询资产详情
	 * @author lizhenghg 2016-12-12
	 * @param request
	 * @param model
	 * @return String
	 */
	public Assets getAssetsByCorrelateID(String correlateID){
		return this.handleAssetsMapper.getAssetsByCorrelateID(correlateID);
	}
	
	/**(批量)生成资产文件
	 * @author lizhenghg 2016-12-12
	 * @param request
	 * @param model
	 * @return String
	 */
	public Feedback toCreateFileForBatch(String assetlist){
		String[] assets = assetlist.split(";");
		String cmdFileURL = null;
		String correlateID = null;
		Map<String, String> paramsMap = new HashMap<String, String>();
		Map<String, String> map = null;
		boolean flag = false;
		if(null != assets && assets.length > 0){
			for(String asset : assets){
				cmdFileURL = asset.substring(0, asset.indexOf("|"));
				correlateID = asset.substring(asset.indexOf("|") + 1);
				map = getFtpInfo(correlateID, cmdFileURL, Global.BT_ASSET_DOWNLOADPATH);
				flag = toCreateFile(map);
				if (flag){
					paramsMap.put("isCreateFile", "0"); // 表示本地已经成功生成文件
					map.put("status", "0");
				}else{
					paramsMap.put("isCreateFile", "1"); // 表示本地生成文件失败
					map.put("status", "-1");
				}
				map.put("tableName", "ott_jointinfo");
				map.put("handleType", "1");
				paramsMap.put("modifyFileName", "ok");
				paramsMap.put("fileName", map.get("fileName"));
				paramsMap.put("createTime", "ok");
				this.handleAssetsMapper.updateJointInfo(paramsMap);
				this.handleAssetsMapper.addJointinfo_handlelog(map);
			}
		}
		return new Feedback(true, "操作成功");
	}
	
	/**(批量)处理资产文件
	 * @author lizhenghg 2016-12-12
	 * @param request
	 * @param model
	 * @return String
	 */
	public Feedback toDealFileForBatch(String assetlist){
		String[] assets = assetlist.split(";");
		String correlateID = null;
		List<Map<String, String>> assetLists = new ArrayList<Map<String, String>>();
		if(null != assets && assets.length > 0){
			for(String asset : assets){
				correlateID = asset.substring(asset.indexOf("|") + 1);
				Map<String, String> p = new HashMap<String, String>();
				p.put("correlateID", correlateID);
				assetLists.add(p);
			}
		}
		parseFile(assetLists);
		return new Feedback(true, "操作成功");
	}
	
	public List<Map<String, Object>> queryOtherCategory(){
		return this.handleAssetsMapper.queryOtherCategory();
	}
	
	public void updateOtherCategory(Map<String, Object> paramsMap){
		this.handleAssetsMapper.updateOtherCategory(paramsMap);
	}
	
	public List<Map<String, String>> queryJointInfoTypeInfo(String elementID, String elementType){
		return this.handleAssetsMapper.queryJointInfoTypeInfo(elementID, elementType);
	}
	
	public void addJointInfoType(String localNavId, String elementID, String elementCode, String elementType){
		this.handleAssetsMapper.addJointInfoType(localNavId, elementID, elementCode, elementType);
	}
}
