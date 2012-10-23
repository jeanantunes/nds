package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.financeiro.DescontoProximosLancamentos;

public interface DescontoProximosLancamentosRepository extends Repository<DescontoProximosLancamentos, Long>{

	
	DescontoProximosLancamentos obterDescontoProximosLancamentosPor(Long idProduto, Date dataLancamento); 
	
}
