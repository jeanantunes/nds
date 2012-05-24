package br.com.abril.nds.client.vo;

import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;

public abstract class RegistroCurvaABC {

	@Export(label = "Participação")
	private BigDecimal participacao;

	@Export(label = "Participação Acumulada")
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
