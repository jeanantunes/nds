package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ControleCotaDTO;
import br.com.abril.nds.model.cadastro.ControleCota;

public interface ControleCotaRepository extends Repository<ControleCota, Long> {
	
	
	
	List<ControleCotaDTO> buscarControleCota();
	
	
	
}
