package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;

public interface RegiaoService {

	void salvarRegiao(Regiao regiao);
	
	List<RegiaoDTO> buscarRegiao();
	
	Regiao obterRegiaoPorId (Long Id);
	
	void addCotaNaRegiao (RegistroCotaRegiao registro);
	
	void excluirRegistroCotaRegiao(Long id);

	void excluirRegiao (Long id);
	
	void alterarRegiao (Regiao regiao);
	
	List<RegiaoCotaDTO> carregarCotasRegiao (FiltroCotasRegiaoDTO filtro);
	
	List<RegiaoCotaDTO> buscarPorCEP (FiltroCotasRegiaoDTO filtro);
	
	List<TipoSegmentoProduto> carregarSegmentos();
	
//	List<RegiaoCotaDTO> carregarTodasCotasDaRegiao ();

}
