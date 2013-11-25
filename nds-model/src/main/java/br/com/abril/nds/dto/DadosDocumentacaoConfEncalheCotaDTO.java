<<<<<<< HEAD
package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Map;

import br.com.abril.nds.vo.ValidacaoVO;

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
	private Map<String, Boolean> listaNossoNumero;
	
	/**
	 * Flag que indica se os documentos de conferencia de encalhe deverão
	 * ser gerados na finalização do mesmo.
	 */
	private boolean indGeraDocumentacaoConferenciaEncalhe = false;

	/**
	 * Define se distribuidor utiliza  impressão de Boleto + Slip 
	 */
	private boolean utilizaBoleto;
	
	private boolean utilizaSlip;
	
	private boolean utilizaBoletoSlip;
	
	private boolean utilizaRecibo;
	
	private ValidacaoVO msgsGeracaoCobranca;
	
	
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
	public Map<String, Boolean> getListaNossoNumero() {
		return listaNossoNumero;
	}

	/**
	 * @param listaNossoNumero the listaNossoNumero to set
	 */
	public void setListaNossoNumero(Map<String, Boolean> listaNossoNumero) {
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
	public boolean isUtilizaBoleto() {
		return utilizaBoleto;
	}

	/**
	 * @param utilizaBoleto the utilizaSlipBoleto to set
	 */
	public void setUtilizaBoleto(boolean utilizaBoleto) {
		this.utilizaBoleto = utilizaBoleto;
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

	public boolean isUtilizaBoletoSlip() {
		return utilizaBoletoSlip;
	}

	public void setUtilizaBoletoSlip(boolean utilizaBoletoSlip) {
		this.utilizaBoletoSlip = utilizaBoletoSlip;
	}

	/**
	 * @return the utilizaRecibo
	 */
	public boolean isUtilizaRecibo() {
		return utilizaRecibo;
	}

	/**
	 * @param utilizaRecibo the utilizaRecibo to set
	 */
	public void setUtilizaRecibo(boolean utilizaRecibo) {
		this.utilizaRecibo = utilizaRecibo;
	}

	public ValidacaoVO getMsgsGeracaoCobranca() {
		return msgsGeracaoCobranca;
	}

	public void setMsgsGeracaoCobranca(ValidacaoVO msgsGeracaoCobranca) {
		this.msgsGeracaoCobranca = msgsGeracaoCobranca;
	}

	
	
}
=======
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
>>>>>>> fase2
