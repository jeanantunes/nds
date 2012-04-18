package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.service.ConsolidadoFinanceiroService;
import br.com.abril.nds.service.FornecedorService;

@Service
public class ConsolidadoFinanceiroServiceImpl implements ConsolidadoFinanceiroService {
	
	@Autowired
	ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Autowired
	FornecedorService fornecedorService;
	
	@Transactional(readOnly=true)
	public List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(FiltroConsolidadoEncalheCotaDTO filtro){		
		return consolidadoFinanceiroRepository.obterMovimentoEstoqueCotaEncalhe(filtro);		
	}
	
	@Transactional(readOnly=true)
	public List<ConsignadoCotaDTO> obterMovimentoEstoqueCotaConsignado(FiltroConsolidadoConsignadoCotaDTO filtro){
		return consolidadoFinanceiroRepository.obterMovimentoEstoqueCotaConsignado(filtro);
	}
}
