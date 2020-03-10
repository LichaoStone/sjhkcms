/**
 * ItvServiceImplResultServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.br.ott.client.api.webservice.baitu.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

public class ItvServiceImplResultServiceLocator extends Service implements ItvServiceImplResultService {

    public ItvServiceImplResultServiceLocator() {
    }


    public ItvServiceImplResultServiceLocator(EngineConfiguration config) {
        super(config);
    }

    public ItvServiceImplResultServiceLocator(String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CSPResponse
    private String CSPResponse_address = "http://10.23.252.13/mac2interface/services/CSPResponse?wsdl";

    public String getCSPResponseAddress() {
        return CSPResponse_address;
    }

    // The WSDD service name defaults to the port name.
    private String CSPResponseWSDDServiceName = "CSPResponse";

    public String getCSPResponseWSDDServiceName() {
        return CSPResponseWSDDServiceName;
    }

    public void setCSPResponseWSDDServiceName(String name) {
        CSPResponseWSDDServiceName = name;
    }

    public ItvServiceImplResult getCSPResponse() throws ServiceException {
       URL endpoint;
        try {
            endpoint = new URL(CSPResponse_address);
        }
        catch (MalformedURLException e) {
            throw new ServiceException(e);
        }
        return getCSPResponse(endpoint);
    }

    public ItvServiceImplResult getCSPResponse(URL portAddress) throws ServiceException {
        try {
            CSPResponseSoapBindingStub _stub = new CSPResponseSoapBindingStub(portAddress, this);
            _stub.setPortName(getCSPResponseWSDDServiceName());
            return _stub;
        }
        catch (AxisFault e) {
            return null;
        }
    }

    public void setCSPResponseEndpointAddress(String address) {
        CSPResponse_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if (ItvServiceImplResult.class.isAssignableFrom(serviceEndpointInterface)) {
                CSPResponseSoapBindingStub _stub = new CSPResponseSoapBindingStub(new URL(CSPResponse_address), this);
                _stub.setPortName(getCSPResponseWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new ServiceException(t);
        }
        throw new ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public Remote getPort(QName portName, Class serviceEndpointInterface) throws ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("CSPResponse".equals(inputPortName)) {
            return getCSPResponse();
        }
        else  {
            Remote _stub = getPort(serviceEndpointInterface);
            ((Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public QName getServiceName() {
        return new QName("http://10.23.252.13/mac2interface/services/CSPResponse?wsdl", "ItvServiceImplResultService");
    }

    private HashSet ports = null;

    public Iterator getPorts() {
        if (ports == null) {
            ports = new HashSet();
            ports.add(new QName("http://10.23.252.13/mac2interface/services/CSPResponse?wsdl", "CSPResponse"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws ServiceException {
        
if ("CSPResponse".equals(portName)) {
            setCSPResponseEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(QName portName, String address) throws ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
