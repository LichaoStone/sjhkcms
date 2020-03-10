/**
 * ItvServiceImplResultService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.br.ott.client.api.webservice.baitu.client;

import java.net.URL;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface ItvServiceImplResultService extends Service {
    public String getCSPResponseAddress();

    public ItvServiceImplResult getCSPResponse() throws ServiceException;

    public ItvServiceImplResult getCSPResponse(URL portAddress) throws ServiceException;
}
