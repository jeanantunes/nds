package br.com.abril.nds.controllers.administracao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.DetalheCotaFechamentoDiarioVO;
import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.FecharDiaDTO;
import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioConsignadoDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO.TipoResumo;
import br.com.abril.nds.dto.ResumoReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.dto.SuplementarFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoControleDeAprovacaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.dto.fechamentodiario.DividaDTO;
import br.com.abril.nds.dto.fechamentodiario.FechamentoDiarioDTO;
import br.com.abril.nds.dto.fechamentodiario.ResumoEstoqueDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.fechamentodiario.TipoDivida;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FecharDiaService;
import br.com.abril.nds.service.ResumoEncalheFecharDiaService;
import br.com.abril.nds.service.ResumoReparteFecharDiaService;
import br.com.abril.nds.service.ResumoSuplementarFecharDiaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.JasperUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
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
public class FecharDiaController {
    
    private static final Logger LOG = LoggerFactory.getLogger(FecharDiaController.class);
	
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
	private CotaService cotaService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private HttpSession session;
	
	private static Distribuidor distribuidor;

	private static final String ATRIBUTO_SESSAO_POSSUI_PENDENCIAS_VALIDACAO = "atributoSessaoValidacao";
	
	private static final String FECHAMENTO_DIARIO_DTO_SESSION_KEY = "FECHAMENTO_DIARIO_DTO_SESSION_KEY"; 
	
	private static final String FECHAMENTO_DIARIO_REPORT_NAME = "fechamento_diario_sumarizacao.jasper";
	
	private static final String FECHAMENTO_DIARIO_REPORT_EXPORT_NAME = "relatorio-fechamento-diario.pdf";
	
	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_FECHAR_DIA)
	public void index(){
		distribuidor = this.distribuidorService.obter();
		result.include("dataOperacao", DateUtil.formatarData(distribuidor.getDataOperacao(), Constantes.DATE_PATTERN_PT_BR));
	}
	
	@SuppressWarnings("static-access")
	@Post
	public void inicializarValidacoes(){		
		FecharDiaDTO dto = new FecharDiaDTO();
		dto.setBaixaBancaria(this.fecharDiaService.existeCobrancaParaFecharDia(distribuidor.getDataOperacao()));
		dto.setGeracaoDeCobranca(this.fecharDiaService.existeGeracaoDeCobranca(distribuidor.getDataOperacao()));
		dto.setRecebimentoFisico(this.fecharDiaService.existeNotaFiscalSemRecebimentoFisico(distribuidor.getDataOperacao()));
		dto.setConfirmacaoDeExpedicao(this.fecharDiaService.existeConfirmacaoDeExpedicao(distribuidor.getDataOperacao()));
		dto.setLancamentoFaltasESobras(this.fecharDiaService.existeLancamentoFaltasESobrasPendentes(distribuidor.getDataOperacao()));
		dto.setControleDeAprovacao(this.distribuidor.isUtilizaControleAprovacao());
		
		result.use(Results.json()).withoutRoot().from(dto).recursive().serialize();
	}
	
	@Post
	@Path("/obterRecebimentoFisicoNaoConfirmado")
	public void obterRecebimentoFisicoNaoConfirmado(){
		
		List<ValidacaoRecebimentoFisicoFecharDiaDTO> listaRecebimentoFisicoNaoConfirmado = this.fecharDiaService.obterNotaFiscalComRecebimentoFisicoNaoConfirmado(distribuidor.getDataOperacao());
		
		TableModel<CellModelKeyValue<ValidacaoRecebimentoFisicoFecharDiaDTO>> tableModel = new TableModel<CellModelKeyValue<ValidacaoRecebimentoFisicoFecharDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaRecebimentoFisicoNaoConfirmado));
		
		tableModel.setTotal(listaRecebimentoFisicoNaoConfirmado.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	@Path("/obterConfirmacaoDeExpedicao")
	public void obterConfirmacaoDeExpedicao(){
		
		List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> listaConfirmacaoDeExpedicao = this.fecharDiaService.obterConfirmacaoDeExpedicao(distribuidor.getDataOperacao());
		
		TableModel<CellModelKeyValue<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO>> tableModel = new TableModel<CellModelKeyValue<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConfirmacaoDeExpedicao));
		
		tableModel.setTotal(listaConfirmacaoDeExpedicao.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	//Grid que é acionado nas validações
	@Post
	@Path("/obterLancamentoFaltaESobra")
	public void obterLancamentoFaltaESobra(){
		
		List<ValidacaoLancamentoFaltaESobraFecharDiaDTO> listaLancamentoFaltaESobra = this.fecharDiaService.obterLancamentoFaltasESobras(distribuidor.getDataOperacao());
		
		TableModel<CellModelKeyValue<ValidacaoLancamentoFaltaESobraFecharDiaDTO>> tableModel = new TableModel<CellModelKeyValue<ValidacaoLancamentoFaltaESobraFecharDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentoFaltaESobra));
		
		tableModel.setTotal(listaLancamentoFaltaESobra.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post
	@Path("validacoesDoCotroleDeAprovacao")
	public void validacoesDoCotroleDeAprovacao(){
		
		List<ValidacaoControleDeAprovacaoFecharDiaDTO> listaLancamentoFaltaESobra = this.fecharDiaService.obterPendenciasDeAprovacao(distribuidor.getDataOperacao(), StatusAprovacao.PENDENTE);
		Boolean pendencia = false;
		for (ValidacaoControleDeAprovacaoFecharDiaDTO dto : listaLancamentoFaltaESobra) {
			if(dto.getDescricaoTipoMovimento().equals("Falta DE") || dto.getDescricaoTipoMovimento().equals("Falta EM") 
					|| dto.getDescricaoTipoMovimento().equals("Sobra DE") || dto.getDescricaoTipoMovimento().equals("Sobra EM")) {
				pendencia = true;
			}
			if(dto.getDescricaoTipoMovimento().equals("Crédito") || dto.getDescricaoTipoMovimento().equals("Débito")){
				pendencia = true;
			}
			if(dto.getDescricaoTipoMovimento().equals("Negociação")){
				pendencia = true;
			}
			if(dto.getDescricaoTipoMovimento().equals("Ajuste de estoque")){
				pendencia = true;
			}
			if(dto.getDescricaoTipoMovimento().equals("Postergação de cobrança")){
				pendencia = true;
			}
		}
		
		this.session.setAttribute(ATRIBUTO_SESSAO_POSSUI_PENDENCIAS_VALIDACAO, pendencia);
		
		this.result.use(Results.json()).from(pendencia).recursive().serialize();
		
	}
	
	@Post
	@Path("obterResumoQuadroReparte")
	public void obterResumoQuadroReparte(){
		
		ResumoReparteFecharDiaDTO dto = this.resumoFecharDiaService.obterResumoGeralReparte(distribuidor.getDataOperacao());
		
		result.use(Results.json()).from(dto, "result").recursive().serialize();
	}
	
	@Post
	@Path("obterResumoQuadroEncalhe")
	public void obterResumoQuadroEncalhe(){
		
		ResumoEncalheFecharDiaDTO dto = this.resumoEncalheFecharDiaService.obterResumoGeralEncalhe(distribuidor.getDataOperacao());
		
		result.use(Results.json()).from(dto, "result").recursive().serialize();
		
	}
	

	@Post
	@Path("obterResumoQuadroSuplementar")
	public void obterResumoQuadroSuplementar(){
		
		ResumoSuplementarFecharDiaDTO dto = this.resumoSuplementarFecharDiaService.obterResumoGeralEncalhe(distribuidor.getDataOperacao());
		
		result.use(Results.json()).from(dto, "result").recursive().serialize();
	}
	
	@Post
	@Path("/obterGridReparte")
	public void obterGridReparte(){
		
		List<ReparteFecharDiaDTO> listaReparte = this.resumoFecharDiaService.obterResumoReparte(distribuidor.getDataOperacao());
		
		TableModel<CellModelKeyValue<ReparteFecharDiaDTO>> tableModel = new TableModel<CellModelKeyValue<ReparteFecharDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaReparte));
		
		tableModel.setTotal(listaReparte.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post
	@Path("/obterGridEncalhe")
	public void obterGridEncalhe(){
		
		List<EncalheFecharDiaDTO> listaEncalhe = this.resumoEncalheFecharDiaService.obterDadosGridEncalhe(distribuidor.getDataOperacao());
		
		TableModel<CellModelKeyValue<EncalheFecharDiaDTO>> tableModel = new TableModel<CellModelKeyValue<EncalheFecharDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaEncalhe));
		
		tableModel.setTotal(listaEncalhe.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post
	@Path("/obterGridSuplementar")
	public void obterGridSuplementar(){
		
		List<SuplementarFecharDiaDTO> listaSuplementar = this.resumoSuplementarFecharDiaService.obterDadosGridSuplementar();
		
		TableModel<CellModelKeyValue<SuplementarFecharDiaDTO>> tableModel = new TableModel<CellModelKeyValue<SuplementarFecharDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaSuplementar));
		
		tableModel.setTotal(listaSuplementar.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	

	@Post
	@Path("/obterGridVenda")
	public void obterGridVenda(String tipoVenda){
		
		List<VendaFechamentoDiaDTO> listaReparte = null;
		if(tipoVenda.equals("encalhe")){
			listaReparte = resumoEncalheFecharDiaService.obterDadosVendaEncalhe(distribuidor.getDataOperacao());
		}else if(tipoVenda.equals("suplementar")){
			listaReparte = resumoSuplementarFecharDiaService.obterVendasSuplementar(distribuidor.getDataOperacao());			
		}
		
		TableModel<CellModelKeyValue<VendaFechamentoDiaDTO>> tableModel = new TableModel<CellModelKeyValue<VendaFechamentoDiaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaReparte));
		
		tableModel.setTotal(listaReparte.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	
	
	@Get
	public void exportarResumoReparte(FileType fileType){
		
		
		try {
		List<ReparteFecharDiaDTO> listaReparte = this.resumoFecharDiaService.obterResumoReparte(distribuidor.getDataOperacao());
		
		if(listaReparte.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
			FileExporter.to("resumo_reparte", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaReparte, ReparteFecharDiaDTO.class, this.httpResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		result.nothing();
		
	}
	
	@Get
	public void exportarResumoEncalhe(FileType fileType){
		
		
		try {
		
		List<EncalheFecharDiaDTO> listaEncalhe = this.resumoEncalheFecharDiaService.obterDadosGridEncalhe(distribuidor.getDataOperacao());
		
		if(listaEncalhe.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
			FileExporter.to("resumo_reparte", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaEncalhe, EncalheFecharDiaDTO.class, this.httpResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		result.nothing();
		
	}
	
	@Get
	public void exportarResumoSuplementar(FileType fileType){
		
		
		try {
		
		List<SuplementarFecharDiaDTO> listaSuplementar = this.resumoSuplementarFecharDiaService.obterDadosGridSuplementar();
		
		if(listaSuplementar.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
			FileExporter.to("resumo_reparte", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaSuplementar, SuplementarFecharDiaDTO.class, this.httpResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		result.nothing();
		
	}
	
	@Get
	public void exportarVendaSuplemntar(FileType fileType){		
		try {
			
			List<VendaFechamentoDiaDTO> listaReparte = resumoSuplementarFecharDiaService.obterVendasSuplementar(distribuidor.getDataOperacao());
			
			if(listaReparte.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
				FileExporter.to("recebimento_fisico", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
						listaReparte, VendaFechamentoDiaDTO.class, this.httpResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		result.nothing();
		
	}
	
	
	
	@Get
	public void exportarRecebimentoFisico(FileType fileType){
		
		
		try {
		List<ValidacaoRecebimentoFisicoFecharDiaDTO> listaRecebimentoFisicoNaoConfirmado = this.fecharDiaService.obterNotaFiscalComRecebimentoFisicoNaoConfirmado(distribuidor.getDataOperacao());
		
		if(listaRecebimentoFisicoNaoConfirmado.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
			FileExporter.to("recebimento_fisico", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaRecebimentoFisicoNaoConfirmado, ValidacaoRecebimentoFisicoFecharDiaDTO.class, this.httpResponse);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		result.nothing();
		
	}
	
	
	
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}
	

	public Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Lazaro Jornaleiro");
		return usuario;
	}
	
	@Post
	public void obterSumarizacaoDividas() {
        Date dataFechamento = getDataFechamento();
        
        Map<TipoDivida, List<SumarizacaoDividasDTO>> sumarizacao = new HashMap<>();
        
        List<SumarizacaoDividasDTO> aReceber = fecharDiaService.sumarizacaoDividasReceberEm(dataFechamento);
        List<SumarizacaoDividasDTO> aVencer = fecharDiaService.sumarizacaoDividasVencerApos(dataFechamento);
        
        sumarizacao.put(TipoDivida.DIVIDA_A_RECEBER, aReceber);
        sumarizacao.put(TipoDivida.DIVIDA_A_VENCER, aVencer);
        
        result.use(CustomMapJson.class).put("sumarizacao", sumarizacao).serialize();
    }
	
	@Post
	public void obterDividasReceber(Integer page, Integer rp) {
	    Date dataFechamento = getDataFechamento();
	    PaginacaoVO paginacao = new PaginacaoVO(page, rp, null);
	    
	    List<Divida> dividas = fecharDiaService.obterDividasReceberEm(dataFechamento, paginacao);
	    Long totalDividas = fecharDiaService.contarDividasReceberEm(dataFechamento);
	    
	    List<DividaDTO> dividasDTO = new ArrayList<>();
	    for (Divida divida : dividas) {
	        dividasDTO.add(DividaDTO.fromDivida(divida));
	    }
	    result.use(FlexiGridJson.class).from(dividasDTO).page(page).total(totalDividas.intValue()).serialize();       
	}
	
	@Get
    public void exportarDividasReceber(FileType fileType) throws IOException {
	    Date dataFechamento = getDataFechamento();
        
        List<Divida> dividas = fecharDiaService.obterDividasReceberEm(dataFechamento, null);
        List<DividaDTO> dividasDTO = new ArrayList<>(dividas.size());

        for (Divida divida : dividas) {
            dividasDTO.add(DividaDTO.fromDivida(divida));
        }
        
        FileExporter.to("dividas-receber", fileType).inHTTPResponse(getNDSFileHeader(), null, null, 
                dividasDTO, DividaDTO.class, httpResponse);
        
        result.nothing();
    }
	
	
	@Post
	public void obterDividasVencer(Integer page, Integer rp) {
	    Date dataFechamento = getDataFechamento();
        PaginacaoVO paginacao = new PaginacaoVO(page, rp, null);
        
        List<Divida> dividas = fecharDiaService.obterDividasVencerApos(dataFechamento, paginacao);
        Long totalDividas = fecharDiaService.contarDividasVencerApos(dataFechamento);
        
        List<DividaDTO> dividasDTO = new ArrayList<>();
        for (Divida divida : dividas) {
            dividasDTO.add(DividaDTO.fromDivida(divida));
        }
        result.use(FlexiGridJson.class).from(dividasDTO).page(page).total(totalDividas.intValue()).serialize();            
	}
	
	@Get
    public void exportarDividasVencer(FileType fileType) throws IOException {
	    Date dataFechamento = getDataFechamento();
        
        List<Divida> dividas = fecharDiaService.obterDividasVencerApos(dataFechamento, null);
        List<DividaDTO> dividasDTO = new ArrayList<>(dividas.size());

        for (Divida divida : dividas) {
            dividasDTO.add(DividaDTO.fromDivida(divida));
        }
        
        FileExporter.to("dividas-vencer", fileType).inHTTPResponse(getNDSFileHeader(), null, null, 
                dividasDTO, DividaDTO.class, httpResponse);
        
        result.nothing();
    }
	
	@Post
	public void obterResumoCotas() {
		
		ResumoFechamentoDiarioCotasDTO resumoFechamentoDiarioCotas = 
			this.fecharDiaService.obterResumoCotas(this.getDataFechamento());
		
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
	public void confirmar() {
		try {
		    Boolean hasPendenciaValidacao = (Boolean) this.session.getAttribute(ATRIBUTO_SESSAO_POSSUI_PENDENCIAS_VALIDACAO);
		    
		    if (hasPendenciaValidacao != null && !hasPendenciaValidacao) {
		        
		        FechamentoDiarioDTO dto = this.fecharDiaService.processarFechamentoDoDia(getUsuario(), getDataFechamento());
		        setFechamentoDiarioDTO(dto);
		        
		        this.session.removeAttribute(ATRIBUTO_SESSAO_POSSUI_PENDENCIAS_VALIDACAO);
		        
		        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, " Fechamento do Dia efetuado com sucesso."),
		                Constantes.PARAM_MSGS).recursive().serialize();
		    }
		    else{
		        
		        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Fechamento do Dia não pode ser confirmado! Existem pendências em aberto!"),
		                Constantes.PARAM_MSGS).recursive().serialize();
		        
		    }
		    
		} catch (RuntimeException ex) {
		    clearFechamentoDiarioDTO();
		    LOG.error("ERRO AO CONFIRMAR FECHAMENTO DO DIA!", ex);
		    throw ex;
		}
	    
	}

	private List<DetalheCotaFechamentoDiarioVO> obterDetalheCotaFechamentoDiario(TipoResumo tipoResumo) {
		
		Date dataFechamento = this.getDataFechamento();
		
		List<Cota> listaCotas = null;
		
		switch (tipoResumo) {
		
			case AUSENTES_REPARTE:
				
				listaCotas =
					this.cotaService.obterCotasAusentesNaExpedicaoDoReparteEm(dataFechamento);
				
				break;
				
			case AUSENTES_ENCALHE:
				
				listaCotas = this.cotaService.obterCotasAusentesNoRecolhimentoDeEncalheEm(dataFechamento);
				
				break;
				
			case NOVAS:
				
				listaCotas = this.cotaService.obterCotasComInicioAtividadeEm(dataFechamento);
				
				break;
				
			case INATIVAS:
				
				listaCotas = this.cotaService.obterCotas(SituacaoCadastro.INATIVO);
				
				break;
		}
		
		List<DetalheCotaFechamentoDiarioVO> listaDetalhesCotaFechamentoDiarioVO =
			new ArrayList<DetalheCotaFechamentoDiarioVO>();
		
		for (Cota cota : listaCotas) {
			
			listaDetalhesCotaFechamentoDiarioVO.add(
				new DetalheCotaFechamentoDiarioVO(cota.getNumeroCota(), cota.getPessoa().getNome()));
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
			this.fecharDiaService.obterResumoConsignado(getDataFechamento());
		
		result.use(CustomMapJson.class).put("resumo", resumoFechamentoDiarioConsignado).serialize();
	}
	
	@Post
	public void obterResumoEstoque() {
		
		ResumoEstoqueDTO resumoFechamentoDiarioEstoque = 
			this.fecharDiaService.obterResumoEstoque(getDataFechamento());
		
		result.use(CustomMapJson.class).put("resumo", resumoFechamentoDiarioEstoque).serialize();
	}
	
    @Post
    public Download gerarRelatorioFechamentoDiario() {
        FechamentoDiarioDTO dto = getFechamentoDiarioDTO();
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("dataFechamento", dto.getDataFechamento());
        parameters.put("fechamentoDiarioDTO", dto);
        
        byte[] relatorio = JasperUtil.runReportPdf(FECHAMENTO_DIARIO_REPORT_NAME, parameters);

        if (relatorio != null) {
            long size = relatorio.length;
            InputStream inputStream = new ByteArrayInputStream(relatorio);
            InputStreamDownload download = new InputStreamDownload(inputStream, FileType.PDF.getContentType(),
                    FECHAMENTO_DIARIO_REPORT_EXPORT_NAME, true, size);
            return download;
        }
        return null;
    }

	
    private Date getDataFechamento() {
        return distribuidorService.obter().getDataOperacao();
    }
    
    private FechamentoDiarioDTO getFechamentoDiarioDTO() {
        FechamentoDiarioDTO dto = (FechamentoDiarioDTO) session.getAttribute(FECHAMENTO_DIARIO_DTO_SESSION_KEY);
        if (dto == null) {
            throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Fechamento Diário não foi confirmado!"));
        }
        clearFechamentoDiarioDTO();
        return dto;
    }
    
    private void setFechamentoDiarioDTO(FechamentoDiarioDTO dto) {
        session.setAttribute(FECHAMENTO_DIARIO_DTO_SESSION_KEY, dto);
    }
    
    private void clearFechamentoDiarioDTO() {
        setFechamentoDiarioDTO(null);
    }

}
