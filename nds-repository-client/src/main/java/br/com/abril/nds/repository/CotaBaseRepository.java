package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.CotaBaseHistoricoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.model.cadastro.CotaBase;


public interface CotaBaseRepository extends Repository<CotaBase, Long> {

	FiltroCotaBaseDTO obterDadosFiltro(CotaBase cotaBase, boolean obterFaturamento, boolean semCotaBase, Integer numeroCota);
	
	List<CotaBaseDTO> obterCotasBases(CotaBase cotaBase, CotaBaseDTO dto);

	CotaBase obterCotaNova(Integer numeroCotaNova);

	FiltroCotaBaseDTO obterCotaDoFiltro(CotaBase cotaBase);

	List<CotaBaseHistoricoDTO> obterCotasHistorico(CotaBase cotaBase, CotaBaseDTO dto);

	List<CotaBaseDTO> obterListaCotaPesquisaGeral(CotaBaseDTO dto);

	List<CotaBaseDTO> obterListaTelaDetalhe(CotaBase cotaBase);

}
