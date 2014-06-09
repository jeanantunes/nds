package br.com.abril.nds.model.cadastro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ParametroCobrancaDistribuidor implements Serializable {
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5431807534550378915L;

	@Column(name = "DIAS_NEGOCIACAO")
	private Integer diasNegociacao;
	
	
	@Column(name = "COBRANCA_DATA_PROGRAMADA")
	private Integer cobrancaDataProgramada;


	public Integer getDiasNegociacao() {
		return diasNegociacao;
	}


	public void setDiasNegociacao(Integer diasNegociacao) {
		this.diasNegociacao = diasNegociacao;
	}


	public Integer getCobrancaDataProgramada() {
		return cobrancaDataProgramada;
	}


	public void setCobrancaDataProgramada(Integer cobrancaDataProgramada) {
		this.cobrancaDataProgramada = cobrancaDataProgramada;
	}
	

}
