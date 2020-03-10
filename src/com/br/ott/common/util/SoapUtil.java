package com.br.ott.common.util;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/* 
 *  
 *  文件名：SoapUtil.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：Administrator
 *  创建时间：2013-7-15 - 下午2:29:51
*/
public final class SoapUtil {

	public static void soap2String(Source source) throws Exception {
		if (source != null) {
			Node root = null;
			if (source instanceof DOMSource) {
				root = ((DOMSource) source).getNode();
			} else if (source instanceof SAXSource) {
				InputSource inSource = ((SAXSource) source).getInputSource();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setNamespaceAware(true);
				Document doc = dbf.newDocumentBuilder().parse(inSource);
				root = (Node) doc.getDocumentElement();
			}
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(new DOMSource(root), new StreamResult(System.out));
		}
	}
}
