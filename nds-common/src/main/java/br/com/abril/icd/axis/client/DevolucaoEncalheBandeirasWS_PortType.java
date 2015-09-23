/**
 * DevolucaoEncalheBandeirasWS_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.abril.icd.axis.client;

public interface DevolucaoEncalheBandeirasWS_PortType extends java.rmi.Remote {
    public void inserirDevolucaoEncalheBandeiras(java.lang.String ufDistribuidor, java.lang.String codDistribuidor, java.lang.Integer tipoNota, java.lang.Integer numNota, java.lang.Integer codVolume, java.lang.Integer qtdSacosPaletes, java.lang.String nomeDestinoEncalhe, br.com.abril.icd.axis.client.ItemNotaEncalheBandeiraVO[] itensNotaEncalheBandeira) throws java.rmi.RemoteException;
}
