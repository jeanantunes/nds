package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ProdutoEdicaoSuplementarDTO;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.MovimentoEstoqueCotaService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.estoque.EstoqueProduto}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class EstoqueProdutoServiceImpl implements EstoqueProdutoService {

	@Autowired
	private EstoqueProdutoRespository estoqueProdutoRespository;
	
	@Autowired
	private MovimentoEstoqueCotaService movimentoEstoqueCotaService;
	
	@Transactional(readOnly = true)
	public EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao) {
		
		return this.estoqueProdutoRespository.buscarEstoquePorProduto(idProdutoEdicao);
	}

	@Override
	@Transactional
	public List<ProdutoEdicaoSuplementarDTO> obterProdutosEdicaoSuplementarDisponivel(
			Date data, Long idCota) {
		
		return this.estoqueProdutoRespository.obterProdutosEdicaoSuplementarNaoDisponivel(idCota, data);
	}

}
