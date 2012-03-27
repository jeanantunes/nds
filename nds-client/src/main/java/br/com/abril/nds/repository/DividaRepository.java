package br.com.abril.nds.repository;

import br.com.abril.nds.model.financeiro.Divida;

public interface DividaRepository extends Repository<Divida, Long>{

	Divida obterUltimaDividaPorCota(Long idCota);
}
