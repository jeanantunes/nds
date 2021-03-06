package br.com.abril.nds.service.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.ValidationException;

import br.com.abril.nds.util.export.cobranca.registrada.*;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.EnumAceite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.BoletoAvulsoDTO;
import br.com.abril.nds.dto.BoletoCotaDTO;
import br.com.abril.nds.dto.BoletoEmBrancoDTO;
import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.DetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.ParametroCobrancaCotaDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.dto.filtro.FiltroBoletoAvulsoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
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
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
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
import br.com.abril.nds.model.financeiro.CobrancaBoletoEmBranco;
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
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaBoletoEmBrancoRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.AcumuloDividasService;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.ControleBaixaBancariaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;
import br.com.abril.nds.util.BigDecimalUtil;
import br.com.abril.nds.util.CorpoBoleto;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.GeradorBoleto;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.NomeBanco;
import br.com.abril.nds.util.TipoBaixaCobranca;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.cnab.UtilitarioCNAB;
import br.com.abril.nds.util.export.cobranca.registrada.builders.DetalheRegistroRegistroPorBancoBuilder;
import br.com.abril.nds.util.export.cobranca.registrada.builders.PopularSacadoBuilder;
import br.com.abril.nds.util.export.cobranca.registrada.builders.RegistroPorBancoBuilder;
import br.com.abril.nds.util.export.cobranca.util.CobRegBaseDTO;
import br.com.abril.nds.util.export.cobranca.util.CobRegParser;
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

    @Autowired
    private NegociacaoDividaService negociacaoDividaService;

    @Autowired
    private CobrancaBoletoEmBrancoRepository cobrancaBoletoEmBrancoRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private RoteirizacaoRepository roteirizacaoRepository;

    @Autowired
    private ParametrosDistribuidorService parametrosDistribuidorService;

    @Autowired
    protected BoxRepository boxRepository;

    @Autowired
	private BancoService bancoService;

    /**
     * Método responsável por obter boletos por numero da cota
     *
     * @param filtro
     * @return Lista de boletos encontrados
     */
    @Override
    @Transactional(readOnly=true)
    public List<BoletoCotaDTO> obterBoletosPorCota(final FiltroConsultaBoletosCotaDTO filtro) {
        return boletoRepository.obterBoletosPorCota(filtro);
    }

	                                        /**
     * Método responsável por obter boleto por nossoNumero
     *
     * @param nossoNumero
     * @return Boletos encontrado
     */
    @Override
    @Transactional(readOnly=true)
    public Boleto obterBoletoPorNossoNumero(final String nossoNumero, final Boolean dividaAcumulada) {
        return boletoRepository.obterPorNossoNumero(nossoNumero, dividaAcumulada,true);
    }

	                                        /**
     * Método responsável por obter a quantidade de boletos por numero da cota
     *
     * @param filtro
     * @return Quantidade de boletos encontrados
     */
    @Override
    @Transactional(readOnly=true)
    public long obterQuantidadeBoletosPorCota(final FiltroConsultaBoletosCotaDTO filtro) {
        return boletoRepository.obterQuantidadeBoletosPorCota(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumoBaixaBoletosDTO obterResumoBaixaFinanceiraBoletos(final Date data) {

        final ResumoBaixaBoletosDTO resumoBaixaBoletos = new ResumoBaixaBoletosDTO();

        resumoBaixaBoletos.setQuantidadePrevisao(boletoRepository.obterQuantidadeBoletosPrevistos(data).intValue());

        resumoBaixaBoletos.setQuantidadeLidos(boletoRepository.obterQuantidadeBoletosLidos(data).intValue());

        resumoBaixaBoletos.setQuantidadeBaixados(boletoRepository.obterQuantidadeBoletosBaixados(data).intValue());

        resumoBaixaBoletos.setQuantidadeRejeitados(boletoRepository.obterQuantidadeBoletosRejeitados(data).intValue());

        resumoBaixaBoletos.setQuantidadeBaixadosComDivergencia(boletoRepository.obterQuantidadeBoletosBaixadosComDivergencia(data).intValue());

        resumoBaixaBoletos.setQuantidadeInadimplentes(boletoRepository.obterQuantidadeBoletosInadimplentes(data).intValue());

        BigDecimal valorTotalBancario = boletoRepository.obterValorTotalBancario(data);

        valorTotalBancario = (valorTotalBancario == null) ? BigDecimal.ZERO : valorTotalBancario;

        resumoBaixaBoletos.setValorTotalBancario(valorTotalBancario.setScale(2, RoundingMode.HALF_EVEN));

        final List<ControleBaixaBancaria> listaControleBaixa = controleBaixaRepository.obterListaControleBaixaBancaria(data, StatusControle.CONCLUIDO_SUCESSO);

        final boolean possuiDiversasBaixas = (listaControleBaixa.size() > 1);

        resumoBaixaBoletos.setPossuiDiversasBaixas(possuiDiversasBaixas);

        return resumoBaixaBoletos;
    }

    @Override
    @Transactional
    public void baixarBoletosAutomatico(final ArquivoPagamentoBancoDTO arquivoPagamento,
            final BigDecimal valorFinanceiro, final Usuario usuario, final Date dataPagamento) {

        if (arquivoPagamento == null) {

            throw new ValidacaoException(TipoMensagem.ERROR, "Arquivo Pagamento deve ser informado!");
        }

        final Date dataOperacao = distribuidorRepository.obterDataOperacaoDistribuidor();

        final Banco banco = bancoRepository.obterBanco(arquivoPagamento.getCodigoBanco(), arquivoPagamento.getNumeroAgencia(), arquivoPagamento.getNumeroConta());


        if (banco == null) {

            throw new ValidacaoException(TipoMensagem.WARNING, "Banco/Conta (Banco="+arquivoPagamento.getCodigoBanco()+" Agencia="+arquivoPagamento.getNumeroAgencia()+
            		" Conta="+arquivoPagamento.getNumeroConta()+") não encontrado no cadastro de Bancos!");
        }

        final ControleBaixaBancaria controleBaixa = controleBaixaRepository.obterControleBaixaBancaria(dataPagamento, banco);

        if (controleBaixa != null && controleBaixa.getStatus().equals(StatusControle.CONCLUIDO_SUCESSO)) {

            throw new ValidacaoException(TipoMensagem.WARNING,
                    String.format("Já foi realizada baixa automática para a data de pagamento informada e banco %s!", banco.getNome()));
        }

        if (valorFinanceiro == null || arquivoPagamento.getSomaPagamentos() == null || valorFinanceiro.compareTo(arquivoPagamento.getSomaPagamentos()) != 0) {
            LOGGER.warn("Valor financeiro inválido! A soma dos valores dos boletos pagos deve ser igual ao valor informado! valor="+valorFinanceiro+" soma="+arquivoPagamento.getSomaPagamentos());
            throw new ValidacaoException(TipoMensagem.WARNING, "Valor financeiro inválido! A soma dos valores dos boletos pagos deve ser igual ao valor informado!");
        }

        controleBaixaService.alterarControleBaixa(StatusControle.INICIADO, dataOperacao, dataPagamento, usuario, banco);

        final ResumoBaixaBoletosDTO resumoBaixaBoletos = new ResumoBaixaBoletosDTO();

        final Date dataNovoMovimento = calendarioService.adicionarDiasUteis(dataOperacao, 1);

        try {
            if (arquivoPagamento != null && arquivoPagamento.getListaPagemento() != null) {

                for (final PagamentoDTO pagamento : arquivoPagamento.getListaPagemento()) {

                	boolean ignorarDataPagamento = true;
            		try {
            			if (pagamento != null && pagamento.getNossoNumero() != null && Long.parseLong(pagamento.getNossoNumero().trim()) > 0) {
            				ignorarDataPagamento = false;
            			}
            		} catch (NumberFormatException nfe) {

            			LOGGER.error("Erro ao fazer parse do Nosso Número.", nfe);
            			throw new ValidacaoException(TipoMensagem.WARNING, String.format("Erro ao fazer parse do Nosso Número: %s", pagamento.getNossoNumero()));
            		}

            		if (!ignorarDataPagamento) {

            			this.baixarBoleto(TipoBaixaCobranca.AUTOMATICA, pagamento, usuario,
            					arquivoPagamento.getNomeArquivo(), dataNovoMovimento, resumoBaixaBoletos, banco, dataPagamento);
            		}

                }

                controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_SUCESSO, dataOperacao, dataPagamento, usuario, banco);

            } else {

                controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS, dataOperacao, dataPagamento, usuario, banco);
            }

            resumoBaixaBoletos.setNomeArquivo(arquivoPagamento.getNomeArquivo());
            resumoBaixaBoletos.setDataCompetencia(DateUtil.formatarDataPTBR(dataOperacao));
            resumoBaixaBoletos.setSomaPagamentos(arquivoPagamento.getSomaPagamentos());

        } catch (final Exception e) {
            LOGGER.error("Falha ao processar a baixa automática", e);
            // gerar movimentos financeiros para cobranças não pagas
            controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
                    dataOperacao, dataPagamento, usuario, banco);

            if (e instanceof ValidacaoException) {

                throw new ValidacaoException(((ValidacaoException) e).getValidacao());

            } else {

                throw new ValidacaoException(TipoMensagem.WARNING, "Falha ao processar a baixa automática: "+ e.getMessage());
            }
        }
    }

    private boolean isCotaAtiva(final Cota cota){

        return cota.getSituacaoCadastro()!=null?cota.getSituacaoCadastro().equals(SituacaoCadastro.ATIVO):false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void adiarDividaBoletosNaoPagos(final Usuario usuario, final Date dataPagamento) {

        LOGGER.warn("INICIO PROCESSO ADIAMENTO DIVIDA BOLETO NAO PAGO");

      try {
        final boolean naoAcumulaDividas = distribuidorRepository.naoAcumulaDividas();

        if(naoAcumulaDividas == true) {
            LOGGER.warn("DISTRIBUIDOR NÃO ACUMULA DIVIDAS.BOLETO NAO ADIADO");
            return;
        }
        } catch ( Exception e ) {
        	LOGGER.error("ERRO OBTENDO ACUMULA DIVIDA. VER SE TABELA POLITICA COBRANCA TEM REGISTROS",e);
        	  throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao verificar se acumula divida! Verificar se Politica de Cobranca foi configurado");
        }

        final List<Cobranca> boletosNaoPagos = boletoRepository.obterBoletosNaoPagos(dataPagamento);

        int contador = 0;

        final int qtdBoletosNaoPagos = boletosNaoPagos.size();

        String nossoNumero = "";

        final TipoMovimentoFinanceiro tipoMovimentoFinanceiroPendente =
                tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.PENDENTE);

        final TipoMovimentoFinanceiro tipoMovimentoFinanceiroJuros =
                tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.JUROS);

        final TipoMovimentoFinanceiro tipoMovimentoFinanceiroMulta =
                tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.MULTA);

        final FormaCobranca formaCobrancaPrincipal =
                formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();

        final Date dataCalculoJuros =
                calendarioService.adicionarDiasRetornarDiaUtil(dataPagamento, 1);

        for (final Cobranca boleto : boletosNaoPagos) {

        	 boolean isPermiteAcumulo = true;

            if (!this.isCotaAtiva(boleto.getCota())) {

                continue;
            }

            LOGGER.warn("ADIANDO DIVIDA BOLETO NAO PAGO [" + ++contador + "]  DE [" + qtdBoletosNaoPagos + "].");

            nossoNumero = boleto.getNossoNumero();

            final Divida divida = boleto.getDivida();

            try {

            	isPermiteAcumulo = permiteAcumuloDivida(isPermiteAcumulo, divida);


                if(isPermiteAcumulo) {
                	LOGGER.warn("ACUMULANDO DIVIDA ="+isPermiteAcumulo);
	                divida.setStatus(StatusDivida.PENDENTE_INADIMPLENCIA);
	                dividaRepository.alterar(divida);

	                boleto.setStatusCobranca(StatusCobranca.NAO_PAGO);
	                cobrancaRepository.alterar(boleto);

	                final Date dataVencimento = this.obterNovaDataVencimentoAcumulo(dataPagamento);

	                //movimentoFinanceiro do valor do boleto
	                final MovimentoFinanceiroCota movimentoPendente = this.gerarMovimentoFinanceiro(
	                        dataPagamento,
	                        usuario,
	                        boleto.getCota(),
	                        dataVencimento,
	                        boleto.getValor().setScale(2, BigDecimal.ROUND_HALF_EVEN),
	                        tipoMovimentoFinanceiroPendente,
	                        "Cobrança não paga");

	                MovimentoFinanceiroCota movimentoJuros = null;

	                MovimentoFinanceiroCota movimentoMulta = null;

	                BigDecimal valor = cobrancaService.calcularJuros(
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

	                valor = cobrancaService.calcularMulta(
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
                } else {
                	LOGGER.warn("NAO ACUMULANDO DIVIDA .. PERMITE ACUMULAR DIVIDA FALSE ="+isPermiteAcumulo);
                }

            } catch (final IllegalArgumentException e) {

                LOGGER.error("FALHA AO ADIAR BOLETO DIVIDA NÃO PAGA NOSSO NUMERO [" + nossoNumero + "]", e);

                // Caso a dívida exceder o limite de acúmulos do distribuidor,
                // esta não será persistida, dando continuidade ao fluxo.
                continue;
            }
        }
    }

	private boolean permiteAcumuloDivida(boolean isPermiteAcumulo, final Divida divida) {
		BigInteger qtdeAcumulo = BigInteger.ZERO;

		BigDecimal valorBoleto = BigDecimal.ZERO;

		Cota cota = divida.getCota();

		LOGGER.warn("permite acumulo divida Cota: "+cota.getNumeroCota());

		BigInteger numeroMaximoAcumulo = acumuloDividasService.obterNumeroMaximoAcumuloCota(cota.getId(), divida.getId());

		LOGGER.warn("permite acumulo divida numeroMaximoAcumulo="+numeroMaximoAcumulo);


		if(cota.isSugereSuspensaoDistribuidor()) {
			LOGGER.warn("SUGERE SUSPENSAO DISTRIBUIDOR="+cota.isSugereSuspensaoDistribuidor());
			ParametrosDistribuidorVO parametrosDistribuidorVO = parametrosDistribuidorService.getParametrosDistribuidor();

			if(parametrosDistribuidorVO.isPararAcumuloDividas()) {
				LOGGER.warn("PARAR DE ACUMULAR DIVIDA=true. ver se atende condicoes de parada  ="+parametrosDistribuidorVO.isPararAcumuloDividas());
					if(parametrosDistribuidorVO.getSugereSuspensaoQuandoAtingirBoletos() != null && !parametrosDistribuidorVO.getSugereSuspensaoQuandoAtingirBoletos().isEmpty()){
					qtdeAcumulo = new BigInteger(parametrosDistribuidorVO.getSugereSuspensaoQuandoAtingirBoletos());
				}

				if(numeroMaximoAcumulo.compareTo(qtdeAcumulo) >= 0) {
					LOGGER.warn("PARAR!! .. qtde de acumulo da cota  ="+numeroMaximoAcumulo+"  ja atingiu maximo="+qtdeAcumulo);
					return false;
				}

				if(parametrosDistribuidorVO.getSugereSuspensaoQuandoAtingirReais() != null && !parametrosDistribuidorVO.getSugereSuspensaoQuandoAtingirReais().isEmpty()){

					valorBoleto = new BigDecimal(parametrosDistribuidorVO.getSugereSuspensaoQuandoAtingirReais());

					if(valorBoleto.intValue() > 0) {
						if(divida.getValor().compareTo(valorBoleto) >= 0) {
							LOGGER.warn("PARAR!!.. VALOR da DIVIDA  ="+divida.getValor()+"  ACIMA DO TETO PERMITIDO "+valorBoleto);
							return false;
						}
					}
				}

			} else {
				LOGGER.warn("PARAR DE ACUMULAR DIVIDA=false. ACUMULAR ENTAO..  ispararacumulodivida (nao) ="+parametrosDistribuidorVO.isPararAcumuloDividas());
				return true;
			}
		} else {
			LOGGER.warn("cota nao  segue suspensao distribuidor="+cota.isSugereSuspensaoDistribuidor());

			if(cota.isSugereSuspensao()) {

				if(cota.getPoliticaSuspensao().getNumeroAcumuloDivida() > 0){
					qtdeAcumulo = BigInteger.valueOf(cota.getPoliticaSuspensao().getNumeroAcumuloDivida().intValue());
				}

				if(numeroMaximoAcumulo.compareTo(qtdeAcumulo) >= 0) {
					LOGGER.warn("NAO PERMITE ACUMULO DIVIDA COTA.. qtde de acumulo da cota  ="+numeroMaximoAcumulo+
							"  ja atingiu maximo="+qtdeAcumulo);

					return false;
				}

				if(cota.getPoliticaSuspensao().getValor() != null) {
					valorBoleto = cota.getPoliticaSuspensao().getValor();

					if(valorBoleto.intValue() > 0) {
						if(divida.getValor().compareTo(valorBoleto) >= 0) {
							LOGGER.warn("NAO PERMITE ACUMULO DIVIDA.. VALOR da DIVIDA  ="+divida.getValor()+
									"  ACIMA DO TETO PERMITIDO "+valorBoleto);
						return false;
						}
					}
				}
			} else {
				LOGGER.warn("PARAR!! COTA NAO SUGERE SUSPENSAO  ");
				return false;
			}
		}

		LOGGER.warn("RETORNANDO PERMITE ACUMULO DIVIDA ="+isPermiteAcumulo);
		return isPermiteAcumulo;
	}

    private Date obterNovaDataVencimentoAcumulo(final Date dataOperacao) {

        final boolean isVencimentoDiaUtil = formaCobrancaRepository.obterFormaCobranca().isVencimentoDiaUtil();

        if (isVencimentoDiaUtil) {

            return calendarioService.adicionarDiasUteis(dataOperacao, 1);

        } else {

            return calendarioService.adicionarDiasRetornarDiaUtil(dataOperacao, 1);
        }
    }

    private MovimentoFinanceiroCota gerarMovimentoFinanceiro(final Date dataOperacao, final Usuario usuario, final Cota cota,
            final Date dataVencimento, final BigDecimal valor, final TipoMovimentoFinanceiro tipoMovimentoFinanceiro, final String observacao) {

        final MovimentoFinanceiroCotaDTO dto = new MovimentoFinanceiroCotaDTO();
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

            return movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(dto).get(0);

        } catch (final NoSuchElementException e) {
            LOGGER.error(e.getMessage(), e);
            return null;

        } catch (final IndexOutOfBoundsException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private AcumuloDivida gerarAcumuloDivida(final Usuario usuario, final Divida divida,
            final MovimentoFinanceiroCota movimentoPendente,
            final MovimentoFinanceiroCota movimentoJuros,
            final MovimentoFinanceiroCota movimentoMulta) {

        final AcumuloDivida acumuloDivida = new AcumuloDivida();

        acumuloDivida.setDividaAnterior(divida);

        acumuloDivida.setMovimentoFinanceiroPendente(movimentoPendente);

        acumuloDivida.setMovimentoFinanceiroJuros(movimentoJuros);

        acumuloDivida.setMovimentoFinanceiroMulta(movimentoMulta);

        ConsolidadoFinanceiroCota c = null;

        for (final ConsolidadoFinanceiroCota cc : divida.getConsolidados()){

            if (cc.getCota().equals(divida.getCota())){

                c = cc;
                break;
            }
        }

        if (c != null) {
            acumuloDivida.setNumeroAcumulo(acumuloDividasService.obterNumeroDeAcumulosDivida(c.getId()).add(BigInteger.ONE));
        }

        acumuloDivida.setDataCriacao(new Date());

        acumuloDivida.setResponsavel(usuario);

        acumuloDivida.setStatus(StatusInadimplencia.ATIVA);

        acumuloDivida.setCota(divida.getCota());

        divida.setAcumulada(true);

        LOGGER.info("DIVIDA ANTERIOR: "+ divida);

        System.out.println(divida);

        if(divida.getDividaRaiz() != null) {
        	//divida.setDividaRaiz(acumuloDivida.getDividaAnterior());
        	 System.out.println(divida);
        }

        dividaRepository.alterar(divida);

        return acumuloDividasService.atualizarAcumuloDivida(acumuloDivida);
    }

    @Override
    @Transactional
    public void baixarBoleto(final TipoBaixaCobranca tipoBaixaCobranca, final PagamentoDTO pagamento, final Usuario usuario,
            final String nomeArquivo,
            final Date dataNovoMovimento, final ResumoBaixaBoletosDTO resumoBaixaBoletos,
            final Banco banco, final Date dataPagamento) {

        final Boleto boleto = this.gerarBaixaBoleto(
                tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
                distribuidorRepository.obterDataOperacaoDistribuidor(),
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
    private void gerarBaixaBoletoAntecipado(final BoletoAntecipado boletoAntecipado, final PagamentoDTO pagamento, final Date dataPagamento) {

        final TipoMovimentoFinanceiro tipoMovimento = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.CREDITO);

        final Usuario usuario = usuarioService.getUsuarioLogado();

        final Date dataOperacao = distribuidorRepository.obterDataOperacaoDistribuidor();

        final MovimentoFinanceiroCotaDTO movimento = new MovimentoFinanceiroCotaDTO();
        movimento.setCota(boletoAntecipado.getChamadaEncalheCota().getCota());
        movimento.setTipoMovimentoFinanceiro(tipoMovimento);
        movimento.setUsuario(usuario);
        movimento.setDataOperacao(dataOperacao);
        movimento.setValor(pagamento.getValorPagamento().subtract(pagamento.getValorDesconto()).add(pagamento.getValorJuros()).add(pagamento.getValorMulta()));
        movimento.setDataCriacao(Calendar.getInstance().getTime());
        movimento.setTipoEdicao(TipoEdicao.INCLUSAO);
        movimento.setDataVencimento(DateUtil.adicionarDias(dataOperacao,1));
        movimento.setObservacao("BOLETO ANTECIPADO - EM BRANCO");
        movimento.setFornecedor(boletoAntecipado.getFornecedor());

        final MovimentoFinanceiroCota movimentoFinanceiroCota = movimentoFinanceiroCotaService.gerarMovimentoFinanceiroCota(movimento, null);

        boletoAntecipado.setDataPagamento(dataPagamento);
        boletoAntecipado.setMovimentoFinanceiroCota(movimentoFinanceiroCota);
        boletoAntecipado.setStatus(StatusDivida.QUITADA);
        boletoAntecipado.setValorDesconto(pagamento.getValorDesconto());
        boletoAntecipado.setValorJuros(pagamento.getValorJuros());
        boletoAntecipado.setValorMulta(pagamento.getValorMulta());
        boletoAntecipado.setValorPago(movimento.getValor());

        boletoAntecipadoRepository.merge(boletoAntecipado);
    }

    private Boleto gerarBaixaBoleto(final TipoBaixaCobranca tipoBaixaCobranca,
            final PagamentoDTO pagamento,
            final Usuario usuario,
            final String nomeArquivo,
            final Date dataOperacao,
            final Date dataNovoMovimento,
            final ResumoBaixaBoletosDTO resumoBaixaBoletos,
            final Banco banco,
            Date dataPagamento) {

        if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {

            validarDadosEntradaBaixaAutomatica(pagamento);

        } else {

            validarDadosEntradaBaixaManual(pagamento);
        }

        final Boleto boleto = boletoRepository.obterPorNossoNumero(pagamento.getNossoNumero(), null, false);

        // Boleto não encontrado na base
        if (boleto == null) {

            // Verifica se boleto consta em boletos antecipados - em branco
            final BoletoAntecipado boletoAntecipado = boletoAntecipadoRepository.obterBoletoAntecipadoPorNossoNumero(pagamento.getNossoNumero());

            if (boletoAntecipado != null) {

                boletoAntecipado.setTipoBaixa(tipoBaixaCobranca);

                this.gerarBaixaBoletoAntecipado(boletoAntecipado, pagamento, dataPagamento);

                return null;
            } else {

                if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {

                    baixarBoletoNaoEncontrado(tipoBaixaCobranca, pagamento, usuario, nomeArquivo, dataOperacao, boleto, resumoBaixaBoletos, banco, dataPagamento);

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

        final Date dataVencimentoUtil = calendarioService.adicionarDiasUteis(boleto.getDataVencimento(), 0);

        dataPagamento = DateUtil.removerTimestamp(dataPagamento);

        // Boleto vencido

        if (dataVencimentoUtil.compareTo(dataPagamento) < 0) {

        	if(this.distribuidorRepository.aceitaBaixaPagamentoVencido()){
        		if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {

        			baixarBoletoVencidoAutomatico(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
        					dataNovoMovimento, dataOperacao,
        					boleto, resumoBaixaBoletos, banco, dataPagamento);

        		}else{

        			baixarBoletoVencidoManual(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
        					dataNovoMovimento, dataOperacao,
        					boleto, resumoBaixaBoletos, banco, dataPagamento);
        		}
        	}else{
        		if (TipoBaixaCobranca.MANUAL.equals(tipoBaixaCobranca)) {
        			throw new ValidacaoException(TipoMensagem.WARNING, "Distribuidor não aceita pagamento vencido.");
        		}else{

        			movimentoFinanceiroCotaService
                    .gerarMovimentosFinanceirosDebitoCredito(
                            getMovimentoFinanceiroCotaDTO(boleto.getCota(),
                                    GrupoMovimentoFinaceiro.CREDITO,
                                    usuario, pagamento.getValorPagamento(),
                                    dataOperacao, null,
                                    calendarioService.adicionarDiasUteis(dataOperacao, 1), null));
        		}
        	}

            return boleto;
        }

        //alterado para HALF_UP, com HALF_EVEN o valor poderia acabar errado, segundo os cálculos feitos dessa maneira
        //a decisão de arredondar pra cima dependeria de mais cáculos e mais um dígito, levando em conta se este é par ou ímpar
        final BigDecimal valorBoleto = boleto.getValor().setScale(2, RoundingMode.HALF_UP);
        final BigDecimal valorPagamento = pagamento.getValorPagamento().setScale(2, RoundingMode.HALF_UP);

        // Boleto pago com valor correto
        if (valorPagamento.compareTo(valorBoleto) == 0) {

            baixarBoletoValorCorreto(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
                    dataOperacao, boleto, resumoBaixaBoletos, banco, dataPagamento);

        } else if (valorPagamento.compareTo(valorBoleto) == 1) {

            if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {

                // Boleto pago com valor acima
                baixarBoletoValorAcima(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
                        dataNovoMovimento, dataOperacao, boleto, resumoBaixaBoletos,
                        banco, dataPagamento);

            } else {

                throw new ValidacaoException(TipoMensagem.WARNING, "Boleto com valor divergente!");
            }

        } else {

            if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {

                // Boleto pago com valor abaixo
                baixarBoletoValorAbaixo(tipoBaixaCobranca, pagamento, usuario, nomeArquivo,
                        dataNovoMovimento, dataOperacao, boleto, resumoBaixaBoletos,
                        banco, dataPagamento);

            } else {

                throw new ValidacaoException(TipoMensagem.WARNING, "Boleto com valor divergente!");
            }
        }

        //ativar cota caso cobrança seja de uma parcela de divida negociada e a mesma ativar a cota ao paga-la
        this.negociacaoDividaService.verificarAtivacaoCotaAposPgtoParcela(boleto, usuario);

        return boleto;
    }

    private void baixarBoletoNaoEncontrado(final TipoBaixaCobranca tipoBaixaCobranca, final PagamentoDTO pagamento,
            final Usuario usuario, final String nomeArquivo, final Date dataOperacao,
            final Boleto boleto, final ResumoBaixaBoletosDTO resumoBaixaBoletos,
            final Banco banco, final Date dataPagamento) {

		                                                                                /*
         * Gera baixa com status de pago, porém o nosso número referente ao
         * pagamento não existe na base
         */

        gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_BOLETO_NAO_ENCONTRADO, boleto,
                dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);
    }

    private void baixarBoletoJaPago(final TipoBaixaCobranca tipoBaixaCobranca, final PagamentoDTO pagamento,
            final Usuario usuario, final String nomeArquivo, final Date dataNovoMovimento,
            final Date dataOperacao, final Boleto boleto, final ResumoBaixaBoletosDTO resumoBaixaBoletos,
            final Banco banco, final Date dataPagamento) {

		 /*
         * Não baixa o boleto, gera baixa com status de boleto pago
         * anteriormente e gera movimento de crédito
         */

        final BaixaCobranca baixaCobranca =
                gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_BAIXA_JA_REALIZADA, boleto,
                        dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);

        String dataFormatada = null;

        try {
			dataFormatada = Util.formataData(boleto.getDataEmissao());
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Problema ao formatar a data do boleto!");
		}

        movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(
                getMovimentoFinanceiroCotaDTO(boleto.getCota(),
                GrupoMovimentoFinaceiro.CREDITO,
                usuario, pagamento.getValorPagamento(),
                dataOperacao, baixaCobranca,
                dataNovoMovimento,
                "Boleto Negociado no dia ("+dataFormatada+") e pago"));
    }

    private void baixarBoletoVencidoAutomatico(final TipoBaixaCobranca tipoBaixaCobranca, final PagamentoDTO pagamento,
            final Usuario usuario, final String nomeArquivo,
            final Date dataNovoMovimento,
            final Date dataOperacao, final Boleto boleto,
            final ResumoBaixaBoletosDTO resumoBaixaBoletos,
            final Banco banco, final Date dataPagamento) {

        BaixaCobranca baixaCobranca = null;

		                                                                                /*
         * Não baixa o boleto, gera baixa com status de não pago por divergência
         * de data e gera movimento de crédito
         */

        if (!distribuidorRepository.aceitaBaixaPagamentoVencido()) {

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

        final FormaCobranca formaCobrancaPrincipal =
                formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();

        final BigDecimal valorJurosCalculado =
                cobrancaService.calcularJuros(null, boleto.getCota().getId(),
                        boleto.getValor(), boleto.getDataVencimento(),
                        dataPagamento,
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

        final BigDecimal valorMultaCalculado =
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

        final String dataPagamentoFormatada = DateUtil.formatarDataPTBR(dataPagamento);

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

    private void baixarBoletoVencidoManual(final TipoBaixaCobranca tipoBaixaCobranca, final PagamentoDTO pagamento,
            final Usuario usuario, final String nomeArquivo,
            final Date dataNovoMovimento,
            final Date dataOperacao, final Boleto boleto,
            final ResumoBaixaBoletosDTO resumoBaixaBoletos,
            final Banco banco, final Date dataPagamento) {

		                                                                                /*
         * Baixa o boleto, calcula o valor pago considerando multas, juros e
         * desconto, e gera baixa cobrança com o valor atualizado
         */

        gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO_DIVERGENCIA_DATA, boleto,
                dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);

        efetivarBaixaCobranca(boleto, dataPagamento);
    }

    private void baixarBoletoValorCorreto(final TipoBaixaCobranca tipoBaixaCobranca, final PagamentoDTO pagamento,
            final Usuario usuario, final String nomeArquivo, final Date dataOperacao,
            final Boleto boleto, final ResumoBaixaBoletosDTO resumoBaixaBoletos,
            final Banco banco, final Date dataPagamento) {

        // Baixa o boleto o gera baixa com status de pago

        gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.PAGO, boleto, dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);

        efetivarBaixaCobranca(boleto, dataPagamento);

        if(boleto.isCobrancaNFe()) {
        	this.gerarCreditoBoletoNFe(usuario, boleto, dataPagamento);
        }
    }

	private void gerarCreditoBoletoNFe(final Usuario usuario, final Boleto boleto, final Date dataPagamento) {
		movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(
				getMovimentoFinanceiroCotaDTO(boleto.getCota(),
						GrupoMovimentoFinaceiro.CREDITO,
						usuario, boleto.getValor(),
						dataPagamento, null,
						calendarioService.adicionarDiasUteis(dataPagamento, 1), null));
	}

    private void baixarBoletoValorAcima(final TipoBaixaCobranca tipoBaixaCobranca, final PagamentoDTO pagamento,
            final Usuario usuario, final String nomeArquivo,
            final Date dataNovoMovimento, final Date dataOperacao, final Boleto boleto,
            final ResumoBaixaBoletosDTO resumoBaixaBoletos,
            final Banco banco, final Date dataPagamento) {

		                                                                                /*
         * Verifica o parâmetro para pagamento a maior, não baixa o boleto, gera
         * baixa com status de não pago por divergência de valor e gera
         * movimento de crédito
         */

        BaixaCobranca baixaCobranca = null;

        if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {

            if (!distribuidorRepository.aceitaBaixaPagamentoMaior()) {

                baixaCobranca = gerarBaixaCobranca(tipoBaixaCobranca, StatusBaixa.NAO_PAGO_DIVERGENCIA_VALOR, boleto,
                        dataOperacao, nomeArquivo, pagamento, usuario, banco, dataPagamento);

                movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(
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

        final BigDecimal valorCredito = pagamento.getValorPagamento().subtract(boleto.getValor());

        final String dataPagamentoFormatada = DateUtil.formatarDataPTBR(dataPagamento);

        final String observacao = "Diferença de Pagamento a Maior (" + dataPagamentoFormatada + ")";

        movimentoFinanceiroCotaService
        .gerarMovimentosFinanceirosDebitoCredito(
                getMovimentoFinanceiroCotaDTO(boleto.getCota(),
                        GrupoMovimentoFinaceiro.CREDITO,
                        usuario, valorCredito,
                        dataOperacao, baixaCobranca,
                        dataNovoMovimento, observacao));
    }

    private void baixarBoletoValorAbaixo(final TipoBaixaCobranca tipoBaixaCobranca, final PagamentoDTO pagamento,
            final Usuario usuario, final String nomeArquivo,
            final Date dataNovoMovimento, final Date dataOperacao, final Boleto boleto,
            final ResumoBaixaBoletosDTO resumoBaixaBoletos, final Banco banco,
            final Date dataPagamento) {

        BaixaCobranca baixaCobranca = null;

		                                                                                /*
         * Verifica o parâmetro para pagamento a menor, não baixa o boleto, gera
         * baixa com status de não pago por divergência de valor e gera
         * movimento de crédito
         */

        if (!distribuidorRepository.aceitaBaixaPagamentoMenor()) {

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

        final BigDecimal valorDebito = boleto.getValor().subtract(pagamento.getValorPagamento());

        final String dataPagamentoFormatada = DateUtil.formatarDataPTBR(dataPagamento);

        final String observacao = "Diferença de Pagamento a Menor (" + dataPagamentoFormatada + ")";

        movimentoFinanceiroCotaService
        .gerarMovimentosFinanceirosDebitoCredito(
                getMovimentoFinanceiroCotaDTO(boleto.getCota(),
                        GrupoMovimentoFinaceiro.DEBITO,
                        usuario, valorDebito,
                        dataOperacao, baixaCobranca,
                        dataNovoMovimento, observacao));
    }

    private void validarDadosEntradaBaixaAutomatica(final PagamentoDTO pagamento) {

        final List<String> listaMensagens = new ArrayList<String>();

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

            final ValidacaoVO validacao = new ValidacaoVO();

            validacao.setTipoMensagem(TipoMensagem.WARNING);
            validacao.setListaMensagens(listaMensagens);

            throw new ValidacaoException(validacao);
        }
    }

    private void validarDadosEntradaBaixaManual(final PagamentoDTO pagamento) {

        final List<String> listaMensagens = new ArrayList<String>();

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

            final ValidacaoVO validacao = new ValidacaoVO();

            validacao.setTipoMensagem(TipoMensagem.WARNING);
            validacao.setListaMensagens(listaMensagens);

            throw new ValidacaoException(validacao);
        }
    }

    private BaixaCobranca gerarBaixaCobranca(final TipoBaixaCobranca tipoBaixaCobranca, final StatusBaixa statusBaixa,
            final Boleto boleto, final Date dataBaixa, final String nomeArquivo,
            final PagamentoDTO pagamento, final Usuario usuario, final Banco banco, final Date dataPagamento) {

        if (TipoBaixaCobranca.AUTOMATICA.equals(tipoBaixaCobranca)) {

            final BaixaAutomatica baixaAutomatica = new BaixaAutomatica();

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

            if (boleto != null) {

                acumuloDividasService.quitarDividasAcumuladas(dataPagamento, boleto.getDivida(),TipoBaixaCobranca.AUTOMATICA);
            }

            return baixaAutomatica;
        }

        if (boleto != null) {

            acumuloDividasService.quitarDividasAcumuladas(dataPagamento, boleto.getDivida(),TipoBaixaCobranca.MANUAL);
        }

        BigDecimal valorPagamento = BigDecimal.ZERO;

        if (pagamento.getValorPagamento() != null) {

            valorPagamento = pagamento.getValorPagamento();
        }

        final BaixaManual baixaManual = new BaixaManual();

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

    private void efetivarBaixaCobranca(final Boleto boleto, final Date dataPagamento) {

        boleto.setDataPagamento(dataPagamento);
        boleto.setStatusCobranca(StatusCobranca.PAGO);
        boleto.getDivida().setStatus(StatusDivida.QUITADA);

        boletoRepository.alterar(boleto);

        this.pagarCobrancasRaizes(boleto.getDivida(), dataPagamento);
    }

    private void pagarCobrancasRaizes(final Divida divida, final Date dataOperacao) {

        Divida dividaRaiz = null;

        dividaRaiz = divida.getDividaRaiz();

        if (dividaRaiz != null) {

    		dividaRaiz.getCobranca().setStatusCobranca(StatusCobranca.PAGO);
    		dividaRaiz.getCobranca().setDataPagamento(dataOperacao);

    		dividaRepository.alterar(dividaRaiz);

    		if(dividaRaiz.getDividaRaiz() != null) {
    			Divida dividaAux = dividaRaiz.getDividaRaiz();

    			if(dividaAux != null  ) {
    				if(!dividaAux.getId().equals(dividaAux.getDividaRaiz() != null ? dividaAux.getDividaRaiz().getId():null)) {
    					this.pagarCobrancasRaizes(dividaAux, dataOperacao);
    				}
    			}
    		}
    	}
    }

    private MovimentoFinanceiroCotaDTO getMovimentoFinanceiroCotaDTO(final Cota cota,
            final GrupoMovimentoFinaceiro grupoMovimentoFinaceiro, final Usuario usuario,
            final BigDecimal valorPagamento, final Date dataOperacao,
            final BaixaCobranca baixaCobranca, final Date dataNovoMovimento, final String observacao) {

        final TipoMovimentoFinanceiro tipoMovimento = tipoMovimentoFinanceiroRepository
                .buscarTipoMovimentoFinanceiro(grupoMovimentoFinaceiro);

        final MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();

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

    private CorpoBoleto gerarCorpoBoletoCota(final Boleto boleto, Pessoa pessoaCedente,
            final boolean aceitaPagamentoVencido, final List<PoliticaCobranca> politicasCobranca){

        final String nossoNumero = boleto.getNossoNumero();
        final String digitoNossoNumero = boleto.getDigitoNossoNumero();
        final BigDecimal valor = boleto.getValor() != null ? boleto.getValor().abs() : BigDecimal.ZERO;
        final Banco banco = boleto.getBanco();
        final Date dataEmissao = boleto.getDataEmissao();
        final Date dataVencimento = boleto.getDataVencimento();
        final Pessoa pessoaSacado = boleto.getCota().getPessoa();
        pessoaCedente = banco.getPessoaJuridicaCedente();

        Endereco endereco = null;

        final Set<EnderecoCota> enderecosCota = boleto.getCota().getEnderecos();

        for(final EnderecoCota enderecoCota : enderecosCota){

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
                false,
                politicasCobranca);
    }

    private CorpoBoleto gerarCorpoBoletoCota(final Boleto boleto, final Pessoa pessoaCedente,
            final boolean aceitaPagamentoVencido){

        final List<PoliticaCobranca> politicasCobranca =
                politicaCobrancaRepository.obterPoliticasCobranca(
                        Arrays.asList(TipoCobranca.BOLETO, TipoCobranca.BOLETO_EM_BRANCO));

        return this.gerarCorpoBoletoCota(boleto, pessoaCedente, aceitaPagamentoVencido, politicasCobranca);
    }

    private CorpoBoleto gerarCorpoBoletoDistribuidor(final BoletoDistribuidor boleto, final Pessoa pessoaSacado, final boolean aceitaPagamentoVencido) {

        final String nossoNumero = boleto.getNossoNumeroDistribuidor();
        final String digitoNossoNumero = boleto.getDigitoNossoNumeroDistribuidor();
        final BigDecimal valor = boleto.getValor();
        final Banco banco = boleto.getBanco();
        final Date dataEmissao = boleto.getDataEmissao();
        final Date dataVencimento = boleto.getDataVencimento();
        final Pessoa pessoaCedente = boleto.getFornecedor().getJuridica();

        Endereco endereco = null;

        final Set<EnderecoFornecedor> enderecosFornecedor = boleto.getFornecedor().getEnderecos();

        for(final EnderecoFornecedor enderecoFornecedor : enderecosFornecedor){

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
    private List<CorpoBoleto> geraCorposBoletoEmBranco(final List<BoletoEmBrancoDTO> boletosEmBrancoDTO){

        final List<CorpoBoleto> corposBoleto = new ArrayList<CorpoBoleto>();

        for (final BoletoEmBrancoDTO bbDTO : boletosEmBrancoDTO){

            corposBoleto.add(this.geraCorpoBoletoEmBranco(bbDTO));
        }

        return corposBoleto;
    }

    /**
     * Gera corpo de Boleto em Branco
     * @param bbDTO
     * @return CorpoBoleto
     */
    private CorpoBoleto geraCorpoBoletoEmBranco(final BoletoEmBrancoDTO bbDTO){

        final Cota cota = cotaRepository.obterPorNumeroDaCota(bbDTO.getNumeroCota());

        final BoletoAntecipado boletoAntecipado = boletoAntecipadoRepository.buscarPorId(bbDTO.getIdBoletoAntecipado());

        final Banco banco = boletoAntecipado.getBanco();

        final CorpoBoleto corpoBoleto = this.geraCorpoBoletoEmBranco(cota,
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
    private CorpoBoleto geraCorpoBoletoEmBranco(final String nossoNumero){

        final BoletoAntecipado boletoAntecipado = boletoAntecipadoRepository.obterBoletoAntecipadoPorNossoNumero(nossoNumero);

        final Cota cota = cotaRepository.obterPorNumerDaCota(boletoAntecipado.getChamadaEncalheCota().getCota().getNumeroCota());

        final Banco banco = boletoAntecipado.getBanco();

        final CorpoBoleto corpoBoleto = this.geraCorpoBoletoEmBranco(cota,
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
    private CorpoBoleto geraCorpoBoletoEmBranco(final Cota cota,
									            final Date dataEmissao,
									            final Date  dataVencimento,
									            final Banco banco,
									            final BigDecimal valorLiquido,
									            final BigDecimal valorDebitos,
									            final BigDecimal valorCreditos,
									            final String nossoNumero,
									            final String digitoNossoNumero) {

        final Pessoa pessoaSacado = cota.getPessoa();

//        final Distribuidor distribuidor = this.distribuidorRepository.obter();
//        final Pessoa pessoaCedente = distribuidor.getJuridica();

        final Pessoa pessoaCedente = banco.getPessoaJuridicaCedente();

        Endereco enderecoSacado = cota.getEnderecoPrincipal().getEndereco();

        final TipoCobranca tipoCobranca = TipoCobranca.BOLETO;

        final boolean aceitaPagamentoVencido = true;

        final CorpoBoleto corpoBoleto = this.geraCorpoBoleto(nossoNumero,
											                 digitoNossoNumero,
											                 valorLiquido,
											                 valorDebitos,
											                 BigDecimal.ZERO,
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

        corpoBoleto.setTituloDeducao(valorCreditos);

        return corpoBoleto;
    }

    private CorpoBoleto geraCorpoBoleto(final String nossoNumero,
            final String digitoNossoNumero,
            BigDecimal valorDocumento,
            BigDecimal valorAcrescimo,
            BigDecimal valorDesconto,
            final Banco banco,
            final Date dataEmissao,
            final Date dataVencimento,
            final Pessoa pessoaCedente,
            final Pessoa pessoaSacado,
            final Endereco enderecoSacado,
            final TipoCobranca tipoCobranca,
            final Integer numeroCota,
            final boolean aceitaPagamentoVencido,
            final boolean boletoEmBranco){

        final List<PoliticaCobranca> politicasCobranca =
                politicaCobrancaRepository.obterPoliticasCobranca(
                        Arrays.asList(TipoCobranca.BOLETO, TipoCobranca.BOLETO_EM_BRANCO));

        return this.geraCorpoBoleto(nossoNumero, digitoNossoNumero, valorDocumento,
                valorAcrescimo, valorDesconto, banco, dataEmissao, dataVencimento,
                pessoaCedente, pessoaSacado, enderecoSacado, tipoCobranca, numeroCota,
                aceitaPagamentoVencido, boletoEmBranco, politicasCobranca);
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
    private CorpoBoleto geraCorpoBoleto(final String nossoNumero,
            final String digitoNossoNumero,
            BigDecimal valorDocumento,
            BigDecimal valorAcrescimo,
            BigDecimal valorDesconto,
            final Banco banco,
            final Date dataEmissao,
            final Date dataVencimento,
            final Pessoa pessoaCedente,
            final Pessoa pessoaSacado,
            final Endereco enderecoSacado,
            final TipoCobranca tipoCobranca,
            final Integer numeroCota,
            final boolean aceitaPagamentoVencido,
            final boolean boletoEmBranco,
            final List<PoliticaCobranca> politicasCobranca) {

        valorDocumento = (valorDocumento == null) ? BigDecimal.ZERO : valorDocumento.abs();

        final CorpoBoleto corpoBoleto = new CorpoBoleto();

        String nomeCedente = "";
        String documentoCedente = "";

        if(pessoaCedente instanceof PessoaJuridica) {

            nomeCedente = ((PessoaJuridica) pessoaCedente).getRazaoSocial();
            documentoCedente = ((PessoaJuridica) pessoaCedente).getCnpj();

        } else {

            nomeCedente = ((PessoaFisica) pessoaCedente).getNome();
            documentoCedente = ((PessoaFisica) pessoaCedente).getCpf();

        }


        //DADOS DO CEDENTE
        corpoBoleto.setCodigoCedente(banco.getCodigoCedente());
        corpoBoleto.setDigitoCodigoCedente(banco.getDigitoCodigoCedente());
        corpoBoleto.setCedenteNome(nomeCedente);
        corpoBoleto.setCedenteDocumento(documentoCedente);


        //DADOS DO SACADO

        String nomeSacado = "";

        String documentoSacado = "";

        if (pessoaSacado instanceof PessoaFisica) {
            nomeSacado = ((PessoaFisica) pessoaSacado).getNome();
            documentoSacado = ((PessoaFisica) pessoaSacado).getCpf();
        }
        if (pessoaSacado instanceof PessoaJuridica) {
            nomeSacado = ((PessoaJuridica) pessoaSacado).getRazaoSocial();
            documentoSacado = ((PessoaJuridica) pessoaSacado).getCnpj();
        }

        if(numeroCota != null && numeroCota >0) {
            corpoBoleto.setSacadoNome(numeroCota + " - "+ nomeSacado);
        } else {
            corpoBoleto.setSacadoNome(nomeSacado);
        }

        corpoBoleto.setSacadoDocumento(documentoSacado);


        //ENDERECO DO SACADO

        if (enderecoSacado != null) {
            corpoBoleto.setEnderecoSacadoUf(enderecoSacado.getUf());
            corpoBoleto.setEnderecoSacadoLocalidade(enderecoSacado.getCidade());
            corpoBoleto.setEnderecoSacadoCep(enderecoSacado.getCep());
            corpoBoleto.setEnderecoSacadoBairro(enderecoSacado.getBairro());
            corpoBoleto.setEnderecoSacadoLogradouro(enderecoSacado.getTipoLogradouro() + " " + enderecoSacado.getLogradouro());
            corpoBoleto.setEnderecoSacadoNumero(enderecoSacado.getNumero());
        } else {
            corpoBoleto.setEnderecoSacadoUf("SP");
            corpoBoleto.setEnderecoSacadoLocalidade("Endereco nao cadastrado.");
            corpoBoleto.setEnderecoSacadoCep("");
            corpoBoleto.setEnderecoSacadoBairro("");
            corpoBoleto.setEnderecoSacadoLogradouro("");
            corpoBoleto.setEnderecoSacadoNumero("");
        }


        //INFORMACOES DA CONTA(BANCO)
        final String contaNumero=banco.getConta().toString();

        corpoBoleto.setContaNumeroBanco(banco.getNumeroBanco());

        corpoBoleto.setContaCarteira(banco.getCarteira());

        for (final PoliticaCobranca politicaCobranca : politicasCobranca) {

            final FormaCobranca formaCobranca = politicaCobranca.getFormaCobranca();

            if (formaCobranca.getBanco().getApelido().equals(banco.getApelido())) {

                if (formaCobranca.getFormaCobrancaBoleto() != null) {

                    corpoBoleto.setContaTipoDeCobranca(formaCobranca.getFormaCobrancaBoleto().toString());

                    break;
                }
            }
        }

        if (corpoBoleto.getContaTipoDeCobranca() == null || corpoBoleto.getContaTipoDeCobranca().isEmpty()){
            corpoBoleto.setContaTipoDeCobranca(FormaCobrancaBoleto.SEM_REGISTRO.name());
        } else {

        	if(corpoBoleto.getContaTipoDeCobranca().equals(FormaCobrancaBoleto.SEM_REGISTRO.name())) {
        		 corpoBoleto.setContaTipoDeCobranca(FormaCobrancaBoleto.SEM_REGISTRO.name());
        	} else {

        		corpoBoleto.setContaTipoDeCobranca(FormaCobrancaBoleto.COM_REGISTRO.name());
        	}
        }

        corpoBoleto.setContaAgencia(banco.getAgencia().intValue());
        corpoBoleto.setDigitoAgencia(banco.getDvAgencia());

        corpoBoleto.setContaNumero(Integer.parseInt(contaNumero));


        //INFORMACOES DO TITULO
        corpoBoleto.setTituloNumeroDoDocumento("9125253");
        corpoBoleto.setTituloNossoNumero(nossoNumero);


        //PARAMETROS ?
        corpoBoleto.setTituloDigitoDoNossoNumero(digitoNossoNumero);
        corpoBoleto.setTituloTipoDeDocumento("DM_DUPLICATA_MERCANTIL");
        if(corpoBoleto.getTituloAceite() == null || corpoBoleto.getTituloAceite().equals("")) {
        	corpoBoleto.setTituloAceite("N");
        } else {
        	if(corpoBoleto.getTituloAceite().equals(EnumAceite.A)) {
        		corpoBoleto.setTituloAceite("A");
        	} else {
        		corpoBoleto.setTituloAceite("N");
        	}
        }

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
        }

        corpoBoleto.setTituloDesconto(valorDesconto);
        corpoBoleto.setTituloDeducao(valorParaPagamentosVencidos);
        corpoBoleto.setTituloMora(valorParaPagamentosVencidos);
        corpoBoleto.setTituloAcrecimo(valorAcrescimo);
        corpoBoleto.setTituloValorCobrado(valorParaPagamentosVencidos);

        // INFORMAÇOES DO BOLETO
        //PARAMETROS ?
        corpoBoleto.setBoletoLocalPagamento("Pagável em qualquer agência bancária até o vencimento. Após o vencimento, consulte as instruções abaixo.");
        corpoBoleto.setBoletoInstrucaoAoSacado("Instrução so Sacado");

        String instrucao1 = null;
        
        //Edita intrucoes 01, atualizando Juros e Multa, de acordo com o valor do boleto
        if(banco.isExibirValorMonetario()){
        	if(banco.getMulta() != null){
        		instrucao1 = banco.getInstrucoes1();

        		if(BigDecimalUtil.isMaiorQueZero(banco.getMulta())){
        			BigDecimal valorBoleto =  valorDocumento.setScale(4, RoundingMode.HALF_EVEN);
        			BigDecimal valorMultaReal = valorBoleto.multiply(banco.getMulta().divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP));

        			valorMultaReal = CurrencyUtil.arredondarValorParaDuasCasas(valorMultaReal);

        			instrucao1 = instrucao1.replace("R$XXX", "R$ "+CurrencyUtil.formatarValor(valorMultaReal));

        		}else{
        			instrucao1 = instrucao1.replace("R$XXX", "R$ 00,00");
        		}
        	} 
        }else{
               	
			instrucao1 = banco.getInstrucoes1();
			
			String multa = "0,00%";
			String juros = "0,00%";
			
			if(BigDecimalUtil.isMaiorQueZero(banco.getMulta())){
				multa = banco.getMulta().toString();
			}
			
			if(BigDecimalUtil.isMaiorQueZero(banco.getJuros())){
				juros = banco.getJuros().toString();
			}
			
			instrucao1 = instrucao1.replace("XXX%", multa);
			instrucao1 = instrucao1.replace("YYY%", juros);
        }

        if(BigDecimalUtil.isMaiorQueZero(banco.getJuros())){

        	if(instrucao1 == null){
        		instrucao1 = banco.getInstrucoes1();
        	}

    		BigDecimal valorBoleto =  valorDocumento.setScale(2, RoundingMode.HALF_EVEN);
    		BigDecimal divisorQtdDias = new BigDecimal(30);
    		BigDecimal valorJurosDiario = banco.getJuros().divide(divisorQtdDias, 4, BigDecimal.ROUND_HALF_UP);

    		BigDecimal valorJurosReal = valorBoleto.multiply(valorJurosDiario.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP));

    		valorJurosReal = CurrencyUtil.arredondarValorParaDuasCasas(valorJurosReal);

    		instrucao1 = instrucao1.replace("R$YYY", "R$ "+ CurrencyUtil.formatarValor(valorJurosReal));

    	}else{

    		if(instrucao1 == null){
        		instrucao1 = banco.getInstrucoes1();
        	}

    		instrucao1 = instrucao1.replace("R$YYY", "R$ 00.00");

    	}

        corpoBoleto.setBoletoInstrucao1(instrucao1 != null ? instrucao1 : banco.getInstrucoes1());
        corpoBoleto.setBoletoInstrucao2(banco.getInstrucoes2());
        corpoBoleto.setBoletoInstrucao3(banco.getInstrucoes3());
        corpoBoleto.setBoletoInstrucao4(banco.getInstrucoes4());
        corpoBoleto.setBoletoInstrucao5("");
        corpoBoleto.setBoletoInstrucao6("");
        corpoBoleto.setBoletoInstrucao7("");

        if(numeroCota != null && numeroCota >0) {
        	ConsultaRoteirizacaoDTO roteirizacao = this.roteirizacao(numeroCota);

        	if(roteirizacao != null) {
        		corpoBoleto.setBoletoInstrucao8(montarRoteirizacao(roteirizacao));
        	} else {
        		corpoBoleto.setBoletoInstrucao8("");
        	}

        } else {
        	corpoBoleto.setBoletoInstrucao8("");
        }

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
    private byte[]  gerarAnexoBoleto(final Boleto boleto) throws IOException, ValidationException {

        final Pessoa pessoaCedente = distribuidorRepository.juridica();

        final boolean aceitaPagamentoVencido = distribuidorRepository.aceitaBaixaPagamentoVencido();

        final GeradorBoleto geradorBoleto = new GeradorBoleto(this.gerarCorpoBoletoCota(boleto, pessoaCedente, aceitaPagamentoVencido));

        final byte[] b = geradorBoleto.getBytePdf();

        return b;
    }

    /**
     *
     * @param boletoAntecipado
     * @return f: Boleto PDF em File.
     * @throws IOException
     * @throws ValidationException
     */
    private byte[] gerarAnexoBoletoEmBranco(final BoletoAntecipado boletoAntecipado) throws IOException, ValidationException {

        final GeradorBoleto geradorBoleto = new GeradorBoleto(this.geraCorpoBoletoEmBranco(boletoAntecipado.getNossoNumero()));

        final byte[] b = geradorBoleto.getBytePdf();

        return b;
    }

    /**
     * Metodo responsavel por enviar boleto por email em formato PDF
     * @param nossoNumero
     * @throws erro ao enviar
     */
    @Override
    @Transactional(readOnly=true)
    public void enviarBoletoEmail(final String nossoNumero) {
        try{

            byte[] anexo = null;

            final Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero,null, false);

            Cota cota = null;

            if (boleto == null){

                final BoletoAntecipado boletoAntecipado = boletoAntecipadoRepository.obterBoletoAntecipadoPorNossoNumero(nossoNumero);

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

            final String[] destinatarios = new String[]{cota.getPessoa().getEmail()};

//            final String assunto = distribuidorRepository.assuntoEmailCobranca();
//            final String mensagem = distribuidorRepository.mensagemEmailCobranca();
//            if(assunto == null || assunto.isEmpty()){
//            	assunto = "[NDS] - Emissão "+nomeAnexo;
//            }
//            if(mensagem == null || mensagem.isEmpty()){
//            	mensagem = "Olá, o boleto segue em anexo.";
//            }

            String nomeAnexo = "Boleto "+nossoNumero+" - Data "+boleto.getDataEmissao()+" - Cota "+cota.getNumeroCota();
            String assunto = "[NDS] - Emissão "+nomeAnexo;
            String mensagem = "Olá, o boleto segue anexo.";

            email.enviar(assunto, mensagem, destinatarios, new AnexoEmail(nomeAnexo, anexo,TipoAnexo.PDF), true);

        } catch(final AutenticacaoEmailException e){

            LOGGER.error(e.getMessage(), e);

            throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao conectar-se com o servidor de e-mail. ");

        } catch(final ValidacaoException e){

            LOGGER.error(e.getMessage(), e);

            throw e;

        }catch(final Exception e){

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
    public byte[] gerarImpressaoBoleto(final String nossoNumero) throws IOException, ValidationException {

        final List<PoliticaCobranca> politicasCobranca =
                politicaCobrancaRepository.obterPoliticasCobranca(
                        Arrays.asList(TipoCobranca.BOLETO, TipoCobranca.BOLETO_EM_BRANCO));
        Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero,null,false);
        return this.gerarImpressaoBoleto(boleto, politicasCobranca);
    }

    @Override
    @Transactional
    public byte[] gerarImpressaoBoleto(final Boleto boleto, final List<PoliticaCobranca> politicasCobranca) throws IOException, ValidationException {

        if (boleto == null){

            return null;
        }

        final Pessoa pessoaCedente = distribuidorRepository.juridica();

        final boolean aceitaPagamentoVencido = distribuidorRepository.aceitaBaixaPagamentoVencido();

        return this.gerarImpressaoBoleto(boleto, pessoaCedente, aceitaPagamentoVencido, politicasCobranca);
    }

    @Override
    @Transactional
    public byte[] gerarImpressaoBoleto(final Boleto boleto, Pessoa cedente, boolean aceitaPagamentoVencido,
            final List<PoliticaCobranca> politicasCobranca)
            throws IOException, ValidationException {

        final GeradorBoleto geradorBoleto =
                new GeradorBoleto(this.gerarCorpoBoletoCota(boleto, cedente, aceitaPagamentoVencido, politicasCobranca));

        byte[] b = null;

        try{

            b = geradorBoleto.getBytePdf();
        }
        catch(Exception e){

            throw new ValidacaoException(TipoMensagem.ERROR, e.getMessage());
        }

    //    cobrancaRepository.atualizarVias(boleto);

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
    public byte[] gerarImpressaoBoletos(final Collection<String> nossoNumeros) throws IOException, ValidationException {

        final List<CorpoBoleto> corpos = new ArrayList<CorpoBoleto>();

        Boleto boleto = null;

        final Pessoa pessoaCedente = distribuidorRepository.juridica();

        final boolean aceitaPagamentoVencido = distribuidorRepository.aceitaBaixaPagamentoVencido();

        for(final String nossoNumero : nossoNumeros) {

            boleto = boletoRepository.obterPorNossoNumero(nossoNumero, null, false);

            if(boleto!= null){
                corpos.add(this.gerarCorpoBoletoCota(boleto, pessoaCedente, aceitaPagamentoVencido));
            }
        }

        if(!corpos.isEmpty()){
            final GeradorBoleto geradorBoleto = new GeradorBoleto(corpos) ;
            final byte[] b = geradorBoleto.getByteGroupPdf();
            return b;
        }
        return null;
    }

    @Override
    @Transactional(readOnly=true)
    public byte[] gerarImpressaoBoletosDistribuidor(final List<BoletoDistribuidor> listaBoletoDistribuidor) throws IOException, ValidationException {

        final List<CorpoBoleto> corpos = new ArrayList<CorpoBoleto>();

        final Pessoa pessoaSacado = distribuidorRepository.juridica();

        final boolean aceitaPagamentoVencido = distribuidorRepository.aceitaBaixaPagamentoVencido();

        for(final BoletoDistribuidor boletoDistribuidor  : listaBoletoDistribuidor){
            corpos.add(this.gerarCorpoBoletoDistribuidor(boletoDistribuidor, pessoaSacado, aceitaPagamentoVencido));
        }

        if(!corpos.isEmpty()) {
            final GeradorBoleto geradorBoleto = new GeradorBoleto(corpos) ;
            final byte[] b = geradorBoleto.getByteGroupPdf();
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
    public byte[] gerarImpressaoBoletoEmBranco(final String nossoNumero) throws IOException, ValidationException {

        final BoletoAntecipado boletoAntecipado = boletoAntecipadoRepository.obterBoletoAntecipadoPorNossoNumero(nossoNumero);

        if (boletoAntecipado != null){

            final GeradorBoleto geradorBoleto = new GeradorBoleto(this.geraCorpoBoletoEmBranco(nossoNumero));

            final byte[] b = geradorBoleto.getBytePdf();

            boletoAntecipado.setVias(boletoAntecipado.getVias() + 1);

            boletoAntecipadoRepository.merge(boletoAntecipado);

            return b;
        }

        final CobrancaBoletoEmBranco boletoEmBranco = this.cobrancaBoletoEmBrancoRepository.obterPorNossoNumero(nossoNumero);

        if (boletoEmBranco == null){

            return null;
        }

        final GeradorBoleto geradorBoleto = new GeradorBoleto(this.geraCorpoBoletoEmBranco(boletoEmBranco.getCota(),
                boletoEmBranco.getDataEmissao(), boletoEmBranco.getDataVencimento(),
                boletoEmBranco.getBanco(), boletoEmBranco.getValor(), BigDecimal.ZERO, BigDecimal.ZERO,
                nossoNumero, boletoEmBranco.getDigitoNossoNumero()));

        final byte[] b = geradorBoleto.getBytePdf();

        cobrancaRepository.incrementarVia(nossoNumero);

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
    public byte[] geraImpressaoBoletosEmBranco(final List<BoletoEmBrancoDTO> boletosEmBrancoDTO) throws ValidationException{

        if (boletosEmBrancoDTO==null){

            return null;
        }

        final List<CorpoBoleto> corposBoleto = this.geraCorposBoletoEmBranco(boletosEmBrancoDTO);

        if(!corposBoleto.isEmpty()){

            final GeradorBoleto geradorBoleto = new GeradorBoleto(corposBoleto) ;

            final byte[] b = geradorBoleto.getByteGroupPdf();

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
    public CobrancaVO obterDadosBoletoPorNossoNumero(final String nossoNumero, final Date dataPagamento) {

        CobrancaVO cobrancaVO = null;

        final Boleto boleto = boletoRepository.obterPorNossoNumero(nossoNumero, null, true);
        if (boleto!=null) {
            cobrancaVO = cobrancaService.obterDadosCobranca(boleto.getId(), dataPagamento);
        }

        return cobrancaVO;
    }

    /**
     * Incrementa o valor de vias
     * @param nossoNumero
     */
    @Override
    @Transactional
    public void incrementarVia(final String... nossoNumero) {
        cobrancaRepository.incrementarVia(nossoNumero);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<DetalheBaixaBoletoDTO> obterBoletosPrevistos(final FiltroDetalheBaixaBoletoDTO filtro) {

        this.validarFiltroBaixaBoleto(filtro);

        return boletoRepository.obterBoletosPrevistos(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalheBaixaBoletoDTO> obterBoletosBaixados(final FiltroDetalheBaixaBoletoDTO filtro) {

        return boletoRepository.obterBoletosBaixados(filtro);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<DetalheBaixaBoletoDTO> obterBoletosRejeitados(final FiltroDetalheBaixaBoletoDTO filtro) {

        this.validarFiltroBaixaBoleto(filtro);

        return boletoRepository.obterBoletosRejeitados(filtro);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<DetalheBaixaBoletoDTO> obterBoletosBaixadosComDivergencia(final FiltroDetalheBaixaBoletoDTO filtro) {

        this.validarFiltroBaixaBoleto(filtro);

        return boletoRepository.obterBoletosBaixadosComDivergencia(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalheBaixaBoletoDTO> obterBoletosInadimplentes(final FiltroDetalheBaixaBoletoDTO filtro) {

        return boletoRepository.obterBoletosInadimplentes(filtro);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<DetalheBaixaBoletoDTO> obterTotalBancario(final FiltroDetalheBaixaBoletoDTO filtro) {

        this.validarFiltroBaixaBoleto(filtro);

        return boletoRepository.obterTotalBancario(filtro);
    }

    private void validarFiltroBaixaBoleto(final FiltroDetalheBaixaBoletoDTO filtro) {

        if (filtro == null) {

            throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não pode ser nulo.");
        }

        if (filtro.getData() == null) {

            throw new ValidacaoException(TipoMensagem.WARNING, "Uma data deve ser informada para a pesquisa.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long obterQuantidadeBoletosPrevistos(final FiltroDetalheBaixaBoletoDTO filtro) {

        return boletoRepository.obterQuantidadeBoletosPrevistos(filtro.getData());
    }

    @Override
    @Transactional(readOnly = true)
    public Long obterQuantidadeBoletosBaixados(final FiltroDetalheBaixaBoletoDTO filtro) {

        return boletoRepository.obterQuantidadeBoletosBaixados(filtro.getData());
    }

    @Override
    @Transactional(readOnly = true)
    public Long obterQuantidadeBoletosRejeitados(final FiltroDetalheBaixaBoletoDTO filtro) {

        return boletoRepository.obterQuantidadeBoletosRejeitados(filtro.getData());
    }

    @Override
    @Transactional(readOnly = true)
    public Long obterQuantidadeBoletosBaixadosComDivergencia(final FiltroDetalheBaixaBoletoDTO filtro) {

        return boletoRepository.obterQuantidadeBoletosBaixadosComDivergencia(filtro.getData());
    }

    @Override
    @Transactional(readOnly = true)
    public Long obterQuantidadeBoletosInadimplentes(final FiltroDetalheBaixaBoletoDTO filtro) {

        return boletoRepository.obterQuantidadeBoletosInadimplentes(filtro.getData());
    }

    @Override
    @Transactional(readOnly = true)
    public Long obterQuantidadeTotalBancario(final FiltroDetalheBaixaBoletoDTO filtro) {

        return boletoRepository.obterQuantidadeTotalBancario(filtro.getData());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoletoCotaDTO> verificaEnvioDeEmail(final List<BoletoCotaDTO> boletosDTO) {

        for(final BoletoCotaDTO boletoDTO : boletosDTO){

            if (boletoDTO.isBoletoAntecipado()){

                if (boletoDTO.getStatusDivida().equals(StatusDivida.BOLETO_ANTECIPADO_EM_ABERTO)){

                    boletoDTO.setStatusCobranca(StatusCobranca.NAO_PAGO.name());
                }

                continue;
            }

            final Long verificaSeRecebeEmail = boletoRepository.verificaEnvioDeEmail(boletoDTO.getNossoNumero());

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
    private Fornecedor obterFornecedorPadraoCota(final Cota cota) {

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
     * Obtem o fator de vencimento da forma de cobrança da cota ou do distribuidor
     *
     * @param fc
     * @return Integer
     */
    private Integer obterFatorVencimentoFormaCobranca(FormaCobranca fc){

    	 Integer fatorVencimento = 0;

         ParametroCobrancaCota parametroCobrancaCota = fc.getParametroCobrancaCota();

         if(parametroCobrancaCota!=null && parametroCobrancaCota.getFatorVencimento()!=null) {

 			fatorVencimento = parametroCobrancaCota.getFatorVencimento();
 		}
 		else {

 			PoliticaCobranca politicaCobranca = fc.getPoliticaCobranca();

 			if(politicaCobranca != null && politicaCobranca.getFatorVencimento() != null){

 				fatorVencimento = politicaCobranca.getFatorVencimento();
 			}
 		}

         return fatorVencimento;
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
    public BoletoEmBrancoDTO obterDadosBoletoEmBrancoPorCE(final CotaEmissaoDTO ceDTO,
            final Date dataRecolhimentoCEDe,
            final Date dataRecolhimentoCEAte){

        final Date dataEmissao = DateUtil.parseDataPTBR(ceDTO.getDataEmissao());

        final Date dataRecolhimento = DateUtil.parseDataPTBR(ceDTO.getDataRecolhimento());

        final BigDecimal valorReparteLiquidoCE = CurrencyUtil.getBigDecimal(ceDTO.getVlrReparteLiquido()).setScale(2, RoundingMode.HALF_UP);

        final BigDecimal valorEncalheCE = CurrencyUtil.getBigDecimal(ceDTO.getVlrEncalhe()).setScale(2, RoundingMode.HALF_UP);

        final BigDecimal valorTotalLiquidoCE = CurrencyUtil.getBigDecimal(ceDTO.getVlrTotalLiquido()).setScale(2, RoundingMode.HALF_UP);

        final Intervalo<Date> intervalo = new Intervalo<Date>(dataRecolhimentoCEDe, dataRecolhimentoCEAte);

        final BigDecimal valorDebitos = this.obterDebitosCota(ceDTO.getNumCota(), intervalo);

        final BigDecimal valorCreditos = this.obterCreditosCota(ceDTO.getNumCota(), intervalo);

        final BigDecimal valorTotalBoletoEmBranco = valorTotalLiquidoCE.add(valorDebitos.subtract(valorCreditos)).setScale(2, RoundingMode.HALF_UP);

        boolean valorMinimoAtingido = this.formaCobrancaService.isValorMinimoAtingido(ceDTO.getIdCota(), valorTotalBoletoEmBranco);

        if (!valorMinimoAtingido){

        	return null;
        }

        final ParametroCobrancaCotaDTO parametroCobrancaCota  = paramtroCobrancaCotaService.obterDadosParametroCobrancaPorCota(ceDTO.getIdCota());

        if (parametroCobrancaCota == null){

            return null;
        }

        final FormaCobranca fc = formaCobrancaService.obterFormaCobrancaCota(ceDTO.getIdCota(), parametroCobrancaCota.getIdFornecedor(), dataEmissao);

        if (fc == null || !fc.getTipoCobranca().equals(TipoCobranca.BOLETO_EM_BRANCO)){

            return null;
        }

        final Integer fatorVencimento = this.obterFatorVencimentoFormaCobranca(fc);

        List<DiaSemana> diasRecolhimento = this.grupoRepository.obterDiasRecolhimentoOperacaoDiferenciada(ceDTO.getNumCota(), dataRecolhimentoCEDe, dataRecolhimentoCEAte);

        final Date dataOperacao = this.obterDataParaCalculoVencimentoBoletoEmBranco(new HashSet<DiaSemana>(diasRecolhimento), dataRecolhimentoCEDe);

        final Date dataVencimento = gerarCobrancaService.obterDataVencimentoCobrancaCota(dataOperacao, fatorVencimento, null);

        final BoletoEmBrancoDTO bbDTO = new BoletoEmBrancoDTO(ceDTO.getIdChamEncCota(),
        		parametroCobrancaCota.getIdFornecedor(),
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

    private BigDecimal obterCreditosCota(Integer numeroCota, Intervalo<Date> intervalo) {

    	return this.obterValoresFinanceiroCota(numeroCota, intervalo, GrupoMovimentoFinaceiro.CREDITO);
    }

    private BigDecimal obterDebitosCota(Integer numeroCota, Intervalo<Date> intervalo) {

    	return this.obterValoresFinanceiroCota(numeroCota, intervalo, GrupoMovimentoFinaceiro.DEBITO);
    }

    private BigDecimal obterValoresFinanceiroCota(Integer numeroCota, Intervalo<Date> intervalo, GrupoMovimentoFinaceiro grupo) {

    	FiltroDebitoCreditoDTO filtro = new FiltroDebitoCreditoDTO();

    	filtro.setGrupoMovimentosFinanceirosDebitosCreditos(Arrays.asList(grupo));

    	filtro.setDataVencimentoInicio(intervalo.getDe());
    	filtro.setDataVencimentoFim(intervalo.getAte());

    	filtro.setNumeroCota(numeroCota);

    	BigDecimal sum = this.movimentoFinanceiroCotaService.obterSomatorioValorMovimentosFinanceiroCota(filtro);

    	return sum == null ? BigDecimal.ZERO : sum;
    }

    private Date obterDataParaCalculoVencimentoBoletoEmBranco(Set<DiaSemana> diasRecolhimento, Date dataRecolhimentoDe) {

    	SortedSet<DiaSemana> dias = new TreeSet<>(new Comparator<DiaSemana>() {

			@Override
			public int compare(DiaSemana o1, DiaSemana o2) {
				return Integer.compare(o1.getCodigoDiaSemana(), o2.getCodigoDiaSemana());
			}
		});

    	dias.addAll(diasRecolhimento);

        DiaSemana menorDiaSemana = dias.first();

        Calendar calDe = Calendar.getInstance();
        calDe.setTime(dataRecolhimentoDe);

        int diferencaDias = menorDiaSemana.getCodigoDiaSemana() - calDe.get(Calendar.DAY_OF_WEEK);

        if (diferencaDias >= 0) {

        	calDe.add(Calendar.DAY_OF_MONTH, diferencaDias);

            return calDe.getTime();

        } else {

        	final int quantidadeDiasSemana = 7;

			calDe.add(Calendar.DAY_OF_MONTH, quantidadeDiasSemana - (diferencaDias*-1));

            return calDe.getTime();
        }
    }

    /**
     * Obtem BoletoAntecipado por ChamadaEncalheCota e Periodo de Recolhimento selecionado na Emissao
     * @param idCE
     * @param dataRecolhimentoCEDe
     * @param dataRecolhimentoCEAte
     * @return BoletoAntecipado
     */
    private BoletoAntecipado obterBoletoAntecipadoPorCECotaEPeriodoRecolhimento(final Long idCE,
            final Date dataRecolhimentoCEDe,
            final Date dataRecolhimentoCEAte) {

        final List<StatusDivida> listaStatusDivida = Arrays.asList(StatusDivida.BOLETO_ANTECIPADO_EM_ABERTO, StatusDivida.QUITADA);

        final BoletoAntecipado boletoAntecipado = boletoAntecipadoRepository.obterBoletoAntecipadoPorCECotaEPeriodoRecolhimento(idCE,
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
    private long obterInicioNumeroReservadoNossoNumeroBoletoAntecipado(final String numeroBanco){

        final NomeBanco nomeBanco = NomeBanco.getByNumeroBanco(numeroBanco);

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
    private void gerarNossoNumeroBoletoEmBrancoDTO(final BoletoEmBrancoDTO bbDTO,
            final Cota cota,
            final Fornecedor fornecedor,
            final Date dataEmissao,
            final Date  dataVencimento,
            final Banco banco,
            final BigDecimal valor){

        final long numeroBancoBoletoAntecipado = this.obterInicioNumeroReservadoNossoNumeroBoletoAntecipado(banco.getNumeroBanco());

        final String nossoNumero = Util.gerarNossoNumero(cota.getNumeroCota(),
                dataEmissao,
                banco!=null?banco.getNumeroBanco():"0",
                fornecedor != null ? fornecedor.getId() : null,
                numeroBancoBoletoAntecipado + bbDTO.getIdBoletoAntecipado(),
                banco!=null?banco.getAgencia():0,
                banco!=null?banco.getConta():0,
                banco != null && banco.getCarteira() != null ? banco.getCarteira() : 0,
                banco.getCodigoCedente(),
                banco.getDigitoCodigoCedente());

        final String digitoNossoNumero = Util.calcularDigitoVerificador(nossoNumero,
                banco!=null?banco.getCodigoCedente():"0",
                        dataVencimento, banco.getNumeroBanco());

        bbDTO.setNossoNumero(nossoNumero);

        bbDTO.setDigitoNossoNumero(digitoNossoNumero);
    }

    /**
     * Salva Boleto Antecipado - Em Branco
     * @param bbDTO
     */
    @Transactional
    @Override
    public void salvaBoletoAntecipado(final BoletoEmBrancoDTO bbDTO){

        BoletoAntecipado boletoAntecipado = this.obterBoletoAntecipadoPorCECotaEPeriodoRecolhimento(bbDTO.getIdChamadaEncalheCota(),
                bbDTO.getDataRecolhimentoCEDe(),
                bbDTO.getDataRecolhimentoCEAte());

        if (boletoAntecipado==null){

            boletoAntecipado = new BoletoAntecipado();
        }

        final ChamadaEncalheCota chamadaEncalheCota = chamadaEncalheCotaRepository.buscarPorId(bbDTO.getIdChamadaEncalheCota());

        final Fornecedor fornecedor = fornecedorRepository.buscarPorId(bbDTO.getIdFornecedor());

        String numeroBanco = bbDTO.getNumeroBanco();
        Long numeroAgencia = bbDTO.getNumeroAgencia();

        String strContaComDV = (bbDTO.getNumeroConta()==null?"":bbDTO.getNumeroConta()) +""+
        (bbDTO.getDigitoVerificadorConta()==null?"":bbDTO.getDigitoVerificadorConta());

        Banco banco = null;

        if( numeroBanco!=null && !numeroBanco.isEmpty() &&
        	numeroAgencia!=null &&
        	strContaComDV!=null && !strContaComDV.isEmpty()) {

        	banco = bancoRepository.obterBanco(numeroBanco, numeroAgencia, Long.parseLong(strContaComDV));

        }

        final Usuario usuario = usuarioService.getUsuarioLogado();

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

            boletoAntecipadoRepository.adicionar(boletoAntecipado);
        }
        else{

            final int vias = boletoAntecipado.getVias();

            boletoAntecipado.setVias(vias + 1);

            boletoAntecipadoRepository.merge(boletoAntecipado);
        }

        final Cota cota = cotaRepository.obterPorNumeroDaCota(bbDTO.getNumeroCota());

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

        boletoAntecipadoRepository.merge(boletoAntecipado);

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
    public void salvaBoletosAntecipado(final List<BoletoEmBrancoDTO> listaBbDTO){

        for (final BoletoEmBrancoDTO bbDTO:listaBbDTO){

            this.salvaBoletoAntecipado(bbDTO);
        }
    }

	/**
     * Verifica se existe boleto antecipado para a cota Data de recolhimento
     * dentro do periodo de emissao CE do Boleto antecipado Boletos em Branco
     *
     * @param idCota
     * @param dataRecolhimento
     * @return boolean
     */
    @Transactional
    @Override
    public boolean existeBoletoAntecipadoCotaDataRecolhimento(final Long idCota, final Date dataRecolhimento){

        final List<StatusDivida> listaStatusDivida = Arrays.asList(StatusDivida.BOLETO_ANTECIPADO_EM_ABERTO, StatusDivida.QUITADA);

        final List<BoletoAntecipado> bas = boletoAntecipadoRepository.obterBoletosAntecipadosPorDataRecolhimentoECota(idCota, dataRecolhimento, listaStatusDivida);

        if (bas==null || bas.isEmpty()){

            return false;
        }

        return true;
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
    private void atualizaBoletosEmitidosNoPeriodoDaNovaEmissao(final BoletoAntecipado novoBoletoAntecipado,
            final Integer numeroCota,
            final Date dataRecolhimentoDe,
            final Date dataRecolhimentoAte){

        final List<BoletoAntecipado> bas = boletoAntecipadoRepository.obterBoletosAntecipadosPorPeriodoRecolhimentoECota(numeroCota,
                numeroCota,
                dataRecolhimentoDe,
                dataRecolhimentoAte);

        for (final BoletoAntecipado ba: bas){

            if (ba.getEmissaoBoletoAntecipado().getBoletoAntecipadoReimpresso() == null && !ba.getId().equals(novoBoletoAntecipado.getId())){

                ba.getEmissaoBoletoAntecipado().setBoletoAntecipadoReimpresso(novoBoletoAntecipado);

                boletoAntecipadoRepository.merge(ba);
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
    public boolean existeBoletoAntecipadoPeriodoRecolhimentoECota(final Integer numeroCotaDe,
            final Integer numeroCotaAte,
            final Date dataRecolhimentoDe,
            final Date dataRecolhimentoAte){

        final List<BoletoAntecipado> bas = boletoAntecipadoRepository.obterBoletosAntecipadosPorPeriodoRecolhimentoECota(numeroCotaDe,
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
    public BoletoAntecipado obterBoletoEmBrancoPorNossoNumero(final String nossoNumero) {

        return boletoAntecipadoRepository.obterBoletoAntecipadoPorNossoNumero(nossoNumero);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly=true)
    public BoletoAntecipado obterBoletoEmBrancoPorId(final Long idBoletoAntecipado) {

        return boletoAntecipadoRepository.buscarPorId(idBoletoAntecipado);
    }

    private ConsultaRoteirizacaoDTO roteirizacao(Integer numeroCota) {

        ConsultaRoteirizacaoDTO roteirizacaoDTO = null;

		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();

		filtro.setNumeroCota(Integer.valueOf(numeroCota));

		List<ConsultaRoteirizacaoDTO> listaRoteirizacaoDTO = this.roteirizacaoRepository.buscarRoteirizacao(filtro);

		for (ConsultaRoteirizacaoDTO item : listaRoteirizacaoDTO){

			if (!item.getTipobox().equals(TipoBox.ESPECIAL)) {

				roteirizacaoDTO = item;

				return roteirizacaoDTO;
			}
		}
	    return null;
	}

    private String montarRoteirizacao(ConsultaRoteirizacaoDTO roteirizacao) {
    	StringBuffer buffer = new StringBuffer();

		buffer.append("Box: ").append(roteirizacao.getNomeBox());
		buffer.append(" / ");
		buffer.append("Roteiro: ").append(roteirizacao.getDescricaoRoteiro());
		buffer.append(" / ");
		buffer.append("Rota : ").append(roteirizacao.getDescricaoRota());
		return buffer.toString();
    }
    
	@Override
	@Transactional
	public byte[] gerarArquivo(final FiltroDividaGeradaDTO filtro) throws Exception {

		LOGGER.debug("Metodo gerar cobranca registrada.");
		
//		filtro.setArquivoCobrancaRegistrada(true);
//		
//		List<GeraDividaDTO> dividas = dividaRepository.obterDividasGeradas(filtro);
		List<GeraDividaDTO> dividas = dividaRepository.obterDividasGeradasArquivo(filtro);

		if(dividas == null || dividas.isEmpty()) {
			return null;
		}

		final Pessoa pessoaCedente = distribuidorRepository.juridica();

		Banco banco = bancoRepository.buscarPorId(filtro.getIdBanco());

		List<CobRegBaseDTO> list = new ArrayList<CobRegBaseDTO>();

		CobRegEnvTipoRegistro00 registro00 = null;

		CobRegEnvTipoRegistroItau00 registroItau = null;

        CobRegEnvTipoRegistroCaixa00 registroCaixa = null;

		CobRegEnvTipoRegistroBradesco00 registroBradesco = null;

		switch (banco.getNumeroBanco()) {

			case UtilitarioCNAB.BANCO_HSBC:
				registro00 = this.montarRegistro00(banco, pessoaCedente);
				break;
			case UtilitarioCNAB.BANCO_BRADESCO:
				registroBradesco = this.montarRegistroBradesco(banco, pessoaCedente);
				break;
			case UtilitarioCNAB.BANCO_ITAU:
				registroItau = this.montarRegistroItau(banco, pessoaCedente);
				break;
			case UtilitarioCNAB.BANCO_DO_BRASIL:
				registro00 = this.montarRegistro00(banco, pessoaCedente);
				break;
			case UtilitarioCNAB.BANCO_CAIXA_ECONOMICA_FEDERAL:
				registroCaixa = this.montarRegistroCaixa(banco, pessoaCedente);
				break;
			case UtilitarioCNAB.BANCO_SANTANDER:
				registro00 = this.montarRegistro00(banco, pessoaCedente);
				break;
		}

		int count = 0;

		if(registro00 != null) {
			list.add(registro00);
		} else if(registroItau != null) {
			list.add(registroItau);
			count = qtdeSequenciaRegistro(registro00, registroItau);
		} else if(registroBradesco != null) {
			list.add(registroBradesco);
			count = qtdeSequenciaRegistro(registro00, registroBradesco);
		} else if(registroCaixa != null) {
            list.add(registroCaixa);
            count = qtdeSequenciaRegistro(registro00, registroCaixa);
        }


		int somaSquencial = 0;
		
		PoliticaCobranca politicaCobranca = politicaCobrancaService.obterPoliticaCobrancaBoleto();

		List<Long> idsCobrancasParaUpdate = new ArrayList<>();
		
		for(GeraDividaDTO divida : dividas) {

			Boleto boleto = boletoRepository.obterPorNossoNumero(divida.getNossoNumero(), null, false);

        	if(boleto!= null){

        		switch (banco.getNumeroBanco()) {
        			case UtilitarioCNAB.BANCO_ITAU:
        				CobRegEnvTipoRegistroItau01 detalheItau = this.montarRegistroItau01(boleto, pessoaCedente, banco, count);
        				
        				if(politicaCobranca != null && politicaCobranca.getFormaCobranca() != null){
    						if(politicaCobranca.getFormaCobranca().isProtestarBoletoRegistrado()){
    							Long qtdDiasProtesto = 0L;
    							if(politicaCobranca.getFormaCobranca().getQuantidadeDiasParaProtesto() != null){
    								qtdDiasProtesto = politicaCobranca.getFormaCobranca().getQuantidadeDiasParaProtesto();
    							}

    							detalheItau.setCodigoInstrucao("34");
    							detalheItau.setPrazo(qtdDiasProtesto.toString());
    						}
        				}
        				list.add(detalheItau);
        				break;
        			case UtilitarioCNAB.BANCO_BRADESCO:
        				CobRegEnvTipoRegistroBradesco01 detalheBradesco = this.montarRegistroBradesco01(boleto, pessoaCedente, banco, count);
        				list.add(detalheBradesco);
        				break;
                    case UtilitarioCNAB.BANCO_CAIXA_ECONOMICA_FEDERAL:
                        CobRegEnvTipoRegistroCaixa01 detalheCaixa = this.montarRegistroCaixa01(boleto, pessoaCedente, banco, count);
                        list.add(detalheCaixa);
                        break;
        			default :
        				CobRegEnvTipoRegistro01 registro01 = this.montarRegistro01(boleto, pessoaCedente, banco, count);
        				list.add(registro01);
        				break;
        		}

        		count++;

        		somaSquencial = count;
        		
        		idsCobrancasParaUpdate.add(divida.getCobrancaId());
        		
        	}
    	}
		
		if(somaSquencial == 0){
			return null;
		}
		
		list.add(this.montarRegistro09(somaSquencial));

		BufferedWriter bw = null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {

			bw = new BufferedWriter(new OutputStreamWriter(baos));
			CobRegParser cobParser = new CobRegParser();

			for (CobRegBaseDTO dto : list) {
				cobParser.parseCobrancaReg(dto, bw);
				bw.append("\r");
				bw.append("\n");
			}

			bw.flush();
			bw.close();
		}catch (Exception e) {
			throw new ValidationException("Erro na geração do arquivo"+ e.getMessage());
		}
		
		cobrancaService.atualizarFlagCobrancaRegistradaGerada(idsCobrancasParaUpdate);
			
		return baos.toByteArray();	

	}

	private int qtdeSequenciaRegistro(CobRegEnvTipoRegistro00 registro00, CobRegEnvTipoRegistroItau00 registroItau) {
		int count = Integer.valueOf(registro00 == null ? registroItau.getSequencial() : registro00.getSequencial())+1;
		return count;
	}

	private int qtdeSequenciaRegistro(CobRegEnvTipoRegistro00 registro00, CobRegEnvTipoRegistroBradesco00 registroBradesco) {
		int count = Integer.valueOf(registro00 == null ? registroBradesco.getSequencial() : registro00.getSequencial())+1;
		return count;
	}

    private int qtdeSequenciaRegistro(CobRegEnvTipoRegistro00 registro00, CobRegEnvTipoRegistroCaixa00 registroCaixa) {
        int count = Integer.valueOf(String.valueOf(registro00 == null ? registroCaixa.getSequencial() : registro00.getSequencial()))+1;
        return count;
    }

	// metodo comum a todos os arquivos
	private CobRegEnvTipoRegistro00 montarRegistro00(Banco banco, Pessoa pessoaCedente) throws ValidationException {
		return RegistroPorBancoBuilder.registroPorBanco(banco, pessoaCedente);
	}

    private CobRegEnvTipoRegistroCaixa00 montarRegistroCaixa(Banco banco, Pessoa pessoaCedente) throws ValidationException {
        return RegistroPorBancoBuilder.registroPorBancoCaixa(banco, pessoaCedente);
    }

	private CobRegEnvTipoRegistroItau00 montarRegistroItau(Banco banco, Pessoa pessoaCedente) throws ValidationException {
		return RegistroPorBancoBuilder.registroPorBancoItau(banco, pessoaCedente);
	}

	private CobRegEnvTipoRegistroBradesco00 montarRegistroBradesco(Banco banco, Pessoa pessoaCedente) throws ValidationException {
		return RegistroPorBancoBuilder.registroPorBancoBradesco(banco, pessoaCedente);
	}

	private CobRegEnvTipoRegistro01 montarRegistro01(Boleto boleto, Pessoa pessoaCedente, Banco banco, int count) throws ValidationException {

		CobRegEnvTipoRegistro01 registro01 = new CobRegEnvTipoRegistro01();

		switch (banco.getNumeroBanco()) {

			case UtilitarioCNAB.BANCO_HSBC:
				return detalheRegistroPorBanco(boleto, banco, count, registro01);

			case UtilitarioCNAB.BANCO_BRADESCO:
				return detalheRegistroPorBanco(boleto, banco, count, registro01);

			case UtilitarioCNAB.BANCO_DO_BRASIL:
				return detalheRegistroPorBanco(boleto, banco, count, registro01);

			case UtilitarioCNAB.BANCO_CAIXA_ECONOMICA_FEDERAL:
				return detalheRegistroPorBanco(boleto, banco, count, registro01);

			case UtilitarioCNAB.BANCO_SANTANDER:
				return detalheRegistroPorBanco(boleto, banco, count, registro01);

			case UtilitarioCNAB.CREDCOMIM:
				return detalheRegistroPorBanco(boleto, banco, count, registro01);

		}

		return registro01;
	}

	private CobRegEnvTipoRegistroItau01 montarRegistroItau01(Boleto boleto, Pessoa pessoaCedente, Banco banco, int count) throws ValidationException {
		return DetalheRegistroRegistroPorBancoBuilder.detalheRegistroPorBancoItau(boleto, banco, count);
	}

	private CobRegEnvTipoRegistroBradesco01 montarRegistroBradesco01(Boleto boleto, Pessoa pessoaCedente, Banco banco, int count) throws ValidationException {
		return DetalheRegistroRegistroPorBancoBuilder.detalheRegistroPorBancoBradesco(boleto, banco, count);
	}

    private CobRegEnvTipoRegistroCaixa01 montarRegistroCaixa01(Boleto boleto, Pessoa pessoaCedente, Banco banco, int count) throws ValidacaoException, ValidationException {
        return DetalheRegistroRegistroPorBancoBuilder.detalheRegistroPorBancoCaixa(boleto, banco, count);
    }

	private CobRegEnvTipoRegistro01 detalheRegistroPorBanco(Boleto boleto, Banco banco, int count, CobRegEnvTipoRegistro01 registro01) throws ValidationException {
		registro01.setTipoRegistro("1");
		registro01.setFiller("");
		registro01.setAgenciaCedente(String.valueOf(banco.getAgencia()));
		registro01.setFiller1("");
		registro01.setContaCliente(String.valueOf(banco.getConta()));
		registro01.setDigitoConta(String.valueOf(banco.getDvConta()));
		registro01.setTaxa(String.valueOf(banco.getJuros().intValue()));
		registro01.setFiller2("");

		// não sei o que setar
		registro01.setNumeroControle("00");
		registro01.setNossoNumero(boleto.getNossoNumero());;
		registro01.setDigitoNossoNumero(boleto.getDigitoNossoNumero());
		registro01.setNumeroContrato("0");
		registro01.setDataSegundoDesconto("0");
		registro01.setValorSegundoDesconto("0");;
		registro01.setFiller3("");
		registro01.setCarteira(String.valueOf(banco.getCarteira()));

		// não sei o que setar
		registro01.setCodigoServico("MX");

		registro01.setNumero(boleto.getNossoNumero());

		try {
			registro01.setDataVencimento(Util.formataData(boleto.getDataVencimento()));
			registro01.setDataEmissao(Util.formataData(boleto.getDataEmissao()));
		} catch (Exception e) {
			throw new ValidationException("Erro ao Formatar a Data Vencimento / Emissão");
		}

		registro01.setValorTitulo(CurrencyUtil.formatarValor(boleto.getValor()).replace(".", ""));
		registro01.setNumeroBanco(banco.getNumeroBanco());
		registro01.setAgencia(String.valueOf(banco.getAgencia()));
		registro01.setFiller4(" ");
		registro01.setEspecie("01");
		registro01.setAceite("A");
		registro01.setJurosDia("0");;
		registro01.setDataDesconto("0");
		registro01.setValorDesconto("0");
		registro01.setValorIOC("0");;
		registro01.setValorAbatimento("0");

		PopularSacadoBuilder.popularSacadoCobrana(registro01, boleto);

		registro01.setSequencialRegistro(String.valueOf(count));

		return registro01;
	}





	private CobRegEnvTipoRegistro09 montarRegistro09(int sequencial) throws ValidationException {

		CobRegEnvTipoRegistro09 registro09 = new CobRegEnvTipoRegistro09();

		registro09.setTipoRegistro("9");
		registro09.setFiller("");
		registro09.setSequencial(String.valueOf(sequencial));

        return registro09;
	}

	@Override
	@Transactional
	public List<BoletoAvulsoDTO> obterDadosBoletoAvulso(FiltroBoletoAvulsoDTO boletoAvulso) {

		List<Cota> cotas = this.boxRepository.obterCotasParaBoletoAvulso(boletoAvulso);

		List<BoletoAvulsoDTO> listaBoleto = new ArrayList<BoletoAvulsoDTO>();

		List<ItemDTO<Integer, String>> bancos = null;

		if(boletoAvulso.getIdBanco() != null && boletoAvulso.getIdBanco() > 0 ) {
			bancos = this.comboBoletoAvulso(boletoAvulso);
		} else {
			bancos = this.bancoService.getComboBancosBoletoAvulso();
		}


		for (int index = 0 ; index < cotas.size() ; index++){

		    Cota itemCota = cotas.get(index);

			listaBoleto.add(new BoletoAvulsoDTO(
		                Long.valueOf(index),
		                null,
		                null,
		                itemCota.getNumeroCota(),
		                itemCota.getPessoa().getNome(),
		                null,
		                null,
		                (boletoAvulso.getValor()!=null?CurrencyUtil.formatarValor(boletoAvulso.getValor().setScale(2, RoundingMode.HALF_UP)):null),
		                boletoAvulso.getObservacao() == null ? "" : boletoAvulso.getObservacao(), bancos));
		}

		return listaBoleto;
	}

	private List<ItemDTO<Integer, String>> comboBoletoAvulso(FiltroBoletoAvulsoDTO boletoAvulso) {

		List<ItemDTO<Integer,String>> comboBancos =  new ArrayList<ItemDTO<Integer,String>>();

		Banco banco = this.bancoService.obterBancoPorId(boletoAvulso.getIdBanco());

		String descricaoBanco =
				(banco.getNumeroBanco() == null ? "" : banco.getNumeroBanco() + "-")
				+ (banco.getApelido() == null ? "" :  banco.getApelido() + " ")
				+ (banco.getConta() == null ? "" : banco.getConta())
				+ (banco.getDvConta() == null ? "" : "-" + banco.getDvConta());

		comboBancos.add(new ItemDTO<Integer,String>(banco.getId().intValue(), descricaoBanco));

		return comboBancos;
	}
}
