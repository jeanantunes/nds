/**
 * DevolucaoEncalheBandeirasWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.icd.axis.client;

public class DevolucaoEncalheBandeirasWSServiceLocator extends org.apache.axis.client.Service implements br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSService {

	boolean homolog=true;
    public DevolucaoEncalheBandeirasWSServiceLocator() {
    }
    
    public DevolucaoEncalheBandeirasWSServiceLocator(boolean ambiente) {
    	System.out.println("DEVOLUCAOBANDEIRA:"+ambiente);
    	homolog=ambiente;
    }


    public DevolucaoEncalheBandeirasWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DevolucaoEncalheBandeirasWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DevolucaoEncalheBandeirasWS
    private java.lang.String DevolucaoEncalheBandeirasWS_address_prod = "http://homolog.icd.dinap.com.br/icd/services/DevolucaoEncalheBandeirasWS";
    private java.lang.String DevolucaoEncalheBandeirasWS_address = "http://www.icd.dinap.com.br/icd/services/DevolucaoEncalheBandeirasWS";
                                 
    public java.lang.String getDevolucaoEncalheBandeirasWSAddress() {
    	if ( homolog) {
    		System.err.println("AMBIENTE HOMOLOGACAO "+DevolucaoEncalheBandeirasWS_address);
        return DevolucaoEncalheBandeirasWS_address;
    	}
    	else
    	{
        System.err.println("AMBIENTE PRODUCAO "+DevolucaoEncalheBandeirasWS_address_prod);
    	return DevolucaoEncalheBandeirasWS_address_prod;
    	}
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DevolucaoEncalheBandeirasWSWSDDServiceName = "DevolucaoEncalheBandeirasWS";

    public java.lang.String getDevolucaoEncalheBandeirasWSWSDDServiceName() {
        return DevolucaoEncalheBandeirasWSWSDDServiceName;
    }

    public void setDevolucaoEncalheBandeirasWSWSDDServiceName(java.lang.String name) {
        DevolucaoEncalheBandeirasWSWSDDServiceName = name;
    }

    public br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWS_PortType getDevolucaoEncalheBandeirasWS() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DevolucaoEncalheBandeirasWS_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDevolucaoEncalheBandeirasWS(endpoint);
    }

    public br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWS_PortType getDevolucaoEncalheBandeirasWS(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSSoapBindingStub _stub = new br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSSoapBindingStub(portAddress, this);
            _stub.setPortName(getDevolucaoEncalheBandeirasWSWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDevolucaoEncalheBandeirasWSEndpointAddress(java.lang.String address) {
        DevolucaoEncalheBandeirasWS_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWS_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSSoapBindingStub _stub = new br.com.abril.icd.axis.client.DevolucaoEncalheBandeirasWSSoapBindingStub(new java.net.URL(DevolucaoEncalheBandeirasWS_address), this);
                _stub.setPortName(getDevolucaoEncalheBandeirasWSWSDDServiceName());
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
        if ("DevolucaoEncalheBandeirasWS".equals(inputPortName)) {
            return getDevolucaoEncalheBandeirasWS();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:DevolucaoEncalheBandeiras", "DevolucaoEncalheBandeirasWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:DevolucaoEncalheBandeiras", "DevolucaoEncalheBandeirasWS"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DevolucaoEncalheBandeirasWS".equals(portName)) {
            setDevolucaoEncalheBandeirasWSEndpointAddress(address);
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
