package br.com.abril.nds.repository;

import java.util.List;


import br.com.abril.nds.dto.DeparaDTO;

import br.com.abril.nds.model.cadastro.Depara;

public interface DeparaRepository extends Repository<Depara, Long> {
	
	
	
	List<DeparaDTO> buscarDepara();
	String obterBoxDinap(String boxfc);
	
}
