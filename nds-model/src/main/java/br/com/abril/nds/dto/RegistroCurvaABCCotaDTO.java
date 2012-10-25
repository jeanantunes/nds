package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

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

	@Export(label = "Código", exhibitionOrder = 2)
	private String codigoProduto;

	@Export(label = "Produto", exhibitionOrder = 3)
	private String nomeProduto;

	@Export(label = "Edição", exhibitionOrder = 4)
	private Long edicaoProduto;

	private BigInteger reparte;

	private BigInteger vendaExemplares;

	private BigDecimal porcentagemVenda;

	private BigDecimal faturamento;

	@Export(label = "Reparte", exhibitionOrder = 5)
	private String reparteFormatado;
	
	@Export(label = "Venda de Exemplares", exhibitionOrder = 6)
	private String vendaExemplaresFormatado;

	@Export(label = "Venda %", exhibitionOrder = 7)
	private String porcentagemVendaFormatado;
	
	@Export(label = "Faturamento R$", exhibitionOrder = 8)
	private String faturamentoFormatado;
	
	@Export(label = "Ranking", exhibitionOrder = 1)
	private Long rkProduto;
	
	private Long idCota;
	
	private Long idProduto;
	
	public RegistroCurvaABCCotaDTO	() {
	}

	public RegistroCurvaABCCotaDTO(String numeroProduto, String codigoProduto,
			Long numeroEdicao, BigInteger reparte,
			BigInteger vendaExemplares, BigDecimal faturamento,Long idCota,Long idProduto) {
		this.codigoProduto = numeroProduto;
		this.nomeProduto = codigoProduto;
		this.edicaoProduto = numeroEdicao;
		this.reparte = reparte;
		this.vendaExemplares = vendaExemplares;
		this.faturamento = faturamento;
		this.idCota = idCota;
		this.idProduto = idProduto;
		this.formatarCampos();
	}
	
	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Long getRkProduto() {
		return rkProduto;
	}

	public void setRkProduto(Long rkProduto) {
		this.rkProduto = rkProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public BigInteger getVendaExemplares() {
		return vendaExemplares;
	}

	public void setVendaExemplares(BigInteger vendaExemplares) {
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
	
	@Export(label = "Participação", exhibitionOrder = 9)
	public String getParticipacaoString() {
		return getParticipacaoFormatado();
	}

	@Export(label = "Participação Acumulada", exhibitionOrder = 10)
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
		faturamentoFormatado = CurrencyUtil.formatarValor( (faturamento==null)?BigInteger.ZERO:faturamento);
	}
	
}
