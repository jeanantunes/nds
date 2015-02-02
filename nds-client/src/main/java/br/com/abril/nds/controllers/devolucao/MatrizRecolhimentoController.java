package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.DataHolder;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.FiltroPesquisaMatrizRecolhimentoVO;
import br.com.abril.nds.client.vo.ProdutoRecolhimentoDiferenciadoVO;
import br.com.abril.nds.client.vo.ProdutoRecolhimentoFormatadoVO;
import br.com.abril.nds.client.vo.ProdutoRecolhimentoVO;
import br.com.abril.nds.client.vo.ResultadoResumoBalanceamentoVO;
import br.com.abril.nds.client.vo.ResumoPeriodoBalanceamentoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.CotaOperacaoDiferenciadaDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.StatusRecebimento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.DistribuicaoFornecedorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.GrupoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoBalanceamentoRecolhimento;
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

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

/**
 * Controller responsável pela Matriz de Recolhimento.
 * 
 * @author Discover Technology
 * 
 */
@Resource
@Path("/devolucao/balanceamentoMatriz")
@Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ)
public class MatrizRecolhimentoController extends BaseController {
    
    @Autowired
    private HttpSession httpSession;
    
    @Autowired
    private HttpServletResponse response;
    
    @Autowired
    private Result result;
    
    @Autowired
    private RecolhimentoService recolhimentoService;
    
    @Autowired
    private FornecedorService fornecedorService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private DistribuicaoFornecedorService distribuicaoFornecedorService;
    
    @Autowired
    private GrupoService grupoService;
    
    @Autowired
    private UsuarioService usuarioService; 
    
    @Autowired
    private LancamentoService lancamentoService;
    
    private static final String ATRIBUTO_SESSAO_FILTRO_PESQUISA_BALANCEAMENTO_RECOLHIMENTO = "filtroPesquisaBalanceamentoRecolhimento";
    
    private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO = "balanceamentoRecolhimento";
    
    private static final String ATRIBUTO_SESSAO_VALIDACAO = "validacao";
    
    private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO = "balanceamentoAlterado";
    
    private static final String ATRIBUTO_SESSAO_PRODUTOS_RECOLHIMENTO = "produtosRecolhimento";

	private static final String SORT_NAME_CODIGO_PRODUTO = "codigoProduto";

	private static final String SORT_NAME_NOME_PRODUTO = "nomeProduto";
	
	private static final String SORT_NAME_NUMERO_EDICAO = "numeroEdicao";
	
	public static final String TRAVA_MATRIZ_RECOLHIMENTO_CONTEXT_ATTRIBUTE = "trava_matriz_recolhimento";
	
	private static final String DATA_HOLDER_ACTION_KEY = "matrizRecolhimentoDataHolder";
    
    @Get
    @Path("/")
    public void index() {
        
        List<Fornecedor> fornecedores = this.fornecedorService.obterFornecedores(SituacaoCadastro.ATIVO);
        
        removerAtributoAlteracaoSessao();
        
        this.result.include("fornecedores", fornecedores);
        
    }
    
    @Post
    @Path("/pesquisar")
    public void pesquisar(Integer anoNumeroSemana, Date dataPesquisa, List<Long> listaIdsFornecedores) {
        
        dataPesquisa = this.tratarData(anoNumeroSemana, dataPesquisa);
        
        anoNumeroSemana = this.tratarSemana(anoNumeroSemana, dataPesquisa);
        
        this.validarDadosPesquisa(dataPesquisa, listaIdsFornecedores);
        
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = null;
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimentoAux = null;
        List<Long> porFornenedor = null;
        
        for (int i = 0; i < listaIdsFornecedores.size(); i++) {
            
            porFornenedor = new ArrayList<Long>();
            porFornenedor.add(listaIdsFornecedores.get(i));
            
            if (balanceamentoRecolhimento == null || 
            	balanceamentoRecolhimento.getMatrizRecolhimento() == null ||
            	balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()
            	) {
                
            	balanceamentoRecolhimento = this.obterBalanceamentoRecolhimento(anoNumeroSemana, porFornenedor,
                        TipoBalanceamentoRecolhimento.AUTOMATICO, false);
                
            } else {
                
                balanceamentoRecolhimentoAux = this.obterBalanceamentoRecolhimento(anoNumeroSemana, porFornenedor,
                        TipoBalanceamentoRecolhimento.AUTOMATICO, false);
                
                if (balanceamentoRecolhimentoAux.getCapacidadeRecolhimentoDistribuidor() != null)
                    balanceamentoRecolhimento.addCapacidadeRecolhimentoDistribuidor(balanceamentoRecolhimentoAux
                            .getCapacidadeRecolhimentoDistribuidor());
                if (balanceamentoRecolhimentoAux.getMediaRecolhimentoDistribuidor() != 0)
                    balanceamentoRecolhimento.addMediaRecolhimentoDistribuidor(balanceamentoRecolhimentoAux
                            .getMediaRecolhimentoDistribuidor());
                if (balanceamentoRecolhimentoAux.getCotasOperacaoDiferenciada() != null)
                    balanceamentoRecolhimento.addCotasOperacaoDiferenciada(balanceamentoRecolhimentoAux
                            .getCotasOperacaoDiferenciada());
                if (balanceamentoRecolhimentoAux.getMatrizRecolhimento() != null)
                    balanceamentoRecolhimento.addMatrizRecolhimento(balanceamentoRecolhimentoAux
                            .getMatrizRecolhimento());
                if (balanceamentoRecolhimentoAux.getProdutosRecolhimentoAgrupados() != null)
                    balanceamentoRecolhimento.addProdutosRecolhimentoAgrupados(balanceamentoRecolhimentoAux
                            .getProdutosRecolhimentoAgrupados());
                if (balanceamentoRecolhimentoAux.getProdutosRecolhimentoNaoBalanceados() != null)
                    balanceamentoRecolhimento.addProdutosRecolhimentoNaoBalanceados(balanceamentoRecolhimentoAux
                            .getProdutosRecolhimentoNaoBalanceados());
                if (balanceamentoRecolhimentoAux.getProdutosRecolhimentoDeOutraSemana() != null)
                    balanceamentoRecolhimento.addProdutosRecolhimentoDeOutraSemana(balanceamentoRecolhimentoAux
                            .getProdutosRecolhimentoDeOutraSemana());
                
            }
            this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO, balanceamentoRecolhimento);
        }
        
        if (balanceamentoRecolhimento == null || balanceamentoRecolhimento.getMatrizRecolhimento() == null
            || balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Não houve carga de informações para o período escolhido!");
        }
        
        ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
        
        boolean utilizaSedeAtendida = grupoService.countTodosGrupos(dataPesquisa) > 0;
        
        resultadoResumoBalanceamento.setUtilizaSedeAtendida(utilizaSedeAtendida);
        
		resultadoResumoBalanceamento.setProdutosRecolhimentoDeOutraSemana(balanceamentoRecolhimento.getProdutosRecolhimentoDeOutraSemana());
		
        removerAtributoAlteracaoSessao();
        
        configurarFiltropesquisa(anoNumeroSemana, dataPesquisa, listaIdsFornecedores);
        
        processarProdutosNaoBalanceadosAposConfirmacaoMatriz(balanceamentoRecolhimento.getProdutosRecolhimentoNaoBalanceados());


        this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
    }
    
    private void processarProdutosNaoBalanceadosAposConfirmacaoMatriz(List<ProdutoRecolhimentoDTO> produtosRecolhimentoNaoBalanceados) {
        
        verificarExecucaoInterfaces();
        
        FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
        
        recolhimentoService.processarProdutosProximaSemanaRecolhimento(produtosRecolhimentoNaoBalanceados, filtro.getAnoNumeroSemana());
    }
    
    private Integer tratarSemana(Integer anoNumeroSemana, Date dataPesquisa) {
        
        if (anoNumeroSemana == null && dataPesquisa != null) {
            
            return SemanaUtil.obterAnoNumeroSemana(dataPesquisa, this.distribuidorService.inicioSemanaRecolhimento()
                    .getCodigoDiaSemana());
        }
        
        return anoNumeroSemana;
    }
    
    private Date tratarData(Integer anoNumeroSemana, Date dataPesquisa) {
        
        if (anoNumeroSemana != null && dataPesquisa == null) {
            
            int anoBase = SemanaUtil.getAno(anoNumeroSemana);
            
            return SemanaUtil.obterDataDaSemanaNoAno(anoNumeroSemana, this.distribuidorService.inicioSemanaRecolhimento()
                    .getCodigoDiaSemana(), anoBase);
        }
        
        return dataPesquisa;
    }
    
    @Post
    @Path("/confirmar")
    @Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void confirmar(List<Date> datasConfirmadas,Date dataPesquisa) {
        
        this.verificarBloqueioMatrizRecolhimento();
        
        verificarExecucaoInterfaces();
        
        if (datasConfirmadas == null || datasConfirmadas.size() <= 0) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Ao menos uma data deve ser selecionada!");
        }
        
        this.salvarMatrizRecolhimento(StatusLancamento.EXPEDIDO, dataPesquisa);
        
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = (BalanceamentoRecolhimentoDTO) this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
        
        validarDatasBalanceamentoMatriz(balanceamentoRecolhimento.getMatrizRecolhimento(), datasConfirmadas);
        
        FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
        
        TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = this
                .clonarMapaRecolhimento(balanceamentoRecolhimento.getMatrizRecolhimento());
        
        Usuario usuario = getUsuarioLogado();
        
        TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizConfirmada = recolhimentoService
                .confirmarBalanceamentoRecolhimento(matrizRecolhimento, filtro.getAnoNumeroSemana(), datasConfirmadas,
                        usuario, balanceamentoRecolhimento.getProdutosRecolhimentoAgrupados());
        
        matrizRecolhimento = this.atualizarMatizComProdutosConfirmados(matrizRecolhimento, matrizConfirmada);
        
        balanceamentoRecolhimento.setMatrizRecolhimento(matrizRecolhimento);
        
        this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO, balanceamentoRecolhimento);
        
        this.verificarBalanceamentosConfirmados();
        
        result.use(Results.json())
                .from(new ValidacaoVO(TipoMensagem.SUCCESS,
                        "Balanceamento da matriz de recolhimento confirmado com sucesso!"), "result").recursive()
                .serialize();
    }
    
    private void validarDatasBalanceamentoMatriz(TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
            List<Date> datasConfirmadas) {
        
        List<String> listaMensagens = new ArrayList<String>();
        
        for (Date data : datasConfirmadas) {
            
            String produtos = "";
            
            List<ProdutoRecolhimentoDTO> produtosRecolhimento = matrizRecolhimento.get(data);
            
            String dataRecolhimentoFormatada = DateUtil.formatarDataPTBR(data);
            
            for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
                
                if (produtoRecolhimento.getNovaData().compareTo(produtoRecolhimento.getDataLancamento()) < 0) {
                    
                    produtos += "<tr>" + "<td><u>Produto:</u> " + produtoRecolhimento.getNomeProduto() + "</td>"
                        + "<td><u>Edição:</u> " + produtoRecolhimento.getNumeroEdicao() + "</td>"
                        + "<td><u>Data recolhimento:</u> " + dataRecolhimentoFormatada + "</td>" + "</tr>";
                }
            }
            
            if (!produtos.isEmpty()) {
                
                listaMensagens.add(" A nova data de lançamento não deve ultrapassar"
                    + " a data de recolhimento prevista");
                
                listaMensagens.add("<table>" + produtos + "</table>");
                
                throw new ValidacaoException(TipoMensagem.WARNING, listaMensagens);
            }
        }
    }
    
    @Post
    @Path("/balancearPorEditor")
    @Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void balancearPorEditor() {
        
        verificarExecucaoInterfaces();
        
        FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
        
        this.validarDadosPesquisa(filtro.getDataPesquisa(), filtro.getListaIdsFornecedores());
        
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = this.obterBalanceamentoRecolhimento(filtro
                .getAnoNumeroSemana(), filtro.getListaIdsFornecedores(), TipoBalanceamentoRecolhimento.EDITOR, true);
        
        ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = this
                .obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
        
        resultadoResumoBalanceamento.setProdutosRecolhimentoDeOutraSemana(
            balanceamentoRecolhimento.getProdutosRecolhimentoDeOutraSemana());
        
        removerAtributoAlteracaoSessao();
        
        processarProdutosNaoBalanceadosAposConfirmacaoMatriz(
            balanceamentoRecolhimento.getProdutosRecolhimentoNaoBalanceados());
        
        this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
    }
    
    @Get
    public void exportar(FileType fileType) throws IOException {
        
        if (fileType == null) {
            
            throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
        }
        
        FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
        
        List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO = obterListaProdutoRecolhimentoDTO(DateUtil
                .formatarDataPTBR(filtro.getDataPesquisa()));
        
        List<ProdutoRecolhimentoVO> listaProdutoRecolhimentoVO;
        List<ProdutoRecolhimentoDiferenciadoVO> listaProdutoRecolhimentoDiferenciadoVO;
        
        // Contem Operação Diferenciada
        boolean isDiferenciada = false;
        
        // Verifica se 0s para sede e atendida. Se sim não é uma operação
        // diferenciada
        for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoDTO) {
            
            if ((produtoRecolhimentoDTO.getExpectativaEncalheSede() != null 
            		&& produtoRecolhimentoDTO.getExpectativaEncalheSede().compareTo(BigDecimal.ZERO) != 0)
                || (produtoRecolhimentoDTO.getExpectativaEncalheAtendida() != null 
                	&& produtoRecolhimentoDTO.getExpectativaEncalheAtendida().compareTo(BigDecimal.ZERO) != 0)) {
                isDiferenciada = true;
                break;
            }
            
        }
        
        if (!isDiferenciada) {
            
            listaProdutoRecolhimentoVO = obterListaProdutoRecolhimentoVO(listaProdutoRecolhimentoDTO);
            
            PaginacaoUtil.ordenarEmMemoria(listaProdutoRecolhimentoVO, filtro.getPaginacaoVO().getOrdenacao(), filtro
                    .getPaginacaoVO().getSortColumn());
            
            FileExporter.to("matriz_recolhimento", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro,
                    listaProdutoRecolhimentoVO, ProdutoRecolhimentoVO.class, this.response);
            
        } else {
            
            listaProdutoRecolhimentoDiferenciadoVO = obterListaProdutoRecolhimentoDiferenciadoVO(listaProdutoRecolhimentoDTO);
            
            PaginacaoUtil.ordenarEmMemoria(listaProdutoRecolhimentoDiferenciadoVO, filtro.getPaginacaoVO()
                    .getOrdenacao(), filtro.getPaginacaoVO().getSortColumn());
            
            FileExporter.to("matriz_recolhimento", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro,
                    listaProdutoRecolhimentoDiferenciadoVO, ProdutoRecolhimentoDiferenciadoVO.class, this.response);
            
        }
        
    }
    
    @Post
    @Path("/balancearPorValor")
    @Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void balancearPorValor() {
        
        verificarExecucaoInterfaces();
        
        FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
        
        this.validarDadosPesquisa(filtro.getDataPesquisa(), filtro.getListaIdsFornecedores());
        
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = this.obterBalanceamentoRecolhimento(filtro
                .getAnoNumeroSemana(), filtro.getListaIdsFornecedores(), TipoBalanceamentoRecolhimento.VALOR, true);
        
        ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = this
                .obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
        
        resultadoResumoBalanceamento.setProdutosRecolhimentoDeOutraSemana(
            balanceamentoRecolhimento.getProdutosRecolhimentoDeOutraSemana());
        
        removerAtributoAlteracaoSessao();
        
        processarProdutosNaoBalanceadosAposConfirmacaoMatriz(
            balanceamentoRecolhimento.getProdutosRecolhimentoNaoBalanceados());
        
        this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
    }
    
    
    @Post
    @Path("/alterarStatusEmBalanceamentoRecolhimento")
    @Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void alterarStatusEmBalanceamentoRecolhimento(Date dataPesquisa) {
        
        salvarMatrizRecolhimento(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO,dataPesquisa);
        
        result.use(Results.json()).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Balanceamento da matriz de recolhimento alterado com sucesso!"),
                Constantes.PARAM_MSGS).recursive().serialize();
    }
    
    @Post
    @Path("/salvar")
    @Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void salvar(Date dataPesquisa) {
        
        salvarMatrizRecolhimento(null,dataPesquisa);
        
        result.use(Results.json()).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Balanceamento da matriz de recolhimento salvo com sucesso!"),
                Constantes.PARAM_MSGS).recursive().serialize();
    }

    
	private void salvarMatrizRecolhimento(StatusLancamento statusLancamento,Date dataPesquisa) {
		this.verificarBloqueioMatrizRecolhimento();
        
        verificarExecucaoInterfaces();
        
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = (BalanceamentoRecolhimentoDTO) this.httpSession
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
        
        Usuario usuario = getUsuarioLogado();
        
        recolhimentoService.salvarBalanceamentoRecolhimento(usuario, balanceamentoRecolhimento,statusLancamento,dataPesquisa);
        
        removerAtributoAlteracaoSessao();
	}
    
    @Post
    @Path("/exibirMatrizFornecedor")
    public void exibirMatrizFornecedor(String dataFormatada, String sortorder, String sortname, Integer page, Integer rp) {
        
        FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
        filtro.setDataPesquisa(DateUtil.parseDataPTBR(dataFormatada));
        
        List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento = obterListaProdutoRecolhimentoDTO(dataFormatada);
        
        this.removeAtributoProdutosParaRecolhimentoSessao();
        
        this.adicionarAtributoProdutosParaRecolhimentoSessao(listaProdutoRecolhimento);
        
        if (listaProdutoRecolhimento != null && !listaProdutoRecolhimento.isEmpty()) {
            
            PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder, sortname);
            
            filtro.setPaginacaoVO(paginacao);
            
            processarBalanceamento(listaProdutoRecolhimento, paginacao, sortname);
        } else {
            
            this.result.use(Results.json()).from(Results.nothing()).serialize();
        }
    }
    
    private List<ProdutoRecolhimentoDTO> obterListaProdutoRecolhimentoDTO(String dataFormatada) {
        
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = (BalanceamentoRecolhimentoDTO) httpSession
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
        
        if (balanceamentoRecolhimento == null || balanceamentoRecolhimento.getMatrizRecolhimento() == null
            || balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Não houve carga de informações para o período escolhido!");
        }
        
        List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento = new ArrayList<ProdutoRecolhimentoDTO>();
        
        Date data = null;
        
        if (dataFormatada != null && !dataFormatada.trim().isEmpty()) {
            
            data = DateUtil.parseDataPTBR(dataFormatada);
        }
        
        if (data != null) {
            
            listaProdutoRecolhimento = balanceamentoRecolhimento.getMatrizRecolhimento().get(data);
            
        } else {
            
            for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : balanceamentoRecolhimento
                    .getMatrizRecolhimento().entrySet()) {
                
                listaProdutoRecolhimento.addAll(entry.getValue());
            }
        }
        
        return listaProdutoRecolhimento;
    }
    
    @Post
    @Path("/voltarConfiguracaoOriginal")
    @Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void voltarConfiguracaoOriginal() {
        
        this.verificarBloqueioMatrizRecolhimento();
        
        this.validarDataConfirmacaoConfiguracaoInicial();
        
        FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
        
        recolhimentoService.voltarConfiguracaoOriginal(filtro.getAnoNumeroSemana(), filtro.getListaIdsFornecedores(),
                getUsuarioLogado());
        
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = this.obterBalanceamentoRecolhimento(filtro
                .getAnoNumeroSemana(), filtro.getListaIdsFornecedores(), TipoBalanceamentoRecolhimento.AUTOMATICO,
                false);
        
        ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = this
                .obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
        
        removerAtributoAlteracaoSessao();
        
        this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
    }
    
	@Post
    @Path("/reprogramarSelecionados")
    @Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void reprogramarSelecionados(String novaDataFormatada, String dataAntigaFormatada, boolean selecionarTodos) {
        
        this.verificarBloqueioMatrizRecolhimento();
        
        verificarExecucaoInterfaces();
        
        FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
       
        Date novaData = DateUtil.parseDataPTBR(novaDataFormatada);
        
        this.validarDadosReprogramar(novaData, filtro.getAnoNumeroSemana());
        
        this.validarDataReprogramacao(filtro.getAnoNumeroSemana(), novaData, filtro.getDataPesquisa());
        
        List<ProdutoRecolhimentoDTO> itensRecolhimentoParaReprogramacao = 
        		this.obterListaProdutoRecolhimentoDataHolder(selecionarTodos);
        
        this.validarListaParaReprogramacao(itensRecolhimentoParaReprogramacao,novaData);
        
        Date dataAntiga = DateUtil.parseDataPTBR(dataAntigaFormatada);
        
        this.atualizarMapaRecolhimento(
        		novaData, 
        		dataAntiga,
        		itensRecolhimentoParaReprogramacao.toArray(new ProdutoRecolhimentoDTO[]{}));
        
        this.adicionarAtributoAlteracaoSessao();
        
        this.result.use(Results.json()).from(Results.nothing()).serialize();
    }

	@Post
    @Path("/reprogramarRecolhimentoUnico")
    @Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void reprogramarRecolhimentoUnico(ProdutoRecolhimentoDTO produtoRecolhimento,
            String dataAntigaFormatada) {
        
        this.verificarBloqueioMatrizRecolhimento();
        
        verificarExecucaoInterfaces();
        
        if (produtoRecolhimento != null) {
            
            Date novaData = produtoRecolhimento.getNovaData();
            
            FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
            
            this.validarDadosReprogramar(produtoRecolhimento.getNovaData(), filtro.getAnoNumeroSemana());
            
            this.validarDataReprogramacao(filtro.getAnoNumeroSemana(), novaData, filtro.getDataPesquisa());
            
            this.validarItemParaReprogramacao(
            		novaData,
            		produtoRecolhimento.getIdFornecedor(),
            		produtoRecolhimento.getIdLancamento());
            
            Date dataAntiga = DateUtil.parseDataPTBR(dataAntigaFormatada);
            
            this.atualizarMapaRecolhimento(novaData, dataAntiga,produtoRecolhimento);
        }
        
        this.adicionarAtributoAlteracaoSessao();
        
        this.result.use(Results.json()).from(Results.nothing()).serialize();
    }
    
    @Post
    @Path("/atualizarResumoBalanceamento")
    @Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void atualizarResumoBalanceamento() {
        
        verificarExecucaoInterfaces();
        
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = (BalanceamentoRecolhimentoDTO) this.httpSession
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
        
        if (balanceamentoRecolhimento == null || balanceamentoRecolhimento.getMatrizRecolhimento() == null
            || balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Não houve carga de informações para o período escolhido!");
        }
        
        ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = this
                .obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
        
        this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
    }
    
    @Post
    @Path("/verificarBalanceamentosAlterados")
    public void verificarBalanceamentosAlterados() {
        
        Boolean balanceamentoAlterado = (Boolean) this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO);
        
        if (balanceamentoAlterado == null) {
            
            balanceamentoAlterado = false;
        }
        
        this.result.use(Results.json()).from(balanceamentoAlterado.toString(), "result").serialize();
    }
    
    @Post
    public void verificarDataReprogramacao(Integer anoNumeroSemana, String novaDataBalanceamentoFormatada) {
        
        Date novaDataBalanceamento = DateUtil.parseDataPTBR(novaDataBalanceamentoFormatada);
        
        Intervalo<Date> intervalo = this.recolhimentoService.getPeriodoRecolhimento(anoNumeroSemana);
        
        ValidacaoDataRecolhimento validacaoDataRecolhimento = this.verificarDataForaDaSemana(intervalo,
                novaDataBalanceamento);
        
        /*
         * TODO: Regra para permitir reprogramação em uma data já confirmada.
         * Está comentado, pois está análise para ser utilizada futuramente.
         * 
         * if (validacaoDataRecolhimento == null) {
         * 
         * validacaoDataRecolhimento =
         * this.verificarDataEmDiaConfirmado(novaDataBalanceamento); }
         */
        
        if (validacaoDataRecolhimento == null) {
            
            validacaoDataRecolhimento = ValidacaoDataRecolhimento.DATA_VALIDA;
        }
        
        this.result.use(Results.json()).withoutRoot().from(validacaoDataRecolhimento).serialize();
    }
    
    private ValidacaoDataRecolhimento verificarDataForaDaSemana(Intervalo<Date> intervalo, Date novaDataBalanceamento) {
        
        boolean dataValidaSemana = DateUtil.validarDataEntrePeriodo(novaDataBalanceamento, intervalo.getDe(), intervalo
                .getAte());
        
        ValidacaoDataRecolhimento validacaoDataRecolhimento = null;
        
        if (!dataValidaSemana) {
            
            validacaoDataRecolhimento = ValidacaoDataRecolhimento.DATA_FORA_SEMANA;
        }
        
        return validacaoDataRecolhimento;
    }
    
    @SuppressWarnings("unused")
    private ValidacaoDataRecolhimento verificarDataEmDiaConfirmado(Date novaDataBalanceamento) {
        
        ValidacaoDataRecolhimento validacaoDataRecolhimento = null;
        
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = (BalanceamentoRecolhimentoDTO) this.httpSession
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
        
        if (balanceamentoRecolhimento == null || balanceamentoRecolhimento.getMatrizRecolhimento() == null
            || balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Sessão expirada!");
        }
        
        for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : balanceamentoRecolhimento.getMatrizRecolhimento()
                .entrySet()) {
            
            if (novaDataBalanceamento.equals(entry.getKey())) {
                
                for (ProdutoRecolhimentoDTO produtosRecolhimento : entry.getValue()) {
                    
                    if (produtosRecolhimento.isBalanceamentoConfirmado()) {
                        
                        validacaoDataRecolhimento = ValidacaoDataRecolhimento.DATA_DIA_CONFIRMADO;
                    }
                }
            }
        }
        
        return validacaoDataRecolhimento;
    }
    
    /**
     * Método que atualiza a matriz de recolhimento de acordo com os produtos
     * confirmados
     * 
     * @param matrizRecolhimento - matriz de recolhimento
     * @param matrizConfirmada - matriz de recolhimento confirmada
     * 
     * @return matriz atualizada
     */
    private TreeMap<Date, List<ProdutoRecolhimentoDTO>> atualizarMatizComProdutosConfirmados(
            TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
            TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizConfirmada) {
        
        for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizConfirmada.entrySet()) {
            
            Date novaData = entry.getKey();
            
            List<ProdutoRecolhimentoDTO> produtosConfirmados = matrizConfirmada.get(novaData);
            
            matrizRecolhimento.put(novaData, produtosConfirmados);
        }
        
        return matrizRecolhimento;
    }
    
    /**
     * Método que verifica se todos os recolhimentos estão confirmados para
     * remover a flag de alteração de dados da sessão.
     */
    private void verificarBalanceamentosConfirmados() {
        
        List<ConfirmacaoVO> listaConfirmacao = montarListaDatasConfirmacao();
        
        boolean balanceamentosConfirmados = true;
        
        for (ConfirmacaoVO confirmacao : listaConfirmacao) {
            
            if (!confirmacao.isConfirmado()) {
                
                balanceamentosConfirmados = false;
                
                break;
            }
        }
        
        if (balanceamentosConfirmados) {
            
            this.removerAtributoAlteracaoSessao();
        }
    }
    
    /**
     * Configura o filtro informado na tela e o armazena na sessão.
     * 
     * @param anoNumeroSemana - número da semana
     * @param dataPesquisa - data da pesquisa
     * @param listaIdsFornecedores - lista de identificadores de fornecedores
     */
    private void configurarFiltropesquisa(Integer anoNumeroSemana, Date dataPesquisa, List<Long> listaIdsFornecedores) {
        
        FiltroPesquisaMatrizRecolhimentoVO filtro = new FiltroPesquisaMatrizRecolhimentoVO(anoNumeroSemana,
                dataPesquisa, listaIdsFornecedores);
        
        this.httpSession.setAttribute(ATRIBUTO_SESSAO_FILTRO_PESQUISA_BALANCEAMENTO_RECOLHIMENTO, filtro);
    }
    
    /**
     * Obtém o filtro para pesquisa da sessão.
     * 
     * @return filtro
     */
    private FiltroPesquisaMatrizRecolhimentoVO obterFiltroSessao() {
        
        FiltroPesquisaMatrizRecolhimentoVO filtro = (FiltroPesquisaMatrizRecolhimentoVO) this.httpSession
                .getAttribute(ATRIBUTO_SESSAO_FILTRO_PESQUISA_BALANCEAMENTO_RECOLHIMENTO);
        
        if (filtro == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Filtro para a pesquisa não encontrado!");
        }
        
        return filtro;
    }
    
    /**
     * Adiciona um indicador, que informa se houve reprogramação de produtos, na
     * sessão.
     */
    private void adicionarAtributoAlteracaoSessao() {
        
        this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO, true);
    }
    
    /**
     * Remove um indicador, que informa se houve reprogramação de produtos, da
     * sessão.
     */
    private void removerAtributoAlteracaoSessao() {
        
        this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO, null);
    }
    
    /**
     * Método que atualiza o mapa de recolhimento de acordo com as escolhas do
     * usuário
     * 
     * @param listaProdutoRecolhimento - lista de produtos a serem alterados
     * @param novaData - nova data de recolhimento
     * @param dataAntiga - data antiga de recolhimento
     */
    private void atualizarMapaRecolhimento(Date novaData, Date dataAntiga,ProdutoRecolhimentoDTO ...listaProdutoRecolhimento) {
        
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimentoSessao = (BalanceamentoRecolhimentoDTO) httpSession
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
        
        TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoSessao = balanceamentoRecolhimentoSessao
                .getMatrizRecolhimento();
        
        TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = clonarMapaRecolhimento(matrizRecolhimentoSessao);
        
        List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoRemover = new ArrayList<ProdutoRecolhimentoDTO>();
        
        List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoAdicionar = new ArrayList<ProdutoRecolhimentoDTO>();
        
        montarListasParaAlteracaoMapa(matrizRecolhimento, listaProdutoRecolhimentoAdicionar,
                listaProdutoRecolhimentoRemover, dataAntiga,listaProdutoRecolhimento);
        
        removerEAdicionarMapa(matrizRecolhimento, balanceamentoRecolhimentoSessao.getProdutosRecolhimentoAgrupados(),
                listaProdutoRecolhimentoAdicionar, listaProdutoRecolhimentoRemover, novaData);
        
        balanceamentoRecolhimentoSessao.setMatrizRecolhimento(matrizRecolhimento);
        
        this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO, balanceamentoRecolhimentoSessao);
    }
    
    /**
     * Cria uma cópia do mapa da matriz de recolhimento. Isso é necessário pois
     * se houver alterações na cópia, não altera os valores do mapa original por
     * referência.
     * 
     * @param matrizRecolhimentoSessao - matriz de recolhimento da sesão
     * 
     * @return cópia do mapa da matriz de recolhimento
     */
    @SuppressWarnings("unchecked")
    private TreeMap<Date, List<ProdutoRecolhimentoDTO>> clonarMapaRecolhimento(
            Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoSessao) {
        
        byte[] mapSerialized = SerializationUtils.serialize(matrizRecolhimentoSessao);
        
        TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = (TreeMap<Date, List<ProdutoRecolhimentoDTO>>) SerializationUtils
                .deserialize(mapSerialized);
        
        return matrizRecolhimento;
    }
    
    /**
     * Monta as listas para alteração do mapa da matriz de recolhimento
     * 
     * @param listaProdutoRecolhimento - lista de produtos de recolhimento
     * @param matrizRecolhimento - matriz de recolhimento
     * @param listaProdutoRecolhimentoAdicionar - lista de produtos que serão
     *            adicionados
     * @param listaProdutoRecolhimentoRemover - lista de produtos que serão
     *            removidos
     * @param dataAntiga - data antiga de recolhimento
     */
    private void montarListasParaAlteracaoMapa(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
            List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoAdicionar,
            List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoRemover, Date dataAntiga, 
            ProdutoRecolhimentoDTO... produtosRecolhimento) {
        
        List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoSessao = null;
        
        if (dataAntiga != null) {
            
            listaProdutoRecolhimentoSessao = matrizRecolhimento.get(dataAntiga);
            
        } else {
            
            listaProdutoRecolhimentoSessao = new ArrayList<ProdutoRecolhimentoDTO>();
            
            for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
                
                listaProdutoRecolhimentoSessao.addAll(entry.getValue());
            }
        }
        
        if(listaProdutoRecolhimentoSessao == null) {
        	
        	throw new ValidacaoException(TipoMensagem.WARNING, "Lançamentos não encontrados. Efetue a pesquisa novamente.");
        }
        
        for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
            
            for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoSessao) {
                
                if (produtoRecolhimentoDTO.getIdLancamento()
                        .equals(produtoRecolhimento.getIdLancamento())) {
                    
                    listaProdutoRecolhimentoRemover.add(produtoRecolhimentoDTO);
                    
                    listaProdutoRecolhimentoAdicionar.add(produtoRecolhimentoDTO);
                    
                    break;
                }
            }
        }
    }
    
    /**
     * Remove e adiona os produtos no mapa da matriz de recolhimento.
     * 
     * @param matrizRecolhimento - mapa da matriz de recolhimento
     * @param listaProdutoRecolhimentoAdicionar - lista de produtos que serão
     *            adicionados
     * @param listaProdutoRecolhimentoRemover - lista de produtos que serão
     *            removidos
     * @param novaData - nova data de recolhimento
     */
    private void removerEAdicionarMapa(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
            List<ProdutoRecolhimentoDTO> produtosRecolhimentoAgrupados,
            List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoAdicionar,
            List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoRemover, Date novaData) {
        
        // Remover do mapa
        for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoRemover) {
            
            List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO = matrizRecolhimento.get(produtoRecolhimentoDTO
                    .getNovaData());
            
            listaProdutoRecolhimentoDTO.remove(produtoRecolhimentoDTO);
            
            if (listaProdutoRecolhimentoDTO.isEmpty()) {
                
                matrizRecolhimento.remove(produtoRecolhimentoDTO.getNovaData());
                
            } else {
                
                matrizRecolhimento.put(produtoRecolhimentoDTO.getNovaData(), listaProdutoRecolhimentoDTO);
            }
        }
        
        // Adicionar no mapa
        for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoAdicionar) {
            
            List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO = matrizRecolhimento.get(novaData);
            
            if (listaProdutoRecolhimentoDTO == null) {
                
                listaProdutoRecolhimentoDTO = new ArrayList<ProdutoRecolhimentoDTO>();
            }
            
            listaProdutoRecolhimentoDTO.add(produtoRecolhimentoDTO);
            
            produtoRecolhimentoDTO.setNovaData(novaData);
            
            matrizRecolhimento.put(novaData, listaProdutoRecolhimentoDTO);
        }
    }
    
    /**
     * Método que processa os balanceamentos para exibição no grid.
     * 
     * @param listaProdutoRecolhimento - lista de produtos de recolhimento
     * @param paginacao - paginação
     * @param sortname - nome da coluna para ordenação
     */
    private void processarBalanceamento(List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento, PaginacaoVO paginacao,
            String sortname) {
        
        List<ProdutoRecolhimentoVO> listaProdutoRecolhimentoVO = obterListaProdutoRecolhimentoVO(listaProdutoRecolhimento);
        
        int totalRegistros = listaProdutoRecolhimentoVO.size();
        
        if(this.isSortNamePorCodigoOuNomeProduto(sortname)){
        
            listaProdutoRecolhimentoVO = PaginacaoUtil.paginarEOrdenarEmMemoria(listaProdutoRecolhimentoVO, paginacao,
                 sortname,SORT_NAME_NUMERO_EDICAO);
        }else{

            listaProdutoRecolhimentoVO = PaginacaoUtil.paginarEOrdenarEmMemoria(listaProdutoRecolhimentoVO, paginacao,
            		sortname);            
        }
        
        TableModel<CellModelKeyValue<ProdutoRecolhimentoFormatadoVO>> tableModel = new TableModel<CellModelKeyValue<ProdutoRecolhimentoFormatadoVO>>();
        
        tableModel.setPage(paginacao.getPaginaAtual());
        tableModel.setTotal(totalRegistros);
        
        List<CellModelKeyValue<ProdutoRecolhimentoFormatadoVO>> listaCellModel = new ArrayList<CellModelKeyValue<ProdutoRecolhimentoFormatadoVO>>();
        
        CellModelKeyValue<ProdutoRecolhimentoFormatadoVO> cellModel = null;
        
        for (ProdutoRecolhimentoVO vo : listaProdutoRecolhimentoVO) {
            
            ProdutoRecolhimentoFormatadoVO produtoRecolhimento = this.formatarProdutoRecolhimento(vo);
            
            produtoRecolhimento.setReplicar(getCheckedFromDataHolder(produtoRecolhimento.getIdLancamento()));

            cellModel = new CellModelKeyValue<ProdutoRecolhimentoFormatadoVO>(Integer.valueOf(vo.getIdLancamento()),produtoRecolhimento);
            
            listaCellModel.add(cellModel);
            
        }
        
        tableModel.setRows(listaCellModel);
        
        result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
    }
    
    @Post
    public void atribuirCheckedParaTodosItens(String actionKey,String fieldKey,String fieldValue){
    	
    	FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
        
        List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento = 
        		obterListaProdutoRecolhimentoDTO(DateUtil.formatarDataPTBR(filtro.getDataPesquisa()));
        
        DataHolder dataHolder = (DataHolder) this.httpSession.getAttribute(DataHolder.SESSION_ATTRIBUTE_NAME);
        
        if(dataHolder == null){
        	
        	dataHolder = new DataHolder();
        	
        	this.httpSession.setAttribute(DataHolder.SESSION_ATTRIBUTE_NAME, dataHolder);
        }
        
   	 	for (ProdutoRecolhimentoDTO item : listaProdutoRecolhimento) {
            
   	 		dataHolder.hold(actionKey, item.getIdLancamento().toString(),fieldKey,fieldValue, dataHolder);
        }
   	 	
   	 	result.nothing();
    }
    
    private boolean isSortNamePorCodigoOuNomeProduto(String sortName){
    	
    	return (SORT_NAME_CODIGO_PRODUTO.equals(sortName)|| SORT_NAME_NOME_PRODUTO.equals(sortName));
    }
    
    private List<ProdutoRecolhimentoVO> obterListaProdutoRecolhimentoVO(
            List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento) {
        
        List<ProdutoRecolhimentoVO> listaProdutoRecolhimentoVO = new LinkedList<ProdutoRecolhimentoVO>();
        
        ProdutoRecolhimentoVO produtoRecolhimentoVO = null;
        
        BigDecimal precoDesconto = BigDecimal.ZERO;
        BigDecimal precoVenda = BigDecimal.ZERO;
        BigDecimal valorDesconto = BigDecimal.ZERO;
        
        for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimento) {
            
            produtoRecolhimentoVO = new ProdutoRecolhimentoVO();
            
            produtoRecolhimentoVO.setStatusLancamento(produtoRecolhimentoDTO.getStatusLancamento().name());
            
            produtoRecolhimentoVO.setIdLancamento(produtoRecolhimentoDTO.getIdLancamento().toString());
            
            produtoRecolhimentoVO.setIdProdutoEdicao(produtoRecolhimentoDTO.getIdProdutoEdicao());
            
            produtoRecolhimentoVO.setCodigoProduto(produtoRecolhimentoDTO.getCodigoProduto());
            
            produtoRecolhimentoVO.setNomeProduto(produtoRecolhimentoDTO.getNomeProduto());
            
            produtoRecolhimentoVO.setNumeroEdicao(produtoRecolhimentoDTO.getNumeroEdicao());
            
            produtoRecolhimentoVO.setPrecoVenda(produtoRecolhimentoDTO.getPrecoVenda());
            
            precoVenda = produtoRecolhimentoDTO.getPrecoVenda() != null ? produtoRecolhimentoDTO.getPrecoVenda()
                    : BigDecimal.ZERO;
            
            valorDesconto = produtoRecolhimentoDTO.getDesconto() != null ? produtoRecolhimentoDTO.getDesconto()
                    : BigDecimal.ZERO;
            
            precoDesconto = precoVenda.subtract(precoVenda.multiply(valorDesconto.divide(new BigDecimal("100"))));
            
            produtoRecolhimentoVO.setPrecoDesconto(precoDesconto);
            
            produtoRecolhimentoVO.setIdFornecedor(produtoRecolhimentoDTO.getIdFornecedor());
            
            produtoRecolhimentoVO.setNomeFornecedor(produtoRecolhimentoDTO.getNomeFornecedor());
            
            produtoRecolhimentoVO.setNomeEditor(produtoRecolhimentoDTO.getNomeEditor());
            
            if (produtoRecolhimentoDTO.getParcial() != null) {
                produtoRecolhimentoVO.setParcial(produtoRecolhimentoDTO.getParcial().getDescricao());
            } else {
                produtoRecolhimentoVO.setParcial("Não");
            }
            
            produtoRecolhimentoVO.setBrinde((produtoRecolhimentoDTO.isPossuiBrinde()) ? "Sim" : "Não");
            
            produtoRecolhimentoVO.setDataLancamento(produtoRecolhimentoDTO.getDataLancamento());
            
            produtoRecolhimentoVO.setDataRecolhimento(produtoRecolhimentoDTO.getDataRecolhimentoPrevista());
            
            produtoRecolhimentoVO.setEncalheSede(produtoRecolhimentoDTO.getExpectativaEncalheSede());
            
            produtoRecolhimentoVO.setEncalheAtendida(produtoRecolhimentoDTO.getExpectativaEncalheAtendida());
            
            produtoRecolhimentoVO.setEncalhe(produtoRecolhimentoDTO.getExpectativaEncalhe());
            
            produtoRecolhimentoVO.setValorTotal(produtoRecolhimentoDTO.getValorTotal());
            
            produtoRecolhimentoVO.setNovaData(produtoRecolhimentoDTO.getNovaData());
            
            produtoRecolhimentoVO.setBloqueioAlteracaoBalanceamento(produtoRecolhimentoDTO.isBalanceamentoConfirmado());
            
            produtoRecolhimentoVO.setPeb(produtoRecolhimentoDTO.getPeb());
            
            listaProdutoRecolhimentoVO.add(produtoRecolhimentoVO);
        }
        return listaProdutoRecolhimentoVO;
    }
    
    private List<ProdutoRecolhimentoDiferenciadoVO> obterListaProdutoRecolhimentoDiferenciadoVO(
            List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento) {
        
        List<ProdutoRecolhimentoDiferenciadoVO> listaProdutoRecolhimentoDiferenciadoVO = new LinkedList<ProdutoRecolhimentoDiferenciadoVO>();
        
        ProdutoRecolhimentoDiferenciadoVO produtoRecolhimentoDiferenciadoVO = null;
        
        BigDecimal precoDesconto = BigDecimal.ZERO;
        BigDecimal precoVenda = BigDecimal.ZERO;
        BigDecimal valorDesconto = BigDecimal.ZERO;
        
        for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimento) {
            
            produtoRecolhimentoDiferenciadoVO = new ProdutoRecolhimentoDiferenciadoVO();
            
            produtoRecolhimentoDiferenciadoVO.setStatusLancamento(produtoRecolhimentoDTO.getStatusLancamento().name());
            
            produtoRecolhimentoDiferenciadoVO.setIdLancamento(produtoRecolhimentoDTO.getIdLancamento().toString());
            
            produtoRecolhimentoDiferenciadoVO.setIdProdutoEdicao(produtoRecolhimentoDTO.getIdProdutoEdicao());
            
            produtoRecolhimentoDiferenciadoVO.setCodigoProduto(produtoRecolhimentoDTO.getCodigoProduto());
            
            produtoRecolhimentoDiferenciadoVO.setNomeProduto(produtoRecolhimentoDTO.getNomeProduto());
            
            produtoRecolhimentoDiferenciadoVO.setNumeroEdicao(produtoRecolhimentoDTO.getNumeroEdicao());
            
            produtoRecolhimentoDiferenciadoVO.setPrecoVenda(produtoRecolhimentoDTO.getPrecoVenda());
            
            precoVenda = produtoRecolhimentoDTO.getPrecoVenda() != null ? produtoRecolhimentoDTO.getPrecoVenda() : BigDecimal.ZERO;
            
            valorDesconto = produtoRecolhimentoDTO.getDesconto() != null ? produtoRecolhimentoDTO.getDesconto() : BigDecimal.ZERO;
            
            precoDesconto = precoVenda.subtract(precoVenda.multiply(valorDesconto.divide(new BigDecimal("100"))));
            
            produtoRecolhimentoDiferenciadoVO.setPrecoDesconto(precoDesconto);
            
            produtoRecolhimentoDiferenciadoVO.setIdFornecedor(produtoRecolhimentoDTO.getIdFornecedor());
            
            produtoRecolhimentoDiferenciadoVO.setNomeFornecedor(produtoRecolhimentoDTO.getNomeFornecedor());
            
            produtoRecolhimentoDiferenciadoVO.setNomeEditor(produtoRecolhimentoDTO.getNomeEditor());
            
            if (produtoRecolhimentoDiferenciadoVO.getParcial() != null) {
                produtoRecolhimentoDiferenciadoVO.setParcial(produtoRecolhimentoDTO.getParcial().getDescricao());
            } else {
                produtoRecolhimentoDiferenciadoVO.setParcial("Não");
            }
            
            produtoRecolhimentoDiferenciadoVO.setBrinde((produtoRecolhimentoDTO.isPossuiBrinde()) ? "Sim" : "Não");
            
            produtoRecolhimentoDiferenciadoVO.setDataLancamento(produtoRecolhimentoDTO.getDataLancamento());
            
            produtoRecolhimentoDiferenciadoVO.setDataRecolhimento(produtoRecolhimentoDTO.getDataRecolhimentoPrevista());
            
            produtoRecolhimentoDiferenciadoVO.setEncalheSede(produtoRecolhimentoDTO.getExpectativaEncalheSede());
            
            produtoRecolhimentoDiferenciadoVO.setEncalheAtendida(produtoRecolhimentoDTO.getExpectativaEncalheAtendida());
            
            produtoRecolhimentoDiferenciadoVO.setEncalhe(produtoRecolhimentoDTO.getExpectativaEncalhe());
            
            produtoRecolhimentoDiferenciadoVO.setValorTotal(produtoRecolhimentoDTO.getValorTotal());
            
            produtoRecolhimentoDiferenciadoVO.setNovaData(produtoRecolhimentoDTO.getNovaData());
            
            produtoRecolhimentoDiferenciadoVO.setBloqueioAlteracaoBalanceamento(produtoRecolhimentoDTO.isBalanceamentoConfirmado());
            
            produtoRecolhimentoDiferenciadoVO.setPeb(produtoRecolhimentoDTO.getPeb());
            
            listaProdutoRecolhimentoDiferenciadoVO.add(produtoRecolhimentoDiferenciadoVO);
        }
        
        return listaProdutoRecolhimentoDiferenciadoVO;
    }
    
    /**
     * Método que formata os valores para serem exibidos na tela.
     * 
     * @param produtoRecolhimento - produto recolhimento
     * 
     * @return produto recolhimento formatado
     */
    private ProdutoRecolhimentoFormatadoVO formatarProdutoRecolhimento(ProdutoRecolhimentoVO produtoRecolhimento) {
        
        ProdutoRecolhimentoFormatadoVO produtoRecolhimentoFormatado = new ProdutoRecolhimentoFormatadoVO();
        
        produtoRecolhimentoFormatado.setIdFornecedor(produtoRecolhimento.getIdFornecedor());
        
        produtoRecolhimentoFormatado.setStatusLancamento(produtoRecolhimento.getStatusLancamento());
        
        produtoRecolhimentoFormatado.setIdLancamento(produtoRecolhimento.getIdLancamento());
        
        produtoRecolhimentoFormatado
                .setIdLancamento((produtoRecolhimento.getIdLancamento() != null) ? produtoRecolhimento
                        .getIdLancamento().toString() : null);
        
        produtoRecolhimentoFormatado
                .setIdProdutoEdicao((produtoRecolhimento.getIdProdutoEdicao() != null) ? produtoRecolhimento
                        .getIdProdutoEdicao().toString() : null);
        
        produtoRecolhimentoFormatado.setCodigoProduto(produtoRecolhimento.getCodigoProduto());
        
        produtoRecolhimentoFormatado.setNomeProduto(produtoRecolhimento.getNomeProduto());
        
        produtoRecolhimentoFormatado
                .setNumeroEdicao((produtoRecolhimento.getNumeroEdicao() != null) ? produtoRecolhimento
                        .getNumeroEdicao().toString() : null);
        
        if (produtoRecolhimento.getPrecoVenda() != null) {
            produtoRecolhimentoFormatado.setPrecoVenda(CurrencyUtil.formatarValor(produtoRecolhimento.getPrecoVenda()));
        } else {
            produtoRecolhimentoFormatado.setPrecoVenda(null);
        }
        
        if (produtoRecolhimento.getPrecoDesconto() != null) {
            produtoRecolhimentoFormatado.setPrecoDesconto(CurrencyUtil.formatarValorQuatroCasas(produtoRecolhimento
                    .getPrecoDesconto()));
        } else {
            produtoRecolhimentoFormatado.setPrecoDesconto(null);
        }
        
        produtoRecolhimentoFormatado.setNomeFornecedor(produtoRecolhimento.getNomeFornecedor());
        
        produtoRecolhimentoFormatado.setNomeEditor(produtoRecolhimento.getNomeEditor());
        
        produtoRecolhimentoFormatado.setParcial(produtoRecolhimento.getParcial());
        
        produtoRecolhimentoFormatado.setBrinde(produtoRecolhimento.getBrinde());
        
        if (produtoRecolhimento.getDataLancamento() != null) {
            produtoRecolhimentoFormatado.setDataLancamento(DateUtil.formatarDataPTBR(produtoRecolhimento
                    .getDataLancamento()));
        } else {
            produtoRecolhimentoFormatado.setDataLancamento(null);
        }
        
        if (produtoRecolhimento.getDataRecolhimento() != null) {
            produtoRecolhimentoFormatado.setDataRecolhimento(DateUtil.formatarDataPTBR(produtoRecolhimento
                    .getDataRecolhimento()));
        } else {
            produtoRecolhimentoFormatado.setDataRecolhimento(null);
        }
        
        produtoRecolhimentoFormatado.setEncalheSede((produtoRecolhimento.getEncalheSede() != null) ? MathUtil.round(
                produtoRecolhimento.getEncalheSede(), 0).toString() : null);
        
        produtoRecolhimentoFormatado.setEncalheAtendida((produtoRecolhimento.getEncalheAtendida() != null) ? MathUtil
                .round(produtoRecolhimento.getEncalheAtendida(), 0).toString() : null);
        
        produtoRecolhimentoFormatado.setEncalhe((produtoRecolhimento.getEncalhe() != null) ? MathUtil.round(
                produtoRecolhimento.getEncalhe(), 0).toString() : null);
        
        if (produtoRecolhimento.getValorTotal() != null) {
            produtoRecolhimentoFormatado.setValorTotal(CurrencyUtil.formatarValorQuatroCasas(produtoRecolhimento
                    .getValorTotal()));
        } else {
            produtoRecolhimentoFormatado.setValorTotal(null);
        }
        
        if (produtoRecolhimento.getNovaData() != null) {
            produtoRecolhimentoFormatado.setNovaData(DateUtil.formatarDataPTBR(produtoRecolhimento.getNovaData()));
        } else {
            produtoRecolhimentoFormatado.setNovaData(null);
        }
        
        produtoRecolhimentoFormatado.setBloqueioAlteracaoBalanceamento(produtoRecolhimento
                .isBloqueioAlteracaoBalanceamento());
        
        produtoRecolhimentoFormatado.setPeb(produtoRecolhimento.getPeb());
        
        return produtoRecolhimentoFormatado;
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
     * Valida os dados para reprogramação.
     * 
     * @param data - data para reprogramação
     * @param numeroSemana - número da semana
     */
    private void validarDadosReprogramar(Date data, Integer numeroSemana) {
        
        if (data == null) {
            
            throw new ValidacaoException(
                    new ValidacaoVO(TipoMensagem.WARNING, "O preenchimento da data é obrigatório!"));
        }
        
        if (numeroSemana == null) {
            
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Semana inválida!"));
        }
    }
    
    private void validarDataReprogramacao(Integer numeroSemana, Date novaData, Date dataBalanceamento) {
        
        Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();

        if (DateUtil.isDataInicialMaiorDataFinal(dataOperacao, novaData)) {
        	
        	throw new ValidacaoException(TipoMensagem.WARNING,
                    "Não é possível reprogramar para uma data anterior à data de operação do sistema.");
        }

        this.recolhimentoService.verificaDataOperacao(novaData);
        
        List<ConfirmacaoVO> confirmacoes = this.montarListaDatasConfirmacao();
        
        Intervalo<Date> periodoRecolhimento = this.recolhimentoService.getPeriodoRecolhimento(numeroSemana);
        
        if (DateUtil.validarDataEntrePeriodo(novaData, periodoRecolhimento.getDe(), periodoRecolhimento.getAte())) {
            
            for (ConfirmacaoVO confirmacao : confirmacoes) {
                
                if (DateUtil.parseDataPTBR(confirmacao.getMensagem()).equals(novaData)) {
                    
                    if (confirmacao.isConfirmado()) {
                        
                        throw new ValidacaoException(TipoMensagem.WARNING,
                                "O recolhimento não pode ser reprogramado para uma data já confirmada!");
                    }
                }
            }    
            
        } else {
            
            if (this.recolhimentoService.existeRecolhimentoBalanceado(novaData)) {
                
                throw new ValidacaoException(TipoMensagem.WARNING, 
                        "O recolhimento não pode ser reprogramado para uma data já confirmada!");
            }
        }
        
    }
    
    /**
     * Valida a lista de produtos informados na tela para reprogramação.
     * 
     * @param listaProdutoRecolhimento - lista de produtos de recolhimento
     */
    private void validarListaParaReprogramacao(List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento, Date novaData) {
        
        if (listaProdutoRecolhimento == null || listaProdutoRecolhimento.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "É necessário selecionar ao menos um produto para realizar a reprogramação!");
        }
        
        for (ProdutoRecolhimentoDTO produto : listaProdutoRecolhimento) {
            
            validarItemParaReprogramacao(novaData,produto.getIdFornecedor(),produto.getIdLancamento());
        }
    }

	private void validarItemParaReprogramacao(Date novaData, Long idFornecedor, Long idLancamento) {
		
		Fornecedor fornecedor = this.fornecedorService.obterPorId(idFornecedor);
		
		List<Integer> diasRecolhimentoFornecedor = this.distribuicaoFornecedorService
		        .obterCodigosDiaDistribuicaoFornecedor(fornecedor.getId(), OperacaoDistribuidor.RECOLHIMENTO);
	
		int codigoDiaCorrente = SemanaUtil.obterDiaDaSemana(novaData);
		
		if (!diasRecolhimentoFornecedor.contains(codigoDiaCorrente)) {
		    
		    throw new ValidacaoException(TipoMensagem.WARNING,
		            "Não é permitido a reprogramação, pois o parametro de recolhimento não está configurado para esta e fornecedor.");
		}
		
		Lancamento lancamento = this.lancamentoService.obterPorId(idLancamento);
		
		if (!(novaData.compareTo(lancamento.getDataLancamentoDistribuidor()) > 0)) {
		    
		    throw new ValidacaoException(TipoMensagem.WARNING,
		            "A data de recolhimento deve ser maior que a data de lançamento.");
		}
	}
    
    /**
     * Obtém a matriz de balanceamento de recolhimento.
     * 
     * @param anoNumeroSemana - número da semana
     * @param listaIdsFornecedores - lista de identificadores dos fornecedores
     * @param tipoBalanceamentoRecolhimento - tipo de balanceamento de
     *            recolhimento
     * @param forcarBalanceamento - indicador para forçar a sugestão através do
     *            balanceamento
     * 
     * @return - objeto contendo as informações do balanceamento
     */
    private BalanceamentoRecolhimentoDTO obterBalanceamentoRecolhimento(Integer anoNumeroSemana,
            List<Long> listaIdsFornecedores, TipoBalanceamentoRecolhimento tipoBalanceamentoRecolhimento,
            boolean forcarBalanceamento) {
        
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = null;
        
        //String statusResumo= StatusRecebimento.
        
        if (anoNumeroSemana != null && listaIdsFornecedores != null) {
            
            balanceamentoRecolhimento = this.recolhimentoService.obterMatrizBalanceamento(anoNumeroSemana,
                    listaIdsFornecedores, tipoBalanceamentoRecolhimento, forcarBalanceamento);
            
            this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO, balanceamentoRecolhimento);
        }
        /*
         * if (balanceamentoRecolhimento == null ||
         * balanceamentoRecolhimento.getMatrizRecolhimento() == null ||
         * balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
         * 
         * throw new ValidacaoException( TipoMensagem.WARNING,
         * "Não houve carga de informações para o período escolhido!"); }
         */
        return balanceamentoRecolhimento;
    }
    
    /**
     * Obtém o resumo do período de balanceamento de acordo com a data da
     * pesquisa e a lista de id's dos fornecedores.
     */
    private ResultadoResumoBalanceamentoVO obterResultadoResumoBalanceamento(
            BalanceamentoRecolhimentoDTO balanceamentoRecolhimento) {
        
    	String statusResumo= "";
    	
        if (balanceamentoRecolhimento == null || balanceamentoRecolhimento.getMatrizRecolhimento() == null
            || balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
            
            return null;
        }
        
        List<ResumoPeriodoBalanceamentoVO> resumoPeriodoBalanceamento = new ArrayList<ResumoPeriodoBalanceamentoVO>();
        
        for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : balanceamentoRecolhimento.getMatrizRecolhimento().entrySet()) {
            
        	statusResumo= "";
        	
        	Date dataRecolhimento = entry.getKey();
            
            ResumoPeriodoBalanceamentoVO itemResumoPeriodoBalanceamento = new ResumoPeriodoBalanceamentoVO();
            
            itemResumoPeriodoBalanceamento.setData(dataRecolhimento);
            
            List<ProdutoRecolhimentoDTO> listaProdutosRecolhimento = entry.getValue();
            
            if (listaProdutosRecolhimento != null && !listaProdutosRecolhimento.isEmpty()) {
                
                Long qtdeTitulos = 0L;
                Long qtdeTitulosParciais = 0L;
                
                Long pesoTotal = 0L;
                BigDecimal qtdeExemplares = BigDecimal.ZERO;
                BigDecimal valorTotal = BigDecimal.ZERO;
                
                for (ProdutoRecolhimentoDTO produtoRecolhimento : listaProdutosRecolhimento) {
                    
                	//Status do Recolhimento
                	//EXPEDIDO.toString(),
    				//EM_BALANCEAMENTO_RECOLHIMENTO.toString(),
    				//BALANCEADO_RECOLHIMENTO.toString(),
    				//EM_RECOLHIMENTO.toString(),
    				//RECOLHIDO.toString() };
    				
                	if(produtoRecolhimento.getStatusLancamento().equals(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO)){
                		statusResumo = StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.name();
                	}else if(produtoRecolhimento.getStatusLancamento().equals(StatusLancamento.BALANCEADO_RECOLHIMENTO)
                		 && (statusResumo.equals("") ||statusResumo.equals(StatusLancamento.BALANCEADO_RECOLHIMENTO.name()))){
                		statusResumo = StatusLancamento.BALANCEADO_RECOLHIMENTO.name();
                	}else {
                		statusResumo = produtoRecolhimento.getStatusLancamento().name();
                	}
                	
                	
                	itemResumoPeriodoBalanceamento.setStatusResumo(statusResumo);
                	
                	BigDecimal expectativaEncalhe = produtoRecolhimento.getExpectativaEncalhe();
                    
                    if (!itemResumoPeriodoBalanceamento.getIdsProdutoEdicao().contains(produtoRecolhimento.getIdProdutoEdicao())) {
                        
                        qtdeTitulos++;
                    }
                    
                    if (produtoRecolhimento.getParcial() != null) {
                        
                        qtdeTitulosParciais++;
                    }
                    
                    if (produtoRecolhimento.getPeso() != null) {
                        
                        pesoTotal = pesoTotal + (produtoRecolhimento.getPeso() * expectativaEncalhe.longValue());
                    }
                    
                    if (produtoRecolhimento.getValorTotal() != null) {
                        
                        valorTotal = valorTotal.add(produtoRecolhimento.getValorTotal());
                    }
                    
                    if (produtoRecolhimento.getExpectativaEncalhe() != null) {
                        
                        qtdeExemplares = qtdeExemplares.add(expectativaEncalhe);
                    }
                    
                    itemResumoPeriodoBalanceamento.getIdsProdutoEdicao().add(produtoRecolhimento.getIdProdutoEdicao());
                    
                }
                
                boolean excedeCapacidadeDistribuidor = false;
                
                if (balanceamentoRecolhimento.getCapacidadeRecolhimentoDistribuidor() != null) {
                    
                    excedeCapacidadeDistribuidor = (new BigDecimal(balanceamentoRecolhimento
                            .getCapacidadeRecolhimentoDistribuidor()).compareTo(qtdeExemplares) == -1);
                }
                
                itemResumoPeriodoBalanceamento.setExcedeCapacidadeDistribuidor(excedeCapacidadeDistribuidor);
                
                itemResumoPeriodoBalanceamento.setPesoTotal(new BigDecimal(pesoTotal / 1000));
                itemResumoPeriodoBalanceamento.setQtdeExemplares(qtdeExemplares.toBigInteger());
                itemResumoPeriodoBalanceamento.setQtdeTitulos(qtdeTitulos);
                
                itemResumoPeriodoBalanceamento.setQtdeTitulosParciais(qtdeTitulosParciais);
                
                itemResumoPeriodoBalanceamento.setValorTotal(valorTotal);
                
                resumoPeriodoBalanceamento.add(itemResumoPeriodoBalanceamento);
            }
        }
        
        this.tratarResumoOperacaoDiferenciada(balanceamentoRecolhimento, resumoPeriodoBalanceamento);
        
        this.ordenarResumoPeriodoPorData(resumoPeriodoBalanceamento);
        
        ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = new ResultadoResumoBalanceamentoVO();
        
        resultadoResumoBalanceamento.setListaResumoPeriodoBalanceamento(resumoPeriodoBalanceamento);
        
        resultadoResumoBalanceamento.setCapacidadeRecolhimentoDistribuidor(balanceamentoRecolhimento
                .getCapacidadeRecolhimentoDistribuidor());
        
        return resultadoResumoBalanceamento;
    }
    
    @SuppressWarnings("unchecked")
    private void ordenarResumoPeriodoPorData(List<ResumoPeriodoBalanceamentoVO> resumoPeriodoBalanceamento) {
        
        ComparatorChain comparatorChain = new ComparatorChain();
        
        comparatorChain.addComparator(new BeanComparator("data"));
        
        Collections.sort(resumoPeriodoBalanceamento, comparatorChain);
    }
    
    private void tratarResumoOperacaoDiferenciada(BalanceamentoRecolhimentoDTO balanceamentoRecolhimento,
            List<ResumoPeriodoBalanceamentoVO> resumoPeriodoBalanceamento) {
        
        Map<Date, List<CotaOperacaoDiferenciadaDTO>> mapOperacaoDifAdicionar = new TreeMap<>();
        Map<Date, List<CotaOperacaoDiferenciadaDTO>> mapOperacaoDifRemover = new TreeMap<>();
        
        this.recolhimentoService.montarMapasOperacaoDiferenciada(mapOperacaoDifAdicionar, mapOperacaoDifRemover,
                balanceamentoRecolhimento.getMatrizRecolhimento(), balanceamentoRecolhimento
                        .getCotasOperacaoDiferenciada());
        
        for (Map.Entry<Date, List<CotaOperacaoDiferenciadaDTO>> entry : mapOperacaoDifAdicionar.entrySet()) {
            
            Date dataRecolhimento = entry.getKey();
            List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada = entry.getValue();
            
            ResumoPeriodoBalanceamentoVO itemResumoPeriodoBalanceamento = this.obterItemResumoBalanceamento(
                    resumoPeriodoBalanceamento, dataRecolhimento);
            
            if (itemResumoPeriodoBalanceamento == null) {
                
                itemResumoPeriodoBalanceamento = new ResumoPeriodoBalanceamentoVO();
                
                itemResumoPeriodoBalanceamento.setData(dataRecolhimento);
                itemResumoPeriodoBalanceamento.setBloquearVisualizacao(true);
                
                resumoPeriodoBalanceamento.add(itemResumoPeriodoBalanceamento);
            }
            
            BigInteger qtdeExemplares = (itemResumoPeriodoBalanceamento.getQtdeExemplares() != null) ? itemResumoPeriodoBalanceamento
                    .getQtdeExemplares()
                    : BigInteger.ZERO;
            
            BigDecimal pesoTotal = (itemResumoPeriodoBalanceamento.getPesoTotal() != null) ? itemResumoPeriodoBalanceamento
                    .getPesoTotal()
                    : BigDecimal.ZERO;
            
            BigDecimal valorTotal = (itemResumoPeriodoBalanceamento.getValorTotal() != null) ? itemResumoPeriodoBalanceamento
                    .getValorTotal()
                    : BigDecimal.ZERO;
            
            Long qtdeTitulos = (itemResumoPeriodoBalanceamento.getQtdeTitulos() != null) ? itemResumoPeriodoBalanceamento
                    .getQtdeTitulos()
                    : 0L;
            
            Long qtdeTitulosParciais = (itemResumoPeriodoBalanceamento.getQtdeTitulosParciais() != null) ? itemResumoPeriodoBalanceamento
                    .getQtdeTitulosParciais()
                    : 0L;
            
            for (CotaOperacaoDiferenciadaDTO cotaOperacaoDiferenciada : cotasOperacaoDiferenciada) {
                
                BigDecimal expectativaEncalhe = cotaOperacaoDiferenciada.getExpectativaEncalhe();
                
                qtdeExemplares = qtdeExemplares.add(expectativaEncalhe.toBigInteger());
                
                pesoTotal = pesoTotal.add(new BigDecimal((cotaOperacaoDiferenciada.getPeso()
                    * expectativaEncalhe.longValue() / 1000)));
                
                valorTotal = valorTotal.add(cotaOperacaoDiferenciada.getValorTotal());
                
                if (!itemResumoPeriodoBalanceamento.getIdsProdutoEdicao().contains(
                        cotaOperacaoDiferenciada.getIdProdutoEdicao())) {
                    
                    qtdeTitulos++;
                    
                    if (cotaOperacaoDiferenciada.getParcial() != null) {
                        
                        qtdeTitulosParciais++;
                    }
                    
                    itemResumoPeriodoBalanceamento.getIdsProdutoEdicao().add(
                            cotaOperacaoDiferenciada.getIdProdutoEdicao());
                }
            }
            
            itemResumoPeriodoBalanceamento.setQtdeExemplares(qtdeExemplares);
            itemResumoPeriodoBalanceamento.setPesoTotal(pesoTotal);
            itemResumoPeriodoBalanceamento.setValorTotal(valorTotal);
            itemResumoPeriodoBalanceamento.setQtdeTitulos(qtdeTitulos);
            itemResumoPeriodoBalanceamento.setQtdeTitulosParciais(qtdeTitulosParciais);
            itemResumoPeriodoBalanceamento.setExibeDestaque(true);
        }
        
        for (Map.Entry<Date, List<CotaOperacaoDiferenciadaDTO>> entry : mapOperacaoDifRemover.entrySet()) {
            
            Date dataRecolhimento = entry.getKey();
            List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada = entry.getValue();
            
            ResumoPeriodoBalanceamentoVO itemResumoPeriodoBalanceamento = this.obterItemResumoBalanceamento(
                    resumoPeriodoBalanceamento, dataRecolhimento);
            
            BigInteger qtdeExemplares = (itemResumoPeriodoBalanceamento.getQtdeExemplares() != null) ? itemResumoPeriodoBalanceamento
                    .getQtdeExemplares()
                    : BigInteger.ZERO;
            
            BigDecimal pesoTotal = (itemResumoPeriodoBalanceamento.getPesoTotal() != null) ? itemResumoPeriodoBalanceamento
                    .getPesoTotal()
                    : BigDecimal.ZERO;
            
            BigDecimal valorTotal = (itemResumoPeriodoBalanceamento.getValorTotal() != null) ? itemResumoPeriodoBalanceamento
                    .getValorTotal()
                    : BigDecimal.ZERO;
            
            for (CotaOperacaoDiferenciadaDTO cotaOperacaoDiferenciada : cotasOperacaoDiferenciada) {
                
                BigDecimal expectativaEncalhe = cotaOperacaoDiferenciada.getExpectativaEncalhe();
                
                qtdeExemplares = qtdeExemplares.subtract(cotaOperacaoDiferenciada.getExpectativaEncalhe()
                        .toBigInteger());
                
                pesoTotal = pesoTotal.subtract(new BigDecimal((cotaOperacaoDiferenciada.getPeso()
                    * expectativaEncalhe.longValue() / 1000)));
                
                valorTotal = valorTotal.subtract(cotaOperacaoDiferenciada.getValorTotal());
            }
            
            itemResumoPeriodoBalanceamento.setQtdeExemplares(qtdeExemplares);
            itemResumoPeriodoBalanceamento.setPesoTotal(pesoTotal);
            itemResumoPeriodoBalanceamento.setValorTotal(valorTotal);
        }
    }
    
    private ResumoPeriodoBalanceamentoVO obterItemResumoBalanceamento(
            List<ResumoPeriodoBalanceamentoVO> resumoPeriodoBalanceamento, Date dataRecolhimento) {
        
        for (ResumoPeriodoBalanceamentoVO itemResumoPeriodoBalanceamento : resumoPeriodoBalanceamento) {
            
            if (dataRecolhimento.compareTo(itemResumoPeriodoBalanceamento.getData()) == 0) {
                
                return itemResumoPeriodoBalanceamento;
            }
        }
        
        return null;
    }
    
    @Get
    public void isTodasDatasConfirmadas() {
        
        List<ConfirmacaoVO> confirmacoesVO = this.montarListaDatasConfirmacao();
        
        for (ConfirmacaoVO confirmacaoVO : confirmacoesVO) {
            
            if (!confirmacaoVO.isConfirmado()) {
                
                this.result.use(Results.json()).withoutRoot().from(false).serialize();
                
                return;
            }
        }
        
        this.result.use(Results.json()).withoutRoot().from(true).serialize();
    }
    
    @Get
    public void obterDatasConfirmadas() {
        
        List<ConfirmacaoVO> confirmacoesVO = this.montarListaDatasConfirmacao();
        
        List<String> datasConfirmadas = new ArrayList<>();
        
        for (ConfirmacaoVO confirmacaoVO : confirmacoesVO) {
            
            if (confirmacaoVO.isConfirmado()) {
                
                datasConfirmadas.add(confirmacaoVO.getMensagem());
            }
        }
        
        this.result.use(Results.json()).from(datasConfirmadas, "result").serialize();
    }
    
    @Post
    @Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void obterDatasConfirmadasReaberturaPost() {
        
        List<ConfirmacaoVO> confirmacoesVO = this.montarListaDatasConfirmacao();
        
        List<String> datasConfirmadasReabertura = new ArrayList<>();
        
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        
        for (ConfirmacaoVO confirmacaoVO : confirmacoesVO) {
            
            try {
                
                data = format.parse(confirmacaoVO.getMensagem());
                
            } catch (ParseException ex) {
                
            }
            
            if (confirmacaoVO.isConfirmado()) {
                
                if (this.distribuidorService.obterDataOperacaoDistribuidor().before(data)) {
                    
                    datasConfirmadasReabertura.add(confirmacaoVO.getMensagem());
                }
            }
        }
        
        this.result.use(Results.json()).from(datasConfirmadasReabertura, "result").serialize();
    }
    
    @Post
    @Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void obterDatasConfirmadasReaberturaCadeadoPost() {
        
        List<ConfirmacaoVO> confirmacoesVO = this.montarListaDatasCadeado();
        
        List<String> datasConfirmadasReabertura = new ArrayList<>();
        
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        
        for (ConfirmacaoVO confirmacaoVO : confirmacoesVO) {
            
            try {
                
                data = format.parse(confirmacaoVO.getMensagem());
                
            } catch (ParseException ex) {
                
            }
            
            if (confirmacaoVO.isConfirmado()) {
                
                if (this.distribuidorService.obterDataOperacaoDistribuidor().before(data)) {
                    
                    datasConfirmadasReabertura.add(confirmacaoVO.getMensagem());
                }
            }
        }
        
        this.result.use(Results.json()).from(datasConfirmadasReabertura, "result").serialize();
    }
    
    @Post
    public void reabrirMatriz(List<Date> datasReabertura) {
        
        this.verificarBloqueioMatrizRecolhimento();
        
        if (datasReabertura == null || datasReabertura.isEmpty()) {
            
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhuma data foi selecionada!"));
        }
        
        this.httpSession.setAttribute(ATRIBUTO_SESSAO_VALIDACAO, this.recolhimentoService.reabrirMatriz(datasReabertura, getUsuarioLogado()));
        
        FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
        
        obterBalanceamentoRecolhimento(filtro.getAnoNumeroSemana(), filtro.getListaIdsFornecedores(), TipoBalanceamentoRecolhimento.AUTOMATICO, false);
        
        this.result.use(PlainJSONSerialization.class).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Reabertura realizada com sucesso!"), "result").recursive()
                .serialize();
    }
    
    @Post
    public void cadeadoMatriz(List<Date> datasReabertura) {
        
        this.verificarBloqueioMatrizRecolhimento();
        
        if (datasReabertura == null || datasReabertura.isEmpty()) {
            
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhuma data foi selecionada!"));
        }
        
        this.httpSession.setAttribute(ATRIBUTO_SESSAO_VALIDACAO, this.recolhimentoService.cadeadoMatriz(datasReabertura, getUsuarioLogado()));
        
        FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
        
        obterBalanceamentoRecolhimento(filtro.getAnoNumeroSemana(), filtro.getListaIdsFornecedores(), TipoBalanceamentoRecolhimento.AUTOMATICO, false);
        
        this.result.use(PlainJSONSerialization.class).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Reabertura realizada com sucesso!"), "result").recursive()
                .serialize();
    }
    
    /**
     * Obtem agrupamento diário para confirmação de Balanceamento
     */
    @Post
    @Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void obterAgrupamentoDiarioBalanceamento() {
        
        List<ConfirmacaoVO> confirmacoesVO = this.montarListaDatasConfirmacao();
        
        if (confirmacoesVO != null) {
            
            result.use(Results.json()).from(confirmacoesVO, "result").serialize();
        }
    }
    
    private void validarDataConfirmacaoConfiguracaoInicial() {
        
        List<ConfirmacaoVO> confirmacoesVO = this.montarListaDatasConfirmacao();
        
        boolean isItensConfirmado = true;
        
        for (ConfirmacaoVO item : confirmacoesVO) {
            if (!item.isConfirmado()) {
                isItensConfirmado = false;
                break;
            }
        }
        
        if (isItensConfirmado) {
            
            String mensagem = " Operação não permitida! Matriz de recolhimento já foi fechada! Não existe itens disponíveis para voltar a configuração inicial.";
            
            throw new ValidacaoException(TipoMensagem.WARNING, mensagem, true);
        }
    }
    
    /**
     * Obtem a concentração ordenada e agrupada por data para a Matriz de
     * Lançamento
     * 
     * @return List<ConfirmacaoVO>: confirmacoesVO
     */
    private List<ConfirmacaoVO> montarListaDatasCadeado() {
        
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = (BalanceamentoRecolhimentoDTO) this.httpSession
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
        
        if (balanceamentoRecolhimento == null || balanceamentoRecolhimento.getMatrizRecolhimento() == null
            || balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Sessão expirada!");
        }
        
        List<ConfirmacaoVO> confirmacoesVO = this.obterDatasCadeado(balanceamentoRecolhimento
                .getMatrizRecolhimento());
        
        return confirmacoesVO;
    }
    
    /**
     * Obtem a concentração ordenada e agrupada por data para a Matriz de
     * Lançamento
     * 
     * @return List<ConfirmacaoVO>: confirmacoesVO
     */
    private List<ConfirmacaoVO> montarListaDatasConfirmacao() {
        
        BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = (BalanceamentoRecolhimentoDTO) this.httpSession
                .getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
        
        if (balanceamentoRecolhimento == null || balanceamentoRecolhimento.getMatrizRecolhimento() == null
            || balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Sessão expirada!");
        }
        
        List<ConfirmacaoVO> confirmacoesVO = this.obterDatasConfirmacao(balanceamentoRecolhimento
                .getMatrizRecolhimento());
        
        return confirmacoesVO;
    }
    
    private List<ConfirmacaoVO> obterDatasCadeado(TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
        
        List<ConfirmacaoVO> confirmacoesVO = new ArrayList<ConfirmacaoVO>();
        
        Map<Date, Boolean> mapaDatasConfirmacaoOrdenada = new LinkedHashMap<Date, Boolean>();
        
        for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
            
            Date novaData = entry.getKey();

            List<ProdutoRecolhimentoDTO> produtosRecolhimento = entry.getValue();

            if (produtosRecolhimento == null || produtosRecolhimento.isEmpty()) {
                
                continue;
            }

            boolean confirmado = false;
            
            for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
                
                confirmado = (!produtoRecolhimento.isBalanceamentoSalvo() && !produtoRecolhimento.isBalanceamentoConfirmado());
                
                if (!confirmado) {
                    
                    break;
                }
            }
            
            mapaDatasConfirmacaoOrdenada.put(novaData, confirmado);
        }
        
        Set<Entry<Date, Boolean>> entrySet = mapaDatasConfirmacaoOrdenada.entrySet();
        
        Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
        
        for (Entry<Date, Boolean> item : entrySet) {
            
        	if (item.getValue() || DateUtil.isDataInicialMaiorIgualDataFinal(item.getKey(), dataOperacao)) {
        	
        		confirmacoesVO.add(new ConfirmacaoVO(DateUtil.formatarDataPTBR(item.getKey()), item.getValue()));
        	}
        }
        
        if (confirmacoesVO.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma data a ser confirmada!");
        }
        
        return confirmacoesVO;
    }

    private List<ConfirmacaoVO> obterDatasConfirmacao(TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
        
        List<ConfirmacaoVO> confirmacoesVO = new ArrayList<ConfirmacaoVO>();
        
        Map<Date, Boolean> mapaDatasConfirmacaoOrdenada = new LinkedHashMap<Date, Boolean>();
        
        for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
            
            Date novaData = entry.getKey();

            List<ProdutoRecolhimentoDTO> produtosRecolhimento = entry.getValue();

            if (produtosRecolhimento == null || produtosRecolhimento.isEmpty()) {
                
                continue;
            }

            boolean confirmado = false;
            
            for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
                
                confirmado = produtoRecolhimento.isBalanceamentoConfirmado();
                
                if (!confirmado) {
                    
                    break;
                }
            }
            
            mapaDatasConfirmacaoOrdenada.put(novaData, confirmado);
        }
        
        Set<Entry<Date, Boolean>> entrySet = mapaDatasConfirmacaoOrdenada.entrySet();
        
        Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
        
        for (Entry<Date, Boolean> item : entrySet) {
            
        	if (item.getValue() || DateUtil.isDataInicialMaiorIgualDataFinal(item.getKey(), dataOperacao)) {
        	
        		confirmacoesVO.add(new ConfirmacaoVO(DateUtil.formatarDataPTBR(item.getKey()), item.getValue()));
        	}
        }
        
        if (confirmacoesVO.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma data a ser confirmada!");
        }
        
        return confirmacoesVO;
    }
    
    private void verificarExecucaoInterfaces() {
        if (distribuidorService.verificaDesbloqueioProcessosLancamentosEstudos()) {
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "As interfaces encontram-se em processamento. Aguarde o termino da execução para continuar!");
        }
    }
    
    public enum ValidacaoDataRecolhimento {
        
        DATA_DIA_CONFIRMADO, DATA_FORA_SEMANA, DATA_VALIDA
    }
    
    @Post
    @Rules(Permissao.ROLE_RECOLHIMENTO_BALANCEAMENTO_MATRIZ_ALTERACAO)
    public void validarLancamentoParaReabertura() {
        
        // this.httpSession.removeAttribute(ATRIBUTO_SESSAO_VALIDACAO);
        /*
         * if(this.httpSession.getAttribute(ATRIBUTO_SESSAO_VALIDACAO)!=null){
         * 
         * String validacao = (String)
         * this.httpSession.getAttribute(ATRIBUTO_SESSAO_VALIDACAO);
         * 
         * if(!validacao.equals("")) { throw new
         * ValidacaoException(TipoMensagem.WARNING, validacao); }
         * 
         * this.httpSession.removeAttribute(ATRIBUTO_SESSAO_VALIDACAO); }
         */
        throw new ValidacaoException(TipoMensagem.WARNING,
                "A chamada de encalhe da data seleciona já foi gerada. Realizar a reimpressão do documento.");
        // this.result.use(Results.json()).from("", "result").serialize();
    }

    /**
     * Adiciona atributo na sessao com todos os produtos para recolhimento
     */
    private void adicionarAtributoProdutosParaRecolhimentoSessao(List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento){
    	
    	this.httpSession.setAttribute(ATRIBUTO_SESSAO_PRODUTOS_RECOLHIMENTO, listaProdutoRecolhimento);
    }
    
    /**
     * Remove atributo na sessao com todos os produtos para recolhimento
     */
    private void removeAtributoProdutosParaRecolhimentoSessao() {
        
        this.httpSession.removeAttribute(ATRIBUTO_SESSAO_PRODUTOS_RECOLHIMENTO);
    }
    
    /**
     * Obtem todos os produtos para recolhimento armazenados na sessão
     */
    private List<ProdutoRecolhimentoDTO> obterAtributoProdutosParaRecolhimentoSessao() {
        
    	@SuppressWarnings("unchecked")
		List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento = (List<ProdutoRecolhimentoDTO>) this.httpSession.getAttribute(ATRIBUTO_SESSAO_PRODUTOS_RECOLHIMENTO);
    	
    	return listaProdutoRecolhimento;
    }
    
    /**
     * Obtem todos os produtos para recolhimento armazenados na sessão 
     * 
     * @return List<ProdutoRecolhimentoFormatadoDTO>
     */
	private List<ProdutoRecolhimentoDTO> obterListaProdutoRecolhimentoDataHolder(boolean todosItens){
    	
        List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento = this.obterAtributoProdutosParaRecolhimentoSessao();
    	
        if(todosItens){
        	return listaProdutoRecolhimento;
        }
        
        return filtrarItensMarcadosParaReprogramacao(listaProdutoRecolhimento); 
    }

	@SuppressWarnings("unchecked")
	private List<ProdutoRecolhimentoDTO> filtrarItensMarcadosParaReprogramacao(
			List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento) {
		
		if(listaProdutoRecolhimento == null || listaProdutoRecolhimento.isEmpty()){
            return listaProdutoRecolhimento;
        }
        
        final Predicate<ProdutoRecolhimentoDTO> itensPredicate = new Predicate<ProdutoRecolhimentoDTO>() {
            
        	@Override
            public boolean apply(final ProdutoRecolhimentoDTO item) {
                
        		String idLancamento = String.valueOf(item.getIdLancamento());
        		
        		boolean isCkecked = ( 
        				getCheckedFromDataHolder(idLancamento) != null
        				&& ("true").equals(getCheckedFromDataHolder(idLancamento))
        		);
        		
            	return isCkecked;
            }
        };
        
        final Collection<ProdutoRecolhimentoDTO> filteredCollection =
                Collections2.filter(listaProdutoRecolhimento, itensPredicate);
        
        return (List<ProdutoRecolhimentoDTO>) ((filteredCollection != null) 
        			? Lists.newArrayList(filteredCollection) 
        					: new ArrayList<>());
	}
    
    private String getCheckedFromDataHolder(String codigo) {
		
		DataHolder dataHolder = (DataHolder) this.httpSession.getAttribute(DataHolder.SESSION_ATTRIBUTE_NAME);
		
		if (dataHolder != null) {

			return dataHolder.getData(DATA_HOLDER_ACTION_KEY, codigo, "checado");
		}
		
		return "false";
	}
    
    @Post
    public void verificarBloqueioMatrizRecolhimentoPost() {
        
        this.verificarBloqueioMatrizRecolhimento();
        
        this.result.use(Results.json()).from(Results.nothing()).serialize();
    }
    
    public void verificarBloqueioMatrizRecolhimento() {
        
        String loginUsuarioContext = 
            (String) this.httpSession.getServletContext().getAttribute(
                    TRAVA_MATRIZ_RECOLHIMENTO_CONTEXT_ATTRIBUTE);
        
        String usuario = super.getUsuarioLogado().getLogin();
        
        if (loginUsuarioContext != null
                && !loginUsuarioContext.equals(usuario)) {
                
            throw new ValidacaoException(
                new ValidacaoVO(TipoMensagem.WARNING, 
                    "A matriz de recolhimento está bloqueada pelo usuário [" 
                        + this.usuarioService.obterNomeUsuarioPorLogin(loginUsuarioContext) + "]. Somente será possível realizar consultas na matriz."));
        }
    }
    
    @Post
    public void bloquearMatrizRecolhimento() {
        
        this.verificarBloqueioMatrizRecolhimento();
        
        String usuario = super.getUsuarioLogado().getLogin();
        
        this.httpSession.getServletContext().setAttribute(
            TRAVA_MATRIZ_RECOLHIMENTO_CONTEXT_ATTRIBUTE, usuario);
        
        this.result.use(Results.json()).from(Results.nothing()).serialize();
    }
    
    @Post
    public void desbloquearMatrizRecolhimentoPost() {
        
        Usuario usuario = getUsuarioLogado();
        
        desbloquearMatrizRecolhimento(this.httpSession.getServletContext(), usuario.getLogin());
        
        this.result.use(Results.json()).from(Results.nothing()).serialize();
    }
    
    public static void desbloquearMatrizRecolhimento(ServletContext servletContext, String usuario) {

        String loginUsuarioContext = 
            (String) servletContext.getAttribute(
                    TRAVA_MATRIZ_RECOLHIMENTO_CONTEXT_ATTRIBUTE);
        
        if (usuario.equals(loginUsuarioContext)) {
            
            servletContext.removeAttribute(TRAVA_MATRIZ_RECOLHIMENTO_CONTEXT_ATTRIBUTE);
        }
    }
    
}