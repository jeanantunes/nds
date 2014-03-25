package br.com.abril.nds.service;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estrategia;

public interface EstrategiaService {

    void inserirEstrategia(Estrategia estrategia);
    Estrategia buscarPorProdutoEdicao(ProdutoEdicao produtoEdicao);
}
