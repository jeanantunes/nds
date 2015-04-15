/**
 * CadConsultaCadastro.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.nfe.homologacao;

public interface CadConsultaCadastro extends javax.xml.rpc.Service {

/**
 * Serviço destinado ao atendimento de solicitações de consulta ao
 * Cadastro de Contribuintes do ICMS da Secretaria de Fazenda Estatual.
 */
    public java.lang.String getCadConsultaCadastroSoapAddress();

    public br.com.abril.nfe.homologacao.CadConsultaCadastroSoap getCadConsultaCadastroSoap() throws javax.xml.rpc.ServiceException;

    public br.com.abril.nfe.homologacao.CadConsultaCadastroSoap getCadConsultaCadastroSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
