package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.CopiaProporcionalDeDistribuicaoVO;
import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.client.vo.TotalizadorProdutoDistribuicaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.ProdutoEdicaoBase;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.EstudoServiceEstudo;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MatrizDistribuicaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/matrizDistribuicao")
public class MatrizDistribuicaoController extends BaseController {

    @Autowired
    private Result result;

    @Autowired
    private FornecedorService fornecedorService;

    @Autowired
    private MatrizDistribuicaoService matrizDistribuicaoService;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletResponse httpResponse;

    @Autowired
    private CalendarioService calendarioService;
    
    @Autowired
    private EstudoServiceEstudo estudoServiceEstudo;

    private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMatrizDistribuicao";

    @Path("/matrizDistribuicao")
    @Rules(Permissao.ROLE_DISTRIBUICAO_MATRIZ_DISTRIBUICAO)
    public void index() {

	session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);

	List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO);
	String data = DateUtil.formatarDataPTBR(new Date());
	result.include("data", data);
	result.include("fornecedores", fornecedores);
    }

    @Post
	public void obterMatrizDistribuicao(Date dataLancamento, List<Long> idsFornecedores) {

	validarDadosPesquisa(dataLancamento, idsFornecedores);

	configurarFiltropesquisa(dataLancamento, idsFornecedores);

	this.result.use(Results.json()).from(Results.nothing()).serialize();
    }


    @Post
	public void obterGridMatrizDistribuicao(String sortorder, String sortname, int page, int rp) {

	FiltroLancamentoDTO filtro = obterFiltroSessao();

	filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));

	TotalizadorProdutoDistribuicaoVO vo = matrizDistribuicaoService.obterMatrizDistribuicao(filtro);

	filtro.setTotalRegistrosEncontrados(vo.getListProdutoDistribuicao().size());

	session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);

	processarDistribuicao(vo, filtro);
    }

	@Post
	public void carregarProdutoEdicaoPorEstudo(BigInteger estudo) {
		
		ProdutoDistribuicaoVO produtoDistribuicaoVO = matrizDistribuicaoService.obterProdutoDistribuicaoPorEstudo(estudo);
		
		result.use(Results.json()).from(produtoDistribuicaoVO,"result").recursive().serialize();
	}
	
	@Post
	public void confirmarCopiarProporcionalDeEstudo(CopiaProporcionalDeDistribuicaoVO copiaProporcionalDeDistribuicaoVO) {
		
		Long idEstudo = matrizDistribuicaoService.confirmarCopiarProporcionalDeEstudo(copiaProporcionalDeDistribuicaoVO);
		
		result.use(Results.json()).from(idEstudo,"result").recursive().serialize();
	}
	
    private void processarDistribuicao(TotalizadorProdutoDistribuicaoVO totProdDistVO, FiltroLancamentoDTO filtro) {

	PaginacaoVO paginacao = filtro.getPaginacao();

		List<ProdutoDistribuicaoVO> listProdutosDistrib = (totProdDistVO.isMatrizFinalizada())? new ArrayList<ProdutoDistribuicaoVO>():
			totProdDistVO.getListProdutoDistribuicao();

	listProdutosDistrib = PaginacaoUtil.paginarEOrdenarEmMemoria(listProdutosDistrib, paginacao, paginacao.getSortColumn());

	TableModel<CellModelKeyValue<ProdutoDistribuicaoVO>> tm = new TableModel<CellModelKeyValue<ProdutoDistribuicaoVO>>();
	List<CellModelKeyValue<ProdutoDistribuicaoVO>> cells = CellModelKeyValue.toCellModelKeyValue(listProdutosDistrib);

	List<Object> resultado = new ArrayList<Object>();

	tm.setRows(cells);
	tm.setPage(paginacao.getPaginaAtual());
	tm.setTotal(filtro.getTotalRegistrosEncontrados());
	resultado.add(tm);

	resultado.add(totProdDistVO.getTotalEstudosGerados());
	resultado.add(totProdDistVO.getTotalEstudosLiberados());
		resultado.add(totProdDistVO.isMatrizFinalizada());

	result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
    }

    @Post
	public void finalizarMatrizDistribuicao(List<ProdutoDistribuicaoVO> produtosDistribuicao) {
		
		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		matrizDistribuicaoService.finalizarMatrizDistribuicao(filtro, produtosDistribuicao);
		
		result.use(Results.json()).from(Results.nothing()).serialize();
	}

	@Post
	public void reabrirMatrizDistribuicao() {
		
		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		matrizDistribuicaoService.reabrirMatrizDistribuicao(filtro);

		result.use(Results.json()).from(Results.nothing()).serialize();
    }


    @Exportable
    public class RodapeDTO {
	@Export(label="Estudos gerados:")
	private String totalEstudosGerados;
	@Export(label="Estudos liberados:")
	private String totalEstudosLiberado;

	public RodapeDTO(String totalGerado, String totalLiberado) {
	    this.totalEstudosGerados = totalGerado;
	    this.totalEstudosLiberado = totalLiberado;

	}

	public String getTotalEstudosGerados() {
	    return totalEstudosGerados;
	}

	public String getTotalEstudosLiberado() {
	    return totalEstudosLiberado;
	}

    }

    /**
     * Exporta os dados da pesquisa.
     * 
     * @param fileType - tipo de arquivo
     * 
     * @throws IOException Exceção de E/S
     */
    @Get
    public void exportar(FileType fileType) throws IOException {

	FiltroLancamentoDTO filtro = obterFiltroSessao();

	TotalizadorProdutoDistribuicaoVO totalizadorProdutoDistribuicaoVO = matrizDistribuicaoService.obterMatrizDistribuicao(filtro);

	if (totalizadorProdutoDistribuicaoVO != null && totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao() != null && 
		!totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao().isEmpty()) {	

	    RodapeDTO rodapeDTO = new RodapeDTO(
		    CurrencyUtil.formatarValor(totalizadorProdutoDistribuicaoVO.getTotalEstudosGerados()), 
		    CurrencyUtil.formatarValor(totalizadorProdutoDistribuicaoVO.getTotalEstudosLiberados())
		    );

	    FileExporter.to("matriz_distribuicao", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, rodapeDTO, 
		    totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao(), ProdutoDistribuicaoVO.class, this.httpResponse);
	}

	result.nothing();
    }


    /**
     * Configura o filtro informado na tela e o armazena na sessão.
     * 
     * @param dataPesquisa - data da pesquisa
     * @param listaIdsFornecedores - lista de identificadores de fornecedores
     */
    private FiltroLancamentoDTO configurarFiltropesquisa(Date dataPesquisa, List<Long> listaIdsFornecedores) {

	FiltroLancamentoDTO filtro =
		new FiltroLancamentoDTO(dataPesquisa, listaIdsFornecedores);

		//filtro.setNomesFornecedor(this.montarNomeFornecedores(listaIdsFornecedores));

	this.session.setAttribute(FILTRO_SESSION_ATTRIBUTE,filtro);

	return filtro;
    }


    /**
     * Valida os dados da pesquisa.
     *  
     * @param numeroSemana - número da semana
     * @param dataPesquisa - data da pesquisa
     * @param listaIdsFornecedores - lista de id's dos fornecedores
     */
    private void validarDadosPesquisa(Date dataPesquisa, List<Long> listaIdsFornecedores) {

	List<String> listaMensagens = new ArrayList<String>();

	if (dataPesquisa == null) {

	    listaMensagens.add("O preenchimento do campo [Data] é obrigatório!");

	}

	if (!listaMensagens.isEmpty()) {

	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
	}
    }

    /**
     * Obtém o filtro para pesquisa da sessão.
     * 
     * @return filtro
     */
    private FiltroLancamentoDTO obterFiltroSessao() {

	FiltroLancamentoDTO filtro = (FiltroLancamentoDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);

	if (filtro == null) {

	    throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
	}

	return filtro;
    }

    @Post
	public void duplicarLinha(ProdutoDistribuicaoVO produtoDistribuicao) {
				
		produtoDistribuicao.setIdUsuario(getUsuarioLogado().getId()) ;
		
		matrizDistribuicaoService.duplicarLinhas(produtoDistribuicao);
		
		this.result.use(Results.json()).from(Results.nothing()).serialize();
	}

	@Post
    public void excluirEstudosSelecionados(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

	if (produtosDistribuicao == null || produtosDistribuicao.isEmpty()) {
	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Selecione um estudo para excluir!"));
	}
	else if (produtosDistribuicao.size() > 1) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Apenas um estudo/linha pode ser selecionado para exclusão!"));
	}

	matrizDistribuicaoService.excluirEstudos(produtosDistribuicao);

	this.result.use(Results.json()).from(Results.nothing()).serialize();
    }


    @Post
    public void reabrirEstudosSelecionados(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

	if (produtosDistribuicao == null || produtosDistribuicao.isEmpty()) {
	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Selecione um ou mais estudos para ser(em) reaberto(s)!"));
	}

	matrizDistribuicaoService.reabrirEstudos(produtosDistribuicao);

	this.result.use(Results.json()).from(Results.nothing()).serialize();
    }

    @Post
    public void gerarEstudoAutomatico(String codigoProduto, BigDecimal reparte) {
    	try {
    		estudoServiceEstudo.gerarEstudoAutomatico(new ProdutoEdicaoBase(codigoProduto), reparte);
    	} catch (Exception e) {
    		throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
    	}
    	result.nothing();
    }
	
	@Get
	public void histogramaPosEstudo(){
		result.forwardTo(HistogramaPosEstudoController.class).histogramaPosEstudo();
	}
	
}
