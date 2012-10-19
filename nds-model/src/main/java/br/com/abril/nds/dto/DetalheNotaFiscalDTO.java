package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class DetalheNotaFiscalDTO implements Serializable {

	private static final long serialVersionUID = -2765492737688529647L;

	private List<DetalheItemNotaFiscalDTO> itensDetalhados;
	
	private BigInteger totalExemplares;
	
	private BigDecimal valorTotalSumarizado;

	/**
	 * @return the itensDetalhados
	 */
	public List<DetalheItemNotaFiscalDTO> getItensDetalhados() {
		return itensDetalhados;
	}

	/**
	 * @param itensDetalhados the itensDetalhados to set
	 */
	public void setItensDetalhados(List<DetalheItemNotaFiscalDTO> itensDetalhados) {
		this.itensDetalhados = itensDetalhados;
	}

	public BigInteger getTotalExemplares() {
		return totalExemplares;
	}

	public void setTotalExemplares(BigInteger totalExemplares) {
		this.totalExemplares = totalExemplares;
	}

	/**
	 * @return the valorTotalSumarizado
	 */
	public BigDecimal getValorTotalSumarizado() {
		return valorTotalSumarizado;
	}

	/**
	 * @param valorTotalSumarizado the valorTotalSumarizado to set
	 */
	public void setValorTotalSumarizado(BigDecimal valorTotalSumarizado) {
		this.valorTotalSumarizado = valorTotalSumarizado;
	}
}
