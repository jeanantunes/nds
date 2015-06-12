package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;




import br.com.abril.nds.dto.DeparaDTO;


import br.com.abril.nds.model.cadastro.Depara;
import br.com.abril.nds.util.Intervalo;

public interface DeparaService {

	
	
	List<DeparaDTO> buscarDepara();
	void salvarDepara(Depara depara);
	void excluirDepara(Long id);
	Depara obterDeparaPorId(Long idDepara);
	void alterarDepara(Depara depara);
	String obterBoxDinap(String boxfc);
	
	
}
