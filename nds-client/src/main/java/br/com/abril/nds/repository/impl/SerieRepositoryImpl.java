package br.com.abril.nds.repository.impl;

import br.com.abril.nds.model.fiscal.nota.Serie;
import br.com.abril.nds.repository.SerieRepository;

public class SerieRepositoryImpl extends AbstractRepositoryModel<Serie, Integer>
		implements SerieRepository {

	public SerieRepositoryImpl() {
		super(Serie.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.SerieRepository#next(int)
	 */
	@Override	
	public synchronized Long next(int numeroSerie){
		
		Serie serie = buscarPorId(numeroSerie);
		
		if(serie == null){
			serie = new Serie(numeroSerie, 0L);
		}		
		serie.next();		
		
		merge(serie);		
		return serie.getCurrentValue();		
	}
}
