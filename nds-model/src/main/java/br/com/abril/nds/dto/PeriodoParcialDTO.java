package br.com.abril.nds.dto;

import java.io.Serializable;

public class PeriodoParcialDTO implements Serializable {

	private static final long serialVersionUID = 7240165519168307608L;
	
	private String periodo;
	private String dataLancamento;
	private String dataRecolhimento;
	private Integer reparte;
	private Integer encalhe;
	private Integer vendas;
	private Integer vendaAcumulada;
	private String percVenda;
	
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	public String getDataRecolhimento() {
		return dataRecolhimento;
	}
	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	public Integer getReparte() {
		return reparte;
	}
	public void setReparte(Integer reparte) {
		this.reparte = reparte;
	}
	public Integer getEncalhe() {
		return encalhe;
	}
	public void setEncalhe(Integer encalhe) {
		this.encalhe = encalhe;
	}
	public Integer getVendas() {
		return vendas;
	}
	public void setVendas(Integer vendas) {
		this.vendas = vendas;
	}
	public Integer getVendaAcumulada() {
		return vendaAcumulada;
	}
	public void setVendaAcumulada(Integer vendaAcumulada) {
		this.vendaAcumulada = vendaAcumulada;
	}
	public String getPercVenda() {
		return percVenda;
	}
	public void setPercVenda(String percVenda) {
		this.percVenda = percVenda;
	}
	
		
}
