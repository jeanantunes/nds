package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaAlteracaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroAlteracaoCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;

/**
 * 
 * @author Discover Technology
 */
public interface AlteracaoCotaRepository extends Repository<Cota, Long> {

	
	List<ConsultaAlteracaoCotaDTO> pesquisarAlteracaoCota(FiltroAlteracaoCotaDTO dto);
}
