package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.model.integracao.StatusIntegracaoNFE;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ItemFechamentoCEIntegracaoDTO implements Serializable {
	
	private static final long serialVersionUID = -6711767359292325571L;

	@Export(label = "Seq", alignment = Alignment.LEFT, exhibitionOrder = 1, widthPercent=5)
	private Long sequencial;
	
	@Export(label = "Código", alignment = Alignment.LEFT, exhibitionOrder = 2,widthPercent=6)
	private String codigoProduto;
	
	@Export(label = "Produto", alignment = Alignment.LEFT, exhibitionOrder = 3, widthPercent=25)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment = Alignment.CENTER, exhibitionOrder = 4, widthPercent=5)
	private Long numeroEdicao;
	
	private BigInteger reparte;

	@Export(label = "Venda", alignment = Alignment.CENTER, exhibitionOrder = 11 )
	private BigInteger venda;

	private BigDecimal precoCapa;
	
	@Export(label = "Preço Capa R$", alignment = Alignment.RIGHT, exhibitionOrder =12 )
	private String precoCapaFormatado;

	private BigDecimal valorVenda;

	private BigInteger encalhe;
	
	@Export(label = "Qtde Dev Inf", alignment = Alignment.CENTER, exhibitionOrder = 6 )
	private BigInteger qtdeDevSemCE;
	
	@Export(label = "Estoque", alignment = Alignment.CENTER, exhibitionOrder = 8)
	private BigInteger estoque;
	
	@Export(label = "Diferença", alignment = Alignment.CENTER, exhibitionOrder =10 )
	private BigInteger diferenca;
	
	@Export(label = "Valor Venda R$", alignment = Alignment.RIGHT, exhibitionOrder = 13)
	private String valorVendaFormatado;
		
	private String tipoFormatado;
	
	private Long idCota;
	
	private Long idProdutoEdicao;
	
	private Long idItemCeIntegracao;
	
	private StatusIntegracaoNFE statusIntegracaoNFE;
	
	private boolean integracaoNFEAprovado;
	
	private String desconto;
	
	
	private BigInteger qtdDevolucao;
	
	private String qtdNota;
	
	private BigDecimal valorTotal;
	
	private BigDecimal valorTotalComDesconto;
	
	private String valorTotalComDescontoFormatado;
	
	private final String REGIME_RECOLHIMENTO_PARCIAL = "PARCIAL";		
	
	
	
	private boolean isProdutoSemCe = false;
	
	@Export(label = "Reparte", alignment = Alignment.CENTER, exhibitionOrder = 7)
	public String getReparteFormatado(){
		
		if(REGIME_RECOLHIMENTO_PARCIAL.equals(this.tipoFormatado)){
			
			return (this.reparte != null 
					&& this.reparte.compareTo(BigInteger.ZERO) == 0)
					? "***" : this.reparte.toString();
		}
		
		return Util.nvl(this.reparte,0).toString();
	}
	
	@Export(label = "Encalhe", alignment = Alignment.CENTER, exhibitionOrder = 9 )
	public String getEncalheFormatado(){
		
		if(REGIME_RECOLHIMENTO_PARCIAL.equals(this.tipoFormatado)){
			
			return (this.encalhe!= null
					&& this.encalhe.compareTo(BigInteger.ZERO) == 0)
					? "***" : this.encalhe.toString();
		}
		
		return Util.nvl(this.encalhe,0).toString();
	}
	
    public boolean getIntegracaoNFEAprovado(){
    	integracaoNFEAprovado =  (statusIntegracaoNFE != null && StatusIntegracaoNFE.isAprovado(statusIntegracaoNFE));
    	return integracaoNFEAprovado;
    }
	
	@Export(label = "Tipo", alignment = Alignment.CENTER, exhibitionOrder = 5)
	public String getTipoFormatado() {
		return tipoFormatado;
	}

	public Long getSequencial() {
		return sequencial;
	}

	public void setSequencial(Long sequencial) {
		this.sequencial = sequencial;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public BigInteger getVenda() {
		return venda;
	}

	public void setVenda(BigInteger venda) {
		this.venda = venda;
	}

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
		this.precoCapaFormatado = CurrencyUtil.formatarValor(precoCapa);
	}

	public BigDecimal getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
		this.valorVendaFormatado = CurrencyUtil.formatarValor(valorVenda);
	}

	public BigInteger getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
	}

	public String getValorVendaFormatado() {
		return valorVendaFormatado;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	public String getPrecoCapaFormatado() {
		return precoCapaFormatado;
	}

	public Long getIdItemCeIntegracao() {
		return idItemCeIntegracao;
	}

	public void setIdItemCeIntegracao(Long idItemCeIntegracao) {
		this.idItemCeIntegracao = idItemCeIntegracao;
	}

	public StatusIntegracaoNFE getStatusIntegracaoNFE() {
		return statusIntegracaoNFE;
	}

	public void setStatusIntegracaoNFE(StatusIntegracaoNFE statusIntegracaoNFE) {
		this.statusIntegracaoNFE = statusIntegracaoNFE;
	}

	public BigInteger getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(BigInteger diferenca) {
		this.diferenca = diferenca;
	}

	public BigInteger getEstoque() {
		return estoque;
	}

	public void setEstoque(BigInteger estoque) {
		this.estoque = estoque;
	}

	public String getDesconto() {
		return desconto;
	}

	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}

	public BigInteger getQtdDevolucao() {
		return qtdDevolucao;
	}

	public void setQtdDevolucao(BigInteger qtdDevolucao) {
		this.qtdDevolucao = qtdDevolucao;
	}

	public String getQtdNota() {
		return qtdNota;
	}

	public void setQtdNota(String qtdNota) {
		this.qtdNota = qtdNota;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
		this.valorVendaFormatado = CurrencyUtil.formatarValor(valorTotal);
	}

	public BigDecimal getValorTotalComDesconto() {
		return valorTotalComDesconto;
	}

	public void setValorTotalComDesconto(BigDecimal valorTotalComDesconto) {
		this.valorTotalComDesconto = valorTotalComDesconto;
		this.valorTotalComDescontoFormatado = CurrencyUtil.formatarValor(valorTotalComDesconto);
	}

	public String getValorTotalComDescontoFormatado() {
		return valorTotalComDescontoFormatado;
	}

	public void setValorTotalComDescontoFormatado(String valorTotalComDescontoFormatado) {
		this.valorTotalComDescontoFormatado = valorTotalComDescontoFormatado;
	}

	public boolean isProdutoSemCe() {
		return isProdutoSemCe;
	}

	public void setProdutoSemCe(boolean isProdutoSemCe) {
		this.isProdutoSemCe = isProdutoSemCe;
	}

	public BigInteger getQtdeDevSemCE() {
		return qtdeDevSemCE;
	}

	public void setQtdeDevSemCE(BigInteger qtdeDevSemCE) {
		this.qtdeDevSemCE = qtdeDevSemCE;
	}
}