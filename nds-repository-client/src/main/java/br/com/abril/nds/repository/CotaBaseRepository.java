package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.model.cadastro.CotaBase;


public interface CotaBaseRepository extends Repository<CotaBase, Long> {

	FiltroCotaBaseDTO obterDadosFiltro(CotaBase cotaBase, boolean obterFaturamento, boolean semCotaBase, Integer numeroCota);
	
	List<CotaBaseDTO> obterCotasBases(CotaBase cotaBase);

	CotaBase obterCotaNova(Integer numeroCotaNova);

	FiltroCotaBaseDTO obterCotaDoFiltro(CotaBase cotaBase);

}
