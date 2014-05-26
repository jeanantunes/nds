package br.com.abril.nds.repository;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.model.cadastro.AssociacaoVeiculoMotoristaRota;

public interface AssociacaoVeiculoMotoristaRotaRepository extends
		Repository<AssociacaoVeiculoMotoristaRota, Long> {

	void removerAssociacaoPorId(Set<Long> ids);

	void removerAssociacaoTransportador(Long id);

	List<AssociacaoVeiculoMotoristaRota> buscarAssociacoesTransportador(
			Long idTransportador, Set<Long> idsIgnorar, String sortname, String sortorder);

	List<Long> buscarIdsRotasPorAssociacao(Set<Long> assocRemovidas);

	boolean verificarAssociacaoMotorista(Long idMotorista, Set<Long> idsIgnorar);

	boolean verificarAssociacaoVeiculo(Long idVeiculo, Set<Long> idsIgnorar);

	boolean verificarAssociacaoRotaRoteiro(Long idRota);
	
	Long obterQuantidadeAssociaoesDaCota(Integer numeroCota);
}