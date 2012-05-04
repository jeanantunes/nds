package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.model.cadastro.Fiador;

public class ConsultaFiadorDTO implements Serializable {

	private static final long serialVersionUID = -2552129998312069689L;
	
	private List<Fiador> listaFiadores;
	
	private Long quantidadeRegistros;

	public List<Fiador> getListaFiadores() {
		return listaFiadores;
	}

	public void setListaFiadores(List<Fiador> listaFiadores) {
		this.listaFiadores = listaFiadores;
	}

	public Long getQuantidadeRegistros() {
		return quantidadeRegistros;
	}

	public void setQuantidadeRegistros(Long quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}
}