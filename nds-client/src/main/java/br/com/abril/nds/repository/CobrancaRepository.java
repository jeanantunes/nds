package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.financeiro.Cobranca;

public interface CobrancaRepository  extends Repository<Cobranca,Integer> {
	
	List<Cobranca> obterCobrancaPorCota(Integer numeroCota);

}
