package br.com.abril.nds.controllers.devolucao;

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
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheFinalizadaException;
import br.com.abril.nds.service.exception.EncalheRecolhimentoParcialException;
import br.com.abril.nds.service.exception.EncalheSemPermissaoSalvarException;
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

@Resource
@Path(value="/devolucao/conferenciaEncalhe")
public class ConferenciaEncalheController extends BaseController {
	
	private static final String DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA = "dadosDocumentacaoConfEncalheCota";
	
	private static final String ID_BOX_LOGADO = "idBoxLogado";
	
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
	private HttpServletResponse httpResponse;
	
	@Path("/")
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA)
	public void index() {
		limparDadosSessao();
		carregarComboBoxEncalhe();
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
		
		this.limparDadosSessao();
		
		if (idBox != null){
		
			this.session.setAttribute(ID_BOX_LOGADO, idBox);
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
	 * Verifica se a cota cuja conferência esta sendo realizada esta salva ou com 
	 * dados em session alterados pelo usuário
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
		
		limparDadosSessaoManterBoxLogado();
		
		this.session.setAttribute(HORA_INICIO_CONFERENCIA, new Date());
		
		if (this.session.getAttribute(ID_BOX_LOGADO) == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Box de recolhimento não informado.");
		}
		
		try {
			
			this.conferenciaEncalheService.verificarChamadaEncalheCota(numeroCota);
			
		} catch (ConferenciaEncalheExistenteException e) {
			
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "REABERTURA"), "result").recursive().serialize();
		
			return;
		
		} catch (ChamadaEncalheCotaInexistenteException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe para essa cota.");
		}
		
		this.session.setAttribute(NUMERO_COTA, numeroCota);
		
		this.result.use(Results.json()).from("").serialize();
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
	public void pesquisarProdutoEdicao(String codigoBarra, Integer sm, Long idProdutoEdicao, Long codigoAnterior, String quantidade){
		
		this.verificarInicioConferencia();
		
		ProdutoEdicaoDTO produtoEdicao = null;
		
		ConferenciaEncalheDTO conferenciaEncalheDTO = null;
		
		Integer numeroCota = this.getNumeroCotaFromSession();
		
		List<ConferenciaEncalheDTO> listaConfSessao = this.getListaConferenciaEncalheFromSession();
		
		try {
			if (codigoBarra != null && !codigoBarra.trim().isEmpty()){
				
				for (ConferenciaEncalheDTO dto : listaConfSessao){
					
					if (codigoBarra.equals(dto.getCodigoDeBarras())){
						
						conferenciaEncalheDTO = dto;
						break;
					}
				}
				
				if (conferenciaEncalheDTO == null){
				
					produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorCodigoDeBarras(numeroCota, codigoBarra);
				}
			} else if (sm != null){
				
				for (ConferenciaEncalheDTO dto : listaConfSessao){
					
					if (sm.equals(dto.getCodigoSM())){
						
						conferenciaEncalheDTO = dto;
						break;
					}
				}
				
				if (conferenciaEncalheDTO == null){
				
					produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorSM(numeroCota, sm);
				}
			} else if (idProdutoEdicao != null){
				
				for (ConferenciaEncalheDTO dto : listaConfSessao){
					
					if (idProdutoEdicao.equals(dto.getIdProdutoEdicao())){
						
						conferenciaEncalheDTO = dto;
						break;
					}
				}
				
				if (conferenciaEncalheDTO == null){
				
					produtoEdicao = this.conferenciaEncalheService.pesquisarProdutoEdicaoPorId(numeroCota, idProdutoEdicao);
				}
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Informe código de barras, SM ou código.");
			}
			
		} catch(ChamadaEncalheCotaInexistenteException e){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe deste produto para essa cota.");
		
		} catch(EncalheRecolhimentoParcialException e) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe para produto parcial na data operação.");
			
		}
		
		if (conferenciaEncalheDTO == null && produtoEdicao == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto Edição não encontrado.");
		} else if (conferenciaEncalheDTO == null){
			
			conferenciaEncalheDTO = this.criarConferenciaEncalhe(produtoEdicao, quantidade, false);
		}
		
		if (codigoAnterior != null && quantidade != null){
			
			conferenciaEncalheDTO = this.atualizarQuantidadeConferida(codigoAnterior, quantidade, produtoEdicao, null);
		}
		
		
		indicarStatusConferenciaEncalheCotaAlterado();

		
		this.result.use(Results.json()).from(conferenciaEncalheDTO, "result").serialize();
	}
	
	@Post
	public void adicionarProdutoConferido(Long idProdutoEdicao, String quantidade, Boolean juramentada) {
		
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
		
		this.atualizarQuantidadeConferida(idProdutoEdicao, quantidade, produtoEdicao, juramentada);
		
		this.carregarListaConferencia(null, false, false);
		
	}
	
	@Post
	public void atualizarValores(Long idConferencia, Long qtdExemplares, Boolean juramentada, BigDecimal valorCapa){
		
		List<ConferenciaEncalheDTO> listaConferencia = this.getListaConferenciaEncalheFromSession();
		
		if(qtdExemplares == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade de exemplares inválida.");
		}
		
		ConferenciaEncalheDTO conf = null;
		
		if (idConferencia != null){
				
			for (ConferenciaEncalheDTO dto : listaConferencia){
				
				if (dto.getIdConferenciaEncalhe().equals(idConferencia)){
					
					dto.setQtdExemplar(BigInteger.valueOf(qtdExemplares));
					
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
		boxEncalhe.setId((Long) this.session.getAttribute(ID_BOX_LOGADO));
		
		controleConfEncalheCota.setBox(boxEncalhe);
		
		
		List<ConferenciaEncalheDTO> listaConferenciaEncalheCotaToSave = 
				obterCopiaListaConferenciaEncalheCota(this.getListaConferenciaEncalheFromSession());
		
		limparIdsTemporarios(listaConferenciaEncalheCotaToSave);
		
		try {
			
			Long idControleConferenciaEncalheCota = this.conferenciaEncalheService.salvarDadosConferenciaEncalhe(
					controleConfEncalheCota, 
					listaConferenciaEncalheCotaToSave, 
					this.getSetConferenciaEncalheExcluirFromSession(), 
					this.getUsuarioLogado());
			
			recarregarInfoConferenciaEncalheCotaEmSession(getNumeroCotaFromSession(), indConferenciaContingencia);
			
			this.session.removeAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
			
			this.getInfoConferenciaSession().setIdControleConferenciaEncalheCota(idControleConferenciaEncalheCota);
			
			StatusConferenciaEncalheCota statusConf = obterStatusConferenciaEncalheCotaFromSession();
			statusConf.setIndConferenciaEncalheCotaSalva(true);
			
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

	public void gerarDocumentoConferenciaEncalhe(DadosDocumentacaoConfEncalheCotaDTO dtoDoc) throws Exception {
		
		try {
				
			Long idControleConferenciaEncalheCota = dtoDoc.getIdControleConferenciaEncalheCota();
			
			boolean isUtilizaBoleto = dtoDoc.isUtilizaSlipBoleto();
			
			boolean isUtilizaSlip = true;//TODO: voltar apos testes...dtoDoc.isUtilizaSlip();
			
			List<byte[]> arquivos = new ArrayList<byte[]>();
			
			if (isUtilizaSlip) {
				
				arquivos.add(conferenciaEncalheService.gerarDocumentosConferenciaEncalhe(
							idControleConferenciaEncalheCota, 
							null, 
							TipoDocumentoConferenciaEncalhe.SLIP));
			}
			
			if(isUtilizaBoleto) {
				
				for(String nossoNumero : dtoDoc.getListaNossoNumero()) {

					arquivos.add(conferenciaEncalheService.gerarDocumentosConferenciaEncalhe(
							idControleConferenciaEncalheCota, 
							nossoNumero,
							TipoDocumentoConferenciaEncalhe.BOLETO_OU_RECIBO));
					
				}
				
			} 

			
			
			byte[] retorno = PDFUtil.mergePDFs(arquivos);
			
			this.session.setAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA, retorno);
			
		} catch (Exception e) {
			
			throw new Exception("Cobrança gerada. Erro ao gerar arquivo(s) de cobrança - " + e.getMessage(), 
					e);
		}
	}
	
	public void imprimirDocumentosCobranca() throws IOException{
		
		Object docs = this.session.getAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA);
		
		if (docs instanceof byte[]){
			
			this.escreverArquivoParaResponse((byte[]) docs, "arquivosCobranca");
			
			this.session.removeAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA);
			
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
			boxEncalhe.setId((Long) this.session.getAttribute(ID_BOX_LOGADO));
			
			controleConfEncalheCota.setBox(boxEncalhe);
			
			List<ConferenciaEncalheDTO> listaConferenciaEncalheCotaToSave = 
					obterCopiaListaConferenciaEncalheCota(this.getListaConferenciaEncalheFromSession());
			
			limparIdsTemporarios(listaConferenciaEncalheCotaToSave);
			
			dadosDocumentacaoConfEncalheCota = 
					
					this.conferenciaEncalheService.finalizarConferenciaEncalhe(
							controleConfEncalheCota, 
							listaConferenciaEncalheCotaToSave, 
							this.getSetConferenciaEncalheExcluirFromSession(), 
							this.getUsuarioLogado());
			
			this.session.removeAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
			
			if(dadosDocumentacaoConfEncalheCota!=null ) {
				Long idControleConferenciaEncalheCota = dadosDocumentacaoConfEncalheCota.getIdControleConferenciaEncalheCota();
				this.getInfoConferenciaSession().setIdControleConferenciaEncalheCota(idControleConferenciaEncalheCota);
			}
				
			
			if(dadosDocumentacaoConfEncalheCota!=null) {
				
				try {
					
					this.gerarDocumentoConferenciaEncalhe(dadosDocumentacaoConfEncalheCota);
					
				} catch (Exception e){
					
					throw new Exception("Cobrança efetuada, erro ao gerar arquivo(s) de cobrança - " + e.getMessage());
				}

				
			}
			
			
			Map<String, Object> dados = new HashMap<String, Object>();
			
			dados.put("tipoMensagem", TipoMensagem.SUCCESS);
			
			dados.put("listaMensagens", 	new String[]{"Operação efetuada com sucesso."});

			dados.put("indGeraDocumentoConfEncalheCota", dadosDocumentacaoConfEncalheCota.isIndGeraDocumentacaoConferenciaEncalhe());
			
			limparDadosSessaoConferenciaEncalheCotaFinalizada();
			
			this.result.use(CustomMapJson.class).put("result", dados).serialize();
			
		} else {
			
			this.result.use(Results.json()).from(
					new ValidacaoVO(TipoMensagem.WARNING, "Conferência não iniciada."), "result").recursive().serialize();
		}
	}
	
	private void escreverArquivoParaResponse(byte[] arquivo, String nomeArquivo) throws IOException {
		
		this.httpResponse.setContentType("application/pdf");
		
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename="+nomeArquivo +".pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		
		output.write(arquivo);

		httpResponse.getOutputStream().close();
		
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
	 */
	@Post
	public void verificarValorTotalCE(BigDecimal valorCEInformado) {
		
		Map<String, Object> resultadoValidacao = new HashMap<String, Object>();
		
		if (valorCEInformado == null || BigDecimal.ZERO.compareTo(valorCEInformado) >= 0 ){
			
			resultadoValidacao.put("valorCEInformadoValido", false);
			
			resultadoValidacao.put("mensagemConfirmacao", "Valor CE jornaleiro informado inválido. Deseja continuar?");
			
			this.result.use(CustomJson.class).from(resultadoValidacao).serialize();

		} else {

			TipoContabilizacaoCE tipoContabilizacaoCE = conferenciaEncalheService.obterTipoContabilizacaoCE();
			
			if (TipoContabilizacaoCE.VALOR.equals(tipoContabilizacaoCE)) {
				
				this.comparValorTotalCEMonetario(valorCEInformado);
				
			} else {
				
				this.comparValorTotalCEQuantidade(valorCEInformado);
				
			}
			
		}
		
	}
	
	/**
	 * Compara se o valor de qtde de itens de encalhe apontado pelo jornaleiro
	 * é igual ao contabilizado na operação de conferência de encalhe.
	 * 
	 * @param valorTotalCEQuantidade
	 */

	private void comparValorTotalCEQuantidade(BigDecimal valorTotalCEQuantidade) {
		
		Map<String, Object> resultadoValidacao = new HashMap<String, Object>();
		
		BigInteger qtde = BigInteger.valueOf(valorTotalCEQuantidade.longValue());
		
		BigInteger qtdeItensConferenciaEncalhe = obterQtdeItensConferenciaEncalhe();

		if (qtdeItensConferenciaEncalhe.compareTo(qtde)!=0){

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
		
		this.session.removeAttribute(ID_BOX_LOGADO);
		this.session.removeAttribute(NUMERO_COTA);
		this.session.removeAttribute(INFO_CONFERENCIA);
		this.session.removeAttribute(NOTA_FISCAL_CONFERENCIA);
		this.session.removeAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
		this.session.removeAttribute(HORA_INICIO_CONFERENCIA);
		this.session.removeAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA);
		this.session.removeAttribute(CONFERENCIA_ENCALHE_COTA_STATUS);
		
		indicarStatusConferenciaEncalheCotaSalvo();
	}
	
	private void limparDadosSessaoManterBoxLogado() {
		
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
		
		InfoConferenciaEncalheCota info = this.getInfoConferenciaSession();
		
		if (info != null){
			
			if (info.getListaConferenciaEncalhe() != null) {
			
				for (ConferenciaEncalheDTO conferenciaEncalheDTO : info.getListaConferenciaEncalhe()){
					
					BigDecimal precoCapa = conferenciaEncalheDTO.getPrecoCapa() == null ? BigDecimal.ZERO : conferenciaEncalheDTO.getPrecoCapa();
					
					BigDecimal desconto = conferenciaEncalheDTO.getDesconto() == null ? BigDecimal.ZERO : conferenciaEncalheDTO.getDesconto();
					
					BigDecimal qtdExemplar = conferenciaEncalheDTO.getQtdExemplar() == null ? BigDecimal.ZERO : new BigDecimal(conferenciaEncalheDTO.getQtdExemplar()); 
					
					valorEncalhe = valorEncalhe.add(precoCapa.subtract(desconto).multiply(qtdExemplar));
				}
			}
			
			valorVendaDia = valorVendaDia.add(info.getReparte().subtract(valorEncalhe));
			
			if (info.getListaDebitoCreditoCota() != null) {
			
				for (DebitoCreditoCotaDTO debitoCreditoCotaDTO : info.getListaDebitoCreditoCota()){
					
					if(debitoCreditoCotaDTO.getValor() == null) {
						continue;
					}
					
					if(OperacaoFinaceira.DEBITO.name().equals(debitoCreditoCotaDTO.getTipoLancamento())) {
						valorDebitoCredito = valorDebitoCredito.subtract(debitoCreditoCotaDTO.getValor());
					}
					
					if(OperacaoFinaceira.CREDITO.name().equals(debitoCreditoCotaDTO.getTipoLancamento())) {
						valorDebitoCredito = valorDebitoCredito.add(debitoCreditoCotaDTO.getValor());
					}
					
					
				}
			}
		}
		
		BigDecimal valorPagar = BigDecimal.ZERO;	
		
		if(BigDecimal.ZERO.compareTo(valorDebitoCredito)>0) {
			valorPagar = valorVendaDia.add(valorDebitoCredito.abs());
		} else {
			valorPagar = valorVendaDia.subtract(valorDebitoCredito.abs());
		}
		
		if (dados != null){
			
			dados.put("valorEncalhe", valorEncalhe);
			dados.put("valorVendaDia", valorVendaDia);
			dados.put("valorDebitoCredito", valorDebitoCredito);
			dados.put("valorPagar", valorPagar);
		}
		
	}
	
	/*
	 * Atualiza quantidade da conferencia ou cria um novo registro caso seja a primeira vez que se esta conferindo o produtoedicao
	 */
	private ConferenciaEncalheDTO atualizarQuantidadeConferida(Long codigoAnterior, String quantidade, ProdutoEdicaoDTO produtoEdicao, Boolean juramentada) {
		
		ConferenciaEncalheDTO conferenciaEncalheDTOSessao = null;
		
		if (codigoAnterior != null){
		
			//busca conferencia na sessão
			List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
			
			for (ConferenciaEncalheDTO dto : lista){
				
				if (dto.getIdProdutoEdicao().equals(codigoAnterior)){
					
					conferenciaEncalheDTOSessao = dto;
					break;
				}
			}
		}
		
		if (conferenciaEncalheDTOSessao != null){
			
			Long qtde = this.processarQtdeExemplar(produtoEdicao.getId(), conferenciaEncalheDTOSessao, quantidade);
			conferenciaEncalheDTOSessao.setQtdExemplar(BigInteger.valueOf(qtde));
			
		} else {
			
			conferenciaEncalheDTOSessao = this.criarConferenciaEncalhe(produtoEdicao, quantidade, true);
		}
		if (juramentada != null) {
			conferenciaEncalheDTOSessao.setJuramentada(juramentada);
		}
		
		indicarStatusConferenciaEncalheCotaAlterado();
		
		return conferenciaEncalheDTOSessao;
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
	private Long processarQtdeExemplar(Long idProdutoEdicao,
			ConferenciaEncalheDTO conferenciaEncalheDTO, String quantidade) {
		
				
		Long qtd = null;
		
		Long pacotePadrao = (long) conferenciaEncalheDTO.getPacotePadrao();
		
		boolean quantidadeInformadaEmExemplares = false; 
		
		ProdutoEdicao produtoEdicao = this.produtoEdicaoService.obterProdutoEdicao(idProdutoEdicao, false);
		
		GrupoProduto grupoProduto = produtoEdicao.getProduto().getTipoProduto().getGrupoProduto();
		
		if(quantidade.contains("e")) {
			quantidade = quantidade.replace("e", "");
			quantidadeInformadaEmExemplares = true;
		}
			
		try {
			qtd = Long.parseLong(quantidade);
		}catch(NumberFormatException e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Quantidade informada inválida"));
		}
		
		if (GrupoProduto.CROMO.equals(grupoProduto)) {
			if (!quantidadeInformadaEmExemplares && conferenciaEncalheDTO.isParcial()) {
				qtd = qtd/pacotePadrao;
			}
		}
		
		return qtd;
	}

	private ConferenciaEncalheDTO criarConferenciaEncalhe(ProdutoEdicaoDTO produtoEdicao, String quantidade, boolean adicionarGrid) {
		
		ConferenciaEncalheDTO conferenciaEncalheDTO = new ConferenciaEncalheDTO();
		
		Long idTemporario = obterIdTemporario();
		
		conferenciaEncalheDTO.setIdConferenciaEncalhe(idTemporario);
		conferenciaEncalheDTO.setCodigo(produtoEdicao.getCodigoProduto());
		conferenciaEncalheDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
		conferenciaEncalheDTO.setCodigoSM(produtoEdicao.getSequenciaMatriz());
		conferenciaEncalheDTO.setIdProdutoEdicao(produtoEdicao.getId());
		conferenciaEncalheDTO.setNomeProduto(produtoEdicao.getNomeProduto());
		conferenciaEncalheDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		
		conferenciaEncalheDTO.setPrecoCapa(produtoEdicao.getPrecoVenda());
		conferenciaEncalheDTO.setPrecoCapaInformado(produtoEdicao.getPrecoVenda());
		
		if (produtoEdicao.getTipoChamadaEncalhe() != null) {
			conferenciaEncalheDTO.setTipoChamadaEncalhe(produtoEdicao.getTipoChamadaEncalhe().name());
		}
		
		conferenciaEncalheDTO.setDataRecolhimento(produtoEdicao.getDataRecolhimentoDistribuidor());
		
		conferenciaEncalheDTO.setParcial(produtoEdicao.isParcial());
		
		
		if (quantidade != null){
			
			Long qtd = this.processarQtdeExemplar(produtoEdicao.getId(), conferenciaEncalheDTO, quantidade);
			
			conferenciaEncalheDTO.setQtdExemplar(BigInteger.valueOf(qtd));
			conferenciaEncalheDTO.setQtdInformada(BigInteger.valueOf(qtd));
		} else {
			
			conferenciaEncalheDTO.setQtdExemplar(BigInteger.ONE);
			conferenciaEncalheDTO.setQtdInformada(BigInteger.ONE);

		}
		
		conferenciaEncalheDTO.setDesconto(produtoEdicao.getDesconto());
		
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
	
}