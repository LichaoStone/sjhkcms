package com.br.ott.common.util;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

/* 
 *  VELOCITY 解析工具
 *  文件名：VelocityUtil.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：陈登民
 *  创建时间：2012-12-11 - 下午5:34:10
 */
public class VelocityUtil {
	private String encoding = "UTF-8";
	/**
	 * URL方式
	 */
	public static final String URL_LOADER = "url";
	/**
	 * 文件方式
	 */
	public static final String FILE_LOADER = "file";
	/**
	 * JAR文件中的VM
	 */
	public static final String JAR_LOADER = "jar";
	/**
	 * ClassPath下的VM
	 */
	public static final String CLASSPATH_LOADER = "class";

	public VelocityUtil() {
	}

	public VelocityUtil(String encoding) {
		this.encoding = encoding;
	}

	public void setEncoding(String encoding) {
		if (!StringUtil.isEmpty(encoding)) {
			this.encoding = encoding;
		}
	}

	/**
	 * 解析VM并填充数据后，返回整个页面内容
	 * 创建人：陈登民
	 * 创建时间：2012-12-11 - 下午6:52:26
	 * @param type VM解析方式 调用该类的常量
	 * @param path 文件的上级目录路径（如：D:\\path\\、http://localhost:8080/file/）
	 * @param fileName VM文件名（如：index.vm）
	 * @param params 参数
	 * @return
	 * 返回类型：String 解析后的整个页面内容（<html>....</html>）
	 * @throws java.lang.Exception 
	 * @exception throws
	 */
	public String parse(String type, String path, String fileName, Map<String, Object> params)
			throws java.lang.Exception {
		Writer writer = null;
		Properties p = setProperty(type, path);
		try {
			if (p == null)
				return null;

			VelocityEngine engine = new VelocityEngine(p);
			VelocityContext context = new VelocityContext();
			Set<Entry<String, Object>> set = params.entrySet();
			Iterator<Entry<String, Object>> it = set.iterator();
			if (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				context.put(entry.getKey(), entry.getValue());
			}
			writer = new StringWriter();
			engine.mergeTemplate(fileName, encoding, context, writer);
			writer.flush();
			return writer.toString();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
			throw new java.lang.Exception();
		} finally {
			p = null;
			if (writer != null) {
				writer.close();
			}
		}
	}

	private Properties setProperty(String type, String root) {
		if (StringUtil.isEmpty(type)) {
			return null;
		}
		Properties p = new Properties();
		p.setProperty(Velocity.ENCODING_DEFAULT, encoding);
		p.setProperty(Velocity.INPUT_ENCODING, encoding);
		p.setProperty(Velocity.OUTPUT_ENCODING, encoding);
		p.setProperty("file.resource.loader.cache", String.valueOf(false));

		String loaderClassKey = "";
		String loaderClassValue = "";
		String loaderPathkey = "";
		if (URL_LOADER.equals(type)) {
			loaderClassKey = "url.resource.loader.class";
			loaderClassValue = "org.apache.velocity.runtime.resource.loader.URLResourceLoader";
			loaderPathkey = "url.resource.loader.root";
		} else if (JAR_LOADER.equals(type)) {
			loaderClassKey = "jar.resource.loader.class";
			loaderClassValue = "org.apache.velocity.runtime.resource.loader.JarResourceLoader";
			loaderPathkey = "jar.resource.loader.path";
		} else if (FILE_LOADER.equals(type)) {
			loaderClassKey = "file.resource.loader.class";
			loaderClassValue = "org.apache.velocity.runtime.resource.loader.FileResourceLoader";
			loaderPathkey = "file.resource.loader.path";
		} else if (CLASSPATH_LOADER.equals(type)) {
			loaderClassKey = "class.resource.loader.class";
			loaderClassValue = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";
			loaderPathkey = "jar.resource.loader.path";
		}

		p.setProperty("resource.loader", type);
		p.setProperty(loaderClassKey, loaderClassValue);
		p.setProperty(loaderPathkey, root);
		return p;
	}

	public static void main(String[] args) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("con", "hello ....nice to meet you ...");
			VelocityUtil vu = new VelocityUtil();
			String html = vu.parse(VelocityUtil.URL_LOADER,
					"http://10.10.10.252:8080/source/template/2236679464639/detail/", "template.vm", params);
			System.out.println(html);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
