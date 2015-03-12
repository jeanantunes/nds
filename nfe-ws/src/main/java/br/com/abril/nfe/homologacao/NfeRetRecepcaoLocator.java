/**
 * NfeRetRecepcaoLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nfe.homologacao;

public class NfeRetRecepcaoLocator extends org.apache.axis.client.Service implements br.com.abril.nfe.homologacao.NfeRetRecepcao {

/**
 * Serviço destinado a retornar o resultado do processamento do lote
 * de NF-e
 */

    public NfeRetRecepcaoLocator() {
    }


    public NfeRetRecepcaoLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NfeRetRecepcaoLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for NfeRetRecepcaoSoap
    private java.lang.String NfeRetRecepcaoSoap_address = "https://homologacao.nfe.fazenda.sp.gov.br/nfeWEB/services/NfeRetRecepcao.asmx";

    public java.lang.String getNfeRetRecepcaoSoapAddress() {
        return NfeRetRecepcaoSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String NfeRetRecepcaoSoapWSDDServiceName = "NfeRetRecepcaoSoap";

    public java.lang.String getNfeRetRecepcaoSoapWSDDServiceName() {
        return NfeRetRecepcaoSoapWSDDServiceName;
    }

    public void setNfeRetRecepcaoSoapWSDDServiceName(java.lang.String name) {
        NfeRetRecepcaoSoapWSDDServiceName = name;
    }

    public br.com.abril.nfe.homologacao.NfeRetRecepcaoSoap getNfeRetRecepcaoSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NfeRetRecepcaoSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNfeRetRecepcaoSoap(endpoint);
    }

    public br.com.abril.nfe.homologacao.NfeRetRecepcaoSoap getNfeRetRecepcaoSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            br.com.abril.nfe.homologacao.NfeRetRecepcaoSoapStub _stub = new br.com.abril.nfe.homologacao.NfeRetRecepcaoSoapStub(portAddress, this);
            _stub.setPortName(getNfeRetRecepcaoSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNfeRetRecepcaoSoapEndpointAddress(java.lang.String address) {
        NfeRetRecepcaoSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (br.com.abril.nfe.homologacao.NfeRetRecepcaoSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                br.com.abril.nfe.homologacao.NfeRetRecepcaoSoapStub _stub = new br.com.abril.nfe.homologacao.NfeRetRecepcaoSoapStub(new java.net.URL(NfeRetRecepcaoSoap_address), this);
                _stub.setPortName(getNfeRetRecepcaoSoapWSDDServiceName());
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
        if ("NfeRetRecepcaoSoap".equals(inputPortName)) {
            return getNfeRetRecepcaoSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetRecepcao", "NfeRetRecepcao");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeRetRecepcao", "NfeRetRecepcaoSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("NfeRetRecepcaoSoap".equals(portName)) {
            setNfeRetRecepcaoSoapEndpointAddress(address);
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
