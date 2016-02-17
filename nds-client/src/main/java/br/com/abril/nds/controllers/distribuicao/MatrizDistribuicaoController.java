package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.SerializationUtils;
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
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroDistribuicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.process.definicaobases.DefinicaoBases;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.EstudoAlgoritmoService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.InformacoesProdutoService;
import br.com.abril.nds.service.MatrizDistribuicaoService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.ProdutoEdicaoAlgoritimoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.SomarEstudosService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.HTMLTableUtil;
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
@Rules(Permissao.ROLE_DISTRIBUICAO_MATRIZ_DISTRIBUICAO)
public class MatrizDistribuicaoController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatrizDistribuicaoController.class);

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

    @Autowired
    private InformacoesProdutoService infoProdService;

    @Autowired
    private DefinicaoBases definicaoBases;

    @Autowired
    private ProdutoService produtoService;
    
    @Autowired
    private EstudoService estudoService;

    @Autowired
    private ProdutoEdicaoAlgoritimoService produtoEdicaoAlgoritimoService;

    private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMatrizDistribuicao";
    public static final String LISTA_DE_DUPLICACOES = "LISTA_DE_DUPLICACOES";
    private static final int MAX_DUPLICACOES_PERMITIDAS = 2;

    @Path("/")
    public void index() {

        session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);

        List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(SituacaoCadastro.ATIVO);
        
        String data = DateUtil.formatarDataPTBR(calendarioService.adicionarDiasUteis(new Date(), 2));
        
        result.include("data", data);
        result.include("fornecedores", fornecedores);
    }

    @SuppressWarnings("unchecked")
    private List<ProdutoDistribuicaoVO> obterListaDeItensDuplicadosNaSessao() {

        return (List<ProdutoDistribuicaoVO>) session.getAttribute(LISTA_DE_DUPLICACOES);
    }

    @Post
    public void obterMatrizDistribuicao(Date dataLancamento, List<Long> idsFornecedores) {

        ParametrosDistribuidorVO parametrosDistribuidorVO = parametrosDistribuidorService.getParametrosDistribuidor();
        validarDadosPesquisa(dataLancamento);
        configurarFiltropesquisa(dataLancamento, idsFornecedores);
        this.result.use(Results.json()).from(parametrosDistribuidorVO).recursive().serialize();
    }

    private List<ProdutoDistribuicaoVO> preparaItensParaVisualizacaoMatrizDistribuicao(List<ProdutoDistribuicaoVO> itens) {

        int idRow = 0;

        List<ProdutoDistribuicaoVO> itensToRemove = new ArrayList<ProdutoDistribuicaoVO>();

        for (int i = 0; i < itens.size(); i++) {

            //int idCopia = 1;

            if (itens.get(i).getIdRow() == null) {

                itens.get(i).setIdRow(idRow++);
            }

            for (int j = i + 1; j < itens.size(); j++) {

                if (itens.get(i).getIdCopia() == null &&
                        (itens.get(i).getCodigoProduto().equals(itens.get(j).getCodigoProduto()) &&
                                itens.get(i).getNumeroEdicao().equals(itens.get(j).getNumeroEdicao()))) {

                    //itens.get(j).setIdCopia(idCopia++);
                    //itens.get(j).setIdRow(itens.get(i).getIdRow());
                    itens.get(i).addItemDuplicado(itens.get(j), itens.get(i).getIdRow());
                    itensToRemove.add(itens.get(j));
                }
            }
        }

        itens.removeAll(itensToRemove);

        List<ProdutoDistribuicaoVO> newItens = new ArrayList<ProdutoDistribuicaoVO>();

        Comparator<ProdutoDistribuicaoVO> cCopias = new Comparator<ProdutoDistribuicaoVO>() {

            @Override
            public int compare(ProdutoDistribuicaoVO p1, ProdutoDistribuicaoVO p2) {
                return p1.compareTo(p2);
            }
        };

        for (int i = 0; i < itens.size(); i++) {

            newItens.add(itens.get(i));

            if (!itens.get(i).getProdutoDistribuicoesDuplicados().isEmpty()) {

                Collections.sort(itens.get(i).getProdutoDistribuicoesDuplicados(), cCopias);
                newItens.addAll(itens.get(i).getProdutoDistribuicoesDuplicados());
            }
        }

        return newItens;
    }

    @Post
    public void obterGridMatrizDistribuicao(String sortorder, String sortname, int page, int rp) {

        FiltroDistribuicaoDTO filtro = obterFiltroSessao();
        filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
        TotalizadorProdutoDistribuicaoVO vo = matrizDistribuicaoService.obterMatrizDistribuicao(filtro);

//        Collections.sort(vo.getListProdutoDistribuicao());

        List<ProdutoDistribuicaoVO> listSession = obterListaDeItensDuplicadosNaSessao();

        if (listSession != null && !listSession.isEmpty()) {

            List<ProdutoDistribuicaoVO> listCopia = new ArrayList<ProdutoDistribuicaoVO>();

            for (ProdutoDistribuicaoVO itemSession : listSession) {
                listCopia.add(itemSession);
            }

            List<ProdutoDistribuicaoVO> newList = new ArrayList<ProdutoDistribuicaoVO>();

            for (ProdutoDistribuicaoVO distribuicaoVO : vo.getListProdutoDistribuicao()) {

                newList.add(distribuicaoVO);

                if (!listCopia.isEmpty()) {

                    for (int i = 0; i < listSession.size(); i++) {

                        ProdutoDistribuicaoVO distribuicaoVOCopia = listSession.get(i);

                        if (distribuicaoVO.getCodigoProduto().equals(distribuicaoVOCopia.getCodigoProduto()) &&
                                distribuicaoVO.getNumeroEdicao().equals(distribuicaoVOCopia.getNumeroEdicao())) {

                            if (listCopia.remove(distribuicaoVOCopia)) {
                                newList.add(distribuicaoVOCopia);
                            }
                        }
                    }
                }
            }

            vo.setListProdutoDistribuicao(newList);
        }

        vo.setListProdutoDistribuicao(preparaItensParaVisualizacaoMatrizDistribuicao(vo.getListProdutoDistribuicao()));

        filtro.setTotalRegistrosEncontrados(vo.getListProdutoDistribuicao().size());
        session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
        processarDistribuicao(vo, filtro);
    }

    @Post
    public void carregarProdutoEdicaoPorEstudo(BigInteger estudo) {

        if (estudo == null) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Digite um número de estudo."));
        }

        FiltroInformacoesProdutoDTO filtro = new FiltroInformacoesProdutoDTO();
        filtro.setNumeroEstudo(estudo.longValue());
        List<InformacoesProdutoDTO> buscarProduto = this.infoProdService.buscarProduto(filtro);

//		ProdutoDistribuicaoVO produtoDistribuicaoVO = matrizDistribuicaoService.obterProdutoDistribuicaoPorEstudo(estudo);


        if (buscarProduto == null || buscarProduto.isEmpty()) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Estudo: [" + estudo + "] não encontrado."));
        }

        InformacoesProdutoDTO infoDTO = buscarProduto.get(0);
        ProdutoDistribuicaoVO produtoDistribuicaoVO = new ProdutoDistribuicaoVO();
        produtoDistribuicaoVO.setCodigoProduto(infoDTO.getCodProduto());
        produtoDistribuicaoVO.setNomeProduto(infoDTO.getNomeProduto());
        produtoDistribuicaoVO.setNumeroEdicao(new BigInteger(infoDTO.getNumeroEdicao().toString()));
        produtoDistribuicaoVO.setClassificacao(infoDTO.getTipoClassificacaoProdutoDescricao());
        produtoDistribuicaoVO.setDataLancto(infoDTO.getDatalanc());
        produtoDistribuicaoVO.setReparte(new BigDecimal(infoDTO.getQtdeReparteEstudo()));
        produtoDistribuicaoVO.setEstudoLiberado(infoDTO.getEstudoLiberado());
        produtoDistribuicaoVO.setQtdeReparteEstudo(infoDTO.getQtdeReparteEstudo());

        result.use(Results.json()).from(produtoDistribuicaoVO, "result").recursive().serialize();
    }

    @Post
    public void confirmarCopiarProporcionalDeEstudo(CopiaProporcionalDeDistribuicaoVO copiaProporcionalDeDistribuicaoVO) {

        Long idEstudo = matrizDistribuicaoService.confirmarCopiarProporcionalDeEstudo(copiaProporcionalDeDistribuicaoVO);
        BigInteger idLancamento = BigInteger.valueOf(copiaProporcionalDeDistribuicaoVO.getIdLancamento());
        removeItemListaDeItensDuplicadosNaSessao(idLancamento, copiaProporcionalDeDistribuicaoVO.getIdCopia());
        
        estudoService.criarRepartePorPDV(idEstudo);
        
        result.use(Results.json()).from(idEstudo, "result").recursive().serialize();
    }

    private void processarDistribuicao(TotalizadorProdutoDistribuicaoVO totProdDistVO, FiltroDistribuicaoDTO filtro) {

        PaginacaoVO paginacao = filtro.getPaginacao();
        List<ProdutoDistribuicaoVO> listProdutosDistrib = (totProdDistVO.isMatrizFinalizada()) ?
                new ArrayList<ProdutoDistribuicaoVO>() : totProdDistVO.getListProdutoDistribuicao();
        listProdutosDistrib = PaginacaoUtil.paginarEmMemoria(listProdutosDistrib, paginacao);
        TableModel<CellModelKeyValue<ProdutoDistribuicaoVO>> tm = new TableModel<CellModelKeyValue<ProdutoDistribuicaoVO>>();
        List<CellModelKeyValue<ProdutoDistribuicaoVO>> cells = CellModelKeyValue.toCellModelKeyValue(listProdutosDistrib);

        List<Object> resultado = new ArrayList<Object>();
        tm.setRows(cells);
        tm.setPage(paginacao.getPaginaAtual());
        tm.setTotal(filtro.getTotalRegistrosEncontrados());
        resultado.add(tm);
        resultado.add(totProdDistVO.getTotalSemEstudo());
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
        
        @Export(label = "Publicações Liberadas:")
        private String totalEstudosLiberado;
        
        @Export(label = "Publicações sem Estudo:")
        private String totalSemEstudo;
        

        public RodapeDTO(String totalSemEstudo, String totalLiberado) {
            this.totalSemEstudo = totalSemEstudo;
            this.totalEstudosLiberado = totalLiberado;

        }

        public String getTotalEstudosLiberado() {
            return totalEstudosLiberado;
        }
        
        public String getTotalSemEstudo() {
            return totalSemEstudo;
        }
        
    }

    /**
     * Exporta os dados da pesquisa.
     * 
     * @param fileType - tipo de arquivo
     * @throws IOException - Exceção de E/S
     */
    @Get
    public void exportar(FileType fileType) throws IOException {

        FiltroDistribuicaoDTO filtro = obterFiltroSessao();

        TotalizadorProdutoDistribuicaoVO totalizadorProdutoDistribuicaoVO = matrizDistribuicaoService.obterMatrizDistribuicao(filtro);

        if (totalizadorProdutoDistribuicaoVO != null &&
                totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao() != null &&
                !totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao().isEmpty()) {

            RodapeDTO rodapeDTO = new RodapeDTO(CurrencyUtil.formatarValor(totalizadorProdutoDistribuicaoVO.getTotalSemEstudo()),
                    CurrencyUtil.formatarValor(totalizadorProdutoDistribuicaoVO.getTotalEstudosLiberados()));

            FileExporter.to("matriz_distribuicao", fileType).inHTTPResponse(
                    this.getNDSFileHeader(),
                    filtro,
                    rodapeDTO,
                    totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao(),
                    ProdutoDistribuicaoVO.class, this.httpResponse);
        }

        result.nothing();
    }

    /**
     * Configura o filtro informado na tela e o armazena na sessÃ£o.
     * 
     * @param dataPesquisa - data da pesquisa
     * @param listaIdsFornecedores - lista de identificadores de fornecedores
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
     * @param dataPesquisa - data da pesquisa
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
     * Obtem o filtro para pesquisa da sessão.
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

        if (Boolean.valueOf(produtoDistribuicao.getLiberado())) {

            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
                    "Não é permitido duplicar linha de um estudo liberado."));
        }

        FiltroDistribuicaoDTO filtro = obterFiltroSessao();

        TotalizadorProdutoDistribuicaoVO totalizadorProdutoDistribuicaoVO = matrizDistribuicaoService.obterMatrizDistribuicao(filtro);

        List<ProdutoDistribuicaoVO> list = totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao();

        //preparaItensParaVisualizacaoMatrizDistribuicao(list);

        Comparator<ProdutoDistribuicaoVO> comparator = new Comparator<ProdutoDistribuicaoVO>() {

            @Override
            public int compare(ProdutoDistribuicaoVO o1, ProdutoDistribuicaoVO o2) {

                return o1.getIdLancamento().compareTo(o2.getIdLancamento());
            }
        };

        Collections.sort(list, comparator);

        int index = Collections.binarySearch(list, produtoDistribuicao, comparator);

        ProdutoDistribuicaoVO distribuicaoVOCopia = (ProdutoDistribuicaoVO) SerializationUtils.clone(list.get(index));
        distribuicaoVOCopia.setIdEstudo(null);

        List<ProdutoDistribuicaoVO> listProdutoDistribuicaoVO = obterListaDeItensDuplicadosNaSessao();

        if (listProdutoDistribuicaoVO == null) {

            listProdutoDistribuicaoVO = new ArrayList<ProdutoDistribuicaoVO>();
        }

        int qtdDuplicacoes = 0;
        
        for (ProdutoDistribuicaoVO produtoDistribuicaoVO : listProdutoDistribuicaoVO) {
        	if (produtoDistribuicaoVO.getIdLancamento().equals(distribuicaoVOCopia.getIdLancamento())) {
                qtdDuplicacoes++;
            }
		}
        

//        for (ProdutoDistribuicaoVO distribVO : listProdutoDistribuicaoVO) {
          for (ProdutoDistribuicaoVO distribVO : totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao()) {

            if (distribVO.getIdLancamento().equals(distribuicaoVOCopia.getIdLancamento())) {

                qtdDuplicacoes++;

                if (qtdDuplicacoes > MAX_DUPLICACOES_PERMITIDAS) {

                    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Não é permitido mais do que "
                        + MAX_DUPLICACOES_PERMITIDAS + " duplicações"));
                }
            }
        }

        distribuicaoVOCopia.setIdCopia(qtdDuplicacoes);

        listProdutoDistribuicaoVO.add(distribuicaoVOCopia);

        session.setAttribute(LISTA_DE_DUPLICACOES, listProdutoDistribuicaoVO);

//		produtoDistribuicao.setIdUsuario(getUsuarioLogado().getId());
//		matrizDistribuicaoService.duplicarLinhas(produtoDistribuicao);
        this.result.use(Results.json()).from(Results.nothing()).serialize();
    }

    public void removeItemListaDeItensDuplicadosNaSessao(BigInteger idLancamento, Integer idCopia) {

        List<ProdutoDistribuicaoVO> distribuicaoVOs = null;

        if (idCopia != null) {

            distribuicaoVOs = obterListaDeItensDuplicadosNaSessao();

            if (distribuicaoVOs != null) {

                ProdutoDistribuicaoVO produtoDistribuicaoVO = null;

                for (ProdutoDistribuicaoVO distribuicaoVO : distribuicaoVOs) {

                    if (distribuicaoVO.getIdLancamento().equals(idLancamento) && distribuicaoVO.getIdCopia().equals(idCopia)) {

                        produtoDistribuicaoVO = distribuicaoVO;
                        break;
                    }
                }

                distribuicaoVOs.remove(produtoDistribuicaoVO);
            }
        }
    }

    @Post
    public void excluirEstudosSelecionados(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

        if (produtosDistribuicao == null || produtosDistribuicao.isEmpty()) {

            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Selecione um estudo para excluir!"));
        }

        if (produtosDistribuicao.size() > 1) {

            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
                    "Não pode haver mais de um estudo marcado para a exclusão."));
        }

        matrizDistribuicaoService.excluirEstudos(produtosDistribuicao);

        for (ProdutoDistribuicaoVO distribuicaoVO : produtosDistribuicao) {

            removeItemListaDeItensDuplicadosNaSessao(distribuicaoVO.getIdLancamento(), distribuicaoVO.getIdCopia());
        }

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

    private boolean validarEdicaoBase(ProdutoDistribuicaoVO produtoDistribuicaoVO, List<ProdutoDistribuicaoVO> umaEdicaoBase) throws Exception {

        EstudoTransient estudoTemp = new EstudoTransient();

        ProdutoEdicaoEstudo prod = produtoEdicaoAlgoritimoService.getProdutoEdicaoEstudo(produtoDistribuicaoVO
                .getCodigoProduto(), produtoDistribuicaoVO.getNumeroEdicao().longValue(), produtoDistribuicaoVO
                .getIdLancamento() == null ? null : produtoDistribuicaoVO.getIdLancamento().longValue());
        estudoTemp.setProdutoEdicaoEstudo(prod);

        definicaoBases.executar(estudoTemp);

        boolean addEdicaoBase = (estudoTemp.getEdicoesBase() != null) ? (estudoTemp.getEdicoesBase().size() == 1) : false;

        if (addEdicaoBase) {

            umaEdicaoBase.add(produtoDistribuicaoVO);
            return false;
        }

        return true;
    }

    @Post
    public void gerarEstudoAutomatico(List<ProdutoDistribuicaoVO> produtoDistribuicaoVOs, boolean confirmaUmaEdicaoBase) {

        EstudoTransient estudoAutomatico = null;

        List<String> htmlEstudo = new ArrayList<>();
        List<String> msgErro = new ArrayList<>();
        List<ProdutoDistribuicaoVO> umaEdicaoBase = new ArrayList<>();
        List<ProdutoDistribuicaoVO> naoPermiteGeracaoAutomaticaList = new ArrayList<>();

        for (ProdutoDistribuicaoVO produtoDistribuicaoVO : produtoDistribuicaoVOs) {
            Produto obterProdutoPorCodigo = this.produtoService.obterProdutoPorCodigo(produtoDistribuicaoVO.getCodigoProduto());
            
            if (obterProdutoPorCodigo.getIsGeracaoAutomatica() == null || obterProdutoPorCodigo.getIsGeracaoAutomatica() == false) {
                naoPermiteGeracaoAutomaticaList.add(produtoDistribuicaoVO);
                msgErro.add("Produto " + produtoDistribuicaoVO.getCodigoProduto()
                    + " não permite geração automática de estudo.");
            }
            
            if(!produtoService.isIcdValido(obterProdutoPorCodigo.getCodigoICD())){
            	naoPermiteGeracaoAutomaticaList.add(produtoDistribuicaoVO);
            	msgErro.add("Produto " + produtoDistribuicaoVO.getCodigoProduto()
            			+ " está com o Código ICD inválido, ajuste-o no Cadastro de Produto.");
            }
            
        }
        produtoDistribuicaoVOs.removeAll(naoPermiteGeracaoAutomaticaList);

        for (ProdutoDistribuicaoVO produtoDistribuicaoVO : produtoDistribuicaoVOs) {
            try {
                if (confirmaUmaEdicaoBase || validarEdicaoBase(produtoDistribuicaoVO, umaEdicaoBase)) {

                    validarGeracaoAutomatica(produtoDistribuicaoVO);

                    ProdutoEdicaoEstudo produto = new ProdutoEdicaoEstudo(produtoDistribuicaoVO.getCodigoProduto());
                    produto.setNumeroEdicao(produtoDistribuicaoVO.getNumeroEdicao().longValue());
                    estudoAutomatico = estudoAlgoritmoService.gerarEstudoAutomatico(produto, produtoDistribuicaoVO.getRepDistrib(), this.getUsuarioLogado());
                    removeItemListaDeItensDuplicadosNaSessao(produtoDistribuicaoVO.getIdLancamento(), produtoDistribuicaoVO.getIdCopia());
                    htmlEstudo.add(HTMLTableUtil.estudoToHTML(estudoAutomatico));
                }

            } catch (ValidacaoException e) {
                LOGGER.error("Erro na geração automatica do estudo.", e);
                msgErro.addAll(e.getValidacao().getListaMensagens());
            } catch (Exception e) {
                LOGGER.error("Erro na geração automatica do estudo.", e);
                msgErro.add("Erro na geração do estudo, produto - " + produtoDistribuicaoVO.getCodigoProduto() + ", edição - " + produtoDistribuicaoVO.getNumeroEdicao());
            }
        }

        result.use(Results.json()).from(new Object[]{htmlEstudo, msgErro, umaEdicaoBase}, "estudo").recursive().serialize();
    }

    private void validarGeracaoAutomatica(ProdutoDistribuicaoVO produtoDistribuicaoVO) {

        Produto produto = produtoService.obterProdutoPorCodigo(produtoDistribuicaoVO.getCodigoProduto());

        if (!produto.getIsGeracaoAutomatica()) {

            throw new ValidacaoException(TipoMensagem.WARNING, "Produto " + produto.getCodigo()
                + " não pode ser gerado pela geração automatica");
        }
    }


    @Get
    public void histogramaPosEstudo(Long idLancamento) {

        result.forwardTo(HistogramaPosEstudoController.class).histogramaPosEstudo(idLancamento);
    }

    @Post
    public void verificarCoincidenciaEntreCotas(Long estudoBase, Long estudoSomado) {

        Boolean existeCoincidencia = somarEstudosService.verificarCoincidenciaEntreCotas(estudoBase, estudoSomado);

        result.use(Results.json()).from((existeCoincidencia != null) ? existeCoincidencia : Boolean.FALSE).serialize();

    }

    @Post
    public void somarEstudos(Long idEstudoBase, ProdutoDistribuicaoVO distribuicaoVO) {
        somarEstudosService.somarEstudos(idEstudoBase, distribuicaoVO);
        result.use(Results.json()).from(distribuicaoVO.getIdEstudo().toString(), "result").serialize();
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }
    
    @Post
    @Path("verificarICD")
    public void verificarICD(String codProduto){
    	
    	if(produtoService.isIcdValido(codProduto)){
    		result.nothing();
    	}else{
    		throw new ValidacaoException(TipoMensagem.WARNING, "Este produto está com o Código ICD inválido, ajuste-o no Cadastro de Produto.");
    	}
    	
    }


}
