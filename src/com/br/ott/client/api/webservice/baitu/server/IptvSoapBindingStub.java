/**
 * IptvSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.br.ott.client.api.webservice.baitu.server;

import java.net.URL;
import java.rmi.RemoteException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.encoding.DeserializerFactory;
import org.apache.axis.encoding.SerializerFactory;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.soap.SOAPConstants;
import org.apache.log4j.Logger;

import com.br.ott.client.operasset.service.HandleAssetsService;
import com.br.ott.common.util.spring.SpringContextHolder;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class IptvSoapBindingStub extends Stub implements ItvServiceImpl {
	private java.util.Vector cachedSerClasses = new java.util.Vector();
	private java.util.Vector cachedSerQNames = new java.util.Vector();
	private java.util.Vector cachedSerFactories = new java.util.Vector();
	private java.util.Vector cachedDeserFactories = new java.util.Vector();

	private Logger logger = Logger.getLogger(IptvSoapBindingStub.class);
	
	static OperationDesc[] _operations;

	static {
		_operations = new OperationDesc[1];
		_initOperationDesc1();
	}

	private static void _initOperationDesc1() {
		OperationDesc oper;
		ParameterDesc param;
		oper = new OperationDesc();
		oper.setName("ExecCmd");
		param = new ParameterDesc(new QName("", "CSPID"),
				org.apache.axis.description.ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(
				new QName("", "LSPID"),
				ParameterDesc.IN,
				new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"),
				String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("", "CorrelateID"),
				ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				String.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("", "CmdFileURL"),
				ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("urn:ItvServiceImpl", "CSPResult"));
		oper.setReturnClass(ItvServiceImpl.class);
		oper.setReturnQName(new QName("", "ExecCmdReturn"));
		oper.setStyle(Style.RPC);
		oper.setUse(Use.ENCODED);
		_operations[0] = oper;

	}

	public IptvSoapBindingStub() throws AxisFault {
		this(null);
	}

	public IptvSoapBindingStub(URL endpointURL, javax.xml.rpc.Service service)
			throws AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public IptvSoapBindingStub(javax.xml.rpc.Service service)
			throws org.apache.axis.AxisFault {
		if (service == null) {
			super.service = new org.apache.axis.client.Service();
		} else {
			super.service = service;
		}
		((org.apache.axis.client.Service) super.service)
				.setTypeMappingVersion("1.2");
		Class cls;
		QName qName;
		Class beansf = BeanSerializerFactory.class;
		Class beandf = BeanDeserializerFactory.class;
		qName = new QName("urn:ItvServiceImpl", "CSPResult");
		cachedSerQNames.add(qName);
		cls = ItvServiceImpl.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);
	}

	protected Call createCall() throws RemoteException {
		try {
			Call _call = super._createCall();
			if (super.maintainSessionSet) {
				_call.setMaintainSession(super.maintainSession);
			}
			if (super.cachedUsername != null) {
				_call.setUsername(super.cachedUsername);
			}
			if (super.cachedPassword != null) {
				_call.setPassword(super.cachedPassword);
			}
			if (super.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(super.cachedEndpoint);
			}
			if (super.cachedTimeout != null) {
				_call.setTimeout(super.cachedTimeout);
			}
			if (super.cachedPortName != null) {
				_call.setPortName(super.cachedPortName);
			}
			Enumeration keys = super.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				_call.setProperty(key, super.cachedProperties.get(key));
			}
			// All the type mapping information is registered
			// when the first call is made.
			// The type mapping information is actually registered in
			// the TypeMappingRegistry of the service, which
			// is the reason why registration is only needed for the first call.
			synchronized (this) {
				if (firstCall()) {
					// must set encoding style before registering serializers
					_call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
					_call.setEncodingStyle(Constants.URI_SOAP11_ENC);
					for (int i = 0; i < cachedSerFactories.size(); ++i) {
						Class cls = (Class) cachedSerClasses.get(i);
						QName qName = (QName) cachedSerQNames.get(i);
						Object x = cachedSerFactories.get(i);
						if (x instanceof Class) {
							Class sf = (Class) cachedSerFactories.get(i);
							Class df = (Class) cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						} else if (x instanceof SerializerFactory) {
							SerializerFactory sf = (SerializerFactory) cachedSerFactories
									.get(i);
							DeserializerFactory df = (DeserializerFactory) cachedDeserFactories
									.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						}
					}
				}
			}
			return _call;
		} catch (Throwable _t) {
			throw new AxisFault("Failure trying to get the Call object", _t);
		}
	}
	
	/**
	 * 与内容提供商百途信息对接接口(采用apache-axis1.4框架)
	 * 
	 * 创建人：lizhenghg 创建时间：2016-12-20
	 * 
	 * @param String CSPID, String LSPID, String CorrelateID, String CmdFileURL
	 * @return 返回类型：execCmd
	 * @exception try{}catch
	 */
	@Override
	public CSPResult execCmd(String CSPID, String LSPID, String CorrelateID,
			String CmdFileURL) {
		CSPResult result = new CSPResult();
		try{
			HandleAssetsService handleAssetsService = SpringContextHolder.getBean(HandleAssetsService.class);
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("cspid", CSPID);
			paramsMap.put("lspid", LSPID);
			paramsMap.put("correlateID", CorrelateID);
			paramsMap.put("cmdFileURL", CmdFileURL);
			paramsMap.put("localFileName", CorrelateID + CmdFileURL.substring(CmdFileURL.lastIndexOf("/") + 1));
			handleAssetsService.addAssetInfo(paramsMap);	
			result.setResult(0);
		}catch(Exception e){
			result.setResult(-1);
			result.setErrorDescription(e.getMessage());
		}
		return result;
	}
}
