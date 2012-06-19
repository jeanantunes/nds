package br.com.abril.nds.dto;

import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class FechamentoFisicoLogicoDTO {

	@Export(label = "Código", alignment = Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto", alignment = Alignment.LEFT, exhibitionOrder = 2)
	private String produto;
	
	@Export(label = "Edição", alignment = Alignment.LEFT, exhibitionOrder = 3)
	private Long edicao;
	
	private Long produtoEdicao;
	private BigDecimal precoCapa;
	private BigDecimal exemplaresDevolucao;
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
	
	private String replicar = "";
	
	private Boolean fechado;
	
	
	public String getReplicar() {
		return replicar;
	}

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
	public BigDecimal getExemplaresDevolucao() {
		return exemplaresDevolucao;
	}
	public void setExemplaresDevolucao(BigDecimal exemplaresDevolucao) {
		this.exemplaresDevolucao = exemplaresDevolucao;
		this.exemplaresDevolucaoFormatado = CurrencyUtil.formatarValorTruncado(this.exemplaresDevolucao).replaceAll("\\D", "");
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
	public Boolean getFechado() {
		return fechado;
	}
	public void setFechado(Boolean fechado) {
		this.fechado = fechado;
	}
}
