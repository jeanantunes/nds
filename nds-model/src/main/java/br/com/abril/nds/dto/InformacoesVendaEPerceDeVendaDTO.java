package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/*
 * Classe utilizada no detalhamento do produto. 
 * 
 * Funcionalidade: total de venda e porcentagem de venda do Reparte da edição.
 * 
 */

public class InformacoesVendaEPerceDeVendaDTO implements Serializable {

	private static final long serialVersionUID = 6561927362578562028L;
	
	private BigDecimal totalVenda;
	private BigDecimal porcentagemDeVenda;
	
	public BigDecimal getTotalVenda() {
		return totalVenda;
	}
	public void setTotalVenda(BigDecimal totalVenda) {
		this.totalVenda = totalVenda;
	}
	public BigDecimal getPorcentagemDeVenda() {
		return porcentagemDeVenda;
	}
	public void setPorcentagemDeVenda(BigDecimal porcentagemDeVenda) {
		this.porcentagemDeVenda = porcentagemDeVenda;
	}


}
