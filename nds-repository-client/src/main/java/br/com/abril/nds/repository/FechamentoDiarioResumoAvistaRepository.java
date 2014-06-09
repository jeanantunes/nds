package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoAvista;

public interface FechamentoDiarioResumoAvistaRepository extends Repository<FechamentoDiarioResumoAvista, Long> {

	public BigDecimal obterSaldoAVistaFechamentoDiarioAnterior(Date dataAtual);
	
	FechamentoDiarioResumoAvista obterResumoConsignado(Date dataFechamento);
	
}
