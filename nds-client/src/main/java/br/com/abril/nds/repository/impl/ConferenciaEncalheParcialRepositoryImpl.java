package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;
import br.com.abril.nds.repository.ConferenciaEncalheParcialRepository;


/**
 * Implementação do repositório de Pessoa
 
 * @author Discover Technology
 *
 */
@Repository
public class ConferenciaEncalheParcialRepositoryImpl extends AbstractRepositoryModel<ConferenciaEncalheParcial, Long> implements ConferenciaEncalheParcialRepository {

	public ConferenciaEncalheParcialRepositoryImpl() {
		super(ConferenciaEncalheParcial.class);
	}
	
	
	/**
	 * Obtém a somatória  da coluna qtde dos registros de conferencia encalhe parcial
	 * relativos a um determinado produtoEdicao e dataMovimento de acordo
	 * com o statusAprovacao.
	 *  
	 * @param statusAprovacao
	 * @param dataMovimento
	 * @param codigoProduto
	 * @param numeroEdicao
	 * 
	 * @return BigDecimal
	 */
	public BigInteger obterQtdTotalEncalheParcial(StatusAprovacao statusAprovacao, Date dataMovimento, String codigoProduto, Long numeroEdicao) {
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select sum(parcial.qtde) 				");		
		
		hql.append(" from ConferenciaEncalheParcial parcial	");
		
		hql.append(" where ");

		hql.append(" parcial.statusAprovacao = :statusAprovacao  and	");
		
		hql.append(" parcial.dataMovimento = :dataMovimento  and		");
		
		hql.append(" parcial.produtoEdicao.id = ");
		
		hql.append(" ( select pe.id from ProdutoEdicao pe where pe.numeroEdicao = :numeroEdicao and pe.produto.codigo = :codigoProduto ) ");
		
		
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("statusAprovacao", statusAprovacao);
		
		query.setParameter("dataMovimento", dataMovimento);

		query.setParameter("codigoProduto", codigoProduto);

		query.setParameter("numeroEdicao", numeroEdicao);
		
		return (BigInteger) query.uniqueResult();
		
	}
	
	
	/**
	 * Obtém uma lista de ConferenciaEncalheParcial.
	 * 
	 * @param diferencaApurada
	 * @param nfParcialGerada
	 * @param statusAprovacao
	 * @param dataMovimento
	 * @param idProdutoEdicao
	 * @param codigoProduto
	 * @param numeroEdicao
	 * 
	 * @return List - ConferenciaEncalheParcial
	 */
	public List<ConferenciaEncalheParcial> obterListaConferenciaEncalhe(
			Boolean diferencaApurada,
			Boolean nfParcialGerada,
			StatusAprovacao statusAprovacao, 
			Date dataMovimento, 
			Long idProdutoEdicao,
			String codigoProduto, 
			Long numeroEdicao) {
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select parcial ");		
		
		hql.append(" from ConferenciaEncalheParcial parcial ");
		
		if( diferencaApurada != null 	||
			nfParcialGerada  != null 	||
			statusAprovacao  != null 	||
			dataMovimento    != null 	||
			(codigoProduto   != null && numeroEdicao != null) ) {
			
			hql.append(" where ");
			
		}
		
		
		
		boolean indAnd = false;
		
		if(diferencaApurada != null) {
			if(indAnd) {
				hql.append(" and ");
			}
			indAnd = true;
			hql.append(" parcial.diferencaApurada = :diferencaApurada ");
		}
		
		if(nfParcialGerada != null) {
			if(indAnd) {
				hql.append(" and ");
			}
			indAnd = true;
			hql.append(" parcial.nfParcialGerada = :nfParcialGerada ");
		}
		
		if(statusAprovacao != null) {
			if(indAnd) {
				hql.append(" and ");
			}
			indAnd = true;
			hql.append(" parcial.statusAprovacao = :statusAprovacao ");
		}
		
		if(dataMovimento != null) {
			if(indAnd) {
				hql.append(" and ");
			}
			indAnd = true;
			hql.append(" parcial.dataMovimento = :dataMovimento ");
		}
		
		if(idProdutoEdicao != null) {
			if(indAnd) {
				hql.append(" and ");
			}
			indAnd = true;
			hql.append(" parcial.produtoEdicao.id = :idProdutoEdicao");
		}
		
		if(codigoProduto!=null && numeroEdicao!=null) {
			if(indAnd) {
				hql.append(" and ");
			}
			indAnd = true;
			hql.append(" parcial.produtoEdicao.id = ");
			hql.append(" ( select pe.id from ProdutoEdicao pe where pe.numeroEdicao = :numeroEdicao and pe.produto.codigo = :codigoProduto ) ");
		}
		
		Query query = getSession().createQuery(hql.toString());

		if(diferencaApurada != null) {
			query.setParameter("diferencaApurada", diferencaApurada);
 		}
		
		if(nfParcialGerada != null) {
			query.setParameter("nfParcialGerada", nfParcialGerada);
		}
		
		if(statusAprovacao != null) {
			query.setParameter("statusAprovacao", statusAprovacao);
		}
		
		if(dataMovimento != null) {
			query.setParameter("dataMovimento", dataMovimento);
		}
		
		if(codigoProduto!=null && numeroEdicao!=null) {
			query.setParameter("codigoProduto", codigoProduto);
			query.setParameter("numeroEdicao", numeroEdicao);
		}
		
		if(idProdutoEdicao != null) {
			query.setParameter("idProdutoEdicao", idProdutoEdicao);
		}
		
		return query.list();
		
	}

	private StringBuffer getSubQueryMovimentoEstoqueCota() {
		
		StringBuffer hqlMovimentoEstoqueCota = new StringBuffer("")
		.append(" ( select sum(movimento.qtde) 								")
		.append(" from MovimentoEstoqueCota movimento						")
		.append(" where 													")
		.append(" movimento.produtoEdicao.id = parcial.produtoEdicao.id and ")
		.append(" movimento.data = parcial.dataMovimento )  				");
		
		return hqlMovimentoEstoqueCota;
		
	}
	
	
	/**
	 * Obtém uma lista de ContagemDevolucao.
	 * 
	 * @param diferencaApurada
	 * @param nfParcialGerada
	 * @param statusAprovacao
	 * @param idProdutoEdicao
	 * @param codigoProduto
	 * @param numeroEdicao
	 * @param dataMovimento
	 * 
	 * @return List<ContagemDevolucaoDTO>
	 */
	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(
			Boolean diferencaApurada,
			Boolean nfParcialGerada,
			StatusAprovacao statusAprovacao, 
			Long idProdutoEdicao,
			String codigoProduto,
			Long numeroEdicao,
			Date dataMovimento) {
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select ");		
		
		hql.append("  parcial.produtoEdicao.id as idProdutoEdicao, 						");
		hql.append("  parcial.produtoEdicao.precoVenda as precoVenda, 					");
		hql.append("  parcial.dataMovimento as dataMovimento, 							");
		hql.append( getSubQueryMovimentoEstoqueCota().toString() + " as qtdDevolucao, 	");
		hql.append("  sum(parcial.qtde) as qtdNota 										");
		
		hql.append(" from ConferenciaEncalheParcial parcial ");
		
		hql.append(" where ");
		
		boolean indAnd = false;
		
		if(diferencaApurada != null) {
			indAnd = true;
			hql.append(" parcial.diferencaApurada = :diferencaApurada ");
		}
		
		if(nfParcialGerada != null) {
			
			if(indAnd) {
				hql.append(" and ");
			}
			
			indAnd = true;
			
			hql.append(" parcial.nfParcialGerada = :nfParcialGerada ");
		}
		
		if(statusAprovacao != null) {
			
			if(indAnd) {
				hql.append(" and ");
			}
			
			indAnd = true;
			
			hql.append(" parcial.statusAprovacao = :statusAprovacao ");
		}
		
		if( ( idProdutoEdicao != null || ( codigoProduto != null && numeroEdicao!=null) ) && 
			dataMovimento != null) {
			
			if(indAnd) {
				hql.append(" and ");
			}
			
			indAnd = true;
			
			if(idProdutoEdicao!=null) {
				hql.append(" parcial.produtoEdicao.id = :idProdutoEdicao ");
			} else {
				hql.append(" parcial.produtoEdicao.id = ");
				hql.append(" ( select pe.id from ProdutoEdicao pe where pe.numeroEdicao = :numeroEdicao and pe.produto.codigo = :codigoProduto ) ");
			}
			
			hql.append(" and parcial.dataMovimento = :dataMovimento ");
			
			
		} else {

			hql.append(" group by ");
			hql.append(" parcial.produtoEdicao.id, ");
			hql.append(" parcial.produtoEdicao.precoVenda, ");
			hql.append(" parcial.dataMovimento ");
			
		}
		
		
		Query query = getSession().createQuery(hql.toString()).setResultTransformer(Transformers.aliasToBean(ContagemDevolucaoDTO.class));
		
		if(diferencaApurada != null) {
			query.setParameter("diferencaApurada", diferencaApurada);
 		}
		
		if(nfParcialGerada != null) {
			query.setParameter("nfParcialGerada", nfParcialGerada);
		}
		
		if(statusAprovacao != null) {
			query.setParameter("statusAprovacao", statusAprovacao);
		}

		if( ( idProdutoEdicao != null || ( codigoProduto != null && numeroEdicao!=null) ) && 
				dataMovimento != null) {

			if(idProdutoEdicao!=null) {
				query.setParameter("idProdutoEdicao", idProdutoEdicao);
			}else {
				query.setParameter("codigoProduto", codigoProduto);
				query.setParameter("numeroEdicao", numeroEdicao);
			}
			
			query.setParameter("dataMovimento", dataMovimento);
			
		}
		
		return query.list();
		
	}


	@Override
	public ConferenciaEncalheParcial obterConferenciaEncalheParcialPor(
		
		Long idProdutoEdicao, Date dataMovimento) {
		
		Criteria criteria = this.getSession().createCriteria(ConferenciaEncalheParcial.class);
			
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		criteria.add(Restrictions.eq("dataMovimento", dataMovimento));
		
		return (ConferenciaEncalheParcial) criteria.list();
	}
	
	
	
}
