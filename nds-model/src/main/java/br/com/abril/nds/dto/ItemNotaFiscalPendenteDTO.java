package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ItemNotaFiscalPendenteDTO implements Serializable {

	private static final long serialVersionUID = 4238499960020175179L;
	
	private String codigoProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private String dia;
	private BigDecimal qtdInformada; 
	private BigDecimal qtdRecebida; 
	private BigDecimal precoCapa; 
	private BigDecimal precoDesconto; 
	private BigDecimal totalDoItem;
	
	public ItemNotaFiscalPendenteDTO() {}
	
	public ItemNotaFiscalPendenteDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, String dia, BigDecimal qtdInformada,
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

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
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
	}

	public BigDecimal getPrecoDesconto() {
		return precoDesconto;
	}

	public void setPrecoDesconto(BigDecimal precoDesconto) {
		this.precoDesconto = precoDesconto;
	}

	public BigDecimal getTotalDoItem() {
		return totalDoItem;
	}

	public void setTotalDoItem(BigDecimal totalDoItem) {
		this.totalDoItem = totalDoItem;
	}

	
	
}
