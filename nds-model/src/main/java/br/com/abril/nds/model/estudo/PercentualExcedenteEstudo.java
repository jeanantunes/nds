package br.com.abril.nds.model.estudo;

import java.math.BigDecimal;

public class PercentualExcedenteEstudo {

	private String eficiencia;
	private BigDecimal pdv;
	private BigDecimal venda;
	
	public String getEficiencia() {
		return eficiencia;
	}
	public void setEficiencia(String eficiencia) {
		this.eficiencia = eficiencia;
	}
	public BigDecimal getPdv() {
		return pdv;
	}
	public void setPdv(BigDecimal pdv) {
		this.pdv = pdv;
	}
	public BigDecimal getVenda() {
		return venda;
	}
	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}
	@Override
	public String toString() {
		return "eficiencia=" + eficiencia + ", pdv="
				+ pdv + ", venda=" + venda;
	}
}
