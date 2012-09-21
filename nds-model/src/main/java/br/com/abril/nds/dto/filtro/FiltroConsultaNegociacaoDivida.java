package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

public class FiltroConsultaNegociacaoDivida implements Serializable {

	private static final long serialVersionUID = -3251004810435516491L;
	
	private Integer numeroCota;
	
	private boolean lancamento;

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public boolean isLancamento() {
		return lancamento;
	}

	public void setLancamento(boolean lancamento) {
		this.lancamento = lancamento;
	}
	
	
	
}
