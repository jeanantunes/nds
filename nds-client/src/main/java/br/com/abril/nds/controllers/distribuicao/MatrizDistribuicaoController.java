package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.CopiaProporcionalDeDistribuicaoVO;
import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.client.vo.TotalizadorProdutoDistribuicaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.filtro.FiltroDistribuicaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estudo.CotaEstudo;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.EstudoAlgoritmoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MatrizDistribuicaoService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.SomarEstudosService;
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

    Logger log = LoggerFactory.getLogger(MatrizDistribuicaoController.class);

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
    private EstudoAlgoritmoService estudoAlgoritmoService;

    @Autowired
    private SomarEstudosService somarEstudosService;

    @Autowired
    private ParametrosDistribuidorService parametrosDistribuidorService;

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

	ParametrosDistribuidorVO parametrosDistribuidorVO = parametrosDistribuidorService.getParametrosDistribuidor();
	validarDadosPesquisa(dataLancamento);
	configurarFiltropesquisa(dataLancamento, idsFornecedores);
	this.result.use(Results.json()).from(parametrosDistribuidorVO).recursive().serialize();
    }

    @Post
    public void obterGridMatrizDistribuicao(String sortorder, String sortname, int page, int rp) {

	FiltroDistribuicaoDTO filtro = obterFiltroSessao();
	filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
	TotalizadorProdutoDistribuicaoVO vo = matrizDistribuicaoService.obterMatrizDistribuicao(filtro);
	filtro.setTotalRegistrosEncontrados(vo.getListProdutoDistribuicao().size());
	session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	processarDistribuicao(vo, filtro);
    }

    @Post
    public void carregarProdutoEdicaoPorEstudo(BigInteger estudo) {

	ProdutoDistribuicaoVO produtoDistribuicaoVO = matrizDistribuicaoService.obterProdutoDistribuicaoPorEstudo(estudo);
	
	if (produtoDistribuicaoVO == null) {
		
		throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Estudo: ["+estudo+"] não encontrado."));
	}
	
	result.use(Results.json()).from(produtoDistribuicaoVO, "result").recursive().serialize();
    }

    @Post
    public void confirmarCopiarProporcionalDeEstudo(CopiaProporcionalDeDistribuicaoVO copiaProporcionalDeDistribuicaoVO) {

	Long idEstudo = matrizDistribuicaoService.confirmarCopiarProporcionalDeEstudo(copiaProporcionalDeDistribuicaoVO);
	result.use(Results.json()).from(idEstudo, "result").recursive().serialize();
    }

    private void processarDistribuicao(TotalizadorProdutoDistribuicaoVO totProdDistVO, FiltroDistribuicaoDTO filtro) {

	PaginacaoVO paginacao = filtro.getPaginacao();
	List<ProdutoDistribuicaoVO> listProdutosDistrib = (totProdDistVO.isMatrizFinalizada()) ? new ArrayList<ProdutoDistribuicaoVO>()
		: totProdDistVO.getListProdutoDistribuicao();
	listProdutosDistrib = PaginacaoUtil.paginarEmMemoria(listProdutosDistrib, paginacao);
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

	FiltroDistribuicaoDTO filtro = obterFiltroSessao();

	matrizDistribuicaoService.finalizarMatrizDistribuicao(filtro, produtosDistribuicao);

	result.use(Results.json()).from(Results.nothing()).serialize();
    }

    @Post
    public void finalizarMatrizDistribuicaoTodosItens(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

	FiltroDistribuicaoDTO filtro = obterFiltroSessao();

	matrizDistribuicaoService.finalizarMatrizDistribuicaoTodosItens(filtro, produtosDistribuicao);

	result.use(Results.json()).from(Results.nothing()).serialize();
    }

    @Post
    public void reabrirMatrizDistribuicao(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

	matrizDistribuicaoService.reabrirMatrizDistribuicao(produtosDistribuicao);

	result.use(Results.json()).from(Results.nothing()).serialize();
    }

    @Post
    public void reabrirMatrizDistribuicaoTodosItens() {

	FiltroDistribuicaoDTO filtro = obterFiltroSessao();

	matrizDistribuicaoService.reabrirMatrizDistribuicaoTodosItens(filtro);

	result.use(Results.json()).from(Results.nothing()).serialize();
    }

    @Exportable
    public class RodapeDTO {
	@Export(label = "Estudos gerados:")
	private String totalEstudosGerados;
	@Export(label = "Estudos liberados:")
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
     * @param fileType
     *            - tipo de arquivo
     * 
     * @throws IOException
     *             Exceção de E/S
     */
    @Get
    public void exportar(FileType fileType) throws IOException {

	FiltroDistribuicaoDTO filtro = obterFiltroSessao();

	TotalizadorProdutoDistribuicaoVO totalizadorProdutoDistribuicaoVO = matrizDistribuicaoService.obterMatrizDistribuicao(filtro);

	if (totalizadorProdutoDistribuicaoVO != null && totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao() != null
		&& !totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao().isEmpty()) {

	    RodapeDTO rodapeDTO = new RodapeDTO(CurrencyUtil.formatarValor(totalizadorProdutoDistribuicaoVO.getTotalEstudosGerados()),
		    CurrencyUtil.formatarValor(totalizadorProdutoDistribuicaoVO.getTotalEstudosLiberados()));

	    FileExporter.to("matriz_distribuicao", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, rodapeDTO,
		    totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao(), ProdutoDistribuicaoVO.class, this.httpResponse);
	}

	result.nothing();

    }

    /**
     * Configura o filtro informado na tela e o armazena na sessão.
     * 
     * @param dataPesquisa
     *            - data da pesquisa
     * @param listaIdsFornecedores
     *            - lista de identificadores de fornecedores
     */
    private FiltroDistribuicaoDTO configurarFiltropesquisa(Date dataPesquisa, List<Long> listaIdsFornecedores) {

	FiltroDistribuicaoDTO filtro = new FiltroDistribuicaoDTO(dataPesquisa, listaIdsFornecedores);

	if (listaIdsFornecedores != null && !listaIdsFornecedores.isEmpty()) {

	    filtro.setNomesFornecedor(this.montarNomeFornecedores(listaIdsFornecedores));
	}

	this.session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);

	return filtro;
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
     * Valida os dados da pesquisa.
     * 
     * @param numeroSemana
     *            - número da semana
     * @param dataPesquisa
     *            - data da pesquisa
     */
    private void validarDadosPesquisa(Date dataPesquisa) {

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
    private FiltroDistribuicaoDTO obterFiltroSessao() {

	FiltroDistribuicaoDTO filtro = (FiltroDistribuicaoDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
	if (filtro == null) {
	    throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
	}
	return filtro;
    }

    @Post
    public void duplicarLinha(ProdutoDistribuicaoVO produtoDistribuicao) {

	produtoDistribuicao.setIdUsuario(getUsuarioLogado().getId());
	matrizDistribuicaoService.duplicarLinhas(produtoDistribuicao);
	this.result.use(Results.json()).from(Results.nothing()).serialize();
    }

    @Post
    public void excluirEstudosSelecionados(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

	if (produtosDistribuicao == null || produtosDistribuicao.isEmpty()) {
	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Selecione um estudo para excluir!"));
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
    public void gerarEstudoAutomatico(String codigoProduto, Long numeroEdicao, BigDecimal reparte) {
	EstudoTransient estudoAutomatico;
	try {
	    ProdutoEdicaoEstudo produto = new ProdutoEdicaoEstudo(codigoProduto);
	    produto.setNumeroEdicao(numeroEdicao);
	    estudoAutomatico = estudoAlgoritmoService.gerarEstudoAutomatico(produto, reparte.toBigInteger(),
		    this.getUsuarioLogado());
	} catch (Exception e) {
	    log.error("Erro na geração automatica do estudo.", e);
	    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
	}
	String htmlEstudo = estudoToHTML(estudoAutomatico);
	result.use(Results.json()).from(htmlEstudo, "estudo").recursive().serialize();
    }

    private String estudoToHTML(EstudoTransient estudo) {
	
	StringBuilder h = new StringBuilder();
	h.append("<html>");
	h.append("<body>");
	h.append("<table border='1'>");
	
	h.append("<tr><td>Numero Cota</td>");
	h.append("<td>Cota Nova</td>");
	h.append("<td>Qtde PDV</td>");
	h.append("<td></td>");
	h.append("<td>Soma Rep Base</td>");
	h.append("<td>Soma Venda Base</td>");
	h.append("<td>Qtde Ed. Base</td>");
	for (int i = 0; i < 6; i++) {
	    h.append(String.format("<td>Rep%s</td>", i));
	    h.append(String.format("<td>Venda%s</td>", i));
	    h.append(String.format("<td>Peso%s</td>", i));
	}
	for (int i = 0; i < 6; i++) {
	    h.append(String.format("<td>Venda Corrigida %s</td>", i));
	}
	h.append("<td>Menor Venda</td>");
	h.append("<td>Peso Menor Venda</td>");
	h.append("<td>Venda Media Nominal</td>");
	h.append("<td>Venda Media Corrigida</td>");
	h.append("<td>Venda Crescente (Ed 6 / Ed 5)</td>");
	h.append("<td>Venda Crescente (Ed 5 / Ed 4)</td>");
	h.append("<td>Venda Crescente (Ed 4 / Ed 3)</td>");
	h.append("<td>Venda Crescente (Ed 3 / Ed 2)</td>");
	h.append("<td>Venda Crescente (Ed 2 / Ed 1)</td>");
	h.append("<td>Indice Correcao Tendencia</td>");
	h.append("<td>Indice Ajuste Cota</td>");
	h.append("<td>Indice Tratamento Regional</td>");
	h.append("<td>Indice Venda Crescente</td>");
	h.append("<td>Indice Correcao Tendencia</td>");
	h.append("<td>Venda Media Final</td>");
	h.append("<td>Reparte Minimo</td>");
	h.append("<td>Reparte Final</td>");
	h.append("<td>Classificacao</td>");
	h.append("</tr>");

	for (CotaEstudo ce : estudo.getCotas()) {
	    if (ce.getReparteCalculado().compareTo(BigInteger.ZERO) > 0) {
		h.append("	<tr>");
		h.append(String.format(" <td>%s</td>", ce.getNumeroCota()));
		h.append(String.format(" <td>%s</td>", ce.isNova()));
		h.append(String.format(" <td>%s</td>", ce.getQuantidadePDVs()));
		BigInteger soma = BigInteger.ZERO;
		BigInteger venda = BigInteger.ZERO;
		for (ProdutoEdicaoEstudo pr : ce.getEdicoesRecebidas()) {
		    soma.add(pr.getReparte().toBigInteger());
		    venda.add(pr.getVenda().toBigInteger());
		}
		h.append(String.format(" <td>%s</td>", soma));
		h.append(String.format(" <td>%s</td>", venda));
		h.append(String.format(" <td>%s</td>", ce.getEdicoesRecebidas().size()));
		h.append(String.format(" <td>%s</td>", venda));
		for (int i = 0; i < 6; i++) {
		    if (i < ce.getEdicoesRecebidas().size()) {
			h.append(String.format(" <td>%s</td>", ce.getEdicoesRecebidas().get(i).getReparte()));
			h.append(String.format(" <td>%s</td>", ce.getEdicoesRecebidas().get(i).getVenda()));
			h.append(String.format(" <td>%s</td>", ce.getEdicoesRecebidas().get(i).getPeso()));
		    } else {
			h.append(" <td></td>");
			h.append(" <td></td>");
			h.append(" <td></td>");    
		    }
		}
		ProdutoEdicaoEstudo menorVenda = new ProdutoEdicaoEstudo();
		menorVenda.setVenda(BigDecimal.ZERO);
		for (int i = 0; i < 6; i++) {
		    if (i < ce.getEdicoesRecebidas().size()) {
			h.append(String.format(" <td>%s</td>", ce.getEdicoesRecebidas().get(i).getVendaCorrigida()));
			if ((menorVenda.getVenda().compareTo(BigDecimal.ZERO) == 0) || (ce.getEdicoesRecebidas().get(i).getVenda().compareTo(menorVenda.getVenda()) < 0)) {
			    menorVenda = ce.getEdicoesRecebidas().get(i);
			}
		    } else {
			h.append(" <td></td>");
		    }
		}
		h.append(String.format(" <td>%s</td>", menorVenda.getVenda()));
		h.append(String.format(" <td>%s</td>", menorVenda.getPeso()));
		h.append(String.format(" <td>%s</td>", ce.getVendaMediaNominal()));
		h.append(String.format(" <td>%s</td>", ce.getVendaMedia()));

		for (int i = 0; i < 6; i++) {
		    if (i < ce.getEdicoesRecebidas().size()) {
			h.append(String.format(" <td>%s</td>", ce.getEdicoesRecebidas().get(i).getDivisaoVendaCrescente()));
		    } else {
			h.append(" <td></td>");
		    }
		}
		h.append(String.format(" <td>%s</td>", ce.getIndiceCorrecaoTendencia()));
		if (ce.getVendaMediaMaisN() != null) {
		    h.append(String.format(" <td>%s</td>", ce.getVendaMediaMaisN()));
		} else if (ce.getPercentualEncalheMaximo() != null) {
		    h.append(String.format(" <td>%s</td>", ce.getPercentualEncalheMaximo()));
		} else {
		    h.append(String.format(" <td>%s</td>", ce.getIndiceAjusteCota()));
		}
		h.append(String.format(" <td>%s</td>", ce.getIndiceTratamentoRegional()));
		h.append(String.format(" <td>%s</td>", ce.getIndiceVendaCrescente()));
		h.append(String.format(" <td>%s</td>", ce.getVendaMedia()));
		h.append(String.format(" <td>%s</td>", ce.getReparteMinimo()));
		h.append(String.format(" <td>%s</td>", ce.getReparteCalculado()));
		h.append(String.format(" <td>%s</td>", ce.getClassificacao()));
		h.append("	</tr>");
	    }
	}
	h.append("</table> <br/> <br/> <br/>");
	h.append("<table>");
	h.append("<tr>");
	h.append("<td>Numero Cota</td>");
	h.append("<td>Classificacao</td>");
	h.append("</tr>");
	for (CotaEstudo ce : estudo.getCotas()) {
	    if (ce.getReparteCalculado().compareTo(BigInteger.ZERO) == 0) {
		h.append("<tr>");
		h.append(String.format("<td>%s</td>", ce.getNumeroCota()));
		h.append(String.format("<td>%s</td>", ce.getClassificacao()));
		h.append("</tr>");
	    }
	}
	h.append("</table>");
	h.append("</body>");
	h.append("</html>");

	return h.toString();

//	StringBuilder sb = new StringBuilder();
//	sb.append(HTMLTableUtil.buildHTMLTable(estudoAutomatico));
//	sb.append("<br>");
//	sb.append(HTMLTableUtil.buildHTMLTable(estudoAutomatico.getEdicoesBase()));
//	sb.append("<br>");
//	sb.append(HTMLTableUtil.buildHTMLTable(estudoAutomatico.getCotas()));
//	return sb.toString();
    }

    @Post
    public void somarEstudos(Long idEstudoBase, ProdutoDistribuicaoVO distribuicaoVO) {
	somarEstudosService.somarEstudos(idEstudoBase, distribuicaoVO);
	result.use(Results.json()).from(Results.nothing()).serialize();
    }

    @Get
    public void histogramaPosEstudo() {
	result.forwardTo(HistogramaPosEstudoController.class).histogramaPosEstudo();
    }
}
