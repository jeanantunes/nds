package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;

public interface TipoMovimentoEstoqueRepository extends Repository<TipoMovimentoEstoque, Long> {
	
	TipoMovimentoEstoque buscarTipoMovimentoEstoque(GrupoMovimentoEstoque grupoMovimentoEstoque);

	List<TipoMovimentoEstoque> buscarTiposMovimentoEstoque(List<GrupoMovimentoEstoque> gruposMovimentoEstoque);
	
	List<Long> buscarIdTiposMovimentoEstoque(List<GrupoMovimentoEstoque> gruposMovimentoEstoque);
	
}
