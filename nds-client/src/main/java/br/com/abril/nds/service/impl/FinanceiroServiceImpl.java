package br.com.abril.nds.service.impl;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
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

import br.com.abril.nds.dto.ContratoTransporteDTO;
import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaDTO;
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
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroContratoCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.ConcentracaoCobrancaCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.ParametroCobrancaCotaRepository;
import br.com.abril.nds.service.FinanceiroService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.TipoMensagem;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class FinanceiroServiceImpl implements FinanceiroService {
	

	
	@Autowired
	private CotaRepository cotaRepository;
		
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
	
	
	/**
	 * Método responsável por obter bancos para preencher combo da camada view
	 * @return comboBancos: bancos cadastrados
	 */
	@Transactional(readOnly=true)
	@Override
	public List<ItemDTO<Integer, String>> getComboBancosTipoCobranca(TipoCobranca tipoCobranca) {
		List<ItemDTO<Integer,String>> comboBancos =  new ArrayList<ItemDTO<Integer,String>>();
		List<Banco> bancos = formaCobrancaRepository.obterBancosPorTipoDeCobranca(tipoCobranca);
		for (Banco itemBanco : bancos){
			comboBancos.add(new ItemDTO<Integer,String>(itemBanco.getId().intValue(), itemBanco.getNumeroBanco()+" - "+itemBanco.getNome()));
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
	 * Método responsável por obter os parametros de cobranca da cota
	 * @param idCota: ID da cota
	 * @return Data Transfer Object com os parametros de cobranca da cota
	 */
	@Override
	@Transactional(readOnly = true)
	public ParametroCobrancaDTO obterDadosParametroCobrancaPorCota(Long idCota) {
		
		Cota cota = cotaRepository.buscarPorId(idCota);
		PoliticaSuspensao politicaSuspensao = null; 
		ParametroCobrancaCota parametroCobranca = null;
		
		ParametroCobrancaDTO parametroCobrancaDTO = null;
		if (cota!=null){
			
			parametroCobranca = cota.getParametroCobranca();

			parametroCobrancaDTO = new ParametroCobrancaDTO(); 

			parametroCobrancaDTO.setIdCota(cota.getId());
			parametroCobrancaDTO.setNumCota(cota.getNumeroCota());
			parametroCobrancaDTO.setComissao(cota.getFatorDesconto());
			parametroCobrancaDTO.setSugereSuspensao(cota.isSugereSuspensao());
			parametroCobrancaDTO.setContrato(cota.isPossuiContrato());
			
			if (parametroCobranca!=null){
				parametroCobrancaDTO.setIdParametroCobranca(parametroCobranca.getId());
				parametroCobrancaDTO.setFatorVencimento(parametroCobranca.getFatorVencimento());
				parametroCobrancaDTO.setValorMinimo(parametroCobranca.getValorMininoCobranca());
				
				politicaSuspensao = parametroCobranca.getPoliticaSuspensao();
				
				if (politicaSuspensao!=null){
					parametroCobrancaDTO.setQtdDividasAberto(politicaSuspensao.getNumeroAcumuloDivida());
					parametroCobrancaDTO.setVrDividasAberto(politicaSuspensao.getValor());
				}
				
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
			
			if (formaCobranca.getTipoFormaCobranca() == TipoFormaCobranca.SEMANAL){
			    concentracoesCobranca = formaCobranca.getConcentracaoCobrancaCota();
			}
			if (formaCobranca.getTipoFormaCobranca() == TipoFormaCobranca.MENSAL){		
			    diaDoMes = formaCobranca.getDiaDoMes();
			}

			
			formaCobrancaDTO = new FormaCobrancaDTO(); 
			formaCobrancaDTO.setIdFormaCobranca(formaCobranca.getId());
			formaCobrancaDTO.setTipoFormaCobranca(formaCobranca.getTipoFormaCobranca());
			formaCobrancaDTO.setRecebeEmail(formaCobranca.isRecebeCobrancaEmail());
	
			
			ContaBancaria contaBancaria = formaCobranca.getContaBancariaCota();
			if (contaBancaria!=null){
				formaCobrancaDTO.setNumBanco(contaBancaria.getNumeroBanco());
				formaCobrancaDTO.setNomeBanco(contaBancaria.getNomeBanco());
				formaCobrancaDTO.setAgencia(contaBancaria.getDvAgencia());
				formaCobrancaDTO.setAgenciaDigito(contaBancaria.getDvAgencia());
				formaCobrancaDTO.setConta(contaBancaria.getConta()!=null?Long.toString(contaBancaria.getConta()):"");
				formaCobrancaDTO.setContaDigito(contaBancaria.getDvConta());
			}
			
			
			if  (diaDoMes!=null){
			    formaCobrancaDTO.setDiaDoMes(diaDoMes);
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
	public void postarParametroCobranca(ParametroCobrancaDTO parametroCobrancaDTO) {

		//COTA
		Cota cota = cotaRepository.buscarPorId(parametroCobrancaDTO.getIdCota());

		if (cota!=null){

			//PARAMETROS DE COBRANCA DA COTA
			ParametroCobrancaCota parametroCobranca = cota.getParametroCobranca();
			
			//POLITICA DE SUSPENSAO DO PARAMETRO DE COBRANCA DA COTA
			PoliticaSuspensao politicaSuspensao = parametroCobranca.getPoliticaSuspensao();

			
			parametroCobranca = (parametroCobranca==null?new ParametroCobrancaCota():parametroCobranca);
			
			politicaSuspensao = (politicaSuspensao==null?new PoliticaSuspensao():politicaSuspensao);

			
			parametroCobranca.setFatorVencimento((int) parametroCobrancaDTO.getFatorVencimento());
			parametroCobranca.setValorMininoCobranca(parametroCobrancaDTO.getValorMinimo());
			
			
			politicaSuspensao.setNumeroAcumuloDivida(parametroCobrancaDTO.getQtdDividasAberto());
			politicaSuspensao.setValor(parametroCobrancaDTO.getVrDividasAberto());

			parametroCobranca.setPoliticaSuspensao(politicaSuspensao);
			
			
			cota.setParametroCobranca(parametroCobranca);
			cota.setFatorDesconto(parametroCobrancaDTO.getComissao());
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
		
		
		Banco banco=this.bancoRepository.buscarPorId(formaCobrancaDTO.getIdBanco());
		boolean novaFormaCobranca=false;
		
			
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
		
		formaCobranca.setConcentracaoCobrancaCota(concentracoesCobranca);
		
		
		formaCobranca.setDiaDoMes(formaCobrancaDTO.getDiaDoMes());
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
		
		if (formaCobrancaDTO.getAgencia()!=null){
			contaBancariaCota.setAgencia(Long.getLong(formaCobrancaDTO.getAgencia()));
		}
		contaBancariaCota.setDvAgencia(formaCobrancaDTO.getAgenciaDigito());
		
		if(formaCobrancaDTO.getConta()!=null){
		    contaBancariaCota.setConta(Long.getLong(formaCobrancaDTO.getConta()));
		}
		contaBancariaCota.setDvConta(formaCobrancaDTO.getContaDigito());
		
		formaCobranca.setContaBancariaCota(contaBancariaCota);

		
		formaCobranca.setAtiva(true);
		
		
		
		
		
		
		
		Fornecedor fornecedor;
	    Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
	    for (Long idFornecedor:formaCobrancaDTO.getFornecedoresId()){
	    	fornecedor = fornecedorService.obterFornecedorPorId(idFornecedor);
	    	fornecedores.add(fornecedor);
	    }
		formaCobranca.setFornecedores(fornecedores);
		
		
		
		
		
		
	    if(novaFormaCobranca){
	    	ParametroCobrancaCota parametroCobranca = this.parametroCobrancaCotaRepository.buscarPorId(formaCobrancaDTO.getIdParametroCobranca());
		    formaCobranca.setParametroCobrancaCota(parametroCobranca);
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
				strConcentracoes = "Todo dia "+formaCobrancaItem.getDiaDoMes();
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
		
		EnderecoDistribuidor enderecoDistribuidor= distribuidorRepository.obterEnderecoPrincipal();
		
		if (enderecoDistribuidor != null) {
			contratante.setEndereco(enderecoDistribuidor.getEndereco());
		}
		contrato.setContratante(contratante);
		
		
		
		Cota cota = cotaRepository.buscarPorId(idCota);	
		if (cota!=null) {
			PessoaContratoDTO contratada = new PessoaContratoDTO();
			contratada.setNome((cota.getPessoa().getNome()!=null?cota.getPessoa().getNome():""));
			contratada.setDocumento((cota.getPessoa().getDocumento()!=null?cota.getPessoa().getDocumento():""));
			
			
			contratante.setTipoPessoa((cota.getPessoa()instanceof PessoaJuridica)?TipoPessoa.JURIDICA:TipoPessoa.FISICA);
			EnderecoCota enderecoCota = cotaRepository.obterEnderecoPrincipal(cota.getId());
			if (enderecoCota != null) {
				contratada.setEndereco(enderecoCota.getEndereco());
			}
			contrato.setContratada(contratada);
			
			
			
			
			
			//VERIFICAR, EMS CITA TODOS VINDOS DE DISTRIBUIDOR
			ContratoCota contratoCota = cota.getContratoCota();
			if (contratoCota!=null){
				contrato.setInicio(cota.getContratoCota().getDataInicio());
				contrato.setTermino(cota.getContratoCota().getDataTermino());
				contrato.setPrazo(cota.getContratoCota().getPrazo()!=null?cota.getContratoCota().getPrazo().toString():"");
			}
			
			ParametroContratoCota parametroContrato= distribuidor.getParametroContratoCota();
			if (parametroContrato!=null){
				contrato.setAvisoPrevio( Integer.toString(parametroContrato.getDiasAvisoRescisao()));
			    contrato.setComplemento(distribuidor.getParametroContratoCota().getComplementoContrato());
			}
			//----------
			
			
			
			
			
		}else{
			throw new IllegalArgumentException("Id da cota não cadastrada");
		}
		
		
		//TODO: Dados dos gestores do Contrato
		
		//TODO: Dados da contratação(Prazo, Inicio e Termino e Aviso Prévio para Recisão)
		
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
	@Transactional(readOnly = true)
	public byte[] geraImpressaoContrato(Long idCota){
		
		byte[] relatorio=null;

		ContratoTransporteDTO contratoDTO = this.obtemContratoTransporte(idCota);
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
	
	
}
