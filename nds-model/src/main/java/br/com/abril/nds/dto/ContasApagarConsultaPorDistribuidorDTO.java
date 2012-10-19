package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ContasApagarConsultaPorDistribuidorDTO implements Serializable {

	private static final long serialVersionUID = -3369681441745266318L;
	
	public ContasApagarConsultaPorDistribuidorDTO(){}
	
	public ContasApagarConsultaPorDistribuidorDTO(Date dataMovimento, BigDecimal consignado, BigDecimal encalhe, 
			BigDecimal suplementacao, BigDecimal faltasSobras, BigDecimal perdasGanhos){
		
		this.data = dataMovimento;
		this.consignado = consignado;
		this.encalhe = encalhe;
		
		if (consignado == null){
			
			consignado = BigDecimal.ZERO;
		}
		
		if (encalhe == null){
			
			encalhe = BigDecimal.ZERO;
		}
		
		this.venda = consignado.subtract(encalhe);
		
		this.suplementacao = suplementacao;
		
		this.faltasSobras = faltasSobras;
		
		this.debitoCredito = perdasGanhos;
		
		this.saldo = consignado.subtract(this.encalhe == null ? BigDecimal.ZERO : this.encalhe)
				.subtract(this.faltasSobras == null ? BigDecimal.ZERO : this.faltasSobras)
				.subtract(this.debitoCredito == null ? BigDecimal.ZERO : this.debitoCredito);
	}
	
	private Date data;
	private BigDecimal consignado;
	private BigDecimal suplementacao;
	private BigDecimal encalhe;
	private BigDecimal venda;
	private BigDecimal faltasSobras;
	private BigDecimal debitoCredito;
	private BigDecimal saldo;
	
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public BigDecimal getConsignado() {
		return consignado;
	}
	public void setConsignado(BigDecimal consignado) {
		this.consignado = consignado;
	}
	public BigDecimal getSuplementacao() {
		return suplementacao;
	}
	public void setSuplementacao(BigDecimal suplementacao) {
		this.suplementacao = suplementacao;
	}
	public BigDecimal getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(BigDecimal encalhe) {
		this.encalhe = encalhe;
	}
	public BigDecimal getVenda() {
		return venda;
	}
	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}
	public BigDecimal getFaltasSobras() {
		return faltasSobras;
	}
	public void setFaltasSobras(BigDecimal faltasSobras) {
		this.faltasSobras = faltasSobras;
	}
	public BigDecimal getDebitoCredito() {
		return debitoCredito;
	}
	public void setDebitoCredito(BigDecimal debitoCredito) {
		this.debitoCredito = debitoCredito;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
}
