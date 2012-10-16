package br.com.abril.nds.dto;

import java.math.BigDecimal;

public class ContasAPagarGridPrincipalProdutoDTO extends FlexiGridDTO<ContasApagarConsultaPorProdutoDTO> {

	private static final long serialVersionUID = -4141401352833129491L;

	private BigDecimal totalPagto;
	private BigDecimal totalDesconto;
	private BigDecimal valorLiquido;
	
	
	public BigDecimal getTotalPagto() {
		return totalPagto;
	}
	public void setTotalPagto(BigDecimal totalPagto) {
		this.totalPagto = totalPagto;
	}
	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}
	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}
	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}
	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}
}
