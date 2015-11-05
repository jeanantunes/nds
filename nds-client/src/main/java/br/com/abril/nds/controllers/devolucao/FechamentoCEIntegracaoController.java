package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.FechamentoCEIntegracaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ItemFechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO.ColunaOrdenacaoFechamentoCEIntegracao;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.DistribuidorTipoNotaFiscal;
import br.com.abril.nds.model.cadastro.DistribuidorTipoNotaFiscal.DistribuidorGrupoNotaFiscal;
import br.com.abril.nds.model.cadastro.NotaFiscalTipoEmissao.NotaFiscalTipoEmissaoEnum;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FechamentoCEIntegracaoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

@Resource
@Path("devolucao/fechamentoCEIntegracao")
@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO)
public class FechamentoCEIntegracaoController extends BaseController{
	
	private static final String BOLETO_GERADO = "boletoDistribuidorGerado";
	
	private static final String FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO = "filtroFechamentoCEIntegracao";

	private static final String CHAMADA_CE_GERADO = "chamadaCeIntegracao";

	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private FechamentoCEIntegracaoService fechamentoCEIntegracaoService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpServletResponse httpResponse;
		
	@Autowired
	private PoliticaCobrancaService politicaCobrancaService;
	
	public FechamentoCEIntegracaoController(Result result) {
		 this.result = result;
	}
	
	@Path("/")
	public void index(){
		
		this.carregarComboFornecedores();
		
		this.setUpTiposCobranca();
	}
	
	private void setUpTiposCobranca() {
		
		List<TipoCobranca> listaTipoCobranca = politicaCobrancaService.obterTiposCobrancaDistribuidor();

		result.include(TipoCobranca.BOLETO.name(), false);
		
		result.include(TipoCobranca.BOLETO_EM_BRANCO.name(), false);
		
		for(TipoCobranca tipoCobranca : listaTipoCobranca) {
			
			if(TipoCobranca.BOLETO.equals(tipoCobranca)) {
			
				result.include(TipoCobranca.BOLETO.name(), true);
			
			} else if(TipoCobranca.BOLETO_EM_BRANCO.equals(tipoCobranca)) {
				
				result.include(TipoCobranca.BOLETO_EM_BRANCO.name(), true);
			
			}
			
		}
		
	}
	
	@Post
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO_ALTERACAO)
	public void reabrirCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro){
		
		validarAnoSemana(filtro.getSemana());
		
		// ver se pode reabrir ce = paramentro NOTA_FISCAL_DEVOLUCAO_AO_FORNECEDOR tem que estar desobriga emissao 
		// marcado para poder reabrir ce 
		Distribuidor distribuidor = distribuidorService.obterParaNFe();
		boolean permiteReabertura=false;
		if(distribuidor.getTiposNotaFiscalDistribuidor() != null && !distribuidor.getTiposNotaFiscalDistribuidor().isEmpty() ) {
			for(DistribuidorTipoNotaFiscal dtnf : distribuidor.getTiposNotaFiscalDistribuidor()) {
				if ( dtnf.getGrupoNotaFiscal().equals(DistribuidorGrupoNotaFiscal.NOTA_FISCAL_DEVOLUCAO_AO_FORNECEDOR)) {
					if ( dtnf.getTipoEmissao() != null &&  dtnf.getTipoEmissao().getTipoEmissao() != null && dtnf.getTipoEmissao().getTipoEmissao().equals(NotaFiscalTipoEmissaoEnum.DESOBRIGA_EMISSAO))
					permiteReabertura=true;
				}
			}
		}
		if ( !permiteReabertura ) {
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING,"Reabertura não Permitida pois já foram emitidas notas fiscais."),"result").recursive().serialize();
			return;
		}
		
		String mensagemReaberturaNaoRealizada = fechamentoCEIntegracaoService.reabrirCeIntegracao(filtro); 
		
		if (mensagemReaberturaNaoRealizada!= null){
						
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING,mensagemReaberturaNaoRealizada),"result").recursive().serialize();
		}
		else{

			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Reabertura realizada com sucesso."),"result").recursive().serialize();
		}
	}
	
	@Post
	@Path("/pesquisaPrincipal")
	public void pesquisaPrincipal(String semana, Long idFornecedor, String comboCEIntegracao,String idChamadaEncalhe, String sortorder, String sortname, int page, int rp){
		
		validarAnoSemana(semana);
		
		
		FiltroFechamentoCEIntegracaoDTO filtroCE = new FiltroFechamentoCEIntegracaoDTO();
		filtroCE.setSemana(semana);
		filtroCE.setIdFornecedor(idFornecedor);
		filtroCE.setComboCeIntegracao(comboCEIntegracao);
		
		filtroCE.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		filtroCE.setOrdenacaoColuna(Util.getEnumByStringValue(ColunaOrdenacaoFechamentoCEIntegracao.values(),sortname));
		
		if ( idChamadaEncalhe != null && !"-1".equals(idChamadaEncalhe))
		filtroCE.setIdChamadaEncalhe(Long.parseLong(idChamadaEncalhe));
		
		this.tratarFiltro(filtroCE);
		
		FechamentoCEIntegracaoDTO fechamentoCEIntegracao = fechamentoCEIntegracaoService.obterCEIntegracaoFornecedor(filtroCE); 
		
		FechamentoCEIntegracaoVO retornoPesquisa = renderizarPesquisa(fechamentoCEIntegracao, filtroCE);
		
		result.use(Results.json()).withoutRoot().from(retornoPesquisa).recursive().serialize();
		
	}
	
	private FechamentoCEIntegracaoVO renderizarPesquisa(FechamentoCEIntegracaoDTO dto, FiltroFechamentoCEIntegracaoDTO filtro) {
		
		TableModel<CellModelKeyValue<ItemFechamentoCEIntegracaoDTO>> tableModel = new TableModel<CellModelKeyValue<ItemFechamentoCEIntegracaoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(dto.getItensFechamentoCE()));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(dto.getQntItensCE());
		
		FechamentoCEIntegracaoVO fechamentoCEIntegracaoVO = new FechamentoCEIntegracaoVO();

		FechamentoCEIntegracaoConsolidadoDTO totalFechamento = dto.getConsolidado();
		fechamentoCEIntegracaoVO.setTotalBruto(CurrencyUtil.formatarValor(totalFechamento.getTotalBruto()));
		fechamentoCEIntegracaoVO.setTotalDesconto(CurrencyUtil.formatarValor(totalFechamento.getTotalDesconto()));
		fechamentoCEIntegracaoVO.setTotalLiquido(CurrencyUtil.formatarValor(totalFechamento.getTotalBruto().subtract(totalFechamento.getTotalDesconto())));
		
		fechamentoCEIntegracaoVO.setListaFechamento(tableModel);
		
		fechamentoCEIntegracaoVO.setSemanaFechada(dto.isSemanaFechada());
		
		return fechamentoCEIntegracaoVO;
		
	}
	
	private void validarAnoSemana(String anoSemana) {
		
		if(StringUtils.isEmpty(anoSemana) || !StringUtils.isNumeric(anoSemana) || 
				StringUtils.isBlank(anoSemana) || anoSemana.length() != 6) { 
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Digite um ano e semana valido (AAAASS). Ex: 201309");
		}
	}
	
	/**
	 * Fechamento de C.E.
	 * @param diferencas
	 */
	@Post
	@Path("fecharCE")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO_ALTERACAO)
	public void fecharCE(List<ItemFechamentoCEIntegracaoDTO> itens){
		
		FiltroFechamentoCEIntegracaoDTO filtro = (FiltroFechamentoCEIntegracaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO);
		fechamentoCEIntegracaoService.salvarCE(itens);
		if(filtro.getComboCeIntegracao().equals("COM")){ 
			fechamentoCEIntegracaoService.fecharCE(filtro, this.obterMapItensCE(itens));
		} else {
			fechamentoCEIntegracaoService.fecharSemCE(itens);
		}
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Fechamento realizado com sucesso."),"result").recursive().serialize();
		
	}

	@Post
	@Path("/geraBoleto")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO_ALTERACAO)
	public void geraBoleto(TipoCobranca tipoCobranca) {
		
		session.setAttribute(BOLETO_GERADO, null);
		
		FiltroFechamentoCEIntegracaoDTO filtro = (FiltroFechamentoCEIntegracaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO);
		
		if(filtro == null || filtro.getSemana() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma pesquisa realizada.");
		}
		
		try {
			
			byte[] boleto = fechamentoCEIntegracaoService.gerarCobrancaBoletoDistribuidor(filtro, tipoCobranca);

			session.setAttribute(BOLETO_GERADO, boleto);
			
			result.use(Results.json()).from("").serialize();
			
		} catch(ValidacaoException e) { 
			throw e;
		} catch(Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração do boleto.");
		}
		
	}
	
	@Get
	@Path("/imprimeBoleto")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO_ALTERACAO)
	public void imprimeBoleto() throws Exception {
		
		byte[] boleto = (byte[]) session.getAttribute(BOLETO_GERADO);
		
		session.setAttribute(BOLETO_GERADO, null);
		
		escreverArquivoParaResponse(boleto, "boleto");
		
	}
	
	private void escreverArquivoParaResponse(byte[] arquivo, String nomeArquivo) throws IOException {
		
		this.httpResponse.setContentType("application/pdf");
		
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename="+nomeArquivo +".pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		
		output.write(arquivo);

		httpResponse.getOutputStream().close();
		
		result.use(Results.nothing());
		
	}
	
	@SuppressWarnings("deprecation")
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroFechamentoCEIntegracaoDTO filtro = (FiltroFechamentoCEIntegracaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO);
		
		if(filtro == null || filtro.getPaginacao() == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro inválido. Por favor, refaça a pesquisa.");
		}
		
		filtro.getPaginacao().setQtdResultadosPorPagina(null);
		filtro.getPaginacao().setPaginaAtual(null);
		
		List<ItemFechamentoCEIntegracaoDTO> listaFechamento = this.fechamentoCEIntegracaoService.buscarItensFechamentoCeIntegracao(filtro);
		
		if(listaFechamento.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
		FechamentoCEIntegracaoVO fechamentoCEIntegracao = new FechamentoCEIntegracaoVO();
		
		FechamentoCEIntegracaoConsolidadoDTO fechamentoConsolidado = 
			this.fechamentoCEIntegracaoService.obterConsolidadoCE(filtro);
		
		if(fechamentoConsolidado == null){
			
			fechamentoCEIntegracao.setTotalBruto(CurrencyUtil.formatarValor(BigDecimal.ZERO));
			fechamentoCEIntegracao.setTotalDesconto(CurrencyUtil.formatarValor(BigDecimal.ZERO));
			fechamentoCEIntegracao.setTotalLiquido(CurrencyUtil.formatarValor(BigDecimal.ZERO));
		}
		else{
			
			fechamentoCEIntegracao.setTotalBruto(CurrencyUtil.formatarValor(fechamentoConsolidado.getTotalBruto()));
			fechamentoCEIntegracao.setTotalDesconto(CurrencyUtil.formatarValor(fechamentoConsolidado.getTotalDesconto()));
			fechamentoCEIntegracao.setTotalLiquido(CurrencyUtil.formatarValor(fechamentoConsolidado.getTotalBruto()
					.subtract(fechamentoConsolidado.getTotalDesconto())));
		}
		
		FileExporter.to("fechamento", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, fechamentoCEIntegracao, 
				listaFechamento, ItemFechamentoCEIntegracaoDTO.class, this.httpResponse);
			
		result.nothing();
	}
	
	private void tratarFiltro(FiltroFechamentoCEIntegracaoDTO filtroAtual) {

		FiltroFechamentoCEIntegracaoDTO filtroSession = (FiltroFechamentoCEIntegracaoDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {
			
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO, filtroAtual);
	}
	
	private void carregarComboFornecedores() {
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = fornecedorService.obterFornecedoresFcDinap();
		
		result.include("listaFornecedores",listaFornecedoresCombo );
	}
	
	public void atualizarEncalheCalcularTotais(Long idItemChamadaFornecedor, BigDecimal venda) {
		
		FiltroFechamentoCEIntegracaoDTO filtro = (FiltroFechamentoCEIntegracaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO);
		
		filtro.setPaginacao(null);
		filtro.setIdItemChamadaEncalheFornecedor(idItemChamadaFornecedor);
		
		FechamentoCEIntegracaoVO fechamentoCEIntegracao = new FechamentoCEIntegracaoVO();
			
		FechamentoCEIntegracaoConsolidadoDTO fechamentoConsolidado = this.fechamentoCEIntegracaoService.buscarConsolidadoItensFechamentoCeIntegracao(filtro, venda);
		
		fechamentoCEIntegracao.setTotalBruto(CurrencyUtil.formatarValor(fechamentoConsolidado.getTotalBruto()));
		fechamentoCEIntegracao.setTotalDesconto(CurrencyUtil.formatarValor(fechamentoConsolidado.getTotalDesconto()));
		fechamentoCEIntegracao.setTotalLiquido(CurrencyUtil.formatarValor(fechamentoConsolidado.getTotalLiquido()));
		
		result.use(Results.json()).withoutRoot().from(fechamentoCEIntegracao).recursive().serialize();
	}
	
	
	  @Get
	    @Path("/obterCESemana")
	    public void obterCESemana(final String semana) {
		  if ( semana == null ) return;
		  FiltroFechamentoCEIntegracaoDTO filtro = new FiltroFechamentoCEIntegracaoDTO();
		  filtro.setSemana(semana);
		  	List<ItemDTO<Long, String>> resultList = new ArrayList<ItemDTO<Long, String>>();
		    resultList.add(new ItemDTO(-1,"Selecione"));
	
		  List<ChamadaEncalheFornecedor> listaChamadaEncalheFornecedor = 
				  fechamentoCEIntegracaoService.obterChamadasEncalheFornecedorCE(filtro);
		    if (listaChamadaEncalheFornecedor != null && listaChamadaEncalheFornecedor.size() > 0 ) {
		  
		    for(ChamadaEncalheFornecedor cef:listaChamadaEncalheFornecedor )
		    	resultList.add(new ItemDTO(cef.getNumeroChamadaEncalhe(),cef.getNumeroChamadaEncalhe().toString()+" ("+cef.getStatusCeNDS()+")"));
		  
		    }
		   // else
		   // 	throw new ValidacaoException(TipoMensagem.WARNING, "Nao encontrado chamada encalhe para esta semana.");
		    result.use(Results.json()).from(resultList, "result").recursive().serialize();
	    }
	  
	  
	  
	
	@Post
	@Path("/salvarCE")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO_ALTERACAO)
	public void salvarCE(List<ItemFechamentoCEIntegracaoDTO> itens){
		
		if(itens != null && !itens.isEmpty()) {
			
			fechamentoCEIntegracaoService.salvarCE(itens);
		}
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Informações salvas com sucesso."), "result").recursive().serialize();
	}
	

	private Map<Long,ItemFechamentoCEIntegracaoDTO> obterMapItensCE(List<ItemFechamentoCEIntegracaoDTO> itens){
		
		if(itens == null || itens.isEmpty()) {
			
			return new HashMap<Long, ItemFechamentoCEIntegracaoDTO>();
		}
		
		Map<Long,ItemFechamentoCEIntegracaoDTO> mapItensCE = Maps.uniqueIndex(itens, new Function<ItemFechamentoCEIntegracaoDTO, Long>() {
			
			@Override
			public Long apply(ItemFechamentoCEIntegracaoDTO item) {
				
				return item.getIdItemCeIntegracao();
		}});
		
		return mapItensCE ;
	}
	
	@Post
	@Path("/geraChamadaCE")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO_ALTERACAO)
	public void geraChamadaCE() {
		
		session.setAttribute(CHAMADA_CE_GERADO, null);
		
		FiltroFechamentoCEIntegracaoDTO filtro = (FiltroFechamentoCEIntegracaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO);
		
		if(filtro == null || filtro.getSemana() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma pesquisa realizada.");
		}
		
		try {
			
			 byte[] chamadasEncalhe = fechamentoCEIntegracaoService.gerarImpressaoChamadaEncalheFornecedor(filtro); 

			session.setAttribute(CHAMADA_CE_GERADO, chamadasEncalhe);
			
			result.use(Results.json()).from("").serialize();
			
		} catch(ValidacaoException e) { 
			throw e;
		} catch(Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração da chamada de enclahe fornecedor.");
		}
	}
		
	@Get
	@Path("/imprimirCE")
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO_ALTERACAO)
	public void imprimirCE() throws Exception {
		
		byte[] chamadaCE = (byte[]) session.getAttribute(CHAMADA_CE_GERADO);
		
		session.setAttribute(CHAMADA_CE_GERADO, null);
		
		escreverArquivoParaResponse(chamadaCE, "chamadas-encalhe");
	}
	
	@Post
	@Path("/pesquisarPerdaGanho")
	public void pesquisarPerdaGanho(String semana, 
									Long idFornecedor,
									String comboCEIntegracao,
									String idChamadaEncalhe,
									String sortorder, 
									String sortname, 
									int page, 
									int rp,
									List<ItemFechamentoCEIntegracaoDTO> itens){
		
		validarAnoSemana(semana);
		
		FiltroFechamentoCEIntegracaoDTO filtroCE = new FiltroFechamentoCEIntegracaoDTO();
		filtroCE.setSemana(semana);
		filtroCE.setIdFornecedor(idFornecedor);
		filtroCE.setComboCeIntegracao(comboCEIntegracao);
		if ( idChamadaEncalhe != null && !"-1".equals(idChamadaEncalhe))
			filtroCE.setIdChamadaEncalhe(Long.parseLong(idChamadaEncalhe));
		
		filtroCE.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		filtroCE.setOrdenacaoColuna(Util.getEnumByStringValue(ColunaOrdenacaoFechamentoCEIntegracao.values(),sortname));
		
		this.tratarFiltro(filtroCE);
		
		Map<Long,ItemFechamentoCEIntegracaoDTO> itensAlteradosFechamento = this.obterMapItensCE(itens);
		
		FechamentoCEIntegracaoDTO fechamentoCEIntegracao = 
				fechamentoCEIntegracaoService.obterDiferencaCEIntegracaoFornecedor(filtroCE,itensAlteradosFechamento); 
		
		TableModel<CellModelKeyValue<ItemFechamentoCEIntegracaoDTO>> tableModel = 
				new TableModel<CellModelKeyValue<ItemFechamentoCEIntegracaoDTO>>();
		
		List<ItemFechamentoCEIntegracaoDTO> itensCE = PaginacaoUtil.paginarEmMemoria(fechamentoCEIntegracao.getItensFechamentoCE(),filtroCE.getPaginacao());
		
		if ( itensCE != null && itensCE.size() > 0 ) {
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(itensCE));
			
			tableModel.setPage(filtroCE.getPaginacao().getPaginaAtual());
			
			tableModel.setTotal(fechamentoCEIntegracao.getQntItensCE());
		}
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
}
