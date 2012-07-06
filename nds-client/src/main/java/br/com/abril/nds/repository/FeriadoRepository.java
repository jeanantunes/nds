package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.TipoFeriado;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Feriado}
 * 
 * @author Discover Technology
 */
public interface FeriadoRepository extends Repository<Feriado, Long> {
	
	/**
	 * Obtém lista feriados de acordo parâmetros passados.
	 * 
	 * @param data
	 * @param tipoFeriado
	 * @param uf
	 * @param idLocalidade
	 * 
	 * @return  {@link List<Feriado>}
	 */
	public List<Feriado> obterFeriados(Date data, TipoFeriado tipoFeriado, String uf, Long idLocalidade);
	
	/**
	 * Obtém registro de feriado de acordo cuja contraint 
	 * seja igual ao parâmetros passados.
	 * 
	 * @param data
	 * @param tipoFeriado
	 * @param uf
	 * @param idLocalidade
	 * 
	 * @return {@link Feriado}
	 */
	public Feriado obterFeriado(Date data, TipoFeriado tipoFeriado, String uf, Long idLocalidade);
	
	
}
