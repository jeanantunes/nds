package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class NfeVO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idNotaFiscal;
	
	@Export(label="Nota")
	private Long numero;
	
	@Export(label="Série")
	private String serie;
	
	@Export(label="Emissão")
	private String emissao;
	
	@Export(label="Tipo Emissão")
	private String tipoEmissao;
	
	@Export(label="CNPJ Destinatário")
	private String cnpjDestinatario;

	@Export(label="CPF Destinatário")
	private String cpfDestinatario;
	
	@Export(label="CNPJ Remetente")
	private String cnpjRemetente;

	@Export(label="CPF Remetente")
	private String cpfRemetente;
	
	@Export(label="Status NF-e")
	private String statusNfe;
	
	@Export(label="Tipo NF-e")
	private String tipoNfe;
	
	@Export(label="Movimento Integração")
	private String movimentoIntegracao;

	private TipoOperacao tipoOperacao;
	
	/**
	 * Obtém tipoOperacao
	 *
	 * @return TipoOperacao
	 */
	public TipoOperacao getTipoOperacao() {
		return tipoOperacao;
	}

	/**
	 * Atribuí tipoOperacao
	 * @param tipoOperacao 
	 */
	public void setTipoOperacao(TipoOperacao tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

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
	 * @return String
	 */
	public String getEmissao() {
		return emissao;
	}

	/**
	 * Atribuí emissao
	 * @param emissao 
	 */
	public void setEmissao(String emissao) {
		this.emissao = emissao;
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
	
	
}
