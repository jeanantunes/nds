package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.client.vo.TipoDescontoCotaVO;
import br.com.abril.nds.model.cadastro.EspecificacaoDesconto;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.model.cadastro.TipoDescontoProduto;
import br.com.abril.nds.repository.TipoDescontoProdutoRepository;
import br.com.abril.nds.service.TipoDescontoProdutoService;

@Service
public class TipoDescontoProdutoServiceImpl implements
		TipoDescontoProdutoService {

	@Autowired
	private TipoDescontoProdutoRepository tipoDescontoProdutoRepository;
	
	@Override
	public void incluirDesconto(TipoDescontoProduto tipoDescontoProduto) {
		 this.tipoDescontoProdutoRepository.adicionar(tipoDescontoProduto);

	}

	@Override
	public List<TipoDescontoCotaVO> obterTipoDescontoCota(
			EspecificacaoDesconto especificacaoDesconto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excluirDesconto(TipoDescontoCota tipoDescontoCota) {
		// TODO Auto-generated method stub

	}

	@Override
	public TipoDescontoCota obterTipoDescontoCotaPorId(long idDesconto) {
		// TODO Auto-generated method stub
		return null;
	}

}
