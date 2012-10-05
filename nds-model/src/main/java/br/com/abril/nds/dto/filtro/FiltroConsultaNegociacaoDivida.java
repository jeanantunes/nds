package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.vo.PaginacaoVO;

public class FiltroConsultaNegociacaoDivida implements Serializable {

	private static final long serialVersionUID = -3251004810435516491L;
	
	private PaginacaoVO paginacaoVO;
	
	private Integer numeroCota;
	
	private boolean lancamento;

	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}

	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}

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