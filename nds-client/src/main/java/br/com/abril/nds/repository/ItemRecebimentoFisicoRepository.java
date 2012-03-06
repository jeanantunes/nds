package br.com.abril.nds.repository;

import java.util.Date;

import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;


public interface ItemRecebimentoFisicoRepository extends Repository<ItemRecebimentoFisico, Long> {

	ItemRecebimentoFisico obterItemPorDataLancamentoIdProdutoEdicao(Date dataLancamento, Long idProdutoEdicao);
}
