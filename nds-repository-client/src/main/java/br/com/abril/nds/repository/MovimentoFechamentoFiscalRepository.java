package br.com.abril.nds.repository;

import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;


public interface MovimentoFechamentoFiscalRepository extends Repository<MovimentoFechamentoFiscal, Long> {
	
	MovimentoFechamentoFiscalCota buscarPorChamadaEncalheCota(ChamadaEncalheCota chamadaEncalheCota);
	
}