package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ResumoEncalheFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 8721756404722658789L;
	
	private BigDecimal totalLogico;
	
	private BigDecimal totalFisico;
	
	private BigDecimal totalJuramentado;
	
	private BigDecimal venda;

	public BigDecimal getTotalLogico() {
		return totalLogico;
	}

	public void setTotalLogico(BigDecimal totalLogico) {
		this.totalLogico = totalLogico;
	}

	public BigDecimal getTotalFisico() {
		return totalFisico;
	}

	public void setTotalFisico(BigDecimal totalFisico) {
		this.totalFisico = totalFisico;
	}

	public BigDecimal getTotalJuramentado() {
		return totalJuramentado;
	}

	public void setTotalJuramentado(BigDecimal totalJuramentado) {
		this.totalJuramentado = totalJuramentado;
	}

	public BigDecimal getVenda() {
		return venda;
	}

	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}
	
	


}
