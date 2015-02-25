package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.repository.DescontoLogisticaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.EditorRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.RecebimentoFisicoRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.repository.TipoProdutoRepository;
import br.com.abril.nds.repository.TipoSegmentoProdutoRepository;
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
	private RecebimentoFisicoRepository recebimentoFisicoRepository;
		
	@Autowired
	private DescontoLogisticaRepository descontoLogisticaRepository;
	
	@Autowired
	private TipoSegmentoProdutoRepository segmentoRepository;
	
	@Autowired
	private TipoClassificacaoProdutoRepository tipoClassRepo;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
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

		if (StringUtils.isBlank(codigoProduto)){
			throw new ValidacaoException(TipoMensagem.ERROR, "Código é obrigatório.");
		}
        Produto produto;
        switch (codigoProduto.length()) {
            case 6:
                produto = produtoRepository.obterProdutoPorCodigoProdin(codigoProduto.concat("01"));
                break;
            default:
                produto = produtoRepository.obterProdutoPorCodigoProdin(codigoProduto);
        }
        if (produto == null) {
            produto = produtoRepository.obterProdutoPorCodigoICD(codigoProduto);
        }
        
        return produto;
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
			int page, int rp,  Boolean isGeracaoAutomatica) {
				
		return this.produtoRepository.pesquisarProdutos(
			codigo, produto, fornecedor, editor, 
			codigoTipoProduto, sortorder, sortname, page, rp, isGeracaoAutomatica);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer pesquisarCountProdutos(String codigo,
			String produto, String fornecedor, String editor,
			Long codigoTipoProduto, Boolean isGeracaoAutomatica) {
				
		return this.produtoRepository.pesquisarCountProdutos(codigo, produto, fornecedor, editor, codigoTipoProduto, isGeracaoAutomatica);
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
			
			throw new ValidacaoException(TipoMensagem.WARNING, 
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
	public void salvarProduto(Produto produto, Long codigoEditor, Long codigoFornecedor, Long idDesconto, Long codigoTipoProduto) {

			Editor editor =	this.editorRepository.buscarPorId(codigoEditor);
			Fornecedor fornecedor = this.fornecedorRepository.buscarPorId(codigoFornecedor);
			TipoProduto tipoProduto = this.tipoProdutoRepository.buscarPorId(codigoTipoProduto);
			
			Date dataOperacao = this.distribuidorRepository.obterDataOperacaoDistribuidor();
			
			if(produto.getId() != null) {
				
				Produto produtoExistente = produtoRepository.buscarPorId(produto.getId());
				
				if(produtoExistente == null) {
					throw new ValidacaoException(TipoMensagem.WARNING, "Produto não encontrado para edição.");
				}
				
				if(!produtoExistente.getCodigo().equals(produto.getCodigo())) {
					
					throw new ValidacaoException(
						TipoMensagem.WARNING, "O campo [Código] não pode ser alterado.");
				}
				
				produtoExistente.setCodigo(produto.getCodigo());
				produtoExistente.setCodigoICD(produto.getCodigoICD());
				produtoExistente.setNome(produto.getNome());
				produtoExistente.setNomeComercial(produto.getNomeComercial());
				produtoExistente.setSlogan(produto.getSlogan());
				produtoExistente.setPeb(produto.getPeb());
				produtoExistente.setFormaComercializacao(produto.getFormaComercializacao());
				produtoExistente.setPeriodicidade(produto.getPeriodicidade());
				produtoExistente.setTributacaoFiscal(produto.getTributacaoFiscal());
				produtoExistente.setPacotePadrao(produto.getPacotePadrao());
				produtoExistente.setSegmentacao(produto.getSegmentacao());
				produtoExistente.setIsGeracaoAutomatica(produto.getIsGeracaoAutomatica());
				produtoExistente.setTipoSegmentoProduto(produto.getTipoSegmentoProduto());

				produtoExistente.setEditor(editor);
				
				this.alteraFornecedorProduto(produtoExistente, fornecedor);
				
				produtoExistente.setTipoProduto(tipoProduto);
				
				if (Origem.MANUAL == produtoExistente.getOrigem()){
					
					if (idDesconto != null){
						
						produtoExistente.setDescontoLogistica(
						        obterDescontoLogistica(idDesconto, fornecedor.getId(), dataOperacao));
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
				
				if (idDesconto != null){
					
					produto.setDescontoLogistica(obterDescontoLogistica(idDesconto, fornecedor.getId(), dataOperacao));
				}
				
				this.produtoRepository.adicionar(produto);
			}
	}

	private DescontoLogistica obterDescontoLogistica(Long idDesconto, Long idFornecedor, Date dataOperacao) {
		
		if (idDesconto != null && idDesconto.intValue() > 0) {
			
		    return this.descontoLogisticaRepository.obterDescontoLogisticaVigente(idDesconto.intValue(),
		                                                                          idFornecedor,
		                                                                          dataOperacao);
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
	@Transactional
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

	@Override
	@Transactional(readOnly = true)
	public List<Produto> obterProdutoLikeCodigo(String codigo) {
			if (codigo == null || codigo.isEmpty()){
				throw new ValidacaoException(TipoMensagem.ERROR, "Nome é obrigatório.");
			}
			
			return produtoRepository.obterProdutoLikeCodigo(codigo);
	}

	@Override
	@Transactional
	public List<String> verificarProdutoExiste(String... codigosProduto) {

        List<String> produtosValidos = new ArrayList<>();
        for (String codigoProduto : codigosProduto) {
            Produto produto = obterProdutoPorCodigo(codigoProduto);
            if (produto != null) {
                produtosValidos.add(codigoProduto);
            }
        }

        return produtosValidos;
    }

    @Override
	@Transactional
	public List<TipoSegmentoProduto> carregarSegmentos() {
		return segmentoRepository.buscarTodos();
	}

	@Override
	@Transactional
	public List<TipoClassificacaoProduto> carregarClassificacaoProduto() {
		return tipoClassRepo.buscarTodos();
	}

    @Override
	@Transactional
	public Produto obterProdutoPorProdin(String codigoProdin) {
		return produtoRepository.obterProdutoPorCodigoProdin(codigoProdin);
	}
    
    /**
     * Alteração de fornecedor do Produto, verificando se o mesmo pode ser alterado
     * @param produto
     * @param fornecedor
     */
    private void alteraFornecedorProduto(Produto produto, Fornecedor fornecedor){
            
        if ( !this.recebimentoFisicoRepository.produtoPossuiRecebimentoFisico(produto.getId())){
                
                produto.setFornecedores(new HashSet<Fornecedor>());
                produto.addFornecedor(fornecedor);
        }else{
                
            if (!fornecedor.getId().equals(produto.getFornecedor().getId())){
                    
                throw new ValidacaoException(TipoMensagem.WARNING, "O [Produto] já possui movimentações e o campo [Fornecedor] não pode ser alterado.");
            }
        }
    }
       
    @Override
    @Transactional
	public String obterCodigoDisponivel() {
		
		String ultimoCodigoRegional = this.produtoRepository.obterUltimoCodigoProdutoRegional();
		
		//primeiro produto regional a ser cadastrado
		if (ultimoCodigoRegional == null){
			
			return "1000000000";
		}
		
		if (!NumberUtils.isDigits(ultimoCodigoRegional)){
			
			ultimoCodigoRegional = ultimoCodigoRegional.replaceAll("[^\\d]", "0");
		}
		
		ultimoCodigoRegional = Long.valueOf(Long.valueOf(ultimoCodigoRegional) + 1).toString();
		
		while (this.produtoRepository.existeProdutoRegional(ultimoCodigoRegional)){
			
			ultimoCodigoRegional = Long.valueOf(Long.valueOf(ultimoCodigoRegional) + 1).toString();
		}
		
		return ultimoCodigoRegional;
	}
    
    @Override
	@Transactional
	public Long obterIdFornecedorUnificadorPorCodigoProduto(String codigoProduto) {

		Produto produto = this.obterProdutoPorCodigo(codigoProduto);
        
		if (produto == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto não encontrado!");
		}
		
		Fornecedor fornecedor = produto.getFornecedor();
		
		if (fornecedor.getFornecedorUnificador() != null) {
			
			fornecedor = fornecedor.getFornecedorUnificador();
		}
		
        return fornecedor.getId();
	}

	@Override
	public Produto obterProdutoPorICDBaseadoNoPrimeiroBarra(String codigoICD) {
		return produtoRepository.obterProdutoPorICDBaseadoNoPrimeiroBarra(codigoICD);
	}
	
	@Override
	public Boolean isIcdValido (String codIcd){
		
		Produto produto = obterProdutoPorCodigo(codIcd);
    	Integer icd;
    	
    	try {
    		icd = Integer.parseInt(produto.getCodigoICD());
		} catch (Exception e) {
			return false;
		}
    	
    	if(icd > 0){
    		return true;
    	}else{
    		return false;
    	}
	}
}