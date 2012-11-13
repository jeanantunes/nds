package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.dne.Localidade;


public interface LocalidadeRepository extends Repository<Localidade, String> {

	List<Localidade> obterListaLocalidadeCotas();
	
	List<Localidade> pesquisarLocalidades(String nomeLocalidade);

}
