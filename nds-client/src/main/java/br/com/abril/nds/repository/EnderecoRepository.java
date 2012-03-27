package br.com.abril.nds.repository;

import java.util.Collection;

import br.com.abril.nds.model.cadastro.Endereco;

public interface EnderecoRepository extends Repository<Endereco, Long> {

	void removerEnderecos(Collection<Long> idsEndereco);
}
