package br.com.abril.nds.controllers.devolucao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.AnaliticoEncalheVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.fechamentoencalhe.GridFechamentoEncalheDTO;
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
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.ChamadaAntecipadaEncalheService;
import br.com.abril.nds.service.CotaService;
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
	private CotaService cotaService;
	
	@Autowired
	private HttpSession session;
	
	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaFechamentoEncalhe";
	
	@Path("/")
	public void index() {
		
		List<Fornecedor> listaFornecedores = fornecedorService.obterFornecedores();
		List<Box> listaBoxes = boxService.buscarPorTipo(TipoBox.ENCALHE);
		
		result.include("dataOperacao", DateUtil.formatarDataPTBR(this.distribuidorService.obterDataOperacaoDistribuidor()));
		result.include("listaFornecedores", listaFornecedores);
		result.include("listaBoxes", listaBoxes);
		
		result.include("permissaoColExemplDevolucao", usuarioPossuiRule(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_COLUNA_EXEMPL_DEVOLUCAO));
	}
	
	@Path("/pesquisar")
	public void pesquisar(String dataEncalhe, Long fornecedorId, Long boxId, Boolean aplicaRegraMudancaTipo,
			String sortname, String sortorder, int rp, int page) {
		
		@SuppressWarnings("unchecked")
		List<FechamentoFisicoLogicoDTO> listaEncalheSession = (List<FechamentoFisicoLogicoDTO>) session.getAttribute("gridFechamentoEncalheDTO");
		
		if(listaEncalheSession == null || listaEncalheSession.isEmpty())
		{
			FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
			filtro.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
			filtro.setFornecedorId(fornecedorId);
			filtro.setBoxId(boxId);
			
			listaEncalheSession = this.fechamentoEncalheService.verificarListaDaSessao(listaEncalheSession, filtro, sortname, sortorder);
			session.setAttribute("gridFechamentoEncalheDTO", listaEncalheSession);
		}
		
		
		List<FechamentoFisicoLogicoDTO> listaEncalhe = 
				consultarItensFechamentoEncalhe(dataEncalhe, fornecedorId, boxId, aplicaRegraMudancaTipo,sortname, sortorder, rp, page);
		
		
		List<FechamentoFisicoLogicoDTO> novaListaEncalhe = this.fechamentoEncalheService.ajustarGrids(listaEncalhe, listaEncalheSession);
		
		int quantidade = this.quantidadeItensFechamentoEncalhe(dataEncalhe, fornecedorId, boxId, aplicaRegraMudancaTipo);
		
		if (listaEncalhe.isEmpty()) {
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Não houve conferência de encalhe nesta data."), "mensagens").recursive().serialize();
		} else {
			this.result.use(FlexiGridJson.class).from(novaListaEncalhe).total(quantidade).page(page).serialize();
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
		
		///verificar depois
		
		if (aplicaRegraMudancaTipo){
			if (boxId != null) {
				FiltroFechamentoEncalheDTO filtroRevomecao = new FiltroFechamentoEncalheDTO(); 
				filtroRevomecao.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
				fechamentoEncalheService.removeFechamentoDetalhado(filtroRevomecao);
			}
		} 
		
		this.session.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE,filtro);
		
		
		List<FechamentoFisicoLogicoDTO> listaEncalhe = fechamentoEncalheService.buscarFechamentoEncalhe(filtro, sortorder, this.resolveSort(sortname), page, rp);
		return listaEncalhe;
	}
	
	
	@Path("/salvar")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO)
	public void salvar(List<FechamentoFisicoLogicoDTO> listaFechamento, String dataEncalhe, Long fornecedorId, Long boxId) {
		
		@SuppressWarnings("unchecked")
		List<FechamentoFisicoLogicoDTO> listaDeGrid = (List<FechamentoFisicoLogicoDTO>) this.session.getAttribute("gridFechamentoEncalheDTO");
				
		gravaFechamentoEncalhe(listaDeGrid, dataEncalhe, fornecedorId,boxId);
		
		this.session.removeAttribute("listaDeGrid");
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Informação gravada com sucesso!"), "result").recursive().serialize();
	}
	
	@Path("/cotasAusentes")
	public void cotasAusentes(Date dataEncalhe, boolean isSomenteCotasSemAcao,
			String sortname, String sortorder, int rp, int page) {

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
	public void postergarCotas(Date dataPostergacao, Date dataEncalhe, List<Long> idsCotas, boolean postergarTodasCotas) {//TODO
		
		if (dataEncalhe != null && dataEncalhe.after(dataPostergacao)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Postergação não pode ser realizada antes da data atual!");
		} else if (  fechamentoEncalheService.buscarUtimoDiaDaSemanaRecolhimento().before(dataPostergacao) ){
			throw new ValidacaoException(TipoMensagem.WARNING, "Postergação deve ter como limite, a data final da semana de recolhimento em vigência!");
		}
		
		try {
			
			if (postergarTodasCotas) {
			
				this.fechamentoEncalheService.postergarTodasCotas(dataEncalhe, dataPostergacao);
			
			} else {
				
				this.fechamentoEncalheService.postergarCotas(dataEncalhe, dataPostergacao, idsCotas);
			}
			
		} catch (Exception e) {
			this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.ERROR, "Erro ao tentar postergar!"), "result").recursive().serialize();
			throw new ValidacaoException();
		}
		
		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Cotas postergadas com sucesso!"), "result").recursive().serialize();
	}
	
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
				
				List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe = 
						this.fechamentoEncalheService.buscarCotasAusentes(dataOperacao, true, null, null, 0, 0);
				
				this.fechamentoEncalheService.realizarCobrancaCotas(dataOperacao, getUsuarioLogado(), listaCotaAusenteEncalhe, null);				
			
				
			} else {
			
				this.fechamentoEncalheService.cobrarCotas(dataOperacao, getUsuarioLogado(), idsCotas);
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
			
			FiltroFechamentoEncalheDTO filtroSessao = 
					(FiltroFechamentoEncalheDTO) this.session.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
			
			this.fechamentoEncalheService.encerrarOperacaoEncalhe(dataEncalhe, getUsuarioLogado(),filtroSessao);
			
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
		
		int totalCotasAusentes = this.fechamentoEncalheService.buscarTotalCotasAusentes(dataEncalhe, true);
		
		if (totalCotasAusentes > 0 && ("VERIFICACAO").equalsIgnoreCase(operacao)) {
			
			this.result.use(Results.json()).from(Boolean.FALSE.toString(), "isNenhumaCotaAusente").recursive().serialize();
			
			return;
		
		} else if (totalCotasAusentes <= 0 && ("VERIFICACAO").equalsIgnoreCase(operacao)) {
			
			this.result.use(Results.json()).from(Boolean.TRUE.toString(), "isNenhumaCotaAusente").recursive().serialize();
			
			return;
		}
			
		try {
			
			FiltroFechamentoEncalheDTO filtroSessao = 
					(FiltroFechamentoEncalheDTO) this.session.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
			
			this.fechamentoEncalheService.encerrarOperacaoEncalhe(dataEncalhe, getUsuarioLogado(),filtroSessao);
		
		} catch(ValidacaoException ve){
			throw ve;
		} catch (Exception e) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao tentar encerrar a operação de encalhe! " + e.getMessage()));
		}
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação de encalhe encerrada com sucesso!"),"result").recursive().serialize();
	}
	

	private String resolveSort(String sortname) {
		
		if (sortname.endsWith("Formatado")) {
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
				this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.ERROR, "Você está tentando fazer uma pesquisa em modo consolidado (soma de todos os boxes). Já existem dados salvos em modo de pesquisa por box. Não será possível realizar a pesquisa."), "result").recursive().serialize();
			} else {
				this.result.use(Results.json()).from("pesquisa","result").serialize() ;   
			}
		} else if ( fechamentoEncalheService.existeFechamentoEncalheConsolidado(filtro)){
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Você está tentando fazer uma pesquisa por box. Já existem dados salvos em modo de pesquisa consolidado (soma de todos os boxes). Se você continuar, os dados serão perdidos. Tem certeza que deseja continuar ?"), "result").recursive().serialize();
		} else {
			this.result.use(Results.json()).from("pesquisa","result").serialize() ;   
		 }
	}
	
	@Path("/salvarNoEncerrementoOperacao")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO)
	public void salvarNoEncerrementoOperacao(List<FechamentoFisicoLogicoDTO> listaFechamento, String dataEncalhe, Long fornecedorId, Long boxId) {
		
		if (listaFechamento !=null && !listaFechamento.isEmpty()) {
			
			gravaFechamentoEncalhe(listaFechamento, dataEncalhe, fornecedorId, boxId);
		}
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	private void gravaFechamentoEncalhe(
			List<FechamentoFisicoLogicoDTO> listaFechamento,
			String dataEncalhe, Long fornecedorId, Long boxId) {
		FiltroFechamentoEncalheDTO filtro = new FiltroFechamentoEncalheDTO();
		filtro.setDataEncalhe(DateUtil.parseDataPTBR(dataEncalhe));
		filtro.setFornecedorId(fornecedorId);
		filtro.setBoxId(boxId);
		if (boxId == null){ 
			fechamentoEncalheService.salvarFechamentoEncalhe(filtro,listaFechamento);
		} else {
			fechamentoEncalheService.salvarFechamentoEncalheBox(filtro, listaFechamento);
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
	
	
	@Get
	@Path("/imprimirArquivoAnaliticoEncalhe")
	public void imprimirArquivoAnaliticoEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortname, String sortorder, int rp, int page, FileType fileType) {
		
		List<AnaliticoEncalheDTO> listDTO = fechamentoEncalheService.buscarAnaliticoEncalhe(filtro, sortorder, this.resolveSort(sortname), page, rp);
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
		List<FechamentoFisicoLogicoDTO> listaDeGrid = (List<FechamentoFisicoLogicoDTO>) session.getAttribute("gridFechamentoEncalheDTO");
		
		boolean insercao = true;
		if(listaDeGrid != null)
		{
			for(FechamentoFisicoLogicoDTO linha : listaDeGrid)
			{
				if(linha.getCodigo().equals(Long.parseLong(codigo)))
				{
					linha.setCodigo(codigo);
					linha.setProdutoEdicao(Long.parseLong(produtoEdicao));
					if(fisico != null)
					{
						linha.setFisico(Long.parseLong(fisico));	
					}
					linha.setReplicar(String.valueOf(checkbox));
					
					insercao = false;
				}
			}
		}
		else
		{
			listaDeGrid = new ArrayList<FechamentoFisicoLogicoDTO>();
		}
	
		if(insercao == true)
		{
			FechamentoFisicoLogicoDTO gridFechamentoEncalheDTO = new FechamentoFisicoLogicoDTO();
			
			if(fisico != null)
			{
				gridFechamentoEncalheDTO.setFisico(Long.parseLong(fisico));	
			}
			
			gridFechamentoEncalheDTO.setCodigo(codigo);
			gridFechamentoEncalheDTO.setProdutoEdicao(Long.parseLong(produtoEdicao));
			gridFechamentoEncalheDTO.setReplicar(String.valueOf(checkbox));
			listaDeGrid.add(gridFechamentoEncalheDTO);
		}
		
		
		session.setAttribute("gridFechamentoEncalheDTO", listaDeGrid);		
		this.result.use(Results.nothing());
	}
}
