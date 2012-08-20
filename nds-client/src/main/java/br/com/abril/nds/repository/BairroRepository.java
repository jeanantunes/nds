package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.dne.Bairro;

public interface BairroRepository extends Repository<Bairro, Long>{

	List<Bairro> pesquisarBairros(String nomeBairro);
}