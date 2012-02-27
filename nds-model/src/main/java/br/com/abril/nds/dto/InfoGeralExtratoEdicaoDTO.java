package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class InfoGeralExtratoEdicaoDTO implements Serializable {

	private BigDecimal saldoTotalExtratoEdicao;
	
	private List<ExtratoEdicaoDTO> listaExtratoEdicao;

	public BigDecimal getSaldoTotalExtratoEdicao() {
		return saldoTotalExtratoEdicao;
	}

	public void setSaldoTotalExtratoEdicao(BigDecimal saldoTotalExtratoEdicao) {
		this.saldoTotalExtratoEdicao = saldoTotalExtratoEdicao;
	}

	public List<ExtratoEdicaoDTO> getListaExtratoEdicao() {
		return listaExtratoEdicao;
	}

	public void setListaExtratoEdicao(List<ExtratoEdicaoDTO> listaExtratoEdicao) {
		this.listaExtratoEdicao = listaExtratoEdicao;
	}
	
	
}
