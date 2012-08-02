package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;

public interface TipoPontoPDVRepository extends Repository<TipoPontoPDV, Long> {
	
	List<TipoPontoPDV> buscarTodosPdvPrincipal();
	
	TipoPontoPDV buscarTipoPontoPdvPrincipal(Long codigoTipoPdv);

}
