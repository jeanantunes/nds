package br.com.abril.nds.service.integracao;

import java.util.List;

import br.com.abril.nds.dto.TributoAliquotaDTO;
import br.com.abril.nds.model.cadastro.RegimeTributario;

public interface RegimeTributarioService {
	
	List<RegimeTributario> obterRegimesTributarios();

	List<TributoAliquotaDTO> obterTributosPeloRegimeTributario(Long regimeTributarioId);
	
}