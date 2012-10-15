package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;

/**
 * @author Discover Technology
 */
public interface AlteracaoCotaService {

	List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota(FiltroAlteracaoCotaDTO filtroAlteracaoCotaDTO); 
	
	
}
