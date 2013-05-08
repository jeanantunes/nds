package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ResumoEstudoHistogramaPosAnaliseDTO {

	private BigInteger qtdCotasAtivas, 
					   qtdCotasRecebemReparte,
					   qtdCotasAdicionadasPelaComplementarAutomatica,
					   qtdReparteMinimoSugerido,
					   qtdReparteMinimoEstudo;
			
	private BigDecimal abrangenciaEstudo,
					   abrangenciaSugerida,
					   abrangenciaDeVenda,
					   qtdRepartePromocional;
	
	public BigInteger getQtdCotasAtivas() {
		return qtdCotasAtivas;
	}

	public void setQtdCotasAtivas(BigInteger qtdCotasAtivas) {
		this.qtdCotasAtivas = qtdCotasAtivas;
	}

	public BigInteger getQtdCotasRecebemReparte() {
		return qtdCotasRecebemReparte;
	}

	public void setQtdCotasRecebemReparte(BigInteger qtdCotasRecebemReparte) {
		this.qtdCotasRecebemReparte = qtdCotasRecebemReparte;
	}

	public BigInteger getQtdCotasAdicionadasPelaComplementarAutomatica() {
		return qtdCotasAdicionadasPelaComplementarAutomatica;
	}

	public void setQtdCotasAdicionadasPelaComplementarAutomatica(
			BigInteger qtdCotasAdicionadasPelaComplementarAutomatica) {
		this.qtdCotasAdicionadasPelaComplementarAutomatica = qtdCotasAdicionadasPelaComplementarAutomatica;
	}

	public BigInteger getQtdReparteMinimoSugerido() {
		return qtdReparteMinimoSugerido;
	}

	public void setQtdReparteMinimoSugerido(BigInteger qtdReparteMinimoSugerido) {
		this.qtdReparteMinimoSugerido = qtdReparteMinimoSugerido;
	}

	public BigInteger getQtdReparteMinimoEstudo() {
		return qtdReparteMinimoEstudo;
	}

	public void setQtdReparteMinimoEstudo(BigInteger qtdReparteMinimoEstudo) {
		this.qtdReparteMinimoEstudo = qtdReparteMinimoEstudo;
	}

	public BigDecimal getAbrangenciaEstudo() {
		return abrangenciaEstudo;
	}

	public void setAbrangenciaEstudo(BigDecimal abrangenciaEstudo) {
		this.abrangenciaEstudo = abrangenciaEstudo;
	}

	public BigDecimal getAbrangenciaSugerida() {
		return abrangenciaSugerida;
	}

	public void setAbrangenciaSugerida(BigDecimal abrangenciaSugerida) {
		this.abrangenciaSugerida = abrangenciaSugerida;
	}

	public BigDecimal getAbrangenciaDeVenda() {
		return abrangenciaDeVenda;
	}

	public void setAbrangenciaDeVenda(BigDecimal abrangenciaDeVenda) {
		this.abrangenciaDeVenda = abrangenciaDeVenda;
	}

	public BigDecimal getQtdRepartePromocional() {
		return qtdRepartePromocional;
	}

	public void setQtdRepartePromocional(BigDecimal qtdRepartePromocional) {
		this.qtdRepartePromocional = qtdRepartePromocional;
	}
	
}
