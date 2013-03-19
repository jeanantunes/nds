package br.com.abril.nds.dto;

import br.com.abril.nds.util.ComponentesPDV;

public class BonificacaoDTO {
	
	private ComponentesPDV componente;
	private String elemento;
	private Double bonificacao;
	private Double reparteMinimo;
	private Boolean todasAsCotas;
	
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
	public Boolean getTodasAsCotas() {
		return todasAsCotas;
	}
	public void setTodasAsCotas(Boolean todasAsCotas) {
		this.todasAsCotas = todasAsCotas;
	}
	
}
