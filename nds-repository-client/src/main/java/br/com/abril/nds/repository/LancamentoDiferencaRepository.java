package br.com.abril.nds.repository;

import br.com.abril.nds.model.estoque.LancamentoDiferenca;

public interface LancamentoDiferencaRepository extends Repository<LancamentoDiferenca, Long> {

	LancamentoDiferenca obterLancamentoDiferencaEstoqueCota(Long idMovimentoEstoqueCota);
	
	LancamentoDiferenca obterLancamentoDiferencaEstoque(Long idMovimentoEstoque);
	
}
