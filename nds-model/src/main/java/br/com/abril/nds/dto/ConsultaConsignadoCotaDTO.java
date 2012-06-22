package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ConsultaConsignadoCotaDTO implements Serializable {
	
	private static final long serialVersionUID = 3076443242075398273L;
	
	private String codigoProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private String nomeFornecedor;
	private Date dataLancamento;
	private BigDecimal precoCapa;
	private BigDecimal precoDesconto;
	private BigDecimal reparte;
	private BigDecimal total;
	private BigDecimal totalDesconto;
	
	
	public ConsultaConsignadoCotaDTO() {}

	public ConsultaConsignadoCotaDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao, String nomeFornecedor, Date dataLancamento,
			BigDecimal precoCapa, BigDecimal precoDesconto, BigDecimal reparte,
			BigDecimal total, BigDecimal totalDesconto) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.nomeFornecedor = nomeFornecedor;
		this.dataLancamento = dataLancamento;
		this.precoCapa = precoCapa;
		this.precoDesconto = precoDesconto;
		this.reparte = reparte;
		this.total = total;
		this.totalDesconto = totalDesconto;
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

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
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

	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	
}
