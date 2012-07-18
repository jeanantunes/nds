package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CalendarioFeriadoDTO;
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
	
	
	/**
	 * Obtem lista de feriados relativos a uma data específica 
	 * ou um feriado anual cujo dia e mês sejam compatíveis com os parêmetros 
	 * de pesquisa.
	 * 
	 * @param dataFeriado
	 * 
	 * @return {@link List<CalendarioFeriadoDTO>}
	 */
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriado(Date dataFeriado);
	
	
	/**
	 * Obtém lista de datas de feriados relativos a um período específico assim como 
	 * feriados flageados como anuais.
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * 
	 * @return {@link List<Date>}
	 */
	public List<CalendarioFeriadoDTO> obterListaDataFeriado(Date dataInicial, Date dataFinal);

	
}
