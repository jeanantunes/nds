package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ParametroCobrancaVO;
import br.com.abril.nds.dto.filtro.FiltroConsultaParametrosCobrancaDTO;
import br.com.abril.nds.service.ParametroCobrancaService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Boleto}
 * 
 * @author Discover Technology
 */
@Service
public class ParametroCobrancaServiceImpl implements ParametroCobrancaService {

	@Override
	@Transactional(readOnly=true)
	public List<ParametroCobrancaVO> obterParametrosCobranca(FiltroConsultaParametrosCobrancaDTO filtro) {
		List<ParametroCobrancaVO> parametrosCobranca = new ArrayList<ParametroCobrancaVO>();
		
		ParametroCobrancaVO pTeste = new ParametroCobrancaVO();
		pTeste.setIdParametro(1l);
		pTeste.setAcumulaDivida("Sim");
		pTeste.setCobrancaUnificada("Sim");
		pTeste.setEvioPorEmail("Sim");
		pTeste.setFormaEmissao("Forma Emissão");
		pTeste.setFormaPagamento("Forma Pagamento");
		pTeste.setValorMinimoEmissao("10");
		pTeste.setBanco("BANCO");
		parametrosCobranca.add(pTeste);
		
		return parametrosCobranca;//!!!
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public int obterQuantidadeParametrosCobranca(FiltroConsultaParametrosCobrancaDTO filtro) {
		return 10;//!!!
	}
	
	
}
