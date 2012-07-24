package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

public class ProdutoEmissaoDTO implements Serializable{

	private static final long serialVersionUID = -6994377052719897181L;

	private Integer sequencia;
	private Integer codigoProduto;
	private String nomeProduto;
	private Integer edicao;
	private String desconto;
	private String tipoRecolhimento;
	private String dataLancamento;
	private String precoComDesconto;
	private Integer reparte;
	private Integer quantidadeDevolvida;
	/**
	 * @return the sequencia
	 */
	public Integer getSequencia() {
		return sequencia;
	}
	/**
	 * @param sequencia the sequencia to set
	 */
	public void setSequencia(Integer sequencia) {
		this.sequencia = sequencia;
	}
	/**
	 * @return the codigoProduto
	 */
	public Integer getCodigoProduto() {
		return codigoProduto;
	}
	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(Integer codigoProduto) {
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
	 * @return the edicao
	 */
	public Integer getEdicao() {
		return edicao;
	}
	/**
	 * @param edicao the edicao to set
	 */
	public void setEdicao(Integer edicao) {
		this.edicao = edicao;
	}
	/**
	 * @return the desconto
	 */
	public String getDesconto() {
		return desconto;
	}
	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}
	/**
	 * @return the tipoRecolhimento
	 */
	public String getTipoRecolhimento() {
		return tipoRecolhimento;
	}
	/**
	 * @param tipoRecolhimento the tipoRecolhimento to set
	 */
	public void setTipoRecolhimento(String tipoRecolhimento) {
		this.tipoRecolhimento = tipoRecolhimento;
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
	 * @return the precoComDesconto
	 */
	public String getPrecoComDesconto() {
		return precoComDesconto;
	}
	/**
	 * @param precoComDesconto the precoComDesconto to set
	 */
	public void setPrecoComDesconto(String precoComDesconto) {
		this.precoComDesconto = precoComDesconto;
	}
	/**
	 * @return the reparte
	 */
	public Integer getReparte() {
		return reparte;
	}
	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(Integer reparte) {
		this.reparte = reparte;
	}
	/**
	 * @return the quantidadeDevolvida
	 */
	public Integer getQuantidadeDevolvida() {
		return quantidadeDevolvida;
	}
	/**
	 * @param quantidadeDevolvida the quantidadeDevolvida to set
	 */
	public void setQuantidadeDevolvida(Integer quantidadeDevolvida) {
		this.quantidadeDevolvida = quantidadeDevolvida;
	}
	
	
}