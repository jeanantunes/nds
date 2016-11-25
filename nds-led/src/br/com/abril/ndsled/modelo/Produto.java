package br.com.abril.ndsled.modelo;

import java.math.BigDecimal;

public class Produto {
	private Integer codigoProduto;
	private Integer edicaoProduto;
	private String nomeProduto;
	private BigDecimal precoCusto;
	private BigDecimal precoCapa;
	private BigDecimal desconto;
	private Integer quantidade;
	private Long codigoBarras;
	private boolean distribuido;

	public Produto() {
		distribuido = false;
	}

	public Integer getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(Integer codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public Integer getEdicaoProduto() {
		return edicaoProduto;
	}

	public void setEdicaoProduto(Integer edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public BigDecimal getPrecoCusto() {
		return precoCusto;
	}

	public void setPrecoCusto(BigDecimal precoCusto) {
		this.precoCusto = precoCusto;
	}

	public BigDecimal getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(BigDecimal precoCapa) {
		this.precoCapa = precoCapa;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Long getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(Long codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public boolean isDistribuido() {
		return distribuido;
	}

	public void setDistribuido(boolean distribuido) {
		this.distribuido = distribuido;
	}

	@Override
	public String toString() {
		if (distribuido) {
			return "* " + codigoProduto + " - " + edicaoProduto + " - "
					+ nomeProduto + " - Reparte: " + quantidade + " Exs";
		} else {
			return codigoProduto + " - " + edicaoProduto + " - " + nomeProduto
					+ " - Reparte: " + quantidade + " Exs";
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoBarras == null) ? 0 : codigoBarras.hashCode());
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		result = prime * result
				+ ((desconto == null) ? 0 : desconto.hashCode());
		result = prime * result + (distribuido ? 1231 : 1237);
		result = prime * result
				+ ((edicaoProduto == null) ? 0 : edicaoProduto.hashCode());
		result = prime * result
				+ ((nomeProduto == null) ? 0 : nomeProduto.hashCode());
		result = prime * result
				+ ((precoCapa == null) ? 0 : precoCapa.hashCode());
		result = prime * result
				+ ((precoCusto == null) ? 0 : precoCusto.hashCode());
		result = prime * result
				+ ((quantidade == null) ? 0 : quantidade.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Produto))
			return false;
		Produto other = (Produto) obj;
		if (codigoBarras == null) {
			if (other.codigoBarras != null)
				return false;
		} else if (!codigoBarras.equals(other.codigoBarras))
			return false;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		if (desconto == null) {
			if (other.desconto != null)
				return false;
		} else if (!desconto.equals(other.desconto))
			return false;
		if (distribuido != other.distribuido)
			return false;
		if (edicaoProduto == null) {
			if (other.edicaoProduto != null)
				return false;
		} else if (!edicaoProduto.equals(other.edicaoProduto))
			return false;
		if (nomeProduto == null) {
			if (other.nomeProduto != null)
				return false;
		} else if (!nomeProduto.equals(other.nomeProduto))
			return false;
		if (precoCapa == null) {
			if (other.precoCapa != null)
				return false;
		} else if (!precoCapa.equals(other.precoCapa))
			return false;
		if (precoCusto == null) {
			if (other.precoCusto != null)
				return false;
		} else if (!precoCusto.equals(other.precoCusto))
			return false;
		if (quantidade == null) {
			if (other.quantidade != null)
				return false;
		} else if (!quantidade.equals(other.quantidade))
			return false;
		return true;
	}

}
