package br.com.abril.nds.client.vo;

import java.math.BigDecimal;

public abstract class RegistroCurvaABC {

	private BigDecimal participacao;

	private BigDecimal participacaoAcumulada;

	public BigDecimal getParticipacao() {
		return participacao;
	}

	public void setParticipacao(BigDecimal participacao) {
		this.participacao = participacao;
	}

	public BigDecimal getParticipacaoAcumulada() {
		return participacaoAcumulada;
	}

	public void setParticipacaoAcumulada(BigDecimal participacaoAcumulada) {
		this.participacaoAcumulada = participacaoAcumulada;
	}

}
