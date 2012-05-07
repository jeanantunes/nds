package br.com.abril.nds.repository;

import java.util.Set;

import br.com.abril.nds.model.cadastro.AssociacaoVeiculoMotoristaRota;

public interface AssociacaoVeiculoMotoristaRotaRepository extends
		Repository<AssociacaoVeiculoMotoristaRota, Long> {

	void removerAssociacaoPorId(Set<Long> ids);
}