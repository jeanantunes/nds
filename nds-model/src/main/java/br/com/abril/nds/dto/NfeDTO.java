package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

public class NfeDTO implements Serializable {

	private String numero;
	private String serie;
	private Date emissao;
	private String tipoEmissao;
	private String cnpjEmissor;
	private String cnpjDestinatario;
	private String statusNfe;
	private String tipoNfe;
	private String movimentoIntegracao;

	/**
	 * Obtém numero
	 *
	 * @return String
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * Atribuí numero
	 * @param numero 
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * Obtém serie
	 *
	 * @return String
	 */
	public String getSerie() {
		return serie;
	}

	/**
	 * Atribuí serie
	 * @param serie 
	 */
	public void setSerie(String serie) {
		this.serie = serie;
	}

	/**
	 * Obtém emissao
	 *
	 * @return Date
	 */
	public Date getEmissao() {
		return emissao;
	}

	/**
	 * Atribuí emissao
	 * @param emissao 
	 */
	public void setEmissao(Date emissao) {
		this.emissao = emissao;
	}

	/**
	 * Obtém cnpjEmissor
	 *
	 * @return String
	 */
	public String getCnpjEmissor() {
		return cnpjEmissor;
	}

	/**
	 * Atribuí cnpjEmissor
	 * @param cnpjEmissor 
	 */
	public void setCnpjEmissor(String cnpjEmissor) {
		this.cnpjEmissor = cnpjEmissor;
	}

	/**
	 * Obtém cnpjDestinatario
	 *
	 * @return String
	 */
	public String getCnpjDestinatario() {
		return cnpjDestinatario;
	}

	/**
	 * Atribuí cnpjDestinatario
	 * @param cnpjDestinatario 
	 */
	public void setCnpjDestinatario(String cnpjDestinatario) {
		this.cnpjDestinatario = cnpjDestinatario;
	}

	/**
	 * Obtém statusNfe
	 *
	 * @return String
	 */
	public String getStatusNfe() {
		return statusNfe;
	}

	/**
	 * Atribuí statusNfe
	 * @param statusNfe 
	 */
	public void setStatusNfe(String statusNfe) {
		this.statusNfe = statusNfe;
	}

	/**
	 * Obtém tipoNfe
	 *
	 * @return String
	 */
	public String getTipoNfe() {
		return tipoNfe;
	}

	/**
	 * Atribuí tipoNfe
	 * @param tipoNfe 
	 */
	public void setTipoNfe(String tipoNfe) {
		this.tipoNfe = tipoNfe;
	}

	/**
	 * Obtém movimentoIntegracao
	 *
	 * @return String
	 */
	public String getMovimentoIntegracao() {
		return movimentoIntegracao;
	}

	/**
	 * Atribuí movimentoIntegracao
	 * @param movimentoIntegracao 
	 */
	public void setMovimentoIntegracao(String movimentoIntegracao) {
		this.movimentoIntegracao = movimentoIntegracao;
	}

	/**
	 * Obtém tipoEmissao
	 *
	 * @return String
	 */
	public String getTipoEmissao() {
		return tipoEmissao;
	}

	/**
	 * Atribuí tipoEmissao
	 * @param tipoEmissao 
	 */
	public void setTipoEmissao(String tipoEmissao) {
		this.tipoEmissao = tipoEmissao;
	}
	
	
	
	
}
