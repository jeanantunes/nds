package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
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
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoCobranca;
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

@Resource
@Path("devolucao/fechamentoCEIntegracao")
@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO)
public class FechamentoCEIntegracaoController extends BaseController{
	
	private static final String BOLETO_GERADO = "boletoDistribuidorGerado";
	
	private static final String FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO = "filtroFechamentoCEIntegracao";
	
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
	public void reabrirCeIntegracao(FiltroFechamentoCEIntegracaoDTO filtro){
		
		validarAnoSemana(filtro.getSemana());
		
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
	public void pesquisaPrincipal(FiltroFechamentoCEIntegracaoDTO filtro, String sortorder, String sortname, int page, int rp){
		
		validarAnoSemana(filtro.getSemana());
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		filtro.setOrdenacaoColuna(Util.getEnumByStringValue(ColunaOrdenacaoFechamentoCEIntegracao.values(),sortname));
		
		this.tratarFiltro(filtro);

		FechamentoCEIntegracaoDTO fechamentoCEIntegracao = fechamentoCEIntegracaoService.obterCEIntegracaoFornecedor(filtro); 
		
		FechamentoCEIntegracaoVO retornoPesquisa = renderizarPesquisa(fechamentoCEIntegracao, filtro);
		
		result.use(Results.json()).withoutRoot().from(retornoPesquisa).recursive().serialize();
		
	}
	
	private FechamentoCEIntegracaoVO renderizarPesquisa(FechamentoCEIntegracaoDTO dto, FiltroFechamentoCEIntegracaoDTO filtro){
		
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
	
	@Post
	@Path("fecharCE")
	public void fecharCE(){
		
		FiltroFechamentoCEIntegracaoDTO filtro = (FiltroFechamentoCEIntegracaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO);
		
		fechamentoCEIntegracaoService.fecharCE(filtro);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Fechamento realizado com sucesso."),"result").recursive().serialize();
		
	}

	@Post
	@Path("/geraBoleto")
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
	
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroFechamentoCEIntegracaoDTO filtro = (FiltroFechamentoCEIntegracaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO);
		
		filtro.getPaginacao().setQtdResultadosPorPagina(null);
		filtro.getPaginacao().setPaginaAtual(null);
		
		List<ItemFechamentoCEIntegracaoDTO> listaFechamento = this.fechamentoCEIntegracaoService.buscarItensFechamentoCeIntegracao(filtro);
		
		if(listaFechamento.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
		FileExporter.to("fechamento", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				listaFechamento, ItemFechamentoCEIntegracaoDTO.class, this.httpResponse);
			
		
		result.nothing();
	}
	
	private void tratarFiltro(FiltroFechamentoCEIntegracaoDTO filtroAtual) {

		FiltroFechamentoCEIntegracaoDTO filtroSession = (FiltroFechamentoCEIntegracaoDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO);
		
		if (filtroSession != null && filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO, filtroAtual);
	}
	
	private void carregarComboFornecedores() {
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresAtivos();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		result.include("listaFornecedores",listaFornecedoresCombo );
	}
	
	public void atualizarEncalheCalcularTotais(Long idItemChamadaFornecedor, BigInteger encalhe, BigInteger venda) {
		
		this.fechamentoCEIntegracaoService.atualizarItemChamadaEncalheFornecedor(
			idItemChamadaFornecedor, encalhe, venda);
		
		FiltroFechamentoCEIntegracaoDTO filtro =
			(FiltroFechamentoCEIntegracaoDTO)
				session.getAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO);
		
		filtro.setPaginacao(null);
		
		FechamentoCEIntegracaoVO fechamentoCEIntegracao = new FechamentoCEIntegracaoVO();
		
		FechamentoCEIntegracaoConsolidadoDTO fechamentoConsolidado = 
			this.fechamentoCEIntegracaoService.buscarConsolidadoItensFechamentoCeIntegracao(filtro);
		
		fechamentoCEIntegracao.setTotalBruto(CurrencyUtil.formatarValor(fechamentoConsolidado.getTotalBruto()));
		fechamentoCEIntegracao.setTotalDesconto(CurrencyUtil.formatarValor(fechamentoConsolidado.getTotalDesconto()));
		fechamentoCEIntegracao.setTotalLiquido(CurrencyUtil.formatarValor(fechamentoConsolidado.getTotalLiquido()));
		
		result.use(Results.json()).withoutRoot().from(fechamentoCEIntegracao).recursive().serialize();
	}

}
