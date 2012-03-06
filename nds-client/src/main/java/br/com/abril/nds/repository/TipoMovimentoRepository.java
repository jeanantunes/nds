package br.com.abril.nds.repository;

import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.movimentacao.DominioTipoMovimento;
import br.com.abril.nds.model.movimentacao.TipoMovimento;

public interface TipoMovimentoRepository extends Repository<TipoMovimento, Long> {
	
	TipoMovimento buscarTipoMovimento(TipoOperacao tipoOperacao, DominioTipoMovimento dominioTipoMovimento);

}
