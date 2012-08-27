package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FechamentoCEIntegracaoDTO {
	
	@Export(label = "Sequencial", alignment = Alignment.LEFT, exhibitionOrder = 1)
	private Integer sequencial;
	
	@Export(label = "Código", alignment = Alignment.LEFT, exhibitionOrder = 2)
	private String codigo;
	
	@Export(label = "Produto", alignment = Alignment.LEFT, exhibitionOrder = 3)
	private String produto;
	
	@Export(label = "Edição", alignment = Alignment.LEFT, exhibitionOrder = 4)
	private Long edicao;
	
	private BigInteger reparte;
	
	private Long produtoEdicao;
	private BigDecimal precoCapa;
	private BigInteger encalhe;
	private BigDecimal total;
	
	@Export(label = "Físico", alignment = Alignment.RIGHT, exhibitionOrder = 7)
	private Long fisico;
	
	@Export(label = "Diferença", alignment = Alignment.RIGHT, exhibitionOrder = 8)
	private Long diferenca = Long.valueOf(0);
	
	@Export(label = "Preço Capa R$", alignment = Alignment.RIGHT, exhibitionOrder = 4)
	private String precoCapaFormatado;

	@Export(label = "Exempl. Devolução", alignment = Alignment.CENTER, exhibitionOrder = 5)
	private String exemplaresDevolucaoFormatado;
	
	@Export(label = "Total R$", alignment = Alignment.RIGHT, exhibitionOrder = 6)
	private String totalFormatado;
	
	private BigInteger venda;
	
	private String valorVendaFormatado;
	
	
	public String getPrecoCapaFormatado() {
		return this.precoCapaFormatado;
	}
	
	public String getExemplaresDevolucaoFormatado() {
		return this.exemplaresDevolucaoFormatado;
	}
	
	public String getTotalFormatado() {
		return this.totalFormatado;
	}
	
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getProduto() {
		return produto;
	}
	public void setProduto(String produto) {
		this.produto = produto;
	}
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	public Long getProdutoEdicao() {
		return produtoEdicao;
	}
	public void setProdutoEdicao(Long produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
		this.precoCapaFormatado = CurrencyUtil.formatarValor(this.precoCapa); 
	}
	public BigInteger getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
		this.exemplaresDevolucaoFormatado = CurrencyUtil.formatarValorTruncado(this.encalhe).replaceAll("\\D", "");
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
		this.totalFormatado = CurrencyUtil.formatarValor(this.total); 
	}
	public Long getFisico() {
		return fisico;
	}
	public void setFisico(Long fisico) {
		this.fisico = fisico;
	}
	public Long getDiferenca() {
		return diferenca;
	}
	public void setDiferenca(Long diferenca) {
		this.diferenca = diferenca;
	}	

	public Integer getSequencial() {
		return sequencial;
	}

	public void setSequencial(Integer sequencial) {
		this.sequencial = sequencial;
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

	public String getvalorVendaFormatado() {
		return valorVendaFormatado;
	}

	public void setvalorVendaFormatado(String valorVendaFormatado) {
		this.valorVendaFormatado = valorVendaFormatado;
	}
	
	
	
}
