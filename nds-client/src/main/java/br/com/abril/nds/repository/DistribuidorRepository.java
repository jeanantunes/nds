package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;

import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;

public interface DistribuidorRepository extends Repository<Distribuidor, Long> {
	
	Distribuidor obter();
	
	List<DistribuicaoFornecedor> buscarDiasDistribuicao(Collection<Long> idsForncedores, OperacaoDistribuidor operacaoDistribuidor);

}
