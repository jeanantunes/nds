package br.com.abril.nds.repository;

import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.movimentacao.GrupoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.TipoMovimentoEstoque;

public interface TipoMovimentoRepository extends Repository<TipoMovimentoEstoque, Long> {
	
	TipoMovimentoEstoque buscarTipoMovimento(TipoOperacao tipoOperacao, GrupoMovimentoEstoque dominioTipoMovimento);

}
