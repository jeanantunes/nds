package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.dne.Logradouro;

public interface LogradouroRepository extends Repository<Logradouro, Long> {

	List<Logradouro> pesquisarLogradouros(String nomeLogradouro);
}