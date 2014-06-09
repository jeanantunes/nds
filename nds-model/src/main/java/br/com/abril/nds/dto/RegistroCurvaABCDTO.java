package br.com.abril.nds.dto;

import java.math.BigDecimal;

import br.com.abril.nds.util.CurrencyUtil;

public abstract class RegistroCurvaABCDTO {

	private BigDecimal participacao;

	private BigDecimal participacaoAcumulada;

	private String participacaoFormatado;
	
	private String participacaoAcumuladaFormatado;
	
	public BigDecimal getParticipacao() {
		return participacao;
	}

	public void setParticipacao(BigDecimal participacao) {
		participacaoFormatado = CurrencyUtil.formatarValor(participacao);
		this.participacao = participacao;
	}

	public BigDecimal getParticipacaoAcumulada() {
		return participacaoAcumulada;
	}

	public void setParticipacaoAcumulada(BigDecimal participacaoAcumulada) {
		participacaoAcumuladaFormatado = CurrencyUtil.formatarValor(participacaoAcumulada);
		this.participacaoAcumulada = participacaoAcumulada;
	}

	public String getParticipacaoFormatado() {
		return participacaoFormatado;
	}

	public void setParticipacaoFormatado(String participacaoFormatado) {
		this.participacaoFormatado = participacaoFormatado;
	}

	public String getParticipacaoAcumuladaFormatado() {
		return participacaoAcumuladaFormatado;
	}

	public void setParticipacaoAcumuladaFormatado(
			String participacaoAcumuladaFormatado) {
		this.participacaoAcumuladaFormatado = participacaoAcumuladaFormatado;
	}
	
}
