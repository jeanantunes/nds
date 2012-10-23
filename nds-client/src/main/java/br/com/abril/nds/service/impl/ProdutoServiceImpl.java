package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaProdutoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.repository.DescontoLogisticaRepository;
import br.com.abril.nds.repository.EditorRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.TipoProdutoRepository;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.TipoMensagem;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Produto}
 * 
 * @author Discover Technology
 */
@Service
public class ProdutoServiceImpl implements ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private EstoqueProdutoService estoqueProdutoService;
	
	@Autowired
	private EditorRepository editorRepository;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private TipoProdutoRepository tipoProdutoRepository;
		
	@Autowired
	private DescontoLogisticaRepository descontoLogisticaRepository;
	
	@Override
	@Transactional(readOnly = true)
	public Produto obterProdutoPorNome(String nome) {
		if (nome == null || nome.isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Nome é obrigatório.");
		}
		
		return produtoRepository.obterProdutoPorNome(nome);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Produto> obterProdutoLikeNome(String nome) {
		if (nome == null || nome.isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Nome é obrigatório.");
		}
		
		return produtoRepository.obterProdutoLikeNome(nome);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Produto obterProdutoPorCodigo(String codigoProduto) {
		if (codigoProduto == null || codigoProduto.isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Código é obrigatório.");
		}
		
		return produtoRepository.obterProdutoPorCodigo(codigoProduto);
	}
	
	@Transactional(readOnly = true)
	public String obterNomeProdutoPorCodigo(String codigoProduto) {
		if (codigoProduto == null || codigoProduto.trim().isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Código é obrigatório.");
		}
		
		return this.produtoRepository.obterNomeProdutoPorCodigo(codigoProduto);
	}

	@Override
	@Transactional(readOnly=true)
	public List<ConsultaProdutoDTO> pesquisarProdutos(String codigo,
			String produto, String fornecedor, String editor,
			Long codigoTipoProduto, String sortorder, String sortname,
			int page, int rp) {
				
		return this.produtoRepository.pesquisarProdutos(
			codigo, produto, fornecedor, editor, 
			codigoTipoProduto, sortorder, sortname, page, rp);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer pesquisarCountProdutos(String codigo,
			String produto, String fornecedor, String editor,
			Long codigoTipoProduto) {
				
		return this.produtoRepository.pesquisarCountProdutos(codigo, produto, fornecedor, editor, codigoTipoProduto);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void removerProduto(Long id) throws UniqueConstraintViolationException {
		
		try {	
			
			Produto produto = this.produtoRepository.buscarPorId(id);
			
			if (produto != null) {
				this.produtoRepository.remover(produto);	
			}
			
		} catch (DataIntegrityViolationException e) {
			throw new UniqueConstraintViolationException("Impossível excluir o registro. Já foram gerados movimentos.");
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Ocorreu um erro ao tentar excluir o produto.");
		}
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ProdutoService#isProdutoEmEstoque(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean isProdutoEmEstoque(String codigoProduto) {
		
		List<ProdutoEdicao> listaProdutoEdicao = this.produtoEdicaoService.obterProdutosEdicaoPorCodigoProduto(codigoProduto);
		
		for (ProdutoEdicao produtoEdicao : listaProdutoEdicao) {
			
			EstoqueProduto estoqueProduto = estoqueProdutoService.buscarEstoquePorProduto(produtoEdicao.getId());
			
			if (estoqueProduto != null) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * @see br.com.abril.nds.service.ProdutoService#salvarProduto(br.com.abril.nds.model.cadastro.Produto, java.lang.Long, java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional
	public void salvarProduto(Produto produto, Long codigoEditor,
			Long codigoFornecedor, Long codigoTipoDesconto,
			Long codigoTipoProduto) {
		
		try {
			
			Editor editor =	this.editorRepository.buscarPorId(codigoEditor);
			produto.setEditor(editor);
			
			Fornecedor fornecedor = this.fornecedorRepository.buscarPorId(codigoFornecedor);
			produto.addFornecedor(fornecedor);
			
			TipoProduto tipoProduto = this.tipoProdutoRepository.buscarPorId(codigoTipoProduto);
			produto.setTipoProduto(tipoProduto);
			
			//TODO: Valor não informado na interface 
			// de cadastro de produto
			produto.setPeso(0L);

			produto = this.produtoRepository.merge(produto);
			
			if (codigoTipoDesconto != null && codigoTipoDesconto.intValue() > 0) {

				DescontoLogistica descontoLogistica = this.descontoLogisticaRepository.obterPorTipoDesconto(codigoTipoDesconto.intValue());
				if (descontoLogistica!=null){
					
					Set<Produto> produtos = new LinkedHashSet<Produto>();
						
				    produtos.add(produto);
				    
				    descontoLogistica.setProdutos(produtos);
					
				    descontoLogistica = this.descontoLogisticaRepository.merge(descontoLogistica);
				
				    produto.setDescontoLogistica(descontoLogistica);
				
				    this.produtoRepository.merge(produto);
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Produto obterProdutoPorID(Long id) {

		Produto produto = this.produtoRepository.obterProdutoPorID(id);
		return produto;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ProdutoService#obterProdutosPelosIds(java.util.List)
	 */
	@Override
	public List<Produto> obterProdutosPelosIds(List<Long> idsProdutos) {
		
		List<Produto> listaProdutos = new ArrayList<Produto>();
		
		for(Long id : idsProdutos) {
			listaProdutos.add(this.produtoRepository.obterProdutoPorID(id));
		}
		
		return listaProdutos;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Produto> obterProdutos() {
		
		return produtoRepository.buscarTodos();
	}

	@Transactional(readOnly=true)
	public List<Produto> obterProdutosOrganizadosNome() {
		return produtoRepository.buscarProdutosOrganizadosNome();
	}
	
	
}