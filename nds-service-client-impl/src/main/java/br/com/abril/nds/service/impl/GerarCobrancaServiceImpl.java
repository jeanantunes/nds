package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.baixaboleto.TipoEmissaoDocumento;
import br.com.abril.nds.dto.CotaCentralizadoraDTO;
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
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoCota;
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
import br.com.abril.nds.model.financeiro.TipoNegociacao;
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
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.NegociacaoDividaRepository;
import br.com.abril.nds.repository.ParametrosDistribuidorEmissaoDocumentoRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.BoletoEmailService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.FormaCobrancaService;
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
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GerarCobrancaServiceImpl.class);

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
	private CobrancaControleConferenciaEncalheCotaRepository cobrancaControleConferenciaEncalheCotaRepository;
	
	@Autowired
	private BoletoDistribuidorRepository boletoDistribuidorRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
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
	private ParametrosDistribuidorEmissaoDocumentoRepository parametrosDistribuidorEmissaoDocumentoRepository;
	
	@Autowired
	private CotaUnificacaoRepository cotaUnificacaoRepository;
	
	@Autowired
	private BancoService bancoService;	
	
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
	            		               new HashSet<String>(),
	            		               false,
	            		               null);
	        
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
	 * @param setNossoNumeroEnvioEmail
	 * @param setNossoNumeroCentralizacao
	 * @throws GerarCobrancaValidacaoException
	 */
	@Override
	@Transactional(noRollbackFor = GerarCobrancaValidacaoException.class, timeout = 500)
	public void gerarCobranca(Long idCota, 
			                  Long idUsuario, 
			                  Set<String> setNossoNumero,
			                  Set<String> setNossoNumeroCentralizacao)
		throws GerarCobrancaValidacaoException {
		
		this.gerarCobrancaCota(idCota, 
				               idUsuario, 
				               setNossoNumero,
				               setNossoNumeroCentralizacao,
        		               false,
        		               null);
		
	}
	
	/**
	 * Consolida Financeiro, Gera Divida e Gera Cobrança para cotas de Tipos Específicos (A_VISTA/CONSIGNADO)
	 * 
	 * @param idCota
	 * @param idUsuario
	 * @param setNossoNumeroEnvioEmail
	 * @param setNossoNumeroCentralizacao
	 * @param tiposCota
	 * @throws GerarCobrancaValidacaoException
	 */
	@Override
	@Transactional(noRollbackFor = GerarCobrancaValidacaoException.class, timeout = 500)
	public void gerarCobranca(Long idCota, 
			                  Long idUsuario, 
			                  Set<String> setNossoNumero,
			                  Set<String> setNossoNumeroCentralizacao,
			                  List<TipoCota> tiposCota)
		throws GerarCobrancaValidacaoException {
		
		this.gerarCobrancaCota(idCota, 
				               idUsuario, 
				               setNossoNumero,
				               setNossoNumeroCentralizacao,
        		               false,
        		               tiposCota);
		
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
	@Transactional(noRollbackFor = GerarCobrancaValidacaoException.class, timeout = 500)
	public void gerarDividaPostergada(Long idCota, 
			                          Long idUsuario)
		throws GerarCobrancaValidacaoException {
		
		this.gerarCobrancaCota(idCota, 
				               idUsuario, 
				               new HashSet<String>(),
				               new HashSet<String>(),
        		               true,
        		               null);
	}
	
    /**
     * Salva informações pendência de envio de documento de cobrança por email
     * 
     * @param nossoNumeroEnvioEmail
     */
    private void salvarBoletoEmailPendenteEnvio(Set<String> nossoNumeroEnvioEmail){
    	
		if (nossoNumeroEnvioEmail!=null && !nossoNumeroEnvioEmail.isEmpty()){

		    this.boletoEmailService.salvarBoletoEmail(nossoNumeroEnvioEmail);
		}
    }
    
    /**
     * Verifica se cota esta suspensa, se estiver verifica se existe chamada de encalhe na data de operação
     * 
     * Vparam cotaAnterior
     * @param cotaAtual
     * @param dataOperacao
     * @return boolean
     */
    private boolean isCotaSuspensaSemChamadaEncalhe(Cota cotaAnterior, Cota cotaAtual, Date dataOperacao){

		if (SituacaoCadastro.SUSPENSO.equals(cotaAnterior.getSituacaoCadastro()) &&
			!this.cotaUnificacaoRepository.verificarCotaUnificada(cotaAnterior.getNumeroCota())){
			
			if (!cotaAtual.equals(cotaAnterior)){
				
				if (this.chamadaEncalheCotaRepository.obterQtdListaChamaEncalheCota(cotaAnterior.getNumeroCota(), 
					dataOperacao, null, false, false, false) <= 0){
					
					return true;
				}
			}
		}
		
		return false;
    }
    
    /**
     * Verifica se cota unifica cobranca de fornecedores
     * Obtém formas de cobrança compativeis com fornecedor e data de operação
     * Verifica se próximo fornecedor tambem se encontra na forma de cobrança do fornecedor anterior
     * 
     * @param cota
     * @param fornecedorAnterior
     * @param fornecedorAtual
     * @param mapFormasCobrancaFornecedor
     * @return boolean
     */
	private boolean isUnificaCobranca(Cota cota,
			                          Fornecedor fornecedorAnterior, 
    		                          Fornecedor fornecedorAtual, 
    		                          Map<Fornecedor, List<FormaCobranca>> mapFormasCobrancaFornecedor){
    	
    	List<FormaCobranca> fcs = mapFormasCobrancaFornecedor.get(fornecedorAnterior);
    	
        for (FormaCobranca fc : fcs) {
        	
        	if (fc.getParametroCobrancaCota() != null && cota != null) {
        		
        		if (cota != null) {
        			
        		    if  (fc.getFornecedores().contains(fornecedorAtual) && !fornecedorAtual.equals(fornecedorAnterior))
        		    	return true;
        		}    
        	}
        }
        
        for (FormaCobranca fc : fcs) {

        	if (fc.getPoliticaCobranca() != null) {
        		
        		if  (fc.getFornecedores().contains(fornecedorAtual) && !fornecedorAtual.equals(fornecedorAnterior))
        			return true;
        	}
        }
       
        return false; 
    }
    
    /**
     * Obtem Data de Vencimento onforme Parametros 
     * 
     * @param dataConsolidado
     * @param fatorVencimento
     * @return Date
     */
	@Override
	@Transactional
	public Date obterDataVencimentoCobrancaCota(Date dataConsolidado, Integer fatorVencimento, String localidade) {
		
		FormaCobranca formaCobranca = formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
		
		if(formaCobranca == null){
			return DateUtil.adicionarDias(dataConsolidado, fatorVencimento);
		}
		
        // verifica se a forma de cobrança principal do distribuidor utiliza
        // dias uteis para geração da data de vencimento da cobrança
		if(formaCobranca.isVencimentoDiaUtil()) {
			return this.calendarioService.adicionarDiasUteis(dataConsolidado, fatorVencimento, localidade);
		}
		
		// odemir fez essa parte
		//if(formaCobranca.isVencimentoDiaUtil()) {
			//Date dt = DateUtil.adicionarDias(dataConsolidado, fatorVencimento);
			//return this.calendarioService.adicionarDiasUteis(dt, 0, localidade);
		//}
		
		return DateUtil.adicionarDias(dataConsolidado, fatorVencimento);
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
			
			Fornecedor fornecedor = chamadaEncalheFornecedor.getFornecedor();
			
			Banco banco = fornecedor.getBanco();
			
			if (banco == null) {
				
                throw new ValidacaoException(TipoMensagem.ERROR, "Fornecedor " + fornecedor.getJuridica().getNome()
                    + " não possui banco vinculado!");
			}
			
			String nossoNumeroDistribuidor = Util.gerarNossoNumeroDistribuidor(
					codigoDistribuidor, 
					dataOperacao, 
					banco.getNumeroBanco(), 
					fornecedor.getId(), 
					chamadaEncalheFornecedor.getId(), 
					banco.getCodigoCedente(),
					banco.getDigitoCodigoCedente());
			
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
				
				boletoDistribuidor.setValor(chamadaEncalheFornecedor.getTotalCreditoApurado().subtract(chamadaEncalheFornecedor.getTotalMargemApurado()));
			}
			
			boletoDistribuidor.setVias(1);
			
			boletoDistribuidorRepository.adicionar(boletoDistribuidor);
			
			listaBoletoDistribuidor.add(boletoDistribuidor);
		}
		
		return listaBoletoDistribuidor;
	}

    /**
     * Obtem forma de cobranca da Cota
     * 
     * @param cota
     * @param valor
     * @param fornecedor
     * @param dataOperacao
     * @return FormaCobranca
     */
    private FormaCobranca obterFormaCobranca(Cota cota,
    		                                 BigDecimal valor, 
    		                                 Fornecedor fornecedor, 
    		                                 Date dataOperacao){

		FormaCobranca fc = this.formaCobrancaService.obterFormaCobranca(cota == null ? null : cota.getId(), 
				                                                        fornecedor == null ? null : fornecedor.getId(), 
													                    dataOperacao,
													                    valor);
		
		return fc;
    }
    
    private FormaCobranca obterFormaCobrancaBoletoAvulso(){

		return this.formaCobrancaService.obterFormaCobrancaBoletoAvulso(TipoCobranca.BOLETO_AVULSO);
		
    }
    
    /**
     * Processa Consolidado e gera Divida e Cobrança para cotas sem Centralização
     * Apura cotas com cetralização para serem processadas após agrupamento
     * 
     * @param cota
     * @param movimentos
     * @param usuario
     * @param qtdDiasNovaCobranca
     * @param dataOperacao
     * @param msgs
     * @param fornecedor
     * @param postergarDividas
     * @param consolidadosCotaCentralizacao
     * @param vlMovFinanTotal
     * @param vlMovPostergado
     * @param setNossoNumero
     * @throws GerarCobrancaValidacaoException
     */
    private void processarConsolidadoDividaCobranca(Cota cota,
											        List<MovimentoFinanceiroCota> movimentos,
											        Usuario usuario, 
											        int qtdDiasNovaCobranca, 
											        Date dataOperacao, 
											        List<String> msgs,
											        Fornecedor fornecedor,
											        boolean postergarDividas,
											        Map<Cota, List<GerarCobrancaHelper>> consolidadosCotaCentralizacao,
											        BigDecimal vlMovFinanTotal,
											        BigDecimal vlMovPostergado,
											        Set<String> setNossoNumero, 
											        boolean isBoletoAvulso, 
											        Map<Integer, Long> cotasBanco, 
											        boolean isCobrancaNFe) throws GerarCobrancaValidacaoException{
    	
    	Map<Cota, List<GerarCobrancaHelper>> consolidadosCota = new HashMap<Cota, List<GerarCobrancaHelper>>();
    	
    	if(isBoletoAvulso) {
    		this.processarConsolidado(cota, 
					  movimentos, 
					  usuario, 
					  qtdDiasNovaCobranca, 
					  dataOperacao, 
					  msgs, 
					  fornecedor, 
					  false, 
					  consolidadosCota, 
					  consolidadosCotaCentralizacao,
					  vlMovFinanTotal, 
					  vlMovPostergado, 
					  isBoletoAvulso,
					  isCobrancaNFe);
    		
    		if(!consolidadosCota.isEmpty()) {
    			
    			this.gerarDividaCobrancaSemCentralizacao(usuario, 
    					dataOperacao, 
    					setNossoNumero, 
    					consolidadosCota,
    					msgs, 
    					isBoletoAvulso, 
    					cotasBanco, 
    					isCobrancaNFe);
    		}
    	} else {
    		
    		this.processarConsolidado(cota, 
    				movimentos, 
    				usuario, 
    				qtdDiasNovaCobranca, 
    				dataOperacao, 
    				msgs, 
    				fornecedor, 
    				postergarDividas, 
    				consolidadosCota, 
    				consolidadosCotaCentralizacao,
    				vlMovFinanTotal, 
    				vlMovPostergado, 
    				isBoletoAvulso,
    				isCobrancaNFe);
    		
    		if(!consolidadosCota.isEmpty()) {
    			
    			this.gerarDividaCobrancaSemCentralizacao(usuario, 
    					dataOperacao, 
    					setNossoNumero, 
    					consolidadosCota,
    					msgs, 
    					isBoletoAvulso, 
    					null, 
    					isCobrancaNFe);
    		}
    	}
    	

    }
	
	/**
     * Gera Cobraça para uma ou todas as cotas com pendências financeiras A
     * divida pode ser postergada caso não hava forma de cobrança compativel com
     * a pendência ou se o parametro postergarDividas == true
     * Pode haver, se passado o parametro, o processamento financeiro e geração de 
     * cobrança apenas para cotas de tipos especificos(A_VISTA/CONSIGNADO)
     * 
     * @param idCota
     * @param idUsuario
     * @param setNossoNumero
     * @param setNossoNumeroCentralizacao
     * @param postergarDividas
     * @param tiposCota
     * @throws GerarCobrancaValidacaoException
     */
	private void gerarCobrancaCota(Long idCota, 
			                       Long idUsuario, 
			                       Set<String> setNossoNumero,
			                       Set<String> setNossoNumeroCentralizacao,
			                       boolean postergarDividas,
			                       List<TipoCota> tiposCota) throws GerarCobrancaValidacaoException {
		

		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		Usuario usuario = this.usuarioRepository.buscarPorId(idUsuario);
		
		Integer numeroDiasNovaCobranca = this.distribuidorRepository.obterNumeroDiasNovaCobranca(); 
		
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = this.movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCota(idCota, dataOperacao, tiposCota);
		
		List<String> msgs = new ArrayList<String>();
		
		Map<Cota, List<GerarCobrancaHelper>> consolidadosCotaCentralizacao = new HashMap<Cota, List<GerarCobrancaHelper>>();
		
		Map<Fornecedor,List<FormaCobranca>> mapFormasCobrancaFornecedor = this.formaCobrancaService.obterMapFornecedorFormasCobranca(dataOperacao);
		
		if (listaMovimentoFinanceiroCota != null && !listaMovimentoFinanceiroCota.isEmpty()) {
			
			MovimentoFinanceiroCota primeiroMovimentoFinanceiroCota = listaMovimentoFinanceiroCota.get(0);
			
			Fornecedor fornecedorAnterior = primeiroMovimentoFinanceiroCota.getFornecedor();
			
			Fornecedor fornecedorAtual = null;
			
			Cota cotaAnterior = primeiroMovimentoFinanceiroCota.getCota();
			
			BigDecimal valorMovimentos = BigDecimal.ZERO;
			
			List<MovimentoFinanceiroCota> movimentos = new ArrayList<MovimentoFinanceiroCota>();

			for (MovimentoFinanceiroCota movimentoFinanceiroCota : listaMovimentoFinanceiroCota){
				
				Cota cotaAtual = movimentoFinanceiroCota.getCota();

				if (this.isCotaSuspensaSemChamadaEncalhe(cotaAnterior, cotaAtual, dataOperacao)){
							
				    continue;
				}
							    
				fornecedorAtual = movimentoFinanceiroCota.getFornecedor();

				if (fornecedorAtual == null) {
			    	
			    	throw new GerarCobrancaValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
			    			                                                  "Fornecedor não encontrado para o [Movimento Financeiro " +
			    			                                                  movimentoFinanceiroCota.getId() + "] [Cota " + 
			    			                                                  cotaAtual.getNumeroCota() + "]."));
			    }
				
				boolean unificaCobranca = this.isUnificaCobranca(cotaAnterior,
						                                         fornecedorAnterior, 
						                                         fornecedorAtual, 
						                                         mapFormasCobrancaFornecedor);
				
				TipoMovimentoFinanceiro tipo = (TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento();

				if ((cotaAtual.getId().equals(cotaAnterior.getId()) &&
				   fornecedorAtual.getId().equals(fornecedorAnterior.getId())) || 
				    unificaCobranca) {
					
					movimentos.add(movimentoFinanceiroCota);			
					  
					if (tipo.getOperacaoFinaceira().equals(OperacaoFinaceira.CREDITO)) {
					    
					    valorMovimentos = valorMovimentos.add(movimentoFinanceiroCota.getValor().negate());
					    
					} else {
					    
						valorMovimentos = valorMovimentos.add(movimentoFinanceiroCota.getValor());
					}
					
				} else {
                  	this.processarConsolidadoDividaCobranca(cotaAnterior, 
														    movimentos, 
														    usuario, 
														    numeroDiasNovaCobranca, 
														    dataOperacao, 
														    msgs, 
														    fornecedorAnterior, 
														    postergarDividas,
														    consolidadosCotaCentralizacao,
														    valorMovimentos, 
														    valorMovimentos,
														    setNossoNumero, 
														    false,
														    null,
														    false);

					cotaAnterior = cotaAtual;
					
					fornecedorAnterior = fornecedorAtual;
					
					movimentos = new ArrayList<MovimentoFinanceiroCota>();
					
					movimentos.add(movimentoFinanceiroCota);
					
					if (tipo.getOperacaoFinaceira().equals(OperacaoFinaceira.CREDITO)) {
					    
						valorMovimentos = movimentoFinanceiroCota.getValor().negate();	
					} else {
				    
				    	valorMovimentos = movimentoFinanceiroCota.getValor();
				    }
				}
			}
			
			//Ultima cota
			this.processarConsolidadoDividaCobranca(cotaAnterior, 
												    movimentos, 
												    usuario, 
												    numeroDiasNovaCobranca, 
												    dataOperacao, 
												    msgs, 
												    fornecedorAnterior, 
												    postergarDividas,
												    consolidadosCotaCentralizacao,
												    valorMovimentos, 
												    valorMovimentos,
												    setNossoNumero, 
												    false,
												    null,
												    false);
		}
		
		//Processamento de Divida e Cobrança de Cotas com Centralização
		if (!consolidadosCotaCentralizacao.isEmpty()){
		
			this.gerarDividaCobrancaComCentralizacao(usuario, 
									                 dataOperacao, 
									                 setNossoNumeroCentralizacao, 
									                 consolidadosCotaCentralizacao,
									                 msgs);
		}
	}

	/**
	 * Gera Divida e Cobrança para Cota sem Centralizacao
	 * 
	 * @param usuario
	 * @param dataOperacao
	 * @param setNossoNumero
	 * @param consolidadosCota
	 * @param msgs
	 * @throws GerarCobrancaValidacaoException 
	 */
	private void gerarDividaCobrancaSemCentralizacao(Usuario usuario, 
						                             Date dataOperacao, 
						                             Set<String> setNossoNumero,
						                             Map<Cota, List<GerarCobrancaHelper>> consolidadosCota,
						                             List<String> msgs,
						                             boolean isBoletAvulso,
						                             Map<Integer, Long> cotasBanco, boolean isCobrancaNFe) throws GerarCobrancaValidacaoException{
		
        for (Entry<Cota, List<GerarCobrancaHelper>> entry : consolidadosCota.entrySet()){
			
			List<GerarCobrancaHelper> lista = entry.getValue();
			
			Cota cota =  entry.getKey();
			
			GerarCobrancaHelper helperPrincipal = null;
			
			List<ConsolidadoFinanceiroCota> consolidados = new ArrayList<ConsolidadoFinanceiroCota>();
			
			for (GerarCobrancaHelper helper : lista){
				
				if (helper.getCota().equals(cota)){
					
					helperPrincipal = helper;
				}
				
				consolidados.add(helper.getConsolidadoFinanceiroCota());
			}
			
			if (helperPrincipal == null){
			    
			    helperPrincipal = lista.get(0);
			}
			
			this.gerarDividaCobranca(cota, 
									 helperPrincipal.getFormaCobrancaPrincipal(),
									 helperPrincipal.isCobrarHoje(),
									 consolidados,
									 usuario, 
									 helperPrincipal.getQtdDiasNovaCobranca(), 
									 msgs, 
									 helperPrincipal.getFornecedor(), 
									 helperPrincipal.getDiasSemanaConcentracaoPagamento(), 
									 dataOperacao, 
									 helperPrincipal.getDataVencimento(),
									 helperPrincipal.getDataConsolidado(),
									 setNossoNumero, 
									 isBoletAvulso,
									 cotasBanco,
									 isCobrancaNFe);
		}
		
        this.salvarBoletoEmailPendenteEnvio(setNossoNumero);
		
		if (!msgs.isEmpty()){
			
			throw new GerarCobrancaValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, msgs));
		}
	}
	
	/**
	 * Verifica se a cobrança deve ser realizada no dia atual
	 * 
	 * @param formaCobranca
	 * @param dataOperacao
	 * @param fatorVencimento
	 * @return boolean
	 */
	private boolean cobrarHoje(FormaCobranca formaCobranca,
			                   Date dataOperacao, 
			                   Integer fatorVencimento){
		
		boolean cobrarHoje = false;
		
		Calendar c = null;
		
		TipoFormaCobranca tipoFormaCobranca = formaCobranca.getTipoFormaCobranca();
		
		switch(tipoFormaCobranca){
		
			case DIARIA:
				
				cobrarHoje = true;
				
				break;
			
			case QUINZENAL:
				
				c = Calendar.getInstance();
				
				c.setTime(dataOperacao);
				
				cobrarHoje = formaCobranca.getDiasDoMes().contains(c.get(Calendar.DAY_OF_MONTH));
				
				break;
			
			case MENSAL:
				
				c = Calendar.getInstance();
				
				c.setTime(dataOperacao);
				
				cobrarHoje = formaCobranca.getDiasDoMes().get(0).equals(c.get(Calendar.DAY_OF_MONTH));
				
				break;
			
			case SEMANAL:
				
				c = Calendar.getInstance();
				
				c.setTime(dataOperacao);
				
				for (ConcentracaoCobrancaCota conc : formaCobranca.getConcentracaoCobrancaCota()){
					
					cobrarHoje = c.get(Calendar.DAY_OF_WEEK) == conc.getDiaSemana().getCodigoDiaSemana();
					
					if (cobrarHoje){
						
						break;
					}
				}
				
				break;
		}
	
		return cobrarHoje;
	}
	
	private List<Map<CotaCentralizadoraDTO, List<GerarCobrancaHelper>>> quebrarUnificacaoPorFornecedor(Map<Cota, List<GerarCobrancaHelper>> consolidadosCota) {
		
		List<Map<CotaCentralizadoraDTO, List<GerarCobrancaHelper>>> list = new ArrayList<>();

		Map<Fornecedor,List<FormaCobranca>> mapFormasCobrancaFornecedor = 
				this.formaCobrancaService.obterMapFornecedorFormasCobranca(this.distribuidorRepository.obterDataOperacaoDistribuidor());

		for (Entry<Cota, List<GerarCobrancaHelper>> entry : consolidadosCota.entrySet()){
			
			Cota unificadora = entry.getKey();
			
			List<GerarCobrancaHelper> helpers = new ArrayList<>();
			
			Map<CotaCentralizadoraDTO, List<GerarCobrancaHelper>> map = new HashMap<>();
			
			Collections.sort(entry.getValue(), new Comparator<GerarCobrancaHelper>() {

				@Override
				public int compare(GerarCobrancaHelper o1, GerarCobrancaHelper o2) {
					return o1.getFornecedor().getId().compareTo(o2.getFornecedor().getId());
				}				
			});

			Fornecedor fornecedorAnterior = entry.getValue().get(0).getFornecedor();
			
			for (GerarCobrancaHelper helper : entry.getValue()) {
				
				if (fornecedorAnterior.equals(helper.getFornecedor()) ||
						this.isUnificaCobranca(helper.getCota(), fornecedorAnterior, helper.getFornecedor(), mapFormasCobrancaFornecedor)) {
					
					helpers.add(helper);

				} else {

					map.put(new CotaCentralizadoraDTO(unificadora, fornecedorAnterior), helpers);
					
					list.add(map);
					
					map = new HashMap<>();
					
					helpers = new ArrayList<>();

					helpers.add(helper);
				}
				
				fornecedorAnterior = helper.getFornecedor();
			}

			map.put(new CotaCentralizadoraDTO(unificadora, fornecedorAnterior), helpers);
			
			list.add(map);
		}
		
		return list;
	}
	
	/**
	 * Gera Divida e Cobrança para Cota com Centralizacao
	 * 
	 * @param usuario
	 * @param dataOperacao
	 * @param setNossoNumero
	 * @param consolidadosCota
	 * @param msgs
	 * @throws GerarCobrancaValidacaoException 
	 */
	private void gerarDividaCobrancaComCentralizacao(Usuario usuario, 
						                             Date dataOperacao, 
						                             Set<String> setNossoNumero,
						                             Map<Cota, List<GerarCobrancaHelper>> consolidadosCota,
						                             List<String> msgs) throws GerarCobrancaValidacaoException {
		
		List<Map<CotaCentralizadoraDTO, List<GerarCobrancaHelper>>> quebraPorFornecedor = this.quebrarUnificacaoPorFornecedor(consolidadosCota); 
		
		for (Map<CotaCentralizadoraDTO, List<GerarCobrancaHelper>> mapPorFornecedor : quebraPorFornecedor) {
		
	        for (Entry<CotaCentralizadoraDTO, List<GerarCobrancaHelper>> entry : mapPorFornecedor.entrySet()) {
				
				List<GerarCobrancaHelper> lista = entry.getValue();
				
				CotaCentralizadoraDTO cotaCentralizadora =  entry.getKey();
				
				BigDecimal valorTotalMovimentos = BigDecimal.ZERO;
				
				GerarCobrancaHelper helperPrincipal = null;
				
				List<ConsolidadoFinanceiroCota> consolidados = new ArrayList<ConsolidadoFinanceiroCota>();

				for (GerarCobrancaHelper helper : lista) {
					
					valorTotalMovimentos = valorTotalMovimentos.add(helper.getConsolidadoFinanceiroCota().getTotal().setScale(2, RoundingMode.HALF_UP));
					
					if (helper.getCota().equals(cotaCentralizadora)){
						
						helperPrincipal = helperPrincipal == null ? helper : helperPrincipal;
					}
					
					consolidados.add(helper.getConsolidadoFinanceiroCota());
				}
				
				if (helperPrincipal == null){
				    
				    helperPrincipal = lista.get(0);
				}
	
				FormaCobranca formaCobranca = this.obterFormaCobranca(cotaCentralizadora.getCota(), 
						                                              valorTotalMovimentos.abs(), 
						                                              cotaCentralizadora.getFornecedor(), 
						                                              dataOperacao);
	
				if (formaCobranca == null) {
					
					if(	valorTotalMovimentos == null || 
						valorTotalMovimentos.compareTo(BigDecimal.ZERO) == 0){
						
						break;
						
					}
					
					MovimentoFinanceiroCota movimentoFinanceiroCota = this.gerarPostergado(cotaCentralizadora.getCota(),
							                                                               helperPrincipal.getQtdDiasNovaCobranca(), 
																                           msgs, 
																                           cotaCentralizadora.getFornecedor(),
																                           consolidados, 
																                           valorTotalMovimentos, 
																                           usuario,
																                           helperPrincipal.getDiasSemanaConcentracaoPagamento(), 
																                           dataOperacao,
																                           null);
	
				    this.movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiroCota);
				    
				    break;
				}
				
				helperPrincipal.setFormaCobrancaPrincipal(formaCobranca);
				
				Integer fatorVencimento = 0;
				
				ParametroCobrancaCota parametroCobrancaCota = formaCobranca.getParametroCobrancaCota();
				
				boolean cobrarHoje = false;
				
				if(parametroCobrancaCota!=null && parametroCobrancaCota.getFatorVencimento()!=null) {
					
					fatorVencimento = parametroCobrancaCota.getFatorVencimento();
				}
				else {
					
					PoliticaCobranca politicaCobranca = formaCobranca.getPoliticaCobranca();
					
					if(politicaCobranca != null && politicaCobranca.getFatorVencimento() != null){
						
						fatorVencimento = politicaCobranca.getFatorVencimento();
					}
					else{
						
						cobrarHoje = true;
					}
				}
				String localidade = helperPrincipal.getCota().getEnderecoPrincipal().getEndereco().getCidade();
				Date dataVencimento = this.obterDataVencimentoCobrancaCota(helperPrincipal.getConsolidadoFinanceiroCota().getDataConsolidado(),fatorVencimento, localidade);
				
				if(!cobrarHoje) {
	
					cobrarHoje = this.cobrarHoje(formaCobranca, dataOperacao, fatorVencimento);
				}
				
				if (dataVencimento == null){
					
		            msgs.add("Não foi possível calcular data de vencimento da cobrança, verifique os parâmetros de cobrança da cota número: "
		                    + cotaCentralizadora.getCota().getNumeroCota());
					
					break;
				}
	
				this.gerarDividaCobrancaCentralizacao(cotaCentralizadora.getCota(),  
													  formaCobranca, 
													  valorTotalMovimentos, 
													  cobrarHoje, 
	                                                  consolidados, 
													  usuario, 
													  helperPrincipal.getQtdDiasNovaCobranca(), 
													  msgs, 
													  cotaCentralizadora.getFornecedor(), 
													  helperPrincipal.getDiasSemanaConcentracaoPagamento(), 
													  dataOperacao, 
													  dataVencimento, 
													  helperPrincipal.getDataConsolidado(), 
													  setNossoNumero);
				
			}
		}
		
        this.salvarBoletoEmailPendenteEnvio(setNossoNumero);
		
		if (!msgs.isEmpty()){
			
			throw new GerarCobrancaValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, msgs));
		}
	}
	
	/**
	 * Adiciona GerarCobrancaHelper em Lista
	 * 
	 * @param cota
	 * @param consolidadosCota
	 * @param consolidadoFinanceiroCota
	 * @param fornecedor
	 * @param formaCobrancaPrincipal
	 * @param dataVencimento
	 * @param qtdDiasNovaCobranca
	 * @param cobrarHoje
	 */
	private void addConsolidadoHelper(Cota cota,
                                      Map<Cota, List<GerarCobrancaHelper>> consolidadosCota,
                                      ConsolidadoFinanceiroCota consolidadoFinanceiroCota,
                                      Fornecedor fornecedor,
	                                  FormaCobranca formaCobrancaPrincipal,
	                                  Date dataVencimento,
	                                  int qtdDiasNovaCobranca,
	                                  boolean cobrarHoje){
		
			
        if (!consolidadosCota.containsKey(cota)){
			
			consolidadosCota.put(cota, new ArrayList<GerarCobrancaHelper>());
		}
		
		GerarCobrancaHelper gcHelper = new GerarCobrancaHelper(consolidadoFinanceiroCota.getCota(),
															   formaCobrancaPrincipal, 
															   cobrarHoje, 
															   consolidadoFinanceiroCota, 
															   qtdDiasNovaCobranca, 
															   fornecedor, 
															   dataVencimento,
															   consolidadoFinanceiroCota.getDataConsolidado());
        
		consolidadosCota.get(cota).add(gcHelper);
	}

	/**
	 * Processa Consolidado
	 * Dintingue cotas com ou sem Centralização
	 * Separa Informações de consolidados em Cotas Centralizadoras e Cotas que não Centralizam
	 * 
	 * @param cota
	 * @param movimentos
	 * @param usuario
	 * @param qtdDiasNovaCobranca
	 * @param dataOperacao
	 * @param msgs
	 * @param fornecedor
	 * @param postergarDividas
	 * @param consolidadosCota
	 * @param consolidadosCotaCentralizacao
	 * @param vlMovFinanTotal
	 * @param vlMovPostergado
	 */
	private void processarConsolidado(Cota cota,
								      List<MovimentoFinanceiroCota> movimentos,
								      Usuario usuario, 
								      int qtdDiasNovaCobranca, 
								      Date dataOperacao, 
								      List<String> msgs,
								      Fornecedor fornecedor,
								      boolean postergarDividas,
								      Map<Cota, List<GerarCobrancaHelper>> consolidadosCota,
								      Map<Cota, List<GerarCobrancaHelper>> consolidadosCotaCentralizacao,
								      BigDecimal vlMovFinanTotal,
								      BigDecimal vlMovPostergado, 
								      boolean isBoletoAvulso,
								      boolean isCobrancaNFe){
		
		
		if(isBoletoAvulso) {
			
			FormaCobranca formaCobrancaDiferenciada = this.obterFormaCobrancaBoletoAvulso();
			
			if(formaCobrancaDiferenciada == null) {
				throw new ValidacaoException(TipoMensagem.ERROR, "Não foi criado uma forma de cobrança para boleto Avulso.");
			}
			
			this.getConsolidadoHelperSemCentralizacao(cota, 
	                movimentos, 
	                usuario, 
	                qtdDiasNovaCobranca, 
	                dataOperacao, 
	                msgs, 
	                fornecedor, 
	                formaCobrancaDiferenciada, 
	                postergarDividas, 
	                consolidadosCota, 
	                vlMovFinanTotal, 
	                isBoletoAvulso,
	                isCobrancaNFe);
			
			
		} else {
			Cota cotaCentralizadora = this.cotaUnificacaoRepository.obterCotaUnificadoraPorCota(cota.getNumeroCota());
			
			if (cotaCentralizadora == null){
				
				FormaCobranca formaCobranca = this.obterFormaCobranca(cota,
						vlMovFinanTotal,
						fornecedor, 
						dataOperacao);
				
				this.getConsolidadoHelperSemCentralizacao(cota, 
						movimentos, 
						usuario, 
						qtdDiasNovaCobranca, 
						dataOperacao, 
						msgs, 
						fornecedor, 
						formaCobranca, 
						postergarDividas, 
						consolidadosCota, 
						vlMovFinanTotal, 
						isBoletoAvulso,
						isCobrancaNFe);
			} else {
				
				this.getConsolidadoHelperComCentralizacao(cota,
						cotaCentralizadora, 
						movimentos, 
						qtdDiasNovaCobranca, 
						dataOperacao, 
						fornecedor,
						consolidadosCotaCentralizacao);
			}	
		}
		
	}
	
	/**
	 * Cotas sem Centralizacao
	 * Gera Consolidado
	 * Extrai informações de forma de cobrança compativel com os parametros passados
	 * Monta Helper com Informações do consolidado gerado e adiciona la lista
	 * 
	 * @param cota
	 * @param movimentos
	 * @param usuario
	 * @param qtdDiasNovaCobranca
	 * @param dataOperacao
	 * @param msgs
	 * @param fornecedor
	 * @param formaCobrancaPrincipal
	 * @param postergarDividas
	 * @param consolidadosCota
	 * @param vlMovFinanTotal
	 */
	private void getConsolidadoHelperSemCentralizacao(Cota cota,
													  List<MovimentoFinanceiroCota> movimentos,
													  Usuario usuario, 
													  int qtdDiasNovaCobranca, 
											          Date dataOperacao, 
											          List<String> msgs,
											          Fornecedor fornecedor,
											          FormaCobranca formaCobrancaPrincipal,
											          boolean postergarDividas,
											          Map<Cota, List<GerarCobrancaHelper>> consolidadosCota,
											          BigDecimal vlMovFinanTotal, 
											          boolean isBoletoAvulso,
											          boolean isCobrancaNFe){
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = this.montarConsolidadoFinanceiro(cota, movimentos, dataOperacao, isBoletoAvulso);
		
		if(isCobrancaNFe) {
			isBoletoAvulso = isCobrancaNFe;
		}
		
		if(cota.isBoletoNFE() && !isBoletoAvulso) {
			MovimentoFinanceiroCota movPost = this.gerarPostergado(cota, 
                    qtdDiasNovaCobranca, 
                    msgs, 
                    fornecedor, 
                    Arrays.asList(consolidadoFinanceiroCota), 
                    vlMovFinanTotal,
                    usuario, 
                    null, 
                    dataOperacao,
                    "Processamento Financeiro - Divida postergada boleto por NFE");

			if (movPost != null){
				this.movimentoFinanceiroCotaRepository.adicionar(movPost);
			}

			return;
		}
		
	    // insere postergado pois não encontrou forma de cobrança ou parametros
        // do método exigem postergação
		if (formaCobrancaPrincipal == null || postergarDividas==true 
				|| (formaCobrancaPrincipal != null && TipoCobranca.BOLETO_EM_BRANCO.equals(formaCobrancaPrincipal.getTipoCobranca()))) {
			
			MovimentoFinanceiroCota movPost = this.gerarPostergado(cota, 
					                                               qtdDiasNovaCobranca, 
					                                               msgs, 
					                                               fornecedor, 
							                                       Arrays.asList(consolidadoFinanceiroCota), 
							                                       vlMovFinanTotal,
							                                       usuario, 
							                                       null, 
							                                       dataOperacao,
							                                       postergarDividas?"Processamento Financeiro - Divida postergada":null);
			
			if (movPost != null){
				
				this.movimentoFinanceiroCotaRepository.adicionar(movPost);
			}
			
			return;
		}
		
		//obtem a data de vencimento de acordo com o dia em que se concentram os pagamentos da cota
		Integer fatorVencimento = 0;
		
		ParametroCobrancaCota parametroCobrancaCota = formaCobrancaPrincipal.getParametroCobrancaCota();
		
		boolean cobrarHoje = false;
		
		if(parametroCobrancaCota!=null && parametroCobrancaCota.getFatorVencimento()!=null) {
			
			fatorVencimento = parametroCobrancaCota.getFatorVencimento();
		}
		else {
			
			PoliticaCobranca politicaCobranca = formaCobrancaPrincipal.getPoliticaCobranca();
			
			if(politicaCobranca != null && politicaCobranca.getFatorVencimento() != null){
				
				if(isBoletoAvulso) {
					fatorVencimento = 0;
				} else {					
					fatorVencimento = politicaCobranca.getFatorVencimento();
				}
				
			}
			else{
			    
				cobrarHoje = true;
			}
		}
		
		String localidade = null;
		if(cota != null && cota.getEnderecoPrincipal() != null) {
			if(cota.getEnderecoPrincipal().getEndereco() != null) {
				localidade = cota.getEnderecoPrincipal().getEndereco().getCidade();
			} else {
				
				throw new ValidacaoException(TipoMensagem.ERROR, String.format("Cota %s sem Endereço.", (cota != null) ? cota.getNumeroCota() : "[Não localizada]"));
			}
		} else {
			
			throw new ValidacaoException(TipoMensagem.ERROR, String.format("Cota %s sem Endereço Principal.", (cota != null) ? cota.getNumeroCota() : "[Não localizada]"));
		}
		
		Date dataVencimento = null;
		
		if(isCobrancaNFe) {
			dataVencimento = this.obterDataVencimentoCobrancaCota(consolidadoFinanceiroCota.getDataConsolidado(), fatorVencimento, localidade);
		} else {
			
			if(isBoletoAvulso) {
				dataVencimento = dataOperacao;
			} else {
				dataVencimento = this.obterDataVencimentoCobrancaCota(consolidadoFinanceiroCota.getDataConsolidado(), fatorVencimento, localidade);
				
			}
		}
		

		if(!cobrarHoje){

			cobrarHoje = this.cobrarHoje(formaCobrancaPrincipal, dataOperacao, fatorVencimento);
		}
		
		if (dataVencimento == null){
			
            msgs.add("Não foi possível calcular data de vencimento da cobrança, verifique os parâmetros de cobrança da cota número: "
                    + cota.getNumeroCota());
			
			return;
		}
		
   	    this.addConsolidadoHelper(cota, 
   	    		                  consolidadosCota, 
   	    		                  consolidadoFinanceiroCota, 
   	    		                  fornecedor, 
   	    		                  formaCobrancaPrincipal, 
   	    		                  dataVencimento, 
   	    		                  qtdDiasNovaCobranca, 
   	    		                  cobrarHoje);
	}

	/**
	 * Cotas com Centralização
	 * Gera Consolidado
	 * Monta Helper com Informações do consolidado gerado e adiciona na lista
	 * 
	 * @param cota
	 * @param cotaCentralizadora
	 * @param movimentos
	 * @param qtdDiasNovaCobranca
	 * @param dataOperacao
	 * @param fornecedor
	 * @param consolidadosCota
	 */
	private void getConsolidadoHelperComCentralizacao(Cota cota,
			                                          Cota cotaCentralizadora,
													  List<MovimentoFinanceiroCota> movimentos,
													  int qtdDiasNovaCobranca, 
											          Date dataOperacao,
											          Fornecedor fornecedor,
											          Map<Cota, List<GerarCobrancaHelper>> consolidadosCota){
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = this.montarConsolidadoFinanceiro(cota, movimentos, dataOperacao, false);
		
   	    this.addConsolidadoHelper(cotaCentralizadora, 
   	    		                  consolidadosCota, 
   	    		                  consolidadoFinanceiroCota, 
   	    		                  fornecedor, 
   	    		                  null, 
   	    		                  null, 
   	    		                  qtdDiasNovaCobranca, 
   	    		                  false);
	}

	/**
	 * Consolida os movimentos financeiros
	 * 
	 * @param cota
	 * @param movimentos
	 * @param dataOperacao
	 * @return ConsolidadoFinanceiroCota
	 */
	private ConsolidadoFinanceiroCota montarConsolidadoFinanceiro(Cota cota, List<MovimentoFinanceiroCota> movimentos, Date dataOperacao, boolean isBoletoAvulso) {
		
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = new ConsolidadoFinanceiroCota();
		consolidadoFinanceiroCota.setCota(cota);
		
		if(isBoletoAvulso) {
			consolidadoFinanceiroCota.setDataConsolidado(this.distribuidorRepository.obterDataOperacaoDistribuidor());
		} else {
			consolidadoFinanceiroCota.setDataConsolidado(dataOperacao);
		}
		
		consolidadoFinanceiroCota.setMovimentos(movimentos);
		
		BigDecimal vlMovFinanDebitoCredito = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncalhe = BigDecimal.ZERO;
		BigDecimal vlMovFinanEncargos = BigDecimal.ZERO;
		BigDecimal vlMovFinanVendaEncalhe = BigDecimal.ZERO;
		BigDecimal vlMovPostergado = BigDecimal.ZERO;
		BigDecimal vlMovConsignado = BigDecimal.ZERO;
		BigDecimal vlMovPendente = BigDecimal.ZERO;
		BigDecimal vlBoletoAvulso = BigDecimal.ZERO;
		
		for (MovimentoFinanceiroCota movimentoFinanceiroCota : movimentos) {
			
			if (!movimentoFinanceiroCota.getCota().getId().equals(cota.getId())) {
				
				continue;
			}
			
			GrupoMovimentoFinaceiro tipoMovimentoFinanceiro = ((TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento()).getGrupoMovimentoFinaceiro();

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
				case DEBITO_COTA_TAXA_DE_ENTREGA_ENTREGADOR:
				case DEBITO_COTA_TAXA_DE_ENTREGA_TRANSPORTADOR:
				case TAXA_EXTRA:
					
					vlMovFinanDebitoCredito = this.adicionarValor(vlMovFinanDebitoCredito, movimentoFinanceiroCota);
					
					break;
					
				case COMPRA_ENCALHE_SUPLEMENTAR:
					
					vlMovFinanVendaEncalhe = this.adicionarValor(vlMovFinanVendaEncalhe, movimentoFinanceiroCota);
					
					break;
					
				case RECEBIMENTO_REPARTE:
					
					vlMovConsignado = this.adicionarValor(vlMovConsignado, movimentoFinanceiroCota);
					
					break;
					
				case JUROS:
				case MULTA:
					
					vlMovFinanEncargos = this.adicionarValor(vlMovFinanEncargos, movimentoFinanceiroCota);
					
					break;
					
				case ENVIO_ENCALHE:

					vlMovFinanEncalhe = this.adicionarValor(vlMovFinanEncalhe, movimentoFinanceiroCota);
					
					break;
					
				case POSTERGADO_DEBITO:
				case POSTERGADO_CREDITO:
					
					vlMovPostergado = this.adicionarValor(vlMovPostergado, movimentoFinanceiroCota);
					
					break;
					
				case PENDENTE:
					
					vlMovPendente = this.adicionarValor(vlMovPendente, movimentoFinanceiroCota);
					
					break;
				
				case BOLETO_AVULSO:
					
					vlBoletoAvulso = this.adicionarValor(vlMovFinanDebitoCredito, movimentoFinanceiroCota);
					
					break;
					
				case BOLETO_NFE:
					
					vlBoletoAvulso = this.adicionarValor(vlMovFinanDebitoCredito, movimentoFinanceiroCota);
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
				.add(vlMovPendente)
				.add(vlBoletoAvulso);
				
		
		consolidadoFinanceiroCota.setTotal(vlMovFinanTotal);
		consolidadoFinanceiroCota.setDebitoCredito(vlMovFinanDebitoCredito);
		consolidadoFinanceiroCota.setEncalhe(vlMovFinanEncalhe);
		consolidadoFinanceiroCota.setEncargos(vlMovFinanEncargos);
		consolidadoFinanceiroCota.setVendaEncalhe(vlMovFinanVendaEncalhe.abs());
		consolidadoFinanceiroCota.setValorPostergado(vlMovPostergado);
		consolidadoFinanceiroCota.setConsignado(vlMovConsignado.abs());
		consolidadoFinanceiroCota.setPendente(vlMovPendente);
		
		this.consolidadoFinanceiroRepository.adicionar(consolidadoFinanceiroCota);	
		
		return consolidadoFinanceiroCota;
	}

    /**
	 * Gera Divida e Cobrança para cada Consolidado criado para Cota Centralizadora
	 * 
	 * @param cota
	 * @param formaCobrancaPrincipal
	 * @param vlMovFinanTotal
	 * @param cobrarHoje
	 * @param consolidadosFinanceiroCota
	 * @param usuario
	 * @param qtdDiasNovaCobranca
	 * @param msgs
	 * @param fornecedor
	 * @param diasSemanaConcentracaoPagamento
	 * @param dataOperacao
	 * @param dataVencimento
	 * @param dataConsolidado
	 * @param setNossoNumero
	 */
	 private void gerarDividaCobrancaCentralizacao(Cota cota,
						                           FormaCobranca formaCobrancaPrincipal,
				 								   BigDecimal vlMovFinanTotal,
				 								   boolean cobrarHoje,
				 								   List<ConsolidadoFinanceiroCota> consolidadosFinanceiroCota,
				 								   Usuario usuario, 
				 								   int qtdDiasNovaCobranca, 
			 									   List<String> msgs, 
			 									   Fornecedor fornecedor,
												   List<Integer> diasSemanaConcentracaoPagamento, 
												   Date dataOperacao, 
												   Date dataVencimento,
												   Date dataConsolidado,
												   Set<String> setNossoNumero) {
	  			
        MovimentoFinanceiroCota movimentoFinanceiroCota = null;
  	    		
  	    Divida novaDivida = null;
  			
  		boolean cotaSuspensa = SituacaoCadastro.SUSPENSO.equals(this.obterSitiacaoCadastroCota(cota.getId()));
  
 		BigDecimal valorMinino = cota.getValorMinimoCobranca();
  			
  		Banco banco = formaCobrancaPrincipal.getBanco();
 			
		if ( (vlMovFinanTotal.compareTo(BigDecimal.ZERO) < 0) &&
			 (vlMovFinanTotal.abs().compareTo(valorMinino) > 0 && cobrarHoje) || 
			 (vlMovFinanTotal.abs().compareTo(valorMinino) > 0 && cotaSuspensa)){
			
		    novaDivida = new Divida();
			novaDivida.setValor(vlMovFinanTotal.abs());
			novaDivida.setData(dataConsolidado);
			novaDivida.setConsolidados(consolidadosFinanceiroCota);
			novaDivida.setCota(cota);
			novaDivida.setStatus(StatusDivida.EM_ABERTO);
			novaDivida.setResponsavel(usuario);
			novaDivida.setOrigemNegociacao(false);
			
		} else if (vlMovFinanTotal.compareTo(valorMinino) != 0) {
  
	        movimentoFinanceiroCota = this.gerarPostergado(cota,
					                                       qtdDiasNovaCobranca, 
					                                       msgs, 
					                                       fornecedor,
					                                       consolidadosFinanceiroCota, 
					                                       vlMovFinanTotal,
					                                       usuario,
					                                       diasSemanaConcentracaoPagamento, 
					                                       dataOperacao,
					                                       null);
		}
		
		Cobranca cobranca = this.salvarDividaCobranca(formaCobrancaPrincipal, 
										      		  novaDivida, 
										      		  cota, 
										      		  banco, 
										      		  dataOperacao, 
										      		  dataVencimento, 
										      		  fornecedor,
										      		  false);
		
		if (movimentoFinanceiroCota != null){
			
			this.movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiroCota);
		}
		
		if (cobranca != null){
			
			setNossoNumero.add(cobranca.getNossoNumero());
		}
	}

	/**
	 * Gera Divida e Cobrança para cada Consolidado criado
	 * 
	 * @param cota
	 * @param formaCobrancaPrincipal
	 * @param cobrarHoje
	 * @param consolidadoFinanceiroCota
	 * @param usuario
	 * @param qtdDiasNovaCobranca
	 * @param msgs
	 * @param fornecedor
	 * @param diasSemanaConcentracaoPagamento
	 * @param dataOperacao
	 * @param dataVencimento
	 * @param dataConsolidado
	 * @return List<String>
	 */
	private void gerarDividaCobranca(Cota cota, 
	                                 FormaCobranca formaCobrancaPrincipal,
									 boolean cobrarHoje, 
									 List<ConsolidadoFinanceiroCota> consolidadoFinanceiroCota,
									 Usuario usuario, 
									 int qtdDiasNovaCobranca, 
									 List<String> msgs, 
									 Fornecedor fornecedor,
									 List<Integer> diasSemanaConcentracaoPagamento, 
									 Date dataOperacao, 
									 Date dataVencimento,
									 Date dataConsolidado,
									 Set<String> setNossoNumero,
									 boolean isBoletAvulso, 
									 Map<Integer, Long> cotasBanco,
									 boolean isCobrancaNFe) {
		
	    Divida novaDivida = null;
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = null;
		
		boolean cotaSuspensa = SituacaoCadastro.SUSPENSO.equals(this.obterSitiacaoCadastroCota(cota.getId()));

		BigDecimal valorMinino = cota.getValorMinimoCobranca(); 
		
		Banco banco = null;
		
		if(isBoletAvulso) {
			if(cotasBanco.containsKey(cota.getNumeroCota())) {
				Long idBanco = cotasBanco.get(cota.getNumeroCota()).longValue();
				
				banco = bancoService.obterBancoPorId(idBanco);
			}
		} else {
			banco = formaCobrancaPrincipal.getBanco();			
		}

		for (ConsolidadoFinanceiroCota consolidado : consolidadoFinanceiroCota) {
			
			BigDecimal valorConsolidado = consolidado.getTotal().setScale(2, RoundingMode.HALF_UP);
			
			if(isBoletAvulso) {
				
				novaDivida = new Divida();
				novaDivida.setValor(valorConsolidado.abs());
				novaDivida.setData(dataConsolidado);
				novaDivida.setConsolidados(Arrays.asList(consolidado));
				novaDivida.setCota(cota);
				novaDivida.setStatus(StatusDivida.EM_ABERTO);
				novaDivida.setResponsavel(usuario);
				novaDivida.setOrigemNegociacao(false);
				
				Date dataEmissao = distribuidorRepository.obterDataOperacaoDistribuidor();
				
                Cobranca cobranca = this.salvarDividaCobranca(formaCobrancaPrincipal, 
									                		  novaDivida, 
									                		  cota, 
									                		  banco, 
									                		  dataEmissao, 
									                		  dataVencimento, 
									                		  fornecedor,
									                		  isCobrancaNFe);	

				if (cobranca != null && setNossoNumero != null){
					
					setNossoNumero.add(cobranca.getNossoNumero());
				}
				
			} else {
				// caso tenha alcançado o valor minino de cobrança e seja um dia de
	            // concentração de cobrança, ou a cota esteja suspensa
				if ( (valorConsolidado.compareTo(BigDecimal.ZERO) < 0) &&
				     (valorConsolidado.abs().compareTo(valorMinino) > 0 && cobrarHoje) || 
					 (valorConsolidado.abs().compareTo(valorMinino) > 0 && cotaSuspensa)){
					
					BigInteger idDividaPendente = dividaRepository.obterIdDividaPendente(consolidado, distribuidorRepository.obterDataOperacaoDistribuidor());
					
					novaDivida = new Divida();
					novaDivida.setValor(valorConsolidado.abs());
					novaDivida.setData(dataConsolidado);
					novaDivida.setConsolidados(Arrays.asList(consolidado));
					novaDivida.setCota(cota);
					novaDivida.setStatus(StatusDivida.EM_ABERTO);
					novaDivida.setResponsavel(usuario);
					
					if(idDividaPendente != null) {
						
						Divida divida = dividaRepository.buscarPorId(Long.valueOf(idDividaPendente.intValue()));
						
						LOGGER.info("Divida Raiz: "+ divida.toString());
						
						if(divida != null) {
							novaDivida.setDividaRaiz(divida);					
						}
					}
					
					novaDivida.setOrigemNegociacao(false);
										
	                Cobranca cobranca = this.salvarDividaCobranca(formaCobrancaPrincipal, 
										                		  novaDivida, 
										                		  cota, 
										                		  banco, 
										                		  dataOperacao, 
										                		  dataVencimento, 
										                		  fornecedor,
										                		  isCobrancaNFe);	

					if (cobranca != null && setNossoNumero != null){
						
						setNossoNumero.add(cobranca.getNossoNumero());
					}
				} 
				else if (valorConsolidado.compareTo(valorMinino) != 0) {

					movimentoFinanceiroCota = this.gerarPostergado(cota,
							                                       qtdDiasNovaCobranca, 
							                                       msgs, 
							                                       fornecedor,
							                                       consolidadoFinanceiroCota, 
							                                       valorConsolidado,
							                                       usuario,
							                                       diasSemanaConcentracaoPagamento, 
							                                       dataOperacao,
							                                       null);
					
					this.movimentoFinanceiroCotaRepository.adicionar(movimentoFinanceiroCota);
				}
			}
		}
	}
	
	/**
	 * Salva Divida e Cobrança conforme parametros de Forma Cobrança
	 * 
	 * @param formaCobrancaPrincipal
	 * @param novaDivida
	 * @param cota
	 * @param banco
	 * @param dataOperacao
	 * @param dataVencimento
	 * @param fornecedor
	 * @return Cobranca
	 */
	private Cobranca salvarDividaCobranca(FormaCobranca formaCobrancaPrincipal, 
				                          Divida novaDivida,
				                          Cota cota,
				                          Banco banco,
				                          Date dataOperacao,
				                          Date dataVencimento,
				                          Fornecedor fornecedor,
				                          boolean isCobrancaNFe) {
			
		Cobranca cobranca = null;
		
		if (novaDivida != null) {
			
			if (novaDivida.getId() == null) {
				
				novaDivida.setId(this.dividaRepository.adicionar(novaDivida));
				
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
				case BOLETO_AVULSO:
					cobranca = new Boleto();
					cobranca.setTipoCobranca(TipoCobranca.BOLETO_AVULSO);
				break;
                default:
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
			cobranca.setCobrancaNFe(isCobrancaNFe);
			
			String nossoNumero =
				Util.gerarNossoNumero(
					cota.getNumeroCota(), 
					cobranca.getDataEmissao(), 
					banco != null ? banco.getNumeroBanco() : "0",
					fornecedor != null ? fornecedor.getId() : null,
					novaDivida.getId(),
					banco != null ? banco.getAgencia() : 0,
					banco != null ? banco.getConta() : 0,
					banco != null && banco.getCarteira() != null ? banco.getCarteira() : null,
					banco != null && banco.getCodigoCedente() != null ? banco.getCodigoCedente() : null, 
					banco != null && banco.getDigitoCodigoCedente() != null ? banco.getDigitoCodigoCedente() : null);
			
			cobranca.setFornecedor(fornecedor);
			cobranca.setNossoNumero(nossoNumero);
			
			String digitoVerificador =
				Util.calcularDigitoVerificador(nossoNumero, banco != null ? banco.getCodigoCedente() : "0", cobranca.getDataVencimento(), banco != null ? banco.getNumeroBanco() : null);
			
			cobranca.setDigitoNossoNumero(digitoVerificador);
			
			cobranca.setNossoNumeroCompleto(nossoNumero + ((digitoVerificador != null) ? digitoVerificador : ""));
			
			cobranca.setValor(novaDivida.getValor());
			
			this.cobrancaRepository.adicionar(cobranca);
		}			
		
		return cobranca;
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
			                                        Usuario usuario,
			                                        List<Integer> diasSemanaConcentracaoPagamento, 
			                                        Date dataOperacao,
			                                        String descPostergado) {
		
		Calendar diaPostergado = Calendar.getInstance();
		diaPostergado.setTime(dataOperacao);
		diaPostergado.add(Calendar.DAY_OF_MONTH, qtdDiasNovaCobranca);
	
		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = null;
		if (vlMovFinanTotal.compareTo(BigDecimal.ZERO) < 0){
			
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
	
	@Transactional(timeout = 500)
	@Override
	public void cancelarDividaCobranca(Long idMovimentoFinanceiroCota, Long idCota, Date dataOperacao, boolean excluiFinanceiro) {
		
		List<ConsolidadoFinanceiroCota> consolidados = null;
		
		if (idMovimentoFinanceiroCota != null) {

			consolidados = this.consolidadoFinanceiroRepository.obterConsolidadoPorIdMovimentoFinanceiro(idMovimentoFinanceiroCota);
		
		} else {
			
			List<Long> idConsolidados = this.consolidadoFinanceiroRepository.obterIdsConsolidadosDataOperacao(idCota, dataOperacao);
			
			if(idConsolidados.size() > 0) {
				consolidados = this.consolidadoFinanceiroRepository.obterConsolidadosDataOperacao(idConsolidados);				
			}
		}
		
		if (consolidados != null && !consolidados.isEmpty()) {
			
			for (ConsolidadoFinanceiroCota consolidado : consolidados) {
				
				Divida divida = this.dividaRepository.obterDividaPorIdConsolidado(consolidado.getId());
				
				if (divida != null) {
					
					Negociacao negociacao = this.negociacaoRepository.obterNegociacaoPorCobranca(consolidado.getId());

					if (negociacao != null && TipoNegociacao.PAGAMENTO_AVULSO.equals(negociacao.getTipoNegociacao())) {
						
						continue;
					}

					if(divida.getCobranca() != null) {
						
						this.cobrancaControleConferenciaEncalheCotaRepository.excluirPorCobranca(divida.getCobranca().getId());
					}
				
					this.removerDividaCobrancaConsolidado(divida, consolidado, dataOperacao);
					
				}
				
				List<MovimentoFinanceiroCota> mfcs = movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCotaDeConsolidado(consolidado.getId());
				
				consolidado.setMovimentos(null);
				
				this.consolidadoFinanceiroRepository.alterar(consolidado);
				
                if(excluiFinanceiro && mfcs != null && !mfcs.isEmpty()) {
			    	this.movimentoFinanceiroCotaService.removerMovimentosFinanceirosCota(consolidado.getId());
				}
                
                this.consolidadoFinanceiroRepository.remover(consolidado);
			    
			}
		} else {
		    //caso esteja finalizando conf. de uma cota centralizada/centralizadora
		    this.movimentoFinanceiroCotaService.removerMovimentosFinanceirosCotaPorDataCota(dataOperacao, idCota);
		}
		
		this.removerPostergados(idCota, dataOperacao);
	}
	
	private void removerDividaCobrancaConsolidado(Divida divida, ConsolidadoFinanceiroCota consolidado,
			Date dataOperacao){
		
		Cobranca cobranca = divida.getCobranca();
		
		if (cobranca != null){
		    
			this.excluirBoletoEmailAssociacao(cobranca.getId());
			
		}
		
		if(cobranca.getTipoCobranca().equals(TipoCobranca.BOLETO_AVULSO)) {			
			LOGGER.debug("Não remover quando for Boleto Avulso");
		} else {
			this.dividaRepository.remover(divida);
		}
		
		
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
	
	private BigDecimal adicionarValor(BigDecimal valor, MovimentoFinanceiroCota movimentoFinanceiroCota) {
		
		if (movimentoFinanceiroCota.getValor() == null) {
			
			return BigDecimal.ZERO;
		}
		
		GrupoMovimentoFinaceiro grupoMovimentoFinaceiro = ((TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento()).getGrupoMovimentoFinaceiro();
		
		if (OperacaoFinaceira.CREDITO.equals(grupoMovimentoFinaceiro.getOperacaoFinaceira())) {

			return MathUtil.defaultRound(valor.add(movimentoFinanceiroCota.getValor()));
		}

		return MathUtil.defaultRound(valor.add(movimentoFinanceiroCota.getValor().negate()));
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

    @Override
	@Transactional(readOnly=true)
	public boolean aceitaEmissaoDocumento(Long idCota, TipoEmissaoDocumento tipoEmissaoDocumento) {
        
        Cota cota = cotaRepository.buscarPorId(idCota);
        
        return aceitaEmissaoDocumento(cota, tipoEmissaoDocumento);
		
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

	@Override
	@Transactional
	public void gerarCobrancaBoletoAvulso(Long idUsuario, MovimentoFinanceiroCota movimentoFinanceiroCota, Map<Integer, Long> cotasBanco) throws GerarCobrancaValidacaoException {
		
		Set<String> setNossoNumero = null;
		
		Date dataOperacao = movimentoFinanceiroCota.getDataIntegracao();
		
		Usuario usuario = this.usuarioRepository.buscarPorId(idUsuario);
		
		Integer numeroDiasNovaCobranca = 0; 
		
		List<String> msgs = new ArrayList<String>();
		
		Map<Cota, List<GerarCobrancaHelper>> consolidadosCotaCentralizacao = new HashMap<Cota, List<GerarCobrancaHelper>>();
		
		Fornecedor fornecedorAnterior = movimentoFinanceiroCota.getFornecedor();
			
		Fornecedor fornecedorAtual = null;
			
		Cota cotaAnterior = cotaRepository.buscarPorId(movimentoFinanceiroCota.getCota().getId());
			
		BigDecimal valorMovimentos = BigDecimal.ZERO;
			
		List<MovimentoFinanceiroCota> movimentos = new ArrayList<MovimentoFinanceiroCota>();
			
		Cota cotaAtual = cotaRepository.buscarPorId(movimentoFinanceiroCota.getCota().getId());
							    
		fornecedorAtual = movimentoFinanceiroCota.getFornecedor();

		if (fornecedorAtual == null) {
			    	
	    	throw new GerarCobrancaValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
			    			                                                  "Fornecedor não encontrado para o [Movimento Financeiro " +
			    			                                                  movimentoFinanceiroCota.getId() + "] [Cota " + 
			    			                                                  cotaAtual.getNumeroCota() + "]."));
	    }
				
		TipoMovimentoFinanceiro tipo = (TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento();
		
		valorMovimentos = valorMovimentos.add(movimentoFinanceiroCota.getValor());
			
		fornecedorAnterior = fornecedorAtual;
			
		movimentos = new ArrayList<MovimentoFinanceiroCota>();
			
		movimentos.add(movimentoFinanceiroCota);
			
		if (tipo.getOperacaoFinaceira().equals(OperacaoFinaceira.CREDITO)) {
			valorMovimentos = movimentoFinanceiroCota.getValor().negate();	
		} else {
	    	valorMovimentos = movimentoFinanceiroCota.getValor();
	    }
		
		this.processarConsolidadoDividaCobranca(cotaAnterior, 
												    movimentos, 
												    usuario, 
												    numeroDiasNovaCobranca, 
												    dataOperacao, 
												    msgs, 
												    fornecedorAnterior, 
												    false,
												    consolidadosCotaCentralizacao,
												    valorMovimentos, 
												    valorMovimentos,
												    setNossoNumero, 
												    true,
												    cotasBanco,
												    false);
		
	}
	
	@Override
	@Transactional
	public void gerarCobrancaBoletoNFe(Long idUsuario, MovimentoFinanceiroCota movimentoFinanceiroCota, Set<String> setNossoNumero) throws GerarCobrancaValidacaoException {
		
		//Date dataOperacao = movimentoFinanceiroCota.getData();
		
		Date dataOperacao = distribuidorRepository.obterDataOperacaoDistribuidor();
		
		Usuario usuario = this.usuarioRepository.buscarPorId(idUsuario);
		
		Integer numeroDiasNovaCobranca = 0; 
		
		List<String> msgs = new ArrayList<String>();
		
		Map<Cota, List<GerarCobrancaHelper>> consolidadosCotaCentralizacao = new HashMap<Cota, List<GerarCobrancaHelper>>();
		
		Fornecedor fornecedorAnterior = movimentoFinanceiroCota.getFornecedor();
			
		Fornecedor fornecedorAtual = null;
			
		Cota cotaAnterior = cotaRepository.buscarPorId(movimentoFinanceiroCota.getCota().getId());
			
		BigDecimal valorMovimentos = BigDecimal.ZERO;
			
		List<MovimentoFinanceiroCota> movimentos = new ArrayList<MovimentoFinanceiroCota>();
			
		Cota cotaAtual = cotaRepository.buscarPorId(movimentoFinanceiroCota.getCota().getId());
							    
		fornecedorAtual = movimentoFinanceiroCota.getFornecedor();

		if (fornecedorAtual == null) {
			    	
	    	throw new GerarCobrancaValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
			    			                                                  "Fornecedor não encontrado para o [Movimento Financeiro " +
			    			                                                  movimentoFinanceiroCota.getId() + "] [Cota " + 
			    			                                                  cotaAtual.getNumeroCota() + "]."));
	    }
				
		TipoMovimentoFinanceiro tipo = (TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento();
		
		valorMovimentos = valorMovimentos.add(movimentoFinanceiroCota.getValor());
			
		fornecedorAnterior = fornecedorAtual;
			
		movimentos = new ArrayList<MovimentoFinanceiroCota>();
			
		movimentos.add(movimentoFinanceiroCota);
			
		if (tipo.getOperacaoFinaceira().equals(OperacaoFinaceira.CREDITO)) {
			valorMovimentos = movimentoFinanceiroCota.getValor().negate();	
		} else {
	    	valorMovimentos = movimentoFinanceiroCota.getValor();
	    }
		
		this.processarConsolidadoDividaCobranca(cotaAnterior, 
												    movimentos, 
												    usuario, 
												    numeroDiasNovaCobranca, 
												    dataOperacao, 
												    msgs, 
												    fornecedorAnterior, 
												    false,
												    consolidadosCotaCentralizacao,
												    valorMovimentos, 
												    valorMovimentos,
												    setNossoNumero, 
												    false,
												    null,
												    true);
		
	}
	
}
