package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.filtro.FiltroStatusCotaDTO;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;

/**
 * Interface para o repositório de histórico de status da cota.
 * 
 * @author Discover Technology
 *
 */
public interface HistoricoSituacaoCotaRepository extends Repository<HistoricoSituacaoCota, Long> {
	
	/**
	 * Obtém o histórico dos status da cota de acordo com o filtro de pesquisa.
	 * 
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return Histórico dos status da cota
	 */
	List<HistoricoSituacaoCota> obterHistoricoStatusCota(FiltroStatusCotaDTO filtro);
	
	/**
	 * Obtém o total do histórico de status da cota de acordo
	 * com o filtro de pesquisa.
	 * 
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return Total de registros
	 */
	Long obterTotalHistoricoStatusCota(FiltroStatusCotaDTO filtro);
	
	/**
	 * Retorna o ultimo histórico de status da cota inativo 
	 * 
	 * @param numeroCota - número da cota
	 * 
	 * @return HistoricoSituacaoCota 
	 */
	HistoricoSituacaoCota obterUltimoHistoricoInativo(Integer numeroCota);

	/**
	 * Busca última suspensão de cota realizada em determinado dia
	 * @param dataOperacao
	 * @return HistoricoSituacaoCota
	 */
	public Date buscarUltimaSuspensaoCotasDia(Date dataOperacao);

	/**
	 * Busca data da última suspensão de cotas realizada
	 * @return Date
	 */
	public Date buscarDataUltimaSuspensaoCotas();

	/**
	 * 
	 * @param filtro - filtro de pesquisa
	 * 
	 * @return List<HistoricoSituacaoCota>
	 */
	List<HistoricoSituacaoCota> obterUltimoHistoricoStatusCota(FiltroStatusCotaDTO filtro);
	
	Long obterTotalUltimoHistoricoStatusCota(FiltroStatusCotaDTO filtro);
}
