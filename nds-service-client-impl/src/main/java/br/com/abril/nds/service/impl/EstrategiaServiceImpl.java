package br.com.abril.nds.service.impl;

import br.com.abril.nds.model.planejamento.EdicaoBaseEstrategia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estrategia;
import br.com.abril.nds.repository.EstrategiaRepository;
import br.com.abril.nds.service.EstrategiaService;

@Service
public class EstrategiaServiceImpl implements EstrategiaService {

    @Autowired
    private EstrategiaRepository estrategiaRepository;
    
    @Override
    @Transactional
    public void inserirEstrategia(Estrategia estrategia) {
    	estrategiaRepository.adicionar(estrategia);
    }

    @Override
    @Transactional
    public Estrategia buscarPorProdutoEdicaoId(ProdutoEdicao produtoEdicao) {
    	return estrategiaRepository.buscarPorProdutoEdicaoId(produtoEdicao);
    }

    @Override
    @Transactional
    public Estrategia buscarPorCodigoProdutoNumeroEdicao(final String codigoProduto, final Long numeroEdicao){
        return estrategiaRepository.buscarPorCodigoProdutoNumeroEdicao(codigoProduto, numeroEdicao);
    }

    @Override
    @Transactional
    public EdicaoBaseEstrategia buscarBasePorCodigoProdutoNumEdicao(Long estrategiaID, String codigoProduto, Long numeroEdicao){
        return estrategiaRepository.buscarBasePorCodigoProdutoNumEdicao(estrategiaID, codigoProduto, numeroEdicao);
    }
}
