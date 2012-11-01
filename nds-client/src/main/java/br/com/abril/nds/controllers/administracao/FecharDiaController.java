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
import br.com.abril.nds.dto.FecharDiaDTO;
import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.SumarizacaoDividasDTO.TipoSumarizacao;
import br.com.abril.nds.dto.ResumoReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoSuplementarFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoControleDeAprovacaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.dto.VendaSuplementarDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomMapJson;
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
		
		result.use(Results.json()).from(pendencia).recursive().serialize();
		
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
	@Path("/obterGridVendaSuplementar")
	public void obterGridVendaSuplementar(){
		
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
        Map<TipoSumarizacao, List<SumarizacaoDividasDTO>> sumarizacao = new HashMap<TipoSumarizacao, List<SumarizacaoDividasDTO>>();
        sumarizacao.put(TipoSumarizacao.DIVIDAS_A_RECEBER, new ArrayList<SumarizacaoDividasDTO>());
        sumarizacao.put(TipoSumarizacao.DIVIDAS_A_VENCER, new ArrayList<SumarizacaoDividasDTO>());
        for (TipoCobranca tc : TipoCobranca.values()) {
            sumarizacao.get(TipoSumarizacao.DIVIDAS_A_RECEBER).add(new SumarizacaoDividasDTO(dataFechamento, TipoSumarizacao.DIVIDAS_A_RECEBER, tc, BigDecimal.valueOf(1500), BigDecimal.valueOf(1000), BigDecimal.valueOf(500)));
            sumarizacao.get(TipoSumarizacao.DIVIDAS_A_VENCER).add(new SumarizacaoDividasDTO(dataFechamento, TipoSumarizacao.DIVIDAS_A_VENCER, tc, BigDecimal.valueOf(1500), BigDecimal.valueOf(1000), BigDecimal.valueOf(500)));
        }
        result.use(CustomMapJson.class).put("sumarizacao", sumarizacao).serialize();
    }
    
    private Date getDataFechamento() {
        return distribuidorService.obter().getDataOperacao();
    }

}
