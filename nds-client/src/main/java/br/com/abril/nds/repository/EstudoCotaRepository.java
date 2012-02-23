package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.planejamento.EstudoCota;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.EstudoCota}  
 * 
 * @author Discover Technology
 *
 */
public interface EstudoCotaRepository extends Repository<EstudoCota, Long> {

	/**
	 * Obtém o estudo da cota de acordo com o
	 * número da cota e a data de referência do lançamento.
	 * 
	 * @param numeroCota - número da cota
	 * @param dataReferencia - data de referência
	 * 
	 * @return {@link EstudoCota}
	 */
	EstudoCota obterEstudoCota(Integer numeroCota, Date dataReferencia);
	
}
