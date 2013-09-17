package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.filtro.FiltroEstoqueProdutosRecolhimento;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoRecolimentoDTO;
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
	public Long buscarEstoqueProdutoRecolhimentoCount(FiltroEstoqueProdutosRecolhimento filtro) {
		
		if (filtro == null || filtro.getDataRecolhimento() == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Campo data é obrigatório.");
		}
		
		return this.estoqueProdutoRespository.buscarEstoqueProdutoRecolhimentoCount(filtro);
	}

	@Override
	public List<EstoqueProdutoRecolimentoDTO> buscarEstoqueProdutoRecolhimento(
			FiltroEstoqueProdutosRecolhimento filtro) {
		
		if (filtro == null || filtro.getDataRecolhimento() == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Campo data é obrigatório.");
		}
		
		return this.estoqueProdutoRespository.buscarEstoqueProdutoRecolhimento(filtro);
	}
}
