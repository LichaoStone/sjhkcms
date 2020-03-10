/*
 * @# SystemExceptionCode.java Aug 23, 2012 2:11:19 PM
 * 
 * Copyright (C) 2011 - 2012 博瑞立方
 * BoRuiCube information technology co. ltd.
 * 
 * All rights reserved!
 */
package com.br.ott.common.exception;

/*
 * @author pananz
 * TODO
 */
public final class SystemExceptionCode {

	/**
	 * 系统异常公共
	 * @author xulun
	 */
	public final static class SysCommonCode {
		//系统未知异常
		public static final String UNKNOWN_ERROR = "20000";
		//异常消息读取失败
		public static final String EXCEPTION_MSG_READER_ERROR = "20001";
		//系统错误异常
		public static final String SYSTEM_ERROR = "20002";

		private SysCommonCode() {
			//empty!
		}
	}

	private SystemExceptionCode() {
		//empty!
	}

	/**
	 * 上传文件异常
	 * @author pananz
	 *
	 */
	public final static class UploadCode {
		//文件找不到
		public static final String FILE_NO_FOUND = "30000";
		//上传文件出错
		public static final String FILE_IO_UPLOAD_ERROR = "30001";
		//上传文件释放资源出错
		public static final String FILE_IO_CLOSE_ERROR = "30002";

		public static final String PRODUCT_OVER_MAX_SIZE = "30003";

		public static final String PARTNER_OVER_MAX_SIZE = "30004";
		public static final String PARTNER_OVER_MAX_SIZE2 = "30005";
		public static final String PARTNER_OVER_MAX_SIZE3 = "30006";
		public static final String PARTNER_OVER_MAX_SIZE4 = "30007";

		private UploadCode() {

		}
	}

	public final static class SyncCode {
		/*
		 * 0 成功。 1失败。
		 */
		public static final String SYNC_TVOD_SUCCESS = "0";
		public static final String SYNC_TVOD_ERROR = "1";
	}
	/**
	 * 对外接口异常
	 * @author pananz
	 *
	 */
	public final static class ApiCode {
		// 成功
		public static final String SUCCESS = "0";
		/** 系统错误 */
		public static final String ERROR = "500";
		/** 必填项参数为空 */
		public static final String NULL_PARAM = "99";
		/** 接口数据解析失败 */
		public static final String DECODE_JSON_ERROR = "9040000";
		private ApiCode() {
			//empty
		}
	}
}
