package br.com.abril.nds.repository;

import br.com.abril.nds.model.planejamento.LancamentoParcial;

public interface LancamentoParcialRepository extends Repository<LancamentoParcial, Long>{

	LancamentoParcial obterLancamentoPorProdutoEdicao(Long idProdutoEdicao);

}
