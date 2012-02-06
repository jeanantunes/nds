package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.Pessoa;

public interface PessoaRepository extends Repository<Pessoa, Long> {
	
	List<Pessoa> buscarPorNome(String nome);

}
