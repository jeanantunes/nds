package br.com.abril.nds.controllers.administracao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.report.RelatorioFechamentoDiario;
import br.com.abril.nds.client.vo.DetalheCotaFechamentoDiarioVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaResumoDTO;
import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.FecharDiaDTO;
import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO.TipoResumo;
import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.dto.fechamentodiario.DividaDTO;
import br.com.abril.nds.dto.fechamentodiario.FechamentoDiarioDTO;
import br.com.abril.nds.dto.fechamentodiario.ResumoEstoqueDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoReparteDTO;
import br.com.abril.nds.dto.fechamentodiario.TipoDivida;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.FecharDiaService;
import br.com.abril.nds.service.ResumoEncalheFecharDiaService;
import br.com.abril.nds.service.ResumoReparteFecharDiaService;
import br.com.abril.nds.service.ResumoSuplementarFecharDiaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.JasperUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.InputStreamDownload;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/fecharDia")
@Rules(Permissao.ROLE_ADMINISTRACAO_FECHAR_DIA)
public class FecharDiaController extends BaseController {
    
    private static final Logger LOGGER = Logger.getLogger(FecharDiaController.class);
	
	@Autowired
	private FecharDiaService fecharDiaService;
	
	@Autowired
	private ResumoReparteFecharDiaService resumoFecharDiaService;
	
	@Autowired
	private ResumoEncalheFecharDiaService resumoEncalheFecharDiaService;
	
	@Autowired
	private ResumoSuplementarFecharDiaService resumoSuplementarFecharDiaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private FechamentoEncalheService fechamentoEncalheService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private HttpServletRequest httpRequest;
	
	@Autowired
	private HttpSession session;
	
	private static Date dataOperacao;

	private static final String FECHAMENTO_DIARIO_DTO_SESSION_KEY = "FECHAMENTO_DIARIO_DTO_SESSION_KEY"; 
	
	private static final String FECHAMENTO_DIARIO_REPORT_EXPORT_NAME = "relatorio-fechamento-diario.pdf";
	
	private static final String INACTIVE_INTERVAL = "inactive_interval";
	
	@Path("/")
	public void index(){
		dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		result.include("dataOperacao", DateUtil.formatarData(dataOperacao, Constantes.DATE_PATTERN_PT_BR));
	}
	
	@Post
	@Rules(Permissao.ROLE_ADMINISTRACAO_FECHAR_DIA_ALTERACAO)
	public void inicializarValidacoes(Date data){
		
		FecharDiaDTO dto = new FecharDiaDTO();
		
		dto.setFechamentoRealizadoNaData(fecharDiaService.isDiaComFechamentoRealizado(data));
		
		if (data != null && !data.equals(dataOperacao)) {
			
			dataOperacao = data;
			
		} else {
			
			this.fecharDiaService.setLockBancoDeDados(true);
		}
		
		if(!dto.getFechamentoRealizadoNaData()){
			
            LOGGER.info("FAZENDO VALIDAÇÕES");
			
			dto.setBaixaBancaria(!this.fecharDiaService.existeCobrancaParaFecharDia(dataOperacao));
			dto.setRecebimentoFisico(this.fecharDiaService.existeNotaFiscalSemRecebimentoFisico(dataOperacao));
			dto.setConfirmacaoDeExpedicao(!this.fecharDiaService.existeConfirmacaoDeExpedicao(dataOperacao));
			dto.setLancamentoFaltasESobras(this.fecharDiaService.existeLancamentoFaltasESobrasPendentes(dataOperacao));
			dto.setControleDeAprovacao(this.distribuidorService.utilizaControleAprovacao());
			dto.setFechamentoEncalhe(this.fechamentoEncalheService.validarEncerramentoOperacaoEncalhe(dataOperacao));
			dto.setConsolidadoCota(this.fecharDiaService.isConsolidadoCotaAVista(data));
			
			dto.setHabilitarConfirmar(
					dataOperacao.equals(this.distribuidorService.obterDataOperacaoDistribuidor())
					&& (dto.isFechamentoPermitido()));
		}
		
		result.use(Results.json()).withoutRoot().from(dto).recursive().serialize();
	}
	
	@Post
	@Path("/obterRecebimentoFisicoNaoConfirmado")
	public void obterRecebimentoFisicoNaoConfirmado(){
		
		List<ValidacaoRecebimentoFisicoFecharDiaDTO> listaRecebimentoFisicoNaoConfirmado = 
				this.fecharDiaService.obterNotaFiscalComRecebimentoFisicoNaoConfirmado(dataOperacao);
		
		TableModel<CellModelKeyValue<ValidacaoRecebimentoFisicoFecharDiaDTO>> tableModel = 
				new TableModel<CellModelKeyValue<ValidacaoRecebimentoFisicoFecharDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaRecebimentoFisicoNaoConfirmado));
		
		tableModel.setTotal(listaRecebimentoFisicoNaoConfirmado.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	@Path("/obterConfirmacaoDeExpedicao")
	public void obterConfirmacaoDeExpedicao(){
		
		List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> listaConfirmacaoDeExpedicao = 
				this.fecharDiaService.obterConfirmacaoDeExpedicao(dataOperacao);
		
		TableModel<CellModelKeyValue<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO>> tableModel = 
				new TableModel<CellModelKeyValue<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConfirmacaoDeExpedicao));
		
		tableModel.setTotal(listaConfirmacaoDeExpedicao.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
    // Grid que é acionado nas validações
	@Post
	@Path("/obterLancamentoFaltaESobra")
	public void obterLancamentoFaltaESobra(){
		
		List<ValidacaoLancamentoFaltaESobraFecharDiaDTO> listaLancamentoFaltaESobra = 
				this.fecharDiaService.obterLancamentoFaltasESobras(dataOperacao);
		
		TableModel<CellModelKeyValue<ValidacaoLancamentoFaltaESobraFecharDiaDTO>> tableModel = 
				new TableModel<CellModelKeyValue<ValidacaoLancamentoFaltaESobraFecharDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentoFaltaESobra));
		
		tableModel.setTotal(listaLancamentoFaltaESobra.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post
	@Path("validacoesDoControleDeAprovacao")
	public void validacoesDoControleDeAprovacao(){
		
		Boolean pendencia = this.fecharDiaService.existePendenciasDeAprovacao(dataOperacao);
		
		this.result.use(Results.json()).from(pendencia).recursive().serialize();
	}
	
	@Post
	@Path("obterResumoQuadroReparte")
	public void obterResumoQuadroReparte(){
		SumarizacaoReparteDTO dto = resumoFecharDiaService.obterSumarizacaoReparte(dataOperacao);
		result.use(CustomMapJson.class).put("result", dto).serialize();
	}
	
	@Post
	@Path("obterResumoQuadroEncalhe")
	public void obterResumoQuadroEncalhe(){
		
		ResumoEncalheFecharDiaDTO dto = 
				this.resumoEncalheFecharDiaService.obterResumoGeralEncalhe(dataOperacao);
		
		result.use(CustomMapJson.class).put("result", dto).serialize();
	}
	
	@Post
	@Path("obterResumoQuadroSuplementar")
	public void obterResumoQuadroSuplementar(){
		
		ResumoSuplementarFecharDiaDTO dto = 
				this.resumoSuplementarFecharDiaService.obterResumoGeralSuplementar(dataOperacao);
		
		result.use(CustomMapJson.class).put("result", dto).serialize();
	}
	
	@Post
	@Path("/obterGridReparte")
	public void obterGridReparte(Integer page, Integer rp){
	    PaginacaoVO paginacao = new PaginacaoVO(page, rp, null);
		List<ReparteFecharDiaDTO> listaReparte = resumoFecharDiaService.obterResumoReparte(dataOperacao, paginacao);
		Long countReparte = resumoFecharDiaService.contarLancamentosExpedidos(dataOperacao);
		result.use(FlexiGridJson.class).from(listaReparte).page(page).total(countReparte.intValue()).serialize();    
	}
	
	@Post
	@Path("/obterGridEncalhe")
	public void obterGridEncalhe(Integer page, Integer rp){
	    PaginacaoVO paginacao = new PaginacaoVO(page, rp, null);
	    List<EncalheFecharDiaDTO> listaEncalhe = resumoEncalheFecharDiaService.obterDadosGridEncalhe(dataOperacao, paginacao);
	    Long countEncalhe = resumoEncalheFecharDiaService.contarProdutoEdicaoEncalhe(dataOperacao);
		result.use(FlexiGridJson.class).from(listaEncalhe).page(page).total(countEncalhe.intValue()).serialize();    
	}
	
	@Post
	@Path("/obterGridSuplementar")
	public void obterGridSuplementar(Integer page, Integer rp){		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, null);		
		List<SuplementarFecharDiaDTO> listaSuplementar = this.resumoSuplementarFecharDiaService.obterDadosGridSuplementar(dataOperacao, paginacao);		
		Long countSuplementar = this.resumoSuplementarFecharDiaService.contarProdutoEdicaoSuplementar(dataOperacao);		
		result.use(FlexiGridJson.class).from(listaSuplementar).page(page).total(countSuplementar.intValue()).serialize();		
	}
	

	@Post
	@Path("/obterGridVenda")
	public void obterGridVenda(String tipoVenda, Integer page, Integer rp){
		List<VendaFechamentoDiaDTO> lista = null;
		Long total = Long.valueOf(0);
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, null);
		if(tipoVenda.equals("encalhe")) {
			lista = resumoEncalheFecharDiaService.obterDadosVendaEncalhe(dataOperacao, paginacao);
			total = resumoEncalheFecharDiaService.contarVendasEncalhe(dataOperacao);
		} else if(tipoVenda.equals("suplementar")){
			lista = resumoSuplementarFecharDiaService.obterVendasSuplementar(dataOperacao,paginacao);
			total = resumoSuplementarFecharDiaService.contarVendasSuplementar(dataOperacao);
		}
		result.use(FlexiGridJson.class).from(lista).page(page).total(total.intValue()).serialize();    
	}
	
	@Get
	public void exportarResumoReparte(FileType fileType){
		
		try {
		List<ReparteFecharDiaDTO> listaReparte = this.resumoFecharDiaService.obterResumoReparte(dataOperacao, null);
		
		if(listaReparte.isEmpty()) {
                throw new ValidacaoException(TipoMensagem.WARNING, "A última pesquisa realizada não obteve resultado.");
		}
		
			FileExporter.to("resumo_reparte", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaReparte, ReparteFecharDiaDTO.class, this.httpResponse);
		} catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
		}
		
		result.nothing();
	}
	
	@Get
	public void exportarResumoEncalhe(FileType fileType){
		
		try {
		
		List<EncalheFecharDiaDTO> listaEncalhe = 
				this.resumoEncalheFecharDiaService.obterDadosGridEncalhe(dataOperacao, null);
		
		if(listaEncalhe.isEmpty()) {
                throw new ValidacaoException(TipoMensagem.WARNING, "A última pesquisa realizada não obteve resultado.");
		}
		
			FileExporter.to("resumo_encalhe", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaEncalhe, EncalheFecharDiaDTO.class, this.httpResponse);
		} catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
		}
		
		result.nothing();
	}
	
	@Get
	public void exportarResumoSuplementar(FileType fileType){
		
		try {
			
			List<SuplementarFecharDiaDTO> listaSuplementar = 
					this.resumoSuplementarFecharDiaService.obterDadosGridSuplementar(dataOperacao, null);
		
		if(listaSuplementar.isEmpty()) {
                throw new ValidacaoException(TipoMensagem.WARNING, "A última pesquisa realizada não obteve resultado.");
		}
		
			FileExporter.to("resumo_reparte", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaSuplementar, SuplementarFecharDiaDTO.class, this.httpResponse);
		} catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
		}
		
		result.nothing();
	}
	
	@Post
	@Path("/exportarVendaSuplemntar")
	public void exportarVendaSuplemntar(FileType fileType, String tipoVenda){		
		try {
			
			List<VendaFechamentoDiaDTO> listaVenda = null;
			
			if(tipoVenda.equals("encalhe")){
				
				listaVenda = resumoEncalheFecharDiaService.obterDadosVendaEncalhe(dataOperacao, null);
			}else if(tipoVenda.equals("suplementar"))
				
				listaVenda = resumoSuplementarFecharDiaService.obterVendasSuplementar(dataOperacao,null);
			if(listaVenda.isEmpty()) {
				
                throw new ValidacaoException(TipoMensagem.WARNING, "A última pesquisa realizada não obteve resultado.");
			}
			
			FileExporter.to("venda_" + tipoVenda, fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaVenda, VendaFechamentoDiaDTO.class, this.httpResponse);
		} catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
		}
	}
	
	@Get
	public void exportarRecebimentoFisico(FileType fileType){
		
		try {
		List<ValidacaoRecebimentoFisicoFecharDiaDTO> listaRecebimentoFisicoNaoConfirmado = 
				this.fecharDiaService.obterNotaFiscalComRecebimentoFisicoNaoConfirmado(dataOperacao);
		
		if(listaRecebimentoFisicoNaoConfirmado.isEmpty()) {
                throw new ValidacaoException(TipoMensagem.WARNING, "A última pesquisa realizada não obteve resultado.");
		}
		
			FileExporter.to("recebimento_fisico", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaRecebimentoFisicoNaoConfirmado, ValidacaoRecebimentoFisicoFecharDiaDTO.class, this.httpResponse);
		} catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
		}
		
		result.nothing();
	}
	
	@Post
	public void obterSumarizacaoDividas() {
        
		Map<TipoDivida, List<SumarizacaoDividasDTO>> sumarizacao = 
				new HashMap<TipoDivida, List<SumarizacaoDividasDTO>>();
        
        List<SumarizacaoDividasDTO> aReceber = fecharDiaService.sumarizacaoDividasReceberEm(dataOperacao);
        List<SumarizacaoDividasDTO> aVencer = fecharDiaService.sumarizacaoDividasVencerApos(dataOperacao);
        
        sumarizacao.put(TipoDivida.DIVIDA_A_RECEBER, aReceber);
        sumarizacao.put(TipoDivida.DIVIDA_A_VENCER, aVencer);
        
        result.use(CustomMapJson.class).put("sumarizacao", sumarizacao).serialize();
    }
	
	@Post
	public void obterDividasReceber(Integer page, Integer rp) {
	    
	    PaginacaoVO paginacao = new PaginacaoVO(page, rp, null);
	    
	    List<DividaDTO> dividas = fecharDiaService.obterDividasReceberEm(dataOperacao, paginacao);
	    Long totalDividas = fecharDiaService.contarDividasReceberEm(dataOperacao);
	    
	    result.use(FlexiGridJson.class).from(dividas).page(page).total(totalDividas.intValue()).serialize();       
	}
	
	@Get
    public void exportarDividasReceber(FileType fileType) throws IOException {
	    
		List<DividaDTO> dividas = fecharDiaService.obterDividasReceberEm(dataOperacao, null);
        
        FileExporter.to("dividas-receber", fileType).inHTTPResponse(getNDSFileHeader(), null, null, 
        		dividas, DividaDTO.class, httpResponse);
        
        result.nothing();
    }
	
	@Post
	public void obterDividasVencer(Integer page, Integer rp) {
	    
        PaginacaoVO paginacao = new PaginacaoVO(page, rp, null);
        
        List<DividaDTO> dividas = fecharDiaService.obterDividasVencerApos(dataOperacao, paginacao);
        Long totalDividas = fecharDiaService.contarDividasVencerApos(dataOperacao);
        
        result.use(FlexiGridJson.class).from(dividas).page(page).total(totalDividas.intValue()).serialize();            
	}
	
	@Get
    public void exportarDividasVencer(FileType fileType) throws IOException {
	    
		List<DividaDTO> dividas = fecharDiaService.obterDividasVencerApos(dataOperacao, null);
        
        FileExporter.to("dividas-vencer", fileType).inHTTPResponse(getNDSFileHeader(), null, null, 
        		dividas, DividaDTO.class, httpResponse);
        
        result.nothing();
    }
	
	@Post
	public void obterResumoCotas() {
		
		ResumoFechamentoDiarioCotasDTO resumoFechamentoDiarioCotas = 
			this.fecharDiaService.obterResumoCotas(dataOperacao);
		
		Map<TipoResumo, Long> mapaResumo = new HashMap<TipoResumo, Long>();
		
		mapaResumo.put(TipoResumo.TOTAL, resumoFechamentoDiarioCotas.getQuantidadeTotal());
		mapaResumo.put(TipoResumo.ATIVAS, resumoFechamentoDiarioCotas.getQuantidadeAtivas());
		mapaResumo.put(TipoResumo.AUSENTES_REPARTE, resumoFechamentoDiarioCotas.getQuantidadeAusentesExpedicaoReparte());
		mapaResumo.put(TipoResumo.AUSENTES_ENCALHE, resumoFechamentoDiarioCotas.getQuantidadeAusentesRecolhimentoEncalhe());
		mapaResumo.put(TipoResumo.NOVAS, resumoFechamentoDiarioCotas.getQuantidadeNovas());
		mapaResumo.put(TipoResumo.INATIVAS, resumoFechamentoDiarioCotas.getQuantidadeInativas());
		
		result.use(CustomMapJson.class).put("resumo", mapaResumo).serialize();
	}
	
	@Post
	public void obterDetalhesResumoCota(TipoResumo tipoResumo) {
		
		List<DetalheCotaFechamentoDiarioVO> listaDetalhesCotaFechamentoDiarioVO = 
			this.obterDetalheCotaFechamentoDiario(tipoResumo);
		
		result.use(FlexiGridJson.class).from(listaDetalhesCotaFechamentoDiarioVO).page(1).total(listaDetalhesCotaFechamentoDiarioVO.size()).serialize();
	}
	
	@Post
	public void obterDataOperacao() { 
		String data = DateUtil.formatarDataPTBR(distribuidorService.obterDataOperacaoDistribuidor());
        result.use(Results.json()).from(data).recursive().serialize();
	}
	
	@Post
	public void confirmar() {
		//Unlock na base de dados
		
		LOGGER.info("INICIO CONFIRMA FECHAMENTO DIA");
		
		this.fecharDiaService.setLockBancoDeDados(false);

		LOGGER.info("LOCK DE BANCO ATIVADO");
		
		this.session.setAttribute(INACTIVE_INTERVAL, this.session.getMaxInactiveInterval());
		
		try {
			
            // evita que a sessão expire antes que o fechamento do dia seja
            // finalizado
			this.session.setMaxInactiveInterval(-1);

			LOGGER.info("SESSION CONFIGURADA PARA ATIVA PERMANENTE");

			Date _dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
			
			Boolean hasPendenciaValidacao = this.fecharDiaService.existePendenciasDeAprovacao(_dataOperacao);
			
			if (hasPendenciaValidacao == null || !hasPendenciaValidacao) {

				LOGGER.info("INICIO PROCESSAMENTO FECHAMENTO DIA");
				
		        FechamentoDiarioDTO dto = this.fecharDiaService.processarFechamentoDoDia(getUsuarioLogado(), _dataOperacao);
		        
				LOGGER.info("FINALIZADO PROCESSAMENTO FECHAMENTO DIA");

		        
		        setFechamentoDiarioDTO(dto);
		        
		        //this.session.removeAttribute(ATRIBUTO_SESSAO_POSSUI_PENDENCIAS_VALIDACAO);
		        
		        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, " Fechamento do Dia efetuado com sucesso."),
		                Constantes.PARAM_MSGS).recursive().serialize();
		    }
		    else{
		        
		    	//Lock novamente na base de dados.
				this.fecharDiaService.setLockBancoDeDados(true);
		        result.use(Results.json()).from(
		        		new ValidacaoVO(
		        				TipoMensagem.WARNING, 
 "Fechamento do Dia não pode ser confirmado! "
                                + "Existem pendências em aberto para a data de operação: "
                                +
		        				new SimpleDateFormat("dd/MM/yyyy").format(_dataOperacao) + "!"),
		                Constantes.PARAM_MSGS).recursive().serialize();
		        
		    }
		    
		} catch (RuntimeException ex) {
		    
			LOGGER.error("ERRO AO CONFIRMAR FECHAMENTO DO DIA!", ex);

			clearFechamentoDiarioDTO();

			throw ex;
			
		}
	}

	private List<DetalheCotaFechamentoDiarioVO> obterDetalheCotaFechamentoDiario(TipoResumo tipoResumo) {
		
		List<CotaResumoDTO> listaCotas = 
				fecharDiaService.obterDetalheCotaFechamentoDiario(dataOperacao, tipoResumo);
		
		List<DetalheCotaFechamentoDiarioVO> listaDetalhesCotaFechamentoDiarioVO =
			new ArrayList<DetalheCotaFechamentoDiarioVO>();
		
		for (CotaResumoDTO cota : listaCotas) {
			
			listaDetalhesCotaFechamentoDiarioVO.add(
				new DetalheCotaFechamentoDiarioVO(cota.getNumero(), cota.getNome()));
		}
		
		return listaDetalhesCotaFechamentoDiarioVO;
	}
	
	@Get
	public void exportarCotas(FileType fileType, TipoResumo tipoResumo) throws IOException {
		
		if (fileType != null && tipoResumo != null) {
		
			List<DetalheCotaFechamentoDiarioVO> listaDetalhesCotaFechamentoDiarioVO = 
				this.obterDetalheCotaFechamentoDiario(tipoResumo);
			
			FileExporter.to("resumo-cotas-" + tipoResumo.getDescricao(), fileType).inHTTPResponse(
				getNDSFileHeader(), null, null, listaDetalhesCotaFechamentoDiarioVO, 
					DetalheCotaFechamentoDiarioVO.class, httpResponse);
		}
		
        result.nothing();
	}
	
	@Post
	public void obterResumoConsignado() {
		
		ResumoFechamentoDiarioConsignadoDTO resumoFechamentoDiarioConsignado = 
			this.fecharDiaService.obterResumoConsignado(dataOperacao);
		
		result.use(CustomMapJson.class).put("resumo", resumoFechamentoDiarioConsignado).serialize();
	}
	
	@Post
	public void obterResumoEstoque() {
		
		ResumoEstoqueDTO resumoFechamentoDiarioEstoque = 
			this.fecharDiaService.obterResumoEstoque(dataOperacao);
		
		result.use(CustomMapJson.class).put("resumo", resumoFechamentoDiarioEstoque).serialize();
	}
	
    @Post
    public Download gerarRelatorioFechamentoDiario(ModoDownload modoDownload) {
        
    	LOGGER.info("FECHAMENTO DIARIO - INICIO GERACAO RELATORIO FECHAMENTO DIARIO");
    	
        // volta o valor original de inativação da sessão
    	if (this.session.getAttribute(INACTIVE_INTERVAL) != null){
    		
    		this.session.setMaxInactiveInterval((int) this.session.getAttribute(INACTIVE_INTERVAL));
    		this.session.removeAttribute(INACTIVE_INTERVAL);
    	}

    	LOGGER.info("FECHAMENTO DIARIO - RETIRADA CONFIGURACAO DE SESSION PERMANENTE");
    	
    	FechamentoDiarioDTO dto = getFechamentoDiarioDTO();
        
        byte[] relatorio = RelatorioFechamentoDiario.exportPdf(dto);
        
    	LOGGER.info("FECHAMENTO DIARIO - OBTIDO BYTES RELATORIO FECHAR DIA");

        if (relatorio != null) {
            long size = relatorio.length;
            InputStream inputStream = new ByteArrayInputStream(relatorio);

            // Inclui o cookie requerido pelo plugin para tratamento da
            // conclusão do download
            if (ModoDownload.JQUERY_FILE_DOWNLOAD_PLUGIN.equals(modoDownload)) {
                Cookie cookie = new Cookie("fileDownload", "true");
                cookie.setPath(httpRequest.getContextPath());
                httpResponse.addCookie(cookie);
            }
            
            InputStreamDownload download = new InputStreamDownload(inputStream, FileType.PDF.getContentType(),
                    FECHAMENTO_DIARIO_REPORT_EXPORT_NAME, true, size);
            
        	LOGGER.info("FECHAMENTO DIARIO - RETORNANDO RELATORIO FECHAR DIA");

            return download;
        }
        
    	LOGGER.info("FECHAMENTO DIARIO - RELATORIO NAO GERADO");
        
        return null;
    }
    
    @Post
    public void transferirDiferencasParaEstoqueDePerdaGanho() {
    	
    	this.fecharDiaService.transferirDiferencasParaEstoqueDePerdaGanho(
    		dataOperacao, getUsuarioLogado().getId());
    	
    	this.result.use(CustomMapJson.class).put("result", "").serialize();
    }
    
    private FechamentoDiarioDTO getFechamentoDiarioDTO() {
        FechamentoDiarioDTO dto = (FechamentoDiarioDTO) session.getAttribute(FECHAMENTO_DIARIO_DTO_SESSION_KEY);
        if (dto == null) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Fechamento Diário não foi confirmado!"));
        }
        clearFechamentoDiarioDTO();
        
        dto.setImagemLogoDistribuidor(JasperUtil.getImagemRelatorio(super.getLogoDistribuidor()));
        
        return dto;
    }
    
    private void setFechamentoDiarioDTO(FechamentoDiarioDTO dto) {
    	session.setAttribute(FECHAMENTO_DIARIO_DTO_SESSION_KEY, dto);
    }
    
    private void clearFechamentoDiarioDTO() {
        setFechamentoDiarioDTO(null);
    }
    
    /**
     * Enum com o modo de download sendo utilizado
     * 
     * @author francisco.garcia
     *
     */
    public static enum ModoDownload {
       
        /**
         * Download normal
         */
        REGULAR_DOWNLOAD,
        
        /**
         * Download sendo efetuado pelo plugin 'jQuery File Download Plugin',
         * que requer parâmetros extras para o tratamento da conclusão do
         * download
         * 
         */
        JQUERY_FILE_DOWNLOAD_PLUGIN
    }

    @Post
    public void isDataOperacaoDistribuidor(Date data){
    	
    	boolean isDataOperacao = false;
    	
    	if (data!= null){
    		
    		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
    		
    		isDataOperacao = (data.compareTo(dataOperacao)< 0);
    	}
    	
    	result.use(CustomMapJson.class).put("isDataOperacaoDistribuidor", isDataOperacao).serialize();
    }
}
