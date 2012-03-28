package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.model.financeiro.Divida;

public interface DividaRepository extends Repository<Divida, Long>{

	Divida obterUltimaDividaPorCota(Long idCota);
	
	/**
	 * Retorna as dividas geradas conforme parametros informados no filtro.
	 * @param filtro
	 * @return List<GeraDividaDTO> 
	 */
	List<GeraDividaDTO> obterDividasGeradas(FiltroDividaGeradaDTO filtro);
}
