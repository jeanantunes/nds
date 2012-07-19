package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.CobrancaCheque;
import br.com.abril.nds.model.financeiro.CobrancaDeposito;
import br.com.abril.nds.model.financeiro.CobrancaDinheiro;
import br.com.abril.nds.model.financeiro.CobrancaTransferenciaBancaria;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.HistoricoAcumuloDivida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.repository.HistoricoAcumuloDividaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;

@Service
public class GerarCobrancaServiceImpl implements GerarCobrancaService {

	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Autowired
	private ControleBaixaBancariaRepository controleBaixaBancariaRepository;
	
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
	private PoliticaCobrancaService politicaCobrancaService;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;

	@Autowired
	private ParametroCobrancaCotaService financeiroService;
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	private FechamentoEncalheRepository fechamentoEncalheRepository;

	
	@Override
	@Transactional(noRollbackFor = GerarCobrancaValidacaoException.class)
	public void gerarCobranca(Long idCota, Long idUsuario, Set<String> setNossoNumero)
		throws GerarCobrancaValidacaoException{
		
		Distribuidor distribuidor = this.distribuidorRepository.obter();
		
		if (this.consolidadoFinanceiroRepository.obterQuantidadeDividasGeradasData((distribuidor.getDataOperacao())) >= 0){
			
			throw new GerarCobrancaValidacaoException(
					new ValidacaoException(TipoMensagem.WARNING, "Já foram geradas dívidas para esta data de operação."));
		}
		
		//alteração na EMS 0028, agora deve verificar se o Fechamento do Encalhe(EMS 0181) tenha sido finalizado
		if (!this.fechamentoEncalheRepository.buscaControleFechamentoEncalhe(distribuidor.getDataOperacao())){
			
			throw new GerarCobrancaValidacaoException(
					new ValidacaoException(TipoMensagem.WARNING, "O fechamento de encalhe deve ser concluído antes de gerar dívidas."));
		}
		
		//Caso esteja gerando cobrança para uma única cota
		if (idCota != null){
			boolean existeCobranca = 
					this.consolidadoFinanceiroRepository.verificarConsodidadoCotaPorDataOperacao(idCota);
			
			if (existeCobranca){
				throw new GerarCobrancaValidacaoException(
						new ValidacaoException(TipoMensagem.WARNING, "Já foi gerada cobrança para esta cota na data de hoje."));
			}
		}

		//Buscar politica de cobrança e forma de cobrança do distribuidor
		PoliticaCobranca politicaPrincipal = this.politicaCobrancaService.obterPoliticaCobrancaPrincipal();
		
		if (politicaPrincipal == null){
			throw new GerarCobrancaValidacaoException(
					new ValidacaoException(TipoMensagem.ERROR, "Politica de cobrança não encontrada."));
		} else if (politicaPrincipal.getFormaCobranca() == null){
			throw new GerarCobrancaValidacaoException(
					new ValidacaoException(TipoMensagem.ERROR, "Forma de cobrança não encontrada."));
		}
		
		//Caso o principal modo de cobrança seja boleto a baixa automática deve ter sido executada
		if (TipoCobranca.BOLETO.equals(politicaPrincipal.getFormaCobranca().getTipoCobranca())){
			ControleBaixaBancaria controleBaixaBancaria = this.controleBaixaBancariaRepository.obterPorData(new Date());
			
			if (controleBaixaBancaria == null || StatusControle.INICIADO.equals(controleBaixaBancaria.getStatus())){
				throw new GerarCobrancaValidacaoException(
						new ValidacaoException(TipoMensagem.ERROR, "Baixa Automática ainda não executada."));
			}
		}
		
		List<String> msgs = new ArrayList<String>();
		
		// buscar movimentos financeiros da cota, se informada, caso contrario de todas as cotas
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCota(idCota);
		
		if (listaMovimentoFinanceiroCota != null && !listaMovimentoFinanceiroCota.isEmpty()){
			
			//Varre todos os movimentos encontrados, agrupando por cota e por fornecedor
			Cota ultimaCota = listaMovimentoFinanceiroCota.get(0).getCota();
			
			Fornecedor ultimoFornecedor = null;
			
			if (listaMovimentoFinanceiroCota.get(0).getMovimentos() != null && 
					!listaMovimentoFinanceiroCota.get(0).getMovimentos().isEmpty() &&
					listaMovimentoFinanceiroCota.get(0).getMovimentos().get(0) != null){
				
				ultimoFornecedor = listaMovimentoFinanceiroCota.get(0).getMovimentos().get(0).getProdutoEdicao().getProduto().getFornecedor();
			}
			
			TipoCobranca tipoCobranca = politicaPrincipal.getFormaCobranca().getTipoCobranca();
			
			List<MovimentoFinanceiroCota> movimentos = new ArrayList<MovimentoFinanceiroCota>();
			
			String nossoNumero = null;
			
			for (MovimentoFinanceiroCota movimentoFinanceiroCota : listaMovimentoFinanceiroCota){
				
				//verifica se cota esta suspensa, se estiver verifica se existe chamada de encalhe na data de operação
				if (SituacaoCadastro.SUSPENSO.equals(ultimaCota.getSituacaoCadastro())){
					
					if (!movimentoFinanceiroCota.getCota().equals(ultimaCota)){
						
						if (this.chamadaEncalheCotaRepository.obterQtdListaChamaEncalheCota(ultimaCota.getNumeroCota(), 
								distribuidor.getDataOperacao(), null, false, false, false) <= 0){
							
							continue;
						}
					}
				}
				
				if (movimentoFinanceiroCota.getCota().equals(ultimaCota) &&
						movimentoFinanceiroCota.getMovimentos() != null && 
						!movimentoFinanceiroCota.getMovimentos().isEmpty() && 
						movimentoFinanceiroCota.getMovimentos().get(0) != null &&
						movimentoFinanceiroCota.getMovimentos().get(0).getProdutoEdicao().getProduto().getFornecedor().equals(ultimoFornecedor)){
					
					movimentos.add(movimentoFinanceiroCota);
				} else {
					
					if (TipoCobranca.BOLETO.equals(politicaPrincipal.getFormaCobranca().getTipoCobranca())){
						this.verificarCotaTemBanco(ultimaCota, msgs);
					}
					
					//Decide se gera movimento consolidado ou postergado para a cota
					nossoNumero = this.inserirConsolidadoFinanceiro(ultimaCota, movimentos,
							politicaPrincipal.getFormaCobranca().getValorMinimoEmissao(), politicaPrincipal.isAcumulaDivida(), idUsuario, 
							tipoCobranca != null ? tipoCobranca : politicaPrincipal.getFormaCobranca().getTipoCobranca(),
							politicaPrincipal.getNumeroDiasNovaCobranca(),
							distribuidor, msgs, ultimoFornecedor);
					
					if (nossoNumero != null){
						
						setNossoNumero.add(nossoNumero);
					}
					
					//Limpa dados para contabilizar próxima cota
					ultimaCota = movimentoFinanceiroCota.getCota();
					
					if (movimentoFinanceiroCota.getMovimentos() != null && 
							!movimentoFinanceiroCota.getMovimentos().isEmpty() &&
							movimentoFinanceiroCota.getMovimentos().get(0) != null){
						
						ultimoFornecedor = movimentoFinanceiroCota.getMovimentos().get(0).getProdutoEdicao().getProduto().getFornecedor();
					} else {
						
						ultimoFornecedor = null;
					}
					
					movimentos = new ArrayList<MovimentoFinanceiroCota>();
					
					movimentos.add(movimentoFinanceiroCota);
				}
			}
			
			if (TipoCobranca.BOLETO.equals(tipoCobranca)){
				this.verificarCotaTemBanco(ultimaCota, msgs);
			}
			
			//Decide se gera movimento consolidado ou postergado para a ultima cota
			nossoNumero = this.inserirConsolidadoFinanceiro(ultimaCota, movimentos, politicaPrincipal.getFormaCobranca().getValorMinimoEmissao(),
					politicaPrincipal.isAcumulaDivida(), idUsuario, 
					tipoCobranca != null ? tipoCobranca : politicaPrincipal.getFormaCobranca().getTipoCobranca(),
					politicaPrincipal.getNumeroDiasNovaCobranca(), distribuidor, msgs, ultimoFornecedor);
			
			if (nossoNumero != null){
				
				setNossoNumero.add(nossoNumero);
			}
		}
		
		if (!msgs.isEmpty()){
			
			throw new GerarCobrancaValidacaoException(
					new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, msgs)));
		}
	}
	
	private boolean verificarCotaTemBanco(Cota cota, List<String> msgs){

		FormaCobranca formaCobtancaPrincipal = this.financeiroService.obterFormaCobrancaPrincipalCota(cota.getId());

		if (cota.getParametroCobranca() == null || formaCobtancaPrincipal == null ||
				formaCobtancaPrincipal.getBanco() == null){	
			
			msgs.add("Para pagamento por boleto é necessário que a cota tenha um banco cadastrado. Número da cota sem banco: " + 
							cota.getNumeroCota());
			
			return false;
		}
		
		return true;
	}
	
	private BigDecimal obterValorMinino(Cota cota, BigDecimal valorMininoDistribuidor){
		BigDecimal valorMinimo = 
				(cota.getParametroCobranca() != null && cota.getParametroCobranca().getValorMininoCobranca() != null) ?
						cota.getParametroCobranca().getValorMininoCobranca() :
							valorMininoDistribuidor;
						
		return valorMinimo;
	}
	
	private String inserirConsolidadoFinanceiro(Cota cota, List<MovimentoFinanceiroCota> movimentos, BigDecimal valorMininoDistribuidor,
			boolean acumulaDivida, Long idUsuario, TipoCobranca tipoCobranca, int qtdDiasNovaCobranca, Distribuidor distribuidor, List<String> msgs,
			Fornecedor fornecedor){
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = new ConsolidadoFinanceiroCota();
		consolidadoFinanceiroCota.setCota(cota);
		consolidadoFinanceiroCota.setDataConsolidado(distribuidor.getDataOperacao());
		consolidadoFinanceiroCota.setMovimentos(movimentos);
		
		BigDecimal vlMovFinanTotal = BigDecimal.ZERO;
		BigDecimal vlMovFinanDebitoCredito = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncalhe = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncargos = BigDecimal.ZERO;
		BigDecimal vlMovFinanVendaEncalhe = BigDecimal.ZERO;
		
		for (MovimentoFinanceiroCota movimentoFinanceiroCota : movimentos){
			switch (((TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento()).getGrupoMovimentoFinaceiro()){
				case CREDITO:
					vlMovFinanTotal = vlMovFinanTotal.add(movimentoFinanceiroCota.getValor());
					vlMovFinanDebitoCredito = vlMovFinanDebitoCredito.add(movimentoFinanceiroCota.getValor());
				break;
				
				case DEBITO:
					vlMovFinanTotal = 
							vlMovFinanTotal.add(
								movimentoFinanceiroCota.getValor() != null ? 
										movimentoFinanceiroCota.getValor().negate() : 
											BigDecimal.ZERO);
					
					vlMovFinanDebitoCredito = 
							vlMovFinanDebitoCredito.add(movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor().negate() : 
										BigDecimal.ZERO);
				break;
				
				case ENVIO_ENCALHE:
					vlMovFinanTotal = vlMovFinanTotal.add(movimentoFinanceiroCota.getValor());
					vlMovFinanEncalhe = vlMovFinanEncalhe.add(movimentoFinanceiroCota.getValor());
				break;
				
				case ESTORNO_REPARTE_COTA_AUSENTE:
					vlMovFinanTotal = vlMovFinanTotal.add(movimentoFinanceiroCota.getValor());
				break;
				
				case JUROS:
					vlMovFinanTotal = 
							vlMovFinanTotal.add(
								movimentoFinanceiroCota.getValor() != null ? 
										movimentoFinanceiroCota.getValor().negate() : 
											BigDecimal.ZERO);
					
					vlMovFinanEncargos = vlMovFinanEncargos.add(
							movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor().negate() : 
										BigDecimal.ZERO);
				break;
				
				case MULTA:
					vlMovFinanTotal = 
							vlMovFinanTotal.add(
								movimentoFinanceiroCota.getValor() != null ? 
										movimentoFinanceiroCota.getValor().negate() : 
											BigDecimal.ZERO);
					
					vlMovFinanEncargos = vlMovFinanEncargos.add(
							movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor().negate() : 
										BigDecimal.ZERO);
				break;
				
				case POSTERGADO:
					vlMovFinanTotal = 
							vlMovFinanTotal.add(
								movimentoFinanceiroCota.getValor() != null ? 
										movimentoFinanceiroCota.getValor().negate() : 
											BigDecimal.ZERO);
					
					vlMovFinanEncargos = vlMovFinanEncargos.add(
							movimentoFinanceiroCota.getValor() != null ? 
									movimentoFinanceiroCota.getValor().negate() : 
										BigDecimal.ZERO);
				break;
				
				case RECEBIMENTO_REPARTE:
					vlMovFinanTotal = 
							vlMovFinanTotal.add(
								movimentoFinanceiroCota.getValor() != null ? 
										movimentoFinanceiroCota.getValor().negate() : 
											BigDecimal.ZERO);
				break;
				
				case RECUPERACAO_REPARTE_COTA_AUSENTE:
					vlMovFinanTotal = 
							vlMovFinanTotal.add(
								movimentoFinanceiroCota.getValor() != null ? 
										movimentoFinanceiroCota.getValor().negate() : 
											BigDecimal.ZERO);
				break;
			}
		}
		consolidadoFinanceiroCota.setTotal(vlMovFinanTotal);
		consolidadoFinanceiroCota.setDebitoCredito(vlMovFinanDebitoCredito);
		consolidadoFinanceiroCota.setEncalhe(vlMovFinanEncalhe);
		consolidadoFinanceiroCota.setEncargos(vlMovFinanEncargos);
		consolidadoFinanceiroCota.setVendaEncalhe(vlMovFinanVendaEncalhe);
		
		Usuario usuario = new Usuario();
		usuario.setId(idUsuario);
		
		FormaCobranca formaCobrancaPrincipal = this.financeiroService.obterFormaCobrancaPrincipalCota(cota.getId());
		
		if (formaCobrancaPrincipal == null){
			
			msgs.add("Forma de cobrança principal para cota de número: " + cota.getNumeroCota() + " não encontrada.");
			
			return null;
		}
		
		Date dataVencimento = null;
		
		List<Integer> diasSemanaConcentracaoPagamento = null;
		
		//obtem a data de vencimento de acordo com o dia em que se concentram os pagamentos da cota
		int fatorVencimento = cota.getParametroCobranca() != null ? cota.getParametroCobranca().getFatorVencimento() : 0;
		
		switch(formaCobrancaPrincipal.getTipoFormaCobranca()){
			case MENSAL:
				dataVencimento = 
				this.calendarioService.adicionarDiasUteis(
						consolidadoFinanceiroCota.getDataConsolidado(), fatorVencimento,
						null, formaCobrancaPrincipal.getDiaDoMes());
			break;
			
			case SEMANAL:
				diasSemanaConcentracaoPagamento = 
						this.cotaRepository.obterDiasConcentracaoPagamentoCota(cota.getId());
				
				dataVencimento = 
						this.calendarioService.adicionarDiasUteis(
								consolidadoFinanceiroCota.getDataConsolidado(), fatorVencimento,
								diasSemanaConcentracaoPagamento, null);
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
		
		//se existe divida
		if (vlMovFinanTotal.compareTo(BigDecimal.ZERO) < 0){
			
			vlMovFinanTotal = vlMovFinanTotal.negate();
			
			BigDecimal valorMinino = this.obterValorMinino(cota, valorMininoDistribuidor);
			
			//caso não tenha alcaçado valor minino de cobrança ou não seja um dia de concentração de cobrança
			if (vlMovFinanTotal.compareTo(valorMinino) < 0 || 
					(diasSemanaConcentracaoPagamento != null && 
					!diasSemanaConcentracaoPagamento.contains(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)))){
				
				//gerar postergado
				consolidadoFinanceiroCota.setValorPostergado(vlMovFinanTotal);
				
				//gera movimento financeiro cota
				movimentoFinanceiroCota = new MovimentoFinanceiroCota();
				movimentoFinanceiroCota.setMotivo("Valor mínimo para dívida não atingido.");
				
				Calendar diaPostergado = Calendar.getInstance();
				diaPostergado.setTime(new Date());
				diaPostergado.add(Calendar.DAY_OF_MONTH, qtdDiasNovaCobranca);
				
				movimentoFinanceiroCota.setData(diaPostergado.getTime());
				movimentoFinanceiroCota.setDataCriacao(new Date());
				movimentoFinanceiroCota.setUsuario(usuario);
				movimentoFinanceiroCota.setValor(vlMovFinanTotal);
				movimentoFinanceiroCota.setLancamentoManual(false);
				movimentoFinanceiroCota.setCota(cota);
				
				tipoMovimentoFinanceiro = new TipoMovimentoFinanceiro();
				tipoMovimentoFinanceiro.setAprovacaoAutomatica(false);
				tipoMovimentoFinanceiro.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.DEBITO);
				
				String descPostergado = null;
				
				if (diasSemanaConcentracaoPagamento != null && !diasSemanaConcentracaoPagamento.contains(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))){
					
					descPostergado = "Não existe acúmulo de pagamento para este dia (" + new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()) + ")";
				} else {
					
					descPostergado = "Valor mínimo para dívida não atingido";
				}
				
				tipoMovimentoFinanceiro.setDescricao("Geração de dívida - " + descPostergado);
				
				movimentoFinanceiroCota.setTipoMovimento(tipoMovimentoFinanceiro);
			} else {
				
				novaDivida = new Divida();
				novaDivida.setValor(vlMovFinanTotal);
				novaDivida.setData(consolidadoFinanceiroCota.getDataConsolidado());
				novaDivida.setConsolidado(consolidadoFinanceiroCota);
				novaDivida.setCota(cota);
				novaDivida.setStatus(StatusDivida.EM_ABERTO);
				novaDivida.setResponsavel(usuario);
				
				BigDecimal valorCalculadoJuros = BigDecimal.ZERO;
				
				//se o distribuidor acumula divida
				if (acumulaDivida){
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
						
						valorCalculadoJuros = 
								this.cobrancaService.calcularJuros(
										null,
										cota,
										distribuidor,
										vlMovFinanTotal.add(novaDivida.getValor()).subtract(valorMulta), 
										divida.getCobranca().getDataVencimento(),
										new Date());
						
						divida.setAcumulada(true);
						novaDivida.getAcumulado().add(divida);
						
						historicoAcumuloDivida = new HistoricoAcumuloDivida();
						historicoAcumuloDivida.setDataInclusao(new Date());
						historicoAcumuloDivida.setDivida(divida);
						historicoAcumuloDivida.setResponsavel(usuario);
						historicoAcumuloDivida.setStatus(StatusInadimplencia.ATIVA);
						
						novaDivida.setValor(valorCalculadoJuros);
					}
				}
			}
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
			}
			
			cobranca.setBanco(formaCobrancaPrincipal.getBanco());
			cobranca.setCota(cota);
			cobranca.setDataEmissao(new Date());
			cobranca.setDivida(novaDivida);
			cobranca.setStatusCobranca(StatusCobranca.NAO_PAGO);
			cobranca.setDataVencimento(dataVencimento);
			
			cobranca.setNossoNumero(
					Util.gerarNossoNumero(
							cota.getNumeroCota(), 
							cobranca.getDataEmissao(), 
							formaCobrancaPrincipal.getBanco().getNumeroBanco(),
							fornecedor != null ? fornecedor.getId() : null,
							movimentos.get(0).getId()
							));
			
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
			this.tipoMovimentoFinanceiroRepository.adicionar(tipoMovimentoFinanceiro);
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
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		Long quantidadeRegistro = movimentoFinanceiroCotaRepository.obterQuantidadeMovimentoFinanceiroDataOperacao(distribuidor.getDataOperacao()); 
		
		return (quantidadeRegistro == null || quantidadeRegistro == 0) ? Boolean.FALSE : Boolean.TRUE;
	}

	@Transactional
	@Override
	public void cancelarDividaCobranca(Set<Long> idsMovimentoFinanceiroCota) {
		
		if (idsMovimentoFinanceiroCota != null && !idsMovimentoFinanceiroCota.isEmpty()){
			
			for (Long idMovFinCota : idsMovimentoFinanceiroCota){
				
				this.cancelarDividaCobranca(idMovFinCota);
			}
		}
	}
	
	@Transactional
	@Override
	public void cancelarDividaCobranca(Long idMovimentoFinanceiroCota) {
		
		if (idMovimentoFinanceiroCota != null){

			ConsolidadoFinanceiroCota consolidado =
					this.consolidadoFinanceiroRepository.obterConsolidadoPorIdMovimentoFinanceiro(idMovimentoFinanceiroCota);
			
			if (consolidado != null){
				
				Divida divida = this.dividaRepository.obterDividaPorIdConsolidado(consolidado.getId());
				
				if (divida != null){
				
					this.cobrancaRepository.excluirCobrancaPorIdDivida(divida.getId());
					
					this.dividaRepository.remover(divida);
				}
				
				this.consolidadoFinanceiroRepository.remover(consolidado);
			}
		}
	}
}