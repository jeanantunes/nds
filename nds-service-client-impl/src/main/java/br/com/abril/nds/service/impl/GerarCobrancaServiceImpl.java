package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.baixaboleto.TipoEmissaoDocumento;
import br.com.abril.nds.dto.GerarCobrancaHelper;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroDistribuicaoCota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.cadastro.TipoParametrosDistribuidorEmissaoDocumento;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.BoletoDistribuidor;
import br.com.abril.nds.model.financeiro.BoletoEmail;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.CobrancaBoletoEmBranco;
import br.com.abril.nds.model.financeiro.CobrancaCheque;
import br.com.abril.nds.model.financeiro.CobrancaDeposito;
import br.com.abril.nds.model.financeiro.CobrancaDinheiro;
import br.com.abril.nds.model.financeiro.CobrancaOutros;
import br.com.abril.nds.model.financeiro.CobrancaTransferenciaBancaria;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Negociacao;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoletoDistribuidorRepository;
import br.com.abril.nds.repository.BoletoEmailRepository;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.CotaUnificacaoRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.ItemChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.repository.ParametrosDistribuidorEmissaoDocumentoRepository;
import br.com.abril.nds.repository.ParcelaNegociacaoRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.BoletoEmailService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.GeradorArquivoCobrancaBancoService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class GerarCobrancaServiceImpl implements GerarCobrancaService {
    
    private static final Logger LOGGER = Logger.getLogger(GerarCobrancaServiceImpl.class);

	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Autowired
	private DividaRepository dividaRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private DocumentoCobrancaService documentoCobrancaService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private GeradorArquivoCobrancaBancoService geradorArquivoCobrancaBancoService;
	
	@Autowired
	private CobrancaControleConferenciaEncalheCotaRepository cobrancaControleConferenciaEncalheCotaRepository;
	
	@Autowired
	private BoletoDistribuidorRepository boletoDistribuidorRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ItemChamadaEncalheFornecedorRepository itemChamadaEncalheFornecedorRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private FormaCobrancaService formaCobrancaService;
	
	@Autowired
	private BoletoEmailService boletoEmailService;
	
	@Autowired
	private NegociacaoDividaRepository negociacaoRepository;
	
	@Autowired
	private BoletoEmailRepository boletoEmailRepository;
	
	@Autowired
	private ParcelaNegociacaoRepository parcelaNegociacaoRepository;
	
	@Autowired
	private ParametrosDistribuidorEmissaoDocumentoRepository parametrosDistribuidorEmissaoDocumentoRepository;
	
	@Autowired
	private CotaUnificacaoRepository cotaUnificacaoRepository;

	    /**
     * Obtém a situação da cota
     * 
     * @param idCota
     * @return SituacaoCadastro
     */
	private SituacaoCadastro obterSitiacaoCadastroCota(Long idCota){
		Cota cota  = this.cotaRepository.buscarPorId(idCota);
		return cota.getSituacaoCadastro();
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean verificarCobrancasGeradas(List<Long> idsCota){
		
		return this.consolidadoFinanceiroRepository.obterQuantidadeDividasGeradasData(idsCota) > 0;
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean verificarCobrancasGeradasNaDataVencimentoDebito(Date dataVencimentoDebito,Long... idsCota ){
		
		return this.consolidadoFinanceiroRepository.obterQuantidadeDividasGeradasData(dataVencimentoDebito,idsCota) > 0;
	}
	
	    /**
     * Gera cobranças para Cotas específicas
     * 
     * @param cotas
     * @param idUsuario
     * @param enviaEmail
     * @throws GerarCobrancaValidacaoException
     */
	@Override
	@Transactional(noRollbackFor = GerarCobrancaValidacaoException.class)
	public void gerarCobranca(List<Cota> cotas, 
	                          Long idUsuario,
	                          boolean enviaEmail)
	    throws GerarCobrancaValidacaoException {
	    
	    for (Cota cota : cotas){
	
	        try {
	        
	            Set<String> nossoNumeroEnvioEmail = new HashSet<String>();
	        
	            this.gerarCobrancaCota(cota.getId(), 
	            		               idUsuario, 
	            		               nossoNumeroEnvioEmail,
	            		               false);
	        
	            if (enviaEmail){
	        
	                try {
	            
	                    this.enviarDocumentosCobrancaEmail(cota, nossoNumeroEnvioEmail);
	            
	                } catch (ValidacaoException e) {
	  
	                    LOGGER.error(e.getMessage(), e);
	                }
	            }
	        
	        } catch (GerarCobrancaValidacaoException e) {
	
	            throw new ValidacaoException(TipoMensagem.ERROR,"Erro ao gerar Cobranca para a [Cota: "+ cota.getNumeroCota() +"]: "+e.getMessage());
	        }
	    }
	}

	    /**
     * Consolida Financeiro, Gera Divida e Gera Cobrança
     * 
     * @param idCota
     * @param idUsuario
     * @param setNossoNumero
     * @throws GerarCobrancaValidacaoException
     */
	@Override
	@Transactional(noRollbackFor = GerarCobrancaValidacaoException.class)
	public void gerarCobranca(Long idCota, 
			                  Long idUsuario, 
			                  Set<String> setNossoNumero)
		throws GerarCobrancaValidacaoException {
		
		this.gerarCobrancaCota(idCota, 
				               idUsuario, 
				               setNossoNumero,
        		               false);
		
		this.geradorArquivoCobrancaBancoService.prepararGerarArquivoCobrancaCnab();
	}
	
	/**
	 * Consolida Financeiro, Gera Divida e Posterga Divida Gerada para Cotas especificas
	 * 
	 * @param List<Cota>
	 * @param idUsuario
	 * @throws GerarCobrancaValidacaoException
	 */
	@Override
	@Transactional(noRollbackFor = GerarCobrancaValidacaoException.class)
	public void gerarDividaPostergadaCotas(List<Cota> cotas, 
			                               Long idUsuario)
		throws GerarCobrancaValidacaoException {
		
		for (Cota c : cotas){
			
			this.gerarDividaPostergada(c.getId(), 
					                   idUsuario);
		}
	}

	/**
	 * Consolida Financeiro, Gera Divida e Posterga Divida Gerada para Cota especifica
	 * 
	 * @param idCota
	 * @param idUsuario
	 * @throws GerarCobrancaValidacaoException
	 */
	@Override
	@Transactional(noRollbackFor = GerarCobrancaValidacaoException.class)
	public void gerarDividaPostergada(Long idCota, 
			                          Long idUsuario)
		throws GerarCobrancaValidacaoException {
		
		this.gerarCobrancaCota(idCota, 
				               idUsuario, 
				               new HashSet<String>(),
        		               true);
	}
	
    /**
     * Salva informações pendência de envio de documento de cobrança por email
     * 
     * @param nossoNumeroEnvioEmail
     */
    private void salvarBoletoEmailPendenteEnvio(Set<String> nossoNumeroEnvioEmail){
    	
		if (nossoNumeroEnvioEmail!=null && !nossoNumeroEnvioEmail.isEmpty()){

	        List<String> listaNossoNumero = new ArrayList<String>(nossoNumeroEnvioEmail);
			
		    this.boletoEmailService.salvarBoletoEmail(listaNossoNumero);
		}
    }
	
	    /**
     * Gera Cobraça para uma ou todas as cotas com pendências financeiras A
     * divida pode ser postergada caso não hava forma de cobrança compativel com
     * a pendência ou se o parametro postergarDividas == true
     * 
     * @param idCota
     * @param idUsuario
     * @param setNossoNumero
     * @param postergarDividas
     * @throws GerarCobrancaValidacaoException
     */
	private void gerarCobrancaCota(Long idCota, 
			                       Long idUsuario, 
			                       Set<String> setNossoNumero,
			                       boolean postergarDividas) 
			throws GerarCobrancaValidacaoException {

		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		Usuario usuario = this.usuarioRepository.buscarPorId(idUsuario);
		
		Integer numeroDiasNovaCobranca = this.distribuidorRepository.obterNumeroDiasNovaCobranca(); 
		
		// buscar movimentos financeiros da cota, se informada, caso contrario de todas as cotas
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCota(idCota, dataOperacao);
		
		List<String> msgs = new ArrayList<String>();
		
		Map<Cota, List<GerarCobrancaHelper>> consolidadosCotaUnficacao = 
			new HashMap<Cota, List<GerarCobrancaHelper>>();
		
		if (listaMovimentoFinanceiroCota != null && !listaMovimentoFinanceiroCota.isEmpty()){
			
			//Varre todos os movimentos encontrados, agrupando por cota e por fornecedor
			Cota ultimaCota = listaMovimentoFinanceiroCota.get(0).getCota();
			
			Fornecedor ultimoFornecedor = null;
			
			BigDecimal valorMovimentos = BigDecimal.ZERO;
			
			List<MovimentoFinanceiroCota> movimentos = new ArrayList<MovimentoFinanceiroCota>();
			
			Fornecedor fornecedorProdutoMovimento = null;
			
			Cota cotaAtual = null;
			
			FormaCobranca formaCobranca = null;
			FormaCobranca formaCobrancaClone = null;

			boolean unificaCobranca = false;
			
			for (MovimentoFinanceiroCota movimentoFinanceiroCota : listaMovimentoFinanceiroCota){
				
                // verifica se cota esta suspensa, se estiver verifica se existe
                // chamada de encalhe na data de operação
				if (SituacaoCadastro.SUSPENSO.equals(ultimaCota.getSituacaoCadastro()) &&
						!this.cotaUnificacaoRepository.verificarCotaUnificada(ultimaCota.getNumeroCota())){
					
					if (!movimentoFinanceiroCota.getCota().equals(ultimaCota)){
						
						if (this.chamadaEncalheCotaRepository.obterQtdListaChamaEncalheCota(ultimaCota.getNumeroCota(), 
								dataOperacao, null, false, false, false) <= 0){
							
							continue;
						}
					}
				}
				
				cotaAtual = movimentoFinanceiroCota.getCota();
			    
				fornecedorProdutoMovimento = movimentoFinanceiroCota.getFornecedor();

				if (fornecedorProdutoMovimento == null){
			    	
			    	throw new GerarCobrancaValidacaoException(
			    			new ValidacaoVO(TipoMensagem.WARNING, 
                            "Fornecedor não encontrado para o [Movimento Financeiro " +
			    			movimentoFinanceiroCota.getId() + "] [Cota " + cotaAtual.getNumeroCota() + "]."));
			    }
				
				if (movimentoFinanceiroCota.getCota().equals(ultimaCota) &&
				   (ultimoFornecedor == null || fornecedorProdutoMovimento.equals(ultimoFornecedor) || unificaCobranca)){
					
					movimentos.add(movimentoFinanceiroCota);

					TipoMovimentoFinanceiro tipo = 
						(TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento();
					  
					if (tipo.getOperacaoFinaceira().equals(OperacaoFinaceira.CREDITO)){
					    
					    valorMovimentos = valorMovimentos.add(
					    	movimentoFinanceiroCota.getValor().negate());
					    
					} else {
					    
						valorMovimentos = valorMovimentos.add(
							movimentoFinanceiroCota.getValor());
					}
					
				} else {
					
					formaCobranca = 
							formaCobrancaService.obterFormaCobranca(
									ultimaCota != null ? ultimaCota.getId() : null, 
									ultimoFornecedor != null ? ultimoFornecedor.getId() : null, 
									dataOperacao, valorMovimentos);

					formaCobrancaClone = this.cloneFormaCobranca(formaCobranca);
					
					if (formaCobrancaClone != null){
						
						if (formaCobrancaClone.getPoliticaCobranca() != null){
					    	
					    	unificaCobranca = formaCobrancaClone.getPoliticaCobranca().isUnificaCobranca();
					    } else if (formaCobrancaClone.getParametroCobrancaCota() != null){
					    	
					    	unificaCobranca = formaCobrancaClone.getParametroCobrancaCota().isUnificaCobranca();
					    }
					}
					
					//Decide se gera movimento consolidado ou postergado para a cota
					this.montarConsolidadoFinanceiro(ultimaCota, 
													  movimentos,
													  usuario,
													  numeroDiasNovaCobranca,
													  dataOperacao,
													  msgs,
													  ultimoFornecedor,
													  formaCobrancaClone,
													  postergarDividas,
													  consolidadosCotaUnficacao);
					
                    // Limpa dados para contabilizar próxima cota
					ultimaCota = movimentoFinanceiroCota.getCota();
					
					ultimoFornecedor = movimentoFinanceiroCota.getFornecedor();
					
					movimentos = new ArrayList<MovimentoFinanceiroCota>();
					
					movimentos.add(movimentoFinanceiroCota);
					
					valorMovimentos = movimentoFinanceiroCota.getValor();
				}
			}
			
			if (formaCobranca == null){
				
				formaCobranca = 
						formaCobrancaService.obterFormaCobranca(
								ultimaCota != null ? ultimaCota.getId() : null, 
								ultimoFornecedor != null ? ultimoFornecedor.getId() : null, 
								dataOperacao, valorMovimentos);
					
				formaCobrancaClone = this.cloneFormaCobranca(formaCobranca);  
			}
			
			//Decide se gera movimento consolidado ou postergado para a ultima cota
			this.montarConsolidadoFinanceiro(ultimaCota,
											  movimentos,
											  usuario,
											  numeroDiasNovaCobranca,
											  dataOperacao,
											  msgs,
											  fornecedorProdutoMovimento,
											  formaCobrancaClone,
											  postergarDividas,
											  consolidadosCotaUnficacao);
		}
		
		for (Entry<Cota, List<GerarCobrancaHelper>> entry : consolidadosCotaUnficacao.entrySet()){
			
			List<GerarCobrancaHelper> lista = entry.getValue();
			Cota cotaUnificadora =  entry.getKey();
			
			BigDecimal valor = BigDecimal.ZERO;
			GerarCobrancaHelper helperPrincipal = null;
			List<ConsolidadoFinanceiroCota> consolidados = new ArrayList<ConsolidadoFinanceiroCota>();
			
			for (GerarCobrancaHelper helper : lista){
				
				valor = valor.add(helper.getConsolidadoFinanceiroCota().getTotal());
				
				if (helper.getCota().equals(cotaUnificadora)){
					
					helperPrincipal = helper;
				}
				
				consolidados.add(helper.getConsolidadoFinanceiroCota());
			}
			
			String nossoNumero = 
				this.gerarDividaCobranca(
					cotaUnificadora, 
					helperPrincipal.getFormaCobrancaPrincipal(),
					valor, 
					helperPrincipal.isCobrarHoje(),
					consolidados,
					usuario, 
					helperPrincipal.getQtdDiasNovaCobranca(), 
					msgs, 
					helperPrincipal.getFornecedor(), 
					helperPrincipal.getDiasSemanaConcentracaoPagamento(), 
					dataOperacao, 
					helperPrincipal.getDataVencimento(),
					helperPrincipal.getDataConsolidado());
			
			if (helperPrincipal.getFormaCobrancaPrincipal() == null ? false :
				helperPrincipal.getFormaCobrancaPrincipal().isRecebeCobrancaEmail() &&
				setNossoNumero != null){
				
				setNossoNumero.add(nossoNumero);
			}
		}
		
		
        this.salvarBoletoEmailPendenteEnvio(setNossoNumero);
		
		if (!msgs.isEmpty()){
			
			throw new GerarCobrancaValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, msgs));
		}
	}

	    /**
     * Retorna a data de vencimento para o boleto, sendo esta calculada da
     * seguinte forma:
     * 
     * É recuperada a data da Terça-feira dentro da semana utilizada na pesquisa
     * principal do fechamentoCEIntegração. A esta data são adicionados 2 dias
     * úteis.
     * 
     * @param anoSemana
     * 
     * @return Date
     */
	private Date obterDataVencimentoBoletoDistribuidor(int anoSemana) {
		
		int anoBase = SemanaUtil.getAno(anoSemana);
		
		int semana = SemanaUtil.getSemana(anoSemana);
		
		Date dataFechamentoSemana = 
			SemanaUtil.obterDataDaSemanaNoAno(
				semana, DiaSemana.TERCA_FEIRA.getCodigoDiaSemana(), anoBase);
		
		Date dataVencimento = this.calendarioService.adicionarDiasUteis(dataFechamentoSemana, 2);
				
		return dataVencimento;
	}
	
	/**
	 * Retorna o valor total do boleto com desconto.
	 * 
	 * @param idChamadaEncalheFornecedor
	 * @param valorBrutoBoleto
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal obterValorBoleto(Long idChamadaEncalheFornecedor, BigDecimal valorBrutoBoleto) {
		
		BigDecimal valorTotalDesconto = 
				itemChamadaEncalheFornecedorRepository.obterTotalDoDescontoItensChamadaEncalheFornecedor(idChamadaEncalheFornecedor);
		
		if(valorTotalDesconto == null) {
			valorTotalDesconto = BigDecimal.ZERO;
		}
		
		return valorBrutoBoleto.subtract(valorTotalDesconto);
		
	}
	
	@Transactional
	public List<BoletoDistribuidor> gerarCobrancaBoletoDistribuidor(
			List<ChamadaEncalheFornecedor> listaChamadaEncalheFornecedor, 
			TipoCobranca tipoCobranca, int semana){
		
		List<BoletoDistribuidor> listaBoletoDistribuidor = new ArrayList<BoletoDistribuidor>();
		
		Date dataOperacao = this.distribuidorRepository.obterDataOperacaoDistribuidor();
		
		Date dataAtual = new Date();
		
		Integer codigoDistribuidor = this.distribuidorRepository.codigo();
		
		for(ChamadaEncalheFornecedor chamadaEncalheFornecedor : listaChamadaEncalheFornecedor) {
			
			BoletoDistribuidor boletoDistribuidor = 
					boletoDistribuidorRepository.obterBoletoDistribuidorPorChamadaEncalheFornecedor(chamadaEncalheFornecedor.getId());
			
			if(boletoDistribuidor != null) {
				boletoDistribuidorRepository.remover(boletoDistribuidor);
			}
			
			chamadaEncalheFornecedor.setFornecedor( chamadaEncalheFornecedor.getItens().get(0).getProdutoEdicao().getProduto().getFornecedor() );
			
			Fornecedor fornecedor = chamadaEncalheFornecedor.getFornecedor();
			
			Banco banco = chamadaEncalheFornecedor.getFornecedor().getBanco();
			
			if (banco == null) {
				
                throw new ValidacaoException(TipoMensagem.ERROR, "Fornecedor " + fornecedor.getJuridica().getNome()
                        + " não possui banco vinculado!");
			}
			
			String nossoNumeroDistribuidor = Util.gerarNossoNumeroDistribuidor(
					codigoDistribuidor, 
					dataOperacao, 
					banco.getNumeroBanco(), 
					fornecedor.getId(), 
					chamadaEncalheFornecedor.getId());
			
			boletoDistribuidor = new BoletoDistribuidor();
			
			boletoDistribuidor.setBanco(banco);
			boletoDistribuidor.setChamadaEncalheFornecedor(chamadaEncalheFornecedor);
			
			boletoDistribuidor.setDataEmissao(dataAtual);
			
			boletoDistribuidor.setDataVencimento(obterDataVencimentoBoletoDistribuidor(semana));
			
			boletoDistribuidor.setNossoNumeroDistribuidor(nossoNumeroDistribuidor);
			boletoDistribuidor.setStatus(null);
			
			boletoDistribuidor.setFornecedor(chamadaEncalheFornecedor.getFornecedor());
			
			boletoDistribuidor.setTipoCobranca(tipoCobranca);
			
			if(TipoCobranca.BOLETO.equals(tipoCobranca)) {
				
				BigDecimal valorLiquidoBoleto = obterValorBoleto(chamadaEncalheFornecedor.getId(), chamadaEncalheFornecedor.getTotalVendaApurada());
				
				boletoDistribuidor.setValor(valorLiquidoBoleto);
			}
			
			boletoDistribuidor.setVias(1);
			
			boletoDistribuidorRepository.adicionar(boletoDistribuidor);
			
			listaBoletoDistribuidor.add(boletoDistribuidor);
		}
		
		return listaBoletoDistribuidor;
	}
	
	private BigDecimal obterValorMinino(Cota cota, BigDecimal valorMininoDistribuidor){
		BigDecimal valorMinimo = 
				(cota.getParametroCobranca() != null && cota.getParametroCobranca().getValorMininoCobranca() != null) ?
						cota.getParametroCobranca().getValorMininoCobranca() :
							valorMininoDistribuidor;
						
		return valorMinimo;
	}
	
	    /**
     * Consolida os movimentos financeiros Gera Divida e Cobrança Posterga as
     * pendências financeiras caso os parametros postergarDividas==true ou
     * formaCobrancaPrincipal==null
     * 
     * @param cota
     * @param movimentos
     * @param usuario
     * @param qtdDiasNovaCobranca
     * @param dataOperacao
     * @param msgs
     * @param fornecedor
     * @param formaCobrancaPrincipal
     */
	private void montarConsolidadoFinanceiro(Cota cota, List<MovimentoFinanceiroCota> movimentos,
			                                    Usuario usuario, 
			                                    int qtdDiasNovaCobranca, 
			                                    Date dataOperacao, 
			                                    List<String> msgs,
			                                    Fornecedor fornecedor,
			                                    FormaCobranca formaCobrancaPrincipal,
			                                    boolean postergarDividas,
			                                    Map<Cota, List<GerarCobrancaHelper>> consolidadosCotaUnficacao){
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = new ConsolidadoFinanceiroCota();
		consolidadoFinanceiroCota.setCota(cota);
		consolidadoFinanceiroCota.setDataConsolidado(dataOperacao);
		consolidadoFinanceiroCota.setMovimentos(movimentos);
		
		BigDecimal vlMovFinanDebitoCredito = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncalhe = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncargos = BigDecimal.ZERO;
		BigDecimal vlMovFinanVendaEncalhe = BigDecimal.ZERO;
		BigDecimal vlMovPostergado = BigDecimal.ZERO;
		BigDecimal vlMovConsignado = BigDecimal.ZERO;
		BigDecimal vlMovPendente = BigDecimal.ZERO;

		for (MovimentoFinanceiroCota movimentoFinanceiroCota : movimentos){
			
			if (!movimentoFinanceiroCota.getCota().getId().equals(cota.getId())) {
				continue;
			}
			
			GrupoMovimentoFinaceiro tipoMovimentoFinanceiro =
				((TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento())
					.getGrupoMovimentoFinaceiro();

			switch (tipoMovimentoFinanceiro) {
			
				case CREDITO:
				case COMPRA_NUMEROS_ATRAZADOS:
				case DEBITO:
				case DEBITO_SOBRE_FATURAMENTO:
				case POSTERGADO_NEGOCIACAO:
				case CREDITO_SOBRE_FATURAMENTO:
				case LANCAMENTO_CAUCAO_LIQUIDA:
				case RESGATE_CAUCAO_LIQUIDA:
				case VENDA_TOTAL:
				case NEGOCIACAO_COMISSAO:
					
					vlMovFinanDebitoCredito = 
						this.adicionarValor(vlMovFinanDebitoCredito, movimentoFinanceiroCota);
					
					break;
					
				case COMPRA_ENCALHE_SUPLEMENTAR:
					
					vlMovFinanVendaEncalhe = 
						this.adicionarValor(vlMovFinanVendaEncalhe, movimentoFinanceiroCota);
					
					break;
					
				case RECEBIMENTO_REPARTE:
					
					vlMovConsignado = 
						this.adicionarValor(vlMovConsignado, movimentoFinanceiroCota);
					
					break;
					
				case JUROS:
				case MULTA:
					
					vlMovFinanEncargos = 
						this.adicionarValor(vlMovFinanEncargos, movimentoFinanceiroCota);
					
					break;
					
				case ENVIO_ENCALHE:

					vlMovFinanEncalhe = 
						this.adicionarValor(vlMovFinanEncalhe, movimentoFinanceiroCota);
					
					break;
					
				case POSTERGADO_DEBITO:
				case POSTERGADO_CREDITO:
					
					vlMovPostergado = 
							this.adicionarValor(vlMovPostergado, movimentoFinanceiroCota);
					
					break;
					
				case PENDENTE:
					vlMovPendente =
						this.adicionarValor(vlMovPendente, movimentoFinanceiroCota);
					
					break;
			}
		}
		
		BigDecimal vlMovFinanTotal = BigDecimal.ZERO
				.add(vlMovFinanEncalhe)
				.add(vlMovConsignado)
				.add(vlMovPostergado)
				.add(vlMovFinanVendaEncalhe)
				.add(vlMovFinanDebitoCredito)
				.add(vlMovFinanEncargos)
				.add(vlMovPendente);
		
		consolidadoFinanceiroCota.setTotal(vlMovFinanTotal);
		consolidadoFinanceiroCota.setDebitoCredito(vlMovFinanDebitoCredito);
		consolidadoFinanceiroCota.setEncalhe(vlMovFinanEncalhe);
		consolidadoFinanceiroCota.setEncargos(vlMovFinanEncargos);
		consolidadoFinanceiroCota.setVendaEncalhe(vlMovFinanVendaEncalhe.abs());
		consolidadoFinanceiroCota.setValorPostergado(vlMovPostergado);
		consolidadoFinanceiroCota.setConsignado(vlMovConsignado.abs());
		consolidadoFinanceiroCota.setPendente(vlMovPendente);
		
        // insere postergado pois não encontrou forma de cobrança ou parametros
        // do método exigem postergação
		if (formaCobrancaPrincipal == null || postergarDividas==true){
			
			MovimentoFinanceiroCota movPost = this.gerarPostergado(cota, 
					                                               qtdDiasNovaCobranca, 
					                                               msgs, 
					                                               fornecedor, 
							                                       Arrays.asList(consolidadoFinanceiroCota), 
							                                       vlMovFinanTotal, 
							                                       vlMovPostergado, 
							                                       usuario, 
							                                       null, 
							                                       dataOperacao,
							                                       postergarDividas?"Processamento Financeiro - Divida postergada":null);
			
			this.consolidadoFinanceiroRepository.adicionar(consolidadoFinanceiroCota);
			
			if (movPost != null){
				
				this.movimentoFinanceiroCotaRepository.adicionar(movPost);
			}
			
			return;
		}
		
		List<Integer> diasSemanaConcentracaoPagamento = null;
		
		//obtem a data de vencimento de acordo com o dia em que se concentram os pagamentos da cota
		Integer fatorVencimento = 0;
		
		ParametroCobrancaCota parametroCobrancaCota = cota.getParametroCobranca();
		
		TipoFormaCobranca tipoFormaCobrancaAntiga = null;
		
		if(parametroCobrancaCota!=null && parametroCobrancaCota.getFatorVencimento()!=null) {
			fatorVencimento = parametroCobrancaCota.getFatorVencimento();
		}
		else {
			
			if(formaCobrancaPrincipal.getPoliticaCobranca() != null && 
					formaCobrancaPrincipal.getPoliticaCobranca().getFatorVencimento() != null){
				
				fatorVencimento = formaCobrancaPrincipal.getPoliticaCobranca().getFatorVencimento();
			}
			
			tipoFormaCobrancaAntiga = formaCobrancaPrincipal.getTipoFormaCobranca();
			formaCobrancaPrincipal.setTipoFormaCobranca(null);
		}
		
		boolean cobrarHoje = false;
		
		Calendar c = null;
		
		Date dataVencimento = this.obterDataVencimentoCobrancaCota(consolidadoFinanceiroCota.getDataConsolidado(),fatorVencimento);
		
		//acertar a data de vencimento de a cota usa parametros de cobranca do distribuidor
		if(formaCobrancaPrincipal.getTipoFormaCobranca() == null) {
			cobrarHoje = true;
		} else {
            // switch usado para se é usado os parametros de cobranca da propria
            // cota
			switch(formaCobrancaPrincipal.getTipoFormaCobranca()){
			
				case DIARIA:
					cobrarHoje = true;
					break;
				
				case QUINZENAL:
					c = Calendar.getInstance();
					c.setTime(dataOperacao);
					cobrarHoje = formaCobrancaPrincipal.getDiasDoMes().contains(c.get(Calendar.DAY_OF_MONTH));
					break;
				
				case MENSAL:
					c = Calendar.getInstance();
					c.setTime(dataOperacao);
					cobrarHoje = formaCobrancaPrincipal.getDiasDoMes().get(0).equals(c.get(Calendar.DAY_OF_MONTH));
					break;
				
				case SEMANAL:
					c = Calendar.getInstance();
					c.setTime(dataOperacao);
					for (ConcentracaoCobrancaCota conc : formaCobrancaPrincipal.getConcentracaoCobrancaCota()){
						cobrarHoje = c.get(Calendar.DAY_OF_WEEK) == conc.getDiaSemana().getCodigoDiaSemana();
						if (cobrarHoje){
							break;
						}
					}
					break;
					
			}
			
		}
		
		/*
		 * Recoloca o TipoFormaCobranca no Local Antigo
		 */
		if(parametroCobrancaCota !=null && parametroCobrancaCota.getFatorVencimento() == null) {
			formaCobrancaPrincipal.setTipoFormaCobranca(tipoFormaCobrancaAntiga);
		}
		
		if (dataVencimento == null){
			
            msgs.add("Não foi possível calcular data de vencimento da cobrança, verifique os parâmetros de cobrança da cota número: "
                    + cota.getNumeroCota());
			
			return;
		}
		
		Cota cotaUnificadora = this.cotaUnificacaoRepository.obterCotaUnificadoraPorCota(cota.getNumeroCota());
		
		if (cotaUnificadora == null || !consolidadosCotaUnficacao.containsKey(cotaUnificadora)){
			
			cotaUnificadora = cotaUnificadora == null ? cota : cotaUnificadora;
			
			consolidadosCotaUnficacao.put(
				cotaUnificadora, 
				new ArrayList<GerarCobrancaHelper>());
		}
		
		consolidadosCotaUnficacao.get(cotaUnificadora).add(
			new GerarCobrancaHelper(
				cota,
				formaCobrancaPrincipal, 
				cobrarHoje, 
				consolidadoFinanceiroCota, 
				qtdDiasNovaCobranca, 
				fornecedor, 
				diasSemanaConcentracaoPagamento, 
				dataVencimento,
				consolidadoFinanceiroCota.getDataConsolidado()));
	}
	
	private String gerarDividaCobranca(Cota cota, FormaCobranca formaCobrancaPrincipal,
			   BigDecimal vlMovFinanTotal, boolean cobrarHoje, List<ConsolidadoFinanceiroCota> consolidadoFinanceiroCota,
			   Usuario usuario, int qtdDiasNovaCobranca, List<String> msgs, Fornecedor fornecedor,
			   List<Integer> diasSemanaConcentracaoPagamento, Date dataOperacao, Date dataVencimento,
			   Date dataConsolidado) {
			
		   Divida novaDivida = null;
			
			MovimentoFinanceiroCota movimentoFinanceiroCota = null;
			
			boolean cotaSuspensa = SituacaoCadastro.SUSPENSO.equals(this.obterSitiacaoCadastroCota(cota.getId()));

			BigDecimal valorMinino = 
					this.obterValorMinino(cota, formaCobrancaPrincipal.getValorMinimoEmissao());
			
			Banco banco = formaCobrancaPrincipal.getBanco();
			
        // caso tenha alcançado o valor minino de cobrança e seja um dia de
        // concentração de cobrança, ou a cota esteja suspensa
			if ( (vlMovFinanTotal.compareTo(BigDecimal.ZERO) < 0) &&
					(vlMovFinanTotal.abs().compareTo(valorMinino) > 0 && cobrarHoje) || 
					(vlMovFinanTotal.abs().compareTo(valorMinino) > 0 && cotaSuspensa)){
				
				novaDivida = new Divida();
				novaDivida.setValor(vlMovFinanTotal.abs());
				novaDivida.setData(dataConsolidado);
				novaDivida.setConsolidados(consolidadoFinanceiroCota);
				novaDivida.setCota(cota);
				novaDivida.setStatus(StatusDivida.EM_ABERTO);
				novaDivida.setResponsavel(usuario);
				novaDivida.setOrigemNegociacao(false);
				
			} else if (vlMovFinanTotal.compareTo(valorMinino) != 0) {

				movimentoFinanceiroCota = this.gerarPostergado(cota,
						                                       qtdDiasNovaCobranca, 
						                                       msgs, 
						                                       fornecedor,
						                                       consolidadoFinanceiroCota, 
						                                       vlMovFinanTotal, 
						                                       vlMovFinanTotal, 
						                                       usuario,
						                                       diasSemanaConcentracaoPagamento, 
						                                       dataOperacao,
						                                       null);
			}
			
			for (ConsolidadoFinanceiroCota consolidado : consolidadoFinanceiroCota){
			
				this.consolidadoFinanceiroRepository.adicionar(consolidado);
			}
			
			Cobranca cobranca = null;
			
			if (novaDivida != null){
				if (novaDivida.getId() == null){
					this.dividaRepository.adicionar(novaDivida);
				} else {
					this.dividaRepository.alterar(novaDivida);
				}
				
				switch (formaCobrancaPrincipal.getTipoCobranca()){
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
					break;
					case TRANSFERENCIA_BANCARIA:
						cobranca = new CobrancaTransferenciaBancaria();
					break;
					case BOLETO_EM_BRANCO:
						cobranca = new CobrancaBoletoEmBranco();
					break;
					case OUTROS:
						cobranca = new CobrancaOutros();
					break;
				}
				
				cobranca.setBanco(banco);
				cobranca.setCota(cota);
				cobranca.setDataEmissao(dataOperacao);
				cobranca.setDivida(novaDivida);
				cobranca.setStatusCobranca(StatusCobranca.NAO_PAGO);
				cobranca.setDataVencimento(dataVencimento);
				cobranca.setVias(0);
				cobranca.setEnviarPorEmail(formaCobrancaPrincipal.isRecebeCobrancaEmail());
				
				String nossoNumero =
					Util.gerarNossoNumero(
						cota.getNumeroCota(), 
						cobranca.getDataEmissao(), 
						banco!=null?banco.getNumeroBanco():"0",
						fornecedor != null ? fornecedor.getId() : null,
						novaDivida.getId(),
						banco!=null?banco.getAgencia():0,
						banco!=null?banco.getConta():0,
						banco!=null?banco.getCarteira():0);
				
				cobranca.setFornecedor(fornecedor);
				cobranca.setNossoNumero(nossoNumero);
				
				String digitoVerificador =
					Util.calcularDigitoVerificador(
						nossoNumero, banco!=null?banco.getCodigoCedente():"0", cobranca.getDataVencimento());
				
				cobranca.setDigitoNossoNumero(digitoVerificador);
				
				cobranca.setNossoNumeroCompleto(
					nossoNumero + ((digitoVerificador != null) ? digitoVerificador : ""));
				
				cobranca.setValor(novaDivida.getValor());
				
				this.cobrancaRepository.adicionar(cobranca);
			}
			
			if (movimentoFinanceiroCota != null){
				
				this.movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiroCota);
			}
			
			if (cobranca != null){
				
				return cobranca.getNossoNumero();
			}
			
			return null;
		}
	
   /**
    * Obtem Data de Vencimento onforme Parametros 
    * @param dataConsolidado
    * @param fatorVencimento
    * @return Date
    */
	@Override
	@Transactional
	public Date obterDataVencimentoCobrancaCota(Date dataConsolidado, Integer fatorVencimento) {
		
		FormaCobranca formaCobranca = formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
		
		if(formaCobranca == null){
			return DateUtil.adicionarDias(dataConsolidado, fatorVencimento);
		}
		
        // verifica se a forma de cobrança principal do distribuidor utiliza
        // dias uteis para geração da data de vencimento da cobrança
		if(formaCobranca.isVencimentoDiaUtil()) {
			return this.calendarioService.adicionarDiasUteis(dataConsolidado, fatorVencimento);
		}
		
		return DateUtil.adicionarDias(dataConsolidado, fatorVencimento);
	}

	    /**
     * Posterga as pendências financeiras da cota
     * 
     * @param cota
     * @param qtdDiasNovaCobranca
     * @param msgs
     * @param fornecedor
     * @param consolidadoFinanceiroCota
     * @param vlMovFinanTotal
     * @param vlMovFinanPostergado
     * @param usuario
     * @param diasSemanaConcentracaoPagamento
     * @param dataOperacao
     * @param descPostergado
     * @return MovimentoFinanceiroCota
     */
	private MovimentoFinanceiroCota gerarPostergado(Cota cota,
			                                        int qtdDiasNovaCobranca, 
			                                        List<String> msgs, 
			                                        Fornecedor fornecedor,
			                                        List<ConsolidadoFinanceiroCota> consolidadoFinanceiroCota,
			                                        BigDecimal vlMovFinanTotal, 
			                                        BigDecimal vlMovFinanPostergado, 
			                                        Usuario usuario,
			                                        List<Integer> diasSemanaConcentracaoPagamento, 
			                                        Date dataOperacao,
			                                        String descPostergado) {
		
		for (ConsolidadoFinanceiroCota consolidado : consolidadoFinanceiroCota){
		
			//gerar postergado
			consolidado.setValorPostergado(vlMovFinanPostergado);
		}
		
		Calendar diaPostergado = Calendar.getInstance();
		diaPostergado.setTime(dataOperacao);
		diaPostergado.add(Calendar.DAY_OF_MONTH, qtdDiasNovaCobranca);
	
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = null;
		if (vlMovFinanTotal.compareTo(BigDecimal.ZERO) > 0){
			
			tipoMovimentoFinanceiro = 
					this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
							GrupoMovimentoFinaceiro.POSTERGADO_CREDITO);
		} else {
			
			tipoMovimentoFinanceiro = 
					this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
							GrupoMovimentoFinaceiro.POSTERGADO_DEBITO);
		}
		
		if (tipoMovimentoFinanceiro == null) {
			
            msgs.add("Tipo de movimento para postergação não encontrado!");
			
			return null;
		}
		
		if (descPostergado == null){
		
			if (diasSemanaConcentracaoPagamento == null){
				
                descPostergado = "Forma de cobrança não encontrada para a cota: " + cota.getNumeroCota()
                        + ", a cobrança será postergada.";
			} else if (!diasSemanaConcentracaoPagamento.contains(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))){
				
                descPostergado = "Não existe acúmulo de pagamento para este dia ("
                        +
						new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()) + ")";
			} else {
				
                descPostergado = "Valor mínimo para dívida não atingido";
			}
		}
		
		//gera movimento financeiro cota
		MovimentoFinanceiroCota movimentoFinanceiroCota = null;
		
		if (vlMovFinanTotal != null && vlMovFinanTotal.compareTo(BigDecimal.ZERO) != 0){
			
			Date data = this.calendarioService.obterProximaDataDiaUtil(diaPostergado.getTime());
			
			movimentoFinanceiroCota = new MovimentoFinanceiroCota();
			movimentoFinanceiroCota.setData(data);
			movimentoFinanceiroCota.setDataCriacao(dataOperacao);
			movimentoFinanceiroCota.setUsuario(usuario);
			movimentoFinanceiroCota.setValor(vlMovFinanTotal.abs());
			movimentoFinanceiroCota.setLancamentoManual(false);
			movimentoFinanceiroCota.setCota(cota);
			movimentoFinanceiroCota.setStatus(StatusAprovacao.APROVADO);
			movimentoFinanceiroCota.setMotivo(descPostergado);
			movimentoFinanceiroCota.setTipoMovimento(tipoMovimentoFinanceiro);
			movimentoFinanceiroCota.setFornecedor(fornecedor!=null?fornecedor:cota.getParametroCobranca().getFornecedorPadrao());
		}
		
		return movimentoFinanceiroCota;
	}

	@Transactional
	@Override
	public void cancelarDividaCobranca(Set<Long> idsMovimentoFinanceiroCota, boolean excluiFinanceiro) {
		
		Date dataOperacao = this.distribuidorRepository.obterDataOperacaoDistribuidor();
		
		if (idsMovimentoFinanceiroCota != null && !idsMovimentoFinanceiroCota.isEmpty()){
			
			for (Long idMovFinCota : idsMovimentoFinanceiroCota){
				
				this.cancelarDividaCobranca(idMovFinCota, null, dataOperacao, excluiFinanceiro);
			}
		}
	}
	
	/**
	 * Remove pendencia de envio de boletos por email da Cobranca
	 * @param cobrancaId
	 */
	private void excluirBoletoEmailAssociacao(Long cobrancaId){
		
		BoletoEmail be = this.boletoEmailRepository.obterBoletoEmailPorCobranca(cobrancaId);
		
		if (be != null){
		
		    this.boletoEmailRepository.remover(be);
		}
	}
	
	@Transactional
	@Override
	public void cancelarDividaCobranca(Long idMovimentoFinanceiroCota, Long idCota, Date dataOperacao, boolean excluiFinanceiro) {
		
		List<ConsolidadoFinanceiroCota> consolidados = null;
		
		if (idMovimentoFinanceiroCota != null){

			consolidados = this.consolidadoFinanceiroRepository.obterConsolidadoPorIdMovimentoFinanceiro(idMovimentoFinanceiroCota);
		} else {
			
			consolidados = this.consolidadoFinanceiroRepository.obterConsolidadosDataOperacao(idCota);
		}
		
		if (consolidados != null) {
			
			for (ConsolidadoFinanceiroCota consolidado : consolidados) {
				
				Divida divida = this.dividaRepository.obterDividaPorIdConsolidado(consolidado.getId());
				
				if (divida != null) {
					
					this.cobrancaControleConferenciaEncalheCotaRepository.excluirPorCobranca(divida.getCobranca().getId());
					
					Negociacao negociacao = this.negociacaoRepository.obterNegociacaoPorCobranca(divida.getCobranca().getId());
					
					if (negociacao != null) {
					    
						if (!negociacao.isNegociacaoAvulsa()) {
						
						    this.parcelaNegociacaoRepository.excluirPorNegociacao(negociacao.getId());
						
						    this.negociacaoRepository.remover(negociacao);
						    
						    this.removerDividaCobrancaConsolidado(divida,consolidado, dataOperacao);
						}
					
					} else {
					
						this.removerDividaCobrancaConsolidado(divida,consolidado, dataOperacao);
					}
				}

				List<MovimentoFinanceiroCota> mfcs = consolidado.getMovimentos();
				
				consolidado.setMovimentos(null);
				
				this.consolidadoFinanceiroRepository.alterar(consolidado);
				
                if (excluiFinanceiro && mfcs!=null && !mfcs.isEmpty()){
					
			    	this.movimentoFinanceiroCotaService.removerMovimentosFinanceirosCota(mfcs);
				}
                
			    this.consolidadoFinanceiroRepository.remover(consolidado);
			}
		}
		
		this.removerPostergados(idCota, dataOperacao);
	}
	
	private void removerDividaCobrancaConsolidado(Divida divida, ConsolidadoFinanceiroCota consolidado,
			Date dataOperacao){
		
		Cobranca cobranca = divida.getCobranca();
		
		if (cobranca != null){
		    
			this.excluirBoletoEmailAssociacao(cobranca.getId());
			
			this.cobrancaRepository.remover(cobranca);
		}
		
	    this.dividaRepository.remover(divida);
	}
	
	private void removerPostergados(Long idCota, Date dataOperacao){
		
		List<TipoMovimentoFinanceiro> listaPostergados = Arrays.asList(
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.POSTERGADO_CREDITO),
						
				this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
						GrupoMovimentoFinaceiro.POSTERGADO_DEBITO)
			);
			
			this.movimentoFinanceiroCotaService.removerPostergadosDia(
					idCota,
					listaPostergados,
					dataOperacao);
	}
	
	private BigDecimal adicionarValor(BigDecimal valor, 
									  MovimentoFinanceiroCota movimentoFinanceiroCota) {
		
		if (movimentoFinanceiroCota.getValor() == null) {
			
			return BigDecimal.ZERO;
		}
		
		GrupoMovimentoFinaceiro grupoMovimentoFinaceiro =
			((TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento())
				.getGrupoMovimentoFinaceiro();
		
		if (OperacaoFinaceira.CREDITO.equals(grupoMovimentoFinaceiro.getOperacaoFinaceira())) {

			return MathUtil.defaultRound(
					valor.add(movimentoFinanceiroCota.getValor()));
		}

		return MathUtil.defaultRound(
			valor.add(movimentoFinanceiroCota.getValor().negate()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean aceitaEnvioEmail(Cota cota, String nossoNumero) {

		boolean distribuidorUtilizaSlip = this.parametrosDistribuidorEmissaoDocumentoRepository
				.isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento.SLIP);

		boolean distribuidorUtilizaBoleto = this.parametrosDistribuidorEmissaoDocumentoRepository
				.isUtilizaImpressao(TipoParametrosDistribuidorEmissaoDocumento.BOLETO);

		boolean distribuidorUtilizaRecibo = false;

		if (!distribuidorUtilizaBoleto) {

			distribuidorUtilizaRecibo = 
					
					this.parametrosDistribuidorEmissaoDocumentoRepository.isUtilizaImpressao(
							TipoParametrosDistribuidorEmissaoDocumento.RECIBO);
		}
		
		if (((!distribuidorUtilizaBoleto || !distribuidorUtilizaRecibo) && !distribuidorUtilizaSlip)) {
			
			return false;
		}
		
		Cobranca cobranca = this.cobrancaRepository.obterCobrancaPorNossoNumero(nossoNumero);
		
		if (!cobranca.isEnviarPorEmail()) {

			return false;
		}

		ParametroDistribuicaoCota parametroDistribuicaoCota = cota.getParametroDistribuicao();
		
		if (parametroDistribuicaoCota == null ||
				(parametroDistribuicaoCota.getSlipImpresso() == null 			|| parametroDistribuicaoCota.getSlipImpresso() == false) &&
				(parametroDistribuicaoCota.getSlipEmail() == null 				|| parametroDistribuicaoCota.getSlipEmail() == false) &&
				(parametroDistribuicaoCota.getBoletoImpresso() == null 			|| parametroDistribuicaoCota.getBoletoImpresso() == false) &&
				(parametroDistribuicaoCota.getBoletoEmail() == null 			|| parametroDistribuicaoCota.getBoletoEmail() == false) &&
				(parametroDistribuicaoCota.getBoletoSlipImpresso() == null 		|| parametroDistribuicaoCota.getBoletoSlipImpresso() == false) &&
				(parametroDistribuicaoCota.getBoletoSlipEmail() == null 		|| parametroDistribuicaoCota.getBoletoSlipEmail() == false) &&
				(parametroDistribuicaoCota.getReciboImpresso() == null 			|| parametroDistribuicaoCota.getReciboImpresso() == false) &&
				(parametroDistribuicaoCota.getReciboEmail() == null 			|| parametroDistribuicaoCota.getReciboEmail() == false) &&
				(parametroDistribuicaoCota.getChamadaEncalheImpresso() == null 	|| parametroDistribuicaoCota.getChamadaEncalheImpresso() == false) &&
				(parametroDistribuicaoCota.getChamadaEncalheEmail() == null 	|| parametroDistribuicaoCota.getChamadaEncalheEmail() == false) &&
				(parametroDistribuicaoCota.getNotaEnvioImpresso() == null 		|| parametroDistribuicaoCota.getNotaEnvioImpresso() == false) &&
				(parametroDistribuicaoCota.getNotaEnvioEmail() == null 			|| parametroDistribuicaoCota.getNotaEnvioEmail() == false)) {

			return true;
		}

		boolean cotaUtilizaSlip = parametroDistribuicaoCota.getSlipEmail();

		boolean cotaUtilizaBoleto = parametroDistribuicaoCota.getBoletoEmail();

		boolean cotaUtilizaRecibo = parametroDistribuicaoCota.getReciboEmail();

		if (((!cotaUtilizaBoleto || !cotaUtilizaRecibo) && !cotaUtilizaSlip)) {
			
			return false;
		}
		
		return true;
	}
	
	@Override
	@Transactional(readOnly=true)
	public boolean aceitaEmissaoDocumento(Cota cota, TipoEmissaoDocumento tipoEmissaoDocumento) {
		
		Boolean aceitaEmissao = this.getMapaEmissaoDocumentos(cota).get(tipoEmissaoDocumento); 

		return aceitaEmissao == null ? false : aceitaEmissao;
	}
	
	private HashMap<TipoEmissaoDocumento, Boolean> getMapaEmissaoDocumentos(Cota cota) {		
		
		HashMap<TipoEmissaoDocumento, Boolean> map = new HashMap<TipoEmissaoDocumento, Boolean>();

		ParametroDistribuicaoCota parametroDistribuicaoCota = cota.getParametroDistribuicao();
		
		if (parametroDistribuicaoCota == null) {
			
			parametroDistribuicaoCota = new ParametroDistribuicaoCota();
		}

		map.put(TipoEmissaoDocumento.EMAIL_BOLETO_RECIBO, parametroDistribuicaoCota.getBoletoEmail());
		map.put(TipoEmissaoDocumento.EMAIL_BOLETO_SLIP, parametroDistribuicaoCota.getBoletoSlipEmail());
		map.put(TipoEmissaoDocumento.EMAIL_CHAMADA_ENCALHE, parametroDistribuicaoCota.getChamadaEncalheEmail());
		map.put(TipoEmissaoDocumento.EMAIL_NOTA_ENVIO, parametroDistribuicaoCota.getNotaEnvioEmail());
		map.put(TipoEmissaoDocumento.EMAIL_SLIP, parametroDistribuicaoCota.getSlipEmail());
		
		map.put(TipoEmissaoDocumento.IMPRESSAO_BOLETO_RECIBO, parametroDistribuicaoCota.getBoletoImpresso());
		map.put(TipoEmissaoDocumento.IMPRESSAO_BOLETO_SLIP, parametroDistribuicaoCota.getBoletoSlipImpresso());
		map.put(TipoEmissaoDocumento.IMPRESSAO_CHAMADA_ENCALHE, parametroDistribuicaoCota.getChamadaEncalheImpresso());
		map.put(TipoEmissaoDocumento.IMPRESSAO_NOTA_ENVIO, parametroDistribuicaoCota.getNotaEnvioImpresso());
		map.put(TipoEmissaoDocumento.IMPRESSAO_SLIP, parametroDistribuicaoCota.getSlipImpresso());

		for (Entry<TipoEmissaoDocumento, Boolean> entry : map.entrySet()) {
			
			Boolean value = entry.getValue();
			
			if (value == null) {
				
				entry.setValue(this.parametrosDistribuidorEmissaoDocumentoRepository.isUtilizaEnvioEmail(entry.getKey().getParametroEmissao()));
			}
		}

		return map;
	}

	private void enviarDocumentosCobrancaEmail(String nossoNumero, String email) throws AutenticacaoEmailException {
		
		byte[] anexo = this.documentoCobrancaService.gerarDocumentoCobranca(nossoNumero);
		
        this.emailService.enviar("Cobrança", "Segue documento de cobrança em anexo.",
								 new String[]{email}, 
								 new AnexoEmail("Cobranca",anexo,TipoAnexo.PDF));		
	}
	
	    /**
     * Envia Cobranças para email da Cota
     * 
     * @param cota
     * @param nossoNumeroEnvioEmail
     */
	@Override
	@Transactional
	public void enviarDocumentosCobrancaEmail(Cota cota, Set<String> nossoNumeroEnvioEmail) {
		
        String email = cota.getPessoa().getEmail();
		
		if (email == null || email.trim().isEmpty()){

			return;
		}
		
		Iterator<String> iterator = nossoNumeroEnvioEmail.iterator(); 
		
        while(iterator.hasNext()){
			
        	String nossoNumero = iterator.next();
        	
			if (this.aceitaEnvioEmail(cota, nossoNumero)) {
				
				try {
		            
					this.enviarDocumentosCobrancaEmail(nossoNumero, email);
            
                } catch (AutenticacaoEmailException e) {
  
                    LOGGER.error(e.getMessage(), e);
                }
			}
		}	
	}
	
    private FormaCobranca cloneFormaCobranca(FormaCobranca formaCobranca) {
		
		if (formaCobranca==null){
			
			return null;
		}
		
		try {
			
			return (FormaCobranca) BeanUtils.cloneBean(formaCobranca);
			
		} catch (Exception e) {

			throw new ValidacaoException(
					TipoMensagem.ERROR,
					"Erro ao tentar obter [FormaCobranca]!");
		}
	}
}
