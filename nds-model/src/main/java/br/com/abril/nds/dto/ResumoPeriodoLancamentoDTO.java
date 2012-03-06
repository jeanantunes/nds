package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ResumoPeriodoLancamentoDTO implements Serializable {

	private static final long serialVersionUID = 7487823794102857136L;

	private Date data;
	private Long qtdeTitulos;
	private BigDecimal qtdeExemplares;
	private BigDecimal pesoTotal;
	private BigDecimal valorTotal;
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public Long getQtdeTitulos() {
		return qtdeTitulos;
	}
	
	public void setQtdeTitulos(Long qtdeTitulos) {
		this.qtdeTitulos = qtdeTitulos;
	}
	
	public BigDecimal getQtdeExemplares() {
		return qtdeExemplares;
	}
	
	public void setQtdeExemplares(BigDecimal qtdeExemplares) {
		this.qtdeExemplares = qtdeExemplares;
	}
	
	public BigDecimal getPesoTotal() {
		return pesoTotal;
	}
	
	public void setPesoTotal(BigDecimal pesoTotal) {
		this.pesoTotal = pesoTotal;
	}
	
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
}
