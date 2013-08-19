package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.ReferenciaCota;

public interface ReferenciaCotaRepository extends Repository<ReferenciaCota, Long> {
	
	void excluirReferenciaCota(Long idBaseReferencia);
}
