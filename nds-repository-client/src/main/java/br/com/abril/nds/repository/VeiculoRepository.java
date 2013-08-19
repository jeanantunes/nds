package br.com.abril.nds.repository;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.Veiculo;

public interface VeiculoRepository extends Repository<Veiculo, Long> {

	public List<Veiculo> buscarVeiculosPorTransportador(Long idTransportador, Set<Long> idsIgnorar,
			String sortname, String sortorder);
	
	void removerPorId(Long idVeiculo);

	public void removerVeiculos(Long idTransportador, Set<Long> listaVeiculosRemover);
}