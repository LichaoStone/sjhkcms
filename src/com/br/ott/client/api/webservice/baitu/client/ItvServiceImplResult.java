/**
 * ItvServiceImplResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.br.ott.client.api.webservice.baitu.client;

import java.net.MalformedURLException;

import javax.xml.rpc.ServiceException;

public interface ItvServiceImplResult extends java.rmi.Remote {
    public CSPResult resultNotify(java.lang.String CSPID, java.lang.String LSPID, java.lang.String correlateID, int xmlReturn, java.lang.String returnfileurl) throws java.rmi.RemoteException,ServiceException,MalformedURLException;
}
