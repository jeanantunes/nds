package br.com.abril.nds.client.vo;

import java.math.BigDecimal;

/**
 * Value Object para os produtos da matriz de recolhimento.
 * 
 * @author Discover Technology
 */
public class ProdutoRecolhimentoVO {

	private String idLancamento;
	
	private String sequencia;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private String numeroEdicao;
	
	private String precoVenda;
	
	private String nomeFornecedor;
	
	private String nomeEditor;
	
	private String parcial;
	
	private String brinde;
	
	private String dataLancamento;
	
	private String dataRecolhimento;
	
	private BigDecimal encalheSede;
	
	private BigDecimal encalheAtendida;
	
	private BigDecimal encalhe;
	
	private String valorTotal;
	
	private String novaData;
	
	private boolean bloqueioDataRecolhimento;
	
	private boolean bloqueioMatrizFechada;

	/**
	 * @return the idLancamento
	 */
	public String getIdLancamento() {
		return idLancamento;
	}

	/**
	 * @param idLancamento the idLancamento to set
	 */
	public void setIdLancamento(String idLancamento) {
		this.idLancamento = idLancamento;
	}

	/**
	 * @return the sequencia
	 */
	public String getSequencia() {
		return sequencia;
	}

	/**
	 * @param sequencia the sequencia to set
	 */
	public void setSequencia(String sequencia) {
		this.sequencia = sequencia;
	}

	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}

	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	/**
	 * @return the nomeProduto
	 */
	public String getNomeProduto() {
		return nomeProduto;
	}

	/**
	 * @param nomeProduto the nomeProduto to set
	 */
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	/**
	 * @return the numeroEdicao
	 */
	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	/**
	 * @param numeroEdicao the numeroEdicao to set
	 */
	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	/**
	 * @return the precoVenda
	 */
	public String getPrecoVenda() {
		return precoVenda;
	}

	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(String precoVenda) {
		this.precoVenda = precoVenda;
	}

	/**
	 * @return the nomeFornecedor
	 */
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	/**
	 * @param nomeFornecedor the nomeFornecedor to set
	 */
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	/**
	 * @return the nomeEditor
	 */
	public String getNomeEditor() {
		return nomeEditor;
	}

	/**
	 * @param nomeEditor the nomeEditor to set
	 */
	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	/**
	 * @return the parcial
	 */
	public String getParcial() {
		return parcial;
	}

	/**
	 * @param parcial the parcial to set
	 */
	public void setParcial(String parcial) {
		this.parcial = parcial;
	}

	/**
	 * @return the brinde
	 */
	public String getBrinde() {
		return brinde;
	}

	/**
	 * @param brinde the brinde to set
	 */
	public void setBrinde(String brinde) {
		this.brinde = brinde;
	}

	/**
	 * @return the dataLancamento
	 */
	public String getDataLancamento() {
		return dataLancamento;
	}

	/**
	 * @param dataLancamento the dataLancamento to set
	 */
	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	/**
	 * @return the dataRecolhimento
	 */
	public String getDataRecolhimento() {
		return dataRecolhimento;
	}

	/**
	 * @param dataRecolhimento the dataRecolhimento to set
	 */
	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	/**
	 * @return the encalheSede
	 */
	public BigDecimal getEncalheSede() {
		return encalheSede;
	}

	/**
	 * @param encalheSede the encalheSede to set
	 */
	public void setEncalheSede(BigDecimal encalheSede) {
		this.encalheSede = encalheSede;
	}

	/**
	 * @return the encalheAtendida
	 */
	public BigDecimal getEncalheAtendida() {
		return encalheAtendida;
	}

	/**
	 * @param encalheAtendida the encalheAtendida to set
	 */
	public void setEncalheAtendida(BigDecimal encalheAtendida) {
		this.encalheAtendida = encalheAtendida;
	}

	/**
	 * @return the encalhe
	 */
	public BigDecimal getEncalhe() {
		return encalhe;
	}

	/**
	 * @param encalhe the encalhe to set
	 */
	public void setEncalhe(BigDecimal encalhe) {
		this.encalhe = encalhe;
	}

	/**
	 * @return the valorTotal
	 */
	public String getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the novaData
	 */
	public String getNovaData() {
		return novaData;
	}

	/**
	 * @param novaData the novaData to set
	 */
	public void setNovaData(String novaData) {
		this.novaData = novaData;
	}

	/**
	 * @return the bloqueioDataRecolhimento
	 */
	public boolean isBloqueioDataRecolhimento() {
		return bloqueioDataRecolhimento;
	}

	/**
	 * @param bloqueioDataRecolhimento the bloqueioDataRecolhimento to set
	 */
	public void setBloqueioDataRecolhimento(boolean bloqueioDataRecolhimento) {
		this.bloqueioDataRecolhimento = bloqueioDataRecolhimento;
	}

	/**
	 * @return the bloqueioMatrizFechada
	 */
	public boolean isBloqueioMatrizFechada() {
		return bloqueioMatrizFechada;
	}

	/**
	 * @param bloqueioMatrizFechada the bloqueioMatrizFechada to set
	 */
	public void setBloqueioMatrizFechada(boolean bloqueioMatrizFechada) {
		this.bloqueioMatrizFechada = bloqueioMatrizFechada;
	}

}
