package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ItemNotaFiscalPendenteDTO implements Serializable {

	private static final long serialVersionUID = 4238499960020175179L;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigoProduto;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private Long numeroEdicao;
	
	@Export(label = "Qtde. Info", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private BigInteger qtdInformada;
	
	@Export(label = "Qtde. Recebida", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private BigInteger qtdRecebida;
	
	
	private BigDecimal precoCapa; 
	private BigDecimal precoDesconto; 
	private BigDecimal totalDoItem;
	
	private Date dataConferenciaEncalhe;
	private Date dataChamadaEncalhe;
	
	private String dia;
	
	private String precoCapaFormatado;
	private String precoDescontoFormatado;
	private String totalDoItemFormatado;
	
	private BigDecimal desconto;
	
	public ItemNotaFiscalPendenteDTO() {}
	
	public ItemNotaFiscalPendenteDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, BigInteger qtdInformada,
			BigInteger qtdRecebida, BigDecimal precoCapa,
			BigDecimal precoDesconto, BigDecimal totalDoItem, Date dataConferenciaEncalhe, Date dataChamadaEncalhe) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.qtdInformada = qtdInformada;
		this.qtdRecebida = qtdRecebida;
		this.setPrecoCapa(precoCapa);
		this.setPrecoDesconto(precoDesconto);
		this.setTotalDoItem(totalDoItem);
		this.setDataConferenciaEncalhe(dataConferenciaEncalhe);
		this.setDataChamadaEncalhe(dataChamadaEncalhe);
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
	
	@Export(label = "Dia", alignment=Alignment.CENTER, exhibitionOrder = 4)
	public String getDia() {
		return dia;
	}
	
	public void setDia(String dia) {
		this.dia = dia;
	}

	public BigInteger getQtdInformada() {
		return qtdInformada;
	}

	public void setQtdInformada(BigInteger qtdInformada) {
		this.qtdInformada = qtdInformada;
	}

	public BigInteger getQtdRecebida() {
		return qtdRecebida;
	}

	public void setQtdRecebida(BigInteger qtdRecebida) {
		this.qtdRecebida = qtdRecebida;
	}

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
		if (precoCapa != null) {
			precoCapaFormatado = CurrencyUtil.formatarValor(precoCapa);
		}
	}
	
	@Export(label = "Preço Capa R$", alignment=Alignment.CENTER, exhibitionOrder = 7)
	public String getprecoCapaFormatado() {
		return precoCapaFormatado;
	}

	public BigDecimal getPrecoDesconto() {
		return precoDesconto;
	}

	public void setPrecoDesconto(BigDecimal precoDesconto) {
		this.precoDesconto = precoDesconto;
		if (precoDesconto != null) {
			precoDescontoFormatado = CurrencyUtil.formatarValor(precoDesconto);
		}
	}
	

	@Export(label = "Preço Desc R$", alignment=Alignment.CENTER, exhibitionOrder = 8)
	public String getprecoDescontoFormatado() {
		return precoDescontoFormatado;
	}

	public BigDecimal getTotalDoItem() {
		return totalDoItem;
	}

	public void setTotalDoItem(BigDecimal totalDoItem) {
		this.totalDoItem = totalDoItem;
		if (totalDoItem != null) {
			totalDoItemFormatado = CurrencyUtil.formatarValor(totalDoItem);
		}
		
	}
	
	@Export(label = "Total R$", alignment=Alignment.CENTER, exhibitionOrder = 9)
	public String getTotalDoItemFormatado() {
		return totalDoItemFormatado;
	}

	public Date getDataConferenciaEncalhe() {
		return dataConferenciaEncalhe;
	}

	public void setDataConferenciaEncalhe(Date dataConferenciaEncalhe) {
		Long qtdDiferencaDias = DateUtil.obterDiferencaDias(getDataConferenciaEncalhe(), getDataChamadaEncalhe());
		this.dia = qtdDiferencaDias.toString() + "° dia";
		this.dataConferenciaEncalhe = dataConferenciaEncalhe;
	}

	public Date getDataChamadaEncalhe() {
		return dataChamadaEncalhe;
	}

	public void setDataChamadaEncalhe(Date dataChamadaEncalhe) {
		Long qtdDiferencaDias = DateUtil.obterDiferencaDias(getDataConferenciaEncalhe(), getDataChamadaEncalhe());
		this.dia = qtdDiferencaDias.toString() + "° dia";
		this.dataChamadaEncalhe = dataChamadaEncalhe;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	
}
