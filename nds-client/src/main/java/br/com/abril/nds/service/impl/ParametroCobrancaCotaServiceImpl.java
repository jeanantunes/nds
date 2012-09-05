package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ContratoVO;
import br.com.abril.nds.dto.ContratoTransporteDTO;
import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaCotaDTO;
import br.com.abril.nds.dto.PessoaContratoDTO;
import br.com.abril.nds.dto.PessoaContratoDTO.TipoPessoa;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.ContaBancaria;
import br.com.abril.nds.model.cadastro.ContratoCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.ConcentracaoCobrancaCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.ParametroCobrancaCotaRepository;
import br.com.abril.nds.service.ContratoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.util.TipoMensagem;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class ParametroCobrancaCotaServiceImpl implements ParametroCobrancaCotaService {
	

	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private CotaService cotaService;
		
	@Autowired
	private BancoRepository bancoRepository;
	
	@Autowired
	private FormaCobrancaRepository formaCobrancaRepository;
	
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
		
		ParametroCobrancaCotaDTO parametroCobrancaDTO = null;
		if (cota!=null){
			
			parametroCobrancaDTO = new ParametroCobrancaCotaDTO();

			parametroCobrancaDTO.setIdCota(cota.getId());
			parametroCobrancaDTO.setNumCota(cota.getNumeroCota());
			parametroCobrancaDTO.setSugereSuspensao(cota.isSugereSuspensao());
			parametroCobrancaDTO.setContrato(cota.isPossuiContrato());
			
			parametroCobranca = cota.getParametroCobranca();
			
			if (parametroCobranca==null){
				parametroCobranca = new ParametroCobrancaCota();
				parametroCobranca.setCota(cota);
				parametroCobranca.setFatorVencimento(1);
				parametroCobranca.setFormasCobrancaCota(null);
				parametroCobranca.setValorMininoCobranca(BigDecimal.ZERO);
				parametroCobranca.setTipoCota(null);
				parametroCobranca.setPoliticaSuspensao(null);
				this.parametroCobrancaCotaRepository.adicionar(parametroCobranca);
			}

			parametroCobrancaDTO.setIdParametroCobranca(parametroCobranca.getId());
			parametroCobrancaDTO.setFatorVencimento(parametroCobranca.getFatorVencimento());
			parametroCobrancaDTO.setValorMinimo((parametroCobranca.getValorMininoCobranca()!=null?parametroCobranca.getValorMininoCobranca():BigDecimal.ZERO));
			parametroCobrancaDTO.setTipoCota(parametroCobranca.getTipoCota());
			
			politicaSuspensao = parametroCobranca.getPoliticaSuspensao();
			
			if (politicaSuspensao!=null){
				parametroCobrancaDTO.setQtdDividasAberto(politicaSuspensao.getNumeroAcumuloDivida());
				parametroCobrancaDTO.setVrDividasAberto((politicaSuspensao.getValor()!=null?politicaSuspensao.getValor():BigDecimal.ZERO));
			}
					
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
	
			
			ContaBancaria contaBancaria = formaCobranca.getContaBancariaCota();
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
        boolean novo=false;
		
		//COTA
		cota = cotaRepository.buscarPorId(parametroCobrancaDTO.getIdCota());

		if (cota!=null){
			

			//PARAMETROS DE COBRANCA DA COTA
			parametroCobranca = cota.getParametroCobranca();
			
			
			if (parametroCobranca==null){
				novo=true;
				parametroCobranca = new ParametroCobrancaCota();
			}
			else{
				novo=false;
				//POLITICA DE SUSPENSAO DO PARAMETRO DE COBRANCA DA COTA
				politicaSuspensao = parametroCobranca.getPoliticaSuspensao();
			}

			parametroCobranca.setFatorVencimento((int) parametroCobrancaDTO.getFatorVencimento());
			parametroCobranca.setValorMininoCobranca((parametroCobrancaDTO.getValorMinimo()!=null?parametroCobrancaDTO.getValorMinimo():BigDecimal.ZERO));
			parametroCobranca.setTipoCota(parametroCobrancaDTO.getTipoCota());
			

			if (politicaSuspensao == null){
				politicaSuspensao = new PoliticaSuspensao();
			}
			politicaSuspensao.setNumeroAcumuloDivida((parametroCobrancaDTO.getQtdDividasAberto()!=null?parametroCobrancaDTO.getQtdDividasAberto():0));
			politicaSuspensao.setValor((parametroCobrancaDTO.getVrDividasAberto()!=null?parametroCobrancaDTO.getVrDividasAberto():BigDecimal.ZERO));
			
			parametroCobranca.setPoliticaSuspensao(politicaSuspensao);
			
			
			if (novo){
				parametroCobranca.setCota(cota);
				this.parametroCobrancaCotaRepository.adicionar(parametroCobranca);
			}
			else{
				this.parametroCobrancaCotaRepository.merge(parametroCobranca);
			}
			
			
			cota.setParametroCobranca(parametroCobranca);
			cota.setSugereSuspensao(parametroCobrancaDTO.isSugereSuspensao());
			cota.setPossuiContrato(parametroCobrancaDTO.isContrato());
			
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
		
		
		FormaCobranca formaCobranca = null;
		ContaBancaria contaBancariaCota = null;
		Set<ConcentracaoCobrancaCota> concentracoesCobranca = null;
		Banco banco = null;
		boolean novaFormaCobranca=false;
		
		
		if (formaCobrancaDTO.getIdBanco()!=null){
		    banco=this.bancoRepository.buscarPorId(formaCobrancaDTO.getIdBanco());
		}
		
			
		if (formaCobrancaDTO.getIdFormaCobranca()!=null){
			formaCobranca = this.formaCobrancaRepository.buscarPorId(formaCobrancaDTO.getIdFormaCobranca());
		}
		

		if (formaCobranca==null){
			novaFormaCobranca=true;

			formaCobranca = new FormaCobranca();
	    }
		else{
			novaFormaCobranca=false;
			
			concentracoesCobranca = formaCobranca.getConcentracaoCobrancaCota();
			
	        //APAGA CONCENTRACOES COBRANCA DA COTA
			if ((concentracoesCobranca!=null)&&(concentracoesCobranca.size() > 0)){
				formaCobranca.setConcentracaoCobrancaCota(null);
				for(ConcentracaoCobrancaCota itemConcentracaoCobranca:concentracoesCobranca){
					this.concentracaoCobrancaRepository.remover(itemConcentracaoCobranca);
				}
			}  
		}
		
		
		FormaCobranca formaCobrancaPrincipal = this.obterFormaCobrancaPrincipalCota(formaCobrancaDTO.getIdCota());
		formaCobranca.setPrincipal(formaCobrancaPrincipal==null);

		
		//CONCENTRACAO COBRANCA (DIAS DA SEMANA)
		concentracoesCobranca = new HashSet<ConcentracaoCobrancaCota>();
		ConcentracaoCobrancaCota concentracaoCobranca;
		if (formaCobrancaDTO.isDomingo()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.DOMINGO);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (formaCobrancaDTO.isSegunda()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SEGUNDA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (formaCobrancaDTO.isTerca()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.TERCA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (formaCobrancaDTO.isQuarta()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.QUARTA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (formaCobrancaDTO.isQuinta()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.QUINTA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (formaCobrancaDTO.isSexta()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SEXTA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (formaCobrancaDTO.isSabado()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SABADO);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		
		if(concentracoesCobranca.size()>0){
		    formaCobranca.setConcentracaoCobrancaCota(concentracoesCobranca);
		}
		
		List<Integer> diasdoMes = new ArrayList<Integer>();
		diasdoMes.add(formaCobrancaDTO.getDiaDoMes());
		diasdoMes.add(formaCobrancaDTO.getPrimeiroDiaQuinzenal());
		diasdoMes.add(formaCobrancaDTO.getSegundoDiaQuinzenal());
		formaCobranca.setDiasDoMes(diasdoMes);
		formaCobranca.setTipoFormaCobranca(formaCobrancaDTO.getTipoFormaCobranca());
		formaCobranca.setTipoCobranca(formaCobrancaDTO.getTipoCobranca());
		formaCobranca.setBanco(banco);
		formaCobranca.setRecebeCobrancaEmail(formaCobrancaDTO.isRecebeEmail());
		 
		
		contaBancariaCota = formaCobranca.getContaBancariaCota();
		if(contaBancariaCota==null){
			contaBancariaCota = new ContaBancaria();
		}
		contaBancariaCota.setNumeroBanco(formaCobrancaDTO.getNumBanco());
		contaBancariaCota.setNomeBanco(formaCobrancaDTO.getNomeBanco());
	    contaBancariaCota.setAgencia(formaCobrancaDTO.getAgencia());
		contaBancariaCota.setDvAgencia(formaCobrancaDTO.getAgenciaDigito());
		contaBancariaCota.setConta(formaCobrancaDTO.getConta());
		contaBancariaCota.setDvConta(formaCobrancaDTO.getContaDigito());
		
		formaCobranca.setContaBancariaCota(contaBancariaCota);

		
		formaCobranca.setAtiva(true);
		
		formaCobranca.setFornecedores(null);
		if ((formaCobrancaDTO.getFornecedoresId()!=null)&&(formaCobrancaDTO.getFornecedoresId().size()>0)){
			Fornecedor fornecedor;
		    Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		    for (Long idFornecedor:formaCobrancaDTO.getFornecedoresId()){
		    	fornecedor = fornecedorService.obterFornecedorPorId(idFornecedor);
		    	if (fornecedor!=null){
		    	    fornecedores.add(fornecedor);
		    	}
		    }
		    if (fornecedores.size()>0){
			    formaCobranca.setFornecedores(fornecedores);
		    }
		}
		
	    if(novaFormaCobranca){
	    	ParametroCobrancaCota parametroCobranca = this.parametroCobrancaCotaRepository.buscarPorId(formaCobrancaDTO.getIdParametroCobranca());
		    formaCobranca.setParametroCobrancaCota(parametroCobranca);

		    formaCobranca.setTaxaJurosMensal((formaCobranca.getTaxaJurosMensal()==null?BigDecimal.ZERO:formaCobranca.getTaxaJurosMensal()));
		    formaCobranca.setTaxaMulta((formaCobranca.getTaxaMulta()==null?BigDecimal.ZERO:formaCobranca.getTaxaMulta()));
		    formaCobranca.setValorMinimoEmissao((formaCobranca.getValorMinimoEmissao()==null?BigDecimal.ZERO:formaCobranca.getValorMinimoEmissao()));

		    formaCobrancaRepository.adicionar(formaCobranca);
	    }    
	    else{
	    	formaCobrancaRepository.merge(formaCobranca);    	
		}
		    
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
		List<FormaCobranca> formasCobranca = this.formaCobrancaRepository.obterFormasCobrancaCota(cota);
		List<FormaCobrancaDTO> formasCobrancaDTO = new LinkedList<FormaCobrancaDTO>();
		
		String strConcentracoes="";
		String strFornecedores="";
		Set<ConcentracaoCobrancaCota> concentracoes = new HashSet<ConcentracaoCobrancaCota>();
		Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		
		for(FormaCobranca formaCobrancaItem:formasCobranca){
			
			strConcentracoes="";
			strFornecedores="";
			
			concentracoes = formaCobrancaItem.getConcentracaoCobrancaCota();
			fornecedores = formaCobrancaItem.getFornecedores();
			
			if(formaCobrancaItem.getTipoFormaCobranca()==TipoFormaCobranca.SEMANAL){
				if ((concentracoes!=null)&&(!concentracoes.isEmpty())){
					for (ConcentracaoCobrancaCota itemConcentracao:concentracoes){
						if (!"".equals(strConcentracoes)){
							strConcentracoes = strConcentracoes + "/";
						}
						strConcentracoes = strConcentracoes + itemConcentracao.getDiaSemana().getDescricaoDiaSemana();
					}
				}				
			}
			else if (formaCobrancaItem.getTipoFormaCobranca()==TipoFormaCobranca.MENSAL){
				strConcentracoes = "Todo dia "+formaCobrancaItem.getDiasDoMes().get(0);
			}	
			else if (formaCobrancaItem.getTipoFormaCobranca()==TipoFormaCobranca.QUINZENAL){
				strConcentracoes = "Todo dia "+formaCobrancaItem.getDiasDoMes().get(0)+" e "+formaCobrancaItem.getDiasDoMes().get(1);
			}
			else if (formaCobrancaItem.getTipoFormaCobranca()==TipoFormaCobranca.DIARIA){
				strConcentracoes = "Diariamente";
			}
			
			if ((fornecedores!=null)&&(!fornecedores.isEmpty())){
				for (Fornecedor itemFornecedor:fornecedores){
					if (!"".equals(strFornecedores)){
						strFornecedores = strFornecedores + "/";
					}
					strFornecedores = strFornecedores + itemFornecedor.getJuridica().getRazaoSocial();
				}
			}
			
			formasCobrancaDTO.add(new FormaCobrancaDTO(formaCobrancaItem.getId(),
					                                   strFornecedores,
					                                   strConcentracoes,
					                                   (formaCobrancaItem.getTipoCobranca()!=null?formaCobrancaItem.getTipoCobranca().getDescTipoCobranca():""),
					                                   (formaCobrancaItem.getBanco()!=null?formaCobrancaItem.getBanco().getNome()+" : "+formaCobrancaItem.getBanco().getAgencia()+" : "+formaCobrancaItem.getBanco().getConta()+"-"+formaCobrancaItem.getBanco().getDvConta():"")
					                                  )
			                    );
		}
		return formasCobrancaDTO;
	}

	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.FinanceiroService#obtemContratoTransporte(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public ContratoTransporteDTO obtemContratoTransporte(long idCota){
		
		ContratoTransporteDTO contrato = new ContratoTransporteDTO();
		
		Distribuidor distribuidor = distribuidorRepository.obter();	
		
		PessoaJuridica pessoaJuridica = distribuidor.getJuridica();		
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
		for (Telefone itemTelefone:distribuidor.getJuridica().getTelefones()){
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
			
			ParametroContratoCota parametroContrato= distribuidor.getParametroContratoCota();
			if (parametroContrato!=null){
				contrato.setAvisoPrevio( Integer.toString(parametroContrato.getDiasAvisoRescisao()));
			    contrato.setComplemento(distribuidor.getParametroContratoCota().getComplementoContrato());
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
		
		if(contrato == null) 
			contrato = new ContratoCota();
		
		contrato.setAvisoPrevioRescisao(Integer.parseInt(contratoDTO.getAvisoPrevio()));
		contrato.setDataInicio(contratoDTO.getInicio());
		contrato.setDataTermino(contratoDTO.getTermino());
		contrato.setExigeDocumentacaoSuspencao(cota.isSugereSuspensao());
		contrato.setCota(cota);
		contrato.setPrazo(Integer.parseInt(contratoDTO.getPrazo()));
		contrato.setRecebido(isRecebido);
		
		this.contratoService.salvarContrato(contrato);
	}

	@Transactional
	public ContratoVO obterArquivoContratoRecebido(Long idCota) {
		
		ContratoVO contratoVO = null;
		
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		
		ContratoCota contrato = cota.getContratoCota();
		
		if (contrato != null) {
			
			//TODO: obter arquivo contrato recebido;
			
			contratoVO = new ContratoVO();
			contratoVO.setIdCota(idCota);
			contratoVO.setDataTermino(contrato.getDataTermino());
			contratoVO.setDataInicio(contrato.getDataInicio());
			contratoVO.setRecebido(true);
			
		}
		
		return contratoVO;
	}

	@Override
	@Transactional(readOnly = true)
	public FormaCobranca obterFormaCobrancaPrincipalCota(Long idCota) {
		return this.formaCobrancaRepository.obterFormaCobrancaPrincipalCota(idCota);
	}

	

	@Override
	@Transactional(readOnly = true)
	public List<FormaCobranca> obterFormasCobrancaCota(Long idCota) {
		Cota cota = this.cotaRepository.buscarPorId(idCota);
		return this.formaCobrancaRepository.obterFormasCobrancaCota(cota);
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



	@Override
	@Transactional
	public boolean validarFormaCobrancaMensal(Long idFormaCobranca, Long idCota, TipoCobranca tipoCobranca,
			List<Long> idFornecedores, Integer diaDoMes) {
		boolean res=true;
		List<FormaCobranca> formas = this.formaCobrancaRepository.obterPorCotaETipoCobranca(idCota, tipoCobranca, idFormaCobranca);
		for (FormaCobranca itemFormaCobranca:formas){
			for (int i=0; i<idFornecedores.size();i++){
				Fornecedor fornecedor= this.fornecedorService.obterFornecedorPorId(idFornecedores.get(i));
				if (itemFormaCobranca.getFornecedores().contains(fornecedor)){
					if (itemFormaCobranca.getDiasDoMes() !=null && itemFormaCobranca.getDiasDoMes().contains(diaDoMes)){
						res=false;
					}
				}
			}
		}
		return res;
	}



	@Override
	@Transactional
	public boolean validarFormaCobrancaSemanal(Long idFormaCobranca, Long idCota, TipoCobranca tipoCobranca, List<Long> idFornecedores, 
			Boolean domingo, Boolean segunda, Boolean terca, Boolean quarta, Boolean quinta, Boolean sexta, Boolean sabado) {
		
		boolean res=true;
		List<FormaCobranca> formas = this.formaCobrancaRepository.obterPorCotaETipoCobranca(idCota, tipoCobranca,idFormaCobranca);
		for (FormaCobranca itemFormaCobranca:formas){
			for (int i=0; i<idFornecedores.size();i++){
				Fornecedor fornecedor= this.fornecedorService.obterFornecedorPorId(idFornecedores.get(i));
				if (itemFormaCobranca.getFornecedores().contains(fornecedor)){
					
					for(ConcentracaoCobrancaCota itemConcentracao:itemFormaCobranca.getConcentracaoCobrancaCota()){
						
						if (
								(domingo && (itemConcentracao.getDiaSemana()==DiaSemana.DOMINGO))||
								(segunda && (itemConcentracao.getDiaSemana()==DiaSemana.SEGUNDA_FEIRA))||
								(terca && (itemConcentracao.getDiaSemana()==DiaSemana.TERCA_FEIRA))||
								(quarta && (itemConcentracao.getDiaSemana()==DiaSemana.QUARTA_FEIRA))||
								(quinta && (itemConcentracao.getDiaSemana()==DiaSemana.QUINTA_FEIRA))||
								(sexta && (itemConcentracao.getDiaSemana()==DiaSemana.SEXTA_FEIRA))||
								(sabado && (itemConcentracao.getDiaSemana()==DiaSemana.SABADO))
						    ){
							res=false;
						}
	
					}
	
				}
			}
		}
		return res;
	}

	
}
