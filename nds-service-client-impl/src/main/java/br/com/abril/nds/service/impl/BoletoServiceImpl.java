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
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoFornecedor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaCobrancaBoleto;
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
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
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
import br.com.abril.nds.repository.DistribuidorRepository;
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
	protected DistribuidorRepository distribuidorRepository;
	
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
		
		resumoBaixaBoletos.setQuantidadeInadimplentes(
			this.boletoRepository.obterQuantidadeBoletosInadimplentes(data).intValue());
		
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
							  			BigDecimal valorFinanceiro, Usuario usuario, Date dataPagamento) {
		
		Date dataOperacao = this.distribuidorRepository.obterDataOperacaoDistribuidor();
		
		Banco banco = this.bancoRepository.obterBanco(arquivoPagamento.getCodigoBanco(),
													  arquivoPagamento.getNumeroAgencia(),
													  arquivoPagamento.getNumeroConta());
		
		if (banco == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, 
				"Banco não encontrado!");
		}
		
		ControleBaixaBancaria controleBaixa =
			this.controleBaixaRepository.obterControleBaixaBancaria(dataPagamento, banco);
		
		if (controleBaixa != null
				&& controleBaixa.getStatus().equals(StatusControle.CONCLUIDO_SUCESSO)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, 
				"Já foi realizada baixa automática para a data de pagamento informada e banco " + banco.getNome() + "!");
		}
		
		if (valorFinanceiro == null || arquivoPagamento.getSomaPagamentos() == null
				|| valorFinanceiro.compareTo(arquivoPagamento.getSomaPagamentos()) != 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, 
				"Valor financeiro inválido! A soma dos valores dos boletos pagos " +
				"deve ser igual ao valor informado!");
		}
				
		this.controleBaixaService.alterarControleBaixa(StatusControle.INICIADO,
												  	   dataOperacao, dataPagamento, usuario, banco);
		
		ResumoBaixaBoletosDTO resumoBaixaBoletos = new ResumoBaixaBoletosDTO();
		
		Date dataNovoMovimento = calendarioService.adicionarDiasUteis(dataOperacao, 1);
		
		try {
			if (arquivoPagamento != null && arquivoPagamento.getListaPagemento() != null) {
				
				for (PagamentoDTO pagamento : arquivoPagamento.getListaPagemento()) {
				
					this.baixarBoleto(TipoBaixaCobranca.AUTOMATICA, pagamento, usuario,
								 	  arquivoPagamento.getNomeArquivo(),
								 	  dataNovoMovimento, resumoBaixaBoletos, banco,
								 	  dataPagamento);
				}
				
				this.controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_SUCESSO,
						  								  	   dataOperacao, dataPagamento, usuario, banco);
				
			} else {
				
				this.controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
															   dataOperacao, dataPagamento, usuario, banco);
			}
			
			resumoBaixaBoletos.setNomeArquivo(arquivoPagamento.getNomeArquivo());
			resumoBaixaBoletos.setDataCompetencia(DateUtil.formatarDataPTBR(dataOperacao));
			resumoBaixaBoletos.setSomaPagamentos(arquivoPagamento.getSomaPagamentos());
			
		} catch (Exception e) {
			
			this.controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
														   dataOperacao, dataPagamento, usuario, banco);
			
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
							 String nomeArquivo,
							 Date dataNovoMovimento, ResumoBaixaBoletosDTO resumoBaixaBoletos,
							 Banco banco, Date dataPagamento) {
		
		Boleto boleto = this.gerarBaixaBoleto(
			tipoBaixaCobranca, pagamento, usuario, nomeArquivo, 
			this.distribuidorRepository.obterDataOperacaoDistribuidor(),
			dataNovoMovimento, resumoBaixaBoletos, banco, dataPagamento);
	
		if (boleto != null) {
		
			boleto.setTipoBaixa(tipoBaixaCobranca);
			
			boletoRepository.merge(boleto);
		}
	}
	
	private Boleto gerarBaixaBoleto(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento, Usuario usuario,
							 String nomeArquivo, Date dataOperacao,
							 Date dataNovoMovimento, ResumoBaixaBoletosDTO resumoBaixaBoletos,
							 Banco banco, Date dataPagamento) {
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
			validarDadosEntradaBaixaAutomatica(pagamento);
			
		} else {
			
			validarDadosEntradaBaixaManual(pagamento);
		}
		
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
										  dataOperacao, boleto, resumoBaixaBoletos, banco, dataPagamento);
				
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Boleto não encontrado!");
			}
			
			return boleto;
		}
			
		// Boleto já foi pago
		if (boleto.getStatusCobranca().equals(StatusCobranca.PAGO)) {
			
			if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
				baixarBoletoJaPago(tipoBaixaCobranca, pagamento, usuario, nomeArquivo, dataNovoMovimento,
								   dataOperacao, boleto, resumoBaixaBoletos, banco, dataPagamento);
			
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "O boleto já foi pago!");
			}
			
			return boleto;
		}
		
		Date dataVencimentoUtil = calendarioService.adicionarDiasUteis(boleto.getDataVencimento(), 0);
		
		dataPagamento = DateUtil.removerTimestamp(dataPagamento);
		
		// Boleto vencido
		if (dataVencimentoUtil.compareTo(dataPagamento) < 0) {
			
			if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
				baixarBoletoVencidoAutomatico(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
											  dataNovoMovimento, dataOperacao,
											  boleto, resumoBaixaBoletos, banco, dataPagamento);
			
			} else {
				
				baixarBoletoVencidoManual(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
										  dataNovoMovimento, dataOperacao,
										  boleto, resumoBaixaBoletos, banco, dataPagamento);
			}
			
			return boleto;
		}
			
		// Boleto pago com valor correto
		if (pagamento.getValorPagamento().compareTo(boleto.getValor()) == 0) {
			
			baixarBoletoValorCorreto(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
									 dataOperacao, boleto, resumoBaixaBoletos, banco, dataPagamento);
			
			return boleto;
			
		} else if (pagamento.getValorPagamento().compareTo(boleto.getValor()) == 1) {
			
			if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
				// Boleto pago com valor acima
				baixarBoletoValorAcima(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
									   dataNovoMovimento, dataOperacao, boleto, resumoBaixaBoletos,
									   banco, dataPagamento);
			
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Boleto com valor divergente!");
			}
			
			return boleto;
			
		} else {
		
			if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
				// Boleto pago com valor abaixo
				baixarBoletoValorAbaixo(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
										dataNovoMovimento, dataOperacao, boleto, resumoBaixaBoletos,
										banco, dataPagamento);
			
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Boleto com valor divergente!");
			}
			
			return boleto;
		}
	}
	
	private void baixarBoletoNaoEncontrado(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
										   Usuario usuario, String nomeArquivo, Date dataOperacao,
										   Boleto boleto, ResumoBaixaBoletosDTO resumoBaixaBoletos,
										   Banco banco, Date dataPagamento) {
		
		/*
		 * Gera baixa com status de pago, porém o nosso número
		 * referente ao pagamento não existe na base
		 * 
		 */
		
		gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_BOLETO_NAO_ENCONTRADO, boleto,
						   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);
	}
	
	private void baixarBoletoJaPago(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
									Usuario usuario, String nomeArquivo, Date dataNovoMovimento,
									Date dataOperacao, Boleto boleto, ResumoBaixaBoletosDTO resumoBaixaBoletos,
									Banco banco, Date dataPagamento) {
		
		/*
		 * Não baixa o boleto, gera baixa com status de boleto pago anteriormente
		 * e gera movimento de crédito
		 * 
		 */
		
		BaixaCobranca baixaCobranca =
			gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_BAIXA_JA_REALIZADA, boleto,
							   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);

		movimentoFinanceiroCotaService
			.gerarMovimentosFinanceirosDebitoCredito(
					getMovimentoFinanceiroCotaDTO(boleto.getCota(),
												  GrupoMovimentoFinaceiro.CREDITO,
												  usuario, pagamento.getValorPagamento(),
												  dataOperacao, baixaCobranca,
												  dataNovoMovimento));
	}
	
	private void baixarBoletoVencidoAutomatico(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
									 		   Usuario usuario, String nomeArquivo,
									 		   Date dataNovoMovimento,
									 		   Date dataOperacao, Boleto boleto,
									 		   ResumoBaixaBoletosDTO resumoBaixaBoletos,
									 		   Banco banco, Date dataPagamento) {
		
		BaixaCobranca baixaCobranca = null;
		
		/*
		 * Não baixa o boleto, gera baixa com status de não pago por divergência de data
		 * e gera movimento de crédito
		 * 
		 */
		
		if (!this.distribuidorRepository.aceitaBaixaPagamentoVencido()) {
			
			baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_DIVERGENCIA_DATA, boleto,
								 			   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);
			
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
							 			   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);

		efetivarBaixaCobranca(boleto, dataOperacao);
		
		BigDecimal valorJurosCalculado = 
				cobrancaService.calcularJuros(null, boleto.getCota(),
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
				cobrancaService.calcularMulta(null, boleto.getCota(),
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
								 		   Usuario usuario, String nomeArquivo,
								 		   Date dataNovoMovimento,
								 		   Date dataOperacao, Boleto boleto,
								 		   ResumoBaixaBoletosDTO resumoBaixaBoletos,
								 		   Banco banco, Date dataPagamento) {
		
		/*
		 * Baixa o boleto, calcula o valor pago considerando multas, juros e desconto, 
		 * e gera baixa cobrança com o valor atualizado
		 * 
		 */
		
		gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_DATA, boleto,
						   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);

		efetivarBaixaCobranca(boleto, dataOperacao);
	}
	
	private void baixarBoletoValorCorreto(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
										  Usuario usuario, String nomeArquivo, Date dataOperacao,
										  Boleto boleto, ResumoBaixaBoletosDTO resumoBaixaBoletos,
										  Banco banco, Date dataPagamento) {
		
		// Baixa o boleto o gera baixa com status de pago
		
		gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO, boleto, dataOperacao,
						   nomeArquivo, pagamento, usuario, banco, dataPagamento);
		
		efetivarBaixaCobranca(boleto, dataOperacao);
	}
	
	private void baixarBoletoValorAcima(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
										Usuario usuario, String nomeArquivo,
										Date dataNovoMovimento, Date dataOperacao, Boleto boleto,
			 							ResumoBaixaBoletosDTO resumoBaixaBoletos,
			 							Banco banco, Date dataPagamento) {
		
		/*
		 * Verifica o parâmetro para pagamento a maior, não baixa o boleto, gera baixa
		 * com status de não pago por divergência de valor e gera movimento de crédito
		 */
		
		BaixaCobranca baixaCobranca = null;
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
		
			if (!this.distribuidorRepository.aceitaBaixaPagamentoMaior()) {
				
				baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR, boleto,
		 				   						   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);
	
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
										   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);
		
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
										 Usuario usuario, String nomeArquivo,
										 Date dataNovoMovimento, Date dataOperacao, Boleto boleto,
										 ResumoBaixaBoletosDTO resumoBaixaBoletos, Banco banco,
										 Date dataPagamento) {
		
		BaixaCobranca baixaCobranca = null;
		
		/*
		 * Verifica o parâmetro para pagamento a menor, não baixa o boleto, gera baixa
		 * com status de não pago por divergência de valor e gera movimento de crédito
		 * 
		 */
		
		if (!this.distribuidorRepository.aceitaBaixaPagamentoMenor()) {
			
			baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR, boleto,
					   						   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);

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
 				   						   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);
		
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
									  		 PagamentoDTO pagamento, Usuario usuario, Banco banco, Date dataPagamento) {
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
			BaixaAutomatica baixaAutomatica = new BaixaAutomatica();
			
			baixaAutomatica.setDataBaixa(dataBaixa);
			baixaAutomatica.setDataPagamento(dataPagamento);
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
			baixaManual.setDataPagamento(dataPagamento);
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
	
	
	private CorpoBoleto gerarCorpoBoletoCota(Boleto boleto){
		
		String nossoNumero = boleto.getNossoNumero();
		String digitoNossoNumero = boleto.getDigitoNossoNumero();
		BigDecimal valor = boleto.getValor();
		Banco banco = boleto.getBanco();
		Date dataEmissao = boleto.getDataEmissao();
		Date dataVencimento = boleto.getDataVencimento();
		Pessoa pessoaCedente = this.distribuidorRepository.juridica(); 
		Pessoa pessoaSacado = boleto.getCota().getPessoa();
		
		Endereco endereco = null;
		
		Set<EnderecoCota> enderecosCota = boleto.getCota().getEnderecos();
		
		for(EnderecoCota enderecoCota : enderecosCota){
			
			endereco = enderecoCota.getEndereco();
			
			if (enderecoCota.getTipoEndereco() == TipoEndereco.COBRANCA){
				break;
			}
		
		}
		
		return geraCorpoBoleto(
				nossoNumero, 
				digitoNossoNumero, 
				valor, 
				banco, 
				dataEmissao, 
				dataVencimento, 
				pessoaCedente, 
				pessoaSacado, 
				endereco, 
				boleto.getTipoCobranca());
		
	}
	
	private CorpoBoleto gerarCorpoBoletoDistribuidor(BoletoDistribuidor boleto) {
		
		String nossoNumero = boleto.getNossoNumeroDistribuidor();
		String digitoNossoNumero = boleto.getDigitoNossoNumeroDistribuidor();
		BigDecimal valor = boleto.getValor();
		Banco banco = boleto.getBanco();
		Date dataEmissao = boleto.getDataEmissao();
		Date dataVencimento = boleto.getDataVencimento();
		Pessoa pessoaCedente = this.distribuidorRepository.juridica();
		Pessoa pessoaSacado = boleto.getFornecedor().getJuridica();
		
		Endereco endereco = null;
		
		Set<EnderecoFornecedor> enderecosFornecedor = boleto.getFornecedor().getEnderecos();
		
		for(EnderecoFornecedor enderecoFornecedor : enderecosFornecedor){
			
			endereco = enderecoFornecedor.getEndereco();
			
			if (enderecoFornecedor.getTipoEndereco() == TipoEndereco.COBRANCA){
				break;
			}
		
		}
		
		return geraCorpoBoleto(
				nossoNumero, 
				digitoNossoNumero, 
				valor, 
				banco, 
				dataEmissao, 
				dataVencimento, 
				pessoaCedente, 
				pessoaSacado, 
				endereco,
				boleto.getTipoCobranca()
				);
		
	}
	
	
	/**
	 * Método responsável por gerar corpo do boleto com os atributos definidos
	 * @param boleto
	 * @return GeradorBoleto: corpo do boleto carregado
	 */
	private CorpoBoleto geraCorpoBoleto(
			String nossoNumero,
			String digitoNossoNumero,
			BigDecimal valor,
			Banco banco,
			Date dataEmissao,
			Date dataVencimento,
			Pessoa pessoaCedente, 
			Pessoa pessoaSacado,
			Endereco enderecoSacado,
			TipoCobranca tipoCobranca
			
			){

		CorpoBoleto corpoBoleto = new CorpoBoleto();
		
		String nomeCedente = "";
		String documentoCedente = "";
		
		if(pessoaCedente instanceof PessoaJuridica) {
			
			nomeCedente = ((PessoaJuridica)pessoaCedente).getRazaoSocial();
			documentoCedente = ((PessoaJuridica)pessoaCedente).getCnpj();
			
			
		} else {
			
			nomeCedente = ((PessoaFisica)pessoaCedente).getNome();
			documentoCedente = ((PessoaFisica)pessoaCedente).getCpf();
			
		}
		
		
		//DADOS DO CEDENTE
		corpoBoleto.setCodigoCedente(Integer.valueOf(banco.getCodigoCedente()));
		corpoBoleto.setCedenteNome(nomeCedente);         
		corpoBoleto.setCedenteDocumento(documentoCedente);

		
		//DADOS DO SACADO
		
		String nomeSacado="";
		
		String documentoSacado="";
		
		if (pessoaSacado instanceof PessoaFisica){
			nomeSacado = ((PessoaFisica) pessoaSacado).getNome();
			documentoSacado = ((PessoaFisica) pessoaSacado).getCpf();
		}
		if (pessoaSacado instanceof PessoaJuridica){
			nomeSacado = ((PessoaJuridica) pessoaSacado).getRazaoSocial();
			documentoSacado = ((PessoaJuridica) pessoaSacado).getCnpj();
		}
		corpoBoleto.setSacadoNome(nomeSacado);          
		corpoBoleto.setSacadoDocumento(documentoSacado); 

		
		//ENDERECO DO SACADO
		
		if (enderecoSacado!=null){
			corpoBoleto.setEnderecoSacadoUf(enderecoSacado.getUf());            
			corpoBoleto.setEnderecoSacadoLocalidade(enderecoSacado.getCidade());     
			corpoBoleto.setEnderecoSacadoCep(enderecoSacado.getCep());         
			corpoBoleto.setEnderecoSacadoBairro(enderecoSacado.getBairro()); 
			corpoBoleto.setEnderecoSacadoLogradouro(enderecoSacado.getLogradouro()); 
			corpoBoleto.setEnderecoSacadoNumero(enderecoSacado.getNumero()); 
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
        String contaNumero=banco.getConta().toString();
        
        corpoBoleto.setContaNumeroBanco(banco.getNumeroBanco());                  
        
        corpoBoleto.setContaCarteira(banco.getCarteira());
       
        List<TipoCobranca> tiposCobranca = Arrays.asList(TipoCobranca.BOLETO, TipoCobranca.BOLETO_EM_BRANCO);
        
        List<PoliticaCobranca> politicasCobranca = politicaCobrancaRepository.obterPoliticasCobranca(tiposCobranca);
        
        for (PoliticaCobranca politicaCobranca : politicasCobranca) {
        	
        	FormaCobranca formaCobranca = politicaCobranca.getFormaCobranca();
        	
        	if (formaCobranca.getBanco().getApelido().equals(banco.getApelido())) {
        		
        		if (formaCobranca.getFormaCobrancaBoleto() != null) {
        		
        			corpoBoleto.setContaTipoDeCobranca(formaCobranca.getFormaCobrancaBoleto().toString());
        			
        			break;
        		}
        	}
        }
        
        if (corpoBoleto.getContaTipoDeCobranca() == null || corpoBoleto.getContaTipoDeCobranca().isEmpty()){
        	
        	corpoBoleto.setContaTipoDeCobranca(FormaCobrancaBoleto.SEM_REGISTRO.name());
        }
        
        corpoBoleto.setContaAgencia(banco.getAgencia().intValue());
        corpoBoleto.setDigitoAgencia(banco.getDvAgencia());    
        
        corpoBoleto.setContaNumero(Integer.parseInt(contaNumero));   
        
         
        //INFORMACOES DO TITULO
        corpoBoleto.setTituloNumeroDoDocumento(nossoNumero);                      
        corpoBoleto.setTituloNossoNumero(nossoNumero);                    
        
        
        //PARAMETROS ?
        corpoBoleto.setTituloDigitoDoNossoNumero(digitoNossoNumero);  
        corpoBoleto.setTituloTipoDeDocumento("DM_DUPLICATA_MERCANTIL");
        corpoBoleto.setTituloAceite("A");
        corpoBoleto.setTituloTipoIdentificadorCNR("COM_VENCIMENTO");
        
        corpoBoleto.setTituloValor(valor);   
        corpoBoleto.setTituloDataDoDocumento(dataEmissao);   
        corpoBoleto.setTituloDataDoVencimento(dataVencimento);  
        corpoBoleto.setTituloDesconto(BigDecimal.ZERO);
        corpoBoleto.setTituloDeducao(BigDecimal.ZERO);
        corpoBoleto.setTituloMora(BigDecimal.ZERO);
        corpoBoleto.setTituloAcrecimo(BigDecimal.ZERO);
        corpoBoleto.setTituloValorCobrado(BigDecimal.ZERO);
        

        //INFORMAÇOES DO BOLETO
        //PARAMETROS ?
        corpoBoleto.setBoletoLocalPagamento("Local do pagamento.");
        corpoBoleto.setBoletoInstrucaoAoSacado("Instrução so Sacado");
        corpoBoleto.setBoletoInstrucao1(banco.getInstrucoes());
        corpoBoleto.setBoletoInstrucao2("");
        corpoBoleto.setBoletoInstrucao3("");
        corpoBoleto.setBoletoInstrucao4("");
        corpoBoleto.setBoletoInstrucao5("");
        corpoBoleto.setBoletoInstrucao6("");
        corpoBoleto.setBoletoInstrucao7("");
        corpoBoleto.setBoletoInstrucao8("");
        
        //BOLETO EM BRANCO
        corpoBoleto.setBoletoSemValor(tipoCobranca.equals(TipoCobranca.BOLETO_EM_BRANCO));
        
        return corpoBoleto;
	}
	
	
	
	/**
	 * 
	 * @param boleto
	 * @return f: Boleto PDF em File.
	 * @throws IOException
	 */
	private byte[]  gerarAnexoBoleto(Boleto boleto) throws IOException {
		
		GeradorBoleto geradorBoleto = new GeradorBoleto(this.gerarCorpoBoletoCota(boleto));
		
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
			
			if(boleto.getCota().getPessoa().getEmail()==null)
				throw new ValidacaoException(TipoMensagem.WARNING, "Cota não possui email cadastrado.");
			
			String[] destinatarios = new String[]{boleto.getCota().getPessoa().getEmail()};
						
			String assunto = this.distribuidorRepository.assuntoEmailCobranca();
			String mensagem = this.distribuidorRepository.mensagemEmailCobranca();
			
			email.enviar(assunto == null ? "" : assunto, 
					     mensagem == null ? "" : mensagem, 
					     destinatarios, 
					     new AnexoEmail("Boleto-"+nossoNumero, anexo,TipoAnexo.PDF),
					     true);
		} catch(ValidacaoException e){
			throw e;
		}catch(Exception e){
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
		
		GeradorBoleto geradorBoleto = new GeradorBoleto(this.gerarCorpoBoletoCota(boleto));
		
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
				corpos.add(this.gerarCorpoBoletoCota(boleto));
			}
		}
		
		if(!corpos.isEmpty()){
			GeradorBoleto geradorBoleto = new GeradorBoleto(corpos) ;
			byte[] b = geradorBoleto.getByteGroupPdf();
	        return b;
		}
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public byte[] gerarImpressaoBoletosDistribuidor(List<BoletoDistribuidor> listaBoletoDistribuidor) throws IOException {
		
		List<CorpoBoleto> corpos = new ArrayList<CorpoBoleto>();
		
		for(BoletoDistribuidor boletoDistribuidor  : listaBoletoDistribuidor){
				corpos.add(this.gerarCorpoBoletoDistribuidor(boletoDistribuidor));
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

		return this.boletoRepository.obterQuantidadeBoletosInadimplentes(filtro.getData());
	}
	
	@Override
	@Transactional(readOnly = true)
	public Long obterQuantidadeTotalBancario(FiltroDetalheBaixaBoletoDTO filtro) {

		return this.boletoRepository.obterQuantidadeTotalBancario(filtro.getData());
	}

}
