package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.movimentacao.MovimentoEstoque;

public interface MovimentoEstoqueRepository extends Repository<MovimentoEstoque, Long> {

	public List<MovimentoEstoque> obterListaMovimentoEstoque(); 
	
}
