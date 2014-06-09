package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.model.fiscal.nota.Identificacao.TipoEmissao;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamento;

public class NfeDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idNotaFiscal;
	
	private Long numero;
	
	private Long serie;
	
	private Date emissao;
	
	private TipoEmissao tipoEmissao;
	
	private String cnpjDestinatario;
	
	private String cpfDestinatario;
	
	private String cnpjRemetente;
	
	private String cpfRemetente;
	
	private StatusProcessamento statusNfe;
	
	private String tipoNfe;
	
	private String movimentoIntegracao;
	
	private boolean notaImpressa;
	
	/**
	 * Obtém numero
	 *
	 * @return Long
	 */
	public Long getNumero() {
		return numero;
	}
	/**
	 * Atribuí numero
	 * @param numero 
	 */
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	/**
	 * Obtém serie
	 *
	 * @return String
	 */
	public Long getSerie() {
		return serie;
	}
	/**
	 * Atribuí serie
	 * @param serie 
	 */
	public void setSerie(Long serie) {
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
	 * Obtém tipoEmissao
	 *
	 * @return String
	 */
	public TipoEmissao getTipoEmissao() {
		return tipoEmissao;
	}
	/**
	 * Atribuí tipoEmissao
	 * @param tipoEmissao 
	 */
	public void setTipoEmissao(TipoEmissao tipoEmissao) {
		this.tipoEmissao = tipoEmissao;
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
	 * Obtém cpfDestinatario
	 *
	 * @return String
	 */
	public String getCpfDestinatario() {
		return cpfDestinatario;
	}
	/**
	 * Atribuí cpfDestinatario
	 * @param cpfDestinatario 
	 */
	public void setCpfDestinatario(String cpfDestinatario) {
		this.cpfDestinatario = cpfDestinatario;
	}
	/**
	 * Obtém cnpjRemetente
	 *
	 * @return String
	 */
	public String getCnpjRemetente() {
		return cnpjRemetente;
	}
	/**
	 * Atribuí cnpjRemetente
	 * @param cnpjRemetente 
	 */
	public void setCnpjRemetente(String cnpjRemetente) {
		this.cnpjRemetente = cnpjRemetente;
	}
	/**
	 * Obtém cpfRemetente
	 *
	 * @return String
	 */
	public String getCpfRemetente() {
		return cpfRemetente;
	}
	/**
	 * Atribuí cpfRemetente
	 * @param cpfRemetente 
	 */
	public void setCpfRemetente(String cpfRemetente) {
		this.cpfRemetente = cpfRemetente;
	}
	/**
	 * Obtém statusNfe
	 *
	 * @return String
	 */
	public StatusProcessamento getStatusNfe() {
		return statusNfe;
	}
	/**
	 * Atribuí statusNfe
	 * @param statusNfe 
	 */
	public void setStatusNfe(StatusProcessamento statusNfe) {
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
	 * Obtém idNotaFiscal
	 *
	 * @return Long
	 */
	public Long getIdNotaFiscal() {
		return idNotaFiscal;
	}
	/**
	 * Atribuí idNotaFiscal
	 * @param idNotaFiscal 
	 */
	public void setIdNotaFiscal(Long idNotaFiscal) {
		this.idNotaFiscal = idNotaFiscal;
	}
	
	public boolean isNotaImpressa() {
		return notaImpressa;
	}
	
	public void setNotaImpressa(boolean notaImpressa) {
		this.notaImpressa = notaImpressa;
	}
	
	
}
