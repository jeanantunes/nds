package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class VendaProdutoDTO implements Serializable {

	private static final long serialVersionUID = 2903790306949832310L;
	
	@Export(label = "Edição", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private Long numEdicao;
	
	@Export(label = "Data Lançamento", alignment=Alignment.CENTER, exhibitionOrder = 2)
	private String dataLancamento;
	
	@Export(label = "Data Recolhimento", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private String dataRecolhimento;
	
	@Export(label = "Reparte", alignment=Alignment.CENTER, exhibitionOrder = 4)
	private BigInteger reparte;
	
	@Export(label = "Venda", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private BigInteger venda;

	private BigDecimal percentualVenda;
	
	private BigDecimal precoCapa;
	
	private BigDecimal total;
	
	private BigDecimal encalhe;
	
	@Export(label = "% Venda", alignment=Alignment.RIGHT, exhibitionOrder = 6)
	private String percentualVendaFormatado;
	
	@Export(label = "Preço Capa R$", alignment=Alignment.RIGHT, exhibitionOrder = 7)
	private String valorPrecoCapaFormatado;
	
	@Export(label = "Total R$", alignment=Alignment.RIGHT, exhibitionOrder = 8)
	private String valorTotalFormatado;
	
	@Export(label = "Chamada Capa", alignment=Alignment.LEFT, exhibitionOrder = 9)
	private String chamadaCapa;
	
	private Long numeroEstudo = Long.valueOf(0);
	private PeriodicidadeProduto periodo;
	private Integer periodoFormatado = 1;
	
	private Boolean parcial;

	private String codigoProduto;
	
	private BigInteger idProdutoEdicao;
	private String statusLancamento;
	
	public VendaProdutoDTO() {
		
	}

	public Long getNumEdicao() {
		return numEdicao;
	}

	public void setNumEdicao(Long numEdicao) {
		this.numEdicao = numEdicao;
	}

	/**
	 * @return the chamadaCapa
	 */
	public String getChamadaCapa() {
		return chamadaCapa;
	}

	/**
	 * @param chamadaCapa the chamadaCapa to set
	 */
	public void setChamadaCapa(String chamadaCapa) {
		this.chamadaCapa = (chamadaCapa != null) ? chamadaCapa : "";
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

	public BigDecimal getPercentualVenda() {
		return percentualVenda;
	}

	public void setPercentualVenda(BigDecimal percentualVenda) {
		this.percentualVenda = percentualVenda;
		this.percentualVendaFormatado = CurrencyUtil.formatarValor(percentualVenda);
	}

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
		this.valorPrecoCapaFormatado = CurrencyUtil.formatarValor(precoCapa);
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
		this.valorTotalFormatado = CurrencyUtil.formatarValor(total);
	}

	public BigDecimal getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(BigDecimal encalhe) {
		this.encalhe = encalhe;
	}

	public String getValorPrecoCapaFormatado() {
		return this.valorPrecoCapaFormatado;
	}

	public void setValorPrecoCapaFormatado(String valorPrecoCapaFormatado) {
		this.valorPrecoCapaFormatado = valorPrecoCapaFormatado;
	}

	public String getValorTotalFormatado() {
		return this.valorTotalFormatado;
	}

	public void setValorTotalFormatado(String valorTotalFormatado) {
		this.valorTotalFormatado = valorTotalFormatado;
	}

	public String getPercentualVendaFormatado() {
		return this.percentualVendaFormatado;
	}

	public void setPercentualVendaFormatado(String percentualVendaFormatado) {
		this.percentualVendaFormatado = percentualVendaFormatado;
	}

	public String getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = DateUtil.formatarDataPTBR(dataLancamento);
	}

	public String getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = DateUtil.formatarDataPTBR(dataRecolhimento);
	}

	public Long getNumeroEstudo() {
		return numeroEstudo;
	}

	public void setNumeroEstudo(Long numeroEstudo) {
		this.numeroEstudo = numeroEstudo;
	}

	public PeriodicidadeProduto getPeriodo() {
		return periodo;
	}

	public void setPeriodo(PeriodicidadeProduto periodo) {
		this.periodo = periodo;
		this.periodoFormatado = periodo.getOrdem();
	}

	public Integer getPeriodoFormatado() {
		return periodoFormatado;
	}
	/**
	 * @return the parcial
	 */
	public Boolean getParcial() {
		return parcial;
	}

	/**
	 * @param parcial the parcial to set
	 */
	public void setParcial(Boolean parcial) {
		this.parcial = parcial;
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

	public BigInteger getIdProdutoEdicao() {
		return idProdutoEdicao;
	}

	public void setIdProdutoEdicao(BigInteger idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	public String getStatusLancamento() {
		return statusLancamento;
	}

	public void setStatusLancamento(String statusLancamento) {
		this.statusLancamento = statusLancamento;
	}
	
}
