package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.client.vo.TotalizadorProdutoDistribuicaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CalendarioService;
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
    public void obterMatrizLancamento(Date dataLancamento, List<Long> idsFornecedores) {

	validarDadosPesquisa(dataLancamento, idsFornecedores);

	configurarFiltropesquisa(dataLancamento, idsFornecedores);

	this.result.use(Results.json()).from(Results.nothing()).serialize();
    }


    @Post
    public void obterGridMatrizLancamento(String sortorder, String sortname, int page, int rp) {

	FiltroLancamentoDTO filtro = obterFiltroSessao();

	filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));

	TotalizadorProdutoDistribuicaoVO vo = matrizDistribuicaoService.obterMatrizDistribuicao(filtro);

	filtro.setTotalRegistrosEncontrados(vo.getListProdutoDistribuicao().size());

	session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);

	processarDistribuicao(vo, filtro);
    }

    private void processarDistribuicao(TotalizadorProdutoDistribuicaoVO totProdDistVO, FiltroLancamentoDTO filtro) {

	PaginacaoVO paginacao = filtro.getPaginacao();

	List<ProdutoDistribuicaoVO> listProdutosDistrib = totProdDistVO.getListProdutoDistribuicao();

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

	result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
    }

    @Post
    public void finalizarMatrizDistribuicao(List<Date> datasConfirmadas) {

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

    private String montarNomeFornecedores(List<Long> idsFornecedores) {

	String nomeFornecedores = "";

	List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresPorId(idsFornecedores);

	if (listaFornecedor != null && !listaFornecedor.isEmpty()) {

	    for (Fornecedor fornecedor : listaFornecedor) {

		if (!nomeFornecedores.isEmpty()) {

		    nomeFornecedores += " / ";
		}

		nomeFornecedores += fornecedor.getJuridica().getRazaoSocial();
	    }
	}

	return nomeFornecedores;
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

	filtro.setNomesFornecedor(this.montarNomeFornecedores(listaIdsFornecedores));

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

	if (listaIdsFornecedores == null || listaIdsFornecedores.isEmpty()) {

	    listaMensagens.add("O preenchimento do campo [Fornecedor] é obrigatório!");
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
    public void excluirEstudosSelecionados(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

	if (produtosDistribuicao == null || produtosDistribuicao.isEmpty()) {
	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Selecione um estudo para excluir!"));
	}
	else if (produtosDistribuicao.size() > 1) {
	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Apenas um estudo pode ser selecionado para exclusão!"));
	}
	else if(produtosDistribuicao.get(0).getIdEstudo() == null || produtosDistribuicao.get(0).getIdEstudo().intValue() == 0) {
	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não existe estudo para o produto selecionado!"));
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
    public void gerarEstudoAutomatico() {

	
    }
}
