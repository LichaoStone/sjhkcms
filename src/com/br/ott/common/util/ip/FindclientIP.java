package com.br.ott.common.util.ip;

import javax.servlet.http.HttpServletRequest;
/**
 * 通过request获取请求客户端ip地址 
 * <功能详细描述>
 * 
 * @author  pKF46373
 * @version  [版本号, 2011-10-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class FindclientIP {
	/**
	 * 获取请求用户ip地址
	 * 
	 * @param request
	 * @return ip地址
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String getIpAddr(HttpServletRequest request) {   
	       String ip = request.getHeader("x-forwarded-for");   
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
	           ip = request.getHeader("Proxy-Client-IP");   
	       }   
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
	           ip = request.getHeader("WL-Proxy-Client-IP");   
	       }   
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
	           ip = request.getRemoteAddr();   
	       }   
	       return ip;   
	   } 
	
	/**
	 * <默认构造函数>
	 */
	public FindclientIP(){
		
	}
}
