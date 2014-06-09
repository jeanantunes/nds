package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.pdv.PeriodoFuncionamentoPDV;

public interface PeriodoFuncionamentoPDVRepository extends Repository<PeriodoFuncionamentoPDV, Long>{
	
	List<PeriodoFuncionamentoPDV> obterPeriodoFuncionamentoPDV(Long idPDV);
}
