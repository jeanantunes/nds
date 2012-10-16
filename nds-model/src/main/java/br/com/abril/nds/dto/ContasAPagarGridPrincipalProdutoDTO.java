package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ContasAPagarGridPrincipalProdutoDTO implements Serializable {

	private static final long serialVersionUID = -4141401352833129491L;

	private BigDecimal totalPagto;
	private BigDecimal totalDesconto;
	private BigDecimal valorLiquido;
	private List<ContasApagarConsultaPorProdutoDTO> grid;
	private Long totalGrid;
	
	
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
	public List<ContasApagarConsultaPorProdutoDTO> getGrid() {
		return grid;
	}
	public void setGrid(List<ContasApagarConsultaPorProdutoDTO> grid) {
		this.grid = grid;
	}
	public Long getTotalGrid() {
		return totalGrid;
	}
	public void setTotalGrid(Long totalGrid) {
		this.totalGrid = totalGrid;
	}
}
