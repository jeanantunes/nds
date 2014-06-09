package br.com.abril.nds.repository;

import br.com.abril.nds.model.fiscal.nota.Serie;

public interface SerieRepository extends Repository<Serie, Integer> {
	
	/**
	 * Recupera o proximo numero da serie.
	 * 
	 * @param numeroSerie Numero da serie.
	 * @return
	 */
	public abstract  Long next(int numeroSerie);

	abstract Long obterNumeroSerieNota();

}
