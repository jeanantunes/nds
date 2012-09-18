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

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.dto.DadosDocumentacaoConfEncalheCotaDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConferenciaEncalheCota;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoContabilizacaoCE;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaCota;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.exception.ChamadaEncalheCotaInexistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheExistenteException;
import br.com.abril.nds.service.exception.ConferenciaEncalheFinalizadaException;
import br.com.abril.nds.service.exception.EncalheExcedeReparteException;
import br.com.abril.nds.service.exception.EncalheRecolhimentoParcialException;
import br.com.abril.nds.service.exception.EncalheSemPermissaoSalvarException;
import br.com.abril.nds.service.impl.ConferenciaEncalheServiceImpl.TipoDocumentoConferenciaEncalhe;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path(value="/devolucao/conferenciaEncalhe")
public class ConferenciaEncalheController {
	
	private static final String DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA = "dadosDocumentacaoConfEncalheCota";
	
	private static final String VALOR_ENCALHE_JORNALEIRO = "valorEncalheJornaleiro";
	
	private static final String ID_BOX_LOGADO = "idBoxLogado";
	
	private static final String INFO_CONFERENCIA = "infoCoferencia";
	
	private static final String SET_CONFERENCIA_ENCALHE_EXCLUIR = "listaConferenciaEncalheExcluir";
	
	private static final String NOTA_FISCAL_CONFERENCIA = "notaFiscalConferencia";
	
	private static final String HORA_INICIO_CONFERENCIA = "horaInicioConferencia";
	
	private static final String NUMERO_COTA = "numeroCotaConferenciaEncalhe";
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;

	@Path("/")
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONFERENCIA_ENCALHE_COTA)
	public void index() {
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
			
			this.result.use(CustomMapJson.class).put("boxes", mapBox).serialize();

			
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
	
	@Post
	public void verificarReabertura(Integer numeroCota){
		
		if (this.session.getAttribute(ID_BOX_LOGADO) == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Box de recolhimento não informado.");
		}
		
		this.session.setAttribute(HORA_INICIO_CONFERENCIA, new Date());
		
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
			
			infoConfereciaEncalheCota = 
					conferenciaEncalheService.obterInfoConferenciaEncalheCota(numeroCota, indConferenciaContingencia);
			
			this.session.setAttribute(INFO_CONFERENCIA, infoConfereciaEncalheCota);
			
			this.setListaConferenciaEncalheToSession(infoConfereciaEncalheCota.getListaConferenciaEncalhe());
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
		
		result.use(CustomMapJson.class).put("result", dados).serialize();
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
	public void pesquisarProdutoEdicao(String codigoBarra, Integer sm, Long idProdutoEdicao, Long codigoAnterior, Long quantidade){
		
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
		
		this.result.use(Results.json()).from(conferenciaEncalheDTO, "result").serialize();
	}
	
	@Post
	public void adicionarProdutoConferido(Long idProdutoEdicao, Long quantidade, Boolean juramentada) {
		
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
		
		this.result.use(CustomMapJson.class).put("result", dados == null ? "" : dados).serialize();
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
		
		this.result.use(CustomMapJson.class).put("result", dados == null ? "" : dados).serialize();
		
	}

	/**
	 * Salva os dados da conferência de encalhe.
	 */
	@Post
	public void salvarConferencia(){
		
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
		
		controleConfEncalheCota.setNotaFiscalEntradaCota(notaFiscal);
		
		Box boxEncalhe = new Box();
		boxEncalhe.setId((Long) this.session.getAttribute(ID_BOX_LOGADO));
		
		controleConfEncalheCota.setBox(boxEncalhe);
		
		List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
		
		for (ConferenciaEncalheDTO dto : lista){
			
			if (dto.getIdConferenciaEncalhe() < 0){
				
				dto.setIdConferenciaEncalhe(null);
			}
		}
		
		try {
			
			Long idControleConferenciaEncalheCota = this.conferenciaEncalheService.salvarDadosConferenciaEncalhe(
					controleConfEncalheCota, 
					this.getListaConferenciaEncalheFromSession(), 
					this.getSetConferenciaEncalheExcluirFromSession(), 
					this.getUsuarioLogado());
			
			this.session.removeAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
			
			this.getInfoConferenciaSession().setIdControleConferenciaEncalheCota(idControleConferenciaEncalheCota);
			
		} catch (EncalheSemPermissaoSalvarException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Somente conferência de produtos de chamadão podem ser salvos, finalize a operação para não perder os dados. ");
			
		} catch (ConferenciaEncalheFinalizadaException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Conferência não pode ser salvar, finalize a operação para não perder os dados.");
			
		} catch (EncalheExcedeReparteException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de encalhe está excedendo quantidade de reparte.");
			
		} 
		
		finally {
			
			this.atribuirIds(lista);
		}
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), "result").recursive().serialize();
	}
	
	private void atribuirIds(List<ConferenciaEncalheDTO> lista) {
		
		if (lista != null){
			for (ConferenciaEncalheDTO dto : lista){
				
				if (dto.getIdConferenciaEncalhe() == null){
				
					int id = (int) System.currentTimeMillis();
					
					if (id > 0){
						id *= -1;
					}
						
					dto.setIdConferenciaEncalhe(new Long(id));
				}
			}
		}
	}

	private void verificarInicioConferencia() {
		
		Date horaInicio = (Date) this.session.getAttribute(HORA_INICIO_CONFERENCIA);
		
		if (horaInicio == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Conferência não iniciada.");
		}
		
	}

	public void gerarSlip() throws IOException {
		
		DadosDocumentacaoConfEncalheCotaDTO dadosDocumentacaoConfEncalheCota = 
				(DadosDocumentacaoConfEncalheCotaDTO) this.session.getAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA);
		//TODO - Slip + Boleto
		byte[] slip = conferenciaEncalheService.gerarDocumentosConferenciaEncalhe(dadosDocumentacaoConfEncalheCota, TipoDocumentoConferenciaEncalhe.SLIP);
	
		escreverArquivoParaResponse(slip, "slip");
		
	}

	public void gerarBoletoOuRecibo() throws IOException {
		
		DadosDocumentacaoConfEncalheCotaDTO dadosDocumentacaoConfEncalheCota = 
				(DadosDocumentacaoConfEncalheCotaDTO) this.session.getAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA);
		
		byte[] boletoOuRecibo = conferenciaEncalheService.gerarDocumentosConferenciaEncalhe(dadosDocumentacaoConfEncalheCota, TipoDocumentoConferenciaEncalhe.BOLETO_OU_RECIBO);
	
		escreverArquivoParaResponse(boletoOuRecibo, "boletoOuRecibo");
		
	}

	
	@Post
	public void finalizarConferencia(){
		
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
			
			controleConfEncalheCota.setNotaFiscalEntradaCota(notaFiscal);
			
			Box boxEncalhe = new Box();
			boxEncalhe.setId((Long) this.session.getAttribute(ID_BOX_LOGADO));
			
			controleConfEncalheCota.setBox(boxEncalhe);
			
			List<ConferenciaEncalheDTO> lista = this.getListaConferenciaEncalheFromSession();
			
			for (ConferenciaEncalheDTO dto : lista){
				
				if (dto.getIdConferenciaEncalhe() < 0){
					
					dto.setIdConferenciaEncalhe(null);
				}
			}
			
			try {
				
				dadosDocumentacaoConfEncalheCota = 
						
						this.conferenciaEncalheService.finalizarConferenciaEncalhe(
								controleConfEncalheCota, 
								this.getListaConferenciaEncalheFromSession(), 
								this.getSetConferenciaEncalheExcluirFromSession(), 
								this.getUsuarioLogado());
				
				this.session.setAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA, dadosDocumentacaoConfEncalheCota);
				
				this.session.removeAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
				
				this.getInfoConferenciaSession().setIdControleConferenciaEncalheCota(dadosDocumentacaoConfEncalheCota.getIdControleConferenciaEncalheCota());
				
			} catch (EncalheExcedeReparteException e) {

				throw new ValidacaoException(TipoMensagem.WARNING, "Conferência de encalhe está excedendo quantidade de reparte.");
			
			} finally {
				
				this.atribuirIds(lista);
			}
			
			
			Map<String, Object> dados = new HashMap<String, Object>();
			
			dados.put("tipoMensagem", TipoMensagem.SUCCESS);
			dados.put("listaMensagens", 	new String[]{"Operação efetuada com sucesso."});

			if(dadosDocumentacaoConfEncalheCota!=null && dadosDocumentacaoConfEncalheCota.isIndGeraDocumentacaoConferenciaEncalhe()) {

				String[] tiposDocumento = null;
				
				if(	dadosDocumentacaoConfEncalheCota.getNossoNumero()!=null && 
					!dadosDocumentacaoConfEncalheCota.getNossoNumero().isEmpty()) {
					
					tiposDocumento = new String[]{
							TipoDocumentoConferenciaEncalhe.SLIP.name(), 
							TipoDocumentoConferenciaEncalhe.BOLETO_OU_RECIBO.name()};
				} else {
					
					tiposDocumento = new String[]{TipoDocumentoConferenciaEncalhe.SLIP.name()};
					
				}
				
				dados.put("tiposDocumento", tiposDocumento);
				
				dados.put("indGeraDocumentoConfEncalheCota", dadosDocumentacaoConfEncalheCota.isIndGeraDocumentacaoConferenciaEncalhe());
			}

			
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
		
		List<ProdutoEdicao> listaProdutoEdicao = this.produtoEdicaoService.obterProdutoPorCodigoNome(codigoNomeProduto);
		
		if (listaProdutoEdicao != null && !listaProdutoEdicao.isEmpty()){
			
			List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
			
			for (ProdutoEdicao produtoEdicao : listaProdutoEdicao){
				
				listaProdutos.add(
						new ItemAutoComplete(
								produtoEdicao.getProduto().getCodigo() + " - " + produtoEdicao.getProduto().getNome() + " - " + produtoEdicao.getNumeroEdicao(), 
								null,
								new Object[]{produtoEdicao.getProduto().getCodigo(), produtoEdicao.getId()}));
			}
			
			result.use(Results.json()).from(listaProdutos, "result").recursive().serialize();
		} else {
		
			result.use(Results.json()).from("", "result").serialize();
		}
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

			this.result.use(CustomMapJson.class).put("result", dadosNotaFiscal).serialize();
			
		} else {

			this.result.use(Results.json()).from("","result").serialize();
			
		}
		
	}
	
	/**
	 * Verifica se o valor total da nota fiscal informada é igual
	 * ao valor de encalhe conferido na operação. 
	 * 
	 */
	@Post
	public void verificarValorTotalNotaFiscal() {
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Map<String, Object> dadosNotaFiscal = (Map) this.session.getAttribute(NOTA_FISCAL_CONFERENCIA);
		
		Map<String, Object> dados = new HashMap<String, Object>();
		
		this.calcularValoresMonetarios(dados);
		
		BigDecimal valorEncalhe = ((BigDecimal)dados.get("valorEncalhe"));
		
		if (	dadosNotaFiscal != null && 
				dadosNotaFiscal.get("valorProdutos") != null && 
				!((BigDecimal)dadosNotaFiscal.get("valorProdutos")).equals(valorEncalhe)){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Valor total do encalhe difere do valor da nota informada.");
			
		}  else {
			
			this.finalizarConferencia();
			
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
			
			resultadoValidacao.put("mensagemConfirmacao", "Valor CE jornaleiro informado inválido, Deseja continuar?");
			
			this.result.use(CustomMapJson.class).put("result", resultadoValidacao).serialize();

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
			
			this.result.use(CustomMapJson.class).put("result", resultadoValidacao).serialize();
			
		} else {

			resultadoValidacao.put("valorCEInformadoValido", true);
			
			this.result.use(CustomMapJson.class).put("result", resultadoValidacao).serialize();

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
			
			this.result.use(CustomMapJson.class).put("result", resultadoValidacao).serialize();
			
		} else {

			resultadoValidacao.put("valorCEInformadoValido", true);
			
			this.result.use(CustomMapJson.class).put("result", resultadoValidacao).serialize();

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
			
			this.result.use(CustomMapJson.class).put("result", dados).serialize();
			
		} catch (ChamadaEncalheCotaInexistenteException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existe chamada de encalhe deste produto para essa cota.");
		} catch (EncalheRecolhimentoParcialException e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
	}
	
	private void limparDadosSessao() {
		
		this.session.removeAttribute(ID_BOX_LOGADO);
		this.session.removeAttribute(INFO_CONFERENCIA);
		this.session.removeAttribute(VALOR_ENCALHE_JORNALEIRO);
		this.session.removeAttribute(NOTA_FISCAL_CONFERENCIA);
		this.session.removeAttribute(SET_CONFERENCIA_ENCALHE_EXCLUIR);
		this.session.removeAttribute(HORA_INICIO_CONFERENCIA);
		this.session.removeAttribute(DADOS_DOCUMENTACAO_CONF_ENCALHE_COTA);
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
			
			if (info.getListaConferenciaEncalhe() != null){
			
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
	private ConferenciaEncalheDTO atualizarQuantidadeConferida(Long codigoAnterior, Long quantidade, ProdutoEdicaoDTO produtoEdicao, Boolean juramentada) {
		
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
			
			conferenciaEncalheDTOSessao.setQtdExemplar(BigInteger.valueOf(quantidade));
		} else {
			
			conferenciaEncalheDTOSessao = this.criarConferenciaEncalhe(produtoEdicao, quantidade, true);
		}
		if (juramentada != null) {
			conferenciaEncalheDTOSessao.setJuramentada(juramentada);
		}
		return conferenciaEncalheDTOSessao;
	}
	
	private ConferenciaEncalheDTO criarConferenciaEncalhe(ProdutoEdicaoDTO produtoEdicao, Long quantidade, boolean adicionarGrid) {
		
		ConferenciaEncalheDTO conferenciaEncalheDTO = new ConferenciaEncalheDTO();
		
		int id = (int) System.currentTimeMillis();
		
		if (id > 0){
			id *= -1;
		}
		
		conferenciaEncalheDTO.setIdConferenciaEncalhe(new Long(id));
		conferenciaEncalheDTO.setCodigo(produtoEdicao.getCodigoProduto());
		conferenciaEncalheDTO.setCodigoDeBarras(produtoEdicao.getCodigoDeBarras());
		conferenciaEncalheDTO.setCodigoSM(produtoEdicao.getSequenciaMatriz());
		conferenciaEncalheDTO.setIdProdutoEdicao(produtoEdicao.getId());
		conferenciaEncalheDTO.setNomeProduto(produtoEdicao.getNomeProduto());
		conferenciaEncalheDTO.setNumeroEdicao(produtoEdicao.getNumeroEdicao());
		
		conferenciaEncalheDTO.setPrecoCapa(produtoEdicao.getPrecoVenda());
		conferenciaEncalheDTO.setPrecoCapaInformado(produtoEdicao.getPrecoVenda());
		
		conferenciaEncalheDTO.setTipoChamadaEncalhe(produtoEdicao.getTipoChamadaEncalhe().name());
		conferenciaEncalheDTO.setDataRecolhimento(produtoEdicao.getDataRecolhimentoDistribuidor());
		
		conferenciaEncalheDTO.setParcial(produtoEdicao.isParcial());
		
		
		if (quantidade != null){
		
			conferenciaEncalheDTO.setQtdExemplar(BigInteger.valueOf(quantidade));
			conferenciaEncalheDTO.setQtdInformada(BigInteger.valueOf(quantidade));
		} else {
			
			conferenciaEncalheDTO.setQtdExemplar(BigInteger.ONE);
			conferenciaEncalheDTO.setQtdInformada(BigInteger.ONE);

		}
		
		conferenciaEncalheDTO.setDesconto(produtoEdicao.getDesconto());
		
		conferenciaEncalheDTO.setValorTotal(produtoEdicao.getPrecoVenda().subtract(produtoEdicao.getDesconto()).multiply(new BigDecimal( conferenciaEncalheDTO.getQtdExemplar()) ));
		
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
	
	//TODO
	private Usuario getUsuarioLogado(){
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		
		return usuario;
	}
}