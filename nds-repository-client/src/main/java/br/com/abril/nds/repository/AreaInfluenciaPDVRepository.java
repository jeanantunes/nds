package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;

public interface AreaInfluenciaPDVRepository extends Repository<AreaInfluenciaPDV, Long> {
	
	List<AreaInfluenciaPDV> obterAreaInfluenciaPDV(Long...codigos );

	public abstract List<AreaInfluenciaPDV> obterTodasAreaInfluenciaPDV();
	
}
