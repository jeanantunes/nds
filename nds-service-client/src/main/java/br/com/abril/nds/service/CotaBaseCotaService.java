package br.com.abril.nds.service;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.CotaBase;
import br.com.abril.nds.model.cadastro.CotaBaseCota;

public interface CotaBaseCotaService {

	void salvar(CotaBaseCota cotaBaseCota);

	Long verificarExistenciaCotaBaseCota(Cota cota);
	
	/*
	 * Verifica se a cota passada está com cotas bases ativas.
	 * Regra: Após 180 dias do cadastro de cota base para uma cota nova, a cota nova não vai ter mais cotas base amarrado a elas.
	 * 
	 */	
	boolean isCotaBaseAtiva(CotaBase cotaBase, Integer[] numerosDeCotasBase);

	boolean isCotaBaseAtiva(CotaBase cotaBase);

	/*
	 * Coloca um status pra false. Assim podemos ter um histórico
	 * 
	 */	
	void desativarCotaBase(CotaBase cotaBase, Cota cotaParaDesativar);

	Long quantidadesDeCotasAtivas(CotaBase cotaBase);	
	
}
