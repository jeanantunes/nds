package br.com.abril.nds.repository;

import br.com.abril.nds.dto.VisaoEstoqueDTO;


public interface VisaoEstoqueRepository {

	VisaoEstoqueDTO obterLancamento();
	VisaoEstoqueDTO obterLancamentoJuramentado();
	VisaoEstoqueDTO obterSuplementar();
	VisaoEstoqueDTO obterRecolhimento();
	VisaoEstoqueDTO obterProdutosDanificados();
}
