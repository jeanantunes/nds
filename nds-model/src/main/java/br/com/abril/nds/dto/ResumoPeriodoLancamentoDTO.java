package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

public class ResumoPeriodoLancamentoDTO implements Serializable {

	private static final long serialVersionUID = 7487823794102857136L;

	private Date data;
	private String dataFormatada;
	private Long qtdeTitulos;
	private BigDecimal qtdeExemplares;
	private String qtdeExemplaresFormatada;
	private BigDecimal pesoTotal;
	private String pesoTotalFormatado;
	private BigDecimal valorTotal;
	private String valorTotalFormatado;
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
		if (data != null) {
			dataFormatada = DateUtil.formatarDataPTBR(data);
		}
	}
	
	public String getDataFormatada() {
		return dataFormatada;
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
		if (qtdeExemplares != null) {
			qtdeExemplaresFormatada = qtdeExemplares.toBigInteger().toString();
		}
	}
	
	public String getQtdeExemplaresFormatado() {
		return qtdeExemplaresFormatada;
	}
	
	public BigDecimal getPesoTotal() {
		return pesoTotal;
	}
	
	public void setPesoTotal(BigDecimal pesoTotal) {
		this.pesoTotal = pesoTotal;
		if (pesoTotal != null) {
			pesoTotalFormatado = pesoTotal.toString();
		}
	}
	
	public String getPesoTotalFormatado() {
		return pesoTotalFormatado;
	}
	
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
		if (valorTotal != null) {
			valorTotalFormatado = CurrencyUtil.formatarValor(valorTotal);
		}
	}
	
	public String getValorTotalFormatado() {
		return valorTotalFormatado;
	}
	
}
