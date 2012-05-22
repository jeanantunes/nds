package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class RegistroCurvaABCCotaVO extends RegistroCurvaABC implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5830163841520335115L;

	@Export(label = "Código", exhibitionOrder = 1)
	private String numeroProduto;

	@Export(label = "Produto", exhibitionOrder = 2)
	private String nomeProduto;

	@Export(label = "Edição", exhibitionOrder = 3)
	private Long edicaoProduto;

	@Export(label = "Reparte", exhibitionOrder = 4)
	private BigDecimal reparte;

	@Export(label = "Venda de Exemplares", exhibitionOrder = 5)
	private BigDecimal vendaExemplares;

	@Export(label = "Venda %", exhibitionOrder = 6)
	private BigDecimal porcentagemVenda;

	@Export(label = "Faturamento R$", exhibitionOrder = 7)
	private BigDecimal faturamento;

	public RegistroCurvaABCCotaVO	() {
	}

	public RegistroCurvaABCCotaVO(String numeroProduto, String nomeProduto,
			Long numeroEdicao, BigDecimal reparte,
			BigDecimal vendaExemplares, BigDecimal faturamento) {
		this.numeroProduto = numeroProduto;
		this.nomeProduto = nomeProduto;
		this.edicaoProduto = numeroEdicao;
		this.reparte = reparte;
		this.vendaExemplares = vendaExemplares;
		this.faturamento = faturamento;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}

	public BigDecimal getVendaExemplares() {
		return vendaExemplares;
	}

	public void setVendaExemplares(BigDecimal vendaExemplares) {
		this.vendaExemplares = vendaExemplares;
	}

	public BigDecimal getPorcentagemVenda() {
		return porcentagemVenda;
	}

	public void setPorcentagemVenda(BigDecimal porcentagemVenda) {
		this.porcentagemVenda = porcentagemVenda;
	}

	public BigDecimal getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(BigDecimal faturamento) {
		this.faturamento = faturamento;
	}

	public String getNumeroProduto() {
		return numeroProduto;
	}

	public void setNumeroProduto(String numeroProduto) {
		this.numeroProduto = numeroProduto;
	}

	public Long getEdicaoProduto() {
		return edicaoProduto;
	}

	public void setEdicaoProduto(Long edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}

}
