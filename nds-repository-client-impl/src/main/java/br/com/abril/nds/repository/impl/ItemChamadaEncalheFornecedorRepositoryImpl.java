package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.fornecedor.ItemChamadaEncalheFornecedor;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ItemChamadaEncalheFornecedorRepository;
import br.com.abril.nds.util.Intervalo;

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
		
		hql.append(" ) * itemChamadaEncalheFornecedor.qtdeDevolucaoApurada ) ");
		
		hql.append(" from ItemChamadaEncalheFornecedor itemChamadaEncalheFornecedor ");
		
		hql.append(" inner join itemChamadaEncalheFornecedor.produtoEdicao as produtoEdicao ");
		
		hql.append(" inner join itemChamadaEncalheFornecedor.chamadaEncalheFornecedor as chamadaEncalheFornecedor  ");
		
		hql.append(" where ");
		
		hql.append(" chamadaEncalheFornecedor.id = :idChamadaEncalheFornecedor ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idChamadaEncalheFornecedor", idChamadaEncalheFornecedor);
	
		return (BigDecimal) query.uniqueResult();
		
	}
	
	
	public List<ItemChamadaEncalheFornecedor> obterItensChamadaEncalheFornecedor(Long idChamadaEncalheFornecedor,Intervalo<Date> periodo){
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select itemChamadaEncalheFornecedor ");

		hql.append(" from ItemChamadaEncalheFornecedor itemChamadaEncalheFornecedor ");
		
		hql.append(" inner join itemChamadaEncalheFornecedor.chamadaEncalheFornecedor as chamadaEncalheFornecedor  ");
		
		hql.append(" inner join itemChamadaEncalheFornecedor.produtoEdicao produtoEdicao ");
		
		hql.append(" inner join produtoEdicao.estoqueProduto estoqueProduto ");
		
		//hql.append(" inner join FechamentoEncalhe fechamentoEncalhe");
		
		hql.append(" where 1 = 1 ");
		
		//hql.append(" fechamentoEncalhe.fechamentoEncalhePK.produtoEdicao.id = produtoEdicao.id " );
		
		//hql.append(" AND fechamentoEncalhe.fechamentoEncalhePK.dataEncalhe BETWEEN :inicioSemana AND :fimSemana ");
		
		hql.append(" AND chamadaEncalheFornecedor.id = :idChamadaEncalheFornecedor ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idChamadaEncalheFornecedor", idChamadaEncalheFornecedor);
		//query.setParameter("inicioSemana", periodo.getDe());
		//query.setParameter("fimSemana", periodo.getAte());
	
		return query.list();
	}
}
