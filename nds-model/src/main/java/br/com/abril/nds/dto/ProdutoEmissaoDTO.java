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
	
	private BigInteger reparte;
	
	private BigInteger quantidadeDevolvida;
	
	private Boolean confereciaRealizada;
	
	private BigDecimal precoVenda;
	private BigDecimal vlrPrecoComDesconto;
	public BigDecimal vlrDesconto;
	
	public BigInteger vendido;
	
	public String vlrVendido;
	
	public String getVlrVendido() {
		return vlrVendido;
	}
	
	public void setVlrVendido(String vlrVendido) {
		this.vlrVendido = vlrVendido;
	}
	
	public BigInteger getVendido() {
		return vendido;
	}
	
	public void setVendido(BigInteger vendido) {
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
		
			vlrDesconto = desconto;
			
			this.desconto = CurrencyUtil.formatarValor(desconto);
			
		} else {
			
			vlrDesconto = BigDecimal.ZERO;
			
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
		this.dataLancamento = DateUtil.formatarData(dataLancamento,"dd/MM/yy");
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
		
		if (precoComDesconto != null){
			
			this.vlrPrecoComDesconto= precoComDesconto; 
			
			this.precoComDesconto = CurrencyUtil.formatarValor(precoComDesconto);
		
		} else {
			
			this.vlrPrecoComDesconto= BigDecimal.ZERO;
			
			this.precoComDesconto = "0.00";
		
		}
		
		
	}
	/**
	 * @return the reparte
	 */
	public BigInteger getReparte() {
		return reparte;
	}
	/**
	 * @param reparte the reparte to set
	 */
	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}
	/**
	 * @return the quantidadeDevolvida
	 */
	public BigInteger getQuantidadeDevolvida() {
		return quantidadeDevolvida;
	}
	/**
	 * @param quantidadeDevolvida the quantidadeDevolvida to set
	 */
	public void setQuantidadeDevolvida(BigInteger quantidadeDevolvida) {
		this.quantidadeDevolvida = quantidadeDevolvida!=null? quantidadeDevolvida : BigInteger.ZERO;
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
	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}
	
	/**
	 * @param precoVenda the precoVenda to set
	 */
	public void setPrecoVenda(BigDecimal precoVenda) {
		
		this.precoVenda = (precoVenda == null)? BigDecimal.ZERO : precoVenda;
	
	}
	
	/**
	 * @return the vlrPrecoComDesconto
	 */
	public BigDecimal getVlrPrecoComDesconto() {
		return vlrPrecoComDesconto;
	}
	
	/**
	 * @param vlrPrecoComDesconto the vlrPrecoComDesconto to set
	 */
	public void setVlrPrecoComDesconto(BigDecimal vlrPrecoComDesconto) {
		this.vlrPrecoComDesconto = vlrPrecoComDesconto;
	}
	
	public BigDecimal getVlrDesconto() {
		return vlrDesconto;
	}
	
	public void setVlrDesconto(BigDecimal vlrDesconto) {
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

	public Boolean getConfereciaRealizada() {
		return confereciaRealizada;
	}

	public void setConfereciaRealizada(Boolean confereciaRealizada) {
		this.confereciaRealizada = confereciaRealizada;
	}
	
}