/**
 * NfeStatusServico.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nfe.homologacao;

public interface NfeStatusServico extends javax.xml.rpc.Service {

/**
 * Serviço destinado à consulta do status do serviçoprestado pelo
 * Portal da Secretaria de Fazenda
 */
    public java.lang.String getNfeStatusServicoSoapAddress();

    public br.com.abril.nfe.homologacao.NfeStatusServicoSoap getNfeStatusServicoSoap() throws javax.xml.rpc.ServiceException;

    public br.com.abril.nfe.homologacao.NfeStatusServicoSoap getNfeStatusServicoSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
