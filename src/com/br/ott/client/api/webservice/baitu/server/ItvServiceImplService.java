/**
 * ItvServiceImplService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.br.ott.client.api.webservice.baitu.server;

import java.net.URL;

import javax.xml.rpc.ServiceException;

public interface ItvServiceImplService extends javax.xml.rpc.Service {
    public String getiptvAddress();

    public ItvServiceImpl getiptv() throws ServiceException;

    public ItvServiceImpl getiptv(URL portAddress) throws ServiceException;
}
