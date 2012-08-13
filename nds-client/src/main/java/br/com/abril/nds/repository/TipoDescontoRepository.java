package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.TipoDesconto;

public interface TipoDescontoRepository extends Repository<TipoDesconto, Long> {	
	
	List<TipoDesconto> obterTiposDescontoCota(Long idCota);
	
	List<TipoDesconto> obterTipoDescontoNaoReferenciadosComCota(Long idCota);
	
}
