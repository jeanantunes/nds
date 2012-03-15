package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class InfoContagemDevolucaoDTO implements Serializable {

	private List<ContagemDevolucaoDTO> listaContagemDevolucao;
	
	private BigDecimal valorTotalGeral;

	public InfoContagemDevolucaoDTO() {}
	
	
	public InfoContagemDevolucaoDTO(
			List<ContagemDevolucaoDTO> listaContagemDevolucao,
			BigDecimal valorTotalGeral) {
		super();
		this.listaContagemDevolucao = listaContagemDevolucao;
		this.valorTotalGeral = valorTotalGeral;
	}


	public List<ContagemDevolucaoDTO> getListaContagemDevolucao() {
		return listaContagemDevolucao;
	}

	public void setListaContagemDevolucao(
			List<ContagemDevolucaoDTO> listaContagemDevolucao) {
		this.listaContagemDevolucao = listaContagemDevolucao;
	}

	public BigDecimal getValorTotalGeral() {
		return valorTotalGeral;
	}

	public void setValorTotalGeral(BigDecimal valorTotalGeral) {
		this.valorTotalGeral = valorTotalGeral;
	}
	
	
}
