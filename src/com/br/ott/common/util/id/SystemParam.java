package com.br.ott.common.util.id;

import org.apache.commons.lang.StringUtils;

/**
 * 获取系统标识，每台服务器都需在启动脚本处设置系统参数，参数值为2位数，默认为99
 * 
 * @author chenzongqi
 *
 */
public final class SystemParam {
	
	private SystemParam() {
		// empty!
	}
	
	/**
	 * 获取当前机器系统标识
	 * 
	 * @return String系统标识
	 */
	public static String getCurrentSysId() {
		return StringUtils.isEmpty(System.getProperty("sysId")) ? "99"
				: System.getProperty("sysId");
	}
}
