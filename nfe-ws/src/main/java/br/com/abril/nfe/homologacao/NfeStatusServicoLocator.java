/**
 * NfeStatusServicoLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nfe.homologacao;

public class NfeStatusServicoLocator extends org.apache.axis.client.Service implements br.com.abril.nfe.homologacao.NfeStatusServico {

/**
 * Serviço destinado à consulta do status do serviçoprestado pelo
 * Portal da Secretaria de Fazenda
 */

    public NfeStatusServicoLocator() {
    }


    public NfeStatusServicoLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NfeStatusServicoLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for NfeStatusServicoSoap
    private java.lang.String NfeStatusServicoSoap_address = "https://homologacao.nfe.fazenda.sp.gov.br/nfeWEB/services/NfeStatusServico.asmx";

    public java.lang.String getNfeStatusServicoSoapAddress() {
        return NfeStatusServicoSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String NfeStatusServicoSoapWSDDServiceName = "NfeStatusServicoSoap";

    public java.lang.String getNfeStatusServicoSoapWSDDServiceName() {
        return NfeStatusServicoSoapWSDDServiceName;
    }

    public void setNfeStatusServicoSoapWSDDServiceName(java.lang.String name) {
        NfeStatusServicoSoapWSDDServiceName = name;
    }

    public br.com.abril.nfe.homologacao.NfeStatusServicoSoap getNfeStatusServicoSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NfeStatusServicoSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNfeStatusServicoSoap(endpoint);
    }

    public br.com.abril.nfe.homologacao.NfeStatusServicoSoap getNfeStatusServicoSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            br.com.abril.nfe.homologacao.NfeStatusServicoSoapStub _stub = new br.com.abril.nfe.homologacao.NfeStatusServicoSoapStub(portAddress, this);
            _stub.setPortName(getNfeStatusServicoSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNfeStatusServicoSoapEndpointAddress(java.lang.String address) {
        NfeStatusServicoSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (br.com.abril.nfe.homologacao.NfeStatusServicoSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                br.com.abril.nfe.homologacao.NfeStatusServicoSoapStub _stub = new br.com.abril.nfe.homologacao.NfeStatusServicoSoapStub(new java.net.URL(NfeStatusServicoSoap_address), this);
                _stub.setPortName(getNfeStatusServicoSoapWSDDServiceName());
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
        if ("NfeStatusServicoSoap".equals(inputPortName)) {
            return getNfeStatusServicoSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico", "NfeStatusServico");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeStatusServico", "NfeStatusServicoSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("NfeStatusServicoSoap".equals(portName)) {
            setNfeStatusServicoSoapEndpointAddress(address);
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
