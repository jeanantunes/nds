package br.com.abril.nds.controllers.cadastro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ContratoVO;
import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.controllers.ErrorController;
import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaCotaDTO;
import br.com.abril.nds.dto.ParametroCobrancaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EntregadorService;
import br.com.abril.nds.service.FileService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.FileImportUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cota/parametroCobrancaCota")
public class ParametroCobrancaCotaController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ParametroCobrancaCotaController.class);
    
    private final Result result;
    
    @Autowired
    private ParametroCobrancaCotaService parametroCobrancaCotaService;
    
    @Autowired
    private PoliticaCobrancaService politicaCobrancaService;
    
    @Autowired
    private BancoService bancoService;
    
    @Autowired
    private DistribuidorService distribuidorService;
    
    @Autowired
    private CotaService cotaService;
    
    @Autowired
    private ParametrosDistribuidorService parametroDistribuidorService;
    
    @Autowired
    private Validator validator;
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private EntregadorService entregadorService;
    
    @Autowired
    private ServletContext servletContext;
    
    @Autowired
    private HttpSession session;
    
    @Autowired
    private ParametroSistemaService parametroSistemaService;
    
    private final HttpServletResponse httpResponse;
    
    private List<ItemDTO<TipoCobranca, String>> listaTiposCobranca = new ArrayList<ItemDTO<TipoCobranca, String>>();
    
    private List<ItemDTO<TipoCota, String>> listaTiposCota = new ArrayList<ItemDTO<TipoCota, String>>();
    
    private static final FileType[] extensoesAceitas = { FileType.DOC, FileType.DOCX, FileType.BMP,
        FileType.GIF, FileType.PDF, FileType.JPEG,
        FileType.JPG, FileType.PNG};
    
    
    
    private static final String CONTRATO_UPLOADED = "contratoUploaded";
    
    
    /**
     * Construtor da classe
     * @param result
     * @param session
     * @param httpResponse
     */
    public ParametroCobrancaCotaController(final Result result, final HttpServletResponse httpResponse) {
        this.result = result;
        this.httpResponse = httpResponse;
    }
    
	                                                        /**
     * Método de chamada da página Pré-carrega itens da pagina com informações
     * default.
     */
    @Get
    public void index() {
        
    }
    
    
	                                                        /**
     * Método de Pré-carregamento de itens da pagina com informações default.
     */
    public void preCarregamento(){
        
        listaTiposCobranca = this.carregaTiposCobrancaDistribuidor();
        
        listaTiposCota = cotaService.getComboTiposCota();
        
        result.include("listaBancos", bancoService.getComboBancos(true));
        
        result.include("listaTiposCobranca",listaTiposCobranca);
        
        result.include("listaTiposCota",listaTiposCota);
    }
    
	                                                        /**
     * Carrega somente os tipos de cobrança configurados nas formas de cobrança
     * do distribuidor
     */
    private List<ItemDTO<TipoCobranca,String>> carregaTiposCobrancaDistribuidor(){
        
        final List<TipoCobranca> tcs = politicaCobrancaService.obterTiposCobrancaDistribuidor();
        
        if (tcs == null){
            
            return null;
        }
        
        final List<ItemDTO<TipoCobranca,String>> comboTiposCobranca = new ArrayList<ItemDTO<TipoCobranca,String>>();
        
        for (final TipoCobranca tc : tcs){
            
            final ItemDTO<TipoCobranca, String> itemDTO = new ItemDTO<TipoCobranca,String>(tc, tc.getDescTipoCobranca());
            
            comboTiposCobranca.add(itemDTO);
        }
        
        return comboTiposCobranca;
    }
    
	                                                        /**
     * Obter tipos de cobrança das formas de cobrança utilizadas pelo
     * distribuidor para carregar combo
     */
    @Post
    @Path("/obterTiposCobranca")
    public void obterTiposCobranca(){
        
        final List<ItemDTO<TipoCobranca,String>> comboTiposCobranca = this.carregaTiposCobrancaDistribuidor();
        
        result.use(Results.json()).from(comboTiposCobranca, "result").recursive().serialize();
    }
    
	                                                        /**
     * Método de Pré-carregamento de fornecedores relacionados com a Cota.
     * 
     * @param idCota
     */
    @Post
    @Path("/fornecedoresCota")
    public void fornecedoresCota(final Long idCota, final ModoTela modoTela, final Long idFormaPagto){
        List<ItemDTO<Long,String>> listaFornecedores;
        
        if (ModoTela.CADASTRO_COTA == modoTela) {
            
            listaFornecedores = parametroCobrancaCotaService.getComboFornecedoresCota(idCota);
            
        } else {
            
            final List<FornecedorDTO> fornecedores = parametroCobrancaCotaService.obterFornecedoresFormaPagamentoHistoricoTitularidade(idFormaPagto);
            
            listaFornecedores = new ArrayList<ItemDTO<Long, String>>(fornecedores.size());
            
            for (final FornecedorDTO fornecedorDTO : fornecedores) {
                listaFornecedores.add(new ItemDTO<Long, String>(fornecedorDTO.getIdFornecedor(), fornecedorDTO.getRazaoSocial()));
            }
        }
        result.use(Results.json()).from(listaFornecedores, "result").recursive().serialize();
    }
    
    @Post
    @Path("/carregarBancos")
    public void carregarBancos(){
        result.use(Results.json()).from(bancoService.getComboBancos(true), "result").recursive().serialize();
    }
    
	                                                        /**
     * Método responsável por obter os parametros de cobranca da Cota para a aba
     * 'Financeiro'.
     * 
     * @throws Mensagens de Validação.
     * @param idCota
     */
    @Post
    @Path("/obterParametroCobranca")
    public void obterParametroCobranca(final Long idCota, final ModoTela modoTela, final Long idHistorico) {
        
        validar();
        
        if (idCota == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "O código da cota informado náo é válido.");
        }
        
        ParametroCobrancaCotaDTO parametroCobranca;
        if (ModoTela.CADASTRO_COTA == modoTela) {
            
            parametroCobranca = parametroCobrancaCotaService.obterDadosParametroCobrancaPorCota(idCota);
            
        } else {
            
            parametroCobranca = parametroCobrancaCotaService.obterParametrosCobrancaHistoricoTitularidadeCota(idCota, idHistorico);
            
        }
        
        if (parametroCobranca != null) {
            
            result.use(Results.json()).from(parametroCobranca,"result").recursive().serialize();
            
        } else {
            
            result.nothing();
        }
    }
    
    @Post
    @Path("/obterParametroCobrancaDistribuidor")
    public void obterParametroCobrancaDistribuidor(final String op) {
        
        if(op==null || op.isEmpty() || op.equals("-1")) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de Pagamento não selecionado.");
        }
        
        final TipoCobranca tipoCobranca = TipoCobranca.valueOf(op);
        
        final List<PoliticaCobranca> politicasCobranca = politicaCobrancaService.obterDadosPoliticaCobranca(tipoCobranca);
        
        ParametroCobrancaDTO parametroCobranca = null;
        
        if(!politicasCobranca.isEmpty()) {
            parametroCobranca = politicaCobrancaService.obterDadosPoliticaCobranca(politicasCobranca.get(0).getId());
        }
        
        FormaCobrancaDTO forma = new FormaCobrancaDTO();
        
        if(parametroCobranca!=null) {
            forma = new FormaCobrancaDTO();
            forma.setTipoCobranca(parametroCobranca.getTipoCobranca());
            forma.setTipoFormaCobranca(parametroCobranca.getTipoFormaCobranca());
            forma.setIdBanco(parametroCobranca.getIdBanco());
            forma.setRecebeEmail(parametroCobranca.isEnvioEmail());
            forma.setDomingo(parametroCobranca.isDomingo());
            forma.setSegunda(parametroCobranca.isSegunda());
            forma.setTerca(parametroCobranca.isTerca());
            forma.setQuarta(parametroCobranca.isQuarta());
            forma.setQuinta(parametroCobranca.isQuinta());
            forma.setSexta(parametroCobranca.isSexta());
            forma.setSabado(parametroCobranca.isSabado());
            forma.setFornecedoresId(parametroCobranca.getFornecedoresId());
        }
        
        result.use(Results.json()).from(forma,"result").recursive().serialize();
    }
    
	                                                        /**
     * Método responsável por obter os dados da uma forma de cobranca do
     * parametro de cobranca da Cota para a aba 'Financeiro'.
     * 
     * @throws Mensagens de Validação.
     * @param idFormaCobranca
     */
    @Post
    @Path("/obterFormaCobranca")
    public void obterFormaCobranca(final Long idFormaCobranca, final ModoTela modoTela) {
        FormaCobrancaDTO formaCobranca = null;
        validar();
        if (idFormaCobranca == null) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "O código da forma de cobrança informado náo é válido.");
        }
        
        if (ModoTela.CADASTRO_COTA == modoTela) {
            formaCobranca = parametroCobrancaCotaService
                    .obterDadosFormaCobranca(idFormaCobranca);
        } else {
            formaCobranca = parametroCobrancaCotaService
                    .obterFormaPagamentoHistoricoTitularidade(idFormaCobranca);
        }
        
        if (formaCobranca == null) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Nenhuma forma de cobrança encontrada para o código de forma de cobrança.");
        }
        
        result.use(Results.json()).from(formaCobranca, "result").recursive()
        .serialize();
    }
    
    @Post
    public void obterQtdFormaCobrancaCota(final Long id){
        final int qtdFormaCobranca = parametroCobrancaCotaService.obterQuantidadeFormasCobrancaCota(id);
        
        result.use(Results.json()).withoutRoot().from(qtdFormaCobranca).serialize();
    }
    
	/**
     * Retorna formas de cobrança da cota para preencher a grid da view
     * 
     * @param idCota
     */
    @Post
    @Path("/obterFormasCobranca")
    public void obterFormasCobranca(final Long idCota, final ModoTela modoTela, final Long idHistorico){
        List<FormaCobrancaDTO> listaFormasCobranca = null;
        int qtdeRegistros;
        
        if (ModoTela.CADASTRO_COTA == modoTela) {
            //VALIDACOES
            validar();
            
            //BUSCA FORMAS DE COBRANCA DA COTA
            listaFormasCobranca = parametroCobrancaCotaService.obterDadosFormasCobrancaPorCota(idCota);
            qtdeRegistros = listaFormasCobranca != null ? listaFormasCobranca.size() : 0;
            
        } else {
            listaFormasCobranca = parametroCobrancaCotaService.obterFormasCobrancaHistoricoTitularidadeCota(idCota, idHistorico);
            qtdeRegistros = listaFormasCobranca.size();
        }
        
        final TableModel<CellModelKeyValue<FormaCobrancaDTO>> tableModel =
                new TableModel<CellModelKeyValue<FormaCobrancaDTO>>();
        
        tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaFormasCobranca));
        tableModel.setPage(1);
        tableModel.setTotal(qtdeRegistros);
        
        result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
    }
   
	/**
     * Método responsável por obter os dados default da forma de cobranca
     * principal dos parametros de cobrança do distribuidor.
     * 
     * @throws Mensagens de Validação.
     */
    @Post
    @Path("/obterFormaCobrancaDefault")
    public void obterFormaCobrancaDefault() {
        
        final PoliticaCobranca politicaPrincipal = politicaCobrancaService.obterPoliticaCobrancaPrincipal();
        
        if (politicaPrincipal==null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma forma de cobrança default encontrada.");
        }
        
        final ParametroCobrancaDTO parametroCobrancaDistribuidor = politicaCobrancaService.obterDadosPoliticaCobranca(politicaPrincipal.getId());
        
        result.use(Results.json()).from(parametroCobrancaDistribuidor,"result").recursive().serialize();
    }
    
	/**
     * Método responsável por postar os dados do parametro de cobrança da cota.
     * 
     * @param cotaCobranca: Data Transfer Object com os dados cadastrados ou
     *            alterados pelo usuário
     */
    @Post
    @Path("/postarParametroCobranca")
    public void postarParametroCobranca(final ParametroCobrancaCotaDTO parametroCobranca, boolean parametroCobrancaAlterado){
        
        validarParametroCobrancaCota(parametroCobranca);
        
        if (parametroCobrancaCotaService.obterQuantidadeFormasCobrancaCota(parametroCobranca.getIdCota()) == 0
        		&& parametroCobrancaAlterado) {

            parametroCobrancaCotaService.inserirFormaCobrancaDoDistribuidorNaCota(parametroCobranca);
            
        } else {
            parametroCobrancaCotaService.postarParametroCobranca(parametroCobranca);
        }
        
        if(parametroCobranca.isContrato()){
        	this.salvarContrato(parametroCobranca.getIdCota(), parametroCobranca.getInicioContrato(), parametroCobranca.getTerminoContrato());
        }
        
        cotaService.salvarCaracteristicasFinanceirasEspecificasCota(parametroCobranca);
        
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Parametros de Cobrança Cadastrados."),
                						Constantes.PARAM_MSGS).recursive().serialize();
    }
    
    private void validarParametroCobrancaCota(final ParametroCobrancaCotaDTO parametroCobranca) {
        
        if(parametroCobranca.getIdFornecedor() == null) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Escolha um Fornecedor Padrão para o Financeiro da Cota.");
        }
    }
    
	/**
     * Formata os dados de FormaCobranca, apagando valores que não são
     * compatíveis com o Tipo de Cobranca escolhido.
     * 
     * @param formaCobranca
     */
    private FormaCobrancaDTO formatarFormaCobranca(final FormaCobrancaDTO formaCobranca){
        
        if (formaCobranca.getTipoFormaCobranca().equals(TipoFormaCobranca.SEMANAL)){
            
            formaCobranca.setDiaDoMes(null);
            formaCobranca.setPrimeiroDiaQuinzenal(null);
            formaCobranca.setSegundoDiaQuinzenal(null);
        }
        
        if (formaCobranca.getTipoFormaCobranca().equals(TipoFormaCobranca.MENSAL)){
            
            formaCobranca.setDomingo(false);
            formaCobranca.setSegunda(false);
            formaCobranca.setTerca(false);
            formaCobranca.setQuarta(false);
            formaCobranca.setQuinta(false);
            formaCobranca.setSexta(false);
            formaCobranca.setSabado(false);
            formaCobranca.setPrimeiroDiaQuinzenal(null);
            formaCobranca.setSegundoDiaQuinzenal(null);
        }
        
        if (formaCobranca.getTipoFormaCobranca().equals(TipoFormaCobranca.DIARIA)){
            
            formaCobranca.setDomingo(false);
            formaCobranca.setSegunda(false);
            formaCobranca.setTerca(false);
            formaCobranca.setQuarta(false);
            formaCobranca.setQuinta(false);
            formaCobranca.setSexta(false);
            formaCobranca.setSabado(false);
            formaCobranca.setDiaDoMes(null);
            formaCobranca.setPrimeiroDiaQuinzenal(null);
            formaCobranca.setSegundoDiaQuinzenal(null);
        }
        
        if (formaCobranca.getTipoFormaCobranca().equals(TipoFormaCobranca.QUINZENAL)){
            
            formaCobranca.setDomingo(false);
            formaCobranca.setSegunda(false);
            formaCobranca.setTerca(false);
            formaCobranca.setQuarta(false);
            formaCobranca.setQuinta(false);
            formaCobranca.setSexta(false);
            formaCobranca.setSabado(false);
            formaCobranca.setDiaDoMes(null);
        }
        
        if ((formaCobranca.getTipoCobranca().equals(TipoCobranca.BOLETO))||(formaCobranca.getTipoCobranca().equals(TipoCobranca.BOLETO_EM_BRANCO))){
            
            formaCobranca.setNumBanco("");
            formaCobranca.setNomeBanco("");
            formaCobranca.setAgencia(null);
            formaCobranca.setAgenciaDigito("");
            formaCobranca.setConta(null);
            formaCobranca.setContaDigito("");
        } else if (formaCobranca.getTipoCobranca().equals(TipoCobranca.DEPOSITO)) {
            
            formaCobranca.setNumBanco("");
            formaCobranca.setNomeBanco("");
            formaCobranca.setAgencia(null);
            formaCobranca.setAgenciaDigito("");
            formaCobranca.setConta(null);
            formaCobranca.setContaDigito("");
        } else if ((formaCobranca.getTipoCobranca().equals(TipoCobranca.DINHEIRO))
            || ((formaCobranca.getTipoCobranca().equals(TipoCobranca.OUTROS)))) {
            
            formaCobranca.setNumBanco("");
            formaCobranca.setNomeBanco("");
            formaCobranca.setAgencia(null);
            formaCobranca.setAgenciaDigito("");
            formaCobranca.setConta(null);
            formaCobranca.setContaDigito("");
            formaCobranca.setIdBanco(null);
        }
        
        return formaCobranca;
    }
    
	                                                        /**
     * Método responsável por persistir os dados da forma de cobranca no banco
     * de dados.
     * 
     * @param formaCobranca
     * @param tipoFormaCobranca
     * @param listaIdsFornecedores
     */
    @Post
    @Path("/postarFormaCobranca")
    public void postarFormaCobranca(FormaCobrancaDTO formaCobranca, final String tipoFormaCobranca, final List<Long> listaIdsFornecedores, final ParametroCobrancaCotaDTO parametroCobranca){
        
        if(tipoFormaCobranca == null)
            throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de Pagamento não selecionado.");
        
        if ((tipoFormaCobranca!=null)&&(!"".equals(tipoFormaCobranca))){
            formaCobranca.setTipoFormaCobranca(TipoFormaCobranca.valueOf(tipoFormaCobranca));
        }
        
        if ((listaIdsFornecedores==null)|| (listaIdsFornecedores.isEmpty())){
        	throw new ValidacaoException(TipoMensagem.WARNING, "Fornecedores  não foram informados.");
        }
        
        formaCobranca.setFornecedoresId(listaIdsFornecedores);

        parametroCobrancaCotaService.validarFormaCobranca(formaCobranca);
        
        formaCobranca = formatarFormaCobranca(formaCobranca);
                
        // Caso a cota não possua formas de cobrança, informa que essa cobrança
        // veio pelo distribuidor
        if (parametroCobrancaCotaService.obterQuantidadeFormasCobrancaCota(formaCobranca.getIdCota()) == 0) {
            formaCobranca.setParametroDistribuidor(true);
        }
        
        if(parametroCobranca != null
                && parametroCobranca.getIdCota() != null
                && parametroCobranca.getValorMinimo() != null
                && parametroCobranca.getIdFornecedor() != null) {
            
            validarParametroCobrancaCota(parametroCobranca);
            
            parametroCobrancaCotaService.postarParametroCobranca(parametroCobranca);
        }
        
        parametroCobrancaCotaService.postarFormaCobranca(formaCobranca);
        
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Forma de Cobrança Cadastrada."),
						                Constantes.PARAM_MSGS)
						                .recursive().serialize();
    }
    
    @Post
    public void carregarArquivoContrato(final Long idCota, final int numeroCota) {
        
        final File tempDir = this.getTemporaryDirUpload(numeroCota);
        
        final ContratoVO contrato = parametroCobrancaCotaService.obterArquivoContratoRecebido(idCota, tempDir);
        
        if (contrato != null) {
            session.setAttribute(CONTRATO_UPLOADED, contrato);
            result.include("isRecebido", contrato.isRecebido());
            
            final File arquivo = contrato.getTempFile();
            
            String fileName = null;
            final Map<String, Object> mapa = new TreeMap<String, Object>();
            if (arquivo != null) {
                fileName = arquivo.getName();
                mapa.put("fileName", fileName);
                result.include("fileName", fileName);
            }
            
            mapa.put("isRecebido", contrato.isRecebido());
            
            result.use(CustomJson.class).from(mapa).serialize();
        } else {
            session.removeAttribute(CONTRATO_UPLOADED);
            result.nothing();
        }
    }
    
    /**
     * Método responsável por desativar Forma de Cobranca
     * 
     * @param idFormaCobranca
     */
    @Post
    @Path("/excluirFormaCobranca")
    public void excluirFormaCobranca(final Long idFormaCobranca){
        
		this.parametroCobrancaCotaService.excluirParametroCobrancaCota(idFormaCobranca);
		
		this.parametroCobrancaCotaService.excluirFormaCobranca(idFormaCobranca);
        
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Forma de Cobrança Excluida."), Constantes.PARAM_MSGS).recursive().serialize();
    }
    
    /**
     * Exibe o  em formato PDF.
     * @param idCota
     * @throws Exception
     */
    @Get
    @Path("/imprimeContrato")
    public void imprimeContrato(final Long idCota, final Date dataInicio, final Date dataTermino, final boolean isRecebido) throws Exception {
        
        byte[] bytes = null;
        
        ContratoVO contrato = (ContratoVO) (session.getAttribute(CONTRATO_UPLOADED));
        
        File arquivo = null;
        
        String contentType = "application/pdf";
        String extension = ".pdf";
        
        if (contrato != null) {
            
            arquivo = contrato.getTempFile();
            
            contrato.setDataInicio(dataInicio);
            contrato.setDataTermino(dataTermino);
            contrato.setIdCota(idCota);
            contrato.setRecebido(isRecebido);
            
        } else {
            contrato = new ContratoVO(dataInicio, dataTermino, isRecebido, idCota);
        }
        
        if (isRecebido && (arquivo != null)) {
            bytes = this.obterArquivoAnexo();
            
            extension = FileImportUtil.getExtensionFile(arquivo.getName());
            contentType = FileImportUtil.getContentTypeByExtension(extension);
            
        } else {
            try {
                bytes = parametroCobrancaCotaService.geraImpressaoContrato(idCota, dataInicio, dataTermino);
            } catch (final ValidacaoException e) {
                
                LOGGER.error(e.getMessage(), e);
                result.forwardTo(ErrorController.class).showError(e);
            }
        }
        
        session.setAttribute(CONTRATO_UPLOADED, contrato);
        
        if(bytes != null && bytes.length > 0) {
            httpResponse.setContentType(contentType);
            httpResponse.setHeader("Content-Disposition", "attachment; filename=contrato"+extension);
            
            final OutputStream output = httpResponse.getOutputStream();
            output.write(bytes);
            
            httpResponse.flushBuffer();
        }
    }
    
    /**
     * @return obtém arquivo anexo
     */
    private byte[] obterArquivoAnexo() {
        
        final ContratoVO contrato = (ContratoVO) session.getAttribute(CONTRATO_UPLOADED);
        
        final File arquivoAnexo = contrato.getTempFile();
        
        byte[] arquivo = null;
        
        try {
            
            final FileInputStream fis = FileUtils.openInputStream(arquivoAnexo);
            
            arquivo = IOUtils.toByteArray(fis);
            
            fis.close();
            
        } catch (final IOException e) {
            
            LOGGER.error(e.getMessage(), e);
        }
        
        return arquivo;
    }
    
    private boolean salvarContrato(Long idCota, final Date inicioContrato, final Date terminoContrato){
    	
    	parametroCobrancaCotaService.salvarContrato(idCota, session.getAttribute(CONTRATO_UPLOADED) != null, inicioContrato, terminoContrato);
        
        if (session.getAttribute(CONTRATO_UPLOADED) != null) {
            
            final ContratoVO contrato = (ContratoVO) session.getAttribute(CONTRATO_UPLOADED);
            
            if(contrato != null) {
            	
                contrato.setDataInicio(inicioContrato);
                contrato.setDataTermino(terminoContrato);

                final File file = contrato.getTempFile();
                
                if (file != null) {
                    
                    final ParametroSistema pathContrato =
                            parametroSistemaService.buscarParametroPorTipoParametro(
                                    TipoParametroSistema.PATH_IMPORTACAO_CONTRATO);
                    
                    final Cota cota = cotaService.obterPorId(contrato.getIdCota());
                    
                    final File path = new File(pathContrato.getValor(), cota.getNumeroCota().toString());
                    
                    path.mkdirs();
                    
                    fileService.limparDiretorio(path);
                    
                    final File novoArquivo = new File(path, file.getName());
                    
                    FileOutputStream fos = null;
                    
                    FileInputStream fis = null;
                    
                    try {
                        fileService.persistirTemporario(path.toString());
                        
                        fos = new FileOutputStream(novoArquivo);
                        
                        fis = new FileInputStream(file);
                        
                        IOUtils.copyLarge(fis, fos);
                        
                        fileService.eliminarReal(path.getAbsolutePath());
                        
                    } catch (final IOException e) {
                        
                        LOGGER.error("Falha ao persistir contrato anexo", e);
                        
                        throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Falha ao persistir contrato anexo"));
                    } finally {
                        
                        if(fis != null) {
                            try {
                                fis.close();
                            } catch (final IOException e) {
                                LOGGER.error("Falha ao fechar o arquivo", e);
                            }
                        }
                        
                        if(fos != null) {
                            try {
                                fos.close();
                            } catch (final IOException e) {
                                LOGGER.error("Falha ao fechar o arquivo", e);
                            }
                        }
                    }
                }
            }
            return true;
        }
        
        return false;
    }
    
	/**
     * Método responsável por postar os dados da aba financeiro que são
     * específicos da cota.
     * 
     * @param parametroCobranca
     */
    @Post
    @Path("/salvarContratoECaracteristicasFinanceirasEspecificasCota")
    public void salvarContratoECaracteristicasFinanceirasEspecificasCota(final ParametroCobrancaCotaDTO parametroCobranca) {
        
        String msg1 = "";
        String msg2 = "";
        String msg = "";
        
        if ((parametroCobranca.isContrato()) && (this.salvarContrato(parametroCobranca.getIdCota(), parametroCobranca.getInicioContrato(), parametroCobranca.getTerminoContrato()))){
            
            msg1 = "Contrato";
        }
        
        if (cotaService.salvarCaracteristicasFinanceirasEspecificasCota(parametroCobranca)){
            
            msg2 = "Características Financeiras Específicas";
        }
        
        if (!msg1.equals("") || !msg2.equals("")){
            
            if (msg1.equals("")){
                
                msg1=msg2;
                
                msg2="";
            }
            
            msg = (msg1+(!msg2.equals("")?" e "+msg2:"")+" cadastrado com sucesso.");
            
            result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, msg),Constantes.PARAM_MSGS).recursive().serialize();
        }
        
        result.nothing();
    }
    
    @Post
    public void uploadContratoAnexo(final UploadedFile uploadedFile, final String numeroCota) throws IOException {
        
        if (uploadedFile == null) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Falha ao carregar arquivo!"));
        }
        
        fileService.validarArquivo(1, uploadedFile, extensoesAceitas);
        
        ContratoVO contrato = new ContratoVO();
        
        if (session.getAttribute(CONTRATO_UPLOADED) != null) {
            
            contrato = (ContratoVO) session.getAttribute(CONTRATO_UPLOADED);
            
            final File arquivo = contrato.getTempFile();
            
            if (arquivo != null && arquivo.exists()) {
                arquivo.delete();
            }
        }
        
        final String fileName = uploadedFile.getFileName();
        
        final File diretorio = this.getTemporaryDirUpload(Integer.parseInt(numeroCota));
        
        final File arquivo = new File(diretorio, fileName);
        
        FileOutputStream fileOutStream = null;;
        
        diretorio.mkdirs();
        
        try {
            
            fileOutStream = new FileOutputStream(arquivo);
            
            IOUtils.copyLarge(uploadedFile.getFile(), fileOutStream);
            
        } catch (final IOException ioe) {
            LOGGER.error(ioe.getMessage(), ioe);
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "Falha ao gravar o arquivo em disco!");
        } finally {
            try {
                if (fileOutStream != null) {
                    fileOutStream.close();
                }
            } catch (final Exception e) {
                
                LOGGER.error(e.getMessage(), e);
                throw new ValidacaoException(TipoMensagem.ERROR,
                        "Falha ao gravar o arquivo em disco!");
            }
        }
        
        final Cota cota = cotaService.obterPorNumeroDaCota(Integer.valueOf(numeroCota));
        
        contrato.setIdCota(cota.getId());
        contrato.setTempFile(arquivo);
        contrato.setRecebido(true);
        session.setAttribute(CONTRATO_UPLOADED, contrato);
        
        result.use(PlainJSONSerialization.class).from(fileName, "fileName").recursive().serialize();
    }
    
    /**
     * Remove arquivo anexo
     */
    @Post("/removerUpload.json")
    public void removerUpload() {
        
        final ContratoVO contrato = (ContratoVO) session.getAttribute(CONTRATO_UPLOADED);
        
        if (contrato != null) {
            
            final File arquivo = contrato.getTempFile();
            
            if(arquivo != null && arquivo.delete()) {
                
                contrato.setTempFile(null);
                contrato.setRecebido(false);
            }
        }
        
        result.nothing();
    }
    
	                                                        /**
     * Obtém um diretório temporario para upload
     * 
     * @param numeroCota
     * @return diretorio temporario
     */
    private File getTemporaryDirUpload(final int numeroCota) {
        
        String pathAplicacao = servletContext.getRealPath("");
        
        pathAplicacao = pathAplicacao.replace("\\", "/");
        
        final File diretorioContratos = new File(pathAplicacao, "contratos");
        
        final String diretorioCotaTemp = numeroCota+File.separator+"temp";
        
        return new File(diretorioContratos, diretorioCotaTemp);
    }
    
	                                                        /**
     * Método responsável pela validação dos dados e rotinas.
     */
    public void validar(){
        
        if (validator.hasErrors()) {
            final List<String> mensagens = new ArrayList<String>();
            for (final Message message : validator.getErrors()) {
                mensagens.add(message.getMessage());
            }
            final ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, mensagens);
            throw new ValidacaoException(validacao);
        }
    }
    
    
    @Post("/calcularDataTermino.json")
    public void calcularDataTermino(final Date dataInicio) {
        
        final ParametrosDistribuidorVO parametrosDistribuidor = parametroDistribuidorService.getParametrosDistribuidor();
        
        if(parametrosDistribuidor.getPrazoContrato() == null) {
            result.nothing();
            return;
        }
        
        final int prazoContratoEmMeses = parametrosDistribuidor.getPrazoContrato();
        
        final Calendar dataTermino = Calendar.getInstance();
        
        dataTermino.setTime(dataInicio);
        
        dataTermino.add(Calendar.MONTH, prazoContratoEmMeses);
        
        result.use(Results.json()).from(dataTermino.getTime(),"dataTermino").recursive().serialize();
    }
    
    @Post
    public void verificarEntregador(final Long idCota){
        
        result.use(Results.json()).from(entregadorService.verificarEntregador(idCota), "result").serialize();
    }
    
    @Post("/obterPoliticaSuspensaoDistribuidor.json")
    public void obterPoliticaSuspensaoDistribuidor(){
        
    	Distribuidor d = this.distribuidorService.obter();
    	
        PoliticaSuspensao pl = d.getPoliticaSuspensao();

        ParametroCobrancaCotaDTO pDTO = new ParametroCobrancaCotaDTO();
        
        pDTO.setSugereSuspensao(d.isSugereSuspensao());
        
        pDTO.setQtdDividasAberto(pl!=null?pl.getNumeroAcumuloDivida():null);
        
        pDTO.setVrDividasAberto(pl!=null?CurrencyUtil.formatarValor(pl.getValor()):null);
        
        this.result.use(Results.json()).from(pDTO, "result").recursive().serialize();
    }
}
