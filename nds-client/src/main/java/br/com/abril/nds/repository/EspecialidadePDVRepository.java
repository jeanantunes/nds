package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.pdv.EspecialidadePDV;

public interface EspecialidadePDVRepository extends Repository<EspecialidadePDV,Long> {
	
	List<EspecialidadePDV> obterEspecialidades(Long... codigos );
}
