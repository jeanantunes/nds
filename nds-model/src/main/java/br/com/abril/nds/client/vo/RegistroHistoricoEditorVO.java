package br.com.abril.nds.client.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

/**
 * Classe responsável por armazenar os valores referente aos registros da
 * pesquisa de registra de históricos de editor.
 * @author InfoA2
 */
@Exportable
public class RegistroHistoricoEditorVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7475719189534371260L;

	private String nomeEditor;
	
	@Export(label = "Código", exhibitionOrder = 1)
	private String codigoProduto;

	@Export(label = "Produto", exhibitionOrder = 2)
	private String nomeProduto;

	@Export(label = "EdicaoProduto", exhibitionOrder = 3, columnType = ColumnType.NUMBER)
	private Long edicaoProduto;

	private BigInteger reparte;

	private BigInteger vendaExemplares;

	private BigDecimal porcentagemVenda;
	
	private BigDecimal valorMargemCota;
	
	private BigDecimal valorMargemDistribuidor;
	
	private BigDecimal faturamento;
	
	private String margemDistribuidorFormatado;
	
	private String margemCotaFormatado;
	
	private String faturamentoFormatado;

	@Export(label = "Reparte", exhibitionOrder = 4, columnType = ColumnType.NUMBER)
	private String reparteFormatado;
	
	@Export(label = "Venda Exs.", exhibitionOrder = 5, columnType = ColumnType.NUMBER)
	private String vendaExemplaresFormatado;
	
	@Export(label = "% Venda", exhibitionOrder = 6,alignment=Alignment.RIGHT, columnType = ColumnType.DECIMAL)
	private String porcentagemVendaFormatado;
	
	@Export(label = "Faturamento R$", exhibitionOrder = 8,alignment=Alignment.RIGHT, columnType = ColumnType.DECIMAL)
	public String getFaturamentoFormatado(){
		return this.faturamentoFormatado;
	}
	
	@Export(label = "Margem Cota R$", exhibitionOrder = 7,alignment=Alignment.RIGHT, columnType = ColumnType.DECIMAL)
	public String getMargemCotaFormatado(){
		return this.margemCotaFormatado;
	}
	
	@Export(label = "Margem Distrib R$", exhibitionOrder = 9, alignment=Alignment.RIGHT, columnType = ColumnType.DECIMAL)
	public String getMargemDistribuidorFormatado(){
		return this.margemDistribuidorFormatado;
	}
	
	public RegistroHistoricoEditorVO() {}
	
	public RegistroHistoricoEditorVO(String nomeEditor, String codigoProduto, String nomeProduto,
			Long edicaoProduto, BigInteger reparte, BigInteger vendaExemplares) {
		this.nomeEditor=nomeEditor;
		this.codigoProduto=codigoProduto;
		this.nomeProduto=nomeProduto;
		this.edicaoProduto=edicaoProduto;
		this.reparte=reparte;
		this.vendaExemplares=vendaExemplares;
		this.formatarCampos();
	}
	
	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getEdicaoProduto() {
		return edicaoProduto;
	}

	public void setEdicaoProduto(Long edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}

	public BigInteger getVendaExemplares() {
		return vendaExemplares;
	}

	public BigDecimal getPorcentagemVenda() {
		return porcentagemVenda;
	}

	public void setPorcentagemVenda(BigDecimal porcentagemVenda) {
		this.porcentagemVendaFormatado = CurrencyUtil.formatarValor(porcentagemVenda);
		this.porcentagemVenda = porcentagemVenda;
	}

	public void setVendaExemplares(BigInteger vendaExemplares) {
		this.vendaExemplares = vendaExemplares;
		this.vendaExemplaresFormatado = vendaExemplares.toString();
	}

	public String getNomeEditor() {
		return nomeEditor;
	}

	public void setNomeEditor(String nomeEditor) {
		this.nomeEditor = nomeEditor;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
		this.reparteFormatado = reparte.toString();
	}

	public String getReparteFormatado() {
		return reparteFormatado;
	}

	public void setReparteFormatado(String reparteFormatado) {
		this.reparteFormatado = reparteFormatado;
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

	private void formatarCampos() {
		this.vendaExemplaresFormatado = CurrencyUtil.formatarValorTruncado(vendaExemplares);
		this.porcentagemVendaFormatado = CurrencyUtil.formatarValor(porcentagemVenda);
	}

	public BigDecimal getValorMargemCota() {
		return valorMargemCota;
	}

	public void setValorMargemCota(BigDecimal valorMargemCota) {
		this.valorMargemCota = valorMargemCota;
		this.margemCotaFormatado = CurrencyUtil.formatarValor(valorMargemCota);
	}

	public BigDecimal getValorMargemDistribuidor() {
		return valorMargemDistribuidor;
	}

	public void setValorMargemDistribuidor(BigDecimal valorMargemDistribuidor) {
		this.valorMargemDistribuidor = valorMargemDistribuidor;
		this.margemDistribuidorFormatado = CurrencyUtil.formatarValor(valorMargemDistribuidor);
	}

	public BigDecimal getFaturamento() {
		return faturamento;
	}

	public void setFaturamento(BigDecimal faturamento) {
		this.faturamento = faturamento;
		this.faturamentoFormatado = CurrencyUtil.formatarValor(faturamento);
	}
}
