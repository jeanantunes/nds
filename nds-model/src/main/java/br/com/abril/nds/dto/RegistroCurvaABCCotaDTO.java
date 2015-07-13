package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.ColumnType;
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

	@Export(label = "Reparte", exhibitionOrder = 5, columnType = ColumnType.NUMBER)
	private String reparteFormatado;
	
	@Export(label = "Venda de Exemplares", exhibitionOrder = 6, columnType = ColumnType.NUMBER)
	private String vendaExemplaresFormatado;

	@Export(label = "Venda %", exhibitionOrder = 7, columnType = ColumnType.DECIMAL)
	private String porcentagemVendaFormatado;
	
	@Export(label = "Faturamento R$", exhibitionOrder = 8, columnType = ColumnType.DECIMAL)
	private String faturamentoFormatado;
	
	@Export(label = "Ranking", exhibitionOrder = 1, columnType = ColumnType.NUMBER)
	private Long rkProduto;
	
	private Long idCota;
	
	private Long idProduto;
	
	private Long idProdutoEdicao;
	
	public RegistroCurvaABCCotaDTO	() {
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
		
		reparteFormatado = CurrencyUtil.formatarValorTruncado(reparte);
	}

	public BigInteger getVendaExemplares() {
		return vendaExemplares;
	}

	public void setVendaExemplares(BigInteger vendaExemplares) {
		this.vendaExemplares = vendaExemplares;
		
		vendaExemplaresFormatado = String.valueOf(vendaExemplares);
	}

	public BigDecimal getPorcentagemVenda() {
		return porcentagemVenda;
	}

	public void setPorcentagemVenda(BigDecimal porcentagemVenda) {
		this.porcentagemVenda = porcentagemVenda;
		
		porcentagemVendaFormatado = CurrencyUtil.formatarValor(porcentagemVenda);
	}

	public BigDecimal getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(BigDecimal faturamento) {
		this.faturamento = faturamento;
		
		faturamentoFormatado = CurrencyUtil.formatarValor(faturamento == null ? BigInteger.ZERO : faturamento);
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
	
	@Export(label = "% Participação", exhibitionOrder = 9, columnType = ColumnType.DECIMAL)
	public String getParticipacaoString() {
		return getParticipacaoFormatado();
	}

	@Export(label = "% Participação Acumulada", exhibitionOrder = 10, columnType = ColumnType.DECIMAL)
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
	
}
