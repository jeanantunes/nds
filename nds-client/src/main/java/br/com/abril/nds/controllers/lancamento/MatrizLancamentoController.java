package br.com.abril.nds.controllers.lancamento;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.SerializationUtils;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ProdutoLancamentoVO;
import br.com.abril.nds.client.vo.ResultadoResumoBalanceamentoVO;
import br.com.abril.nds.client.vo.ResumoPeriodoBalanceamentoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ConfirmacaoVO;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/matrizLancamento")
@Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ)
public class MatrizLancamentoController extends BaseController {
    
    @Autowired
    private Result result;
    
    @Autowired
    private FornecedorService fornecedorService;
    
    @Autowired
    private MatrizLancamentoService matrizLancamentoService;
    
    @Autowired
    private LancamentoRepository lancamentoRepositoryService;
    
    @Autowired
    private HttpSession session;
    
    @Autowired
    private HttpServletResponse httpResponse;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private CalendarioService calendarioService;
    
    @Value("${data_cabalistica}")
    private String dataCabalistica;
    
    private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMatrizBalanceamento";
    
    private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO = "balanceamentoLancamento";
    
    private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO = "balanceamentoAlterado";
    
    private static final String DATA_ATUAL_SELECIONADA = "dataAtualSelecionada";
    
    @Path("/")
    public void index() {
        
        removerAtributoAlteracaoSessao();
        
        session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);
        
        final List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(SituacaoCadastro.ATIVO);
        final String data = DateUtil.formatarDataPTBR(new Date());
        result.include("data", data);
        result.include("fornecedores", fornecedores);
    }
    
    @Post
    public void obterMatrizLancamento(final Date dataLancamento, final List<Long> idsFornecedores) {
        
        validarDadosPesquisa(dataLancamento, idsFornecedores);
        
        removerAtributoAlteracaoSessao();
        
        final FiltroLancamentoDTO filtro = configurarFiltropesquisa(dataLancamento, idsFornecedores);
        
        final BalanceamentoLancamentoDTO balanceamentoLancamento = this.obterBalanceamentoLancamento(filtro);
        
        final ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = this
                .obterResultadoResumoLancamento(balanceamentoLancamento);
        
        result.use(CustomJson.class).put("resultado", resultadoResumoBalanceamento).serialize();
        
    }
    
    @Post
    @Path("/salvar")
    public void salvar(final Date dataLancamento, final List<Long> idsFornecedores) {
        
        //Solicitado para salvar somente no dia
        
        
        this.verificarExecucaoInterfaces();
        
        final BalanceamentoLancamentoDTO balanceamentoLancamento = (BalanceamentoLancamentoDTO) session
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
        
        if (balanceamentoLancamento == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
        }
        
        final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamentoSessao = balanceamentoLancamento
                .getMatrizLancamento();
        
        Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento = this.cloneObject(matrizLancamentoSessao);
        
        final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno = matrizLancamentoService
                .salvarMatrizLancamento(dataLancamento,idsFornecedores,matrizLancamento, getUsuarioLogado());
        
        matrizLancamento = this.atualizarMatizComProdutosConfirmados(matrizLancamento, matrizLancamentoRetorno);
        
        balanceamentoLancamento.setMatrizLancamento(matrizLancamento);
        
        session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO, balanceamentoLancamento);
        
        this.removerAtributoAlteracaoSessao();
        
        result.use(Results.json()).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Balanceamento da matriz de lancamento salvo com sucesso!"),
                "result").recursive().serialize();
    }
    
    /**
     * Obtem lista de produtos do balanceamento
     * 
     * @param data
     * @param balanceamentoLancamento
     * @return List<ProdutoLancamentoDTO>
     */
    private List<ProdutoLancamentoDTO> getListaProdutoBalanceamento(final Date data,
            final BalanceamentoLancamentoDTO balanceamentoLancamento) {
        
        List<ProdutoLancamentoDTO> listaProdutoBalanceamento = new ArrayList<>();
        
        if (data != null) {
            
            listaProdutoBalanceamento = balanceamentoLancamento.getMatrizLancamento().get(data);
            
        } else {
            
            for (final Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : balanceamentoLancamento.getMatrizLancamento()
                    .entrySet()) {
                
                listaProdutoBalanceamento.addAll(entry.getValue());
            }
        }
        
        return listaProdutoBalanceamento;
    }
    
    @Post
    public void obterGridMatrizLancamento(final String dataLancamentoFormatada, final String sortorder, final String sortname, final int page,
            final int rp) {
        
        final BalanceamentoLancamentoDTO balanceamentoLancamento = (BalanceamentoLancamentoDTO) session
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
        
        if (balanceamentoLancamento == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
        }
        
        session.setAttribute(DATA_ATUAL_SELECIONADA, dataLancamentoFormatada);
        
        Date data = null;
        
        if (dataLancamentoFormatada != null && !dataLancamentoFormatada.trim().isEmpty()) {
            
            data = DateUtil.parseDataPTBR(dataLancamentoFormatada);
        }
        
        final List<ProdutoLancamentoDTO> listaProdutoBalanceamento = this.getListaProdutoBalanceamento(data,
                balanceamentoLancamento);
        
        final FiltroLancamentoDTO filtro = obterFiltroSessao();
        
        filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
        
        List<ProdutoLancamentoDTO> listaProdutoBalanceamentoNaoAgrupados = this
                .obterListaLancamentosDTONaoAgrupados(listaProdutoBalanceamento);
        
        if (listaProdutoBalanceamentoNaoAgrupados != null) {
            filtro.setTotalRegistrosEncontrados(listaProdutoBalanceamentoNaoAgrupados.size());
        }
        
        session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
        
        if (listaProdutoBalanceamentoNaoAgrupados != null && !listaProdutoBalanceamentoNaoAgrupados.isEmpty()) {
            
            processarBalanceamento(listaProdutoBalanceamentoNaoAgrupados, filtro);
            
        } else {
            
            listaProdutoBalanceamentoNaoAgrupados = new ArrayList<>();
            result.use(FlexiGridJson.class).from(listaProdutoBalanceamentoNaoAgrupados).page(page).total(0).serialize();
        }
    }
    
    private List<ProdutoLancamentoDTO> obterListaLancamentosDTONaoAgrupados(
            final List<ProdutoLancamentoDTO> listaProdutoBalanceamento) {
        
        final List<ProdutoLancamentoDTO> listaProdutoBalanceamentoClone = this.cloneObject(listaProdutoBalanceamento);
        
        this.removerProdutosAgrupados(listaProdutoBalanceamentoClone);
        
        return listaProdutoBalanceamentoClone;
    }
    
    /**
     * Obtem lista de todos os produtos de lançamento
     * 
     * @return List<ProdutoLancamentoDTO>
     */
    private List<ProdutoLancamentoDTO> getProdutoLancamentoDTOFromMatrizSessao() {
        
        final BalanceamentoLancamentoDTO balanceamentoLancamento = (BalanceamentoLancamentoDTO) session
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
        
        if (balanceamentoLancamento == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
        }
        
        final List<ProdutoLancamentoDTO> listaProdutoBalanceamento = this.getListaProdutoBalanceamento(null,
                balanceamentoLancamento);
        
        return listaProdutoBalanceamento;
    }
    
    /**
     * Obtem lista de produtos de lançamento por data
     * 
     * @param dataSelecionada
     * 
     * @return List<ProdutoLancamentoDTO>
     */
    private List<ProdutoLancamentoDTO> getProdutoLancamentoDTOFromMatrizSessao(final Date dataSelecionada) {
        
        final BalanceamentoLancamentoDTO balanceamentoLancamento = (BalanceamentoLancamentoDTO) session
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
        
        if (balanceamentoLancamento == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
        }
        
        final List<ProdutoLancamentoDTO> listaProdutoBalanceamento = this.getListaProdutoBalanceamento(dataSelecionada,
                balanceamentoLancamento);
        
        return listaProdutoBalanceamento;
    }
    
    @Post
    @Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void confirmarMatrizLancamento(final List<Date> datasConfirmadas) {
        
        this.verificarExecucaoInterfaces();
        
        final BalanceamentoLancamentoDTO balanceamentoLancamento = (BalanceamentoLancamentoDTO) session
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
        
        if (balanceamentoLancamento == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
        }
        
        this.validarDatasConfirmacao(datasConfirmadas.toArray(new Date[] {}));
        
        final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamentoSessao = balanceamentoLancamento
                .getMatrizLancamento();
        
        Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento = this.cloneObject(matrizLancamentoSessao);
        
        final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno = matrizLancamentoService
                .confirmarMatrizLancamento(matrizLancamento, datasConfirmadas, getUsuarioLogado());
        
        matrizLancamento = this.atualizarMatizComProdutosConfirmados(matrizLancamento, matrizLancamentoRetorno);
        
        balanceamentoLancamento.setMatrizLancamento(matrizLancamento);
        
        session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO, balanceamentoLancamento);
        
        this.verificarLancamentosConfirmados();
        
        result.use(Results.json()).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Balanceamento da matriz de lançamento confirmado com sucesso!"),
                "result").recursive().serialize();
    }
    
    private void validarDatasConfirmacao(final Date... datasConfirmadas) {
        
        if (datasConfirmadas == null || datasConfirmadas.length <= 0) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Selecione ao menos uma data!");
        }
        
        final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
        
        for (final Date dataConfirmada : datasConfirmadas) {
            
            if (!dataOperacao.before(dataConfirmada)) {
                
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Não é possível confirmar uma data menor que a data de operação [ "
                                + DateUtil.formatarDataPTBR(dataOperacao) + " ]");
            }
        }
    }
    
    @Post
    @Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void voltarConfiguracaoOriginal() {
        
        this.verificarExecucaoInterfaces();
        
        BalanceamentoLancamentoDTO balanceamentoLancamento = (BalanceamentoLancamentoDTO) session
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
        
        if (balanceamentoLancamento == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
        }
        
        final FiltroLancamentoDTO filtro = obterFiltroSessao();
        
        matrizLancamentoService.voltarConfiguracaoInicial(filtro.getData(), balanceamentoLancamento,
                getUsuarioLogado());
        
        // F2
        // filtro.getData(), balanceamentoLancamento.getMatrizLancamento(),
        // getUsuarioLogado());
        
        filtro.setPaginacao(null);
        filtro.setTotalRegistrosEncontrados(null);
        
        balanceamentoLancamento = this.obterBalanceamentoLancamento(filtro);
        
        final ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = this
                .obterResultadoResumoLancamento(balanceamentoLancamento);
        
        removerAtributoAlteracaoSessao();
        
        result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
    }
    
    @Post
    @Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void perguntarDataConfirmadaOuNao(final ProdutoLancamentoDTO produtoLancamento) {
        
        final boolean retornoDataConfirmada = matrizLancamentoService.isDataConfirmada(produtoLancamento);
        result.use(Results.json()).from(retornoDataConfirmada).serialize();
    }
    
    @Post
    @Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void reprogramarLancamentosSelecionados(final List<ProdutoLancamentoVO> produtosLancamento,
            final String novaDataFormatada) {
        
        this.verificarExecucaoInterfaces();
        
        this.validarDadosReprogramar(novaDataFormatada);
        
        adicionarAtributoAlteracaoSessao();
        
        final Date novaData = DateUtil.parseDataPTBR(novaDataFormatada);
        
        this.validarDatasConfirmacao(novaData);
        
        this.validarListaParaReprogramacao(produtosLancamento);
        
        this.validarDataReprogramacao(produtosLancamento, novaData);
        
        this.atualizarMapaLancamento(produtosLancamento, novaData);
        
        result.use(Results.json()).from(Results.nothing()).serialize();
    }
    
    private void removerProdutosAgrupados(final List<ProdutoLancamentoDTO> produtosLancamento) {
        
        if (produtosLancamento == null) {
            return;
        }
        
        final Iterator<ProdutoLancamentoDTO> iterator = produtosLancamento.iterator();
        
        while (iterator.hasNext()) {
            
            final ProdutoLancamentoDTO produtoLancamento = iterator.next();
            
            if (produtoLancamento.isLancamentoAgrupado()) {
                
                iterator.remove();
            }
        }
    }
    
    @Post
    @Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void reprogramarLancamentoUnico(final ProdutoLancamentoVO produtoLancamento) {
        
        this.verificarExecucaoInterfaces();
        if (produtoLancamento != null) {
            final String novaDataFormatada = produtoLancamento.getNovaDataLancamento();
            
            this.validarDadosReprogramar(novaDataFormatada);
            
            adicionarAtributoAlteracaoSessao();
            
            final Date novaData = DateUtil.parseDataPTBR(novaDataFormatada);
            
            this.validarDatasConfirmacao(novaData);
            
            final List<ProdutoLancamentoVO> produtosLancamento = new ArrayList<ProdutoLancamentoVO>();
            
            produtosLancamento.add(produtoLancamento);
            
            this.validarListaParaReprogramacao(produtosLancamento);
            
            this.validarDataReprogramacao(produtosLancamento, novaData);
            
            this.atualizarMapaLancamento(produtosLancamento, novaData);
        }
        
        result.use(Results.json()).from(Results.nothing()).serialize();
    }
    
    /**
     * Método que atualiza a matriz de lançamento de acordo com os produtos da
     * matriz de retorno
     * 
     * @param matrizLancamento - matriz de lançamento
     * @param matrizLancamentoRetorno - matriz de lançamento de retorno
     * 
     * @return matriz atualizada
     */
    private Map<Date, List<ProdutoLancamentoDTO>> atualizarMatizComProdutosConfirmados(
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno) {
        
        for (final Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : matrizLancamentoRetorno.entrySet()) {
            
            final Date novaData = entry.getKey();
            
            final List<ProdutoLancamentoDTO> produtosLancamentoRetorno = matrizLancamentoRetorno.get(novaData);
            
            matrizLancamento.put(novaData, produtosLancamentoRetorno);
        }
        
        return matrizLancamento;
    }
    
    /**
     * Método que verifica se todos os lançamentos estão confirmados para
     * remover a flag de alteração de dados da sessão.
     */
    private void verificarLancamentosConfirmados() {
        
        final List<ConfirmacaoVO> listaConfirmacao = montarListaDatasConfirmacao();
        
        boolean lancamentosConfirmados = true;
        
        for (final ConfirmacaoVO confirmacao : listaConfirmacao) {
            
            if (!confirmacao.isConfirmado()) {
                
                lancamentosConfirmados = false;
                
                break;
            }
        }
        
        if (lancamentosConfirmados) {
            
            removerAtributoAlteracaoSessao();
        }
    }
    
    /**
     * Valida os dados para reprogramação.
     * 
     * @param data - data para reprogramação
     */
    private void validarDadosReprogramar(final String data) {
        
        if (data == null || data.trim().isEmpty()) {
            
            throw new ValidacaoException(
                    new ValidacaoVO(TipoMensagem.WARNING, "O preenchimento da data é obrigatório!"));
        }
        
        if (!DateUtil.isValidDatePTBR(data)) {
            
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Data inválida!"));
        }
    }
    
    /**
     * Valida a data de reprogramação de lançamento.
     * 
     * @param produtosLancamento - lista de produtos de lançamento
     * @param novaData - nova data de recolhimento
     */
    private void validarDataReprogramacao(final List<ProdutoLancamentoVO> produtosLancamento, final Date novaData) {
        
        matrizLancamentoService.verificaDataOperacao(novaData);
        
        matrizLancamentoService.validarDiaSemanaDistribuicaoFornecedores(novaData);
        
        final List<String> listaMensagens = new ArrayList<String>();
        
        String produtos = "";
        
        final Integer qtdDiasLimiteParaReprogLancamento = distribuidorService.qtdDiasLimiteParaReprogLancamento();
        
        for (final ProdutoLancamentoVO produtoLancamento : produtosLancamento) {
            
            final String dataRecolhimentoPrevistaFormatada = produtoLancamento.getDataRecolhimentoPrevista();
            
            if (dataRecolhimentoPrevistaFormatada == null || dataRecolhimentoPrevistaFormatada.trim().isEmpty()) {
                
                continue;
            }
            
            final Date dataRecolhimentoPrevista = DateUtil.parseDataPTBR(produtoLancamento.getDataRecolhimentoPrevista());
            
            final Date dataLimiteReprogramacao = calendarioService.subtrairDiasUteisComOperacao(dataRecolhimentoPrevista,
                    qtdDiasLimiteParaReprogLancamento);
            
            if (novaData.compareTo(dataLimiteReprogramacao) == 1) {
                
                if (produtos.isEmpty()) {
                    produtos += "<table>";
                }
                
                produtos += "<tr>" + "<td><u>Produto:</u> " + produtoLancamento.getNomeProduto() + "</td>"
                    + "<td><u>Edição:</u> " + produtoLancamento.getNumeroEdicao() + "</td>"
                        + "<td><u>Data recolhimento:</u> " + dataRecolhimentoPrevistaFormatada + "</td>" + "</tr>";
            }
        }
        
        if (!produtos.isEmpty()) {
            
            listaMensagens.add("A nova data de lançamento não deve ultrapassar "
                    + "a data de recolhimento prevista menos a quantidade de dias limite ["
                    + qtdDiasLimiteParaReprogLancamento + "] para o(s) produto(s):");
            
            listaMensagens
            .add(produtos
                    + "</table> "
                        + "Para lançar o produto na data informada, é necessário alterar a data de recolhimento da(s) edição(ões).");
            
            final ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagens);
            
            throw new ValidacaoException(validacao);
        }
    }
    
    /**
     * Valida a lista de produtos informados na tela para reprogramação.
     * 
     * @param produtosLancamento - lista de produtos de lançamento
     */
    private void validarListaParaReprogramacao(final List<ProdutoLancamentoVO> produtosLancamento) {
        
        if (produtosLancamento == null || produtosLancamento.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "É necessário selecionar ao menos um produto para realizar a reprogramação!");
        }
    }
    
    /**
     * Método que atualiza o mapa de lançamento de acordo com as escolhas do
     * usuário
     * 
     * @param produtosLancamento - lista de produtos a serem alterados
     * @param novaData - nova data de lançamento
     */
    private void atualizarMapaLancamento(final List<ProdutoLancamentoVO> produtosLancamento, final Date novaData) {
        
        final BalanceamentoLancamentoDTO balanceamentoLancamentoSessao = (BalanceamentoLancamentoDTO) session
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
        
        if (balanceamentoLancamentoSessao == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
        }
        
        final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamentoSessao = balanceamentoLancamentoSessao
                .getMatrizLancamento();
        
        final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento = this.cloneObject(matrizLancamentoSessao);
        
        final List<ProdutoLancamentoDTO> listaProdutoLancamentoAlterar = new ArrayList<ProdutoLancamentoDTO>();
        List<ProdutoLancamentoDTO> listaProdutoLancamentoAlterarAux = new ArrayList<ProdutoLancamentoDTO>();
        
        this.montarListasParaAlteracaoMapa(produtosLancamento, listaProdutoLancamentoAlterar);
        
        listaProdutoLancamentoAlterarAux =  this.cloneObject(listaProdutoLancamentoAlterar);
        //Remove lancamentos expedidos de serem alterados TRAC 105
        for (final ProdutoLancamentoDTO produtoLancamentoDTO: listaProdutoLancamentoAlterarAux){
            if(produtoLancamentoDTO.getStatus().equals(StatusLancamento.EXPEDIDO)
                    || produtoLancamentoDTO.getStatus().equals(StatusLancamento.BALANCEADO)){
                listaProdutoLancamentoAlterar.remove(produtoLancamentoDTO);
            }
        }
        

        if(calendarioService.isFeriadoSemOperacao(novaData)
  	    || calendarioService.isFeriadoMunicipalSemOperacao(novaData)){
        	
            throw new ValidacaoException(
                    TipoMensagem.WARNING,
                    "A data de lançamento não pode ser alterada para uma data feriado não operante!");
            
        	
        }
  	  
        this.removerEAdicionarMapa(matrizLancamento, listaProdutoLancamentoAlterar, novaData);
        
        balanceamentoLancamentoSessao.setMatrizLancamento(matrizLancamento);
        
        session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO, balanceamentoLancamentoSessao);
    }
    
    /**
     * Cria uma cópia do objeto informado. Isso é necessário pois se houver
     * alterações na cópia, não altera os valores do objeto original por
     * referência.
     * 
     * @param object - objeto a ser clonado
     * 
     * @return objeto clonado
     */
    @SuppressWarnings("unchecked")
    private <T extends Object> T cloneObject(final T object) {
        
        final byte[] serialized = SerializationUtils.serialize(object);
        
        final T objectCloned = (T) SerializationUtils.deserialize(serialized);
        
        return objectCloned;
    }
    
    /**
     * Monta as listas para alteração do mapa da matriz de lançamento
     * 
     * @param produtosLancamento - lista de produtos de lançamento
     * @param listaProdutoLancamentoAlterar - lista de produtos que serão
     *            alterados
     */
    private void montarListasParaAlteracaoMapa(final List<ProdutoLancamentoVO> produtosLancamento,
            final List<ProdutoLancamentoDTO> listaProdutoLancamentoAlterar) {
        
        final List<ProdutoLancamentoDTO> listaProdutoLancamentoSessao = this.getProdutoLancamentoDTOFromMatrizSessao();
        
        for (final ProdutoLancamentoVO produtoLancamento : produtosLancamento) {
            
            for (final ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamentoSessao) {
                
                if (produtoLancamentoDTO.getIdLancamento().equals(Long.valueOf(produtoLancamento.getId()))) {
                    
                    listaProdutoLancamentoAlterar.add(produtoLancamentoDTO);
                    
                    final List<ProdutoLancamentoDTO> produtosLancamentoAgrupados = produtoLancamentoDTO
                            .getProdutosLancamentoAgrupados();
                    
                    if (!produtosLancamentoAgrupados.isEmpty()) {
                        
                        listaProdutoLancamentoAlterar.addAll(produtosLancamentoAgrupados);
                    }
                    
                    break;
                }
            }
        }
    }
    
    /**
     * Remove e adiona os produtos no mapa da matriz de lançamento.
     * 
     * @param matrizLancamento - mapa da matriz de lançamento
     * @param listaProdutoLancamentoAlterar - lista de produtos que serão
     *            alterados
     * @param novaData - nova data de lançamento
     */
    private void removerEAdicionarMapa(final Map<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
            final List<ProdutoLancamentoDTO> listaProdutoLancamentoAlterar, final Date novaData) {
        
        // Remover do mapa
        for (final ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamentoAlterar) {
            
            final List<ProdutoLancamentoDTO> produtosLancamentoDTO = matrizLancamento.get(produtoLancamentoDTO
                    .getNovaDataLancamento());
            
            produtosLancamentoDTO.remove(produtoLancamentoDTO);
            
            
            if (produtosLancamentoDTO.isEmpty()) {
                
                matrizLancamento.remove(produtoLancamentoDTO.getNovaDataLancamento());
                
            } else {
            	//Verificar se ja possui o produto - edicao no dia.
            	//caso exista, nao permitir que a data seje alterada trac 184
            	
            	boolean existeProdutoEdicaoDia = lancamentoRepositoryService.existeProdutoEdicaoParaDia(produtoLancamentoDTO,novaData);
            	
            	if(existeProdutoEdicaoDia){
            		
            		String stNovadata = "";
            		
            		if(novaData!=null){
            			SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
            			stNovadata = ft.format(novaData);
            		}
            		 throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, 
            				 "Já existe o produto "+produtoLancamentoDTO.getNomeProduto()+
            				 " , edição "+produtoLancamentoDTO.getNumeroEdicao()+
            				 " para o dia "+stNovadata));
            	}
            	
                produtoLancamentoDTO.setAlterado(true);
                produtoLancamentoDTO.setStatus(StatusLancamento.CONFIRMADO);
                produtoLancamentoDTO.setStatusLancamento(StatusLancamento.CONFIRMADO.name());
                produtoLancamentoDTO.setDataLancamentoDistribuidor(novaData);
                matrizLancamento.put(produtoLancamentoDTO.getNovaDataLancamento(), produtosLancamentoDTO);
            }
        }
        
        // Adicionar no mapa
        for (final ProdutoLancamentoDTO produtoLancamentoAdicionar : listaProdutoLancamentoAlterar) {
            
            List<ProdutoLancamentoDTO> produtosLancamento = matrizLancamento.get(novaData);
            
            if (produtosLancamento == null) {
                
                produtosLancamento = new ArrayList<ProdutoLancamentoDTO>();
            }
            
            produtoLancamentoAdicionar.setNovaDataLancamento(novaData);
            produtoLancamentoAdicionar.setStatus(StatusLancamento.CONFIRMADO);
            
            matrizLancamentoService.tratarAgrupamentoPorProdutoDataLcto(produtoLancamentoAdicionar, produtosLancamento);
            
            produtosLancamento.add(produtoLancamentoAdicionar);
            
            matrizLancamento.put(novaData, produtosLancamento);
        }
    }
    
    private void processarBalanceamento(List<ProdutoLancamentoDTO> listaProdutoLancamento, final FiltroLancamentoDTO filtro) {
        
        final PaginacaoVO paginacao = filtro.getPaginacao();
        
        final Double valorTotal = this.getValorTotal(listaProdutoLancamento);
        
        final List<ProdutoLancamentoVO> listaProdutoBalanceamentoVO = getProdutosLancamentoVO(listaProdutoLancamento);
        
        listaProdutoLancamento = PaginacaoUtil.paginarEOrdenarEmMemoria(listaProdutoLancamento, paginacao, paginacao
                .getSortColumn());
        
        final List<ProdutoLancamentoVO> listaProdutoBalanceamentoPaginacaoVO = getProdutosLancamentoVO(listaProdutoLancamento);
        
        final TableModel<CellModelKeyValue<ProdutoLancamentoVO>> tm = new TableModel<CellModelKeyValue<ProdutoLancamentoVO>>();
        final List<CellModelKeyValue<ProdutoLancamentoVO>> cells = CellModelKeyValue
                .toCellModelKeyValue(listaProdutoBalanceamentoPaginacaoVO);
        
        final List<Object> resultado = new ArrayList<Object>();
        
        tm.setRows(cells);
        
        tm.setPage(paginacao.getPaginaAtual());
        
        tm.setTotal(filtro.getTotalRegistrosEncontrados());
        
        resultado.add(tm);
        resultado.add(CurrencyUtil.formatarValor(valorTotal));
        resultado.add(listaProdutoBalanceamentoVO);
        
        result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
        
    }
    
    private Double getValorTotal(final List<ProdutoLancamentoDTO> listaProdutoLancamento) {
        
        Double valorTotal = 0.0;
        
        for (final ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamento) {
            
            valorTotal += produtoLancamentoDTO.getValorTotal().doubleValue();
        }
        
        return valorTotal;
    }
    
    private List<ProdutoLancamentoVO> getProdutosLancamentoVO(final List<ProdutoLancamentoDTO> listaProdutoLancamento) {
        
        final List<ProdutoLancamentoVO> listaProdutoBalanceamentoVO = new LinkedList<ProdutoLancamentoVO>();
        
        for (final ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamento) {
            
            listaProdutoBalanceamentoVO.add(getVoProdutoBalanceamento(produtoLancamentoDTO));
        }
        
        return listaProdutoBalanceamentoVO;
    }
    
    private ProdutoLancamentoVO getVoProdutoBalanceamento(final ProdutoLancamentoDTO produtoLancamentoDTO) {
        
        final ProdutoLancamentoVO produtoBalanceamentoVO = new ProdutoLancamentoVO();
        
        produtoBalanceamentoVO.setCodigoProduto(produtoLancamentoDTO.getCodigoProduto());
        
        produtoBalanceamentoVO.setNovaDataLancamento(DateUtil.formatarDataPTBR(produtoLancamentoDTO
                .getNovaDataLancamento()));
        
        produtoBalanceamentoVO.setDataLancamentoPrevista(DateUtil.formatarDataPTBR(produtoLancamentoDTO
                .getDataLancamentoPrevista()));
        
        produtoBalanceamentoVO.setDataRecolhimentoPrevista(DateUtil.formatarDataPTBR(produtoLancamentoDTO
                .getDataRecolhimentoPrevista()));
        
        produtoBalanceamentoVO.setId(produtoLancamentoDTO.getIdLancamento());
        
        produtoBalanceamentoVO.setDescricaoLancamento(produtoLancamentoDTO.getDescricaoLancamento());
        
        produtoBalanceamentoVO.setNomeProduto(produtoLancamentoDTO.getNomeProduto());
        produtoBalanceamentoVO.setNumeroEdicao(produtoLancamentoDTO.getNumeroEdicao());
        
        produtoBalanceamentoVO.setPrecoVenda(CurrencyUtil.formatarValor(produtoLancamentoDTO.getPrecoVenda()));
        
        produtoBalanceamentoVO.setRepartePrevisto(produtoLancamentoDTO.getRepartePrevisto().toString());
        
        produtoBalanceamentoVO.setValorTotal(CurrencyUtil.formatarValor(produtoLancamentoDTO.getValorTotal()));
        
        if (produtoLancamentoDTO.getReparteFisico() == null) {
            produtoBalanceamentoVO.setReparteFisico("0");
        } else {
            produtoBalanceamentoVO.setReparteFisico(produtoLancamentoDTO.getReparteFisico().toString());
        }
        
        if (produtoLancamentoDTO.getDistribuicao() == null) {
            produtoBalanceamentoVO.setDistribuicao("");
        } else {
            produtoBalanceamentoVO.setDistribuicao(produtoLancamentoDTO.getDistribuicao().toString());
        }
        
        produtoBalanceamentoVO.setBloquearData(matrizLancamentoService.isProdutoConfirmado(produtoLancamentoDTO));
        
        produtoBalanceamentoVO.setIdProdutoEdicao(produtoLancamentoDTO.getIdProdutoEdicao());
        
        produtoBalanceamentoVO.setPossuiFuro(produtoLancamentoDTO.isPossuiFuro());
        
        final String reparteFisico = produtoBalanceamentoVO.getReparteFisico();
        final String distribuicao = produtoBalanceamentoVO.getDistribuicao();
        
        if ((reparteFisico.equals("0") || reparteFisico.equals("") || distribuicao.equals("0") || distribuicao
                .equals(""))
                && !produtoLancamentoDTO.getStatus().equals(StatusLancamento.EXPEDIDO)) {
            
            produtoBalanceamentoVO.setDestacarLinha(true);
        }
        
        produtoBalanceamentoVO.setPeb(produtoLancamentoDTO.getPeb());
        
        produtoBalanceamentoVO.setStatusLancamento(produtoLancamentoDTO.getStatusLancamento());
        
        produtoBalanceamentoVO.setFornecedorId(produtoLancamentoDTO.getIdFornecedor());
        
        if (validarStatusParaExclusaoLancamento(produtoLancamentoDTO.getStatus())) {
            
            produtoBalanceamentoVO.setCancelado(true);
            
        } else {
            
            produtoBalanceamentoVO.setCancelado(false);
        }
        
        return produtoBalanceamentoVO;
    }
    
    private boolean validarStatusParaExclusaoLancamento(final StatusLancamento status) {
        
        return StatusLancamento.CONFIRMADO.equals(status) || StatusLancamento.EM_BALANCEAMENTO.equals(status)
                || StatusLancamento.PLANEJADO.equals(status) || StatusLancamento.FURO.equals(status);
    }
    
    @Exportable
    public class RodapeDTO {
        
        @Export(label = "Valor Total R$:")
        private final String total;
        
        public RodapeDTO(final String total) {
            this.total = total;
        }
        
        public String getTotal() {
            return total;
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
    public void exportar(final FileType fileType) throws IOException {
        
        final String dataSelecionada = (String) session.getAttribute(DATA_ATUAL_SELECIONADA);
        
        Date data = null;
        
        if (dataSelecionada != null && !dataSelecionada.trim().isEmpty()) {
            
            data = DateUtil.parseDataPTBR(dataSelecionada);
        }
        
        final List<ProdutoLancamentoDTO> listaProdutoBalanceamento = getProdutoLancamentoDTOFromMatrizSessao(data);
        
        final FiltroLancamentoDTO filtro = obterFiltroSessao();
        
        if (listaProdutoBalanceamento != null && !listaProdutoBalanceamento.isEmpty()) {
            
            final Double valorTotal = getValorTotal(listaProdutoBalanceamento);
            
            final List<ProdutoLancamentoVO> listaProdutoBalanceamentoVO = getProdutosLancamentoVO(listaProdutoBalanceamento);
            
            final RodapeDTO rodape = new RodapeDTO(CurrencyUtil.formatarValor(valorTotal));
            
            FileExporter.to("matriz_balanceamento", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, rodape,
                    listaProdutoBalanceamentoVO, ProdutoLancamentoVO.class, httpResponse);
        }
        
        result.nothing();
    }
    
    private String montarNomeFornecedores(final List<Long> idsFornecedores) {
        
        String nomeFornecedores = "";
        
        final List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresPorId(idsFornecedores);
        
        if (listaFornecedor != null && !listaFornecedor.isEmpty()) {
            
            for (final Fornecedor fornecedor : listaFornecedor) {
                
                if (!nomeFornecedores.isEmpty()) {
                    
                    nomeFornecedores += " / ";
                }
                
                nomeFornecedores += fornecedor.getJuridica().getRazaoSocial();
            }
        }
        
        return nomeFornecedores;
    }
    
    /**
     * Obtém a matriz de balanceamento de balanceamento.
     * 
     * @param dataBalanceamento - data de balanceamento
     * @param listaIdsFornecedores - lista de identificadores dos fornecedores
     * 
     * @return - objeto contendo as informações do balanceamento
     */
    private BalanceamentoLancamentoDTO obterBalanceamentoLancamento(final FiltroLancamentoDTO filtro) {
        
        final BalanceamentoLancamentoDTO balanceamento = matrizLancamentoService.obterMatrizLancamento(filtro);
        
        session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO, balanceamento);
        
        if (balanceamento == null || balanceamento.getMatrizLancamento() == null
                || balanceamento.getMatrizLancamento().isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Não houve carga de informações para o período escolhido!");
        }
        
        return balanceamento;
    }
    
    /**
     * Configura o filtro informado na tela e o armazena na sessão.
     * 
     * @param dataPesquisa - data da pesquisa
     * @param listaIdsFornecedores - lista de identificadores de fornecedores
     */
    private FiltroLancamentoDTO configurarFiltropesquisa(final Date dataPesquisa, final List<Long> listaIdsFornecedores) {
        
        final FiltroLancamentoDTO filtro = new FiltroLancamentoDTO(dataPesquisa, listaIdsFornecedores);
        
        filtro.setNomesFornecedor(this.montarNomeFornecedores(listaIdsFornecedores));
        
        session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
        
        return filtro;
    }
    
    /**
     * Valida os dados da pesquisa.
     * 
     * @param numeroSemana - número da semana
     * @param dataPesquisa - data da pesquisa
     * @param listaIdsFornecedores - lista de id's dos fornecedores
     */
    private void validarDadosPesquisa(final Date dataPesquisa, final List<Long> listaIdsFornecedores) {
        
        final List<String> listaMensagens = new ArrayList<String>();
        
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
     * Obtém o resumo do período de balanceamento de acordo com a data da
     * pesquisa e a lista de id's dos fornecedores.
     */
    private ResultadoResumoBalanceamentoVO obterResultadoResumoLancamento(
            final BalanceamentoLancamentoDTO balanceamentoBalanceamento) {
        
        if (balanceamentoBalanceamento == null || balanceamentoBalanceamento.getMatrizLancamento() == null
                || balanceamentoBalanceamento.getMatrizLancamento().isEmpty()) {
            
            return null;
        }
        
        final List<ResumoPeriodoBalanceamentoVO> resumoPeriodoBalanceamento = new ArrayList<ResumoPeriodoBalanceamentoVO>();
        
        for (final Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : balanceamentoBalanceamento.getMatrizLancamento()
                .entrySet()) {
            
            final Date dataRecolhimento = entry.getKey();
            
            final ResumoPeriodoBalanceamentoVO itemResumoPeriodoBalanceamento = new ResumoPeriodoBalanceamentoVO();
            
            itemResumoPeriodoBalanceamento.setData(dataRecolhimento);
            
            final List<ProdutoLancamentoDTO> listaProdutosRecolhimento = entry.getValue();
            
            if (listaProdutosRecolhimento != null && !listaProdutosRecolhimento.isEmpty()) {
                
                final boolean exibeDestaque = false;
                
                Long qtdeTitulos = 0L;
                Long qtdeTitulosParciais = 0L;
                
                Long pesoTotal = 0L;
                BigInteger qtdeExemplares = BigInteger.ZERO;
                BigDecimal valorTotal = BigDecimal.ZERO;
                
                for (final ProdutoLancamentoDTO produtoBalanceamento : listaProdutosRecolhimento) {
                    
                    if (produtoBalanceamento.isLancamentoAgrupado()) {
                        
                        continue;
                    }
                    
                    if (produtoBalanceamento.getParcial() != null) {
                        
                        qtdeTitulosParciais++;
                    }
                    
                    if (produtoBalanceamento.getPeso() != null) {
                        
                        pesoTotal += produtoBalanceamento.getPeso();
                    }
                    
                    if (produtoBalanceamento.getValorTotal() != null) {
                        
                        valorTotal = valorTotal.add(produtoBalanceamento.getValorTotal());
                    }
                    
                    if (produtoBalanceamento.getRepartePrevisto() != null) {
                        
                        qtdeExemplares = qtdeExemplares.add(produtoBalanceamento.getRepartePrevisto());
                    }
                    
                    qtdeTitulos++;
                }
                
                boolean excedeCapacidadeDistribuidor = false;
                
                if (balanceamentoBalanceamento.getCapacidadeDistribuicao() != null) {
                    
                    excedeCapacidadeDistribuidor = balanceamentoBalanceamento.getCapacidadeDistribuicao().compareTo(
                            qtdeExemplares) == -1;
                }
                
                itemResumoPeriodoBalanceamento.setExcedeCapacidadeDistribuidor(excedeCapacidadeDistribuidor);
                
                itemResumoPeriodoBalanceamento.setExibeDestaque(exibeDestaque);
                itemResumoPeriodoBalanceamento.setPesoTotal(new BigDecimal(pesoTotal / 1000));
                itemResumoPeriodoBalanceamento.setQtdeExemplares(qtdeExemplares);
                itemResumoPeriodoBalanceamento.setQtdeTitulos(qtdeTitulos);
                
                itemResumoPeriodoBalanceamento.setQtdeTitulosParciais(qtdeTitulosParciais);
                
                itemResumoPeriodoBalanceamento.setValorTotal(valorTotal);
            }
            
            resumoPeriodoBalanceamento.add(itemResumoPeriodoBalanceamento);
        }
        
        final ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = new ResultadoResumoBalanceamentoVO();
        
        resultadoResumoBalanceamento.setListaResumoPeriodoBalanceamento(resumoPeriodoBalanceamento);
        
        resultadoResumoBalanceamento.setCapacidadeRecolhimentoDistribuidor(balanceamentoBalanceamento
                .getCapacidadeDistribuicao());
        
        resultadoResumoBalanceamento.setListaProdutosLancamentosCancelados(balanceamentoBalanceamento
                .getProdutosLancamentosCancelados());
        
        return resultadoResumoBalanceamento;
    }
    
    /**
     * Obtém o filtro para pesquisa da sessão.
     * 
     * @return filtro
     */
    private FiltroLancamentoDTO obterFiltroSessao() {
        
        final FiltroLancamentoDTO filtro = (FiltroLancamentoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
        
        if (filtro == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
        }
        
        return filtro;
    }
    
    /**
     * Obtem agrupamento diário para confirmação de Balanceamento
     */
    @Post
    public void obterAgrupamentoDiarioBalanceamento() {
        
        final List<ConfirmacaoVO> confirmacoesVO = this.montarListaDatasConfirmacao();
        final List<ConfirmacaoVO> confirmacoesAuxVO = new ArrayList<>();
        
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        
        for (final ConfirmacaoVO confirmacaoVO : confirmacoesVO) {
            
            try {
                
                data = format.parse(confirmacaoVO.getMensagem());
                
            } catch (final ParseException ex) {
                
            }
            
            if (!confirmacaoVO.isConfirmado()) {
                
                if (distribuidorService.obterDataOperacaoDistribuidor().getTime() <= data.getTime()) {
                    
                    confirmacoesAuxVO.add(confirmacaoVO);
                }
            }
        }
        
        if (confirmacoesAuxVO != null) {
            
            result.use(Results.json()).from(confirmacoesAuxVO, "result").serialize();
        }
    }
    
    /**
     * Obtem a concentração ordenada e agrupada por data para a Matriz de
     * Lançamento
     * 
     * @return List<ConfirmacaoVO>: confirmacoesVO
     */
    private List<ConfirmacaoVO> montarListaDatasConfirmacao() {
        
        final BalanceamentoLancamentoDTO balanceamentoLancamento = (BalanceamentoLancamentoDTO) session
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
        
        if (balanceamentoLancamento == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
        }
        
        final List<ConfirmacaoVO> confirmacoesVO = matrizLancamentoService
                .obterDatasConfirmacao(balanceamentoLancamento);
        
        return confirmacoesVO;
    }
    
    @Post
    public void verificarBalanceamentosAlterados() {
        
        Boolean balanceamentoAlterado = (Boolean) session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO);
        
        if (balanceamentoAlterado == null) {
            
            balanceamentoAlterado = false;
        }
        
        result.use(Results.json()).from(balanceamentoAlterado.toString(), "result").serialize();
    }
    
    /**
     * Adiciona um indicador, que informa se houve reprogramação de produtos, na
     * sessão.
     */
    private void adicionarAtributoAlteracaoSessao() {
        
        session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO, true);
    }
    
    /**
     * Remove um indicador, que informa se houve reprogramação de produtos, da
     * sessão.
     */
    private void removerAtributoAlteracaoSessao() {
        
        session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO, null);
    }
    
    @Post
    @Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void atualizarResumoBalanceamento() {
        
        final BalanceamentoLancamentoDTO balanceamentoLancamento = (BalanceamentoLancamentoDTO) session
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_LANCAMENTO);
        
        if (balanceamentoLancamento == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
        }
        
        final ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = this
                .obterResultadoResumoLancamento(balanceamentoLancamento);
        
        result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
    }
    
    private void verificarExecucaoInterfaces() {
        if (distribuidorService.verificaDesbloqueioProcessosLancamentosEstudos()) {
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "As interfaces encontram-se em processamento. Aguarde o termino da execução para continuar!");
        }
    }
    
    @Get
    public void obterDatasConfirmadasReabertura() {
        
        final List<ConfirmacaoVO> confirmacoesVO = this.montarListaDatasConfirmacao();
        
        final List<String> datasConfirmadasReabertura = new ArrayList<>();
        
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        
        for (final ConfirmacaoVO confirmacaoVO : confirmacoesVO) {
            
            try {
                
                data = format.parse(confirmacaoVO.getMensagem());
                
            } catch (final ParseException ex) {
                
            }
            
            if (confirmacaoVO.isConfirmado()) {
                
                if (distribuidorService.obterDataOperacaoDistribuidor().before(data)) {
                    
                    datasConfirmadasReabertura.add(confirmacaoVO.getMensagem());
                }
            }
        }
        
        result.use(Results.json()).from(datasConfirmadasReabertura, "result").serialize();
    }
    
    @Post
    public void reabrirMatriz(final List<Date> datasReabertura, final Date dataLancamento, final List<Long> idsFornecedores) {
        
        if (datasReabertura == null || datasReabertura.isEmpty()) {
            
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhuma data foi selecionada!"));
        }
        matrizLancamentoService.reabrirMatriz(datasReabertura, getUsuarioLogado());
        
        validarDadosPesquisa(dataLancamento, idsFornecedores);
        
        removerAtributoAlteracaoSessao();
        
        final FiltroLancamentoDTO filtro = configurarFiltropesquisa(dataLancamento, idsFornecedores);
        
        // Recarrega o objeto na sessao
        this.obterBalanceamentoLancamento(filtro);
        
        result.use(PlainJSONSerialization.class).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Reabertura realizada com sucesso!"), "result").recursive()
                .serialize();
    }
    
    @Post
    @Rules(Permissao.ROLE_LANCAMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void excluirLancamento(final ProdutoLancamentoVO produtoLancamento) {
        
        final Date data = DateUtil.parseDataPTBR(dataCabalistica);
        
        final Lancamento lancamento = lancamentoRepositoryService.buscarPorId(produtoLancamento.getId());
        
        lancamento.setDataLancamentoDistribuidor(data);
        lancamento.voltarStatusOriginal();
        // atualizarLancamento(produtoLancamento.getId(),data);
        
        lancamentoRepositoryService.merge(lancamento);
        
        result.use(PlainJSONSerialization.class).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Excluido com sucesso!"), "result").recursive().serialize();
        
    }
    
}
