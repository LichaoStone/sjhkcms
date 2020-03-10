/**
 * CSPResponseSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.br.ott.client.api.webservice.baitu.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;
import org.apache.axis.Constants;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.encoding.DeserializerFactory;
import org.apache.axis.encoding.ser.ArrayDeserializerFactory;
import org.apache.axis.encoding.ser.ArraySerializerFactory;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.encoding.ser.EnumDeserializerFactory;
import org.apache.axis.encoding.ser.EnumSerializerFactory;
import org.apache.axis.encoding.ser.SimpleDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListSerializerFactory;
import org.apache.axis.encoding.ser.SimpleSerializerFactory;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

import com.br.ott.Global;

public class CSPResponseSoapBindingStub extends Stub implements
		ItvServiceImplResult {
	private java.util.Vector cachedSerClasses = new java.util.Vector();
	private java.util.Vector cachedSerQNames = new java.util.Vector();
	private java.util.Vector cachedSerFactories = new java.util.Vector();
	private java.util.Vector cachedDeserFactories = new java.util.Vector();

	static OperationDesc[] _operations;

	static {
		_operations = new OperationDesc[1];
		_initOperationDesc1();
	}

	private static void _initOperationDesc1() {
		OperationDesc oper;
		ParameterDesc param;
		oper = new OperationDesc();
		oper.setName("ResultNotify");
		param = new ParameterDesc(
				new QName("", "CSPID"),
				ParameterDesc.IN,
				new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"),
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
		param = new ParameterDesc(new QName("", "xmlReturn"), ParameterDesc.IN,
				new QName("http://www.w3.org/2001/XMLSchema", "int"),
				int.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("", "returnfileurl"),
				ParameterDesc.IN, new QName(
						"http://schemas.xmlsoap.org/soap/encoding/", "string"),
				String.class, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("urn:ItvServiceImplResult", "CSPResult"));
		oper.setReturnClass(CSPResult.class);
		oper.setReturnQName(new QName("", "ResultNotifyReturn"));
		oper.setStyle(Style.RPC);
		oper.setUse(Use.ENCODED);
		_operations[0] = oper;

	}

	public CSPResponseSoapBindingStub() throws AxisFault {
		this(null);
	}

	public CSPResponseSoapBindingStub(URL endpointURL,
			javax.xml.rpc.Service service) throws AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public CSPResponseSoapBindingStub(javax.xml.rpc.Service service)
			throws AxisFault {
		if (service == null) {
			super.service = new org.apache.axis.client.Service();
		} else {
			super.service = service;
		}
		((org.apache.axis.client.Service) super.service)
				.setTypeMappingVersion("1.2");
		Class cls;
		QName qName;
		QName qName2;
		Class beansf = BeanSerializerFactory.class;
		Class beandf = BeanDeserializerFactory.class;
		Class enumsf = EnumSerializerFactory.class;
		Class enumdf = EnumDeserializerFactory.class;
		Class arraysf = ArraySerializerFactory.class;
		Class arraydf = ArrayDeserializerFactory.class;
		Class simplesf = SimpleSerializerFactory.class;
		Class simpledf = SimpleDeserializerFactory.class;
		Class simplelistsf = SimpleListSerializerFactory.class;
		Class simplelistdf = SimpleListDeserializerFactory.class;
		qName = new QName("urn:ItvServiceImplResult", "CSPResult");
		cachedSerQNames.add(qName);
		cls = CSPResult.class;
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
						} else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
							org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) cachedSerFactories
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
			throw new org.apache.axis.AxisFault(
					"Failure trying to get the Call object", _t);
		}
	}

	public CSPResult resultNotify(String CSPID, String LSPID,
			String correlateID, int xmlReturn, String returnfileurl) throws RemoteException, ServiceException, MalformedURLException {
		Service service = new Service();
		Call call = (Call)service.createCall();
		call.setOperation(_operations[0]);
		call.setUseSOAPAction(true);
		call.setSOAPActionURI("");
		call.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
		call.setTargetEndpointAddress(new java.net.URL(Global.C2_BAITU_IP));
		call.setOperationName(new QName(Global.C2_BAITU_IP, "ResultNotify"));
		setRequestHeaders(call);
		setAttachments(call);
		try {
			Object resp = call.invoke(new Object[] { CSPID, LSPID,
					correlateID, new java.lang.Integer(xmlReturn),
					returnfileurl });

			if (resp instanceof RemoteException) {
				throw (RemoteException) resp;
			} else {
				extractAttachments(call);
				try {
					return (CSPResult) resp;
				} catch (Exception _exception) {
					return (CSPResult) JavaUtils
							.convert(resp, CSPResult.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

}
