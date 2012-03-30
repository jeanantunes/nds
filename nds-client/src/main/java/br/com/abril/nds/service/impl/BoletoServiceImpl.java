package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
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
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BaixaCobrancaRepository;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.ControleBaixaBancariaService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.GeradorBoleto;
import br.com.abril.nds.util.TipoBaixaCobranca;
import br.com.abril.nds.util.TipoMensagem;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Boleto}
 * 
 * @author Discover Technology
 */
@Service
public class BoletoServiceImpl implements BoletoService {
	
	@Autowired
	private EmailService email;

	@Autowired
	private BoletoRepository boletoRepository;
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Autowired
	private BaixaCobrancaRepository baixaCobrancaRepository;
	
	@Autowired
	private ControleBaixaBancariaRepository controleBaixaRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ControleBaixaBancariaService controleBaixaService;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private CobrancaService cobrancaService;
	
	@Override
	@Transactional(readOnly=true)
	public List<Boleto> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {
		return this.boletoRepository.obterBoletosPorCota(filtro);
	}
	
	@Override
	@Transactional(readOnly=true)
	public long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {
		return this.boletoRepository.obterQuantidadeBoletosPorCota(filtro);
	}
	
	@Override
	@Transactional
	public ResumoBaixaBoletosDTO baixarBoletosAutomatico(ArquivoPagamentoBancoDTO arquivoPagamento,
							  				   			 BigDecimal valorFinanceiro, Usuario usuario) {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		ControleBaixaBancaria controleBaixa =
				controleBaixaRepository.obterPorData(dataOperacao);
		
		if (controleBaixa != null
			&& controleBaixa.getStatus().equals(StatusControle.CONCLUIDO_SUCESSO)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, 
				"Já foi realizada baixa automática na data de operação atual!");
		}
		
		if (valorFinanceiro == null
				|| valorFinanceiro.compareTo(arquivoPagamento.getSomaPagamentos()) != 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, 
				"Valor financeiro inválido! A soma dos valores dos boletos pagos " +
				"deve ser igual ao valor informado!");
		}
		
		PoliticaCobranca politicaCobranca =
			politicaCobrancaRepository.obterPorTipoCobranca(TipoCobranca.BOLETO);
		
		controleBaixaService.alterarControleBaixa(StatusControle.INICIADO,
												  dataOperacao, usuario);
		
		ResumoBaixaBoletosDTO resumoBaixaBoletos = new ResumoBaixaBoletosDTO();
		
		Date dataNovoMovimento = calendarioService.adicionarDiasUteis(dataOperacao, 1);
		
		try {
			if (arquivoPagamento != null && arquivoPagamento.getListaPagemento() != null) {
				
				for (PagamentoDTO pagamento : arquivoPagamento.getListaPagemento()) {
				
					baixarBoleto(TipoBaixaCobranca.AUTOMATICA, pagamento, usuario,
								 arquivoPagamento.getNomeArquivo(), politicaCobranca,
								 distribuidor, dataNovoMovimento, resumoBaixaBoletos);
				}
				
				controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_SUCESSO,
						  								  dataOperacao, usuario);
				
			} else {
				
				controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
						  								  dataOperacao, usuario);
			}
			
			resumoBaixaBoletos.setNomeArquivo(arquivoPagamento.getNomeArquivo());
			resumoBaixaBoletos.setDataCompetencia(DateUtil.formatarDataPTBR(dataOperacao));
			resumoBaixaBoletos.setSomaPagamentos(arquivoPagamento.getSomaPagamentos());
			
			return resumoBaixaBoletos;
			
		} catch (Exception e) {
			
			controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
					  								  dataOperacao, usuario);
			
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
							 Date dataNovoMovimento, ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		incrementarBoletosLidos(resumoBaixaBoletos);
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
			validarDadosEntradaBaixaAutomatica(pagamento);
			
		} else {
			
			validarDadosEntradaBaixaManual(pagamento);
		}
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		Boleto boleto = boletoRepository.obterPorNossoNumero(pagamento.getNossoNumero(), null);
		
		// Boleto não encontrado na base
		if (boleto == null) {
		
			baixarBoletoNaoEncontrado(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
									  dataOperacao, boleto, resumoBaixaBoletos);
			
			return;
		}
			
		// Boleto já foi pago
		if (boleto.getStatusCobranca().equals(StatusCobranca.PAGO)) {
			
			baixarBoletoJaPago(tipoBaixaCobranca, pagamento, usuario, nomeArquivo, dataNovoMovimento,
							   dataOperacao, boleto, resumoBaixaBoletos);
			
			return;
		}
		
		Date dataVencimentoUtil = calendarioService.adicionarDiasUteis(boleto.getDataVencimento(), 0);
		
		// Boleto vencido
		if (dataVencimentoUtil.compareTo(pagamento.getDataPagamento()) < 0) {
			
			baixarBoletoVencido(tipoBaixaCobranca, pagamento, usuario, nomeArquivo, politicaCobranca,
								distribuidor, dataNovoMovimento, dataOperacao, boleto, resumoBaixaBoletos);
			
			return;
		}
			
		// Boleto pago com valor correto
		if (pagamento.getValorPagamento().compareTo(boleto.getValor()) == 0) {
			
			baixarBoletoValorCorreto(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
									 dataOperacao, boleto, resumoBaixaBoletos);
			
			return;
			
		} else if (pagamento.getValorPagamento().compareTo(boleto.getValor()) == 1) {
			
			// Boleto pago com valor acima
			baixarBoletoValorAcima(tipoBaixaCobranca, pagamento, usuario, nomeArquivo, politicaCobranca,
								   dataNovoMovimento, dataOperacao, boleto, resumoBaixaBoletos);
			
			return;
			
		} else {
		
			// Boleto pago com valor abaixo
			baixarBoletoValorAbaixo(tipoBaixaCobranca, pagamento, usuario, nomeArquivo, politicaCobranca,
									dataNovoMovimento, dataOperacao, boleto, resumoBaixaBoletos);
			
			return;
		}
	}
	
	private void baixarBoletoNaoEncontrado(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
										   Usuario usuario, String nomeArquivo, Date dataOperacao,
										   Boleto boleto, ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		/*
		 * Gera baixa com status de pago, porém o nosso número
		 * referente ao pagamento não existe na base
		 * 
		 */
		
		gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_BOLETO_NAO_ENCONTRADO, boleto,
						   dataOperacao, nomeArquivo, pagamento,
						   pagamento.getValorPagamento(), usuario);

		incrementarBoletosBaixados(resumoBaixaBoletos);
	}
	
	private void baixarBoletoJaPago(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
									Usuario usuario, String nomeArquivo, Date dataNovoMovimento,
									Date dataOperacao, Boleto boleto,
									ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		/*
		 * Não baixa o boleto, gera baixa com status de boleto pago anteriormente
		 * e gera movimento de crédito
		 * 
		 */
		
		BaixaCobranca baixaCobranca =
			gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_BAIXA_JA_REALIZADA, boleto,
							   dataOperacao, nomeArquivo, pagamento,
							   pagamento.getValorPagamento(), usuario);

		incrementarBoletosRejeitados(resumoBaixaBoletos);

		movimentoFinanceiroCotaService.gerarMovimentoFinanceiroDebitoCredito(
				boleto.getCota(), GrupoMovimentoFinaceiro.CREDITO, usuario,
				pagamento.getValorPagamento(), dataOperacao, baixaCobranca, dataNovoMovimento);
	}
	
	private void baixarBoletoVencido(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
									 Usuario usuario, String nomeArquivo, PoliticaCobranca politicaCobranca,
									 Distribuidor distribuidor, Date dataNovoMovimento,
									 Date dataOperacao, Boleto boleto,
									 ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		BaixaCobranca baixaCobranca = null;
		
		/*
		 * Não baixa o boleto, gera baixa com status de não pago por divergência de data
		 * e gera movimento de crédito
		 * 
		 */
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
		
			if (politicaCobranca == null || !politicaCobranca.isAceitaBaixaPagamentoVencido()) {
				
				baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_DIVERGENCIA_DATA, boleto,
									 			   dataOperacao, nomeArquivo, pagamento,
									 			   pagamento.getValorPagamento(), usuario);
	
				incrementarBoletosRejeitados(resumoBaixaBoletos);
				
				movimentoFinanceiroCotaService
					.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
														   GrupoMovimentoFinaceiro.CREDITO,
													   	   usuario, pagamento.getValorPagamento(),
													   	   dataOperacao, baixaCobranca,
													   	   dataNovoMovimento);
				
				return;
				
			}
		}
		
		/*
		 * Baixa o boleto, gera baixa com status de pago com divergência de data,
		 * calcula multas e juros em cima do valor que deveria ser pago 
		 * e gera movimento de crédito ou débito se necessário
		 * 
		 */
		
		baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_DATA, boleto,
							 			   dataOperacao, nomeArquivo, pagamento,
							 			   pagamento.getValorPagamento(), usuario);

		efetivarBaixaCobranca(boleto, dataOperacao);
		
		incrementarBoletosBaixadosComDivergencia(resumoBaixaBoletos);
		
		BigDecimal valorJurosCalculado = 
				cobrancaService.calcularJuros(distribuidor, boleto.getCota(),
											  boleto.getValor(), boleto.getDataVencimento(),
											  pagamento.getDataPagamento());
		
		if (valorJurosCalculado.compareTo(BigDecimal.ZERO) == 1) {
			
			movimentoFinanceiroCotaService
				.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
												   	   GrupoMovimentoFinaceiro.JUROS,
												   	   usuario, valorJurosCalculado,
												   	   dataOperacao, baixaCobranca,
												   	   dataNovoMovimento);
		}
		
		BigDecimal valorMultaCalculado = 
				cobrancaService.calcularMulta(distribuidor, boleto.getCota(),
											  boleto.getValor());
		
		if (valorMultaCalculado.compareTo(BigDecimal.ZERO) == 1) {
			
			movimentoFinanceiroCotaService
				.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
												       GrupoMovimentoFinaceiro.MULTA,
												       usuario, valorMultaCalculado,
												       dataOperacao, baixaCobranca,
												       dataNovoMovimento);
		}
		
		BigDecimal diferencaValor = null;
		
		if (pagamento.getValorPagamento().compareTo(boleto.getValor()) == 1) {
		
			diferencaValor = pagamento.getValorPagamento().subtract(boleto.getValor());
			
			movimentoFinanceiroCotaService
				.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
												   	   GrupoMovimentoFinaceiro.CREDITO,
												   	   usuario, diferencaValor,
												   	   dataOperacao, baixaCobranca,
												   	   dataNovoMovimento);
			
		} else if (pagamento.getValorPagamento().compareTo(boleto.getValor()) == -1) {
			
			diferencaValor = boleto.getValor().subtract(pagamento.getValorPagamento());
			
			movimentoFinanceiroCotaService
				.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
											   	   	   GrupoMovimentoFinaceiro.DEBITO,
											   	   	   usuario, diferencaValor,
											   	   	   dataOperacao, baixaCobranca,
											   	   	   dataNovoMovimento);
		}
	}
	
	private void baixarBoletoValorCorreto(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
										  Usuario usuario, String nomeArquivo, Date dataOperacao,
										  Boleto boleto, ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		// Baixa o boleto o gera baixa com status de pago
		
		gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO, boleto, dataOperacao, nomeArquivo,
				 		   pagamento, pagamento.getValorPagamento(), usuario);
		
		efetivarBaixaCobranca(boleto, dataOperacao);
		
		incrementarBoletosBaixados(resumoBaixaBoletos);
	}
	
	private void baixarBoletoValorAcima(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
										Usuario usuario, String nomeArquivo,  PoliticaCobranca politicaCobranca,
										Date dataNovoMovimento, Date dataOperacao, Boleto boleto,
			 							ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		/*
		 * Verifica o parâmetro para pagamento a maior, não baixa o boleto, gera baixa
		 * com status de não pago por divergência de valor e gera movimento de crédito
		 */
		
		BaixaCobranca baixaCobranca = null;
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
		
			if (politicaCobranca == null || !politicaCobranca.isAceitaBaixaPagamentoMaior()) {
				
				baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR, boleto,
		 				   						   dataOperacao, nomeArquivo, pagamento,
		 				   						   pagamento.getValorPagamento(), usuario);
	
				incrementarBoletosRejeitados(resumoBaixaBoletos);
	
				movimentoFinanceiroCotaService
					.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
							   							   GrupoMovimentoFinaceiro.CREDITO,
							   							   usuario, pagamento.getValorPagamento(),
							   							   dataOperacao, baixaCobranca,
													   	   dataNovoMovimento);
				
				return;
			}
		}
		
		/*
		 * Baixa o boleto, gera baixa com status de pago por divergência de valor
		 * e gera movimento de crédito da diferença
		 * 
		 */
		
		baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_VALOR, boleto,
										   dataOperacao, nomeArquivo, pagamento,
										   pagamento.getValorPagamento(), usuario);
		
		efetivarBaixaCobranca(boleto, dataOperacao);
		
		incrementarBoletosBaixadosComDivergencia(resumoBaixaBoletos);
		
		BigDecimal valorCredito = pagamento.getValorPagamento().subtract(boleto.getValor());
		
		movimentoFinanceiroCotaService
			.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
												   GrupoMovimentoFinaceiro.CREDITO,
											   	   usuario, valorCredito,
											   	   dataOperacao, baixaCobranca,
											   	   dataNovoMovimento);
	}
	
	private void baixarBoletoValorAbaixo(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
										 Usuario usuario, String nomeArquivo, PoliticaCobranca politicaCobranca,
										 Date dataNovoMovimento, Date dataOperacao, Boleto boleto,
										 ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		BaixaCobranca baixaCobranca = null;
		
		/*
		 * Verifica o parâmetro para pagamento a menor, não baixa o boleto, gera baixa
		 * com status de não pago por divergência de valor e gera movimento de crédito
		 * 
		 */
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
		
			if (politicaCobranca == null || !politicaCobranca.isAceitaBaixaPagamentoMenor()) {
				
				baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR, boleto,
						   						   dataOperacao, nomeArquivo, pagamento,
						   						   pagamento.getValorPagamento(), usuario);
	
				incrementarBoletosRejeitados(resumoBaixaBoletos);
	
				movimentoFinanceiroCotaService
					.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
								   						   GrupoMovimentoFinaceiro.CREDITO,
								   						   usuario, pagamento.getValorPagamento(),
								   						   dataOperacao, baixaCobranca,
													   	   dataNovoMovimento);
				
				return;
			}
		}
		
		/*
		 * Baixa o boleto, gera baixa com status de pago por divergência de valor
		 * e gera movimento de débito da diferença
		 * 
		 */

		baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_VALOR, boleto,
 				   						   dataOperacao, nomeArquivo, pagamento,
 				   						   pagamento.getValorPagamento(), usuario);
		
		efetivarBaixaCobranca(boleto, dataOperacao);
		
		incrementarBoletosBaixadosComDivergencia(resumoBaixaBoletos);
		
		BigDecimal valorDebito = boleto.getValor().subtract(pagamento.getValorPagamento());
		
		movimentoFinanceiroCotaService
			.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
												   GrupoMovimentoFinaceiro.DEBITO,
											   	   usuario, valorDebito,
											   	   dataOperacao, baixaCobranca,
											   	   dataNovoMovimento);
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
									  		 PagamentoDTO pagamento, BigDecimal valorPago,
									  		 Usuario usuario) {
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
			BaixaAutomatica baixaAutomatica = new BaixaAutomatica();
			
			baixaAutomatica.setDataBaixa(dataBaixa);
			baixaAutomatica.setValorPago(valorPago);
			baixaAutomatica.setCobranca(boleto);
			
			baixaAutomatica.setNomeArquivo(nomeArquivo);
			baixaAutomatica.setStatus(statusBaixa);
			baixaAutomatica.setNumeroRegistroArquivo(pagamento.getNumeroRegistro());
			baixaAutomatica.setNossoNumero(pagamento.getNossoNumero());
			
			baixaCobrancaRepository.adicionar(baixaAutomatica);
			
			return baixaAutomatica;
			
		} else {
			
			BaixaManual baixaManual = new BaixaManual();
			
			baixaManual.setDataBaixa(dataBaixa);
			baixaManual.setValorPago(valorPago);
			baixaManual.setCobranca(boleto);
			
			baixaManual.setResponsavel(usuario);
			
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
	
	private void incrementarBoletosLidos(ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		if (resumoBaixaBoletos != null) {
		
			resumoBaixaBoletos.setQuantidadeLidos(
					resumoBaixaBoletos.getQuantidadeLidos() + 1);
			
		}
	}
	
	private void incrementarBoletosBaixados(ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		if (resumoBaixaBoletos != null) {
		
			resumoBaixaBoletos.setQuantidadeBaixados(
					resumoBaixaBoletos.getQuantidadeBaixados() + 1);
			
		}
	}
	
	private void incrementarBoletosRejeitados(ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		if (resumoBaixaBoletos != null) {
			
			resumoBaixaBoletos.setQuantidadeRejeitados(
					resumoBaixaBoletos.getQuantidadeRejeitados() + 1);
			
		}
	}

	private void incrementarBoletosBaixadosComDivergencia(ResumoBaixaBoletosDTO resumoBaixaBoletos) {
	
		if (resumoBaixaBoletos != null) {
			
			resumoBaixaBoletos.setQuantidadeBaixadosComDivergencia(
					resumoBaixaBoletos.getQuantidadeBaixadosComDivergencia() + 1);
			
		}
	}
	
	private GeradorBoleto geraBoleto(Boleto boleto){

		
		GeradorBoleto geradorBoleto = new GeradorBoleto();
		
		
		//DADOS DO CEDENTE
		geradorBoleto.setCedenteNome(distribuidorService.obter().getJuridica().getRazaoSocial());         
		geradorBoleto.setCedenteDocumento(distribuidorService.obter().getJuridica().getCnpj());
		
		
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
		geradorBoleto.setSacadoNome(nomeSacado);          
		geradorBoleto.setSacadoDocumento(documentoSacado); 

		
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
			geradorBoleto.setEnderecoSacadoUf(endereco.getUf());            
			geradorBoleto.setEnderecoSacadoLocalidade(endereco.getCidade());     
			geradorBoleto.setEnderecoSacadoCep(endereco.getCep());         
			geradorBoleto.setEnderecoSacadoBairro(endereco.getBairro()); 
			geradorBoleto.setEnderecoSacadoLogradouro(endereco.getLogradouro()); 
			geradorBoleto.setEnderecoSacadoNumero(Integer.toString(endereco.getNumero())); 
		}
		else{
			geradorBoleto.setEnderecoSacadoUf("SP");
			geradorBoleto.setEnderecoSacadoLocalidade("Endereco nao cadastrado.");
			geradorBoleto.setEnderecoSacadoCep("");
			geradorBoleto.setEnderecoSacadoBairro("");
			geradorBoleto.setEnderecoSacadoLogradouro("");
			geradorBoleto.setEnderecoSacadoNumero("");
		}

		
		//INFORMACOES DA CONTA(BANCO)
        String contaNumero=boleto.getBanco().getConta().toString();
        String contaNossoNumero=boleto.getNossoNumero().toString();
        String contaNumeroDocumento="123456";//???
        geradorBoleto.setContaNumeroBanco(boleto.getBanco().getNumeroBanco());                  
        geradorBoleto.setContaCarteira(boleto.getBanco().getCarteira().getCodigo());
        if (boleto.getBanco().getCarteira().getCodigo()==1){
        	geradorBoleto.setContaTipoDeCobranca("SEM_REGISTRO");
        }
        if (boleto.getBanco().getCarteira().getCodigo()==30){  
        	geradorBoleto.setContaTipoDeCobranca("COM_REGISTRO");
        }
        geradorBoleto.setContaAgencia(boleto.getBanco().getAgencia().intValue());    
        geradorBoleto.setContaNumero(Integer.parseInt(contaNumero));   
        
         
        //INFORMACOES DO TITULO
        geradorBoleto.setTituloNumeroDoDocumento(contaNumeroDocumento);                      
        geradorBoleto.setTituloNossoNumero(contaNossoNumero);                    
        
        //PARAMETROS ?
        geradorBoleto.setTituloDigitoDoNossoNumero("4");  
        geradorBoleto.setTituloTipoDeDocumento("DM_DUPLICATA_MERCANTIL");
        geradorBoleto.setTituloAceite("A");
        geradorBoleto.setTituloTipoIdentificadorCNR("COM_VENCIMENTO");
        
        geradorBoleto.setTituloValor(boleto.getValor());   
        geradorBoleto.setTituloDataDoDocumento(boleto.getDataEmissao());   
        geradorBoleto.setTituloDataDoVencimento(boleto.getDataVencimento());  
        geradorBoleto.setTituloDesconto(BigDecimal.ZERO);
        geradorBoleto.setTituloDeducao(BigDecimal.ZERO);
        geradorBoleto.setTituloMora(BigDecimal.ZERO);
        geradorBoleto.setTituloAcrecimo(BigDecimal.ZERO);
        geradorBoleto.setTituloValorCobrado(BigDecimal.ZERO);
        

        //INFORMAÇOES DO BOLETO
        //PARAMETROS ?
        geradorBoleto.setBoletoLocalPagamento("Local do pagamento.");
        geradorBoleto.setBoletoInstrucaoAoSacado("Instrução so Sacado");
        geradorBoleto.setBoletoInstrucao1(boleto.getBanco().getInstrucoes());
        geradorBoleto.setBoletoInstrucao2("");
        geradorBoleto.setBoletoInstrucao3("");
        geradorBoleto.setBoletoInstrucao4("");
        geradorBoleto.setBoletoInstrucao5("");
        geradorBoleto.setBoletoInstrucao6("");
        geradorBoleto.setBoletoInstrucao7("");
        geradorBoleto.setBoletoInstrucao8("");
        
        return geradorBoleto;
	}
	
	private File gerarAnexoBoleto(Boleto boleto) throws IOException {
		GeradorBoleto geradorBoleto = this.geraBoleto(boleto);
		File f = geradorBoleto.getFilePdf();
        return f;
	}
	
	@Override
	@Transactional(readOnly=true)
	public void enviarBoletoEmail(String nossoNumero) {
		try{
			
			Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero,null);
			
			File anexo = this.gerarAnexoBoleto(boleto);
			String[] destinatarios = new String[]{boleto.getCota().getPessoa().getEmail()};
	
			Distribuidor distribuidor = distribuidorService.obter();
			String assunto=(distribuidor.getPoliticaCobranca()!=null?distribuidor.getPoliticaCobranca().getAssuntoEmailCobranca():"");
			String mensagem=(distribuidor.getPoliticaCobranca()!=null?distribuidor.getPoliticaCobranca().getMensagemEmailCobranca():"");
			email.enviar(assunto, 
					     mensagem, 
					     destinatarios, 
					     anexo);
		}
		catch(Exception e){
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro no envio.");
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public byte[] gerarImpressaoBoleto(String nossoNumero) throws IOException {
		
		Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero,null);
		
		GeradorBoleto geradorBoleto = this.geraBoleto(boleto);
		byte[] b = geradorBoleto.getBytePdf();
        return b;
	}
	
	@Override
	@Transactional(readOnly=true)
	public CobrancaVO obterDadosCobranca(String nossoNumero) {
		
		//PARAMETROS PARA CALCULO DE JUROS E MULTA
		Distribuidor distribuidor = distribuidorService.obter();
        Date dataOperacao = distribuidor.getDataOperacao();
		
		CobrancaVO cobranca=null;
		
		Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero,false);
		
		if ((boleto!=null)&&(boleto.getDataPagamento()==null)){
			cobranca = new CobrancaVO();
			
			cobranca.setNossoNumero(boleto.getNossoNumero());	
			String cota = "";
			if ((boleto.getCota().getPessoa()) instanceof PessoaFisica){
				cota = ((PessoaFisica) boleto.getCota().getPessoa()).getNome();
			}
			if ((boleto.getCota().getPessoa()) instanceof PessoaJuridica){
				cota = ((PessoaJuridica) boleto.getCota().getPessoa()).getRazaoSocial();
			}
			cobranca.setCota(cota);
			cobranca.setBanco(boleto.getBanco().getNome());
			cobranca.setDataVencimento((boleto.getDataVencimento()!=null?DateUtil.formatarDataPTBR(boleto.getDataVencimento()):""));
			cobranca.setDataEmissao((boleto.getDataEmissao()!=null?DateUtil.formatarDataPTBR(boleto.getDataEmissao()):""));
			cobranca.setValor(CurrencyUtil.formatarValor(boleto.getValor()));
			cobranca.setDividaTotal(CurrencyUtil.formatarValor(boleto.getDivida().getValor()));
			
			//CALCULO DE JUROS E MULTA
			BigDecimal valorJurosCalculado = BigDecimal.ZERO;
			BigDecimal valorMultaCalculado = BigDecimal.ZERO;
			Date dataVencimentoUtil = calendarioService.adicionarDiasUteis(boleto.getDataVencimento(), 0);
			if (dataVencimentoUtil.compareTo(dataOperacao) < 0) {
				//CALCULA JUROS
				valorJurosCalculado = cobrancaService.calcularJuros(distribuidor, boleto.getCota(),
													  boleto.getValor(), boleto.getDataVencimento(),
													  dataOperacao);
				//CALCULA MULTA
				valorMultaCalculado = cobrancaService.calcularMulta(distribuidor, boleto.getCota(),
													  boleto.getValor());
			}
			
			cobranca.setDataPagamento( DateUtil.formatarDataPTBR(dataOperacao) );
			cobranca.setDesconto( CurrencyUtil.formatarValor(BigDecimal.ZERO) );
			cobranca.setJuros( CurrencyUtil.formatarValor(valorJurosCalculado) );
            cobranca.setMulta( CurrencyUtil.formatarValor(valorMultaCalculado) );
            
            //CALCULA VALOR TOTAL
            Double valorTotal=(boleto.getValor().doubleValue() + 
            		          valorJurosCalculado.doubleValue() +
            		          valorMultaCalculado.doubleValue());
            
			cobranca.setValorTotal( CurrencyUtil.formatarValor(valorTotal) );
		}
		return cobranca;
	}

}
