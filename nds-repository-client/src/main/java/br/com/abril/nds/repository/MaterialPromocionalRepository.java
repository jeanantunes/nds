package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.MaterialPromocional;

public interface MaterialPromocionalRepository extends Repository<MaterialPromocional, Long> {

	List<MaterialPromocional> obterMateriaisPromocional(Long...codigos);
	
	List<MaterialPromocional> obterMateriaisPromocionalNotIn(Long...codigos);
}
