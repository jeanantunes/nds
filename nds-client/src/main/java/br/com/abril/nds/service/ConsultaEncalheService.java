package br.com.abril.nds.service;

import br.com.abril.nds.dto.InfoConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;

public interface ConsultaEncalheService {

	public InfoConsultaEncalheDTO pesquisarEncalhe(FiltroConsultaEncalheDTO filtro);
	
}
