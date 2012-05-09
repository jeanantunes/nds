package br.com.abril.nds.repository;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.EnderecoTransportador;

public interface EnderecoTransportadorRepository extends Repository<EnderecoTransportador, Long> {

	EnderecoTransportador buscarEnderecoPorEnderecoTransportador(Long idEndereco, Long idTransportador);

	void removerEnderecosTransportador(Set<Long> listaEnderecosRemover);

	void excluirEnderecosPorIdTransportador(Long id);

	List<EnderecoAssociacaoDTO> buscarEnderecosTransportador(Long id, Set<Long> idsIgnorar);

	boolean verificarEnderecoPrincipalTransportador(Long id,
			Set<Long> idsIgnorar);
}