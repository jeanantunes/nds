package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

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
	 * List de nosso numero gerado durante a geração de cobrança.
	 */
	private List<String> listaNossoNumero;
	
	/**
	 * Flag que indica se os documentos de conferencia de encalhe deverão
	 * ser gerados na finalização do mesmo.
	 */
	private boolean indGeraDocumentacaoConferenciaEncalhe = false;

	/**
	 * Define se distribuidor utiliza  impressão de Boleto + Slip no mesmo documento
	 */
	private boolean utilizaSlipBoleto;
	
	private boolean utilizaSlip;
	
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
	 * @return the listaNossoNumero
	 */
	public List<String> getListaNossoNumero() {
		return listaNossoNumero;
	}

	/**
	 * @param listaNossoNumero the listaNossoNumero to set
	 */
	public void setListaNossoNumero(List<String> listaNossoNumero) {
		this.listaNossoNumero = listaNossoNumero;
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

	/**
	 * @return the utilizaSlip
	 */
	public boolean isUtilizaSlip() {
		return utilizaSlip;
	}

	/**
	 * @param utilizaSlip the utilizaSlip to set
	 */
	public void setUtilizaSlip(boolean utilizaSlip) {
		this.utilizaSlip = utilizaSlip;
	}
	
}
