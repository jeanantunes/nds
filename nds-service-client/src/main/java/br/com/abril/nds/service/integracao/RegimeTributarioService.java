package br.com.abril.nds.service.integracao;

import java.util.List;

import br.com.abril.nds.model.fiscal.notafiscal.RegimeTributario;

public interface RegimeTributarioService {
	
	List<RegimeTributario> obterRegimesTributarios();
	
}