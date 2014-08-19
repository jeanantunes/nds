package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CurrencyUtil;

public class TotalizadorRankingSegmentoDTO {
	
	private BigInteger quantidadeRegistros;
	
	private BigDecimal totalFaturamentoCapa;
	
	private String totalFaturamentoCapaFormatado;

	public Integer getQuantidadeRegistros() {
		return quantidadeRegistros == null ? 0 : quantidadeRegistros.intValue();
	}

	public void setQuantidadeRegistros(BigInteger quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}

	public BigDecimal getTotalFaturamentoCapa() {
		return totalFaturamentoCapa;
	}

	public void setTotalFaturamentoCapa(BigDecimal totalFaturamentoCapa) {
		this.totalFaturamentoCapa = totalFaturamentoCapa;
		this.totalFaturamentoCapaFormatado = CurrencyUtil.formatarValor(totalFaturamentoCapa);
	}

	public String getTotalFaturamentoCapaFormatado() {
		return totalFaturamentoCapaFormatado;
	}

}
