package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.SocioCota;

public interface SocioCotaRepository extends Repository<SocioCota, Long> {
	
	List<SocioCota> obterSocioCotaPorIdCota(Long idCota);
	
	boolean existeSocioPrincipalCota(Long idCota);
	
}
