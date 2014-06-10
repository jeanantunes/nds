package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.desconto.DescontoDTO;
import br.com.abril.nds.model.financeiro.DescontoProximosLancamentos;

public interface DescontoProximosLancamentosRepository extends Repository<DescontoProximosLancamentos, Long>{

	
	DescontoProximosLancamentos obterDescontoProximosLancamentosPor(Long idProduto, Date dataLancamento);

	List<DescontoDTO> obterDescontosProximosLancamentos(Date dataOperacaoInicioDesconto); 
	
}
