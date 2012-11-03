package br.com.abril.nds.dto;

import java.math.BigDecimal;

public class ContasAPagarGridPrincipalFornecedorDTO extends FlexiGridDTO<ContasApagarConsultaPorDistribuidorDTO> {

	private static final long serialVersionUID = 6376500344584101700L;

	private BigDecimal totalBruto;
	private BigDecimal totalDesconto;
	private BigDecimal saldo;
	
	
	public BigDecimal getTotalBruto() {
		return totalBruto;
	}
	public void setTotalBruto(BigDecimal totalBruto) {
		this.totalBruto = totalBruto;
	}
	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}
	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
}
