package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ContratoTransporteDTO;
import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaDTO;
import br.com.abril.nds.dto.PessoaContratoDTO;
import br.com.abril.nds.dto.PessoaContratoDTO.TipoPessoa;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.ContaBancaria;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.ConcentracaoCobrancaCotaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.ParametroCobrancaCotaRepository;
import br.com.abril.nds.service.FinanceiroService;

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

		ParametroCobrancaDTO parametroCobrancaDTO = null;
		if (cota!=null){
			
			
			ParametroCobrancaCota parametroCobranca = cota.getParametroCobranca();
				
			PoliticaSuspensao politicaSuspensao = parametroCobranca.getPoliticaSuspensao();

			parametroCobrancaDTO = new ParametroCobrancaDTO(); 

			parametroCobrancaDTO.setIdParametroCobranca(parametroCobranca.getId());
			parametroCobrancaDTO.setIdCota(cota.getId());
			parametroCobrancaDTO.setNumCota(cota.getNumeroCota());
			parametroCobrancaDTO.setComissao(cota.getFatorDesconto());
			parametroCobrancaDTO.setSugereSuspensao(cota.isSugereSuspensao());
			parametroCobrancaDTO.setContrato(cota.isPossuiContrato());
			
			
			if (parametroCobranca!=null){
				parametroCobrancaDTO.setFatorVencimento(parametroCobranca.getFatorVencimento());
				parametroCobrancaDTO.setValorMinimo(parametroCobranca.getValorMininoCobranca());
			}

			if (politicaSuspensao!=null){
				parametroCobrancaDTO.setQtdDividasAberto(politicaSuspensao.getNumeroAcumuloDivida());
				parametroCobrancaDTO.setVrDividasAberto(politicaSuspensao.getValor());
			}
		}

		return parametroCobrancaDTO;
	}
	
	
    
	/**
	 * Método responsável por obter os dados da forma de cobranca
	 * @param idFormaCobranca: ID da forma de cobranca
	 * @return Data Transfer Object com os dados da forma de cobranca
	 * TO-DO
	 */
	@Override
	@Transactional(readOnly = true)
	public FormaCobrancaDTO obterDadosFormaCobranca(Long idFormaCobranca) {
		
		FormaCobrancaDTO formaCobrancaDTO = null;
		
		FormaCobranca formaCobranca = this.formaCobrancaRepository.buscarPorId(idFormaCobranca);
		
		if (formaCobranca!=null){
			
		    /*
			Set<ConcentracaoCobrancaCota> concentracoesCobranca = formaCobranca.getConcentracaoCobrancaCota();
			
			ContaBancaria contaBancaria = formaCobranca.getContaBancariaCota();
			
			formaCobrancaDTO = new FormaCobrancaDTO(); 
			
			
			formaCobrancaDTO.setIdFormaCobranca(formaCobranca.getId());
			formaCobrancaDTO.setRecebeEmail(formaCobranca.isRecebeCobrancaEmail());
	
			
			if (contaBancaria!=null){
				formaCobrancaDTO.setNumBanco(contaBancaria.getNumeroBanco());
				formaCobrancaDTO.setNomeBanco(contaBancaria.getNomeBanco());
				formaCobrancaDTO.setAgencia(contaBancaria.getDvAgencia());
				formaCobrancaDTO.setAgenciaDigito(contaBancaria.getDvAgencia());
				formaCobrancaDTO.setConta(contaBancaria.getConta()!=null?Long.toString(contaBancaria.getConta()):"");
				formaCobrancaDTO.setContaDigito(contaBancaria.getDvConta());
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
			
		    */
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
	 * TO-DO
	 */
	@Override
	@Transactional
	public void postarFormaCobranca(FormaCobrancaDTO formaCobrancaDTO) {
		
		FormaCobranca formaCobranca = null;
		ContaBancaria contaBancariaCota = null;
		Set<ConcentracaoCobrancaCota> concentracoesCobranca = null;
		
		Banco banco=this.bancoRepository.buscarPorId(formaCobrancaDTO.getIdBanco());
		boolean novaFormaCobranca=false;
		
        if (formaCobrancaDTO.getTipoCobranca()!=null){
        	/*
			formaCobranca = formaCobrancaRepository.obterPorTipoEBanco(formaCobrancaDTO.getTipoCobranca(), banco);
			
			if (formaCobranca==null){
				novaFormaCobranca=true;
            	formaCobranca = new FormaCobranca();
    			formaCobranca.setTipoCobranca(formaCobrancaDTO.getTipoCobranca());
    			formaCobranca.setBanco(banco);
		    }
			else{
				novaFormaCobranca=false;

		        //APAGA CONCENTRACOES COBRANCA DA COTA
				if ((concentracoesCobranca!=null)&&(concentracoesCobranca.size() > 0)){
					formaCobranca.setConcentracaoCobrancaCota(null);
					for(ConcentracaoCobrancaCota itemConcentracaoCobranca:concentracoesCobranca){
						this.concentracaoCobrancaRepository.remover(itemConcentracaoCobranca);
					}
				}  
				
			}
			
			
			formaCobranca.setRecebeCobrancaEmail(formaCobrancaDTO.isRecebeEmail());
			
			
	        //CONCENTRACAO COBRANCA (DIAS DA SEMANA)
			ConcentracaoCobrancaCota concentracaoCobranca;
			if (formaCobrancaDTO.isDomingo()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.DOMINGO);
				concentracaoCobranca.setFormaCobrancaCota(formaCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			if (formaCobrancaDTO.isSegunda()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.SEGUNDA_FEIRA);
				concentracaoCobranca.setFormaCobrancaCota(formaCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			if (formaCobrancaDTO.isTerca()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.TERCA_FEIRA);
				concentracaoCobranca.setFormaCobrancaCota(formaCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			if (formaCobrancaDTO.isQuarta()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.QUARTA_FEIRA);
				concentracaoCobranca.setFormaCobrancaCota(formaCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			if (formaCobrancaDTO.isQuinta()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.QUINTA_FEIRA);
				concentracaoCobranca.setFormaCobrancaCota(formaCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			if (formaCobrancaDTO.isSexta()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.SEXTA_FEIRA);
				concentracaoCobranca.setFormaCobrancaCota(formaCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			if (formaCobrancaDTO.isSabado()){
				
				concentracaoCobranca=new ConcentracaoCobrancaCota();
				concentracaoCobranca.setDiaSemana(DiaSemana.SABADO);
				concentracaoCobranca.setFormaCobrancaCota(formaCobranca);
				
				this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
				concentracoesCobranca.add(concentracaoCobranca);
			}
			
			formaCobranca.setConcentracaoCobrancaCota(concentracoesCobranca);
	        
	  
			contaBancariaCota = formaCobranca.getContaBancaria();
			contaBancariaCota.setNumeroBanco(formaCobrancaDTO.getNumBanco());
			contaBancariaCota.setNomeBanco(formaCobrancaDTO.getNomeBanco());
			contaBancariaCota.setAgencia(Long.getLong(formaCobrancaDTO.getAgencia()));
			contaBancariaCota.setDvAgencia(formaCobrancaDTO.getAgenciaDigito());
			contaBancariaCota.setConta(Long.getLong(formaCobrancaDTO.getConta()));
			contaBancariaCota.setDvConta(formaCobrancaDTO.getContaDigito());
			
			formaCobranca.setContaBancariaCota(contaBancariaCota);

			
		    if(novaFormaCobranca){
		    	ParametroCobrancaCota parametroCobranca = this.parametroCobrancaCotaRepository.buscarPorId(formaCobrancaDTO.getIdParametroCobranca());
			    formaCobranca.setParametroCobrancaCota(parametroCobranca);
			    formaCobrancaRepository.adicionar(formaCobranca);
		    }    
		    else{
		    	formaCobrancaRepository.merge(formaCobranca);    	
			}
		    */
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
		List<FormaCobranca> formasCobranca = this.formaCobrancaRepository.buscarTodos();
		List<FormaCobrancaDTO> formasCobrancaVO = new LinkedList<FormaCobrancaDTO>();
		for(FormaCobranca formaCobrancaItem:formasCobranca){
			formasCobrancaVO.add(new FormaCobrancaDTO(formaCobrancaItem.getId(),
					                                 "Fornecedor Teste",
					                                 "Concentração Teste",
					                                 formaCobrancaItem.getTipoCobranca().getDescTipoCobranca(),
					                                 formaCobrancaItem.getBanco().getNome()+" : "+formaCobrancaItem.getBanco().getAgencia()+" : "+formaCobrancaItem.getBanco().getConta()+"-"+formaCobrancaItem.getBanco().getDvConta()
					                                )
			                    );
		}
		return formasCobrancaVO;
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
		
		contratante.setNome(pessoaJuridica.getNome());
		contratante.setDocumento(pessoaJuridica.getDocumento());
		contratante.setTipoPessoa(TipoPessoa.JURIDICA);
		
		EnderecoDistribuidor enderecoDistribuidor= distribuidorRepository.obterEnderecoPrincipal();
		
		if (enderecoDistribuidor != null) {
			contratante.setEndereco(enderecoDistribuidor.getEndereco());
		}
		contrato.setContratante(contratante);
		
		
		
		Cota cota = cotaRepository.buscarPorId(idCota);	
		if (cota!=null) {
			PessoaContratoDTO contratada = new PessoaContratoDTO();
			contratada.setNome(cota.getPessoa().getNome());
			contratada.setDocumento(cota.getPessoa().getDocumento());
			
			contratante.setTipoPessoa((cota.getPessoa()instanceof PessoaJuridica)?TipoPessoa.JURIDICA:TipoPessoa.FISICA);
			EnderecoCota enderecoCota = cotaRepository
					.obterEnderecoPrincipal(cota.getId());
			if (enderecoCota != null) {
				contratada.setEndereco(enderecoCota.getEndereco());
			}
			contrato.setContratada(contratada);
		}else{
			throw new IllegalArgumentException("Id da cota não cadastrada");
		}
		
		
		//TODO: Dados dos gestores do Contrato
		
		//TODO: Dados da contratação(Prazo, Inicio e Termino e Aviso Prévio para Recisão)
		
		return contrato;
	}


}
