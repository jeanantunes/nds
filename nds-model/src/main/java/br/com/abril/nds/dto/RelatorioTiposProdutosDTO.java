package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RelatorioTiposProdutosDTO implements Serializable {

	private static final long serialVersionUID = -9000972218243942262L;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2, widthPercent=35)
	private String produto;
	
	@Export(label = "Edição", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private Long edicao;
	
	@Export(label = "Preço Capa R$", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private BigDecimal precoCapa;
	
	@Export(label = "Faturamento R$", alignment=Alignment.RIGHT, exhibitionOrder = 5)
	private BigDecimal faturamento;
	
	@Export(label = "Tipo Produto", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private String tipoProduto;
	
	@Export(label = "Lcto", alignment=Alignment.CENTER, exhibitionOrder = 7)
	private Date lancamento;
	
	@Export(label = "Rclto", alignment=Alignment.CENTER, exhibitionOrder = 8)
	private Date recolhimento;
	
	
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
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}
	public void setPrecoCapa(BigDecimal precoCapa) {
		
		if (precoCapa == null){
			
			precoCapa = BigDecimal.ZERO;
		}
		
		this.precoCapa = precoCapa.setScale(2, RoundingMode.HALF_EVEN);
	}
	/**
	 * @return the faturamento
	 */
	public BigDecimal getFaturamento() {
		return faturamento;
	}
	/**
	 * @param faturamento the faturamento to set
	 */
	public void setFaturamento(BigDecimal faturamento) {
		
		if (faturamento == null){
			
			faturamento = BigDecimal.ZERO;
		}
		
		this.faturamento = faturamento.setScale(2, RoundingMode.HALF_EVEN);
	}
	public String getTipoProduto() {
		return tipoProduto;
	}
	public void setTipoProduto(String tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	public Date getLancamento() {
		return lancamento;
	}
	public void setLancamento(Date lancamento) {
		this.lancamento = lancamento;
	}
	public Date getRecolhimento() {
		return recolhimento;
	}
	public void setRecolhimento(Date recolhimento) {
		this.recolhimento = recolhimento;
	}
}