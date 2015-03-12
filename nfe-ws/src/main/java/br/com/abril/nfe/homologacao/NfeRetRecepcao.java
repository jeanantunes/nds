/**
 * NfeRetRecepcao.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nfe.homologacao;

public interface NfeRetRecepcao extends javax.xml.rpc.Service {

/**
 * Serviço destinado a retornar o resultado do processamento do lote
 * de NF-e
 */
    public java.lang.String getNfeRetRecepcaoSoapAddress();

    public br.com.abril.nfe.homologacao.NfeRetRecepcaoSoap getNfeRetRecepcaoSoap() throws javax.xml.rpc.ServiceException;

    public br.com.abril.nfe.homologacao.NfeRetRecepcaoSoap getNfeRetRecepcaoSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
