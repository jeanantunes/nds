package br.com.abril.nds.server.repository;

import java.util.List;

import br.com.abril.nds.repository.Repository;
import br.com.abril.nds.server.model.Indicador;

public interface IndicadorRepository extends Repository<Indicador, Long> {

	List<Indicador> buscarIndicadores();
}