/**
 * NfeCancelamento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nfe.homologacao;

public interface NfeCancelamento extends javax.xml.rpc.Service {

/**
 * Serviço destinado ao atendimento de solicitações de cancelamento
 * de notas fiscais eletrônicas
 */
    public java.lang.String getNfeCancelamentoSoapAddress();

    public br.com.abril.nfe.homologacao.NfeCancelamentoSoap getNfeCancelamentoSoap() throws javax.xml.rpc.ServiceException;

    public br.com.abril.nfe.homologacao.NfeCancelamentoSoap getNfeCancelamentoSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
