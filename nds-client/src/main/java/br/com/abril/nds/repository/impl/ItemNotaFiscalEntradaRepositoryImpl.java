package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ItemDanfe;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.repository.ItemNotaFiscalEntradaRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}
 * 
 * @author william.machado
 * 
 */
@Repository
public class ItemNotaFiscalEntradaRepositoryImpl extends
		AbstractRepositoryModel<ItemNotaFiscalEntrada, Long> implements ItemNotaFiscalEntradaRepository {

	/**
	 * Construtor padrão.
	 */
	public ItemNotaFiscalEntradaRepositoryImpl() {
		super(ItemNotaFiscalEntrada.class);
	}
	
	public List<ItemNotaFiscalEntrada> buscarItensPorIdNota(Long idNotaFiscal){
		
		String hql = "from ItemNotaFiscalEntrada item "				
				+ " where item.notaFiscal.id = :idNotaFiscal";

		Query query = super.getSession().createQuery(hql);

		query.setParameter("idNotaFiscal", idNotaFiscal);

		return query.list();
	}
	

	public List<ItemDanfe> obterListaItemNotaFiscalEntradaDadosDanfe(Long idNotaFiscal) {
		
		StringBuffer hql = new StringBuffer("")
		
		.append(" select ")
		
		.append(" item.produtoEdicao.produto.codigo as codigoProduto,		")
		.append(" item.produtoEdicao.produto.codigo as descricaoProduto, 	")
		.append(" item.qtde as quantidadeProduto, ")
		.append(" item.produtoEdicao.precoVenda as valorUnitarioProduto,	")
		.append(" (item.produtoEdicao.precoVenda * item.qtde) as valorTotalProduto, ")
		.append(" item.NCMProduto as NCMProduto,		")
		.append(" item.CFOPProduto as CFOPProduto,		")
		.append(" item.unidadeProduto as unidadeProduto,")
		.append(" item.CSTProduto as CSTProduto, 		")
		.append(" item.CSOSNProduto as CSOSNProduto, 	")
		.append(" item.baseCalculoProduto as baseCalculoProduto,	")
		.append(" item.aliquotaICMSProduto as aliquotaICMSProduto,	")
		.append(" item.valorICMSProduto as valorICMSProduto,	")
		.append(" item.aliquotaIPIProduto as aliquotaIPIProduto,	")
		.append(" item.valorIPIProduto as valorIPIProduto	")
		
		.append(" from ItemNotaFiscalEntrada item ")

		.append(" where ")
		
		.append(" item.notaFiscal.id = :idNotaFiscal ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ItemDanfe.class));
		
		query.setParameter("idNotaFiscal", idNotaFiscal);
		
		return query.list();
		
	}
	
	@Override
	public List<ItemNotaFiscalEntrada> obtemPorControleConferenciaEncalheCota(
			long idControleConferencia,String  orderBy,Ordenacao ordenacao, Integer firstResult, Integer maxResults) {
		
		
		Criteria criteria = consultaPorControleConferenciaEncalheCota(idControleConferencia);
		
		if(Ordenacao.ASC ==  ordenacao){
			criteria.addOrder(Order.asc(orderBy));
		}else if(Ordenacao.DESC ==  ordenacao){
			criteria.addOrder(Order.desc(orderBy));
		}
		
		if(firstResult != null) {
			
			criteria.setFirstResult(firstResult);
			
		}
		
		if(maxResults != null) {
			
			criteria.setMaxResults(maxResults);
			
		}
		
		
		return criteria.list();
	}

	/**
	 * @param idControleConferencia
	 * @return
	 * @throws HibernateException
	 */
	private Criteria consultaPorControleConferenciaEncalheCota(
			long idControleConferencia) throws HibernateException {
		Criteria criteria = getSession().createCriteria(ItemNotaFiscalEntrada.class);		
		criteria.createAlias("notaFiscal", "notaFiscal");
		criteria.createAlias("notaFiscal.controleConferenciaEncalheCota", "controleConferenciaEncalheCota");
		
		criteria.createAlias("produtoEdicao", "produtoEdicao");
		criteria.createAlias("produtoEdicao.produto", "produto");
		criteria.add(Restrictions.eq("controleConferenciaEncalheCota.id", idControleConferencia));
		return criteria;
	}
	
	@Override
	public Long quantidadePorControleConferenciaEncalheCota(
			long idControleConferencia){
		

		Criteria criteria = consultaPorControleConferenciaEncalheCota(idControleConferencia);
		
		criteria.setProjection(Projections.rowCount());
		
		
		return (Long) criteria.uniqueResult();
		
	}
	
}
