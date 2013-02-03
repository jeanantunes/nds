package br.com.abril.nds.service;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBaseCota;

public interface CotaBaseCotaService {

	void salvar(CotaBaseCota cotaBaseCota);

	Long verificarExistenciaCotaBaseCota(Cota cota);

}
