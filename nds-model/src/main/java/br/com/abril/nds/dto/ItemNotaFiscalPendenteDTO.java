package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class ItemNotaFiscalPendenteDTO implements Serializable {

	private static final long serialVersionUID = 4238499960020175179L;
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigoProduto;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment=Alignment.CENTER, exhibitionOrder = 3)
	private Long numeroEdicao;
	
	@Export(label = "Dia", alignment=Alignment.CENTER, exhibitionOrder = 4)
	private Integer dia;
	
	@Export(label = "Qtde. Info", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private BigDecimal qtdInformada;
	
	@Export(label = "Qtde. Recebida", alignment=Alignment.CENTER, exhibitionOrder = 6)
	private BigDecimal qtdRecebida;
	
	
	private BigDecimal precoCapa; 
	private BigDecimal precoDesconto; 
	private BigDecimal totalDoItem;
	
	
	private String precoCapaFormatado;
	private String precoDescontoFormatado;
	private String totalDoItemFormatado;
	
	public ItemNotaFiscalPendenteDTO() {}
	
	public ItemNotaFiscalPendenteDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, Integer dia, BigDecimal qtdInformada,
			BigDecimal qtdRecebida, BigDecimal precoCapa,
			BigDecimal precoDesconto, BigDecimal totalDoItem) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.dia = dia;
		this.qtdInformada = qtdInformada;
		this.qtdRecebida = qtdRecebida;
		this.precoCapa = precoCapa;
		this.precoDesconto = precoDesconto;
		this.totalDoItem = totalDoItem;
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

	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	public BigDecimal getQtdInformada() {
		return qtdInformada;
	}

	public void setQtdInformada(BigDecimal qtdInformada) {
		this.qtdInformada = qtdInformada;
	}

	public BigDecimal getQtdRecebida() {
		return qtdRecebida;
	}

	public void setQtdRecebida(BigDecimal qtdRecebida) {
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
	

	
	
}
