package br.com.abril.nds.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.xml.bind.ValidationException;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.BoletoCotaDTO;
import br.com.abril.nds.dto.BoletoEmBrancoDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
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
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.financeiro.AcumuloDivida;
import br.com.abril.nds.model.financeiro.BaixaAutomatica;
import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.model.financeiro.BaixaManual;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.BoletoAntecipado;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.EmissaoBoletoAntecipado;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusBaixa;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BaixaCobrancaRepository;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.BoletoAntecipadoRepository;
import br.com.abril.nds.repository.BoletoEmailRepository;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.AcumuloDividasService;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.ControleBaixaBancariaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;
import br.com.abril.nds.util.CorpoBoleto;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.GeradorBoleto;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.NomeBanco;
import br.com.abril.nds.util.TipoBaixaCobranca;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Boleto}
 * 
 * @author Discover Technology
 */
@Service
public class BoletoServiceImpl implements BoletoService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BoletoServiceImpl.class);
	
	@Autowired
	protected EmailService email;

	@Autowired
	protected BoletoRepository boletoRepository;
	
	@Autowired
	protected ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	protected PoliticaCobrancaService politicaCobrancaService;
	
	@Autowired
	protected FormaCobrancaService formaCobrancaService;
	
	@Autowired
	protected UsuarioService usuarioService;
	
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
	protected BoletoAntecipadoRepository boletoAntecipadoRepository;

	@Autowired
	protected TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	protected BancoRepository bancoRepository;
	
	@Autowired
	protected DividaRepository dividaRepository;
	
	@Autowired
	protected CotaRepository cotaRepository;
	
	@Autowired
	protected FornecedorRepository fornecedorRepository;
	
	@Autowired
	protected AcumuloDividasService acumuloDividasService;
	
	@Autowired
	protected FormaCobrancaRepository formaCobrancaRepository;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	protected BoletoEmailRepository boletoEmailRepository;
	
	@Autowired
	private ParametroCobrancaCotaService paramtroCobrancaCotaService;
	
	
	        /**
     * Método responsável por obter boletos por numero da cota
     * 
     * @param filtro
     * @return Lista de boletos encontrados
     */
	@Override
	@Transactional(readOnly=true)
	public List<BoletoCotaDTO> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {
		return this.boletoRepository.obterBoletosPorCota(filtro);
	}
	
	        /**
     * Método responsável por obter boleto por nossoNumero
     * 
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
     * 
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
		
		BigDecimal valorTotalBancario = this.boletoRepository.obterValorTotalBancario(data);
		
		valorTotalBancario = (valorTotalBancario == null) ? BigDecimal.ZERO : valorTotalBancario;
		
		resumoBaixaBoletos.setValorTotalBancario(valorTotalBancario.setScale(2, RoundingMode.HALF_EVEN));
		
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
                    "Já foi realizada baixa automática para a data de pagamento informada e banco " + banco.getNome()
                            + "!");
		}
		
		if (valorFinanceiro == null || arquivoPagamento.getSomaPagamentos() == null
				|| valorFinanceiro.compareTo(arquivoPagamento.getSomaPagamentos()) != 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, 
                    "Valor financeiro inválido! A soma dos valores dos boletos pagos "
                            +
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

            // gerar movimentos financeiros para cobranças não pagas
			this.controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
                    dataOperacao, dataPagamento, usuario, banco);
			
			if (e instanceof ValidacaoException) {

				throw new ValidacaoException(((ValidacaoException) e).getValidacao());
			
			} else {
			
				throw new ValidacaoException(TipoMensagem.WARNING, 
 "Falha ao processar a baixa automática: "
                        + e.getMessage());
			}
		}
	}
	
	private boolean isCotaInativa(Cota cota){
		
		return cota.getSituacaoCadastro()!=null?cota.getSituacaoCadastro().equals(SituacaoCadastro.INATIVO):false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public void adiarDividaBoletosNaoPagos(Usuario usuario, Date dataPagamento) {

		LOGGER.info("INICIO PROCESSO ADIAMENTO DIVIDA BOLETO NAO PAGO");
		
		boolean naoAcumulaDividas = this.distribuidorRepository.naoAcumulaDividas();
		
		if(naoAcumulaDividas == true) {
            LOGGER.info("DISTRIBUIDOR NÃO ACUMULA DIVIDAS");
			return;
		}
		
		List<Cobranca> boletosNaoPagos = this.boletoRepository.obterBoletosNaoPagos(dataPagamento);
		
		Integer numeroMaximoAcumulosDistribuidor = 
			this.distribuidorRepository.numeroMaximoAcumuloDividas();
		
		int contador = 0;
		
		int qtdBoletosNaoPagos = 0;
		
		if(boletosNaoPagos!=null) {
			qtdBoletosNaoPagos = boletosNaoPagos.size();
		}
		
		String nossoNumero = "";
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroPendente =
			this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
				GrupoMovimentoFinaceiro.PENDENTE);
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroJuros =
			this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
				GrupoMovimentoFinaceiro.JUROS);
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroMulta =
			this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
				GrupoMovimentoFinaceiro.MULTA);
		
		FormaCobranca formaCobrancaPrincipal = 
			this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
		
		Date dataCalculoJuros = 
			this.calendarioService.adicionarDiasRetornarDiaUtil(dataPagamento, 1);
		
		for (Cobranca boleto : boletosNaoPagos) {
			
			if (this.isCotaInativa(boleto.getCota())){
				
				continue;
			}
			
			LOGGER.info("ADIANDO DIVIDA BOLETO NAO PAGO [" + ++contador + "]  DE [" + qtdBoletosNaoPagos + "].");
			
			nossoNumero = boleto.getNossoNumero();
			
			Divida divida = boleto.getDivida();
			
			try {

				this.validarAcumuloDivida(divida, numeroMaximoAcumulosDistribuidor);

				divida.setStatus(StatusDivida.PENDENTE_INADIMPLENCIA);
				this.dividaRepository.alterar(divida);
				
				boleto.setStatusCobranca(StatusCobranca.NAO_PAGO);
				this.cobrancaRepository.alterar(boleto);
				
				Date dataVencimento = this.obterNovaDataVencimentoAcumulo(dataPagamento);
				
				//movimentoFinanceiro do valor do boleto
				MovimentoFinanceiroCota movimentoPendente = this.gerarMovimentoFinanceiro(
						dataPagamento,
						usuario,
						boleto.getCota(),
						dataVencimento,
						boleto.getValor().setScale(2, BigDecimal.ROUND_HALF_EVEN), 
						tipoMovimentoFinanceiroPendente,
 "Cobrança não paga");
				
				MovimentoFinanceiroCota movimentoJuros = null;
				
				MovimentoFinanceiroCota movimentoMulta = null;
				
				BigDecimal valor = this.cobrancaService.calcularJuros(
						boleto.getBanco(), 
						boleto.getCota().getId(), 
						boleto.getValor().setScale(2, BigDecimal.ROUND_HALF_EVEN), 
						boleto.getDataVencimento(), 
						dataCalculoJuros, formaCobrancaPrincipal);
					
				if (valor != null && valor.compareTo(BigDecimal.ZERO) > 0){
					
					//movimento finaceiro juros
					movimentoJuros = this.gerarMovimentoFinanceiro(
							dataPagamento,
							usuario,
							boleto.getCota(),
							dataVencimento,
							valor,
							tipoMovimentoFinanceiroJuros,
 "Cobrança não paga");
				}
				
				valor = this.cobrancaService.calcularMulta(
						boleto.getBanco(), 
						boleto.getCota(), 
						boleto.getValor().setScale(2, BigDecimal.ROUND_HALF_EVEN),
						formaCobrancaPrincipal);
				
				if (valor != null && valor.compareTo(BigDecimal.ZERO) > 0){
					
					//movimento financeiro multa
					movimentoMulta = this.gerarMovimentoFinanceiro(
							dataPagamento,
							usuario,
							boleto.getCota(), 
							dataVencimento,
							valor,
							tipoMovimentoFinanceiroMulta,
 "Cobrança não paga");
				}

				this.gerarAcumuloDivida(usuario, divida, movimentoPendente, movimentoJuros, movimentoMulta);

			} catch (IllegalArgumentException e) {
			
                LOGGER.error("FALHA AO ADIAR BOLETO DIVIDA NÃO PAGA NOSSO NUMERO [" + nossoNumero + "]", e);
				
                // Caso a dívida exceder o limite de acúmulos do distribuidor,
                // esta não será persistida, dando continuidade ao fluxo.
				continue;
			}
		}
	}
	
	private Date obterNovaDataVencimentoAcumulo(Date dataOperacao) {

		boolean isVencimentoDiaUtil = this.formaCobrancaRepository.obterFormaCobranca().isVencimentoDiaUtil();
		
		if (isVencimentoDiaUtil) {
			
			return this.calendarioService.adicionarDiasUteis(dataOperacao, 1);
		
		} else {
			
			return this.calendarioService.adicionarDiasRetornarDiaUtil(dataOperacao, 1);
		}
	}
	
	private MovimentoFinanceiroCota gerarMovimentoFinanceiro(Date dataOperacao, Usuario usuario, Cota cota, 
			Date dataVencimento, BigDecimal valor, TipoMovimentoFinanceiro tipoMovimentoFinanceiro, String observacao) {

		MovimentoFinanceiroCotaDTO dto = new MovimentoFinanceiroCotaDTO();
		dto.setAprovacaoAutomatica(true);
		dto.setCota(cota);
		dto.setDataCriacao(dataOperacao);
		dto.setDataOperacao(dataOperacao);
		dto.setDataVencimento(dataVencimento);
		dto.setLancamentoManual(false);
		dto.setObservacao(observacao);
		dto.setTipoEdicao(TipoEdicao.INCLUSAO);
		dto.setUsuario(usuario);
		dto.setValor(valor);
		dto.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
		dto.setFornecedor(this.obterFornecedorPadraoCota(cota));
	
		try {
			
			return this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(dto).get(0);
		
		} catch (NoSuchElementException e) {
			
			return null;

		} catch (IndexOutOfBoundsException e) {
			
			return null;
		} 
	}
	
	private AcumuloDivida gerarAcumuloDivida(Usuario usuario, Divida divida, 
											 MovimentoFinanceiroCota movimentoPendente,
										     MovimentoFinanceiroCota movimentoJuros,
										     MovimentoFinanceiroCota movimentoMulta) {

		AcumuloDivida acumuloDivida = new AcumuloDivida();

		acumuloDivida.setDividaAnterior(divida);

		acumuloDivida.setMovimentoFinanceiroPendente(movimentoPendente);

		acumuloDivida.setMovimentoFinanceiroJuros(movimentoJuros);

		acumuloDivida.setMovimentoFinanceiroMulta(movimentoMulta);

		ConsolidadoFinanceiroCota c = null;
		
		for (ConsolidadoFinanceiroCota cc : divida.getConsolidados()){
			
			if (cc.getCota().equals(divida.getCota())){
				
				c = cc;
				break;
			}
		}
		
		acumuloDivida.setNumeroAcumulo(
			this.acumuloDividasService.obterNumeroDeAcumulosDivida(
				c.getId()).add(BigInteger.ONE));
		
		acumuloDivida.setDataCriacao(new Date());
		
		acumuloDivida.setResponsavel(usuario);
		
		acumuloDivida.setStatus(StatusInadimplencia.ATIVA);
		
		acumuloDivida.setCota(divida.getCota());

		return this.acumuloDividasService.atualizarAcumuloDivida(acumuloDivida);
	}

	private void validarAcumuloDivida(Divida divida, Integer numeroMaximoAcumulosDistribuidor) {

		Integer numeroMaximoAcumuloCota = this.acumuloDividasService.obterNumeroMaximoAcumuloCota(divida.getCota().getId()).intValue();
		
		if (numeroMaximoAcumuloCota >= numeroMaximoAcumulosDistribuidor) {
			
			throw new IllegalArgumentException("Acumulo excedeu o limite do distribuidor.");
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

	        /**
     * Baixa Boleto Antecipado - Em Branco Gera movimento financeiro de crédito
     * para a cota referente à Boleto Antecipado - Em Branco
     * 
     * @param boletoAntecipado
     * @param valorPago
     * @param dataPagamento
     */
	private void gerarBaixaBoletoAntecipado(BoletoAntecipado boletoAntecipado, 
											PagamentoDTO pagamento, 
											Date dataPagamento){

		TipoMovimentoFinanceiro tipoMovimento = this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.CREDITO);
		
		Usuario usuario = this.usuarioService.getUsuarioLogado();
		
		Date dataOperacao = this.distribuidorRepository.obterDataOperacaoDistribuidor();
		
		MovimentoFinanceiroCotaDTO movimento = new MovimentoFinanceiroCotaDTO();
		movimento.setCota(boletoAntecipado.getChamadaEncalheCota().getCota());
		movimento.setTipoMovimentoFinanceiro(tipoMovimento);
		movimento.setUsuario(usuario);
		movimento.setDataOperacao(dataOperacao);
        movimento.setValor(pagamento.getValorPagamento());
        movimento.setDataCriacao(Calendar.getInstance().getTime());
		movimento.setTipoEdicao(TipoEdicao.INCLUSAO);
		movimento.setDataVencimento(DateUtil.adicionarDias(dataOperacao,1));
		movimento.setObservacao("BOLETO ANTECIPADO - EM BRANCO");
		movimento.setFornecedor(boletoAntecipado.getFornecedor());

		MovimentoFinanceiroCota movimentoFinanceiroCota = this.movimentoFinanceiroCotaService.gerarMovimentoFinanceiroCota(movimento, null);
		
		boletoAntecipado.setDataPagamento(dataPagamento);
		boletoAntecipado.setMovimentoFinanceiroCota(movimentoFinanceiroCota);
		boletoAntecipado.setStatus(StatusDivida.QUITADA);
		boletoAntecipado.setValorDesconto(pagamento.getValorDesconto());
		boletoAntecipado.setValorJuros(pagamento.getValorJuros());
		boletoAntecipado.setValorMulta(pagamento.getValorMulta());
		boletoAntecipado.setValorPago(pagamento.getValorPagamento());
		
		this.boletoAntecipadoRepository.merge(boletoAntecipado);
	}
	
	private Boleto gerarBaixaBoleto(TipoBaixaCobranca tipoBaixaCobranca, 
			                        PagamentoDTO pagamento, 
			                        Usuario usuario,
							        String nomeArquivo, 
							        Date dataOperacao,
							        Date dataNovoMovimento, 
							        ResumoBaixaBoletosDTO resumoBaixaBoletos,
							        Banco banco, 
							        Date dataPagamento) {
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
			validarDadosEntradaBaixaAutomatica(pagamento);
			
		} else {
			
			validarDadosEntradaBaixaManual(pagamento);
		}
		
		Boleto boleto = null;
		
		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
			
			//boleto = boletoRepository.obterPorNossoNumeroCompleto(pagamento.getNossoNumero(), null);
			
			boleto = boletoRepository.obterPorNossoNumero(pagamento.getNossoNumero(), null);
			
		} else {
		
			boleto = boletoRepository.obterPorNossoNumero(pagamento.getNossoNumero(), null);
		}		
		
        // Boleto não encontrado na base
		if (boleto == null) {
			
			// Verifica se boleto consta em boletos antecipados - em branco
			BoletoAntecipado boletoAntecipado = this.boletoAntecipadoRepository.obterBoletoAntecipadoPorNossoNumero(pagamento.getNossoNumero());  
			
			if (boletoAntecipado != null){
			
				boletoAntecipado.setTipoBaixa(tipoBaixaCobranca);
				
				this.gerarBaixaBoletoAntecipado(boletoAntecipado,
												pagamento,
												dataPagamento);
				
				return null;
			}
			else{
				
				if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {
				
					baixarBoletoNaoEncontrado(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
											  dataOperacao, boleto, resumoBaixaBoletos, banco, dataPagamento);
					
				} else {
					
                    throw new ValidacaoException(TipoMensagem.WARNING, "Boleto não encontrado!");
				}
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
		
		BigDecimal valorBoleto = MathUtil.round(boleto.getValor(), 2);
		BigDecimal valorPagamento = MathUtil.round(pagamento.getValorPagamento(), 2);
		
		// Boleto pago com valor correto
		if (valorPagamento.compareTo(valorBoleto) == 0) {
			
			baixarBoletoValorCorreto(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
									 dataOperacao, boleto, resumoBaixaBoletos, banco, dataPagamento);
			
			return boleto;
			
		} else if (valorPagamento.compareTo(valorBoleto) == 1) {
			
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
         * Gera baixa com status de pago, porém o nosso número referente ao
         * pagamento não existe na base
         */
		
		gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_BOLETO_NAO_ENCONTRADO, boleto,
						   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);
	}
	
	private void baixarBoletoJaPago(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
									Usuario usuario, String nomeArquivo, Date dataNovoMovimento,
									Date dataOperacao, Boleto boleto, ResumoBaixaBoletosDTO resumoBaixaBoletos,
									Banco banco, Date dataPagamento) {
		
		                /*
         * Não baixa o boleto, gera baixa com status de boleto pago
         * anteriormente e gera movimento de crédito
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
												  dataNovoMovimento, null));
	}
	
	private void baixarBoletoVencidoAutomatico(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
									 		   Usuario usuario, String nomeArquivo,
									 		   Date dataNovoMovimento,
									 		   Date dataOperacao, Boleto boleto,
									 		   ResumoBaixaBoletosDTO resumoBaixaBoletos,
									 		   Banco banco, Date dataPagamento) {
		
		BaixaCobranca baixaCobranca = null;
		
		                /*
         * Não baixa o boleto, gera baixa com status de não pago por divergência
         * de data e gera movimento de crédito
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
												  dataNovoMovimento, null));
			
			return;
		}
		
		                /*
         * Baixa o boleto, gera baixa com status de pago com divergência de
         * data, calcula multas e juros em cima do valor que deveria ser pago e
         * gera movimento de crédito ou débito se necessário
         */
		
		baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_DATA, boleto,
							 			   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);

		efetivarBaixaCobranca(boleto, dataOperacao);
		
		FormaCobranca formaCobrancaPrincipal = 
			this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
		
		BigDecimal valorJurosCalculado = 
				cobrancaService.calcularJuros(null, boleto.getCota().getId(),
											  boleto.getValor(), boleto.getDataVencimento(),
											  pagamento.getDataPagamento(),
											  formaCobrancaPrincipal);
		
		if (valorJurosCalculado.compareTo(BigDecimal.ZERO) == 1) {
			
			movimentoFinanceiroCotaService
				.gerarMovimentosFinanceirosDebitoCredito(
					getMovimentoFinanceiroCotaDTO(boleto.getCota(),
												  GrupoMovimentoFinaceiro.JUROS,
												  usuario, valorJurosCalculado,
												  dataOperacao, baixaCobranca,
												  dataNovoMovimento, null));
		}
		
		BigDecimal valorMultaCalculado = 
				cobrancaService.calcularMulta(null, boleto.getCota(),
											  boleto.getValor(),
											  formaCobrancaPrincipal);
		
		if (valorMultaCalculado.compareTo(BigDecimal.ZERO) == 1) {
			
			movimentoFinanceiroCotaService
				.gerarMovimentosFinanceirosDebitoCredito(
					getMovimentoFinanceiroCotaDTO(boleto.getCota(),
												  GrupoMovimentoFinaceiro.MULTA,
												  usuario, valorMultaCalculado,
												  dataOperacao, baixaCobranca,
												  dataNovoMovimento, null));
		}
		
		BigDecimal diferencaValor = null;
		
		String observacao = null;
		
		String dataPagamentoFormatada = DateUtil.formatarDataPTBR(dataPagamento);
		
		if (pagamento.getValorPagamento().compareTo(boleto.getValor()) == 1) {
		
			diferencaValor = pagamento.getValorPagamento().subtract(boleto.getValor());
			
            observacao = "Diferença de Pagamento a Maior (" + dataPagamentoFormatada + ")";
			
			movimentoFinanceiroCotaService
				.gerarMovimentosFinanceirosDebitoCredito(
					getMovimentoFinanceiroCotaDTO(boleto.getCota(),
												  GrupoMovimentoFinaceiro.CREDITO,
												  usuario, diferencaValor,
												  dataOperacao, baixaCobranca,
												  dataNovoMovimento, observacao));
			
		} else if (pagamento.getValorPagamento().compareTo(boleto.getValor()) == -1) {
			
			diferencaValor = boleto.getValor().subtract(pagamento.getValorPagamento());
			
            observacao = "Diferença de Pagamento a Maior (" + dataPagamentoFormatada + ")";
			
			movimentoFinanceiroCotaService
				.gerarMovimentosFinanceirosDebitoCredito(
					getMovimentoFinanceiroCotaDTO(boleto.getCota(),
											   	  GrupoMovimentoFinaceiro.DEBITO,
											   	  usuario, diferencaValor,
											   	  dataOperacao, baixaCobranca,
											   	  dataNovoMovimento, observacao));
		}
	}
	
	private void baixarBoletoVencidoManual(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
								 		   Usuario usuario, String nomeArquivo,
								 		   Date dataNovoMovimento,
								 		   Date dataOperacao, Boleto boleto,
								 		   ResumoBaixaBoletosDTO resumoBaixaBoletos,
								 		   Banco banco, Date dataPagamento) {
		
		                /*
         * Baixa o boleto, calcula o valor pago considerando multas, juros e
         * desconto, e gera baixa cobrança com o valor atualizado
         */
		
		gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_DATA, boleto,
						   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);

		efetivarBaixaCobranca(boleto, dataPagamento);
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
         * Verifica o parâmetro para pagamento a maior, não baixa o boleto, gera
         * baixa com status de não pago por divergência de valor e gera
         * movimento de crédito
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
							   						  dataNovoMovimento, null));
				
				return;
			}
		}
		
		                /*
         * Baixa o boleto, gera baixa com status de pago por divergência de
         * valor e gera movimento de crédito da diferença
         */
		
		baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_VALOR, boleto,
										   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);
		
		efetivarBaixaCobranca(boleto, dataOperacao);
		
		BigDecimal valorCredito = pagamento.getValorPagamento().subtract(boleto.getValor());
		
		String dataPagamentoFormatada = DateUtil.formatarDataPTBR(dataPagamento);
		
        String observacao = "Diferença de Pagamento a Maior (" + dataPagamentoFormatada + ")";
		
		movimentoFinanceiroCotaService
			.gerarMovimentosFinanceirosDebitoCredito(
				getMovimentoFinanceiroCotaDTO(boleto.getCota(),
											  GrupoMovimentoFinaceiro.CREDITO,
											  usuario, valorCredito,
											  dataOperacao, baixaCobranca,
											  dataNovoMovimento, observacao));
	}
	
	private void baixarBoletoValorAbaixo(TipoBaixaCobranca tipoBaixaCobranca, PagamentoDTO pagamento,
										 Usuario usuario, String nomeArquivo,
										 Date dataNovoMovimento, Date dataOperacao, Boleto boleto,
										 ResumoBaixaBoletosDTO resumoBaixaBoletos, Banco banco,
										 Date dataPagamento) {
		
		BaixaCobranca baixaCobranca = null;
		
		                /*
         * Verifica o parâmetro para pagamento a menor, não baixa o boleto, gera
         * baixa com status de não pago por divergência de valor e gera
         * movimento de crédito
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
							   					  dataNovoMovimento, null));
			
			return;
		}
		
		                /*
         * Baixa o boleto, gera baixa com status de pago por divergência de
         * valor e gera movimento de débito da diferença
         */

		baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_VALOR, boleto,
 				   						   dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);
		
		efetivarBaixaCobranca(boleto, dataOperacao);
		
		BigDecimal valorDebito = boleto.getValor().subtract(pagamento.getValorPagamento());

		String dataPagamentoFormatada = DateUtil.formatarDataPTBR(dataPagamento);
		
        String observacao = "Diferença de Pagamento a Menor (" + dataPagamentoFormatada + ")";
		
		movimentoFinanceiroCotaService
			.gerarMovimentosFinanceirosDebitoCredito(
				getMovimentoFinanceiroCotaDTO(boleto.getCota(),
											  GrupoMovimentoFinaceiro.DEBITO,
											  usuario, valorDebito,
											  dataOperacao, baixaCobranca,
											  dataNovoMovimento, observacao));
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
		}
		
		if (boleto != null) {

			this.acumuloDividasService.quitarDividasAcumuladas(dataPagamento, boleto.getDivida());
		}
			
		BigDecimal valorPagamento = BigDecimal.ZERO;
		
		if (pagamento.getValorPagamento() != null) {
			
			valorPagamento = pagamento.getValorPagamento();
		}

		BaixaManual baixaManual = new BaixaManual();
		
		baixaManual.setDataBaixa(dataBaixa);
		baixaManual.setDataPagamento(dataPagamento);
		baixaManual.setValorPago(valorPagamento);
		baixaManual.setCobranca(boleto);
		
		baixaManual.setResponsavel(usuario);
		baixaManual.setValorJuros(pagamento.getValorJuros());
		baixaManual.setValorMulta(pagamento.getValorMulta());
		baixaManual.setValorDesconto(pagamento.getValorDesconto());
		baixaManual.setStatus(StatusBaixa.PAGO);
		
		baixaCobrancaRepository.adicionar(baixaManual);
		
		return baixaManual;
	}
	
	private void efetivarBaixaCobranca(Boleto boleto, Date dataPagamento) {
		
		boleto.setDataPagamento(dataPagamento);
		boleto.setStatusCobranca(StatusCobranca.PAGO);
		boleto.getDivida().setStatus(StatusDivida.QUITADA);
		
		boletoRepository.alterar(boleto);
		
		this.pagarCobrancasRaizes(boleto.getDivida(), dataPagamento);	
	}

	private void pagarCobrancasRaizes(Divida divida, Date dataOperacao) {
		
		Divida dividaRaiz = null;
		
		if (divida != null) {
			
			dividaRaiz = divida.getDividaRaiz();
		}
		
		while (dividaRaiz != null) {
		
			dividaRaiz.setStatus(StatusDivida.QUITADA);
			
			dividaRaiz.getCobranca().setStatusCobranca(StatusCobranca.PAGO);
			dividaRaiz.getCobranca().setDataPagamento(dataOperacao);
			
			this.dividaRepository.alterar(dividaRaiz);
			
			dividaRaiz = dividaRaiz.getDividaRaiz();
		}
	}
	
	private MovimentoFinanceiroCotaDTO getMovimentoFinanceiroCotaDTO(Cota cota,
			GrupoMovimentoFinaceiro grupoMovimentoFinaceiro, Usuario usuario,
			BigDecimal valorPagamento, Date dataOperacao,
			BaixaCobranca baixaCobranca, Date dataNovoMovimento, String observacao) {

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
				
		movimentoFinanceiroCotaDTO.setObservacao(observacao);

		return movimentoFinanceiroCotaDTO;
	}
	
	private CorpoBoleto gerarCorpoBoletoCota(Boleto boleto, Pessoa pessoaCedente, boolean aceitaPagamentoVencido){
		
		String nossoNumero = boleto.getNossoNumero();
		String digitoNossoNumero = boleto.getDigitoNossoNumero();
		BigDecimal valor = boleto.getValor() != null ? boleto.getValor().abs() : BigDecimal.ZERO;
		Banco banco = boleto.getBanco();
		Date dataEmissao = boleto.getDataEmissao();
		Date dataVencimento = boleto.getDataVencimento();
		Pessoa pessoaSacado = boleto.getCota().getPessoa();
		
		Endereco endereco = null;
		
		Set<EnderecoCota> enderecosCota = boleto.getCota().getEnderecos();
		
		for(EnderecoCota enderecoCota : enderecosCota){
			
			endereco = enderecoCota.getEndereco();
			
			if (enderecoCota.getTipoEndereco() == TipoEndereco.COBRANCA){
				break;
			}
		
		}
		
		return geraCorpoBoleto(nossoNumero, 
				               digitoNossoNumero, 
				               valor, 
				               null,
				               null, 
				               banco, 
				               dataEmissao, 
				               dataVencimento, 
				               pessoaCedente, 
				               pessoaSacado, 
				               endereco, 
				               boleto.getTipoCobranca(),
				               boleto.getCota().getNumeroCota(),
				               aceitaPagamentoVencido,
				               false);
		
	}
	
	private CorpoBoleto gerarCorpoBoletoDistribuidor(BoletoDistribuidor boleto, Pessoa pessoaSacado, boolean aceitaPagamentoVencido) {
		
		String nossoNumero = boleto.getNossoNumeroDistribuidor();
		String digitoNossoNumero = boleto.getDigitoNossoNumeroDistribuidor();
		BigDecimal valor = boleto.getValor();
		Banco banco = boleto.getBanco();
		Date dataEmissao = boleto.getDataEmissao();
		Date dataVencimento = boleto.getDataVencimento();
		Pessoa pessoaCedente = boleto.getFornecedor().getJuridica();
		
		Endereco endereco = null;
		
		Set<EnderecoFornecedor> enderecosFornecedor = boleto.getFornecedor().getEnderecos();
		
		for(EnderecoFornecedor enderecoFornecedor : enderecosFornecedor){
			
			endereco = enderecoFornecedor.getEndereco();
			
			if (enderecoFornecedor.getTipoEndereco() == TipoEndereco.COBRANCA){
				break;
			}
		
		}
		
		return geraCorpoBoleto(nossoNumero, 
				               digitoNossoNumero, 
				               valor, 
				               null,
				               null,
				               banco, 
				               dataEmissao, 
				               dataVencimento, 
				               pessoaCedente, 
				               pessoaSacado,
				               endereco,
				               boleto.getTipoCobranca(),
				               null,
				               aceitaPagamentoVencido,
				               false);
	}

	/**
	 * Gera Corpos de Boleto em Branco
	 * @param boletosEmBrancoDTO
	 * @return List<CorpoBoleto>
	 */
	private List<CorpoBoleto> geraCorposBoletoEmBranco(List<BoletoEmBrancoDTO> boletosEmBrancoDTO){
		
		List<CorpoBoleto> corposBoleto = new ArrayList<CorpoBoleto>();
		
		for (BoletoEmBrancoDTO bbDTO : boletosEmBrancoDTO){

			corposBoleto.add(this.geraCorpoBoletoEmBranco(bbDTO));
		}
		
		return corposBoleto;
	}
	
	/**
	 * Gera corpo de Boleto em Branco
	 * @param bbDTO
	 * @return CorpoBoleto
	 */
	private CorpoBoleto geraCorpoBoletoEmBranco(BoletoEmBrancoDTO bbDTO){
		
		Cota cota = this.cotaRepository.obterPorNumeroDaCota(bbDTO.getNumeroCota());
		
		BoletoAntecipado boletoAntecipado = this.boletoAntecipadoRepository.buscarPorId(bbDTO.getIdBoletoAntecipado());
		
		Banco banco = boletoAntecipado.getBanco();
		
		Fornecedor fornecedor = boletoAntecipado.getFornecedor();

		CorpoBoleto corpoBoleto = this.geraCorpoBoletoEmBranco(cota, 
											                   fornecedor, 
											                   bbDTO.getData(), 
											                   bbDTO.getDataVencimento(),
											                   banco, 
											                   bbDTO.getValorTotalLiquido(),
											                   bbDTO.getValorTotalDebitos(),
											                   bbDTO.getValorTotalCreditos(),
											                   bbDTO.getNossoNumero(),
											                   bbDTO.getDigitoNossoNumero());
		
		return corpoBoleto;
	}
	
	/**
	 * Gera corpo de Boleto em Branco
	 * @param bbDTO
	 * @return nossoNumero
	 */
	private CorpoBoleto geraCorpoBoletoEmBranco(String nossoNumero){
		
		BoletoAntecipado boletoAntecipado = this.boletoAntecipadoRepository.obterBoletoAntecipadoPorNossoNumero(nossoNumero);
		
		Cota cota = this.cotaRepository.obterPorNumerDaCota(boletoAntecipado.getChamadaEncalheCota().getCota().getNumeroCota());

		Banco banco = boletoAntecipado.getBanco();
		
		Fornecedor fornecedor = boletoAntecipado.getFornecedor();

		CorpoBoleto corpoBoleto = this.geraCorpoBoletoEmBranco(cota, 
											                   fornecedor, 
											                   boletoAntecipado.getData(), 
											                   boletoAntecipado.getDataVencimento(),
											                   banco, 
											                   boletoAntecipado.getValor(),
											                   boletoAntecipado.getEmissaoBoletoAntecipado().getValorDebito(),
											                   boletoAntecipado.getEmissaoBoletoAntecipado().getValorCredito(),
											                   boletoAntecipado.getNossoNumero(),
											                   boletoAntecipado.getDigitoNossoNumero());
		
		return corpoBoleto;
	}

	/**
	 * Gera Corpo de Boleto em Branco
	 * @param cota
	 * @param fornecedor
	 * @param dataEmissao
	 * @param dataVencimento
	 * @param banco
	 * @param valorLiquido
	 * @param valorDebitos
	 * @param valorCreditos
	 * @param nossoNumero
	 * @param digitoNossoNumero
	 * @return CorpoBoleto
	 */
	private CorpoBoleto geraCorpoBoletoEmBranco(Cota cota,
			                                    Fornecedor fornecedor,
			                                    Date dataEmissao,
			                                    Date  dataVencimento,
			                                    Banco banco,
			                                    BigDecimal valorLiquido,
			                                    BigDecimal valorDebitos,
			                                    BigDecimal valorCreditos,
			                                    String nossoNumero,
			                                    String digitoNossoNumero){

		Pessoa pessoaSacado = cota.getPessoa();

        Pessoa pessoaCedente = fornecedor.getJuridica();
		
		Endereco enderecoSacado = null;
		
		Set<EnderecoFornecedor> enderecosFornecedor = fornecedor.getEnderecos();
		
		for(EnderecoFornecedor enderecoFornecedor : enderecosFornecedor){
			
			enderecoSacado = enderecoFornecedor.getEndereco();
			
			if (enderecoFornecedor.getTipoEndereco() == TipoEndereco.COBRANCA){
				
				break;
			}
		}
		
		TipoCobranca tipoCobranca = TipoCobranca.BOLETO;

		boolean aceitaPagamentoVencido = true;

		CorpoBoleto corpoBoleto = this.geraCorpoBoleto(nossoNumero, 
				                                       digitoNossoNumero, 
				                                       valorLiquido, 
				                                       valorDebitos,
				                                       valorCreditos,			                                       
				                                       banco, 
				                                       dataEmissao, 
				                                       dataVencimento, 
				                                       pessoaCedente, 
				                                       pessoaSacado, 
				                                       enderecoSacado, 
				                                       tipoCobranca, 
				                                       cota.getNumeroCota(), 
				                                       aceitaPagamentoVencido, 
				                                       true);
		
		return corpoBoleto;
	}

	        /**
     * Método responsável por gerar corpo do boleto com os atributos definidos
     * 
     * @param nossoNumero
     * @param digitoNossoNumero
     * @param valorDocumento
     * @param valorAcrescimo
     * @param valorDesconto
     * @param banco
     * @param dataEmissao
     * @param dataVencimento
     * @param pessoaCedente
     * @param pessoaSacado
     * @param enderecoSacado
     * @param tipoCobranca
     * @param numeroCota
     * @param aceitaPagamentoVencido
     * @param boletoEmBranco
     * @return GeradorBoleto: corpo do boleto carregado
     */
	private CorpoBoleto geraCorpoBoleto(String nossoNumero,
										String digitoNossoNumero,
										BigDecimal valorDocumento,
										BigDecimal valorAcrescimo,
										BigDecimal valorDesconto,
										Banco banco,
										Date dataEmissao,
										Date dataVencimento,
										Pessoa pessoaCedente, 
										Pessoa pessoaSacado,
										Endereco enderecoSacado,
										TipoCobranca tipoCobranca, 
										Integer numeroCota,
										boolean aceitaPagamentoVencido,
										boolean boletoEmBranco){

		valorDocumento = (valorDocumento == null) ? BigDecimal.ZERO : valorDocumento.abs();
		
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
		
		if(numeroCota != null && numeroCota >0){
			corpoBoleto.setSacadoNome(numeroCota + " - "+ nomeSacado);
		}else{
			corpoBoleto.setSacadoNome(nomeSacado);
		}
		          
		corpoBoleto.setSacadoDocumento(documentoSacado); 

		
		//ENDERECO DO SACADO
		
		if (enderecoSacado!=null){
			corpoBoleto.setEnderecoSacadoUf(enderecoSacado.getUf());            
			corpoBoleto.setEnderecoSacadoLocalidade(enderecoSacado.getCidade());     
			corpoBoleto.setEnderecoSacadoCep(enderecoSacado.getCep());         
			corpoBoleto.setEnderecoSacadoBairro(enderecoSacado.getBairro()); 
			corpoBoleto.setEnderecoSacadoLogradouro(enderecoSacado.getTipoLogradouro() + " " + enderecoSacado.getLogradouro()); 
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
        
        corpoBoleto.setTituloValor(valorDocumento.setScale(2, RoundingMode.HALF_EVEN));   
        corpoBoleto.setTituloDataDoDocumento(dataEmissao);   
        corpoBoleto.setTituloDataDoVencimento(dataVencimento);  
        
        BigDecimal valorParaPagamentosVencidos = null;
        
        if (!aceitaPagamentoVencido) {
        	
        	valorParaPagamentosVencidos = BigDecimal.ZERO;
        	
        	valorDesconto = BigDecimal.ZERO;
        	
        	valorAcrescimo = BigDecimal.ZERO;
        } 
        
        if (boletoEmBranco){
        	
        	valorDesconto = valorDesconto.compareTo(BigDecimal.ZERO)>0?valorDesconto:null;
        	
        	valorAcrescimo = valorAcrescimo.compareTo(BigDecimal.ZERO)>0?valorAcrescimo:null;
        }
        
        corpoBoleto.setTituloDesconto(valorDesconto);
        corpoBoleto.setTituloDeducao(valorParaPagamentosVencidos);
        corpoBoleto.setTituloMora(valorParaPagamentosVencidos);
        corpoBoleto.setTituloAcrecimo(valorAcrescimo);
        corpoBoleto.setTituloValorCobrado(valorParaPagamentosVencidos);
        

        // INFORMAÇOES DO BOLETO
        //PARAMETROS ?
        corpoBoleto
                .setBoletoLocalPagamento("Pagável em qualquer agência bancária até o vencimento. Não receber após o vencimento.");
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
	 * @throws ValidationException 
	 */
	private byte[]  gerarAnexoBoleto(Boleto boleto) throws IOException, ValidationException {
		
		Pessoa pessoaCedente = this.distribuidorRepository.juridica(); 
		
		boolean aceitaPagamentoVencido = this.distribuidorRepository.aceitaBaixaPagamentoVencido();
		
		GeradorBoleto geradorBoleto = new GeradorBoleto(this.gerarCorpoBoletoCota(boleto, pessoaCedente, aceitaPagamentoVencido));
		
		byte[] b = geradorBoleto.getBytePdf();
        
		return b;
	}
	
	/**
	 * 
	 * @param boletoAntecipado
	 * @return f: Boleto PDF em File.
	 * @throws IOException
	 * @throws ValidationException 
	 */
	private byte[] gerarAnexoBoletoEmBranco(BoletoAntecipado boletoAntecipado) throws IOException, ValidationException {
		
		GeradorBoleto geradorBoleto = new GeradorBoleto(this.geraCorpoBoletoEmBranco(boletoAntecipado.getNossoNumero()));
		
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
			
			byte[] anexo = null;
			
			Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero,null);
			
			Cota cota = null;
			
            if (boleto == null){
				
            	BoletoAntecipado boletoAntecipado = this.boletoAntecipadoRepository.obterBoletoAntecipadoPorNossoNumero(nossoNumero);
            	
            	cota = boletoAntecipado.getChamadaEncalheCota().getCota();
            	
				anexo = this.gerarAnexoBoletoEmBranco(boletoAntecipado);
			}
            else{
			
            	cota = boleto.getCota();
            	
			    anexo = this.gerarAnexoBoleto(boleto);
            }
			
			if(cota.getPessoa().getEmail()==null){
		
                throw new ValidacaoException(TipoMensagem.WARNING, "Cota não possui email cadastrado.");
			}
			
			String[] destinatarios = new String[]{cota.getPessoa().getEmail()};
						
			String assunto = this.distribuidorRepository.assuntoEmailCobranca();
			
			String mensagem = this.distribuidorRepository.mensagemEmailCobranca();
			
			email.enviar(assunto == null ? "" : assunto, 
					     mensagem == null ? "" : mensagem, 
					     destinatarios, 
					     new AnexoEmail("Boleto-"+nossoNumero, anexo,TipoAnexo.PDF),
					     true);
		
		} catch(AutenticacaoEmailException e){
			
			LOGGER.error(e.getMessage(), e);
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao conectar-se com o servidor de e-mail. ");
			
		} catch(ValidacaoException e){
			
			LOGGER.error(e.getMessage(), e);
			
			throw e;
			
		}catch(Exception e){
			
			LOGGER.error(e.getMessage(), e);
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Erro no envio. ");
		}
	}
	
	        /**
     * Método responsável por gerar impressao em formato PDF
     * 
     * @param nossoNumero
     * @return b: Boleto PDF em Array de bytes
     * @throws IOException
     * @throws ValidationException
     */
	@Override
	@Transactional
	public byte[] gerarImpressaoBoleto(String nossoNumero) throws IOException, ValidationException {
		
		Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero,null);
		
		if (boleto == null){
			
			return null;
		}
		
		Pessoa pessoaCedente = this.distribuidorRepository.juridica(); 
		
		boolean aceitaPagamentoVencido = this.distribuidorRepository.aceitaBaixaPagamentoVencido();
		
		GeradorBoleto geradorBoleto = new GeradorBoleto(this.gerarCorpoBoletoCota(boleto, pessoaCedente, aceitaPagamentoVencido));
		
		byte[] b = geradorBoleto.getBytePdf();
		
		cobrancaRepository.atualizarVias(boleto);
		
        return b;
	}
	
	        /**
     * Método responsável pela busca de dados referentes à cobrança
     * 
     * @param nossoNumero
     * @return CobrancaVO: dados da cobrança
     * @throws ValidationException
     */
	@Override
	@Transactional(readOnly=true)
	public byte[] gerarImpressaoBoletos(Collection<String> nossoNumeros) throws IOException, ValidationException {
		
		List<CorpoBoleto> corpos = new ArrayList<CorpoBoleto>();
		
		Boleto boleto = null;
		
		Pessoa pessoaCedente = this.distribuidorRepository.juridica(); 
		
		boolean aceitaPagamentoVencido = this.distribuidorRepository.aceitaBaixaPagamentoVencido();
		
		for(String nossoNumero  : nossoNumeros){
			
			boleto = boletoRepository.obterPorNossoNumero(nossoNumero,null);

			if(boleto!= null){
				corpos.add(this.gerarCorpoBoletoCota(boleto, pessoaCedente, aceitaPagamentoVencido));
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
	public byte[] gerarImpressaoBoletosDistribuidor(List<BoletoDistribuidor> listaBoletoDistribuidor) throws IOException, ValidationException {
		
		List<CorpoBoleto> corpos = new ArrayList<CorpoBoleto>();
		
		Pessoa pessoaSacado = this.distribuidorRepository.juridica();
		
		boolean aceitaPagamentoVencido = this.distribuidorRepository.aceitaBaixaPagamentoVencido();

		for(BoletoDistribuidor boletoDistribuidor  : listaBoletoDistribuidor){
				corpos.add(this.gerarCorpoBoletoDistribuidor(boletoDistribuidor, pessoaSacado, aceitaPagamentoVencido));
		}
		
		if(!corpos.isEmpty()){
			GeradorBoleto geradorBoleto = new GeradorBoleto(corpos) ;
			byte[] b = geradorBoleto.getByteGroupPdf();
	        return b;
		}
		return null;
	}
	
	        /**
     * Método responsável por gerar impressao de Boleto Antecipado (Em Branco)
     * em formato PDF
     * 
     * @param nossoNumero
     * @return b: Boleto PDF em Array de bytes
     * @throws IOException
     * @throws ValidationException
     */
	@Override
	@Transactional
	public byte[] gerarImpressaoBoletoEmBranco(String nossoNumero) throws IOException, ValidationException {
		
		BoletoAntecipado boletoAntecipado = this.boletoAntecipadoRepository.obterBoletoAntecipadoPorNossoNumero(nossoNumero);
		
		if (boletoAntecipado == null){
			
			return null;
		}

		GeradorBoleto geradorBoleto = new GeradorBoleto(this.geraCorpoBoletoEmBranco(nossoNumero));

		byte[] b = geradorBoleto.getBytePdf();
		
		boletoAntecipado.setVias(boletoAntecipado.getVias() + 1);
		
		this.boletoAntecipadoRepository.merge(boletoAntecipado);
		
        return b;
	}
	
	        /**
     * Gera Impressão de Boletos em Branco apenas para a impressão - Sem
     * Cobrança e Sem Financeiro Cadastrado
     * 
     * @param boletosEmBrancoDTO
     * @return byte[]
     * @throws ValidationException
     */
	@Transactional
	@Override
	public byte[] geraImpressaoBoletosEmBranco(List<BoletoEmBrancoDTO> boletosEmBrancoDTO) throws ValidationException{
		
		if (boletosEmBrancoDTO==null){
			
			return null;
		}
		
		List<CorpoBoleto> corposBoleto = this.geraCorposBoletoEmBranco(boletosEmBrancoDTO);
		
		if(!corposBoleto.isEmpty()){
			
			GeradorBoleto geradorBoleto = new GeradorBoleto(corposBoleto) ;
			
			byte[] b = geradorBoleto.getByteGroupPdf();
			
	        return b;
		}
		
		return null;
	}
	
	        /**
     * Método responsável por obter os dados de uma cobrança
     * 
     * @param nossoNumero
     * @param dataPagamento
     * @return CobrancaVO: dados da cobrança
     */
	@Override
	@Transactional(readOnly=true)
	public CobrancaVO obterDadosBoletoPorNossoNumero(String nossoNumero, Date dataPagamento) {
		
		CobrancaVO cobrancaVO = null;
		
		Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero,null);
		if (boleto!=null){
		    cobrancaVO = this.cobrancaService.obterDadosCobranca(boleto.getId(), dataPagamento);
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

	@Override
	@Transactional(readOnly = true)
	public List<BoletoCotaDTO> verificaEnvioDeEmail(List<BoletoCotaDTO> boletosDTO) {
		
		for(BoletoCotaDTO boletoDTO : boletosDTO){
			
			if (boletoDTO.isBoletoAntecipado()){
				
				if (boletoDTO.getStatusDivida().equals(StatusDivida.BOLETO_ANTECIPADO_EM_ABERTO)){
					
					boletoDTO.setStatusCobranca(StatusCobranca.NAO_PAGO.name());
				}

				continue;
			}
			
			Long verificaSeRecebeEmail = this.boletoRepository.verificaEnvioDeEmail(boletoDTO.getNossoNumero());
			
			if(verificaSeRecebeEmail.intValue() == 0){
				
				boletoDTO.setRecebeCobrancaEmail(false);
			}
		}
		
		return boletosDTO;
	}
	
	        /**
     * Obtém o fornecedor padrão da cota para cobrança.
     * 
     * @param cota Cota que deseja obter o fornecedor.
     * 
     * @return Fornecedor padrão da cota.
     */
	private Fornecedor obterFornecedorPadraoCota(Cota cota) {
		
		Fornecedor fornecedor = cota.getParametroCobranca() == null ? 
										null : cota.getParametroCobranca().getFornecedorPadrao();

		if (fornecedor == null && cota.getFornecedores() != null && !cota.getFornecedores().isEmpty()) {
			
			if (cota.getFornecedores()!=null && !cota.getFornecedores().isEmpty()) {
				
				fornecedor = cota.getFornecedores().iterator().next();
				
			}
			
		}

		return fornecedor;
	}

	/**
	 * Obtem dados de boleto em Branco utilizando dados de CE e periodo de recolhimento do filtro
	 * @param ceDTO
	 * @param dataRecolhimentoCEDe
	 * @param dataRecolhimentoCEAte
	 * @return BoletoEmBrancoDTO
	 */
	@Transactional
	@Override
	public BoletoEmBrancoDTO obterDadosBoletoEmBrancoPorCE(CotaEmissaoDTO ceDTO, 
			                                               Date dataRecolhimentoCEDe, 
			                                               Date dataRecolhimentoCEAte){
		
		Date dataEmissao = DateUtil.parseDataPTBR(ceDTO.getDataEmissao());
		
		Date dataRecolhimento = DateUtil.parseDataPTBR(ceDTO.getDataRecolhimento());
		
		BigDecimal valorReparteLiquidoCE = CurrencyUtil.getBigDecimal(ceDTO.getVlrReparteLiquido()).setScale(2, RoundingMode.HALF_UP);
		
		BigDecimal valorEncalheCE = CurrencyUtil.getBigDecimal(ceDTO.getVlrEncalhe()).setScale(2, RoundingMode.HALF_UP);
		
		BigDecimal valorTotalLiquidoCE = CurrencyUtil.getBigDecimal(ceDTO.getVlrTotalLiquido()).setScale(2, RoundingMode.HALF_UP);
		
		BigDecimal valorDebitos = null;//this.debitoCreditoCotaService.obterTotalDebitoCota(ceDTO.getNumCota(), dataEmissao);
		
		BigDecimal valorCreditos = null;//this.debitoCreditoCotaService.obterTotalCreditoCota(ceDTO.getNumCota(), dataEmissao);
		
		valorDebitos = (valorDebitos!=null?valorDebitos.setScale(2, RoundingMode.HALF_UP):BigDecimal.ZERO);
		
		valorCreditos = (valorCreditos!=null?valorCreditos.setScale(2, RoundingMode.HALF_UP):BigDecimal.ZERO);
		
		BigDecimal valorTotalBoletoEmBranco = valorTotalLiquidoCE.add(valorDebitos.subtract(valorCreditos)).setScale(2, RoundingMode.HALF_UP);
		
		Fornecedor fornecedor = this.paramtroCobrancaCotaService.obterFornecedorPadraoCota(ceDTO.getIdCota());
		
		if (fornecedor == null){
			
			return null;
		}
		
		FormaCobranca fc = this.formaCobrancaService.obterFormaCobrancaCota(ceDTO.getIdCota(), fornecedor.getId(), dataEmissao, valorTotalBoletoEmBranco);
		
		if (fc == null || !fc.getTipoCobranca().equals(TipoCobranca.BOLETO_EM_BRANCO)){
			
			return null;
		}
		
		Date dataVencimento = this.gerarCobrancaService.obterDataVencimentoCobrancaCota(dataEmissao, fc.getParametroCobrancaCota().getFatorVencimento());
		
		BoletoEmBrancoDTO bbDTO = new BoletoEmBrancoDTO(ceDTO.getIdChamEncCota(),
				                                        fornecedor.getId(),
				                                        fc.getBanco()!=null?fc.getBanco().getNumeroBanco():null,
				                                        fc.getBanco()!=null?fc.getBanco().getAgencia():null,
				                                        fc.getBanco()!=null?fc.getBanco().getConta():null,
				                                        fc.getBanco()!=null?fc.getBanco().getDvConta():null,
				                                        ceDTO.getNumCota(),
				                                        valorReparteLiquidoCE,
				                                        valorEncalheCE,
										                valorTotalLiquidoCE, 				               
										                valorDebitos, 
										                valorCreditos, 
										                dataEmissao, 
										                dataRecolhimento,
										                dataVencimento,
										                dataRecolhimentoCEDe,
										                dataRecolhimentoCEAte);

		return bbDTO;
	}
	
	/**
	 * Obtem BoletoAntecipado por ChamadaEncalheCota e Periodo de Recolhimento selecionado na Emissao
	 * @param idCE
	 * @param dataRecolhimentoCEDe
	 * @param dataRecolhimentoCEAte
	 * @return BoletoAntecipado
	 */
	private BoletoAntecipado obterBoletoAntecipadoPorCECotaEPeriodoRecolhimento(Long idCE, 
																	            Date dataRecolhimentoCEDe, 
																	            Date dataRecolhimentoCEAte) {
		
        List<StatusDivida> listaStatusDivida = Arrays.asList(StatusDivida.BOLETO_ANTECIPADO_EM_ABERTO, StatusDivida.QUITADA);
		
		BoletoAntecipado boletoAntecipado = this.boletoAntecipadoRepository.obterBoletoAntecipadoPorCECotaEPeriodoRecolhimento(idCE, 
				                                                                                                               dataRecolhimentoCEDe, 
				                                                                                                               dataRecolhimentoCEAte, 
				                                                                                                               listaStatusDivida);
		
		return boletoAntecipado;
	}
	
	        /**
     * Obtem valor inicial da faixa de número reservada para a geração de Nosso
     * Numero de boletos antecipados (Em Branco) Somado ao atributo
     * idBoletoAntecipado para a composição do Nosso Número Considera o tamanho
     * aceitável para cada banco
     * 
     * @param numeroBanco
     * @return long
     */
	private long obterInicioNumeroReservadoNossoNumeroBoletoAntecipado(String numeroBanco){
		
		NomeBanco nomeBanco = NomeBanco.getByNumeroBanco(numeroBanco);
		
		switch (nomeBanco) {
		
		    case BANCO_BRADESCO: 
		    case BANCO_ITAU:
		    case HSBC:
		    	
				return 99000000;
			
			case BANCO_ABN_AMRO_REAL:
			case BANCO_DO_BRASIL:
			case BANCO_DO_ESTADO_DO_ESPIRITO_SANTO:
			case BANCO_DO_ESTADO_DO_RIO_GRANDE_DO_SUL:
			case BANCO_DO_NORDESTE_DO_BRASIL:
			case BANCO_INTEMEDIUM:
			case BANCO_RURAL:
			case BANCO_SAFRA:
			case BANCO_SANTANDER:
			case BANCO_SICREDI:	
			case BANCOOB:
			case CAIXA_ECONOMICA_FEDERAL:
			case MERCANTIL_DO_BRASIL:
			case NOSSA_CAIXA:	
			case UNIBANCO:
	
				return 990000;
	
			default:
	
				return 99000000;
		}
	}
	
	        /**
     * Atualiza os campos Nosso Número e Dígito Nosso Número em
     * BoletoEmBrancoDTO conforme BoletoAntecipado persistido
     * 
     * @param bbDTO
     * @param cota
     * @param fornecedor
     * @param dataEmissao
     * @param dataVencimento
     * @param banco
     * @param valor
     */
	private void gerarNossoNumeroBoletoEmBrancoDTO(BoletoEmBrancoDTO bbDTO,
									               Cota cota,
									               Fornecedor fornecedor,
									               Date dataEmissao,
									               Date  dataVencimento,
									               Banco banco,
									               BigDecimal valor){
		
		long numeroBancoBoletoAntecipado = this.obterInicioNumeroReservadoNossoNumeroBoletoAntecipado(banco.getNumeroBanco());
		
		String nossoNumero = Util.gerarNossoNumero(cota.getNumeroCota(), 
                                                   dataEmissao, 
                                                   banco!=null?banco.getNumeroBanco():"0",
									               fornecedor != null ? fornecedor.getId() : null,
									               numeroBancoBoletoAntecipado + bbDTO.getIdBoletoAntecipado(),
									               banco!=null?banco.getAgencia():0,
									               banco!=null?banco.getConta():0,
									               banco!=null?banco.getCarteira():0);

        String digitoNossoNumero = Util.calcularDigitoVerificador(nossoNumero, 
                                                                  banco!=null?banco.getCodigoCedente():"0", 
                                                                  dataVencimento);
        
        bbDTO.setNossoNumero(nossoNumero);
        
        bbDTO.setDigitoNossoNumero(digitoNossoNumero);
	}
	
	/**
	 * Salva Boleto Antecipado - Em Branco
	 * @param bbDTO
	 */
	@Transactional
	@Override
    public void salvaBoletoAntecipado(BoletoEmBrancoDTO bbDTO){
		
		BoletoAntecipado boletoAntecipado = this.obterBoletoAntecipadoPorCECotaEPeriodoRecolhimento(bbDTO.getIdChamadaEncalheCota(),
				                                                                                    bbDTO.getDataRecolhimentoCEDe(),
				                                                                                    bbDTO.getDataRecolhimentoCEAte());
		
		if (boletoAntecipado==null){
		
    	    boletoAntecipado = new BoletoAntecipado();
		}    
		
    	ChamadaEncalheCota chamadaEncalheCota = this.chamadaEncalheCotaRepository.buscarPorId(bbDTO.getIdChamadaEncalheCota());
    	
    	Fornecedor fornecedor = this.fornecedorRepository.buscarPorId(bbDTO.getIdFornecedor());
    	
    	Banco banco = this.bancoRepository.obterBanco(bbDTO.getNumeroBanco(), 
                									  bbDTO.getNumeroAgencia(), 
                                                      Long.parseLong((bbDTO.getNumeroConta().toString() + bbDTO.getDigitoVerificadorConta())));
    	
    	Usuario usuario = this.usuarioService.getUsuarioLogado();
    	
    	boletoAntecipado.setUsuario(usuario);
    	boletoAntecipado.setChamadaEncalheCota(chamadaEncalheCota);
    	boletoAntecipado.setFornecedor(fornecedor);
    	boletoAntecipado.setBanco(banco);
    	boletoAntecipado.setData(bbDTO.getData());
    	boletoAntecipado.setDataVencimento(bbDTO.getDataVencimento());
    	boletoAntecipado.setNossoNumero("0");
    	boletoAntecipado.setDigitoNossoNumero("0");
    	boletoAntecipado.setEmissaoBoletoAntecipado(new EmissaoBoletoAntecipado());
    	boletoAntecipado.getEmissaoBoletoAntecipado().setValorDebito(bbDTO.getValorTotalDebitos());
    	boletoAntecipado.getEmissaoBoletoAntecipado().setValorCredito(bbDTO.getValorTotalCreditos());
    	boletoAntecipado.getEmissaoBoletoAntecipado().setValorReparte(bbDTO.getValorReparteLiquido());
    	boletoAntecipado.getEmissaoBoletoAntecipado().setValorEncalhe(bbDTO.getValorEncalhe());
    	boletoAntecipado.getEmissaoBoletoAntecipado().setValorCe(bbDTO.getValorTotalLiquido());
    	boletoAntecipado.getEmissaoBoletoAntecipado().setDataRecolhimentoCEDe(bbDTO.getDataRecolhimentoCEDe());
    	boletoAntecipado.getEmissaoBoletoAntecipado().setDataRecolhimentoCEAte(bbDTO.getDataRecolhimentoCEAte());
    	boletoAntecipado.setValor(bbDTO.getValorTotalLiquido().add(bbDTO.getValorTotalDebitos().subtract(bbDTO.getValorTotalCreditos())));
    	boletoAntecipado.setStatus(StatusDivida.BOLETO_ANTECIPADO_EM_ABERTO);
    	boletoAntecipado.setTipoCobranca(TipoCobranca.BOLETO_EM_BRANCO);
    	
    	if (boletoAntecipado.getId()==null){
    	
    		boletoAntecipado.setVias(1);
    		
    	    this.boletoAntecipadoRepository.adicionar(boletoAntecipado);
    	}
    	else{
    		
    		int vias = boletoAntecipado.getVias();
    		
    		boletoAntecipado.setVias(vias + 1);
    		
    		this.boletoAntecipadoRepository.merge(boletoAntecipado);
    	}
    	
        Cota cota = this.cotaRepository.obterPorNumeroDaCota(bbDTO.getNumeroCota());
        
        bbDTO.setIdBoletoAntecipado(boletoAntecipado.getId());
        
        this.gerarNossoNumeroBoletoEmBrancoDTO(bbDTO,
        		                               cota, 
        		                               fornecedor, 
        		                               bbDTO.getData(), 
        		                               bbDTO.getDataVencimento(), 
        		                               banco, 
        		                               boletoAntecipado.getValor());
        
        boletoAntecipado.setNossoNumero(bbDTO.getNossoNumero());
        
        boletoAntecipado.setDigitoNossoNumero(bbDTO.getDigitoNossoNumero());
        
        this.boletoAntecipadoRepository.merge(boletoAntecipado);
        
        this.atualizaBoletosEmitidosNoPeriodoDaNovaEmissao(boletoAntecipado,
        		                                           bbDTO.getNumeroCota(), 
											               bbDTO.getDataRecolhimentoCEDe(), 
											               bbDTO.getDataRecolhimentoCEAte());
	}
	
    /**
     * Salva Boletos Antecipados - Em Branco
     * @param listaBbDTO
     */
	@Transactional
	@Override
	public void salvaBoletosAntecipado(List<BoletoEmBrancoDTO> listaBbDTO){
		
		for (BoletoEmBrancoDTO bbDTO:listaBbDTO){
			
			this.salvaBoletoAntecipado(bbDTO);
		}
	}
	
	        /**
     * Verifica se existe boleto antecipado para a cota Data de recolhimento
     * dentro do periodo de emissao CE do Boleto antecipado Boletos em Branco
     * sem reimpressão
     * 
     * @param idCota
     * @param dataRecolhimento
     * @return boolean
     */
	@Transactional
	@Override
	public boolean existeBoletoAntecipadoCotaDataRecolhimento(Long idCota, Date dataRecolhimento){
		
		List<StatusDivida> listaStatusDivida = Arrays.asList(StatusDivida.BOLETO_ANTECIPADO_EM_ABERTO, StatusDivida.QUITADA);
		
		List<BoletoAntecipado> bas = this.boletoAntecipadoRepository.obterBoletosAntecipadosPorDataRecolhimentoECota(idCota, dataRecolhimento, listaStatusDivida);
		
		if (bas==null || bas.isEmpty()){
			
			return false;
		}
		
		for (BoletoAntecipado ba : bas){
			
			if (ba.getEmissaoBoletoAntecipado().getBoletoAntecipadoReimpresso() == null){
				
				return true;
			}
		}
		
		return false;
	}
	
	        /**
     * Atualiza boletos antecipados por periodo de recolhimento da CE e numero
     * cota - re-emissao de CE Adiciona referência do novo Boleto Impresso nos
     * boletos ja emitidos no mesmo período Atualiza apenas boletos qua nao
     * possuem atualização anterior
     * 
     * @param novoBoletoAntecipado
     * @param numeroCota
     * @param dataRecolhimentoDe
     * @param dataRecolhimentoAte
     */
	private void atualizaBoletosEmitidosNoPeriodoDaNovaEmissao(BoletoAntecipado novoBoletoAntecipado,
			                                                   Integer numeroCota,
			                                                   Date dataRecolhimentoDe,
			                                                   Date dataRecolhimentoAte){
		
		List<BoletoAntecipado> bas = this.boletoAntecipadoRepository.obterBoletosAntecipadosPorPeriodoRecolhimentoECota(numeroCota,
				                                                                                                        numeroCota,  
				                                                                                                        dataRecolhimentoDe, 
				                                                                                                        dataRecolhimentoAte);
		
        for (BoletoAntecipado ba: bas){
        	
        	if (ba.getEmissaoBoletoAntecipado().getBoletoAntecipadoReimpresso() == null && !ba.getId().equals(novoBoletoAntecipado.getId())){
 
    		    ba.getEmissaoBoletoAntecipado().setBoletoAntecipadoReimpresso(novoBoletoAntecipado);
    		
    		    this.boletoAntecipadoRepository.merge(ba);
        	}
        }
	}
	
	/**
	 * Verifica se existe Boleto Antecipado emitido para a faixa de cotas no periodo de recolhimento
	 * @param numeroCotaDe
	 * @param numeroCotaAte
	 * @param dataRecolhimentoDe
	 * @param dataRecolhimentoAte
	 * @return boolean
	 */
	@Transactional(readOnly=true)
	@Override
	public boolean existeBoletoAntecipadoPeriodoRecolhimentoECota(Integer numeroCotaDe,
																  Integer numeroCotaAte,
														          Date dataRecolhimentoDe,
														          Date dataRecolhimentoAte){
		
		List<BoletoAntecipado> bas = this.boletoAntecipadoRepository.obterBoletosAntecipadosPorPeriodoRecolhimentoECota(numeroCotaDe,
				                                                                                                        numeroCotaAte,
																										                dataRecolhimentoDe, 
																										                dataRecolhimentoAte);
		
		return (bas!=null && bas.size()>0);
	}
	
	        /**
     * Método responsável por obter boleto Antecipado (Em Branco) por
     * nossoNumero
     * 
     * @param nossoNumero
     * @return Boletos encontrado
     */
	@Override
	@Transactional(readOnly=true)
	public BoletoAntecipado obterBoletoEmBrancoPorNossoNumero(String nossoNumero) {
		
		return boletoAntecipadoRepository.obterBoletoAntecipadoPorNossoNumero(nossoNumero);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly=true)
	public BoletoAntecipado obterBoletoEmBrancoPorId(Long idBoletoAntecipado) {

		return this.boletoAntecipadoRepository.buscarPorId(idBoletoAntecipado);
	}
	
	
}
