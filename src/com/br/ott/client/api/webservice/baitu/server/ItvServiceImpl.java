/**
 * ItvServiceImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.br.ott.client.api.webservice.baitu.server;

import java.rmi.Remote;

public interface ItvServiceImpl extends Remote {
    public CSPResult execCmd(String CSPID, String LSPID, String CorrelateID, String CmdFileURL);
}
