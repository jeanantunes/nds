package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ContasApagarConsultaPorDistribuidorDTO implements Serializable {

	private static final long serialVersionUID = -3369681441745266318L;
	
	public ContasApagarConsultaPorDistribuidorDTO(){}
	
	public ContasApagarConsultaPorDistribuidorDTO(Date dataMovimento, BigDecimal consignado, BigDecimal suplementacaoEntrada,
			BigDecimal suplementacaoSaida, BigDecimal faltas, BigDecimal sobras, BigDecimal perdas, BigDecimal ganhos){
		
		this.data = dataMovimento;
		this.consignado = consignado;
		
		if (suplementacaoEntrada == null){
			
			suplementacaoEntrada = BigDecimal.ZERO;
		}
		
		if (suplementacaoSaida == null){
			
			suplementacaoSaida = BigDecimal.ZERO;
		}
		
		this.suplementacao = suplementacaoEntrada.subtract(suplementacaoSaida);
		
		if (faltas == null){
			
			faltas = BigDecimal.ZERO;
		}
		
		if (sobras == null){
			
			sobras = BigDecimal.ZERO;
		}
		
		this.faltasSobras = sobras.subtract(faltas);
		
		if (perdas == null){
			
			perdas = BigDecimal.ZERO;
		}
		
		if (ganhos == null){
			
			ganhos = BigDecimal.ZERO;
		}
		
		this.debitoCredito = ganhos.subtract(perdas);
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
