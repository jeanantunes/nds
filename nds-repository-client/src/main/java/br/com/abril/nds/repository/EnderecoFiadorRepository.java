package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoFiador;

public interface EnderecoFiadorRepository extends Repository<EnderecoFiador, Long> {

	List<EnderecoAssociacaoDTO> buscaEnderecosFiador(Long idFiador,	Set<Long> idsIgnorar);

	List<Endereco> buscarEnderecosPessoaPorFiador(Long idFiador);

	EnderecoFiador buscarEnderecoPorEnderecoFiador(Long idEndereco, Long idFiador);

	void excluirEnderecosPorIdFiador(Long idFiador);

	void excluirEnderecosFiador(Collection<Long> idsEnderecoFiador);

	boolean verificarEnderecoPrincipalFiador(Long id, Set<Long> idsIgnorar);

	public abstract Endereco buscaPrincipal(Long idFiador);
}