package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.Veiculo;

public interface VeiculoRepository extends Repository<Veiculo, Long> {

	void removerPorId(Long idVeiculo);
}