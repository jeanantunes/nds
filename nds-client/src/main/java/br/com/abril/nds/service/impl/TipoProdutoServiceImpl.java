package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.repository.NCMRepository;
import br.com.abril.nds.repository.TipoProdutoRepository;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementacao referente ao servico da entidade 
 * {@link br.com.abril.nds.model.cadastro.TipoProduto}
 * 
 * @author Discover Technology
 *
 */
@Service
public class TipoProdutoServiceImpl implements TipoProdutoService {
	
	@Autowired
	private TipoProdutoRepository tipoProdutoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private NCMRepository ncmRepository;
	
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
	public void remover(Long id) throws UniqueConstraintViolationException, ValidacaoException {
		
		
			TipoProduto tipoProduto = this.tipoProdutoRepository.buscarPorId(id);
		
			if(tipoProduto != null) {
				
				if (!this.isTipoProdutoDistribuidor(tipoProduto)) {
					throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
							"Tipos de produtos cadastrados pela Treelog não podem ser removidos."));
				}
				
				try {
					
					this.tipoProdutoRepository.remover(tipoProduto);
				
				} catch (DataIntegrityViolationException e) {
					throw new UniqueConstraintViolationException("Não foi possível excluir o registro. Existem Produtos vinculados a ele");
				}
				catch (Exception e) {
					throw new ValidacaoException(TipoMensagem.WARNING, "Ocorreu um erro ao tentar excluir o registro");
				}	
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
			
			this.validaEdicaoTipoProduto(tipoProduto);
			
		} else {
			
			if (entity.getGrupoProduto() == null) {
				entity.setGrupoProduto(GrupoProduto.OUTROS);
			}
		}
		TipoProduto tipoProduto = null;
		
		try {
			tipoProduto = this.tipoProdutoRepository.merge(entity);
		} catch (DataIntegrityViolationException e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Já existem Tipos de Produtos que utilizam este código ou esta descrição"));
		}		
		
		return tipoProduto;
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoProdutoService#busca(java.lang.String, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<TipoProduto> busca(String nomeTipoProduto, Long codigo, String codigoNCM, String codigoNBM, String orderBy,
			Ordenacao ordenacao, int initialResult, int maxResults) {
		
		return this.tipoProdutoRepository.busca(nomeTipoProduto, codigo, codigoNCM, codigoNBM, orderBy, ordenacao, initialResult, maxResults);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoProdutoService#quantidade(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public Long quantidade(String nomeTipoProduto,Long codigo, String codigoNCM, String codigoNBM) {
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
	 * Valida se um tipo de produto pode ser editado.
	 * @param tipoProduto tipo produto para ser validado
	 * @throws ValidacaoException lanca exception caso o tipoProduto nao possa ser alterado.
	 */
	private void validaEdicaoTipoProduto(TipoProduto tipoProduto) throws ValidacaoException {
		
		if (!this.isTipoProdutoDistribuidor(tipoProduto)) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
					"Tipos de produtos cadastrados pela Treelog não podem ser modificados."));
		}
		
		if (this.hasProdutoEmEstoqueVinculado(tipoProduto)) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
					"Este Tipo Produto já possui produtos em estoque vinculados e não pode ser modificado."));
		}
	}
	
	/**
	 * Verifica se existe um produto em estoque vinculado ao tipo produto.
	 * @param tipoProduto 
	 * @return true caso exista
	 */
	private boolean hasProdutoEmEstoqueVinculado(TipoProduto tipoProduto) {
		
		if (this.tipoProdutoRepository.hasProdutoVinculado(tipoProduto)) {
			
			List<Produto> listaProduto = tipoProduto.getListaProdutos();
			
			if (listaProduto != null) {
				for (Produto produto : listaProduto) {
					if (this.produtoService.isProdutoEmEstoque(produto.getCodigo())) {
						return true;
					}
				}
			}
		}
		
		return false;
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

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.TipoProdutoService#getCodigoSugerido()
	 */
	@Override
	@Transactional(readOnly=true)
	public String getCodigoSugerido() {
		
		Long codigo = this.tipoProdutoRepository.getMaxCodigo();
		
		codigo += 1L;
		
		return codigo.toString();
	}

	/**
	 * Obtem lista de NCM
	 * @return List<NCM>
	 */
	@Override
	@Transactional(readOnly=true)
	public List<NCM> obterListaNCM() {
		List<NCM> listaNcm = ncmRepository.buscarTodos();
		return listaNcm;
	}

	/**
	 * Obtem NCM por id
	 * @return NCM
	 */
	@Override
	@Transactional(readOnly=true)
	public NCM obterNCMporId(Long idNcm) {
        NCM ncm = this.ncmRepository.buscarPorId(idNcm);
		return ncm;
	}

	/**
	 * Obtem NCM por codigo
	 * @return NCM
	 */
	@Override
	@Transactional(readOnly=true)
	public NCM obterNCMporCodigo(Long codigoNcm) {
		 NCM ncm = this.ncmRepository.obterPorCodigo(codigoNcm);
		return ncm;
	}

}
