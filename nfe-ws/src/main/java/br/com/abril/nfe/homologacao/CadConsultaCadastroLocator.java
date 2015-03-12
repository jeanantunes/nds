/**
 * CadConsultaCadastroLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nfe.homologacao;

public class CadConsultaCadastroLocator extends org.apache.axis.client.Service implements br.com.abril.nfe.homologacao.CadConsultaCadastro {

/**
 * Serviço destinado ao atendimento de solicitações de consulta ao
 * Cadastro de Contribuintes do ICMS da Secretaria de Fazenda Estatual.
 */

    public CadConsultaCadastroLocator() {
    }


    public CadConsultaCadastroLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CadConsultaCadastroLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CadConsultaCadastroSoap
    private java.lang.String CadConsultaCadastroSoap_address = "https://homologacao.nfe.fazenda.sp.gov.br/nfeWEB/services/cadconsultacadastro.asmx";

    public java.lang.String getCadConsultaCadastroSoapAddress() {
        return CadConsultaCadastroSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CadConsultaCadastroSoapWSDDServiceName = "CadConsultaCadastroSoap";

    public java.lang.String getCadConsultaCadastroSoapWSDDServiceName() {
        return CadConsultaCadastroSoapWSDDServiceName;
    }

    public void setCadConsultaCadastroSoapWSDDServiceName(java.lang.String name) {
        CadConsultaCadastroSoapWSDDServiceName = name;
    }

    public br.com.abril.nfe.homologacao.CadConsultaCadastroSoap getCadConsultaCadastroSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CadConsultaCadastroSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCadConsultaCadastroSoap(endpoint);
    }

    public br.com.abril.nfe.homologacao.CadConsultaCadastroSoap getCadConsultaCadastroSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            br.com.abril.nfe.homologacao.CadConsultaCadastroSoapStub _stub = new br.com.abril.nfe.homologacao.CadConsultaCadastroSoapStub(portAddress, this);
            _stub.setPortName(getCadConsultaCadastroSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCadConsultaCadastroSoapEndpointAddress(java.lang.String address) {
        CadConsultaCadastroSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (br.com.abril.nfe.homologacao.CadConsultaCadastroSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                br.com.abril.nfe.homologacao.CadConsultaCadastroSoapStub _stub = new br.com.abril.nfe.homologacao.CadConsultaCadastroSoapStub(new java.net.URL(CadConsultaCadastroSoap_address), this);
                _stub.setPortName(getCadConsultaCadastroSoapWSDDServiceName());
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
        if ("CadConsultaCadastroSoap".equals(inputPortName)) {
            return getCadConsultaCadastroSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/CadConsultaCadastro", "CadConsultaCadastro");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.portalfiscal.inf.br/nfe/wsdl/CadConsultaCadastro", "CadConsultaCadastroSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CadConsultaCadastroSoap".equals(portName)) {
            setCadConsultaCadastroSoapEndpointAddress(address);
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
