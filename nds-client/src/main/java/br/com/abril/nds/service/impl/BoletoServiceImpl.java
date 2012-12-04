package br.com.abril.nds.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.DetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.BaixaManual;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BaixaCobrancaRepository;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.ControleBaixaBancariaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;
import br.com.abril.nds.util.CorpoBoleto;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.GeradorBoleto;
import br.com.abril.nds.util.TipoBaixaCobranca;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Boleto}
 * 
 * @author Discover Technology
 */
@Service
public class BoletoServiceImpl implements BoletoService {
	
	@Autowired
	protected EmailService email;

	@Autowired
	protected BoletoRepository boletoRepository;
	
	@Autowired
	protected PoliticaCobrancaService politicaCobrancaService;
	
	@Autowired
	protected PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Autowired
	protected BaixaCobrancaRepository baixaCobrancaRepository;
	
	@Autowired
	protected ControleBaixaBancariaRepository controleBaixaRepository;
	
	@Autowired
	protected DistribuidorService distribuidorService;
	
	@Autowired
	protected ControleBaixaBancariaService controleBaixaService;
	
	@Autowired
	protected MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	protected CalendarioService calendarioService;
	
	@Autowired
	protected CobrancaService cobrancaService;
	
	@Autowired
	protected CobrancaRepository cobrancaRepository;

	@Autowired
	protected TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	protected BancoRepository bancoRepository;
	
	/**
	 * Método responsável por obter boletos por numero da cota
	 * @param filtro
	 * @return Lista de boletos encontrados
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Boleto> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {
		return this.boletoRepository.obterBoletosPorCota(filtro);
	}
	
	/**
	 * Método responsável por obter boleto por nossoNumero
	 * @param nossoNumero
	 * @return Boletos encontrado
	 */
	@Override
	@Transactional(readOnly=true)
	public Boleto obterBoletoPorNossoNumero(String nossoNumero, Boolean dividaAcumulada) {
		return boletoRepository.obterPorNossoNumero(nossoNumero, dividaAcumulada);
	}

	
	/**
	 * Método responsável por obter a quantidade de boletos por numero da cota
	 * @param filtro
	 * @return Quantidade de boletos encontrados
	 */
	@Override
	@Transactional(readOnly=true)
	public long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {
		return this.boletoRepository.obterQuantidadeBoletosPorCota(filtro);
	}
	
	@Override
	@Transactional(readOnly = true)
	public ResumoBaixaBoletosDTO obterResumoBaixaFinanceiraBoletos(Date data) {
		
		ResumoBaixaBoletosDTO resumoBaixaBoletos = new ResumoBaixaBoletosDTO();
		
		resumoBaixaBoletos.setQuantidadePrevisao(
			this.boletoRepository.obterQuantidadeBoletosPrevistos(data).intValue());
		
		resumoBaixaBoletos.setQuantidadeLidos(
			this.boletoRepository.obterQuantidadeBoletosLidos(data).intValue());
		
		resumoBaixaBoletos.setQuantidadeBaixados(
			this.boletoRepository.obterQuantidadeBoletosBaixados(data).intValue());
		
		resumoBaixaBoletos.setQuantidadeRejeitados(
			this.boletoRepository.obterQuantidadeBoletosRejeitados(data).intValue());
		
		resumoBaixaBoletos.setQuantidadeBaixadosComDivergencia(
			this.boletoRepository.obterQuantidadeBoletosBaixadosComDivergencia(data).intValue());
		
		Date dataVencimento = calendarioService.subtrairDiasUteis(data, 1);
		
		resumoBaixaBoletos.setQuantidadeInadimplentes(
			this.boletoRepository.obterQuantidadeBoletosInadimplentes(dataVencimento).intValue());
		
		resumoBaixaBoletos.setValorTotalBancario(
			this.boletoRepository.obterValorTotalBancario(data));
		
		List<ControleBaixaBancaria> listaControleBaixa =
			this.controleBaixaRepository.obterListaControleBaixaBancaria(
				data, StatusControle.CONCLUIDO_SUCESSO);
		
		boolean possuiDiversasBaixas = (listaControleBaixa.size() > 1);
		
		resumoBaixaBoletos.setPossuiDiversasBaixas(possuiDiversasBaixas);

		return resumoBaixaBoletos;
	}
	
	@Override
	@Transactional
	public void baixarBoletosAutomatico(ArquivoPagamentoBancoDTO arquivoPagamento,
							  			BigDecimal valorFinanceiro, Usuario usuario) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		if (distribuidor == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, 
					"Parâmetros do distribuidor não encontrados!");
		}
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		Banco banco = this.bancoRepository.obterBanco(arquivoPagamento.getCodigoBanco(),
													  arquivoPagamento.getNumeroAgencia(),
													  arquivoPagamento.getNumeroConta());
		
		if (banco == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, 
				"Banco não encontrado!");
		}
		
		ControleBaixaBancaria controleBaixa =
			this.controleBaixaRepository.obterControleBaixaBancaria(dataOperacao, banco);
		
		if (controleBaixa != null
				&& controleBaixa.getStatus().equals(StatusControle.CONCLUIDO_SUCESSO)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, 
				"Já foi realizada baixa automática na data de operação atual para o banco " + banco.getNome() + "!");
		}
		
		if (valorFinanceiro == null || arquivoPagamento.getSomaPagamentos() == null
				|| valorFinanceiro.compareTo(arquivoPagamento.getSomaPagamentos()) != 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, 
				"Valor financeiro inválido! A soma dos valores dos boletos pagos " +
				"deve ser igual ao valor informado!");
		}
		
		PoliticaCobranca politicaCobranca =
			this.politicaCobrancaRepository.obterPorTipoCobranca(TipoCobranca.BOLETO);
		
		if (politicaCobranca == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, 
				"Política de cobrança para boletos não encontrada!");
		}
		
		this.controleBaixaService.alterarControleBaixa(StatusControle.INICIADO,
												  	   dataOperacao, usuario, banco);
		
		ResumoBaixaBoletosDTO resumoBaixaBoletos = new ResumoBaixaBoletosDTO();
		
		Date dataNovoMovimento = calendarioService.adicionarDiasUteis(dataOperacao, 1);
		
		try {
			if (arquivoPagamento != null && arquivoPagamento.getListaPagemento() != null) {
				
				for (PagamentoDTO pagamento : arquivoPagamento.getListaPagemento()) {
				
					this.baixarBoleto(TipoBaixaCobranca.AUTOMATICA, pagamento, usuario,
								 	  arquivoPagamento.getNomeArquivo(), politicaCobranca,
								 	  distribuidor, dataNovoMovimento, resumoBaixaBoletos, banco);
				}
				
				this.controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_SUCESSO,
						  								  	   dataOperacao, usuario, banco);
				
			} else {
				
				this.controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
															   dataOperacao, usuario, banco);
			}
			
			resumoBaixaBoletos.setNomeArquivo(arquivoPagamento.getNomeArquivo());
			resumoBaixaBoletos.setDataCompetencia(DateUtil.formatarDataPTBR(dataOperacao));
			resumoBaixaBoletos.setSomaPagamentos(arquivoPagamento.getSomaPagamentos());
			
		} catch (Exception e) {
			
			this.controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
														   dataOperacao, usuario, banco);
			
			if (e instanceof ValidacaoException) {
				
				throw new ValidacaoException(((ValidacaoException) e).getValidacao());
			
			} else {
			
				throw new ValidacaoException(TipoMensagem.WARNING, 
											 "Falha ao processar a baixa automática: " + e.getMessage());
			}
		}
	}
	
	@Override
	@Transactional
	public void baixarBoleto(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento, Usuario usuario,
							 String nomeArquivo, PoliticaCobranca politicaCobranca, Distribuidor distribuidor,
							 Date dataNovoMovimento, ResumoBaixaBoletosDTO resumoBaixaBoletos, Banco banco) {
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
			validarDadosEntradaBaixaAutomatica(pagamento);
			
		} else {
			
			validarDadosEntradaBaixaManual(pagamento);
		}
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		Boleto boleto = null;
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
			boleto = boletoRepository.obterPorNossoNumeroCompleto(pagamento.getNossoNumero(), null);
			
		} else {
		
			boleto = boletoRepository.obterPorNossoNumero(pagamento.getNossoNumero(), null);
		}		
		
		// Boleto não encontrado na base
		if (boleto == null) {
		
			if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
				baixarBoletoNaoEncontrado(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
										  dataOperacao, boleto, resumoBaixaBoletos, banco);
				
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Boleto não encontrado!");
			}
			
			return;
		}
			
		// Boleto já foi pago
		if (boleto.getStatusCobranca().equals(StatusCobranca.PAGO)) {
			
			if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
				baixarBoletoJaPago(tipoBaixaCobranca, pagamento, usuario, nomeArquivo, dataNovoMovimento,
								   dataOperacao, boleto, resumoBaixaBoletos, banco);
			
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "O boleto já foi pago!");
			}
			
			return;
		}
		
		Date dataVencimentoUtil = calendarioService.adicionarDiasUteis(boleto.getDataVencimento(), 0);
		
		Date dataPagamento = DateUtil.removerTimestamp(pagamento.getDataPagamento());
		
		// Boleto vencido
		if (dataVencimentoUtil.compareTo(dataPagamento) < 0) {
			
			if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
				baixarBoletoVencidoAutomatico(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
											  politicaCobranca, distribuidor, dataNovoMovimento,
											  dataOperacao, boleto, resumoBaixaBoletos, banco);
			
			} else {
				
				baixarBoletoVencidoManual(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
										  politicaCobranca, distribuidor, dataNovoMovimento,
										  dataOperacao, boleto, resumoBaixaBoletos, banco);
			}
			
			return;
		}
			
		// Boleto pago com valor correto
		if (pagamento.getValorPagamento().compareTo(boleto.getValor()) == 0) {
			
			baixarBoletoValorCorreto(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
									 dataOperacao, boleto, resumoBaixaBoletos, banco);
			
			return;
			
		} else if (pagamento.getValorPagamento().compareTo(boleto.getValor()) == 1) {
			
			if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
				// Boleto pago com valor acima
				baixarBoletoValorAcima(tipoBaixaCobranca, pagamento, usuario, nomeArquivo, politicaCobranca,
									   dataNovoMovimento, dataOperacao, boleto, resumoBaixaBoletos, banco);
			
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Boleto com valor divergente!");
			}
			
			return;
			
		} else {
		
			if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
				// Boleto pago com valor abaixo
				baixarBoletoValorAbaixo(tipoBaixaCobranca, pagamento, usuario, nomeArquivo, politicaCobranca,
										dataNovoMovimento, dataOperacao, boleto, resumoBaixaBoletos, banco);
			
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Boleto com valor divergente!");
			}
			
			return;
		}
	}
	
	private void baixarBoletoNaoEncontrado(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
										   Usuario usuario, String nomeArquivo, Date dataOperacao,
										   Boleto boleto, ResumoBaixaBoletosDTO resumoBaixaBoletos, Banco banco) {
		
		/*
		 * Gera baixa com status de pago, porém o nosso número
		 * referente ao pagamento não existe na base
		 * 
		 */
		
		gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_BOLETO_NAO_ENCONTRADO, boleto,
						   dataOperacao, nomeArquivo, pagamento, usuario, banco);
	}
	
	private void baixarBoletoJaPago(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
									Usuario usuario, String nomeArquivo, Date dataNovoMovimento,
									Date dataOperacao, Boleto boleto,
									ResumoBaixaBoletosDTO resumoBaixaBoletos, Banco banco) {
		
		/*
		 * Não baixa o boleto, gera baixa com status de boleto pago anteriormente
		 * e gera movimento de crédito
		 * 
		 */
		
		BaixaCobranca baixaCobranca =
			gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_BAIXA_JA_REALIZADA, boleto,
							   dataOperacao, nomeArquivo, pagamento, usuario, banco);

		movimentoFinanceiroCotaService
			.gerarMovimentosFinanceirosDebitoCredito(
					getMovimentoFinanceiroCotaDTO(boleto.getCota(),
												  GrupoMovimentoFinaceiro.CREDITO,
												  usuario, pagamento.getValorPagamento(),
												  dataOperacao, baixaCobranca,
												  dataNovoMovimento));
	}
	
	private void baixarBoletoVencidoAutomatico(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
									 		   Usuario usuario, String nomeArquivo, PoliticaCobranca politicaCobranca,
									 		   Distribuidor distribuidor, Date dataNovoMovimento,
									 		   Date dataOperacao, Boleto boleto,
									 		   ResumoBaixaBoletosDTO resumoBaixaBoletos, Banco banco) {
		
		BaixaCobranca baixaCobranca = null;
		
		/*
		 * Não baixa o boleto, gera baixa com status de não pago por divergência de data
		 * e gera movimento de crédito
		 * 
		 */
		
		if (politicaCobranca == null || !politicaCobranca.isAceitaBaixaPagamentoVencido()) {
			
			baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_DIVERGENCIA_DATA, boleto,
								 			   dataOperacao, nomeArquivo, pagamento, usuario, banco);
			
			movimentoFinanceiroCotaService
				.gerarMovimentosFinanceirosDebitoCredito(
					getMovimentoFinanceiroCotaDTO(boleto.getCota(),
												  GrupoMovimentoFinaceiro.CREDITO,
												  usuario, pagamento.getValorPagamento(),
												  dataOperacao, baixaCobranca,
												  dataNovoMovimento));
			
			return;
		}
		
		/*
		 * Baixa o boleto, gera baixa com status de pago com divergência de data,
		 * calcula multas e juros em cima do valor que deveria ser pago 
		 * e gera movimento de crédito ou débito se necessário
		 * 
		 */
		
		baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_DATA, boleto,
							 			   dataOperacao, nomeArquivo, pagamento, usuario, banco);

		efetivarBaixaCobranca(boleto, dataOperacao);
		
		BigDecimal valorJurosCalculado = 
				cobrancaService.calcularJuros(null, boleto.getCota(), distribuidor,
											  boleto.getValor(), boleto.getDataVencimento(),
											  pagamento.getDataPagamento());
		
		if (valorJurosCalculado.compareTo(BigDecimal.ZERO) == 1) {
			
			movimentoFinanceiroCotaService
				.gerarMovimentosFinanceirosDebitoCredito(
					getMovimentoFinanceiroCotaDTO(boleto.getCota(),
												  GrupoMovimentoFinaceiro.JUROS,
												  usuario, valorJurosCalculado,
												  dataOperacao, baixaCobranca,
												  dataNovoMovimento));
		}
		
		BigDecimal valorMultaCalculado = 
				cobrancaService.calcularMulta(null, boleto.getCota(), distribuidor,
											  boleto.getValor());
		
		if (valorMultaCalculado.compareTo(BigDecimal.ZERO) == 1) {
			
			movimentoFinanceiroCotaService
				.gerarMovimentosFinanceirosDebitoCredito(
					getMovimentoFinanceiroCotaDTO(boleto.getCota(),
												  GrupoMovimentoFinaceiro.MULTA,
												  usuario, valorMultaCalculado,
												  dataOperacao, baixaCobranca,
												  dataNovoMovimento));
		}
		
		BigDecimal diferencaValor = null;
		
		if (pagamento.getValorPagamento().compareTo(boleto.getValor()) == 1) {
		
			diferencaValor = pagamento.getValorPagamento().subtract(boleto.getValor());
			
			movimentoFinanceiroCotaService
				.gerarMovimentosFinanceirosDebitoCredito(
					getMovimentoFinanceiroCotaDTO(boleto.getCota(),
												  GrupoMovimentoFinaceiro.CREDITO,
												  usuario, diferencaValor,
												  dataOperacao, baixaCobranca,
												  dataNovoMovimento));
			
		} else if (pagamento.getValorPagamento().compareTo(boleto.getValor()) == -1) {
			
			diferencaValor = boleto.getValor().subtract(pagamento.getValorPagamento());
			
			movimentoFinanceiroCotaService
				.gerarMovimentosFinanceirosDebitoCredito(
					getMovimentoFinanceiroCotaDTO(boleto.getCota(),
											   	  GrupoMovimentoFinaceiro.DEBITO,
											   	  usuario, diferencaValor,
											   	  dataOperacao, baixaCobranca,
											   	  dataNovoMovimento));
		}
	}
	
	private void baixarBoletoVencidoManual(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
								 		   Usuario usuario, String nomeArquivo, PoliticaCobranca politicaCobranca,
								 		   Distribuidor distribuidor, Date dataNovoMovimento,
								 		   Date dataOperacao, Boleto boleto,
								 		   ResumoBaixaBoletosDTO resumoBaixaBoletos, Banco banco) {
		
		/*
		 * Baixa o boleto, calcula o valor pago considerando multas, juros e desconto, 
		 * e gera baixa cobrança com o valor atualizado
		 * 
		 */
		
		gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_DATA, boleto,
						   dataOperacao, nomeArquivo, pagamento, usuario, banco);

		efetivarBaixaCobranca(boleto, dataOperacao);
	}
	
	private void baixarBoletoValorCorreto(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
										  Usuario usuario, String nomeArquivo, Date dataOperacao,
										  Boleto boleto, ResumoBaixaBoletosDTO resumoBaixaBoletos, Banco banco) {
		
		// Baixa o boleto o gera baixa com status de pago
		
		gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO, boleto, dataOperacao,
						   nomeArquivo, pagamento, usuario, banco);
		
		efetivarBaixaCobranca(boleto, dataOperacao);
	}
	
	private void baixarBoletoValorAcima(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
										Usuario usuario, String nomeArquivo,  PoliticaCobranca politicaCobranca,
										Date dataNovoMovimento, Date dataOperacao, Boleto boleto,
			 							ResumoBaixaBoletosDTO resumoBaixaBoletos, Banco banco) {
		
		/*
		 * Verifica o parâmetro para pagamento a maior, não baixa o boleto, gera baixa
		 * com status de não pago por divergência de valor e gera movimento de crédito
		 */
		
		BaixaCobranca baixaCobranca = null;
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
		
			if (politicaCobranca == null || !politicaCobranca.isAceitaBaixaPagamentoMaior()) {
				
				baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR, boleto,
		 				   						   dataOperacao, nomeArquivo, pagamento, usuario, banco);
	
				movimentoFinanceiroCotaService
					.gerarMovimentosFinanceirosDebitoCredito(
						getMovimentoFinanceiroCotaDTO(boleto.getCota(),
							   						  GrupoMovimentoFinaceiro.CREDITO,
							   						  usuario, pagamento.getValorPagamento(),
							   						  dataOperacao, baixaCobranca,
							   						  dataNovoMovimento));
				
				return;
			}
		}
		
		/*
		 * Baixa o boleto, gera baixa com status de pago por divergência de valor
		 * e gera movimento de crédito da diferença
		 * 
		 */
		
		baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_VALOR, boleto,
										   dataOperacao, nomeArquivo, pagamento, usuario, banco);
		
		efetivarBaixaCobranca(boleto, dataOperacao);
		
		BigDecimal valorCredito = pagamento.getValorPagamento().subtract(boleto.getValor());
		
		movimentoFinanceiroCotaService
			.gerarMovimentosFinanceirosDebitoCredito(
				getMovimentoFinanceiroCotaDTO(boleto.getCota(),
											  GrupoMovimentoFinaceiro.CREDITO,
											  usuario, valorCredito,
											  dataOperacao, baixaCobranca,
											  dataNovoMovimento));
	}
	
	private void baixarBoletoValorAbaixo(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
										 Usuario usuario, String nomeArquivo, PoliticaCobranca politicaCobranca,
										 Date dataNovoMovimento, Date dataOperacao, Boleto boleto,
										 ResumoBaixaBoletosDTO resumoBaixaBoletos, Banco banco) {
		
		BaixaCobranca baixaCobranca = null;
		
		/*
		 * Verifica o parâmetro para pagamento a menor, não baixa o boleto, gera baixa
		 * com status de não pago por divergência de valor e gera movimento de crédito
		 * 
		 */
		
		if (politicaCobranca == null || !politicaCobranca.isAceitaBaixaPagamentoMenor()) {
			
			baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR, boleto,
					   						   dataOperacao, nomeArquivo, pagamento, usuario, banco);

			movimentoFinanceiroCotaService
				.gerarMovimentosFinanceirosDebitoCredito(
					getMovimentoFinanceiroCotaDTO(boleto.getCota(),
							   					  GrupoMovimentoFinaceiro.CREDITO,
							   					  usuario, pagamento.getValorPagamento(),
							   					  dataOperacao, baixaCobranca,
							   					  dataNovoMovimento));
			
			return;
		}
		
		/*
		 * Baixa o boleto, gera baixa com status de pago por divergência de valor
		 * e gera movimento de débito da diferença
		 * 
		 */

		baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_VALOR, boleto,
 				   						   dataOperacao, nomeArquivo, pagamento, usuario, banco);
		
		efetivarBaixaCobranca(boleto, dataOperacao);
		
		BigDecimal valorDebito = boleto.getValor().subtract(pagamento.getValorPagamento());
		
		movimentoFinanceiroCotaService
			.gerarMovimentosFinanceirosDebitoCredito(
				getMovimentoFinanceiroCotaDTO(boleto.getCota(),
											  GrupoMovimentoFinaceiro.DEBITO,
											  usuario, valorDebito,
											  dataOperacao, baixaCobranca,
											  dataNovoMovimento));
	}
	
	private void validarDadosEntradaBaixaAutomatica(PagamentoDTO pagamento) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (pagamento.getDataPagamento() == null) {
			
			listaMensagens.add("Data de pagmento é obrigatória!");
		}
		
		if (pagamento.getNossoNumero() == null) {

			listaMensagens.add("Nosso número é obrigatória!");
		}
		
		if (pagamento.getNumeroRegistro() == null) {

			listaMensagens.add("Número de registro do arquivo é obrigatório!");
		}
		
		if (pagamento.getValorPagamento() == null) {

			listaMensagens.add("Valor do pagmento é obrigatório!");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO();
			
			validacao.setTipoMensagem(TipoMensagem.WARNING);
			validacao.setListaMensagens(listaMensagens);
			
			throw new ValidacaoException(validacao);
		}
	}

	private void validarDadosEntradaBaixaManual(PagamentoDTO pagamento) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (pagamento.getDataPagamento() == null) {
			
			listaMensagens.add("Data de pagmento é obrigatória!");
		}
		
		if (pagamento.getNossoNumero() == null) {

			listaMensagens.add("Nosso número é obrigatória!");
		}
				
		if (pagamento.getValorPagamento() == null) {

			listaMensagens.add("Valor do pagmento é obrigatório!");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO();
			
			validacao.setTipoMensagem(TipoMensagem.WARNING);
			validacao.setListaMensagens(listaMensagens);
			
			throw new ValidacaoException(validacao);
		}
	}
	
	private BaixaCobranca gerarBaixaCobranca(TipoBaixaCobranca tipoBaixaCobranca, StatusBaixa statusBaixa,
											 Boleto boleto, Date dataBaixa, String nomeArquivo,
									  		 PagamentoDTO pagamento, Usuario usuario, Banco banco) {
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
			BaixaAutomatica baixaAutomatica = new BaixaAutomatica();
			
			baixaAutomatica.setDataBaixa(dataBaixa);
			baixaAutomatica.setValorPago(pagamento.getValorPagamento());
			baixaAutomatica.setCobranca(boleto);
			
			baixaAutomatica.setNomeArquivo(nomeArquivo);
			baixaAutomatica.setStatus(statusBaixa);
			baixaAutomatica.setNumeroRegistroArquivo(pagamento.getNumeroRegistro().intValue());
			baixaAutomatica.setNossoNumero(pagamento.getNossoNumero());
			baixaAutomatica.setBanco(banco);
			
			baixaCobrancaRepository.adicionar(baixaAutomatica);
			
			return baixaAutomatica;
			
		} else {
			
			BigDecimal valorPagamento = BigDecimal.ZERO;
			BigDecimal valorJuros = BigDecimal.ZERO;
			BigDecimal valorMulta = BigDecimal.ZERO;
			BigDecimal valorDesconto = BigDecimal.ZERO;
			
			if (pagamento.getValorPagamento() != null) {
				
				valorPagamento = pagamento.getValorPagamento();
			}
			
			if (pagamento.getValorJuros() != null) {
				
				valorJuros = pagamento.getValorJuros();
			}
			
			if (pagamento.getValorMulta() != null) {
				
				valorMulta = pagamento.getValorMulta();
			}
			
			if (pagamento.getValorDesconto() != null) {
						
				valorDesconto = pagamento.getValorDesconto();
			}
			
			BigDecimal valorCalculadoPagamento = 
				valorPagamento.add(valorJuros).add(valorMulta).subtract(valorDesconto);
				
			BaixaManual baixaManual = new BaixaManual();
			
			baixaManual.setDataBaixa(dataBaixa);
			baixaManual.setValorPago(valorCalculadoPagamento);
			baixaManual.setCobranca(boleto);
			
			baixaManual.setResponsavel(usuario);
			baixaManual.setValorJuros(pagamento.getValorJuros());
			baixaManual.setValorMulta(pagamento.getValorMulta());
			baixaManual.setValorDesconto(pagamento.getValorDesconto());
			
			baixaCobrancaRepository.adicionar(baixaManual);
			
			return baixaManual;
		}
	}
	
	private void efetivarBaixaCobranca(Boleto boleto, Date dataOperacao) {
		
		boleto.setDataPagamento(dataOperacao);
		boleto.setStatusCobranca(StatusCobranca.PAGO);
		boleto.getDivida().setStatus(StatusDivida.QUITADA);
		
		boletoRepository.alterar(boleto);
	}
	
	private MovimentoFinanceiroCotaDTO getMovimentoFinanceiroCotaDTO(Cota cota,
			GrupoMovimentoFinaceiro grupoMovimentoFinaceiro, Usuario usuario,
			BigDecimal valorPagamento, Date dataOperacao,
			BaixaCobranca baixaCobranca, Date dataNovoMovimento) {

		TipoMovimentoFinanceiro tipoMovimento = this.tipoMovimentoFinanceiroRepository
				.buscarTipoMovimentoFinanceiro(grupoMovimentoFinaceiro);

		MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();

		movimentoFinanceiroCotaDTO.setCota(cota);

		movimentoFinanceiroCotaDTO.setUsuario(usuario);

		movimentoFinanceiroCotaDTO.setValor(valorPagamento);

		movimentoFinanceiroCotaDTO.setBaixaCobranca(baixaCobranca);

		movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimento);

		movimentoFinanceiroCotaDTO.setDataOperacao(dataOperacao);
		
		movimentoFinanceiroCotaDTO.setDataCriacao(dataOperacao);

		movimentoFinanceiroCotaDTO.setDataVencimento(dataNovoMovimento);
		
		movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);

		return movimentoFinanceiroCotaDTO;
	}
	
	/**
	 * Método responsável por gerar corpo do boleto com os atributos definidos
	 * @param boleto
	 * @return GeradorBoleto: corpo do boleto carregado
	 */
	private CorpoBoleto geraCorpoBoleto(Boleto boleto){

		CorpoBoleto corpoBoleto = new CorpoBoleto();
		PessoaJuridica distribuidor = distribuidorService.obter().getJuridica();
		
		
		//DADOS DO CEDENTE
		corpoBoleto.setCedenteNome(distribuidor.getRazaoSocial());         
		corpoBoleto.setCedenteDocumento(distribuidor.getCnpj());

		
		//DADOS DO SACADO
		Pessoa pessoa = boleto.getCota().getPessoa();
		String nomeSacado="";
		String documentoSacado="";
		if (pessoa instanceof PessoaFisica){
			nomeSacado = ((PessoaFisica) pessoa).getNome();
			documentoSacado = ((PessoaFisica) pessoa).getCpf();
		}
		if (pessoa instanceof PessoaJuridica){
			nomeSacado = ((PessoaJuridica) pessoa).getNomeFantasia();
			documentoSacado = ((PessoaJuridica) pessoa).getCnpj();
		}
		corpoBoleto.setSacadoNome(nomeSacado);          
		corpoBoleto.setSacadoDocumento(documentoSacado); 

		
		//ENDERECO DO SACADO
		Endereco endereco = null;
		Set<EnderecoCota> enderecosCota = boleto.getCota().getEnderecos();
		for(EnderecoCota enderecoCota : enderecosCota){
			endereco = enderecoCota.getEndereco();
			if (enderecoCota.getTipoEndereco() == TipoEndereco.COBRANCA){
			    break;
			}
		}
		if (endereco!=null){
			corpoBoleto.setEnderecoSacadoUf(endereco.getUf());            
			corpoBoleto.setEnderecoSacadoLocalidade(endereco.getCidade());     
			corpoBoleto.setEnderecoSacadoCep(endereco.getCep());         
			corpoBoleto.setEnderecoSacadoBairro(endereco.getBairro()); 
			corpoBoleto.setEnderecoSacadoLogradouro(endereco.getLogradouro()); 
			corpoBoleto.setEnderecoSacadoNumero(endereco.getNumero()); 
		}
		else{
			corpoBoleto.setEnderecoSacadoUf("SP");
			corpoBoleto.setEnderecoSacadoLocalidade("Endereco nao cadastrado.");
			corpoBoleto.setEnderecoSacadoCep("");
			corpoBoleto.setEnderecoSacadoBairro("");
			corpoBoleto.setEnderecoSacadoLogradouro("");
			corpoBoleto.setEnderecoSacadoNumero("");
		}

		
		//INFORMACOES DA CONTA(BANCO)
        String contaNumero=boleto.getBanco().getConta().toString();
        String contaNumeroDocumento=boleto.getNossoNumero();
        corpoBoleto.setContaNumeroBanco(boleto.getBanco().getNumeroBanco());                  
        corpoBoleto.setContaCarteira(boleto.getBanco().getCarteira());
       
        List<TipoCobranca> tiposCobranca = Arrays.asList(TipoCobranca.BOLETO, TipoCobranca.BOLETO_EM_BRANCO);
        
        List<PoliticaCobranca> politicasCobranca = politicaCobrancaRepository.obterPoliticasCobranca(tiposCobranca);
        
        for (PoliticaCobranca politicaCobranca : politicasCobranca) {
        	
        	FormaCobranca formaCobranca = politicaCobranca.getFormaCobranca();
        	
        	if (formaCobranca.getBanco().getApelido().equals(boleto.getBanco().getApelido())) {
        		
        		if (formaCobranca.getFormaCobrancaBoleto() != null) {
        		
        			corpoBoleto.setContaTipoDeCobranca(formaCobranca.getFormaCobrancaBoleto().toString());
        			
        			break;
        		}
        	}
        }
        
        corpoBoleto.setContaAgencia(boleto.getBanco().getAgencia().intValue());    
        corpoBoleto.setContaNumero(Integer.parseInt(contaNumero));   
        
         
        //INFORMACOES DO TITULO
        corpoBoleto.setTituloNumeroDoDocumento(contaNumeroDocumento);                      
        corpoBoleto.setTituloNossoNumero(boleto.getNossoNumero());                    
        
        
        //PARAMETROS ?
        corpoBoleto.setTituloDigitoDoNossoNumero(boleto.getDigitoNossoNumero());  
        corpoBoleto.setTituloTipoDeDocumento("DM_DUPLICATA_MERCANTIL");
        corpoBoleto.setTituloAceite("A");
        corpoBoleto.setTituloTipoIdentificadorCNR("COM_VENCIMENTO");
        
        corpoBoleto.setTituloValor(boleto.getValor());   
        corpoBoleto.setTituloDataDoDocumento(boleto.getDataEmissao());   
        corpoBoleto.setTituloDataDoVencimento(boleto.getDataVencimento());  
        corpoBoleto.setTituloDesconto(BigDecimal.ZERO);
        corpoBoleto.setTituloDeducao(BigDecimal.ZERO);
        corpoBoleto.setTituloMora(BigDecimal.ZERO);
        corpoBoleto.setTituloAcrecimo(BigDecimal.ZERO);
        corpoBoleto.setTituloValorCobrado(BigDecimal.ZERO);
        

        //INFORMAÇOES DO BOLETO
        //PARAMETROS ?
        corpoBoleto.setBoletoLocalPagamento("Local do pagamento.");
        corpoBoleto.setBoletoInstrucaoAoSacado("Instrução so Sacado");
        corpoBoleto.setBoletoInstrucao1(boleto.getBanco().getInstrucoes());
        corpoBoleto.setBoletoInstrucao2("");
        corpoBoleto.setBoletoInstrucao3("");
        corpoBoleto.setBoletoInstrucao4("");
        corpoBoleto.setBoletoInstrucao5("");
        corpoBoleto.setBoletoInstrucao6("");
        corpoBoleto.setBoletoInstrucao7("");
        corpoBoleto.setBoletoInstrucao8("");
        
        return corpoBoleto;
	}
	
	/**
	 * 
	 * @param boleto
	 * @return f: Boleto PDF em File.
	 * @throws IOException
	 */
	private byte[]  gerarAnexoBoleto(Boleto boleto) throws IOException {
		GeradorBoleto geradorBoleto = new GeradorBoleto(this.geraCorpoBoleto(boleto));
		byte[] b = geradorBoleto.getBytePdf();
        return b;
	}
	
	/**
	 * Metodo responsavel por enviar boleto por email em formato PDF
	 * @param nossoNumero
	 * @throws erro ao enviar
	 */
	@Override
	@Transactional(readOnly=true)
	public void enviarBoletoEmail(String nossoNumero) {
		try{
			
			Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero,null);
			
			byte[] anexo = this.gerarAnexoBoleto(boleto);
			String[] destinatarios = new String[]{boleto.getCota().getPessoa().getEmail()};
			
			PoliticaCobranca politicaPrincipal = this.politicaCobrancaService.obterPoliticaCobrancaPrincipal();
	
			String assunto=(politicaPrincipal!=null?politicaPrincipal.getAssuntoEmailCobranca():"");
			String mensagem=(politicaPrincipal!=null?politicaPrincipal.getMensagemEmailCobranca():"");
			email.enviar(assunto, 
					     mensagem, 
					     destinatarios, 
					     new AnexoEmail("Boleto-"+nossoNumero, anexo,TipoAnexo.PDF));
		}
		catch(Exception e){
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro no envio.");
		}
	}
	
	/**
	 * Método responsável por gerar impressao em formato PDF
	 * @param nossoNumero
	 * @return b: Boleto PDF em Array de bytes
	 * @throws IOException
	 */
	@Override
	@Transactional
	public byte[] gerarImpressaoBoleto(String nossoNumero) throws IOException {
		
		Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero,null);
		GeradorBoleto geradorBoleto = new GeradorBoleto(this.geraCorpoBoleto(boleto)) ;
		byte[] b = geradorBoleto.getBytePdf();
		cobrancaRepository.atualizarVias(boleto); 
        return b;
	}
	
	/**
	 * Método responsável pela busca de dados referentes à cobrança
	 * @param nossoNumero
	 * @return CobrancaVO: dados da cobrança
	 */
	@Override
	@Transactional(readOnly=true)
	public byte[] gerarImpressaoBoletos(List<String> nossoNumeros) throws IOException {
		
		List<CorpoBoleto> corpos = new ArrayList<CorpoBoleto>();
		
		Boleto boleto = null;
		
		for(String nossoNumero  : nossoNumeros){
			
			boleto = boletoRepository.obterPorNossoNumero(nossoNumero,null);
			
			if(boleto!= null){
				corpos.add(this.geraCorpoBoleto(boleto));
			}
		}
		
		if(!corpos.isEmpty()){
			GeradorBoleto geradorBoleto = new GeradorBoleto(corpos) ;
			byte[] b = geradorBoleto.getByteGroupPdf();
	        return b;
		}
		return null;
	}

	/**
	 * Método responsável por obter os dados de uma cobrança
	 * @param nossoNumero
	 * @return CobrancaVO: dados da cobrança
	 */
	@Override
	@Transactional(readOnly=true)
	public CobrancaVO obterDadosBoletoPorNossoNumero(String nossoNumero) {
		
		CobrancaVO cobrancaVO = null;
		
		Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero,null);
		if (boleto!=null){
		    cobrancaVO = this.cobrancaService.obterDadosCobranca(boleto.getId());
		}
		
		return cobrancaVO;
	}

	/**
	 * Incrementa o valor de vias
	 * @param nossoNumero
	 */
	@Override
	@Transactional
	public void incrementarVia(String... nossoNumero) {
		cobrancaRepository.incrementarVia(nossoNumero);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<DetalheBaixaBoletoDTO> obterBoletosPrevistos(FiltroDetalheBaixaBoletoDTO filtro) {

		this.validarFiltroBaixaBoleto(filtro);
		
		return this.boletoRepository.obterBoletosPrevistos(filtro);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<DetalheBaixaBoletoDTO> obterBoletosBaixados(FiltroDetalheBaixaBoletoDTO filtro) {
		
		return this.boletoRepository.obterBoletosBaixados(filtro);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<DetalheBaixaBoletoDTO> obterBoletosRejeitados(FiltroDetalheBaixaBoletoDTO filtro) {

		this.validarFiltroBaixaBoleto(filtro);
		
		return this.boletoRepository.obterBoletosRejeitados(filtro);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<DetalheBaixaBoletoDTO> obterBoletosBaixadosComDivergencia(FiltroDetalheBaixaBoletoDTO filtro) {

		this.validarFiltroBaixaBoleto(filtro);
		
		return this.boletoRepository.obterBoletosBaixadosComDivergencia(filtro);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<DetalheBaixaBoletoDTO> obterBoletosInadimplentes(FiltroDetalheBaixaBoletoDTO filtro) {
		
		return this.boletoRepository.obterBoletosInadimplentes(filtro);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public List<DetalheBaixaBoletoDTO> obterTotalBancario(FiltroDetalheBaixaBoletoDTO filtro) {

		this.validarFiltroBaixaBoleto(filtro);
		
		return this.boletoRepository.obterTotalBancario(filtro);
	}
	
	private void validarFiltroBaixaBoleto(FiltroDetalheBaixaBoletoDTO filtro) {
		
		if (filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não pode ser nulo.");
		}
		
		if (filtro.getData() == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Uma data deve ser informada para a pesquisa.");
		}
		
		if (filtro.getDataVencimento() == null) {
	
			throw new ValidacaoException(TipoMensagem.WARNING, "A data de vencimento deve ser informada.");
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long obterQuantidadeBoletosPrevistos(FiltroDetalheBaixaBoletoDTO filtro) {

		return this.boletoRepository.obterQuantidadeBoletosPrevistos(filtro.getData());
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long obterQuantidadeBoletosBaixados(FiltroDetalheBaixaBoletoDTO filtro) {

		return this.boletoRepository.obterQuantidadeBoletosBaixados(filtro.getData());
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long obterQuantidadeBoletosRejeitados(FiltroDetalheBaixaBoletoDTO filtro) {

		return this.boletoRepository.obterQuantidadeBoletosRejeitados(filtro.getData());
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long obterQuantidadeBoletosBaixadosComDivergencia(FiltroDetalheBaixaBoletoDTO filtro) {

		return this.boletoRepository.obterQuantidadeBoletosBaixadosComDivergencia(filtro.getData());
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long obterQuantidadeBoletosInadimplentes(FiltroDetalheBaixaBoletoDTO filtro) {

		return this.boletoRepository.obterQuantidadeBoletosInadimplentes(filtro.getDataVencimento());
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long obterQuantidadeTotalBancario(FiltroDetalheBaixaBoletoDTO filtro) {

		return this.boletoRepository.obterQuantidadeTotalBancario(filtro.getData());
	}

}
