package br.com.abril.nds.repository;

import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ParametroEmissaoNotaFiscal;

public interface ParametroEmissaoNotaFiscalRepository extends Repository<ParametroEmissaoNotaFiscal, Long> {

	public ParametroEmissaoNotaFiscal obterParametroEmissaoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal);

	
}
