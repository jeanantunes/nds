package br.com.abril.nds.service;

import java.util.Date;

import br.com.abril.nds.model.cadastro.HistoricoDescontoLogistica;

public interface HistoricoDescontoLogisticaService {
	
	HistoricoDescontoLogistica obterDesconto(Integer tipoDesconto, Date dataInicioVigencia);
}
