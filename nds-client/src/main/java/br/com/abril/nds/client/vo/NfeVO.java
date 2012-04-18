package br.com.abril.nds.client.vo;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class NfeVO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	@Export(label="Nota")
	private String numero;
	
	@Export(label="Série")
	private String serie;
	
	@Export(label="Emissão")
	private String emissao;
	
	@Export(label="Tipo Emissão")
	private String tipoEmissao;
	
	@Export(label="CNPJ Emissor")
	private String cnpjEmissor;
	
	@Export(label="CNPJ Destinatário")
	private String cnpjDestinatario;
	
	@Export(label="Status NF-e")
	private String statusNfe;
	
	@Export(label="Tipo NF-e")
	private String tipoNfe;
	
	@Export(label="Movimento Integração")
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
