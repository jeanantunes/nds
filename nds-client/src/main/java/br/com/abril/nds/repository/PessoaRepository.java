package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaJuridica;

public interface PessoaRepository extends Repository<Pessoa, Long> {
	
	List<Pessoa> buscarPorNome(String nome);
	
	List<PessoaJuridica> buscarPorCnpj(String cnpj);

}
