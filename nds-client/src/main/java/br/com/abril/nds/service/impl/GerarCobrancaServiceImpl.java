package br.com.abril.nds.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
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
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.HistoricoAcumuloDividaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;

@Service
public class GerarCobrancaServiceImpl implements GerarCobrancaService {

	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private ControleConferenciaEncalheRepository controleConferenciaEncalheRepository;
	
	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
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
	
	@Override
	@Transactional
	public void gerarCobranca(Long idCota, Long idUsuario) {
		
		// verificar se a operação de conferencia ja foi concluida
		StatusOperacao statusOperacao = this.controleConferenciaEncalheRepository.obterStatusConferenciaDataOperacao();
		
		if (statusOperacao == null || !StatusOperacao.CONCLUIDO.equals(statusOperacao)){
			throw new ValidacaoException(TipoMensagem.ERROR, "A conferência de box de encalhe deve ser concluída antes de gerar dívidas.");
		}
		
		//Caso esteja gerando cobrança para uma única cota
		if (idCota != null){
			boolean existeCobranca = 
					this.consolidadoFinanceiroRepository.verificarConsodidadoCotaPorData(idCota, new Date());
			
			if (existeCobranca){
				throw new ValidacaoException(TipoMensagem.WARNING, "Já foi gerada cobrança para esta cota na data de hoje.");
			}
		}
		
		//Buscar politica de cobrança e forma de cobrança do distribuidor
		PoliticaCobranca politicaCobranca = this.politicaCobrancaRepository.buscarPoliticaCobrancaPorDistribuidor();
		if (politicaCobranca == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Politica de cobrança não encontrada.");
		} else if (politicaCobranca.getFormaCobranca() == null){
			throw new ValidacaoException(TipoMensagem.ERROR, "Forma de cobrança não encontrada.");
		}
		
		//Caso o principal modo de cobrança seja boleto a baixa automática deve ter sido executada
		if (TipoCobranca.BOLETO.equals(politicaCobranca.getFormaCobranca().getTipoCobranca())){
			ControleBaixaBancaria controleBaixaBancaria = this.controleBaixaBancariaRepository.obterPorData(new Date());
			
			if (controleBaixaBancaria == null || !StatusControle.CONCLUIDO_SUCESSO.equals(controleBaixaBancaria.getStatus())){
				throw new ValidacaoException(TipoMensagem.ERROR, "Baixa Automática ainda não executada.");
			}
		}
		
		// buscar movimentos financeiros da cota para a data de operação em andamento
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCotaDataOperacao(idCota, new Date());
		
		if (listaMovimentoFinanceiroCota != null &&
				!listaMovimentoFinanceiroCota.isEmpty()){
			
			//Varre todos os movimentos encontrados, agrupando por cota
			Cota ultimaCota = listaMovimentoFinanceiroCota.get(0).getCota();
			
			TipoCobranca tipoCobranca = politicaCobranca.getFormaCobranca().getTipoCobranca();
			
			if (TipoCobranca.BOLETO.equals(tipoCobranca)){
				this.verificarCotaTemBanco(ultimaCota);
			}
			
			List<MovimentoFinanceiroCota> movimentos = new ArrayList<MovimentoFinanceiroCota>();
			
			for (MovimentoFinanceiroCota movimentoFinanceiroCota : listaMovimentoFinanceiroCota){
				if (movimentoFinanceiroCota.getCota().equals(ultimaCota)){
					
					movimentos.add(movimentoFinanceiroCota);
				} else {
					
					if (TipoCobranca.BOLETO.equals(politicaCobranca.getFormaCobranca().getTipoCobranca())){
						this.verificarCotaTemBanco(ultimaCota);
					}
					
					//Decide se gera movimento consolidado ou postergado para a cota
					BigDecimal valorMinimo = 
							this.obterValorMinino(ultimaCota, politicaCobranca.getFormaCobranca().getValorMinimoEmissao());
					
					this.inserirConsolidadoFinanceiro(ultimaCota, movimentos,
							valorMinimo, politicaCobranca.isAcumulaDivida(), idUsuario, 
							tipoCobranca != null ? tipoCobranca : politicaCobranca.getFormaCobranca().getTipoCobranca());
					
					//Limpa dados para contabilizar próxima cota
					ultimaCota = movimentoFinanceiroCota.getCota();
					
					movimentos = new ArrayList<MovimentoFinanceiroCota>();
					
					movimentos.add(movimentoFinanceiroCota);
				}
			}
			
			//Decide se gera movimento consolidado ou postergado para a ultima cota
			BigDecimal valorMinimo = 
					this.obterValorMinino(ultimaCota, politicaCobranca.getFormaCobranca().getValorMinimoEmissao());
			
			this.inserirConsolidadoFinanceiro(ultimaCota, movimentos, valorMinimo,
					politicaCobranca.isAcumulaDivida(), idUsuario, 
					tipoCobranca != null ? tipoCobranca : politicaCobranca.getFormaCobranca().getTipoCobranca());
		}
	}
	
	private boolean verificarCotaTemBanco(Cota cota){
		if (cota.getParametroCobranca() == null || cota.getParametroCobranca().getFormaCobranca() == null ||
				cota.getParametroCobranca().getFormaCobranca().getBanco() == null){
			
			throw new ValidacaoException(
					TipoMensagem.ERROR, 
					"Para pagamento por boleto é necessário que a cota tenha um banco cadastrado. Número da cota sem banco: " + 
							cota.getNumeroCota());
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
	
	private void inserirConsolidadoFinanceiro(Cota cota, List<MovimentoFinanceiroCota> movimentos, BigDecimal valorMinino,
			boolean acumulaDivida, Long idUsuario, TipoCobranca tipoCobranca){
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = new ConsolidadoFinanceiroCota();
		consolidadoFinanceiroCota.setCota(cota);
		consolidadoFinanceiroCota.setDataConsolidado(new Date());
		consolidadoFinanceiroCota.setMovimentos(movimentos);
		
		BigDecimal vlMovFinanTotal = BigDecimal.ZERO;
		BigDecimal vlMovFinanDebitoCredito = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncalhe = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncargos = BigDecimal.ZERO;
		BigDecimal vlMovFinanVendaEncalhe = BigDecimal.ZERO;
		
		for (MovimentoFinanceiroCota movimentoFinanceiroCota : movimentos){
			switch (movimentoFinanceiroCota.getTipoMovimento().getGrupoMovimentoFinaceiro()){
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
		
		//obtem a data de vencimento de acordo com o dia em que se concentram os pagamentos da cota
		List<Integer> diasSemanaConcentracaoPagamento = 
				this.cotaRepository.obterDiasConcentracaoPagamentoCota(cota.getId());
		
		int fatorVencimento = cota.getParametroCobranca() != null ? cota.getParametroCobranca().getFatorVencimento() : 0;
		Date dataVencimento = 
				this.calendarioService.adicionarDiasUteis(
						consolidadoFinanceiroCota.getDataConsolidado(), fatorVencimento,
						diasSemanaConcentracaoPagamento);
		
		Divida novaDivida = null;
		
		HistoricoAcumuloDivida historicoAcumuloDivida = null;
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = null;
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = null;
		
		//se existe divida
		if (vlMovFinanTotal.compareTo(BigDecimal.ZERO) < 0){
			
			vlMovFinanTotal = vlMovFinanTotal.negate();
			
			if (vlMovFinanTotal.compareTo(valorMinino) < 0){
				//gerar postergado
				consolidadoFinanceiroCota.setValorPostergado(vlMovFinanTotal);
				
				//gera movimento financeiro cota
				movimentoFinanceiroCota = new MovimentoFinanceiroCota();
				movimentoFinanceiroCota.setMotivo("Valor mínimo para dívida não atingido.");
				movimentoFinanceiroCota.setData(dataVencimento);
				movimentoFinanceiroCota.setDataCriacao(new Date());
				movimentoFinanceiroCota.setUsuario(usuario);
				movimentoFinanceiroCota.setValor(vlMovFinanTotal);
				movimentoFinanceiroCota.setLancamentoManual(false);
				movimentoFinanceiroCota.setCota(cota);
				
				tipoMovimentoFinanceiro = new TipoMovimentoFinanceiro();
				tipoMovimentoFinanceiro.setAprovacaoAutomatica(false);
				tipoMovimentoFinanceiro.setGrupoMovimentoFinaceiro(GrupoMovimentoFinaceiro.DEBITO);
				tipoMovimentoFinanceiro.setDescricao("Geração de dívida - Valor mínimo para dívida não atingido.");
				
				movimentoFinanceiroCota.setTipoMovimento(tipoMovimentoFinanceiro);
			} else {
				
				novaDivida = new Divida();
				novaDivida.setValor(vlMovFinanTotal);
				novaDivida.setData(consolidadoFinanceiroCota.getDataConsolidado());
				novaDivida.setConsolidado(consolidadoFinanceiroCota);
				novaDivida.setCota(cota);
				novaDivida.setStatus(StatusDivida.EM_ABERTO);
				novaDivida.setResponsavel(usuario);
				
				//se o distribuidor acumula divida
				if (acumulaDivida){
					Divida divida = this.dividaRepository.obterUltimaDividaPorCota(cota.getId());
					
					//caso não tenha divida anterior, ou tenha sido quitada
					if (divida == null || StatusDivida.QUITADA.equals(divida.getStatus())){
						divida = novaDivida;
					} else {
						
						divida.setAcumulada(true);
						novaDivida.getAcumulado().add(divida);
						
						historicoAcumuloDivida = new HistoricoAcumuloDivida();
						historicoAcumuloDivida.setDataInclusao(new Date());
						historicoAcumuloDivida.setDivida(divida);
						historicoAcumuloDivida.setResponsavel(usuario);
						historicoAcumuloDivida.setStatus(StatusInadimplencia.ATIVA);
					}
					
					novaDivida.setValor(
							vlMovFinanTotal.add(novaDivida.getValor() == null ? BigDecimal.ZERO : novaDivida.getValor()));
				}
			}
		}
		
		this.consolidadoFinanceiroRepository.adicionar(consolidadoFinanceiroCota);
		
		if (novaDivida != null){
			if (novaDivida.getId() == null){
				this.dividaRepository.adicionar(novaDivida);
			} else {
				this.dividaRepository.alterar(novaDivida);
			}
			
			if (historicoAcumuloDivida != null){
				this.historicoAcumuloDividaRepository.adicionar(historicoAcumuloDivida);
			}
			
			Cobranca cobranca = null;
			
			switch (tipoCobranca){
				case BOLETO:
					cobranca = new Boleto();
					((Boleto)(cobranca)).setBanco(cota.getParametroCobranca().getFormaCobranca().getBanco());
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
			
			if (cobranca != null){
				cobranca.setCota(cota);
				cobranca.setDataEmissao(new Date());
				cobranca.setDivida(novaDivida);
				cobranca.setStatusCobranca(StatusCobranca.NAO_PAGO);
				cobranca.setDataVencimento(dataVencimento);
				cobranca.setNossoNumero(Util.gerarNossoNumero(cota.getNumeroCota(), cobranca.getDataEmissao()));
				cobranca.setValor(novaDivida.getValor());
				
				this.cobrancaRepository.adicionar(cobranca);
				
				if (cota.getParametroCobranca().isRecebeCobrancaEmail()){
					File documentoCobranca = 
							this.documentoCobrancaService.gerarDocumentoCobranca(cobranca.getNossoNumero());
					
					try {
						this.emailService.enviar(
								"Cobrança", 
								"Segue documento de cobrança em anexo.", 
								new String[]{cota.getPessoa().getEmail()}, 
								documentoCobranca);
						
						this.cobrancaRepository.incrementarVia(cobranca.getNossoNumero());
					} catch (AutenticacaoEmailException e) {
						
					}
				}
			}
		}
		
		if (movimentoFinanceiroCota != null){
			this.tipoMovimentoFinanceiroRepository.adicionar(tipoMovimentoFinanceiro);
			this.movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiroCota);
		}
	}
}