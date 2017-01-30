package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.ColumnType;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Footer;
import br.com.abril.nds.util.export.FooterType;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class ExtracaoContaCorrenteDTO implements Serializable {

	private static final long serialVersionUID = -1121559493748523550L;
	
	@Export(label = "SM", alignment = Alignment.CENTER, widthPercent = 3f, exhibitionOrder = 1)
	private Integer sequenciaMatriz;
	
	@Export(label = "CÓDIGO", alignment = Alignment.CENTER, widthPercent = 4f, exhibitionOrder = 2)
	private String codigoProduto;
	
	@Export(label = "PRODUTO", alignment=Alignment.LEFT, widthPercent = 10f, exhibitionOrder = 3)
	private String nomeProduto;
	
	@Export(label = "EDIÇÃO", alignment = Alignment.CENTER, widthPercent = 4f, exhibitionOrder = 4)
	private Long numeroEdicao;
	
	@Export(label = "R$ CAPA", alignment = Alignment.CENTER, widthPercent = 3f, exhibitionOrder = 5)
	private BigDecimal precoCapa;
	
	@Export(label = "PP", alignment = Alignment.CENTER, widthPercent = 3f, exhibitionOrder = 6)
	private Integer pacotePadrao;
	
	@Export(label = "DESCONTO", alignment = Alignment.CENTER, widthPercent = 4f, exhibitionOrder = 7)
	private BigDecimal desconto;
	
	@Export(label = "REPARTE", alignment = Alignment.CENTER, widthPercent = 4f, exhibitionOrder = 8)
	@Footer(label = "Total", type = FooterType.SUM,columnType = ColumnType.NUMBER)
	private BigInteger reparte;
	
	@Export(label = "VENDA ENC.", alignment = Alignment.CENTER, widthPercent = 5f, exhibitionOrder = 9)
	@Footer(type = FooterType.SUM,columnType = ColumnType.NUMBER)
	private BigInteger vendaEncalhe;
	
	@Export(label = "ENCALHE", alignment = Alignment.CENTER, widthPercent = 4f, exhibitionOrder = 10)
	@Footer(type = FooterType.SUM,columnType = ColumnType.NUMBER)
	private BigInteger encalhe;
	
	@Export(label = "VENDA", alignment = Alignment.CENTER, widthPercent = 3f, exhibitionOrder = 11)
	@Footer(type = FooterType.SUM,columnType = ColumnType.NUMBER)
	private BigInteger venda;
	
	@Export(label = "R$ VENDA TOTAL", alignment = Alignment.CENTER, widthPercent = 7f, exhibitionOrder = 12)
	@Footer(type = FooterType.SUM,columnType = ColumnType.MOEDA)
	private String vendaTotalFormatado;
	
	private BigDecimal vendaTotal;
	
	@Footer(label = "Total desconto logistica",type = FooterType.SUM,columnType = ColumnType.MOEDA)
	private BigDecimal descLogistica;
	
	@Footer(label = "Total desconto cota", type = FooterType.SUM,columnType = ColumnType.MOEDA)
	private BigDecimal descCota;

	public Integer getSequenciaMatriz() {
		return sequenciaMatriz;
	}

	public void setSequenciaMatriz(Integer sequenciaMatriz) {
		this.sequenciaMatriz = sequenciaMatriz;
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

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	public Integer getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigInteger getReparte() {
		return reparte;
	}

	public void setReparte(BigInteger reparte) {
		this.reparte = reparte;
	}

	public BigInteger getVendaEncalhe() {
		return vendaEncalhe;
	}

	public void setVendaEncalhe(BigInteger vendaEncalhe) {
		this.vendaEncalhe = vendaEncalhe;
	}

	public BigInteger getEncalhe() {
		return encalhe;
	}

	public void setEncalhe(BigInteger encalhe) {
		this.encalhe = encalhe;
	}

	public BigInteger getVenda() {
		return venda;
	}

	public void setVenda(BigInteger venda) {
		this.venda = venda;
	}

	public BigDecimal getVendaTotal() {
		return vendaTotal;
	}

	public void setVendaTotal(BigDecimal vendaTotal) {
		if(vendaTotal.toString().equalsIgnoreCase("0E-8")){
			this.vendaTotal = BigDecimal.ZERO;
		}else{
			this.vendaTotal = vendaTotal;
			this.vendaTotalFormatado = CurrencyUtil.formatarValor(vendaTotal);
		}
	}

	public BigDecimal getDescLogistica() {
		return descLogistica;
	}

	public void setDescLogistica(BigDecimal descLogistica) {
		this.descLogistica = descLogistica;
	}

	public BigDecimal getDescCota() {
		return descCota;
	}

	public void setDescCota(BigDecimal descCota) {
		this.descCota = descCota;
	}

	public String getVendaTotalFormatado() {
		return vendaTotalFormatado;
	}

	public void setVendaTotalFormatado(String vendaTotalFormatado) {
		this.vendaTotalFormatado = vendaTotalFormatado;
	}
	
	

}
