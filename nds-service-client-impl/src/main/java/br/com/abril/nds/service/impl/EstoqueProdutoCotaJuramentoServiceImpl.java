package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.EstoqueProdutoCotaJuramentado;
import br.com.abril.nds.repository.EstoqueProdutoCotaJuramentadoRepository;
import br.com.abril.nds.service.EstoqueProdutoCotaJuramentoService;

@Service
public class EstoqueProdutoCotaJuramentoServiceImpl implements
		EstoqueProdutoCotaJuramentoService {

	@Autowired
	private EstoqueProdutoCotaJuramentadoRepository repository;
	
	@Override
	public EstoqueProdutoCotaJuramentado obterEstoqueProdutoJuramentoPorProdutoEdicao(
			ProdutoEdicao produtoEdicao) {
		return null;
	}

}
