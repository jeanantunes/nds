package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BaixaAutomaticaRepository;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.ControleBaixaBancariaService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
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
	private BoletoRepository boletoRepository;
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Autowired
	private BaixaAutomaticaRepository baixaAutomaticaRepository;
	
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
		
		validarDadosEntrada(pagamento);
		
		Date dataOperacao = distribuidor.getDataOperacao();
		
		Boleto boleto = boletoRepository.obterPorNossoNumero(pagamento.getNossoNumero());
		
		// Boleto não encontrado na base
		if (boleto == null) {
		
			baixarBoletoNaoEncontrado(tipoBaixaCobranca, pagamento, nomeArquivo, dataOperacao,
									  boleto, resumoBaixaBoletos);
			
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
			
			baixarBoletoValorCorreto(tipoBaixaCobranca, pagamento, nomeArquivo, dataOperacao,
									 boleto, resumoBaixaBoletos);
			
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
										   String nomeArquivo, Date dataOperacao, Boleto boleto,
										   ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		/*
		 * Gera baixa com status de pago, porém o nosso número
		 * referente ao pagamento não existe na base
		 * 
		 */
		
		gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_BOLETO_NAO_ENCONTRADO, boleto,
						   dataOperacao, nomeArquivo, pagamento.getNumeroRegistro(),
						   pagamento.getValorPagamento());

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
							   dataOperacao, nomeArquivo, pagamento.getNumeroRegistro(),
							   pagamento.getValorPagamento());

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
		
		if (politicaCobranca == null || !politicaCobranca.isAceitaBaixaPagamentoVencido()) {
			
			baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_DIVERGENCIA_DATA, boleto,
								 			   dataOperacao, nomeArquivo, pagamento.getNumeroRegistro(),
								 			   pagamento.getValorPagamento());

			incrementarBoletosRejeitados(resumoBaixaBoletos);
			
			movimentoFinanceiroCotaService
				.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
													   GrupoMovimentoFinaceiro.CREDITO,
												   	   usuario, pagamento.getValorPagamento(),
												   	   dataOperacao, baixaCobranca,
												   	   dataNovoMovimento);
			
			return;
			
		}
		
		/*
		 * Baixa o boleto, gera baixa com status de pago com divergência de data,
		 * calcula multas e juros em cima do valor que deveria ser pago 
		 * e gera movimento de crédito ou débito se necessário
		 * 
		 */
		
		baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_DATA, boleto,
							 			   dataOperacao, nomeArquivo, pagamento.getNumeroRegistro(),
							 			   pagamento.getValorPagamento());

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
										  String nomeArquivo, Date dataOperacao, Boleto boleto,
										  ResumoBaixaBoletosDTO resumoBaixaBoletos) {
		
		// Baixa o boleto o gera baixa com status de pago
		
		gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO, boleto, dataOperacao, nomeArquivo,
				 		   pagamento.getNumeroRegistro(), pagamento.getValorPagamento());
		
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
		
		if (politicaCobranca == null || !politicaCobranca.isAceitaBaixaPagamentoMaior()) {
			
			baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR, boleto,
	 				   						   dataOperacao, nomeArquivo, pagamento.getNumeroRegistro(),
	 				   						   pagamento.getValorPagamento());

			incrementarBoletosRejeitados(resumoBaixaBoletos);

			movimentoFinanceiroCotaService
				.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
						   							   GrupoMovimentoFinaceiro.CREDITO,
						   							   usuario, pagamento.getValorPagamento(),
						   							   dataOperacao, baixaCobranca,
												   	   dataNovoMovimento);
			
			return;
		}
		
		/*
		 * Baixa o boleto, gera baixa com status de pago por divergência de valor
		 * e gera movimento de crédito da diferença
		 * 
		 */
		
		baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_VALOR, boleto,
										   dataOperacao, nomeArquivo, pagamento.getNumeroRegistro(),
										   pagamento.getValorPagamento());
		
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
		
		if (politicaCobranca == null || !politicaCobranca.isAceitaBaixaPagamentoMenor()) {
			
			baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR, boleto,
					   						   dataOperacao, nomeArquivo, pagamento.getNumeroRegistro(),
					   						   pagamento.getValorPagamento());

			incrementarBoletosRejeitados(resumoBaixaBoletos);

			movimentoFinanceiroCotaService
				.gerarMovimentoFinanceiroDebitoCredito(boleto.getCota(),
							   						   GrupoMovimentoFinaceiro.CREDITO,
							   						   usuario, pagamento.getValorPagamento(),
							   						   dataOperacao, baixaCobranca,
												   	   dataNovoMovimento);
			
			return;
		}
		
		/*
		 * Baixa o boleto, gera baixa com status de pago por divergência de valor
		 * e gera movimento de débito da diferença
		 * 
		 */

		baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_VALOR, boleto,
 				   						   dataOperacao, nomeArquivo, pagamento.getNumeroRegistro(),
 				   						   pagamento.getValorPagamento());
		
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
	
	private void validarDadosEntrada(PagamentoDTO pagamento) {
		
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

	private BaixaCobranca gerarBaixaCobranca(TipoBaixaCobranca tipoBaixaCobranca, StatusBaixa statusBaixa,
											 Boleto boleto, Date dataBaixa, String nomeArquivo,
									  		 Integer numeroLinhaArquivo, BigDecimal valorPago) {
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
			BaixaAutomatica baixaAutomatica = new BaixaAutomatica();
			
			baixaAutomatica.setDataBaixa(dataBaixa);
			baixaAutomatica.setNomeArquivo(nomeArquivo);
			baixaAutomatica.setStatus(statusBaixa);
			baixaAutomatica.setNumeroRegistroArquivo(numeroLinhaArquivo);
			baixaAutomatica.setValorPago(valorPago);
			baixaAutomatica.setBoleto(boleto);
			
			baixaAutomaticaRepository.adicionar(baixaAutomatica);
			
			return baixaAutomatica;
			
		} else if (TipoBaixaCobranca.MANUAL.equals(tipoBaixaCobranca)) {
			
			//TODO:
			
			return null;
			
		} else {
			
			return null;
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
	
    @Override
	@Transactional(readOnly=true)
	public GeradorBoleto geraBoleto(String nossoNumero){
    	
		Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero);		
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
		Endereco endereco=new Endereco();
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
			geradorBoleto.setEnderecoSacadoUf("");
			geradorBoleto.setEnderecoSacadoLocalidade("Endereco nao cadastrado.");
			geradorBoleto.setEnderecoSacadoCep("");
			geradorBoleto.setEnderecoSacadoBairro("");
			geradorBoleto.setEnderecoSacadoLogradouro("");
			geradorBoleto.setEnderecoSacadoNumero("");
		}

        String contaNumero=boleto.getBanco().getConta().toString();
        String contaNossoNumero=boleto.getNossoNumero().toString();
        String contaNumeroDocumento="123456";//???
        
        //INFORMACOES DA CONTA(BANCO)
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
	
	@Override
	@Transactional(readOnly=true)
	public byte[] gerarImpressaoBoleto(String nossoNumero) throws IOException {
		GeradorBoleto boleto = this.geraBoleto(nossoNumero);
		byte[] b = boleto.getBytePdf();
        return b;
	}

	@Override
	@Transactional(readOnly=true)
	public File gerarAnexoBoleto(String nossoNumero) throws IOException {
		GeradorBoleto boleto = this.geraBoleto(nossoNumero);
		File f = boleto.getFilePdf();
        return f;
	}

	@Override
	@Transactional(readOnly=true)
	public String obterEmailCota(String nossoNumero) {
		Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero);
		String email=boleto.getCota().getPessoa().getEmail();
		return email;
	}

	@Override
	@Transactional(readOnly=true)
	public Boleto obterBoletoPorNossoNumero(String nossoNumero) {
		Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero);
		return boleto;
	}
	
	@Override
	@Transactional(readOnly=true)
	public CobrancaVO obterDadosCobranca(String nossoNumero) {
		CobrancaVO cobranca=null;
		Boleto boleto=this.obterBoletoPorNossoNumero(nossoNumero);
		
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
			cobranca.setValor(boleto.getValor());
			
			cobranca.setDividaTotal(boleto.getDivida().getValor());
			
			cobranca.setDataPagamento(DateUtil.formatarDataPTBR(Calendar.getInstance().getTime()));
			cobranca.setDesconto(BigDecimal.ZERO);
				
			
			
			
			//PARAMETROS PARA CALCULO DE JUROS E MULTA
			Distribuidor distribuidor = distribuidorService.obter();
	        Date dataOperacao = distribuidor.getDataOperacao();
	        
	        //AQUI BUSCAR JUROS E MULTA
			
	        cobranca.setJuros(BigDecimal.ZERO);
            cobranca.setMulta(BigDecimal.ZERO);
            
            
            
			
			cobranca.setValorTotal(boleto.getDivida().getValor());
		}
		return cobranca;
	}


}
