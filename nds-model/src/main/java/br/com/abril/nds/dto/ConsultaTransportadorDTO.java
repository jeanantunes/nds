package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.model.cadastro.Transportador;

public class ConsultaTransportadorDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1522238394042825831L;

	private List<Transportador> transportadores;
	
	private Long quantidadeRegistros;

	public List<Transportador> getTransportadores() {
		return transportadores;
	}

	public void setTransportadores(List<Transportador> transportadores) {
		this.transportadores = transportadores;
	}

	public Long getQuantidadeRegistros() {
		return quantidadeRegistros;
	}

	public void setQuantidadeRegistros(Long quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}
}