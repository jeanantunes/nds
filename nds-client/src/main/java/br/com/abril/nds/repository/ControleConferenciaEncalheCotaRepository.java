package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;

public interface ControleConferenciaEncalheCotaRepository  extends Repository<ControleConferenciaEncalheCota,Long> {
	
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

	
}
