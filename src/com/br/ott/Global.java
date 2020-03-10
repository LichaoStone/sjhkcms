package com.br.ott;

/*
 * 文 件 名:  Global.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  cKF46827
 * 修改时间:  2011-8-30
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

import com.br.ott.common.util.ReadProperties;
import com.br.ott.common.util.StringUtil;

/**
 * <全局变量>
 * 
 * @author cKF46827
 * @version [版本号, 2011-8-30]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class Global {

	private Global() {
	}

	/**
	 * 当次请求发起时的系统时间
	 */
	public static final String SESSION_REQ_SYSTEM_TIME = "system_req_time";

	private static ReadProperties prop = new ReadProperties("config.properties");

	public static final String CURRENT_USER = "user";

	public static final String COMMUN_USER = "comm";
	
	/** 合作伙伴图片路径 */
	public static final String PARTNER_IMG_URL = prop.getPara("partner.img");

	/** 资源WEB地址 */
	public static final String SERVER_SOURCE_URL = prop
			.getPara("server.source.url");
	/** 上传图片等地址 */
	public static final String SOURCE_UPLOAD_SOURCE_URL = prop
			.getPara("sourceUpload.source.url");

	/** 发送邮件服务器地址 */
	public static final boolean EMAIL_ENABLE = StringUtil.isEmpty(prop
			.getPara("email.enable")) ? false : Boolean.valueOf(prop
			.getPara("email.enable"));
	public static final String RECEIVE_EMAIL_ADRRESS = prop
			.getPara("receive.email.address");

	public static final String EMAIL_HOSTNAME = prop.getPara("email.hostName");
	public static final String SEND_EMAIL_ADRRESS = prop
			.getPara("send.email.adrress");
	public static final String SEND_EMAIL_PORT = prop
			.getPara("send.email.port");
	public static final String SEND_EMAIL_USERNAME = prop
			.getPara("send.email.userName");
	public static final String SEND_EMAIL_PASSWORD = prop
			.getPara("send.email.password");

	/** 邮箱内容 */
	public static final String SEND_EMAIL_EMAILCONTENT = prop
			.getPara("send.email.emailContent");
	/** 邮箱主题 */
	public static final String SEND_EMAIL_EMAILSUBJECT = prop
			.getPara("send.email.emailSubject");

	public static final String CONTENT_TYPE = "Content-Type";

	public static final String ERROR_CODE = "ERRORCODE";

	public static final String ERROR_MESSAGE = "ERRORMESSAGE";

	/**
	 * 通过KEY获取对应的值 创建人：陈登民 创建时间：2012-12-3 - 下午2:41:52
	 * 
	 * @param key
	 * @return 返回类型：String
	 * @exception throws
	 */
	public static String getProperties(String key) {
		try {
			return prop.getPara(key);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static final boolean FTP_ENABLE = Boolean.valueOf(prop
			.getPara("ftp.enable"));
	public static final String FTP_HOSTNAME = prop.getPara("yd.ftp.hostname");
	public static final int FTP_PORT = StringUtil.isEmpty(prop
			.getPara("yd.ftp.port")) ? 21 : Integer.valueOf(prop
			.getPara("yd.ftp.port"));
	public static final String FTP_ACCOUNT = prop.getPara("yd.ftp.account");
	public static final String FTP_PASSWD = prop.getPara("yd.ftp.passwd");

	public static final String EPG_IMG = prop.getPara("epg.img");

	public static final String CSP_EPG_IMG = prop.getPara("bt_epg_img");
	public static final String CSP_FILE_PATH = prop.getPara("bt_file_path");
	public static final String SOURCE_PATH = prop.getPara("source.path");
	public static final String ASSET_DATA = prop.getPara("asset.data");

	public static final String HS_ASSET_OFF_URL = prop
			.getPara("hs.asset.off.url");
	
	/**
	 * C2对接使用到的参数开始
	 */
	public static final String BT_ASSET_DOWNLOADPATH = prop.getPara("bt.asset.downloadPath");
		
	public static final String BT_SECOND_ID = prop.getPara("bt.second.id");
		
	public static final String LOCAL_IP = prop.getPara("local.ip");
	
	static public final String C2_ZHONGXING_VODPLAYURL = prop.getPara("c2_zhongxing_vodPlayUrl");
	
	static public final String C2_BAITU_IP = prop.getPara("c2_baitu_ip");
	/**
	 * C2对接使用到的参数结束
	 */
	
	/**
	 * 内容提供商提供的直播节目单保存路径
	 */
	public static final String ZHIBO_PROGRAM_DOWNLOADPATH = prop.getPara("zhibo_program.downloadPath");
	
}
