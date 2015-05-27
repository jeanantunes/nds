/**
 * NfeConsulta.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nfe.homologacao;

public interface NfeConsulta extends javax.xml.rpc.Service {

/**
 * Serviço destinado ao atendimento de solicitações de consulta da
 * situação atual da NF-e na Base de Dados do Portal sa Secretaria de
 * Fazenda Estatual.
 */
    public java.lang.String getNfeConsultaSoapAddress();

    public br.com.abril.nfe.homologacao.NfeConsultaSoap getNfeConsultaSoap() throws javax.xml.rpc.ServiceException;

    public br.com.abril.nfe.homologacao.NfeConsultaSoap getNfeConsultaSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
