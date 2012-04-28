package br.com.abril.nds.service.exception;

import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;

public class TipoMovimentoEstoqueInexistenteException extends Exception {

	private static final long serialVersionUID = 5934315384413012367L;
	
	private GrupoMovimentoEstoque grupoMovimentoEstoque;
	
	public TipoMovimentoEstoqueInexistenteException(GrupoMovimentoEstoque grupoMovimentoEstoque) {
		super("Movimento de estoque do tipo " + grupoMovimentoEstoque.name() +  " não existe no banco de dados.");
		this.grupoMovimentoEstoque = grupoMovimentoEstoque;
	}

	public GrupoMovimentoEstoque getGrupoMovimentoEstoque() {
		return grupoMovimentoEstoque;
	}
}
