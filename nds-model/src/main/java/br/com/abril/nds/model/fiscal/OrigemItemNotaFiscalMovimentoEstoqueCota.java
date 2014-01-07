package br.com.abril.nds.model.fiscal;

import java.util.List;

import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;

public class OrigemItemNotaFiscalMovimentoEstoqueCota extends OrigemItemNotaFiscal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1646731362475540050L;
	
	OrigemItem origem = OrigemItem.MOVIMENTO_ESTOQUE_COTA;
	
	private List<MovimentoEstoqueCota> listaMovimentoEstoqueCotas;

	public List<MovimentoEstoqueCota> getListaMovimentoEstoqueCotas() {
		return listaMovimentoEstoqueCotas;
	}

	public void setListaMovimentoEstoqueCotas(List<MovimentoEstoqueCota> listaMovimentoEstoqueCotas) {
		this.listaMovimentoEstoqueCotas = listaMovimentoEstoqueCotas;
	}
	
	public OrigemItem getOrigem() {
		return origem;
	}

	public String obterOrigemItemNotaFiscal() {
		return this.getClass().getName();
	};
	
}