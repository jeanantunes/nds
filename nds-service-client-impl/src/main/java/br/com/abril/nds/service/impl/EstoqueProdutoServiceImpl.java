package br.com.abril.nds.service.impl;

<<<<<<< HEAD
=======
import java.util.Date;
>>>>>>> fase2
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
import br.com.abril.nds.dto.filtro.FiltroEstoqueProdutosRecolhimento;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
=======
import br.com.abril.nds.dto.ProdutoEdicaoSuplementarDTO;
>>>>>>> fase2
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoRecolimentoDTO;
import br.com.abril.nds.repository.EstoqueProdutoRespository;
import br.com.abril.nds.service.EstoqueProdutoService;

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
	
	@Transactional(readOnly = true)
	public EstoqueProduto buscarEstoquePorProduto(Long idProdutoEdicao) {
		
		return this.estoqueProdutoRespository.buscarEstoquePorProduto(idProdutoEdicao);
	}

<<<<<<< HEAD
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
=======
	@Transactional
	public List<ProdutoEdicaoSuplementarDTO> obterProdutosEdicaoSuplementarDisponivel(
			Date data, Long idCota) {
		
		return this.estoqueProdutoRespository.obterProdutosEdicaoSuplementarNaoDisponivel(idCota, data);
	}

>>>>>>> fase2
}
