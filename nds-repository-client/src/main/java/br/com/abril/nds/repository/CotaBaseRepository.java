package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBase;


public interface CotaBaseRepository extends Repository<CotaBase, Long> {

	FiltroCotaBaseDTO obterDadosFiltro(Integer numeroCota, boolean obterFaturamento);
	
	List<CotaBaseDTO> obterCotasBases(Cota cotaNova);

}
