package br.com.abril.nds.service;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.CalendarioFeriadoDTO;
import br.com.abril.nds.util.export.FileExporter.FileType;

/**
 * Interface que define serviços referentes a funcionalidades de calendário.
 * 
 * @author Discover Technology
 */
public interface CalendarioService {

	/**
	 * Adiciona dias úteis a uma data.
	 * 
	 * @param data
	 *            - data a ser adicionada
	 * @param numDias
	 *            - número de dias
	 * 
	 * @return nova data calculada
	 */
	Date adicionarDiasUteis(Date data, int numDias);

	/**
	 * Adiciona dias a uma data e retorna a a data, caso a mesma seja válida,
	 * caso contrário, retorna a próxima data válida.
	 * 
	 * @param data
	 *            - data a ser adicionada
	 * @param numDias
	 *            - número de dias
	 * 
	 * @return nova data calculada
	 */
	Date adicionarDiasRetornarDiaUtil(Date data, int numDias);

	/**
	 * Subtrai dias úteis a uma data.
	 * 
	 * @param data
	 *            - data a ser subtraída
	 * @param numDias
	 *            - número de dias
	 * 
	 * @return nova data calculada
	 */
	Date subtrairDiasUteis(Date data, int numDias);

	/**
	 * Verifica se a data informada é dia útil.
	 * 
	 * @param data
	 *            - data para verificação
	 * 
	 * @return indicação se a data é dia útil
	 */
	boolean isDiaUtil(Date data);

	/**
	 * Adiciona dias úteis a uma data.
	 * 
	 * @param data
	 *            - data a ser adicionada
	 * @param numDias
	 *            - número de dias
	 * @param diasSemanaConcentracaoCobranca
	 *            - dias da semana onde a data deve cair
	 * @param diaMesConcentracaoCobranca
	 *            - dia do mes onde a data deve cair
	 * 
	 * @return nova data calculada
	 */
	Date adicionarDiasUteis(Date data, int numDias,
			List<Integer> diasSemanaConcentracaoCobranca,
			List<Integer> diaMesConcentracaoCobranca);

	/**
	 * Cadastro novo feriado.
	 * 
	 * @param calendarioFeriado
	 */
	void cadastrarFeriado(CalendarioFeriadoDTO calendarioFeriado);

	/**
	 * Obtém lista dos feriados cadastrado para determinada data.
	 * 
	 * @param dataFeriado
	 * 
	 * @return {@link List<CalendarioFeriadoDTO>}
	 */
	List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoDataEspecifica(
			Date dataFeriado);

	/**
	 * Obtém mapa de datas com feriados cadastrados.
	 * 
	 * @param anoVigencia
	 * 
	 * @return Map<Date, String>
	 */
	public Map<Date, String> obterListaDataFeriado(int anoVigencia);

	public List<String> obterListaLocalidadePdv();

	/**
	 * Exclui o Feriado
	 * 
	 * @param idFeriado
	 */
	public void excluirFeriado(Long idFeriado);

	/**
	 * Obtém os feriados do mes de um determinado ano
	 * 
	 * @param mes
	 * @param ano
	 * @return
	 */
	public List<CalendarioFeriadoDTO> obterListaCalendarioFeriadoMensal(
			int mes, int ano);

	
	/**
	 * Obtém os Feriado de um determinado Ano.
	 * 
	 * @param ano
	 * @return
	 */
	public List<CalendarioFeriadoDTO> obterFeriadosPorAno(int ano);
	
	/**
	 * Obtém o relatorio jasper do calendario feriado
	 * 
	 * @param fileType
	 * @param tipoPesquisaFeriado
	 * @param mes
	 * @param ano
	 * @param logoDistribuidor
	 * @return
	 */
	public byte[] obterRelatorioCalendarioFeriado(FileType fileType,
			TipoPesquisaFeriado tipoPesquisaFeriado, int mes, int ano,InputStream logoDistribuidor);

	/**
	 * Verifica se a data possui feriados que não operam
	 * 
	 * @param data
	 * @return
	 */
	boolean isFeriadoSemOperacao(Date data);
	
	/**
	 * Verifica se a data possui feriados Municipais que não operam
	 * 
	 * @param data
	 * @return
	 */
	boolean isFeriadoMunicipalSemOperacao(Date data);
	
	public enum TipoPesquisaFeriado {
		FERIADO_MENSAL, FERIADO_ANUAL;
	}

}