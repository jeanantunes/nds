package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Footer;
import br.com.abril.nds.util.export.FooterType;

@Exportable
public class RelatorioNFeExemplaresDTO implements Serializable {
	
	private static final long serialVersionUID = -5054002962389418662L;
	
	public RelatorioNFeExemplaresDTO() {
		
	}

	private Long idCota;
	
	@Export(label="COTA", alignment=Alignment.LEFT, widthPercent=5, fontSize=6)
	private Integer numeroCota;
	
	private String nomeCota;
	
	@Export(label="CODIGO", alignment=Alignment.CENTER, fontSize=7)
	private String codigoProduto; 
	
	@Export(label="NOME DO PRODUTO", alignment=Alignment.LEFT, widthPercent=25, fontSize=7)
	private String nomeProduto;
	
	@Export(label="EDIÇÃO", alignment=Alignment.CENTER, fontSize=7)
	private Long edicao;
	
	@Export(label="NCM", alignment=Alignment.CENTER, fontSize=7)
	private Long ncm;
	
	@Export(label="QTDE DE VENDA", alignment=Alignment.CENTER, fontSize=7)
	private BigInteger exemplares;
	
	@Export(label="PRECO CAPA", alignment=Alignment.CENTER, columnType = ColumnType.MOEDA_QUATRO_CASAS, fontSize=7)
	private BigDecimal precoCapa;
	
	@Export(label="PRECO C/ DESC", alignment=Alignment.CENTER, columnType = ColumnType.MOEDA_QUATRO_CASAS, fontSize=7)
	private BigDecimal precoDesconto;
	
	@Export(label="VALOR TOTAL CAPA", alignment=Alignment.CENTER, columnType = ColumnType.MOEDA_QUATRO_CASAS, fontSize=7)
	private BigDecimal valorCapa; 
	
	@Export(label="VALOR TOTAL COM DESC", alignment=Alignment.CENTER, columnType = ColumnType.MOEDA_QUATRO_CASAS, fontSize=7)
	@Footer(type = FooterType.SUM,columnType = ColumnType.MOEDA)
	private BigDecimal valorTotalDesconto; 
	
	@Export(label="VALOR DO PIS", alignment=Alignment.CENTER, columnType = ColumnType.MOEDA_QUATRO_CASAS, fontSize=7)
	@Footer(type = FooterType.SUM,columnType = ColumnType.MOEDA)
	private BigDecimal valorPis;
	
	@Export(label="VALOR DO CONFINS", alignment=Alignment.CENTER, columnType = ColumnType.MOEDA_QUATRO_CASAS, fontSize=7)
	@Footer(type = FooterType.SUM,columnType = ColumnType.MOEDA, printVertical=true, alignWithHeader="12")
	private BigDecimal valorConfins;
	
	private boolean imposto;
	
	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
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

	public Long getEdicao() {
		return edicao;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}

	public Long getNcm() {
		return ncm;
	}

	public void setNcm(Long ncm) {
		this.ncm = ncm;
	}

	
	
	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	public BigDecimal getPrecoDesconto() {
		return precoDesconto;
	}

	public void setPrecoDesconto(BigDecimal precoDesconto) {
		this.precoDesconto = precoDesconto;
	}

	public BigDecimal getValorCapa() {
		return valorCapa;
	}

	public void setValorCapa(BigDecimal valorCapa) {
		this.valorCapa = valorCapa;
	}

	public BigDecimal getValorTotalDesconto() {
		return valorTotalDesconto;
	}

	public void setValorTotalDesconto(BigDecimal valorTotalDesconto) {
		this.valorTotalDesconto = valorTotalDesconto;
	}

	public BigDecimal getValorPis() {
		return valorPis;
	}

	public void setValorPis(BigDecimal valorPis) {
		this.valorPis = valorPis;
	}

	public BigDecimal getValorConfins() {
		return valorConfins;
	}

	public void setValorConfins(BigDecimal valorConfins) {
		this.valorConfins = valorConfins;
	}

	public BigInteger getExemplares() {
		return exemplares;
	}

	public void setExemplares(BigInteger exemplares) {
		this.exemplares = exemplares;
	}

	public boolean isImposto() {
		return imposto;
	}

	public void setImposto(boolean imposto) {
		this.imposto = imposto;
	}
}