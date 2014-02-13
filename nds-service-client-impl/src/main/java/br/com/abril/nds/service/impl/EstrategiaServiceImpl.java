package br.com.abril.nds.service.impl;

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
    
    @Transactional
    public Estrategia buscarPorProdutoEdicao(ProdutoEdicao produtoEdicao) {
    	return estrategiaRepository.buscarPorProdutoEdicao(produtoEdicao);
    }
}
