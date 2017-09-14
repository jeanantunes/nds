package br.com.abril.nds.service;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.EdicaoBaseEstrategia;
import br.com.abril.nds.model.planejamento.Estrategia;
import org.springframework.transaction.annotation.Transactional;

public interface EstrategiaService {

    void inserirEstrategia(Estrategia estrategia);
    Estrategia buscarPorProdutoEdicaoId(ProdutoEdicao produtoEdicao);

    Estrategia buscarPorCodigoProdutoNumeroEdicao(String codigoProduto, Long numeroEdicao);

    @Transactional
    EdicaoBaseEstrategia buscarBasePorCodigoProdutoNumEdicao(Long estrategiaID, String codigoProduto, Long numeroEdicao);
}
