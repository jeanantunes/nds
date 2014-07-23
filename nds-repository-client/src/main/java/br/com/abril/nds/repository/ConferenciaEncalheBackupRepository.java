package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.model.estoque.ConferenciaEncalheBackup;

public interface ConferenciaEncalheBackupRepository extends Repository<ConferenciaEncalheBackup, Long> {
	
	/**
	 * Retorna os ids dos registros de {@code ConferenciaEncalheBackup} 
	 * na data de operação da cota em questão.
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * 
	 * @return List - Long
	 */
	public List<Long> obterIdConferenciasEncalheBackupNaData(Integer numeroCota, Date dataOperacao);
	
	public List<ConferenciaEncalheDTO> obterDadosConferenciasEncalheBackup(Integer numeroCota, Date dataOperacao);
	
	/**
	 * Retorna {@code true} se existirem
	 * registros de {@code ConferenciaEncalheBackup} 
	 * na data de operação para a cota.
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * 
	 * @return boolean
	 */
	public boolean existemConferenciasEncalheBackupNaData(Integer numeroCota, Date dataOperacao);
	
	
}
