package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.Fiador;

public interface FiadorRepository extends Repository<Fiador, Long> {

	Fiador obterFiadorPorCpf(String cpf);
	
	Fiador obterFiadorPorCnpj(String cnpj);
}