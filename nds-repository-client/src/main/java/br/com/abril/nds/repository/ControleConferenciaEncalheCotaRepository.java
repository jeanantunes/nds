package br.com.abril.nds.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;

public interface ControleConferenciaEncalheCotaRepository  extends Repository<ControleConferenciaEncalheCota,Long> {
	
	/**
	 * Obtém uma lista de datas em que foram 
	 * realizadas finalização de conferência
	 * de encalhe de acordo com o range de 
	 * datas e cota informados.
	 * 
	 * @param idCota
	 * @param dataDe
	 * @param dataAte
	 * 
	 * @return List<Date>
	 */
	public List<Date> obterDatasControleConferenciaEncalheCotaFinalizada(Long idCota, Date dataDe, Date dataAte);
	
	/**
	 * Obtém registro de ControleConferenciaEncalheCota referente a uma 
	 * cota e dataOperação.
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * 
	 * @return ControleConferenciaEncalheCota
	 */
	public ControleConferenciaEncalheCota obterControleConferenciaEncalheCota(Integer numeroCota, Date dataOperacao);

	/**
	 * Obtém todas as ControleConferenciaEncalheCota pelo FiltroConsultaEncalheDTO.
	 * 
	 * @param filtro
	 * @return List<ControleConferenciaEncalheCota>
	 */
	public List<ControleConferenciaEncalheCota> obterControleConferenciaEncalheCotaPorFiltro(FiltroConsultaEncalheDTO filtro);	
	
	/**
	 * Obtém o status do registro de controleConferenciaEncalheCota.
	 * 
	 * @param idControleConferenciaEncalheCota
	 * 
	 * @return StatusOperacao
	 */
	public StatusOperacao obterStatusControleConferenciaEncalheCota(Long idControleConferenciaEncalheCota);

	/**
	 * Obtém lista de ids de controle conferencia de encalhe da cota associados
	 * a chamada de encalhe dentro do periodo de tempo, fornecedor, e cota pesquisados.
	 * 
	 * @param filtro
	 * 
	 * @return List - Long
	 */
	public List<Long> obterListaIdControleConferenciaEncalheCota(FiltroConsultaEncalheDTO filtro);
	
	/**
	 * Obtém a flag para sinalizar se aceita Juramentado 
	 * cota e dataOperação.
	 * 
	 * @param idCota
	 * @return boolean
	 */
	public boolean obterAceitaJuramentado(Long idCota);

	/**
	 * Verifica se a cota possui conferencia de encalhe finalizada na data
	 * @param idCota
	 * @param dataOperacao
	 * @return boolean
	 */
	boolean isConferenciaEncalheCotaFinalizada(Long idCota, Date dataOperacao);

	/**
     * Obtém ControleConferenciaEncalheCota por Cobrança
     * @param idCobranca
     * @return ControleConferenciaEncalheCota
     */
	ControleConferenciaEncalheCota obterControleConferenciaEncalheCotaPorIdCobranca(Long idCobranca);

	List<Integer> obterListaNumCotaConferenciaEncalheCota(FiltroConsultaEncalheDTO filtro);
}
