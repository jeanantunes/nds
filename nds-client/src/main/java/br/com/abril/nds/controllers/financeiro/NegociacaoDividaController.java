package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.CalculaParcelasVO;
import br.com.abril.nds.client.vo.NegociacaoDividaDetalheVO;
import br.com.abril.nds.client.vo.NegociacaoDividaVO;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroCalculaParcelas;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PeriodicidadeCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("financeiro/negociacaoDivida")
public class NegociacaoDividaController {
	
	private static final String FILTRO_NEGOCIACAO_DIVIDA = "FILTRO_NEGOCIACAO_DIVIDA";
	
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
	}
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroConsultaNegociacaoDivida filtro, String sortname, String sortorder, int rp, int page) {
		
		this.session.setAttribute(FILTRO_NEGOCIACAO_DIVIDA, filtro);
		
		List<NegociacaoDividaDTO> list = negociacaoDividaService.obterDividasPorCota(filtro);	
		List<NegociacaoDividaVO> listDividas = new ArrayList<NegociacaoDividaVO>();
		for (NegociacaoDividaDTO negociacao : list) {
			listDividas.add(new NegociacaoDividaVO(negociacao));
		}
		
		result.use(FlexiGridJson.class).from(listDividas).total(listDividas.size()).page(page).serialize();
	}
	
	
	@Path("/pesquisarDetalhes.json")
	public void pesquisarDetalhes(FiltroConsultaNegociacaoDivida filtro, String sortname, String sortorder, int rp, int page) {
		// TODO
		this.session.setAttribute(FILTRO_NEGOCIACAO_DIVIDA, filtro);
		
		List<NegociacaoDividaDTO> list = negociacaoDividaService.obterDividasPorCota(filtro);
		List<NegociacaoDividaDetalheVO> listDividas = new ArrayList<NegociacaoDividaDetalheVO>();
		/*for (Cobranca c : list){
			NegociacaoDividaDetalheVO ndd = new NegociacaoDividaDetalheVO();
			ndd.setData(DateUtil.formatarDataPTBR(c.getDivida().getData()));
			if(c.getStatusCobranca() == StatusCobranca.PAGO)
				ndd.setTipo("Pagamento");
			else
				ndd.setTipo("Dívida");
			ndd.setValor("-"+ CurrencyUtil.formatarValor(c.getDivida().getValor()));
			ndd.setObservacao("TESTE");
			listDividas.add(ndd);
		}*/
		result.use(FlexiGridJson.class).from(listDividas).total(listDividas.size()).page(page).serialize();
	}
	
	@Path("/calcularParcelas.json")
	public void calcularParcelas(FiltroCalculaParcelas filtro) {
		/*System.out.println("Mensal "+filtro.getMensalDia());
		System.out.println("Periodicidade "+filtro.getPeriodicidade());
		System.out.println("Q1 "+filtro.getQuinzenalDia1());
		System.out.println("Q2 "+filtro.getQuinzenalDia2());
		System.out.println("Valor "+filtro.getValorSelecionado());
		System.out.println("Qntd parcelas "+filtro.getQntdParcelas());
		System.out.println("TipoPagamento "+filtro.getTipoPagamento().toString());*/

		List<CalculaParcelasVO> listParcelas = new ArrayList<CalculaParcelasVO>();
		if(filtro.getTipoPagamento().equals(TipoCobranca.CHEQUE)){
			//TODO
		}else{
			
			
			for (int i = 0; i < filtro.getQntdParcelas(); i++) {
				CalculaParcelasVO parcela = new CalculaParcelasVO();
				/* NUMERO DA PARCELA */
				parcela.setNumParcela(Integer.toString(i+1));
				
				/* VALOR DA PARCELA */
				Double valor = Double.valueOf((filtro.getValorSelecionado().replaceAll("[.]", "")).replaceAll("[,]", "."));
				valor = valor/filtro.getQntdParcelas();
				parcela.setParcela(CurrencyUtil.formatarValor(valor));
				
				/* DATA VENCIMENTO */
				switch(filtro.getPeriodicidade()){
					case DIARIO:
						parcela.setDataVencimento(DateUtil.formatarDataPTBR(DateUtil.adicionarDias(new Date(), 1)));	
					break;
					
					case SEMANAL:
						if(!listParcelas.isEmpty())
							for (int j = 0; j < filtro.getSemanalDias().size(); j++) {
								filtro.getSemanalDias().get(j);
							}
					break;
					
					case QUINZENAL:
						
					break;
					
					case MENSAL:
						Calendar data = Calendar.getInstance();
						if(data.get(Calendar.DAY_OF_MONTH) > Integer.parseInt(filtro.getMensalDia())){	
							data.add(Calendar.MONTH, 1);	
						}
						data.set(Calendar.DAY_OF_MONTH, Integer.parseInt(filtro.getMensalDia()));
						parcela.setDataVencimento(DateUtil.formatarDataPTBR(data.getTime()));
						
				}
				
				
				/*if(filtro.getPeriodicidade().equals(PeriodicidadeCobranca.DIARIO)){
				
					parcela.setDataVencimento(DateUtil.formatarDataPTBR(DateUtil.adicionarDias(new Date(), 1)));
					
				}else if(filtro.getPeriodicidade().equals(PeriodicidadeCobranca.SEMANAL)){
					
						//parcela.setDataVencimento();
					
				}else if(filtro.getPeriodicidade().equals(PeriodicidadeCobranca.QUINZENAL)){
					
					parcela.setDataVencimento(DateUtil.formatarDataPTBR(DateUtil.adicionarDias(new Date(), 1)));
					
				}else{
					Calendar data = Calendar.getInstance();
					if(data.get(Calendar.DAY_OF_MONTH) > Integer.parseInt(filtro.getMensalDia())){	
						data.add(Calendar.MONTH, 1);	
					}
					data.set(Calendar.DAY_OF_MONTH, Integer.parseInt(filtro.getMensalDia()));
					parcela.setDataVencimento(DateUtil.formatarDataPTBR(data.getTime()));
				}*/
				
				/* ENCARGOS */
				Banco banco = bancoService.obterBancoPorId(filtro.getIdBanco());
				Double encargos = 0.0;
				BigDecimal juros = cobrancaService.calcularJuros(banco, cotaService.obterPorNumeroDaCota(filtro.getNumeroCota()), 
							distribuidorService.obter(), BigDecimal.valueOf(valor), DateUtil.parseDataPTBR(parcela.getDataVencimento()), new Date());
				BigDecimal multas = cobrancaService.calcularMulta(banco, cotaService.obterPorNumeroDaCota(filtro.getNumeroCota()), 
							distribuidorService.obter(), BigDecimal.valueOf(valor));
				encargos = juros.add(multas).doubleValue();
					
				
				parcela.setEncargos(CurrencyUtil.formatarValor(encargos));
					
				
				/* PARCELA TOTAL */
				valor = valor + encargos;
				parcela.setParcTotal(CurrencyUtil.formatarValor(valor));
		
				
				listParcelas.add(parcela);	
			}
			
			
		}
		this.result.use(Results.json()).from(listParcelas, "result").recursive().serialize();
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
	

	
	
}
