package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBase;

public interface CotaBaseService {
	
	FiltroCotaBaseDTO obterDadosFiltro(Integer numeroCota, boolean obterFaturamento, boolean semCotaBase);
	
	List<CotaBaseDTO> obterCotasBases(Cota cotaNova);

	void salvar(CotaBase cotaBase);

}
