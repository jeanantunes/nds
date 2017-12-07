package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.EdicaoBaseEstrategia;
import br.com.abril.nds.model.planejamento.Estrategia;

public interface EstrategiaRepository extends Repository<Estrategia, Long> {


	Estrategia buscarPorProdutoEdicaoId(ProdutoEdicao produtoEdicao);

	Estrategia buscarPorCodigoProdutoNumeroEdicao(String codigoProduto, Long numeroEdicao);

	EdicaoBaseEstrategia buscarBasePorCodigoProdutoNumEdicao(Long estrategiaID, String codigoProduto, Long numeroEdicao);
}
