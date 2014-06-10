package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.fechar.dia.FechamentoDiarioResumoConsignado;

public interface FechamentoDiarioResumoConsignadoRepository extends Repository<FechamentoDiarioResumoConsignado, Long> {

	public BigDecimal obterSaldoConsignadoFechamentoDiarioAnterior(Date dataAtual);
	
	FechamentoDiarioResumoConsignado obterResumoConsignado(Date dataFechamento);
	
}
