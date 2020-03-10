package com.br.ott.client.common.quartz;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/* 
 *  
 *  文件名：PropertiesUtils.java
 *  创建人：pananz
 *  创建时间：2012-12-31 - 下午6:00:39
 */
public final class PropertiesUtils {
	private static Logger logger = Logger.getLogger(PropertiesUtils.class);
	private String path;

	public PropertiesUtils(String fileName) {
		path = this.getClass().getResource("").getPath() + fileName;
	}

	public String getParam(String field) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			Properties prop = new Properties();
			prop.load(fis);
			return prop.getProperty(field);
		} catch (IOException e) {
			logger.error("load Properties encounter error!", e);
		} finally {
			try {
				if(fis!=null){
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public boolean setParam(String key, String value) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(path);
			Properties prop = new Properties();
			prop.load(fis);
			logger.debug(path + "==" + prop.getProperty(key));
			fos = new FileOutputStream(path);
			prop.setProperty(key, value);
			// 将Properties集合保存到流中
			prop.store(fos, "Update '" + key + "' value to " + value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
