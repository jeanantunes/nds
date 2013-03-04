package br.com.abril.nds.dto;

import java.io.Serializable;

public class HistogramaFaixaVendaDTO implements Serializable {

	private String faixaReparte;
	private Long repTotal;
	private Long repMedio;
	private Long vdaTotal;
	private Long vdaMedio;
	private Long percVenda;
	private Long encalheMedio;
	private Long partReparte;
	private Long partVenda;
	private Long qtdeCotas;
	private Long cotasEsmagadas;
	private Long vendaEsmagadas;
	
	public String getFaixaReparte() {
		return faixaReparte;
	}
	public void setFaixaReparte(String faixaReparte) {
		this.faixaReparte = faixaReparte;
	}
	public Long getRepTotal() {
		return repTotal;
	}
	public void setRepTotal(Long repTotal) {
		this.repTotal = repTotal;
	}
	public Long getRepMedio() {
		return repMedio;
	}
	public void setRepMedio(Long repMedio) {
		this.repMedio = repMedio;
	}
	public Long getVdaTotal() {
		return vdaTotal;
	}
	public void setVdaTotal(Long vdaTotal) {
		this.vdaTotal = vdaTotal;
	}
	public Long getVdaMedio() {
		return vdaMedio;
	}
	public void setVdaMedio(Long vdaMedio) {
		this.vdaMedio = vdaMedio;
	}
	public Long getPercVenda() {
		return percVenda;
	}
	public void setPercVenda(Long percVenda) {
		this.percVenda = percVenda;
	}
	public Long getEncalheMedio() {
		return encalheMedio;
	}
	public void setEncalheMedio(Long encalheMedio) {
		this.encalheMedio = encalheMedio;
	}
	public Long getPartReparte() {
		return partReparte;
	}
	public void setPartReparte(Long partReparte) {
		this.partReparte = partReparte;
	}
	public Long getPartVenda() {
		return partVenda;
	}
	public void setPartVenda(Long partVenda) {
		this.partVenda = partVenda;
	}
	public Long getQtdeCotas() {
		return qtdeCotas;
	}
	public void setQtdeCotas(Long qtdeCotas) {
		this.qtdeCotas = qtdeCotas;
	}
	public Long getCotasEsmagadas() {
		return cotasEsmagadas;
	}
	public void setCotasEsmagadas(Long cotasEsmagadas) {
		this.cotasEsmagadas = cotasEsmagadas;
	}
	public Long getVendaEsmagadas() {
		return vendaEsmagadas;
	}
	public void setVendaEsmagadas(Long vendaEsmagadas) {
		this.vendaEsmagadas = vendaEsmagadas;
	}
	
}

