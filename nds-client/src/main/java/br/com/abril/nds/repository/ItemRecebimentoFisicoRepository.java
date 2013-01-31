package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;


public interface ItemRecebimentoFisicoRepository extends Repository<ItemRecebimentoFisico, Long> {

	Long obterItemPorDataLancamentoIdProdutoEdicao(Date dataLancamento, Long idProdutoEdicao);
	
	List<ItemRecebimentoFisico> obterItemPorIdRecebimentoFisico(Long idRecebimentoFisico);
}
