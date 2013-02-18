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
		return repo.buscarTodos();
	}

	@Transactional
	@Override
	public TipoClassificacaoProduto buscarPorId(Long id) {
		return repo.buscarPorId(id);
	}

}
