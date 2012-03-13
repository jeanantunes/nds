package br.com.abril.nds.repository;

import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.movimentacao.GrupoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.TipoMovimentoEstoque;

public interface TipoMovimentoEstoqueRepository extends Repository<TipoMovimentoEstoque, Long> {
	
	TipoMovimentoEstoque buscarTipoMovimentoEstoque(TipoOperacao tipoOperacao, GrupoMovimentoEstoque grupoMovimentoEstoque);

}
