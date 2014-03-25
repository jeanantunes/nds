package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBase;
import br.com.abril.nds.model.cadastro.CotaBaseCota;

public interface CotaBaseCotaRepository extends Repository<CotaBaseCota,Long>{
	
	Long verificarExistenciaCotaBaseCota(Cota cota);

	boolean isCotaBaseAtiva(CotaBase cotaBase, Integer[] numerosDeCotasBase);

	CotaBaseCota desativarCotaBase(CotaBase cotaBase, Cota cotaParaDesativar);

	Long quantidadesDeCotasAtivas(CotaBase cotaBase);

}
