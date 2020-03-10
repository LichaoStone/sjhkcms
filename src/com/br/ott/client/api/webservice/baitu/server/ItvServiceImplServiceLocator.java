/**
 * ItvServiceImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.br.ott.client.api.webservice.baitu.server;

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

public class ItvServiceImplServiceLocator extends Service implements ItvServiceImplService {

    public ItvServiceImplServiceLocator() {
    }


    public ItvServiceImplServiceLocator(EngineConfiguration config) {
        super(config);
    }

    public ItvServiceImplServiceLocator(String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for iptv
    private String iptv_address = "http://10.23.243.81:10537/sdhkcms/services/iptv?wsdl";

    public String getiptvAddress() {
        return iptv_address;
    }

    // The WSDD service name defaults to the port name.
    private String iptvWSDDServiceName = "iptv";

    public String getiptvWSDDServiceName() {
        return iptvWSDDServiceName;
    }

    public void setiptvWSDDServiceName(String name) {
        iptvWSDDServiceName = name;
    }

    public ItvServiceImpl getiptv() throws ServiceException {
       URL endpoint;
        try {
            endpoint = new URL(iptv_address);
        }
        catch (MalformedURLException e) {
            throw new ServiceException(e);
        }
        return getiptv(endpoint);
    }

    public ItvServiceImpl getiptv(URL portAddress) throws ServiceException {
        try {
            IptvSoapBindingStub _stub = new IptvSoapBindingStub(portAddress, this);
            _stub.setPortName(getiptvWSDDServiceName());
            return _stub;
        }
        catch (AxisFault e) {
            return null;
        }
    }

    public void setiptvEndpointAddress(String address) {
        iptv_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if (ItvServiceImpl.class.isAssignableFrom(serviceEndpointInterface)) {
                IptvSoapBindingStub _stub = new IptvSoapBindingStub(new URL(iptv_address), this);
                _stub.setPortName(getiptvWSDDServiceName());
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
        if ("iptv".equals(inputPortName)) {
            return getiptv();
        }
        else  {
            Remote _stub = getPort(serviceEndpointInterface);
            ((Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public QName getServiceName() {
        return new QName("http://10.23.243.81:10537/sdhkcms/services/iptv?wsdl", "ItvServiceImplService");
    }

    private HashSet ports = null;

    public Iterator getPorts() {
        if (ports == null) {
            ports = new HashSet();
            ports.add(new QName("http://10.23.243.81:10537/sdhkcms/services/iptv?wsdl", "iptv"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws ServiceException {
        
if ("iptv".equals(portName)) {
            setiptvEndpointAddress(address);
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
