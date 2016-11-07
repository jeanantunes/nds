package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.fechar.dia.FechamentoDiario;

public interface FechamentoDiarioRepository extends Repository<FechamentoDiario, Long>{
	
	/**
	 * Retorna a ultima data do fechamento do dia anterior a data informada para consulta
	 * 
	 * @param dataFechamento
	 * @return
	 */
	Date obterDataUltimoFechamento(Date dataFechamento);
	
	/**
	 * Retorna a ultima data que teve fechamento diario
	 * @return
	 */
	Date obterDataUltimoFechamento();
	
	FechamentoDiario obterFechamentoDiario(Date dataFechamento);
	
}
