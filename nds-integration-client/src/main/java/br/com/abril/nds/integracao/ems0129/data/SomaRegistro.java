package br.com.abril.nds.integracao.ems0129.data;

import java.math.BigDecimal;

public class SomaRegistro {
	
	private int qtdeRegistros = 0;
	
	private BigDecimal valorTotalBruto;
	
	private BigDecimal valorTotalDesconto;
	
	
	public SomaRegistro() {

		this.valorTotalBruto = BigDecimal.ZERO;
		this.valorTotalDesconto = BigDecimal.ZERO;
	}
	
	
	public int getQtdeRegistros() {
		return this.qtdeRegistros;
	}

	public void addQtdeRegistros(int qtdeRegistros) {
		this.qtdeRegistros += qtdeRegistros;
	}

	public BigDecimal getValorTotalBruto() {
		return this.valorTotalBruto.multiply(BigDecimal.valueOf(this.qtdeRegistros));
	}

	public void addValorTotalBruto(BigDecimal valorTotalBruto) {
		this.valorTotalBruto = this.valorTotalBruto.add(valorTotalBruto);
	}

	public BigDecimal getValorTotalDesconto() {
		return this.valorTotalDesconto.multiply(BigDecimal.valueOf(this.qtdeRegistros));
	}

	public void addValorTotalDesconto(BigDecimal valorTotalDesconto) {
		this.valorTotalDesconto = this.valorTotalDesconto.add(valorTotalDesconto);
	}
		
}
