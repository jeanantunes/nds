package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.ComponentesPDV;

public class BonificacaoDTO {
	
	private ComponentesPDV componente;
	private String elemento;
	private Double bonificacao;
	private Double reparteMinimo;
	private boolean todasAsCotas;
	
	public void setBonificacaoBigDecimal(BigDecimal bonificacao) {
	    this.bonificacao = bonificacao.doubleValue();
	}
	
	public BigDecimal getBonificacaoBigDecimal() {
	    return BigDecimal.valueOf(bonificacao);
	}
	
	public void setReparteMinimoBigInteger(BigInteger reparteMinimo) {
	    this.reparteMinimo = reparteMinimo.doubleValue();
	}
	
	public BigInteger getReparteMinimoBigInteger() {
	    return BigInteger.valueOf(reparteMinimo.longValue());
	}
	
	public ComponentesPDV getComponente() {
		return componente;
	}
	public void setComponente(ComponentesPDV componente) {
		this.componente = componente;
	}
	public String getElemento() {
		return elemento;
	}
	public void setElemento(String elemento) {
		this.elemento = elemento;
	}
	public Double getBonificacao() {
		return bonificacao;
	}
	public void setBonificacao(Double bonificacao) {
		this.bonificacao = bonificacao;
	}
	public Double getReparteMinimo() {
		return reparteMinimo;
	}
	public void setReparteMinimo(Double reparteMinimo) {
		this.reparteMinimo = reparteMinimo;
	}
	public boolean isTodasAsCotas() {
		return todasAsCotas;
	}
	public void setTodasAsCotas(boolean todasAsCotas) {
		this.todasAsCotas = todasAsCotas;
	}
	
}
