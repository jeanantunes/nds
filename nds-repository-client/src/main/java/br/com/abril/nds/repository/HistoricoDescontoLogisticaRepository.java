package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.HistoricoDescontoLogistica;

public interface HistoricoDescontoLogisticaRepository extends Repository<HistoricoDescontoLogistica, Long> {
	
	HistoricoDescontoLogistica obterHistoricoDesconto(Integer tipoDesconto, Date inicioVigenciaDesconto);

	List<HistoricoDescontoLogistica> obterProximosDescontosVigente(Date dataOperacao);
}
