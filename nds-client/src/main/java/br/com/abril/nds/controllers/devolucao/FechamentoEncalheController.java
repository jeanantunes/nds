package br.com.abril.nds.controllers.devolucao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.DataHolder;
import br.com.abril.nds.client.vo.AnaliticoEncalheVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliticoEncalheDTO;
import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.ExtracaoContaCorrenteDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTOCego;
import br.com.abril.nds.dto.filtro.FiltroExtracaoContaCorrenteDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.SemaforoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.PDFUtil;
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
 * Classe responsável pelo controle das ações referentes à tela de chamadão de
 * publicações.
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
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private DocumentoCobrancaService documentoCobrancaService;
	
	@Autowired
	private SemaforoService semaforoService;
	
	@Autowired
	protected HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaFechamentoEncalhe";
	
	private static final String DATA_HOLDER_ACTION_KEY = "fechamentoEncalhe";
	
	private static final String STATUS_FINALIZADO = "FINALIZADO";
	
	private static final String KEY_COBRANCA_COTAS = "cobrancaCotas";
	
	private static final String SET_NOSSO_NUMERO = "setNossoNumero";
	
	private static final ConcurrentMap<String, String> CACHE_COBRANCA_COTAS = new ConcurrentHashMap<>();
	
	@Path("/")
	public void index() {
		
		List<Fornecedor> listaFornecedores = fornecedorService.obterFornecedores();
		List<Box> listaBoxes = boxService.buscarPorTipo(Arrays.asList(TipoBox.ENCALHE));
		
		result.include("dataOperacao", DateUtil.formatarDataPTBR(this.distribuidorService.obterDataOperacaoDistribuidor()));
		result.include("aceitaJuramentado", this.distribuidorService.aceitaJuramentado());
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
						TipoMensagem.WARNING, "Não houve conferência de encalhe nesta data."), "mensagens")
                    .recursive().serialize();
		} else {
			List<FechamentoFisicoLogicoDTO> listaEncalhe = consultarItensFechamentoEncalhe(dataEncalhe, fornecedorId, boxId, aplicaRegraMudancaTipo, sortname, sortorder, rp, page);

			this.getSession().setAttribute("gridFechamentoEncalheDTO", listaEncalhe);
			
			this.result.use(FlexiGridJson.class).from(listaEncalhe).total(quantidade).page(page).serialize();
		}
	}
	
	private int quantidadeItensFechamentoEncalhe(
			String dataEncalhe, 
			Long fornecedorId, 
			Long boxId,
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
	public void salvar(List<FechamentoFisicoLogicoDTO> listaFechamento, boolean isAllFechamentos, 
					   String dataEncalhe, Long fornecedorId, Long boxId) {
		
		
		listaFechamento = (List<FechamentoFisicoLogicoDTO>) this.getSession().getAttribute("gridFechamentoEncalheDTO");
		
		List<FechamentoFisicoLogicoDTO> todosFechamentos = this.consultarItensFechamentoEncalhe(dataEncalhe, fornecedorId, boxId, false, null, null, 0, 0);
		
		List<FechamentoFisicoLogicoDTO> fechamentos = mergeItensFechamento(todosFechamentos, listaFechamento);

		gravaFechamentoEncalhe(fechamentos, isAllFechamentos, dataEncalhe, fornecedorId, boxId);
		
		this.getSession().removeAttribute("listaDeGrid");
		
        this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Informação gravada com sucesso!"), "result").recursive().serialize();
	}
	
	public int getIndex(List <FechamentoFisicoLogicoDTO> fechamentosBanco, FechamentoFisicoLogicoDTO fechamento) {
           for (FechamentoFisicoLogicoDTO fb : fechamentosBanco) {
        	   if ( fb.getProdutoEdicao().equals(fechamento.getProdutoEdicao()))
        			   return fechamentosBanco.indexOf(fb);
        	   
           }
		return 0; // erro
	}
	
	private List<FechamentoFisicoLogicoDTO> mergeItensFechamento(List<FechamentoFisicoLogicoDTO> fechamentosBanco, List<FechamentoFisicoLogicoDTO> fechamentoTela) {
		
		if (fechamentoTela == null) {
			
			return fechamentosBanco;
		}
		
		for (FechamentoFisicoLogicoDTO fechamento : fechamentoTela) {
			
		//	 int index1 = fechamentosBanco.indexOf(fechamento);
			
			int index = getIndex(fechamentosBanco,fechamento);
			
			if ( index < 0 ) {
				
				LOGGER.error("Erro ao salvar .. Fechamento produtoedicao="+fechamento.getProdutoEdicao() +" digitado na tela nao encontrado no banco");
				throw new ValidacaoException(TipoMensagem.WARNING, "ERRO.Produto na tela ja nao existe na base de dados.Efetuar pesquisa novamente");
				
			}
			LOGGER.warn( "index="+fechamentosBanco.get(index).getSequencia()+" "+index +" produtoid="+fechamentosBanco.get(index).getProdutoEdicao() +" codigo="+
				fechamentosBanco.get(index).getCodigo()+"  qtd="+
				fechamentosBanco.get(index).getFisico());
			
			fechamentosBanco.get(index).setFisico(fechamento.getFisico());
			LOGGER.warn( "index="+fechamentosBanco.get(index).getSequencia()+" "+index +" produtoid="+fechamentosBanco.get(index).getProdutoEdicao() +" codigo="+
					fechamentosBanco.get(index).getCodigo()+"  qtd="+
					fechamentosBanco.get(index).getFisico());
		}
		
		return fechamentosBanco;
	}
	
	@Path("/cotasAusentesConfirmacao")
	public void cotasAusentesConfirmacao(Date dataEncalhe, boolean isSomenteCotasSemAcao, List<Long> idsCotas, String sortname, String sortorder, int rp, int page) {

		List<CotaAusenteEncalheDTO> listaCotasAusenteEncalhe =
			this.fechamentoEncalheService.buscarCotasAusentes(dataEncalhe, isSomenteCotasSemAcao, sortorder, sortname, page, rp);
		
		int total = this.fechamentoEncalheService.buscarTotalCotasAusentes(dataEncalhe, isSomenteCotasSemAcao, null);
		
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
		
		int total = this.fechamentoEncalheService.buscarTotalCotasAusentes(dataEncalhe, isSomenteCotasSemAcao,null);
		
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
	public void carregarDataPostergacao(Date dataEncalhe) {
		
		if(dataEncalhe == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma data de encalhe informada na pesquisa");
		}
		
		Date dataPostergacao = DateUtil.adicionarDias(dataEncalhe, 1);
		
		dataPostergacao = 
			this.calendarioService.adicionarDiasRetornarDiaUtil(dataPostergacao, 0);
		
		if (dataPostergacao != null) {
			
			String dataFormatada = DateUtil.formatarData(dataPostergacao, "dd/MM/yyyy");
			
			this.result.use(Results.json()).from(dataFormatada, "result").recursive().serialize();
			
		} else {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma data util de encalhe encontrada");
		
		}
		
	}
	
	@Path("/postergarCotas")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO)
	public void postergarCotas(Date dataPostergacao, Date dataEncalhe, List<Long> idsCotas, boolean postergarTodasCotas) {
		
		if (dataEncalhe != null && dataEncalhe.after(dataPostergacao)) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Postergação não pode ser realizada antes da data atual!");
		} 
		/**
		 * 09/10/2013
		 * Regra solicitada pela Magali / Rodrigo
		 * Remover a validacao da semana de recolhimento e permitir qualquer data futura em relacao a data de operacao
		 */
		else if (!dataPostergacao.after(distribuidorService.obterDataOperacaoDistribuidor())) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "A Data de Postergação deve ser maior que a data de operação!");
		} 
		
		ValidacaoVO validacaoCotaConferenciaNaoFinalizada = getValidacaoCotaConferenciaNaoFinalizada(dataEncalhe);
		
		if(validacaoCotaConferenciaNaoFinalizada != null) {
			
			this.result.use(Results.json()).withoutRoot().from(validacaoCotaConferenciaNaoFinalizada).recursive().serialize();
			
			return;
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
                // Adiciona as contas com dívida
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
		
	private void removerCotasAusentesLista(List<CotaAusenteEncalheDTO> listaCotasAusentes, List<Long> idsCotas) {
		
		if(idsCotas != null) {
			
			for(Long idCota : idsCotas) {

				listaCotasAusentes.remove(new CotaAusenteEncalheDTO(idCota));
			}
		}
	}
	
	@Post
	public void verificarCobrancaGerada(List<Long> idsCotas, boolean cobrarTodasCotas){
		
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
		ValidacaoVO validacaoCotaConferenciaNaoFinalizada = getValidacaoCotaConferenciaNaoFinalizada(dataOperacao);
		
		if(validacaoCotaConferenciaNaoFinalizada != null) {
			this.result.use(Results.json()).withoutRoot().from(validacaoCotaConferenciaNaoFinalizada).recursive().serialize();
			return;
		}
		
		try {
			
			this.limparStatusCobrancaCotas();
			
			if (cobrarTodasCotas) {
				
				List<CotaAusenteEncalheDTO> listaCotaAusenteEncalhe = 
						this.fechamentoEncalheService.buscarCotasAusentes(dataOperacao, true, null, null, 0, 0);
				
				//idsCotas a serem retirados da lista
				removerCotasAusentesLista(listaCotaAusenteEncalhe, idsCotas);
				
				this.realizarCobrancaTodasCotas(dataOperacao, listaCotaAusenteEncalhe);				
			
			} else {
			
				this.realizarCobrancaCotasEspecificas(idsCotas, dataOperacao);
			}

		} catch (ValidacaoException e) {
            LOGGER.warn(e.getMessage(), e);
			this.result.use(Results.json()).from(e.getValidacao(), "result").recursive().serialize();
			return;
		} catch (GerarCobrancaValidacaoException e) {
            LOGGER.warn(e.getMessage(), e);
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
				
				this.setStatusCobrancaCotas(status);

				this.fechamentoEncalheService.cobrarCota(dataOperacao, getUsuarioLogado(), idCota);

			}	
		} catch (GerarCobrancaValidacaoException e) {
            LOGGER.error(e.getMessage(), e);
			ex = e;

		} catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
			this.setStatusCobrancaCotas(STATUS_FINALIZADO);

			throw e;
			
		} finally {
			
			this.setStatusCobrancaCotas(STATUS_FINALIZADO);
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
				
				this.setStatusCobrancaCotas(status);

				this.fechamentoEncalheService.realizarCobrancaCota(dataOperacao,
												                   getUsuarioLogado(), 
												                   cotaAusenteEncalheDTO.getIdCota(),
												                   validacaoVO);					
			}
		} catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
			
		} finally {
			
			this.setStatusCobrancaCotas(STATUS_FINALIZADO);
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
				boolean confCega = !usuarioPossuiRule(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_CONF_CEGA);
				
				boolean permissaoVisualiza = usuarioPossuiRule(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE);
			
				if (permissaoVisualiza && confCega) {
				FileExporter.to("fechamentos-encalhe", fileType).inHTTPResponse(
					this.getNDSFileHeader(), null, null, listaEncalhe, 
					FechamentoFisicoLogicoDTO.class, this.response);
				} else {
					List<FechamentoFisicoLogicoDTOCego> listaEncalhecego = new ArrayList();
					
					for (FechamentoFisicoLogicoDTO ffl:listaEncalhe) {
						FechamentoFisicoLogicoDTOCego fflcego = new FechamentoFisicoLogicoDTOCego();
						BeanUtils.copyProperties(ffl, fflcego);
						listaEncalhecego.add(fflcego);
					}
					
					
					
					FileExporter.to("fechamentos-encalhecego", fileType).inHTTPResponse(
							this.getNDSFileHeader(), null, null, listaEncalhecego, 
							FechamentoFisicoLogicoDTOCego.class, this.response);
				}
				
				
			} catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao gerar o arquivo!"));
			}
		}
		
		this.result.use(Results.nothing());
	}
	
	@Get
	public void exportarExtracaoCC(FiltroExtracaoContaCorrenteDTO filtro) throws IOException {
		
		if(filtro.getDataDe().equals(filtro.getDataAte())){
			Integer semanaDe = distribuidorService.obterNumeroSemana(filtro.getDataDe());
			
			filtro.setTituloRelatorio(DateUtil.formatarDataPTBR(filtro.getDataDe())+" a "
					+DateUtil.formatarDataPTBR(filtro.getDataAte())+" - SEMANA "+semanaDe.toString().substring(4, 6));
		}else{
			Integer semanaDe = distribuidorService.obterNumeroSemana(filtro.getDataDe());
			Integer semanaAte = distribuidorService.obterNumeroSemana(filtro.getDataAte());
			
			if(semanaDe.equals(semanaAte)){
				filtro.setTituloRelatorio(" - "+DateUtil.formatarDataPTBR(filtro.getDataDe())+" a "
						+DateUtil.formatarDataPTBR(filtro.getDataAte())+" - SEMANA "+semanaDe.toString().substring(4, 6));
			}else{
				filtro.setTituloRelatorio(" - "+DateUtil.formatarDataPTBR(filtro.getDataDe())+" a "
						+DateUtil.formatarDataPTBR(filtro.getDataAte())+" - SEMANA "+semanaDe.toString().substring(4, 6) + " a " + semanaAte.toString().substring(4, 6));
			}
		}
		
		List<ExtracaoContaCorrenteDTO> listExtracoes = this.fechamentoEncalheService.extracaoContaCorrente(filtro);
		
		List<Integer> cotasNaoProcessadas = this.fechamentoEncalheService.extracaoContaCorrente_BuscarCotasObservacoes(filtro);
		
		filtro.setBuscarCotasPostergadas(true);
		List<Integer> cotasPostergadas = this.fechamentoEncalheService.extracaoContaCorrente_BuscarCotasObservacoes(filtro);
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		HSSFSheet aba = workbook.createSheet("Extrato Conta Corrente ");
		
		HSSFRow row;
			
		Map<Integer, Object[]> mapDadosPlanilha = new TreeMap<Integer, Object[]>();
		
		mapDadosPlanilha.put(1, new Object[] {
				"FECHAMENTO ENCALHE "+filtro.getTituloRelatorio().toUpperCase(),"", "", "", "", "", "", "", "", "", "", "" });
		
		mapDadosPlanilha.put(2, new Object[] {
				"", "", "", "", "", "", "", "","", "","", "" });
		
		mapDadosPlanilha.put(3, new Object[] {
				"SM", "CÓDIGO", "PRODUTO", "EDIÇÃO","R$ CAPA", "PP", "DESCONTO", "REPARTE","VENDA DE ENC.", "ENCALHE","VENDA", "R$ VENDA TOTAL"});
		
		Integer id = 4;
		Integer idSubLines;
		Integer idPrimeiroQuadro;
		Integer idSegundoQuadro;
		
		BigInteger totalReparte = BigInteger.ZERO;
		BigInteger totalVendaEnc = BigInteger.ZERO;
		BigInteger totalEncalhe = BigInteger.ZERO;
		BigInteger totalVenda = BigInteger.ZERO;
		BigDecimal totalVendaTotal = BigDecimal.ZERO;
		
		BigInteger qtdRemessa = BigInteger.ZERO;
		BigInteger qtdDevolucao = BigInteger.ZERO;
		BigInteger qtdVenda = BigInteger.ZERO;
		BigDecimal brutoRemessa = BigDecimal.ZERO;
		BigDecimal brutoDevolucao = BigDecimal.ZERO;
		BigDecimal brutoVenda = BigDecimal.ZERO;
		BigDecimal descontoCotaRemessa = BigDecimal.ZERO;
		BigDecimal descontoCotaDevolucao = BigDecimal.ZERO;
		BigDecimal descontoCotaVenda = BigDecimal.ZERO;
		BigDecimal descontoDistribRemessa = BigDecimal.ZERO;
		BigDecimal descontoDistribDevolucao = BigDecimal.ZERO;
		BigDecimal descontoDistribVenda = BigDecimal.ZERO;
		BigDecimal liquidoCotaRemessa = BigDecimal.ZERO;
		BigDecimal liquidoCotaDevolucao = BigDecimal.ZERO;
		BigDecimal liquidoCotaVenda = BigDecimal.ZERO;
		BigDecimal liquidoDistribRemessa = BigDecimal.ZERO;
		BigDecimal liquidoDistribDevolucao = BigDecimal.ZERO;
		BigDecimal liquidoDistribVenda = BigDecimal.ZERO;
		
		for (ExtracaoContaCorrenteDTO dto : listExtracoes) {
			mapDadosPlanilha.put(id, new Object[] {
					dto.getSequenciaMatriz().toString(), 
					dto.getCodigoProduto(),
					dto.getNomeProduto(),
					dto.getNumeroEdicao().toString(),
					formatarCelularValorMonetario(dto.getPrecoCapa()),
					dto.getPacotePadrao().toString(),
					formatarCelularValorMonetario(dto.getDesconto()),
					dto.getReparte().toString(),
					dto.getVendaEncalhe().toString(),
					dto.getEncalhe().toString(),
					dto.getVenda().toString(),
					formatarCelularValorMonetario(dto.getVendaTotal())
					});
			
			//Totais quadros
			
			qtdRemessa = qtdRemessa.add(dto.getReparte());
			qtdDevolucao = qtdDevolucao.add(dto.getEncalhe());
			brutoRemessa = brutoRemessa.add(dto.getPrecoCapa().multiply(new BigDecimal(dto.getReparte())));
			brutoDevolucao = brutoDevolucao.add(dto.getPrecoCapa().multiply(new BigDecimal(dto.getEncalhe())));
			descontoCotaRemessa = descontoCotaRemessa.add(dto.getDescCota().multiply(new BigDecimal(dto.getReparte())));
			descontoCotaDevolucao = descontoCotaDevolucao.add(dto.getDescCota().multiply(new BigDecimal(dto.getEncalhe())));
			descontoDistribRemessa = descontoDistribRemessa.add(dto.getDescLogistica().multiply(new BigDecimal(dto.getReparte())));
			descontoDistribDevolucao = descontoDistribDevolucao.add(dto.getDescLogistica().multiply(new BigDecimal(dto.getEncalhe())));
			
			totalReparte = totalReparte.add(dto.getReparte());
			totalVendaEnc = totalVendaEnc.add(dto.getVendaEncalhe());
			totalEncalhe = totalEncalhe.add(dto.getEncalhe());
			totalVenda = totalVenda.add(dto.getVenda());
			totalVendaTotal = totalVendaTotal.add(dto.getVendaTotal());
			
			++id;
		}
		
		qtdVenda = qtdRemessa.subtract(qtdDevolucao);
		brutoVenda = brutoRemessa.subtract(brutoDevolucao);
		descontoCotaVenda = descontoCotaRemessa.subtract(descontoCotaDevolucao);
		descontoDistribVenda = descontoDistribRemessa.subtract(descontoDistribVenda);
		
		liquidoCotaRemessa = brutoRemessa.subtract(descontoCotaRemessa);
		liquidoCotaDevolucao = brutoDevolucao.subtract(descontoCotaDevolucao);
		liquidoCotaVenda = brutoVenda.subtract(descontoCotaVenda);
		
		liquidoDistribRemessa = brutoRemessa.subtract(descontoDistribRemessa);
		liquidoDistribDevolucao = brutoDevolucao.subtract(descontoDistribDevolucao);
		liquidoDistribVenda = brutoVenda.subtract(descontoDistribVenda);
		
		idSubLines = id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "TOTAL GERAL", "", "", "", "", "", totalReparte.toString(), totalVendaEnc.toString(),
					totalEncalhe.toString(), totalVenda.toString(), formatarCelularValorMonetario(totalVendaTotal)});
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "", "", "", "", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "", "", "", "", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "", "", "", "", "", "","", "","", "" });
		++id;
		
		idPrimeiroQuadro = id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "VISÃO - COTA", "", "", "", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "TOTAL", "REMESSA", "DEVOLUÇÃO", "VENDA", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "QUANTIDADE", qtdRemessa.toString(), qtdDevolucao.toString(), qtdVenda.toString(), "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "BRUTO", formatarCelularValorMonetario(brutoRemessa), formatarCelularValorMonetario(brutoDevolucao), formatarCelularValorMonetario(brutoVenda), "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "DESCONTO_COTA", formatarCelularValorMonetario(descontoCotaRemessa), formatarCelularValorMonetario(descontoCotaDevolucao), formatarCelularValorMonetario(descontoCotaVenda), "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "LIQUIDO", formatarCelularValorMonetario(liquidoCotaRemessa), formatarCelularValorMonetario(liquidoCotaDevolucao), formatarCelularValorMonetario(liquidoCotaVenda), "", "","", "","", "" });
		
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "", "", "", "", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "", "", "", "", "", "","", "","", "" });
		++id;
		
		idSegundoQuadro = id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "VISÃO - DISTRIBUIDOR", "", "", "", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "TOTAL", "REMESSA", "DEVOLUÇÃO", "VENDA", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "QUANTIDADE", qtdRemessa.toString(), qtdDevolucao.toString(), qtdVenda.toString(), "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "BRUTO", formatarCelularValorMonetario(brutoRemessa), formatarCelularValorMonetario(brutoDevolucao), formatarCelularValorMonetario(brutoVenda), "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "DESCONTO_DISTRIBUIDOR", formatarCelularValorMonetario(descontoDistribRemessa), formatarCelularValorMonetario(descontoDistribDevolucao), formatarCelularValorMonetario(descontoDistribVenda), "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "LIQUIDO", formatarCelularValorMonetario(liquidoDistribRemessa), formatarCelularValorMonetario(liquidoDistribDevolucao), formatarCelularValorMonetario(liquidoDistribVenda), "", "","", "","", "" });
		
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "", "", "", "", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "MARGEM DISTRIBUIDOR", "", "", "", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "", "", "", "", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "", "", "", "", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "*Encalhe ainda não processado - Cotas: "+concatenarCotasObservacoes(cotasNaoProcessadas), "", "", "", "", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "", "", "", "", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "", "", "", "", "", "", "","", "","", "" });
		++id;
		
		mapDadosPlanilha.put(id, new Object[] {
				"", "*Encalhe postergado (Fechamento Encalhe) - Cotas: "+concatenarCotasObservacoes(cotasPostergadas), "", "", "", "", "", "","", "","", "" });
		++id;
		
		Set<Integer> keyId = mapDadosPlanilha.keySet();
		
		int rowid = 0;
		
		List<Cell> referenceFirstLine = new ArrayList<>();
		List<Cell> referenceSubHeader = new ArrayList<>();
		
		for (Integer key : keyId) {
			row = aba.createRow(rowid++);
			Object [] objectArr = mapDadosPlanilha.get(key);
			int cellid = 0;
			
			for (Object obj : objectArr) {
				Cell cell = row.createCell(cellid++);
				cell.setCellValue((String)obj);
				
				aba.autoSizeColumn(cell.getColumnIndex());
				
				if(key.equals(1) || key.equals(2)){
					referenceFirstLine.add(cell);
					continue;
				}
				
				if(key.equals(3)){
					referenceSubHeader.add(cell);
					continue;
				}
				
				if(key.equals(idSubLines)){
			        cell.setCellStyle(getStyleSubLines(aba));
			        continue;
				}
				
				if(key <= (listExtracoes.size()+3)){
					CellStyle style = aba.getWorkbook().createCellStyle();
			        setBorderPadrao(style);
					cell.setCellStyle(style);
				}
				
				if(key.equals(idPrimeiroQuadro+1)){
					CellStyle cellStyle = aba.getWorkbook().createCellStyle();
					cellStyle.setFont(getFontPadrao(aba, 10));
					cell.setCellStyle(cellStyle);
				}
				
			}
		}
		
		for (Cell cell : referenceFirstLine){
			CellStyle cellStyle = this.getHeaderColumnCellStyle(aba);
			cell.setCellStyle(cellStyle);
		}
		
		for (Cell cell : referenceSubHeader) {
			CellStyle cellStyle = this.getSubHeaderColumnCellStyle(aba);
			cell.setCellStyle(cellStyle);
		}
		
		// getStyleHeaderPrimeiroQuadro
		aba.addMergedRegion(CellRangeAddress.valueOf("C"+idPrimeiroQuadro+":F"+idPrimeiroQuadro));
		aba.addMergedRegion(CellRangeAddress.valueOf("C"+idSegundoQuadro+":F"+idSegundoQuadro));
		
		this.setStyleHeaderPrimeiroQuadro(aba, idPrimeiroQuadro);
		this.setStyleSubHeaderPrimeiroQuadro(aba, idPrimeiroQuadro);
		
		this.setStyleHeaderSegundoQuadro(aba, idSegundoQuadro);
		this.setStyleSubHeaderSegundoQuadro(aba, idSegundoQuadro);
		
		this.setStyleLinhasQuadro(aba, idPrimeiroQuadro);
		this.setStyleLinhasQuadro(aba, idSegundoQuadro);
		
		this.setStyleLinhasResumo(aba, idSegundoQuadro);
		
		this.setFormulaMargemDistribuidor(aba, idSegundoQuadro);
		
		this.setFormatCurrencyRangerCell(aba, "D"+(idPrimeiroQuadro+3)+":F"+(idPrimeiroQuadro+5));
		
		this.setFormatCurrencyRangerCell(aba, "D"+(idSegundoQuadro+3)+":F"+(idSegundoQuadro+5));
		
		this.setFormatLineFrameRangerCell(aba, "D"+(idPrimeiroQuadro+2)+":F"+(idPrimeiroQuadro+2));
		
		this.setFormatLineFrameRangerCell(aba, "D"+(idSegundoQuadro+2)+":F"+(idSegundoQuadro+2));
		
		this.setObservacoes(aba, idSegundoQuadro+10);
		
		this.setObservacoes(aba, idSegundoQuadro+13);
		
		this.autoSizeColumns(workbook);
		
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=Extracao_conta_corrente.xls");
		
		OutputStream output;

        output = this.httpResponse.getOutputStream();
        
        ByteArrayOutputStream outBA = new ByteArrayOutputStream();
        workbook.write(outBA);
		
        output.write(outBA.toByteArray());
        
        output.flush();
        output.close();

        httpResponse.getOutputStream().close();
		
	}
	
	private String formatarCelularValorMonetario(BigDecimal valor){
		
		return CurrencyUtil.arredondarValorParaDuasCasas(valor).toString().replace('.', ',');
	}
	
	private String concatenarCotasObservacoes(List<Integer> cotas){
		
		String cotasConcatenadas = "";
		
		if(!cotas.isEmpty()){
			for (int i = 0; i < cotas.size(); i++) {
				if(i==0){
					cotasConcatenadas += ""+cotas.get(i);
				}else{
					cotasConcatenadas += ", "+cotas.get(i);
				}
				if(i == (cotas.size()-1)){
					cotasConcatenadas += ".";
				}
			}
		}
		
		return cotasConcatenadas;
	}

	private void setStyleHeaderPrimeiroQuadro(HSSFSheet aba, Integer idPrimeiroQuadro) {
		
		CellRangeAddress region = CellRangeAddress.valueOf("C"+idPrimeiroQuadro+":F"+idPrimeiroQuadro);
		
		setStyleHeaderQuadro(aba, region, false, true);
	}
	
	private void setStyleSubHeaderPrimeiroQuadro(HSSFSheet aba, Integer idPrimeiroQuadro) {
		
		CellRangeAddress region = CellRangeAddress.valueOf("C"+(idPrimeiroQuadro+1)+":G"+(idPrimeiroQuadro+1));
		
		setStyleHeaderQuadro(aba, region, true, true);
	}
	
	private void setStyleHeaderSegundoQuadro(HSSFSheet aba, Integer idPrimeiroQuadro) {
		
		CellRangeAddress region = CellRangeAddress.valueOf("C"+idPrimeiroQuadro+":F"+idPrimeiroQuadro);
		
		setStyleHeaderQuadro(aba, region, false, false);
	}
	
	private void setStyleSubHeaderSegundoQuadro(HSSFSheet aba, Integer idPrimeiroQuadro) {
		
		CellRangeAddress region = CellRangeAddress.valueOf("C"+(idPrimeiroQuadro+1)+":G"+(idPrimeiroQuadro+1));
		
		setStyleHeaderQuadro(aba, region, true, false);
	}

	private void setStyleHeaderQuadro(HSSFSheet aba, CellRangeAddress region, boolean isSubHead, boolean isPrimeiro) {
		
		CellStyle style = aba.getWorkbook().createCellStyle();
		
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
		style.setFont(getFontPadrao(aba, 10));
		
		if(isPrimeiro){
			style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		}else{
			style.setFillForegroundColor(HSSFColor.LIME.index);
		}
		
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		if(isSubHead){
			style.setBorderBottom(CellStyle.BORDER_THIN);
		}
		
		for(int i=region.getFirstRow();i<=region.getLastRow();i++){
			Row rowRager = aba.getRow(i);
			for(int j=region.getFirstColumn();j<region.getLastColumn();j++){
				Cell cellRanger = rowRager.getCell(j);
				cellRanger.setCellStyle(style);
			}
		}
	}
	
	private void setStyleLinhasQuadro(HSSFSheet aba, Integer idPrimeiroQuadro) {
		
		CellRangeAddress region = CellRangeAddress.valueOf("C"+(idPrimeiroQuadro+2)+":C"+(idPrimeiroQuadro+5));
		
		CellStyle style = aba.getWorkbook().createCellStyle();
		
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
		
		style.setBorderBottom(CellStyle.BORDER_THIN);
		
		for(int i=region.getFirstRow();i<=region.getLastRow();i++){
			Row rowRager = aba.getRow(i);
			for(int j=region.getFirstColumn();j<=region.getLastColumn();j++){
				Cell cellRanger = rowRager.getCell(j);
				cellRanger.setCellStyle(style);
			}
		}
	}
	
	private void setStyleLinhasResumo(HSSFSheet aba, Integer idSegundoQuadro) {
		
		CellRangeAddress region = CellRangeAddress.valueOf("C"+(idSegundoQuadro+7)+":F"+(idSegundoQuadro+7));
		
		CellStyle style = aba.getWorkbook().createCellStyle();
		
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		style.setFont(getFontPadrao(aba, 10));
		
		for(int i=region.getFirstRow();i<=region.getLastRow();i++){
			Row rowRager = aba.getRow(i);
			for(int j=region.getFirstColumn();j<=region.getLastColumn();j++){
				Cell cellRanger = rowRager.getCell(j);
				cellRanger.setCellStyle(style);
			}
		}
	}
	
	private void setFormulaMargemDistribuidor(HSSFSheet aba, Integer idSegundoQuadro) {
		
		CellRangeAddress region = CellRangeAddress.valueOf("F"+(idSegundoQuadro+7)+":F"+(idSegundoQuadro+7));
		
		CellStyle style = aba.getWorkbook().createCellStyle();
		
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		style.setFont(getFontPadrao(aba, 10));
		
		DataFormat df = aba.getWorkbook().createDataFormat();
		style.setDataFormat(df.getFormat("$#.###,##"));
		
		for(int i=region.getFirstRow();i<=region.getLastRow();i++){
			Row rowRager = aba.getRow(i);
			for(int j=region.getFirstColumn();j<=region.getLastColumn();j++){
				Cell cellRanger = rowRager.getCell(j);
				cellRanger.setCellFormula("F"+(idSegundoQuadro+5)+"-F"+(idSegundoQuadro-3));
				cellRanger.setCellStyle(style);
			}
		}
	}
	
	private void setFormatCurrencyRangerCell(HSSFSheet aba, String rangerCell) {
		
		CellRangeAddress region = CellRangeAddress.valueOf(rangerCell);
		
		CellStyle style = aba.getWorkbook().createCellStyle();
		
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
		
		style.setBorderBottom(CellStyle.BORDER_THIN);
		
		DataFormat df = aba.getWorkbook().createDataFormat();
		style.setDataFormat(df.getFormat("$#.###,##"));
		
		for(int i=region.getFirstRow();i<=region.getLastRow();i++){
			Row rowRager = aba.getRow(i);
			for(int j=region.getFirstColumn();j<=region.getLastColumn();j++){
				Cell cellRanger = rowRager.getCell(j);
				cellRanger.setCellFormula(1+"*"+new BigDecimal(cellRanger.getStringCellValue().replace(',', '.')));
				cellRanger.setCellStyle(style);
			}
		}
	}
	
	private void setFormatLineFrameRangerCell(HSSFSheet aba, String rangerCell) {
		
		CellRangeAddress region = CellRangeAddress.valueOf(rangerCell);
		
		CellStyle style = aba.getWorkbook().createCellStyle();
		
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
		
		style.setBorderBottom(CellStyle.BORDER_THIN);
		
		for(int i=region.getFirstRow();i<=region.getLastRow();i++){
			Row rowRager = aba.getRow(i);
			for(int j=region.getFirstColumn();j<=region.getLastColumn();j++){
				Cell cellRanger = rowRager.getCell(j);
				cellRanger.setCellStyle(style);
			}
		}
	}
	
	private CellStyle getStyleSubLines(HSSFSheet aba) {
		CellStyle style = aba.getWorkbook().createCellStyle();
		
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		style.setFont(getFontPadrao(aba, 10));
		
		return style;
	}
	
	private CellStyle getHeaderColumnCellStyle(HSSFSheet sheet) {

		sheet.addMergedRegion(CellRangeAddress.valueOf("A1:L2"));
		
		final CellStyle style = sheet.getWorkbook().createCellStyle();

        style.setFillForegroundColor(HSSFColor.WHITE.index);

        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
        
        style.setFont(getFontPadrao(sheet, 13));
        
		return style;
	}
	
	private CellStyle getSubHeaderColumnCellStyle(HSSFSheet sheet) {

		final CellStyle style = sheet.getWorkbook().createCellStyle();

        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);

        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        
        setBorderPadrao(style);
        
        style.setAlignment((short)1);

        style.setFont(getFontPadrao(sheet, 10));
		
		return style;
	}
	
	private void setObservacoes(HSSFSheet aba, Integer idObservacoes) {
		
		aba.addMergedRegion(CellRangeAddress.valueOf("B"+idObservacoes+":L"+(idObservacoes+1)));

		CellRangeAddress region = CellRangeAddress.valueOf("B"+idObservacoes+":L"+idObservacoes);
		
		CellStyle style = aba.getWorkbook().createCellStyle();
		style.setWrapText(true);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
//		style.setAlignment(CellStyle.ALIGN_RIGHT);
		
		for(int i=region.getFirstRow();i<=region.getLastRow();i++){
			Row rowRager = aba.getRow(i);
			for(int j=region.getFirstColumn();j<=region.getLastColumn();j++){
				Cell cellRanger = rowRager.getCell(j);
				cellRanger.setCellStyle(style);
			}
		}
	}
	
	private void setBorderPadrao(final CellStyle style) {
		style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
	}

	private Font getFontPadrao(HSSFSheet sheet, int sizeFont) {
		
		final Font font = sheet.getWorkbook().createFont();
        
        font.setFontName("Arial");
        font.setFontHeightInPoints((short)sizeFont);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setItalic(false);
        font.setColor(HSSFColor.BLACK.index);
        
		return font;
	}
	
	private void autoSizeColumns(Workbook workbook) {
	    int numberOfSheets = workbook.getNumberOfSheets();
	    for (int i = 0; i < numberOfSheets; i++) {
	        Sheet sheet = workbook.getSheetAt(i);
	        if (sheet.getPhysicalNumberOfRows() > 0) {
	            Row row = sheet.getRow(0);
	            Iterator<Cell> cellIterator = row.cellIterator();
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                int columnIndex = cell.getColumnIndex();
	                sheet.autoSizeColumn(columnIndex);
	            }
	        }
	    }
	}
	
	@Path("/encerrarOperacaoEncalhe")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO)
	public void encerrarOperacaoEncalhe(Date dataEncalhe) {
		
		fechamentoEncalheService.verificarEstoqueProdutoNaoAtualizado();
		
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
			
			Set<String> nossoNumero = this.fechamentoEncalheService.encerrarOperacaoEncalhe(dataEncalhe, getUsuarioLogado(), filtroSessao, listaEncalhe, true);
			
			this.session.setAttribute(SET_NOSSO_NUMERO, nossoNumero);
			
		} catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao tentar encerrar a operação de encalhe!"));
		}
		
        this.result.use(Results.json()).from(
                new ValidacaoVO(TipoMensagem.SUCCESS, "Operação de encalhe encerrada com sucesso!"), "result")
                .recursive().serialize();
	}
	
	    /**
     * Cria um objeto {@link ValidacaoVO} com informações das cotas que possuem
     * conferencia de encalhe não finalizada.
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
			return validacao;
		}
		List<CotaDTO> listaCotaConferenciaPendenciaErro = fechamentoEncalheService.obterListaCotaConferenciaPendenciaErro(dataEncalhe);
		
		if(listaCotaConferenciaPendenciaErro!=null && !listaCotaConferenciaPendenciaErro.isEmpty()) {
			
			StringBuffer msg = new StringBuffer();
            msg.append("A seguintes cotas estao pendentes na tela  Status Processo Encalhe: ");
			
			for(CotaDTO cota : listaCotaConferenciaPendenciaErro) {
				
				msg.append("<br/>");
				msg.append(" [").
				append(cota.getNumeroCota())
				.append("] ")
				.append(" - ")
				.append(cota.getNomePessoa());
				
			}
			
			validacao = new ValidacaoVO(TipoMensagem.WARNING, msg.toString());
			return validacao;
		}
		
		return validacao;
	}
	
	    /**
     * Caso a operação realizada seja de VERIFICACAO
     * 
     * Serão realizadas validações retornando para view mensagens de sucesso ou
     * erro em relação a estas validações. As validações são:
     * 
     * - Verifica se existem cotas com conferencia não finalizada Caso existam
     * retorna mensagem de WARNING com uma lista destas cota.
     * 
     * - Verifica se existem cota ausentes retornando uma mensagem informando se
     * foram encontradas ou não cotas ausentes.
     * 
     * 
     * Caso a operação seja de CONFIRMACAO
     * 
     * Sera realizada uma validação (Se existem cotas com conferencia não
     * finalizada). Caso a validação seja de sucesso sera efetuado o
     * encerramento do encalhe
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
			
			Set<String> nossoNumero = this.fechamentoEncalheService.encerrarOperacaoEncalhe(dataEncalhe, getUsuarioLogado(), filtroSessao, listaEncalhe, true);
			
			this.session.setAttribute(SET_NOSSO_NUMERO, nossoNumero);
			
			final Map<String,Object> retornoFechamento = new HashMap<>();
			retornoFechamento.put("isImprimirBoletos", !nossoNumero.isEmpty());
			retornoFechamento.put("mensagem",  new ValidacaoVO(TipoMensagem.SUCCESS, "Operação de encalhe encerrada com sucesso!"));
			
			result.use(CustomJson.class).put("result", retornoFechamento).serialize();
		
		} catch(ValidacaoException ve){
			
			throw ve;
			
		} catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
                    "Erro ao tentar encerrar a operação de encalhe! " + e.getMessage()));
		}
	}

	private void validarObrigatoriedadeQuantidadeFisica(List<FechamentoFisicoLogicoDTO> listaEncalhe, boolean isAllFechamentos) {
        
	    if (!isAllFechamentos) {
	    
    	    for (FechamentoFisicoLogicoDTO dto : listaEncalhe) {
    	        
    	        if (!Boolean.valueOf(dto.getReplicar()) && dto.getFisico() == null) {
    	            
    	            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "É necessário informar o valor físico para todas as edições!"));
    	        }
    	    }
	    }
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
				
                String msgPesquisaConsolidado = "Você está tentando fazer uma "
                    + "pesquisa em modo consolidado (soma de todos os boxes). " 
                	+ "Já existem dados salvos em modo de pesquisa por box. "
                    + "Se você continuar, os dados serão consolidados. " 
                	+ "Tem certeza que deseja continuar ?";
				
				this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, msgPesquisaConsolidado), "result").recursive().serialize();
			
			} else {
				
				this.result.use(Results.json()).from("pesquisa","result").serialize() ;   
			
			}
		
		} else if ( fechamentoEncalheService.existeFechamentoEncalheConsolidado(filtro)){
			
            String msgPesquisaPorBox = "Você está tentando fazer uma pesquisa por box. "
                + "Já existem dados salvos em modo de pesquisa consolidado (soma de todos os boxes). "
                + "Não será possível realizar a pesquisa.";
			
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.ERROR, msgPesquisaPorBox), "result").recursive().serialize();
		
		} else {
		
			this.result.use(Results.json()).from("pesquisa","result").serialize() ;   
		
		}
	}
	
	@Path("/salvarNoEncerrementoOperacao")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_ENCALHE_ALTERACAO)
	public void salvarNoEncerrementoOperacao(List<FechamentoFisicoLogicoDTO> listaFechamento, 
											 boolean isAllFechamentos, String dataEncalhe, 
											 Long fornecedorId, Long boxId) {
		
		fechamentoEncalheService.verificarEstoqueProdutoNaoAtualizado();
		
		listaFechamento = (List<FechamentoFisicoLogicoDTO>) this.getSession().getAttribute("gridFechamentoEncalheDTO");
		
		listaFechamento = this.mergeItensFechamento(this.consultarItensFechamentoEncalhe(dataEncalhe, fornecedorId, boxId, false, null, null, 0, 0), listaFechamento);

		this.validarObrigatoriedadeQuantidadeFisica(listaFechamento, isAllFechamentos);
		
		if (listaFechamento !=null && !listaFechamento.isEmpty()) {
			
			gravaFechamentoEncalhe(listaFechamento, isAllFechamentos, dataEncalhe, fornecedorId, boxId);
		}

		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	private void gravaFechamentoEncalhe(
			List<FechamentoFisicoLogicoDTO> listaFechamento,
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
                LOGGER.error(e.getMessage(), e);
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
				if(linha != null && linha.getCodigo() != null && linha.getCodigo().equals(codigo) && linha.getProdutoEdicao().equals(Long.parseLong(produtoEdicao)) ){
					linha.setCodigo(codigo);
					linha.setProdutoEdicao(Long.parseLong(produtoEdicao));
					if(fisico != null){
						linha.setFisico(new BigInteger(fisico));	
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
				gridFechamentoEncalheDTO.setFisico(new BigInteger(fisico));	
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
	
	@SuppressWarnings("unchecked")
	@Get
	public void imprimirBoletosCotasAusentes() throws ValidationException, IOException{
		
		Set<String> setNossoNumero = (Set<String>) this.session.getAttribute(SET_NOSSO_NUMERO);
		
		if (setNossoNumero == null || setNossoNumero.isEmpty()){
			
			result.nothing();
			return;
		}
		
		List<byte[]> docs = new ArrayList<byte[]>();
		
		for (String nossoNumero : setNossoNumero){
		    
		    docs.add(this.documentoCobrancaService.gerarDocumentoCobranca(nossoNumero, false));
		}
		
		byte[] dados = PDFUtil.mergePDFs(docs);
		
		this.response.setContentType("application/pdf");
		this.response.setHeader("Content-Disposition", "attachment; filename=boletosCotasUnificadas_" +
				Calendar.getInstance().getTimeInMillis() + ".pdf");
		
		OutputStream out = this.response.getOutputStream();
		out.write(dados);
		out.flush();
		out.close();
		
		result.nothing();
		
		this.session.removeAttribute(SET_NOSSO_NUMERO);
	}

	private HttpSession getSession() {

		return this.session;
	}
	
	private String getStatusCobrancaCotas() {
		
		return CACHE_COBRANCA_COTAS.get(KEY_COBRANCA_COTAS);
	}
	
	private void setStatusCobrancaCotas(String status) {
		
		CACHE_COBRANCA_COTAS.put(KEY_COBRANCA_COTAS, status);
	}
	
	private void limparStatusCobrancaCotas() {
		
		CACHE_COBRANCA_COTAS.clear();
	}
	
}
