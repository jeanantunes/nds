package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PDFUtil;
import br.com.abril.nds.client.vo.CalculaParcelasVO;
import br.com.abril.nds.client.vo.NegociacaoDividaDetalheVO;
import br.com.abril.nds.client.vo.NegociacaoDividaVO;
import br.com.abril.nds.dto.DiaSemanaDTO;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.NegociacaoDividaPaginacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCalculaParcelas;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.PeriodicidadeCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.financeiro.ParcelaNegociacao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("financeiro/negociacaoDivida")
public class NegociacaoDividaController {
	
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
	@Rules(Permissao.ROLE_FINANCEIRO_NEGOCIACAO_DIVIDA)
	public void index(){
		
		Integer qntdParcelas = distribuidorService.obter().getNegociacaoAteParcelas();
		List<Integer> parcelas = new ArrayList<Integer>();
		for(int i = 1; i <= qntdParcelas; i++){
			parcelas.add(i);
		}
		
		
		FiltroConsultaBancosDTO  filtro = new FiltroConsultaBancosDTO();
		filtro.setAtivo(true);
		List<Banco> bancos = bancoService.obterBancos(filtro);
		
	
		
		result.include("qntdParcelas", parcelas);
		result.include("bancos", bancos);
		result.include("tipoPagamento", TipoCobranca.values());
		
		this.session.setAttribute(ID_ULTIMA_NEGOCIACAO, null);
	}
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroConsultaNegociacaoDivida filtro, String sortname, String sortorder, int rp, int page) {
		
		this.session.setAttribute(ID_ULTIMA_NEGOCIACAO, null);
		
		filtro.setPaginacaoVO(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.session.setAttribute(FILTRO_NEGOCIACAO_DIVIDA, filtro);
		
		NegociacaoDividaPaginacaoDTO dto = this.negociacaoDividaService.obterDividasPorCotaPaginado(filtro);
		
		List<NegociacaoDividaDTO> list = dto.getListaNegociacaoDividaDTO();
		List<NegociacaoDividaVO> listDividas = new ArrayList<NegociacaoDividaVO>();
		for (NegociacaoDividaDTO negociacao : list) {
			listDividas.add(new NegociacaoDividaVO(negociacao));
		}
		
		result.use(
				FlexiGridJson.class).from(
						listDividas).total(
								dto.getQuantidadeRegistros().intValue()).page(page).serialize();
	}
	
	
	@Path("/pesquisarDetalhes.json")
	public void pesquisarDetalhes(Long idCobranca) {
		
		List<NegociacaoDividaDetalheVO> listDividas = negociacaoDividaService.obterDetalhesCobranca(idCobranca);//new ArrayList<NegociacaoDividaDetalheVO>();
		System.out.println(listDividas.size());
		result.use(FlexiGridJson.class).from(listDividas).total(listDividas.size()).page(1).serialize();
	}
	
	@Path("/calcularParcelas.json")
	public void calcularParcelas(FiltroCalculaParcelas filtro) {
		
		List<CalculaParcelasVO> listParcelas = new ArrayList<CalculaParcelasVO>();
		
		Double valorParcela = filtro.getValorSelecionado() / filtro.getQntdParcelas();
		
		Date dataAnterior = new Date();
			
		for (int i = 0; i < filtro.getQntdParcelas(); i++) {
			CalculaParcelasVO parcela = new CalculaParcelasVO();
			
			parcela.setNumParcela(Integer.toString(i+1));
			parcela.setParcela(CurrencyUtil.formatarValor(valorParcela));
			
			dataAnterior = getDataParcela(dataAnterior, filtro.getPeriodicidade(), filtro.getSemanalDias(), filtro.getMensalDia());
			
			parcela.setDataVencimento(DateUtil.formatarDataPTBR(dataAnterior));
						
			Banco banco = bancoService.obterBancoPorId(filtro.getIdBanco());
			
			Double encargos = 0.0;
			
			if( !filtro.getTipoPagamento().equals(TipoCobranca.CHEQUE) || (filtro.getIsentaEncargos()!= null && filtro.getIsentaEncargos()) )
				encargos = calcularEncargos(valorParcela, DateUtil.parseDataPTBR(parcela.getDataVencimento()),filtro.getNumeroCota(), banco);
						
			parcela.setEncargos(CurrencyUtil.formatarValor(encargos));
							
			valorParcela = valorParcela + encargos;
			
			parcela.setParcTotal(CurrencyUtil.formatarValor(valorParcela));
				
			listParcelas.add(parcela);	
		}
			
		
		this.result.use(Results.json()).from(listParcelas, "result").recursive().serialize();
	}
	
	private Double calcularEncargos(Double valorParcela, Date dataVencimento, Integer numeroCota, Banco banco) {
		
		Double encargos = 0.0;
		
		BigDecimal juros = cobrancaService.calcularJuros(banco, cotaService.obterPorNumeroDaCota(numeroCota), 
				distribuidorService.obter(), BigDecimal.valueOf(valorParcela), dataVencimento, new Date());
		
		BigDecimal multas = cobrancaService.calcularMulta(banco, cotaService.obterPorNumeroDaCota(numeroCota), 
					distribuidorService.obter(), BigDecimal.valueOf(valorParcela));
		
		encargos = juros.add(multas).doubleValue();
				
		return encargos;
	}


	private Date getDataParcela(Date dataAnterior, PeriodicidadeCobranca periodicidade, List<DiaSemanaDTO>semanalDias, Integer diaMensal) {
		
		switch(periodicidade){
			
			case DIARIO:
				return DateUtil.adicionarDias(dataAnterior, 1);	
						
			case SEMANAL:
				
				if(semanalDias == null || semanalDias.isEmpty())
					throw new ValidacaoException(TipoMensagem.WARNING, "Dia(s) da semana não selecionado(s).");
				
				Calendar proximoDia = Calendar.getInstance();
				
				while(true) {
					
					proximoDia.setTime(DateUtil.adicionarDias(proximoDia.getTime(), 1));
									
					for(DiaSemanaDTO dia : semanalDias) {
												
						if(proximoDia.get(Calendar.DAY_OF_WEEK) == dia.getNumDia()) 
							return proximoDia.getTime();												
					}
				}
			
			case QUINZENAL:
				return DateUtil.adicionarDias(dataAnterior, 15);	
			
			case MENSAL:
				
				if(diaMensal==null)
					throw new ValidacaoException(TipoMensagem.WARNING, "Dia mensal não selecionado.");
				
				Calendar data = Calendar.getInstance();
				data.setTime(dataAnterior);
				
				if(data.get(Calendar.DAY_OF_MONTH) > diaMensal){	
					data.add(Calendar.MONTH, 1);	
				}
				data.set(Calendar.DAY_OF_MONTH, diaMensal);
				
				return data.getTime();
		}		
		
		return null;
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
			
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Negociação já efetuada."), "result").recursive().serialize();
			
			return;
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
		}
		
		idNegociacao = this.negociacaoDividaService.criarNegociacao(
				filtro.getNumeroCota(), 
				parcelas, 
				valorDividaComissao,
				idsCobrancas, 
				this.getUsuario(), 
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
		
		FiltroConsultaNegociacaoDivida filtro = (FiltroConsultaNegociacaoDivida) this.session.getAttribute(FILTRO_NEGOCIACAO_DIVIDA);
		
		this.result.use(Results.json()).from(this.descontoService.obterComissaoCota(filtro.getNumeroCota()), "result").serialize();
	}
	
	private NDSFileHeader getNDSFileHeader() {

		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		Distribuidor distribuidor = distribuidorService.obter();

		if (distribuidor != null) {
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}

		ndsFileHeader.setData(new Date());
		ndsFileHeader.setNomeUsuario(getUsuario().getNome());
		return ndsFileHeader;
	}
	
	// TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {

		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Jornaleiro da Silva");

		return usuario;
	}
	
	public void imprimirNegociacao() throws Exception{
		
		Long idNegociacao = (Long) this.session.getAttribute(ID_ULTIMA_NEGOCIACAO);
		
		if (idNegociacao == null){
			
			throw new ValidacaoException(
					TipoMensagem.WARNING, "É necessário confirmar a negociação antes de imprimir.");
		}
		
		byte[] arquivo = this.negociacaoDividaService.imprimirNegociacao(idNegociacao);
		
		this.httpServletResponse.setContentType("application/pdf");
		this.httpServletResponse.setHeader("Content-Disposition", "attachment; filename=negociacao.pdf");
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