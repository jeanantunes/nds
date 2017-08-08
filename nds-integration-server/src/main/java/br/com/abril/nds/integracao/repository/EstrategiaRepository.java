package br.com.abril.nds.integracao.repository;

import br.com.abril.nds.model.integracao.icd.IcdEdicaoBaseEstrategia;
import br.com.abril.nds.model.integracao.icd.IcdEstrategia;

import java.math.BigInteger;
import java.util.List;

public interface EstrategiaRepository extends Repository<IcdEstrategia, Long>{

	List<IcdEstrategia> obterEstrategias(Long codigoDistribuidor);

	List<IcdEdicaoBaseEstrategia> obterEdicaoBaseEstrategia(Integer codigoPraca, BigInteger codigoLancamentoEdicao);

}
