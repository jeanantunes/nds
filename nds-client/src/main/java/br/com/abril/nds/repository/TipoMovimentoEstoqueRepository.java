package br.com.abril.nds.repository;

import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.TipoOperacao;

public interface TipoMovimentoEstoqueRepository extends Repository<TipoMovimentoEstoque, Long> {
	
	TipoMovimentoEstoque buscarTipoMovimentoEstoque(TipoOperacao tipoOperacao, GrupoMovimentoEstoque grupoMovimentoEstoque);

}
