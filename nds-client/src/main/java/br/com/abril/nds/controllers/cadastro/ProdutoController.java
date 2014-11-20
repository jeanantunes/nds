package br.com.abril.nds.controllers.cadastro;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.BaseComboVO;
import br.com.abril.nds.client.vo.ProdutoCadastroVO;
import br.com.abril.nds.client.vo.ProdutoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ConsultaProdutoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.ClasseSocial;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.FaixaEtaria;
import br.com.abril.nds.model.cadastro.FormaFisica;
import br.com.abril.nds.model.cadastro.FormatoProduto;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Sexo;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.DescontoLogisticaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.EditorService;
import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
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
@Rules(Permissao.ROLE_CADASTRO_PRODUTO)
public class ProdutoController extends BaseController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse response;
	
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
	private DescontoService descontoService;
	
	@Autowired
	private DescontoLogisticaService descontoLogisticaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private TipoClassificacaoProdutoService tipoClassificacaoProdutoService;
	
	private static final String PRODUTO_MANUAL = "MANUAL";

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroListaCadastroDeProdutos";
		
	public ProdutoController(Result result) {
		this.result = result;
	}
	
	@Path("/")
	public void index() {
		
	    final List<TipoProduto> listaTipoProduto = this.tipoProdutoService.obterTiposProdutoDesc();
		
		if (listaTipoProduto != null && !listaTipoProduto.isEmpty()) {
			this.result.include("listaTipoProduto", listaTipoProduto);
		}
		
		carregarDadosSegmentacao();
	}
	
	/**
	 * Metodo a ser utilizado para o componente autocomplete.js
	 * @param codigo
	 * @throws ValidacaoException
	 */
	@Post
	public void pesquisarPorCodigoProdutoAutoComplete(String codigo) throws ValidacaoException{
		
		pesquisarPorCodigoProduto(codigo); 
	}
	
	
	@Post
	public void pesquisarPorCodigoProduto(String codigoProduto) throws ValidacaoException{
		
		Produto produto = produtoService.obterProdutoPorCodigo(codigoProduto);
		
		if (produto == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Produto com o código \"" + codigoProduto
                    + "\" não encontrado!");
		} else {
			result.use(Results.json()).from(produto, "result").serialize();
		}		
	}

	/**
	 * Metodo a ser utilizado no componente autocomplete.js
	 * @param nome
	 */
	@Post
	public void autoCompletarPorNomeProdutoAutoComplete(String nome) {
		
		autoCompletarPorNome(nome); 
	}
	
	@Post
    @Get
	public void autoCompletarPorNomeProduto(String nomeProduto) {
		List<Produto> listaProduto = this.produtoService.obterProdutoLikeNome(nomeProduto, Constantes.QTD_MAX_REGISTROS_AUTO_COMPLETE);
		
		List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
		
		if (listaProduto != null && !listaProduto.isEmpty()) {
			Produto produtoAutoComplete = null;
			
			for (Produto produto : listaProduto) {
				produtoAutoComplete = new Produto();
				produtoAutoComplete.setCodigo(produto.getCodigo());
				
				
				ItemAutoComplete itemAutoComplete = new ItemAutoComplete(produtoAutoComplete.getCodigo(), produto.getNome(), produtoAutoComplete);
				
				listaProdutos.add(itemAutoComplete);
			}
		}
		
		result.use(Results.json()).from(listaProdutos, "result").include("value", "chave").serialize();
	}
	
	@Post
	public void autoCompletarPorNome(String nome) {
		List<Produto> listaProduto = this.produtoService.obterProdutoLikeNome(nome, Constantes.QTD_MAX_REGISTROS_AUTO_COMPLETE);
		
		List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
		
		if (listaProduto != null && !listaProduto.isEmpty()) {
			ProdutoVO produtoAutoComplete = null;
			
			for (Produto produto : listaProduto) {
				produtoAutoComplete = new ProdutoVO(produto.getCodigo(),produto.getNome(),produto);
				
				ItemAutoComplete itemAutoComplete = new ItemAutoComplete(produtoAutoComplete.getNumero(), produtoAutoComplete.getLabel(), produtoAutoComplete);
				
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
	
	/**
	 * Metodo a ser utilizado para o componente autocomplete.js
	 * @param codigo
	 */
	@Post
	public void autoCompletarPorCodigoProdutoAutoComplete(String codigo) {
		
		List<Produto> listaProduto = this.produtoService.obterProdutoLikeCodigo(codigo);
		
		List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
		
		if (listaProduto != null && !listaProduto.isEmpty()) {
			ProdutoVO produtoAutoComplete = null;
			
			for (Produto produto : listaProduto) {
				produtoAutoComplete = new ProdutoVO(produto.getCodigo(),produto.getNome(),produto);
				
				ItemAutoComplete itemAutoComplete =
					new ItemAutoComplete(produtoAutoComplete.getNumero(), produtoAutoComplete.getLabel(), produtoAutoComplete);
				
				listaProdutos.add(itemAutoComplete);
			}
		}
		
		result.use(Results.json()).from(listaProdutos, "result").include("value", "chave").serialize();
	}
	
	
	@Post
	public void autoCompletarPorCodProduto(String codigoProduto) {
		List<Produto> listaProduto = this.produtoService.obterProdutoLikeCodigo(codigoProduto);
		
		List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
		
		if (listaProduto != null && !listaProduto.isEmpty()){
			
			for (Produto produto : listaProduto) {
				ItemAutoComplete itemAutoComplete =
						new ItemAutoComplete(produto.getCodigo(), produto.getCodigo(), produto.getId().intValue());

				listaProdutos.add(itemAutoComplete);
			}
		}
		
		result.use(Results.json()).from(listaProdutos, "result").include("value", "chave").serialize();
	}
	
	@Post
	public void pesquisarPorNomeProdutoAutoComplete(String nome,String codigo) {
		
		pesquisarPorNomeProduto(nome, codigo);
	}
	
	@Post
	public void pesquisarPorNomeProduto(String nomeProduto,String codigoProduto) {
		
		Produto produto = null;
		
		if(codigoProduto!= null){
			
			produto = this.produtoService.obterProdutoPorCodigo(codigoProduto);
			
			if(produto == null || !produto.getNome().equals(nomeProduto)){
				
				produto = this.produtoService.obterProdutoPorNome(nomeProduto);
			}
		}
		else{
			
			produto = this.produtoService.obterProdutoPorNome(nomeProduto);
		}
		
		if (produto == null) {
		
			result.nothing();
			
		} else {
			
			result.use(Results.json()).from(produto, "result").serialize();
		}
	}
	
	
	/**
	 * Auto complete para produto. Casos de pesquisa por nome onde existem protudos com nomes iguais.
	 * @param produtos
	 * @return List<ItemAutoComplete>
	 */
	public List<ItemAutoComplete> getAutocompleteCodigoProduto(List<Produto> produtos){
		
		List<ItemAutoComplete> listaCotasAutoComplete = new ArrayList<ItemAutoComplete>();
		
		for (Produto produto : produtos){
			
			String numeroExibicao = produto.getCodigo();

			listaCotasAutoComplete.add(new ItemAutoComplete(numeroExibicao, null, produto));
		}
		
		return listaCotasAutoComplete;
	}
	
	
	@Post
	public void validarNumeroEdicao(String codigoProduto, String numeroEdicao) {
		
		boolean numEdicaoValida = false;
			
		ProdutoEdicao produtoEdicao =
			produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, numeroEdicao);
		
		numEdicaoValida = (produtoEdicao != null);
		
		if (!numEdicaoValida) {

            throw new ValidacaoException(TipoMensagem.WARNING, "Edição \"" + numeroEdicao
                    + "\" não encontrada para o produto!");
			
		} else {
			
			result.use(Results.json()).withoutRoot().from(produtoEdicao).serialize();
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
			Long codigoTipoProduto, String sortorder, String sortname, int page, int rp, Boolean isGeracaoAutomatica) {
		
		int startSearch = page*rp - rp;

        if (StringUtils.isNotBlank(codigo)) {
            Produto produtoDB = produtoService.obterProdutoPorCodigo(codigo);
            codigo = produtoDB.getCodigoICD();
        }

        FiltroProdutoDTO filtroProdutoDTO =
				new FiltroProdutoDTO(codigo,produto,editor,fornecedor,codigoTipoProduto,sortorder,sortname,isGeracaoAutomatica);
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroProdutoDTO);
		
		List<ConsultaProdutoDTO> listaProdutos =
			this.produtoService.pesquisarProdutos(codigo, produto, fornecedor, editor, 
				codigoTipoProduto, sortorder, sortname, startSearch, rp, isGeracaoAutomatica);
		
		Integer totalResultados = this.produtoService.pesquisarCountProdutos(codigo, produto, fornecedor, editor, codigoTipoProduto, isGeracaoAutomatica);
		
		this.result.use(FlexiGridJson.class).from(listaProdutos).total(totalResultados).page(page).serialize();
	}
	
	    /**
     * Carrega os combos do modal de inclusão/edição do Produto.
     */
	@Post
	public void carregarDadosProduto() {
		
		List<Object> listaCombos = new ArrayList<Object>();

		listaCombos.add(parseComboTipoProduto(this.tipoProdutoService.obterTiposProdutoDesc()));

		listaCombos.add(parseComboFornecedor(this.fornecedorService.obterFornecedoresDesc()));

		listaCombos.add(parseComboEditor(this.editorService.obterEditoresDesc()));
	
		this.result.use(Results.json()).from(listaCombos, "result").recursive().serialize();
	}
	
	@Post
	public void carregarDadosDesconto (String origemProduto){
		
		if(PRODUTO_MANUAL.equals(origemProduto)){
			
			List<BaseComboVO> listaBaseComboVO = new ArrayList<BaseComboVO>();
			listaBaseComboVO.add(new BaseComboVO(null, "Produto Manual"));
			
			this.result.use(Results.json()).from(listaBaseComboVO, "result").recursive().serialize();
		}
		else{
			
			this.result.use(Results.json()).from(getComboDescontoLogistica(), "result").recursive().serialize();
		}
	}
	
	    /**
     * Carrega os combos do modal de inclusão/edição do Produto-Segmentação.
     */
	@Post
	public void carregarDadosSegmentacao() {
		
		final List<ItemDTO<ClasseSocial,String>> listaClasseSocial =  new ArrayList<ItemDTO<ClasseSocial,String>>();
		for(ClasseSocial item:ClasseSocial.values()){
			listaClasseSocial.add(new ItemDTO<ClasseSocial,String>(item,item.getDescClasseSocial()));
		}
		result.include("listaClasseSocial", listaClasseSocial);
		
		final List<ItemDTO<Sexo,String>> listaSexo =  new ArrayList<ItemDTO<Sexo,String>>();
		for(Sexo item:Sexo.values()){
			listaSexo.add(new ItemDTO<Sexo,String>(item,item.name()));
		}
		result.include("listaSexo", listaSexo);	
		
		final List<ItemDTO<FaixaEtaria,String>> listaFaixaEtaria =  new ArrayList<ItemDTO<FaixaEtaria,String>>();
		for(FaixaEtaria item:FaixaEtaria.values()){
			listaFaixaEtaria.add(new ItemDTO<FaixaEtaria,String>(item,item.getDescFaixaEtaria()));
		}
		result.include("listaFaixaEtaria", listaFaixaEtaria);	

		final List<ItemDTO<FormatoProduto,String>> listaFormatoProduto =  new ArrayList<ItemDTO<FormatoProduto,String>>();
		for(FormatoProduto item:FormatoProduto.values()){
			listaFormatoProduto.add(new ItemDTO<FormatoProduto,String>(item,item.getDescFormatoProduto()));
		}
		result.include("listaFormatoProduto", listaFormatoProduto);	
		
		final List<ItemDTO<FormaFisica,String>> listaFormaFisica =  new ArrayList<ItemDTO<FormaFisica,String>>();
		for(FormaFisica item:FormaFisica.values()){
			listaFormaFisica.add(new ItemDTO<FormaFisica,String>(item,item.getDescFormaFisica()));
		}
		result.include("listaFormaFisica", listaFormaFisica);
		
		final List<ItemDTO<PeriodicidadeProduto,String>> listaPeriodicidade =  new ArrayList<ItemDTO<PeriodicidadeProduto,String>>();
		for(PeriodicidadeProduto item:PeriodicidadeProduto.values()){
			listaPeriodicidade.add(new ItemDTO<PeriodicidadeProduto,String>(item,item.toString()));
		}
		result.include("listaPeriodicidade", listaPeriodicidade);
		
		this.carregarComboSegmento();
		
    }
	
	/**
	 * Remove um Produto.
	 * 
	 * @param id
	 */
	@Post
	@Rules(Permissao.ROLE_CADASTRO_PRODUTO_ALTERACAO)
	public void removerProduto(Long id) {
		
		this.produtoService.removerProduto(id);
			
		this.result.use(Results.json()).from(
new ValidacaoVO(TipoMensagem.SUCCESS, "Produto excluído com sucesso!"),
				"result").recursive().serialize();
	}

	/**
	 * Salva o produto.
	 * 
	 * @param produto
	 * @param codigoEditor
	 * @param codigoFornecedor
	 * @param idDesconto
	 * @param codigoTipoProduto
	 */
	@Post
	@Rules(Permissao.ROLE_CADASTRO_PRODUTO_ALTERACAO)
	public void salvarProduto(Produto produto, Long codigoEditor, Long codigoFornecedor, Long idDesconto, 
			Long codigoTipoProduto) {
		
		this.validarProduto(produto, codigoEditor, codigoFornecedor, idDesconto, codigoTipoProduto);
		
		if(codigoEditor == -1L) {
            codigoEditor = editorService.criarEditorFornecedor(codigoFornecedor);
		}
        
		this.produtoService.salvarProduto(produto, codigoEditor, codigoFornecedor, idDesconto, codigoTipoProduto);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Produto salvo com sucesso!"), "result").recursive().serialize();
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
		
		Produto produto = this.produtoService.obterProdutoPorID(id);
		
		if (produto == null) {
            throw new ValidacaoException(TipoMensagem.ERROR, "Produto não encontrado!");
		}
		
		ProdutoCadastroVO produtoCadastroVO = ProdutoCadastroVO.parseProdutoToProdutoCadastroVO(produto);
		
		this.result.use(Results.json()).from(produtoCadastroVO, "result").recursive().serialize();
	}
	
	/**
	 * Retorna uma lista fornecedores relacionados a um determinado produto
	 * 
	 * @param codigoProduto
	 * @return  List<ItemDTO<Long, String>>
	 */
	@Post
	@Path("/pesquisarFornecedorProduto")
	public void pesquisarFornecedorPorProduto(String codigoProduto){
		
		codigoProduto = StringUtils.leftPad(codigoProduto, 8, '0');
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresPorProduto(codigoProduto, GrupoFornecedor.PUBLICACAO);
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		result.use(Results.json()).from(listaFornecedoresCombo, "result").recursive().serialize();
	}
		
	/**
	 * Valida o produto.
	 * 
	 * @param produto
	 * @param codigoEditor
	 * @param codigoFornecedor
	 * @param idDesconto
	 * @param codigoTipoProduto
	 */
	private void validarProduto(Produto produto, Long codigoEditor, Long codigoFornecedor, 
			Long idDesconto, Long codigoTipoProduto) {

		List<String> listaMensagens = new ArrayList<String>();
		
		if (produto != null) {

			if (produto.getCodigo() == null || produto.getCodigo().trim().isEmpty()) {
                listaMensagens.add("O preenchimento do campo [Código] é obrigatório!");
			}else{
				
				String msgCodigoTreelog = this.validarCodigoProduto(codigoFornecedor, produto.getCodigo());
				        
				if (msgCodigoTreelog != null){
		          
					listaMensagens.add(msgCodigoTreelog);
		        }
				
				Produto produtoExistente = produtoService.obterProdutoPorCodigo(produto.getCodigo());

				if(produtoExistente != null && !produtoExistente.getId().equals(produto.getId())){
                    listaMensagens.add(" O código [" + produto.getCodigo()
                            + "] já esta sendo utilizado por outro produto ");
				}
				produto.setCodigo(produto.getCodigo().trim());
			}

			if(produto.getCodigoICD() == null || produto.getCodigoICD().trim().isEmpty()){
                listaMensagens.add("O preenchimento do campo [Código ICD] é obrigatório!");
			}
			
			if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
                listaMensagens.add("O preenchimento do campo [Produto] é obrigatório!");
			}else{
				produto.setNome(produto.getNome().trim());
			}
			
			if (codigoFornecedor == null || codigoFornecedor.intValue() == 0) {
                listaMensagens.add("O preenchimento do campo [Fornecedor] é obrigatório!");
			}
			
			if (produto.getPeb() <= 0) {
                listaMensagens.add("O preenchimento do campo [PEB] é obrigatório!");
			}else{
				produto.setPeb(produto.getPeb());
			}
			
			if (produto.getPacotePadrao() <= 0) {
                listaMensagens.add("O preenchimento do campo [Pacote Padrão] é obrigatório!");
			}else{
				produto.setPacotePadrao(produto.getPacotePadrao());
			}
			
			if ((idDesconto == null || idDesconto.intValue() == 0) &&
					(produto.getDescricaoDesconto() == null || produto.getDescricaoDesconto().trim().isEmpty())) {
                listaMensagens.add("O preenchimento do campo [Tipo de Desconto] é obrigatório!");
			}
			
			if (produto.getDesconto() == null){
                listaMensagens.add("O preenchimento do campo [% Desconto] é obrigatório!");
			}
			
			if (produto.getDesconto() != null && 
					(produto.getDesconto().compareTo(new BigDecimal(100)) > 0 ||
					produto.getDesconto().compareTo(BigDecimal.ZERO) < 0)){
				listaMensagens.add("O percentual de desconto deve estar entre 0% e 100%.");
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
			
			if (produto.getTipoSegmentoProduto().getId() == null) {
                listaMensagens.add("O preenchimento do campo [Tipo Segmento] é obrigatório!");
			}
			
			if (codigoEditor == null || codigoEditor.intValue() == 0) {
                listaMensagens.add("O preenchimento do campo [Editor] é obrigatório!");
            }
		}
		
		if (listaMensagens != null && !listaMensagens.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
		}
	}
	
	private String validarCodigoProduto(Long codigoFornecedor, String codigoProduto) {

		if (codigoProduto == null || codigoFornecedor == null) {

			return null;
		}

		boolean produtoTreelog = false;

		Origem origem = fornecedorService.obterOrigemCadastroFornecedor(codigoFornecedor);
		     
		if (Origem.INTERFACE.equals(origem)){

			produtoTreelog = true;
		}

		if (!produtoTreelog && (!codigoProduto.startsWith("10") || codigoProduto.length() != 10)) {

            return "Os produtos de Fornecedores Terceiros devem ter códigos iniciados por '10' com 10 dígitos.";
		}

		return null;
	}

	@Post
	public void validarCodigoProdutoInput(Long codigoFornecedor, String codigoProduto) {

		String retorno = this.validarCodigoProduto(codigoFornecedor, codigoProduto);

		if (retorno != null) {

			throw new ValidacaoException(TipoMensagem.WARNING, retorno);
		}

		result.nothing();
	}
	
	@Post
	public void obterCodigoDisponivel(Long idFornecedor){
	    
		Origem origem = fornecedorService.obterOrigemCadastroFornecedor(idFornecedor);
				
		Object[] dados = new Object[4];
		     
		if (Origem.INTERFACE.equals(origem)) {

			dados[0] = true;
			dados[1] = "";
			dados[2] = "";
			dados[3] = "";
			
			result.use(Results.json()).from(dados, "result").serialize();

			return;
		}
		
		Editor editor = editorService.obterEditorPorFornecedor(idFornecedor);
		boolean possuiEditor = (null != editor);
		
		Fornecedor fornecedor = fornecedorService.obterFornecedorPorId(idFornecedor);
		
		dados[0] = false;
		dados[1] = this.produtoService.obterCodigoDisponivel();
		dados[2] = possuiEditor;
		dados[3] = fornecedor.getJuridica().getRazaoSocial();

		result.use(Results.json()).from(dados, "result").serialize();
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
			listaBaseComboVO.add(new BaseComboVO(editor.getId(), editor.getPessoaJuridica().getNome()));
		}
		
		return listaBaseComboVO;
	}
	
	/**
	 * Popular combo.
	 * 
	 * @return List<BaseComboVO>
	 */
	private List<BaseComboVO> getComboDescontoLogistica() {
		
		List<BaseComboVO> listaBaseComboVO = new ArrayList<BaseComboVO>();
		
		listaBaseComboVO.add(getDefaultBaseComboVO());

		List<DescontoLogistica> listaDescontos = descontoLogisticaService.obterTodos();

		for (DescontoLogistica descontoLogistica : listaDescontos) {
			listaBaseComboVO.add(new BaseComboVO(descontoLogistica.getId(), descontoLogistica.getDescricao() != null ? descontoLogistica.getDescricao() : ""));                       
		}

		return listaBaseComboVO;
	}
	
	/**
	 * Popular combo lista de Segmento.
	 */
	private void carregarComboSegmento() {

		List<ItemDTO<Long,String>> comboSegmento =  new ArrayList<ItemDTO<Long,String>>();

		List<TipoSegmentoProduto> segmentos = produtoService.carregarSegmentos();
		Collections.sort(segmentos, new Comparator<TipoSegmentoProduto>() {
			@Override
			public int compare(TipoSegmentoProduto o1, TipoSegmentoProduto o2) {
				if(o1 != null && o2 == null) return -1;
				if(o1 == null && o2 != null) return 1;
				if(o1.getDescricao() != null && o2.getDescricao() == null) return -1;
				if(o1.getDescricao() != null && o2.getDescricao() == null) return 1;
				return o1.getDescricao().compareTo(o2.getDescricao());
			}
		});

		for (TipoSegmentoProduto itemSegmento : segmentos) {
			comboSegmento.add(new ItemDTO<Long,String>(itemSegmento.getId(), itemSegmento.getDescricao()));
		}

		result.include("listaSegmentoProduto", comboSegmento);
	}
	
	
	/**
	 * Retorna o valor default para combo.
	 * 
	 * @return
	 */
	private BaseComboVO getDefaultBaseComboVO() {
		return new BaseComboVO(0L, "");
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroProdutoDTO filtro = (FiltroProdutoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		List<ConsultaProdutoDTO> listaProdutos = null;
		
		if (filtro != null){
			
			listaProdutos =
					this.produtoService.pesquisarProdutos(filtro.getCodigo(), filtro.getNome(), filtro.getFornecedor(), filtro.getEditor(), 
						filtro.getTipoProduto().getCodigo(), null, null, 0, 0, filtro.getIsGeracaoAutomatica());
		}
		
		if (listaProdutos == null || listaProdutos.isEmpty()){
			
			listaProdutos = new ArrayList<ConsultaProdutoDTO>();
		}
		
		/*
		FileExporter.to("produtos", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				listaProdutos, ConsultaProdutoDTO.class, this.response);
		*/
		
		FileExporter.to("produtos", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, 
				listaProdutos, ConsultaProdutoDTO.class, this.response);
		
		result.nothing();
	}
	
}