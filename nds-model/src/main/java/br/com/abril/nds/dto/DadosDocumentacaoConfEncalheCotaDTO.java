package br.com.abril.nds.dto;

import java.io.Serializable;

public class DadosDocumentacaoConfEncalheCotaDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * idControleConferenciaEncalheCota para o qual os 
	 * documentos são gerados.
	 */
	private Long idControleConferenciaEncalheCota;
	
	/**
	 * Nosso numero gerado durante a geração de cobrança.
	 */
	private String nossoNumero;
	
	/**
	 * Flag que indica se os documentos de conferencia de encalhe deverão
	 * ser gerados na finalização do mesmo.
	 */
	private boolean indGeraDocumentacaoConferenciaEncalhe = false;

	/**
	 * Define se distribuidor utiliza  impressão de Boleto + Slip no mesmo documento
	 */
	private boolean utilizaSlipBoleto;
	/**
	 * Obtém idControleConferenciaEncalheCota
	 *
	 * @return Long
	 */
	public Long getIdControleConferenciaEncalheCota() {
		return idControleConferenciaEncalheCota;
	}

	/**
	 * Atribuí idControleConferenciaEncalheCota
	 * @param idControleConferenciaEncalheCota 
	 */
	public void setIdControleConferenciaEncalheCota(
			Long idControleConferenciaEncalheCota) {
		this.idControleConferenciaEncalheCota = idControleConferenciaEncalheCota;
	}

	/**
	 * Obtém nossoNumero
	 *
	 * @return String
	 */
	public String getNossoNumero() {
		return nossoNumero;
	}

	/**
	 * Atribuí nossoNumero
	 * @param nossoNumero 
	 */
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	/**
	 * Obtém indGeraDocumentacaoConferenciaEncalhe
	 *
	 * @return boolean
	 */
	public boolean isIndGeraDocumentacaoConferenciaEncalhe() {
		return indGeraDocumentacaoConferenciaEncalhe;
	}

	/**
	 * Atribuí indGeraDocumentacaoConferenciaEncalhe
	 * @param indGeraDocumentacaoConferenciaEncalhe 
	 */
	public void setIndGeraDocumentacaoConferenciaEncalhe(
			boolean indGeraDocumentacaoConferenciaEncalhe) {
		this.indGeraDocumentacaoConferenciaEncalhe = indGeraDocumentacaoConferenciaEncalhe;
	}

	/**
	 * @return the utilizaSlipBoleto
	 */
	public boolean isUtilizaSlipBoleto() {
		return utilizaSlipBoleto;
	}

	/**
	 * @param utilizaSlipBoleto the utilizaSlipBoleto to set
	 */
	public void setUtilizaSlipBoleto(boolean utilizaSlipBoleto) {
		this.utilizaSlipBoleto = utilizaSlipBoleto;
	}
	
	
}
