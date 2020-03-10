package com.br.ott.common.util;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * 解析xml类
 * <功能详细描述>
 * 
 * @author  jKF46825
 * @version  [版本号, May 11, 2011]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public final class ReadXML {
	/**
	 * 默认构造函数
	 */
	private ReadXML() {

	}
	/**
     * 实例化一个记录日志
     */
    private  static Logger logger=Logger.getLogger(ReadXML.class);
	/**
	 * 把xml解析成一个Document对象
	 * <功能详细描述>
	 * @param inputstream
	 * @return [参数说明]
	 * 
	 * @return Document [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static Document getDocment(InputStream inputstream) {
		try {
			DocumentBuilderFactory docfactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docbuilder = docfactory.newDocumentBuilder();
			Document doc = docbuilder.parse(inputstream);
			return doc;
		} catch (Exception e) {
		    logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 解析xml文件里面的数据
	 * <功能详细描述>
	 * @param rootElementName:上一级节点名称
	 * @param inputStream:文件输出流
	 * @return [参数说明]
	 * 
	 * @return HashMap<Object,String> [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static HashMap<String, Object> resolutionXMLin(
			String rootElementName, InputStream inputStream) {
		Document document = ReadXML.getDocment(inputStream);
		HashMap<String, Object> map = new HashMap<String, Object>();
		NodeList element = document.getElementsByTagName(rootElementName);
		for (int j = 0; j < element.getLength(); j++) {
			NodeList list = element.item(j).getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node.getTextContent().trim().length() > 0) {
				    logger.debug("tagName:" + node.getNodeName()
							+ "\t node value:" + node.getTextContent());
					map.put(node.getNodeName(), node.getTextContent());
				}
			}
		}
		if (map.size() > 0) {
			return (HashMap<String, Object>) map;
		}
		return null;
	}

	/**
	 * 解析xml文件里面的数据
	 * <功能详细描述>
	 * @param rootElementName:上一级节点名称
	 * @param inputStream:文件输出流
	 * @return [参数说明]
	 * 
	 * @return HashMap<Object,String> [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static HashMap<String, Object> resolutionXMLin2(
			String rootElementName, InputStream inputStream) {
		Document document = ReadXML.getDocment(inputStream);
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (document != null) {
			NodeList element = document.getElementsByTagName(rootElementName);
			for (int j = 0; j < element.getLength(); j++) {
				NodeList list = element.item(j).getChildNodes();
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					if (node.getTextContent().trim().length() > 0) {
						NodeList clist = node.getChildNodes();
						if (clist.getLength() > 0) {
							for (int k = 0; k < clist.getLength(); k++) {
								Node cnode = clist.item(k);
								map.put(cnode.getNodeName(), cnode
										.getTextContent());
							}
						} else {
							map.put(node.getNodeName(), node.getTextContent());
						}
					}
				}
			}
			if (map.size() > 0) {
				return map;
			}
			return null;
		} else {
			return null;
		}
	}
}
