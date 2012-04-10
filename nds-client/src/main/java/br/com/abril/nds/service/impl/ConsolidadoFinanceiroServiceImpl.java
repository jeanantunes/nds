package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.service.ConsolidadoFinanceiroService;

@Service
public class ConsolidadoFinanceiroServiceImpl implements ConsolidadoFinanceiroService {
	
	@Autowired
	ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Transactional(readOnly=true)
	public List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(FiltroConsolidadoEncalheCotaDTO filtro){
		return consolidadoFinanceiroRepository.obterMovimentoEstoqueCotaEncalhe(filtro);		
	}
}
