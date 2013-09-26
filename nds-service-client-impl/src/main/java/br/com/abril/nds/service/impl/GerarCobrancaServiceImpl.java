package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
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
import br.com.abril.nds.util.SemanaUtil;
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
	public void gerarCobranca(Long idCota, Long idUsuario, Map<String, Boolean> setNossoNumero)
		throws GerarCobrancaValidacaoException {
		
		this.gerarCobrancaCota(idCota, idUsuario, setNossoNumero);
		
		this.geradorArquivoCobrancaBancoService.prepararGerarArquivoCobrancaCnab();
	}

	private void gerarCobrancaCota(Long idCota, Long idUsuario, Map<String, Boolean> setNossoNumero) 
			throws GerarCobrancaValidacaoException {

		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		Usuario usuario = this.usuarioRepository.buscarPorId(idUsuario);
		
		Integer numeroDiasNovaCobranca = this.distribuidorRepository.obterNumeroDiasNovaCobranca(); 
		
		//cancela cobrança gerada para essa data de operação para efetuar recalculo
		this.cancelarDividaCobranca(null, idCota, dataOperacao);
		
		// buscar movimentos financeiros da cota, se informada, caso contrario de todas as cotas
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCota(idCota);
		
		List<String> msgs = new ArrayList<String>();
		
		if (listaMovimentoFinanceiroCota != null && !listaMovimentoFinanceiroCota.isEmpty()){
			
			//Varre todos os movimentos encontrados, agrupando por cota e por fornecedor
			Cota ultimaCota = listaMovimentoFinanceiroCota.get(0).getCota();
			
			Fornecedor ultimoFornecedor = listaMovimentoFinanceiroCota.get(0).getFornecedor();
			
			if (ultimoFornecedor == null){
		    	
		    	throw new GerarCobrancaValidacaoException(
		    			new ValidacaoVO(
		    			TipoMensagem.WARNING, 
		    			"Fornecedor não encontrado para o [Movimento Financeiro " + 
		    					listaMovimentoFinanceiroCota.get(0).getId() + "] [Cota " + ultimaCota.getNumeroCota() + "]."));
		    }
			
			BigDecimal valorMovimentos = BigDecimal.ZERO;
			
			List<MovimentoFinanceiroCota> movimentos = new ArrayList<MovimentoFinanceiroCota>();
			
			String nossoNumero = null;

			Fornecedor fornecedorProdutoMovimento = null;
			
			Cota cotaAtual = null;
			
			FormaCobranca formaCobranca = null;
			FormaCobranca formaCobrancaClone = null;

			boolean unificaCobranca = false;
			
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
			    	
			    	throw new GerarCobrancaValidacaoException(
			    			new ValidacaoVO(TipoMensagem.WARNING, 
			    			"Fornecedor não encontrado para o [Movimento Financeiro " + 
			    			movimentoFinanceiroCota.getId() + "] [Cota " + cotaAtual.getNumeroCota() + "]."));
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

					formaCobrancaClone = this.cloneFormaCobranca(formaCobranca);
					
					if (formaCobrancaClone != null){
						
						if (formaCobrancaClone.getPoliticaCobranca() != null){
					    	
					    	unificaCobranca = formaCobrancaClone.getPoliticaCobranca().isUnificaCobranca();
					    } else if (formaCobrancaClone.getParametroCobrancaCota() != null){
					    	
					    	unificaCobranca = formaCobrancaClone.getParametroCobrancaCota().isUnificaCobranca();
					    }
					}
					
					//Decide se gera movimento consolidado ou postergado para a cota
					nossoNumero = this.inserirConsolidadoFinanceiro(ultimaCota, 
																	movimentos,
																	usuario, 
																	numeroDiasNovaCobranca,
																	dataOperacao, 
																	msgs, 
																	ultimoFornecedor,
																	formaCobrancaClone);
					
					if (nossoNumero != null){
						
						setNossoNumero.put(nossoNumero,
								formaCobrancaClone == null ? false :
									formaCobrancaClone.isRecebeCobrancaEmail());
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
					
				formaCobrancaClone = this.cloneFormaCobranca(formaCobranca);  
			}
			
			//Decide se gera movimento consolidado ou postergado para a ultima cota
			nossoNumero = this.inserirConsolidadoFinanceiro(ultimaCota, 
															movimentos, 
															usuario, 
															numeroDiasNovaCobranca, 
															dataOperacao, 
															msgs, 
															fornecedorProdutoMovimento, 
															formaCobrancaClone);
			
			if (nossoNumero != null){
				
				setNossoNumero.put(nossoNumero,
						formaCobrancaClone == null ? false :
							formaCobrancaClone.isRecebeCobrancaEmail());
			}
		}
		
		if (!msgs.isEmpty()){
			
			throw new GerarCobrancaValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, msgs));
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
	 * @param anoSemana
	 * 
	 * @return Date
	 */
	private Date obterDataVencimentoBoletoDistribuidor(int anoSemana) {
		
		int anoBase = SemanaUtil.getAno(anoSemana);
		
		int semana = SemanaUtil.getSemana(anoSemana);
		
		Date dataFechamentoSemana = 
			SemanaUtil.obterDataDaSemanaNoAno(
				semana, DiaSemana.TERCA_FEIRA.getCodigoDiaSemana(), anoBase);
		
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
			
			chamadaEncalheFornecedor.setFornecedor( chamadaEncalheFornecedor.getItens().get(0).getProdutoEdicao().getProduto().getFornecedor() );
			
			if(boletoDistribuidor != null) {
				
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
	
	@SuppressWarnings("null")
	private String inserirConsolidadoFinanceiro(Cota cota, List<MovimentoFinanceiroCota> movimentos,
			Usuario usuario, int qtdDiasNovaCobranca, Date dataOperacao, List<String> msgs,
			Fornecedor fornecedor,FormaCobranca formaCobrancaPrincipal){
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = new ConsolidadoFinanceiroCota();
		consolidadoFinanceiroCota.setCota(cota);
		consolidadoFinanceiroCota.setDataConsolidado(dataOperacao);
		consolidadoFinanceiroCota.setMovimentos(movimentos);
		
		BigDecimal vlMovFinanDebitoCredito = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncalhe = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncargos = BigDecimal.ZERO;
		BigDecimal vlMovFinanVendaEncalhe = BigDecimal.ZERO;
		BigDecimal vlMovPostergado = BigDecimal.ZERO;
		BigDecimal vlMovConsignado = BigDecimal.ZERO;
		BigDecimal vlMovPendente = BigDecimal.ZERO;

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
				case PENDENTE:
					vlMovPendente =
						this.adicionarValor(vlMovPendente, movimentoFinanceiroCota);
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
				.add(vlMovPendente);
		
		consolidadoFinanceiroCota.setTotal(vlMovFinanTotal);
		consolidadoFinanceiroCota.setDebitoCredito(vlMovFinanDebitoCredito);
		consolidadoFinanceiroCota.setEncalhe(vlMovFinanEncalhe);
		consolidadoFinanceiroCota.setEncargos(vlMovFinanEncargos);
		consolidadoFinanceiroCota.setVendaEncalhe(vlMovFinanVendaEncalhe.abs());
		consolidadoFinanceiroCota.setValorPostergado(vlMovPostergado);
		consolidadoFinanceiroCota.setConsignado(vlMovConsignado.abs());
		consolidadoFinanceiroCota.setPendente(vlMovPendente.abs());
		
		//insere postergado pois não encontrou forma de cobrança
		if (formaCobrancaPrincipal == null){
			
			MovimentoFinanceiroCota movPost = 
					this.gerarPostergado(cota, qtdDiasNovaCobranca, msgs, fornecedor, 
							consolidadoFinanceiroCota, vlMovFinanTotal, vlMovPostergado, usuario, null, dataOperacao);
			
			this.consolidadoFinanceiroRepository.adicionar(consolidadoFinanceiroCota);
			
			if (movPost != null){
				
				this.movimentoFinanceiroCotaRepository.adicionar(movPost);
			}
			
			return null;
		}
		
		Date dataVencimento = null;
		
		List<Integer> diasSemanaConcentracaoPagamento = null;
		
		//obtem a data de vencimento de acordo com o dia em que se concentram os pagamentos da cota
		Integer fatorVencimento = 0;
		
		ParametroCobrancaCota parametroCobrancaCota = cota.getParametroCobranca();
		
		TipoFormaCobranca tipoFormaCobrancaAntiga = null;
		
		if(parametroCobrancaCota!=null && parametroCobrancaCota.getFatorVencimento()!=null) {
			fatorVencimento = parametroCobrancaCota.getFatorVencimento();
		}
		else {
			
			if(formaCobrancaPrincipal.getPoliticaCobranca().getFatorVencimento() != null){
				
				fatorVencimento = formaCobrancaPrincipal.getPoliticaCobranca().getFatorVencimento();
			}
			
			tipoFormaCobrancaAntiga = formaCobrancaPrincipal.getTipoFormaCobranca();
			formaCobrancaPrincipal.setTipoFormaCobranca(null);
		}
		
		boolean cobrarHoje = false;
		
		Calendar c = null;
		
		//acertar a data de vencimento de a cota usa parametros de cobranca do distribuidor
		if(formaCobrancaPrincipal.getTipoFormaCobranca() == null) {
			dataVencimento = 
					this.calendarioService.adicionarDiasUteis(consolidadoFinanceiroCota.getDataConsolidado(), 
															  fatorVencimento);
			cobrarHoje = true;
		} else {
			// switch usado para se é usado os parametros de cobranca da propria cota
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
					c = Calendar.getInstance();
					c.setTime(dataOperacao);
					cobrarHoje = 
							formaCobrancaPrincipal.getDiasDoMes().contains(
									c.get(Calendar.DAY_OF_MONTH));
				break;
				
				case MENSAL:
					dataVencimento = 
					this.calendarioService.adicionarDiasUteis(consolidadoFinanceiroCota.getDataConsolidado(), 
															  fatorVencimento,
															  null, 
															  formaCobrancaPrincipal.getDiasDoMes());
					c = Calendar.getInstance();
					c.setTime(dataOperacao);
					cobrarHoje =
							formaCobrancaPrincipal.getDiasDoMes().get(0).equals(
									c.get(Calendar.DAY_OF_MONTH));
				break;
				
				case SEMANAL:
					diasSemanaConcentracaoPagamento = this.cotaRepository.obterDiasConcentracaoPagamentoCota(cota.getId());
					
					dataVencimento = 
							this.calendarioService.adicionarDiasUteis(consolidadoFinanceiroCota.getDataConsolidado(), 
															  fatorVencimento,
															  diasSemanaConcentracaoPagamento, 
															  null);
					c = Calendar.getInstance();
					c.setTime(dataOperacao);
					
					for (ConcentracaoCobrancaCota conc : formaCobrancaPrincipal.getConcentracaoCobrancaCota()){
						
						cobrarHoje = 
								c.get(Calendar.DAY_OF_WEEK) == conc.getDiaSemana().getCodigoDiaSemana();
						
						if (cobrarHoje){
							break;
						}
					}
				break;
			}
			
		}
		
		/*
		 * Recoloca o TipoFormaCobranca no Local Antigo
		 */
		if(parametroCobrancaCota !=null && parametroCobrancaCota.getFatorVencimento() == null) {
			formaCobrancaPrincipal.setTipoFormaCobranca(tipoFormaCobrancaAntiga);
		}
		
		
		
		if (dataVencimento == null){
			
			msgs.add("Não foi possível calcular data de vencimento da cobrança, verifique os parâmetros de cobrança da cota número: " + cota.getNumeroCota());
			
			return null;
		}
		
		Divida novaDivida = null;
		
		HistoricoAcumuloDivida historicoAcumuloDivida = null;
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = null;
		
		boolean cotaSuspensa = SituacaoCadastro.SUSPENSO.equals(this.obterSitiacaoCadastroCota(cota.getId()));

		BigDecimal valorMinino = 
				this.obterValorMinino(cota, formaCobrancaPrincipal.getValorMinimoEmissao());
		
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
			
			/*
			 * 
			 * Código comentado por solicitação do Cesar, funcionalidade feita no momento da Baixa Financeira.
			 * 16/05/2013 - Trac 581
			 * 
			BigDecimal valorCalculadoJuros = BigDecimal.ZERO;
			
			boolean isAcumulaDivida = 
					formaCobrancaPrincipal != null && formaCobrancaPrincipal.getPoliticaCobranca() != null ?
							formaCobrancaPrincipal.getPoliticaCobranca().isAcumulaDivida() : false;
			
			//se o distribuidor acumula divida
			if (isAcumulaDivida) {

				
				Divida divida = this.dividaRepository.obterDividaParaAcumuloPorCota(cota.getId());

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
									divida.getValor().abs(),
									divida.getCobranca().getDataVencimento(),
									dataOperacao);

					this.dividaRepository.alterar(divida);

					novaDivida.setDividaRaiz(divida);
					
					historicoAcumuloDivida = new HistoricoAcumuloDivida();
					historicoAcumuloDivida.setDataInclusao(new Date());
					historicoAcumuloDivida.setDivida(divida);
					historicoAcumuloDivida.setResponsavel(usuario);
					historicoAcumuloDivida.setStatus(StatusInadimplencia.ATIVA);

					novaDivida.setValor(vlMovFinanTotal.abs().add(valorCalculadoJuros.abs()));
				}

			} else {
				consolidadoFinanceiroCota.getTotal().subtract(consolidadoFinanceiroCota.getPendente().abs());
				consolidadoFinanceiroCota.setPendente(BigDecimal.ZERO);
			}*/

		} else if (vlMovFinanTotal.compareTo(valorMinino) != 0) {

			movimentoFinanceiroCota = this.gerarPostergado(cota,
					qtdDiasNovaCobranca, msgs, fornecedor,
					consolidadoFinanceiroCota, vlMovFinanTotal, vlMovFinanTotal, usuario,
					diasSemanaConcentracaoPagamento, dataOperacao);
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
			
			switch (formaCobrancaPrincipal.getTipoCobranca()){
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
			cobranca.setDataEmissao(dataOperacao);
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
		}
		
		if (movimentoFinanceiroCota != null){
			
			this.movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiroCota);
		}
		
		if (cobranca != null){
			
			return cobranca.getNossoNumero();
		}
		
		return null;
	}

	private MovimentoFinanceiroCota gerarPostergado(Cota cota,
			int qtdDiasNovaCobranca, List<String> msgs, Fornecedor fornecedor,
			ConsolidadoFinanceiroCota consolidadoFinanceiroCota,
			BigDecimal vlMovFinanTotal, BigDecimal vlMovFinanPostergado, Usuario usuario,
			List<Integer> diasSemanaConcentracaoPagamento, Date dataOperacao) {
		
		//gerar postergado
		consolidadoFinanceiroCota.setValorPostergado(vlMovFinanPostergado);
		
		Calendar diaPostergado = Calendar.getInstance();
		diaPostergado.setTime(dataOperacao);
		diaPostergado.add(Calendar.DAY_OF_MONTH, qtdDiasNovaCobranca);
	
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = null;
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
		
		if (diasSemanaConcentracaoPagamento == null){
			
			descPostergado = "Forma de cobrança não encontrada para a cota: "+ cota.getNumeroCota() +", a cobrança será postergada.";
		} else if (!diasSemanaConcentracaoPagamento.contains(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))){
			
			descPostergado = "Não existe acúmulo de pagamento para este dia (" + 
					new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()) + ")";
		} else {
			
			descPostergado = "Valor mínimo para dívida não atingido";
		}
		
		//gera movimento financeiro cota
		MovimentoFinanceiroCota movimentoFinanceiroCota = null;
		
		if (vlMovFinanTotal != null && vlMovFinanTotal.compareTo(BigDecimal.ZERO) != 0){
			
			Date data = this.calendarioService.obterProximaDataDiaUtil(diaPostergado.getTime());
			
			movimentoFinanceiroCota = new MovimentoFinanceiroCota();
			movimentoFinanceiroCota.setData(data);
			movimentoFinanceiroCota.setDataCriacao(dataOperacao);
			movimentoFinanceiroCota.setUsuario(usuario);
			movimentoFinanceiroCota.setValor(vlMovFinanTotal.abs());
			movimentoFinanceiroCota.setLancamentoManual(false);
			movimentoFinanceiroCota.setCota(cota);
			movimentoFinanceiroCota.setStatus(StatusAprovacao.APROVADO);
			movimentoFinanceiroCota.setMotivo(descPostergado);
			movimentoFinanceiroCota.setTipoMovimento(tipoMovimentoFinanceiro);
			movimentoFinanceiroCota.setFornecedor(fornecedor!=null?fornecedor:cota.getParametroCobranca().getFornecedorPadrao());
		}
		
		return movimentoFinanceiroCota;
	}

	@Transactional
	@Override
	public void cancelarDividaCobranca(Set<Long> idsMovimentoFinanceiroCota) {
		
		Date dataOperacao = this.distribuidorRepository.obterDataOperacaoDistribuidor();
		
		if (idsMovimentoFinanceiroCota != null && !idsMovimentoFinanceiroCota.isEmpty()){
			
			for (Long idMovFinCota : idsMovimentoFinanceiroCota){
				
				this.cancelarDividaCobranca(idMovFinCota, null, dataOperacao);
			}
		}
	}
	
	@Transactional
	@Override
	public void cancelarDividaCobranca(Long idMovimentoFinanceiroCota, Long idCota, Date dataOperacao) {
		
		List<ConsolidadoFinanceiroCota> consolidados = null;
		
		if (idMovimentoFinanceiroCota != null){

			consolidados = this.consolidadoFinanceiroRepository.obterConsolidadoPorIdMovimentoFinanceiro(idMovimentoFinanceiroCota);
		} else {
			
			consolidados = this.consolidadoFinanceiroRepository.obterConsolidadosDataOperacao(idCota);
		}
		
		if (consolidados != null) {
			
			for (ConsolidadoFinanceiroCota consolidado : consolidados) {
				
				Divida divida = this.dividaRepository.obterDividaPorIdConsolidado(consolidado.getId());
				
				if (divida != null) {
				
					this.cobrancaControleConferenciaEncalheCotaRepository.excluirPorCobranca(divida.getCobranca().getId());
					
					Negociacao negociacao = this.negociacaoRepository.obterNegociacaoPorCobranca(divida.getCobranca().getId());
					
					if (negociacao != null) {
					    
						if (!negociacao.isNegociacaoAvulsa()) {
						
						    this.parcelaNegociacaoRepository.excluirPorNegociacao(negociacao.getId());
						
						    this.negociacaoRepository.remover(negociacao);
						    
						    this.removerDividaCobrancaConsolidado(divida,consolidado, dataOperacao);
						}
					
					} else {
					
						this.removerDividaCobrancaConsolidado(divida,consolidado, dataOperacao);
					}
				}

				List<MovimentoFinanceiroCota> mfcs = consolidado.getMovimentos();
				
				consolidado.setMovimentos(null);
				
				this.consolidadoFinanceiroRepository.alterar(consolidado);
				
                if (mfcs!=null && !mfcs.isEmpty()){
					
			    	this.movimentoFinanceiroCotaService.removerMovimentosFinanceirosCota(mfcs);
				}
				
			    this.consolidadoFinanceiroRepository.remover(consolidado);
			}
		}
		
		this.removerPostergados(idCota, dataOperacao);
	}
	
	private void removerDividaCobrancaConsolidado(Divida divida, ConsolidadoFinanceiroCota consolidado,
			Date dataOperacao){
		
		this.cobrancaRepository.remover(divida.getCobranca());
		
	    this.dividaRepository.remover(divida);
	}
	
	private void removerPostergados(Long idCota, Date dataOperacao){
		
		List<TipoMovimentoFinanceiro> listaPostergados = Arrays.asList(
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.POSTERGADO_CREDITO),
						
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.POSTERGADO_DEBITO)
			);
			
			this.movimentoFinanceiroCotaService.removerPostergadosDia(
					idCota,
					listaPostergados,
					dataOperacao);
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
	
	@Override
	@Transactional
	public void enviarDocumentosCobrancaEmail(String nossoNumero, String email) throws AutenticacaoEmailException{
			
		byte[] anexo = this.documentoCobrancaService.gerarDocumentoCobranca(nossoNumero);
		
		this.emailService.enviar("Cobrança", 
								 "Segue documento de cobrança em anexo.", 
								 new String[]{email}, 
								 new AnexoEmail("Cobranca",anexo,TipoAnexo.PDF));
		
		this.cobrancaRepository.incrementarVia(nossoNumero);
	}
	
	/**
	 * Envia Cobranças para email da Cota
	 * @param cota
	 * @param nossoNumeroEnvioEmail
	 * @throws AutenticacaoEmailException
	 */
	@Override
	@Transactional
	public void enviarDocumentosCobrancaEmail(Cota cota, Map<String, Boolean> nossoNumeroEnvioEmail) throws AutenticacaoEmailException{
		
        String email = cota.getPessoa().getEmail();
		
		if (email == null || email.trim().isEmpty()){

			throw new ValidacaoException(TipoMensagem.ERROR,"A [cota: "+ cota.getNumeroCota() +"] não possui email cadastrado");
		}
		
        for (String nossoNumero : nossoNumeroEnvioEmail.keySet()){
			
			if (nossoNumeroEnvioEmail.get(nossoNumero)){

				try {
					
					this.enviarDocumentosCobrancaEmail(nossoNumero, email);
					
				} catch (AutenticacaoEmailException e) {
					
					throw new ValidacaoException(TipoMensagem.ERROR,"Erro ao Enviar Email de Cobrança para a [Cota:"+ cota.getNumeroCota() +"]: "+e.getMessage());
				}
			}
		}	
	}
	
	private FormaCobranca cloneFormaCobranca(FormaCobranca formaCobranca) {
		
		if (formaCobranca==null){
			
			return null;
		}
		
		try {
			
			if (formaCobranca == null) {
				
				return null;
			}
			
			return (FormaCobranca) BeanUtils.cloneBean(formaCobranca);
			
		} catch (Exception e) {

			throw new ValidacaoException(
					TipoMensagem.ERROR,
					"Erro ao tentar obter [FormaCobranca]!");
		}
	}
}
