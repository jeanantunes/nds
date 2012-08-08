package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ParametroCobrancaVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaDTO;
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.ConcentracaoCobrancaCotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.PoliticaCobrancaService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Boleto}
 * 
 * @author Discover Technology
 */
@Service
public class PoliticaCobrancaServiceImpl implements PoliticaCobrancaService {
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Autowired
	private FormaCobrancaRepository formaCobrancaRepository;
	
	@Autowired
	private BancoRepository bancoRepository;
	
	@Autowired
	private ConcentracaoCobrancaCotaRepository concentracaoCobrancaRepository;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	
	
	 
	    
	/**
	  * Método responsável por obter combo de Formas de Emissão
	  * @return comboFormasEmissao: Formas de emissão
	  */
	@Override
	public List<ItemDTO<FormaEmissao, String>> getComboFormasEmissao() {
		List<ItemDTO<FormaEmissao,String>> comboFormasEmissao =  new ArrayList<ItemDTO<FormaEmissao,String>>();
		for (FormaEmissao itemFormaEmissao: FormaEmissao.values()){
			comboFormasEmissao.add(new ItemDTO<FormaEmissao,String>(itemFormaEmissao, itemFormaEmissao.getDescFormaEmissao()));
		}
		return comboFormasEmissao;
	}
	
	
	/**
	  * Método responsável por obter combo de Formas de Emissão dependendo do Tipo de Cobrança
	  * @return comboFormasEmissao: Formas de emissão
	  */
	@Override
	public List<ItemDTO<FormaEmissao, String>> getComboFormasEmissaoTipoCobranca(TipoCobranca tipoCobranca) {
		List<ItemDTO<FormaEmissao,String>> comboFormasEmissao =  new ArrayList<ItemDTO<FormaEmissao,String>>();
		for (FormaEmissao itemFormaEmissao: FormaEmissao.values()){
			if (itemFormaEmissao == FormaEmissao.INDIVIDUAL_AGREGADA){
				if (tipoCobranca == TipoCobranca.BOLETO_EM_BRANCO){
					comboFormasEmissao.add(new ItemDTO<FormaEmissao,String>(itemFormaEmissao, itemFormaEmissao.getDescFormaEmissao()));
				}
			}
			else{
			    comboFormasEmissao.add(new ItemDTO<FormaEmissao,String>(itemFormaEmissao, itemFormaEmissao.getDescFormaEmissao()));
			}
		}
		return comboFormasEmissao;
	}
	

	@Override
	@Transactional(readOnly=true)
	public List<ParametroCobrancaVO> obterDadosPoliticasCobranca(
			FiltroParametrosCobrancaDTO filtro) {
		
		List<PoliticaCobranca> politicasCobranca = politicaCobrancaRepository.obterPoliticasCobranca(filtro);
		ParametroCobrancaVO parametroCobranca = null;
		List<ParametroCobrancaVO> parametrosCobranca = null;
		FormaCobranca forma = null;
		if ((politicasCobranca!=null)&&(politicasCobranca.size()>0)){
			parametrosCobranca = new ArrayList<ParametroCobrancaVO>();
			for(PoliticaCobranca itemPolitica:politicasCobranca){
				parametroCobranca = new ParametroCobrancaVO();
			    
				parametroCobranca.setIdPolitica(itemPolitica.getId());
				parametroCobranca.setAcumulaDivida(itemPolitica.isAcumulaDivida()?"Sim":"Não");
				parametroCobranca.setCobrancaUnificada(itemPolitica.isUnificaCobranca()?"Sim":"Não");
				parametroCobranca.setFormaEmissao(itemPolitica.getFormaEmissao()!=null?itemPolitica.getFormaEmissao().getDescFormaEmissao():"");
				
				forma = itemPolitica.getFormaCobranca();
				if (forma!=null){
					parametroCobranca.setForma(forma.getTipoCobranca()!=null?forma.getTipoCobranca().getDescTipoCobranca():"");
					parametroCobranca.setBanco(forma.getBanco()!=null?forma.getBanco().getNome():"");
					parametroCobranca.setValorMinimoEmissao(forma.getValorMinimoEmissao()!=null?forma.getValorMinimoEmissao().toString():"");
					parametroCobranca.setEnvioEmail(forma.isRecebeCobrancaEmail()?"Sim":"Não");
					StringBuilder fornecedores = new StringBuilder();
					int i = 0;
					for (Fornecedor fornecedor: forma.getFornecedores()) {
						fornecedores.append(fornecedor.getJuridica().getNome());
						i++;
						if(i < forma.getFornecedores().size()){
							fornecedores.append(", ");
						}
						
					}					
					parametroCobranca.setFornecedores(fornecedores.toString());
					
					i = 0;
					StringBuilder concentracaoPagamentos= new StringBuilder();
					for (ConcentracaoCobrancaCota cobranca: forma.getConcentracaoCobrancaCota()) {
						concentracaoPagamentos.append(cobranca.getDiaSemana().getDescricaoDiaSemana().substring(0, 3));
						i++;
						if(i < forma.getConcentracaoCobrancaCota().size()){
							concentracaoPagamentos.append(", ");
						}
						
					}					
					parametroCobranca.setConcentracaoPagamentos(concentracaoPagamentos.toString());
				}
				
				parametroCobranca.setPrincipal(itemPolitica.isPrincipal());
				
				
				parametrosCobranca.add(parametroCobranca);
			}
		}
	    
		return parametrosCobranca;
	}

	@Override
	@Transactional(readOnly=true)
	public int obterQuantidadePoliticasCobranca(
			FiltroParametrosCobrancaDTO filtro) {
		return this.politicaCobrancaRepository.obterQuantidadePoliticasCobranca(filtro);
	}

	@Override
	@Transactional(readOnly=true)
	public PoliticaCobranca obterPoliticaCobrancaPrincipal() {
		return this.politicaCobrancaRepository.buscarPoliticaCobrancaPrincipal();
	}

	
	
	
	
	
	
	/**
	 * Método responsável por obter os dados da politica de cobranca
	 * @param idPolitica: ID da politica de cobranca
	 * @return Data Transfer Object com os dados da politica de cobranca
	 */
	@Override
	@Transactional(readOnly = true)
	public ParametroCobrancaDTO obterDadosPoliticaCobranca(Long idPolitica) {
		
		PoliticaCobranca politica = null;
		ParametroCobrancaDTO parametroCobrancaDTO = null;	
		politica = this.politicaCobrancaRepository.buscarPorId(idPolitica);
		FormaCobranca formaCobranca = null;
		formaCobranca = politica.getFormaCobranca();
		
		if (politica!=null){
			
			parametroCobrancaDTO = new ParametroCobrancaDTO();
			
			parametroCobrancaDTO.setIdPolitica(politica.getId());
			parametroCobrancaDTO.setPrincipal(politica.isPrincipal()?true:false);
			parametroCobrancaDTO.setAcumulaDivida(politica.isAcumulaDivida()?true:false);
			parametroCobrancaDTO.setFormaEmissao(politica.getFormaEmissao());
			parametroCobrancaDTO.setUnificada(politica.isUnificaCobranca()?true:false);
			
			Set<ConcentracaoCobrancaCota> concentracoesCobranca=null;
			
			if (formaCobranca!=null){
			
				if (formaCobranca.getTipoFormaCobranca() == TipoFormaCobranca.SEMANAL){
				    concentracoesCobranca = formaCobranca.getConcentracaoCobrancaCota();
				}		
				parametroCobrancaDTO.setDiasDoMes(new ArrayList<Integer>(formaCobranca.getDiasDoMes()));
				

				parametroCobrancaDTO.setTipoFormaCobranca(formaCobranca.getTipoFormaCobranca());
				parametroCobrancaDTO.setEnvioEmail(formaCobranca.isRecebeCobrancaEmail()?true:false);
				parametroCobrancaDTO.setVencimentoDiaUtil(formaCobranca.isVencimentoDiaUtil()?true:false);
				parametroCobrancaDTO.setValorMinimo(formaCobranca.getValorMinimoEmissao());
				parametroCobrancaDTO.setFormaCobrancaBoleto(formaCobranca.getFormaCobrancaBoleto());
				
				
				
				if ((concentracoesCobranca!=null)&&(concentracoesCobranca.size() > 0)){
					for (ConcentracaoCobrancaCota itemConcentracaoCobranca:concentracoesCobranca){
						
						DiaSemana dia = itemConcentracaoCobranca.getDiaSemana();
						if (dia==DiaSemana.DOMINGO){
							parametroCobrancaDTO.setDomingo(true);
						}
		
						if (dia==DiaSemana.SEGUNDA_FEIRA){
							parametroCobrancaDTO.setSegunda(true);
						}    
						
						if (dia==DiaSemana.TERCA_FEIRA){
							parametroCobrancaDTO.setTerca(true);
						}    
						
						if (dia==DiaSemana.QUARTA_FEIRA){
							parametroCobrancaDTO.setQuarta(true);
						}
						
					    if (dia==DiaSemana.QUINTA_FEIRA){
					    	parametroCobrancaDTO.setQuinta(true);
					    }    
					    
						if (dia==DiaSemana.SEXTA_FEIRA){
							parametroCobrancaDTO.setSexta(true);
						}    
						
						if (dia==DiaSemana.SABADO){
							parametroCobrancaDTO.setSabado(true);
						}
						
					}
				}
				
			}
			

			Banco banco = formaCobranca.getBanco();
			if (banco!=null){
				parametroCobrancaDTO.setIdBanco(banco.getId());
				parametroCobrancaDTO.setTaxaMulta(banco.getMulta());
				parametroCobrancaDTO.setValorMulta(banco.getVrMulta());
				parametroCobrancaDTO.setTaxaJuros(banco.getJuros());
				parametroCobrancaDTO.setInstrucoes(banco.getInstrucoes());
				
			}
			parametroCobrancaDTO.setTipoCobranca(formaCobranca.getTipoCobranca());
			
			
			Set<Fornecedor> fornecedores = formaCobranca.getFornecedores();
			List<Long> fornecedoresID = new ArrayList<Long>();
			if (fornecedores!=null){
			    for(Fornecedor itemFornecedor:fornecedores){
				    fornecedoresID.add(itemFornecedor.getId());
			    }
			    parametroCobrancaDTO.setFornecedoresId(fornecedoresID);
			}		
 
		}
		
		return parametroCobrancaDTO;
	}
	
	
	@Override
	@Transactional
	public void postarPoliticaCobranca(ParametroCobrancaDTO parametroCobrancaDTO) {

        PoliticaCobranca politica = null;
		FormaCobranca formaCobranca = null;
		Set<ConcentracaoCobrancaCota> concentracoesCobranca = null;
		Banco banco = null;
		boolean novaPolitica=false;
		boolean novaForma=false;
		
		if (parametroCobrancaDTO.getIdBanco()!=null){
		    banco=this.bancoRepository.buscarPorId(parametroCobrancaDTO.getIdBanco());
		}
		
			
		if (parametroCobrancaDTO.getIdPolitica()!=null){
			politica = this.politicaCobrancaRepository.buscarPorId(parametroCobrancaDTO.getIdPolitica());
		}
		
		
		if(politica==null){
			novaPolitica = true;
			novaForma=true;
			politica = new PoliticaCobranca();	
			formaCobranca = new FormaCobranca();
		}
		else{
			novaPolitica = false;
			
			formaCobranca = politica.getFormaCobranca();
			
			if (formaCobranca!=null){
				novaForma=false;

				concentracoesCobranca = formaCobranca.getConcentracaoCobrancaCota();
				
		        //APAGA CONCENTRACOES COBRANCA DA FORMA DE COBRANCA
				if ((concentracoesCobranca!=null)&&(concentracoesCobranca.size() > 0)){
					formaCobranca.setConcentracaoCobrancaCota(null);
					for(ConcentracaoCobrancaCota itemConcentracaoCobranca:concentracoesCobranca){
						this.concentracaoCobrancaRepository.remover(itemConcentracaoCobranca);
					}
				}  
			}
			else{
				novaForma=true;
				formaCobranca = new FormaCobranca();
			}
			
		}
		
		
		PoliticaCobranca parametroCobrancaPrincipal = this.obterPoliticaCobrancaPrincipal();
		
		if(parametroCobrancaPrincipal != null && parametroCobrancaDTO.isPrincipal()){
			parametroCobrancaPrincipal.setPrincipal(false);
			politicaCobrancaRepository.alterar(parametroCobrancaPrincipal);
		}
		politica.setPrincipal(parametroCobrancaDTO.isPrincipal());
		politica.setAcumulaDivida(parametroCobrancaDTO.isAcumulaDivida());
		politica.setFormaEmissao(parametroCobrancaDTO.getFormaEmissao());
		politica.setUnificaCobranca(parametroCobrancaDTO.isUnificada());
		politica.setAtivo(true);
		politica.setDistribuidor(distribuidorRepository.obter());
		
		
		formaCobranca.setDiasDoMes(parametroCobrancaDTO.getDiasDoMes());
		formaCobranca.setTipoFormaCobranca(parametroCobrancaDTO.getTipoFormaCobranca());
		formaCobranca.setTipoCobranca(parametroCobrancaDTO.getTipoCobranca());
		formaCobranca.setBanco(banco);
		formaCobranca.setRecebeCobrancaEmail(parametroCobrancaDTO.isEnvioEmail());
		formaCobranca.setAtiva(true);
		formaCobranca.setTaxaJurosMensal(parametroCobrancaDTO.getTaxaJuros());
	    formaCobranca.setTaxaMulta(parametroCobrancaDTO.getTaxaMulta());
	    formaCobranca.setValorMinimoEmissao(parametroCobrancaDTO.getValorMinimo());
	    formaCobranca.setVencimentoDiaUtil(parametroCobrancaDTO.isVencimentoDiaUtil());
	    
	    if(TipoCobranca.BOLETO.equals(parametroCobrancaDTO.getTipoCobranca()) || TipoCobranca.BOLETO_EM_BRANCO.equals(parametroCobrancaDTO.getTipoCobranca())){
	    	formaCobranca.setFormaCobrancaBoleto(parametroCobrancaDTO.getFormaCobrancaBoleto());
	    }else{
	    	formaCobranca.setFormaCobrancaBoleto(null);
	    }
	    
	    		
		//CONCENTRACAO COBRANCA (DIAS DA SEMANA)
		concentracoesCobranca = new HashSet<ConcentracaoCobrancaCota>();
		ConcentracaoCobrancaCota concentracaoCobranca;
		if (parametroCobrancaDTO.isDomingo()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.DOMINGO);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (parametroCobrancaDTO.isSegunda()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SEGUNDA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (parametroCobrancaDTO.isTerca()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.TERCA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (parametroCobrancaDTO.isQuarta()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.QUARTA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (parametroCobrancaDTO.isQuinta()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.QUINTA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (parametroCobrancaDTO.isSexta()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SEXTA_FEIRA);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		if (parametroCobrancaDTO.isSabado()){
			
			concentracaoCobranca=new ConcentracaoCobrancaCota();
			concentracaoCobranca.setDiaSemana(DiaSemana.SABADO);
			concentracaoCobranca.setFormaCobranca(formaCobranca);
			
			this.concentracaoCobrancaRepository.adicionar(concentracaoCobranca);
			concentracoesCobranca.add(concentracaoCobranca);
		}
		
		if(concentracoesCobranca.size()>0){
		    formaCobranca.setConcentracaoCobrancaCota(concentracoesCobranca);
		}
		
        
		formaCobranca.setFornecedores(null);
		if ((parametroCobrancaDTO.getFornecedoresId()!=null)&&(parametroCobrancaDTO.getFornecedoresId().size()>0)){
			Fornecedor fornecedor;
		    Set<Fornecedor> fornecedores = new HashSet<Fornecedor>();
		    for (Long idFornecedor:parametroCobrancaDTO.getFornecedoresId()){
		    	fornecedor = fornecedorService.obterFornecedorPorId(idFornecedor);
		    	if (fornecedor!=null){
		    	    fornecedores.add(fornecedor);
		    	}
		    }
		    if (fornecedores.size()>0){
			    formaCobranca.setFornecedores(fornecedores);
		    }
		}
		
		
	    if(novaPolitica){
	    	
		    formaCobrancaRepository.adicionar(formaCobranca);	
		    
		    politica.setFormaCobranca(formaCobranca);
		    
		    politicaCobrancaRepository.adicionar(politica);
		    
	    }    
	    else{

	    	if(novaForma){
			    formaCobrancaRepository.adicionar(formaCobranca);
		    }    
		    else{
		    	formaCobrancaRepository.merge(formaCobranca);    	
			}
	    	
	    	politica.setFormaCobranca(formaCobranca);
	    	
	    	politicaCobrancaRepository.merge(politica);   	
		}
		
	}

	@Override
	@Transactional
	public void dasativarPoliticaCobranca(long idPolitica) {
		this.politicaCobrancaRepository.desativarPoliticaCobranca(idPolitica);
	}
	
	
	@Override
	@Transactional
	public boolean validarFormaCobrancaMensal(Long idPoliticaCobranca, Distribuidor distribuidor,TipoCobranca tipoCobranca,
			List<Long> idFornecedores, Integer diaDoMes) {
		
		boolean res=true;
		Long idFormaCobrancaExcept = null;
		
		if (idPoliticaCobranca!=null){
		    PoliticaCobranca politica = this.politicaCobrancaRepository.buscarPorId(idPoliticaCobranca);
		    idFormaCobrancaExcept = politica.getFormaCobranca().getId();
		}    
		
		List<FormaCobranca> formas = this.formaCobrancaRepository.obterPorDistribuidorETipoCobranca(distribuidor.getId(), tipoCobranca, idFormaCobrancaExcept);
		for (FormaCobranca itemFormaCobranca:formas){
			for (int i=0; i<idFornecedores.size();i++){
				Fornecedor fornecedor= this.fornecedorService.obterFornecedorPorId(idFornecedores.get(i));
				if (itemFormaCobranca.getFornecedores().contains(fornecedor)){
					if (diaDoMes==itemFormaCobranca.getDiasDoMes().get(0)){
						res=false;
					}
				}
			}
		}
		return res;
	}



	@Override
	@Transactional
	public boolean validarFormaCobrancaSemanal(Long idPoliticaCobranca, Distribuidor distribuidor, TipoCobranca tipoCobranca, List<Long> idFornecedores, 
			Boolean domingo, Boolean segunda, Boolean terca, Boolean quarta, Boolean quinta, Boolean sexta, Boolean sabado) {
		
		boolean res=true;
        Long idFormaCobrancaExcept = null;
		
		if (idPoliticaCobranca!=null){
			PoliticaCobranca politica = this.politicaCobrancaRepository.buscarPorId(idPoliticaCobranca);
			idFormaCobrancaExcept = politica.getFormaCobranca().getId();
		}	
		
		List<FormaCobranca> formas = this.formaCobrancaRepository.obterPorDistribuidorETipoCobranca(distribuidor.getId(), tipoCobranca, idFormaCobrancaExcept);
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
