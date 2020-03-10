
package com.br.ott.common.util.spring;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * spring工具类.
 * 
 * 获取spring的bean对象.
 * 
 * @author alvin hwang
 */
public final class SpringUtils {

	private SpringUtils() {
		// empty!
	}
	
	/**
	 * 通过session上下文查找bean对象.
	 * 
	 * @param <T>
	 * @param session
	 *            session对象.
	 * @param beanName
	 *            spring配置的name信息.
	 * @param clazz
	 *            对象类型.
	 * @return spring的实例.
	 */
	public static <T> T getBean(HttpSession session, String beanName,
			Class<T> clazz) {
		ServletContext servletContext = session.getServletContext();
		return getBean(servletContext, beanName, clazz);
	}

	/**
	 * 通过<code>ServletContext</code>上下文查找bean对象.
	 * 
	 * @param <T>
	 * @param servletContext
	 *            servelet上下文信息.
	 * @param beanName
	 *            spring配置的name信息.
	 * @param clazz
	 *            对象类型.
	 * @return spring的实例.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(ServletContext servletContext, String beanName,
			Class<T> clazz) {
		Object bean = null;
		WebApplicationContext wac = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		if (wac != null) {
			bean = wac.getBean(beanName);
		}
		return (T) bean;
	}
}
