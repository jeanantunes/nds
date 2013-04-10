package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estrategia;

public interface EstrategiaRepository extends Repository<Estrategia, Long> {


	Estrategia buscarPorProdutoEdicao(ProdutoEdicao produtoEdicao);

}
