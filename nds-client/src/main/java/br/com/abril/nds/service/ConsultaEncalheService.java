package br.com.abril.nds.service;

import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;

public interface ConsultaEncalheService {

	ConsultaEncalheDTO pesquisarEncalhe(FiltroConsultaEncalheDTO filtro);
	
}
