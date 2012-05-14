package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ParametroCobrancaVO;
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
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
			    
				parametroCobranca.setAcumulaDivida(itemPolitica.isAcumulaDivida());
				parametroCobranca.setCobrancaUnificada(itemPolitica.isUnificaCobranca());
				parametroCobranca.setFormaEmissao((itemPolitica.getFormaEmissao()!=null?itemPolitica.getFormaEmissao().getDescFormaEmissao():""));
				
				forma = itemPolitica.getFormaCobranca();
				if (forma!=null){
					parametroCobranca.setFormaPagamento((forma.getTipoCobranca()!=null?forma.getTipoCobranca().getDescTipoCobranca():""));
					parametroCobranca.setBanco((forma.getBanco()!=null?forma.getBanco().getNome():""));
					parametroCobranca.setValorMinimoEmissao(forma.getValorMinimoEmissao().toString());
					parametroCobranca.setEvioPorEmail(forma.isRecebeCobrancaEmail());
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
		return this.politicaCobrancaRepository.buscarPoliticaCobrancaPorDistribuidor();
	}
	
}
