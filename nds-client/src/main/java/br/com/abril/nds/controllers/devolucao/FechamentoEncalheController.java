package br.com.abril.nds.controllers.devolucao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.DataHolder;
import br.com.abril.nds.client.vo.AnaliticoEncalheVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.ChamadaAntecipadaEncalheService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes à
 * tela de chamadão de publicações.
 * 
 * @author Discover Technology
 */
@Resource
@Path("devolucao/fechamentoEncalhe")
@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE)
public class FechamentoEncalheController extends BaseController {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(FechamentoEncalheController.class);
	
	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private FechamentoEncalheService fechamentoEncalheService;

	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private ChamadaAntecipadaEncalheService chamadaAntecipadaEncalheService;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	protected BoletoService boletoService;
	
	@Autowired
	protected HttpSession session;
	
	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaFechamentoEncalhe";
	
	private static final String DATA_HOLDER_ACTION_KEY = "fechamentoEncalhe";
	
	private static final String STATUS_FINALIZADO = "FINALIZADO";
	
	private static final String KEY_COBRANCA_COTAS = "cobrancaCotas";
	
	private static final ConcurrentMap<String, String> CACHE_COBRANCA_COTAS = new ConcurrentHashMap<>();
	
	@Path("/")
	public void index() {
		
		List<Fornecedor> listaFornecedores = fornecedorService.obterFornecedores();
		List<Box> listaBoxes = boxService.buscarPorTipo(TipoBox.ENCALHE);
		
		result.include("dataOperacao", DateUtil.formatarDataPTBR(this.distribuidorService.obterDataOperacaoDistribuidor()));
		result.include("listaFornecedores", listaFornecedores);
		result.include("listaBoxes", listaBoxes);
		
		boolean confCega = !usuarioPossuiRule(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_CONF_CEGA);
		
		boolean permissaoVisualiza = usuarioPossuiRule(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE);
		
		result.include("permissaoColExemplDevolucao", permissaoVisualiza && confCega);
		
		boolean permissaoEdicao = usuarioPossuiRule(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO);
		
		result.include("permissaoBtnConfirmar", permissaoEdicao && confCega);
	}
	
	@Path("/pesquisar")
	public void pesquisar(String dataEncalhe, Long fornecedorId, Long boxId, Boolean aplicaRegraMudancaTipo,
			String sortname, String sortorder, int rp, int page) {
		
		int quantidade = this.quantidadeItensFechamentoEncalhe(dataEncalhe, fornecedorId, boxId, aplicaRegraMudancaTipo);
		
		if (quantidade == 0) {
			
			this.getSession().removeAttribute("gridFechamentoEncalheDTO");
			this.result.use(Results.json()).from(
				new ValidacaoVO(
						TipoMensagem.WARNING, 
						"Não houve conferência de encalhe nesta data."), "mensagens").recursive().serialize();
		} else {
			List<FechamentoFisicoLogicoDTO> listaEncalhe = 
					consultarItensFechamentoEncalhe(dataEncalhe, fornecedorId, boxId, 
							aplicaRegraMudancaTipo, sortname, sortorder, rp, page);

			this.getSession().setAttribute("gridFechamentoEncalheDTO", listaEncalhe);
			
			this.result.use(FlexiGridJson.class).from(listaEncalhe).total(quantidade).page(page).serialize();
		}
	}
	
	private int quantidadeItensFechamentoEncalhe(
			String dataEncalhe, Long fornecedorId, Long boxId,
			Boolean aplicaRegraMudancaTipo) {
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
		filtro.setFornecedorId(fornecedorId);
		filtro.setBoxId(boxId);
		
		int quantidadeFechamentoEncalhe = this.fechamentoEncalheService.buscarQuantidadeConferenciaEncalhe(filtro);
		
		return quantidadeFechamentoEncalhe;
	}	

	private List<FechamentoFisicoLogicoDTO> consultarItensFechamentoEncalhe(
			String dataEncalhe, Long fornecedorId, Long boxId,
			Boolean aplicaRegraMudancaTipo, String sortname, String sortorder,
			int rp, int page) {
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
		filtro.setFornecedorId(fornecedorId);
		filtro.setBoxId(boxId);
		
		if (aplicaRegraMudancaTipo && boxId == null){
				
				FiltroFechamentoEncalheDTO filtroRevomecao = new FiltroFechamentoEncalheDTO(); 
				
				filtroRevomecao.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
				
				fechamentoEncalheService.converteFechamentoDetalhadoEmConsolidado(filtroRevomecao);
				
		} 
		
		this.getSession().setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE,filtro);
		
		
		List<FechamentoFisicoLogicoDTO> listaEncalhe = fechamentoEncalheService.buscarFechamentoEncalhe(filtro, sortorder, this.resolveSort(sortname), page, rp);
		
		for (FechamentoFisicoLogicoDTO fechamentoFisicoLogico : listaEncalhe) {
			
			fechamentoFisicoLogico.setReplicar(getCheckedFromDataHolder(fechamentoFisicoLogico.getProdutoEdicao().toString()));
		}
		
		return listaEncalhe;
	}
	
	private String getCheckedFromDataHolder(String codigo) {
		
		DataHolder dataHolder = (DataHolder) this.getSession().getAttribute(DataHolder.SESSION_ATTRIBUTE_NAME);
		
		if (dataHolder != null) {

			return dataHolder.getData(DATA_HOLDER_ACTION_KEY, codigo, "checado");
		}
		
		return "false";
	}
	
	
	@Path("/salvar")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO)
	public void salvar(List<FechamentoFisicoLogicoDTO> listaFechamento, List<FechamentoFisicoLogicoDTO> listaNaoReplicados,
					   boolean isAllFechamentos, String dataEncalhe, Long fornecedorId, Long boxId) {
		
		List<FechamentoFisicoLogicoDTO> todosFechamentos = this.consultarItensFechamentoEncalhe(dataEncalhe, fornecedorId, boxId, false, null, null, 0, 0);
		
		List<FechamentoFisicoLogicoDTO> fechamentos = mergeItensFechamento(todosFechamentos, listaFechamento);

		gravaFechamentoEncalhe(fechamentos, listaNaoReplicados, isAllFechamentos, dataEncalhe, fornecedorId, boxId);
		
		this.getSession().removeAttribute("listaDeGrid");
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Informação gravada com sucesso!"), "result").recursive().serialize();
	}
	
	private List<FechamentoFisicoLogicoDTO> mergeItensFechamento(List<FechamentoFisicoLogicoDTO> fechamentosBanco,
																 List<FechamentoFisicoLogicoDTO> fechamentoTela) {
		
		if (fechamentoTela == null) {
			
			return fechamentosBanco;
		}
		
		for (FechamentoFisicoLogicoDTO fechamento : fechamentoTela) {
			
			int index = fechamentosBanco.indexOf(fechamento);
			
			fechamentosBanco.get(index).setFisico(fechamento.getFisico());
		}
		
		return fechamentosBanco;
	}
	
	@Path("/cotasAusentesConfirmacao")
	public void cotasAusentesConfirmacao(Date dataEncalhe, boolean isSomenteCotasSemAcao, List<Long> idsCotas, String sortname, String sortorder, int rp, int page) {

		List<CotaAusenteEncalheDTO> listaCotasAusenteEncalhe =
			this.fechamentoEncalheService.buscarCotasAusentes(dataEncalhe, isSomenteCotasSemAcao, sortorder, sortname, page, rp);
		
		int total = this.fechamentoEncalheService.buscarTotalCotasAusentes(dataEncalhe, isSomenteCotasSemAcao);
		
		if (listaCotasAusenteEncalhe == null || listaCotasAusenteEncalhe.isEmpty()) {
			
			if (isSomenteCotasSemAcao) {
			
				this.result.nothing();
				
				return;
			}
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota ausente!");
		}
		
		this.result.use(FlexiGridJson.class).from(listaCotasAusenteEncalhe).total(total).page(page).serialize();
	}
	
	
	@Path("/cotasAusentes")
	public void cotasAusentes(Date dataEncalhe, boolean isSomenteCotasSemAcao, List<Long> idsCotas, String sortname, String sortorder, int rp, int page) {

		List<CotaAusenteEncalheDTO> listaCotasAusenteEncalhe =
			this.fechamentoEncalheService.buscarCotasAusentes(dataEncalhe, isSomenteCotasSemAcao, sortorder, sortname, page, rp);
		
		int total = this.fechamentoEncalheService.buscarTotalCotasAusentes(dataEncalhe, isSomenteCotasSemAcao);
		
		if (listaCotasAusenteEncalhe == null || listaCotasAusenteEncalhe.isEmpty()) {
			
			if (isSomenteCotasSemAcao) {
			
				this.result.nothing();
				
				return;
			}
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota ausente!");
		}
		
		this.result.use(FlexiGridJson.class).from(listaCotasAusenteEncalhe).total(total).page(page).serialize();
	}
	
	@Path("/cotasAusentesSemAcao")
	public void cotasAusentesSemAcao(Date dataEncalhe,
			String sortname, String sortorder, int rp, int page) {
		
	}
	
	@Path("carregarDataPostergacao")
	public void carregarDataPostergacao(Date dataEncalhe, Date dataPostergacao) {
		
		try {
			
			int quantidadeDias = 0;
			
			if (dataPostergacao == null) {
				quantidadeDias = 1;
				dataPostergacao = dataEncalhe;
			}
			
			
			dataPostergacao = 
				this.calendarioService.adicionarDiasRetornarDiaUtil(dataPostergacao, quantidadeDias);
			
			if (dataPostergacao != null) {
				String dataFormatada = DateUtil.formatarData(dataPostergacao, "dd/MM/yyyy");
				this.result.use(Results.json()).from(dataFormatada, "result").recursive().serialize();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Path("/postergarCotas")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO)
	public void postergarCotas(Date dataPostergacao, Date dataEncalhe, List<Long> idsCotas, boolean postergarTodasCotas) {
		
		if (dataEncalhe != null && dataEncalhe.after(dataPostergacao)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Postergação não pode ser realizada antes da data atual!");
		} 
		/**
		 * 09/10/2013
		 * Regra solicitada pela Magali / Rodrigo
		 * Remover a validacao da semana de recolhimento e permitir qualquer data futura em relacao a data de operacao
		 */
		else if (!dataPostergacao.after(distribuidorService.obterDataOperacaoDistribuidor())) {
			throw new ValidacaoException(TipoMensagem.WARNING, "A Data de Postergação deve ser maior que a data de operação!");
		} 
		
		try {
			List<CotaAusenteEncalheDTO> listaCotasAusentes = this.fechamentoEncalheService.buscarCotasAusentes(dataEncalhe, true, null, null, 0, 0);
			if (postergarTodasCotas) {
				
				if(listaCotasAusentes != null){
					
					//idsCotas a serem retirados da lista 
					//removerCotasAusentesLista(listaCotasAusentes, idsCotas);
					
					this.fechamentoEncalheService.postergarTodasCotas(dataEncalhe, dataPostergacao, listaCotasAusentes);
				}
				
			
			} else {
				//Adiciona as contas com dívida
				idsCotas.addAll(getIdsCotasAusentesComDivida(listaCotasAusentes));
				
				this.fechamentoEncalheService.postergarCotas(dataEncalhe, dataPostergacao, idsCotas);
			}
			
		} catch (Exception e) {
			
			LOGGER.error("Erro ao tentar postergar!", e);
			
			this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.ERROR, "Erro ao tentar postergar!"), "result").recursive().serialize();
			throw new ValidacaoException();
		}
		
		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Cotas postergadas com sucesso!"), "result").recursive().serialize();
	}
		
//	private void removerCotasAusentesLista(List<CotaAusenteEncalheDTO> listaCotasAusentes, List<Long> idsCotas) {
//		
//		ArrayList<CotaAusenteEncalheDTO> newRefListaCotasAusentes = new ArrayList<CotaAusenteEncalheDTO>(listaCotasAusentes);
//		
//		if(idsCotas != null) {
//			
//			for(Long idCota : idsCotas) {
//				for(int i=0; i < listaCotasAusentes.size(); i++) {
//					CotaAusenteEncalheDTO dto = newRefListaCotasAusentes.get(i);
//					
//					if(dto != null && dto.getIdCota().equals(idCota)) {
//						newRefListaCotasAusentes.add(listaCotasAusentes.get(i));
//					}
//				}
//			}
//			
//			listaCotasAusentes.removeAll(newRefListaCotasAusentes);
//		}
//		
//	}

	@Path("/dataSugestaoPostergarCota")
	public void carregarDataSugestaoPostergarCota(String dataEncalhe) throws ParseException {
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dataEncalhe);
				
		Date resultado = chamadaAntecipadaEncalheService.obterProximaDataEncalhe(date);
		
		if (resultado != null){

		    this.result.use(Results.json()).from(resultado, "resultado").serialize();
		}
		else{
			
			this.result.use(Results.nothing());
		}
	}
	
	@Post
	public void veificarCobrancaGerada(List<Long> idsCotas, boolean cobrarTodasCotas){
		
		if (!cobrarTodasCotas && (idsCotas == null || idsCotas.isEmpty())) {

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Selecine pelo menos uma Cota para cobrar!"));
		}
		
		if (this.gerarCobrancaService.verificarCobrancasGeradas(idsCotas)){
			
			this.result.use(Results.json()).from(
					new ValidacaoVO(TipoMensagem.WARNING, 
							"Já existe cobrança gerada para a data de operação atual, continuar irá sobrescreve-la. Deseja continuar?"), 
							"result").recursive().serialize();
			return;
		}
		
		this.result.use(Results.json()).from("").serialize();
	}

	@Path("/cobrarCotas")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO)
	public void cobrarCotas(Date dataOperacao, List<Long> idsCotas, boolean cobrarTodasCotas) {

		if (!cobrarTodasCotas && (idsCotas == null || idsCotas.isEmpty())) {
			this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.WARNING, "Selecine pelo menos uma Cota para cobrar!"), "result").recursive().serialize();
			return;
		}
		
		try {
			
			if (cobrarTodasCotas) {
				
				//idsCotas a serem retirados da lista
				//removerCotasAusentesLista(listaCotaAusenteEncalhe, idsCotas);
				
				List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe = 
						this.fechamentoEncalheService.buscarCotasAusentes(dataOperacao, true, null, null, 0, 0);
				
				this.realizarCobrancaTodasCotas(dataOperacao, listaCotaAusenteEncalhe);				
			
			} else {
			
				this.realizarCobrancaCotasEspecificas(idsCotas, dataOperacao);
			}

		} catch (ValidacaoException e) {
			this.result.use(Results.json()).from(e.getValidacao(), "result").recursive().serialize();
			return;
		} catch (GerarCobrancaValidacaoException e) {
			this.result.use(Results.json()).from(
				new ValidacaoException(TipoMensagem.WARNING, e.getValidacaoVO().getListaMensagens()).getValidacao(), "result").recursive().serialize();
			return;
		}

		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Cotas cobradas com sucesso!"), "result").recursive().serialize();		
	}
	
	private void realizarCobrancaCotasEspecificas(List<Long> idsCotas, Date dataOperacao) throws GerarCobrancaValidacaoException {
		
		GerarCobrancaValidacaoException ex = null;

		int statusCobrancaCota = 0;
		int totalCotas = idsCotas.size();

		try {
			for (Long idCota : idsCotas) {

				String status =  "Cota " + statusCobrancaCota++ + " de " + totalCotas;
				
				this.setStatusCobrancaCota(status);

				this.fechamentoEncalheService.cobrarCota(dataOperacao, getUsuarioLogado(), idCota);

			}	
		} catch (GerarCobrancaValidacaoException e) {

			ex = e;

		} catch (Exception e) {

			this.setStatusCobrancaCota(STATUS_FINALIZADO);

			throw e;
			
		} finally {
			
			this.setStatusCobrancaCota(STATUS_FINALIZADO);
		}

		if (ex != null){

			throw ex;
		}
	}

	private void realizarCobrancaTodasCotas(Date dataOperacao, List<CotaAusenteEncalheDTO> listaCotasAusentes) throws GerarCobrancaValidacaoException {
		
		ValidacaoVO validacaoVO = new ValidacaoVO();
		
		validacaoVO.setListaMensagens(new ArrayList<String>());

		int statusCobrancaCota = 0;
		
		int totalCotas = listaCotasAusentes.size();
		
		try {
			
			for (CotaAusenteEncalheDTO cotaAusenteEncalheDTO : listaCotasAusentes){
				
				String status =  "Cota " + (++statusCobrancaCota) + " de " + totalCotas;
				
				this.setStatusCobrancaCota(status);

				this.fechamentoEncalheService.realizarCobrancaCota(dataOperacao,
												                   getUsuarioLogado(), 
												                   cotaAusenteEncalheDTO.getIdCota(),
												                   validacaoVO);					
			}
		} catch (Exception e) {

			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
			
		} finally {
			
			this.setStatusCobrancaCota(STATUS_FINALIZADO);
		}

		if (validacaoVO.getListaMensagens() != null && !validacaoVO.getListaMensagens().isEmpty()){

			throw new ValidacaoException(validacaoVO);
		}
	}

	@Post
	public void obterStatusCobrancaCota() {
		
		String status = this.getStatusCobrancaCotas();
		
		result.use(Results.json()).withoutRoot().from(status==null?"Processando cotas..." : status).recursive().serialize();
	}

	private Collection<? extends Long> getIdsCotasAusentesComDivida(List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe) {

		List<Long> idsCotasAusentesComDivida = new ArrayList<Long>();
		for (CotaAusenteEncalheDTO cotaAusenteEncalheDTO : listaCotaAusenteEncalhe) {
			
			if(!cotaAusenteEncalheDTO.isIndPossuiChamadaEncalheCota()) {
				
				if (cotaAusenteEncalheDTO.isIndMFCNaoConsolidado()) {
					idsCotasAusentesComDivida.add(cotaAusenteEncalheDTO.getIdCota());
				} 
				
			}
		}
		
		return idsCotasAusentesComDivida;
	}

	@SuppressWarnings("deprecation")
	@Get
	@Path("/exportarArquivo")
	public void exportarArquivo(Date dataEncalhe, String sortname, String sortorder, 
			int rp, int page, FileType fileType) {

		List<CotaAusenteEncalheDTO> listaCotasAusenteEncalhe =
			this.fechamentoEncalheService.buscarCotasAusentes(dataEncalhe, false, sortorder, sortname, 0, 0);

		if (listaCotasAusenteEncalhe != null && !listaCotasAusenteEncalhe.isEmpty()) {
		
			try {
					
				FileExporter.to("cotas-ausentes", fileType).inHTTPResponse(
					this.getNDSFileHeader(), null, null, listaCotasAusenteEncalhe, 
				CotaAusenteEncalheDTO.class, this.response);
				
			} catch (Exception e) {
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao gerar o arquivo!"));
			}
		}
	
		this.result.use(Results.nothing());
	}

	@SuppressWarnings("deprecation")
	@Get
	@Path("/imprimirArquivo")
	public void imprimirArquivo(Date dataEncalhe, Long fornecedorId, Long boxId,
			String sortname, String sortorder, int rp, int page, FileType fileType) {
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(dataEncalhe);
		filtro.setFornecedorId(fornecedorId);
		filtro.setBoxId(boxId);
		
		List<FechamentoFisicoLogicoDTO> listaEncalhe = fechamentoEncalheService.buscarFechamentoEncalhe(
				filtro, sortorder, this.resolveSort(sortname), null, null);
		
		if (listaEncalhe != null && !listaEncalhe.isEmpty()) {
		
			try {
				
				FileExporter.to("fechamentos-encalhe", fileType).inHTTPResponse(
					this.getNDSFileHeader(), null, null, listaEncalhe, 
					FechamentoFisicoLogicoDTO.class, this.response);
				
			} catch (Exception e) {
				
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao gerar o arquivo!"));
			}
		}
		
		this.result.use(Results.nothing());
	}

	@Path("/encerrarOperacaoEncalhe")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO)
	public void encerrarOperacaoEncalhe(Date dataEncalhe) {
		
		try {
		
			if (dataEncalhe == null) {
				
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Data de encalhe inválida!"));
			}
			
			ValidacaoVO validacaoCotaConferenciaNaoFinalizada = getValidacaoCotaConferenciaNaoFinalizada(dataEncalhe);
			
			if(validacaoCotaConferenciaNaoFinalizada != null) {
				this.result.use(Results.json()).withoutRoot().from(validacaoCotaConferenciaNaoFinalizada).recursive().serialize();
				return;
			}
			
			FiltroFechamentoEncalheDTO filtroSessao = (FiltroFechamentoEncalheDTO) this.getSession().getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
			
			@SuppressWarnings("unchecked")
			List<FechamentoFisicoLogicoDTO> listaEncalhe = (List<FechamentoFisicoLogicoDTO>) this.getSession().getAttribute("gridFechamentoEncalheDTO");
			
			this.fechamentoEncalheService.encerrarOperacaoEncalhe(dataEncalhe, getUsuarioLogado(), filtroSessao, listaEncalhe);
			
		} catch (Exception e) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao tentar encerrar a operação de encalhe!"));
		}
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação de encalhe encerrada com sucesso!"), "result").recursive().serialize();
	}
	
	/**
	 * Cria um objeto {@link ValidacaoVO} com informações das cotas
	 * que possuem conferencia de encalhe não finalizada.
	 * 
	 * @param dataEncalhe
	 */
	private ValidacaoVO getValidacaoCotaConferenciaNaoFinalizada(Date dataEncalhe) {
		
		ValidacaoVO validacao = null;
		
		List<CotaDTO> listaCotaConferenciaNaoFinalizada = fechamentoEncalheService.obterListaCotaConferenciaNaoFinalizada(dataEncalhe);
		
		if(listaCotaConferenciaNaoFinalizada!=null && !listaCotaConferenciaNaoFinalizada.isEmpty()) {
			
			StringBuffer msg = new StringBuffer();
			msg.append("A seguintes cotas possuem conferencia de encalhe não confirmada: ");
			
			for(CotaDTO cota : listaCotaConferenciaNaoFinalizada) {
				
				msg.append("<br/>");
				msg.append(" [").
				append(cota.getNumeroCota())
				.append("] ")
				.append(" - ")
				.append(cota.getNomePessoa());
				
			}
			
			validacao = new ValidacaoVO(TipoMensagem.WARNING, msg.toString());
		}
		
		return validacao;
	}
	
	/**
	 * Caso a operação realizada seja de VERIFICACAO
	 * 
	 * 		Serão realizadas validações retornando
	 * 		para view mensagens de sucesso ou erro 
	 * 		em relação a estas validações. As validações são:
	 * 		
	 * 		- Verifica se existem cotas com conferencia não finalizada
	 * 		  Caso existam retorna mensagem de WARNING com uma lista
	 * 		  destas cota.
	 * 
	 * 		- Verifica se existem cota ausentes retornando uma 
	 * 		  mensagem informando se foram encontradas ou não
	 * 		  cotas ausentes.
	 * 
	 * 
	 * Caso a operação seja de CONFIRMACAO
	 * 	
	 *		Sera realizada uma validação (Se existem
	 *		cotas com conferencia não finalizada).
	 *		Caso a validação seja de sucesso sera 
	 *		efetuado o encerramento do encalhe
	 * 	
	 * 
	 * @param dataEncalhe
	 * @param operacao
	 */
	@Path("/verificarEncerrarOperacaoEncalhe")
	public void verificarEncerrarOperacaoEncalhe(Date dataEncalhe, String operacao) {
		
		if (dataEncalhe == null) {
				
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Data de encalhe inválida!"));
		}
		
		
		ValidacaoVO validacaoCotaConferenciaNaoFinalizada = getValidacaoCotaConferenciaNaoFinalizada(dataEncalhe);
		
		if(validacaoCotaConferenciaNaoFinalizada != null) {
			
			this.result.use(Results.json()).withoutRoot().from(validacaoCotaConferenciaNaoFinalizada).recursive().serialize();
			
			return;
		}
		
		int totalCotasAusentes = this.fechamentoEncalheService.buscarTotalCotasAusentesSemPostergado(dataEncalhe, true, true);
		
		if (totalCotasAusentes > 0 && ("VERIFICACAO").equalsIgnoreCase(operacao)) {
			
			this.result.use(Results.json()).from(Boolean.FALSE.toString(), "isNenhumaCotaAusente").recursive().serialize();
			
			return;
		
		} else if (totalCotasAusentes <= 0 && ("VERIFICACAO").equalsIgnoreCase(operacao)) {
			
			this.result.use(Results.json()).from(Boolean.TRUE.toString(), "isNenhumaCotaAusente").recursive().serialize();
			
			return;
		}
			
		try {
			
			FiltroFechamentoEncalheDTO filtroSessao = (FiltroFechamentoEncalheDTO) this.getSession().getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
			
			@SuppressWarnings("unchecked")
			List<FechamentoFisicoLogicoDTO> listaEncalhe = (List<FechamentoFisicoLogicoDTO>) this.getSession().getAttribute("gridFechamentoEncalheDTO");
			
			this.fechamentoEncalheService.encerrarOperacaoEncalhe(dataEncalhe, getUsuarioLogado(), filtroSessao, listaEncalhe);
		
		} catch(ValidacaoException ve){
			
			throw ve;
			
		} catch (Exception e) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao tentar encerrar a operação de encalhe! " + e.getMessage()));
		}
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação de encalhe encerrada com sucesso!"),"result").recursive().serialize();
	}

	private String resolveSort(String sortname) {

		if (sortname != null && sortname.endsWith("Formatado")) {
			
			return sortname.substring(0, sortname.indexOf("Formatado"));
			
		} else {
			
			return sortname;
			
		}
	}
	
	@Path("/verificarMensagemConsistenciaDados")
	public void verificarMensagemConsistenciaDados(String dataEncalhe, Long fornecedorId, Long boxId) {
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
		filtro.setFornecedorId(fornecedorId);
		filtro.setBoxId(boxId);
		
		if (boxId == null){
			
			if (fechamentoEncalheService.existeFechamentoEncalheDetalhado(filtro)){
				
				String msgPesquisaConsolidado = "Você está tentando fazer uma " +
						"pesquisa em modo consolidado (soma de todos os boxes). " +
						"Já existem dados salvos em modo de pesquisa por box. " +
						"Se você continuar, os dados serão perdidos. " +
						"Tem certeza que deseja continuar ?";
				
				this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, msgPesquisaConsolidado), "result").recursive().serialize();
			
			} else {
				
				this.result.use(Results.json()).from("pesquisa","result").serialize() ;   
			
			}
		
		} else if ( fechamentoEncalheService.existeFechamentoEncalheConsolidado(filtro)){
			
			String msgPesquisaPorBox = "Você está tentando fazer uma pesquisa por box. " +
					"Já existem dados salvos em modo de pesquisa consolidado (soma de todos os boxes). " +
					"Não será possível realizar a pesquisa.";
			
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.ERROR, msgPesquisaPorBox), "result").recursive().serialize();
		
		} else {
		
			this.result.use(Results.json()).from("pesquisa","result").serialize() ;   
		
		}
	}
	
	@Path("/salvarNoEncerrementoOperacao")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO)
	public void salvarNoEncerrementoOperacao(List<FechamentoFisicoLogicoDTO> listaFechamento,
											 List<FechamentoFisicoLogicoDTO> listaNaoReplicados, boolean isAllFechamentos, 
											 String dataEncalhe, Long fornecedorId, Long boxId) {
		
		if (isAllFechamentos || (listaNaoReplicados != null && !listaNaoReplicados.isEmpty())) {

			listaFechamento = this.consultarItensFechamentoEncalhe(dataEncalhe, fornecedorId, boxId, false, null, null, 0, 0);
		}
		
		if (listaFechamento !=null && !listaFechamento.isEmpty()) {
			
			gravaFechamentoEncalhe(listaFechamento, listaNaoReplicados, isAllFechamentos, dataEncalhe, fornecedorId, boxId);
		}

		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	private void gravaFechamentoEncalhe(
			List<FechamentoFisicoLogicoDTO> listaFechamento,
			List<FechamentoFisicoLogicoDTO> listaNaoReplicados, 
			boolean isAllFechamentos, 
			String dataEncalhe, 
			Long fornecedorId, 
			Long boxId) {
		
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		
		filtro.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
		
		filtro.setFornecedorId(fornecedorId);
		
		filtro.setBoxId(boxId);
		
		filtro.setCheckAll(isAllFechamentos);
		
		if (boxId == null){ 
			
			fechamentoEncalheService.salvarFechamentoEncalhe(filtro,listaFechamento, listaNaoReplicados);
		} else {
			
			fechamentoEncalheService.salvarFechamentoEncalheBox(filtro, listaFechamento, listaNaoReplicados);
		}
	}
	
	//------------------
	// Analítico Encalhe
	//------------------
	
	@Path("/analitico")
	public void analiticoEncalhe() {
		this.index();
	}
	
	@Path("/pesquisarAnalitico.json")
	public void pesquisarAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro, String sortname, String sortorder, int rp, int page) {
	
		List<AnaliticoEncalheDTO> listDTO = fechamentoEncalheService.buscarAnaliticoEncalhe(filtro, sortorder, this.resolveSort(sortname), page, rp);
		
		Integer totalRegistro = fechamentoEncalheService.buscarTotalAnaliticoEncalhe(filtro);
		
		BigDecimal valorTotalAnalitico = fechamentoEncalheService.obterValorTotalAnaliticoEncalhe(filtro);
		
		if(valorTotalAnalitico == null) {
			valorTotalAnalitico = BigDecimal.ZERO;
		}
		
		List<AnaliticoEncalheVO> listVO = new ArrayList<AnaliticoEncalheVO>();
		
		for (AnaliticoEncalheDTO dto : listDTO) {
			listVO.add(new AnaliticoEncalheVO(dto));
		}
		
		TableModel<CellModelKeyValue<AnaliticoEncalheVO>> tableModel = new TableModel<CellModelKeyValue<AnaliticoEncalheVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listVO));
		
		tableModel.setTotal((totalRegistro!= null)? totalRegistro.intValue():0);
		
		tableModel.setPage(page);
		
		
		this.result.use(CustomMapJson.class)
		.put("tableModel", tableModel)
		.put("valorTotalAnalitico", CurrencyUtil.formatarValor(valorTotalAnalitico))
		.put("qtdCotas", ( (totalRegistro!= null)? totalRegistro.intValue():0) ).serialize();
	}
	
	@SuppressWarnings("deprecation")
	@Get
	@Path("/imprimirArquivoAnaliticoEncalhe")
	public void imprimirArquivoAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortname, String sortorder, int rp, int page, FileType fileType) {
		
		
		List<AnaliticoEncalheDTO> listDTO = fechamentoEncalheService.buscarAnaliticoEncalhe(filtro, sortorder, this.resolveSort(sortname), null, null);
		List<AnaliticoEncalheVO> listVO = new ArrayList<AnaliticoEncalheVO>();
		for (AnaliticoEncalheDTO dto : listDTO) {
			listVO.add(new AnaliticoEncalheVO(dto));
		}
		
		if (listVO != null && !listVO.isEmpty()) {
		
			try {
				
				FileExporter.to("analitico-encalhe", fileType).inHTTPResponse(
					this.getNDSFileHeader(), null, null, listVO, 
					AnaliticoEncalheVO.class, this.response);
				
			} catch (Exception e) {
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao gerar o arquivo!"));
			}
		}
		
		this.result.use(Results.nothing());
	}
	
	@Post
	public void enviarGridAnteriorParaSession(String codigo, String produtoEdicao, String fisico, boolean checkbox){
		
		@SuppressWarnings("unchecked")
		List<FechamentoFisicoLogicoDTO> listaDeGrid = (List<FechamentoFisicoLogicoDTO>) this.getSession().getAttribute("gridFechamentoEncalheDTO");
		
		boolean insercao = true;
		if(listaDeGrid != null && !listaDeGrid.isEmpty()){
			
			Iterator<FechamentoFisicoLogicoDTO> iterator = new ArrayList<FechamentoFisicoLogicoDTO>(listaDeGrid).iterator();
			
			while(iterator.hasNext()){
				FechamentoFisicoLogicoDTO linha = iterator.next();
				if(linha != null && linha.getCodigo() != null && linha.getCodigo().equals(codigo)){
					linha.setCodigo(codigo);
					linha.setProdutoEdicao(Long.parseLong(produtoEdicao));
					if(fisico != null){
						linha.setFisico(Long.parseLong(fisico));	
					}
					linha.setReplicar(String.valueOf(checkbox));
					
					insercao = false;
				}
			}

		} else {
			listaDeGrid = new ArrayList<FechamentoFisicoLogicoDTO>();
		}
	
		if(insercao == true) {
			FechamentoFisicoLogicoDTO gridFechamentoEncalheDTO = new FechamentoFisicoLogicoDTO();
			
			if(fisico != null) {
				gridFechamentoEncalheDTO.setFisico(Long.parseLong(fisico));	
			}
			
			gridFechamentoEncalheDTO.setCodigo(codigo);
			gridFechamentoEncalheDTO.setProdutoEdicao(Long.parseLong(produtoEdicao));
			gridFechamentoEncalheDTO.setReplicar(String.valueOf(checkbox));
			listaDeGrid.add(gridFechamentoEncalheDTO);
		}
		
		this.getSession().setAttribute("gridFechamentoEncalheDTO", listaDeGrid);		
		
		this.result.use(Results.nothing());
	}
	
	@Path("/limparDadosDaSessaoGrid")
	public void limparDadosDaSessaoGrid(){
		
		this.getSession().removeAttribute("gridFechamentoEncalheDTO");
		
		this.result.use(Results.nothing());
	}

	private HttpSession getSession() {

		return this.session;
	}
	
	private String getStatusCobrancaCotas() {
		
		return CACHE_COBRANCA_COTAS.get(KEY_COBRANCA_COTAS);
	}
	
	private void setStatusCobrancaCota(String status) {
		
		CACHE_COBRANCA_COTAS.put(KEY_COBRANCA_COTAS, status);
	}
	
}
