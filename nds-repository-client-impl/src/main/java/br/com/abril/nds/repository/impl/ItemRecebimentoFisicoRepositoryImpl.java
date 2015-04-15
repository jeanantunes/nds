package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}
 * 
 * @author william.machado
 * 
 */
@Repository
public class ItemRecebimentoFisicoRepositoryImpl extends AbstractRepositoryModel<ItemRecebimentoFisico, Long> implements ItemRecebimentoFisicoRepository {

	/**
	 * Construtor padrão.
	 */
	public ItemRecebimentoFisicoRepositoryImpl() {
		super(ItemRecebimentoFisico.class);
	}
	
	@Override
	public Long obterItemPorDataLancamentoIdProdutoEdicao(Date dataLancamento, Long idProdutoEdicao) {
		
		if(	idProdutoEdicao == null || dataLancamento == null) {
			throw new NullPointerException();
		}
		
		
		
		String hql = " select itemRecebimento.id from ItemRecebimentoFisico itemRecebimento "
				   + " join itemRecebimento.itemNotaFiscal itemNotaFiscal "
				   + " join itemNotaFiscal.produtoEdicao produtoEdicao "
				   + " where itemNotaFiscal.dataLancamento = :dataLancamento " 
				   + " and produtoEdicao.id = :idProdutoEdicao";
		
		Query query = super.getSession().createQuery(hql);
		
		query.setParameter("dataLancamento", dataLancamento);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
				
		query.setMaxResults(1);
		
		return (Long) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemRecebimentoFisico> obterItemPorIdRecebimentoFisico(Long idRecebimentoFisico) {
		
		if (idRecebimentoFisico == null) {
			throw new NullPointerException();
		}
		
		String hql = " select itemRecebimentoFisico "
				   + " from ItemRecebimentoFisico itemRecebimentoFisico "
				   + " where itemRecebimentoFisico.recebimentoFisico.id = :idRecebimentoFisico ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("idRecebimentoFisico", idRecebimentoFisico);
		
		return query.list();
	}

	@Override
	public void removerRecebimentoFisicoLancamento(Long idLancamento) {

		String delete = " delete from LANCAMENTO_ITEM_RECEB_FISICO where LANCAMENTO_ID = :idLancamento";
		
		Query query = this.getSession().createSQLQuery(delete);
		
		query.setParameter("idLancamento", idLancamento);
		
		query.executeUpdate();
	}
}
