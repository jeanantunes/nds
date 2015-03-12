/**
 * NfeRecepcao.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nfe.homologacao;

public interface NfeRecepcao extends javax.xml.rpc.Service {

/**
 * Serviço destinado à recepção de mensagens de lote de NF-e
 */
    public java.lang.String getNfeRecepcaoSoapAddress();

    public br.com.abril.nfe.homologacao.NfeRecepcaoSoap getNfeRecepcaoSoap() throws javax.xml.rpc.ServiceException;

    public br.com.abril.nfe.homologacao.NfeRecepcaoSoap getNfeRecepcaoSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
