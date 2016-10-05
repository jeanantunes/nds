package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.assembler.HistoricoTitularidadeCotaDTOAssembler;
import br.com.abril.nds.client.vo.ContratoVO;
import br.com.abril.nds.client.vo.FormaCobrancaDefaultVO;
import br.com.abril.nds.dto.ContratoTransporteDTO;
import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaCotaDTO;
import br.com.abril.nds.dto.PessoaContratoDTO;
import br.com.abril.nds.dto.PessoaContratoDTO.TipoPessoa;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.ContaBancariaDeposito;
import br.com.abril.nds.model.cadastro.ContratoCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaUnificacao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCota;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFinanceiro;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaFormaPagamento;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.ConcentracaoCobrancaCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.CotaUnificacaoRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.ParametroCobrancaCotaRepository;
import br.com.abril.nds.service.ContratoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FileService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.vo.ValidacaoVO;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class ParametroCobrancaCotaServiceImpl implements ParametroCobrancaCotaService {
		
	private static final Logger LOGGER = LoggerFactory.getLogger(ParametroCobrancaCotaServiceImpl.class);
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private CotaService cotaService;
		
	@Autowired
	private BancoRepository bancoRepository;
	
	@Autowired
	private FormaCobrancaRepository formaCobrancaRepository;
	
	@Autowired
	private FormaCobrancaService formaCobrancaService;
	
	@Autowired
	private ParametroCobrancaCotaRepository parametroCobrancaCotaRepository;
	
	@Autowired
	private ConcentracaoCobrancaCotaRepository concentracaoCobrancaRepository;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ContratoService contratoService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
	@Autowired
	private CotaUnificacaoRepository cotaUnificacaoRepository;
	
	/**
	 * Método responsável por obter bancos para preencher combo da camada view
	 * @param TipoCobranca
	 * @return comboBancos: bancos cadastrados
	 */
	@Transactional(readOnly=true)
	@Override
	public List<ItemDTO<Integer, String>> getComboBancosTipoCobranca(TipoCobranca tipoCobranca) {
		List<ItemDTO<Integer,String>> comboBancos =  new ArrayList<ItemDTO<Integer,String>>();
		List<Banco> bancos = formaCobrancaRepository.obterBancosPorTipoDeCobranca(tipoCobranca);
		for (Banco itemBanco : bancos){
			comboBancos.add(new ItemDTO<Integer,String>(itemBanco.getId().intValue(), itemBanco.getNumeroBanco()+"-"+itemBanco.getNome()+" "+itemBanco.getConta()+"-"+itemBanco.getDvConta()));
		}
		return comboBancos;
	}
	
	
	
	/**
	 * Método responsável por obter tipos de cobrança para preencher combo da camada view
	 * @return comboTiposPagamento: Tipos de cobrança padrão.
	 */
	@Override
	public List<ItemDTO<TipoCobranca, String>> getComboTiposCobranca() {
		List<ItemDTO<TipoCobranca,String>> comboTiposCobranca =  new ArrayList<ItemDTO<TipoCobranca,String>>();
		for (TipoCobranca itemTipoCobranca: TipoCobranca.values()){
			comboTiposCobranca.add(new ItemDTO<TipoCobranca,String>(itemTipoCobranca, itemTipoCobranca.getDescTipoCobranca()));
		}
		return comboTiposCobranca;
	}
	
	
	
	/**
	  * Método responsável por obter combo de Fornecedores relacionados com a Cota
	  * @param idCota
	  * @return comboFornecedores: Fornecedores relacionados com a cota.
	  */
	@Override
	public List<ItemDTO<Long, String>> getComboFornecedoresCota(Long idCota) {
		List<Fornecedor> fornecedores = this.cotaService.obterFornecedoresCota(idCota);
		List<ItemDTO<Long,String>> comboFornecedores = new ArrayList<ItemDTO<Long,String>>();
		for (Fornecedor itemFornecedor: fornecedores){
			comboFornecedores.add(new ItemDTO<Long,String>(itemFornecedor.getId(), itemFornecedor.getJuridica().getRazaoSocial()));
		}
		return comboFornecedores;
	}


	
	/**
	 * Método responsável por obter os parametros de cobranca da cota
	 * @param idCota: ID da cota
	 * @return Data Transfer Object com os parametros de cobranca da cota
	 */
	@Override
	@Transactional
	public ParametroCobrancaCotaDTO obterDadosParametroCobrancaPorCota(Long idCota) {
		
		Cota cota = cotaRepository.buscarPorId(idCota);
		
		PoliticaSuspensao politicaSuspensao = null; 
		
		ParametroCobrancaCota parametroCobranca = null;
		
		boolean parametroDistribuidor = false;
		
		ParametroCobrancaCotaDTO parametroCobrancaDTO = null;
		
		if (cota != null) {
			
			Distribuidor distribuidor = distribuidorRepository.obter();
			
			List<FormaCobranca> formasCobranca = this.formaCobrancaRepository.obterFormasCobrancaCota(cota.getParametroCobranca());
			
			parametroCobrancaDTO = new ParametroCobrancaCotaDTO();
			parametroCobrancaDTO.setIdCota(cota.getId());
			parametroCobrancaDTO.setNumCota(cota.getNumeroCota());
			parametroCobrancaDTO.setContrato(cota.isPossuiContrato());
			parametroCobrancaDTO.setTipoCota(cota.getTipoCota());
			parametroCobrancaDTO.setSituacaoCadastro(cota.getSituacaoCadastro().name());
			
			if(cota.getContratoCota() != null) {
				
				parametroCobrancaDTO.setInicioContrato(cota.getContratoCota().getDataInicio());
				parametroCobrancaDTO.setTerminoContrato(cota.getContratoCota().getDataTermino());
			}
			
			parametroCobranca = cota.getParametroCobranca();
			if (parametroCobranca == null || formasCobranca == null || formasCobranca.isEmpty()) {
				
				parametroDistribuidor = true;
				FormaCobranca formaCobrancaDistribuidor = this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
				PoliticaCobranca politicaCobranca = formaCobrancaDistribuidor != null ? formaCobrancaDistribuidor.getPoliticaCobranca() : null;
				
				if(politicaCobranca != null) {
					
					if(parametroCobranca == null) {
						parametroCobranca = new ParametroCobrancaCota();
						parametroCobranca.setFormasCobrancaCota(null);
					}
					if(parametroCobranca.getFatorVencimento() == null) parametroCobranca.setFatorVencimento(politicaCobranca.getFatorVencimento());
					if(parametroCobranca.isUnificaCobranca() == null) parametroCobranca.setUnificaCobranca(politicaCobranca.isUnificaCobranca());
					if(parametroCobranca.getFornecedorPadrao() == null) parametroCobranca.setFornecedorPadrao(politicaCobranca.getFornecedorPadrao());
				}
			}

			  // ver se os parametros de cobranca estao ok
			
			if ( parametroCobranca == null ) {
				LOGGER.error("ERRO AO CARREGAR PARAMETRO DE COBRANCA PARA COTA e nem para o DISTRIBUIDOR "+idCota);
				throw new ValidacaoException(TipoMensagem.ERROR, "Parametros de Cobranca nao Cadastrado para Cota e nem para Distribuidor!!");
			}
			parametroCobrancaDTO.setIdParametroCobranca(parametroCobranca.getId());
			parametroCobrancaDTO.setFatorVencimento((parametroCobranca.getFatorVencimento()==null) ? 0 : parametroCobranca.getFatorVencimento());
			
			if (cota.getValorMinimoCobranca() != null) {
				
				parametroCobrancaDTO.setValorMinimo(CurrencyUtil.formatarValor(cota.getValorMinimoCobranca()));
			}
			
			parametroCobrancaDTO.setUnificaCobranca(parametroCobranca.isUnificaCobranca());
			parametroCobrancaDTO.setDevolveEncalhe(cota.isDevolveEncalhe() != null ? cota.isDevolveEncalhe() : true);
			parametroCobrancaDTO.setParametroDistribuidor(parametroDistribuidor);
			
			if (cota.isSugereSuspensaoDistribuidor()) {
				
				politicaSuspensao = distribuidor.getPoliticaSuspensao();	
			} else {
				
				if (cota.isSugereSuspensao()){
						
				    politicaSuspensao = cota.getPoliticaSuspensao();
				}
			}
			
			if(politicaSuspensao!=null){
				
				parametroCobrancaDTO.setVrDividasAberto(politicaSuspensao.getValor()!=null?CurrencyUtil.formatarValor(politicaSuspensao.getValor()):null);
				parametroCobrancaDTO.setQtdDividasAberto(politicaSuspensao.getNumeroAcumuloDivida());				
			}
			
			parametroCobrancaDTO.setSugereSuspensao(cota.isSugereSuspensao());
			parametroCobrancaDTO.setSugereSuspensaoDistribuidor(cota.isSugereSuspensaoDistribuidor());
			parametroCobrancaDTO.setIdFornecedor(parametroCobranca.getFornecedorPadrao()!=null?parametroCobranca.getFornecedorPadrao().getId():null);			
		}

		return parametroCobrancaDTO;
	}

	/**
	 * Método responsável por obter os dados da forma de cobranca
	 * @param idFormaCobranca: ID da forma de cobranca
	 * @return Data Transfer Object com os dados da forma de cobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobrancaDTO obterDadosFormaCobranca(Long idFormaCobranca) {
		
		FormaCobrancaDTO formaCobrancaDTO = null;
		
		FormaCobranca formaCobranca = this.formaCobrancaRepository.buscarPorId(idFormaCobranca);
		
		if (formaCobranca!=null){
			
			
			Set<ConcentracaoCobrancaCota> concentracoesCobranca=null;
			Integer diaDoMes=null;
			Integer primeiroDiaQuinzenal=null;
			Integer segundoDiaQuinzenal=null;
			
			if (formaCobranca.getTipoFormaCobranca() == TipoFormaCobranca.SEMANAL){
			    concentracoesCobranca = formaCobranca.getConcentracaoCobrancaCota();
			}
			if (formaCobranca.getTipoFormaCobranca() == TipoFormaCobranca.MENSAL){		
			    diaDoMes = (formaCobranca.getDiasDoMes().size()>0)?formaCobranca.getDiasDoMes().get(0):null;
			}
			if (formaCobranca.getTipoFormaCobranca() == TipoFormaCobranca.QUINZENAL){		
				primeiroDiaQuinzenal = (formaCobranca.getDiasDoMes().size()>0)?formaCobranca.getDiasDoMes().get(0):null;
				segundoDiaQuinzenal = (formaCobranca.getDiasDoMes().size()>1)?formaCobranca.getDiasDoMes().get(1):null;
			}

			
			formaCobrancaDTO = new FormaCobrancaDTO(); 
			formaCobrancaDTO.setIdFormaCobranca(formaCobranca.getId());
			formaCobrancaDTO.setTipoFormaCobranca(formaCobranca.getTipoFormaCobranca());
			formaCobrancaDTO.setRecebeEmail(formaCobranca.isRecebeCobrancaEmail());
	
			
			ContaBancariaDeposito contaBancaria = formaCobranca.getContaBancariaCota();
			if (contaBancaria!=null){
				formaCobrancaDTO.setNumBanco(contaBancaria.getNumeroBanco());
				formaCobrancaDTO.setNomeBanco(contaBancaria.getNomeBanco());
				formaCobrancaDTO.setAgencia(contaBancaria.getAgencia());
				formaCobrancaDTO.setAgenciaDigito(contaBancaria.getDvAgencia());
				formaCobrancaDTO.setConta(contaBancaria.getConta());
				formaCobrancaDTO.setContaDigito(contaBancaria.getDvConta());
			}
			
			
			if  (diaDoMes!=null){
			    formaCobrancaDTO.setDiaDoMes(diaDoMes);
			}
			
			if  (primeiroDiaQuinzenal!=null){
			    formaCobrancaDTO.setPrimeiroDiaQuinzenal(primeiroDiaQuinzenal);
			}
			
			if  (segundoDiaQuinzenal!=null){
			    formaCobrancaDTO.setSegundoDiaQuinzenal(segundoDiaQuinzenal);
			}
			
			
			if ((concentracoesCobranca!=null)&&(concentracoesCobranca.size() > 0)){
				for (ConcentracaoCobrancaCota itemConcentracaoCobranca:concentracoesCobranca){
					
					DiaSemana dia = itemConcentracaoCobranca.getDiaSemana();
					if (dia==DiaSemana.DOMINGO){
						formaCobrancaDTO.setDomingo(true);
					}
	
					if (dia==DiaSemana.SEGUNDA_FEIRA){
						formaCobrancaDTO.setSegunda(true);
					}    
					
					if (dia==DiaSemana.TERCA_FEIRA){
						formaCobrancaDTO.setTerca(true);
					}    
					
					if (dia==DiaSemana.QUARTA_FEIRA){
						formaCobrancaDTO.setQuarta(true);
					}
					
				    if (dia==DiaSemana.QUINTA_FEIRA){
				    	formaCobrancaDTO.setQuinta(true);
				    }    
				    
					if (dia==DiaSemana.SEXTA_FEIRA){
						formaCobrancaDTO.setSexta(true);
					}    
					
					if (dia==DiaSemana.SABADO){
						formaCobrancaDTO.setSabado(true);
					}
					
				}
			}
			

			Banco banco = formaCobranca.getBanco();
			if (banco!=null){
				formaCobrancaDTO.setIdBanco(banco.getId());
			}
			formaCobrancaDTO.setTipoCobranca(formaCobranca.getTipoCobranca());
			
			
			Set<Fornecedor> fornecedores = formaCobranca.getFornecedores();
			List<Long> fornecedoresID = new ArrayList<Long>();
			if (fornecedores!=null){
			    for(Fornecedor itemFornecedor:fornecedores){
				    fornecedoresID.add(itemFornecedor.getId());
			    }
			    formaCobrancaDTO.setFornecedoresId(fornecedoresID);
			}		
 
		}
		
		return formaCobrancaDTO;
	}

	
	
	/**
	 * Método responsável por persistir os parametros de cobranca da cota
	 * @param Dados dos parametros de cobranca da cota
	 */
	@Override
	@Transactional
	public void postarParametroCobranca(ParametroCobrancaCotaDTO parametroCobrancaDTO) {
		
		ParametroCobrancaCota parametroCobranca= null;
        PoliticaSuspensao politicaSuspensao = null;
        Cota cota = null;
		
		//COTA
		cota = cotaRepository.buscarPorId(parametroCobrancaDTO.getIdCota());

		if (cota != null) {
			

			//PARAMETROS DE COBRANCA DA COTA
			parametroCobranca = cota.getParametroCobranca();
			
			
			if (parametroCobranca == null) {
				parametroCobranca = new ParametroCobrancaCota();
			} else {
				//POLITICA DE SUSPENSAO DO PARAMETRO DE COBRANCA DA COTA
				politicaSuspensao = cota.getPoliticaSuspensao();
			}

			parametroCobranca.setFatorVencimento((int) parametroCobrancaDTO.getFatorVencimento());
			parametroCobranca.setUnificaCobranca(parametroCobrancaDTO.isUnificaCobranca());
			
			if (politicaSuspensao == null) {
				politicaSuspensao = new PoliticaSuspensao();
			}
			
			if (parametroCobrancaDTO.isSugereSuspensao()) {
				
				politicaSuspensao.setNumeroAcumuloDivida(parametroCobrancaDTO.getQtdDividasAberto());
				politicaSuspensao.setValor(CurrencyUtil.converterValor(parametroCobrancaDTO.getVrDividasAberto()));
			
			} else {
			
				politicaSuspensao.setNumeroAcumuloDivida(null);
				politicaSuspensao.setValor(null);
			}
			
			cota.setPoliticaSuspensao(politicaSuspensao);
			cota.setParametroCobranca(parametroCobranca);
			
			Fornecedor fornecedor = this.fornecedorService.obterFornecedorPorId(parametroCobrancaDTO.getIdFornecedor());
			parametroCobranca.setFornecedorPadrao(fornecedor);
			
			cota.setDevolveEncalhe(parametroCobrancaDTO.isDevolveEncalhe());
			
			cota.setParametroCobranca(parametroCobranca);
			cota.setSugereSuspensao(parametroCobrancaDTO.isSugereSuspensao());
			cota.setSugereSuspensaoDistribuidor(parametroCobrancaDTO.isSugereSuspensaoDistribuidor());
			cota.setPossuiContrato(parametroCobrancaDTO.isContrato());
			cota.setValorMinimoCobranca(CurrencyUtil.converterValor(parametroCobrancaDTO.getValorMinimo()));
						
			this.cotaRepository.merge(cota);
		}
	}
	
	

	/**
	 * Método responsável por alterar ou incluir uma forma de cobranca dos parametros de cobranca da cota
	 * @param Dados da forma de cobranca do parametro de cobranca da cota
	 */
	@Override
	@Transactional
	public void postarFormaCobranca(FormaCobrancaDTO formaCobrancaDTO) {
		
	    tratarPrimeiraFormaCobrancaEspecificaParaCota(formaCobrancaDTO);
	    	    
		FormaCobranca formaCobranca = null;
		ContaBancariaDeposito contaBancariaCota = null;
		Set<ConcentracaoCobrancaCota> concentracoesCobranca = null;
		
		Banco banco = null;
		boolean novaFormaCobranca=false;
		
		if (formaCobrancaDTO.getIdBanco()!=null) {
			
		    banco=this.bancoRepository.buscarPorId(formaCobrancaDTO.getIdBanco());
		}

		if (formaCobrancaDTO.getIdFormaCobranca() != null && !formaCobrancaDTO.isParametroDistribuidor()) {
			
			formaCobranca = this.formaCobrancaRepository.buscarPorId(formaCobrancaDTO.getIdFormaCobranca());
		}

		if (formaCobranca==null) {
			
			formaCobranca = new FormaCobranca();
			formaCobranca.setTipoFormaCobranca(formaCobrancaDTO.getTipoFormaCobranca());
			formaCobranca.setTipoCobranca(formaCobrancaDTO.getTipoCobranca());
			formaCobranca.setBanco(banco);
			formaCobranca.setRecebeCobrancaEmail(formaCobrancaDTO.isRecebeEmail());
			
			formaCobrancaRepository.adicionar(formaCobranca);
			novaFormaCobranca=true;
	    } else {
	    	
			novaFormaCobranca=false;
			
			formaCobranca.setTipoFormaCobranca(formaCobrancaDTO.getTipoFormaCobranca());
			formaCobranca.setTipoCobranca(formaCobrancaDTO.getTipoCobranca());
			formaCobranca.setBanco(banco);
			formaCobranca.setRecebeCobrancaEmail(formaCobrancaDTO.isRecebeEmail());
			
			
			concentracoesCobranca = formaCobranca.getConcentracaoCobrancaCota();
			
	        //APAGA CONCENTRACOES COBRANCA DA COTA
			if ((concentracoesCobranca != null) && (concentracoesCobranca.size() > 0)){
				
				formaCobranca.setConcentracaoCobrancaCota(null);
				for(ConcentracaoCobrancaCota itemConcentracaoCobranca:concentracoesCobranca){
					
					this.concentracaoCobrancaRepository.remover(itemConcentracaoCobranca);
				}
			}  
		}
		
		FormaCobranca formaCobrancaPrincipal = this.formaCobrancaService.obterFormaCobrancaPrincipalCota(formaCobrancaDTO.getIdCota());
			
		if(formaCobrancaPrincipal == null) {
			
			formaCobranca.setPrincipal(true);
		} else {
			
			formaCobranca.setPrincipal(false);
		}
		
		//CONCENTRACAO COBRANCA (DIAS DA SEMANA)
		concentracoesCobranca = new HashSet<ConcentracaoCobrancaCota>();
		ConcentracaoCobrancaCota concentracaoCobranca;
		if (formaCobrancaDTO.isDomingo()) {
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.DOMINGO);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		
		if (formaCobrancaDTO.isSegunda()) {
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SEGUNDA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		
		if (formaCobrancaDTO.isTerca()) {
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.TERCA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		
		if (formaCobrancaDTO.isQuarta()) {
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.QUARTA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		
		if (formaCobrancaDTO.isQuinta()) {
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.QUINTA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		
		if (formaCobrancaDTO.isSexta()) {
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SEXTA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		
		if (formaCobrancaDTO.isSabado()) {
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SABADO);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		
		if(concentracoesCobranca.size()>0) {
			
		    formaCobranca.setConcentracaoCobrancaCota(concentracoesCobranca);
		} else if (formaCobrancaDTO.getConcentracaoCobrancaCota() != null) {
			
			formaCobranca.setConcentracaoCobrancaCota(formaCobrancaDTO.getConcentracaoCobrancaCota());
		}
	
		
		if (formaCobrancaDTO.getDiasDoMes() != null) {
			
			formaCobranca.setDiasDoMes(formaCobrancaDTO.getDiasDoMes());
		} else {
			
			List<Integer> diasdoMes = new ArrayList<Integer>();
			diasdoMes.add(formaCobrancaDTO.getDiaDoMes());
			diasdoMes.add(formaCobrancaDTO.getPrimeiroDiaQuinzenal());
			diasdoMes.add(formaCobrancaDTO.getSegundoDiaQuinzenal());
			formaCobranca.setDiasDoMes(diasdoMes);
		}
		
		contaBancariaCota = formaCobranca.getContaBancariaCota();
		if(contaBancariaCota == null) {
			
			contaBancariaCota = new ContaBancariaDeposito();
		}
		
		contaBancariaCota.setNumeroBanco(formaCobrancaDTO.getNumBanco());
		contaBancariaCota.setNomeBanco(formaCobrancaDTO.getNomeBanco());
	    contaBancariaCota.setAgencia(formaCobrancaDTO.getAgencia());
		contaBancariaCota.setDvAgencia(formaCobrancaDTO.getAgenciaDigito());
		contaBancariaCota.setConta(formaCobrancaDTO.getConta());
		contaBancariaCota.setDvConta(formaCobrancaDTO.getContaDigito());
		
		formaCobranca.setContaBancariaCota(contaBancariaCota);
		
		formaCobranca.setTaxaJurosMensal((formaCobranca.getTaxaJurosMensal() == null ? BigDecimal.ZERO : formaCobranca.getTaxaJurosMensal()));
	    formaCobranca.setTaxaMulta((formaCobranca.getTaxaMulta() == null ? BigDecimal.ZERO : formaCobranca.getTaxaMulta()));
		
		formaCobranca.setAtiva(true);
		
		formaCobranca.setFornecedores(null);
		if(formaCobrancaDTO.getFornecedores() != null) {
			
			formaCobranca.setFornecedores(formaCobrancaDTO.getFornecedores());
			
		} else if ((formaCobrancaDTO.getFornecedoresId()!=null)&&(formaCobrancaDTO.getFornecedoresId().size()>0)) {
			
			Fornecedor fornecedor;
		    Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		    for (Long idFornecedor:formaCobrancaDTO.getFornecedoresId()) {
		    	
		    	fornecedor = fornecedorService.obterFornecedorPorId(idFornecedor);
		    	
		    	if (fornecedor != null) {
		    		
		    	    fornecedores.add(fornecedor);
		    	}
		    }
		    
		    if (fornecedores.size() > 0) {
		    	
			    formaCobranca.setFornecedores(fornecedores);
		    }
		}

	    if(novaFormaCobranca || formaCobrancaDTO.isParametroDistribuidor()) {

	    	FormaCobranca formaCobrancaDistribuidor = this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
			
	    	Cota c = cotaRepository.buscarPorId(formaCobrancaDTO.getIdCota());
	    		    	
	    	if(c.getParametroCobranca() == null) {	    	
		    	
	    		ParametroCobrancaCota parametroCobranca;
		    	parametroCobranca = new ParametroCobrancaCota();
		    	
				parametroCobranca.setFatorVencimento(formaCobrancaDistribuidor.getPoliticaCobranca().getFatorVencimento());
				parametroCobranca.setUnificaCobranca(formaCobrancaDistribuidor.getPoliticaCobranca().isUnificaCobranca());
				parametroCobranca.setFornecedorPadrao(formaCobrancaDistribuidor.getPoliticaCobranca().getFornecedorPadrao());
				c.setPoliticaSuspensao(null);
				c.setParametroCobranca(parametroCobranca);
				
				parametroCobrancaCotaRepository.adicionar(parametroCobranca);
				formaCobranca.setPoliticaCobranca(null);
				formaCobranca.setParametroCobrancaCota(parametroCobranca);
	    	} else {
	    		
	    		if(c.getParametroCobranca().getFormasCobrancaCota() == null) {
	    			
	    			Set<FormaCobranca> formasCobrancaCota = new HashSet<>();
	    			
	    			c.getParametroCobranca().setFormasCobrancaCota(formasCobrancaCota);
	    		}
	    		
	    		formaCobranca.setParametroCobrancaCota(c.getParametroCobranca());
			}
			
		    formaCobrancaRepository.merge(formaCobranca);
		    
	    } else {
	    	
	    	formaCobrancaRepository.merge(formaCobranca);
		}    
    }	

	@Override
	@Transactional(readOnly=true)
	public List<FormaCobrancaDefaultVO> obterFormaCobrancaCotaDefault(Integer numeroCota) {
		
		return this.formaCobrancaRepository.obterFormaCobrancaCotaDefault(numeroCota);
	}
	

	private void tratarPrimeiraFormaCobrancaEspecificaParaCota(FormaCobrancaDTO formaCobrancaDTO) {
       
       if( !isCriacaoPrimeiraFormaCobrancaEspecificaParaCota(formaCobrancaDTO) )
           return;
       
       copiarFormaCobrancaDoDistribuidorParaACota(formaCobrancaDTO.getIdCota());       
       
       this.validarFormaCobranca(formaCobrancaDTO);
   }

	
    private void copiarFormaCobrancaDoDistribuidorParaACota(Long idCota) {
        
        FormaCobranca fc = this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidorCompleto();
        
        FormaCobranca copia = new FormaCobranca();
        copia.setAtiva(true);
        copia.setBanco(fc.getBanco());
        copia.setContaBancariaCota(fc.getContaBancariaCota());
        copia.setDiasDoMes(fc.getDiasDoMes());
        copia.setFormaCobrancaBoleto(fc.getFormaCobrancaBoleto());
        copia.setFornecedores(fc.getFornecedores());
        copia.setInstrucoes(fc.getInstrucoes());
        copia.setParametroCobrancaCota(fc.getParametroCobrancaCota());
        copia.setPoliticaCobranca(fc.getPoliticaCobranca());
        copia.setPrincipal(true);
        copia.setRecebeCobrancaEmail(fc.isRecebeCobrancaEmail());
        copia.setTaxaJurosMensal(fc.getTaxaJurosMensal());
        copia.setTaxaMulta(fc.getTaxaMulta());
        copia.setTipoCobranca(fc.getTipoCobranca());
        copia.setTipoFormaCobranca(fc.getTipoFormaCobranca());
        copia.setValorMulta(fc.getValorMulta());
        copia.setVencimentoDiaUtil(fc.isVencimentoDiaUtil());
        
        Cota cota = cotaRepository.buscarCotaPorID(idCota);
        ParametroCobrancaCota parametro = cota.getParametroCobranca();
        copia.setParametroCobrancaCota(parametro);
        
        copia = formaCobrancaRepository.merge(copia);
        
        Set<ConcentracaoCobrancaCota> listaCCC = new HashSet<>();
        for (ConcentracaoCobrancaCota conc : fc.getConcentracaoCobrancaCota()) {
            ConcentracaoCobrancaCota ccc = new ConcentracaoCobrancaCota();
            ccc.setDiaSemana(conc.getDiaSemana());
            ccc.setFormaCobranca(copia);
            
            concentracaoCobrancaRepository.adicionar(ccc);
            listaCCC.add(ccc);
        }
        copia.setConcentracaoCobrancaCota(listaCCC);
    }



    private boolean isCriacaoPrimeiraFormaCobrancaEspecificaParaCota(FormaCobrancaDTO formaCobrancaDTO) {
        
        return formaCobrancaDTO.getIdFormaCobranca() == null && formaCobrancaDTO.isParametroDistribuidor() == true;
    }
    
    
    
    /**
    * Obtém Formas de Cobrança da cota
    * @param idCota: ID da cota
    * @return Formas de cobrança da Cota
    */
	@Override
	@Transactional
	public List<FormaCobrancaDTO> obterDadosFormasCobrancaPorCota(Long idCota) {
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		List<FormaCobranca> formasCobranca = this.formaCobrancaRepository.obterFormasCobrancaCota(cota.getParametroCobranca());
		
		List<FormaCobrancaDTO> formasCobrancaDTO = new LinkedList<FormaCobrancaDTO>();
		
		if (formasCobranca != null && !formasCobranca.isEmpty()){
			
			if (this.cotaUnificacaoRepository.verificarCotaUnificadora(cota.getNumeroCota())){
				
				this.criarDTODadosFormasCobrancas(formasCobrancaDTO, formasCobranca, false, "Unificadora");
			} else {
				
				this.criarDTODadosFormasCobrancas(formasCobrancaDTO, formasCobranca, false, null);
			}
		}
		
		List<CotaUnificacao> unis = this.cotaUnificacaoRepository.obterCotaUnificacaoPorCotaUnificada(cota.getNumeroCota());
		
		if (unis != null && !unis.isEmpty()) {
			
			for (CotaUnificacao unificacao : unis) {
				
				List<FormaCobranca> lis = this.formaCobrancaRepository.obterFormasCobrancaCota(unificacao.getCota().getParametroCobranca());
				
				boolean paramDistrib = false;
				if (lis == null || lis.isEmpty()) {
					
					lis = new ArrayList<>();
					FormaCobranca formaCobrancaDistribuidor = this.formaCobrancaRepository.obterFormaCobranca();
					lis.add(formaCobrancaDistribuidor);
					paramDistrib = true;
				}
				
				this.criarDTODadosFormasCobrancas(formasCobrancaDTO, lis, paramDistrib, "Unificada na cota " + unificacao.getCota().getNumeroCota());
			}
		}
		
		// caso não encontre as formas de cobrança... é utilizado a forma de cobrança PRINCIPAL do Distribuidor
		if (formasCobrancaDTO.isEmpty()) {
			
			if (formasCobranca == null || formasCobranca.size() == 0) {
				formasCobranca = new ArrayList<FormaCobranca>();
				FormaCobranca formaCobrancaDistribuidor = this.formaCobrancaRepository.obterFormaCobranca();
				if (formaCobrancaDistribuidor !=null) {
					formasCobranca.add(formaCobrancaDistribuidor);
				}
			}
			
			if (this.cotaUnificacaoRepository.verificarCotaUnificadora(cota.getNumeroCota())) {
				
				this.criarDTODadosFormasCobrancas(formasCobrancaDTO, formasCobranca, true, "Unificadora");
			} else {
			
				CotaUnificacao c = this.cotaUnificacaoRepository.obterCotaUnificacaoPorCotaCentralizadora(cota.getNumeroCota());
				
				if (c != null) {
					
					this.criarDTODadosFormasCobrancas(formasCobrancaDTO, formasCobranca, true, "Unificada na cota " + c.getCota().getNumeroCota());
				} else {
				
					this.criarDTODadosFormasCobrancas(formasCobrancaDTO, formasCobranca, true, null);
				}
			}
		}
		
		return formasCobrancaDTO;
	}

	private void criarDTODadosFormasCobrancas(List<FormaCobrancaDTO> formasCobrancaDTO,
			List<FormaCobranca> formasCobranca, boolean isParametroDistribuidor,
			String descUnificacao){
		
		StringBuilder strConcentracoes;
		StringBuilder strFornecedores;
		Set<ConcentracaoCobrancaCota> concentracoes;
		Set<Fornecedor> fornecedores;
		
		for(FormaCobranca formaCobrancaItem:formasCobranca){
			
			strConcentracoes = new StringBuilder();
			strFornecedores = new StringBuilder();
			
			concentracoes = formaCobrancaItem.getConcentracaoCobrancaCota();
			fornecedores = formaCobrancaItem.getFornecedores();
			
			if(formaCobrancaItem.getTipoFormaCobranca()==TipoFormaCobranca.SEMANAL){
				if ((concentracoes!=null)&&(!concentracoes.isEmpty())){
					for (ConcentracaoCobrancaCota itemConcentracao:concentracoes){
						if (strConcentracoes.length()>0){
							strConcentracoes.append("/");
						}
						strConcentracoes .append(itemConcentracao.getDiaSemana().getDescricaoDiaSemana());
					}
				}				
			}
			else if (formaCobrancaItem.getTipoFormaCobranca()==TipoFormaCobranca.MENSAL){
				strConcentracoes .append("Todo dia ").append(formaCobrancaItem.getDiasDoMes().get(0));
			}	
			else if (formaCobrancaItem.getTipoFormaCobranca()==TipoFormaCobranca.QUINZENAL){
				strConcentracoes .append("Todo dia ").append(formaCobrancaItem.getDiasDoMes().get(0));
				strConcentracoes .append(" e ").append(formaCobrancaItem.getDiasDoMes().get(1));
			}
			else if (formaCobrancaItem.getTipoFormaCobranca()==TipoFormaCobranca.DIARIA){
				strConcentracoes .append("Diariamente");
			}
			
			final String detalhesTipoPagto = this.obterDetalheTipoPagamento(formaCobrancaItem);
			

			FormaCobrancaDTO formaCobranca = new FormaCobrancaDTO(
				formaCobrancaItem.getId(),
				strFornecedores.toString(),
				strConcentracoes.toString(),
				(formaCobrancaItem.getTipoCobranca()!=null?formaCobrancaItem.getTipoCobranca().getDescTipoCobranca():""),
				detalhesTipoPagto,
				isParametroDistribuidor,
				descUnificacao
			);

			if ((fornecedores!=null)&&(!fornecedores.isEmpty())){
				for (Fornecedor itemFornecedor:fornecedores){
					formaCobranca.addFornecedor(new Fornecedor(itemFornecedor.getId(), itemFornecedor.getJuridica().getNomeFantasia()));
				}
				formaCobranca.getFornecedores().size();
			}
			
			formasCobrancaDTO.add(formaCobranca);
		}
	}

	private String obterDetalheTipoPagamento(final FormaCobranca formaCobrancaItem) {
		
		String detalhesTipoPagto = "";
		
		if(formaCobrancaItem.getBanco()!= null){
			
			detalhesTipoPagto = (formaCobrancaItem.getBanco().getDvConta() != null)
					? String.format("%s : %s : %s - %s",
									formaCobrancaItem.getBanco().getNome(), 
									formaCobrancaItem.getBanco().getAgencia(), 
									formaCobrancaItem.getBanco().getConta(),
				                    formaCobrancaItem.getBanco().getDvConta())
		            
				                    : String.format("%s : %s : %s",
						    						formaCobrancaItem.getBanco().getNome(), 
						    						formaCobrancaItem.getBanco().getAgencia(), 
						    						formaCobrancaItem.getBanco().getConta());                    
		}
		
		return detalhesTipoPagto;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.FinanceiroService#obtemContratoTransporte(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public ContratoTransporteDTO obtemContratoTransporte(long idCota){
		
		ContratoTransporteDTO contrato = new ContratoTransporteDTO();
		
		PessoaJuridica pessoaJuridica = this.distribuidorRepository.juridica();		
		PessoaContratoDTO contratante = new PessoaContratoDTO();		
		
		contratante.setNome((pessoaJuridica.getNome()!=null?pessoaJuridica.getNome():""));
		contratante.setDocumento((pessoaJuridica.getDocumento()!=null?pessoaJuridica.getDocumento():""));
		contratante.setTipoPessoa(TipoPessoa.JURIDICA);
		
		Endereco endereco = distribuidorRepository.obterEnderecoPrincipal().getEndereco();
		String descEndereco="";
		if (endereco!=null){
			descEndereco = descEndereco + 
					       endereco.getLogradouro()+", "+
					       endereco.getNumero()+" - "+
					       endereco.getBairro()+" - "+
					       endereco.getCidade()+"-"+
					       endereco.getUf();
		}
		
		contratante.setDescEndereco(descEndereco);
		contratante.setDescEnderecoGestor(descEndereco);
		
		
		String descTelefones = "";
		for (Telefone itemTelefone: pessoaJuridica.getTelefones()){
			descTelefones=descTelefones+"("+itemTelefone.getDdd()+")"+itemTelefone.getNumero()+"    ";
		}
		contratante.setDescTelefones(descTelefones);
		
		contratante.setNomeGestor((pessoaJuridica.getNome()!=null?pessoaJuridica.getNome():""));
		contratante.setEmailGestor((pessoaJuridica.getEmail()!=null?pessoaJuridica.getEmail():""));
		
		contrato.setContratante(contratante);
		

		Cota cota = cotaRepository.buscarPorId(idCota);	
		if (cota!=null) {
			
			PessoaContratoDTO contratada = new PessoaContratoDTO();
			
			contratada.setNome((cota.getPessoa().getNome()!=null?cota.getPessoa().getNome():""));
			contratada.setDocumento((cota.getPessoa().getDocumento()!=null?cota.getPessoa().getDocumento():""));

			contratante.setTipoPessoa((cota.getPessoa()instanceof PessoaJuridica)?TipoPessoa.JURIDICA:TipoPessoa.FISICA);

			
			EnderecoCota enderecoCota = cotaRepository.obterEnderecoPrincipal(cota.getId());
			if (enderecoCota != null){
				
				endereco = enderecoCota.getEndereco();
				
				descEndereco="";
				
				if (endereco!=null){
					
					descEndereco = descEndereco + 
							       endereco.getLogradouro()+", "+
							       endereco.getNumero()+" - "+
							       endereco.getBairro()+" - "+
							       endereco.getCidade()+"-"+
							       endereco.getUf();
				}
			}
			
			contratada.setDescEndereco(descEndereco);
			contratada.setDescEnderecoGestor(descEndereco);
			
			
			descTelefones = "";
			for (Telefone itemTelefone:cota.getPessoa().getTelefones()){
				descTelefones=descTelefones+"("+itemTelefone.getDdd()+")"+itemTelefone.getNumero()+"    ";
			}
			contratada.setDescTelefones(descTelefones);
			
			contratada.setNomeGestor((pessoaJuridica.getNome()!=null?pessoaJuridica.getNome():""));
			contratada.setEmailGestor((pessoaJuridica.getEmail()!=null?pessoaJuridica.getEmail():""));
			
			contrato.setContratada(contratada);
			
			
			ContratoCota contratoCota = cota.getContratoCota();
			if (contratoCota!=null){
				contrato.setPrazo(cota.getContratoCota().getPrazo()!=null?cota.getContratoCota().getPrazo().toString():"");
			}
			
			ParametroContratoCota parametroContrato = this.distribuidorRepository.parametroContratoCota();
			if (parametroContrato != null) {
				contrato.setAvisoPrevio( Integer.toString(parametroContrato.getDiasAvisoRescisao()));
			    contrato.setComplemento(parametroContrato.getComplementoContrato());
			    contrato.setCondicoes(parametroContrato.getCondicoesContratacao());
			}
			
			
		}else{
			throw new IllegalArgumentException("Id da cota não cadastrada");
		}

		return contrato;
	}

	

	/**
	 * Gera um relatório à partir de um Objeto com atributos e listas definidas
	 * @param list
	 * @param pathJasper
	 * @return Array de bytes do relatório gerado
	 * @throws JRException
	 * @throws URISyntaxException
	 */
	private byte[] gerarDocumentoIreport(List<ContratoTransporteDTO> list, String pathJasper) throws JRException, URISyntaxException{

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
		
		URL url = Thread.currentThread().getContextClassLoader().getResource(pathJasper);
		
		String path = url.toURI().getPath();
		
		return  JasperRunManager.runReportToPdf(path, null, jrDataSource);
	}
	
	@Override
	@Transactional
	public byte[] geraImpressaoContrato(Long idCota, Date dataInicio, Date dataTermino){
		
		byte[] relatorio=null;

		ContratoTransporteDTO contratoDTO = this.obtemContratoTransporte(idCota);
		contratoDTO.setInicio(dataInicio);
		contratoDTO.setTermino(dataTermino);
		List<ContratoTransporteDTO> listaContratos = new ArrayList<ContratoTransporteDTO>();
		listaContratos.add(contratoDTO);
		
		try{
		    relatorio = this.gerarDocumentoIreport(listaContratos, "/reports/contratoTransporte.jasper");
		}
		catch(Exception e){
			throw new ValidacaoException(TipoMensagem.WARNING, "Erro ao gerar Contrato.");
		}
		return relatorio;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.ParametroCobrancaCotaService#salvarContrato(java.lang.Long, boolean, java.util.Date, java.util.Date)
	 */
	@Transactional
	public void salvarContrato(Long idCota, boolean isRecebido, Date dataInicio, Date dataTermino) {
		
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		
		ContratoTransporteDTO contratoDTO = this.obtemContratoTransporte(idCota);
		contratoDTO.setInicio(dataInicio);
		contratoDTO.setTermino(dataTermino);
		
		ContratoCota contrato = cota.getContratoCota();
		
		contrato = (contrato == null)?new ContratoCota():contrato;
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.setTime(contratoDTO.getInicio());
		c2.setTime(contratoDTO.getTermino());
		
		int prazoEmMeses = (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12;     
        prazoEmMeses = prazoEmMeses + ((12 - (c1.get(Calendar.MONTH)) + (c2.get(Calendar.MONTH)-12)));     
        		
		contrato.setAvisoPrevioRescisao(contratoDTO.getAvisoPrevio()!=null?Integer.parseInt(contratoDTO.getAvisoPrevio()):0);
		contrato.setDataInicio(contratoDTO.getInicio());
		contrato.setDataTermino(contratoDTO.getTermino());
		contrato.setExigeDocumentacaoSuspencao(cota.isSugereSuspensao());
		contrato.setPrazo(prazoEmMeses);
		contrato.setRecebido(isRecebido);

		cota.setPossuiContrato(true);
		cota.setContratoCota(contrato);
		
		this.cotaRepository.merge(cota);
	}

	@Transactional
	public ContratoVO obterArquivoContratoRecebido(Long idCota, File tempDir) {
		
		ContratoVO contratoVO = null;
		
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		
		ContratoCota contrato = cota.getContratoCota();
		
		if (contrato != null) {
			
			contratoVO = new ContratoVO();
			contratoVO.setIdCota(idCota);
			contratoVO.setDataTermino(contrato.getDataTermino());
			contratoVO.setDataInicio(contrato.getDataInicio());
			contratoVO.setRecebido(contrato.isRecebido());
			
			this.fileService.limparDiretorio(tempDir);
			
			if (contrato.isRecebido()) {
			
			//TODO: obter arquivo contrato recebido;
			
			ParametroSistema pathContrato = 
					this.parametroSistemaService.buscarParametroPorTipoParametro(
							TipoParametroSistema.PATH_IMPORTACAO_CONTRATO);
			
			File diretorioContrato = new File(pathContrato.getValor(), cota.getNumeroCota().toString());
			
			File contratoRecebido = null;
			
			if (diretorioContrato.exists()) {
				
				File[] files = diretorioContrato.listFiles();
				
				contratoRecebido = files[0];
			}
			
			if (contratoRecebido==null){
				
				return null;
			}
			
			File arquivoTemporario = new File(tempDir, contratoRecebido.getName());
			
			File arquivoContrato = new File(diretorioContrato, contratoRecebido.getName());
			
			try {
								
				FileOutputStream fos = new FileOutputStream(arquivoTemporario);
				
				FileInputStream fis = new FileInputStream(arquivoContrato);
							
				IOUtils.copyLarge(fis, fos);

				fis.close();
				
				fos.close();
				
			} catch (IOException e) {
				
				LOGGER.error("Falha ao persistir contrato anexo", e);
				
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Falha ao persistir contrato anexo"));
			}
			
			contratoVO.setTempFile(arquivoTemporario);
			}
		}
		
		return contratoVO;
	}

	
	@Override
	@Transactional(readOnly = true)
	public List<FormaCobranca> obterFormasCobrancaCota(Long idCota) {
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		return this.formaCobrancaRepository.obterFormasCobrancaCota(cota.getParametroCobranca());
	}



	@Override
	@Transactional(readOnly = true)
	public int obterQuantidadeFormasCobrancaCota(Long idCota) {
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		return this.formaCobrancaRepository.obterQuantidadeFormasCobrancaCota(cota);
	}


	
	@Override
	@Transactional
	public void excluirFormaCobranca(Long idFormaCobranca) {
		this.formaCobrancaRepository.desativarFormaCobranca(idFormaCobranca);
	}

    /**
     * {@inheritDoc}
     * 
     **/
	@Transactional(readOnly = true)
	@Override
	public ParametroCobrancaCotaDTO obterParametrosCobrancaHistoricoTitularidadeCota(Long idCota, Long idHistorico) {
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!"); 
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        return HistoricoTitularidadeCotaDTOAssembler.toParametroCobrancaCotaDTO(historico.getFinanceiro());
    }


    /**
     * {@inheritDoc}
     */
	@Transactional(readOnly = true)
	@Override
    public List<FormaCobrancaDTO> obterFormasCobrancaHistoricoTitularidadeCota(Long idCota, Long idHistorico) {
        Validate.notNull(idHistorico, "Identificador do Histórico não deve ser nulo!"); 
        
        HistoricoTitularidadeCota historico = cotaRepository.obterHistoricoTitularidade(idCota, idHistorico);
        HistoricoTitularidadeCotaFinanceiro financeiro = historico.getFinanceiro();
        
        List<FormaCobrancaDTO> empty = Collections.emptyList();
        return financeiro == null ? empty : new ArrayList<FormaCobrancaDTO>(
                HistoricoTitularidadeCotaDTOAssembler.toFormaCobrancaDTOCollection(financeiro
                        .getFormasPagamento()));
    }

    /**
     * {@inheritDoc}
     */
	@Override
	@Transactional(readOnly = true)
    public List<FornecedorDTO> obterFornecedoresFormaPagamentoHistoricoTitularidade(Long idFormaPagto) {
        Validate.notNull(idFormaPagto, "Identificador da Forma de Pagamento não deve ser nulo!");
        
        HistoricoTitularidadeCotaFormaPagamento formaPagto = cotaRepository.obterFormaPagamentoHistoricoTitularidade(idFormaPagto);
        return new ArrayList<FornecedorDTO>(HistoricoTitularidadeCotaDTOAssembler.toFornecedorDTOCollection(formaPagto.getFornecedores()));
    }

    /**
     * {@inheritDoc}
     */
	@Override
	@Transactional(readOnly = true)
    public FormaCobrancaDTO obterFormaPagamentoHistoricoTitularidade(
            Long idFormaPagto) {
	    Validate.notNull(idFormaPagto, "Identificador da Forma de Pagamento não deve ser nulo!");
	    
	    HistoricoTitularidadeCotaFormaPagamento formaPagto = cotaRepository.obterFormaPagamentoHistoricoTitularidade(idFormaPagto);
        return HistoricoTitularidadeCotaDTOAssembler.toFormaCobrancaDTO(formaPagto);
    }
	
	@Override
	@Transactional
	public void alterarParametro(ParametroCobrancaCota parametroCobrancaCota){
		this.parametroCobrancaCotaRepository.merge(parametroCobrancaCota);
	}
	
	@Transactional(readOnly = true)
	public List<BigDecimal> comboValoresMinimos() {
		List<BigDecimal> valoresMinimos = new ArrayList<>();
		for(BigDecimal valorMinimo : this.parametroCobrancaCotaRepository.comboValoresMinimos()) {
			valoresMinimos.add(CurrencyUtil.arredondarValorParaDuasCasas(valorMinimo));
		}
		return valoresMinimos;
	}

	/**
	 * Remove parametro de cobranca da cota e suas formas de cobranca
	 * @param formaCobrancaId
	 */
	@Override
	@Transactional
	public void excluirParametroCobrancaCota(Long formaCobrancaId) {
		
		FormaCobranca fc = this.formaCobrancaRepository.buscarPorId(formaCobrancaId);

		if (fc == null) {
			
			return;
		}
		
		ParametroCobrancaCota pcc = fc.getParametroCobrancaCota();
		Cota cota = parametroCobrancaCotaRepository.obterCotaPorParametroCobranca(pcc);
		
		if (pcc == null) {
			
			return;
		}
		
		if (this.obterQuantidadeFormasCobrancaCota(cota.getId()) == 0) {
			
			Set<FormaCobranca> fcs = pcc.getFormasCobrancaCota();
			
			for (FormaCobranca formaCobranca : fcs){
				
				formaCobranca.setFornecedores(null);
				
				this.formaCobrancaRepository.remover(formaCobranca);
			}
			
			cota.setParametroCobranca(null);
			this.parametroCobrancaCotaRepository.remover(pcc);
		}
	}

	@Override
	@Transactional
	public void inserirFormaCobrancaDoDistribuidorNaCota(ParametroCobrancaCotaDTO parametroCobranca) {

		FormaCobranca formaCobrancaDistribuidor = this.formaCobrancaService.obterFormaCobrancaPrincipalDistribuidor();
		
		Cota cota = cotaRepository.buscarPorId(parametroCobranca.getIdCota());
		
		FormaCobranca formaCobranca = new FormaCobranca();
		formaCobranca.setAtiva(true);
		formaCobranca.setBanco(formaCobrancaDistribuidor.getBanco());
		
		Set<ConcentracaoCobrancaCota> listaCCC = new HashSet<>();
		for(ConcentracaoCobrancaCota conc : formaCobrancaDistribuidor.getConcentracaoCobrancaCota()) {
			ConcentracaoCobrancaCota ccc = new ConcentracaoCobrancaCota();
			ccc.setDiaSemana(conc.getDiaSemana());
			ccc.setFormaCobranca(formaCobranca);
			concentracaoCobrancaRepository.adicionar(ccc);
			listaCCC.add(ccc);
		}
		
		formaCobranca.setConcentracaoCobrancaCota(listaCCC);
		formaCobranca.setContaBancariaCota(formaCobrancaDistribuidor.getContaBancariaCota());
		
		List<Integer> dias = new ArrayList<>();
		dias.addAll(formaCobrancaDistribuidor.getDiasDoMes());
		formaCobranca.setDiasDoMes(dias);
		
		formaCobranca.setFormaCobrancaBoleto(formaCobrancaDistribuidor.getFormaCobrancaBoleto());
		
		Set<Fornecedor> fornecs = new HashSet<>();
		fornecs.addAll(formaCobrancaDistribuidor.getFornecedores());
		
		formaCobranca.setFornecedores(fornecs);
		formaCobranca.setInstrucoes(formaCobrancaDistribuidor.getInstrucoes());
		
		if(cota.getParametroCobranca() == null) {
			
			ParametroCobrancaCota pcc = new ParametroCobrancaCota();
		
			pcc.setFatorVencimento(Integer.valueOf(""+ parametroCobranca.getFatorVencimento()));
			pcc.setFornecedorPadrao(fornecedorService.obterFornecedorPorId(parametroCobranca.getIdFornecedor()));
			pcc.setUnificaCobranca(parametroCobranca.isUnificaCobranca());
			
			PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
			
			politicaSuspensao.setNumeroAcumuloDivida(parametroCobranca.getQtdDividasAberto());
			politicaSuspensao.setValor(CurrencyUtil.converterValor(parametroCobranca.getVrDividasAberto()));
			
			cota.setPoliticaSuspensao(politicaSuspensao);
			cota.setParametroCobranca(pcc);
			
			cota.setParametroCobranca(pcc);
			
			parametroCobrancaCotaRepository.adicionar();
		} else {
			
			cota.getParametroCobranca().setFatorVencimento(Integer.valueOf(""+ parametroCobranca.getFatorVencimento()));
			cota.getParametroCobranca().setFornecedorPadrao(fornecedorService.obterFornecedorPorId(parametroCobranca.getIdFornecedor()));
		}
		
		formaCobranca.setParametroCobrancaCota(cota.getParametroCobranca());
		
		cota.setValorMinimoCobranca(CurrencyUtil.converterValor(parametroCobranca.getValorMinimo()));
		
		this.cotaRepository.merge(cota);
		
		formaCobranca.setPrincipal(true);
		formaCobranca.setRecebeCobrancaEmail(formaCobrancaDistribuidor.isRecebeCobrancaEmail());
		formaCobranca.setTaxaJurosMensal(formaCobrancaDistribuidor.getTaxaJurosMensal());
		formaCobranca.setTaxaMulta(formaCobrancaDistribuidor.getTaxaMulta());
		formaCobranca.setValorMulta(formaCobrancaDistribuidor.getValorMulta());
		formaCobranca.setTipoCobranca(formaCobrancaDistribuidor.getTipoCobranca());
		formaCobranca.setTipoFormaCobranca(formaCobrancaDistribuidor.getTipoFormaCobranca());
		formaCobranca.setVencimentoDiaUtil(formaCobrancaDistribuidor.isVencimentoDiaUtil());
		
		formaCobrancaRepository.adicionar(formaCobranca);
		cotaRepository.merge(cota); // Odemir
	}
	

    /**
     * Método responsável pela validação dos dados da Forma de Cobranca.
     * 
     * @param formaCobranca
     */
    @Override
    public void validarFormaCobranca(final FormaCobrancaDTO formaCobranca){
        
        //validar();
        
        if(formaCobranca.getTipoCobranca()==null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Escolha um Tipo de Pagamento.");
        }
        
        if (formaCobranca.getTipoFormaCobranca()==null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Selecione um tipo de concentração de Pagamentos.");
        }
        
        if(formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.MENSAL){
            if (formaCobranca.getDiaDoMes()==null){
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Para o tipo de cobrança Mensal é necessário informar o dia do mês.");
            }
            else{
                if ((formaCobranca.getDiaDoMes()>31)||(formaCobranca.getDiaDoMes()<1)){
                    throw new ValidacaoException(TipoMensagem.WARNING, "Dia do mês inválido.");
                }
            }
        }
        
        if(formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.QUINZENAL){
            if ((formaCobranca.getPrimeiroDiaQuinzenal()==null) || (formaCobranca.getSegundoDiaQuinzenal()==null)){
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Para o tipo de cobrança Quinzenal é necessário informar dois dias do mês.");
            }
            else{
                if ((formaCobranca.getPrimeiroDiaQuinzenal()>31)||(formaCobranca.getPrimeiroDiaQuinzenal()<1)||(formaCobranca.getSegundoDiaQuinzenal()>31)||(formaCobranca.getSegundoDiaQuinzenal()<1)){
                    throw new ValidacaoException(TipoMensagem.WARNING, "Dia do mês inválido.");
                }
            }
        }
        
        if(formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.SEMANAL){
            if((!formaCobranca.isDomingo())&&
                    (!formaCobranca.isSegunda())&&
                    (!formaCobranca.isTerca())&&
                    (!formaCobranca.isQuarta())&&
                    (!formaCobranca.isQuinta())&&
                    (!formaCobranca.isSexta())&&
                    (!formaCobranca.isSabado())){
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Para o tipo de cobrança Semanal é necessário marcar ao menos um dia da semana.");
            }
        }
        
        if (formaCobranca.getIdBanco()==null){
            if ((formaCobranca.getTipoCobranca()==TipoCobranca.BOLETO)||
                    (formaCobranca.getTipoCobranca()==TipoCobranca.BOLETO_EM_BRANCO)||
                    (formaCobranca.getTipoCobranca()==TipoCobranca.CHEQUE)||
                    (formaCobranca.getTipoCobranca()==TipoCobranca.TRANSFERENCIA_BANCARIA)||
                    (formaCobranca.getTipoCobranca()==TipoCobranca.DEPOSITO)){
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Para o Tipo de Cobrança selecionado é necessário a escolha de um Banco.");
            }
        }
        
        if (formaCobranca.getTipoCobranca()==TipoCobranca.TRANSFERENCIA_BANCARIA){
            
            if (StringUtils.isEmpty(formaCobranca.getNomeBanco())) {
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Para o Tipo de Cobrança selecionado é necessário digitar o nome do Banco.");
            }
            if (StringUtils.isEmpty(formaCobranca.getNumBanco())) {
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Para o Tipo de Cobrança selecionado é necessário digitar o numero do Banco.");
            }
            
            if (formaCobranca.getConta() == null) {
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Para o Tipo de Cobrança selecionado é necessário digitar o numero da Conta.");
            }
            if (StringUtils.isEmpty(formaCobranca.getContaDigito())) {
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Para o Tipo de Cobrança selecionado é necessário digitar o dígito da Conta.");
            }
            
            if (formaCobranca.getAgencia() == null) {
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Para o Tipo de Cobrança selecionado é necessário digitar o numero da Agência.");
            }
            if (StringUtils.isEmpty(formaCobranca.getAgenciaDigito())) {
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Para o Tipo de Cobrança selecionado é necessário digitar o dígito da Agência.");
            }
        }
        
        if (formaCobranca.isRecebeEmail()){
            final Cota cota = cotaService.obterPorId(formaCobranca.getIdCota());
            if (cota.getPessoa().getEmail()==null){
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Cadastre um e-mail para a cota ou desmarque a opção de envio de email.");
            }
        }
        
        if ((formaCobranca.getFornecedoresId()!=null)&&(formaCobranca.getFornecedoresId().size()>0)){
            
            // VERIFICA SE A FORMA DE COBRANÇA JA EXISTE PARA O FORNECEDOR E DIA
            // DA CONCENTRAÇÃO SEMANAL
            if (formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.SEMANAL){
                if (!formaCobrancaService.validarFormaCobrancaSemanal(formaCobranca.getIdFormaCobranca(),
                        formaCobranca.getIdCota(),
                        formaCobranca.getFornecedoresId(),
                        formaCobranca.getTipoFormaCobranca(),
                        formaCobranca.isDomingo(),
                        formaCobranca.isSegunda(),
                        formaCobranca.isTerca(),
                        formaCobranca.isQuarta(),
                        formaCobranca.isQuinta(),
                        formaCobranca.isSexta(),
                        formaCobranca.isSabado())){
                    
                    throw new ValidacaoException(TipoMensagem.WARNING,
                            "Os fornecedores já possuem uma forma de pagamento cadastrada para este dia.");
                }
            }
            
            // VERIFICA SE A FORMA DE COBRANÇA JA EXISTE PARA O FORNECEDOR E DIA
            // DA CONCENTRAÇÃO MENSAL
            else{
                if (!formaCobrancaService.validarFormaCobrancaMensal(formaCobranca.getIdFormaCobranca(),
                        formaCobranca.getIdCota(),
                        formaCobranca.getFornecedoresId(),
                        formaCobranca.getTipoFormaCobranca(),
                        Arrays.asList(formaCobranca.getDiaDoMes(),
                                formaCobranca.getPrimeiroDiaQuinzenal(),
                                formaCobranca.getSegundoDiaQuinzenal()))){
                    
                    throw new ValidacaoException(TipoMensagem.WARNING,
                            "Os fornecedores já possuem uma forma de pagamento cadastrada para este dia.");
                }
            }
        }
    }
    
    /**
     * Obtem Politica de Suspensao da Cota ou do Distribuidor
     * 
     * @param cota
     * @return PoliticaSuspensao
     */
    @Override
    @Transactional(readOnly = true)
    public PoliticaSuspensao obterPoliticaSuspensaoCota(Cota cota) {
    	
    	if (cota.isSugereSuspensaoDistribuidor()){
    		
    		Distribuidor d = this.distribuidorRepository.obter();
    		
    		return d.getPoliticaSuspensao();
    	}
        
        return cota.getPoliticaSuspensao();
    }
    
    /**
     * Verifica se cota utiliza politica de suspensao, seja propria ou do distribuidor
     * 
     * @param cota
     * @return boolean
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isSugereSuspensao(Cota cota) {
    	
    	if (cota.isSugereSuspensaoDistribuidor()){
    		
    		Distribuidor d = this.distribuidorRepository.obter();
    		
    		return d.isSugereSuspensao();
    	}
    		
    	return cota.isSugereSuspensao();
    }
}
