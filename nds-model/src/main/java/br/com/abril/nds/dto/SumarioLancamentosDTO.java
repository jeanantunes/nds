package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;

public class SumarioLancamentosDTO implements Serializable {
	
	private static final long serialVersionUID = 741762215627505699L;

	private Long totalLancamentos;
	private BigDecimal valorTotalLancamentos;
	private String valorTotalFormatado;
	
	public Long getTotalLancamentos() {
		return totalLancamentos;
	}
	
	public void setTotalLancamentos(Long totalLancamentos) {
		this.totalLancamentos = totalLancamentos;
	}
	
	public BigDecimal getValorTotalLancamentos() {
		return valorTotalLancamentos;
	}
	
	public void setValorTotalLancamentos(BigDecimal valorTotalLancamentos) {
		this.valorTotalLancamentos = valorTotalLancamentos;
		if (valorTotalLancamentos != null) {
			valorTotalFormatado = CurrencyUtil.formatarValor(valorTotalLancamentos);
		} else {
			valorTotalFormatado = CurrencyUtil.formatarValor(BigDecimal.ZERO);
		}
	}
	
	public String getValorTotalFormatado() {
		return valorTotalFormatado;
	}

}
