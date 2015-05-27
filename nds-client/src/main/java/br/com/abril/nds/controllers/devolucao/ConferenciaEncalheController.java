package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.component.BloqueioConferenciaEncalheComponent;
import br.com.abril.nds.client.log.LogFuncional;
import br.com.abril.nds.client.util.Constants;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.component.ConferenciaEncalheAsyncComponent;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DadosDocumentacaoConfEncalheCotaDTO;
import br.com.abril.nds.dto.DataCEConferivelDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.enums.TipoDocumentoConferenciaEncalhe;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.ChamadaEncalheCotaService;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.GrupoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.NFeService;
import br.com.abril.nds.service.NaturezaOperacaoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.exception.EncalheRecolhimentoParcialException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.session.scoped.ConferenciaEncalheSessionScopeAttr;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.PDFUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

import com.itextpdf.text.pdf.codec.Base64;

@Resource
@Path(value="/devolucao/conferenciaEncalhe")
@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA)
public class ConferenciaEncalheController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConferenciaEncalheController.class);	
	
	private static final String DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA = "dadosDocumentacaoConfEncalheCota";
	
	private static final String CONF_IMPRESSAO_ENCALHE_COTA = "configImpressaoEncalheCota";
	
	private static final String TIPOS_DOCUMENTO_IMPRESSAO_ENCALHE = "tipos_documento_impressao_encalhe";
	
	private static final String INFO_CONFERENCIA = "infoCoferencia";
	
	private static final String NOTA_FISCAL_CONFERENCIA = "notaFiscalConferencia";
	
	private static final String HORA_INICIO_CONFERENCIA = "horaInicioConferencia";
	
	private static final String NUMERO_COTA = "numeroCotaConferenciaEncalhe";
	
	private static final String COTA = "cotaConferenciaEncalhe";
	
	private static final int QUANTIDADE_MAX_REGISTROS = 15;
	
	private static final String CONFERENCIA_ENCALHE_COTA_STATUS = "CONFERENCIA_ENCALHE_COTA_STATUS";
	
	private static final String DATAS_ENCALHE_CONFERIVEIS = "DATAS_ENCALHE_CONFERIVEIS";
	
	private static final String IND_COTA_EXIGE_NFE = "IND_COTA_EXIGE_NFE";
	
	private static final String VENDA_NEGATIVA_AUTORIZADA_NA_CONFERENCIA = "vendaNegativaAutorizadaNaConferencia";
	
	private static final String IND_USUARIO_SUPERVISOR = "indUsuarioSupervisorParaConferenciaEncalhe";
	
	private static String ID_BOX_LOGADO_SESSION = "idBoxLogado";
	
	private static String QTD = "QTD";
	
	/*
     * Conferência de encalhe da cota que foi iniciada porém ainda não foi
     * salva.
     */
	private static final String CONF_ENC_COTA_STATUS_INICIADA_NAO_SALVA = "INICIADA_NAO_SALVA";
	
	/*
     * Conferência de encalhe da cota que foi iniciada e já foi salva.
     */
	private static final String CONF_ENC_COTA_STATUS_INICIADA_SALVA = "INICIADA_SALVA";
	
	/*
     * Nenhuma conferência de encalhe da cota iniciada em aberto.
     */
	private static final String CONF_ENC_COTA_STATUS_NAO_INICIADA = "NAO_INICIADA";

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;
	
	@Autowired
	private ConferenciaEncalheAsyncComponent conferenciaEncalheAsyncComponent;  
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private NaturezaOperacaoService naturezaOperacaoService;

	@Autowired
	private GrupoService grupoService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private ChamadaEncalheCotaService chamadaEncalheCotaService;
	
	@Autowired
    private NFeService nFeService;
	
	@Autowired
	private BloqueioConferenciaEncalheComponent bloqueioConferenciaEncalheComponent;
	
	@Autowired
	private ConferenciaEncalheSessionScopeAttr conferenciaEncalheSessionScopeAttr;
	
	private void preCarregarBoxes() {
		
		 // Obter box usuário
		if(this.getUsuarioLogado() != null && this.getUsuarioLogado().getBox() != null && 
				this.getUsuarioLogado().getBox().getId() != null) {
			
			if(conferenciaEncalheSessionScopeAttr != null) {
				conferenciaEncalheSessionScopeAttr.setIdBoxLogado(this.getUsuarioLogado().getBox().getId());
			}
			
			this.result.include("idBoxLogado", conferenciaEncalheSessionScopeAttr.getIdBoxLogado());
		}
		
		limparDadosSessao();
		carregarComboBoxEncalhe();
	}
	
	@Path("/")
	@LogFuncional(value="Conferência de Encalhe [Abertura da tela]")
	public void index() {
		
		bloqueioConferenciaEncalheComponent.validarUsuarioConferindoCota(this.session, null);
		
		this.result.include("dataOperacao", DateUtil.formatarDataPTBR(distribuidorService.obterDataOperacaoDistribuidor()));
		
		final TipoContabilizacaoCE tipoContabilizacaoCE = conferenciaEncalheService.obterTipoContabilizacaoCE();
		
		if(tipoContabilizacaoCE!=null) {
			this.result.include("tipoContabilizacaoCE", tipoContabilizacaoCE.name());
		}
		
        this.preCarregarBoxes();
	}
	
	@Path("/contingencia")
	@LogFuncional(value="Conferência de Encalhe [Abertura tela Contingência]")
	public void contingencia() {
		
		final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();

		this.result.include("dataOperacao", DateUtil.formatarDataPTBR(dataOperacao));
		
		final TipoContabilizacaoCE tipoContabilizacaoCE = conferenciaEncalheService.obterTipoContabilizacaoCE();
		
		if(tipoContabilizacaoCE!=null) {
			this.result.include("tipoContabilizacaoCE", tipoContabilizacaoCE.name());
		}
		
		this.preCarregarBoxes();
	}
	
	public void carregarComboBoxEncalheContingencia() {
		
		final List<Box> boxes = this.conferenciaEncalheService.obterListaBoxEncalhe(this.getUsuarioLogado().getId());
		
		if(boxes != null) {

			final Map<String, String> mapBox = new HashMap<String, String>();
			
			for(final Box box : boxes) {
				mapBox.put(box.getId().toString(), box.getNome());
			}
			
			this.result.use(CustomJson.class).from(mapBox).serialize();
		} else {

			this.result.use(Results.json()).from("").serialize();
		}
	}
	
	private void carregarComboBoxEncalhe() {
		
		final List<Box> boxes = this.conferenciaEncalheService.obterListaBoxEncalhe(this.getUsuarioLogado().getId());
		
		this.result.include("boxes", boxes);
	}
	
	@Post
	@Path("/obterBoxLogado")
	public void obterBoxLogado() {
		
        Long idBoxlogado = (Long) this.session.getAttribute(ID_BOX_LOGADO_SESSION);
        
        if(idBoxlogado != null) {

    		this.result.use(Results.json()).from(idBoxlogado).serialize();

        } else {
    		
        	idBoxlogado = (usuarioService.getUsuarioLogado() != null) ? 
        						(usuarioService.getUsuarioLogado().getBox() != null ? usuarioService.getUsuarioLogado().getBox().getId() : 0L) : 0L;
        	this.result.use(Results.json()).from(idBoxlogado == 0L ? "" : idBoxlogado).serialize();
        	
        }
        
	}
	
	@Post
	@LogFuncional(value="Conferência de Encalhe [Escolha do box]")
	public void salvarIdBoxSessao(final Long idBox){
		
		if (idBox != null){
		
			final Usuario usuarioLogado = this.getUsuarioLogado();
			this.session.setAttribute("usuarioSupervisor", usuarioLogado.isSupervisor());
			
			conferenciaEncalheSessionScopeAttr.setIdBoxLogado(idBox);
			
			this.session.setAttribute(ID_BOX_LOGADO_SESSION, idBox);
			
			this.result.include("boxes", idBox);
			
			alterarBoxUsuario(idBox);
		} else {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Box de recolhimento é obrigatório.");
		}
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	/**
     * Realiza a remoção da trava da session do usuario com conferencia de
     * encalhe
     */
	@Post
	public void removerTravaConferenciaEncalheCotaUsuario() {
		
		bloqueioConferenciaEncalheComponent.removerTravaConferenciaCotaUsuario(session);
		
		this.result.nothing();
	}
	
	class StatusConferenciaEncalheCota {
		
		private boolean indConferenciaEncalheCotaSalva;

		public Integer getNumeroCota() {
			return (Integer) session.getAttribute(NUMERO_COTA);
		}

		public boolean isIndConferenciaEncalheCotaSalva() {
			return indConferenciaEncalheCotaSalva;
		}

		public void setIndConferenciaEncalheCotaSalva(
				final boolean indConferenciaEncalheCotaSalva) {
			this.indConferenciaEncalheCotaSalva = indConferenciaEncalheCotaSalva;
		}
		
	}
	
	private StatusConferenciaEncalheCota obterStatusConferenciaEncalheCotaFromSession() {
		
		StatusConferenciaEncalheCota statusConferenciaEncalheCota = (StatusConferenciaEncalheCota) this.session.getAttribute(CONFERENCIA_ENCALHE_COTA_STATUS);
		
		if(statusConferenciaEncalheCota == null) {
			statusConferenciaEncalheCota = new StatusConferenciaEncalheCota();
			statusConferenciaEncalheCota.setIndConferenciaEncalheCotaSalva(true);
			this.session.setAttribute(CONFERENCIA_ENCALHE_COTA_STATUS, statusConferenciaEncalheCota);
		}
		
		return statusConferenciaEncalheCota;
		
	}
	
	            /**
     * Verifica se o usuario esta iniciando (ou reiniciando) a conferência de
     * uma cota sem ter salvo (ou finalizado) os dados de uma conferência em andamento.
     * @param numeroCota
     */
	public void verificarConferenciaEncalheCotaStatus(final Integer numeroCota) {
		
		final Map<String, Object> resultado = new HashMap<String, Object>();
		
		final String conferenciaEncalheCotaStatus = obterStatusConferenciaEncalheCota();
		
		if(numeroCota != null) {
			resultado.put("NUMERO_COTA_IGUAL", numeroCota.equals(session.getAttribute(NUMERO_COTA)));
		}
		
		resultado.put(CONFERENCIA_ENCALHE_COTA_STATUS, conferenciaEncalheCotaStatus);
		
		this.result.use(CustomJson.class).from(resultado).serialize();
	}
	
	private String obterStatusConferenciaEncalheCota() {
		
		final StatusConferenciaEncalheCota statusConferenciaEncalheCota = obterStatusConferenciaEncalheCotaFromSession();
		
		if( statusConferenciaEncalheCota.getNumeroCota() != null && !statusConferenciaEncalheCota.isIndConferenciaEncalheCotaSalva() ) {
			return CONF_ENC_COTA_STATUS_INICIADA_NAO_SALVA;
		} else if( statusConferenciaEncalheCota.getNumeroCota() != null && statusConferenciaEncalheCota.isIndConferenciaEncalheCotaSalva() ){
			return CONF_ENC_COTA_STATUS_INICIADA_SALVA;
		} else {
			return CONF_ENC_COTA_STATUS_NAO_INICIADA;
		}
		
	}
	
	public void verificarCotaExigeNFe(final Integer numeroCota) {
				
		final boolean exigeNfe = conferenciaEncalheService.isCotaExigeNfe(numeroCota);
		
		this.result.use(CustomMapJson.class).put(IND_COTA_EXIGE_NFE, exigeNfe).serialize();
	}
	 
	/**
     * Ponto de inicio de uma conferência de encalhe.
     * Realiza validações antes do inicio da operação de encalhe da cota.
     * @param numeroCota
     */
	@Post
	@LogFuncional(value="Conferência de Encalhe [Início da conferência]")
	public void iniciarConferenciaEncalhe(final Integer numeroCota) {

		bloqueioConferenciaEncalheComponent.validarUsuarioConferindoCota(this.session, numeroCota);

		limparDadosSessao();
		
		final Cota cota = this.conferenciaEncalheService.validarCotaParaInicioConferenciaEncalhe(numeroCota);
		
		if (conferenciaEncalheSessionScopeAttr.getIdBoxLogado() == null){
	        throw new ValidacaoException(TipoMensagem.WARNING, "Box de recolhimento não informado.");
	    }
		
		carregarMapaDatasEncalheConferiveis(numeroCota);
		
		if(this.conferenciaEncalheService.verificarCotaComConferenciaEncalheFinalizada(numeroCota)) {

			this.result.use(CustomMapJson.class).put("IND_COTA_RECOLHE_NA_DATA", "S").put("IND_REABERTURA", "S").serialize();

		} else {
			
			Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
			
			List<Date> datas = this.grupoService.obterDatasRecolhimentoOperacaoDiferenciada(numeroCota, dataOperacao);
			
			if(this.conferenciaEncalheService.isCotaComReparteARecolherNaDataOperacao(numeroCota, datas)) {
				
				this.result.use(CustomMapJson.class).put("IND_COTA_RECOLHE_NA_DATA", "S").serialize();	
			
			} else {
				
				this.result.use(CustomMapJson.class)
				.put("IND_COTA_RECOLHE_NA_DATA", "N").put("msg",
                        "Cota não possui recolhimento planejado para a data de operação atual.")
                        .serialize();
				
				bloqueioConferenciaEncalheComponent.removerTravaConferenciaCotaUsuario(this.session);
			
			}
		}
		
		this.session.setAttribute(NUMERO_COTA, numeroCota);
        this.session.setAttribute(COTA, cota);
        this.session.setAttribute(HORA_INICIO_CONFERENCIA, new Date());
		
	}
	
	@SuppressWarnings("unchecked")
	private Map<Long, DataCEConferivelDTO> obterFromSessionMapaDatasEncalheConferiveis() {
		
		return (Map<Long, DataCEConferivelDTO>) session.getAttribute(DATAS_ENCALHE_CONFERIVEIS);
		
	}
	
	private void carregarMapaDatasEncalheConferiveis(final Integer numeroCota) {
		session.setAttribute(DATAS_ENCALHE_CONFERIVEIS, conferenciaEncalheService.obterDatasChamadaEncalheConferiveis(numeroCota));
	}
	
	
	/**
     * Cria em session flag para indicar que os registros de conferencia de
     * encalhe da cota que estão em session ainda não foram alterados pelo
     * usuario.
     */
	private void indicarStatusConferenciaEncalheCotaSalvo() {
		final StatusConferenciaEncalheCota statusConferenciaEncalhe = obterStatusConferenciaEncalheCotaFromSession();
		statusConferenciaEncalhe.setIndConferenciaEncalheCotaSalva(true);
	}
	
	/**
     * Cria em session flag para indicar que os registros de conferencia de
     * encalhe da cota que estão em session já foram alterados pelo usuario.
     */
	private void indicarStatusConferenciaEncalheCotaAlterado() {
		final StatusConferenciaEncalheCota statusConferenciaEncalhe = obterStatusConferenciaEncalheCotaFromSession();
		statusConferenciaEncalhe.setIndConferenciaEncalheCotaSalva(false);
	}
	
	/**
     * Obtém no banco de dados as informações da conferencia de encalhe da cota em questão e setta em session.
	 * Verifica se há encalhe informado em algum item da lista de conferência
	 * 
	 * @param itensConferencia
	 * @return boolean
	 */
	private boolean isEncalheInformado(Set<ConferenciaEncalheDTO> itensConferencia){
		
		for (ConferenciaEncalheDTO item : itensConferencia){
			
			if (item.getQtdInformada().compareTo(BigInteger.ZERO) > 0){
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
     * Obtém no banco de dados as informações da conferencia de encalhe da cota
     * em questão e setta em session.
     * 
     * @param numeroCota
     * @param indConferenciaContingencia
     */
	private void recarregarInfoConferenciaEncalheCotaEmSession(final Integer numeroCota, final boolean indConferenciaContingencia) {
		
		final InfoConferenciaEncalheCota infoConfereciaEncalheCota = conferenciaEncalheService.obterInfoConferenciaEncalheCota(numeroCota, indConferenciaContingencia);
	
		this.session.setAttribute(INFO_CONFERENCIA, infoConfereciaEncalheCota);
		
		indicarStatusConferenciaEncalheCotaSalvo();
		if(this.isEncalheInformado(infoConfereciaEncalheCota.getListaConferenciaEncalhe())){
		
		try {
			
			bloqueioConferenciaEncalheComponent.atribuirTravaConferenciaCotaUsuario(this.getNumeroCotaFromSession(), this.session);
		} catch(Throwable e) {
			
			LOGGER.error("A Cota %s está sendo conferida.", e);
			throw new ValidacaoException(TipoMensagem.WARNING, String.format("A Cota %s está sendo conferida.", numeroCota));
		}
		
		if(this.isEncalheInformado(infoConfereciaEncalheCota.getListaConferenciaEncalhe())) {
	        	
	        bloqueioConferenciaEncalheComponent.atribuirTravaConferenciaCotaUsuario(this.getNumeroCotaFromSession(), this.session);
		}	
		}
	}
	
	@Post
	@LogFuncional(value="Conferência de Encalhe [Carregamento da lista de encalhes]")
	public void carregarListaConferencia(
			Integer numeroCota, 
			final boolean indObtemDadosFromBD,  
			final boolean indConferenciaContingencia){
		
		final Map<String, Object> dados = obterMapaConferenciaEncalhe(numeroCota, indObtemDadosFromBD, indConferenciaContingencia);
		
		result.use(CustomJson.class).from(dados).serialize();
	}

	@Post
    public void excluirNotasFiscaisPorReabertura(Integer numeroCota, final boolean indObtemDadosFromBD, final boolean indConferenciaContingencia) {
	    
	    InfoConferenciaEncalheCota infoConfereciaEncalheCota = this.getInfoConferenciaSession();
        
        if (infoConfereciaEncalheCota == null || indObtemDadosFromBD){
            infoConfereciaEncalheCota = conferenciaEncalheService.obterInfoConferenciaEncalheCota(numeroCota, indConferenciaContingencia);
        }
        
        this.conferenciaEncalheService.excluirNotasFiscaisPorReabertura(infoConfereciaEncalheCota);
	    
        infoConfereciaEncalheCota.setNfeDigitada(false);
        
        if(verificarItensExistenteProcessoUtilizaNFe(infoConfereciaEncalheCota)){            
            this.result.use(CustomMapJson.class).put("RET_NFE_DIGITADA", true).serialize();
        }
        
        this.session.setAttribute(INFO_CONFERENCIA, infoConfereciaEncalheCota);
                
        this.session.setAttribute(NUMERO_COTA, numeroCota);
        
        this.result.use(Results.json()).from("OK.").recursive().serialize();
    }
	
	private boolean verificarItensExistenteProcessoUtilizaNFe(InfoConferenciaEncalheCota infoConfereciaEncalheCota) {
        
        for(ConferenciaEncalheDTO conf : infoConfereciaEncalheCota.getListaConferenciaEncalhe()){
            if(conf.isProcessoUtilizaNfe()){
                return true;
            }
        }
        return false;
    }

	/**
	 * Retorna um mapa com os dados apresentados na 
	 * conferencia de encalhe.
	 * 
	 * @param numeroCota
	 * @param indObtemDadosFromBD
	 * @param indConferenciaContingencia
	 * 
	 * @return Map
	 */
	private Map<String, Object> obterMapaConferenciaEncalhe(Integer numeroCota, final boolean indObtemDadosFromBD, final boolean indConferenciaContingencia) {

		final Date horaInicio = (Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA);
		
	    if (numeroCota == null) {
			numeroCota = this.getNumeroCotaFromSession();
		} else {
			this.session.setAttribute(NUMERO_COTA, numeroCota);
		}
		
		if (horaInicio == null){
			this.session.setAttribute(HORA_INICIO_CONFERENCIA, new Date());
		}
		
		InfoConferenciaEncalheCota infoConfereciaEncalheCota = this.getInfoConferenciaSession();
		
		if (infoConfereciaEncalheCota == null || indObtemDadosFromBD) {
			
			recarregarInfoConferenciaEncalheCotaEmSession(numeroCota, indConferenciaContingencia);
			infoConfereciaEncalheCota = this.getInfoConferenciaSession();
		}
		
		carregarValorInformadoInicial();
		
		final Map<String, Object> dados = new HashMap<String, Object>();
		
		Collection<ConferenciaEncalheDTO> listaOrdenada = ordenarListaConferenciaEncalhe(
				infoConfereciaEncalheCota.getListaConferenciaEncalhe(), 
				indConferenciaContingencia, 
				indObtemDadosFromBD);
		
		dados.put("listaConferenciaEncalhe", listaOrdenada);
		
		dados.put("listaDebitoCredito", this.obterTableModelDebitoCreditoCota(infoConfereciaEncalheCota.getListaDebitoCreditoCota()));
		
		dados.put("reparte", (infoConfereciaEncalheCota.getReparte() == null ? BigDecimal.ZERO : infoConfereciaEncalheCota.getReparte()).setScale(4, RoundingMode.HALF_UP));
		
		dados.put("indDistribuidorAceitaJuramentado", infoConfereciaEncalheCota.isDistribuidorAceitaJuramentado());
		
		dados.put("processoUtilizaNfe", infoConfereciaEncalheCota.isProcessoUtilizaNfe());
		
		dados.put("nfeDigitada", infoConfereciaEncalheCota.isNfeDigitada());
		
		this.calcularValoresMonetarios(dados, false);
		
		final Cota cota = infoConfereciaEncalheCota.getCota();
		if(cota.getPessoa() instanceof PessoaJuridica && 
				cota.getParametrosCotaNotaFiscalEletronica() != null) {
			
			dados.put("isContribuinteICMS", cota.getParametrosCotaNotaFiscalEletronica().isContribuinteICMS());
		} else {
			
			dados.put("isContribuinteICMS", false); 
		}
		
		this.session.setAttribute(COTA, cota);
		
		if (cota != null){
			dados.put("razaoSocial", cota.getPessoa() instanceof PessoaFisica ? ((PessoaFisica)cota.getPessoa()).getNome() : ((PessoaJuridica)cota.getPessoa()).getRazaoSocial());
			dados.put("situacao", cota.getSituacaoCadastro().toString());
			
			dados.put("cotaAVista", TipoCota.A_VISTA.equals(cota.getTipoCota()));
		}
		
		if(infoConfereciaEncalheCota.getNotaFiscalEntradaCota()!=null) {

			final Map<String, Object> dadosNotaFiscal = new HashMap<String, Object>();
			
			if(infoConfereciaEncalheCota.isProcessoUtilizaNfe()){			    
			    dadosNotaFiscal.put("numero", infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getNumero());
			    dadosNotaFiscal.put("serie", 	infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getSerie());
			    dadosNotaFiscal.put("dataEmissao", DateUtil.formatarDataPTBR(infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getDataEmissao()));
			    dadosNotaFiscal.put("chaveAcesso", infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getChaveAcesso());
			    dadosNotaFiscal.put("valorProdutos", infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getValorProdutos());
			}
			
			this.session.setAttribute(NOTA_FISCAL_CONFERENCIA, dadosNotaFiscal);

			dados.put("notaFiscal", dadosNotaFiscal);

			
		} else if(session.getAttribute(NOTA_FISCAL_CONFERENCIA) != null ){
			
			dados.put("notaFiscal", session.getAttribute(NOTA_FISCAL_CONFERENCIA));
			
		} else {
			
			dados.put("notaFiscal", "");
			
		}

		this.calcularTotais(dados);
		
		return dados;
	}
	
	private Collection<ConferenciaEncalheDTO> ordenarConferenciasEncalheContingencia(Collection<ConferenciaEncalheDTO> conferenciasContingencia) {
		
		conferenciasContingencia = PaginacaoUtil.ordenarEmMemoria(new ArrayList<ConferenciaEncalheDTO>(conferenciasContingencia), 
				Ordenacao.ASC, 
				"dataRecolhimento", "codigoSM", "numeroEdicao");
		
		Iterator<ConferenciaEncalheDTO> it = conferenciasContingencia.iterator();
		
		List<ConferenciaEncalheDTO> confsForaDoPrimeiroDia = new ArrayList<>();
		
		Integer primeiroDiaRecolhimento = 1;
		
		while(it.hasNext()) {
			
			ConferenciaEncalheDTO iterado = it.next();
			
			if(!primeiroDiaRecolhimento.equals(iterado.getDia())) {
				confsForaDoPrimeiroDia.add(iterado);
				it.remove();
			}
			
			
		}
		
		conferenciasContingencia.addAll(confsForaDoPrimeiroDia);
		
		return conferenciasContingencia;
		
		
	}
	
	
	private Collection<ConferenciaEncalheDTO> ordenarListaConferenciaEncalhe(
			Set<ConferenciaEncalheDTO> lista,
			boolean indConferenciaContingencia, 
			boolean indFromBD) {
		
		if(indConferenciaContingencia) {
			
			return ordenarConferenciasEncalheContingencia(lista);
			
		} 
		
		if(indFromBD) {
			
			Collection<ConferenciaEncalheDTO> listaConferenciaEncalhe = 
					PaginacaoUtil.ordenarEmMemoria(new ArrayList<ConferenciaEncalheDTO>(lista), 
					Ordenacao.ASC, 
					"dataRecolhimento", "codigoSM", "numeroEdicao");
			
			Integer qtde = listaConferenciaEncalhe.size();
			
			for(ConferenciaEncalheDTO conferencia : listaConferenciaEncalhe) {
				conferencia.setInstanteConferido(--qtde);
			}
			
			return listaConferenciaEncalhe;
			
		} else {
			
			return PaginacaoUtil.ordenarEmMemoria(new ArrayList<ConferenciaEncalheDTO>(lista), 
					Ordenacao.DESC, 
					"instanteConferido");
			
		}
		
		
		
		
	}
	
	/**
	 * Carrega os valores de qtdInformada e precoCapaInformada 
	 * (referentes ao itens de nota) com os mesmos valores de 
	 * qtdExemplar e precoCapaInformado. 
	 */
	private void carregarValorInformadoInicial() {
		
		Set<ConferenciaEncalheDTO> listaConferenciaEncalhe = getListaConferenciaEncalheFromSession();
		
		if(listaConferenciaEncalhe == null || listaConferenciaEncalhe.isEmpty()) {
			return;
		}
		
		for(final ConferenciaEncalheDTO conferencia : listaConferenciaEncalhe) {
	        conferencia.setQtdInformada(conferencia.getQtdExemplar());
	        conferencia.setPrecoCapaInformado(conferencia.getPrecoCapa());
		}
		
	}
	
	private void calcularTotais(final Map<String, Object> dados) {
		
		BigInteger qtdInformada = BigInteger.ZERO;
		BigInteger qtdRecebida = BigInteger.ZERO;
		
		final InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info != null){
			
			if (info.getListaConferenciaEncalhe() != null && !info.getListaConferenciaEncalhe().isEmpty()){
			
				for (final ConferenciaEncalheDTO conferenciaEncalheDTO : info.getListaConferenciaEncalhe()){
					if(conferenciaEncalheDTO.isProcessoUtilizaNfe()){
					    if (conferenciaEncalheDTO.getQtdInformada() != null){
					        qtdInformada = qtdInformada.add(conferenciaEncalheDTO.getQtdInformada());
					    }
					    
					    if (conferenciaEncalheDTO.getQtdExemplar() != null){
					        qtdRecebida = qtdRecebida.add(conferenciaEncalheDTO.getQtdExemplar());
					    }
					}
				}
			}
		}
		
		dados.put("qtdInformada", qtdInformada);
		dados.put("qtdRecebida", qtdRecebida);
	}
	
	@Post
	public void autoCompleteProdutoEdicaoCodigoDeBarras(final Integer numeroCota, final String codigoBarra) {
		
		if (codigoBarra == null || codigoBarra.trim().isEmpty()) {

            throw new ValidacaoException(TipoMensagem.WARNING, "Código de barras inválido.");
		}

		final List<ItemAutoComplete> listaProdutos = 
				this.conferenciaEncalheService.obterListaProdutoEdicaoParaRecolhimentoPorCodigoBarras(numeroCota, codigoBarra); 

		if (listaProdutos == null || listaProdutos.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nehum produto Encontrado.");
		}

		this.result.use(Results.json()).from(listaProdutos, "result").recursive().serialize();
	}
	
	@Post
	public void autoCompleteProdutoEdicaoCodigoSM(final Integer numeroCota, final Integer sm) {
		
		if (sm == null) {

            throw new ValidacaoException(TipoMensagem.WARNING, "Código SM inválido.");
		}
		
		final List<ItemAutoComplete> listaProdutos = 
				this.conferenciaEncalheService.obterListaProdutoEdicaoParaRecolhimentoPorCodigoSM(
						numeroCota, 
						sm, 
						QUANTIDADE_MAX_REGISTROS, 
						obterFromSessionMapaDatasEncalheConferiveis(), getInfoConferenciaSession().isIndCotaOperacaoDiferenciada()); 

		if (listaProdutos == null || listaProdutos.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nehum produto Encontrado.");
		}

		this.result.use(Results.json()).from(listaProdutos, "result").recursive().serialize();
	}

	/**
     * Obtém o objeto do tipo ConferenciaEncalheDTO que esta na lista de
     * conferencia em session com idProdutoEdicao igual ao passado
     * por parâmetro.
     * 
     * @param idProdutoEdicao
     * 
     * @return ConferenciaEncalheDTO
     */
	private ConferenciaEncalheDTO getConferenciaEncalheDTOFromSession(final Long idProdutoEdicao) {
		
		final Set<ConferenciaEncalheDTO> listaConfSessao = this.getListaConferenciaEncalheFromSession();
		
		for (final ConferenciaEncalheDTO dto : listaConfSessao){
			
			if (idProdutoEdicao.equals(dto.getIdProdutoEdicao())){
				return  dto;
			}
			
		}
		
		return null;
	}

	/**
	 * Obtém a quantidade de encalhe a partir do que foi informado na grid de encalhe.
	 * Esta informação pode conter o sufixo "*" indicando que o produto é CROMO e que 
	 * a quantidade informada equivale a de envelopes. 
	 * 
	 * @param qtdeEncalhe
	 * 
	 * @return BigInteger
	 */
	private BigInteger obterQuantidadeEncalheDaString(String qtdeEncalhe) {
		
		if(qtdeEncalhe == null || qtdeEncalhe.trim().isEmpty()) {
			return null;
		}
		
		if(qtdeEncalhe.contains(Constants.ENVELOPE_DE_CROMO)) {
			qtdeEncalhe = qtdeEncalhe.replace(Constants.ENVELOPE_DE_CROMO, "");
		}
		
		try {
			return BigInteger.valueOf(Long.parseLong(qtdeEncalhe));
		} catch(NumberFormatException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade informa de encalhe inválida.");
		}
		
	}
	
	@Post
	public void pesquisarProdutoEdicao(final Long idProdutoEdicaoAnterior, final String quantidade){
		
		this.verificarInicioConferencia();
		
		ProdutoEdicaoDTO produtoEdicao = null;
		
		ConferenciaEncalheDTO conferenciaEncalheDTO = null;
		
		final Integer numeroCota = this.getNumeroCotaFromSession();
		
		
		if(idProdutoEdicaoAnterior == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Informe o Código ou Nome do Produto.");
		}
		
		try {

			if (idProdutoEdicaoAnterior != null){
				
				conferenciaEncalheDTO = getConferenciaEncalheDTOFromSession(idProdutoEdicaoAnterior);
				
				produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(numeroCota, idProdutoEdicaoAnterior);
			} 
			
		} catch(final EncalheRecolhimentoParcialException e) {
            LOGGER.error("Não existe chamada de encalhe deste produto para essa cota: " + e.getMessage(), e);
            throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe para produto parcial na data operação.");
		}
		
		if (produtoEdicao == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Produto Edição não encontrado.");
		}
		
		BigInteger qtdeEncalhe = processarQtdeExemplar(produtoEdicao.isContagemPacote(), produtoEdicao.getId(), produtoEdicao.getPacotePadrao(), quantidade);
		
		if (conferenciaEncalheDTO == null) {
			conferenciaEncalheDTO = this.criarConferenciaEncalhe(produtoEdicao, qtdeEncalhe, false);
		} else {
			conferenciaEncalheDTO.setQtdExemplar(qtdeEncalhe);
		}

		indicarStatusConferenciaEncalheCotaAlterado();

		this.result.use(Results.json()).from(conferenciaEncalheDTO, "result").serialize();
	}
	
	/**
	 * Verifica item repetido na lista de conferencia, e altera quantidade quando encontrado
	 * 
	 * @param idProdutoEdicao
	 * @param qtd
	 */
	private void atualizarProdutoRepetido(final long idProdutoEdicao, final BigInteger qtd, final boolean indConferenciaContingencia){
		
		final Set<ConferenciaEncalheDTO> listaConferencia = this.getListaConferenciaEncalheFromSession();
		
		for (final ConferenciaEncalheDTO ceDTO : listaConferencia){
			
			if (ceDTO.getIdProdutoEdicao().equals(idProdutoEdicao)){
				
				ceDTO.setQtdExemplar(ceDTO.getQtdInformada().add(qtd));
				
				final BigDecimal preco = (ceDTO.getPrecoComDesconto() != null) ? ceDTO.getPrecoComDesconto() : 
					(ceDTO.getPrecoCapa() != null) ? ceDTO.getPrecoCapa() : BigDecimal.ZERO;  

					ceDTO.setValorTotal(preco.multiply(new BigDecimal(ceDTO.getQtdExemplar().intValue())));
			}
		}
		
	}
	
	/**
	 * Obtem ProdutoEdicaoDTO validando existencia de chamada de encalhe
	 * 
	 * @param idProdutoEdicao
	 * @return ProdutoEdicaoDTO
	 */
	private ProdutoEdicaoDTO getProdutoEdicaoDTO(Long idProdutoEdicao){
		
		ProdutoEdicaoDTO produtoEdicao = null;
		
		try {
			
			produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(this.getNumeroCotaFromSession(), idProdutoEdicao);
		} catch (final EncalheRecolhimentoParcialException e) {
			
            LOGGER.error("Não existe chamada de encalhe para produto parcial na data operação: " + e.getMessage(), e);
            
            throw new ValidacaoException(TipoMensagem.WARNING,"Não existe chamada de encalhe para produto parcial na data operação.");
		} 
		
		return produtoEdicao;
	}
	
	/**
	 * Obtem mensagem de venda negativa na digitação da quantidade informada
	 * 
	 * @param idProdutoEdicao
	 * @param quantidade
	 * @param juramentada
	 * @param indConferenciaContingencia
	 */
	@Post
	public void informaVendaNegativa(final Long idProdutoEdicao, final String quantidade, final Boolean juramentada, final boolean indConferenciaContingencia){
		
        //final boolean supervisor = usuarioService.isSupervisor();
        final boolean supervisor = (boolean) (this.session.getAttribute("usuarioSupervisor") != null ? this.session.getAttribute("usuarioSupervisor") : false);

        ConferenciaEncalheDTO conferenciaEncalheDTOSessao = getConferenciaEncalheDTOFromSession(idProdutoEdicao);

		if (conferenciaEncalheDTOSessao == null){
			
			ProdutoEdicaoDTO produtoEdicao = this.getProdutoEdicaoDTO(idProdutoEdicao);
			
			final BigInteger qtdeEncalhe = obterQuantidadeEncalheDaString(quantidade);
			
			conferenciaEncalheDTOSessao = this.criarConferenciaEncalhe(produtoEdicao, qtdeEncalhe, true);
			
		}

		if (juramentada != null) {
			
			conferenciaEncalheDTOSessao.setJuramentada(juramentada);
		}

		boolean isVendaNegativa = this.validarVendaNegativaProduto(quantidade,indConferenciaContingencia, conferenciaEncalheDTOSessao, supervisor);
		
		if (!isVendaNegativa){
			
			result.nothing();
		}
	}

	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	@LogFuncional(value="Conferência de Encalhe [Adicionar produto]")
	public void adicionarProdutoConferido(final Long produtoEdicaoId, final String qtdExemplares, final Boolean juramentada, final boolean indConferenciaContingencia) {
		
		if (produtoEdicaoId == null){
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Produto é obrigatório.");
		}
		
		if (qtdExemplares == null){
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade é obrigatório.");
		}

		ConferenciaEncalheDTO conferenciaEncalheDTOSessao = getConferenciaEncalheDTOFromSession(produtoEdicaoId);

		if (conferenciaEncalheDTOSessao != null){
			
			final BigInteger qtdeEncalhe = this.obterQuantidadeEncalheDaString(qtdExemplares);
			
			conferenciaEncalheDTOSessao.setQtdExemplar(qtdeEncalhe);
			conferenciaEncalheDTOSessao.setOcultarItem(false);

			this.atualizarProdutoRepetido(produtoEdicaoId, qtdeEncalhe, indConferenciaContingencia);
			
		} else {
			
			ProdutoEdicaoDTO produtoEdicao = this.getProdutoEdicaoDTO(produtoEdicaoId);
			
			final BigInteger qtdeEncalhe = this.obterQuantidadeEncalheDaString(qtdExemplares);
			
			conferenciaEncalheDTOSessao = this.criarConferenciaEncalhe(produtoEdicao, qtdeEncalhe, true);
			
		}

		if (juramentada != null) {
			
			conferenciaEncalheDTOSessao.setJuramentada(juramentada);
		}
		
		indicarStatusConferenciaEncalheCotaAlterado();
		
		this.carregarListaConferencia(null, false, indConferenciaContingencia);		
	}
	
	private void desautorizarVendaNegativa() {
		session.setAttribute(VENDA_NEGATIVA_AUTORIZADA_NA_CONFERENCIA, null);
	}
	
	private boolean isUsuarioSupervisor() {
	
		Boolean indUsuarioSupervisor = (Boolean) this.session.getAttribute(IND_USUARIO_SUPERVISOR);
		
		if(indUsuarioSupervisor == null) {
			indUsuarioSupervisor = usuarioService.isSupervisor();
			this.session.setAttribute(IND_USUARIO_SUPERVISOR, indUsuarioSupervisor);
		}
		
		return indUsuarioSupervisor;
	
	}
	
	private boolean isVendaNegativaAutorizada(Long idProdutoEdicaoVerificar) {
		
		if(isUsuarioSupervisor()) {
			return true;
		}
		
		Long idProdutoEdicao = (Long) session.getAttribute(VENDA_NEGATIVA_AUTORIZADA_NA_CONFERENCIA);
		
		if(idProdutoEdicao == null) {
			return false;
		}
		
		return idProdutoEdicao.equals(idProdutoEdicaoVerificar); 
		
	}
	
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	@LogFuncional(value="Conferência de Encalhe [Adicionar produto]")
	public void adicionarProdutoEdicaoConferidoDiretamente(final Long produtoEdicaoId, final String qtdExemplares) {
		
		if (produtoEdicaoId == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Produto é obrigatório.");
		}
		
		if (qtdExemplares == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade é obrigatório.");
		}
		
		ConferenciaEncalheDTO conferenciaEncalheDTOSessao = getConferenciaEncalheDTOFromSession(produtoEdicaoId);
		
		String msgInformativa = null;
		
		if (conferenciaEncalheDTOSessao != null){
			
			final BigInteger novaQtdeEncalhe = processarQtdeExemplar(
					conferenciaEncalheDTOSessao.getIsContagemPacote(), 
					produtoEdicaoId, conferenciaEncalheDTOSessao.getPacotePadrao(), qtdExemplares);
			
			BigInteger qtdeEncalheTotal = novaQtdeEncalhe.add(conferenciaEncalheDTOSessao.getQtdExemplar());
			
			if(isEncalheExcedendoReparte(qtdeEncalheTotal, conferenciaEncalheDTOSessao, false)) {
				
				if(isVendaNegativaAutorizada(produtoEdicaoId)) {
					msgInformativa = "Realizando venda negativa do produto edicão.";
				} else {
					
					Map<String, Object> erroSupervisor = new HashMap<String, Object>();
					erroSupervisor.put("msgErroSupervisor", "Venda negativa! É necessario autenticar o supervisor");
					erroSupervisor.put("idProdutoEdicao", produtoEdicaoId);
					erroSupervisor.put("qtdExemplares", qtdExemplares);
					result.use(CustomJson.class).from(erroSupervisor).serialize();
					return;
					
				}
				
			}

			conferenciaEncalheDTOSessao.setQtdExemplar(qtdeEncalheTotal);
			conferenciaEncalheDTOSessao.setQtdInformada(qtdeEncalheTotal);
			 
			final BigDecimal precoCapa = conferenciaEncalheDTOSessao.getPrecoCapa() == null ? BigDecimal.ZERO : conferenciaEncalheDTOSessao.getPrecoCapa();
			final BigDecimal desconto = conferenciaEncalheDTOSessao.getDesconto() == null ? BigDecimal.ZERO : conferenciaEncalheDTOSessao.getDesconto();
			final BigDecimal qtdExemplar = conferenciaEncalheDTOSessao.getQtdExemplar() == null ? BigDecimal.ZERO : new BigDecimal(conferenciaEncalheDTOSessao.getQtdExemplar()); 
			
			conferenciaEncalheDTOSessao.setValorTotal(precoCapa.subtract(desconto).multiply( qtdExemplar ));
			
		} else {
			
			ProdutoEdicaoDTO produtoEdicao = this.getProdutoEdicaoDTO(produtoEdicaoId);
			
			final BigInteger qtdeEncalhe = processarQtdeExemplar(
					produtoEdicao.isContagemPacote(), 
					produtoEdicaoId, produtoEdicao.getPacotePadrao(), qtdExemplares);
			
			ConferenciaEncalheDTO conf = new ConferenciaEncalheDTO();
			
			conf.setQtdExemplar(qtdeEncalhe); 
			conf.setIdConferenciaEncalhe(null);
			conf.setIdProdutoEdicao(produtoEdicaoId);
			
			if(isEncalheExcedendoReparte(qtdeEncalhe, conf, false)) {
				
				if(isVendaNegativaAutorizada(produtoEdicaoId)) {
				
					msgInformativa = "Realizando venda negativa do produto edicão.";
				
				} else {

					Map<String, Object> erroSupervisor = new HashMap<String, Object>();
					erroSupervisor.put("msgErroSupervisor", "Venda negativa! É necessario autenticar o supervisor");
					erroSupervisor.put("idProdutoEdicao", produtoEdicaoId);
					erroSupervisor.put("qtdExemplares", qtdExemplares);
					result.use(CustomJson.class).from(erroSupervisor).serialize();
					return;

				}

			}
			
			this.criarConferenciaEncalhe(produtoEdicao, qtdeEncalhe, true);
			
		}
		
		desautorizarVendaNegativa();
		
		indicarStatusConferenciaEncalheCotaAlterado();
		
		Map<String, Object> dadosConferenciaEncalhe = obterMapaConferenciaEncalhe(null, false, false);
		
		if(msgInformativa!=null) {
			dadosConferenciaEncalhe.put("msgInformativa", msgInformativa);
		}
		
		result.use(CustomJson.class).from(dadosConferenciaEncalhe).serialize();
		
	}
	
	/**
	 * Método que busca uma lista de produto edicao
	 * cujo código de barras seja semelhante ao informado.
	 * 
	 * Caso for encontrado apenas um produto edicao com código de
	 * barras semelhante ao informado o produto edição em questão
	 * sera adicionado a lista de conferencia diretamente.
	 * 
	 * @param codigoBarra
	 * @param qtdExemplares
	 */
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void encalharProdutoEdicaoPorCodigoDeBarras(
			final String codigoBarra,
			final String qtdExemplares) {
		
		if (codigoBarra == null || codigoBarra.trim().isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Código de barras inválido.");
		}

		final List<ItemAutoComplete> listaProdutos = 
				this.conferenciaEncalheService.obterListaProdutoEdicaoParaRecolhimentoPorCodigoBarras(getNumeroCotaFromSession(), codigoBarra); 

		if (listaProdutos == null || listaProdutos.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nehum produto Encontrado.");
		}
		
		if(listaProdutos.size()==1) {
			
			Long idProdutoEdicao = (Long) listaProdutos.get(0).getChave();
			
			adicionarProdutoEdicaoConferidoDiretamente(idProdutoEdicao, qtdExemplares);
			
			return;
		}

		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("produtosEdicao", listaProdutos);
		data.put("qtdExemplares", qtdExemplares);
		
		this.result.use(CustomJson.class).from(data).serialize();
		
	}
	
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	@LogFuncional(value="Conferência de Encalhe [Autorizar venda negativa]")
	public void autorizarVendaNegativa(
			final Long produtoEdicaoId,
			final String qtdExemplares,
			final String usuario, 
			final String senha) {

		final  boolean permitir = this.usuarioService.verificarUsuarioSupervisor(usuario, senha);
		
		if (!permitir) {
			
			String msgErroSupervisor = "Usuário/senha inválido(s) ou usuário não é supervisor";
			result.use(Results.json()).from(msgErroSupervisor, "msgErroSupervisor").serialize();
			return;
					
		}
		
		this.session.setAttribute(VENDA_NEGATIVA_AUTORIZADA_NA_CONFERENCIA, produtoEdicaoId);
		
		adicionarProdutoEdicaoConferidoDiretamente(produtoEdicaoId, qtdExemplares);
		
	}	
	
	
	
	/**
	 * Valida se a quantidade informada excede a quantidade especificada no reparte
	 * @param qtdExemplares
	 * @param dto
	 */
	private boolean isEncalheExcedendoReparte(final BigInteger qtdExemplares, final ConferenciaEncalheDTO dto, final boolean indConferenciaContingencia){
		
		ConferenciaEncalheDTO conferenciaEncalheDTONaoValidado = null;
		
		try {
			
			conferenciaEncalheDTONaoValidado = (ConferenciaEncalheDTO)BeanUtils.cloneBean(dto);
			conferenciaEncalheDTONaoValidado.setQtdExemplar(qtdExemplares);
			
		} catch (final Exception e) {
			LOGGER.error("Falha ao validar quantidade de itens de encalhe: " + e.getMessage(), e);
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao validar quantidade de itens de encalhe.");
		} 
		
		return conferenciaEncalheService.validarQtdeEncalheExcedeQtdeReparte(
				conferenciaEncalheDTONaoValidado, getCotaFromSession(), null, indConferenciaContingencia, true);
	}
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	@LogFuncional(value="Conferência de Encalhe [Atualizar valores grid]")
	public void atualizarValoresGridInteira(final List<ConferenciaEncalheDTO> listaConferenciaEncalhe, final boolean indConferenciaContingencia) {
		
		if(listaConferenciaEncalhe!=null && !listaConferenciaEncalhe.isEmpty()) {

			for(final ConferenciaEncalheDTO conf : listaConferenciaEncalhe) {
				
				final Long idConferencia = conf.getIdConferenciaEncalhe();
				
				final String qtdExemplaresDaGrid = conf.getQtdExemplarDaGrid();
				
				final Boolean juramentada = conf.getJuramentada();
				
				atualizarItemConferenciaEncalhe(idConferencia, qtdExemplaresDaGrid, juramentada, null, indConferenciaContingencia);
				
			}
			
		}
		
		this.result.use(Results.json()).from("").serialize();
		
	}
	
	/**
	 * Verifica se a quantidade informada de encalhe da grid é diferente da quantidade em session
	 * indicando assim que a quantidade de encalhe foi alterada.
	 * 
	 * @param qtdeEncalheDaGrid
	 * @param qtdeEncalheEmSession
	 * 
	 * @return boolean
	 */
	private boolean isQuantidadeEncalheAlterada(BigInteger qtdeEncalheDaGrid, BigInteger qtdeEncalheEmSession) {
			
		if(qtdeEncalheEmSession == null) {
			return true;
		}
		
		if(qtdeEncalheDaGrid == null) {
			return true;
		}
		
		if(qtdeEncalheEmSession.compareTo(qtdeEncalheDaGrid) != 0) {
			return true;
		}
		
		return false;
		
	}
	
	private ConferenciaEncalheDTO atualizarItemConferenciaEncalhe(
			final Long idConferencia, 
			String qtdExemplaresDaGrid, 
			final Boolean juramentada, 
			final BigDecimal valorCapa, 
			final boolean indConferenciaContingencia) {
		
		final Set<ConferenciaEncalheDTO> listaConferencia = this.getListaConferenciaEncalheFromSession();
		
		if(qtdExemplaresDaGrid == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade de exemplares inválida.");
		}

		boolean utilizaContagemEnvelope = false;
		
		if(qtdExemplaresDaGrid.contains(Constants.ENVELOPE_DE_CROMO)) {
			qtdExemplaresDaGrid = qtdExemplaresDaGrid.replace(Constants.ENVELOPE_DE_CROMO, "");
			utilizaContagemEnvelope = true;
		}
		
		BigInteger qtdExemplares = new BigInteger(qtdExemplaresDaGrid);
		
		ConferenciaEncalheDTO conf = null;
		
		if (idConferencia != null){
				
			for (final ConferenciaEncalheDTO dto : listaConferencia){
				
				if (dto.getIdConferenciaEncalhe().equals(idConferencia)){

					if (dto.getIsContagemPacote() && !utilizaContagemEnvelope && isQuantidadeEncalheAlterada(qtdExemplares, dto.getQtdExemplar())) {
						qtdExemplares = qtdExemplares.multiply(BigInteger.valueOf(dto.getPacotePadrao()));
					}
					
					dto.setQtdExemplar(qtdExemplares);
					dto.setQtdInformada(qtdExemplares);
					
					if (juramentada != null){
					
						dto.setJuramentada(juramentada);
					}
					
					if (valorCapa != null){
						
						dto.setPrecoCapa(valorCapa);
					}
					 
					final BigDecimal precoCapa = dto.getPrecoCapa() == null ? BigDecimal.ZERO : dto.getPrecoCapa();
					final BigDecimal desconto = dto.getDesconto() == null ? BigDecimal.ZERO : dto.getDesconto();
					final BigDecimal qtdExemplar = dto.getQtdExemplar() == null ? BigDecimal.ZERO : new BigDecimal(dto.getQtdExemplar()); 
					
					dto.setValorTotal(precoCapa.subtract(desconto).multiply( qtdExemplar ));
					
					conf = dto;
					
					break;
				}
			}
		}
		
		indicarStatusConferenciaEncalheCotaAlterado();
		
		return conf;
		
	}
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)	
	@LogFuncional(value="Conferência de Encalhe [Atualizar valores grid]")
	public void atualizarValores(final Long idConferencia, String qtdExemplares, final Boolean juramentada, final BigDecimal valorCapa, final boolean indConferenciaContingencia){
		
		final ConferenciaEncalheDTO conf = atualizarItemConferenciaEncalhe(idConferencia, qtdExemplares, juramentada, valorCapa, indConferenciaContingencia);
		
		final Map<String, Object> dados = new HashMap<String, Object>();
		
		dados.put("conf", conf);
		
		dados.put("reparte", (this.getInfoConferenciaSession().getReparte() == null ? BigDecimal.ZERO : this.getInfoConferenciaSession().getReparte()).setScale(4, RoundingMode.HALF_UP));
		
		this.calcularValoresMonetarios(dados, false);
		
		this.calcularTotais(dados);

		this.result.use(CustomJson.class).from(dados == null ? "" : dados).serialize();
	}
	
	@Post
	@LogFuncional(value="Conferência de Encalhe [Verificar permissão supervisor]")
	public boolean verificarPermissaoSupervisorProduto(final String qtdExemplares,
											 final String usuario, 
											 final String senha, 
											 final Long produtoEdicaoId) {
		if (produtoEdicaoId != null) {
			 
			 final ProdutoEdicao produtoEdicao = produtoEdicaoService.buscarPorID(produtoEdicaoId);
			 
			 if(produtoEdicao == null) {
				 
				 throw new ValidacaoException(TipoMensagem.ERROR, "Produto Edição não encontrado.");
			 }
			 
			 final ConferenciaEncalheDTO dto = new ConferenciaEncalheDTO();
			 dto.setIdProdutoEdicao(produtoEdicaoId);
			 dto.setPacotePadrao(produtoEdicao.getPacotePadrao());
			 
		 	 BigInteger qtdeEncalhe =  this.obterQuantidadeEncalhe(qtdExemplares, dto);
		 	
		     if (this.isEncalheExcedendoReparte(qtdeEncalhe, dto, false)) {
		         
		         this.result.use(Results.json()).from("Venda negativa no encalhe, permissão requerida.", "result").serialize();
		         
		         return true;
		     }
		 }
		
		return false;
	}
	
	@Post
	@LogFuncional(value="Conferência de Encalhe [Verificar permissão supervisor]")
	public void verificarPermissaoSupervisor(final Long idConferencia, String qtdExemplares, 
			final String usuario, final String senha, final boolean indConferenciaContingencia,
			final Long produtoEdicaoId, final boolean indPesquisaProduto){
		
		if (qtdExemplares == null) {
		
			qtdExemplares = this.session.getAttribute(QTD).toString();
			this.session.setAttribute(QTD,"");
		}
		
		if (qtdExemplares == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe a quantidade de exemplares.");
		}
		
		boolean isVendaNegativaProduto = false; 
		
        if (usuario != null) {
            
            this.validarAutenticidadeSupervisor(usuario, senha);
            
        } else {
    		
            final Set<ConferenciaEncalheDTO> listaConferencia = this.getListaConferenciaEncalheFromSession();
            
            final boolean supervisor = (boolean) (this.session.getAttribute("usuarioSupervisor") != null ? this.session.getAttribute("usuarioSupervisor") : false);
            //usuarioService.isSupervisor();
            
            if (!this.verificarProdutoJaConferido(listaConferencia, produtoEdicaoId, idConferencia)) {
                
                ProdutoEdicaoDTO pDto = null;
                
                try {
                    
                     pDto = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(
                            this.getNumeroCotaFromSession(), 
                            produtoEdicaoId);
                } catch (final EncalheRecolhimentoParcialException e) {
                    LOGGER.error("Não existe chamada de encalhe para produto parcial na data operação: " + e.getMessage(), e);
                    throw new ValidacaoException(TipoMensagem.WARNING,
                            "Não existe chamada de encalhe para produto parcial na data operação.");
                }
                
                final ConferenciaEncalheDTO dto = 
                        this.criarConferenciaEncalhe(pDto, new BigInteger(qtdExemplares), false);
                
                isVendaNegativaProduto = this.validarVendaNegativaProduto(
                        qtdExemplares,indConferenciaContingencia, dto, supervisor);
                
            } else {
            
                for (final ConferenciaEncalheDTO dto : listaConferencia) {
                    
                    String qtdJaInformada = null;
                    
                    if (indPesquisaProduto) {
                        
                        qtdJaInformada = dto.getQtdInformada() == null ? this.obterQuantidadeEncalheDaString(qtdExemplares).toString() : 
                            dto.getQtdInformada().add(this.obterQuantidadeEncalheDaString(qtdExemplares)).toString();
                    } else {
                        
                        qtdJaInformada = qtdExemplares;
                    }
                    
                    if (produtoEdicaoId != null) {
                        
                        if (produtoEdicaoId.equals(dto.getIdProdutoEdicao())) {
                            
                        	isVendaNegativaProduto = this.validarVendaNegativaProduto(qtdJaInformada,indConferenciaContingencia, dto, supervisor);
                        }
                    } else {
                        
                        if (idConferencia.equals(dto.getIdConferenciaEncalhe())) {
                            
                        	isVendaNegativaProduto = this.validarVendaNegativaProduto(qtdJaInformada, indConferenciaContingencia, dto, supervisor);
                        }
                    }
                    
                    if (isVendaNegativaProduto){
                        break;
                    }
                }
            }
        }
    
        if(!isVendaNegativaProduto){
        	
            this.result.use(Results.json()).from("", "result").serialize();
        }
	}

	private boolean verificarProdutoJaConferido(Set<ConferenciaEncalheDTO> listaConferencia, Long produtoEdicaoId,
	        Long idConferencia) {
        
	    if (listaConferencia == null || listaConferencia.isEmpty()){
	        
	        return false;
	    }
	    
	    for (ConferenciaEncalheDTO dto : listaConferencia){
	        
	        if (produtoEdicaoId != null){
	        
    	        if (produtoEdicaoId.equals(dto.getIdProdutoEdicao())){
    	            return true;
    	        }
	        } else if (idConferencia != null){
	            
	            if (idConferencia.equals(dto.getIdConferenciaEncalhe())){
	                
	                return true;
	            }
	        }
	    }
	    
        return false;
    }

    private boolean validarVendaNegativaProduto(final String qtdExemplares,
										        final boolean indConferenciaContingencia,
										        final ConferenciaEncalheDTO dto,
										        boolean supervisor) {
		
    	this.session.setAttribute(QTD, qtdExemplares);
    	
		BigInteger qtdeEncalhe = this.obterQuantidadeEncalhe(qtdExemplares, dto);
		
		if (this.isEncalheExcedendoReparte(qtdeEncalhe, dto, indConferenciaContingencia)) {
		    
		    Object[] ret = new Object[2];
		    
		    ret[0] = supervisor;
		    
		    if (supervisor){
		        
		        ret[1] = "Venda negativa no encalhe.";
		    } else {
		        
		        ret[1] = "Venda negativa no encalhe, permissão requerida.";
		    }
		    
		    this.result.use(Results.json()).from(ret, "result").serialize();
		    
		    return true;
		}
		
		return false;
	}

	private BigInteger obterQuantidadeEncalhe(final String qtdExemplares,final ConferenciaEncalheDTO dto) {
		
		BigInteger qtdeEncalhe;
		
		if(isQuantidadeEncalheAlterada(this.obterQuantidadeEncalheDaString(qtdExemplares), dto.getQtdExemplar())) {
			
			qtdeEncalhe = processarQtdeExemplar(dto.getIsContagemPacote(), dto.getIdProdutoEdicao(), dto.getPacotePadrao(), qtdExemplares);
		} 
		else {
			
			qtdeEncalhe = obterQuantidadeEncalheDaString(qtdExemplares);
		}
		
		return qtdeEncalhe;
	}

	private void validarAutenticidadeSupervisor(final String usuario, final String senha) {
		
		final boolean permitir = this.usuarioService.verificarUsuarioSupervisor(usuario, senha);
		
		if (!permitir) {
		
			throw new ValidacaoException(TipoMensagem.WARNING, "Usuário/senha inválido(s) ou usuário não é supervisor");
		}
	}

	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	@LogFuncional(value="Conferência de Encalhe [Alterar quantidade]")
	public void alterarQtdeValorInformado(final Long idConferencia, final Long qtdInformada, final BigDecimal valorCapaInformado){
		
		final Set<ConferenciaEncalheDTO> listaConferencia = this.getListaConferenciaEncalheFromSession();
		
		ConferenciaEncalheDTO conf = null;
		
		if (idConferencia != null) {
				
			for (final ConferenciaEncalheDTO dto : listaConferencia){
				
			    if(dto.isProcessoUtilizaNfe()){
			        
			        if (dto.getIdConferenciaEncalhe().equals(idConferencia)){
			            
			            dto.setQtdInformada(BigInteger.valueOf(qtdInformada));
			            
			            if (valorCapaInformado != null){
			                
			                dto.setPrecoCapaInformado(valorCapaInformado);
			            }
			            
			            conf = dto;
			            
			            break;
			        }
			    }
			    
			}
		}
		
		final Map<String, Object> dados = new HashMap<String, Object>();
		
		dados.put("conf", conf);
		
		dados.put("reparte", (this.getInfoConferenciaSession().getReparte() == null ? BigDecimal.ZERO : this.getInfoConferenciaSession().getReparte()).setScale(4, RoundingMode.HALF_UP));
		
		this.calcularValoresMonetarios(dados, false);
		
		this.calcularTotais(dados);
		
		this.result.use(CustomJson.class).from(dados == null ? "" : dados).serialize();
		
	}
	
	/**
	 * Salva Conferencia de encalhe da Cota
	 * @param infoConferenciaEncalheCota TODO
	 * @param controleConfEncalheCota
	 * @param listaConferenciaEncalheCotaToSave
	 * @param indConferenciaContingencia
	 */
	private void salvarConferenciaCota(InfoConferenciaEncalheCota infoConferenciaEncalheCota,
			                           final ControleConferenciaEncalheCota controleConfEncalheCota,
			                           final Set<ConferenciaEncalheDTO> listaConferenciaEncalheCotaToSave, final boolean indConferenciaContingencia){
		
		if (controleConfEncalheCota.getDataOperacao() == null) {
		    
			controleConfEncalheCota.setDataOperacao(this.distribuidorService.obterDataOperacaoDistribuidor());
		}
		
        if (controleConfEncalheCota.getUsuario() == null) {
        	
			controleConfEncalheCota.setUsuario(this.usuarioService.getUsuarioLogado());
		}
		
		this.conferenciaEncalheService.criarBackupConferenciaEncalhe(getUsuarioLogado(), infoConferenciaEncalheCota, controleConfEncalheCota);
		
		limparIdsTemporarios(listaConferenciaEncalheCotaToSave);
		
		this.conferenciaEncalheAsyncComponent.salvarConferenciaEncalhe(controleConfEncalheCota, 
		         new ArrayList<ConferenciaEncalheDTO>(listaConferenciaEncalheCotaToSave), 
		         this.getUsuarioLogado(),
		         indConferenciaContingencia);
		
		bloqueioConferenciaEncalheComponent.removerTravaConferenciaCotaUsuario(this.session);
		
		limparDadosSessao();
	}

	            /**
     * Salva os dados da conferência de encalhe.
     */
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	@LogFuncional(value="Conferência de Encalhe [Salvar conferência]")
	public void salvarConferencia(final boolean indConferenciaContingencia){
		
		final Integer numeroCota = (Integer) this.session.getAttribute(NUMERO_COTA);
		bloqueioConferenciaEncalheComponent.validarUsuarioConferindoCota(session, numeroCota);
		
		this.verificarInicioConferencia();
		
		final ControleConferenciaEncalheCota controleConfEncalheCota = new ControleConferenciaEncalheCota();
		controleConfEncalheCota.setDataInicio((Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA));
		
		final InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de encalhe não inicializada.");
		}
		
		controleConfEncalheCota.setCota(info.getCota());
		controleConfEncalheCota.setId(this.getInfoConferenciaSession().getIdControleConferenciaEncalheCota());
		
		this.carregarNotasFiscais(controleConfEncalheCota, info);
		
		final Box boxEncalhe = new Box();
		boxEncalhe.setId(conferenciaEncalheSessionScopeAttr.getIdBoxLogado());
		controleConfEncalheCota.setBox(boxEncalhe);
		
		final Set<ConferenciaEncalheDTO> listaConferenciaEncalheCotaToSave = obterCopiaListaConferenciaEncalheCota(this.getListaConferenciaEncalheFromSession());
		
		this.salvarConferenciaCota(info, controleConfEncalheCota, listaConferenciaEncalheCotaToSave, indConferenciaContingencia);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
                "result").recursive()
                .serialize();
	}
	
	
	private Long obterIdTemporario() {
		
		int id = (int) System.currentTimeMillis();
		
		if (id > 0){
			id *= -1;
		}
		
		return Long.valueOf(id);
		
	}
	
	
	private void verificarInicioConferencia() {
		
		final Date horaInicio = (Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA);
		
		if (horaInicio == null){
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de Encalhe não inicializada.");
		}
		
	}

	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	@LogFuncional(value="Conferência de Encalhe [Geração de documento]")
	public void gerarDocumentoConferenciaEncalhe(final DadosDocumentacaoConfEncalheCotaDTO dtoDoc) throws Exception {
		
		try {
			final ArrayList<String> tiposDocumentoImpressao = new ArrayList<String>();
			final Long idControleConferenciaEncalheCota = dtoDoc.getIdControleConferenciaEncalheCota();
			final List<byte[]> arquivos = new ArrayList<byte[]>();
			final Map<String, byte[]> mapFileNameFile = new HashMap<String, byte[]>();
			
			boolean geraNovoNumeroSlip = true;
			
			if(dtoDoc.isUtilizaBoletoSlip()) {//Slip-PDF+Boleto
				
				arquivos.add(conferenciaEncalheService.gerarDocumentosConferenciaEncalhe(
					idControleConferenciaEncalheCota, 
					null, 
					TipoDocumentoConferenciaEncalhe.SLIP_PDF,
					geraNovoNumeroSlip));
				
				geraNovoNumeroSlip = false;
				
				for(final String nossoNumero : dtoDoc.getListaNossoNumero().keySet()) {

					arquivos.add(conferenciaEncalheService.gerarDocumentosConferenciaEncalhe(
							idControleConferenciaEncalheCota, 
							nossoNumero,
							TipoDocumentoConferenciaEncalhe.BOLETO,
							false));
				}
				
				this.tratarRetornoGeracaoDocumentoPDF(
					tiposDocumentoImpressao, arquivos, mapFileNameFile,
					TipoDocumentoConferenciaEncalhe.BOLETO.name()+"+"+TipoDocumentoConferenciaEncalhe.SLIP_PDF.name());
				
			} else if(dtoDoc.isUtilizaBoleto()) {//Boleto
				
				for(final String nossoNumero : dtoDoc.getListaNossoNumero().keySet()) {

					arquivos.add(conferenciaEncalheService.gerarDocumentosConferenciaEncalhe(
							idControleConferenciaEncalheCota, 
							nossoNumero,
							TipoDocumentoConferenciaEncalhe.BOLETO,
							false));
				}

				this.tratarRetornoGeracaoDocumentoPDF(
					tiposDocumentoImpressao, arquivos, mapFileNameFile,
					TipoDocumentoConferenciaEncalhe.BOLETO.name());
			}
			
			if(dtoDoc.isUtilizaRecibo()) {//Recibo
				
				for(final String nossoNumero : dtoDoc.getListaNossoNumero().keySet()) {
					
					arquivos.add(conferenciaEncalheService.gerarDocumentosConferenciaEncalhe(
							idControleConferenciaEncalheCota, 
							nossoNumero, 
							TipoDocumentoConferenciaEncalhe.RECIBO,
							false));
				}
				
				this.tratarRetornoGeracaoDocumentoPDF(
					tiposDocumentoImpressao, arquivos, mapFileNameFile,
					TipoDocumentoConferenciaEncalhe.RECIBO.name());
			}
			
			if(dtoDoc.isUtilizaSlip()) {//Slip-TXT / Matricial

				arquivos.add(conferenciaEncalheService.gerarDocumentosConferenciaEncalhe(
					idControleConferenciaEncalheCota, 
					null,
					TipoDocumentoConferenciaEncalhe.SLIP_TXT,
					geraNovoNumeroSlip));
				
				this.tratarRetornoGeracaoDocumentoMatricial(
					tiposDocumentoImpressao, arquivos, mapFileNameFile,
					TipoDocumentoConferenciaEncalhe.SLIP_TXT.name());
			}
			
			this.session.setAttribute(TIPOS_DOCUMENTO_IMPRESSAO_ENCALHE, tiposDocumentoImpressao);
			this.session.setAttribute(CONF_IMPRESSAO_ENCALHE_COTA, dtoDoc);
			this.session.setAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA, mapFileNameFile);
			
		} catch (final ValidacaoException e) {
			LOGGER.error("Erro de validacao ao gerar os erros de validacao: " + e.getMessage(), e);
			if(e.getValidacao() != null){
				throw new Exception(e.getValidacao().getListaMensagens().get(0));
			}
			
		}catch (final Exception e) {
            LOGGER.error("Cobrança gerada. Erro ao gerar arquivo(s) de cobrança: " + e.getMessage(), e);
            throw new Exception("Cobrança gerada. Erro ao gerar arquivo(s) de cobrança - " + e.getMessage(), e);
		}
	}

	private void tratarRetornoGeracaoDocumentoPDF(final ArrayList<String> tiposDocumentoImpressao,
												  final List<byte[]> arquivos,
												  final Map<String, byte[]> mapFileNameFile,
												  final String nomeChave) {
		
		final List<byte[]> arquivosImpressao = new ArrayList<>();
		
		for (final byte[] arquivo : arquivos) {
			
			if (arquivo != null) {
				
				arquivosImpressao.add(arquivo);
			}
		}
		
		if (!arquivosImpressao.isEmpty()) {
		
			final byte[] arquivoImpressao = PDFUtil.mergePDFs(arquivosImpressao);
			mapFileNameFile.put(nomeChave, arquivoImpressao);
			tiposDocumentoImpressao.add(nomeChave);
		}
		
		arquivos.clear();
	}
	
	private void tratarRetornoGeracaoDocumentoMatricial(final ArrayList<String> tiposDocumentoImpressao,
		  												final List<byte[]> arquivos,
		  												final Map<String, byte[]> mapFileNameFile,
		  												final String nomeChave) {

		final byte[] arquivoImpressao = arquivos.get(0);
		mapFileNameFile.put(nomeChave, arquivoImpressao);
		tiposDocumentoImpressao.add(nomeChave);
		arquivos.clear();
	}

	@SuppressWarnings("unchecked")
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	@LogFuncional(value="Conferência de Encalhe [Impressão documento cobrança]")
	public void imprimirDocumentosCobranca(final String tipo_documento_impressao_encalhe) throws IOException{
		
		final Map<String, byte[]> arquivos = (Map<String, byte[]>) this.session.getAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA);
		
		if(arquivos != null && !arquivos.isEmpty()) {
			
			final byte[] bs = arquivos.get(tipo_documento_impressao_encalhe);
			
			final Map<String, Object> dados = new HashMap<String, Object>();
			
			if(bs != null && bs.length > 0) {
				
				if(tipo_documento_impressao_encalhe.equals(TipoDocumentoConferenciaEncalhe.SLIP_TXT.name())){
					dados.put("resultado", new String(arquivos.get(tipo_documento_impressao_encalhe)));
				}else{
					dados.put("resultado", Base64.encodeBytes(arquivos.get(tipo_documento_impressao_encalhe)));	
				}
				
			}else{
				dados.put("resultado", "");
			}

			dados.put("tipo_documento_impressao_encalhe", tipo_documento_impressao_encalhe);
			
			this.result.use(CustomJson.class).from(dados).serialize();
			
		} else {
			
			this.result.use(Results.nothing());
		}
	}	
	
	@Post
	public void verificarCobrancaGerada(){
		
		final InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de encalhe não inicializada.");
		}
		
		if (info.getCota() == null){
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de encalhe não inicializada.");
		}
		
		final List<Long> idsCota = new ArrayList<>();
		idsCota.add(info.getCota().getId());
		
		if (this.gerarCobrancaService.verificarCobrancasGeradas(idsCota)){
			
			this.result.use(Results.json()).from(
					new ValidacaoVO(TipoMensagem.WARNING, 
                            "Já existe cobrança gerada para a data de operação atual, continuar irá sobrescreve-la. Deseja continuar?"),
							"result").recursive().serialize();
			return;
		}
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	private void limparIdsTemporarios(final Set<ConferenciaEncalheDTO> listaConferenciaEncalheDTO) {
		
		for (final ConferenciaEncalheDTO dto : listaConferenciaEncalheDTO){
			
			if (dto.getIdConferenciaEncalhe() < 0){
				
				dto.setIdConferenciaEncalhe(null);
			}
		}		
	}
	
	private Set<ConferenciaEncalheDTO> obterCopiaListaConferenciaEncalheCota(final Set<ConferenciaEncalheDTO> oldListaConferenciaEncalheCota) {
		
		final Set<ConferenciaEncalheDTO> newListaConferenciaEncalheCota = new HashSet<ConferenciaEncalheDTO>();
		
		for(final ConferenciaEncalheDTO conf : oldListaConferenciaEncalheCota) {
		
			try {
				
				newListaConferenciaEncalheCota.add((ConferenciaEncalheDTO)BeanUtils.cloneBean(conf));
			
			} catch (final Exception e) {
                LOGGER.error("Falha na execução do sistema: " + e.getMessage(), e);
                throw new ValidacaoException(TipoMensagem.ERROR, "Falha na execução do sistema.");
			}
		}
		
		return newListaConferenciaEncalheCota;
		
	}
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	@LogFuncional(value="Conferência de Encalhe [Finalizar conferência]")
	public void finalizarConferencia(final boolean indConferenciaContingencia) throws Exception {
		
		final Date horaInicio = (Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA);
		
		final Integer numeroCota = (Integer) this.session.getAttribute(NUMERO_COTA);
		bloqueioConferenciaEncalheComponent.validarUsuarioConferindoCota(session, numeroCota);
		
		if (horaInicio != null) {
		
			final ControleConferenciaEncalheCota controleConfEncalheCota = new ControleConferenciaEncalheCota();
			
			controleConfEncalheCota.setDataInicio(horaInicio);
			
			final InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
			
			if (info == null) {

                throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de encalhe não inicializada.");
			}
			
			controleConfEncalheCota.setCota(info.getCota());
			controleConfEncalheCota.setId(info.getIdControleConferenciaEncalheCota());
			
	        this.carregarNotasFiscais(controleConfEncalheCota, info);
			
			if (controleConfEncalheCota.getDataOperacao() == null) {
			    
				controleConfEncalheCota.setDataOperacao(this.distribuidorService.obterDataOperacaoDistribuidor());
			}
			
            if (controleConfEncalheCota.getUsuario() == null) {
            	
				controleConfEncalheCota.setUsuario(this.usuarioService.getUsuarioLogado());
			}
            
			final Long idBox = conferenciaEncalheSessionScopeAttr.getIdBoxLogado();
			
			final Box boxEncalhe = this.boxService.buscarPorId(idBox);
			
			controleConfEncalheCota.setBox(boxEncalhe);
			
			final Set<ConferenciaEncalheDTO> listaConferenciaEncalheCotaToSave = obterCopiaListaConferenciaEncalheCota(this.getListaConferenciaEncalheFromSession());
		
			for (final ConferenciaEncalheDTO dto : listaConferenciaEncalheCotaToSave) {			
				dto.setOcultarItem(false);
			}
			
			this.conferenciaEncalheService.criarBackupConferenciaEncalhe(getUsuarioLogado(), info, controleConfEncalheCota);
			
			limparIdsTemporarios(info.getListaConferenciaEncalhe());
			
			/*
			dadosDocumentacaoConfEncalheCota = this.conferenciaEncalheService.finalizarConferenciaEncalhe(controleConfEncalheCota, 
																										  listaConferenciaEncalheCotaToSave, 
																										  this.getSetConferenciaEncalheExcluirFromSession(), 
																										  this.getUsuarioLogado(),
																										  indConferenciaContingencia,
																										  info.getReparte());
			
			agendarAgoraAtualizacaoEstoqueProdutoConf();
			
			this.session.removeAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
			
			final Long idControleConferenciaEncalheCota = dadosDocumentacaoConfEncalheCota.getIdControleConferenciaEncalheCota();
			
			this.getInfoConferenciaSession().setIdControleConferenciaEncalheCota(idControleConferenciaEncalheCota);
				
			try {
				
				this.gerarDocumentoConferenciaEncalhe(dadosDocumentacaoConfEncalheCota);
			} catch (final Exception e){
                LOGGER.error("Erro ao gerar documentos da conferência de encalhe: " + e.getMessage(), e);
                throw new Exception("Erro ao gerar documentos da conferência de encalhe - " + e.getMessage());
			}
			*/

			this.conferenciaEncalheAsyncComponent.finalizarConferenciaEncalheAsync(
					  controleConfEncalheCota, 
					  new ArrayList<ConferenciaEncalheDTO>(info.getListaConferenciaEncalhe()), 
					  this.getUsuarioLogado(),
					  indConferenciaContingencia,
					  info.getReparte());
			
			final Map<String, Object> dados = new HashMap<String, Object>();
			
			dados.put("tipoMensagem", TipoMensagem.SUCCESS);
			dados.put(TIPOS_DOCUMENTO_IMPRESSAO_ENCALHE, session.getAttribute(TIPOS_DOCUMENTO_IMPRESSAO_ENCALHE));
			
/*
			if(dadosDocumentacaoConfEncalheCota.getMsgsGeracaoCobranca() != null) {
				
				dados.put("listaMensagens", dadosDocumentacaoConfEncalheCota.getMsgsGeracaoCobranca().getListaMensagens());
				
			} else {

				String msgSucess = "";
				
				if (listaConferenciaEncalheCotaToSave == null || listaConferenciaEncalheCotaToSave.isEmpty()) {
                    msgSucess = "Operação efetuada com sucesso. Nenhum ítem encalhado, total cobrado.";
				} else {
                    msgSucess = "Operação efetuada com sucesso.";
				}
				
				dados.put("listaMensagens", 	new String[]{msgSucess});
				
			}

			dados.put("indGeraDocumentoConfEncalheCota", dadosDocumentacaoConfEncalheCota.isIndGeraDocumentacaoConferenciaEncalhe());
			*/

			String msgSucess = "";
			if (listaConferenciaEncalheCotaToSave == null || listaConferenciaEncalheCotaToSave.isEmpty()){
                msgSucess = "Operação efetuada com sucesso. Nenhum ítem encalhado, total cobrado.";
			} else {
                msgSucess = "Operação efetuada com sucesso.";
			}
			
			dados.put("listaMensagens", 	new String[]{msgSucess});
			
			limparDadosSessao();
			limparDadosSessaoConferenciaEncalheCotaFinalizada();
			
			this.result.use(CustomMapJson.class).put("result", dados).serialize();
			
		} else {
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Conferência de Encalhe não inicializada."), "result").recursive().serialize();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    private void carregarNotasFiscais(final ControleConferenciaEncalheCota controleConfEncalheCota, final InfoConferenciaEncalheCota info) {
        
	    final Map<String, Object> dadosNotaFiscal = (Map) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA);
        
        final NaturezaOperacao naturezaOperacao = this.nFeService.regimeEspecialParaCota(info.getCota());
        
        final List<NotaFiscalEntradaCota> notaFiscalEntradaCotas = new ArrayList<NotaFiscalEntradaCota>();
        
        NotaFiscalEntradaCota notaFiscal = null;
        
        final List<ItemNotaFiscalEntrada> itens = new ArrayList<ItemNotaFiscalEntrada>();
        
        if(dadosNotaFiscal!=null) {
            notaFiscal = new NotaFiscalEntradaCota();
            notaFiscal.setNumero((Long) dadosNotaFiscal.get("numero"));
            notaFiscal.setSerie((String) dadosNotaFiscal.get("serie"));
            notaFiscal.setDataEmissao( DateUtil.parseDataPTBR((String) dadosNotaFiscal.get("dataEmissao")));
            notaFiscal.setChaveAcesso((String) dadosNotaFiscal.get("chaveAcesso"));
            notaFiscal.setValorProdutos(CurrencyUtil.arredondarValorParaDuasCasas((BigDecimal) dadosNotaFiscal.get("valorProdutos")));
            notaFiscal.setControleConferenciaEncalheCota(controleConfEncalheCota);
            notaFiscal.setCota(info.getCota());
            notaFiscal.setDataExpedicao(DateUtil.parseDataPTBR((String) dadosNotaFiscal.get("dataEmissao")));
            notaFiscal.setNaturezaOperacao(naturezaOperacao);
            
            notaFiscal.setOrigem(Origem.MANUAL);
            notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.RECEBIDA);
            notaFiscal.setValorInformado(CurrencyUtil.arredondarValorParaDuasCasas((BigDecimal) dadosNotaFiscal.get("valorProdutos")));
            notaFiscal.setValorBruto(CurrencyUtil.arredondarValorParaDuasCasas((BigDecimal) dadosNotaFiscal.get("valorProdutos")));
            notaFiscal.setValorDesconto(CurrencyUtil.arredondarValorParaDuasCasas((BigDecimal) dadosNotaFiscal.get("valorProdutos")));
            
            for(final ConferenciaEncalheDTO conferenciaEncalhe : this.getListaConferenciaEncalheFromSession()) {
                
                if(conferenciaEncalhe.isProcessoUtilizaNfe()){
                    
                    final ProdutoEdicao produtoEdicao = new ProdutoEdicao();
                    produtoEdicao.setId(conferenciaEncalhe.getIdProdutoEdicao());
                    
                    final ItemNotaFiscalEntrada itemNotaFiscalEntrada = new ItemNotaFiscalEntrada();
                    itemNotaFiscalEntrada.setQtde(conferenciaEncalhe.getQtdInformada());
                    itemNotaFiscalEntrada.setProdutoEdicao(produtoEdicao);
                    itemNotaFiscalEntrada.setTipoLancamento(TipoLancamento.LANCAMENTO);
                    itemNotaFiscalEntrada.setDataRecolhimento(conferenciaEncalhe.getDataRecolhimento());
                    
                    if(conferenciaEncalhe.getDataLancamento() == null) {
                    	
                    	Lancamento	l =	lancamentoService.obterUltimoLancamentoDaEdicaoParaCota(produtoEdicao.getId(), info.getCota().getId(), distribuidorService.obterDataOperacaoDistribuidor());
                    	
                    	itemNotaFiscalEntrada.setDataLancamento(l.getDataLancamentoDistribuidor());
                    } else {
                    	
                    	itemNotaFiscalEntrada.setDataLancamento(conferenciaEncalhe.getDataLancamento());
                    	
                    }
                    
                    itemNotaFiscalEntrada.setNotaFiscal(notaFiscal);
                    itemNotaFiscalEntrada.setPreco(conferenciaEncalhe.getPrecoComDesconto());
                    itemNotaFiscalEntrada.setOrigem(Origem.MANUAL);
                    itens.add(itemNotaFiscalEntrada);
                    
                }
            }
            
            notaFiscal.setItens(itens);
            notaFiscalEntradaCotas.add(notaFiscal);
            controleConfEncalheCota.setNotaFiscalEntradaCota(notaFiscalEntradaCotas);
            controleConfEncalheCota.setProcessoUtilizaNfe(true);
            controleConfEncalheCota.setNfeDigitada(true);
        }
    }
	
	@Post
	public void autocompletarProdutoPorCodigoNome(final String codigoNomeProduto){

		final List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
		
		final List<ProdutoEdicao> listaProdutoEdicao = this.obterProduto(codigoNomeProduto);
		
		if (listaProdutoEdicao != null && !listaProdutoEdicao.isEmpty()){
			
			for (final ProdutoEdicao produtoEdicao : listaProdutoEdicao){
				
				listaProdutos.add(
					new ItemAutoComplete(
								produtoEdicao.getProduto().getCodigo() + " - " + produtoEdicao.getProduto().getNome() + " - " + produtoEdicao.getNumeroEdicao(), 
								null,
								new Object[]{produtoEdicao.getProduto().getCodigo(), produtoEdicao.getId()}));
			}
			
			
		}
		
		result.use(Results.json()).from(listaProdutos, "result").recursive().serialize();
	}
	
	@Post
	public void pesquisarProdutoPorCodigoNome(final String codigoNomeProduto){
		
		final Map<Long, DataCEConferivelDTO> mapaDataCEConferivelDTO = obterFromSessionMapaDatasEncalheConferiveis();
		
		final List<ProdutoEdicao> listaProdutoEdicao =
			this.produtoEdicaoService.obterProdutoPorCodigoNomeParaRecolhimento(
				codigoNomeProduto, getNumeroCotaFromSession(), QUANTIDADE_MAX_REGISTROS, mapaDataCEConferivelDTO);
		
		final List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
		
		if (listaProdutoEdicao != null && !listaProdutoEdicao.isEmpty()){
			
			for (final ProdutoEdicao produtoEdicao : listaProdutoEdicao){
				
				listaProdutos.add(
						new ItemAutoComplete(
								produtoEdicao.getProduto().getCodigo() + " - " + produtoEdicao.getProduto().getNome() + " - " + produtoEdicao.getNumeroEdicao(), 
								null,
								new Object[]{produtoEdicao.getProduto().getCodigo(), produtoEdicao.getId()}));
			}
			
			
		}
		
		result.use(Results.json()).from(listaProdutos, "result").recursive().serialize();
	}
	
	/**
	 * Serializa as informações do produto edição 
	 * se tiver sido encontrado apenas um unico 
	 * produto edição de acordo com código e/ou nome
	 * do produto informado.
	 * 
	 * 
	 * @param codigoNomeProduto
	 * @throws EncalheRecolhimentoParcialException
	 */
	@Post
	public void autocompletarUnicoProdutoPorCodigoNome(final String codigoNomeProduto) throws EncalheRecolhimentoParcialException {

		final List<ProdutoEdicao> listaProdutoEdicao = this.obterProduto(codigoNomeProduto);
		
		if (listaProdutoEdicao != null && listaProdutoEdicao.size() == 1) {

			final Integer numeroCota = this.getNumeroCotaFromSession();
			
			final ProdutoEdicaoDTO p = 
					this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(numeroCota, listaProdutoEdicao.get(0).getId());
			
			final Map<String, Object> dados = new HashMap<String, Object>();
			
			if (p != null){
				
				dados.put("idProdutoEdicaoNovoEncalhe", p.getId());
				dados.put("descricaoProduto", p.getCodigoProduto() + " - " + p.getNomeProduto() + " - " + p.getNumeroEdicao());
				dados.put("numeroEdicao", p.getNumeroEdicao());
				dados.put("precoVenda", p.getPrecoVenda());
				dados.put("desconto", p.getPrecoComDesconto());
				dados.put("parcial",p.isParcial());
			}
			
			this.result.use(CustomJson.class).from(dados).serialize();
		
		} else {
			
			throw new ValidacaoException(TipoMensagem.NONE, "Há mais de uma edição para o produto pesquisado.");
		}
	}

	private List<ProdutoEdicao> obterProduto(final String codigoNomeProduto) {

		final Map<Long, DataCEConferivelDTO> mapaDataCEConferivelDTO = obterFromSessionMapaDatasEncalheConferiveis();
		
		final List<ProdutoEdicao> listaProdutoEdicao =
			this.produtoEdicaoService.obterProdutoPorCodigoNomeParaRecolhimento(
				codigoNomeProduto, getNumeroCotaFromSession(), QUANTIDADE_MAX_REGISTROS, mapaDataCEConferivelDTO);
		
		return listaProdutoEdicao;
	}
	
	@Post
	public void buscarDetalhesProduto(final Long idConferenciaEncalhe){
		
		final Set<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
		for (final ConferenciaEncalheDTO dto : lista){
			
			if (dto.getIdConferenciaEncalhe().equals(idConferenciaEncalhe)){
				
				result.use(Results.json()).from(dto, "result").serialize();
				return;
			}
		}
		
		result.use(Results.json()).from("", "result").serialize();
	}
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	@LogFuncional(value="Conferência de Encalhe [Excluir conferência]")
	public void excluirConferencia(final Long idConferenciaEncalhe) {
		
		Set<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
		for (final ConferenciaEncalheDTO dto : lista) {
			
			if (dto.getIdConferenciaEncalhe().equals(idConferenciaEncalhe)) {				
				dto.setQtdExemplar(BigInteger.valueOf(0));
				dto.setQtdInformada(BigInteger.valueOf(0));
				dto.setOcultarItem(true);
				break;
			}
		}
		
		indicarStatusConferenciaEncalheCotaAlterado();
		
		this.carregarListaConferencia(null, false, false);
	}
	

	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	@LogFuncional(value="Conferência de Encalhe [Gravar observação]")
	public void gravarObservacaoConferecnia(final Long idConferenciaEncalhe, final String observacao){
		
		final Set<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
		for (final ConferenciaEncalheDTO dto : lista){
			
			if (dto.getIdConferenciaEncalhe().equals(idConferenciaEncalhe)){
				
				dto.setObservacao(observacao);
				break;
			}
		}
		
		indicarStatusConferenciaEncalheCotaAlterado();
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	
	private void validarCamposNotaFiscalEntrada(final NotaFiscalEntradaCota notaFiscalEntradaCota) {
		
		if(notaFiscalEntradaCota == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Dados da nota fiscal inválidos.");
		}
		
		if(Util.validarChaveAcesso(notaFiscalEntradaCota.getChaveAcesso())) {
			throw new ValidacaoException(TipoMensagem.WARNING, "A 'Chave de Acesso' inválida!");
		}
		
		final List<String> mensagens = new ArrayList<String>();
		
		if(notaFiscalEntradaCota.getNumero() == null || notaFiscalEntradaCota.getNumero() == 0 ) {
            mensagens.add("Número da nota fiscal deve ser preenchido.");
		}
		
		if(notaFiscalEntradaCota.getSerie() == null || notaFiscalEntradaCota.getSerie().isEmpty()) {
            mensagens.add("Série da nota fiscal deve ser preenchida.");
		}
		
		if(notaFiscalEntradaCota.getDataEmissao() == null) {
            mensagens.add("Data Emissão deve ser preenchida.");
		}
		
		if(notaFiscalEntradaCota.getValorProdutos() == null) {
			mensagens.add("Valor Total deve ser preenchido.");
		}
		
		if(notaFiscalEntradaCota.getChaveAcesso() == null || notaFiscalEntradaCota.getChaveAcesso().isEmpty()) {
			mensagens.add("A chave de acesso deve ser preenchida.");
		}
		
		if(notaFiscalEntradaCota.getChaveAcesso().length() <= 43) {
			mensagens.add("A quantidade de caracteres digitada para o campo ['Chave de Acesso'] e inválida.");
		}
		
		if(!mensagens.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
		
	}
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	@LogFuncional(value="Conferência de Encalhe [Salvar nota fiscal]")
	public void salvarNotaFiscal(final NotaFiscalEntradaCota notaFiscal){
		
	    Map<String, Object> dadosNotaFiscal = new HashMap<String, Object>();
	    
		validarCamposNotaFiscalEntrada(notaFiscal);
		
		BigDecimal valorProdutos = CurrencyUtil.arredondarValorParaDuasCasas(notaFiscal.getValorProdutos());
		
		dadosNotaFiscal.put("numero", notaFiscal.getNumero());
		dadosNotaFiscal.put("serie", 	notaFiscal.getSerie());
		dadosNotaFiscal.put("dataEmissao", DateUtil.formatarDataPTBR(notaFiscal.getDataEmissao()));
		dadosNotaFiscal.put("chaveAcesso", notaFiscal.getChaveAcesso());
		
		dadosNotaFiscal.put("valorProdutos", valorProdutos.setScale(2, RoundingMode.HALF_UP));
		
		this.session.setAttribute(NOTA_FISCAL_CONFERENCIA, dadosNotaFiscal);
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	@Post
	@SuppressWarnings("unchecked")
	@LogFuncional(value="Conferência de Encalhe [Carregar nota fiscal]")
	public void carregarNotaFiscal(){
		
		final Map<String, Object> dadosNotaFiscal = (Map<String, Object>) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA);
		
		if(dadosNotaFiscal!=null) {

			this.result.use(CustomJson.class).from(dadosNotaFiscal);
			
		} else {

			this.result.use(Results.json()).from("","result").serialize();
			
		}
		
	}
	
    /**
     * Verifica se o valor total da nota fiscal informada é igual ao valor de
     * encalhe conferido na operação.
     * 
     * @throws Exception
     * 
     */
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	@LogFuncional(value="Conferência de Encalhe [Verificar total nota fiscal]")
	public void verificarValorTotalNotaFiscal(final boolean indConferenciaContingencia) throws Exception {
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final Map<String, Object> dadosNotaFiscal = (Map) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA);
		
		final Map<String, Object> dadosMonetarios = new HashMap<String, Object>();
		
		this.calcularValoresMonetarios(dadosMonetarios, true);
		
		final BigDecimal valorEncalhe = CurrencyUtil.arredondarValorParaDuasCasas(((BigDecimal) dadosMonetarios.get("valorEncalhe")));
		
		if (dadosNotaFiscal != null && dadosNotaFiscal.get("valorProdutos") != null && ((BigDecimal) dadosNotaFiscal.get("valorProdutos")).compareTo(valorEncalhe) != 0){
		    
		    
			final Map<String, Object> dadosResposta = new HashMap<String, Object>();
			
			dadosResposta.put("tipoMensagem", TipoMensagem.WARNING);
			dadosResposta.put("listaMensagens", new String[]{"Valor total do encalhe difere do valor da nota informada."});
			
			this.calcularValoresMonetarios(dadosMonetarios, true);
			this.result.use(CustomMapJson.class).put("result", dadosResposta).serialize();
			
		}  else {
			
			this.finalizarConferencia(indConferenciaContingencia);
			
		}
	}
	
	            /**
     * Verifica se o valor total de chamada encalhe informado é igual ao valor
     * de encalhe conferido na operação.
     * 
     * @param valorCEInformado
     * @param qtdCEInformado
     */
	@Post
	@LogFuncional(value="Conferência de Encalhe [Verificar total CE]")
	public void verificarValorTotalCE(final BigDecimal valorCEInformado, final BigInteger qtdCEInformado) {

		final Map<String, Object> resultadoValidacao = new HashMap<String, Object>();
		
		final TipoContabilizacaoCE tipoContabilizacaoCE = conferenciaEncalheService.obterTipoContabilizacaoCE();
		
		if(tipoContabilizacaoCE == null) {
			resultadoValidacao.put("valorCEInformadoValido", true);
			this.result.use(CustomJson.class).from(resultadoValidacao).serialize();
			return;
		}
		
		if(TipoContabilizacaoCE.VALOR.equals(tipoContabilizacaoCE)) {
			
			if (valorCEInformado == null || BigDecimal.ZERO.compareTo(valorCEInformado) >= 0 ){
				resultadoValidacao.put("valorCEInformadoValido", false);
                resultadoValidacao.put("mensagemConfirmacao",
                        "Valor CE jornaleiro informado inválido. Deseja continuar?");
				this.result.use(CustomJson.class).from(resultadoValidacao).serialize();
				return;
			} 
			
		} else {
			
			if (qtdCEInformado == null || BigInteger.ZERO.compareTo(qtdCEInformado) >= 0 ){
				
				resultadoValidacao.put("valorCEInformadoValido", false);
                resultadoValidacao.put("mensagemConfirmacao",
                        "Qtde CE jornaleiro informada inválido. Deseja continuar?");
				this.result.use(CustomJson.class).from(resultadoValidacao).serialize();
				return;

			} 
			
		}

			
		if (TipoContabilizacaoCE.VALOR.equals(tipoContabilizacaoCE)) {
			this.comparValorTotalCEMonetario(valorCEInformado);
		} else {
			this.comparValorTotalCEQuantidade(qtdCEInformado);
		}
		
		
	}
	
	            /**
     * Compara se o valor de qtde de itens de encalhe apontado pelo jornaleiro é
     * igual ao contabilizado na operação de conferência de encalhe.
     * 
     * @param valorTotalCEQuantidade
     */

	private void comparValorTotalCEQuantidade(final BigInteger valorTotalCEQuantidade) {
		
		final Map<String, Object> resultadoValidacao = new HashMap<String, Object>();
		
		final BigInteger qtdeItensConferenciaEncalhe = obterQtdeItensConferenciaEncalhe();

		if (qtdeItensConferenciaEncalhe.compareTo(valorTotalCEQuantidade)!=0){

			resultadoValidacao.put("valorCEInformadoValido", false);
			
			resultadoValidacao.put("mensagemConfirmacao", "Qtde total do encalhe difere da quantidade CE jornaleiro informado, Deseja continuar?");
			
			this.result.use(CustomJson.class).from(resultadoValidacao).serialize();
			
		} else {

			resultadoValidacao.put("valorCEInformadoValido", true);
			
			this.result.use(CustomJson.class).from(resultadoValidacao).serialize();

		}
		
	}

	
	            /**
     * Compara se o valor monetario de encalhe apontado pelo jornaleiro é igual
     * ao contabilizado na operação de conferência de encalhe.
     * 
     * @param valorTotalCEMonetario
     */
	private void comparValorTotalCEMonetario(final BigDecimal valorTotalCEMonetario) {
		
		final Map<String, Object> resultadoValidacao = new HashMap<String, Object>();
		
		final Map<String, Object> valoresMonetarios = new HashMap<String, Object>();
		
		this.calcularValoresMonetarios(valoresMonetarios, false);
		
		final BigDecimal valorEncalhe = ((BigDecimal) valoresMonetarios.get("valorEncalhe"));

		//Comparacao ignora 0,005 (meio) centavo p/ resolver problema de arredondamento de 4 casas decimais p/ 2.
		if (!Util.isDiferencaMenorMeioCentavo(valorEncalhe, valorTotalCEMonetario)){

			resultadoValidacao.put("valorCEInformadoValido", false);
			
			resultadoValidacao.put("mensagemConfirmacao", "Valor total do encalhe difere do valor CE jornaleiro informado, Deseja continuar?");
			
			this.result.use(CustomJson.class).from(resultadoValidacao).serialize();
			
		} else {

			resultadoValidacao.put("valorCEInformadoValido", true);
			
			this.result.use(CustomJson.class).from(resultadoValidacao).serialize();

		}
		
	}

	@Post
	public void pesquisarProdutoEdicaoPorId(final Long idProdutoEdicao){
		
		
		final Integer numeroCota = this.getNumeroCotaFromSession();
		
		try {
			final ProdutoEdicaoDTO p = 
					this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(numeroCota, idProdutoEdicao);
			
			final Map<String, Object> dados = new HashMap<String, Object>();
			
			if (p != null){
				
				dados.put("numeroEdicao", p.getNumeroEdicao());
				dados.put("precoVenda", p.getPrecoVenda());
				dados.put("desconto", p.getPrecoComDesconto());
				dados.put("parcial",p.isParcial());
			}
			
			this.result.use(CustomJson.class).from(dados).serialize();
			
		} catch (final EncalheRecolhimentoParcialException e) {
			
            LOGGER.error(
                    "Erro no ao pesquisar Produto Edicação por Id Encalhe Recolhimento Parcial: " + e.getMessage(), e);
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());

		}
	}
	
	private void limparDadosSessao() {
		
		this.session.removeAttribute(NUMERO_COTA);
		this.session.removeAttribute(INFO_CONFERENCIA);
		this.session.removeAttribute(NOTA_FISCAL_CONFERENCIA);
		this.session.removeAttribute(HORA_INICIO_CONFERENCIA);
		this.session.removeAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA);
		this.session.removeAttribute(CONFERENCIA_ENCALHE_COTA_STATUS);
		
		indicarStatusConferenciaEncalheCotaSalvo();
	}
	
	private void limparDadosSessaoConferenciaEncalheCotaFinalizada() {
		
	    this.session.removeAttribute(NUMERO_COTA);
		this.session.removeAttribute(INFO_CONFERENCIA);
		this.session.removeAttribute(NOTA_FISCAL_CONFERENCIA);
		this.session.removeAttribute(HORA_INICIO_CONFERENCIA);
		this.session.removeAttribute(CONFERENCIA_ENCALHE_COTA_STATUS);

		bloqueioConferenciaEncalheComponent.removerTravaConferenciaCotaUsuario(this.session);
		
		indicarStatusConferenciaEncalheCotaSalvo();
		
	}
	
	private InfoConferenciaEncalheCota getInfoConferenciaSession() {
		
		return (InfoConferenciaEncalheCota) this.session.getAttribute(INFO_CONFERENCIA);
	}

	private BigInteger obterQtdeItensConferenciaEncalhe() {
	
		final InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		BigInteger qtdItens = BigInteger.ZERO;
		
		if (info != null){
			
			if (info.getListaConferenciaEncalhe() != null){
				
				for (final ConferenciaEncalheDTO conferenciaEncalheDTO : info.getListaConferenciaEncalhe()){
					
					final BigInteger qtdExemplar = conferenciaEncalheDTO.getQtdExemplar() ==  null ? BigInteger.ZERO : conferenciaEncalheDTO.getQtdExemplar();
					
					qtdItens = qtdItens.add(qtdExemplar);
					
				}
				
			}
		}
				
		return qtdItens;
		
	}
	
	            /**
     * Carrega o mapa passado como parâmetro com o seguinte valores:
     * 
     * valorEncalhe = total do encalhe conferido até o momento nesta operação.
     * valorVendaDia = valorReparte subtraído valorEncalhe. valorDebitoCredito =
     * Creditos e Debitos da Cota valorPagar =
     * 
     * @param dados
     */
	private void calcularValoresMonetarios(final Map<String, Object> dados, boolean apenasProcessoUtilizaNfe){
		
		BigDecimal valorEncalhe = BigDecimal.ZERO;
		BigDecimal valorVendaDia = BigDecimal.ZERO;
		BigDecimal valorDebitoCredito = BigDecimal.ZERO;
		BigDecimal valorTotal = BigDecimal.ZERO;
		BigDecimal valorEncalheAtualizado = BigDecimal.ZERO;
		BigDecimal valorVendaDiaAtualizado = BigDecimal.ZERO;
		BigDecimal valorTotalNota = BigDecimal.ZERO;
		
		final InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info != null){
			
			if (info.getListaConferenciaEncalhe() != null && !info.getListaConferenciaEncalhe().isEmpty()) {
			
				for (final ConferenciaEncalheDTO conferenciaEncalheDTO : info.getListaConferenciaEncalhe()) {
					
				    if(!apenasProcessoUtilizaNfe || (apenasProcessoUtilizaNfe && conferenciaEncalheDTO.isProcessoUtilizaNfe())) {
				        final BigDecimal precoCapa = CurrencyUtil.arredondarValorParaDuasCasas(conferenciaEncalheDTO.getPrecoCapa() == null ? BigDecimal.ZERO : conferenciaEncalheDTO.getPrecoCapa());
	                    
	                    final BigDecimal desconto =  CurrencyUtil.arredondarValorParaDuasCasas(conferenciaEncalheDTO.getDesconto() == null ? BigDecimal.ZERO : conferenciaEncalheDTO.getDesconto());
	                    
	                    final BigDecimal precoComDesconto =  CurrencyUtil.arredondarValorParaQuatroCasas(conferenciaEncalheDTO.getPrecoComDesconto() == null ? BigDecimal.ZERO : conferenciaEncalheDTO.getPrecoComDesconto());
	                    
	                    final BigDecimal qtdExemplar = conferenciaEncalheDTO.getQtdExemplar() == null ? BigDecimal.ZERO : new BigDecimal(conferenciaEncalheDTO.getQtdExemplar());
	                    
	                    valorTotal = valorTotal.add(precoCapa.subtract(desconto).multiply(new BigDecimal(conferenciaEncalheDTO.getQtdInformada())));
	                    
	                    valorTotalNota = valorTotalNota.add(precoComDesconto.multiply(new BigDecimal(conferenciaEncalheDTO.getQtdInformada())));
	                    
	                    valorEncalhe = valorEncalhe.add(precoComDesconto.multiply(qtdExemplar));
	                    
	                    valorEncalheAtualizado = valorEncalheAtualizado.add(precoCapa.subtract(desconto).multiply(new BigDecimal(conferenciaEncalheDTO.getQtdInformada())));
				        
				    }
				}
			}
			
			valorVendaDia = valorVendaDia.add(info.getReparte().subtract(valorEncalhe));
			valorVendaDiaAtualizado = valorVendaDiaAtualizado.add(info.getReparte().subtract(valorEncalheAtualizado));
			
			if (info.getListaDebitoCreditoCota() != null) {
			
				for (final DebitoCreditoCota debitoCreditoCotaDTO : info.getListaDebitoCreditoCota()){
					
					if(debitoCreditoCotaDTO.getValor() == null) {
						continue;
					}
					
					if(OperacaoFinaceira.DEBITO.equals(debitoCreditoCotaDTO.getTipoLancamento())) {
						valorDebitoCredito = valorDebitoCredito.subtract(debitoCreditoCotaDTO.getValor());
					}
						
					if(OperacaoFinaceira.CREDITO.equals(debitoCreditoCotaDTO.getTipoLancamento())) {
						valorDebitoCredito = valorDebitoCredito.add(debitoCreditoCotaDTO.getValor());
					}
				}
			}
		}
		
		BigDecimal valorPagar = BigDecimal.ZERO;	
		BigDecimal valorPagarAtualizado = BigDecimal.ZERO;
		
		if(BigDecimal.ZERO.compareTo(valorDebitoCredito)>0) {
			valorPagar = valorVendaDia.add(valorDebitoCredito.abs());
			valorPagarAtualizado = valorVendaDiaAtualizado.add(valorDebitoCredito.abs());
		} else {
			valorPagar = valorVendaDia.subtract(valorDebitoCredito.abs());
			valorPagarAtualizado = valorVendaDiaAtualizado.subtract(valorDebitoCredito.abs());
		}
		
		if (dados != null){
			
			dados.put("valorEncalhe", valorEncalhe.setScale(4, RoundingMode.HALF_UP));
			dados.put("valorVendaDia", valorVendaDia.setScale(4, RoundingMode.HALF_UP));
			dados.put("valorDebitoCredito", valorDebitoCredito.abs());
			dados.put("valorPagar",  CurrencyUtil.arredondarValorParaDuasCasas(valorPagar.setScale(2, RoundingMode.HALF_UP)));
			dados.put("valorTotal",  valorTotal.setScale(2, RoundingMode.HALF_UP));
			dados.put("valorTotalNota", valorTotalNota.setScale(2, RoundingMode.HALF_UP));
			dados.put("valorPagarAtualizado",  CurrencyUtil.arredondarValorParaQuatroCasas(valorPagarAtualizado));
			
			dados.put("idconf", info.getIdControleConferenciaEncalheCota());
		}
		
		dados.put("notaFiscal", session.getAttribute(NOTA_FISCAL_CONFERENCIA));
	}
	
	/**
     * Processa a quantidade informada pelo usuario, validando quando um produto
     * CROMO é informado.
     * 
     * @param idProdutoEdicao
     * @param conferenciaEncalheDTO
     * @param quantidade - quantidade informada pelo usuario (EX: 100 ou 100e);
     * 
     * @return quantidade
     */
			
	private BigInteger processarQtdeExemplar(
			boolean isContagemPacote,
			final Long idProdutoEdicao,
			Integer pacotePadrao,
			String quantidade) {

		if(idProdutoEdicao == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto edição de encalhe inválido.");
		}
		
		if(quantidade == null || quantidade.trim().isEmpty()) {
			return BigInteger.ZERO;
		}
		
		if(!isContagemPacote) {
			return obterQuantidadeEncalheDaString(quantidade);
		}
		
		if(quantidade.contains(Constants.ENVELOPE_DE_CROMO)) {
			return obterQuantidadeEncalheDaString(quantidade);
		}
		
		BigInteger qtde = obterQuantidadeEncalheDaString(quantidade);
		
		return qtde.multiply(BigInteger.valueOf(pacotePadrao));
		
	}

	private ConferenciaEncalheDTO criarConferenciaEncalhe(final ProdutoEdicaoDTO produtoEdicao, BigInteger quantidade, 
														  final boolean adicionarGrid) {
		
		final Integer numeroCota = getNumeroCotaFromSession();
		
		final ConferenciaEncalheDTO conferenciaEncalheDTO = new ConferenciaEncalheDTO();
		
		final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		final Long idTemporario = obterIdTemporario();
		
		conferenciaEncalheDTO.setIdConferenciaEncalhe(idTemporario);
		
		conferenciaEncalheDTO.setCodigo(produtoEdicao.getCodigoProduto());
		
		conferenciaEncalheDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
		
		conferenciaEncalheDTO.setCodigoSM(produtoEdicao.getSequenciaMatriz());
		
		conferenciaEncalheDTO.setIdProdutoEdicao(produtoEdicao.getId());
		
		conferenciaEncalheDTO.setNomeProduto(produtoEdicao.getNomeProduto());
		
		conferenciaEncalheDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		
		conferenciaEncalheDTO.setQtdReparte(produtoEdicao.getReparte());
		
		conferenciaEncalheDTO.setDesconto(produtoEdicao.getDesconto());
		
		conferenciaEncalheDTO.setPrecoComDesconto(produtoEdicao.getPrecoComDesconto());
		
		conferenciaEncalheDTO.setPrecoCapa(produtoEdicao.getPrecoVenda());
		
		conferenciaEncalheDTO.setPrecoCapaInformado(produtoEdicao.getPrecoVenda());
		
		conferenciaEncalheDTO.setPacotePadrao(produtoEdicao.getPacotePadrao());

		conferenciaEncalheDTO.setContagemPacote(produtoEdicao.isContagemPacote());
		
		if (produtoEdicao.getTipoChamadaEncalhe() != null) {
			
			conferenciaEncalheDTO.setTipoChamadaEncalhe(produtoEdicao.getTipoChamadaEncalhe().name());
			
			InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
			
			ChamadaEncalheCota chamadaEncalheCota = chamadaEncalheCotaService.obterChamadaEncalheCota(info.getCota().getId(), produtoEdicao.getId(), produtoEdicao.getDataRecolhimentoDistribuidor());
			
			List<Lancamento> lancamentos = null;
			if(chamadaEncalheCota != null && chamadaEncalheCota.getChamadaEncalhe() != null) {
			    conferenciaEncalheDTO.setProcessoUtilizaNfe(chamadaEncalheCota.isProcessoUtilizaNfe());
			    
			    lancamentos = new ArrayList<>(chamadaEncalheCota.getChamadaEncalhe().getLancamentos());
			}
			
			if(lancamentos != null && !lancamentos.isEmpty()) {
				
				Collections.sort(lancamentos, new Comparator<Lancamento>() {
					
					@Override
					public int compare(Lancamento l1, Lancamento l2) {
						if(l1 != null && l2 != null) {
							if(l1.getDataLancamentoDistribuidor().getTime() > l2.getDataLancamentoDistribuidor().getTime()) {
								return 1;
							} else {
								return -1;
							}
						} else if (l1 == null && l2 != null) {
							return 1;
						} else if (l1 != null && l2 == null) {
							return -1;
						}
						
						return 0;
					}
				});
				
				conferenciaEncalheDTO.setDataLancamento(lancamentos.get(0).getDataLancamentoDistribuidor());
				
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR
						, String.format("Impossível obter o lançamento do Produto: %s - %s.", produtoEdicao.getCodigoProduto(), produtoEdicao.getNumeroEdicao()));
			}
			
		}

		
		conferenciaEncalheDTO.setDataRecolhimento(produtoEdicao.getDataRecolhimentoDistribuidor());
		
		conferenciaEncalheDTO.setDataConferencia(dataOperacao);
		
		conferenciaEncalheDTO.setParcialNaoFinal(this.conferenciaEncalheService.isParcialNaoFinal(produtoEdicao.getId()));
		
		if (quantidade != null) {
			conferenciaEncalheDTO.setQtdExemplar(quantidade);
			conferenciaEncalheDTO.setQtdInformada(quantidade);
		} else {
			conferenciaEncalheDTO.setQtdExemplar(BigInteger.ONE);
			conferenciaEncalheDTO.setQtdInformada(BigInteger.ONE);
		}
		
		conferenciaEncalheDTO.setValorTotal(produtoEdicao.getPrecoVenda().subtract(produtoEdicao.getDesconto()).multiply(new BigDecimal( conferenciaEncalheDTO.getQtdExemplar()) ));
		
		conferenciaEncalheDTO.setNomeEditor(produtoEdicao.getEditor());
		
		conferenciaEncalheDTO.setNomeFornecedor(produtoEdicao.getNomeFornecedor());
		
		conferenciaEncalheDTO.setChamadaCapa(produtoEdicao.getChamadaCapa());
		
		if (adicionarGrid) {
			
			this.getListaConferenciaEncalheFromSession().add(conferenciaEncalheDTO);
		
		}

		final Integer diaRecolhimento = this.distribuidorService.obterDiaDeRecolhimentoDaData(dataOperacao, 
				                                                            conferenciaEncalheDTO.getDataRecolhimento(),
				                                                            numeroCota,
				                                                            produtoEdicao.getId(), null, null);
				
		conferenciaEncalheDTO.setDia(diaRecolhimento != null ? diaRecolhimento : null);
		
		return conferenciaEncalheDTO;
	}
	
	            /**
     * Obtém tableModel para grid OutrosValores (Debitos e Creditos da cota).
     * 
     * @param listaDebitoCreditoCota
     * 
     * @return TableModel<CellModelKeyValue<DebitoCreditoCotaVO>>
     */
	private TableModel<CellModelKeyValue<DebitoCreditoCota>> 
		obterTableModelDebitoCreditoCota(final List<DebitoCreditoCota> listaDebitoCreditoCota) {

		final TableModel<CellModelKeyValue<DebitoCreditoCota>> tableModelDebitoCreditoCota = 
				new TableModel<CellModelKeyValue<DebitoCreditoCota>>();
		
		tableModelDebitoCreditoCota.setRows(CellModelKeyValue.toCellModelKeyValue(listaDebitoCreditoCota));
		tableModelDebitoCreditoCota.setTotal((listaDebitoCreditoCota!= null) ? listaDebitoCreditoCota.size() : 0);
		tableModelDebitoCreditoCota.setPage(1);
		
		return tableModelDebitoCreditoCota;
	}
	
	private Set<ConferenciaEncalheDTO> getListaConferenciaEncalheFromSession() {
		
		final InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de encalhe não inicializada.");
		}
		
		Set<ConferenciaEncalheDTO> lista = info.getListaConferenciaEncalhe();
		
		if (lista == null){
			info.setListaConferenciaEncalhe(new HashSet<ConferenciaEncalheDTO>());
		}
		
		return info.getListaConferenciaEncalhe();
	}
	
	private Integer getNumeroCotaFromSession(){
		
		final Integer numeroCota = (Integer) this.session.getAttribute(NUMERO_COTA);
		
		if (numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe uma cota.");
		}
		
		return numeroCota;
	}
	
	private Cota getCotaFromSession(){
		
		final Cota cota = (Cota) this.session.getAttribute(COTA);
		
		if (cota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe uma cota.");
		}
		
		return cota;
	}
	
	private void alterarBoxUsuario(final Long idBox) {
		
		final Box box = this.boxService.buscarPorId(idBox);
		
		final Usuario usuarioLogado = this.getUsuarioLogado();
		
		usuarioLogado.setBox(box);
		
		usuarioService.salvar(usuarioLogado);
	}
	
	@Post
	@LogFuncional(value="Conferência de Encalhe [Ordenação por SM]")
	public void ordenarListaPorSM(){
		
		final InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de encalhe não inicializada.");
		}
		
		List<ConferenciaEncalheDTO> lista = new ArrayList<ConferenciaEncalheDTO>();
		
		lista.addAll(info.getListaConferenciaEncalhe());
		
		lista = (List<ConferenciaEncalheDTO>) PaginacaoUtil.ordenarEmMemoria(lista, Ordenacao.ASC, "dataRecolhimento", "codigoSM");
		
		final Map<String, Object> dados = new HashMap<String, Object>();
		
		dados.put("itensConferencia", lista);
		
		dados.put("isDistribuidorAceitaJuramentado", info.isDistribuidorAceitaJuramentado());
		
		result.use(CustomJson.class).from(dados).serialize();
	}
	
}
