package br.com.abril.nds.repository;

import br.com.abril.nds.model.financeiro.BoletoDistribuidor;

public interface BoletoDistribuidorRepository extends Repository<BoletoDistribuidor, Long> {

	BoletoDistribuidor obterBoletoDistribuidorPorChamadaEncalheFornecedor(Long idChamadaEncalheFornecedor);

	
}
