package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.repository.TipoProdutoRepository;
import br.com.abril.nds.service.TipoProdutoService;

/**
 * Implementação de TipoProdutoService
 * 
 * @author Discover Technology
 */
@Service
public class TipoProdutoServiceImpl implements TipoProdutoService {

	@Autowired
	private TipoProdutoRepository tipoProdutoRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<TipoProduto> obterTodosTiposProduto() {

		return this.tipoProdutoRepository.buscarTodos();
	}

}
