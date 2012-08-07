package br.com.abril.nds.controllers.cadastro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.BaseComboVO;
import br.com.abril.nds.client.vo.ProdutoCadastroVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ConsultaProdutoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ClasseSocial;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.FaixaEtaria;
import br.com.abril.nds.model.cadastro.FormatoProduto;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Sexo;
import br.com.abril.nds.model.cadastro.TemaProduto;
import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.EditorService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.TipoDescontoService;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes a produtos.
 * 
 * @author Discover Technology
 */
@Resource
@Path("/produto")
public class ProdutoController {

	private Result result;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private EstoqueProdutoService estoqueProdutoService;
	
	@Autowired
	private TipoProdutoService tipoProdutoService;

	@Autowired
	private EditorService editorService;

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private TipoDescontoService tipoDescontoService;
	
	private static List<ItemDTO<ClasseSocial,String>> listaClasseSocial =  new ArrayList<ItemDTO<ClasseSocial,String>>();
	  
	private static List<ItemDTO<Sexo,String>> listaSexo =  new ArrayList<ItemDTO<Sexo,String>>();
	
	private static List<ItemDTO<FaixaEtaria,String>> listaFaixaEtaria =  new ArrayList<ItemDTO<FaixaEtaria,String>>();
	
	private static List<ItemDTO<FormatoProduto,String>> listaFormatoProduto =  new ArrayList<ItemDTO<FormatoProduto,String>>();
	
	private static List<ItemDTO<TipoLancamento,String>> listaTipoLancamento =  new ArrayList<ItemDTO<TipoLancamento,String>>();
	
	private static List<ItemDTO<TemaProduto,String>> listaTemaProduto =  new ArrayList<ItemDTO<TemaProduto,String>>();
	
	public ProdutoController(Result result) {
		this.result = result;
	}
	
	@Path("/")
	public void index() {
		
		List<TipoProduto> listaTipoProduto = this.tipoProdutoService.obterTodosTiposProduto();
		
		carregarDadosSegmentacao();
		
		if (listaTipoProduto != null && !listaTipoProduto.isEmpty()) {
			this.result.include("listaTipoProduto", listaTipoProduto);
		}
	}
	
	@Post
	public void pesquisarPorCodigoProduto(String codigoProduto) throws ValidacaoException{
		Produto produto = produtoService.obterProdutoPorCodigo(codigoProduto);
		
		if (produto == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto com o código \"" + codigoProduto + "\" não encontrado!");
			
		} else {
			
			result.use(Results.json()).from(produto, "result").serialize();
			
		}		
	}

	@Post
	public void autoCompletarPorPorNomeProduto(String nomeProduto) {
		List<Produto> listaProduto = this.produtoService.obterProdutoLikeNomeProduto(nomeProduto);
		
		List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
		
		if (listaProduto != null && !listaProduto.isEmpty()) {
			Produto produtoAutoComplete = null;
			
			for (Produto produto : listaProduto) {
				produtoAutoComplete = new Produto();
				produtoAutoComplete.setCodigo(produto.getCodigo());
				
				ItemAutoComplete itemAutoComplete =
					new ItemAutoComplete(produto.getNome(), null, produtoAutoComplete);
				
				listaProdutos.add(itemAutoComplete);
			}
		}
		
		result.use(Results.json()).from(listaProdutos, "result").include("value", "chave").serialize();
	}

	@Post
	public void autoCompletarPorNomeFornecedor(String nomeFornecedor) {
		
		List<Fornecedor> listaFornecedor = this.fornecedorService.obterFornecedorLikeNomeFornecedor(nomeFornecedor);
		
		List<ItemAutoComplete> listaFornecedores = new ArrayList<ItemAutoComplete>();
		
		if (listaFornecedor != null && !listaFornecedor.isEmpty()) {
			Fornecedor fornecedorAutoComplete = null;
			
			for (Fornecedor fornecedor : listaFornecedor) {
				fornecedorAutoComplete = new Fornecedor();
				fornecedorAutoComplete.setId(fornecedor.getId());
				
				ItemAutoComplete itemAutoComplete =
					new ItemAutoComplete(fornecedor.getJuridica().getNomeFantasia(), null, fornecedorAutoComplete);
				
				listaFornecedores.add(itemAutoComplete);
			}
		}
		
		result.use(Results.json()).from(listaFornecedores, "result").include("value", "chave").serialize();
	}
	
	@Post
	public void autoCompletarEdicaoPorProduto(String codigoProduto) {
		List<ProdutoEdicao> listaProdutoEdicao = this.produtoEdicaoService.obterProdutosEdicaoPorCodigoProduto(codigoProduto);
		
		List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
				
		if (listaProdutoEdicao != null && !listaProdutoEdicao.isEmpty()) {			
					
			for (ProdutoEdicao produtoEd : listaProdutoEdicao) {
				
				ItemAutoComplete itemAutoComplete =
					new ItemAutoComplete(produtoEd.getNumeroEdicao().toString(), produtoEd.getNumeroEdicao().toString(), produtoEd.getId().intValue());
				
				listaProdutos.add(itemAutoComplete);
			}
		}
		
		result.use(Results.json()).from(listaProdutos, "result").include("value", "chave").serialize();
	}
		
	@Post
	public void pesquisarPorNomeProduto(String nomeProduto) {
		Produto produto = this.produtoService.obterProdutoPorNomeProduto(nomeProduto);
		
		if (produto == null) {
		
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto \"" + nomeProduto + "\" não encontrado!");
		
		}
			
		result.use(Results.json()).from(produto, "result").serialize();
	}
	
	@Post
	public void validarNumeroEdicao(String codigoProduto, String numeroEdicao) {
		
		boolean numEdicaoValida = false;
			
		ProdutoEdicao produtoEdicao =
			produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
		numEdicaoValida = (produtoEdicao != null);
		
		if (!numEdicaoValida) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Edição \"" + numeroEdicao + "\" não encontrada para o produto!");
			
		} else {
			
			result.use(Results.json()).from("", "result").serialize();			
		}
	}
	
	@Post
	@Path("/obterProdutoEdicao")
	public void obterProdutoEdicaoPorCodProdutoNumEdicao(String codigoProduto, String numeroEdicao) {
		
		ProdutoEdicao produtoEdicao =
			produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
		if (produtoEdicao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Edição não encontrada para o produto!");
		}
		
		result.use(Results.json()).from(produtoEdicao, "result").serialize();
	}
	
	@Post
	@Path("/obterEstoque")
	public void obterEstoque(Long idProdutoEdicao) {
		
		if (idProdutoEdicao == null) {
			
			result.use(Results.json()).from("", "estoqueProduto").serialize();
		}
		
		EstoqueProduto estoqueProduto =
			this.estoqueProdutoService.buscarEstoquePorProduto(idProdutoEdicao);
		
		if (estoqueProduto == null) {
			
			result.use(Results.json()).from("", "estoqueProduto").serialize();
			
		} else {
		
			result.use(Results.json()).from(estoqueProduto, "result").serialize();
		}
	}
	
	/**
	 * Pesquisa os produtos com paginação.
	 * 
	 * @param codigo
	 * @param produto
	 * @param fornecedor
	 * @param editor
	 * @param codigoTipoProduto
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	@Path("/pesquisarProdutos")
	public void pesquisarProdutos(String codigo, String produto, String fornecedor, String editor,
			Long codigoTipoProduto, String sortorder, String sortname, int page, int rp) {
		
		int startSearch = page*rp - rp;
		
		List<ConsultaProdutoDTO> listaProdutos =
			this.produtoService.pesquisarProdutos(codigo, produto, fornecedor, editor, 
				codigoTipoProduto, sortorder, sortname, startSearch, rp);
		
		Integer totalResultados = this.produtoService.pesquisarCountProdutos(codigo, produto, fornecedor, editor, codigoTipoProduto);
		
		this.result.use(FlexiGridJson.class).from(listaProdutos).total(totalResultados).page(page).serialize();
	}
	
	/**
	 * Carrega os combos do modal de inclusão/edição do Produto.
	 */
	@Post
	public void carregarDadosProduto() {
		
		List<Object> listaCombos = new ArrayList<Object>();

		listaCombos.add(parseComboTipoProduto(this.tipoProdutoService.obterTodosTiposProduto()));

		listaCombos.add(parseComboFornecedor(this.fornecedorService.obterFornecedores()));

		listaCombos.add(parseComboEditor(this.editorService.obterEditores()));
		
		listaCombos.add(parseComboTipoDesconto(this.tipoDescontoService.obterTodosTiposDescontos()));
		
		this.result.use(Results.json()).from(listaCombos, "result").recursive().serialize();
	}
	
	/**
	 * Carrega os combos do modal de inclusão/edição do Produto-Segmentação.
	 */
	@Post
	public void carregarDadosSegmentacao() {
		
		listaClasseSocial.clear();
		for(ClasseSocial item:ClasseSocial.values()){
			listaClasseSocial.add(new ItemDTO<ClasseSocial,String>(item,item.getDescClasseSocial()));
		}
		result.include("listaClasseSocial",listaClasseSocial);
		
		listaSexo.clear();
		for(Sexo item:Sexo.values()){
			listaSexo.add(new ItemDTO<Sexo,String>(item,item.name()));
		}
		result.include("listaSexo",listaSexo);	
		
		listaFaixaEtaria.clear();
		for(FaixaEtaria item:FaixaEtaria.values()){
			listaFaixaEtaria.add(new ItemDTO<FaixaEtaria,String>(item,item.getDescFaixaEtaria()));
		}
		result.include("listaFaixaEtaria",listaFaixaEtaria);	

		listaFormatoProduto.clear();
		for(FormatoProduto item:FormatoProduto.values()){
			listaFormatoProduto.add(new ItemDTO<FormatoProduto,String>(item,item.getDescFormatoProduto()));
		}
		result.include("listaFormatoProduto",listaFormatoProduto);	

		listaTipoLancamento.clear();
		for(TipoLancamento item:TipoLancamento.values()){
			listaTipoLancamento.add(new ItemDTO<TipoLancamento,String>(item,item.getDescricao()));
		}
		result.include("listaTipoLancamento",listaTipoLancamento);	
		
		listaTemaProduto.clear();
		for(TemaProduto item:TemaProduto.values()){
			listaTemaProduto.add(new ItemDTO<TemaProduto,String>(item,item.getDescTemaProduto()));
		}
		result.include("listaTemaProduto",listaTemaProduto);	
    }
	
	/**
	 * Remove um Produto.
	 * 
	 * @param id
	 */
	@Post
	public void removerProduto(Long id) {
		
		try {
			
			this.produtoService.removerProduto(id);
			
		} catch (UniqueConstraintViolationException e) {
			
			this.result.use(Results.json()).from(
					new ValidacaoVO(TipoMensagem.WARNING, e.getMessage()), 
					"result").recursive().serialize();
			throw new ValidacaoException();
		}
			
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Produto excluído com sucesso!"), 
				"result").recursive().serialize();
	}
	
	/**
	 * Carrega o percentual de Desconto do Produto.
	 * 
	 * @param codigoTipoDesconto
	 */
	@Post
	public void carregarPercentualDesconto(Long codigoTipoDesconto) {
	
		TipoDesconto tipoDesconto = 
			this.tipoDescontoService.obterTipoDescontoPorID(codigoTipoDesconto);
		
		BigDecimal porcentagem = BigDecimal.ZERO;
		
		if (tipoDesconto != null) {
			porcentagem = tipoDesconto.getPorcentagem();
		}
		
		this.result.use(Results.json()).from(porcentagem, "result").recursive().serialize();
	}

	/**
	 * Salva o produto.
	 * 
	 * @param produto
	 * @param codigoEditor
	 * @param codigoFornecedor
	 * @param codigoTipoDesconto
	 * @param codigoTipoProduto
	 */
	@Post
	public void salvarProduto(Produto produto, Long codigoEditor, Long codigoFornecedor, Long codigoTipoDesconto, 
			Long codigoTipoProduto) {
		
		this.validarProduto(
			produto, codigoEditor, codigoFornecedor, 
			codigoTipoDesconto, codigoTipoProduto);
		
		try {
			
			this.produtoService.salvarProduto(
				produto, codigoEditor, codigoFornecedor, 
				codigoTipoDesconto, codigoTipoProduto);
			
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao tentar salvar o Produto!");
		}
		
		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Produto salvo com sucesso!"), "result").recursive().serialize();
	}
	
	/**
	 * Carrega o produto para edição.
	 * 
	 * @param id
	 */
	@Post
	public void carregarProdutoParaEdicao(Long id) {
		
		if (id == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto não encontrado!");
		}
		
		Produto produto =
			this.produtoService.obterProdutoPorID(id);
		
		if (produto == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto não encontrado!");
		}
		
		ProdutoCadastroVO produtoCadastroVO = ProdutoCadastroVO.parseProdutoToProdutoCadastroVO(produto);
		
		this.result.use(Results.json()).from(produtoCadastroVO, "result").recursive().serialize();
	}
	
	/**
	 * Valida o produto.
	 * 
	 * @param produto
	 * @param codigoEditor
	 * @param codigoFornecedor
	 * @param codigoTipoDesconto
	 * @param codigoTipoProduto
	 */
	private void validarProduto(Produto produto, Long codigoEditor, Long codigoFornecedor, 
			Long codigoTipoDesconto, Long codigoTipoProduto) {

		List<String> listaMensagens = new ArrayList<String>();
		
		if (produto != null) {

			if (produto.getCodigo() == null || produto.getCodigo().trim().isEmpty()) {
				listaMensagens.add("O preenchimento do campo [Código] é obrigatório!");
			} else {
				produto.setCodigo(produto.getCodigo().trim());
			}

			if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
				listaMensagens.add("O preenchimento do campo [Produto] é obrigatório!");
			} else {
				produto.setNome(produto.getNome().trim());
			}
			
			if (codigoFornecedor == null || codigoFornecedor.intValue() == 0) {
				listaMensagens.add("O preenchimento do campo [Fornecedor] é obrigatório!");
			}
			
			if (produto.getPeb() <= 0) {
				listaMensagens.add("O preenchimento do campo [PEB] é obrigatório!");
			} else {
				produto.setPeb(produto.getPeb());
			}
			
			if (produto.getPacotePadrao() <= 0) {
				listaMensagens.add("O preenchimento do campo [Pacote Padrão] é obrigatório!");
			} else {
				produto.setPacotePadrao(produto.getPacotePadrao());
			}
			
			if (codigoTipoDesconto == null || codigoTipoDesconto.intValue() == 0) {
				listaMensagens.add("O preenchimento do campo [Tipo de Desconto] é obrigatório!");
			}
			
			if (codigoTipoProduto == null || codigoTipoProduto.intValue() == 0) {
				listaMensagens.add("O preenchimento do campo [Tipo de Produto] é obrigatório!");
			}

			if (produto.getFormaComercializacao() == null) {
				listaMensagens.add("O preenchimento do campo [Forma Comercialização] é obrigatório!");
			}
			
			if (produto.getPeriodicidade() == null) {
				listaMensagens.add("O preenchimento do campo [Periodicidade] é obrigatório!");
			}
			
			if (produto.getTributacaoFiscal() == null) {
				listaMensagens.add("O preenchimento do campo [Tributação Fiscal] é obrigatório!");
			}

			if (produto.getGrupoEditorial() != null && !produto.getGrupoEditorial().trim().isEmpty()) {
				produto.setGrupoEditorial(produto.getGrupoEditorial().trim());
			}
			
			if (produto.getSubGrupoEditorial() != null && !produto.getSubGrupoEditorial().trim().isEmpty()) {
				produto.setSubGrupoEditorial(produto.getSubGrupoEditorial().trim());
			}
	
		}
		
		if (listaMensagens != null && !listaMensagens.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, listaMensagens));
		}
	}
		
	/**
	 * Parse para combo.
	 * 
	 * @param listaTipoDesconto
	 * @return
	 */
	private List<BaseComboVO> parseComboTipoDesconto(List<TipoDesconto> listaTipoDesconto) {
		
		List<BaseComboVO> listaBaseComboVO = new ArrayList<BaseComboVO>();

		listaBaseComboVO.add(getDefaultBaseComboVO());
		
		for (TipoDesconto tipoDesconto : listaTipoDesconto) {
			listaBaseComboVO.add(new BaseComboVO(tipoDesconto.getId(), tipoDesconto.getDescricao()));
		}
		
		return listaBaseComboVO;
	}
	
	/**
	 * Parse para combo.
	 * 
	 * @param listaTipoProduto
	 * @return
	 */
	private List<BaseComboVO> parseComboTipoProduto(List<TipoProduto> listaTipoProduto) {
		
		List<BaseComboVO> listaBaseComboVO = new ArrayList<BaseComboVO>();

		listaBaseComboVO.add(getDefaultBaseComboVO());
		
		for (TipoProduto tipoProduto : listaTipoProduto) {
			listaBaseComboVO.add(new BaseComboVO(tipoProduto.getId(), tipoProduto.getDescricao()));
		}
		
		return listaBaseComboVO;
	}
	
	/**
	 * Parse para combo.
	 * 
	 * @param listaFornecedor
	 * @return
	 */
	private List<BaseComboVO> parseComboFornecedor(List<Fornecedor> listaFornecedor) {
		
		List<BaseComboVO> listaBaseComboVO = new ArrayList<BaseComboVO>();

		listaBaseComboVO.add(getDefaultBaseComboVO());
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaBaseComboVO.add(new BaseComboVO(fornecedor.getId(), fornecedor.getJuridica().getNomeFantasia()));
		}
		
		return listaBaseComboVO;
	}

	/**
	 * Parse para combo.
	 * 
	 * @param listaEditor
	 * @return
	 */
	private List<BaseComboVO> parseComboEditor(List<Editor> listaEditor) {
		
		List<BaseComboVO> listaBaseComboVO = new ArrayList<BaseComboVO>();
		
		listaBaseComboVO.add(getDefaultBaseComboVO());
		
		for (Editor editor : listaEditor) {
			listaBaseComboVO.add(new BaseComboVO(editor.getId(), editor.getNome()));
		}
		
		return listaBaseComboVO;
	}
	
	/**
	 * Retorna o valor default para combo.
	 * 
	 * @return
	 */
	private BaseComboVO getDefaultBaseComboVO() {
		return new BaseComboVO(0L, "");
	}
	
}
