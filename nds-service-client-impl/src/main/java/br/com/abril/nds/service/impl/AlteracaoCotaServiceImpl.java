package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.repository.AlteracaoCotaRepository;
import br.com.abril.nds.service.AlteracaoCotaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Cota}
 * 
 * @author Discover Technology
 */
@Service
public class AlteracaoCotaServiceImpl implements AlteracaoCotaService {

	@Autowired
	private AlteracaoCotaRepository alteracaoCotaRepository;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private FornecedorService fornecedorService;


	@Override
	@Transactional(readOnly = true)
	public List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO) {
		return alteracaoCotaRepository.pesquisarAlteracaoCota(filtroAlteracaoCotaDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public int contarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO) {
		return alteracaoCotaRepository.contarAlteracaoCota(filtroAlteracaoCotaDTO);
	}

	

	@Override
	@Transactional(readOnly = true)
	public FiltroAlteracaoCotaDTO preencherFiltroAlteracaoCotaDTO(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO) {
	
		return filtroAlteracaoCotaDTO;
	}
	
		
}