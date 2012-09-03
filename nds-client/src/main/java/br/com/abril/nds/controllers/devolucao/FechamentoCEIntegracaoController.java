package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.FechamentoCEIntegracaoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
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
@Path("devolucao/fechamentoCEIntegracao")
public class FechamentoCEIntegracaoController {
	
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
	private BoletoService boletoService;
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	
	public FechamentoCEIntegracaoController(Result result) {
		 this.result = result;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_DEVOLUCAO_FECHAMENTO_INTEGRACAO)
	public void index(){
		this.carregarComboFornecedores();
		
	}
	
	@Post
	@Path("/pesquisaPrincipal")
	public void pesquisaPrincipal(FiltroFechamentoCEIntegracaoDTO filtro, String sortorder, String sortname, int page, int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>> tableModel = efetuarConsultaFechamentoCEIntegracao(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	
	private TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>> efetuarConsultaFechamentoCEIntegracao(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		if(filtro.getSemana() != null){
			filtro.setPeriodoRecolhimento(obterDataDaSemana(filtro));
		}
		
		List<FechamentoCEIntegracaoDTO> listaFechamento = this.fechamentoCEIntegracaoService.buscarFechamentoEncalhe(filtro);
		
		if(listaFechamento.size() == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A pesquisa realizada não obteve resultado.");
		}
		
		listaFechamento = this.fechamentoCEIntegracaoService.calcularVenda(listaFechamento);
		
		TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>> tableModel = new TableModel<CellModelKeyValue<FechamentoCEIntegracaoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaFechamento));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(listaFechamento.size());
		
		return tableModel;
	}
	
	private Intervalo<Date> obterDataDaSemana(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		Date dataInicioSemana = 
				DateUtil.obterDataDaSemanaNoAno(
					filtro.getSemana().intValue(), distribuidor.getInicioSemana().getCodigoDiaSemana(), null);
			
		Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
		
		Intervalo<Date> periodoRecolhimento = new Intervalo<Date>(dataInicioSemana, dataFimSemana);
		
		return periodoRecolhimento;
		
	}

	@Post
	public void buscarTotalDaPesquisa(){
		
		FiltroFechamentoCEIntegracaoDTO filtro = (FiltroFechamentoCEIntegracaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO);
		
		List<FechamentoCEIntegracaoDTO> listaFechamento = this.fechamentoCEIntegracaoService.buscarFechamentoEncalhe(filtro);
		
		listaFechamento = this.fechamentoCEIntegracaoService.calcularVenda(listaFechamento);
		double totalBruto = 0;
		BigDecimal desconto = new BigDecimal(0);
		for(FechamentoCEIntegracaoDTO dto: listaFechamento){
			double valorDaVenda =  dto.getVenda().doubleValue() * dto.getPrecoCapa().doubleValue();
			totalBruto = totalBruto + valorDaVenda;
			Cota cota = this.cotaService.obterPorId(dto.getIdCota());
			ProdutoEdicao pe = this.produtoEdicaoService.obterProdutoEdicao(dto.getIdProdutoEdicao());
			desconto.add(this.descontoService.obterDescontoPorCotaProdutoEdicao(cota, pe));
			
		}		
		
		StringBuilder html = new StringBuilder();
		html.append("<td width='88' valign='top'><strong>Total Bruto R$:</strong></td>");
		html.append("<td width='50 valign='top'>"+  (CurrencyUtil.formatarValor(totalBruto)) +"</td>");
		html.append(" <td width='106' valign='top'><strong>Total Desconto R$:</strong></td>");
		html.append(" <td width='49' valign='top'>"+(CurrencyUtil.formatarValor(desconto))+"</td>");
		html.append(" <td width='93' valign='top'><strong>Total Líquido R$:</strong></td>");
		html.append(" <td width='70' valign='top'>"+(CurrencyUtil.formatarValor(totalBruto - desconto.doubleValue()))+"</td>");
		
		this.result.use(Results.json()).from(html.toString(), "result").recursive().serialize();
		
	}
	
	@Get
	@Path("/imprimeBoleto")
	public void imprimeBoleto(String nossoNumero) throws Exception{


		byte[] b = boletoService.gerarImpressaoBoleto(nossoNumero);

		this.httpResponse.setContentType("application/pdf");
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=boleto.pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		output.write(b);

//		//CONTROLE DE VIAS IMPRESSAS
//		boletoService.incrementarVia(nossoNumero);
		
		httpResponse.flushBuffer();
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroFechamentoCEIntegracaoDTO filtro = (FiltroFechamentoCEIntegracaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_FECHAMENTO_CE_INTEGRACAO);
		
		List<FechamentoCEIntegracaoDTO> listaFechamento = this.fechamentoCEIntegracaoService.buscarFechamentoEncalhe(filtro);
		
		listaFechamento = this.fechamentoCEIntegracaoService.calcularVenda(listaFechamento);
		
		if(listaFechamento.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
		FileExporter.to("fechamento", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				listaFechamento, FechamentoCEIntegracaoDTO.class, this.httpResponse);
			
		
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

	private void validarEntrada(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		if(filtro.getSemana() == null || filtro.getSemana() == 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Todos os filtros devem ser preenchidos!");
		}		
		
		
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
