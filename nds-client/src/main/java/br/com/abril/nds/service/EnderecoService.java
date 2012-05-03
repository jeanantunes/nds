package br.com.abril.nds.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Endereco;

public interface EnderecoService {

	void removerEndereco(Endereco endereco);

	Endereco salvarEndereco(Endereco endereco);
	
	Endereco obterEnderecoPorId(Long idEndereco);

	void cadastrarEnderecos(List<EnderecoAssociacaoDTO> listaEnderecos);
	
	void removerEnderecos(Collection<Long> idsEndereco);

	Endereco buscarEnderecoPorId(Long idEndereco);

	List<EnderecoAssociacaoDTO> buscarEnderecosPorIdPessoa(Long idPessoa, Set<Long> idsIgnorar);
}
