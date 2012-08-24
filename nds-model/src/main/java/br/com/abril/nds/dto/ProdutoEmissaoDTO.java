package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

public class ProdutoEmissaoDTO implements Serializable{

	private static final long serialVersionUID = -6994377052719897181L;
	
	
	private Long idProdutoEdicao;
	private Integer sequencia;
	private String codigoBarras;
	private String codigoProduto;
	private String nomeProduto;
	private Long edicao;
	private String desconto;
	private String tipoRecolhimento;
	private String dataLancamento;
	private String precoComDesconto;
	private Integer reparte;
	private Integer quantidadeDevolvida;
	private Double precoVenda;
	private Double vlrPrecoComDesconto;
	public Double vlrDesconto;
	public Integer vendido;
	public String vlrVendido;
	
	public String getVlrVendido() {
		return vlrVendido;
	}
	
	public void setVlrVendido(String vlrVendido) {
		this.vlrVendido = vlrVendido;
	}
	
	public Integer getVendido() {
		return vendido;
	}
	
	public void setVendido(Integer vendido) {
		this.vendido = vendido;
	}
	
	/**
	 * @return the seqStringuencia
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
	 * @return the edicao
	 */
	public Long getEdicao() {
		return edicao;
	}
	/**
	 * @param edicao the edicao to set
	 */
	public void setEdicao(Long edicao) {
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
	public void setDesconto(BigDecimal desconto) {
		
		if (desconto != null){
		
			vlrDesconto = desconto.doubleValue();
			this.desconto = CurrencyUtil.formatarValor(desconto);
		} else {
			
			vlrDesconto = 0d;
			this.desconto = "0.00";
		}
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
	public void setTipoRecolhimento(Boolean tipoRecolhimento) {
		this.tipoRecolhimento = tipoRecolhimento ? "P":"F";
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
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = DateUtil.formatarDataPTBR(dataLancamento);
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
	public void setPrecoComDesconto(BigDecimal precoComDesconto) {
		this.vlrPrecoComDesconto= precoComDesconto.doubleValue(); 
		this.precoComDesconto = CurrencyUtil.formatarValor(precoComDesconto);
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
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte.intValue();
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
	public void setQuantidadeDevolvida(BigInteger quantidadeDevolvida) {
		this.quantidadeDevolvida = quantidadeDevolvida!=null?quantidadeDevolvida.intValue():0;
	}
	/**
	 * @return the codigoBarras
	 */
	public String getCodigoBarras() {
		return codigoBarras;
	}
	/**
	 * @param codigoBarras the codigoBarras to set
	 */
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	/**
	 * @return the precoVenda
	 */
	public Double getPrecoVenda() {
		return precoVenda;
	}
	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda.doubleValue();
	}
	/**
	 * @return the vlrPrecoComDesconto
	 */
	public Double getVlrPrecoComDesconto() {
		return vlrPrecoComDesconto;
	}
	/**
	 * @param vlrPrecoComDesconto the vlrPrecoComDesconto to set
	 */
	public void setVlrPrecoComDesconto(Double vlrPrecoComDesconto) {
		this.vlrPrecoComDesconto = vlrPrecoComDesconto;
	}
	
	public Double getVlrDesconto() {
		return vlrDesconto;
	}
	
	public void setVlrDesconto(Double vlrDesconto) {
		this.vlrDesconto = vlrDesconto;
	}

	/**
	 * @return the idProdutoEdicao
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}
}