package br.com.abril.nds.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.CalculaParcelasVO;
import br.com.abril.nds.client.vo.NegociacaoDividaDetalheVO;

import br.com.abril.nds.dto.ImpressaoNegociacaoDTO;
import br.com.abril.nds.dto.ImpressaoNegociacaoParecelaDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.NegociacaoDividaPaginacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCalculaParcelas;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.financeiro.AcumuloDivida;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.CobrancaCheque;
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
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.financeiro.TipoNegociacao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.AcumuloDividasRepository;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConcentracaoCobrancaCotaRepository;
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
import br.com.abril.nds.service.ImpressaoDividaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.NegociacaoDividaService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.SituacaoCotaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.BigDecimalUtil;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class NegociacaoDividaServiceImpl implements NegociacaoDividaService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(NegociacaoDividaServiceImpl.class);
    
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
    private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
    
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
    
    @Autowired
    private ConcentracaoCobrancaCotaRepository concentracaoCobrancaCotaRepository;
    
    @Autowired
    private SituacaoCotaService situacaoCotaService;
    
    @Autowired
    private AcumuloDividasRepository acumuloDividasRepository;
    
    @Autowired
    private ImpressaoDividaService impressaoDividaService;
    
    @Override
    @Transactional(readOnly = true)
    public NegociacaoDividaPaginacaoDTO obterDividasPorCotaPaginado(final FiltroConsultaNegociacaoDivida filtro) {
        
        final NegociacaoDividaPaginacaoDTO retorno = new NegociacaoDividaPaginacaoDTO();
        
        retorno.setListaNegociacaoDividaDTO(this.obterDividasPorCota(filtro));
        retorno.setQuantidadeRegistros(negociacaoDividaRepository.obterCotaPorNumeroCount(filtro));
        
        return retorno;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<NegociacaoDividaDTO> obterDividasPorCota(final FiltroConsultaNegociacaoDivida filtro) {

    	filtro.setDataOperacao(this.distribuidorService.obterDataOperacaoDistribuidor());
    	
        final List<NegociacaoDividaDTO> dividas = this.negociacaoDividaRepository.obterNegociacaoPorCota(filtro);
        final Cota cota = this.cotaRepository.obterPorNumeroDaCota(filtro.getNumeroCota());
        final Date data = this.distribuidorService.obterDataOperacaoDistribuidor();
        
        final FormaCobranca formaCobrancaPrincipal = this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
        
        for (final NegociacaoDividaDTO divida : dividas) {
            
            if (divida.getPrazo() != 0) {
                
                final Banco banco = this.bancoRepository.buscarBancoPorIdCobranca(divida.getIdCobranca());
                BigDecimal encargo = (divida.getEncargos() != null) ? divida.getEncargos() : BigDecimal.ZERO;
                
                encargo = encargo.add(this.cobrancaService.calcularJuros(banco, cota.getId(), divida.getVlDivida(), divida.getDtVencimento(), data, formaCobrancaPrincipal));
                
                if (divida.getDtVencimento().compareTo(data) < 0) {
                    encargo = encargo.add(this.cobrancaService.calcularMulta(banco, cota, divida.getVlDivida(), formaCobrancaPrincipal));
                }
                divida.setEncargos(encargo.setScale(2, RoundingMode.HALF_EVEN));
                
                divida.setVlDivida(divida.getVlDivida().setScale(DEFAULT_SCALE, RoundingMode.HALF_EVEN));

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
    public Long criarNegociacao(final Integer numeroCota, final List<ParcelaNegociacao> parcelas, final TipoNegociacao tipoNegociacao,
            final BigDecimal valorDividaParaComissao, final List<Long> idsCobrancasOriginarias,
            final Usuario usuarioResponsavel, final boolean negociacaoAvulsa, final Integer ativarCotaAposParcela,
            final BigDecimal comissaoParaSaldoDivida, final boolean isentaEncargos, final FormaCobranca formaCobranca,
            final Long idBanco) {
        
        // lista para mensagens de validação
        final List<String> msgs = new ArrayList<String>();
        final Date dataAtual = new Date();
        
        final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
        
        if (formaCobranca != null) {
            if ( idBanco == null ) {
            	 LOGGER.error("Banco nao encontrado idBanco="+idBanco+" cota="+numeroCota+"  formaCobranca="+formaCobranca);
            	 throw new ValidacaoException(TipoMensagem.WARNING, "Banco nao Selecionado.");
            }
            final Banco banco = bancoRepository.buscarPorId(idBanco);
            formaCobranca.setBanco(banco);
        }
        
        // valida dados de entrada
        this.validarDadosEntrada(msgs, parcelas, valorDividaParaComissao, usuarioResponsavel, comissaoParaSaldoDivida, formaCobranca);
        
        // Cota e Cobrança originária não são validados no método acima
        // para evitar que se faça duas vezes a mesma consulta
        Cota cota = null;
        if (numeroCota == null) {
            
            msgs.add("Cota da negociação não encontrada.");
        } else {
            
            cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
            
            if (cota == null) {
                
                msgs.add("Cota da negociação não encontrada.");
            }
        }
        
        final List<Cobranca> cobrancasOriginarias = new ArrayList<Cobranca>();
        
        final TipoMovimentoFinanceiro tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO);
        
        // Cobrança da onde se originou a negociação
        for (final Long idCobranca : idsCobrancasOriginarias) {
            
            final Cobranca cobrancaOriginaria = cobrancaRepository.buscarPorId(idCobranca);
            
            if (cobrancaOriginaria == null) {
                
                msgs.add("Cobrança de ID " + idCobranca + " não encontrada.");
            } else {
                
                // Cobrança original deve ter seu status modificado para pago
                // e sua divida deve ter seus status modificado para negociada
                cobrancaOriginaria.setStatusCobranca(StatusCobranca.PAGO);
                
                cobrancaOriginaria.setDataPagamento(dataOperacao);
                
                cobrancaOriginaria.getDivida().setStatus(StatusDivida.NEGOCIADA);
                
                dividaRepository.merge(cobrancaOriginaria.getDivida());
                cobrancaRepository.merge(cobrancaOriginaria);
                
                cobrancasOriginarias.add(cobrancaOriginaria);
                
                //buscar dividas pendentes por inadimplencia
                //essas devem ter status de suas cobranças como pagas
                //a única maneira de chegar a elas é pela data do movimento financeiro
                this.alterarStatusCobrancaDividaAcumuladas(cobrancaOriginaria, dataOperacao);
            }
        }
        
        if (!msgs.isEmpty()) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, msgs);
        }
        
        // caso a negociacão seja feita em parcelas
        if (parcelas != null && !parcelas.isEmpty()) {
            
            final Banco banco = formaCobranca.getBanco();
            BigDecimal totalNegociacao = BigDecimal.ZERO;
            // Popula o movimento financeiro de cada parcela
            // Caso seja uma negociação avulsa o movimento financeiro servirá
            // para rastreabilidade da negociação, caso não seja uma negociação
            // avulsa
            // será insumo para próxima geração de cobrança
            for (final ParcelaNegociacao parcelaNegociacao : parcelas) {
                
                final MovimentoFinanceiroCota movFinan = parcelaNegociacao.getMovimentoFinanceiroCota();
                
                movFinan.setCota(cota);
                movFinan.setUsuario(usuarioResponsavel);
                movFinan.setData(parcelaNegociacao.getDataVencimento());
                movFinan.setDataCriacao(dataAtual);
                movFinan.setStatus(StatusAprovacao.APROVADO);
                movFinan.setAprovador(usuarioResponsavel);
                movFinan.setTipoMovimento(tipoMovimentoFinanceiro);
                movFinan.setObservacao("Negociação de dívida.");
                
                if (!isentaEncargos
                		&& parcelaNegociacao.getEncargos() != null
                        && parcelaNegociacao.getEncargos().compareTo(BigDecimal.ZERO) != 0) {

                	movFinan.setValor(movFinan.getValor().add(parcelaNegociacao.getEncargos()));                        
                }

                final Fornecedor fornecedor = cota.getParametroCobranca() != null && cota.getParametroCobranca().getFornecedorPadrao() != null ? 
                		cota.getParametroCobranca().getFornecedorPadrao() : 
                			formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor().getPoliticaCobranca().getFornecedorPadrao();
                        
                if (fornecedor == null) {
                    
                    throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
                    "A [Cota] necessita de um [Fornecedor Padrão] em [Parâmetros] Financeiros !"));
                }
                
                parcelaNegociacao.getMovimentoFinanceiroCota().setFornecedor(fornecedor);

                totalNegociacao = totalNegociacao.add(movFinan.getValor());

                movimentoFinanceiroCotaRepository.adicionar(movFinan);
            }
            
            // Caso essa seja uma negociação avulsa as parcelas não devem entrar 
            // nas próximas
            // gerações de cobrança, para isso é necessário criar um consolidado
            // financeiro para
            // os movimentos financeiros das parcelas
            if (negociacaoAvulsa) {
                
                ConsolidadoFinanceiroCota consolidado = null;
                for (final ParcelaNegociacao parcelaNegociacao : parcelas) {

                    BigDecimal valorTotalParcela = parcelaNegociacao.getMovimentoFinanceiroCota().getValor();
                    
                    if (isentaEncargos && parcelaNegociacao.getEncargos() != null) {

                    	valorTotalParcela.subtract(parcelaNegociacao.getEncargos());
                    }

                    consolidado = new ConsolidadoFinanceiroCota();
                    consolidado.setCota(cota);
                    consolidado.setDataConsolidado(dataOperacao);
                    final List<MovimentoFinanceiroCota> movs = new ArrayList<MovimentoFinanceiroCota>();
                    movs.add(parcelaNegociacao.getMovimentoFinanceiroCota());
                    
                    consolidado.setMovimentos(movs);
                    
                    consolidado.setEncalhe(BigDecimal.ZERO);
                    consolidado.setVendaEncalhe(BigDecimal.ZERO);
                    
                    final GrupoMovimentoFinaceiro grupoMovimentoFinaceiro = ((TipoMovimentoFinanceiro) parcelaNegociacao
                            .getMovimentoFinanceiroCota().getTipoMovimento()).getGrupoMovimentoFinaceiro();
                    
                    consolidado.setPendente(BigDecimal.ZERO);
                    consolidado.setEncargos(BigDecimal.ZERO);

                    if (OperacaoFinaceira.DEBITO.equals(grupoMovimentoFinaceiro.getOperacaoFinaceira())) {
                        
                        consolidado.setDebitoCredito(valorTotalParcela.negate());
                        consolidado.setTotal(valorTotalParcela.negate());

                    } else {
                        
                        consolidado.setDebitoCredito(valorTotalParcela);
                        consolidado.setTotal(valorTotalParcela);                        
                    }
                    
                    consolidadoFinanceiroRepository.adicionar(consolidado);
                    
                    final Divida divida = new Divida();
                    divida.setData(dataOperacao);
                    divida.setResponsavel(usuarioResponsavel);
                    divida.setCota(cota);
                    divida.setValor(parcelaNegociacao.getMovimentoFinanceiroCota().getValor());
                    divida.setStatus(StatusDivida.EM_ABERTO);
                    divida.setConsolidados(Arrays.asList(consolidado));
                    divida.setOrigemNegociacao(true);
                    
                    Cobranca cobranca = null;
                    
                    final TipoCobranca tipoCobranca = formaCobranca.getTipoCobranca();
                    switch (tipoCobranca) {
                    case BOLETO:
                        cobranca = new Boleto();
                        break;
                    case BOLETO_EM_BRANCO:
                        cobranca = new Boleto();
                        break;
                    case CHEQUE:
                        cobranca = new CobrancaCheque();
                        break;
                    case TRANSFERENCIA_BANCARIA:
                        cobranca = new CobrancaTransferenciaBancaria();
                        break;
                    case DEPOSITO:
                    case DINHEIRO:
                    case OUTROS:
                        cobranca = new CobrancaDinheiro();
                        cobranca.setTipoCobranca(tipoCobranca);
                        break;
                    default:
                        break;
                    }
                    
                    if (cobranca != null) {
                        cobranca.setCota(cota);
                        cobranca.setBanco(formaCobranca.getBanco());
                        cobranca.setDivida(divida);
                        cobranca.setDataEmissao(dataOperacao);
                        cobranca.setStatusCobranca(StatusCobranca.NAO_PAGO);
                        cobranca.setDataVencimento(parcelaNegociacao.getDataVencimento());
                        cobranca.setValor(valorTotalParcela);
                        cobranca.setOriundaNegociacaoAvulsa(true);
                        dividaRepository.adicionar(divida);
                        
                        String nossoNumero =
                				Util.gerarNossoNumero(
                					cota.getNumeroCota(), 
                					cobranca.getDataEmissao(), 
                					banco != null ? banco.getNumeroBanco() : "0",
                					null,
                					divida.getId(),
                					banco != null ? banco.getAgencia() : 0,
                					banco != null ? banco.getConta() : 0,
                					banco != null && banco.getCarteira() != null ? banco.getCarteira() : null);
                			
            			cobranca.setNossoNumero(nossoNumero);
            			
            			String digitoVerificador = Util.calcularDigitoVerificador(nossoNumero, banco != null ? banco.getCodigoCedente() : "0", cobranca.getDataVencimento());
            			cobranca.setDigitoNossoNumero(digitoVerificador);
            			cobranca.setNossoNumeroCompleto(nossoNumero + ((digitoVerificador != null) ? digitoVerificador : ""));
                        
                        cobrancaRepository.adicionar(cobranca);
                    }
                }
            }            
        }
        
        if (formaCobranca != null) {
            
            formaCobrancaRepository.adicionar(formaCobranca);
            
            if (formaCobranca.getConcentracaoCobrancaCota() != null) {
                for (final ConcentracaoCobrancaCota c : formaCobranca.getConcentracaoCobrancaCota()) {
                    
                    concentracaoCobrancaCotaRepository.adicionar(c);
                }
            }
        }
        
        // cria registro da negociação
        final Negociacao negociacao = new Negociacao();
        negociacao.setAtivarCotaAposParcela(ativarCotaAposParcela);
        negociacao.setCobrancasOriginarias(cobrancasOriginarias);
        negociacao.setTipoNegociacao(tipoNegociacao);
        negociacao.setComissaoParaSaldoDivida(comissaoParaSaldoDivida == null ? BigDecimal.ZERO
                : comissaoParaSaldoDivida);
        negociacao.setIsentaEncargos(isentaEncargos);
        negociacao.setNegociacaoAvulsa(negociacaoAvulsa);
        negociacao.setFormaCobranca(formaCobranca);
        negociacao.setParcelas(parcelas);
        negociacao.setValorDividaPagaComissao(valorDividaParaComissao);
        negociacao.setValorOriginal(valorDividaParaComissao);
        negociacao.setDataCriacao(new Date());
        
        if (negociacao.getParcelas() != null) {
            
            for (final ParcelaNegociacao parcelaNegociacao : negociacao.getParcelas()) {
                
                parcelaNegociacao.setNegociacao(negociacao);
                
                parcelaNegociacaoRepository.adicionar(parcelaNegociacao);
            }
        }
        
        negociacaoDividaRepository.adicionar(negociacao);
        
        return negociacao.getId();
    }
    
    private void alterarStatusCobrancaDividaAcumuladas(Cobranca cobranca, Date dataOperacao){
        
        for (ConsolidadoFinanceiroCota con : cobranca.getDivida().getConsolidados()){
            
            for (MovimentoFinanceiroCota mfc : con.getMovimentos()){
                
                List<AcumuloDivida> dividasPendentesIndadimplencia = 
                        this.acumuloDividasRepository.buscarDividasPendentesIndimplencia(
                                mfc.getDataCriacao(),
                                cobranca);
                
                for (AcumuloDivida acumulo : dividasPendentesIndadimplencia){
                    
                    Cobranca cobrancaPendente = acumulo.getDividaAnterior().getCobranca();
                    
                    cobrancaPendente.setStatusCobranca(StatusCobranca.PAGO);
                    cobrancaPendente.setDataPagamento(dataOperacao);
                    this.cobrancaRepository.merge(cobrancaPendente);
                    
                    acumulo.setStatus(StatusInadimplencia.QUITADA);
                    this.acumuloDividasRepository.merge(acumulo);
                }
            }
        }
    }

    private void validarDadosEntrada(final List<String> msgs, final List<ParcelaNegociacao> parcelas,
            final BigDecimal valorDividaParaComissao, final Usuario usuarioResponsavel,
            final BigDecimal comissaoParaSaldoDivida, final FormaCobranca formaCobranca) {
        
        final List<TipoCobranca> tiposCobranca = Arrays.asList(TipoCobranca.DINHEIRO, TipoCobranca.OUTROS);
        
        // caso não tenha parcelas e nem comissão para saldo preenchidos
        if ((formaCobranca != null && !tiposCobranca.contains(formaCobranca.getTipoCobranca()))
                && ((parcelas == null || parcelas.isEmpty()) && comissaoParaSaldoDivida == null)) {
            
            msgs.add("Forma de pagamento é obrigatória.");
        }
        
        // caso tenha parcelas
        if (parcelas != null && comissaoParaSaldoDivida == null) {
            
            // data de vencimento e valor são campos obrigatórios
            for (final ParcelaNegociacao parcelaNegociacao : parcelas) {
                
                if (parcelaNegociacao.getDataVencimento() == null) {
                    
                    msgs.add("Data de vencimento da parcela " + (parcelas.indexOf(parcelaNegociacao) + 1)
                        + " inválido.");
                }
                
                final MovimentoFinanceiroCota mov = parcelaNegociacao.getMovimentoFinanceiroCota();
                
                if (mov == null) {
                    
                    msgs.add("Valor da parcela " + (parcelas.indexOf(parcelaNegociacao) + 1) + " inválido.");
                } else {
                    
                    if (mov.getValor() == null || mov.getValor().equals(BigDecimal.ZERO)) {
                        
                        msgs.add("Valor da parcela " + (parcelas.indexOf(parcelaNegociacao) + 1) + " inválido.");
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
                
                if (formaCobranca.getBanco() == null || formaCobranca.getBanco().getId() == null) {
                    
                    msgs.add("Banco é obrigatório.");
                }
                
                if (formaCobranca.getTipoFormaCobranca() == null) {
                    
                    msgs.add("Frequência da cobrança é obrigatório.");
                } else {
                    
                    switch (formaCobranca.getTipoFormaCobranca()) {
                    case DIARIA:
                        break;
                    case MENSAL:
                        if (formaCobranca.getDiasDoMes() == null || formaCobranca.getDiasDoMes().isEmpty()
                        || formaCobranca.getDiasDoMes().size() != 1) {
                            
                            msgs.add("Parâmetro dias da cobrança inválidos.");
                        }
                        break;
                    case QUINZENAL:
                        if (formaCobranca.getDiasDoMes() == null || formaCobranca.getDiasDoMes().isEmpty()
                        || formaCobranca.getDiasDoMes().size() != 2) {
                            
                            msgs.add("Parâmetro dias da cobrança inválidos.");
                        }
                        break;
                    case SEMANAL:
                        if (formaCobranca.getConcentracaoCobrancaCota() == null
                        || formaCobranca.getConcentracaoCobrancaCota().isEmpty()) {
                            msgs.add("Selecione pelo menos um dia da semana.");
                        }
                        break;
                    default:
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Negociacao obterNegociacaoPorId(final Long idNegociacao) {
        
        final Negociacao negociacao = negociacaoDividaRepository.buscarPorId(idNegociacao);
        
        Hibernate.initialize(negociacao.getParcelas());
        Hibernate.initialize(negociacao.getCobrancasOriginarias());
        
        return negociacao;
    }
    
    @Override
    @Transactional
    public List<byte[]> gerarBoletosNegociacao(final Long idNegociacao) {
        
        final List<byte[]> boletos = new ArrayList<byte[]>();
        
        final Negociacao negociacao = negociacaoDividaRepository.buscarPorId(idNegociacao);
        
        if (negociacao != null) {
            
            if (TipoNegociacao.PAGAMENTO_AVULSO.equals(negociacao.getTipoNegociacao())) {
                
                for (final ParcelaNegociacao parcelaNegociacao : negociacao.getParcelas()) {
                    
                    final String nossoNumero = cobrancaRepository
                            .obterNossoNumeroPorMovimentoFinanceiroCota(parcelaNegociacao.getMovimentoFinanceiroCota()
                                    .getId());
                    
                    boletos.add(documentoCobrancaService.gerarDocumentoCobranca(nossoNumero));
                }
            }
        }
        
        return boletos;
    }
    
    @Override
    @Transactional(readOnly=true) 
    public List<byte[]> imprimirRecibos(final Long idNegociacao) {
    	
    	final List<String> listaNossoNumero = this.negociacaoDividaRepository.obterListaNossoNumeroPorNegociacao(idNegociacao);

    	List<byte[]> recibos = new ArrayList<byte[]>();
    	
    	for (String nossoNumero : listaNossoNumero) {
    		
    		recibos.add(this.impressaoDividaService.gerarArquivoImpressao(nossoNumero));
    	}
    	
    	return recibos;
    }
    
    @Override
    @Transactional(readOnly = true)
    public byte[] imprimirNegociacao(final Long idNegociacao, final String valorDividaSelecionada) throws Exception {
        
        final Negociacao negociacao = this.obterNegociacaoPorId(idNegociacao);
        
        if (negociacao == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Negociação não encontrada.");
        }
        
        final Cota cota = negociacao.getCobrancasOriginarias().get(0).getCota();
        
        final ImpressaoNegociacaoDTO impressaoNegociacaoDTO = new ImpressaoNegociacaoDTO();
        // campo cota(numero)
        impressaoNegociacaoDTO.setNumeroCota(cota.getNumeroCota());
        // campo nome
        impressaoNegociacaoDTO.setNomeCota(cota.getPessoa().getNome());
        
        // campo Comissão da Cota para pagamento da dívida
        impressaoNegociacaoDTO.setComissaoParaPagamento(negociacao.getComissaoParaSaldoDivida());
        
        if (negociacao.getParcelas() == null || negociacao.getParcelas().isEmpty()) {
            
            BigDecimal comissaoAtual = descontoService.obterComissaoCota(cota.getNumeroCota());
            
            if (comissaoAtual == null) {
                
                comissaoAtual = BigDecimal.ZERO;
            }
            
            // campo Comissão da Cota
            impressaoNegociacaoDTO.setComissaoAtualCota(comissaoAtual.setScale(2, RoundingMode.HALF_EVEN));
            
            // campo Comissão da Cota enquanto houver saldo de dívida
            impressaoNegociacaoDTO.setComissaoCotaEnquantoHouverSaldo(comissaoAtual.subtract(negociacao
                    .getComissaoParaSaldoDivida()));
        }
        
        // campo frequencia de pagamento
        if (negociacao.getFormaCobranca() != null) {
            
            final StringBuilder aux = new StringBuilder();
            
            final TipoFormaCobranca tipoFormaCobranca = negociacao.getFormaCobranca().getTipoFormaCobranca();
            
            switch (tipoFormaCobranca) {
            case DIARIA:
                
                break;
            case MENSAL:
                aux.append("Todo dia ").append(negociacao.getFormaCobranca().getDiasDoMes().get(0));
                break;
            case QUINZENAL:
                aux.append("Todo dia ");
                aux.append(negociacao.getFormaCobranca().getDiasDoMes().get(0));
                aux.append(" e ");
                aux.append(negociacao.getFormaCobranca().getDiasDoMes().get(1));
                break;
            case SEMANAL:
                for (final ConcentracaoCobrancaCota concen : negociacao.getFormaCobranca()
                        .getConcentracaoCobrancaCota()) {
                    
                    if (aux.length() > 0) {
                        aux.append(", ");
                    }
                    
                    aux.append(concen.getDiaSemana().getDescricaoDiaSemana());
                }
                break;
            default:
                break;
            }
            
            impressaoNegociacaoDTO.setFrequenciaPagamento(negociacao.getFormaCobranca().getTipoFormaCobranca()
                    .getDescricao()
                    + (!negociacao.getFormaCobranca().getTipoFormaCobranca().equals(TipoFormaCobranca.DIARIA) ? ": " : " ")
                    + aux);
            
            final Banco banco = negociacao.getFormaCobranca().getBanco();
            
            impressaoNegociacaoDTO.setNumeroBanco(banco.getNumeroBanco());
            impressaoNegociacaoDTO.setNomeBanco(banco.getNome());
            impressaoNegociacaoDTO.setAgenciaBanco(banco.getAgencia());
            impressaoNegociacaoDTO.setContaBanco(banco.getConta());
        }
        
        // campo negociacao avulsa
        impressaoNegociacaoDTO.setNegociacaoAvulsa(TipoNegociacao.PAGAMENTO_AVULSO.equals(negociacao.getTipoNegociacao()));
        // campo isenta encargos
        impressaoNegociacaoDTO.setIsentaEncargos(negociacao.isIsentaEncargos());
        
        impressaoNegociacaoDTO.setParcelasCheques(new ArrayList<ImpressaoNegociacaoParecelaDTO>());
        
        BigDecimal totalParcelas = BigDecimal.ZERO;
        for (final ParcelaNegociacao parcela : negociacao.getParcelas()) {
            
            final ImpressaoNegociacaoParecelaDTO vo = new ImpressaoNegociacaoParecelaDTO();
            
            final int nParcela = negociacao.getParcelas().indexOf(parcela) + 1;
            
            vo.setNumeroParcela(nParcela);
            
            if (negociacao.getAtivarCotaAposParcela() != null && negociacao.getAtivarCotaAposParcela() == nParcela) {
                
                vo.setAtivarAoPagar(true);
            }
            
            vo.setDataVencimento(parcela.getDataVencimento());
            vo.setNumeroCheque(parcela.getNumeroCheque());
            
            final BigDecimal encargos = parcela.getEncargos() == null ? BigDecimal.ZERO : parcela.getEncargos();
            
            vo.setValor(parcela.getMovimentoFinanceiroCota().getValor().subtract(encargos));
            vo.setEncagos(encargos);
            vo.setParcelaTotal(parcela.getMovimentoFinanceiroCota().getValor());
            
            totalParcelas = totalParcelas.add(vo.getParcelaTotal());
            
            impressaoNegociacaoDTO.getParcelasCheques().add(vo);
        }
        
        // campo divida selecionada (este valorDividaSelecionada é retornado do
        // resultado que aparece no html)
        if (BigDecimal.ZERO.equals(totalParcelas)) {
            impressaoNegociacaoDTO.setTotalDividaSelecionada(CurrencyUtil.converterValor(valorDividaSelecionada));
        } else {
            impressaoNegociacaoDTO.setTotalDividaSelecionada(totalParcelas);
        }
        
        final List<ImpressaoNegociacaoDTO> listaJasper = new ArrayList<ImpressaoNegociacaoDTO>();
        listaJasper.add(impressaoNegociacaoDTO);
        
        final JRDataSource jrDataSource = new JRBeanCollectionDataSource(listaJasper);
        
        final URL diretorioReports = Thread.currentThread().getContextClassLoader().getResource("reports/");
        
        String path = diretorioReports.toURI().getPath();
        
        final Map<String, Object> parameters = new HashMap<String, Object>();

        if (TipoNegociacao.COMISSAO.equals(negociacao.getTipoNegociacao())) {
            
            path += "/negociacao_divida_comissao.jasper";

        } else if (TipoCobranca.CHEQUE.equals(negociacao.getFormaCobranca().getTipoCobranca())) {

        	path += "/negociacao_divida_cheque.jasper";

        } else {

        	path += "/negociacao_divida_boleto.jasper";
        	
            parameters.put("TIPO_COBRANCA", negociacao.getFormaCobranca().getTipoCobranca().getDescricao());
        }

        InputStream inputStream = parametrosDistribuidorService.getLogotipoDistribuidor();
        
        if (inputStream == null) {
            inputStream = new ByteArrayInputStream(new byte[0]);
        }
        
        parameters.put("TOTAL_PARCELAS", CurrencyUtil.formatarValor(totalParcelas.setScale(2, RoundingMode.HALF_EVEN)));
        parameters.put("SUBREPORT_DIR", diretorioReports.toURI().getPath());
        
        final String nomeDistribuidor = distribuidorService.obterRazaoSocialDistribuidor();
        parameters.put("NOME_DISTRIBUIDOR", nomeDistribuidor);
        
        parameters.put("LOGO_DISTRIBUIDOR", inputStream);
        
        parameters.put("COTA_ATIVA", SituacaoCadastro.ATIVO.equals(cota.getSituacaoCadastro()));
        
        return JasperRunManager.runReportToPdf(path, parameters, jrDataSource);
    }
    
    @Override
    @Transactional
    public List<NegociacaoDividaDetalheVO> obterDetalhesCobranca(final Long idCobranca) {
        
        if (idCobranca == null) {
            
            throw new ValidacaoException(TipoMensagem.WARNING, "Id da cobrança inválido.");
        }
        
        return cobrancaRepository.obterDetalhesCobranca(idCobranca);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CalculaParcelasVO> recalcularParcelas(final FiltroCalculaParcelas filtro,
            final List<CalculaParcelasVO> parcelas) {
        
        final BigDecimal valorSelecionado = filtro.getValorSelecionadoSemEncargo();              

        Collections.sort(parcelas, new Comparator<CalculaParcelasVO>() {
            
            @Override
            public int compare(final CalculaParcelasVO o1, final CalculaParcelasVO o2) {
                return o1.getNumParcela().compareTo(o2.getNumParcela());
            }
        });
        
        BigDecimal valorParcelasModificadas = BigDecimal.ZERO;

        int qtdParcelasModificadas = 0;
        
        for (final CalculaParcelasVO calculaParcelasVO : parcelas) {
            if (calculaParcelasVO.isModificada()) {
                
                if (calculaParcelasVO.getParcela() == null){
                    
                    throw new ValidacaoException(TipoMensagem.WARNING, "Valor de parcela inválido");
                }
                
                valorParcelasModificadas = valorParcelasModificadas.add(CurrencyUtil.converterValor(calculaParcelasVO.getParcela()));
                qtdParcelasModificadas++;
            }
            
        }
        final BigDecimal qtd = BigDecimal.valueOf(filtro.getQntdParcelas() - qtdParcelasModificadas);
        BigDecimal novoValorParcela;
        if (qtd.intValue() > 0) {
            novoValorParcela = (valorSelecionado.subtract(valorParcelasModificadas)).divide(qtd,
                    DEFAULT_SCALE, RoundingMode.HALF_UP);
            
        } else {
            novoValorParcela = BigDecimal.ZERO;
        }
        BigDecimal valorParcela;
        BigDecimal valorTotal = BigDecimal.ZERO;

        Integer qtdParcelas = filtro.getQntdParcelas() != null ? filtro.getQntdParcelas() : 0; 

        BigDecimal somaEncargo = BigDecimal.ZERO;
        BigDecimal valorEncargoPorParcela = 
        		filtro.getValorEncargoSelecionado().divide(BigDecimal.valueOf(filtro.getQntdParcelas()), RoundingMode.HALF_UP);

        Date dataBase = null;
        
        for (int i = 0; i < parcelas.size(); i++) {
            
            final CalculaParcelasVO calculaParcelasVO = parcelas.get(i);
            if (!calculaParcelasVO.isModificada()) {
                if (i == parcelas.size() - 1) {
                    novoValorParcela = valorSelecionado.subtract(valorTotal);
                    novoValorParcela = novoValorParcela.setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
                }
                valorParcela = novoValorParcela;
                calculaParcelasVO.setParcela(CurrencyUtil.formatarValor(novoValorParcela));
                
                if (dataBase != null) {
                	dataBase = 
                			this.getDataParcela(
                				dataBase, 
                				filtro.getPeriodicidade(), 
                				filtro.getSemanalDias(), 
                				filtro.getQuinzenalDia1(), 
                				filtro.getQuinzenalDia2(), 
                				filtro.getMensalDia());

                	calculaParcelasVO.setDataVencimento(DateUtil.formatarDataPTBR(dataBase));
                }
                
            } else {
        		
                valorParcela = CurrencyUtil.converterValor(calculaParcelasVO.getParcela());

                dataBase = DateUtil.parseDataPTBR(calculaParcelasVO.getDataVencimento());
            }

            valorTotal = valorTotal.add(valorParcela);
            
            if (valorTotal.compareTo(valorSelecionado) > 0) {
            	
            	throw new ValidacaoException(TipoMensagem.WARNING, "A soma das parcelas ultrapassa o valor da dívida.");
            }

            if (BigDecimalUtil.eq(somaEncargo, filtro.getValorEncargoSelecionado())) {

            	valorEncargoPorParcela = BigDecimal.ZERO;
            }

            if (!TipoCobranca.CHEQUE.equals(filtro.getTipoPagamento())
                    && (filtro.getIsentaEncargos() != null && !filtro.getIsentaEncargos())) {
                
            	if (i == qtdParcelas - 1) {
                    valorEncargoPorParcela = filtro.getValorEncargoSelecionado().subtract(somaEncargo);
                    valorEncargoPorParcela = valorEncargoPorParcela.setScale(DEFAULT_SCALE, RoundingMode.HALF_UP);
                }
                
                somaEncargo = somaEncargo.add(valorEncargoPorParcela);
            
            } else {

            	valorEncargoPorParcela = BigDecimal.ZERO;
            }
            	
            calculaParcelasVO.setEncargos(CurrencyUtil.formatarValor(valorEncargoPorParcela));
            calculaParcelasVO.setParcTotal(CurrencyUtil.formatarValor(valorParcela.add(valorEncargoPorParcela)));
            
        }
        
        if (valorTotal.compareTo(valorSelecionado) != 0) {
            throw new ValidacaoException(TipoMensagem.WARNING, "A Soma de todas as parcelas deve ser "
                    + CurrencyUtil.formatarValor(filtro.getValorSelecionado()) + "!");
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
    private Date calculaParcela(final FiltroCalculaParcelas filtro, final BigDecimal valorParcela, final Date dataBase,
            final int numeroParcela, final CalculaParcelasVO parcela, final FormaCobranca formaCobranca) {
        Date dataParcela;
        parcela.setNumParcela(Integer.toString(numeroParcela + 1));
        parcela.setParcela(CurrencyUtil.formatarValor(valorParcela));
        
        dataParcela = getDataParcela(dataBase, filtro.getPeriodicidade(), filtro.getSemanalDias(), filtro
                .getQuinzenalDia1(), filtro.getQuinzenalDia2(), filtro.getMensalDia());
        
        if (formaCobranca.isVencimentoDiaUtil()) {
            dataParcela = calendarioService.adicionarDiasUteis(dataParcela, 0);
        }
        
        parcela.setDataVencimento(DateUtil.formatarDataPTBR(dataParcela));
        
        return dataParcela;
    }
    
    /**
     * Obtem valor minimo de cobrança das configurações de parametro de cobrança
     * 
     * @param numeroCota
     * @return BigDecimal
     */
    private BigDecimal obterValorMinimoEmissao(final Integer numeroCota) {
        
        final Cota cota = cotaRepository.obterPorNumeroDaCota(numeroCota);
        
        BigDecimal valorMinimoCobranca = cota.getValorMinimoCobranca();
                
                if (valorMinimoCobranca == null) {
                    
                    final FormaCobranca formaCobranca = formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
                    
                    valorMinimoCobranca = formaCobranca != null ? cota.getValorMinimoCobranca() : null;
                }
                
                return valorMinimoCobranca;
    }
    
    /**
     * Obtem quantidade de parcelas para que o valor das parcelas esteja de
     * acordo com o valor mínimo dos parametros de cobrança
     * 
     * @param filtro
     * @return Integer
     */
    @Transactional
    @Override
    public Integer obterQuantidadeParcelasConformeValorMinimo(final FiltroCalculaParcelas filtro) {
        
        final BigDecimal valorMinimoCobranca = this.obterValorMinimoEmissao(filtro.getNumeroCota());
        
        final BigDecimal valorTotal = filtro.getValorSelecionado();
        
        if (valorTotal == null || valorTotal.compareTo(valorMinimoCobranca) < 0) {
            
            return 1;
        }
        
        int quantidadeParcelas = distribuidorService.obter().getNegociacaoAteParcelas();
        
        BigDecimal valorParcela = BigDecimal.ZERO;
        
        do {
            
            valorParcela = valorTotal.divide(BigDecimal.valueOf(quantidadeParcelas), DEFAULT_SCALE,
                    RoundingMode.HALF_EVEN);
            
            quantidadeParcelas--;
        } while ((valorParcela.compareTo(valorMinimoCobranca) < 0) && (quantidadeParcelas >= 0));
        
        return Integer.valueOf(quantidadeParcelas + 1);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CalculaParcelasVO> calcularParcelas(final FiltroCalculaParcelas filtro) {
        final List<CalculaParcelasVO> listParcelas = new ArrayList<CalculaParcelasVO>();
        
        final Integer qntParcelas = filtro.getQntdParcelas();
        if ( filtro.getValorSelecionadoSemEncargo() == null ) return listParcelas;
        if ( filtro.getValorEncargoSelecionado() == null ) return listParcelas;
        BigDecimal valorParcela = filtro.getValorSelecionadoSemEncargo().divide(BigDecimal.valueOf(qntParcelas),
                DEFAULT_SCALE, RoundingMode.HALF_EVEN);
        BigDecimal somaParelas = BigDecimal.ZERO;
        
        BigDecimal valorEncargo = filtro.getValorEncargoSelecionado().divide(BigDecimal.valueOf(qntParcelas),
                DEFAULT_SCALE, RoundingMode.HALF_EVEN);
        BigDecimal somaEncargo = BigDecimal.ZERO;
        
        Date dataBase = this.distribuidorService.obterDataOperacaoDistribuidor();
        
        final FormaCobranca formaCobranca = formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
        
        for (int i = 0; i < qntParcelas; i++) {
            final CalculaParcelasVO parcela = new CalculaParcelasVO();
            
            if (BigDecimalUtil.eq(somaEncargo, filtro.getValorEncargoSelecionado())) {

            	valorEncargo = BigDecimal.ZERO;
            }

            if (i == qntParcelas - 1) {
                valorParcela = filtro.getValorSelecionadoSemEncargo().subtract(somaParelas);
                valorParcela = valorParcela.setScale(DEFAULT_SCALE, RoundingMode.HALF_EVEN);
            }
            
            somaParelas = somaParelas.add(valorParcela);
            
            if (!TipoCobranca.CHEQUE.equals(filtro.getTipoPagamento())
                    && (filtro.getIsentaEncargos() != null && !filtro.getIsentaEncargos())) {

                if (i == qntParcelas - 1) {
                    valorEncargo = filtro.getValorEncargoSelecionado().subtract(somaEncargo);
                    valorEncargo = valorEncargo.setScale(DEFAULT_SCALE, RoundingMode.HALF_EVEN);
                }
                
                somaEncargo = somaEncargo.add(valorEncargo);

            } else {
                
                valorEncargo = BigDecimal.ZERO;
            }

            
            dataBase = calculaParcela(filtro, valorParcela, dataBase, i, parcela, formaCobranca);
            parcela.setEncargos(CurrencyUtil.formatarValor(valorEncargo));

            parcela.setParcTotal(CurrencyUtil.formatarValor(valorParcela.add(valorEncargo)));
            
            listParcelas.add(parcela);
        }
        return listParcelas;
    }
    
    private Date getDataParcela(final Date dataBase, final TipoFormaCobranca periodicidade,
            final List<DiaSemana> semanalDias, Integer quinzenalDia1, Integer quinzenalDia2, final Integer diaMensal) {
        
        Calendar proximoDia = DateUtil.toCalendar(dataBase);
        
        int mesBase = proximoDia.get(Calendar.MONTH);
        
        switch (periodicidade) {
        
        case DIARIA:
            
            return calendarioService.adicionarDiasUteis(dataBase, 1);
            
        case SEMANAL:
            
            if (semanalDias == null || semanalDias.isEmpty()) {
                
                throw new ValidacaoException(TipoMensagem.WARNING, "Dia(s) da semana não selecionado(s).");
            }
            
            while (true) {
                
                proximoDia = DateUtil.adicionarDias(proximoDia, 1);
                
                for (final DiaSemana dia : semanalDias) {
                    
                    if (proximoDia.get(Calendar.DAY_OF_WEEK) == dia.getCodigoDiaSemana()) {
                        return proximoDia.getTime();
                    }
                }
            }
            
        case QUINZENAL:
            
            if (quinzenalDia1 == null || quinzenalDia1.compareTo(0) == 0 || quinzenalDia2 == null
            || quinzenalDia2.compareTo(0) == 0) {
                
                throw new ValidacaoException(TipoMensagem.WARNING, "Dia(s) quinzenal(ais) inválido(s).");
            }
            
            if (quinzenalDia1.compareTo(31) == 1 || quinzenalDia2.compareTo(31) == 1) {
                
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Dia(s) quinzenal(ais) não deve(m) ser maior do que 31.");
            }
            
            if (quinzenalDia1.compareTo(quinzenalDia2) >= 0) {
                
                throw new ValidacaoException(TipoMensagem.WARNING, "O 1º dia deve ser menor que o 2º.");
            }
            
            while (true) {
                
                if (quinzenalDia1 > proximoDia.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    
                    quinzenalDia1 = proximoDia.getActualMaximum(Calendar.DAY_OF_MONTH);
                }
                
                proximoDia.set(Calendar.DAY_OF_MONTH, quinzenalDia1);
                
                if (proximoDia.getTime().compareTo(dataBase) == 1) {
                    
                    return proximoDia.getTime();
                }
                
                if (quinzenalDia2 > proximoDia.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    
                    quinzenalDia2 = proximoDia.getActualMaximum(Calendar.DAY_OF_MONTH);
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
                
                throw new ValidacaoException(TipoMensagem.WARNING, "Dia do mês inválido.");
            }
            
            if (diaMensal.compareTo(31) == 1) {
                
                throw new ValidacaoException(TipoMensagem.WARNING, "Dia do mês não deve ser maior do que 31.");
            }
            
            while (true) {
                
                if (diaMensal > proximoDia.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    
                    proximoDia.set(Calendar.DAY_OF_MONTH, proximoDia.getActualMaximum(Calendar.DAY_OF_MONTH));
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
        default:
            break;
        }
        
        return null;
    }
    
    @Override
    @Transactional
    public void abaterNegociacaoPorComissao(final Long idCota, BigDecimal valorTotalReparte, BigDecimal valorTotalEncalhe, final Usuario usuario) {
        
        // verifica se existe valor para abater das negociações
        if (valorTotalReparte != null && valorTotalReparte.compareTo(BigDecimal.ZERO) > 0) {
            
        	valorTotalEncalhe = valorTotalEncalhe == null ? BigDecimal.ZERO : valorTotalEncalhe;
        	
            // busca negociações por comissão ainda não quitadas
            final Negociacao negociacao = negociacaoDividaRepository.obterNegociacaoPorComissaoCota(idCota);
            
            if (negociacao != null) {           	
                	
                // caso todo o valor da conferencia tenha sido usado para quitação das negociações
                if (valorTotalReparte.compareTo(BigDecimal.ZERO) <= 0) {
                    return;
                }
                
                final BigDecimal comissao = negociacao.getComissaoParaSaldoDivida();
                final BigDecimal valorVenda = valorTotalReparte.subtract(valorTotalEncalhe);
                
                final BigDecimal valorComissao = valorVenda.multiply(comissao.divide(BigDecimalUtil.CEM));                 

                final BigDecimal valorRestanteNegociacao = negociacao.getValorDividaPagaComissao().subtract(valorComissao);

                BigDecimal valorMovimentoNegociacao = BigDecimal.ZERO;
                
                if (valorRestanteNegociacao.compareTo(BigDecimal.ZERO) > 0) {

                	valorMovimentoNegociacao = valorComissao;

                	negociacao.setValorDividaPagaComissao(valorRestanteNegociacao);

                } else {

                    // gera crédito para cota caso a comissão gere sobra na quitação
                    final MovimentoFinanceiroCota movimentoFinanceiro = 
                    		this.criarMovimentoFinanceiroPorGrupo(idCota, valorRestanteNegociacao.negate(), usuario, GrupoMovimentoFinaceiro.CREDITO);

                    this.movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiro);

                	valorMovimentoNegociacao = valorComissao;
                	
                    negociacao.setValorDividaPagaComissao(BigDecimal.ZERO);
                }

                final MovimentoFinanceiroCota movimentoFinanceiro = 
                		this.criarMovimentoFinanceiroPorGrupo(idCota, valorMovimentoNegociacao, usuario, GrupoMovimentoFinaceiro.NEGOCIACAO_COMISSAO);
                
                movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiro);
                
                if (negociacao.getMovimentosFinanceiroCota() == null) {
                    
                    negociacao.setMovimentosFinanceiroCota(new ArrayList<MovimentoFinanceiroCota>());
                }
                
                negociacao.getMovimentosFinanceiroCota().add(movimentoFinanceiro);
                
                negociacaoDividaRepository.alterar(negociacao);
            }
        }
    }
    
    private MovimentoFinanceiroCota criarMovimentoFinanceiroPorGrupo(Long idCota, BigDecimal valor, Usuario usuario, GrupoMovimentoFinaceiro grupoMovimentoFinaceiro) {
    	
        final Cota cota = cotaRepository.buscarPorId(idCota);
        
        final TipoMovimentoFinanceiro tipoMovimentoFinanceiro = tipoMovimentoFinanceiroRepository
                .buscarTipoMovimentoFinanceiro(grupoMovimentoFinaceiro);
        
    	final Date dataOperacao = distribuidorService.obterDataOperacaoDistribuidor();
    	
    	String observacao = GrupoMovimentoFinaceiro.CREDITO.equals(grupoMovimentoFinaceiro) ? "Acerto de negociação por comissão" : "Negociação por comissão";
        
    	final MovimentoFinanceiroCotaDTO movDTO = new MovimentoFinanceiroCotaDTO();
        movDTO.setAprovacaoAutomatica(true);
        movDTO.setCota(cota);
        movDTO.setDataAprovacao(dataOperacao);
        movDTO.setDataCriacao(dataOperacao);
        movDTO.setDataOperacao(dataOperacao);
        movDTO.setDataVencimento(movDTO.getDataAprovacao());
        movDTO.setObservacao(observacao);
        movDTO.setTipoEdicao(TipoEdicao.INCLUSAO);
        movDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
        movDTO.setUsuario(usuario);
        movDTO.setValor(valor);
        
        return movimentoFinanceiroCotaService.gerarMovimentoFinanceiroCota(movDTO, null);
    }

    @Transactional
    @Override
    public void verificarAtivacaoCotaAposPgtoParcela(Cobranca cobranca, Usuario usuario){
        
        if (this.negociacaoDividaRepository.verificarAtivacaoCotaAposPgtoParcela(cobranca.getId())){
            
            HistoricoSituacaoCota novoHistoricoSituacaoCota = new HistoricoSituacaoCota();
            
            novoHistoricoSituacaoCota.setCota(cobranca.getCota());
            
            Date dataOperacaoDistribuidor = this.distribuidorService.obterDataOperacaoDistribuidor();
            
            novoHistoricoSituacaoCota.setTipoEdicao(TipoEdicao.INCLUSAO);
            
            novoHistoricoSituacaoCota.setResponsavel(usuario);
            
            novoHistoricoSituacaoCota.setSituacaoAnterior(
                novoHistoricoSituacaoCota.getCota().getSituacaoCadastro());
            
            novoHistoricoSituacaoCota.setDataInicioValidade(dataOperacaoDistribuidor);
            
            novoHistoricoSituacaoCota.setNovaSituacao(SituacaoCadastro.ATIVO);
            
            novoHistoricoSituacaoCota.setDescricao("Pagamento Boleto - Negociação");
            
            this.situacaoCotaService.atualizarSituacaoCota(
                novoHistoricoSituacaoCota, dataOperacaoDistribuidor);
        }
    }
}
