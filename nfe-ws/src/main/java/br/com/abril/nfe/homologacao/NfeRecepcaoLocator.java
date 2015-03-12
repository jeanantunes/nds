/**
 * NfeRecepcaoLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nfe.homologacao;

public class NfeRecepcaoLocator extends org.apache.axis.client.Service implements br.com.abril.nfe.homologacao.NfeRecepcao {

/**
 * Serviço destinado à recepção de mensagens de lote de NF-e
 */

    public NfeRecepcaoLocator() {
    }


    public NfeRecepcaoLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NfeRecepcaoLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for NfeRecepcaoSoap
    private java.lang.String NfeRecepcaoSoap_address = "https://homologacao.nfe.fazenda.sp.gov.br/nfeWEB/services/NfeRecepcao.asmx";

    public java.lang.String getNfeRecepcaoSoapAddress() {
        return NfeRecepcaoSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String NfeRecepcaoSoapWSDDServiceName = "NfeRecepcaoSoap";

    public java.lang.String getNfeRecepcaoSoapWSDDServiceName() {
        return NfeRecepcaoSoapWSDDServiceName;
    }

    public void setNfeRecepcaoSoapWSDDServiceName(java.lang.String name) {
        NfeRecepcaoSoapWSDDServiceName = name;
    }

    public br.com.abril.nfe.homologacao.NfeRecepcaoSoap getNfeRecepcaoSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NfeRecepcaoSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNfeRecepcaoSoap(endpoint);
    }

    public br.com.abril.nfe.homologacao.NfeRecepcaoSoap getNfeRecepcaoSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            br.com.abril.nfe.homologacao.NfeRecepcaoSoapStub _stub = new br.com.abril.nfe.homologacao.NfeRecepcaoSoapStub(portAddress, this);
            _stub.setPortName(getNfeRecepcaoSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNfeRecepcaoSoapEndpointAddress(java.lang.String address) {
        NfeRecepcaoSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (br.com.abril.nfe.homologacao.NfeRecepcaoSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                br.com.abril.nfe.homologacao.NfeRecepcaoSoapStub _stub = new br.com.abril.nfe.homologacao.NfeRecepcaoSoapStub(new java.net.URL(NfeRecepcaoSoap_address), this);
                _stub.setPortName(getNfeRecepcaoSoapWSDDServiceName());
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
        if ("NfeRecepcaoSoap".equals(inputPortName)) {
            return getNfeRecepcaoSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeRecepcao", "NfeRecepcao");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeRecepcao", "NfeRecepcaoSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("NfeRecepcaoSoap".equals(portName)) {
            setNfeRecepcaoSoapEndpointAddress(address);
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
