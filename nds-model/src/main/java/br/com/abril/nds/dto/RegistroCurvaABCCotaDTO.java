package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

/**
 * Classe responsável por armazenar os valores referente aos registros da
 * pesquisa de registra de curva ABC da cota.
 * @author InfoA2
 */
@Exportable
public class RegistroCurvaABCCotaDTO extends RegistroCurvaABCDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5830163841520335115L;

	@Export(label = "Código", exhibitionOrder = 1)
	private String codigoProduto;

	@Export(label = "Produto", exhibitionOrder = 2)
	private String nomeProduto;

	@Export(label = "Edição", exhibitionOrder = 3)
	private Long edicaoProduto;

	private BigDecimal reparte;

	private BigDecimal vendaExemplares;

	private BigDecimal porcentagemVenda;

	private BigDecimal faturamento;

	@Export(label = "Reparte", exhibitionOrder = 4)
	private String reparteFormatado;
	
	@Export(label = "Venda de Exemplares", exhibitionOrder = 5)
	private String vendaExemplaresFormatado;

	@Export(label = "Venda %", exhibitionOrder = 6)
	private String porcentagemVendaFormatado;
	
	@Export(label = "Faturamento R$", exhibitionOrder = 7)
	private String faturamentoFormatado;
	
	public RegistroCurvaABCCotaDTO	() {
	}

	public RegistroCurvaABCCotaDTO(String numeroProduto, String codigoProduto,
			Long numeroEdicao, BigDecimal reparte,
			BigDecimal vendaExemplares, BigDecimal faturamento) {
		this.codigoProduto = numeroProduto;
		this.nomeProduto = codigoProduto;
		this.edicaoProduto = numeroEdicao;
		this.reparte = reparte;
		this.vendaExemplares = vendaExemplares;
		this.faturamento = faturamento;
		this.formatarCampos();
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
		porcentagemVendaFormatado = CurrencyUtil.formatarValorTruncado(porcentagemVenda);
		this.porcentagemVenda = porcentagemVenda;
	}

	public BigDecimal getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(BigDecimal faturamento) {
		this.faturamento = faturamento;
	}

	public Long getEdicaoProduto() {
		return edicaoProduto;
	}

	public void setEdicaoProduto(Long edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	@Export(label = "Participação", exhibitionOrder = 7)
	public String getParticipacaoString() {
		return getParticipacaoFormatado();
	}

	@Export(label = "Participação Acumulada", exhibitionOrder = 8)
	public String getParticipacaoAcumuladaString() {
		return getParticipacaoAcumuladaFormatado();
	}

	public String getVendaExemplaresFormatado() {
		return vendaExemplaresFormatado;
	}

	public void setVendaExemplaresFormatado(String vendaExemplaresFormatado) {
		this.vendaExemplaresFormatado = vendaExemplaresFormatado;
	}

	public String getPorcentagemVendaFormatado() {
		return porcentagemVendaFormatado;
	}

	public void setPorcentagemVendaFormatado(String porcentagemVendaFormatado) {
		this.porcentagemVendaFormatado = porcentagemVendaFormatado;
	}

	public String getFaturamentoFormatado() {
		return faturamentoFormatado;
	}

	public void setFaturamentoFormatado(String faturamentoFormatado) {
		this.faturamentoFormatado = faturamentoFormatado;
	}

	private void formatarCampos() {
		reparteFormatado = CurrencyUtil.formatarValorTruncado(reparte);
		vendaExemplaresFormatado = CurrencyUtil.formatarValorTruncado(vendaExemplares);
		porcentagemVendaFormatado = CurrencyUtil.formatarValorTruncado(porcentagemVenda);
		faturamentoFormatado = CurrencyUtil.formatarValor(faturamento);
	}
	
}
