/*
 * 文 件 名:  ReadProperties.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jKF46825
 * 修改时间:  2011-6-25
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.br.ott.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * <获取资源文件里面内容> <功能详细描述>
 * 
 * @author jKF46825
 * @version [版本号, 2011-6-25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class ReadProperties {
	/**
	 * 实例化一个记录日志对象
	 */
	private static Logger logger = Logger.getLogger(ReadProperties.class);
	private static Properties prop;
	private InputStream in;

	/**
	 * <默认构造函数>
	 */
	public ReadProperties(String fileName) {
		in = ReadProperties.class.getResourceAsStream("/" + fileName);
		if (prop == null) {
			prop = new Properties();
		}
		try {
			prop.load(in);
		} catch (IOException e) {
			logger.error("load Properties encounter error!", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * <输出一个属性的值> <功能详细描述>
	 * 
	 * @param fileName
	 *            :文件名
	 * @param field
	 *            :属性名字
	 * @return [参数说明]
	 * 
	 * @return String [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public String getPara(String field) {
		return prop.getProperty(field);
	}

	/**
	 * <输出一个属性的值> <多个参数引用的时候，用','隔开>
	 * 
	 * @param fileName
	 *            :文件名
	 * @param field
	 *            :属性名
	 * @param value
	 *            :属性值
	 * @return [参数说明]
	 * 
	 * @return String [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public String getPara(String field, String value) {
		String strMsg = prop.getProperty(field);
		Object[] tmpC = value.split(",");
		String message = MessageFormat.format(strMsg, tmpC);
		logger.debug(message);
		return message;
	}

	/**
	 * <输出资源文件全部内容> <功能详细描述>
	 * 
	 * @param fileName
	 *            [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void printList() {
		Enumeration<?> enu = prop.propertyNames(); // 取出所有的key
		prop.list(System.out); // System.out可以改为其他的输出流（包括可以输出到文件）
		while (enu.hasMoreElements()) {
			logger.debug("key=" + enu.nextElement());
			logger.debug("value="
					+ prop.getProperty((String) enu.nextElement()));
		}
	}

	public static void main(String[] args) {
		ReadProperties pro = new ReadProperties(
				"allMessages_zh_CN.input.properties");
		logger.debug(pro.getPara("Test001", "parm1"));
		String str = pro.getPara("tm_stus");
		logger.debug("str:" + str);
		pro.printList();
	}
}
