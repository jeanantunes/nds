package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ItemChamadaEncalheFornecedorRepository;

@Repository
public class ItemChamadaEncalheFornecedorRepositoryImpl extends AbstractRepositoryModel<ItemChamadaEncalheFornecedor, Long> implements ItemChamadaEncalheFornecedorRepository {

	
	/**
	 * Construtor.
	 */
	public ItemChamadaEncalheFornecedorRepositoryImpl() {
		
		super(ItemChamadaEncalheFornecedor.class);
		
	}
	
	
	public BigDecimal obterTotalDoDescontoItensChamadaEncalheFornecedor(Long idChamadaEncalheFornecedor) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select sum( ( produtoEdicao.precoVenda * ");
		
		hql.append(" ( ");
		
		hql.append(" select case ");
		
		hql.append(" when ( produtoDescontoLogisitca is not null and produtoDescontoLogisitca.percentualDesconto is not null ) ");
		
		hql.append(" then produtoDescontoLogisitca.percentualDesconto ");
		
		hql.append(" else prodEdicaoDescontoLogisitca.percentualDesconto end as desconto ");
		
		hql.append(" from ProdutoEdicao prodEdicao ");

		hql.append(" inner join prodEdicao.produto as prod ");
		
		hql.append(" left outer join prodEdicao.descontoLogistica as prodEdicaoDescontoLogisitca ");
		
		hql.append(" left outer join prod.descontoLogistica as produtoDescontoLogisitca ");

		hql.append(" where ");
		
		hql.append(" prodEdicao.id = produtoEdicao.id ");
		
		hql.append(" ) ");
		
		hql.append(" ) * coalesce(itemChamadaEncalheFornecedor.qtdeDevolucaoApurada, 0) ) ");
		
		hql.append(" from ItemChamadaEncalheFornecedor itemChamadaEncalheFornecedor ");
		
		hql.append(" inner join itemChamadaEncalheFornecedor.produtoEdicao as produtoEdicao ");
		
		hql.append(" inner join itemChamadaEncalheFornecedor.chamadaEncalheFornecedor as chamadaEncalheFornecedor  ");
		
		hql.append(" where ");
		
		hql.append(" chamadaEncalheFornecedor.id = :idChamadaEncalheFornecedor ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idChamadaEncalheFornecedor", idChamadaEncalheFornecedor);
		
		return (BigDecimal) query.uniqueResult();
		
	}
	
	
	public List<ItemChamadaEncalheFornecedor> obterItensChamadaEncalheFornecedor(Long idChamadaEncalheFornecedor){
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select itemChamadaEncalheFornecedor ");

		hql.append(" from ItemChamadaEncalheFornecedor itemChamadaEncalheFornecedor ");
		
		hql.append(" inner join itemChamadaEncalheFornecedor.chamadaEncalheFornecedor as chamadaEncalheFornecedor  ");
		
		hql.append(" inner join itemChamadaEncalheFornecedor.produtoEdicao produtoEdicao ");
		
		hql.append(" left join produtoEdicao.estoqueProduto estoqueProduto ");
		
		hql.append(" where chamadaEncalheFornecedor.id = :idChamadaEncalheFornecedor ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idChamadaEncalheFornecedor", idChamadaEncalheFornecedor);
		
		return query.list();
	}
	
	@Override
	public void removerItensChamadaEncalheFornecedor(
			List<ItemChamadaEncalheFornecedor> itens) {
		for (ItemChamadaEncalheFornecedor item:itens)
			this.removerPorId(item.getId());
	}
}