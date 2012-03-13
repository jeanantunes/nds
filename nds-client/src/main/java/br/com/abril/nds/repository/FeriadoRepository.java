package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.cadastro.Feriado;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Feriado}
 * 
 * @author Discover Technology
 */
public interface FeriadoRepository extends Repository<Feriado, Long> {
	
	/**
	 * Obtém um feriado de acordo com a data informada.
	 * 
	 * @param data - data a ser encontrada
	 * 
	 * @return {@link Feriado}
	 */
	Feriado obterPorData(Date data);
	
}
