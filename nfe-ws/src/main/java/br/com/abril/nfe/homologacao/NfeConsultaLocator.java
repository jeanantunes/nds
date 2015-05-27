/**
 * NfeConsultaLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nfe.homologacao;

public class NfeConsultaLocator extends org.apache.axis.client.Service implements br.com.abril.nfe.homologacao.NfeConsulta {

/**
 * Serviço destinado ao atendimento de solicitações de consulta da
 * situação atual da NF-e na Base de Dados do Portal sa Secretaria de
 * Fazenda Estatual.
 */

    public NfeConsultaLocator() {
    }


    public NfeConsultaLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NfeConsultaLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for NfeConsultaSoap
    private java.lang.String NfeConsultaSoap_address = "https://homologacao.nfe.fazenda.sp.gov.br/nfeWEB/services/NfeConsulta.asmx";

    public java.lang.String getNfeConsultaSoapAddress() {
        return NfeConsultaSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String NfeConsultaSoapWSDDServiceName = "NfeConsultaSoap";

    public java.lang.String getNfeConsultaSoapWSDDServiceName() {
        return NfeConsultaSoapWSDDServiceName;
    }

    public void setNfeConsultaSoapWSDDServiceName(java.lang.String name) {
        NfeConsultaSoapWSDDServiceName = name;
    }

    public br.com.abril.nfe.homologacao.NfeConsultaSoap getNfeConsultaSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NfeConsultaSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNfeConsultaSoap(endpoint);
    }

    public br.com.abril.nfe.homologacao.NfeConsultaSoap getNfeConsultaSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            br.com.abril.nfe.homologacao.NfeConsultaSoapStub _stub = new br.com.abril.nfe.homologacao.NfeConsultaSoapStub(portAddress, this);
            _stub.setPortName(getNfeConsultaSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNfeConsultaSoapEndpointAddress(java.lang.String address) {
        NfeConsultaSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (br.com.abril.nfe.homologacao.NfeConsultaSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                br.com.abril.nfe.homologacao.NfeConsultaSoapStub _stub = new br.com.abril.nfe.homologacao.NfeConsultaSoapStub(new java.net.URL(NfeConsultaSoap_address), this);
                _stub.setPortName(getNfeConsultaSoapWSDDServiceName());
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
        if ("NfeConsultaSoap".equals(inputPortName)) {
            return getNfeConsultaSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta", "NfeConsulta");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/NfeConsulta", "NfeConsultaSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("NfeConsultaSoap".equals(portName)) {
            setNfeConsultaSoapEndpointAddress(address);
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
