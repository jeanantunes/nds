/**
 * NfeInutilizacaoLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nfe.homologacao;

public class NfeInutilizacaoLocator extends org.apache.axis.client.Service implements br.com.abril.nfe.homologacao.NfeInutilizacao {

/**
 * Serviço destinado ao atendimento de solicitações de inutilização
 * de numeração.
 */

    public NfeInutilizacaoLocator() {
    }


    public NfeInutilizacaoLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NfeInutilizacaoLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for NfeInutilizacaoSoap
    private java.lang.String NfeInutilizacaoSoap_address = "https://homologacao.nfe.fazenda.sp.gov.br/nfeWEB/services/NfeInutilizacao.asmx";

    public java.lang.String getNfeInutilizacaoSoapAddress() {
        return NfeInutilizacaoSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String NfeInutilizacaoSoapWSDDServiceName = "NfeInutilizacaoSoap";

    public java.lang.String getNfeInutilizacaoSoapWSDDServiceName() {
        return NfeInutilizacaoSoapWSDDServiceName;
    }

    public void setNfeInutilizacaoSoapWSDDServiceName(java.lang.String name) {
        NfeInutilizacaoSoapWSDDServiceName = name;
    }

    public br.com.abril.nfe.homologacao.NfeInutilizacaoSoap getNfeInutilizacaoSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NfeInutilizacaoSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNfeInutilizacaoSoap(endpoint);
    }

    public br.com.abril.nfe.homologacao.NfeInutilizacaoSoap getNfeInutilizacaoSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            br.com.abril.nfe.homologacao.NfeInutilizacaoSoapStub _stub = new br.com.abril.nfe.homologacao.NfeInutilizacaoSoapStub(portAddress, this);
            _stub.setPortName(getNfeInutilizacaoSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNfeInutilizacaoSoapEndpointAddress(java.lang.String address) {
        NfeInutilizacaoSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (br.com.abril.nfe.homologacao.NfeInutilizacaoSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                br.com.abril.nfe.homologacao.NfeInutilizacaoSoapStub _stub = new br.com.abril.nfe.homologacao.NfeInutilizacaoSoapStub(new java.net.URL(NfeInutilizacaoSoap_address), this);
                _stub.setPortName(getNfeInutilizacaoSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("NfeInutilizacaoSoap".equals(inputPortName)) {
            return getNfeInutilizacaoSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao", "NfeInutilizacao");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeInutilizacao", "NfeInutilizacaoSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("NfeInutilizacaoSoap".equals(portName)) {
            setNfeInutilizacaoSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
