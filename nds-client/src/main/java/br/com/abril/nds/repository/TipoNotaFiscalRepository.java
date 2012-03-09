package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;

public interface TipoNotaFiscalRepository extends Repository<TipoNotaFiscal, Long> {

	List<TipoNotaFiscal> obterTiposNotasFiscais();
	
	List<TipoNotaFiscal> obterTiposNotasFiscais(TipoOperacao tipoOperacao);
	
}
