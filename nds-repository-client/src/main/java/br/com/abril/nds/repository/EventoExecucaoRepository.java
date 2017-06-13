package br.com.abril.nds.repository;

import br.com.abril.nds.model.integracao.EventoExecucao;

/**
 * Repositório de dados referentes ao acumulo de dívidas.
 * 
 * @author Discover Technology
 *
 */
public interface EventoExecucaoRepository extends Repository<EventoExecucao, Long> {

	EventoExecucao findByNome(String nome);
	
}
