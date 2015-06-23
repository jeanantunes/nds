package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.service.ChamadaEncalheCotaService;

@Service
public class ChamadaEncalheCotaServiceImpl implements ChamadaEncalheCotaService {
	
//	private static final Logger LOGGER = LoggerFactory.getLogger(ChamadaEncalheCotaServiceImpl.class);
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository; 

	@Autowired
	 private ChamadaEncalheRepository chamadaEncalheRepository;

	@Override
	public ChamadaEncalheCota obterChamadaEncalheCota(long cotaId,
			long produtoEdicaoId, Date dataRecolhimentoDistribuidor) {
		
		return chamadaEncalheCotaRepository.obterChamadaEncalheCota(cotaId, produtoEdicaoId, dataRecolhimentoDistribuidor);
	}
	
	@Override
	 @Transactional
	 public Set<Lancamento> obterLancamentos(ChamadaEncalhe chamadaEncalhe) {
	  
	  return chamadaEncalheRepository.obterLancamentos(chamadaEncalhe.getId());
	  
	 }
	
	
}