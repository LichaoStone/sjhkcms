package com.br.ott.test;

import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;


/**
 * 文件名：BarTest.java 版权：BoRuiCubeNet. Copyright 2014-2015,All rights reserved
 * 公司名称：青岛博瑞立方科技有限公司 创建人：pananz 创建时间：2016-6-17
 */
public class BarTest {

	public static void main(String[] args) {
		try {
//			String wsdlUrl = "http://localhost/sjhkcms/services/test";

			 String wsdlUrl = "http://localhost/sjhkcms/services/iptv";
			String nameSpaceUri = "http://localhost/sjhkcms/services/iptv?wsdl";
			// 创建调用对象
			Service service = new Service();
			Call call = null;
			call = (Call) service.createCall();
			System.out.println(">>>sayHollo");
//			call.setOperationName("sayHollo");
			 call.setOperationName(new QName(nameSpaceUri, "execCmd"));
			call.setTargetEndpointAddress(new URL(wsdlUrl));
			String ret = (String) call.invoke(new Object[] { "pananz" });
			System.out.println("return value is " + ret);

			// testC2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public static void testC2() {
		try {
			ItvServiceImplService itvLocator = new ItvServiceImplServiceLocator();
			ItvServiceImpl itvImpl = itvLocator.getiptv();
			CSPResult result = itvImpl.execCmd("aa", "bb", "cc", "ddd");
			System.out.println(result.getResult() + ":"
					+ result.getErrorDescription());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}*/
}
