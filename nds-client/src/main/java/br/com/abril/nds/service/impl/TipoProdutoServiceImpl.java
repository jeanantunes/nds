package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.repository.TipoProdutoRepository;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente ao serviço da entidade 
 * {@link br.com.abril.nds.model.cadastro.TipoProduto}
 * 
 * @author Discover Technology
 *
 */
@Service
public class TipoProdutoServiceImpl implements TipoProdutoService {
	
	@Autowired
	private TipoProdutoRepository tipoProdutoRepository;
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoProdutoService#obterPorId(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public TipoProduto obterPorId(Long id) {
		return this.tipoProdutoRepository.buscarPorId(id);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoProdutoService#remover(java.lang.Long)
	 */
	@Override
	@Transactional(rollbackFor={ValidacaoException.class})
	public void remover(Long id) throws ValidacaoException {
		
		TipoProduto tipoProduto = this.tipoProdutoRepository.buscarPorId(id);
		
		if(tipoProduto != null) {

			this.validaTipoProduto(tipoProduto);
			
			this.tipoProdutoRepository.remover(tipoProduto);
			
		}
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoProdutoService#merge(br.com.abril.nds.model.cadastro.TipoProduto)
	 */
	@Override
	@Transactional(rollbackFor={ValidacaoException.class})
	public TipoProduto merge(TipoProduto entity) throws ValidacaoException {
		
		if (entity.getId() != null) {
			
			TipoProduto tipoProduto = this.tipoProdutoRepository.buscarPorId(entity.getId());

			this.validaTipoProduto(tipoProduto);
			
		} else {
			
			if (entity.getGrupoProduto() == null) {
				entity.setGrupoProduto(GrupoProduto.OUTROS);
			}
		}
		
		return this.tipoProdutoRepository.merge(entity);
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoProdutoService#busca(java.lang.String, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TipoProduto> busca(String nomeTipoProduto, String codigo, String codigoNCM, String codigoNBM, String orderBy,
			Ordenacao ordenacao, int initialResult, int maxResults) {
		
		return this.tipoProdutoRepository.busca(nomeTipoProduto, codigo, codigoNCM, codigoNBM, orderBy, ordenacao, initialResult, maxResults);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoProdutoService#quantidade(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public Long quantidade(String nomeTipoProduto,String codigo, String codigoNCM, String codigoNBM) {
		return this.tipoProdutoRepository.quantidade(nomeTipoProduto, codigo, codigoNCM, codigoNBM);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoProdutoService#buscaPorId(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public TipoProduto buscaPorId(Long id) {
		
		TipoProduto tipoProduto = this.tipoProdutoRepository.buscarPorId(id);
		
		return tipoProduto;
	}
	

	/**
	 * @see br.com.abril.nds.service.TipoProdutoService#obterTodosTiposProduto()
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TipoProduto> obterTodosTiposProduto() {
		
		return this.tipoProdutoRepository.buscarTodos();
	}
	
	/**
	 * Valida se um tipo de produto pode ser editado ou removido.
	 * @param tipoProduto tipo produto para ser validado
	 * @throws ValidacaoException lança exception caso o tipoProduto não possa ser alterado ou removido.
	 */
	private void validaTipoProduto(TipoProduto tipoProduto) throws ValidacaoException {
		
		
		if (this.tipoProdutoRepository.hasProdutoVinculado(tipoProduto)) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					"O Tipo de Produto já esta em uso."));
		}
		
		if (!isTipoProdutoDistribuidor(tipoProduto)) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
					"O Tipo de Produto não pode ser modificado."));
		}
	}
	
	/**
	 * Verifica se um tipo de produto foi cadastrado pelo distribuidor
	 * @return true para tipo produto que tenham sido inseridos pelo distribuidor.
	 */
	private boolean isTipoProdutoDistribuidor(TipoProduto tipoProduto) {
		
		if (tipoProduto.getGrupoProduto() == GrupoProduto.OUTROS) {
			return true;
		}
		
		return false;
	}

}
