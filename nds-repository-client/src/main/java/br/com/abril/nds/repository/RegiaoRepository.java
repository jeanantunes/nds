package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.model.distribuicao.Regiao;

public interface RegiaoRepository extends Repository<Regiao, Long> {
	
	List<RegiaoCotaDTO> buscarCotasPorSegmento (FiltroCotasRegiaoDTO filtro);
	
	List<RegiaoDTO> buscarRegiao();
	
	void execucaoQuartz ();
}
