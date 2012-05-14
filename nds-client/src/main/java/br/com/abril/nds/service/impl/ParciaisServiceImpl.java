package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class ParciaisServiceImpl implements ParciaisService{

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Override
	@Transactional
	public void gerarPeriodosParcias(Long idProdutoEdicao, Integer qtdePeriodos) {
		
		if(qtdePeriodos == null || qtdePeriodos<=0)
			throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade de periodos informada deve ser maior que zero.");
		
		if(idProdutoEdicao == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "Id do ProdutoEdicao do produto deve ser Informada.");
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		
	}

}
