package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.FechamentoCEIntegracaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FechamentoCEIntegracaoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
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
	@Rules(Permissao.ROLE_RECOLHIMENTO_FECHAMENTO_INTEGRACAO)
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
	@Path("/pesquisaPrincipal")
	public void pesquisaPrincipal(FiltroFechamentoCEIntegracaoDTO filtro, String sortorder, String sortname, int page, int rp){
		
		validarAnoSemana(filtro.getSemana());
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.tratarFiltro(filtro);

		filtro.setPeriodoRecolhimento(obterDataDaSemana(filtro.getSemana()));

		FechamentoCEIntegracaoVO fechamentoCEIntegracaoVO = fechamentoCEIntegracaoService.construirFechamentoCEIntegracaoVO(filtro); 
		
		result.use(Results.json()).withoutRoot().from(fechamentoCEIntegracaoVO).recursive().serialize();
		
	}
	
	private void validarAnoSemana(String anoSemana) {
		
		if(StringUtils.isEmpty(anoSemana) || !StringUtils.isNumeric(anoSemana) || 
				StringUtils.isBlank(anoSemana) || anoSemana.length() != 6) { 
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Digite um ano e semana valido (AAAASS). Ex: 201309");
		}
	}
	
	private Intervalo<Date> obterDataDaSemana(String anoSemana) {
		
		Date data = obterDataBase(anoSemana, this.distribuidorService.obterDataOperacaoDistribuidor()); 
		
		Integer semana = Integer.parseInt(anoSemana.substring(4));
		
		Date dataInicioSemana = 
				DateUtil.obterDataDaSemanaNoAno(
					semana, this.distribuidorService.inicioSemana().getCodigoDiaSemana(), data);
			
		Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
		
		Intervalo<Date> periodoRecolhimento = new Intervalo<Date>(dataInicioSemana, dataFimSemana);
		
		return periodoRecolhimento;
		
	}
	
	private Date obterDataBase(String anoSemana, Date data) {
		
		String ano = anoSemana.substring(0,4);
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.set(Calendar.YEAR, Integer.parseInt(ano));
		
		return c.getTime();
	}

	@Post
	@Path("fecharCE")
	public void fecharCE(String[] listaEncalhe, String[] listaIdProdutoEdicao, String idFornecedor, String semana){
		String listaEncalhePronta[] = listaEncalhe[0].split(",");		
		String listaIdProdutoEdicaoPronta[] = listaIdProdutoEdicao[0].split(",");		
		
		fechamentoCEIntegracaoService.fecharCE(listaEncalhePronta, listaIdProdutoEdicaoPronta, idFornecedor, new Integer(semana));
		
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
		
		List<FechamentoCEIntegracaoDTO> listaFechamento = this.fechamentoCEIntegracaoService.buscarFechamentoEncalhe(filtro);
		
		if(listaFechamento.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
		FileExporter.to("fechamento", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				listaFechamento, FechamentoCEIntegracaoDTO.class, this.httpResponse);
			
		
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

}
