package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
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
	public List<Produto> obterProdutoLikeNome(String nome, Integer qtdMaxResult) {
		if (nome == null || nome.isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Nome é obrigatório.");
		}
		
		return produtoRepository.obterProdutoLikeNome(nome, qtdMaxResult);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Produto obterProdutoPorCodigo(String codigoProduto) {
		if (codigoProduto == null || codigoProduto.isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Código é obrigatório.");
		}
		
		return produtoRepository.obterProdutoPorCodigo(codigoProduto);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Produto obterProdutoBalanceadosPorCodigo(String codigoProduto, Date dataLancamento) {
		
		if (codigoProduto == null || codigoProduto.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Código é obrigatório!");
		}
		
		if (dataLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Data de lançamento é obrigatória!");
		}
		
		return produtoRepository.obterProdutoBalanceadosPorCodigo(codigoProduto, dataLancamento);
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
	@Transactional
	public void removerProduto(Long id) {
		
		try {	
			
			Produto produto = this.produtoRepository.buscarPorId(id);
			
			if (produto != null){
				
				produto.setFornecedores(null);
				
				this.produtoRepository.merge(produto);
				this.produtoRepository.remover(produto);
			}
		} catch (Exception e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, 
					"Impossível excluir o produto. Existem associações que não podem ser excluídas.");
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

			Editor editor =	this.editorRepository.buscarPorId(codigoEditor);
			Fornecedor fornecedor = this.fornecedorRepository.buscarPorId(codigoFornecedor);
			TipoProduto tipoProduto = this.tipoProdutoRepository.buscarPorId(codigoTipoProduto);
			
			if(produto.getId()!=null) {
				
				Produto produtoExistente = produtoRepository.buscarPorId(produto.getId());
				
				if(produtoExistente == null) {
					throw new ValidacaoException(TipoMensagem.WARNING, "Produto não encontrado para edição.");
				}
				
				if(!produtoExistente.getCodigo().equals(produto.getCodigo())) {
					
					throw new ValidacaoException(
						TipoMensagem.WARNING, "O campo [Código] não pode ser alterado.");
				}
				
				produtoExistente.setCodigo(produto.getCodigo());
				produtoExistente.setSlogan(produto.getSlogan());
				produtoExistente.setPeb(produto.getPeb());
				produtoExistente.setFormaComercializacao(produto.getFormaComercializacao());
				produtoExistente.setPeriodicidade(produto.getPeriodicidade());
				produtoExistente.setGrupoEditorial(produto.getGrupoEditorial());
				produtoExistente.setSubGrupoEditorial(produto.getSubGrupoEditorial());
				produtoExistente.setTributacaoFiscal(produto.getTributacaoFiscal());
				produtoExistente.setPacotePadrao(produto.getPacotePadrao());
				produtoExistente.setSegmentacao(produto.getSegmentacao());
				
				produtoExistente.setEditor(editor);
				produtoExistente.addFornecedor(fornecedor);
				produtoExistente.setTipoProduto(tipoProduto);
				
				if (Origem.MANUAL == produtoExistente.getOrigem()){
					
					if (codigoTipoDesconto != null){
						
						produtoExistente.setDescontoLogistica(obterDescontoLogistica(codigoTipoDesconto));
					} else {
						
						produtoExistente.setDescontoLogistica(null);
						produtoExistente.setDesconto(produto.getDesconto());
						produtoExistente.setDescricaoDesconto(produto.getDescricaoDesconto());
					}
				}
				
				this.produtoRepository.alterar(produtoExistente);
				
			} else {
				
				produto.setEditor(editor);
				produto.addFornecedor(fornecedor);
				produto.setTipoProduto(tipoProduto);
				produto.setOrigem(Origem.MANUAL);
				
				//TODO: Valor não informado na interface de cadastro de produto
				produto.setPeso(0L);
				
				if (codigoTipoDesconto != null){
					
					produto.setDescontoLogistica(obterDescontoLogistica(codigoTipoDesconto));
				}
				
				this.produtoRepository.adicionar(produto);
			}
	}

	private DescontoLogistica obterDescontoLogistica(Long codigoTipoDesconto) {
		
		if (codigoTipoDesconto != null && codigoTipoDesconto.intValue() > 0) {
			return this.descontoLogisticaRepository.obterPorTipoDesconto(codigoTipoDesconto.intValue());
		}
		
		return null;
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
	public List<Produto> obterProdutosBalanceadosOrdenadosNome(Date dataLancamento) {
		
		if (dataLancamento == null) {
		
			throw new ValidacaoException(TipoMensagem.ERROR, "Data de lançamento é obrigatória!");
		}
		
		return produtoRepository.buscarProdutosBalanceadosOrdenadosNome(dataLancamento);
	}
	
	
}