package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.ProdutoService;

@Service
public class ProdutoServiceImpl implements ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Transactional
	@Override
	public List<Produto> obterProdutoPorNomeProduto(String nome) {
		if (nome == null || nome.isEmpty()){
			throw new IllegalArgumentException("Nome é obrigatório.");
		}
		
		return produtoRepository.obterProdutoPorNomeProduto(nome);
	}
}