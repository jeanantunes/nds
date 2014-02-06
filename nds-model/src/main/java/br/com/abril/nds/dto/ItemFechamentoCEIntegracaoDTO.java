package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.model.integracao.StatusIntegracaoNFE;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ItemFechamentoCEIntegracaoDTO implements Serializable {
	
	private static final long serialVersionUID = -6711767359292325571L;

	@Export(label = "Sequencial", alignment = Alignment.LEFT, exhibitionOrder = 1)
	private Long sequencial;
	
	@Export(label = "Código", alignment = Alignment.LEFT, exhibitionOrder = 2)
	private String codigoProduto;
	
	@Export(label = "Produto", alignment = Alignment.LEFT, exhibitionOrder = 3)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment = Alignment.CENTER, exhibitionOrder = 4)
	private Long numeroEdicao;
	
	@Export(label = "Reparte", alignment = Alignment.CENTER, exhibitionOrder = 5)
	private BigInteger reparte;

	@Export(label = "Venda", alignment = Alignment.CENTER, exhibitionOrder = 6)
	private BigInteger venda;

	private BigDecimal precoCapa;
	
	@Export(label = "Preço Capa R$", alignment = Alignment.RIGHT, exhibitionOrder = 7)
	private String precoCapaFormatado;

	private BigDecimal valorVenda;

	@Export(label = "Encalhe", alignment = Alignment.CENTER, exhibitionOrder = 8)
	private BigInteger encalhe;
	
	@Export(label = "Valor Venda R$", alignment = Alignment.RIGHT, exhibitionOrder = 9)
	private String valorVendaFormatado;
		
	private String tipoFormatado;
	
	private Long idCota;
	
	private Long idProdutoEdicao;
	
	private Long idItemCeIntegracao;
	
	private StatusIntegracaoNFE statusIntegracaoNFE;
	
	private boolean integracaoNFEAprovado;
	
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
	
	
}
