package br.com.abril.nds.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CalculaParcelasVO;
import br.com.abril.nds.client.vo.NegociacaoDividaDetalheVO;
import br.com.abril.nds.dto.DiaSemanaDTO;
import br.com.abril.nds.dto.ImpressaoNegociacaoDTO;
import br.com.abril.nds.dto.ImpressaoNegociacaoParecelaDTO;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.NegociacaoDividaPaginacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCalculaParcelas;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.CobrancaCheque;
import br.com.abril.nds.model.financeiro.CobrancaDeposito;
import br.com.abril.nds.model.financeiro.CobrancaDinheiro;
import br.com.abril.nds.model.financeiro.CobrancaTransferenciaBancaria;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.ParcelaNegociacao;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.repository.ParcelaNegociacaoRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class NegociacaoDividaServiceImpl implements NegociacaoDividaService {
	
	private static final int DEFAULT_SCALE = 2;


	@Autowired
	private NegociacaoDividaRepository negociacaoDividaRepository;

	@Autowired
	private CobrancaRepository cobrancaRepository;

	@Autowired
	private DividaRepository dividaRepository;

	@Autowired
	private CotaRepository cotaRepository;

	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;

	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;

	@Autowired
	private ParcelaNegociacaoRepository parcelaNegociacaoRepository;

	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;

	@Autowired
	private FormaCobrancaRepository formaCobrancaRepository;

	@Autowired
	private DocumentoCobrancaService documentoCobrancaService;

	@Autowired
	private BancoRepository bancoRepository;

	@Autowired
	private DescontoService descontoService;

	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;

	@Autowired
	private BancoService bancoService;
	
	@Autowired
	private CobrancaService cobrancaService;
	
	@Autowired
	private DistribuidorService distribuidorService;	
	
	@Autowired 
	private CalendarioService calendarioService;
	
	@Autowired
	private FormaCobrancaService formaCobrancaService;
	

	@Override
	@Transactional(readOnly = true)
	public NegociacaoDividaPaginacaoDTO obterDividasPorCotaPaginado(
			FiltroConsultaNegociacaoDivida filtro) {

		NegociacaoDividaPaginacaoDTO retorno = new NegociacaoDividaPaginacaoDTO();

		retorno.setListaNegociacaoDividaDTO(this.obterDividasPorCota(filtro));
		retorno.setQuantidadeRegistros(this.negociacaoDividaRepository
				.obterCotaPorNumeroCount(filtro));

		return retorno;
	}

	@Override
	@Transactional(readOnly = true)
	public List<NegociacaoDividaDTO> obterDividasPorCota(
			FiltroConsultaNegociacaoDivida filtro) {

		List<NegociacaoDividaDTO> dividas = this.negociacaoDividaRepository
				.obterNegociacaoPorCota(filtro);
		Cota cota = cotaRepository.obterPorNumerDaCota(filtro.getNumeroCota());
		Date data = DateUtil.removerTimestamp(new Date());

		FormaCobranca formaCobrancaPrincipal = 
			this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
		
		for (NegociacaoDividaDTO divida : dividas) {
			
			if (divida.getPrazo() != 0){

				Banco banco = this.bancoRepository.buscarBancoPorIdCobranca(divida
						.getIdCobranca());
				BigDecimal encargo = (divida.getEncargos()!=null)?divida.getEncargos():BigDecimal.ZERO;
	
				encargo = encargo.add(cobrancaService.calcularJuros(banco,
						cota.getId(),
						divida.getVlDivida(), divida.getDtVencimento(), data, formaCobrancaPrincipal));
	
				
				if (divida.getDtVencimento().compareTo(data) < 0) {
					encargo = encargo.add(cobrancaService.calcularMulta(banco,
							cota, divida.getVlDivida(), formaCobrancaPrincipal));
				}
				divida.setEncargos(encargo);
				divida.setTotal(divida.getVlDivida().add(encargo));
			} else {
				
				divida.setEncargos(BigDecimal.ZERO);
				divida.setTotal(divida.getVlDivida());
			}
		}

		return dividas;
	}

	@Override
	@Transactional
	public Long criarNegociacao(Integer numeroCota,
			List<ParcelaNegociacao> parcelas,
			BigDecimal valorDividaParaComissao,
			List<Long> idsCobrancasOriginarias, Usuario usuarioResponsavel,
			boolean negociacaoAvulsa, Integer ativarCotaAposParcela,
			BigDecimal comissaoParaSaldoDivida, boolean isentaEncargos,
			FormaCobranca formaCobranca, Long idBanco) {

		// lista para mensagens de validação
		List<String> msgs = new ArrayList<String>();
		Date dataAtual = new Date();

		if (formaCobranca != null) {

			Banco banco = this.bancoRepository.buscarPorId(idBanco);
			formaCobranca.setBanco(banco);
		}

		// valida dados de entrada
		this.validarDadosEntrada(msgs, dataAtual, parcelas,
				valorDividaParaComissao, usuarioResponsavel,
				ativarCotaAposParcela, comissaoParaSaldoDivida, formaCobranca);

		// Cota e Cobrança originária não são validados no método acima
		// para evitar que se faça duas vezes a mesma consulta
		Cota cota = null;
		if (numeroCota == null) {

			msgs.add("Cota da negociação não encontrada.");
		} else {

			cota = this.cotaRepository.obterPorNumerDaCota(numeroCota);

			if (cota == null) {

				msgs.add("Cota da negociação não encontrada.");
			}
		}

		List<Cobranca> cobrancasOriginarias = new ArrayList<Cobranca>();

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = this.tipoMovimentoFinanceiroRepository
				.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);

		// Cobrança da onde se originou a negociação
		for (Long idCobranca : idsCobrancasOriginarias) {

			Cobranca cobrancaOriginaria = this.cobrancaRepository
					.buscarPorId(idCobranca);

			if (cobrancaOriginaria == null) {

				msgs.add("Cobrança de ID " + idCobranca + " não encontrada.");
			} else {

				// Cobrança original deve ter seu status modificado para pago
				// e sua divida deve ter seus status modificado para negociada
				cobrancaOriginaria.setStatusCobranca(StatusCobranca.PAGO);
				
				cobrancaOriginaria.setDataPagamento(dataAtual);
				
				cobrancaOriginaria.getDivida().setStatus(StatusDivida.NEGOCIADA);
				
				this.dividaRepository.merge(cobrancaOriginaria.getDivida());
				this.cobrancaRepository.merge(cobrancaOriginaria);

				cobrancasOriginarias.add(cobrancaOriginaria);
			}
		}

		if (!msgs.isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING, msgs);
		}
		
		//caso a negociacão seja feita em parcelas
		if (parcelas != null && !parcelas.isEmpty()){
			
			Banco banco = formaCobranca.getBanco();
			BigDecimal totalNegociacao = BigDecimal.ZERO;
			// Popula o movimento financeiro de cada parcela
			// Caso seja uma negociação avulsa o movimento financeiro servirá
			// para rastreabilidade da negociação, caso não seja uma negociação
			// avulsa
			// será insumo para próxima geração de cobrança
			for (ParcelaNegociacao parcelaNegociacao : parcelas) {

				parcelaNegociacao.getMovimentoFinanceiroCota().setCota(cota);
				parcelaNegociacao.getMovimentoFinanceiroCota().setUsuario(usuarioResponsavel);
				parcelaNegociacao.getMovimentoFinanceiroCota().setData(parcelaNegociacao.getDataVencimento());
				parcelaNegociacao.getMovimentoFinanceiroCota().setDataCriacao(dataAtual);
				parcelaNegociacao.getMovimentoFinanceiroCota().setStatus(StatusAprovacao.APROVADO);
				parcelaNegociacao.getMovimentoFinanceiroCota().setAprovador(usuarioResponsavel);
				parcelaNegociacao.getMovimentoFinanceiroCota().setTipoMovimento(tipoMovimentoFinanceiro);
				parcelaNegociacao.getMovimentoFinanceiroCota().setObservacao("Negociação de dívida.");
				
				Fornecedor fornecedor = 
						cota.getParametroCobranca() != null 
						? cota.getParametroCobranca().getFornecedorPadrao()
						: null;
				
				if (fornecedor == null){

					throw new ValidacaoException(
							new ValidacaoVO(
									TipoMensagem.WARNING, 
									"A [Cota] necessita de um [Fornecedor Padrão] em [Parâmetros] Financeiros !"));
				}
				
				parcelaNegociacao.getMovimentoFinanceiroCota().setFornecedor(fornecedor);
				
				totalNegociacao = 
						totalNegociacao.add(
								parcelaNegociacao.getMovimentoFinanceiroCota().getValor());

				this.movimentoFinanceiroCotaRepository
						.adicionar(parcelaNegociacao
								.getMovimentoFinanceiroCota());
			}
			
			//Caso essa seja uma negociação avulsa as parcelas não devem entrar nas próximas
			//gerações de cobrança, para isso é necessário criar um consolidado financeiro para
			//os movimentos financeiros das parcelas
			if (negociacaoAvulsa){
				
				ConsolidadoFinanceiroCota consolidado = null;
				for (ParcelaNegociacao parcelaNegociacao : parcelas){
					
					BigDecimal valorTotalParcela = 
							parcelaNegociacao.getMovimentoFinanceiroCota().getValor();
					
					BigDecimal valorOriginalParcela = 
							parcelaNegociacao.getMovimentoFinanceiroCota().getValor();
					
					MovimentoFinanceiroCota movFinanMulta = null;
					
					if (parcelaNegociacao.getEncargos() != null &&
							parcelaNegociacao.getEncargos().compareTo(BigDecimal.ZERO) != 0){
						
						valorTotalParcela = valorTotalParcela.add(parcelaNegociacao.getEncargos());
						
						movFinanMulta = 
								this.criarMovimentoFinanceiroMulta(parcelaNegociacao.getMovimentoFinanceiroCota(),
										parcelaNegociacao.getEncargos());
					}
					
					consolidado = new ConsolidadoFinanceiroCota();
					consolidado.setCota(cota);
					consolidado.setDataConsolidado(dataAtual);
					List<MovimentoFinanceiroCota> movs = new ArrayList<MovimentoFinanceiroCota>();
					movs.add(parcelaNegociacao.getMovimentoFinanceiroCota());
					
					if (movFinanMulta != null){
						
						movs.add(movFinanMulta);
					}
					
					consolidado.setMovimentos(movs);

					consolidado.setEncalhe(BigDecimal.ZERO);
					consolidado.setVendaEncalhe(BigDecimal.ZERO);
					
					GrupoMovimentoFinaceiro grupoMovimentoFinaceiro =
						((TipoMovimentoFinanceiro) parcelaNegociacao.getMovimentoFinanceiroCota()
							.getTipoMovimento()).getGrupoMovimentoFinaceiro();
					
					if (OperacaoFinaceira.CREDITO.equals(grupoMovimentoFinaceiro.getOperacaoFinaceira())) {
						
						consolidado.setDebitoCredito(valorOriginalParcela.negate());
						
					} else {
						
						consolidado.setDebitoCredito(valorOriginalParcela);
					}
					
					consolidado.setPendente(BigDecimal.ZERO);
					consolidado.setEncargos(parcelaNegociacao.getEncargos());
					
					consolidado.setTotal(valorTotalParcela);
					
					this.consolidadoFinanceiroRepository.adicionar(consolidado);

					Divida divida = new Divida();
					divida.setData(dataAtual);
					divida.setResponsavel(usuarioResponsavel);
					divida.setCota(cota);
					divida.setValor(parcelaNegociacao
							.getMovimentoFinanceiroCota().getValor());
					divida.setStatus(StatusDivida.EM_ABERTO);
					divida.setConsolidado(consolidado);
					divida.setOrigemNegociacao(true);

					Cobranca cobranca = null;

					switch (formaCobranca.getTipoCobranca()) {
					case BOLETO:
						cobranca = new Boleto();
						break;
					case BOLETO_EM_BRANCO:
						cobranca = new Boleto();
						break;
					case CHEQUE:
						cobranca = new CobrancaCheque();
						break;
					case DEPOSITO:
						cobranca = new CobrancaDeposito();
						break;
					case DINHEIRO:
						cobranca = new CobrancaDinheiro();
						break;
					case OUTROS:
						cobranca = new CobrancaDinheiro();
						break;
					case TRANSFERENCIA_BANCARIA:
						cobranca = new CobrancaTransferenciaBancaria();
						break;
					}

					cobranca.setCota(cota);
					cobranca.setBanco(formaCobranca.getBanco());
					cobranca.setDivida(divida);
					cobranca.setDataEmissao(dataAtual);
					cobranca.setStatusCobranca(StatusCobranca.NAO_PAGO);
					cobranca.setDataVencimento(parcelaNegociacao
							.getDataVencimento());
					cobranca.setValor(valorTotalParcela);
					cobranca.setEncargos(parcelaNegociacao.getEncargos());

					this.dividaRepository.adicionar(divida);
					
					cobranca.setNossoNumero(
							Util.gerarNossoNumero(
									numeroCota, 
									dataAtual, 
									banco.getNumeroBanco(),
									null, 
									divida.getId(),
									banco.getAgencia(),
									banco.getConta(),
									banco.getCarteira()
							)
					);
					
					this.cobrancaRepository.adicionar(cobranca);
				}
			}

			this.formaCobrancaRepository.adicionar(formaCobranca);
		}

		// cria registro da negociação
		Negociacao negociacao = new Negociacao();
		negociacao.setAtivarCotaAposParcela(ativarCotaAposParcela);
		negociacao.setCobrancasOriginarias(cobrancasOriginarias);
		negociacao.setComissaoParaSaldoDivida(comissaoParaSaldoDivida);
		negociacao.setIsentaEncargos(isentaEncargos);
		negociacao.setNegociacaoAvulsa(negociacaoAvulsa);
		negociacao.setFormaCobranca(formaCobranca);
		negociacao.setParcelas(parcelas);
		negociacao.setValorDividaPagaComissao(valorDividaParaComissao);

		if (negociacao.getParcelas() != null) {

			for (ParcelaNegociacao parcelaNegociacao : negociacao.getParcelas()) {

				parcelaNegociacao.setNegociacao(negociacao);

				this.parcelaNegociacaoRepository.adicionar(parcelaNegociacao);
			}
		}

		this.negociacaoDividaRepository.adicionar(negociacao);

		return negociacao.getId();
	}

	private MovimentoFinanceiroCota criarMovimentoFinanceiroMulta(
			MovimentoFinanceiroCota movimentoFinanceiroCota, BigDecimal valor) {
		
		MovimentoFinanceiroCota novo = new MovimentoFinanceiroCota();
		novo.setAprovadoAutomaticamente(movimentoFinanceiroCota.isAprovadoAutomaticamente());
		novo.setAprovador(movimentoFinanceiroCota.getAprovador());
		novo.setBaixaCobranca(movimentoFinanceiroCota.getBaixaCobranca());
		novo.setConsolidadoFinanceiroCota(movimentoFinanceiroCota.getConsolidadoFinanceiroCota());
		novo.setCota(movimentoFinanceiroCota.getCota());
		novo.setData(movimentoFinanceiroCota.getData());
		novo.setDataAprovacao(movimentoFinanceiroCota.getDataAprovacao());
		novo.setDataIntegracao(movimentoFinanceiroCota.getDataIntegracao());
		novo.setFornecedor(movimentoFinanceiroCota.getFornecedor()!=null?
				           movimentoFinanceiroCota.getFornecedor():
				           movimentoFinanceiroCota.getCota().getParametroCobranca().getFornecedorPadrao());
		//novo.setHistoricos(movimentoFinanceiroCota.getHistoricos());
		novo.setLancamentoManual(movimentoFinanceiroCota.isLancamentoManual());
		novo.setMotivo(movimentoFinanceiroCota.getMotivo());
		novo.setObservacao("Multa proveniente de negociação (encargos)");
		novo.setParcelaNegociacao(movimentoFinanceiroCota.getParcelaNegociacao());
		novo.setPrazo(movimentoFinanceiroCota.getPrazo());
		novo.setStatus(movimentoFinanceiroCota.getStatus());
		novo.setStatusIntegracao(movimentoFinanceiroCota.getStatusIntegracao());
		
		TipoMovimentoFinanceiro tipo = 
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.MULTA);
		
		novo.setTipoMovimento(tipo);
		novo.setUsuario(movimentoFinanceiroCota.getUsuario());
		novo.setValor(valor);
		
		this.movimentoFinanceiroCotaRepository.adicionar(novo);
		
		return novo;
	}

	private void validarDadosEntrada(List<String> msgs, Date dataAtual,
			List<ParcelaNegociacao> parcelas,
			BigDecimal valorDividaParaComissao, Usuario usuarioResponsavel,
			Integer ativarCotaAposParcela, BigDecimal comissaoParaSaldoDivida,
			FormaCobranca formaCobranca) {

		// caso não tenha parcelas e nem comissão para saldo preenchidos
		if ((parcelas == null || parcelas.isEmpty())
				&& comissaoParaSaldoDivida == null) {

			msgs.add("Forma de pagamento é obrigatória.");
		}

		// caso tenha parcelas
		if (parcelas != null && comissaoParaSaldoDivida == null) {

			// data de vencimento e valor são campos obrigatórios
			for (ParcelaNegociacao parcelaNegociacao : parcelas) {

				if (parcelaNegociacao.getDataVencimento() == null) {

					msgs.add("Data de vencimento da parcela "
							+ (parcelas.indexOf(parcelaNegociacao) + 1)
							+ " inválido.");
				}

				MovimentoFinanceiroCota mov = parcelaNegociacao
						.getMovimentoFinanceiroCota();

				if (mov == null) {

					msgs.add("Valor da parcela "
							+ (parcelas.indexOf(parcelaNegociacao) + 1)
							+ " inválido.");
				} else {

					if (mov.getValor() == null
							|| mov.getValor().equals(BigDecimal.ZERO)) {

						msgs.add("Valor da parcela "
								+ (parcelas.indexOf(parcelaNegociacao) + 1)
								+ " inválido.");
					}
				}
			}
		} else {

			if (valorDividaParaComissao == null) {

				msgs.add("Valor total da negociação inválido.");
			}
		}

		// usuário responsável por fazer a negociação é obrigatório
		if (usuarioResponsavel == null || usuarioResponsavel.getId() == null) {

			msgs.add("Usuário responsável pela negociação inválido.");
		}

		// se a comissão não foi preenchida deve haver forma de cobrança e tipo
		// de cobrança
		// para as parcelas
		if ((comissaoParaSaldoDivida == null && formaCobranca == null)
				|| (comissaoParaSaldoDivida != null && formaCobranca != null)) {

			msgs.add("A negociação deve ter saldo ou parcelas.");
		} else {

			if (formaCobranca != null) {

				if (formaCobranca.getTipoCobranca() == null) {

					msgs.add("Parâmetro Tipo de Cobrança inválido.");
				}

				if (formaCobranca.getBanco() == null
						|| formaCobranca.getBanco().getId() == null) {

					msgs.add("Banco é obrigatório.");
				}

				if (formaCobranca.getTipoFormaCobranca() == null) {

					msgs.add("Frequência da cobrança é obrigatório.");
				} else {

					switch (formaCobranca.getTipoFormaCobranca()) {
					case DIARIA:
						break;
					case MENSAL:
						if (formaCobranca.getDiasDoMes() == null
								|| formaCobranca.getDiasDoMes().isEmpty()
								|| formaCobranca.getDiasDoMes().size() != 1) {

							msgs.add("Parâmetro dias da cobrança inválidos.");
						}
						break;
					case QUINZENAL:
						if (formaCobranca.getDiasDoMes() == null
								|| formaCobranca.getDiasDoMes().isEmpty()
								|| formaCobranca.getDiasDoMes().size() != 2) {

							msgs.add("Parâmetro dias da cobrança inválidos.");
						}
						break;
					case SEMANAL:
						if (formaCobranca.getConcentracaoCobrancaCota() == null
								|| formaCobranca.getConcentracaoCobrancaCota()
										.isEmpty()) {
							msgs.add("Selecione pelo menos um dia da semana.");
						}
						break;
					}
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Negociacao obterNegociacaoPorId(Long idNegociacao) {

		Negociacao negociacao = this.negociacaoDividaRepository
				.buscarPorId(idNegociacao);

		Hibernate.initialize(negociacao.getParcelas());
		Hibernate.initialize(negociacao.getCobrancasOriginarias());

		return negociacao;
	}

	@Override
	@Transactional
	public List<byte[]> gerarBoletosNegociacao(Long idNegociacao) {

		List<byte[]> boletos = new ArrayList<byte[]>();

		Negociacao negociacao = this.negociacaoDividaRepository
				.buscarPorId(idNegociacao);

		if (negociacao != null) {

			if (negociacao.isNegociacaoAvulsa()) {

				for (ParcelaNegociacao parcelaNegociacao : negociacao
						.getParcelas()) {

					String nossoNumero = this.cobrancaRepository
							.obterNossoNumeroPorMovimentoFinanceiroCota(parcelaNegociacao
									.getMovimentoFinanceiroCota().getId());

					boletos.add(this.documentoCobrancaService
							.gerarDocumentoCobranca(nossoNumero));
				}
			}
		}

		return boletos;
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] imprimirNegociacao(Long idNegociacao, String valorDividaSelecionada) throws Exception {

		Negociacao negociacao = this.obterNegociacaoPorId(idNegociacao);

		if (negociacao == null) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"Negociação não encontrada.");
		}

		Cota cota = negociacao.getCobrancasOriginarias().get(0).getCota();

		ImpressaoNegociacaoDTO impressaoNegociacaoDTO = new ImpressaoNegociacaoDTO();
		// campo cota(numero)
		impressaoNegociacaoDTO.setNumeroCota(cota.getNumeroCota());
		// campo nome
		impressaoNegociacaoDTO.setNomeCota(cota.getPessoa().getNome());

		// campo Comissão da Cota para pagamento da dívida
		impressaoNegociacaoDTO.setComissaoParaPagamento(negociacao
				.getComissaoParaSaldoDivida());

		if (negociacao.getParcelas() == null
				|| negociacao.getParcelas().isEmpty()) {

			BigDecimal comissaoAtual = this.descontoService
					.obterComissaoCota(cota.getNumeroCota());

			if (comissaoAtual == null) {

				comissaoAtual = BigDecimal.ZERO;
			}

			// campo Comissão da Cota
			impressaoNegociacaoDTO.setComissaoAtualCota(comissaoAtual.setScale(
					2, RoundingMode.HALF_EVEN));

			// campo Comissão da Cota enquanto houver saldo de dívida
			impressaoNegociacaoDTO
					.setComissaoCotaEnquantoHouverSaldo(comissaoAtual
							.subtract(negociacao.getComissaoParaSaldoDivida()));
		}

		// campo frequencia de pagamento
		if (negociacao.getFormaCobranca() != null) {

			String aux = "";

			TipoFormaCobranca tipoFormaCobranca = negociacao.getFormaCobranca()
					.getTipoFormaCobranca();

			switch (tipoFormaCobranca) {
			case DIARIA:

				break;
			case MENSAL:
				aux = "Todo dia "
						+ negociacao.getFormaCobranca().getDiasDoMes().get(0);
				break;
			case QUINZENAL:
				aux = "Todo dia "
						+ negociacao.getFormaCobranca().getDiasDoMes().get(0)
						+ " e "
						+ negociacao.getFormaCobranca().getDiasDoMes().get(1);
				break;
			case SEMANAL:
				for (ConcentracaoCobrancaCota concen : negociacao
						.getFormaCobranca().getConcentracaoCobrancaCota()) {

					aux = aux + concen.getDiaSemana().getDescricaoDiaSemana();
				}
				break;
			}

			impressaoNegociacaoDTO.setFrequenciaPagamento(negociacao
					.getFormaCobranca().getTipoFormaCobranca().getDescricao()
					+ (!negociacao.getFormaCobranca().getTipoFormaCobranca()
							.equals(TipoFormaCobranca.DIARIA) ? ": " : " ")
					+ aux);

			Banco banco = negociacao.getFormaCobranca().getBanco();

			impressaoNegociacaoDTO.setNumeroBanco(banco.getNumeroBanco());
			impressaoNegociacaoDTO.setNomeBanco(banco.getNome());
			impressaoNegociacaoDTO.setAgenciaBanco(banco.getAgencia());
			impressaoNegociacaoDTO.setContaBanco(banco.getConta());
		}

		// campo negociacao avulsa
		impressaoNegociacaoDTO.setNegociacaoAvulsa(negociacao
				.isNegociacaoAvulsa());
		// campo isenta encargos
		impressaoNegociacaoDTO.setIsentaEncargos(negociacao.isIsentaEncargos());

		impressaoNegociacaoDTO
				.setParcelasCheques(new ArrayList<ImpressaoNegociacaoParecelaDTO>());

		BigDecimal totalParcelas = BigDecimal.ZERO;
		for (ParcelaNegociacao parcela : negociacao.getParcelas()) {

			ImpressaoNegociacaoParecelaDTO vo = new ImpressaoNegociacaoParecelaDTO();

			int nParcela = negociacao.getParcelas().indexOf(parcela) + 1;

			vo.setNumeroParcela(nParcela);

			if (negociacao.getAtivarCotaAposParcela() != null
					&& negociacao.getAtivarCotaAposParcela() == nParcela) {

				vo.setAtivarAoPagar(true);
			}

			vo.setDataVencimento(parcela.getDataVencimento());
			vo.setNumeroCheque(parcela.getNumeroCheque());
			vo.setValor(parcela.getMovimentoFinanceiroCota().getValor());

			BigDecimal encargos = parcela.getEncargos() == null ? BigDecimal.ZERO
					: parcela.getEncargos();

			vo.setEncagos(encargos);
			vo.setParcelaTotal(parcela.getMovimentoFinanceiroCota().getValor()
					.add(encargos));

			totalParcelas = totalParcelas.add(vo.getParcelaTotal());

			impressaoNegociacaoDTO.getParcelasCheques().add(vo);
		}
		
		// campo divida selecionada (este valorDividaSelecionada é retornado do resultado que aparece no html)
		if (BigDecimal.ZERO.equals(totalParcelas)) {
			impressaoNegociacaoDTO.setTotalDividaSelecionada(CurrencyUtil.converterValor(valorDividaSelecionada));
		} else {
			impressaoNegociacaoDTO.setTotalDividaSelecionada(totalParcelas);
		}
		

		List<ImpressaoNegociacaoDTO> listaJasper = new ArrayList<ImpressaoNegociacaoDTO>();
		listaJasper.add(impressaoNegociacaoDTO);

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(listaJasper);

		URL diretorioReports = Thread.currentThread().getContextClassLoader()
				.getResource("reports/");

		String path = diretorioReports.toURI().getPath();

		if (impressaoNegociacaoDTO.getParcelasCheques().isEmpty()) {

			path += "/negociacao_divida_comissao.jasper";
		} else if (impressaoNegociacaoDTO.getParcelasCheques().get(0)
				.getNumeroCheque() == null) {

			path += "/negociacao_divida_boleto.jasper";

		} else {

			path += "/negociacao_divida_cheque.jasper";
		}
		InputStream inputStream = parametrosDistribuidorService
				.getLogotipoDistribuidor();

		if (inputStream == null) {
			inputStream = new ByteArrayInputStream(new byte[0]);
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("TOTAL_PARCELAS",CurrencyUtil.formatarValor(
				totalParcelas.setScale(2, RoundingMode.HALF_EVEN)));
		parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());

		String nomeDistribuidor = this.distribuidorService.obterRazaoSocialDistribuidor();
		parameters.put("NOME_DISTRIBUIDOR",nomeDistribuidor);

		parameters.put("LOGO_DISTRIBUIDOR", inputStream);
		
		parameters.put("COTA_ATIVA", SituacaoCadastro.ATIVO.equals(cota.getSituacaoCadastro()));
		
		return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
	}

	@Override
	@Transactional
	public List<NegociacaoDividaDetalheVO> obterDetalhesCobranca(Long idCobranca) {

		if (idCobranca == null) {

			throw new ValidacaoException(TipoMensagem.WARNING,
					"Id da cobrança inválido.");
		}

		return this.cobrancaRepository.obterDetalhesCobranca(idCobranca);
	}
	@Override
	@Transactional(readOnly=true)
	public List<CalculaParcelasVO> recalcularParcelas(
			FiltroCalculaParcelas filtro, List<CalculaParcelasVO> parcelas) {
		
		BigDecimal valorSelecionado = filtro.getValorSelecionado();
		
		Collections.sort(parcelas, new Comparator<CalculaParcelasVO>() {
			@Override
			public int compare(CalculaParcelasVO o1, CalculaParcelasVO o2) {
				return o1.getNumParcela().compareTo(o2.getNumParcela());
			}
		});
		Banco banco = bancoService.obterBancoPorId(filtro.getIdBanco());
		
		
		
		
		BigDecimal valorParcelasModificadas =  BigDecimal.ZERO;
		Cota cota = cotaRepository.obterPorNumerDaCota(filtro.getNumeroCota());
		FormaCobranca formaCobranca = this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();	
		int qtdParcelasModificadas = 0;
		for (CalculaParcelasVO calculaParcelasVO : parcelas) {
			if(calculaParcelasVO.isModificada()){
				valorParcelasModificadas = valorParcelasModificadas.add(CurrencyUtil.converterValor(calculaParcelasVO.getParcela()));
				qtdParcelasModificadas++;
			}
			
		}
		BigDecimal qtd = BigDecimal.valueOf(filtro.getQntdParcelas() - qtdParcelasModificadas);
		BigDecimal novoValorParcela;		
		if(qtd.intValue() > 0){
			novoValorParcela = (filtro.getValorSelecionado().subtract(valorParcelasModificadas)).divide(qtd,DEFAULT_SCALE,RoundingMode.HALF_EVEN);
			
			
		}else{
			novoValorParcela =  BigDecimal.ZERO;
		}
		BigDecimal valorParcela;
		BigDecimal valorTotal = BigDecimal.ZERO;
		
		FormaCobranca formaCobrancaPrincipal = 
			this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
		
		for (int i=0;i < parcelas.size();i++) {				
			
			CalculaParcelasVO calculaParcelasVO = parcelas.get(i);
			if(!calculaParcelasVO.isModificada()){
				if(i == parcelas.size() -1){
					novoValorParcela = valorSelecionado.subtract(valorTotal);
					novoValorParcela = novoValorParcela.setScale(DEFAULT_SCALE,RoundingMode.HALF_EVEN);
				}
				valorParcela =  novoValorParcela;
				calculaParcelasVO.setParcela(CurrencyUtil.formatarValor(novoValorParcela));
			}else{
				valorParcela = CurrencyUtil.converterValor(calculaParcelasVO.getParcela());					
			}
			valorTotal = valorTotal.add(valorParcela);
			
			Date dataVencimento = DateUtil.parseDataPTBR(calculaParcelasVO.getDataVencimento());
			
			if(formaCobranca.isVencimentoDiaUtil()){
				dataVencimento = calendarioService.adicionarDiasUteis(dataVencimento, 0);
			}				
		
			calculaParcelasVO.setDataVencimento(DateUtil.formatarDataPTBR(dataVencimento));			
			
			BigDecimal encargos = BigDecimal.ZERO;
			if (!filtro.getTipoPagamento().equals(TipoCobranca.CHEQUE)
					&& (filtro.getIsentaEncargos() != null && !filtro
							.getIsentaEncargos())){				
				
				BigDecimal juros = cobrancaService.calcularJuros(banco,
						cota.getId(),
						valorParcela, dataVencimento, new Date(), formaCobrancaPrincipal);

				BigDecimal multas = cobrancaService.calcularMulta(banco,
						cota,
						valorParcela, formaCobrancaPrincipal);

				encargos = juros.add(multas);
			}			
			calculaParcelasVO.setEncargos(CurrencyUtil.formatarValor(encargos));
			calculaParcelasVO.setParcTotal(CurrencyUtil.formatarValor(valorParcela.add(encargos)));			
			
		}
		
		
		if(valorTotal.compareTo(valorSelecionado) != 0){
			throw new ValidacaoException(TipoMensagem.WARNING, "A Soma de todas as parcelas deve ser " + CurrencyUtil.formatarValor(filtro.getValorSelecionado())+"!");
		}
		return parcelas;
		
		
	}

	/**
	 * @param filtro
	 * @param valorParcela
	 * @param dataBase
	 * @param numeroParcela
	 * @param parcela
	 * @return
	 */
	private Date calculaParcela(FiltroCalculaParcelas filtro,
			BigDecimal valorParcela, Date dataBase, int numeroParcela,
			CalculaParcelasVO parcela, Banco banco,Cota cota,FormaCobranca formaCobranca) {
		Date dataParcela;
		parcela.setNumParcela(Integer.toString(numeroParcela + 1));
		parcela.setParcela(CurrencyUtil.formatarValor(valorParcela));

		dataParcela = getDataParcela(dataBase, filtro.getPeriodicidade(),
				filtro.getSemanalDias(), filtro.getQuinzenalDia1(),
				filtro.getQuinzenalDia2(), filtro.getMensalDia());
		
		if(formaCobranca.isVencimentoDiaUtil()){
			dataParcela = calendarioService.adicionarDiasUteis(dataParcela, 0);
		}

		parcela.setDataVencimento(DateUtil.formatarDataPTBR(dataParcela));

		return dataParcela;
	}
	@Override
	@Transactional(readOnly=true)
	public List<CalculaParcelasVO> calcularParcelas(
			FiltroCalculaParcelas filtro) {
		List<CalculaParcelasVO> listParcelas = new ArrayList<CalculaParcelasVO>();

		BigDecimal valorParcela = 
				filtro.getValorSelecionadoSemEncargo().divide(
						BigDecimal.valueOf(
								filtro.getQntdParcelas()),
								DEFAULT_SCALE, RoundingMode.HALF_EVEN);		
		BigDecimal somaParelas = BigDecimal.ZERO;
		
		
		BigDecimal valorEncargo = 
				filtro.getValorEncargoSelecionado().divide(
						BigDecimal.valueOf(
								filtro.getQntdParcelas()),
								DEFAULT_SCALE, RoundingMode.HALF_EVEN);
		BigDecimal somaEncargo = BigDecimal.ZERO;

		Date dataBase = new Date();
		Cota cota = cotaRepository.obterPorNumerDaCota(filtro.getNumeroCota());
		Banco banco = bancoService.obterBancoPorId(filtro.getIdBanco());
		
		FormaCobranca formaCobranca = this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();	

		for (int i = 0; i < filtro.getQntdParcelas(); i++) {
			CalculaParcelasVO parcela = new CalculaParcelasVO();
			
			if(i == filtro.getQntdParcelas() -1){
				valorParcela = filtro.getValorSelecionadoSemEncargo().subtract(somaParelas);
				valorParcela = valorParcela.setScale(DEFAULT_SCALE,RoundingMode.HALF_EVEN);
			}
			
			somaParelas = somaParelas.add(valorParcela);
			
			if (!filtro.getTipoPagamento().equals(TipoCobranca.CHEQUE)
					&& (filtro.getIsentaEncargos() != null && !filtro
							.getIsentaEncargos())){
			
				if(i == filtro.getQntdParcelas() -1){
					valorEncargo = filtro.getValorEncargoSelecionado().subtract(somaEncargo);
					valorEncargo = valorEncargo.setScale(DEFAULT_SCALE,RoundingMode.HALF_EVEN);
				}
				
				somaEncargo = somaEncargo.add(valorEncargo);
			} else {
				
				valorEncargo = BigDecimal.ZERO;
			}

			dataBase = calculaParcela(filtro, valorParcela, dataBase, i,
					parcela, banco,cota,formaCobranca);
			
			parcela.setEncargos(CurrencyUtil.formatarValor(valorEncargo));

			parcela.setParcTotal(CurrencyUtil.formatarValor(valorParcela.add(valorEncargo)));

			listParcelas.add(parcela);
		}
		return listParcelas;
	}



	private Date getDataParcela(Date dataBase, TipoFormaCobranca periodicidade,
			List<DiaSemanaDTO> semanalDias, Integer quinzenalDia1,
			Integer quinzenalDia2, Integer diaMensal) {

		Calendar proximoDia = DateUtil.toCalendar(dataBase);

		int mesBase = proximoDia.get(Calendar.MONTH);

		switch (periodicidade) {

		case DIARIA:		
			
			return DateUtil.adicionarDias(dataBase, 1);

		case SEMANAL:

			if (semanalDias == null || semanalDias.isEmpty()) {

				throw new ValidacaoException(TipoMensagem.WARNING,
						"Dia(s) da semana não selecionado(s).");
			}

			while (true) {

				proximoDia = DateUtil.adicionarDias(proximoDia, 1);

				for (DiaSemanaDTO dia : semanalDias) {

					if (proximoDia.get(Calendar.DAY_OF_WEEK) == dia.getNumDia())

						return proximoDia.getTime();
				}
			}

		case QUINZENAL:

			if (quinzenalDia1 == null || quinzenalDia1.compareTo(0) == 0
					|| quinzenalDia2 == null || quinzenalDia2.compareTo(0) == 0) {

				throw new ValidacaoException(TipoMensagem.WARNING,
						"Dia(s) quinzenal(ais) inválido(s).");
			}

			if (quinzenalDia1.compareTo(31) == 1
					|| quinzenalDia2.compareTo(31) == 1) {

				throw new ValidacaoException(TipoMensagem.WARNING,
						"Dia(s) quinzenal(ais) não deve(m) ser maior do que 31.");
			}

			if (quinzenalDia1.compareTo(quinzenalDia2) >= 0) {

				throw new ValidacaoException(TipoMensagem.WARNING,
						"O 1º dia deve ser menor que o 2º.");
			}

			while (true) {

				if (quinzenalDia1 > proximoDia
						.getActualMaximum(Calendar.DAY_OF_MONTH)) {

					quinzenalDia1 = proximoDia
							.getActualMaximum(Calendar.DAY_OF_MONTH);
				}

				proximoDia.set(Calendar.DAY_OF_MONTH, quinzenalDia1);

				if (proximoDia.getTime().compareTo(dataBase) == 1) {

					return proximoDia.getTime();
				}

				if (quinzenalDia2 > proximoDia
						.getActualMaximum(Calendar.DAY_OF_MONTH)) {

					quinzenalDia2 = proximoDia
							.getActualMaximum(Calendar.DAY_OF_MONTH);
				}

				proximoDia.set(Calendar.DAY_OF_MONTH, quinzenalDia2);

				if (proximoDia.getTime().compareTo(dataBase) == 1) {

					return proximoDia.getTime();
				}

				mesBase++;

				proximoDia.set(Calendar.MONTH, mesBase);
			}

		case MENSAL:

			if (diaMensal == null || diaMensal.compareTo(0) == 0) {

				throw new ValidacaoException(TipoMensagem.WARNING,
						"Dia do mês inválido.");
			}

			if (diaMensal.compareTo(31) == 1) {

				throw new ValidacaoException(TipoMensagem.WARNING,
						"Dia do mês não deve ser maior do que 31.");
			}

			while (true) {

				if (diaMensal > proximoDia
						.getActualMaximum(Calendar.DAY_OF_MONTH)) {

					proximoDia.set(Calendar.DAY_OF_MONTH,
							proximoDia.getActualMaximum(Calendar.DAY_OF_MONTH));
				} else {

					proximoDia.set(Calendar.DAY_OF_MONTH, diaMensal);
				}

				if (proximoDia.getTime().compareTo(dataBase) == 1) {

					return proximoDia.getTime();
				}

				mesBase++;

				proximoDia.set(Calendar.DAY_OF_MONTH, 1);
				proximoDia.set(Calendar.MONTH, mesBase);
			}
		}

		return null;
	}

}