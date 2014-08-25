package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;

@Service
public class TipoClassificacaoProdutoServiceImpl implements	TipoClassificacaoProdutoService {

	@Autowired
	private TipoClassificacaoProdutoRepository repo;
	
	@Transactional(readOnly = true)
	@Override
	public List<TipoClassificacaoProduto> obterTodos() {
		return repo.obterTodos();
	}

	@Transactional
	@Override
	public TipoClassificacaoProduto buscarPorId(Long id) {
		return repo.buscarPorId(id);
	}

    @Override
    @Transactional(readOnly=true)
    public TipoClassificacaoProduto obterPorClassificacao(String classificacao) {
       return repo.obterPorClassificacao(classificacao);
    }

    @Transactional(readOnly=true)
	@Override
	public Boolean validarClassificacao(String classificacao) {
    	return repo.validarClassificacao(classificacao);
	}

}
