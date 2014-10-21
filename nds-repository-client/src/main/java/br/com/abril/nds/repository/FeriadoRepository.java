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
	 * @param localidade
	 * 
	 * @return {@link List<Feriado>}
	 */
	public List<Feriado> obterFeriados(Date data, TipoFeriado tipoFeriado,
			String uf, String localidade);
	

	/**
	 * Obtem lista de feriados relativos a uma data específica ou um feriado
	 * anual cujo dia e mês sejam compatíveis com os parêmetros de pesquisa.
	 * 
	 * @param dataFeriado
	 * 
	 * @return {@link List<CalendarioFeriadoDTO>}
	 */
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoDataEspecifica(
			Date dataFeriado);

	/**
	 * Obtém lista de feriados referente ao mês e ano passados como parâmetros
	 * assim com os feriados dos mêses de todos os anos (caso este esteja
	 * marcados como feriados anuais).
	 * 
	 * @param mes
	 * @param ano
	 * 
	 * @return {@link List<CalendarioFeriadoDTO>}
	 */
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoMensal(
			int mes, int ano);

	/**
	 * Obtém lista de datas de feriados relativos a um período específico assim
	 * como feriados flageados como anuais.
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * 
	 * @return {@link List<CalendarioFeriadoDTO>}
	 */
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoPeriodo(
			Date dataInicial, Date dataFinal);

	/**
	 * Recupera um feriado anual com base na data recebida
	 * 
	 * @param data
	 *            data base para a recuperação do feriado anual, serão
	 *            utilizados como base apenas o dia e mês da data
	 * @param tipo
	 *            tipo de feriado para pesquisa
	 * @return {@link Feriado} que corresponde ao feriado anual cadastrado ou
	 *         null caso não exista feriado correspondente cadastrado
	 */
	public Feriado obterFeriadoAnualTipo(Date data, TipoFeriado tipo);
	
	/**
	 * Obtém feriado anual com base na data e na localidade
	 * 
	 * @param data
	 * @param localidade
	 * @return
	 */
	public Feriado obterFeriadoAnualLocalidade(Date data, String localidade);

	boolean isFeriado(Date data);

    public abstract boolean isNaoOpera(Date data);

    public abstract boolean isFeriado(Date data, String localidade);

    public abstract boolean isOpera(Date data);

    public abstract boolean isFeriadoComOperacao(Date data, String localidade);

    public abstract boolean isNaoOpera(Date data, String localidade);
}
