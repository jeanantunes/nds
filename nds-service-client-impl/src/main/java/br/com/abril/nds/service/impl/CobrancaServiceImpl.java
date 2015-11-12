package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.lightcouch.CouchDbClient;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.abril.nds.client.vo.CobrancaDividaVO;
import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.client.vo.DetalhesDividaVO;
import br.com.abril.nds.dto.ExportarCobrancaDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.PagamentoDividasDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.BaixaManual;
import br.com.abril.nds.model.financeiro.BoletoAntecipado;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BaixaCobrancaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.AcumuloDividasService;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.BigDecimalUtil;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TipoBaixaCobranca;

@Service
public class CobrancaServiceImpl implements CobrancaService {
	
	private static final String DB_NAME = "exportacao_cobranca";
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	protected DistribuidorService distribuidorService;

	@Autowired
	protected CalendarioService calendarioService;
	
	@Autowired
	protected MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	protected CotaRepository cotaRepository;
	
	@Autowired
	protected MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	protected TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	protected BaixaCobrancaRepository baixaCobrancaRepository;
	
	@Autowired
	protected FormaCobrancaService formaCobrancaService;

	@Autowired
	protected AcumuloDividasService acumuloDividasService;
	
	@Autowired
	private DividaRepository dividaRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private NegociacaoDividaService negociacaoDividaService;
	
	@Autowired
	private CouchDbProperties couchDbProperties;
	
	private CouchDbClient couchDbClient;
	
	@PostConstruct
	public void initCouchDbClient() {
		
		org.lightcouch.CouchDbProperties properties = new org.lightcouch.CouchDbProperties()
			// .setDbName(DB_NAME)
			.setDbName(DB_NAME+"_db_"+
					
					 String.format("%08d",Integer.parseInt(distribuidorService.obter().getCodigoDistribuidorDinap())<=0?
							 Integer.parseInt(distribuidorService.obter().getCodigoDistribuidorFC())
							 :Integer.parseInt(distribuidorService.obter().getCodigoDistribuidorDinap())))
			.setCreateDbIfNotExist(true)
			.setProtocol(couchDbProperties.getProtocol())
			.setHost(couchDbProperties.getHost())
			.setPort(couchDbProperties.getPort())
			.setUsername(couchDbProperties.getUsername())
			.setPassword(couchDbProperties.getPassword())
			.setMaxConnections(30000)
			.setConnectionTimeout(30000); // timeout de 30 segundos
	
		this.couchDbClient = new CouchDbClient(properties);

	}
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public BigDecimal calcularJuros(Banco banco, 
									Long idCota,
									BigDecimal valorDivida, 
									Date dataVencimento, 
									Date dataCalculoJuros,
									FormaCobranca formaCobrancaPrincipal) {

		//TODO: JUROS E MULTA - VERIFICAR NA COBRANÇA (POSSIVEL ALTERAÇÃO NO MODELO) - FALAR COM CÉSAR
		
		Banco bancoFormaCobrancaPrincipal = null;
		
		if (formaCobrancaPrincipal != null) {
			bancoFormaCobrancaPrincipal  =  formaCobrancaPrincipal.getBanco();
		}
		
		BigDecimal taxaJurosMensal = null;
		BigDecimal valorCalculadoJuros = null;

		// Obtém taxa de juros do banco informado (caso exista)
		if (banco != null && banco.getJuros() != null) {
		
			taxaJurosMensal = banco.getJuros();
			
		// Obtém taxa de juros do banco da forma de cobrança princial (caso exista)
		} else if (bancoFormaCobrancaPrincipal != null
						&& bancoFormaCobrancaPrincipal.getJuros() != null) {			
			
			taxaJurosMensal = bancoFormaCobrancaPrincipal.getJuros();
			
		// Obtém taxa de juros da forma de cobrança princial (caso exista)
		} else if (formaCobrancaPrincipal != null 
						&& formaCobrancaPrincipal.getTaxaJurosMensal() != null) {
	
			taxaJurosMensal = formaCobrancaPrincipal.getTaxaJurosMensal();
		}
		
		if (taxaJurosMensal == null || taxaJurosMensal.equals(BigDecimal.ZERO)) {
			
			return BigDecimal.ZERO;
		}
		
		dataCalculoJuros = this.calendarioService.obterProximaDataDiaUtil(dataCalculoJuros);
		
		long quantidadeDias = DateUtil.obterDiferencaDias(dataVencimento, dataCalculoJuros);

		BigDecimal taxaJurosDiaria = MathUtil.divide(taxaJurosMensal, new BigDecimal(30));

		valorCalculadoJuros = valorDivida.multiply(MathUtil.divide(taxaJurosDiaria, new BigDecimal(100)));

		return valorCalculadoJuros.multiply(new BigDecimal(quantidadeDias));
	}

	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public BigDecimal calcularMulta(Banco banco, Cota cota,
									BigDecimal valor,
									FormaCobranca formaCobrancaPrincipal) {
		

		//TODO: JUROS E MULTA - VERIFICAR NA COBRANÇA (POSSIVEL ALTERAÇÃO NO MODELO) - FALAR COM CÉSAR
		
		Banco bancoFormaCobrancaPrincipal = null;
		
		if (formaCobrancaPrincipal != null) {
			bancoFormaCobrancaPrincipal  =  formaCobrancaPrincipal.getBanco();
		}
		
		BigDecimal taxaMulta = null;
		BigDecimal valorCalculadoMulta = null;
		
		// Obtém valor ou taxa de multa do banco informado (caso exista)
		if (banco != null && (banco.getVrMulta() != null || banco.getMulta() != null)) {
		
			if (banco.getVrMulta() != null) {
			
				valorCalculadoMulta = banco.getVrMulta();
				
			} else {
				
				taxaMulta = banco.getMulta();
			}
			
		// Obtém valor ou taxa de multa do banco da forma de cobrança princial (caso exista)
		} else if (bancoFormaCobrancaPrincipal != null
						&& (bancoFormaCobrancaPrincipal.getVrMulta() != null 
								|| bancoFormaCobrancaPrincipal.getMulta() != null)) {
			
			if (bancoFormaCobrancaPrincipal.getVrMulta() != null) {
				
				valorCalculadoMulta = bancoFormaCobrancaPrincipal.getVrMulta();
				
			} else {
				
				taxaMulta = bancoFormaCobrancaPrincipal.getMulta();
			}
			
		// Obtém valor ou taxa de multa da forma de cobrança princial (caso exista)
		} else if (formaCobrancaPrincipal != null 
						&& (formaCobrancaPrincipal.getValorMulta() != null
								|| formaCobrancaPrincipal.getTaxaMulta() != null)) {
	
			if (formaCobrancaPrincipal.getValorMulta() != null) {
			
				valorCalculadoMulta = formaCobrancaPrincipal.getValorMulta();
				
			} else {
				
				taxaMulta = formaCobrancaPrincipal.getTaxaMulta();
			}
		}
	
		if (valorCalculadoMulta == null && taxaMulta != null) {
		
			valorCalculadoMulta = valor.multiply(MathUtil.divide(taxaMulta, new BigDecimal(100)));
		}
		
		return (valorCalculadoMulta != null) ? valorCalculadoMulta : BigDecimal.ZERO;
	}
	
	
	/**
	 * Método responsável por obter cobranças por numero da cota e vencimento
	 * @param filtro
	 * @return Lista de cobrancas encontradas
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Cobranca> obterCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro) {
		return null;//this.cobrancaRepository.obterCobrancasPorCota(filtro);TODO
	}
	
	
	/**
	 * Método responsável por obter quantidade cobranças por numero da cota e vencimento
	 * @param filtro
	 * @return int
	 */
	@Override
	@Transactional(readOnly=true)
	public int obterQuantidadeCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro) {
		return (int) this.cobrancaRepository.obterQuantidadeCobrancasPorCota(filtro);
	}
	
	
	/**
	 * Método responsável por obter dados de cobranças por numero da cota e vencimento
	 * @param filtro
	 * @return Lista de value objects com dados de cobrancas encontradas
	 */
	@Override
	@Transactional(readOnly=true)
	public List<CobrancaVO> obterDadosCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro) {

    	filtro.setAcumulaDivida(false);

    	return this.cobrancaRepository.obterCobrancasPorCota(filtro);
	}

	/**
	 * Método responsável por obter dados de cobrança por código
	 * @param idCobranca
	 * @param dataPagamento
	 * @return value object com dados da cobranca encontrada
	 */
	@Override
	@Transactional(readOnly=true)
	public CobrancaVO obterDadosCobranca(Long idCobranca, Date dataPagamento) {
		//PARAMETROS PARA CALCULO DE JUROS E MULTA
		
		CobrancaVO cobranca=null;
		
		Cobranca cob = cobrancaRepository.buscarPorId(idCobranca);
		
		if ((cob!=null)&&(cob.getStatusCobranca()==StatusCobranca.NAO_PAGO)){
			
		    if (cob.getDataPagamento() != null){
		        
		        return null;
		    }
		    
			cobranca = new CobrancaVO();
			
			cobranca.setNossoNumero(cob.getNossoNumero());	
			
			String cota = "";
			
			if ((cob.getCota().getPessoa()) instanceof PessoaFisica){
				cota = cob.getCota().getNumeroCota()+"-"+((PessoaFisica) cob.getCota().getPessoa()).getNome();
			}
			
			if ((cob.getCota().getPessoa()) instanceof PessoaJuridica){
				cota = cob.getCota().getNumeroCota()+"-"+((PessoaJuridica) cob.getCota().getPessoa()).getRazaoSocial();
			}
			
			cobranca.setCota(cota);
			cobranca.setBanco((cob.getBanco()!=null?cob.getBanco().getNome():""));
			cobranca.setDataVencimento((cob.getDataVencimento()!=null?DateUtil.formatarDataPTBR(cob.getDataVencimento()):""));
			cobranca.setDataEmissao((cob.getDataEmissao()!=null?DateUtil.formatarDataPTBR(cob.getDataEmissao()):""));
			cobranca.setValor(CurrencyUtil.formatarValor(cob.getValor()));
			cobranca.setDividaTotal(CurrencyUtil.formatarValor(cob.getDivida().getValor()));	
			
			//CALCULO DE JUROS E MULTA
			BigDecimal valorJurosCalculado = BigDecimal.ZERO;
			BigDecimal valorMultaCalculado = BigDecimal.ZERO; 
			
			Date vencimentoDiaUtil = calendarioService.adicionarDiasUteis(cob.getDataVencimento(), 0);

		    Date dataVencimento = DateUtil.parseDataPTBR((DateUtil.formatarDataPTBR(vencimentoDiaUtil)));
			
		    if (dataPagamento == null) {
				
				dataPagamento = dataVencimento;
			}
		    
			//CALCULA VALOR DO SALDO DA DIVIDA(MOVIMENTOS DE PAGAMENTO PARCIAL)
			BigDecimal saldoDivida = this.obterSaldoDivida(cob.getId());
			cobranca.setValorSaldo(CurrencyUtil.formatarValor(saldoDivida));
			
			if (dataVencimento.compareTo(dataPagamento) < 0) {
				
				FormaCobranca formaCobrancaPrincipal = 
					this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
				
				//CALCULA JUROS
				valorJurosCalculado =
					this.calcularJuros(cob.getBanco(), cob.getCota().getId(),
							           cob.getValor().subtract(saldoDivida), cob.getDataVencimento(),
							           dataPagamento, formaCobrancaPrincipal);
				//CALCULA MULTA
				valorMultaCalculado =
					this.calcularMulta(cob.getBanco(), cob.getCota(),
							           cob.getValor().subtract(saldoDivida), formaCobrancaPrincipal);
			}
			
			cobranca.setDataPagamento( DateUtil.formatarDataPTBR(dataPagamento) );
			cobranca.setDesconto( CurrencyUtil.formatarValor(BigDecimal.ZERO) );
			cobranca.setJuros( CurrencyUtil.formatarValor(valorJurosCalculado) );
            cobranca.setMulta( CurrencyUtil.formatarValor(valorMultaCalculado) );
            
            BigDecimal valor  = cob.getValor();
            
            //CALCULA VALOR TOTAL
            BigDecimal valorTotal = valor.add(valorJurosCalculado).add(valorMultaCalculado);
			cobranca.setValorTotal( CurrencyUtil.formatarValor(valorTotal) );
			
		}
		return cobranca;
	}	

	@Override
	@Transactional(readOnly=true)
	public CobrancaVO obterDadosCobrancaBoletoAntecipado(String nossoNumero, Date dataPagamento, BigDecimal valor) {

		BoletoAntecipado boleto = this.boletoService.obterBoletoEmBrancoPorNossoNumero(nossoNumero);
		
		if (boleto == null || boleto.getStatus().equals(StatusDivida.QUITADA)) {
			
			return null;
		}
		
		BigDecimal valorJurosCalculado = BigDecimal.ZERO;
		BigDecimal valorMultaCalculado = BigDecimal.ZERO;
		
		if (valor == null) {

			valor = boleto.getValor();
		}
		
		Date dataVencimento = boleto.getDataVencimento();
		
		if (dataPagamento == null) {
			
			dataPagamento = dataVencimento;
		}
		
		if (dataVencimento.compareTo(dataPagamento) < 0) {

			FormaCobranca formaCobrancaPrincipal = 
					this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
			
			Cota cota = boleto.getChamadaEncalheCota().getCota();
			
			valorJurosCalculado =
					this.calcularJuros(boleto.getBanco(), cota.getId(),
							           valor, boleto.getDataVencimento(),
							           dataPagamento, formaCobrancaPrincipal);
				valorMultaCalculado =
					this.calcularMulta(boleto.getBanco(), cota,
									   valor, formaCobrancaPrincipal);
		}
		
		CobrancaVO cobranca = this.parseBoletoAntecipadoToCobrancaVO(boleto);

		cobranca.setDataPagamento( DateUtil.formatarDataPTBR(dataPagamento) );

		BigDecimal desconto = boleto.getValor().subtract(valor);
		
		cobranca.setDesconto( CurrencyUtil.formatarValor(desconto) );
		
		cobranca.setJuros( CurrencyUtil.formatarValor(valorJurosCalculado) );
        cobranca.setMulta( CurrencyUtil.formatarValor(valorMultaCalculado) );
        
        BigDecimal valorTotal = valor.add(valorJurosCalculado.setScale(2, RoundingMode.HALF_EVEN)).add(valorMultaCalculado.setScale(2, RoundingMode.HALF_EVEN));
		cobranca.setValorTotal( CurrencyUtil.formatarValor(MathUtil.round(valorTotal,2)) );
		
		return cobranca;
	}
	
	private CobrancaVO parseBoletoAntecipadoToCobrancaVO(BoletoAntecipado boleto) {
		
		CobrancaVO cobranca = new CobrancaVO();
		
		cobranca.setBanco(boleto.getBanco().getNome());
		cobranca.setCodigo(boleto.getId().toString());
		cobranca.setCota(boleto.getChamadaEncalheCota().getCota().getPessoa().getNome());
		cobranca.setDataEmissao(DateUtil.formatarDataPTBR(boleto.getData()));
		cobranca.setDataPagamento(DateUtil.formatarDataPTBR(boleto.getDataPagamento()));
		cobranca.setDataVencimento(DateUtil.formatarDataPTBR(boleto.getDataVencimento()));
		cobranca.setDividaTotal(CurrencyUtil.formatarValor(boleto.getValor()));
		cobranca.setNossoNumero(boleto.getNossoNumero());
		cobranca.setValor(CurrencyUtil.formatarValor(boleto.getValor()));
		cobranca.setValorTotal(CurrencyUtil.formatarValor(boleto.getValor()));
		cobranca.setBoletoAntecipado(true);
		
		return cobranca;
	}
	
	/**
	 * Método responsável por obter dados somados de cobranças por códigos
	 * @param List<Long> idCobrancas
	 * @return Data Transfer object com dados somados das cobrancas encontradas e calculadas.
	 */
	@Override
	@Transactional(readOnly=true)
	public CobrancaDividaVO obterDadosCobrancas(List<Long> idCobrancas, Date dataPagamento) {
		
		CobrancaDividaVO pagamento = new CobrancaDividaVO();
	
		BigDecimal totalJuros = BigDecimal.ZERO;
		BigDecimal totalMulta = BigDecimal.ZERO;
		BigDecimal totalDividas = BigDecimal.ZERO;
		BigDecimal totalSaldoDividas = BigDecimal.ZERO;
		
		dataPagamento = DateUtil.parseDataPTBR((DateUtil.formatarDataPTBR(dataPagamento)));
		
		FormaCobranca formaCobrancaPrincipal = 
			this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
		
		for (Long item : idCobrancas){
        
			Cobranca cobranca = cobrancaRepository.buscarPorId(item);
			
			if (cobranca!= null &&  StatusCobranca.NAO_PAGO.equals(cobranca.getStatusCobranca())){
				
				BigDecimal valorJurosCalculado = BigDecimal.ZERO;
				BigDecimal valorMultaCalculado = BigDecimal.ZERO; 
				
				Date vencimentoDiaUtil = calendarioService.adicionarDiasUteis(cobranca.getDataVencimento(), 0);

			    Date dataVencimento = DateUtil.parseDataPTBR((DateUtil.formatarDataPTBR(vencimentoDiaUtil)));
				
				//CALCULA VALOR DO SALDO DA DIVIDA(MOVIMENTOS DE PAGAMENTO PARCIAL)
				BigDecimal saldoDivida = this.obterSaldoDivida(cobranca.getId());
				
				BigDecimal valorCobranca = cobranca.getValor();
				valorCobranca = valorCobranca.setScale(2,RoundingMode.HALF_EVEN);
				
				BigDecimal valorCobrancaJuros = valorCobranca.subtract(saldoDivida);
				valorCobrancaJuros = valorCobrancaJuros.setScale(2,RoundingMode.HALF_EVEN);
				
				
				if (dataVencimento.compareTo(dataPagamento) < 0) {
					
					valorJurosCalculado =
						this.calcularJuros(cobranca.getBanco(), cobranca.getCota().getId(),
											valorCobrancaJuros, 
										    cobranca.getDataVencimento(),
										    dataPagamento, formaCobrancaPrincipal);
					valorMultaCalculado =
						this.calcularMulta(cobranca.getBanco(), cobranca.getCota(),
											valorCobranca.subtract(saldoDivida), formaCobrancaPrincipal);
				}
				
			    BigDecimal valor  = cobranca.getValor();
			    valor = valor.setScale(2, RoundingMode.HALF_EVEN);
			    
			    valorJurosCalculado = valorJurosCalculado.setScale(4, RoundingMode.HALF_EVEN);
			    valorMultaCalculado = valorMultaCalculado.setScale(4, RoundingMode.HALF_EVEN);
	            
				totalJuros = totalJuros.add(valorJurosCalculado);
		        totalMulta = totalMulta.add(valorMultaCalculado);
		        totalDividas = totalDividas.add(valor);
		        totalSaldoDividas = totalSaldoDividas.add(saldoDivida); 
				
			} 
		}
		
		totalJuros = totalJuros.setScale(4,RoundingMode.HALF_EVEN);
		totalMulta = totalMulta.setScale(4, RoundingMode.HALF_EVEN);
		totalSaldoDividas = totalSaldoDividas.setScale(4, RoundingMode.HALF_EVEN);
		
		
		BigDecimal valorPagamento = totalDividas.add(totalJuros).add(totalMulta).subtract(totalSaldoDividas);
		valorPagamento=  valorPagamento.setScale(2,RoundingMode.HALF_EVEN);
		
		pagamento.setValorJuros(CurrencyUtil.formatarValorQuatroCasas(totalJuros));
		pagamento.setValorMulta(CurrencyUtil.formatarValorQuatroCasas(totalMulta));
		pagamento.setValorDividas(CurrencyUtil.formatarValor(totalDividas.subtract(totalSaldoDividas)));
		pagamento.setValorPagamento(CurrencyUtil.formatarValor(valorPagamento));
		pagamento.setValorDesconto(CurrencyUtil.formatarValorQuatroCasas(BigDecimal.ZERO));
		pagamento.setValorSaldo(CurrencyUtil.formatarValorQuatroCasas(BigDecimal.ZERO));

		return pagamento;
	}
	
	
	/**
	 * Obtém saldo da Cota
	 * 
	 * @param idCobranca
	 * @return
	 */
	@Override
	@Transactional
	public BigDecimal obterSaldoCota(Integer numeroCota) {
		
		BigDecimal debito = this.movimentoFinanceiroCotaRepository.obterSaldoCotaPorOperacao(numeroCota, OperacaoFinaceira.DEBITO);
		BigDecimal credito = this.movimentoFinanceiroCotaRepository.obterSaldoCotaPorOperacao(numeroCota, OperacaoFinaceira.CREDITO);
        BigDecimal saldo = credito.subtract(debito);

		return saldo;
	}
	
	
	/**
	 * Obtém detalhes da Cobranca(Dívida)
	 * 
	 * @param idCobranca
	 * @return
	 */
	@Override
	@Transactional
	public List<DetalhesDividaVO> obterDetalhesDivida(Long idCobranca){

		DetalhesDividaVO detalhe;
		List<DetalhesDividaVO> detalhes = new ArrayList<DetalhesDividaVO>();
       
		Cobranca cobranca = this.cobrancaRepository.buscarPorId(idCobranca);
		detalhe = new DetalhesDividaVO();
		detalhe.setData(cobranca.getDataEmissao()!=null?DateUtil.formatarData(cobranca.getDataEmissao(),"dd/MM/yyyy"):"");
		detalhe.setValor(cobranca.getValor()!=null?CurrencyUtil.formatarValor(cobranca.getValor()):"");
		detalhe.setTipo("Dívida");
		
		String descBaixa = this.baixaCobrancaRepository.obterDescricaoBaixaPorCobranca(idCobranca);
		
		detalhe.setObservacao(descBaixa == null ? "" : descBaixa);
		detalhes.add(detalhe);
		
		List<MovimentoFinanceiroCota> movimentos = 
		        this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceirosPorCobranca(idCobranca);
        
		for(MovimentoFinanceiroCota item:movimentos){
			
			
			detalhe = new DetalhesDividaVO();
			detalhe.setData(item.getData()!=null?DateUtil.formatarData(item.getData(),"dd/MM/yyyy"):"");
			detalhe.setObservacao(item.getObservacao()!=null?item.getObservacao():"");
			detalhe.setValor(item.getValor()!=null?CurrencyUtil.formatarValor(item.getValor()):"");
			detalhe.setTipo("Pagamento");
			detalhes.add(detalhe);
			
			
			BaixaManual baixaManual = (BaixaManual) item.getBaixaCobranca();
			if (baixaManual!=null){
				
				if (baixaManual.getValorJuros().compareTo(BigDecimal.ZERO) > 0){
					detalhe = new DetalhesDividaVO();
					detalhe.setData(item.getData()!=null?DateUtil.formatarData(item.getData(),"dd/MM/yyyy"):"");
					detalhe.setObservacao(item.getObservacao()!=null?item.getObservacao():"");
					detalhe.setValor(baixaManual.getValorJuros()!=null?CurrencyUtil.formatarValor(baixaManual.getValorJuros()):"");
					detalhe.setTipo("Juros");
					detalhes.add(detalhe);
				}
				
				if (baixaManual.getValorMulta().compareTo(BigDecimal.ZERO) > 0){
					detalhe = new DetalhesDividaVO();
					detalhe.setData(item.getData()!=null?DateUtil.formatarData(item.getData(),"dd/MM/yyyy"):"");
					detalhe.setObservacao(item.getObservacao()!=null?item.getObservacao():"");
					detalhe.setValor(baixaManual.getValorMulta()!=null?CurrencyUtil.formatarValor(baixaManual.getValorMulta()):"");
					detalhe.setTipo("Multa");
					detalhes.add(detalhe);
			    }
				
			    if (baixaManual.getValorDesconto().compareTo(BigDecimal.ZERO) > 0){
					detalhe = new DetalhesDividaVO();
					detalhe.setData(item.getData()!=null?DateUtil.formatarData(item.getData(),"dd/MM/yyyy"):"");
					detalhe.setObservacao(item.getObservacao()!=null?item.getObservacao():"");
					detalhe.setValor(baixaManual.getValorDesconto()!=null?CurrencyUtil.formatarValor(baixaManual.getValorDesconto()):"");
					detalhe.setTipo("Desconto");
					detalhes.add(detalhe);
				}
			}
			
		}

	    return detalhes;
	}
	
	
	/**
	 * Obtém saldo da Cobranca(Dívida)
	 * 
	 * @param idCobranca
	 * @return
	 */
	@Override
	@Transactional
	public BigDecimal obterSaldoDivida(Long idCobranca){

		BigDecimal saldo = BigDecimal.ZERO;
		
		BigDecimal debito = this.movimentoFinanceiroCotaRepository.obterSaldoCobrancaPorOperacao(idCobranca, OperacaoFinaceira.DEBITO);
		if (debito==null){
			debito = BigDecimal.ZERO;
		}
		BigDecimal credito = this.movimentoFinanceiroCotaRepository.obterSaldoCobrancaPorOperacao(idCobranca, OperacaoFinaceira.CREDITO);
		if (credito==null){
			credito = BigDecimal.ZERO;
		}
        saldo = credito.subtract(debito);
        
		return saldo;
	}
	
	
	/**
	 *Método responsável por baixar dividas manualmente 
	 * @param pagamento
	 * @param idCobrancas
	 * @param manterPendente
	 */
	@Override
	@Transactional
	public void baixaManualDividas(PagamentoDividasDTO pagamento,List<Long> idCobrancas,Boolean manterPendente) {
		
		StatusAprovacao statusAprovacao = StatusAprovacao.APROVADO;
		if (manterPendente){
			statusAprovacao = StatusAprovacao.PENDENTE;
			
			pagamento.setObservacoes(
			        (pagamento.getObservacoes() == null || pagamento.getObservacoes().isEmpty() ? "" : pagamento.getObservacoes() + " - ") + 
                    "Divida baixada com pendência. Aguardando Confirmação.");
		}
		
		BigDecimal valorPagamentoCobranca = pagamento.getValorPagamento();
		valorPagamentoCobranca = valorPagamentoCobranca.setScale(2, RoundingMode.HALF_EVEN);
		
		List<Cobranca> cobrancasOrdenadas = this.cobrancaRepository.obterCobrancasOrdenadasPorVencimento(idCobrancas);
		
		Cobranca cobrancaParcial = null;
		Cobranca cobrancaTotal = null;
		BaixaManual baixaManualTotal = null; 
		
		validarBaixaCobranca(cobrancasOrdenadas, pagamento);
		
		Map<Long, ComposicaoBaixaFinanceira> mapComposicaoBaixaFinanceira = obterMapaComposicaoBaixasFinanceira(pagamento, cobrancasOrdenadas);
		
		//armazena valor arredondado para duas casas para evitar créditos/débitos indevidos oriundos de décimos de centavos
		BigDecimal valorRestanteAuxiliar = valorPagamentoCobranca.setScale(2, RoundingMode.HALF_EVEN);
		
		for (Cobranca itemCobranca:cobrancasOrdenadas) {
			
			BigDecimal valorJuros = BigDecimalUtil.obterValorNaoNulo(mapComposicaoBaixaFinanceira.get(itemCobranca.getId()).getJuros());
			BigDecimal valorMulta = BigDecimalUtil.obterValorNaoNulo(mapComposicaoBaixaFinanceira.get(itemCobranca.getId()).getMulta());
			BigDecimal valorDesconto = BigDecimalUtil.obterValorNaoNulo(mapComposicaoBaixaFinanceira.get(itemCobranca.getId()).getDesconto());
			
			BigDecimal valorCobranca = itemCobranca.getValor().subtract(this.obterSaldoDivida(itemCobranca.getId()));
			valorCobranca = valorCobranca.setScale(2, RoundingMode.HALF_EVEN);
			BigDecimal valorCobrancaCorrigida = valorCobranca.add(valorJuros).add(valorMulta).subtract(valorDesconto);
			valorCobrancaCorrigida = valorCobrancaCorrigida.setScale(2, RoundingMode.HALF_EVEN);
			
			if ( valorRestanteAuxiliar.compareTo(valorCobrancaCorrigida) >= 0 ) {
				
		    	itemCobranca.setDataPagamento(pagamento.getDataPagamento());
		    	itemCobranca.setTipoBaixa(TipoBaixaCobranca.MANUAL);
		    	itemCobranca.setStatusCobranca(StatusCobranca.PAGO);
		    	itemCobranca.getDivida().setStatus(StatusDivida.QUITADA);
		    	itemCobranca.setBanco( (pagamento.getBanco()==null)?itemCobranca.getBanco() :  pagamento.getBanco() );
		    	
		    	this.dividaRepository.merge(itemCobranca.getDivida());
		    	
		    	cobrancaTotal = this.cobrancaRepository.merge(itemCobranca);
		    	
		    	baixaManualTotal = this.criarRegistroBaixaManual(
		    			cobrancaTotal, 
		    			pagamento, 
		    			valorCobrancaCorrigida, 
		    			StatusBaixa.PAGO, 
		    			statusAprovacao, 
		    			mapComposicaoBaixaFinanceira.get(cobrancaTotal.getId()));
		    	
				valorPagamentoCobranca = valorPagamentoCobranca.subtract(valorCobrancaCorrigida);
				
				valorRestanteAuxiliar = valorPagamentoCobranca.setScale(2, RoundingMode.HALF_EVEN);
				
				this.acumuloDividasService.quitarDividasAcumuladas(itemCobranca.getDataPagamento(), itemCobranca.getDivida(),TipoBaixaCobranca.MANUAL);
		    
			} else {
		    	cobrancaParcial = itemCobranca;
		    	break;
		    	
		    }
			
			//ativar cota caso cobrança seja de uma parcela de divida negociada e a mesma ativar a cota ao paga-la
			this.negociacaoDividaService.verificarAtivacaoCotaAposPgtoParcela(itemCobranca, pagamento.getUsuario());
		}
		
		
		if(BigDecimalUtil.isMaiorQueZero(valorRestanteAuxiliar)) {
			
			if(cobrancaParcial != null) {
				
				this.lancamentoBaixaParcialAMenor(cobrancaParcial, 
						pagamento, 
						valorRestanteAuxiliar, 
						StatusBaixa.PAGAMENTO_PARCIAL, 
						statusAprovacao, 
						mapComposicaoBaixaFinanceira.get(cobrancaParcial.getId()));
				
			} else {
				
				lancamentoBaixaParcialAMaior(cobrancaTotal, baixaManualTotal, pagamento, valorRestanteAuxiliar);
				
			}
			
			
		}
		
	}
	
	private void validarBaixaCobranca(List<Cobranca> cobrancas, PagamentoDividasDTO pagamento) {
		
		BigDecimal valorJuros = pagamento.getValorJuros();
		BigDecimal valorMulta = pagamento.getValorMulta();
		BigDecimal valorDesconto = pagamento.getValorDesconto();
		
		BigDecimal valorPagamentoCobranca = pagamento.getValorPagamento().subtract(valorJuros).subtract(valorMulta).add(valorDesconto);
		valorPagamentoCobranca = valorPagamentoCobranca.setScale(2, RoundingMode.HALF_EVEN);
		
		Distribuidor distrib = distribuidorService.obter();
		
		boolean aceitaPagamentoMaior 	= distrib.getAceitaBaixaPagamentoMaior();
		boolean aceitaPagamentoMenor 	= distrib.getAceitaBaixaPagamentoMenor();
		boolean aceitaPagamentoVencido 	= distrib.getAceitaBaixaPagamentoVencido();	
		
		BigDecimal valorTotalAPagar = BigDecimal.ZERO;
		
		Date dataVencimento = null;
		
		for (Cobranca itemCobranca: cobrancas) {
			
			if(dataVencimento == null || dataVencimento.compareTo(itemCobranca.getDataVencimento()) > 0) {
				dataVencimento = itemCobranca.getDataVencimento();
			}
			
			BigDecimal saldoDivida = this.obterSaldoDivida(itemCobranca.getId());
			BigDecimal valorPagar = itemCobranca.getValor().subtract(saldoDivida);
			
			valorPagar = valorPagar.setScale(2, RoundingMode.HALF_EVEN);
			
			valorTotalAPagar = valorTotalAPagar.add(valorPagar);
			
		}
		
		if(!aceitaPagamentoMaior && valorPagamentoCobranca.compareTo(valorTotalAPagar) > 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Distribuidor não aceita pagamento excedendo o valor da cobrança.");
		}
		
		if(!aceitaPagamentoMenor && valorPagamentoCobranca.compareTo(valorTotalAPagar) < 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Distribuidor não aceita pagamento menor que o valor da cobrança.");
		}
		
		boolean dataDePagamentoMaiorQueDataVencimento = (pagamento.getDataPagamento().compareTo(dataVencimento) > 0);
		
		if(!aceitaPagamentoVencido && dataDePagamentoMaiorQueDataVencimento) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Distribuidor não aceita pagamento de cobrança vencida.");
		}
		
	}
	
	private BaixaManual criarRegistroBaixaManual(
			Cobranca cobranca,
			PagamentoDividasDTO pagamento,
			BigDecimal valorCobrancaCorrigida,
			StatusBaixa status, 
			StatusAprovacao statusAprovacao,
			ComposicaoBaixaFinanceira composicaoBaixaFinanceira){
		
		
		BigDecimal valorJuros = BigDecimalUtil.obterValorNaoNulo(composicaoBaixaFinanceira.getJuros());
		BigDecimal valorMulta = BigDecimalUtil.obterValorNaoNulo(composicaoBaixaFinanceira.getMulta());
		BigDecimal valorDesconto = BigDecimalUtil.obterValorNaoNulo(composicaoBaixaFinanceira.getDesconto());

		
		BaixaManual baixaManual = new BaixaManual();
		
		
		
		baixaManual.setDataBaixa(pagamento.getDataOperacao());
		baixaManual.setDataPagamento(pagamento.getDataPagamento());
		
		baixaManual.setValorPago(valorCobrancaCorrigida);
		
		baixaManual.setCobranca(cobranca);
		baixaManual.setResponsavel(pagamento.getUsuario());
		
		baixaManual.setValorJuros(valorJuros);
		baixaManual.setValorMulta(valorMulta);
		baixaManual.setValorDesconto(valorDesconto);
		
		baixaManual.setStatus(status);
		baixaManual.setStatusAprovacao(statusAprovacao);
		baixaManual.setObservacao(pagamento.getObservacoes());
		baixaManual.setBanco(pagamento.getBanco());
		
		baixaCobrancaRepository.adicionar(baixaManual);

		return baixaManual;
		
	}
	
	class ComposicaoBaixaFinanceira {
		
		private BigDecimal multa;
		private BigDecimal juros;
		private BigDecimal desconto;
		
		public BigDecimal getMulta() {
			return multa;
		}
		public void setMulta(BigDecimal multa) {
			this.multa = multa;
		}
		public BigDecimal getJuros() {
			return juros;
		}
		public void setJuros(BigDecimal juros) {
			this.juros = juros;
		}
		public BigDecimal getDesconto() {
			return desconto;
		}
		public void setDesconto(BigDecimal desconto) {
			this.desconto = desconto;
		}
		
		
	}
	
	protected ComposicaoBaixaFinanceira calcularMultaJurosDesconto(
			Map<Long, ComposicaoBaixaFinanceira> map, 
			List<Cobranca> cobrancas, 
			Date dataPagamento) {
		
		BigDecimal totalJuros = BigDecimal.ZERO;
		
		BigDecimal totalMulta = BigDecimal.ZERO;
		
		FormaCobranca formaCobrancaPrincipal = 
			this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
		
		for(Cobranca cobranca : cobrancas) {
			
			BigDecimal valorJurosCalculado = BigDecimal.ZERO;
			BigDecimal valorMultaCalculado = BigDecimal.ZERO; 
			
			Date vencimentoDiaUtil = calendarioService.adicionarDiasUteis(cobranca.getDataVencimento(), 0);

		    Date dataVencimento = DateUtil.parseDataPTBR((DateUtil.formatarDataPTBR(vencimentoDiaUtil)));
			
			BigDecimal saldoDivida = this.obterSaldoDivida(cobranca.getId());
			
			BigDecimal valorCobranca = cobranca.getValor();
			valorCobranca = valorCobranca.setScale(2, RoundingMode.HALF_EVEN);
			
			BigDecimal valorCobrancaJuros = valorCobranca.subtract(saldoDivida);
			valorCobrancaJuros = valorCobrancaJuros.setScale(2, RoundingMode.HALF_EVEN);
			
			if (dataVencimento.compareTo(dataPagamento) < 0) {
				valorJurosCalculado =
					this.calcularJuros(cobranca.getBanco(), cobranca.getCota().getId(),
										valorCobrancaJuros, 
									    cobranca.getDataVencimento(),
									    dataPagamento, formaCobrancaPrincipal);
				valorMultaCalculado =
					this.calcularMulta(cobranca.getBanco(), cobranca.getCota(),
										valorCobranca.subtract(saldoDivida), formaCobrancaPrincipal);
			}
			
		    BigDecimal valor  = cobranca.getValor();
		    valor = valor.setScale(2, RoundingMode.HALF_EVEN);
		    
		    valorJurosCalculado = valorJurosCalculado.setScale(4, RoundingMode.HALF_EVEN);
		    valorMultaCalculado = valorMultaCalculado.setScale(4, RoundingMode.HALF_EVEN);
            
		    ComposicaoBaixaFinanceira composicao = new ComposicaoBaixaFinanceira();
		    composicao.setJuros(valorJurosCalculado);
		    composicao.setMulta(valorMultaCalculado);
		    
		    map.put(cobranca.getId(), composicao);
		    
			totalJuros = totalJuros.add(valorJurosCalculado);
	        totalMulta = totalMulta.add(valorMultaCalculado);
			
		}
		
		ComposicaoBaixaFinanceira composicaoTotalBaixaFinanceira = new ComposicaoBaixaFinanceira();
		
		composicaoTotalBaixaFinanceira.setJuros(totalJuros);
		composicaoTotalBaixaFinanceira.setMulta(totalMulta);
		
		return composicaoTotalBaixaFinanceira;
		
	}
	
	
	
	protected Map<Long, ComposicaoBaixaFinanceira> obterMapaComposicaoBaixasFinanceira(
			PagamentoDividasDTO pagamento, 
			List<Cobranca> cobrancas) {
		
		Map<Long, ComposicaoBaixaFinanceira> map = new HashMap<Long, ComposicaoBaixaFinanceira>();
		
		Date dataPagamento = pagamento.getDataPagamento();
		
		ComposicaoBaixaFinanceira totaisBaixaFinanceira = calcularMultaJurosDesconto(map, cobrancas, dataPagamento);

		boolean jurosEncontrado = BigDecimalUtil.isMaiorQueZero(totaisBaixaFinanceira.getJuros());
		boolean multaEncontrada = BigDecimalUtil.isMaiorQueZero(totaisBaixaFinanceira.getMulta());

		BigDecimal totalDesconto = pagamento.getValorDesconto();
		
		BigDecimal qtdCobrancas = new BigDecimal(cobrancas.size());
		
		for(Entry<Long, ComposicaoBaixaFinanceira> entry : map.entrySet()) {
			
			ComposicaoBaixaFinanceira composicaoBaixaFinanceira = entry.getValue();
			
			if(jurosEncontrado) {
				
				if(!BigDecimalUtil.isMaiorQueZero(composicaoBaixaFinanceira.getJuros())) {
					continue;
				}
				
				BigDecimal valorJurosProporcional = composicaoBaixaFinanceira.getJuros().multiply(pagamento.getValorJuros()).divide(totaisBaixaFinanceira.getJuros(), 4, RoundingMode.HALF_EVEN);
				composicaoBaixaFinanceira.setJuros(valorJurosProporcional);
				
			} else {
				composicaoBaixaFinanceira.setJuros(totaisBaixaFinanceira.getJuros().divide(qtdCobrancas, 4, RoundingMode.HALF_EVEN));
			}
			
			if(multaEncontrada) {
				
				if(!BigDecimalUtil.isMaiorQueZero(composicaoBaixaFinanceira.getMulta())) {
					continue;
				}
				
				BigDecimal valorMultaProporcional = composicaoBaixaFinanceira.getMulta().multiply(pagamento.getValorMulta()).divide(totaisBaixaFinanceira.getMulta(), 4, RoundingMode.HALF_EVEN);
				composicaoBaixaFinanceira.setMulta(valorMultaProporcional);
				
			} else {
				composicaoBaixaFinanceira.setMulta(totaisBaixaFinanceira.getMulta().divide(qtdCobrancas, 4, RoundingMode.HALF_EVEN));
			}
			
			if(BigDecimalUtil.isMaiorQueZero(totalDesconto)) {
				
				composicaoBaixaFinanceira.setDesconto(totalDesconto.divide(qtdCobrancas, 4, RoundingMode.HALF_EVEN));
				
			}
			
		}
		
		
		return map;
		
	}
	
	private void lancamentoBaixaParcialAMaior(
			Cobranca cobrancaTotal, 
			BaixaManual baixaManualTotal, 
			PagamentoDividasDTO pagamento, 
			BigDecimal valorExcedentePagamentoCobranca) {
	
		Date dataVencimento = obterProximaDataVencimentoParaCota(cobrancaTotal.getCota().getId());
		
		String dataPagamentoFormatada = DateUtil.formatarDataPTBR(pagamento.getDataPagamento());
		
		String observacao = "Diferença de Pagamento a Maior (" + dataPagamentoFormatada + ")";
		
		if (!StringUtil.isEmpty(pagamento.getObservacoes())) {
			
			observacao += " - " + pagamento.getObservacoes();
		}
		
		gerarMovimentoFinanceiroCota(
				baixaManualTotal, cobrancaTotal.getCota(), pagamento.getUsuario(), valorExcedentePagamentoCobranca.setScale(2, RoundingMode.HALF_EVEN), 
				pagamento.getDataPagamento(), dataVencimento,
				observacao, GrupoMovimentoFinaceiro.CREDITO,
				cobrancaTotal.getFornecedor()
		);

		BigDecimal valorPago = baixaManualTotal.getValorPago();
		valorPago = valorPago.add(valorExcedentePagamentoCobranca);
		baixaManualTotal.setValorPago(valorPago);
		
		baixaCobrancaRepository.merge(baixaManualTotal);
		
	}
	
	private void lancamentoBaixaParcialAMenor(
			Cobranca cobrancaParcial,
			PagamentoDividasDTO pagamento,
			BigDecimal valorRestante, 
			StatusBaixa status, 
			StatusAprovacao statusAprovacao, 
			ComposicaoBaixaFinanceira composicaoBaixaFinanceira){

		
		
		cobrancaParcial.setDataPagamento(pagamento.getDataPagamento());
		cobrancaParcial.setTipoBaixa(TipoBaixaCobranca.MANUAL);
		cobrancaParcial.setStatusCobranca(StatusCobranca.PAGO);
		cobrancaParcial.getDivida().setStatus(StatusDivida.QUITADA);
		cobrancaParcial.setBanco( (pagamento.getBanco()==null)?cobrancaParcial.getBanco() :  pagamento.getBanco() );
    	this.cobrancaRepository.merge(cobrancaParcial);
    	
    	BaixaManual baixaManual = new BaixaManual();
		
		baixaManual.setDataBaixa(pagamento.getDataOperacao());
		baixaManual.setDataPagamento(pagamento.getDataPagamento());
		baixaManual.setValorPago(valorRestante);
		baixaManual.setCobranca(cobrancaParcial);
		baixaManual.setResponsavel(pagamento.getUsuario());
		
		baixaManual.setValorJuros(BigDecimalUtil.obterValorNaoNulo(composicaoBaixaFinanceira.getJuros()));
		baixaManual.setValorMulta(BigDecimalUtil.obterValorNaoNulo(composicaoBaixaFinanceira.getMulta()));
		baixaManual.setValorDesconto(BigDecimalUtil.obterValorNaoNulo(composicaoBaixaFinanceira.getDesconto()));
		
		baixaManual.setStatus(StatusBaixa.PAGAMENTO_PARCIAL);
		baixaManual.setStatusAprovacao(statusAprovacao);
		baixaManual.setObservacao(pagamento.getObservacoes());
		baixaManual.setBanco(pagamento.getBanco());
		
		baixaCobrancaRepository.adicionar(baixaManual);
		

		BigDecimal valorCobranca = cobrancaParcial.getValor().subtract(this.obterSaldoDivida(cobrancaParcial.getId()));
		BigDecimal valorCobrancaCorrigida = 
				valorCobranca
				.add(BigDecimalUtil.obterValorNaoNulo(composicaoBaixaFinanceira.getJuros()))
				.add(BigDecimalUtil.obterValorNaoNulo(composicaoBaixaFinanceira.getMulta()))
				.subtract(BigDecimalUtil.obterValorNaoNulo(composicaoBaixaFinanceira.getDesconto()));
		
		BigDecimal valorEmDebito = valorCobrancaCorrigida.subtract(valorRestante);
		
		Date dataVencimento = obterProximaDataVencimentoParaCota(cobrancaParcial.getCota().getId());
		
		valorEmDebito = valorEmDebito.setScale(2, RoundingMode.HALF_EVEN);
		
		String dataPagamentoFormatada = DateUtil.formatarDataPTBR(pagamento.getDataPagamento());
		
		String observacao = "Diferença de Pagamento a Menor (" + dataPagamentoFormatada + ")";
		
		if (!StringUtil.isEmpty(pagamento.getObservacoes())) {
			
			observacao += " - " + pagamento.getObservacoes();
		}
		
		gerarMovimentoFinanceiroCota(
			baixaManual, cobrancaParcial.getCota(), pagamento.getUsuario(), valorEmDebito, 
			cobrancaParcial.getDataVencimento(), dataVencimento, 
			observacao, GrupoMovimentoFinaceiro.DEBITO,
			cobrancaParcial.getFornecedor()
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void reverterBaixaManualDividas(List<Long> idCobrancas) {

		List<Cobranca> cobrancasOrdenadas = this.cobrancaRepository.obterCobrancasOrdenadasPorVencimento(idCobrancas);

		for (Cobranca itemCobranca : cobrancasOrdenadas) {

	    	itemCobranca.setStatusCobranca(StatusCobranca.NAO_PAGO);
	    	itemCobranca.getDivida().setStatus(StatusDivida.EM_ABERTO);
	    	itemCobranca.setDataPagamento(null);

	    	this.cobrancaRepository.merge(itemCobranca);

	    	BaixaCobranca baixaCobranca = this.baixaCobrancaRepository.obterUltimaBaixaCobranca(itemCobranca.getId());

	    	this.processarReversaoUltimaBaixaCobranca(baixaCobranca);
		}
	}
	
	private void processarReversaoUltimaBaixaCobranca(BaixaCobranca baixaCobranca) {
		
		if (baixaCobranca.getStatus() == StatusBaixa.PAGAMENTO_PARCIAL) {

			processarReversaoMovimentosFinanceirosBaixaCobranca(baixaCobranca.getMovimentosFinanceiros());
		}

		baixaCobrancaRepository.remover(baixaCobranca);
	}
	
	private void processarReversaoMovimentosFinanceirosBaixaCobranca(List<MovimentoFinanceiroCota> movimentosFinanceiros) {

		for (MovimentoFinanceiroCota movimentoFinanceiroCota : movimentosFinanceiros) {

			gerarMovimentoFinanceiroCota(
				movimentoFinanceiroCota.getBaixaCobranca(), movimentoFinanceiroCota.getCota(), 
				movimentoFinanceiroCota.getUsuario(), movimentoFinanceiroCota.getValor(), 
				movimentoFinanceiroCota.getData(), null, 
				movimentoFinanceiroCota.getObservacao(), GrupoMovimentoFinaceiro.DEBITO,
				movimentoFinanceiroCota.getFornecedor()
			);
		}
	}

	/**
	 * Obtém a próxima data de vencimento utilizada na criação do 
	 * movimento financeiro de débito ou crédito (relativo a pagamento
	 * a menor ou a maior na baixa financeira manual).
	 * @param idCota
	 * @return
	 */
	private Date obterProximaDataVencimentoParaCota(Long idCota) {
		
		Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
		
		Date dataVencimento = null;
		
		FormaCobranca frmCobranca = formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
		
		if(frmCobranca != null && frmCobranca.isVencimentoDiaUtil()) {
			dataVencimento = this.calendarioService.adicionarDiasUteis(dataOperacao, 1);
		} else {
			dataVencimento = DateUtil.adicionarDias(dataOperacao, 1);
		}
		
		return dataVencimento;
		
	}
	
	private void gerarMovimentoFinanceiroCota(BaixaCobranca baixaCobranca, Cota cota, Usuario usuario,											 
											  BigDecimal valor,  Date dataPagamento, Date dataVencimento, String observacoes, 
											  GrupoMovimentoFinaceiro grupoMovimentoFinaceiro,
											  Fornecedor fornecedor) {

		TipoMovimentoFinanceiro tipoMovimento = 
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(grupoMovimentoFinaceiro);

		MovimentoFinanceiroCotaDTO movimento = new MovimentoFinanceiroCotaDTO();
		movimento.setCota(cota);
		movimento.setTipoMovimentoFinanceiro(tipoMovimento);
		movimento.setUsuario(usuario);
		movimento.setDataOperacao(dataPagamento);
		movimento.setBaixaCobranca(baixaCobranca);
        movimento.setValor(valor);
        movimento.setDataCriacao(Calendar.getInstance().getTime());
		movimento.setTipoEdicao(TipoEdicao.INCLUSAO);
		
		movimento.setDataVencimento(dataVencimento);
		
		movimento.setObservacao(observacoes);
		
		fornecedor = fornecedor!=null?fornecedor:cota.getParametroCobranca()!=null?cota.getParametroCobranca().getFornecedorPadrao():null;
	
        if (fornecedor == null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "A [Cota "+cota.getNumeroCota()+"] necessita de um [Fornecedor Padrão] em [Parâmetros] Financeiros !");
		}

		movimento.setFornecedor(fornecedor);
		
		this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimento);
	}
	
	
	/**
	 *Método responsável por validar negociação, verificando se as datas de vencimento das dividas estão de acordo com a configuração do Distribuidor
	 * @param idCobrancas
	 */
	@Override
	@Transactional
	public boolean validaNegociacaoDividas(List<Long> idCobrancas) {
		
		boolean res=true;
		
		Integer diasNegociacao = this.distribuidorService.diasNegociacao();
		
		if (diasNegociacao!=null){
			
			for (Long id:idCobrancas){
				Cobranca cobranca = this.cobrancaRepository.buscarPorId(id);
				
				if (this.distribuidorService.obterDataOperacaoDistribuidor().getTime() > 
						DateUtil.adicionarDias(cobranca.getDataVencimento(), diasNegociacao).getTime()){
					res=false;
					break;
				}

			}
	    }
		return res;
	}
	
	@Override
	@Transactional
	public void confirmarBaixaManualDividas(List<Long> idsCobranca) {

		List<BaixaManual> baixasManual =
			this.baixaCobrancaRepository.obterBaixasManual(idsCobranca);

		for (BaixaManual baixaManual : baixasManual) {

			baixaManual.setStatusAprovacao(StatusAprovacao.APROVADO);

	    	this.baixaCobrancaRepository.merge(baixaManual);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<TipoCobranca> obterTiposCobrancaCadastradas() {
		
		return this.cobrancaRepository.obterTiposCobrancaCadastradas();
	}
	
	@Transactional
    @Override
    public void validarDataPagamentoCobranca(List<Long> idCobrancas, Date dataPagamento) {
        List<Cobranca> cobrancas = this.cobrancaRepository.obterCobrancasDataEmissaoMaiorQue(dataPagamento, idCobrancas);
        
        if(cobrancas != null && !cobrancas.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "A Data de Pagamento não pode ser menor que a Data de Emissão dos itens selecionados");
        }
    }
	
	@Transactional
	@Override
	public List<Cobranca> obterCobrancasEfetuadaNaDataOperacaoDistribuidor(Date dataOperacaoDistribuidor){
		return this.cobrancaRepository.obterCobrancasEfetuadaNaDataOperacaoDistribuidor(dataOperacaoDistribuidor);
	}
	
	@Override
	@Transactional
	public String processarExportacaoCobranca(Date dataOperacaoDistribuidor){

		List<ExportarCobrancaDTO> cobrancas = this.cobrancaRepository.obterCobrancasNaDataDeOperacaoDoDistribuidor(dataOperacaoDistribuidor);
		
		if(cobrancas == null || cobrancas.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há cobranças na data de operação corrente.");
		}
		
		Gson gson = new Gson();
		JsonArray jA = new JsonArray();
		BigDecimal sumValorTotal = BigDecimal.ZERO;
		
		
		for (ExportarCobrancaDTO cobranca : cobrancas) {
			
			sumValorTotal = sumValorTotal.add(cobranca.getVlr_total());
			
			JsonElement jElement = new JsonParser().parse(gson.toJson(cobranca)); 
			jA.add(jElement);
		}
		
		sumValorTotal = sumValorTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		JsonObject json = new JsonObject();
		
		String dataFormatada = DateUtil.formatarData(dataOperacaoDistribuidor, Constantes.DATE_PATTERN_PT_BR_FOR_FILE);

		String docName = "cobrancas_"+dataFormatada;
		
		try {
			
			JsonObject jsonDoc = couchDbClient.find(JsonObject.class, docName);

			this.couchDbClient.remove(jsonDoc);
		} catch (NoDocumentException e) {

		}
		
		json.addProperty("_id", docName);
		json.add(dataFormatada, jA);
		
		this.couchDbClient.save(json); 
		return "Cobranças exportadas: "+cobrancas.size()+", Total financeiro: R$"+sumValorTotal;
		
	}
	
	@Transactional
	@Override
	public String processarCobrancaConsolidada(Date dataOperacaoDistribuidor){
		
		String dataFormatada = DateUtil.formatarData(dataOperacaoDistribuidor, Constantes.DATE_PATTERN_PT_BR_FOR_FILE);
		
		String docName = "cobrancas_"+dataFormatada;
		JsonObject jsonDoc = new JsonObject();
		try {
			jsonDoc = couchDbClient.find(JsonObject.class, docName);
		} catch (NoDocumentException e) {

		}
		
		if(jsonDoc == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há cobranças consolidadas para serem processadas nesta  data: "+dataFormatada);
		}
		
		Gson gson = new Gson();

		JsonArray jaCobrancas = jsonDoc.getAsJsonArray(dataFormatada);
		
		if(jaCobrancas == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há cobranças consolidadas para serem processadas nesta data: "+dataFormatada);
		}
		
		List<ExportarCobrancaDTO> cobrancas = new ArrayList<>();
		List<ExportarCobrancaDTO> cobrancasProcessadas = new ArrayList<>(); 
		
		
		for (JsonElement jsonElement : jaCobrancas) {
			ExportarCobrancaDTO cobranca = gson.fromJson(jsonElement, ExportarCobrancaDTO.class);
			cobrancas.add(cobranca);
		}
		
		int atu=0;
		int proc=0;
		
		for (ExportarCobrancaDTO cobranca : cobrancas) {
			proc++;
			if(cobranca.getCotaProcessada() == true && cobranca.getNossoNumero() != null){
				atu++;
				this.cobrancaRepository.updateNossoNumero(dataOperacaoDistribuidor, cobranca.getCod_jornaleiro(), cobranca.getNossoNumero());
				cobrancasProcessadas.add(cobranca);
			}
		}
		
		if (atu > 0 && atu == proc){ // se foi tudo processado, remover do banco
			couchDbClient.remove(jsonDoc);
		}else{
			
			cobrancas.removeAll(cobrancasProcessadas);
			
			JsonObject jsonDocUpdate = new JsonObject();
			
			JsonArray jA = new JsonArray();
			
			for (ExportarCobrancaDTO cobranca : cobrancas) {
				JsonElement jElement = new JsonParser().parse(gson.toJson(cobranca)); 
				jA.add(jElement);
			}
			
			jsonDocUpdate.addProperty("_id", docName);
			jsonDocUpdate.add(dataFormatada, jA);
			
			this.couchDbClient.remove(jsonDoc);
			this.couchDbClient.save(jsonDocUpdate);
			
			}
		
		return " Processados:"+proc+" Atualizados:"+atu;
		
	}
	
	@Override
	@Transactional
	public Cobranca obterCobrancaPorNossoNumeroConsolidado (String nossoNumeroConsolidado){
		return this.cobrancaRepository.obterCobrancaPorNossoNumeroConsolidado(nossoNumeroConsolidado);
	}
	
	@Transactional
	@Override
	public Long qtdCobrancasConsolidadasBaixadas (Date dataOperacao){
		return this.cobrancaRepository.qtdCobrancasConsolidadasBaixadas(dataOperacao);
	}
}
