package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.distribuicao.regiao}
 *
 */
public interface RegiaoService {

	void salvarRegiao(Regiao regiao);
	
	void excluirRegiao (Long id);
	
	List<RegiaoDTO> buscarRegiao();
	
	List<RegiaoCotaDTO> carregarCotasRegiao (Long id);

	void excluirRegistroCotaRegiao(Long id);
	
	List<RegiaoCotaDTO> buscarPorCEP (FiltroCotasRegiaoDTO filtro);
	
	void addCotaNaRegiao (RegistroCotaRegiao registro);

	Regiao obterRegiaoPorId (Long Id);
	
}
