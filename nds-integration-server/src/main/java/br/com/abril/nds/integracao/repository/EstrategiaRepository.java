package br.com.abril.nds.integracao.repository;

import br.com.abril.nds.integracao.model.canonic.EMS2021Input;
import br.com.abril.nds.integracao.model.canonic.EMS2021InputItem;
import br.com.abril.nds.model.integracao.icd.IcdEstrategia;

import java.math.BigInteger;
import java.util.List;

public interface EstrategiaRepository extends Repository<IcdEstrategia, Long>{

	List<EMS2021Input> obterEstrategias(Long codigoDistribuidor);

	List<EMS2021InputItem> obterEdicaoBaseEstrategia(Integer codigoPraca, BigInteger codigoLancamentoEdicao);

}
