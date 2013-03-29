package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.CobrancaBoletoEmBranco;
import br.com.abril.nds.model.financeiro.CobrancaCheque;
import br.com.abril.nds.model.financeiro.CobrancaDeposito;
import br.com.abril.nds.model.financeiro.CobrancaDinheiro;
import br.com.abril.nds.model.financeiro.CobrancaOutros;
import br.com.abril.nds.model.financeiro.CobrancaTransferenciaBancaria;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.HistoricoAcumuloDivida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoletoDistribuidorRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.HistoricoAcumuloDividaRepository;
import br.com.abril.nds.repository.ItemChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.repository.ParcelaNegociacaoRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.GeradorArquivoCobrancaBancoService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class GerarCobrancaServiceImpl implements GerarCobrancaService {

	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Autowired
	private DividaRepository dividaRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private HistoricoAcumuloDividaRepository historicoAcumuloDividaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private DocumentoCobrancaService documentoCobrancaService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private CobrancaService cobrancaService;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private GeradorArquivoCobrancaBancoService geradorArquivoCobrancaBancoService;
	
	@Autowired
	private CobrancaControleConferenciaEncalheCotaRepository cobrancaControleConferenciaEncalheCotaRepository;
	
	@Autowired
	private BoletoDistribuidorRepository boletoDistribuidorRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ItemChamadaEncalheFornecedorRepository itemChamadaEncalheFornecedorRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private FormaCobrancaService formaCobrancaService;
	
	@Autowired
	private NegociacaoDividaRepository negociacaoRepository;
	
	@Autowired
	private ParcelaNegociacaoRepository parcelaNegociacaoRepository;
	
	/**
	 * Obtém a situação da cota
	 * @param idCota
	 * @return SituacaoCadastro
	 */
	private SituacaoCadastro obterSitiacaoCadastroCota(Long idCota){
		Cota cota  = this.cotaRepository.buscarPorId(idCota);
		return cota.getSituacaoCadastro();
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean verificarCobrancasGeradas(List<Long> idsCota){
		
		return this.consolidadoFinanceiroRepository.obterQuantidadeDividasGeradasData(idsCota) > 0;
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean verificarCobrancasGeradasNaDataVencimentoDebito(Date dataVencimentoDebito,Long... idsCota ){
		
		return this.consolidadoFinanceiroRepository.obterQuantidadeDividasGeradasData(dataVencimentoDebito,idsCota) > 0;
	}

	@Override
	@Transactional(noRollbackFor = GerarCobrancaValidacaoException.class)
	public void gerarCobranca(Long idCota, Long idUsuario, Set<String> setNossoNumero)
		throws GerarCobrancaValidacaoException {
		
		this.gerarCobrancaCota(idCota, idUsuario, setNossoNumero);
		
		this.geradorArquivoCobrancaBancoService.prepararGerarArquivoCobrancaCnab();
	}

	private void gerarCobrancaCota(Long idCota, Long idUsuario, Set<String> setNossoNumero) throws GerarCobrancaValidacaoException {

		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		
		Integer numeroDiasNovaCobranca = this.distribuidorRepository.obterNumeroDiasNovaCobranca(); 
		
		//cancela cobrança gerada para essa data de operação para efetuar recalculo
		this.cancelarDividaCobranca(null, idCota);
		
		// buscar movimentos financeiros da cota, se informada, caso contrario de todas as cotas
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCota(idCota);
		
		List<String> msgs = new ArrayList<String>();
		
		if (listaMovimentoFinanceiroCota != null && !listaMovimentoFinanceiroCota.isEmpty()){
			
			//Varre todos os movimentos encontrados, agrupando por cota e por fornecedor
			Cota ultimaCota = listaMovimentoFinanceiroCota.get(0).getCota();
			
			Fornecedor ultimoFornecedor = listaMovimentoFinanceiroCota.get(0).getFornecedor();
			
			if (ultimoFornecedor == null){
		    	
		    	throw new ValidacaoException(
		    			TipoMensagem.WARNING, 
		    			"Fornecedor não encontrado para o [Movimento Financeiro " + 
		    					listaMovimentoFinanceiroCota.get(0).getId() + "] [Cota " + ultimaCota.getNumeroCota() + "].");
		    }
			
			BigDecimal valorMovimentos = BigDecimal.ZERO;
			
			List<MovimentoFinanceiroCota> movimentos = new ArrayList<MovimentoFinanceiroCota>();
			
			String nossoNumero = null;

			Fornecedor fornecedorProdutoMovimento = null;
			
			Cota cotaAtual = null;
			
			FormaCobranca formaCobranca = null;
			
			boolean unificaCobranca = false;

			TipoCobranca tipoCobranca = null;
			
			for (MovimentoFinanceiroCota movimentoFinanceiroCota : listaMovimentoFinanceiroCota){
				
				//verifica se cota esta suspensa, se estiver verifica se existe chamada de encalhe na data de operação
				if (SituacaoCadastro.SUSPENSO.equals(ultimaCota.getSituacaoCadastro())){
					
					if (!movimentoFinanceiroCota.getCota().equals(ultimaCota)){
						
						if (this.chamadaEncalheCotaRepository.obterQtdListaChamaEncalheCota(ultimaCota.getNumeroCota(), 
								dataOperacao, null, false, false, false) <= 0){
							
							continue;
						}
					}
				}
				
				cotaAtual = movimentoFinanceiroCota.getCota();
			    
				fornecedorProdutoMovimento = movimentoFinanceiroCota.getFornecedor();

				if (fornecedorProdutoMovimento == null){
			    	
			    	throw new ValidacaoException(
			    			TipoMensagem.WARNING, 
			    			"Fornecedor não encontrado para o [Movimento Financeiro " + 
			    			movimentoFinanceiroCota.getId() + "] [Cota " + cotaAtual.getNumeroCota() + "].");
			    }
				
				if (unificaCobranca || 
				   (movimentoFinanceiroCota.getCota().equals(ultimaCota) &&
				   (fornecedorProdutoMovimento != null && fornecedorProdutoMovimento.equals(ultimoFornecedor) ||
					fornecedorProdutoMovimento == ultimoFornecedor))){
					
					movimentos.add(movimentoFinanceiroCota);
					
					TipoMovimentoFinanceiro tipo = 
							(TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento();
					
					if (tipo.getOperacaoFinaceira().equals(OperacaoFinaceira.CREDITO)){
						
						valorMovimentos = valorMovimentos.add(movimentoFinanceiroCota.getValor().negate());
					} else {
						
						valorMovimentos = valorMovimentos.add(movimentoFinanceiroCota.getValor());
					}
				} else {
					
					formaCobranca = 
							formaCobrancaService.obterFormaCobranca(
									ultimaCota != null ? ultimaCota.getId() : null, 
									ultimoFornecedor != null ? ultimoFornecedor.getId() : null, 
									dataOperacao, valorMovimentos.compareTo(BigDecimal.ZERO) >= 0?valorMovimentos:valorMovimentos.negate());

					if (formaCobranca.getPoliticaCobranca() != null){
				    	
				    	unificaCobranca = formaCobranca.getPoliticaCobranca().isUnificaCobranca();
				    } else if (formaCobranca.getParametroCobrancaCota() != null){
				    	
				    	unificaCobranca = formaCobranca.getParametroCobrancaCota().isUnificaCobranca();
				    }
				    
				    tipoCobranca = formaCobranca.getTipoCobranca();
					
					//Decide se gera movimento consolidado ou postergado para a cota
					nossoNumero = this.inserirConsolidadoFinanceiro(ultimaCota, 
																	movimentos,
																	formaCobranca.getValorMinimoEmissao(), 
																	idUsuario, 
																	tipoCobranca != null ? tipoCobranca : formaCobranca.getTipoCobranca(),
																	numeroDiasNovaCobranca,
																	dataOperacao, 
																	msgs, 
																	fornecedorProdutoMovimento,
																	formaCobranca);
					
					if (nossoNumero != null){
						
						setNossoNumero.add(nossoNumero);
					}
					
					//Limpa dados para contabilizar próxima cota
					ultimaCota = movimentoFinanceiroCota.getCota();
					
					if (!unificaCobranca){
						
						ultimoFornecedor = movimentoFinanceiroCota.getFornecedor();
					} else {
						
						ultimoFornecedor = null;
					}
					
					movimentos = new ArrayList<MovimentoFinanceiroCota>();
					
					movimentos.add(movimentoFinanceiroCota);
					
					valorMovimentos = movimentoFinanceiroCota.getValor();
				}
			}
			
			if (formaCobranca == null){
				
				formaCobranca = 
						formaCobrancaService.obterFormaCobranca(
								ultimaCota != null ? ultimaCota.getId() : null, 
								ultimoFornecedor != null ? ultimoFornecedor.getId() : null, 
								dataOperacao, valorMovimentos.compareTo(BigDecimal.ZERO) > 0?valorMovimentos:valorMovimentos.negate());
			}
			
			//Decide se gera movimento consolidado ou postergado para a ultima cota
			nossoNumero = this.inserirConsolidadoFinanceiro(ultimaCota, 
															movimentos, 
															formaCobranca.getValorMinimoEmissao(),
															idUsuario, 
															tipoCobranca != null ? tipoCobranca : formaCobranca.getTipoCobranca(),
															numeroDiasNovaCobranca, 
															dataOperacao, 
															msgs, 
															fornecedorProdutoMovimento, 
															formaCobranca);
			
			if (nossoNumero != null){
				
				setNossoNumero.add(nossoNumero);
			}
		}
		
		if (!msgs.isEmpty()){
			
			throw new GerarCobrancaValidacaoException(new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, msgs)));
		}
	}

	/**
	 * Retorna a data de vencimento para o boleto, sendo esta calculada 
	 * da seguinte forma:
	 * 
	 * É recuperada a data da Terça-feira dentro da semana utilizada na pesquisa 
	 * principal do fechamentoCEIntegração. A esta data são adicionados 2 dias
	 * úteis.
	 * 
	 * @param semana
	 * 
	 * @return Date
	 */
	private Date obterDataVencimentoBoletoDistribuidor(int semana) {
		
		Date dataFechamentoSemana = 
				DateUtil.obterDataDaSemanaNoAno(
						semana, DiaSemana.TERCA_FEIRA.getCodigoDiaSemana(), 
						this.distribuidorRepository.obterDataOperacaoDistribuidor());
		
		Date dataVencimento = this.calendarioService.adicionarDiasUteis(dataFechamentoSemana, 2);
				
		return dataVencimento;
	}
	
	/**
	 * Retorna o valor total do boleto com desconto.
	 * 
	 * @param idChamadaEncalheFornecedor
	 * @param valorBrutoBoleto
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal obterValorBoleto(Long idChamadaEncalheFornecedor, BigDecimal valorBrutoBoleto) {
		
		BigDecimal valorTotalDesconto = 
				itemChamadaEncalheFornecedorRepository.obterTotalDoDescontoItensChamadaEncalheFornecedor(idChamadaEncalheFornecedor);
		
		if(valorTotalDesconto == null) {
			valorTotalDesconto = BigDecimal.ZERO;
		}
		
		return valorBrutoBoleto.subtract(valorTotalDesconto);
		
	}
	
	@Transactional
	public List<BoletoDistribuidor> gerarCobrancaBoletoDistribuidor(
			List<ChamadaEncalheFornecedor> listaChamadaEncalheFornecedor, 
			TipoCobranca tipoCobranca, int semana){
		
		List<BoletoDistribuidor> listaBoletoDistribuidor = new ArrayList<BoletoDistribuidor>();
		
		Date dataOperacao = this.distribuidorRepository.obterDataOperacaoDistribuidor();
		
		Date dataAtual = new Date();
		
		Integer codigoDistribuidor = this.distribuidorRepository.codigo();
		
		for(ChamadaEncalheFornecedor chamadaEncalheFornecedor : listaChamadaEncalheFornecedor) {
			
			BoletoDistribuidor boletoDistribuidor = 
					boletoDistribuidorRepository.obterBoletoDistribuidorPorChamadaEncalheFornecedor(chamadaEncalheFornecedor.getId());
			
			if(boletoDistribuidor!=null) {
				
				Integer vias = boletoDistribuidor.getVias();
				
				vias+=1;
				
				boletoDistribuidor.setVias(vias);
				
				if(TipoCobranca.BOLETO.equals(tipoCobranca)) {
					
					BigDecimal valorLiquidoBoleto = obterValorBoleto(chamadaEncalheFornecedor.getId(), chamadaEncalheFornecedor.getTotalVendaApurada());
					
					boletoDistribuidor.setValor(valorLiquidoBoleto);
					
					boletoDistribuidor.setTipoCobranca(tipoCobranca);
					
					boletoDistribuidorRepository.alterar(boletoDistribuidor);
					
				}
				
			} else {
				
				BigDecimal valorLiquidoBoleto = obterValorBoleto(chamadaEncalheFornecedor.getId(), chamadaEncalheFornecedor.getTotalVendaApurada());
				
				Fornecedor fornecedor = chamadaEncalheFornecedor.getFornecedor();
				
				if (chamadaEncalheFornecedor.getFornecedor().getBanco() == null) {
					throw new ValidacaoException(TipoMensagem.ERROR, "Fornecedor selecionado não possui banco vinculado!");
				}
				
				Banco banco = chamadaEncalheFornecedor.getFornecedor().getBanco();
				
				String nossoNumeroDistribuidor = Util.gerarNossoNumeroDistribuidor(
						codigoDistribuidor, 
						dataOperacao, 
						banco.getNumeroBanco(), 
						fornecedor.getId(), 
						chamadaEncalheFornecedor.getId());
				
				boletoDistribuidor = new BoletoDistribuidor();
				
				boletoDistribuidor.setBanco(banco);
				boletoDistribuidor.setChamadaEncalheFornecedor(chamadaEncalheFornecedor);
				
				boletoDistribuidor.setDataEmissao(dataAtual);
				
				boletoDistribuidor.setDataVencimento(obterDataVencimentoBoletoDistribuidor(semana));
				
				boletoDistribuidor.setNossoNumeroDistribuidor(nossoNumeroDistribuidor);
				boletoDistribuidor.setStatus(null);
				
				boletoDistribuidor.setFornecedor(chamadaEncalheFornecedor.getFornecedor());
				
				boletoDistribuidor.setTipoCobranca(tipoCobranca);
				
				if(TipoCobranca.BOLETO.equals(tipoCobranca)) {
					boletoDistribuidor.setValor(valorLiquidoBoleto);
				}
				
				boletoDistribuidor.setVias(1);
				
				boletoDistribuidorRepository.adicionar(boletoDistribuidor);
				
			}
			
			listaBoletoDistribuidor.add(boletoDistribuidor);
			
		}
		
		
		return listaBoletoDistribuidor;
		

	}
	
	private BigDecimal obterValorMinino(Cota cota, BigDecimal valorMininoDistribuidor){
		BigDecimal valorMinimo = 
				(cota.getParametroCobranca() != null && cota.getParametroCobranca().getValorMininoCobranca() != null) ?
						cota.getParametroCobranca().getValorMininoCobranca() :
							valorMininoDistribuidor;
						
		return valorMinimo;
	}
	
	private String inserirConsolidadoFinanceiro(Cota cota, List<MovimentoFinanceiroCota> movimentos, BigDecimal valorMininoDistribuidor,
			Long idUsuario, TipoCobranca tipoCobranca, int qtdDiasNovaCobranca, Date dataOperacao, List<String> msgs,
			Fornecedor fornecedor,FormaCobranca formaCobrancaPrincipal){
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = new ConsolidadoFinanceiroCota();
		consolidadoFinanceiroCota.setCota(cota);
		consolidadoFinanceiroCota.setDataConsolidado(dataOperacao);
		consolidadoFinanceiroCota.setMovimentos(movimentos);
		consolidadoFinanceiroCota.setPendente(
			this.obterValorPendenteCobrancaConsolidado(cota.getNumeroCota())
		);
		
		BigDecimal vlMovFinanDebitoCredito = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncalhe = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncargos = BigDecimal.ZERO;
		BigDecimal vlMovFinanVendaEncalhe = BigDecimal.ZERO;
		BigDecimal vlMovPostergado = BigDecimal.ZERO;
		BigDecimal vlMovConsignado = BigDecimal.ZERO;

		for (MovimentoFinanceiroCota movimentoFinanceiroCota : movimentos){
			
			if (!movimentoFinanceiroCota.getCota().getId().equals(cota.getId())) {
				continue;
			}
			
			GrupoMovimentoFinaceiro tipoMovimentoFinanceiro =
					((TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento()).getGrupoMovimentoFinaceiro();

			switch (tipoMovimentoFinanceiro){
				case CREDITO:
				case COMPRA_NUMEROS_ATRAZADOS:
				case DEBITO:
				case DEBITO_SOBRE_FATURAMENTO:
				case POSTERGADO_NEGOCIACAO:
				case CREDITO_SOBRE_FATURAMENTO:
				case VENDA_TOTAL:
					
					vlMovFinanDebitoCredito = 
						this.adicionarValor(vlMovFinanDebitoCredito, movimentoFinanceiroCota);
				break;
				case COMPRA_ENCALHE_SUPLEMENTAR:
					
					vlMovFinanVendaEncalhe = 
						this.adicionarValor(vlMovFinanVendaEncalhe, movimentoFinanceiroCota);
				break;
				case RECEBIMENTO_REPARTE:
					
					vlMovConsignado = 
						this.adicionarValor(vlMovConsignado, movimentoFinanceiroCota);
				break;
				case JUROS:
				case MULTA:
					
					vlMovFinanEncargos = 
						this.adicionarValor(vlMovFinanEncargos, movimentoFinanceiroCota);
				break;
				case ENVIO_ENCALHE:

					vlMovFinanEncalhe = 
						this.adicionarValor(vlMovFinanEncalhe, movimentoFinanceiroCota);
				break;
				case POSTERGADO_DEBITO:
				case POSTERGADO_CREDITO:
					
					vlMovPostergado = 
						this.adicionarValor(vlMovPostergado, movimentoFinanceiroCota);
				break;
			}
		}
		
		BigDecimal vlMovFinanTotal = BigDecimal.ZERO
				.add(vlMovFinanEncalhe)
				.add(vlMovConsignado)
				.add(vlMovPostergado)
				.add(vlMovFinanVendaEncalhe)
				.add(vlMovFinanDebitoCredito)
				.add(vlMovFinanEncargos)
				.subtract(consolidadoFinanceiroCota.getPendente()!=null?consolidadoFinanceiroCota.getPendente():BigDecimal.ZERO);
		
		consolidadoFinanceiroCota.setTotal(vlMovFinanTotal);
		consolidadoFinanceiroCota.setDebitoCredito(vlMovFinanDebitoCredito);
		consolidadoFinanceiroCota.setEncalhe(vlMovFinanEncalhe);
		consolidadoFinanceiroCota.setEncargos(vlMovFinanEncargos);
		consolidadoFinanceiroCota.setVendaEncalhe(vlMovFinanVendaEncalhe.abs());
		consolidadoFinanceiroCota.setValorPostergado(vlMovPostergado.abs());
		consolidadoFinanceiroCota.setConsignado(vlMovConsignado.abs());
		
		Usuario usuario = this.usuarioRepository.buscarPorId(idUsuario);
		
		Date dataVencimento = null;
		
		List<Integer> diasSemanaConcentracaoPagamento = null;
		
		//obtem a data de vencimento de acordo com o dia em que se concentram os pagamentos da cota
		int fatorVencimento = 0;
		
		ParametroCobrancaCota parametroCobrancaCota = cota.getParametroCobranca();
		
		if(parametroCobrancaCota!=null && parametroCobrancaCota.getFatorVencimento()!=null) {
			fatorVencimento = parametroCobrancaCota.getFatorVencimento();
		}
		
		boolean cobrarHoje = false;
		
		switch(formaCobrancaPrincipal.getTipoFormaCobranca()){

			case DIARIA:
				dataVencimento = 
				this.calendarioService.adicionarDiasUteis(consolidadoFinanceiroCota.getDataConsolidado(), 
														  fatorVencimento, 
														  null, 
														  null);
				cobrarHoje = true;
			break;
			
			case QUINZENAL:
				dataVencimento = 
				this.calendarioService.adicionarDiasUteis(consolidadoFinanceiroCota.getDataConsolidado(), 
														  fatorVencimento,
														  null, 
														  formaCobrancaPrincipal.getDiasDoMes());
				cobrarHoje = 
						formaCobrancaPrincipal.getDiasDoMes().contains(
								Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			break;
			
			case MENSAL:
				dataVencimento = 
				this.calendarioService.adicionarDiasUteis(consolidadoFinanceiroCota.getDataConsolidado(), 
														  fatorVencimento,
														  null, 
														  formaCobrancaPrincipal.getDiasDoMes());
				cobrarHoje =
						formaCobrancaPrincipal.getDiasDoMes().get(0).equals(
								Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			break;
			
			case SEMANAL:
				diasSemanaConcentracaoPagamento = this.cotaRepository.obterDiasConcentracaoPagamentoCota(cota.getId());
				
				dataVencimento = 
				this.calendarioService.adicionarDiasUteis(consolidadoFinanceiroCota.getDataConsolidado(), 
														  fatorVencimento,
														  diasSemanaConcentracaoPagamento, 
														  null);
				
				cobrarHoje = 
						diasSemanaConcentracaoPagamento.contains(
								Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
			break;
		}
		
		if (dataVencimento == null){
			
			msgs.add("Não foi possível calcular data de vencimento da cobrança, verifique os parâmetros de cobrança da cota número: " + cota.getNumeroCota());
			
			return null;
		}
		
		Divida novaDivida = null;
		
		HistoricoAcumuloDivida historicoAcumuloDivida = null;
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = null;
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = null;
			
		boolean cotaSuspensa = SituacaoCadastro.SUSPENSO.equals(this.obterSitiacaoCadastroCota(cota.getId()));

		BigDecimal valorMinino = 
				this.obterValorMinino(cota, valorMininoDistribuidor);
		
		//caso tenha alcançado o valor minino de cobrança e seja um dia de concentração de cobrança, ou a cota esteja suspensa
		if ( (vlMovFinanTotal.compareTo(BigDecimal.ZERO) < 0) &&
				(vlMovFinanTotal.abs().compareTo(valorMinino) > 0 && cobrarHoje) || 
				(vlMovFinanTotal.abs().compareTo(valorMinino) > 0 && cotaSuspensa)){

			if (formaCobrancaPrincipal.getBanco() == null) {
				
				return null;
			}
			
			novaDivida = new Divida();
			novaDivida.setValor(vlMovFinanTotal.abs());
			novaDivida.setData(consolidadoFinanceiroCota.getDataConsolidado());
			novaDivida.setConsolidado(consolidadoFinanceiroCota);
			novaDivida.setCota(cota);
			novaDivida.setStatus(StatusDivida.EM_ABERTO);
			novaDivida.setResponsavel(usuario);
			novaDivida.setOrigemNegociacao(false);
			
			BigDecimal valorCalculadoJuros = BigDecimal.ZERO;
			
			boolean isAcumulaDivida = 
					formaCobrancaPrincipal != null && formaCobrancaPrincipal.getPoliticaCobranca() != null ?
							formaCobrancaPrincipal.getPoliticaCobranca().isAcumulaDivida() : false;
			
			//se o distribuidor acumula divida
			if (isAcumulaDivida) {

				Calendar diaDivida = Calendar.getInstance();
				diaDivida.setTime(new Date());
				diaDivida.add(Calendar.DAY_OF_MONTH, qtdDiasNovaCobranca * -1);
				
				Divida divida = this.dividaRepository.obterDividaParaAcumuloPorCota(cota.getId(), diaDivida.getTime());

				//caso não tenha divida anterior, ou tenha sido quitada
				if (divida == null || StatusDivida.QUITADA.equals(divida.getStatus())){
					divida = novaDivida;
				} else {
					
					ConsolidadoFinanceiroCota consolidadoDivida = divida.getConsolidado();
					
					BigDecimal valorMulta = BigDecimal.ZERO;
					
					if (consolidadoDivida != null){
						List<MovimentoFinanceiroCota> movimentoFinanceiroDivida = consolidadoDivida.getMovimentos();
						for (MovimentoFinanceiroCota m : movimentoFinanceiroDivida){
							if (((TipoMovimentoFinanceiro) m.getTipoMovimento()).getGrupoMovimentoFinaceiro().equals(GrupoMovimentoFinaceiro.MULTA)){
								valorMulta = m.getValor();
								break;
							}
						}
					}

					divida.setAcumulada(true);

					valorCalculadoJuros = 
							this.cobrancaService.calcularJuros(
									null,
									cota.getId(),
									vlMovFinanTotal.add(divida.getValor()).add(valorMulta.abs()),
									divida.getCobranca().getDataVencimento(),
									new Date());

					this.dividaRepository.alterar(divida);

					novaDivida.setDividaRaiz(divida);
					
					historicoAcumuloDivida = new HistoricoAcumuloDivida();
					historicoAcumuloDivida.setDataInclusao(new Date());
					historicoAcumuloDivida.setDivida(divida);
					historicoAcumuloDivida.setResponsavel(usuario);
					historicoAcumuloDivida.setStatus(StatusInadimplencia.ATIVA);

					novaDivida.setValor(vlMovFinanTotal.add(valorCalculadoJuros.abs()));
				}

			} else {
				consolidadoFinanceiroCota.getTotal().subtract(consolidadoFinanceiroCota.getPendente().abs());
				consolidadoFinanceiroCota.setPendente(BigDecimal.ZERO);
			}

		} else if (vlMovFinanTotal.compareTo(valorMinino) != 0) {

			//gerar postergado
			consolidadoFinanceiroCota.setValorPostergado(vlMovFinanTotal);
			
			//gera movimento financeiro cota
			movimentoFinanceiroCota = new MovimentoFinanceiroCota();
			
			Calendar diaPostergado = Calendar.getInstance();
			diaPostergado.setTime(new Date());
			diaPostergado.add(Calendar.DAY_OF_MONTH, qtdDiasNovaCobranca);
			
			movimentoFinanceiroCota.setData(diaPostergado.getTime());
			movimentoFinanceiroCota.setDataCriacao(new Date());
			movimentoFinanceiroCota.setUsuario(usuario);
			movimentoFinanceiroCota.setValor(vlMovFinanTotal);
			movimentoFinanceiroCota.setLancamentoManual(false);
			movimentoFinanceiroCota.setCota(cota);
			
			if (vlMovFinanTotal.compareTo(BigDecimal.ZERO) > 0){
				
				tipoMovimentoFinanceiro = 
						this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
								GrupoMovimentoFinaceiro.POSTERGADO_CREDITO);
			} else {
				
				tipoMovimentoFinanceiro = 
						this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
								GrupoMovimentoFinaceiro.POSTERGADO_DEBITO);
			}
			
			if (tipoMovimentoFinanceiro == null) {
				
				msgs.add("Tipo de movimento para postergação não encontrado!");
				
				return null;
			}
			
			String descPostergado = null;
			
			if (diasSemanaConcentracaoPagamento != null && 
					!diasSemanaConcentracaoPagamento.contains(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))){
				
				descPostergado = "Não existe acúmulo de pagamento para este dia (" + 
						new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()) + ")";
			} else {
				
				descPostergado = "Valor mínimo para dívida não atingido";
			}
			
			movimentoFinanceiroCota.setMotivo(descPostergado);
			
			
			movimentoFinanceiroCota.setTipoMovimento(tipoMovimentoFinanceiro);
			
			movimentoFinanceiroCota.setFornecedor(fornecedor);
		}
		
		this.consolidadoFinanceiroRepository.adicionar(consolidadoFinanceiroCota);
		
		Cobranca cobranca = null;
		
		if (novaDivida != null){
			if (novaDivida.getId() == null){
				this.dividaRepository.adicionar(novaDivida);
			} else {
				this.dividaRepository.alterar(novaDivida);
			}
			
			if (historicoAcumuloDivida != null){
				this.historicoAcumuloDividaRepository.adicionar(historicoAcumuloDivida);
			}
			
			switch (tipoCobranca){
				case BOLETO:
					cobranca = new Boleto();
				break;
				case CHEQUE:
					cobranca = new CobrancaCheque();
				break;
				case DINHEIRO:
					cobranca = new CobrancaDinheiro();
				break;
				case DEPOSITO:
					cobranca = new CobrancaDeposito();
				case TRANSFERENCIA_BANCARIA:
					cobranca = new CobrancaTransferenciaBancaria();
				break;
				case BOLETO_EM_BRANCO:
					cobranca = new CobrancaBoletoEmBranco();
				break;
				case OUTROS:
					cobranca = new CobrancaOutros();
				break;
			}
			
			cobranca.setBanco(formaCobrancaPrincipal.getBanco());
			cobranca.setCota(cota);
			cobranca.setDataEmissao(new Date());
			cobranca.setDivida(novaDivida);
			cobranca.setStatusCobranca(StatusCobranca.NAO_PAGO);
			cobranca.setDataVencimento(dataVencimento);
			cobranca.setVias(0);
			
			Banco banco = formaCobrancaPrincipal.getBanco();
			
			String nossoNumero =
				Util.gerarNossoNumero(
					cota.getNumeroCota(), 
					cobranca.getDataEmissao(), 
					banco.getNumeroBanco(),
					fornecedor != null ? fornecedor.getId() : null,
					movimentos.get(0).getId(),
					banco.getAgencia(),
					banco.getConta(),
					banco.getCarteira());
			
			cobranca.setFornecedor(fornecedor);
			cobranca.setNossoNumero(nossoNumero);
			
			String digitoVerificador =
				Util.calcularDigitoVerificador(
					nossoNumero, banco.getCodigoCedente(), cobranca.getDataVencimento());
			
			cobranca.setDigitoNossoNumero(digitoVerificador);
			
			cobranca.setNossoNumeroCompleto(
				nossoNumero + ((digitoVerificador != null) ? digitoVerificador : ""));
			
			cobranca.setValor(novaDivida.getValor());
			
			this.cobrancaRepository.adicionar(cobranca);
			
			if (formaCobrancaPrincipal.isRecebeCobrancaEmail()){
				
				try {
					byte[]anexo = this.documentoCobrancaService.gerarDocumentoCobranca(cobranca.getNossoNumero());
					
					this.emailService.enviar(
							"Cobrança", 
							"Segue documento de cobrança em anexo.", 
							new String[]{cota.getPessoa().getEmail()}, 
							new AnexoEmail("Cobranca",anexo,TipoAnexo.PDF));
					
					this.cobrancaRepository.incrementarVia(cobranca.getNossoNumero());
				} catch (AutenticacaoEmailException e) {
					
					msgs.add("Erro ao enviar email de cobrança para cota número: " + cota.getNumeroCota());
				} catch (ValidacaoException e) {
					
					msgs.add("Erro ao enviar email de cobrança para cota número: " + cota.getNumeroCota() + " - " + e.getValidacao().getListaMensagens().get(0));
				}
			}
		}
		
		if (movimentoFinanceiroCota != null){
			
			this.movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiroCota);
		}
		
		if (cobranca != null){
			
			return cobranca.getNossoNumero();
		}
		
		return null;
	}
	
	@Transactional(readOnly=true)
	@Override
	public Boolean validarDividaGeradaDataOperacao() {
		
		Long quantidadeRegistro = 
				movimentoFinanceiroCotaRepository.obterQuantidadeMovimentoFinanceiroDataOperacao(
						this.distribuidorRepository.obterDataOperacaoDistribuidor()); 
		
		return (quantidadeRegistro == null || quantidadeRegistro == 0) ? Boolean.FALSE : Boolean.TRUE;
	}

	@Transactional
	@Override
	public void cancelarDividaCobranca(Set<Long> idsMovimentoFinanceiroCota) {
		
		if (idsMovimentoFinanceiroCota != null && !idsMovimentoFinanceiroCota.isEmpty()){
			
			for (Long idMovFinCota : idsMovimentoFinanceiroCota){
				
				this.cancelarDividaCobranca(idMovFinCota, null);
			}
		}
	}
	
	@Transactional
	@Override
	public void cancelarDividaCobranca(Long idMovimentoFinanceiroCota, Long idCota) {
		
		List<ConsolidadoFinanceiroCota> consolidados = null;
		
		if (idMovimentoFinanceiroCota != null){

			consolidados = 
					this.consolidadoFinanceiroRepository.obterConsolidadoPorIdMovimentoFinanceiro(
							idMovimentoFinanceiroCota);
		} else {
			
			consolidados =
					this.consolidadoFinanceiroRepository.obterConsolidadosDataOperacao(idCota);
		}
		
		if (consolidados != null){
			
			for (ConsolidadoFinanceiroCota consolidado : consolidados){
				
				Divida divida = this.dividaRepository.obterDividaPorIdConsolidado(consolidado.getId());
				
				if (divida != null){
				
					this.cobrancaControleConferenciaEncalheCotaRepository.excluirPorCobranca(divida.getCobranca().getId());
					
					Negociacao negociacao = this.negociacaoRepository.obterNegociacaoPorCobranca(divida.getCobranca().getId());
					
					if (negociacao != null){
					    
						if (!negociacao.isNegociacaoAvulsa()){
						
						    this.parcelaNegociacaoRepository.excluirPorNegociacao(negociacao.getId());
						
						    this.negociacaoRepository.remover(negociacao);
						    
						    this.removerDividaCobrancaConsolidado(divida,consolidado);
						}
					
					} else{
					
						this.removerDividaCobrancaConsolidado(divida,consolidado);
					}
				}
				
			    this.consolidadoFinanceiroRepository.remover(consolidado);
			}
		}
	}
	
	private void removerDividaCobrancaConsolidado(Divida divida, ConsolidadoFinanceiroCota consolidado){
		
		this.cobrancaRepository.remover(divida.getCobranca());
		
	    this.dividaRepository.remover(divida);
	    
		List<TipoMovimentoFinanceiro> listaPostergados = Arrays.asList(
			this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
					GrupoMovimentoFinaceiro.POSTERGADO_CREDITO),
					
			this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
					GrupoMovimentoFinaceiro.POSTERGADO_DEBITO)
		);
		
		this.movimentoFinanceiroCotaService.removerPostergadosDia(
				consolidado.getCota().getId(), 
				listaPostergados);
	}
	
	/**
	 * 
	 * Retorna o valor de cobrança em aberto que a cota não pagou até a presente data de geração do novo consolidado em questão.
	 * 
	 * @param numeroCota - número da cota
	 * 
	 * @return BigDecimal - valor pendente de cobrança do sonsolidado
	 */
	private BigDecimal obterValorPendenteCobrancaConsolidado(Integer numeroCota){
		
		return cobrancaRepository.obterValorCobrancaNaoPagoDaCota(numeroCota);
	}
	
	private BigDecimal adicionarValor(BigDecimal valor, MovimentoFinanceiroCota movimentoFinanceiroCota){
		
		if (movimentoFinanceiroCota.getValor() == null){
			
			return BigDecimal.ZERO;
		}
		
		switch(((TipoMovimentoFinanceiro)movimentoFinanceiroCota.getTipoMovimento()).getGrupoMovimentoFinaceiro().getOperacaoFinaceira()){
			case CREDITO:
				return valor.add(movimentoFinanceiroCota.getValor());
			case DEBITO:
				return valor.add(movimentoFinanceiroCota.getValor().negate());
			default:
				return BigDecimal.ZERO;
		}
	}
}
