package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ParametroCobrancaVO;
import br.com.abril.nds.dto.ParametroCobrancaDTO;
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.ConcentracaoCobrancaCotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.FormaCobrancaRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.service.DistribuidorService;
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
			    
				parametroCobranca.setAcumula_divida(itemPolitica.isAcumulaDivida()?"Sim":"Não");
				parametroCobranca.setCobranca_unificada(itemPolitica.isUnificaCobranca()?"Sim":"Não");
				parametroCobranca.setFormaEmissao(itemPolitica.getFormaEmissao()!=null?itemPolitica.getFormaEmissao().getDescFormaEmissao():"");
				
				forma = itemPolitica.getFormaCobranca();
				if (forma!=null){
					parametroCobranca.setForma(forma.getTipoCobranca()!=null?forma.getTipoCobranca().getDescTipoCobranca():"");
					parametroCobranca.setBanco(forma.getBanco()!=null?forma.getBanco().getNome():"");
					parametroCobranca.setVlr_minimo_emissao(forma.getValorMinimoEmissao().toString());
					parametroCobranca.setEnvio_email(forma.isRecebeCobrancaEmail()?"Sim":"Não");
				}
				
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
		
			
		if (parametroCobrancaDTO.getIdParametro()!=null){
			politica = this.politicaCobrancaRepository.buscarPorId(parametroCobrancaDTO.getIdParametro());
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
		politica.setPrincipal(parametroCobrancaPrincipal==null);

		
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
		
		
		formaCobranca.setDiaDoMes(parametroCobrancaDTO.getDiaDoMes());
		formaCobranca.setTipoFormaCobranca(parametroCobrancaDTO.getTipoFormaCobranca());
		formaCobranca.setTipoCobranca(parametroCobrancaDTO.getTipoCobranca());
		formaCobranca.setBanco(banco);
		formaCobranca.setRecebeCobrancaEmail(parametroCobrancaDTO.isEvioEmail());

		
		formaCobranca.setAtiva(true);
		
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
		
		politica.setAcumulaDivida(parametroCobrancaDTO.isAcumulaDivida());
		politica.setFormaEmissao(parametroCobrancaDTO.getFormaEmissao());
		politica.setUnificaCobranca(parametroCobrancaDTO.isUnificada());
		politica.setUnificaCobranca(parametroCobrancaDTO.isUnificada());
		politica.setDistribuidor(distribuidorRepository.obter());
		
	    if(novaPolitica){
	    	
	    	formaCobranca.setTaxaJurosMensal((formaCobranca.getTaxaJurosMensal()==null?BigDecimal.ZERO:formaCobranca.getTaxaJurosMensal()));
		    formaCobranca.setTaxaMulta((formaCobranca.getTaxaMulta()==null?BigDecimal.ZERO:formaCobranca.getTaxaMulta()));
		    formaCobranca.setValorMinimoEmissao((formaCobranca.getValorMinimoEmissao()==null?BigDecimal.ZERO:formaCobranca.getValorMinimoEmissao()));

		    formaCobrancaRepository.adicionar(formaCobranca);	
		    
		    politica.setFormaCobranca(formaCobranca);
		    
		    politicaCobrancaRepository.adicionar(politica);
		    
	    }    
	    else{

	    	if(novaForma){
			    formaCobranca.setTaxaJurosMensal((formaCobranca.getTaxaJurosMensal()==null?BigDecimal.ZERO:formaCobranca.getTaxaJurosMensal()));
			    formaCobranca.setTaxaMulta((formaCobranca.getTaxaMulta()==null?BigDecimal.ZERO:formaCobranca.getTaxaMulta()));
			    formaCobranca.setValorMinimoEmissao((formaCobranca.getValorMinimoEmissao()==null?BigDecimal.ZERO:formaCobranca.getValorMinimoEmissao()));

			    formaCobrancaRepository.adicionar(formaCobranca);
		    }    
		    else{
		    	formaCobrancaRepository.merge(formaCobranca);    	
			}
	    	
	    	politica.setFormaCobranca(formaCobranca);
	    	
	    	politicaCobrancaRepository.merge(politica);   	
		}
		
	}
	
}
