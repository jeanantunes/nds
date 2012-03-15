package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.List;

public class ArquivoPagamentoBancoDTO {

	private List<PagamentoDTO> listaPagemento;
	
	private BigDecimal somaPagamentos;

	/**
	 * @return the listaPagemento
	 */
	public List<PagamentoDTO> getListaPagemento() {
		return listaPagemento;
	}

	/**
	 * @param listaPagemento the listaPagemento to set
	 */
	public void setListaPagemento(List<PagamentoDTO> listaPagemento) {
		this.listaPagemento = listaPagemento;
	}

	/**
	 * @return the somaPagamentos
	 */
	public BigDecimal getSomaPagamentos() {
		return somaPagamentos;
	}

	/**
	 * @param somaPagamentos the somaPagamentos to set
	 */
	public void setSomaPagamentos(BigDecimal somaPagamentos) {
		this.somaPagamentos = somaPagamentos;
	}

}
