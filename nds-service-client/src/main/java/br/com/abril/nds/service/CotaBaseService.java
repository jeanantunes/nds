package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaBaseDTO;
import br.com.abril.nds.dto.CotaBaseHistoricoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.model.cadastro.CotaBase;

public interface CotaBaseService {
	
	FiltroCotaBaseDTO obterDadosFiltro(CotaBase cotaBase, boolean obterFaturamento, boolean semCotaBase, Integer numeroCota);
	
	List<CotaBaseDTO> obterCotasBases(CotaBase cotaBase, CotaBaseDTO dto);

	void salvar(CotaBase cotaBase);

	CotaBase obterCotaNova(Integer numeroCota, Boolean ativo);

	FiltroCotaBaseDTO obterCotaDoFiltro(CotaBase cotaBase);

	void atualizar(CotaBase cotaBaseJaSalva);

	List<CotaBaseHistoricoDTO> obterCotasHistorico(CotaBase cotaBase, CotaBaseDTO dto);
	
	List<CotaBaseDTO>  obterListaCotaPesquisaGeral(CotaBaseDTO dto);

	List<CotaBaseDTO> obterListaTelaDetalhe(CotaBase cotaBase);

}
