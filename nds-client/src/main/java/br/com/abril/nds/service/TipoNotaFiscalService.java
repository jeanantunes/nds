package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.fiscal.TipoNotaFiscal;

public interface TipoNotaFiscalService {

	List<TipoNotaFiscal> obterTiposNotasFiscais();
	
	TipoNotaFiscal obterPorId(Long id);
	
}
