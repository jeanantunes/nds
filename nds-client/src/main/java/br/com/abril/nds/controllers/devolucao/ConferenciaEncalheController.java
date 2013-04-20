package br.com.abril.nds.controllers.devolucao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DadosDocumentacaoConfEncalheCotaDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.enums.TipoDocumentoConferenciaEncalhe;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheFinalizadaException;
import br.com.abril.nds.service.exception.EncalheRecolhimentoParcialException;
import br.com.abril.nds.service.exception.EncalheSemPermissaoSalvarException;
import br.com.abril.nds.service.exception.FechamentoEncalheRealizadoException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.sessionscoped.ConferenciaEncalheSessionScopeAttr;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.PDFUtil;
import br.com.abril.nds.util.TableModel;
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
	
	private ConferenciaEncalheSessionScopeAttr conferenciaEncalheSessionScopeAttr;
	
	public ConferenciaEncalheController(ConferenciaEncalheSessionScopeAttr conferenciaEncalheSessionScopeAttr){
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
	
	private static final int QUANTIDADE_MAX_REGISTROS = 15;
	
	private static final String CONFERENCIA_ENCALHE_COTA_STATUS = "CONFERENCIA_ENCALHE_COTA_STATUS";
	
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
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private BoxService boxService;

	@Autowired
	private HttpServletResponse httpResponse;
	
	@Path("/")
	public void index() {
		
		this.result.include(
				"dataOperacao", 
				DateUtil.formatarDataPTBR(distribuidorService.obterDataOperacaoDistribuidor()));
		
		TipoContabilizacaoCE tipoContabilizacaoCE = conferenciaEncalheService.obterTipoContabilizacaoCE();
		
		if(tipoContabilizacaoCE!=null) {
			this.result.include("tipoContabilizacaoCE", tipoContabilizacaoCE.name());
		}
		
		//Obter box usuário
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
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();

		this.result.include("dataOperacao", DateUtil.formatarDataPTBR(dataOperacao));
		
		TipoContabilizacaoCE tipoContabilizacaoCE = conferenciaEncalheService.obterTipoContabilizacaoCE();
		
		if(tipoContabilizacaoCE!=null) {
			this.result.include("tipoContabilizacaoCE", tipoContabilizacaoCE.name());
		}
	}
	
	public void carregarComboBoxEncalheContingencia() {
		
		List<Box> boxes = 
				this.conferenciaEncalheService.obterListaBoxEncalhe(this.getUsuarioLogado().getId());
		
		if( boxes!=null ) {

			Map<String, String> mapBox = new HashMap<String, String>();
			
			for(Box box : boxes) {
				mapBox.put(box.getId().toString(), box.getNome());
			}
			
			this.result.use(CustomJson.class).from(mapBox).serialize();
		} else {

			this.result.use(Results.json()).from("").serialize();
		}
	}
	
	private void carregarComboBoxEncalhe() {
		
		List<Box> boxes = 
				this.conferenciaEncalheService.obterListaBoxEncalhe(this.getUsuarioLogado().getId());
		
		this.result.include("boxes", boxes);
	}
	
	@Post
	public void salvarIdBoxSessao(Long idBox){
		
		if (idBox != null){
		
			conferenciaEncalheSessionScopeAttr.setIdBoxLogado(idBox);
			
			this.result.include("boxes", idBox);
			
			alterarBoxUsuario(idBox);
		} else {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Box de recolhimento é obrigatório.");
		}
		
		this.result.use(Results.json()).from("").serialize();
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
				boolean indConferenciaEncalheCotaSalva) {
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
	 * Verifica se o usuario esta iniciando (ou reiniciando) a conferência de uma cota 
	 * sem ter salvo (ou finalizado) os dados de uma conferência em andamento.
	 * 
	 * @param numeroCota
	 */
	public void verificarConferenciaEncalheCotaStatus(Integer numeroCota) {
		
		Map<String, Object> resultado = new HashMap<String, Object>();
		
		String conferenciaEncalheCotaStatus = obterStatusConferenciaEncalheCota();
		
		if(numeroCota != null) {
			resultado.put("NUMERO_COTA_IGUAL", numeroCota.equals(session.getAttribute(NUMERO_COTA)));
		}
		
		resultado.put(CONFERENCIA_ENCALHE_COTA_STATUS, conferenciaEncalheCotaStatus);
		
		this.result.use(CustomJson.class).from(resultado).serialize();
		
	}
	
	private String obterStatusConferenciaEncalheCota() {
		
		StatusConferenciaEncalheCota statusConferenciaEncalheCota = obterStatusConferenciaEncalheCotaFromSession();
		
		if( statusConferenciaEncalheCota.getNumeroCota() != null && 
			!statusConferenciaEncalheCota.isIndConferenciaEncalheCotaSalva() ) {
			
			return CONF_ENC_COTA_STATUS_INICIADA_NAO_SALVA;
			
		} else if( statusConferenciaEncalheCota.getNumeroCota() != null && 
				statusConferenciaEncalheCota.isIndConferenciaEncalheCotaSalva() ){
			
			return CONF_ENC_COTA_STATUS_INICIADA_SALVA;
			
		} else {
			
			return CONF_ENC_COTA_STATUS_NAO_INICIADA;
			
		}
		
	}
	
	public void verificarCotaEmiteNFe(Integer numeroCota) {
				
		boolean emiteNfe = conferenciaEncalheService.isCotaEmiteNfe(numeroCota);
		
		this.result.use(CustomMapJson.class).put(IND_COTA_EMITE_NFE, emiteNfe).serialize();
	}
	
	@Post
	public void verificarReabertura(Integer numeroCota){
		
		limparDadosSessao();
		
		this.session.setAttribute(HORA_INICIO_CONFERENCIA, new Date());
		
		if (conferenciaEncalheSessionScopeAttr.getIdBoxLogado() == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Box de recolhimento não informado.");
		}
		
		try {
			
			this.conferenciaEncalheService.validarFechamentoEncalheRealizado();
			
		} catch(FechamentoEncalheRealizadoException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
			
		}
		
		try {
			
			this.conferenciaEncalheService.verificarChamadaEncalheCota(numeroCota);
			
		} catch (ConferenciaEncalheExistenteException e) {

			this.result.use(CustomMapJson.class)
			.put("IND_COTA_RECOLHE_NA_DATA", "S")
			.put("IND_REABERTURA", "S").serialize();

			return;
		
		} catch (ChamadaEncalheCotaInexistenteException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe para essa cota.");
		}
		
		boolean indCotaComRecolhimento = conferenciaEncalheService.isCotaComReparteARecolherNaDataOperacao(numeroCota);

		this.session.setAttribute(NUMERO_COTA, numeroCota);
		
		if(!indCotaComRecolhimento) {
			
			this.result.use(CustomMapJson.class)
			.put("IND_COTA_RECOLHE_NA_DATA", "N")
			.put("msg", "Cota não possui recolhimento planejado para a data de operação atual.").serialize();
			
		} else {
			
			this.result.use(CustomMapJson.class).put("IND_COTA_RECOLHE_NA_DATA", "S").serialize();
			
		}
		
	}
	
	/**
	 * Cria em session flag para indicar que os registros de conferencia de encalhe da cota
	 * que estão em session ainda não foram alterados pelo usuario.
	 */
	private void indicarStatusConferenciaEncalheCotaSalvo() {
		StatusConferenciaEncalheCota statusConferenciaEncalhe = obterStatusConferenciaEncalheCotaFromSession();
		statusConferenciaEncalhe.setIndConferenciaEncalheCotaSalva(true);
	}
	
	/**
	 * Cria em session flag para indicar que os registros de conferencia de encalhe da cota
	 * que estão em session já foram alterados pelo usuario.
	 */
	private void indicarStatusConferenciaEncalheCotaAlterado() {
		StatusConferenciaEncalheCota statusConferenciaEncalhe = obterStatusConferenciaEncalheCotaFromSession();
		statusConferenciaEncalhe.setIndConferenciaEncalheCotaSalva(false);
	}
	
	private void recarregarInfoConferenciaEncalheCotaEmSession(Integer numeroCota, boolean indConferenciaContingencia) {
		
		InfoConferenciaEncalheCota infoConfereciaEncalheCota = 
				conferenciaEncalheService.obterInfoConferenciaEncalheCota(numeroCota, indConferenciaContingencia);
	
		this.session.setAttribute(INFO_CONFERENCIA, infoConfereciaEncalheCota);
		
		this.setListaConferenciaEncalheToSession(infoConfereciaEncalheCota.getListaConferenciaEncalhe());
		
		indicarStatusConferenciaEncalheCotaSalvo();
		
	}
	
	@Post
	public void carregarListaConferencia(Integer numeroCota, boolean indObtemDadosFromBD,  boolean indConferenciaContingencia){
		
		if (numeroCota == null){
			
			numeroCota = this.getNumeroCotaFromSession();
		} else {
			
			this.session.setAttribute(NUMERO_COTA, numeroCota);
		}
		
		Date horaInicio = (Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA);
		
		if (horaInicio == null){
			
			this.session.setAttribute(HORA_INICIO_CONFERENCIA, new Date());
		}
		
		InfoConferenciaEncalheCota infoConfereciaEncalheCota = this.getInfoConferenciaSession();
		
		if (infoConfereciaEncalheCota == null || indObtemDadosFromBD){
			
			recarregarInfoConferenciaEncalheCotaEmSession(numeroCota, indConferenciaContingencia);
			
			infoConfereciaEncalheCota = this.getInfoConferenciaSession();
		
		}
		
		carregarValorInformadoInicial(infoConfereciaEncalheCota.getListaConferenciaEncalhe());
		
		Map<String, Object> dados = new HashMap<String, Object>();
		
		dados.put("listaConferenciaEncalhe", infoConfereciaEncalheCota.getListaConferenciaEncalhe());
		
		dados.put("listaDebitoCredito", this.obterTableModelDebitoCreditoCota(infoConfereciaEncalheCota.getListaDebitoCreditoCota()));
		
		dados.put("reparte", infoConfereciaEncalheCota.getReparte() == null ? BigDecimal.ZERO : infoConfereciaEncalheCota.getReparte());
		
		dados.put("indDistribuidorAceitaJuramentado", infoConfereciaEncalheCota.isDistribuidorAceitaJuramentado());
		
		this.calcularValoresMonetarios(dados);
		
		Cota cota = infoConfereciaEncalheCota.getCota();
		
		if (cota != null){
			
			dados.put("razaoSocial", 
				cota.getPessoa() instanceof PessoaFisica ? 
						((PessoaFisica)cota.getPessoa()).getNome() : 
							((PessoaJuridica)cota.getPessoa()).getRazaoSocial());
			
			dados.put("situacao", cota.getSituacaoCadastro().toString());
		}
		
		if(infoConfereciaEncalheCota.getNotaFiscalEntradaCota()!=null) {

			Map<String, Object> dadosNotaFiscal = new HashMap<String, Object>();
			
			dadosNotaFiscal.put("numero", infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getNumero());
			dadosNotaFiscal.put("serie", 	infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getSerie());
			dadosNotaFiscal.put("dataEmissao", DateUtil.formatarDataPTBR(infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getDataEmissao()));
			dadosNotaFiscal.put("chaveAcesso", infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getChaveAcesso());
			dadosNotaFiscal.put("valorProdutos", infoConfereciaEncalheCota.getNotaFiscalEntradaCota().getValorProdutos());
			
			this.session.setAttribute(NOTA_FISCAL_CONFERENCIA, dadosNotaFiscal);

			dados.put("notaFiscal", dadosNotaFiscal);

			
		} else if( session.getAttribute(NOTA_FISCAL_CONFERENCIA) != null ){
			
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
	private void carregarValorInformadoInicial(List<ConferenciaEncalheDTO> listaConferenciaEncalhe) {
		
		if(listaConferenciaEncalhe == null || listaConferenciaEncalhe.isEmpty()) {
			return;
		}
		
		for(ConferenciaEncalheDTO conferencia : listaConferenciaEncalhe) {
		
			conferencia.setQtdInformada(conferencia.getQtdExemplar());
			conferencia.setPrecoCapaInformado(conferencia.getPrecoCapa());
			
		}
		
	}
	
	private void calcularTotais(Map<String, Object> dados) {
		
		BigInteger qtdInformada = BigInteger.ZERO;
		BigInteger qtdRecebida = BigInteger.ZERO;
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info != null){
			
			if (info.getListaConferenciaEncalhe() != null){
			
				for (ConferenciaEncalheDTO conferenciaEncalheDTO : info.getListaConferenciaEncalhe()){
					
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
	public void autoCompleteProdutoEdicaoCodigoDeBarras(String codigoBarra) {
		
		if (codigoBarra == null || codigoBarra.trim().isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Código de barras inválido.");
		}

		List<ItemAutoComplete> listaProdutos = this.produtoEdicaoService.obterPorCodigoBarraILike(codigoBarra);
		if (listaProdutos == null || listaProdutos.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nehum produto Encontrado.");
		}

		this.result.use(Results.json()).from(listaProdutos, "result").recursive().serialize();
	}

	/**
	 * Obtém o objeto do tipo ConferenciaEncalheDTO que esta na lista 
	 * de conferencia em session com idProdutoEdicao ou codigoSM igual 
	 * ao passado por parâmetro.
	 * 
	 * @param idProdutoEdicao
	 * @param sm
	 * 
	 * @return ConferenciaEncalheDTO
	 */
	private ConferenciaEncalheDTO getConferenciaEncalheDTOFromSession(Long idProdutoEdicao, Integer sm) {
		
		List<ConferenciaEncalheDTO> listaConfSessao = this.getListaConferenciaEncalheFromSession();
		
		if(idProdutoEdicao != null) {
			for (ConferenciaEncalheDTO dto : listaConfSessao){
				if (idProdutoEdicao.equals(dto.getIdProdutoEdicao())){
					return  dto;
				}
			}
		} else if( sm != null) {
			for (ConferenciaEncalheDTO dto : listaConfSessao){
				if (sm.equals(dto.getCodigoSM())){
					return  dto;
				}
			}
		}
		
		return null;
		
	}
	
	@Post
	public void pesquisarProdutoEdicaoCodigoSM(Integer sm, Long idProdutoEdicaoAnterior, String quantidade){

		this.verificarInicioConferencia();
		ProdutoEdicaoDTO produtoEdicao = null;
		ConferenciaEncalheDTO conferenciaEncalheDTO = null;
		Integer numeroCota = this.getNumeroCotaFromSession();
		
		if(sm == null && idProdutoEdicaoAnterior==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe SM.");
		}

		try {
			
			conferenciaEncalheDTO = getConferenciaEncalheDTOFromSession(null, sm);
			produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorSM(numeroCota, sm);
			
		} catch(ChamadaEncalheCotaInexistenteException e){
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe deste produto para essa cota.");
		} catch(EncalheRecolhimentoParcialException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe para produto parcial na data operação.");
		}
		
		if (conferenciaEncalheDTO == null && produtoEdicao == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto Edição não encontrado.");
		} else if (conferenciaEncalheDTO == null){
			conferenciaEncalheDTO = this.criarConferenciaEncalhe(produtoEdicao, quantidade, false, false);
		}
		
		if (idProdutoEdicaoAnterior != null && quantidade != null){
			
			conferenciaEncalheDTO = getConferenciaEncalheDTOFromSession(idProdutoEdicaoAnterior, null);

			if (conferenciaEncalheDTO != null){
				BigInteger qtde = this.processarQtdeExemplar(produtoEdicao.getId(), conferenciaEncalheDTO, quantidade, false);
				conferenciaEncalheDTO.setQtdExemplar(qtde);
			} else {
				conferenciaEncalheDTO = this.criarConferenciaEncalhe(produtoEdicao, quantidade, false, false);
			}
		
		}

		indicarStatusConferenciaEncalheCotaAlterado();

		this.result.use(Results.json()).from(conferenciaEncalheDTO, "result").serialize();
		
		
	}
	
	@Post
	public void pesquisarProdutoEdicao(Long idProdutoEdicaoAnterior, String quantidade){
		
		this.verificarInicioConferencia();
		
		ProdutoEdicaoDTO produtoEdicao = null;
		
		ConferenciaEncalheDTO conferenciaEncalheDTO = null;
		
		Integer numeroCota = this.getNumeroCotaFromSession();
		
		
		if(idProdutoEdicaoAnterior == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe código de barras, SM ou código.");
		}
		
		try {

			if (idProdutoEdicaoAnterior != null){
				
				conferenciaEncalheDTO = getConferenciaEncalheDTOFromSession(idProdutoEdicaoAnterior, null);
				
				produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(numeroCota, idProdutoEdicaoAnterior);
			} 
			
		} catch(ChamadaEncalheCotaInexistenteException e){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe deste produto para essa cota.");
		} catch(EncalheRecolhimentoParcialException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe para produto parcial na data operação.");
		}
		
		if (conferenciaEncalheDTO == null && produtoEdicao == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto Edição não encontrado.");
		} else if (conferenciaEncalheDTO == null){
			
			conferenciaEncalheDTO = this.criarConferenciaEncalhe(produtoEdicao, quantidade, false, false);
		}
		
		if (idProdutoEdicaoAnterior != null && quantidade != null){
			
			conferenciaEncalheDTO = getConferenciaEncalheDTOFromSession(idProdutoEdicaoAnterior, null);

			if (conferenciaEncalheDTO != null){
				
				BigInteger qtde = this.processarQtdeExemplar(produtoEdicao.getId(), conferenciaEncalheDTO, quantidade, false);
				
				conferenciaEncalheDTO.setQtdExemplar(qtde);
			} else {
				
				conferenciaEncalheDTO = this.criarConferenciaEncalhe(produtoEdicao, quantidade, false, false);
			}
		
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
	private List<ConferenciaEncalheDTO> atualizarProdutoRepetido(long idProdutoEdicao, BigInteger qtd, boolean indConferenciaContingencia){
		
		List<ConferenciaEncalheDTO> listaConferencia = this.getListaConferenciaEncalheFromSession();
		
		for (ConferenciaEncalheDTO ceDTO : listaConferencia){
			
			if (ceDTO.getIdProdutoEdicao().equals(idProdutoEdicao)){
				
				this.validarExcedeReparte(ceDTO.getQtdInformada().add(qtd).longValue(), ceDTO, indConferenciaContingencia);
				
				ceDTO.setQtdExemplar(ceDTO.getQtdInformada().add(qtd));
				
				BigDecimal preco = (ceDTO.getPrecoComDesconto() != null) ? ceDTO.getPrecoComDesconto() : 
					(ceDTO.getPrecoCapa() != null) ? ceDTO.getPrecoCapa() : BigDecimal.ZERO;  

					ceDTO.setValorTotal(preco.multiply(new BigDecimal(ceDTO.getQtdExemplar().intValue())));
			}
		}
		
		return listaConferencia;
	}
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void adicionarProdutoConferido(Long idProdutoEdicao, String quantidade, Boolean juramentada, boolean indConferenciaContingencia) {
		
		if (idProdutoEdicao == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto é obrigatório.");
		}
		
		if (quantidade == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade é obrigatório.");
		}
		
		ProdutoEdicaoDTO produtoEdicao = null;
		
		try {
			
			produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(
					this.getNumeroCotaFromSession(), 
					idProdutoEdicao);
		} catch (EncalheRecolhimentoParcialException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe para produto parcial na data operação.");
		} catch (ChamadaEncalheCotaInexistenteException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
		
		ConferenciaEncalheDTO conferenciaEncalheDTOSessao = getConferenciaEncalheDTOFromSession(idProdutoEdicao, null);

		if (conferenciaEncalheDTOSessao != null){
			
			BigInteger qtde = this.processarQtdeExemplar(produtoEdicao.getId(), conferenciaEncalheDTOSessao, quantidade, indConferenciaContingencia);
			
			conferenciaEncalheDTOSessao.setQtdExemplar(qtde);
			
			this.setListaConferenciaEncalheToSession(this.atualizarProdutoRepetido(idProdutoEdicao, qtde, indConferenciaContingencia));
			
		} else {
			
			conferenciaEncalheDTOSessao = this.criarConferenciaEncalhe(produtoEdicao, quantidade, true, indConferenciaContingencia);
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
	private void validarExcedeReparte(Long qtdExemplares, ConferenciaEncalheDTO dto, boolean indConferenciaContingencia){
		
		ConferenciaEncalheDTO conferenciaEncalheDTONaoValidado = null;
		
		try {
			
			conferenciaEncalheDTONaoValidado = (ConferenciaEncalheDTO)BeanUtils.cloneBean(dto);
			conferenciaEncalheDTONaoValidado.setQtdExemplar(BigInteger.valueOf(qtdExemplares));
			
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao validar quantidade de itens de encalhe.");
		} 
		
		conferenciaEncalheService.validarQtdeEncalheExcedeQtdeReparte(
				conferenciaEncalheDTONaoValidado, getNumeroCotaFromSession(), null, indConferenciaContingencia);
	}
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void atualizarValores(Long idConferencia, Long qtdExemplares, Boolean juramentada, BigDecimal valorCapa, boolean indConferenciaContingencia){
		
		List<ConferenciaEncalheDTO> listaConferencia = this.getListaConferenciaEncalheFromSession();
		
		if(qtdExemplares == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade de exemplares inválida.");
		}
		
		ConferenciaEncalheDTO conf = null;
		
		if (idConferencia != null){
				
			for (ConferenciaEncalheDTO dto : listaConferencia){
				
				if (dto.getIdConferenciaEncalhe().equals(idConferencia)){
					
					this.validarExcedeReparte(qtdExemplares, dto, indConferenciaContingencia);
					
					dto.setQtdExemplar(BigInteger.valueOf(qtdExemplares));
					dto.setQtdInformada(BigInteger.valueOf(qtdExemplares));
					
					if (juramentada != null){
					
						dto.setJuramentada(juramentada);
					}
					
					if (valorCapa != null){
						
						dto.setPrecoCapa(valorCapa);
					}
					 
					BigDecimal precoCapa = dto.getPrecoCapa() == null ? BigDecimal.ZERO : dto.getPrecoCapa();
					BigDecimal desconto = dto.getDesconto() == null ? BigDecimal.ZERO : dto.getDesconto();
					BigDecimal qtdExemplar = dto.getQtdExemplar() == null ? BigDecimal.ZERO : new BigDecimal(dto.getQtdExemplar()); 
					
					dto.setValorTotal(precoCapa.subtract(desconto).multiply( qtdExemplar ));
					
					conf = dto;
					
					break;
				}
			}
		}
		
		Map<String, Object> dados = new HashMap<String, Object>();
		
		dados.put("conf", conf);
		
		dados.put("reparte", this.getInfoConferenciaSession().getReparte() == null ? BigDecimal.ZERO : this.getInfoConferenciaSession().getReparte());
		
		this.calcularValoresMonetarios(dados);
		
		this.calcularTotais(dados);
		
		indicarStatusConferenciaEncalheCotaAlterado();
		
		this.result.use(CustomJson.class).from(dados == null ? "" : dados).serialize();
	}

	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void alterarQtdeValorInformado(Long idConferencia, Long qtdInformada, BigDecimal valorCapaInformado){
		
		List<ConferenciaEncalheDTO> listaConferencia = this.getListaConferenciaEncalheFromSession();
		
		ConferenciaEncalheDTO conf = null;
		
		if (idConferencia != null) {
				
			for (ConferenciaEncalheDTO dto : listaConferencia){
				
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
		
		Map<String, Object> dados = new HashMap<String, Object>();
		
		dados.put("conf", conf);
		
		dados.put("reparte", this.getInfoConferenciaSession().getReparte() == null ? BigDecimal.ZERO : this.getInfoConferenciaSession().getReparte());
		
		this.calcularValoresMonetarios(dados);
		
		this.calcularTotais(dados);
		
		this.result.use(CustomJson.class).from(dados == null ? "" : dados).serialize();
		
	}

	/**
	 * Salva os dados da conferência de encalhe.
	 */
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void salvarConferencia(boolean indConferenciaContingencia){
		
		this.verificarInicioConferencia();
		
		ControleConferenciaEncalheCota controleConfEncalheCota = new ControleConferenciaEncalheCota();
		controleConfEncalheCota.setDataInicio((Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA));
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada.");
		}
		
		controleConfEncalheCota.setCota(info.getCota());
		controleConfEncalheCota.setId(this.getInfoConferenciaSession().getIdControleConferenciaEncalheCota());

		@SuppressWarnings({ "rawtypes", "unchecked" })
		Map<String, Object> dadosNotaFiscal = (Map) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA);
		
		NotaFiscalEntradaCota notaFiscal = null;
		
		if(dadosNotaFiscal!=null) {
			
			notaFiscal = new NotaFiscalEntradaCota();
			
			notaFiscal.setNumero((Long) dadosNotaFiscal.get("numero"));
			notaFiscal.setSerie((String) dadosNotaFiscal.get("serie"));
			notaFiscal.setDataEmissao( DateUtil.parseDataPTBR((String) dadosNotaFiscal.get("dataEmissao")));
			notaFiscal.setChaveAcesso((String) dadosNotaFiscal.get("chaveAcesso"));
			notaFiscal.setValorProdutos((BigDecimal) dadosNotaFiscal.get("valorProdutos"));
			
		}
		
		List<NotaFiscalEntradaCota> notaFiscalEntradaCotas = new ArrayList<NotaFiscalEntradaCota>();
		notaFiscalEntradaCotas.add(notaFiscal);
		controleConfEncalheCota.setNotaFiscalEntradaCota(notaFiscalEntradaCotas);
				
		Box boxEncalhe = new Box();
		boxEncalhe.setId(conferenciaEncalheSessionScopeAttr.getIdBoxLogado());
		
		controleConfEncalheCota.setBox(boxEncalhe);
		
		
		List<ConferenciaEncalheDTO> listaConferenciaEncalheCotaToSave = 
				obterCopiaListaConferenciaEncalheCota(this.getListaConferenciaEncalheFromSession());
		
		limparIdsTemporarios(listaConferenciaEncalheCotaToSave);
		
		try {
			
			this.conferenciaEncalheService.salvarDadosConferenciaEncalhe(
					controleConfEncalheCota, 
					listaConferenciaEncalheCotaToSave, 
					this.getSetConferenciaEncalheExcluirFromSession(), 
					this.getUsuarioLogado(),
					indConferenciaContingencia);
			
			limparDadosSessao();
			
		} catch (EncalheSemPermissaoSalvarException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Somente conferência de produtos de chamadão podem ser salvos, finalize a operação para não perder os dados. ");
			
		} catch (ConferenciaEncalheFinalizadaException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Conferência não pode ser salvar, finalize a operação para não perder os dados.");
			
		}
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
	}
	
	
	private Long obterIdTemporario() {
		
		int id = (int) System.currentTimeMillis();
		
		if (id > 0){
			id *= -1;
		}
		
		return new Long(id);
		
	}
	
	
	private void verificarInicioConferencia() {
		
		Date horaInicio = (Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA);
		
		if (horaInicio == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Conferência não iniciada.");
		}
		
	}

	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void gerarDocumentoConferenciaEncalhe(DadosDocumentacaoConfEncalheCotaDTO dtoDoc) throws Exception {
		
		try {
			ArrayList<String> tiposDocumentoImpressao = new ArrayList<String>();
			Long idControleConferenciaEncalheCota = dtoDoc.getIdControleConferenciaEncalheCota();
			List<byte[]> arquivos = new ArrayList<byte[]>();
			Map<String, byte[]> mapFileNameFile = new HashMap<String, byte[]>();
			
			if(dtoDoc.isUtilizaBoletoSlip()){//Slip-PDF+Boleto
				
				gerarSlipPDFFinalizacaoEncalhe(tiposDocumentoImpressao,
						idControleConferenciaEncalheCota, arquivos,
						mapFileNameFile);
				
				gerarBoletoFinalizacaoEncalhe(dtoDoc,
						tiposDocumentoImpressao,
						idControleConferenciaEncalheCota, arquivos,
						mapFileNameFile);
				
				
				byte[] arquivoSlip = PDFUtil.mergePDFs(arquivos);
				String nomeChave = TipoDocumentoConferenciaEncalhe.BOLETO_SLIP.name()+"+"+TipoDocumentoConferenciaEncalhe.SLIP_PDF.name();
				mapFileNameFile.put(nomeChave, arquivoSlip);
				tiposDocumentoImpressao.add(nomeChave);

				arquivos.clear();
				
			}else if(dtoDoc.isUtilizaBoleto()) {//Boleto
				
				gerarBoletoFinalizacaoEncalhe(dtoDoc,
						tiposDocumentoImpressao,
						idControleConferenciaEncalheCota, arquivos,
						mapFileNameFile);

				byte[] arquivoBoleto = PDFUtil.mergePDFs(arquivos);
				String nomeChave = TipoDocumentoConferenciaEncalhe.BOLETO_SLIP.name();
				mapFileNameFile.put(nomeChave, arquivoBoleto);
				tiposDocumentoImpressao.add(nomeChave);
				arquivos.clear();
			}
			
			if(dtoDoc.isUtilizaSlip()){//Slip-TXT / Matricial

				gerarSlipMatricialFinalizacaoEncalhe(tiposDocumentoImpressao,
						idControleConferenciaEncalheCota, mapFileNameFile);
			}

			this.session.setAttribute(TIPOS_DOCUMENTO_IMPRESSAO_ENCALHE, tiposDocumentoImpressao);
			this.session.setAttribute(CONF_IMPRESSAO_ENCALHE_COTA, dtoDoc);
			this.session.setAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA, mapFileNameFile);
			
		} catch (ValidacaoException e) {
			
			if(e.getValidacao() != null){
				throw new Exception(e.getValidacao().getListaMensagens().get(0));
			}
			
		}catch (Exception e) {
			
			throw new Exception("Cobrança gerada. Erro ao gerar arquivo(s) de cobrança - " + e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void imprimirDocumentosCobranca(String tipo_documento_impressao_encalhe) throws IOException{
		
		Map<String, byte[]> arquivos = (Map<String, byte[]>) this.session.getAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA);
		
		if(arquivos != null && !arquivos.isEmpty()) {
			
			byte[] bs = arquivos.get(tipo_documento_impressao_encalhe);
			
			if(bs != null) {
				
				Map<String, Object> dados = new HashMap<String, Object>();
				
				if(tipo_documento_impressao_encalhe.equals(TipoDocumentoConferenciaEncalhe.SLIP_TXT.name())){
					dados.put("resultado", new String(arquivos.get(tipo_documento_impressao_encalhe)));
				}else{
					dados.put("resultado", Base64.encodeBytes(arquivos.get(tipo_documento_impressao_encalhe)));	
				}
				
				dados.put("tipo_documento_impressao_encalhe", tipo_documento_impressao_encalhe);
				this.result.use(CustomJson.class).from(dados).serialize();
			}else{
				this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.ERROR, "Cenário de impressão não tratado, favor contatar a área de sistemas."));
			}
			
		} else {
			
			this.result.use(Results.nothing());
		}
	}

	@Post
	public void veificarCobrancaGerada(){
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada.");
		}
		
		if (info.getCota() == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe a Cota.");
		}
		
		List<Long> idsCota = new ArrayList<>();
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
	
	private void limparIdsTemporarios(List<ConferenciaEncalheDTO> listaConferenciaEncalheDTO) {
		
		for (ConferenciaEncalheDTO dto : listaConferenciaEncalheDTO){
			
			if (dto.getIdConferenciaEncalhe() < 0){
				
				dto.setIdConferenciaEncalhe(null);
			}
		}		
	}
	
	private List<ConferenciaEncalheDTO> obterCopiaListaConferenciaEncalheCota(List<ConferenciaEncalheDTO> oldListaConferenciaEncalheCota) {
		
		List<ConferenciaEncalheDTO> newListaConferenciaEncalheCota = new ArrayList<ConferenciaEncalheDTO>();
		
		for(ConferenciaEncalheDTO conf : oldListaConferenciaEncalheCota) {
		
			try {
				
				newListaConferenciaEncalheCota.add((ConferenciaEncalheDTO)BeanUtils.cloneBean(conf));
			
			} catch (Exception e) {
			
				throw new ValidacaoException(TipoMensagem.ERROR, "Falha na execução do sistema.");
			}
		}
		
		return newListaConferenciaEncalheCota;
		
	}
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void finalizarConferencia(boolean indConferenciaContingencia) throws Exception {
		
		Date horaInicio = (Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA);
		
		DadosDocumentacaoConfEncalheCotaDTO dadosDocumentacaoConfEncalheCota = null;
		
		if (horaInicio != null){
		
			ControleConferenciaEncalheCota controleConfEncalheCota = new ControleConferenciaEncalheCota();
			controleConfEncalheCota.setDataInicio(horaInicio);
			
			InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
			
			if (info == null){
				throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada.");
			}
			
			controleConfEncalheCota.setCota(info.getCota());
			controleConfEncalheCota.setId(info.getIdControleConferenciaEncalheCota());
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Map<String, Object> dadosNotaFiscal = (Map) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA);
			
			NotaFiscalEntradaCota notaFiscal = null;
			
			if(dadosNotaFiscal!=null) {
				
				notaFiscal = new NotaFiscalEntradaCota();
				
				notaFiscal.setNumero((Long) dadosNotaFiscal.get("numero"));
				notaFiscal.setSerie((String) dadosNotaFiscal.get("serie"));
				notaFiscal.setDataEmissao( DateUtil.parseDataPTBR((String) dadosNotaFiscal.get("dataEmissao")));
				notaFiscal.setChaveAcesso((String) dadosNotaFiscal.get("chaveAcesso"));
				notaFiscal.setValorProdutos((BigDecimal) dadosNotaFiscal.get("valorProdutos"));
				
			}
			
			List<NotaFiscalEntradaCota> notaFiscalEntradaCotas = new ArrayList<NotaFiscalEntradaCota>();
			notaFiscalEntradaCotas.add(notaFiscal);
			controleConfEncalheCota.setNotaFiscalEntradaCota(notaFiscalEntradaCotas);
			
			Box boxEncalhe = new Box();
			boxEncalhe.setId(conferenciaEncalheSessionScopeAttr.getIdBoxLogado());
			
			controleConfEncalheCota.setBox(boxEncalhe);
			
			List<ConferenciaEncalheDTO> listaConferenciaEncalheCotaToSave = 
					obterCopiaListaConferenciaEncalheCota(this.getListaConferenciaEncalheFromSession());
			
			limparIdsTemporarios(listaConferenciaEncalheCotaToSave);
			
			dadosDocumentacaoConfEncalheCota = 
					
					this.conferenciaEncalheService.finalizarConferenciaEncalhe(
							controleConfEncalheCota, 
							listaConferenciaEncalheCotaToSave, 
							this.getSetConferenciaEncalheExcluirFromSession(), 
							this.getUsuarioLogado(),
							indConferenciaContingencia);
			
			this.session.removeAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
			
			Long idControleConferenciaEncalheCota = dadosDocumentacaoConfEncalheCota.getIdControleConferenciaEncalheCota();
			
			this.getInfoConferenciaSession().setIdControleConferenciaEncalheCota(idControleConferenciaEncalheCota);
				
			try {
				this.gerarDocumentoConferenciaEncalhe(dadosDocumentacaoConfEncalheCota);
			} catch (Exception e){
				throw new Exception("Erro ao gerar documentos da conferência de encalhe - " + e.getMessage());
			}
			
			Map<String, Object> dados = new HashMap<String, Object>();
			
			dados.put("tipoMensagem", TipoMensagem.SUCCESS);
			dados.put(TIPOS_DOCUMENTO_IMPRESSAO_ENCALHE, session.getAttribute(TIPOS_DOCUMENTO_IMPRESSAO_ENCALHE));
			
			if(dadosDocumentacaoConfEncalheCota.getMsgsGeracaoCobranca()!=null) {
				
				dados.put("listaMensagens", dadosDocumentacaoConfEncalheCota.getMsgsGeracaoCobranca().getListaMensagens());
				
			} else {

				String msgSucess = "";
				
				if (listaConferenciaEncalheCotaToSave == null || listaConferenciaEncalheCotaToSave.isEmpty()){
					msgSucess = "Operação efetuada com sucesso. Nenhum ítem encalhado, total cobrado.";
				} else {
					msgSucess = "Operação efetuada com sucesso.";
				}
				
				dados.put("listaMensagens", 	new String[]{msgSucess});
				
			}
			

			dados.put("indGeraDocumentoConfEncalheCota", dadosDocumentacaoConfEncalheCota.isIndGeraDocumentacaoConferenciaEncalhe());
			
			limparDadosSessaoConferenciaEncalheCotaFinalizada();
			
			this.result.use(CustomMapJson.class).put("result", dados).serialize();
			
		} else {
			
			this.result.use(Results.json()).from(
					new ValidacaoVO(TipoMensagem.WARNING, "Conferência não iniciada."), "result").recursive().serialize();
		}
	}
	
	private void escreverArquivoParaResponse(byte[] arquivo, String nomeArquivo) throws IOException {
		
		this.httpResponse.setContentType("application/octet-stream");
		this.httpResponse.setHeader("Content-Disposition", 
									"attachment; filename="+nomeArquivo);
		
		OutputStream output = this.httpResponse.getOutputStream();
		
		output.write(arquivo);

		output.close();

		result.use(Results.nothing());
	}

	
	@Post
	public void pesquisarProdutoPorCodigoNome(String codigoNomeProduto){
		
		List<ProdutoEdicao> listaProdutoEdicao =
			this.produtoEdicaoService.obterProdutoPorCodigoNome(codigoNomeProduto, QUANTIDADE_MAX_REGISTROS);
		
		List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
		
		if (listaProdutoEdicao != null && !listaProdutoEdicao.isEmpty()){
			
			for (ProdutoEdicao produtoEdicao : listaProdutoEdicao){
				
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
	public void buscarDetalhesProduto(Long idConferenciaEncalhe){
		
		List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
		for (ConferenciaEncalheDTO dto : lista){
			
			if (dto.getIdConferenciaEncalhe().equals(idConferenciaEncalhe)){
				
				result.use(Results.json()).from(dto, "result").serialize();
				return;
			}
		}
		
		result.use(Results.json()).from("", "result").serialize();
	}
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void excluirConferencia(Long idConferenciaEncalhe){
		
		List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
		for (ConferenciaEncalheDTO dto : lista){
			
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
	public void gravarObservacaoConferecnia(Long idConferenciaEncalhe, String observacao){
		
		List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
		for (ConferenciaEncalheDTO dto : lista){
			
			if (dto.getIdConferenciaEncalhe().equals(idConferenciaEncalhe)){
				
				dto.setObservacao(observacao);
				break;
			}
		}
		
		indicarStatusConferenciaEncalheCotaAlterado();
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	
	private void validarCamposNotaFiscalEntrada(NotaFiscalEntradaCota notaFiscalEntradaCota) {
		
		if(notaFiscalEntradaCota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Dados da nota fiscal inválidos.");
		}
		
		List<String> mensagens = new ArrayList<String>();
		
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
	public void salvarNotaFiscal(NotaFiscalEntradaCota notaFiscal){
		
		validarCamposNotaFiscalEntrada(notaFiscal);
		
		Map<String, Object> dadosNotaFiscal = new HashMap<String, Object>();
		
		dadosNotaFiscal.put("numero", notaFiscal.getNumero());
		dadosNotaFiscal.put("serie", 	notaFiscal.getSerie());
		dadosNotaFiscal.put("dataEmissao", DateUtil.formatarDataPTBR(notaFiscal.getDataEmissao()));
		dadosNotaFiscal.put("chaveAcesso", notaFiscal.getChaveAcesso());
		dadosNotaFiscal.put("valorProdutos", notaFiscal.getValorProdutos());
		
		this.session.setAttribute(NOTA_FISCAL_CONFERENCIA, dadosNotaFiscal);
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	@Post
	public void carregarNotaFiscal(){
		
		@SuppressWarnings("unchecked")
		Map<String, Object> dadosNotaFiscal = (Map<String, Object>) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA);
		
		if(dadosNotaFiscal!=null) {

			this.result.use(CustomJson.class).from(dadosNotaFiscal).serialize();
			
		} else {

			this.result.use(Results.json()).from("","result").serialize();
			
		}
		
	}
	
	/**
	 * Verifica se o valor total da nota fiscal informada é igual
	 * ao valor de encalhe conferido na operação. 
	 * @throws Exception 
	 * 
	 */
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA_ALTERACAO)
	public void verificarValorTotalNotaFiscal(boolean indConferenciaContingencia) throws Exception {
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Map<String, Object> dadosNotaFiscal = (Map) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA);
		
		Map<String, Object> dadosMonetarios = new HashMap<String, Object>();
		
		this.calcularValoresMonetarios(dadosMonetarios);
		
		BigDecimal valorEncalhe = ((BigDecimal)dadosMonetarios.get("valorEncalhe"));
		
		if (	dadosNotaFiscal != null && 
				dadosNotaFiscal.get("valorProdutos") != null && 
				((BigDecimal)dadosNotaFiscal.get("valorProdutos")).compareTo(valorEncalhe) != 0){
			
			Map<String, Object> dadosResposta = new HashMap<String, Object>();
			
			dadosResposta.put("tipoMensagem", TipoMensagem.WARNING);
			dadosResposta.put("listaMensagens",
							  new String[]{"Valor total do encalhe difere do valor da nota informada."});
			
			this.result.use(CustomMapJson.class).put("result", dadosResposta).serialize();
			
		}  else {
			
			this.finalizarConferencia(indConferenciaContingencia);
			
		}
	}
	
	/**
	 * Verifica se o valor total de chamada encalhe informado é igual
	 * ao valor de encalhe conferido na operação. 
	 * 
	 * @param valorCEInformado
	 * @param qtdCEInformado
	 */
	@Post
	public void verificarValorTotalCE(BigDecimal valorCEInformado, BigInteger qtdCEInformado) {

		Map<String, Object> resultadoValidacao = new HashMap<String, Object>();
		
		TipoContabilizacaoCE tipoContabilizacaoCE = conferenciaEncalheService.obterTipoContabilizacaoCE();
		
		if(tipoContabilizacaoCE == null) {
			resultadoValidacao.put("valorCEInformadoValido", true);
			this.result.use(CustomJson.class).from(resultadoValidacao).serialize();
			return;
		}
		
		if(TipoContabilizacaoCE.VALOR.equals(tipoContabilizacaoCE)) {
			
			if (valorCEInformado == null || BigDecimal.ZERO.compareTo(valorCEInformado) >= 0 ){
				resultadoValidacao.put("valorCEInformadoValido", false);
				resultadoValidacao.put("mensagemConfirmacao", "Valor CE jornaleiro informado inválido. Deseja continuar?");
				this.result.use(CustomJson.class).from(resultadoValidacao).serialize();
				return;
			} 
			
		} else {
			
			if (qtdCEInformado == null || BigInteger.ZERO.compareTo(qtdCEInformado) >= 0 ){
				
				resultadoValidacao.put("valorCEInformadoValido", false);
				resultadoValidacao.put("mensagemConfirmacao", "Qtde CE jornaleiro informada inválido. Deseja continuar?");
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
	 * Compara se o valor de qtde de itens de encalhe apontado pelo jornaleiro
	 * é igual ao contabilizado na operação de conferência de encalhe.
	 * 
	 * @param valorTotalCEQuantidade
	 */

	private void comparValorTotalCEQuantidade(BigInteger valorTotalCEQuantidade) {
		
		Map<String, Object> resultadoValidacao = new HashMap<String, Object>();
		
		BigInteger qtdeItensConferenciaEncalhe = obterQtdeItensConferenciaEncalhe();

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
	 * Compara se o valor monetario de encalhe apontado pelo jornaleiro
	 * é igual ao contabilizado na operação de conferência de encalhe.
	 * 
	 * @param valorTotalCEMonetario
	 */
	private void comparValorTotalCEMonetario(BigDecimal valorTotalCEMonetario) {
		
		Map<String, Object> resultadoValidacao = new HashMap<String, Object>();
		
		Map<String, Object> valoresMonetarios = new HashMap<String, Object>();
		
		this.calcularValoresMonetarios(valoresMonetarios);
		
		BigDecimal valorEncalhe = ((BigDecimal) valoresMonetarios.get("valorEncalhe"));

		if (valorEncalhe.compareTo(valorTotalCEMonetario)!=0){

			resultadoValidacao.put("valorCEInformadoValido", false);
			
			resultadoValidacao.put("mensagemConfirmacao", "Valor total do encalhe difere do valor CE jornaleiro informado, Deseja continuar?");
			
			this.result.use(CustomJson.class).from(resultadoValidacao).serialize();
			
		} else {

			resultadoValidacao.put("valorCEInformadoValido", true);
			
			this.result.use(CustomJson.class).from(resultadoValidacao).serialize();

		}
		
	}

	@Post
	public void pesquisarProdutoEdicaoPorId(Long idProdutoEdicao){
		
		
		Integer numeroCota = this.getNumeroCotaFromSession();
		
		try {
			ProdutoEdicaoDTO p = 
					this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(numeroCota, idProdutoEdicao);
			
			Map<String, Object> dados = new HashMap<String, Object>();
			
			if (p != null){
				
				dados.put("numeroEdicao", p.getNumeroEdicao());
				dados.put("precoVenda", p.getPrecoVenda());
				dados.put("desconto", p.getDesconto());
			}
			
			this.result.use(CustomJson.class).from(dados).serialize();
			
		} catch (ChamadaEncalheCotaInexistenteException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe deste produto para essa cota.");
		} catch (EncalheRecolhimentoParcialException e) {
			
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
	
	private void limparDadosSessaoConferenciaEncalheCotaFinalizada() {
		

		this.session.removeAttribute(NUMERO_COTA);
		this.session.removeAttribute(INFO_CONFERENCIA);
		this.session.removeAttribute(NOTA_FISCAL_CONFERENCIA);
		this.session.removeAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
		this.session.removeAttribute(HORA_INICIO_CONFERENCIA);
		this.session.removeAttribute(CONFERENCIA_ENCALHE_COTA_STATUS);
		
		indicarStatusConferenciaEncalheCotaSalvo();
		
	}
	
	private InfoConferenciaEncalheCota getInfoConferenciaSession() {
		
		return (InfoConferenciaEncalheCota) this.session.getAttribute(INFO_CONFERENCIA);
	}

	private BigInteger obterQtdeItensConferenciaEncalhe() {
	
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		BigInteger qtdItens = BigInteger.ZERO;
		
		if (info != null){
			
			if (info.getListaConferenciaEncalhe() != null){
				
				for (ConferenciaEncalheDTO conferenciaEncalheDTO : info.getListaConferenciaEncalhe()){
					
					BigInteger qtdExemplar = conferenciaEncalheDTO.getQtdExemplar() ==  null ? BigInteger.ZERO : conferenciaEncalheDTO.getQtdExemplar();
					
					qtdItens = qtdItens.add(qtdExemplar);
					
				}
				
			}
		}
				
		return qtdItens;
		
	}
	
	/**
	 * Carrega o mapa passado como parâmetro com o seguinte valores:
	 * 
	 * valorEncalhe  		=	total do encalhe conferido até o momento nesta operação.
	 * valorVendaDia 		=	valorReparte subtraído valorEncalhe.
	 * valorDebitoCredito 	=	Creditos e Debitos da Cota 
	 * valorPagar			= 	
	 * 
	 * @param dados
	 */
	private void calcularValoresMonetarios(Map<String, Object> dados){
		
		BigDecimal valorEncalhe = BigDecimal.ZERO;
		BigDecimal valorVendaDia = BigDecimal.ZERO;
		BigDecimal valorDebitoCredito = BigDecimal.ZERO;
		BigDecimal valorTotal = BigDecimal.ZERO;
		BigDecimal valorEncalheAtualizado = BigDecimal.ZERO;
		BigDecimal valorVendaDiaAtualizado = BigDecimal.ZERO;
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info != null){
			
			if (info.getListaConferenciaEncalhe() != null) {
			
				for (ConferenciaEncalheDTO conferenciaEncalheDTO : info.getListaConferenciaEncalhe()){
					
					BigDecimal precoCapa = conferenciaEncalheDTO.getPrecoCapa() == null ? BigDecimal.ZERO : conferenciaEncalheDTO.getPrecoCapa();
					
					BigDecimal desconto = conferenciaEncalheDTO.getDesconto() == null ? BigDecimal.ZERO : conferenciaEncalheDTO.getDesconto();
					
					BigDecimal qtdExemplar = conferenciaEncalheDTO.getQtdExemplar() == null ? BigDecimal.ZERO : new BigDecimal(conferenciaEncalheDTO.getQtdExemplar());
					
					valorTotal = precoCapa.multiply(new BigDecimal(conferenciaEncalheDTO.getQtdInformada()));
					
					valorEncalhe = valorEncalhe.add(precoCapa.subtract(desconto).multiply(qtdExemplar));
					
					valorEncalheAtualizado = valorEncalheAtualizado.add(precoCapa.subtract(desconto).multiply(
						new BigDecimal(conferenciaEncalheDTO.getQtdInformada()))
					);
				}
			}
			
			valorVendaDia = valorVendaDia.add(info.getReparte().subtract(valorEncalhe));
			valorVendaDiaAtualizado = valorVendaDiaAtualizado.add(info.getReparte().subtract(valorEncalheAtualizado));
			
			if (info.getListaDebitoCreditoCota() != null) {
			
				for (DebitoCreditoCotaDTO debitoCreditoCotaDTO : info.getListaDebitoCreditoCota()){
					
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
			
			dados.put("valorEncalhe", valorEncalhe);
			dados.put("valorVendaDia", valorVendaDia);
			dados.put("valorDebitoCredito", valorDebitoCredito);
			dados.put("valorPagar", valorPagar);
			dados.put("valorTotal", valorTotal);
			dados.put("valorPagarAtualizado", valorPagarAtualizado);
		}
		
	}
	
	/**
	 * Processa a quantidade informada pelo usuario, 
	 * validando quando um produto CROMO é informado. 
	 * 
	 * @param idProdutoEdicao
	 * @param conferenciaEncalheDTO
	 * @param quantidade - quantidade informada pelo usuario (EX: 100 ou 100e);
	 * 
	 * @return quantidade
	 */
	private BigInteger processarQtdeExemplar(Long idProdutoEdicao,
			ConferenciaEncalheDTO conferenciaEncalheDTO, String quantidade, boolean indConferenciaContingencia) {
		
		Long pacotePadrao = (long) conferenciaEncalheDTO.getPacotePadrao();
		
		boolean quantidadeInformadaEmExemplares = false; 
		
		ProdutoEdicao produtoEdicao = this.produtoEdicaoService.obterProdutoEdicao(idProdutoEdicao, false);
		
		GrupoProduto grupoProduto = produtoEdicao.getProduto().getTipoProduto().getGrupoProduto();
		
		if(quantidade.contains("e")) {
			quantidade = quantidade.replace("e", "");
			quantidadeInformadaEmExemplares = true;
		}
		
		BigInteger qtd = BigInteger.ZERO;
		
		try {
			qtd = BigInteger.valueOf(Long.parseLong(quantidade));
		}catch(Exception e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Quantidade informada inválida"));
		}
		
		if (GrupoProduto.CROMO.equals(grupoProduto)) {
			if (!quantidadeInformadaEmExemplares && conferenciaEncalheDTO.isParcial()) {
				qtd = qtd.divide(BigInteger.valueOf(pacotePadrao));
			}
		}
		
		
		ConferenciaEncalheDTO conferenciaEncalheDTONaoValidado = null;
		try {
			conferenciaEncalheDTONaoValidado = (ConferenciaEncalheDTO)BeanUtils.cloneBean(conferenciaEncalheDTO);
			conferenciaEncalheDTONaoValidado.setQtdExemplar(qtd);
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha ao validar quantidade de itens de encalhe.");
		} 
		conferenciaEncalheService.validarQtdeEncalheExcedeQtdeReparte(
				conferenciaEncalheDTONaoValidado, getNumeroCotaFromSession(), null, indConferenciaContingencia);
		
		return  qtd;
	}

	private ConferenciaEncalheDTO criarConferenciaEncalhe(ProdutoEdicaoDTO produtoEdicao, String quantidade, 
														  boolean adicionarGrid, boolean indConferenciaContingencia) {
		
		ConferenciaEncalheDTO conferenciaEncalheDTO = new ConferenciaEncalheDTO();
		
		Long idTemporario = obterIdTemporario();
		
		conferenciaEncalheDTO.setDia(produtoEdicao.getDia());
		conferenciaEncalheDTO.setIdConferenciaEncalhe(idTemporario);
		conferenciaEncalheDTO.setCodigo(produtoEdicao.getCodigoProduto());
		conferenciaEncalheDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
		conferenciaEncalheDTO.setCodigoSM(produtoEdicao.getSequenciaMatriz());
		conferenciaEncalheDTO.setIdProdutoEdicao(produtoEdicao.getId());
		conferenciaEncalheDTO.setNomeProduto(produtoEdicao.getNomeProduto());
		conferenciaEncalheDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		
		conferenciaEncalheDTO.setDesconto(produtoEdicao.getDesconto());
		conferenciaEncalheDTO.setPrecoComDesconto(produtoEdicao.getPrecoComDesconto());
		conferenciaEncalheDTO.setPrecoCapa(produtoEdicao.getPrecoVenda());
		
		conferenciaEncalheDTO.setPrecoCapaInformado(produtoEdicao.getPrecoVenda());
		
		if (produtoEdicao.getTipoChamadaEncalhe() != null) {
			conferenciaEncalheDTO.setTipoChamadaEncalhe(produtoEdicao.getTipoChamadaEncalhe().name());
		}
		
		conferenciaEncalheDTO.setDataRecolhimento(produtoEdicao.getDataRecolhimentoDistribuidor());
		
		conferenciaEncalheDTO.setParcial(produtoEdicao.isParcial());
		
		
		if (quantidade != null){
			
			BigInteger qtd = this.processarQtdeExemplar(produtoEdicao.getId(), conferenciaEncalheDTO, quantidade, indConferenciaContingencia);
			
			conferenciaEncalheDTO.setQtdExemplar(qtd);
			conferenciaEncalheDTO.setQtdInformada(qtd);
		} else {
			
			conferenciaEncalheDTO.setQtdExemplar(BigInteger.ONE);
			conferenciaEncalheDTO.setQtdInformada(BigInteger.ONE);

		}
		
		
		conferenciaEncalheDTO.setValorTotal(produtoEdicao.getPrecoVenda().subtract(produtoEdicao.getDesconto()).multiply(new BigDecimal( conferenciaEncalheDTO.getQtdExemplar()) ));
		
		conferenciaEncalheDTO.setNomeEditor(produtoEdicao.getEditor());
		
		conferenciaEncalheDTO.setNomeFornecedor(produtoEdicao.getNomeFornecedor());
		
		conferenciaEncalheDTO.setChamadaCapa(produtoEdicao.getChamadaCapa());
		
		if (adicionarGrid){
			
			List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
			lista.add(conferenciaEncalheDTO);
			this.setListaConferenciaEncalheToSession(lista);
		}
		
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
		obterTableModelDebitoCreditoCota(List<DebitoCreditoCotaDTO> listaDebitoCreditoCota) {

		TableModel<CellModelKeyValue<DebitoCreditoCotaDTO>> tableModelDebitoCreditoCota = 
				new TableModel<CellModelKeyValue<DebitoCreditoCotaDTO>>();
		
		tableModelDebitoCreditoCota.setRows(CellModelKeyValue.toCellModelKeyValue(listaDebitoCreditoCota));
		tableModelDebitoCreditoCota.setTotal((listaDebitoCreditoCota!= null) ? listaDebitoCreditoCota.size() : 0);
		tableModelDebitoCreditoCota.setPage(1);
		
		return tableModelDebitoCreditoCota;
	}
	
	private List<ConferenciaEncalheDTO> getListaConferenciaEncalheFromSession() {
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada.");
		}
		
		List<ConferenciaEncalheDTO> lista = info.getListaConferenciaEncalhe();
		
		if (lista == null){
			
			lista = new ArrayList<ConferenciaEncalheDTO>();
		}
		
		return lista;
	}

	private void setListaConferenciaEncalheToSession(List<ConferenciaEncalheDTO> listaConferenciaEncalheDTO) {
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info == null){
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada.");
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
		
		Integer numeroCota = (Integer) this.session.getAttribute(NUMERO_COTA);
		
		if (numeroCota == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Informe uma cota.");
		}
		
		return numeroCota;
	}
	
	private void alterarBoxUsuario(Long idBox) {
		
		Box box = this.boxService.buscarPorId(idBox);
		
		Usuario usuarioLogado = this.getUsuarioLogado();
		
		usuarioLogado.setBox(box);
		
		usuarioService.salvar(usuarioLogado);
	}
	
	private void gerarSlipMatricialFinalizacaoEncalhe(
			ArrayList<String> tiposDocumentoImpressao,
			Long idControleConferenciaEncalheCota,
			Map<String, byte[]> mapFileNameFile) {
		
		//Imprime apenas SLIP txt, dados para matricial.
		byte[] slipMatricial = conferenciaEncalheService.gerarSlipMatricial(idControleConferenciaEncalheCota, true);
		mapFileNameFile.put(TipoDocumentoConferenciaEncalhe.SLIP_TXT.name(), slipMatricial);
		tiposDocumentoImpressao.add(TipoDocumentoConferenciaEncalhe.SLIP_TXT.name());
	}

	private void gerarSlipPDFFinalizacaoEncalhe(
			ArrayList<String> tiposDocumentoImpressao,
			Long idControleConferenciaEncalheCota, List<byte[]> arquivos,
			Map<String, byte[]> mapFileNameFile) {
		arquivos.add(conferenciaEncalheService.gerarDocumentosConferenciaEncalhe(
				idControleConferenciaEncalheCota, 
				null, 
				TipoDocumentoConferenciaEncalhe.SLIP_PDF));
		
	}

	private void gerarBoletoFinalizacaoEncalhe(
			DadosDocumentacaoConfEncalheCotaDTO dtoDoc,
			ArrayList<String> tiposDocumentoImpressao,
			Long idControleConferenciaEncalheCota, List<byte[]> arquivos,
			Map<String, byte[]> mapFileNameFile) {
		
		for(String nossoNumero : dtoDoc.getListaNossoNumero().keySet()) {

			arquivos.add(conferenciaEncalheService.gerarDocumentosConferenciaEncalhe(
					idControleConferenciaEncalheCota, 
					nossoNumero,
					TipoDocumentoConferenciaEncalhe.BOLETO_SLIP));
			
		}
	}
}
