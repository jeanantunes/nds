package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;

public interface TiposEstabelecimentoRepository extends Repository<TipoEstabelecimentoAssociacaoPDV,Long> {
	
	TipoEstabelecimentoAssociacaoPDV obterTipoEstabelecimentoAssociacaoPDV(Long codigo);
}
