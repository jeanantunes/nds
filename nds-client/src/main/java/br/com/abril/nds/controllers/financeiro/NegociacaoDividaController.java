package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.CalculaParcelasVO;
import br.com.abril.nds.client.vo.NegociacaoDividaDetalheVO;
import br.com.abril.nds.client.vo.NegociacaoDividaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.NegociacaoDividaPaginacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCalculaParcelas;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.financeiro.ParcelaNegociacao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.PDFUtil;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("financeiro/negociacaoDivida")
@Rules(Permissao.ROLE_FINANCEIRO_NEGOCIACAO_DIVIDA)
public class NegociacaoDividaController extends BaseController {
	
	private static final String FILTRO_NEGOCIACAO_DIVIDA = "FILTRO_NEGOCIACAO_DIVIDA";

	private static final String ID_ULTIMA_NEGOCIACAO = "ID_ULTIMA_NEGOCIACAO";
	
	@Autowired
	private NegociacaoDividaService negociacaoDividaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private BancoService bancoService;
	
	@Autowired
	private CobrancaService cobrancaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private DescontoService descontoService;
	
	private Result result;
	
	public NegociacaoDividaController(Result result) {
		super();
		this.result = result;
	}

	@Path("/")
	public void index(){
		
		Integer qntdParcelas = this.distribuidorService.negociacaoAteParcelas();
		
		qntdParcelas = (qntdParcelas == null ? 0 : qntdParcelas);
		
		List<Integer> parcelas = new ArrayList<Integer>();
		
		for(int i = 1; i <= qntdParcelas; i++){
			parcelas.add(i);
		}
		
		FiltroConsultaBancosDTO  filtro = new FiltroConsultaBancosDTO();
		filtro.setAtivo(true);
		
		List<Banco> bancos = this.bancoService.obterBancos(filtro);
		
		this.result.include("qntdParcelas", parcelas);
		this.result.include("bancos", bancos);
		
		List<TipoCobranca> tiposCobranca = this.cobrancaService.obterTiposCobrancaCadastradas();
		
		this.result.include("tipoPagamento", tiposCobranca);
		
		this.session.setAttribute(ID_ULTIMA_NEGOCIACAO, null);
	}
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroConsultaNegociacaoDivida filtro, String sortname, String sortorder, int rp, int page) {
		
		this.session.setAttribute(ID_ULTIMA_NEGOCIACAO, null);
		
		filtro.setPaginacaoVO(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.session.setAttribute(FILTRO_NEGOCIACAO_DIVIDA, filtro);
		
		NegociacaoDividaPaginacaoDTO dto = this.negociacaoDividaService.obterDividasPorCotaPaginado(filtro);
		
		List<NegociacaoDividaDTO> listaNegociacaoDivida = dto.getListaNegociacaoDividaDTO();
		
		if (listaNegociacaoDivida.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<NegociacaoDividaVO> listDividas = new ArrayList<NegociacaoDividaVO>();
		for (NegociacaoDividaDTO negociacao : listaNegociacaoDivida) {
			listDividas.add(new NegociacaoDividaVO(negociacao));
		}
		
		this.result.use(
				FlexiGridJson.class).from(
						listDividas).total(
								dto.getQuantidadeRegistros().intValue()).page(page).serialize();
	}
	
	
	@Path("/pesquisarDetalhes.json")
	public void pesquisarDetalhes(Long idCobranca) {
		
		List<NegociacaoDividaDetalheVO> listDividas = negociacaoDividaService.obterDetalhesCobranca(idCobranca);
		
		result.use(FlexiGridJson.class).from(listDividas).total(listDividas.size()).page(1).serialize();
	}
	
	@Path("/calcularParcelas.json")
	public void calcularParcelas(FiltroCalculaParcelas filtro) {		
		this.result.use(Results.json()).from(negociacaoDividaService.calcularParcelas(filtro), "result").recursive().serialize();
	}
	
	@Path("/recalcularParcelas.json")
	public void recalcularParcelas(FiltroCalculaParcelas filtro, List<CalculaParcelasVO> parcelas) {		
		this.result.use(Results.json()).from(negociacaoDividaService.recalcularParcelas(filtro,parcelas), "result").recursive().serialize();
	}
	
	
	@Path("/exportar")
	public void exportar(FileType fileType) throws IOException {
		
		FiltroConsultaNegociacaoDivida filtro = (FiltroConsultaNegociacaoDivida) this.session.getAttribute(FILTRO_NEGOCIACAO_DIVIDA);
		
		List<NegociacaoDividaDTO> listDividas = negociacaoDividaService.obterDividasPorCota(filtro);
		
		FileExporter.to("consulta-box", fileType).inHTTPResponse(
				this.getNDSFileHeader(), null, null,
				listDividas, NegociacaoDividaDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	@Post
	public void confirmarNegociacao(boolean porComissao, BigDecimal comissaoAtualCota, BigDecimal comissaoUtilizar, 
			TipoCobranca tipoCobranca, TipoFormaCobranca tipoFormaCobranca, List<DiaSemana> diasSemana,
			Integer diaInicio, Integer diaFim, boolean negociacaoAvulsa, boolean isentaEncargos,
			Integer ativarAposPagar, List<ParcelaNegociacao> parcelas, List<Long> idsCobrancas, Long idBanco,
			BigDecimal valorDividaComissao,boolean recebeCobrancaPorEmail){
		
		Long idNegociacao = (Long) this.session.getAttribute(ID_ULTIMA_NEGOCIACAO);
		
		if (idNegociacao != null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Negociação já efetuada.");
		}
		
		FiltroConsultaNegociacaoDivida filtro = (FiltroConsultaNegociacaoDivida) 
				this.session.getAttribute(FILTRO_NEGOCIACAO_DIVIDA);
		
		FormaCobranca formaCobranca = null;
		
		if (porComissao){
			
			parcelas = null;
		} else {
			
			formaCobranca = new FormaCobranca();
			formaCobranca.setTipoCobranca(tipoCobranca);
			formaCobranca.setTipoFormaCobranca(tipoFormaCobranca);
			formaCobranca.setRecebeCobrancaEmail(recebeCobrancaPorEmail);
			
			Set<ConcentracaoCobrancaCota> concentracaoCobrancaCota = new HashSet<ConcentracaoCobrancaCota>();
			if (diasSemana != null){
				for (DiaSemana codigoDia : diasSemana){
					
					ConcentracaoCobrancaCota concentracao = new ConcentracaoCobrancaCota();
					concentracao.setDiaSemana(codigoDia);
					concentracao.setFormaCobranca(formaCobranca);
					
					concentracaoCobrancaCota.add(concentracao);
				}
			}
			
			formaCobranca.setConcentracaoCobrancaCota(concentracaoCobrancaCota);
			comissaoUtilizar = null;
			
			if (diaInicio != null){
			
				List<Integer> diasMes = new ArrayList<Integer>();
				diasMes.add(diaInicio);
				
				if (diaFim != null){
					
					diasMes.add(diaFim);
				}
				
				formaCobranca.setDiasDoMes(diasMes);
			}
		}
		
		idNegociacao = this.negociacaoDividaService.criarNegociacao(
				filtro.getNumeroCota(), 
				parcelas, 
				valorDividaComissao,
				idsCobrancas, 
				this.getUsuarioLogado(), 
				negociacaoAvulsa, 
				ativarAposPagar, 
				comissaoUtilizar, 
				isentaEncargos,
				formaCobranca,
				idBanco);
		
		this.session.setAttribute(ID_ULTIMA_NEGOCIACAO, idNegociacao);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Negociação efetuada."), "result").recursive().serialize();
	}

	@Post
	public void buscarComissaoCota(){
		
		BigDecimal comissao = this.descontoService.obterComissaoParametroDistribuidor();
		
		Integer numeroCota = 
				((FiltroConsultaNegociacaoDivida)this.session.getAttribute(FILTRO_NEGOCIACAO_DIVIDA))
				.getNumeroCota();
		
		BigDecimal comissaoCota = this.descontoService.obterComissaoCota(numeroCota);
		
		if (comissao == null || BigDecimal.ZERO.compareTo(comissao) == 0 ||
				comissaoCota == null || BigDecimal.ZERO.compareTo(comissaoCota) == 0){
			
			this.result.use(Results.json()).from("", "result").serialize();
		} else {
			
			List<BigDecimal> valoresDesconto = new ArrayList<BigDecimal>();
			valoresDesconto.add(comissao);
			valoresDesconto.add(comissaoCota.setScale(2, RoundingMode.HALF_EVEN));
			
			this.result.use(Results.json()).from(valoresDesconto, "result").recursive().serialize();
		}
	}
	
	public void imprimirNegociacao() throws Exception{
		
		Long idNegociacao = (Long) this.session.getAttribute(ID_ULTIMA_NEGOCIACAO);
		
		if (idNegociacao == null){
			
			throw new ValidacaoException(
					TipoMensagem.WARNING, "É necessário confirmar a negociação antes de imprimir.");
		}
		
		byte[] arquivo = this.negociacaoDividaService.imprimirNegociacao(idNegociacao);
		
		this.httpServletResponse.setContentType("application/pdf");
		this.httpServletResponse.setHeader("Content-Disposition", "attachment; filename=negociacao_"+idNegociacao+".pdf");
		this.httpServletResponse.getOutputStream().write(arquivo);
		this.httpServletResponse.getOutputStream().close();
		
		this.result.use(Results.nothing());
	}
	
	public void imprimirBoletos() throws IOException{
		
		Long idNegociacao = (Long) this.session.getAttribute(ID_ULTIMA_NEGOCIACAO);
		
		if (idNegociacao == null){
			
			throw new ValidacaoException(
					TipoMensagem.WARNING, "É necessário confirmar a negociação antes de imprimir.");
		}
		
		List<byte[]> lista = this.negociacaoDividaService.gerarBoletosNegociacao(idNegociacao);
		
		byte[] arquivo = PDFUtil.mergePDFs(lista);
		
		this.httpServletResponse.setContentType("application/pdf");
		
		this.httpServletResponse.setHeader("Content-Disposition", "attachment; filename=boletos.pdf");
		
		OutputStream output = this.httpServletResponse.getOutputStream();
		
		output.write(arquivo);
		
		this.httpServletResponse.getOutputStream().close();
		
		result.use(Results.nothing());
	}
}