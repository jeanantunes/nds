package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Pessoa;

public interface PessoaRepository extends Repository<Pessoa, Long> {
	
	List<Pessoa> buscarPorNome(String nome);
	
	List<Pessoa> buscarPorCnpj(String cnpj);

}
