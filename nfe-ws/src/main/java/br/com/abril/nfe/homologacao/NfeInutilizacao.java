/**
 * NfeInutilizacao.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nfe.homologacao;

public interface NfeInutilizacao extends javax.xml.rpc.Service {

/**
 * Serviço destinado ao atendimento de solicitações de inutilização
 * de numeração.
 */
    public java.lang.String getNfeInutilizacaoSoapAddress();

    public br.com.abril.nfe.homologacao.NfeInutilizacaoSoap getNfeInutilizacaoSoap() throws javax.xml.rpc.ServiceException;

    public br.com.abril.nfe.homologacao.NfeInutilizacaoSoap getNfeInutilizacaoSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
