package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.service.ChamadaEncalheCotaService;

@Service
public class ChamadaEncalheCotaServiceImpl implements ChamadaEncalheCotaService {
	
//	private static final Logger LOGGER = LoggerFactory.getLogger(ChamadaEncalheCotaServiceImpl.class);
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository; 


	@Override
	public ChamadaEncalheCota obterChamadaEncalheCota(long cotaId,
			long produtoEdicaoId, Date dataRecolhimentoDistribuidor) {
		
		return chamadaEncalheCotaRepository.obterChamadaEncalheCota(cotaId, produtoEdicaoId, dataRecolhimentoDistribuidor);
	}
	
	
}