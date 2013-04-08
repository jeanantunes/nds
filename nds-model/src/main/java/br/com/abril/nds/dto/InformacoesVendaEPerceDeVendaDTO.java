package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/*
 * Classe utilizada no detalhamento do produto. 
 * 
 * Funcionalidade: total de venda e porcentagem de venda do Reparte da edição.
 * 
 */

public class InformacoesVendaEPerceDeVendaDTO implements Serializable {

	private static final long serialVersionUID = 6561927362578562028L;
	
	private BigInteger totalVenda;
	private BigInteger porcentagemDeVenda;

	public BigInteger getTotalVenda() {
		return totalVenda;
	}
	public void setTotalVenda(BigInteger totalVenda) {
		this.totalVenda = totalVenda;
	}
	public BigInteger getPorcentagemDeVenda() {
		return porcentagemDeVenda;
	}
	public void setPorcentagemDeVenda(BigInteger porcentagemDeVenda) {
		this.porcentagemDeVenda = porcentagemDeVenda;
	}
}
