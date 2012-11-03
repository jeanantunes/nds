package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.DetalheCotaFechamentoDiarioVO;
import br.com.abril.nds.dto.FecharDiaDTO;
import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO;
import br.com.abril.nds.dto.ResumoFechamentoDiarioCotasDTO.TipoResumo;
import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoControleDeAprovacaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.dto.VendaSuplementarDTO;
import br.com.abril.nds.dto.fechamentodiario.DividaDTO;
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
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/fecharDia")
public class FecharDiaController {
	
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
	
	private static Distribuidor distribuidor;
	
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
		
		result.use(Results.json()).from(pendencia).recursive().serialize();
		
	}
	
	@Post
	@Path("obterResumoQuadroReparte")
	public void obterResumoQuadroReparte(){
		
		List<BigDecimal> listaDeResultados = new ArrayList<BigDecimal>();

		List<ReparteFecharDiaDTO> lista = this.resumoFecharDiaService.obterValorReparte(distribuidor.getDataOperacao(), true);
		
		BigDecimal totalReparte = lista.get(0).getValorTotalReparte();
		listaDeResultados.add(totalReparte);
		
		lista = this.resumoFecharDiaService.obterValorDiferenca(distribuidor.getDataOperacao(), true, "sobra");
		BigDecimal totalSobras = lista.get(0).getSobras() != null ? lista.get(0).getSobras() : BigDecimal.ZERO;
		listaDeResultados.add(totalSobras);
		
		
		lista = this.resumoFecharDiaService.obterValorDiferenca(distribuidor.getDataOperacao(), true, "falta");
		BigDecimal totalFaltas = lista.get(0).getFaltas() != null ? lista.get(0).getFaltas() : BigDecimal.ZERO;
		listaDeResultados.add(totalFaltas);
		
		
		lista = this.resumoFecharDiaService.obterValorTransferencia(distribuidor.getDataOperacao(), true);
		BigDecimal totalTranferencia = BigDecimal.ZERO;
		if(lista.get(0).getTransferencias() != null){
			totalTranferencia = lista.get(0).getTransferencias();			
		}
		listaDeResultados.add(totalTranferencia);
		
		
		BigDecimal totalADistribuir = (totalReparte.add(totalSobras)).subtract(totalFaltas);
		listaDeResultados.add(totalADistribuir);
		
		lista = this.resumoFecharDiaService.obterValorDistribuido(distribuidor.getDataOperacao(), true);
		BigDecimal totalDistribuido = lista.get(0).getDistribuidos() != null ? lista.get(0).getDistribuidos() : BigDecimal.ZERO;
		listaDeResultados.add(totalDistribuido);
		
		BigDecimal sobraDistribuido = totalADistribuir.subtract(totalDistribuido);
		listaDeResultados.add(sobraDistribuido);
		BigDecimal diferenca = totalDistribuido.subtract(sobraDistribuido);
		listaDeResultados.add(diferenca);
		
		result.use(Results.json()).from(listaDeResultados, "result").serialize();
	}
	
	@Post
	@Path("obterResumoQuadroEncalhe")
	public void obterResumoQuadroEncalhe(){
		
		List<BigDecimal> listaDeEncalhes = new ArrayList<BigDecimal>();
		
		BigDecimal totalLogico = this.resumoEncalheFecharDiaService.obterValorEncalheLogico(distribuidor.getDataOperacao());
		listaDeEncalhes.add(totalLogico);
		
		BigDecimal totalFisico = this.resumoEncalheFecharDiaService.obterValorEncalheFisico(distribuidor.getDataOperacao(), false);
		listaDeEncalhes.add(totalFisico);
		
		BigDecimal totalJuramentado = this.resumoEncalheFecharDiaService.obterValorEncalheFisico(distribuidor.getDataOperacao(), true);;
		listaDeEncalhes.add(totalJuramentado);
		
		List<ReparteFecharDiaDTO> lista = this.resumoFecharDiaService.obterValorReparte(distribuidor.getDataOperacao(), true);
		BigDecimal venda = lista.get(0).getValorTotalReparte().subtract(totalFisico) ;
		listaDeEncalhes.add(venda);
		
		result.use(Results.json()).from(listaDeEncalhes, "result").recursive().serialize();
		
	}
	

	@Post
	@Path("obterResumoQuadroSuplementar")
	public void obterResumoQuadroSuplementar(){
		
		List<BigDecimal> listaDeSuplementares = new ArrayList<BigDecimal>();
		
		BigDecimal totalEstoqueLogico = this.resumoSuplementarFecharDiaService.obterValorEstoqueLogico(distribuidor.getDataOperacao());
		listaDeSuplementares.add(totalEstoqueLogico);
		
		BigDecimal totalTransferencia = this.resumoSuplementarFecharDiaService.obterValorTransferencia(distribuidor.getDataOperacao());
		listaDeSuplementares.add(totalTransferencia);
		
		BigDecimal totalVenda = this.resumoSuplementarFecharDiaService.obterValorVenda(distribuidor.getDataOperacao());
		listaDeSuplementares.add(totalVenda);
		
		BigDecimal totalFisico = this.resumoSuplementarFecharDiaService.obterValorFisico(distribuidor.getDataOperacao());
		listaDeSuplementares.add(totalEstoqueLogico.subtract(totalFisico));
		
		result.use(Results.json()).from(listaDeSuplementares, "result").recursive().serialize();
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
	@Path("/obterGridVendaSuplemntar")
	public void obterGridVendaSuplemntar(){
		
		List<VendaSuplementarDTO> listaReparte = resumoSuplementarFecharDiaService.obterVendasSuplementar(distribuidor.getDataOperacao());
		
		TableModel<CellModelKeyValue<VendaSuplementarDTO>> tableModel = new TableModel<CellModelKeyValue<VendaSuplementarDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaReparte));
		
		tableModel.setTotal(listaReparte.size());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Get
	public void exportarVendaSuplemntar(FileType fileType){		
		try {
			
			List<VendaSuplementarDTO> listaReparte = resumoSuplementarFecharDiaService.obterVendasSuplementar(distribuidor.getDataOperacao());
			
			if(listaReparte.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			
				FileExporter.to("recebimento_fisico", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
						listaReparte, VendaSuplementarDTO.class, this.httpResponse);
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
	    int totalDividas = fecharDiaService.contarDividasReceberEm(dataFechamento);
	    
	    List<DividaDTO> dividasDTO = new ArrayList<>();
	    for (Divida divida : dividas) {
	        dividasDTO.add(DividaDTO.fromDivida(divida));
	    }
	    result.use(FlexiGridJson.class).from(dividasDTO).page(page).total(totalDividas).serialize();       
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
        int totalDividas = fecharDiaService.contarDividasVencerApos(dataFechamento);
        
        List<DividaDTO> dividasDTO = new ArrayList<>();
        for (Divida divida : dividas) {
            dividasDTO.add(DividaDTO.fromDivida(divida));
        }
        result.use(FlexiGridJson.class).from(dividasDTO).page(page).total(totalDividas).serialize();            
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
    
    private Date getDataFechamento() {
        return distribuidorService.obter().getDataOperacao();
    }

}
