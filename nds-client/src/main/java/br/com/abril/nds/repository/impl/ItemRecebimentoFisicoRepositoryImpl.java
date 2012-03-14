package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}
 * 
 * @author william.machado
 * 
 */
@Repository
public class ItemRecebimentoFisicoRepositoryImpl extends
		AbstractRepository<ItemRecebimentoFisico, Long> implements ItemRecebimentoFisicoRepository {

	/**
	 * Construtor padrão.
	 */
	public ItemRecebimentoFisicoRepositoryImpl() {
		super(ItemRecebimentoFisico.class);
	}
	
	@Override
	public ItemRecebimentoFisico obterItemPorDataLancamentoIdProdutoEdicao(Date dataLancamento, Long idProdutoEdicao) {
		
		if(	idProdutoEdicao == null || dataLancamento == null) {
			throw new NullPointerException();
		}
		
		Criteria criteria = super.getSession().createCriteria(ItemRecebimentoFisico.class, "itemRecebimento");
		
		criteria.createAlias("itemRecebimento.itemNotaFiscal", "itemNotaFiscal");
		criteria.createAlias("itemNotaFiscal.produtoEdicao", "produtoEdicao");
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		criteria.add(Restrictions.eq("itemNotaFiscal.dataLancamento", dataLancamento));
		
		criteria.setMaxResults(1);
		
		return (ItemRecebimentoFisico) criteria.uniqueResult();
	}
	
	@Override
	public List<ItemRecebimentoFisico> obterItemPorIdRecebimentoFisico(Long idRecebimentoFisico) {
		
		if(	idRecebimentoFisico == null) {
			throw new NullPointerException();
		}
		
		String hql = " select itemRecebimentoFisico "
				   + " from ItemRecebimentoFisico itemRecebimentoFisico "
				   + " where itemRecebimentoFisico.recebimentoFisico.id = :idRecebimentoFisico ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("idRecebimentoFisico", idRecebimentoFisico);
		
		return query.list();
	}

}
