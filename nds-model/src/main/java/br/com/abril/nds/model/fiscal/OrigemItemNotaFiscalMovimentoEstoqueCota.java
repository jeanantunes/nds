package br.com.abril.nds.model.fiscal;

import java.util.List;

import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;

public class OrigemItemNotaFiscalMovimentoEstoqueCota {

	private List<MovimentoEstoqueCota> listaMovimentoEstoqueCotas;

	public List<MovimentoEstoqueCota> getListaMovimentoEstoqueCotas() {
		return listaMovimentoEstoqueCotas;
	}

	public void setListaMovimentoEstoqueCotas(
			List<MovimentoEstoqueCota> listaMovimentoEstoqueCotas) {
		this.listaMovimentoEstoqueCotas = listaMovimentoEstoqueCotas;
	}
}
