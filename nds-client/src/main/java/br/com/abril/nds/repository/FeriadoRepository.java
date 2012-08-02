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
	 * Obtem lista de feriados relativos a uma data específica 
	 * ou um feriado anual cujo dia e mês sejam compatíveis com os parêmetros 
	 * de pesquisa.
	 * 
	 * @param dataFeriado
	 * 
	 * @return {@link List<CalendarioFeriadoDTO>}
	 */
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoDataEspecifica(Date dataFeriado);
	
	
	/**
	 * Obtém lista de feriados referente ao mês e ano passados como parâmetros
	 * assim com os feriados dos mêses de todos os anos (caso este esteja marcados
	 * como feriados anuais).
	 * 
	 * @param mes
	 * @param ano
	 * 
	 * @return {@link List<CalendarioFeriadoDTO>}
	 */
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoMensal(int mes, int ano);

	
	/**
	 * Obtém lista de datas de feriados relativos a um período específico assim como 
	 * feriados flageados como anuais.
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * 
	 * @return {@link List<CalendarioFeriadoDTO>}
	 */
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoPeriodo(Date dataInicial, Date dataFinal);

	
}
