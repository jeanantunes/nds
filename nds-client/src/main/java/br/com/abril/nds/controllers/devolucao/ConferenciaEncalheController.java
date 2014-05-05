package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.Constants;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DadosDocumentacaoConfEncalheCotaDTO;
import br.com.abril.nds.dto.DataCEConferivelDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
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
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.GrupoService;
import br.com.abril.nds.service.MovimentoEstoqueService;
import br.com.abril.nds.service.NaturezaOperacaoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.exception.ConferenciaEncalheFinalizadaException;
import br.com.abril.nds.service.exception.EncalheRecolhimentoParcialException;
import br.com.abril.nds.service.exception.EncalheSemPermissaoSalvarException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.sessionscoped.ConferenciaEncalheSessionScopeAttr;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.PDFUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
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
	
	private final ConferenciaEncalheSessionScopeAttr conferenciaEncalheSessionScopeAttr;
	
	public ConferenciaEncalheController(final ConferenciaEncalheSessionScopeAttr conferenciaEncalheSessionScopeAttr){
		this.conferenciaEncalheSessionScopeAttr = conferenciaEncalheSessionScopeAttr;
	}
	
	private static final String DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA = "dadosDocumentacaoConfEncalheCota";
	
	private static final String CONF_IMPRESSAO_ENCALHE_COTA = "configImpressaoEncalheCota";
	
	private static final String TIPOS_DOCUMENTO_IMPRESSAO_ENCALHE = "tipos_documento_impressao_encalhe";
	
	private static final String INFO_CONFERENCIA = "infoCoferencia";
	
	private static final String SET_CONFERENCIA_ENCALHE_EXCLUIR = "listaConferenciaEncalheExcluir";
	
	private static final String NOTA_FISCAL_CONFERENCIA = "notaFiscalConferencia";
	
	private static final String HORA_INICIO_CONFERENCIA = "horaInicioConferencia";
	
	private static final String NUMERO_COTA = "numeroCotaConferenciaEncalhe";
	
	private static final String COTA = "cotaConferenciaEncalhe";
	
	private static final int QUANTIDADE_MAX_REGISTROS = 15;
	
	private static final String CONFERENCIA_ENCALHE_COTA_STATUS = "CONFERENCIA_ENCALHE_COTA_STATUS";
	
	private static final String DATAS_ENCALHE_CONFERIVEIS = "DATAS_ENCALHE_CONFERIVEIS";
	
	private static final String IND_COTA_EMITE_NFE = "IND_COTA_EMITE_NFE";
	
	/*
     * Conferência de encalhe da cota que foi iniciada porém ainda não foi salva.
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
	private MovimentoEstoqueService movimentoEstoqueService;
	
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
	
	@Path("/")
	@SuppressWarnings("unchecked")
	public void index() {

		final ServletContext context = this.session.getServletContext();
		
		final Map<Integer, String> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, String>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
		
		validarUsuarioConferindoCota(this.getIdentificacaoUnicaUsuarioLogado(), mapaCotaConferidaUsuario);
		
		this.result.include("dataOperacao", DateUtil.formatarDataPTBR(distribuidorService.obterDataOperacaoDistribuidor()));
		
		final TipoContabilizacaoCE tipoContabilizacaoCE = conferenciaEncalheService.obterTipoContabilizacaoCE();
		
		if(tipoContabilizacaoCE!=null) {
			this.result.include("tipoContabilizacaoCE", tipoContabilizacaoCE.name());
		}
		
        // Obter box usuário
		if(this.getUsuarioLogado()!= null && this.getUsuarioLogado().getBox() != null && 
				this.getUsuarioLogado().getBox().getId() != null){
			
			if(conferenciaEncalheSessionScopeAttr != null){
				conferenciaEncalheSessionScopeAttr.setIdBoxLogado(this.getUsuarioLogado().getBox().getId());
			}
		}
		
		limparDadosSessao();
		carregarComboBoxEncalhe();
	}
	
	@Path("/contingencia")
	public void contingencia() {
		
		final Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();

		this.result.include("dataOperacao", DateUtil.formatarDataPTBR(dataOperacao));
		
		final TipoContabilizacaoCE tipoContabilizacaoCE = conferenciaEncalheService.obterTipoContabilizacaoCE();
		
		if(tipoContabilizacaoCE!=null) {
			this.result.include("tipoContabilizacaoCE", tipoContabilizacaoCE.name());
		}
	}
	
	public void carregarComboBoxEncalheContingencia() {
		
		final List<Box> boxes = this.conferenciaEncalheService.obterListaBoxEncalhe(this.getUsuarioLogado().getId());
		
		if( boxes!=null ) {

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
	public void salvarIdBoxSessao(final Long idBox){
		
		if (idBox != null){
		
			conferenciaEncalheSessionScopeAttr.setIdBoxLogado(idBox);
			
			this.result.include("boxes", idBox);
			
			alterarBoxUsuario(idBox);
		} else {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Box de recolhimento é obrigatório.");
		}
		
		this.result.use(Results.json()).from("").serialize();
	}
	

	
	@SuppressWarnings("unchecked")
	private void atribuirTravaConferenciaCotaUsuario(final Integer numeroCota) {
		
		final String loginUsuarioLogado = this.getIdentificacaoUnicaUsuarioLogado();
			
		verificarTravaConferenciaCotaUsuario(numeroCota);
	
		final ServletContext context = this.session.getServletContext();
	
		Map<Integer, String> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, String>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
	
		Map<String, String> mapaLoginNomeUsuario = (LinkedHashMap<String, String>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO);
		
		if(mapaCotaConferidaUsuario == null) {
			mapaCotaConferidaUsuario = new LinkedHashMap<>();
			context.setAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO, mapaCotaConferidaUsuario);
		}

		if(mapaLoginNomeUsuario == null) {
			mapaLoginNomeUsuario = new LinkedHashMap<>();
			context.setAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO, mapaLoginNomeUsuario);
		}

		mapaLoginNomeUsuario.put(loginUsuarioLogado, getIdentificacaoUsuarioLogado());
		mapaCotaConferidaUsuario.put(numeroCota, loginUsuarioLogado);
	}
	
	/**
     * Realiza a remoção da trava da session do usuario com conferencia de encalhe
     */
	@Post
	@SuppressWarnings("unchecked")
	public void removerTravaConferenciaEncalheCotaUsuario() {
		
		final String loginUsuarioLogado = this.getIdentificacaoUnicaUsuarioLogado();
		
		final  Map<Integer, String> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, String>) session.getServletContext().getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
		
		final Map<String, String> mapaLoginNomeUsuario = 
			(LinkedHashMap<String, String>) session.getServletContext().getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO);
		
		removerTravaConferenciaCotaUsuario(session.getServletContext(), loginUsuarioLogado, mapaCotaConferidaUsuario, mapaLoginNomeUsuario);
		
		this.result.nothing();
	}

	public static void removerTravaConferenciaCotaUsuario(final ServletContext context, final String userSessionID, final Map<Integer, String> mapaCotaConferidaUsuario, final Map<String, String> mapaLoginNomeUsuario) {
			
		if(mapaLoginNomeUsuario != null) {
			mapaLoginNomeUsuario.remove(userSessionID);
		}
		
		if(mapaCotaConferidaUsuario == null || mapaCotaConferidaUsuario.isEmpty()) {
			return;
		}
		
		final Set<Integer> cotasEmConferencia = new HashSet<>(mapaCotaConferidaUsuario.keySet()) ;

		for(final Integer numeroCota : cotasEmConferencia) {
			if( mapaCotaConferidaUsuario.get(numeroCota).equals(userSessionID) ) {
				mapaCotaConferidaUsuario.remove(numeroCota);
			}
		}
	}
	
	private void verificarTravaConferenciaCotaUsuario(final Integer numeroCota) {
		
		final String loginUsuarioLogado = this.getIdentificacaoUsuarioLogado();
		
		final ServletContext context = this.session.getServletContext();
		
		@SuppressWarnings("unchecked")
		final Map<Integer, String> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, String>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
		
		@SuppressWarnings("unchecked")
		final Map<String, String> mapaLoginNomeUsuario = (LinkedHashMap<String, String>) context.getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO);
			
		if(mapaCotaConferidaUsuario==null || mapaCotaConferidaUsuario.isEmpty()) {
			return;
		} 
		
		validarUsuarioConferindoCota(loginUsuarioLogado, mapaCotaConferidaUsuario);
		
		if(!mapaCotaConferidaUsuario.containsKey(numeroCota)) {
			return;
		}
		
		final String donoDoLockCotaConferida = mapaCotaConferidaUsuario.get(numeroCota);
		
		if(loginUsuarioLogado.equals(donoDoLockCotaConferida)) {
			return;
		}
		
        String nomeUsuario = "Não identificado";
		
		if(mapaLoginNomeUsuario != null && mapaLoginNomeUsuario.get(donoDoLockCotaConferida) != null) {
			nomeUsuario = mapaLoginNomeUsuario.get(donoDoLockCotaConferida);
		}
		
		throw new ValidacaoException(TipoMensagem.WARNING, 
             " Não é possível iniciar a conferência de encalhe para esta cota, a mesma esta sendo conferida pelo(a) usuário(a) [ " + nomeUsuario + " ] ");
	}
	
	/**
	 * Se o usuário (session id) ja estiver conferindo alguma cota,
	 * não sera permitido que este inicie uma nova conferência.
	 * 
	 * @param login
	 * @param mapaCotaConferidaUsuario
	 */
	private void validarUsuarioConferindoCota(final String login, Map<Integer, String> mapaCotaConferidaUsuario) {
		if (mapaCotaConferidaUsuario != null && mapaCotaConferidaUsuario.containsValue(login)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não possível executar mais de uma conferência de encalhe ao mesmo tempo!");
		}
	}
	
	private String getIdentificacaoUsuarioLogado() {
		
		final Usuario usuario = getUsuarioLogado();
		
		if(usuario == null) {
            return "Não Identificado";
		}
		
		if(usuario.getNome() != null && !usuario.getNome().isEmpty()) {
			return usuario.getNome();
		}
		
		if(usuario.getLogin() != null && !usuario.getLogin().isEmpty()) {
			return usuario.getLogin();
		}
		
        return "Não Identificado";
	}
	
	private String getIdentificacaoUnicaUsuarioLogado() {
		
		final Usuario usuario = getUsuarioLogado();
		
		final String sessionID = this.session.getId();
		
		if (usuario.getLogin() != null && !usuario.getLogin().isEmpty()) {
			
			return usuario.getLogin() + sessionID;
		}
		
        return "Não Identificado";
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
	
	public void verificarCotaEmiteNFe(final Integer numeroCota) {
				
		final boolean emiteNfe = conferenciaEncalheService.isCotaEmiteNfe(numeroCota);
		
		this.result.use(CustomMapJson.class).put(IND_COTA_EMITE_NFE, emiteNfe).serialize();
	}
	 
     /**
     * Ponto de inicio de uma conferência de encalhe.
     * Realiza validações antes do inicio da operação de encalhe da cota.
     * @param numeroCota
     */
	@Post
	public void iniciarConferenciaEncalhe(final Integer numeroCota){
		
		limparDadosSessao();

		final Cota cota = this.conferenciaEncalheService.validarCotaParaInicioConferenciaEncalhe(numeroCota);
		
		if (conferenciaEncalheSessionScopeAttr.getIdBoxLogado() == null){
	        throw new ValidacaoException(TipoMensagem.WARNING, "Box de recolhimento não informado.");
	    }
		
		atribuirTravaConferenciaCotaUsuario(numeroCota);
		
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
				    .put("IND_COTA_RECOLHE_NA_DATA", "N")
				    .put("msg", "Cota não possui recolhimento planejado para a data de operação atual.").serialize();
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
     * @param numeroCota
     * @param indConferenciaContingencia
     */
	private void recarregarInfoConferenciaEncalheCotaEmSession(final Integer numeroCota, final boolean indConferenciaContingencia) {
		
		final InfoConferenciaEncalheCota infoConfereciaEncalheCota = conferenciaEncalheService.obterInfoConferenciaEncalheCota(numeroCota, indConferenciaContingencia);
	
		this.session.setAttribute(INFO_CONFERENCIA, infoConfereciaEncalheCota);
		
		this.setListaConferenciaEncalheToSession(infoConfereciaEncalheCota.getListaConferenciaEncalhe());
		
		indicarStatusConferenciaEncalheCotaSalvo();
	}
	
	@Post
	public void carregarListaConferencia(Integer numeroCota, final boolean indObtemDadosFromBD, final boolean indConferenciaContingencia){
		
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
		
		if (infoConfereciaEncalheCota == null || indObtemDadosFromBD){
			recarregarInfoConferenciaEncalheCotaEmSession(numeroCota, indConferenciaContingencia);
			infoConfereciaEncalheCota = this.getInfoConferenciaSession();
		}
		
		carregarValorInformadoInicial(infoConfereciaEncalheCota.getListaConferenciaEncalhe());
		
		final Map<String, Object> dados = new HashMap<String, Object>();
		
		dados.put("listaConferenciaEncalhe", infoConfereciaEncalheCota.getListaConferenciaEncalhe());
		
		dados.put("listaDebitoCredito", this.obterTableModelDebitoCreditoCota(infoConfereciaEncalheCota.getListaDebitoCreditoCota()));
		
		dados.put("reparte", (infoConfereciaEncalheCota.getReparte() == null ? BigDecimal.ZERO : infoConfereciaEncalheCota.getReparte()).setScale(4, RoundingMode.HALF_UP));
		
		dados.put("indDistribuidorAceitaJuramentado", infoConfereciaEncalheCota.isDistribuidorAceitaJuramentado());
		
		this.calcularValoresMonetarios(dados);
		
		final Cota cota = infoConfereciaEncalheCota.getCota();
		this.session.setAttribute(COTA, cota);
		
		if (cota != null){
			dados.put("razaoSocial", cota.getPessoa() instanceof PessoaFisica ? ((PessoaFisica)cota.getPessoa()).getNome() : ((PessoaJuridica)cota.getPessoa()).getRazaoSocial());
			dados.put("situacao", cota.getSituacaoCadastro().toString());
			
			dados.put("cotaAVista", TipoCota.A_VISTA.equals(cota.getTipoCota()));
		}
		
		if(infoConfereciaEncalheCota.getNotaFiscalEntradaCota()!=null) {

			final Map<String, Object> dadosNotaFiscal = new HashMap<String, Object>();
			
			dadosNotaFiscal.put("numero", infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getNumero());
			dadosNotaFiscal.put("serie", 	infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getSerie());
			dadosNotaFiscal.put("dataEmissao", DateUtil.formatarDataPTBR(infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getDataEmissao()));
			dadosNotaFiscal.put("chaveAcesso", infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getChaveAcesso());
			dadosNotaFiscal.put("valorProdutos", infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getValorProdutos());
			
			this.session.setAttribute(NOTA_FISCAL_CONFERENCIA, dadosNotaFiscal);

			dados.put("notaFiscal", dadosNotaFiscal);

			
		} else if(session.getAttribute(NOTA_FISCAL_CONFERENCIA) != null ){
			
			dados.put("notaFiscal", session.getAttribute(NOTA_FISCAL_CONFERENCIA));
			
		} else {
			
			dados.put("notaFiscal", "");
			
		}

		this.calcularTotais(dados);
		
		result.use(CustomJson.class).from(dados).serialize();
	}
	
	/**
	 * Carrega os valores de qtdInformada e precoCapaInformada 
	 * (referentes ao itens de nota) com os mesmos valores de 
	 * qtdExemplar e precoCapaInformado. 
	 */
	private void carregarValorInformadoInicial(final List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {
		
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
					
					if (conferenciaEncalheDTO.getQtdInformada() != null){
						qtdInformada = qtdInformada.add(conferenciaEncalheDTO.getQtdInformada());
					}
					
					if (conferenciaEncalheDTO.getQtdExemplar() != null){
						qtdRecebida = qtdRecebida.add(conferenciaEncalheDTO.getQtdExemplar());
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

	            /**
     * Obtém o objeto do tipo ConferenciaEncalheDTO que esta na lista de
     * conferencia em session com idProdutoEdicao ou codigoSM igual ao passado
     * por parâmetro.
     * 
     * @param idProdutoEdicao
     * @param sm
     * 
     * @return ConferenciaEncalheDTO
     */
	private ConferenciaEncalheDTO getConferenciaEncalheDTOFromSession(final Long idProdutoEdicao, final Integer sm) {
		
		final List<ConferenciaEncalheDTO> listaConfSessao = this.getListaConferenciaEncalheFromSession();
		
		if(idProdutoEdicao != null) {
			for (final ConferenciaEncalheDTO dto : listaConfSessao){
				if (idProdutoEdicao.equals(dto.getIdProdutoEdicao())){
					return  dto;
				}
			}
		} else if( sm != null) {
			for (final ConferenciaEncalheDTO dto : listaConfSessao){
				if (sm.equals(dto.getCodigoSM())){
					return  dto;
				}
			}
		}
		
		return null;
		
	}

	/**
	 * Obtém a quantidade de encalhe a partir do que foi informado na grid de encalhe.
	 * Esta informação pode conter o sufixo "e" indicando que o produto é CROMO e que 
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
	public void pesquisarProdutoEdicaoCodigoSM(final Integer sm, final Long idProdutoEdicaoAnterior, final String quantidade){
		
		this.verificarInicioConferencia();
		
		ProdutoEdicaoDTO produtoEdicao = null;
		
		ConferenciaEncalheDTO conferenciaEncalheDTO = null;
		
		final Integer numeroCota = this.getNumeroCotaFromSession();
		
		if(sm == null && idProdutoEdicaoAnterior==null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe SM.");
		}
		
		try {
			conferenciaEncalheDTO = getConferenciaEncalheDTOFromSession(null, sm);
			produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorSM(numeroCota, sm);
		}  catch(final EncalheRecolhimentoParcialException e) {
			
            LOGGER.error("Não existe chamada de encalhe para produto parcial na data operação: " + e.getMessage(), e);
			
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Não existe chamada de encalhe para produto parcial na data operação.");
		}
		
		if (produtoEdicao == null){
            LOGGER.error("Produto Edição não encontrado.");
            throw new ValidacaoException(TipoMensagem.WARNING, "Produto Edição não encontrado.");
		} 
		
		BigInteger qtdeEncalhe = processarQtdeExemplar(produtoEdicao.getId(), produtoEdicao.getPacotePadrao(), quantidade);
		
		if (conferenciaEncalheDTO == null){
			conferenciaEncalheDTO = this.criarConferenciaEncalhe(produtoEdicao, qtdeEncalhe, false, false);
		}
		
		if (idProdutoEdicaoAnterior != null){
			conferenciaEncalheDTO = getConferenciaEncalheDTOFromSession(idProdutoEdicaoAnterior, null);
		}
		
		if (conferenciaEncalheDTO != null){
			conferenciaEncalheDTO.setQtdExemplar(qtdeEncalhe);
		} else {
			conferenciaEncalheDTO = this.criarConferenciaEncalhe(produtoEdicao, qtdeEncalhe, false, false);
		}

		indicarStatusConferenciaEncalheCotaAlterado();

		this.result.use(Results.json()).from(conferenciaEncalheDTO, "result").serialize();
	}
	
	@Post
	public void pesquisarProdutoEdicao(final Long idProdutoEdicaoAnterior, final String quantidade){
		
		this.verificarInicioConferencia();
		
		ProdutoEdicaoDTO produtoEdicao = null;
		
		ConferenciaEncalheDTO conferenciaEncalheDTO = null;
		
		final Integer numeroCota = this.getNumeroCotaFromSession();
		
		
		if(idProdutoEdicaoAnterior == null) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Informe código de barras, SM ou código.");
		}
		
		try {

			if (idProdutoEdicaoAnterior != null){
				
				conferenciaEncalheDTO = getConferenciaEncalheDTOFromSession(idProdutoEdicaoAnterior, null);
				
				produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(numeroCota, idProdutoEdicaoAnterior);
			} 
			
		} catch(final EncalheRecolhimentoParcialException e) {
            LOGGER.error("Não existe chamada de encalhe deste produto para essa cota: " + e.getMessage(), e);
            throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe para produto parcial na data operação.");
		}
		
		if (produtoEdicao == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Produto Edição não encontrado.");
		}
		
		BigInteger qtdeEncalhe = processarQtdeExemplar(produtoEdicao.getId(), produtoEdicao.getPacotePadrao(), quantidade);
		
		if (conferenciaEncalheDTO == null) {
			conferenciaEncalheDTO = this.criarConferenciaEncalhe(produtoEdicao, qtdeEncalhe, false, false);
		} else {
			conferenciaEncalheDTO.setQtdExemplar(qtdeEncalhe);
		}

		indicarStatusConferenciaEncalheCotaAlterado();

		this.result.use(Results.json()).from(conferenciaEncalheDTO, "result").serialize();
	}
	
	/**
	 * Verifica item tepetido na lista de conferencia, e altera quantidade quando encontrado
	 * @param idProdutoEdicao
	 * @param qtd
	 * @return List<ConferenciaEncalheDTO>
	 */
	private List<ConferenciaEncalheDTO> atualizarProdutoRepetido(final long idProdutoEdicao, final BigInteger qtd, final boolean indConferenciaContingencia){
		
		final List<ConferenciaEncalheDTO> listaConferencia = this.getListaConferenciaEncalheFromSession();
		
		for (final ConferenciaEncalheDTO ceDTO : listaConferencia){
			
			if (ceDTO.getIdProdutoEdicao().equals(idProdutoEdicao)){
				
				this.validarExcedeReparte(ceDTO.getQtdInformada().add(qtd), ceDTO, indConferenciaContingencia);
				
				ceDTO.setQtdExemplar(ceDTO.getQtdInformada().add(qtd));
				
				final BigDecimal preco = (ceDTO.getPrecoComDesconto() != null) ? ceDTO.getPrecoComDesconto() : 
					(ceDTO.getPrecoCapa() != null) ? ceDTO.getPrecoCapa() : BigDecimal.ZERO;  

					ceDTO.setValorTotal(preco.multiply(new BigDecimal(ceDTO.getQtdExemplar().intValue())));
			}
		}
		
		return listaConferencia;
	}
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void adicionarProdutoConferido(final Long idProdutoEdicao, final String quantidade, final Boolean juramentada, final boolean indConferenciaContingencia) {
		
		if (idProdutoEdicao == null){
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Produto é obrigatório.");
		}
		
		if (quantidade == null){
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade é obrigatório.");
		}
		
		ProdutoEdicaoDTO produtoEdicao = null;
		
		try {
			produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(this.getNumeroCotaFromSession(), idProdutoEdicao);
		} catch (final EncalheRecolhimentoParcialException e) {
            LOGGER.error("Não existe chamada de encalhe para produto parcial na data operação: " + e.getMessage(), e);
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Não existe chamada de encalhe para produto parcial na data operação.");
		} 
		
		ConferenciaEncalheDTO conferenciaEncalheDTOSessao = getConferenciaEncalheDTOFromSession(idProdutoEdicao, null);

		if (conferenciaEncalheDTOSessao != null){
			
			final BigInteger qtdeEncalhe = obterQuantidadeEncalheDaString(quantidade);
			
			conferenciaEncalheDTOSessao.setQtdExemplar(qtdeEncalhe);
			
			this.setListaConferenciaEncalheToSession(this.atualizarProdutoRepetido(idProdutoEdicao, qtdeEncalhe, indConferenciaContingencia));
			
		} else {
			
			final BigInteger qtdeEncalhe = obterQuantidadeEncalheDaString(quantidade);
			
			conferenciaEncalheDTOSessao = this.criarConferenciaEncalhe(produtoEdicao, qtdeEncalhe, true, indConferenciaContingencia);
			
		}
		
		if (juramentada != null) {
			
			conferenciaEncalheDTOSessao.setJuramentada(juramentada);
		}
		
		indicarStatusConferenciaEncalheCotaAlterado();
		
		this.carregarListaConferencia(null, false, false);
	}
	
	/**
	 * Valida se a quantidade informada excede a quantidade especificada no reparte
	 * @param qtdExemplares
	 * @param dto
	 */
	private boolean validarExcedeReparte(final BigInteger qtdExemplares, final ConferenciaEncalheDTO dto, final boolean indConferenciaContingencia){
		
		ConferenciaEncalheDTO conferenciaEncalheDTONaoValidado = null;
		
		try {
			
			conferenciaEncalheDTONaoValidado = (ConferenciaEncalheDTO)BeanUtils.cloneBean(dto);
			conferenciaEncalheDTONaoValidado.setQtdExemplar(qtdExemplares);
			
		} catch (final Exception e) {
			LOGGER.error("Falha ao validar quantidade de itens de encalhe: " + e.getMessage(), e);
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao validar quantidade de itens de encalhe.");
		} 
		
		return conferenciaEncalheService.validarQtdeEncalheExcedeQtdeReparte(
				conferenciaEncalheDTONaoValidado, getCotaFromSession(), null, indConferenciaContingencia);
	}
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
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
		
		final List<ConferenciaEncalheDTO> listaConferencia = this.getListaConferenciaEncalheFromSession();
		
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
					
					this.validarExcedeReparte(qtdExemplares, dto, indConferenciaContingencia);
					
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
	public void atualizarValores(final Long idConferencia, String qtdExemplares, final Boolean juramentada, final BigDecimal valorCapa, final boolean indConferenciaContingencia){
		
		final ConferenciaEncalheDTO conf = atualizarItemConferenciaEncalhe(idConferencia, qtdExemplares, juramentada, valorCapa, indConferenciaContingencia);
		
		final Map<String, Object> dados = new HashMap<String, Object>();
		
		dados.put("conf", conf);
		
		dados.put("reparte", (this.getInfoConferenciaSession().getReparte() == null ? BigDecimal.ZERO : this.getInfoConferenciaSession().getReparte()).setScale(4, RoundingMode.HALF_UP));
		
		this.calcularValoresMonetarios(dados);
		
		this.calcularTotais(dados);
		
		this.result.use(CustomJson.class).from(dados == null ? "" : dados).serialize();
	}
	
	@Post
	public boolean verificarPermissaoSupervisorProduto(final String qtdExemplares,
											 final String usuario, 
											 final String senha, 
											 final Long produtoEdicaoId){
		if (produtoEdicaoId != null) {
			 
			 final ProdutoEdicao produtoEdicao = produtoEdicaoService.buscarPorID(produtoEdicaoId);
			 
			 if(produtoEdicao == null){
				 throw new ValidacaoException(TipoMensagem.ERROR, "Produto Edição não encontrado.");
			 }
			 
			 final ConferenciaEncalheDTO dto = new ConferenciaEncalheDTO();
			 dto.setIdProdutoEdicao(produtoEdicaoId);
			 dto.setPacotePadrao(produtoEdicao.getPacotePadrao());
			 
		 	 BigInteger qtdeEncalhe =  this.obterQuantidadeEncalhe(qtdExemplares,dto);
		 	
		     if (this.validarExcedeReparte(qtdeEncalhe, dto, false)) {
		         
		         this.result.use(Results.json()).from("Venda negativa no encalhe, permissão requerida.", "result").serialize();
		         
		         return true;
		     }
		 }
		
		return false;
	}
	
	@Post
	public void verificarPermissaoSupervisor(final Long idConferencia, final String qtdExemplares, 
			final String usuario, final String senha, final boolean indConferenciaContingencia,
			final Long produtoEdicaoId, final boolean indPesquisaProduto){
	
		boolean isVendaNegativaProduto = false; 
		
		if(qtdExemplares == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Favor informar o valor de encalhe!");
		}
		
        if (usuario != null) {
            
            this.validarAutenticidadeSupervisor(usuario, senha);
            
        } else {
    		
            final List<ConferenciaEncalheDTO> listaConferencia = this.getListaConferenciaEncalheFromSession();
            
            final boolean supervisor = usuarioService.isSupervisor();
            
            if (listaConferencia == null || listaConferencia.isEmpty()){
                
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
                        this.criarConferenciaEncalhe(pDto, new BigInteger(qtdExemplares), false, indConferenciaContingencia);
                
                isVendaNegativaProduto = this.validarVendaNegativaProduto(
                        qtdExemplares,indConferenciaContingencia, dto, supervisor);
                
            } else {
            
                for (final ConferenciaEncalheDTO dto : listaConferencia) {
                    
                    String qtdJaInformada = null;
                    
                    if (indPesquisaProduto){
                        
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

	private boolean validarVendaNegativaProduto(final String qtdExemplares,
										 final boolean indConferenciaContingencia,
										 final ConferenciaEncalheDTO dto,
										 boolean supervisor) {
		
		BigInteger qtdeEncalhe = this.obterQuantidadeEncalhe(qtdExemplares, dto);
		
		if (this.validarExcedeReparte(qtdeEncalhe, dto, indConferenciaContingencia)) {
		    
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
			
			qtdeEncalhe = processarQtdeExemplar(dto.getIdProdutoEdicao(), dto.getPacotePadrao(), qtdExemplares);
		} 
		else {
			
			qtdeEncalhe = obterQuantidadeEncalheDaString(qtdExemplares);
		}
		
		return qtdeEncalhe;
	}

	private void validarAutenticidadeSupervisor(final String usuario,final String senha) {
		
		final boolean permitir = this.usuarioService.verificarUsuarioSupervisor(usuario, senha);
		
		if (!permitir) {
		
			throw new ValidacaoException(TipoMensagem.WARNING, "Usuário/senha inválido(s) ou usuário não é supervisor");
		}
	}

	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void alterarQtdeValorInformado(final Long idConferencia, final Long qtdInformada, final BigDecimal valorCapaInformado){
		
		final List<ConferenciaEncalheDTO> listaConferencia = this.getListaConferenciaEncalheFromSession();
		
		ConferenciaEncalheDTO conf = null;
		
		if (idConferencia != null) {
				
			for (final ConferenciaEncalheDTO dto : listaConferencia){
				
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
		
		final Map<String, Object> dados = new HashMap<String, Object>();
		
		dados.put("conf", conf);
		
		dados.put("reparte", (this.getInfoConferenciaSession().getReparte() == null ? BigDecimal.ZERO : this.getInfoConferenciaSession().getReparte()).setScale(4, RoundingMode.HALF_UP));
		
		this.calcularValoresMonetarios(dados);
		
		this.calcularTotais(dados);
		
		this.result.use(CustomJson.class).from(dados == null ? "" : dados).serialize();
		
	}
	
	/**
	 * Salva Conferencia de encalhe da Cota
	 * @param controleConfEncalheCota
	 * @param listaConferenciaEncalheCotaToSave
	 * @param indConferenciaContingencia
	 */
    @SuppressWarnings("unchecked")
	private void salvarConferenciaCota(final ControleConferenciaEncalheCota controleConfEncalheCota,
			                           final List<ConferenciaEncalheDTO> listaConferenciaEncalheCotaToSave,
			                           final boolean indConferenciaContingencia){
		
		try {

	        this.conferenciaEncalheService.salvarDadosConferenciaEncalhe(controleConfEncalheCota, 
																         listaConferenciaEncalheCotaToSave, 
																         this.getSetConferenciaEncalheExcluirFromSession(), 
																         this.getUsuarioLogado(),
																         indConferenciaContingencia);
	        

		} catch (final EncalheSemPermissaoSalvarException e) {
            LOGGER.error(
                    "Somente conferência de produtos de chamadão podem ser salvos, finalize a operação para não perder os dados: "
                            + e.getMessage(), e);
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Somente conferência de produtos de chamadão podem ser salvos, finalize a operação para não perder os dados. ");
			
		} catch (final ConferenciaEncalheFinalizadaException e) {
            LOGGER.error("Conferência não pode ser salvar, finalize a operação para não perder os dados: " + e.getMessage(), e);
            throw new ValidacaoException(TipoMensagem.WARNING, "Conferência não pode ser salvar, finalize a operação para não perder os dados.");
			
		}
		
		
		final String loginUsuarioLogado = this.getIdentificacaoUnicaUsuarioLogado();
		
		final Map<Integer, String> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, String>) session.getServletContext().getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
		
		final Map<String, String> mapaLoginNomeUsuario = 
			(LinkedHashMap<String, String>) session.getServletContext().getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO);
		
		removerTravaConferenciaCotaUsuario(this.session.getServletContext(), loginUsuarioLogado, mapaCotaConferidaUsuario, mapaLoginNomeUsuario);
		
		limparDadosSessao();
	}

	            /**
     * Salva os dados da conferência de encalhe.
     */
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void salvarConferencia(final boolean indConferenciaContingencia){
		
		this.verificarInicioConferencia();
		
		final ControleConferenciaEncalheCota controleConfEncalheCota = new ControleConferenciaEncalheCota();
		controleConfEncalheCota.setDataInicio((Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA));
		
		final InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de encalhe não inicializada.");
		}
		
		controleConfEncalheCota.setCota(info.getCota());
		controleConfEncalheCota.setId(this.getInfoConferenciaSession().getIdControleConferenciaEncalheCota());

		@SuppressWarnings({ "rawtypes", "unchecked" })
		final Map<String, Object> dadosNotaFiscal = (Map) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA);
		
		NotaFiscalEntradaCota notaFiscal = null;
		
		if(dadosNotaFiscal!=null) {
			
			notaFiscal = new NotaFiscalEntradaCota();
			
			notaFiscal.setNumero((Long) dadosNotaFiscal.get("numero"));
			notaFiscal.setSerie((String) dadosNotaFiscal.get("serie"));
			notaFiscal.setDataEmissao( DateUtil.parseDataPTBR((String) dadosNotaFiscal.get("dataEmissao")));
			notaFiscal.setChaveAcesso((String) dadosNotaFiscal.get("chaveAcesso"));
			notaFiscal.setValorProdutos((BigDecimal) dadosNotaFiscal.get("valorProdutos"));
			notaFiscal.setControleConferenciaEncalheCota(controleConfEncalheCota);
			notaFiscal.setCota(info.getCota());
		}
		
		final List<NotaFiscalEntradaCota> notaFiscalEntradaCotas = new ArrayList<NotaFiscalEntradaCota>();
		notaFiscalEntradaCotas.add(notaFiscal);
		controleConfEncalheCota.setNotaFiscalEntradaCota(notaFiscalEntradaCotas);
				
		final Box boxEncalhe = new Box();
		boxEncalhe.setId(conferenciaEncalheSessionScopeAttr.getIdBoxLogado());
		
		controleConfEncalheCota.setBox(boxEncalhe);
		
		
		final List<ConferenciaEncalheDTO> listaConferenciaEncalheCotaToSave = 
				obterCopiaListaConferenciaEncalheCota(this.getListaConferenciaEncalheFromSession());
		
		limparIdsTemporarios(listaConferenciaEncalheCotaToSave);
		
		this.salvarConferenciaCota(controleConfEncalheCota, listaConferenciaEncalheCotaToSave, indConferenciaContingencia);
		
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
	
	private void limparIdsTemporarios(final List<ConferenciaEncalheDTO> listaConferenciaEncalheDTO) {
		
		for (final ConferenciaEncalheDTO dto : listaConferenciaEncalheDTO){
			
			if (dto.getIdConferenciaEncalhe() < 0){
				
				dto.setIdConferenciaEncalhe(null);
			}
		}		
	}
	
	private List<ConferenciaEncalheDTO> obterCopiaListaConferenciaEncalheCota(final List<ConferenciaEncalheDTO> oldListaConferenciaEncalheCota) {
		
		final List<ConferenciaEncalheDTO> newListaConferenciaEncalheCota = new ArrayList<ConferenciaEncalheDTO>();
		
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
	public void finalizarConferencia(final boolean indConferenciaContingencia) throws Exception {
		
		final Date horaInicio = (Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA);
		
		DadosDocumentacaoConfEncalheCotaDTO dadosDocumentacaoConfEncalheCota = null;
		
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
			
			final List<ConferenciaEncalheDTO> listaConferenciaEncalheCotaToSave = obterCopiaListaConferenciaEncalheCota(this.getListaConferenciaEncalheFromSession());
			
			limparIdsTemporarios(listaConferenciaEncalheCotaToSave);
			
			dadosDocumentacaoConfEncalheCota = this.conferenciaEncalheService.finalizarConferenciaEncalhe(controleConfEncalheCota, 
																										  listaConferenciaEncalheCotaToSave, 
																										  this.getSetConferenciaEncalheExcluirFromSession(), 
																										  this.getUsuarioLogado(),
																										  indConferenciaContingencia,
																										  info.getReparte());
			
			this.session.removeAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
			
			final Long idControleConferenciaEncalheCota = dadosDocumentacaoConfEncalheCota.getIdControleConferenciaEncalheCota();
			
			this.getInfoConferenciaSession().setIdControleConferenciaEncalheCota(idControleConferenciaEncalheCota);
				
			try {
				
				this.gerarDocumentoConferenciaEncalhe(dadosDocumentacaoConfEncalheCota);
			} catch (final Exception e){
                LOGGER.error("Erro ao gerar documentos da conferência de encalhe: " + e.getMessage(), e);
                throw new Exception("Erro ao gerar documentos da conferência de encalhe - " + e.getMessage());
			}
			
			final Map<String, Object> dados = new HashMap<String, Object>();
			
			dados.put("tipoMensagem", TipoMensagem.SUCCESS);
			dados.put(TIPOS_DOCUMENTO_IMPRESSAO_ENCALHE, session.getAttribute(TIPOS_DOCUMENTO_IMPRESSAO_ENCALHE));
			
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
			
			limparDadosSessao();
			limparDadosSessaoConferenciaEncalheCotaFinalizada();
			this.result.use(CustomMapJson.class).put("result", dados).serialize();
			
		} else {
			
			this.result.use(Results.json()).from(
                    new ValidacaoVO(TipoMensagem.WARNING, "Conferência de Encalh não inicializada."), "result")
                    .recursive().serialize();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    private void carregarNotasFiscais(final ControleConferenciaEncalheCota controleConfEncalheCota, final InfoConferenciaEncalheCota info) {
        final Map<String, Object> dadosNotaFiscal = (Map) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA);
        
        
        TipoAtividade tipoAtividade = this.distribuidorService.tipoAtividade();
        
        //FIXME: Ajustar a funcionalidade de NF-e de Terceiros 
        NaturezaOperacao tipoNotaFiscal = this.naturezaOperacaoService.obterNaturezaOperacao(tipoAtividade, TipoDestinatario.FORNECEDOR, TipoOperacao.ENTRADA);
        
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
            notaFiscal.setDataExpedicao( DateUtil.parseDataPTBR((String) dadosNotaFiscal.get("dataEmissao")));
            notaFiscal.setTipoNotaFiscal(tipoNotaFiscal);
            notaFiscal.setOrigem(Origem.MANUAL);
            notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.RECEBIDA);
            notaFiscal.setValorInformado(CurrencyUtil.arredondarValorParaDuasCasas((BigDecimal) dadosNotaFiscal.get("valorProdutos")));
            notaFiscal.setValorBruto(CurrencyUtil.arredondarValorParaDuasCasas((BigDecimal) dadosNotaFiscal.get("valorProdutos")));
            notaFiscal.setValorDesconto(CurrencyUtil.arredondarValorParaDuasCasas((BigDecimal) dadosNotaFiscal.get("valorProdutos")));
            
            for(final ConferenciaEncalheDTO conferenciaEncalhe : this.getListaConferenciaEncalheFromSession()) {

                final ProdutoEdicao produtoEdicao = new ProdutoEdicao();
                produtoEdicao.setId(conferenciaEncalhe.getIdProdutoEdicao());
                
                final ItemNotaFiscalEntrada itemNotaFiscalEntrada = new ItemNotaFiscalEntrada();
                itemNotaFiscalEntrada.setQtde(conferenciaEncalhe.getQtdInformada());
                itemNotaFiscalEntrada.setProdutoEdicao(produtoEdicao);
                itemNotaFiscalEntrada.setTipoLancamento(TipoLancamento.LANCAMENTO);
                itemNotaFiscalEntrada.setDataRecolhimento(conferenciaEncalhe.getDataRecolhimento());
                itemNotaFiscalEntrada.setDataLancamento(conferenciaEncalhe.getDataLancamento());
                itemNotaFiscalEntrada.setNotaFiscal(notaFiscal);
                itemNotaFiscalEntrada.setDesconto(conferenciaEncalhe.getPrecoComDesconto());
                itens.add(itemNotaFiscalEntrada);
                
            }
            notaFiscal.setItens(itens);
            notaFiscalEntradaCotas.add(notaFiscal);
            controleConfEncalheCota.setNotaFiscalEntradaCota(notaFiscalEntradaCotas);
        }
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
	
	@Post
	public void buscarDetalhesProduto(final Long idConferenciaEncalhe){
		
		final List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
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
	public void excluirConferencia(final Long idConferenciaEncalhe){
		
		final List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
		for (final ConferenciaEncalheDTO dto : lista){
			
			if (dto.getIdConferenciaEncalhe().equals(idConferenciaEncalhe)){
				
				if (idConferenciaEncalhe > 0){
					
					this.getSetConferenciaEncalheExcluirFromSession().add(idConferenciaEncalhe);
				}
				
				lista.remove(dto);
				break;
			}
		}
		
		indicarStatusConferenciaEncalheCotaAlterado();
		
		this.carregarListaConferencia(null, false, false);
	}
	

	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void gravarObservacaoConferecnia(final Long idConferenciaEncalhe, final String observacao){
		
		final List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
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
		
		final List<String> mensagens = new ArrayList<String>();
		
		if(notaFiscalEntradaCota.getNumero() == null) {
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
		
		if(!mensagens.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
		
	}
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void salvarNotaFiscal(final NotaFiscalEntradaCota notaFiscal){
		
	    Map<String, Object> dadosNotaFiscal = new HashMap<String, Object>();
	    
		validarCamposNotaFiscalEntrada(notaFiscal);
		
		BigDecimal valorProdutos = CurrencyUtil.arredondarValorParaDuasCasas(notaFiscal.getValorProdutos());
		
		dadosNotaFiscal.put("numero", notaFiscal.getNumero());
		dadosNotaFiscal.put("serie", 	notaFiscal.getSerie());
		dadosNotaFiscal.put("dataEmissao", DateUtil.formatarDataPTBR(notaFiscal.getDataEmissao()));
		dadosNotaFiscal.put("chaveAcesso", notaFiscal.getChaveAcesso());
		dadosNotaFiscal.put("valorProdutos", valorProdutos);
		
		this.session.setAttribute(NOTA_FISCAL_CONFERENCIA, dadosNotaFiscal);
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	@Post
	public void carregarNotaFiscal(){
		
		@SuppressWarnings("unchecked")
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
	public void verificarValorTotalNotaFiscal(final boolean indConferenciaContingencia) throws Exception {
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final Map<String, Object> dadosNotaFiscal = (Map) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA);
		
		final Map<String, Object> dadosMonetarios = new HashMap<String, Object>();
		
		this.calcularValoresMonetarios(dadosMonetarios);
		
		final BigDecimal valorEncalhe = CurrencyUtil.arredondarValorParaDuasCasas(((BigDecimal)dadosMonetarios.get("valorEncalhe")));
		
		if (dadosNotaFiscal != null && dadosNotaFiscal.get("valorProdutos") != null && ((BigDecimal)dadosNotaFiscal.get("valorProdutos")).compareTo(valorEncalhe) != 0){
			
			final Map<String, Object> dadosResposta = new HashMap<String, Object>();
			
			dadosResposta.put("tipoMensagem", TipoMensagem.WARNING);
			dadosResposta.put("listaMensagens", new String[]{"Valor total do encalhe difere do valor da nota informada."});
			
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
		
		this.calcularValoresMonetarios(valoresMonetarios);
		
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
				dados.put("desconto", p.getDesconto());
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
		this.session.removeAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
		this.session.removeAttribute(HORA_INICIO_CONFERENCIA);
		this.session.removeAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA);
		this.session.removeAttribute(CONFERENCIA_ENCALHE_COTA_STATUS);
		
		indicarStatusConferenciaEncalheCotaSalvo();
	}
	
	@SuppressWarnings("unchecked")
	private void limparDadosSessaoConferenciaEncalheCotaFinalizada() {
		
	    this.session.removeAttribute(NUMERO_COTA);
		this.session.removeAttribute(INFO_CONFERENCIA);
		this.session.removeAttribute(NOTA_FISCAL_CONFERENCIA);
		this.session.removeAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
		this.session.removeAttribute(HORA_INICIO_CONFERENCIA);
		this.session.removeAttribute(CONFERENCIA_ENCALHE_COTA_STATUS);
		
		final String loginUsuarioLogado = this.getIdentificacaoUnicaUsuarioLogado();
		
		final Map<Integer, String> mapaCotaConferidaUsuario = (LinkedHashMap<Integer, String>) session.getServletContext().getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_USUARIO);
		
		final Map<String, String> mapaLoginNomeUsuario = (LinkedHashMap<String, String>) session.getServletContext().getAttribute(Constants.MAP_TRAVA_CONFERENCIA_COTA_LOGIN_NOME_USUARIO);
		
		removerTravaConferenciaCotaUsuario(this.session.getServletContext(), loginUsuarioLogado, mapaCotaConferidaUsuario, mapaLoginNomeUsuario);
		
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
	private void calcularValoresMonetarios(final Map<String, Object> dados){
		
		BigDecimal valorEncalhe = BigDecimal.ZERO;
		BigDecimal valorVendaDia = BigDecimal.ZERO;
		BigDecimal valorDebitoCredito = BigDecimal.ZERO;
		BigDecimal valorTotal = BigDecimal.ZERO;
		BigDecimal valorEncalheAtualizado = BigDecimal.ZERO;
		BigDecimal valorVendaDiaAtualizado = BigDecimal.ZERO;
		
		final InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info != null){
			
			if (info.getListaConferenciaEncalhe() != null && !info.getListaConferenciaEncalhe().isEmpty()) {
			
				for (final ConferenciaEncalheDTO conferenciaEncalheDTO : info.getListaConferenciaEncalhe()){
					
					final BigDecimal precoCapa = CurrencyUtil.arredondarValorParaDuasCasas(conferenciaEncalheDTO.getPrecoCapa() == null ? BigDecimal.ZERO : conferenciaEncalheDTO.getPrecoCapa());
					
					final BigDecimal desconto =  CurrencyUtil.arredondarValorParaDuasCasas(conferenciaEncalheDTO.getDesconto() == null ? BigDecimal.ZERO : conferenciaEncalheDTO.getDesconto());
					
					final BigDecimal precoComDesconto =  CurrencyUtil.arredondarValorParaDuasCasas(conferenciaEncalheDTO.getPrecoComDesconto() == null ? BigDecimal.ZERO : conferenciaEncalheDTO.getPrecoComDesconto());
					
					final BigDecimal qtdExemplar = conferenciaEncalheDTO.getQtdExemplar() == null ? BigDecimal.ZERO : new BigDecimal(conferenciaEncalheDTO.getQtdExemplar());
					
					valorTotal = valorTotal.add( CurrencyUtil.arredondarValorParaQuatroCasas(conferenciaEncalheDTO.getValorTotal() != null ? conferenciaEncalheDTO.getValorTotal() :  BigDecimal.ZERO ));
					
					valorEncalhe = valorEncalhe.add(precoComDesconto.multiply(qtdExemplar));
					
					valorEncalheAtualizado = valorEncalheAtualizado.add(precoCapa.subtract(desconto).multiply(new BigDecimal(conferenciaEncalheDTO.getQtdInformada())));
				}
			}
			
			valorVendaDia = valorVendaDia.add(info.getReparte().subtract(valorEncalhe));
			valorVendaDiaAtualizado = valorVendaDiaAtualizado.add(info.getReparte().subtract(valorEncalheAtualizado));
			
			if (info.getListaDebitoCreditoCota() != null) {
			
				for (final DebitoCreditoCotaDTO debitoCreditoCotaDTO : info.getListaDebitoCreditoCota()){
					
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
			dados.put("valorTotal",  valorTotal.setScale(4, RoundingMode.HALF_UP));
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
			final Long idProdutoEdicao,
			Integer pacotePadrao,
			String quantidade) {

		if(idProdutoEdicao == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto edição de encalhe inválido.");
		}
		
		if(quantidade == null || quantidade.trim().isEmpty()) {
			return null;
		}
		
		boolean isContagemPacote = this.conferenciaEncalheService.isContagemPacote(idProdutoEdicao);
		
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
														  final boolean adicionarGrid, final boolean indConferenciaContingencia) {
		
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

		conferenciaEncalheDTO.setContagemPacote(this.conferenciaEncalheService.isContagemPacote(produtoEdicao.getId()));
		
		if (produtoEdicao.getTipoChamadaEncalhe() != null) {
			
			conferenciaEncalheDTO.setTipoChamadaEncalhe(produtoEdicao.getTipoChamadaEncalhe().name());
		}
		
		conferenciaEncalheDTO.setDataRecolhimento(produtoEdicao.getDataRecolhimentoDistribuidor());
		
		conferenciaEncalheDTO.setDataConferencia(dataOperacao);
		
		conferenciaEncalheDTO.setParcialNaoFinal(this.conferenciaEncalheService.isParcialNaoFinal(produtoEdicao.getId()));
		
		if (quantidade != null){
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
		
		if (adicionarGrid){
			
			final List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
			
			lista.add(conferenciaEncalheDTO);
			
			this.setListaConferenciaEncalheToSession(lista);
		}

		final Integer diaRecolhimento = this.distribuidorService.obterDiaDeRecolhimentoDaData(dataOperacao, 
				                                                            conferenciaEncalheDTO.getDataRecolhimento(),
				                                                            numeroCota,
				                                                            produtoEdicao.getId(), null);
				
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
	private TableModel<CellModelKeyValue<DebitoCreditoCotaDTO>> 
		obterTableModelDebitoCreditoCota(final List<DebitoCreditoCotaDTO> listaDebitoCreditoCota) {

		final TableModel<CellModelKeyValue<DebitoCreditoCotaDTO>> tableModelDebitoCreditoCota = 
				new TableModel<CellModelKeyValue<DebitoCreditoCotaDTO>>();
		
		tableModelDebitoCreditoCota.setRows(CellModelKeyValue.toCellModelKeyValue(listaDebitoCreditoCota));
		tableModelDebitoCreditoCota.setTotal((listaDebitoCreditoCota!= null) ? listaDebitoCreditoCota.size() : 0);
		tableModelDebitoCreditoCota.setPage(1);
		
		return tableModelDebitoCreditoCota;
	}
	
	private List<ConferenciaEncalheDTO> getListaConferenciaEncalheFromSession() {
		
		final InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de encalhe não inicializada.");
		}
		
		List<ConferenciaEncalheDTO> lista = info.getListaConferenciaEncalhe();
		
		if (lista == null){
			
			lista = new ArrayList<ConferenciaEncalheDTO>();
		}
		
		return lista;
	}

	private void setListaConferenciaEncalheToSession(final List<ConferenciaEncalheDTO> listaConferenciaEncalheDTO) {
		
		final InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de encalhe não inicializada.");
		}
		
		info.setListaConferenciaEncalhe(listaConferenciaEncalheDTO);
	}
	
	private Set<Long> getSetConferenciaEncalheExcluirFromSession(){
		
		@SuppressWarnings("unchecked")
		Set<Long> set = (Set<Long>) this.session.getAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
		
		if (set == null){
			
			set = new HashSet<Long>();
			
			this.session.setAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR, set);
		}
		
		return set;
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
	
}
