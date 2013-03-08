package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.model.distribuicao.Regiao;

public interface RegiaoRepository extends Repository<Regiao, Long> {
	
//	List<RegiaoCotaDTO> carregarCotasRegiao (FiltroCotasRegiaoDTO filtro);
	
	List<RegiaoDTO> buscarRegiao();
	
//	List<RegiaoCotaDTO> buscarTodasCotasDaRegiao ();
	
//	List<RegiaoCotaDTO> buscarPorCEP (FiltroCotasRegiaoDTO filtro);
	
//	List <TipoSegmentoProdutoDTO> carregarSegmentos();

}
