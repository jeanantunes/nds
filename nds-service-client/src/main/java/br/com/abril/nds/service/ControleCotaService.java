package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ControleCotaDTO;
import br.com.abril.nds.model.cadastro.ControleCota;
import br.com.abril.nds.model.cadastro.Depara;

public interface ControleCotaService {

	
	
	List<ControleCotaDTO> buscarControleCota();
	void salvarControleCota(ControleCota controleCota);
	void excluirControleCota(Long id);
	ControleCota obterControleCotaPorId(Long id);
	void alterarControleCota(ControleCota controleCota);
	
	void salvarCotaUnificacao(Integer numeroCotaCentralizadora, 
            List<Integer> numeroCotasCentralizadas);
	
	
	Integer buscarCotaMaster(Integer cota);
	
}
