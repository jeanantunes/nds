package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;
import br.com.abril.nds.repository.ConferenciaEncalheParcialRepository;


/**
 * Implementação do repositório de Pessoa
 
 * @author Discover Technology
 *
 */
@Repository
public class ConferenciaEncalheParcialRepositoryImpl extends AbstractRepository<ConferenciaEncalheParcial, Long> implements ConferenciaEncalheParcialRepository {

	public ConferenciaEncalheParcialRepositoryImpl() {
		super(ConferenciaEncalheParcial.class);
	}
	
	
	/**
	 * Obtém a somatória  da coluna qtde dos registros de conferencia encalhe parcial
	 * relativos a um determinado produtoEdicao e dataRecolhimentoDistribuidor de acordo
	 * com o statusAprovacao.
	 *  
	 * @param statusAprovacao
	 * @param dataRecolhimentoDistribuidor
	 * @param codigoProduto
	 * @param numeroEdicao
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal obterQtdTotalEncalheParcial(StatusAprovacao statusAprovacao, Date dataRecolhimentoDistribuidor, String codigoProduto, Long numeroEdicao) {
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select sum(parcial.qtde) 				");		
		hql.append(" from ConferenciaEncalheParcial parcial ");
		hql.append(" where 									");
		hql.append(" parcial.statusAprovacao = :statusAprovacao  and							");
		hql.append(" parcial.dataRecolhimentoDistribuidor = :dataRecolhimentoDistribuidor  and	");
		hql.append(" parcial.produtoEdicao.id = ");
		hql.append(" ( select pe.id from ProdutoEdicao pe where pe.numeroEdicao = :numeroEdicao and pe.produto.codigo = :codigoProduto ) ");
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("statusAprovacao", statusAprovacao);
		
		query.setParameter("dataRecolhimentoDistribuidor", dataRecolhimentoDistribuidor);

		query.setParameter("codigoProduto", codigoProduto);

		query.setParameter("numeroEdicao", numeroEdicao);
		
		return (BigDecimal) query.uniqueResult();
		
	}
	
	/**
	 * Obtém uma lista de ConferenciaEncalheParcial  relativos a um determinado 
	 * produtoEdicao e dataRecolhimentoDistribuidor de acordo  com o statusAprovacao.
	 *  
	 * @param statusAprovacao
	 * @param dataRecolhimentoDistribuidor
	 * @param codigoProduto
	 * @param numeroEdicao
	 * 
	 * @return List - ConferenciaEncalheParcial
	 */
	public List<ConferenciaEncalheParcial> obterListaConferenciaEncalhe(StatusAprovacao statusAprovacao, Date dataRecolhimentoDistribuidor, String codigoProduto, Long numeroEdicao) {
		
		StringBuffer hql = new StringBuffer("");
		
		hql.append(" select parcial 															");		
		hql.append(" from ConferenciaEncalheParcial parcial 									");
		hql.append(" where 																		");
		hql.append(" parcial.statusAprovacao = :statusAprovacao  and							");
		hql.append(" parcial.dataRecolhimentoDistribuidor = :dataRecolhimentoDistribuidor  and	");
		hql.append(" parcial.produtoEdicao.id = ");
		hql.append(" ( select pe.id from ProdutoEdicao pe where pe.numeroEdicao = :numeroEdicao and pe.produto.codigo = :codigoProduto ) ");
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("statusAprovacao", statusAprovacao);
		
		query.setParameter("dataRecolhimentoDistribuidor", dataRecolhimentoDistribuidor);

		query.setParameter("codigoProduto", codigoProduto);

		query.setParameter("numeroEdicao", numeroEdicao);
		
		return query.list();
		
	}

	
}
