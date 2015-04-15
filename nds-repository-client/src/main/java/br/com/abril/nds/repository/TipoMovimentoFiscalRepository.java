package br.com.abril.nds.repository;

import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoFiscal;

public interface TipoMovimentoFiscalRepository extends Repository<TipoMovimentoFiscal, Long> {
	
	TipoMovimentoFiscal buscarTiposMovimentoFiscalPorTipoOperacao(OperacaoEstoque operacaoEstoque);
	
}