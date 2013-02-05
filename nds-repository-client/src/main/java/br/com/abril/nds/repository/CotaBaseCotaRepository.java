package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBaseCota;

public interface CotaBaseCotaRepository extends Repository<CotaBaseCota,Long>{
	
	Long verificarExistenciaCotaBaseCota(Cota cota);

	boolean isCotaBaseAtiva(Cota cota);
	 

}
