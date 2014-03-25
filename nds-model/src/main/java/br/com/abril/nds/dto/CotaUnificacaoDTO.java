package br.com.abril.nds.dto;

import java.util.List;

import br.com.abril.nds.client.vo.CotaVO;

public class CotaUnificacaoDTO {

	private Integer numeroCota;
	
	private List<CotaVO> cotas;

	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public List<CotaVO> getCotas() {
		return cotas;
	}

	public void setCotas(List<CotaVO> cotas) {
		this.cotas = cotas;
	}
}
