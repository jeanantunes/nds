package br.com.abril.nds.service.impl;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorClassificacaoCotaRepository;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.RankingFaturamentoService;

public class ReclassificacaoCotaServiceImplTest {
	@Autowired
	CotaService cotaService; 
	
	
	@Autowired
	DistribuidorClassificacaoCotaRepository distribuidorClassificacaoCotaRepository;
	
	@Autowired
	RankingFaturamentoService rankingFaturamentoService;
	
	@Autowired
	CotaRepository cotaRepository;
	
	@Autowired
	ClassificacaoCotaServiceImpl classificacaoCotaService;
	
	
	@Test
	public void executarServico(){
		
		ClassificacaoCotaServiceImpl  eCompl = mock(ClassificacaoCotaServiceImpl.class);
		eCompl.executeReclassificacaoCota();
	}

}
