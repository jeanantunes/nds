package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.financeiro.DescontoProximosLancamentos;

public interface DescontoProximosLancamentosRepository extends Repository<DescontoProximosLancamentos, Long>{

	
	List<DescontoProximosLancamentos> obterDescontoProximosLancamentosPor(Long idProduto, Date dataLancamento); 
	
}
