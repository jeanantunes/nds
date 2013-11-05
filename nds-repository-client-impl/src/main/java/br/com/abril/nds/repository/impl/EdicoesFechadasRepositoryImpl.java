package br.com.abril.nds.repository.impl;
 
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EdicoesFechadasRepository;

@Repository
public class EdicoesFechadasRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoque, Long>  implements EdicoesFechadasRepository {

	/**
	 * Construtor padrão.
	 */
	public EdicoesFechadasRepositoryImpl() {
		super(MovimentoEstoque.class);
	}

	

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoTotalEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	public BigInteger obterResultadoTotalEdicoesFechadas(Date dataDe, Date dataAte, Long idFornecedor) {
StringBuilder hql = new StringBuilder();
		
		hql.append("  SELECT cast( count(distinct produtoEdicao.id) as big_integer) 				")
		    .append(" FROM Lancamento AS lancamento 					")
			.append(" inner JOIN lancamento.produtoEdicao AS produtoEdicao 			")
			.append(" inner JOIN produtoEdicao.estoqueProduto AS estoqueProduto ")
			.append(" inner JOIN produtoEdicao.produto AS produto ")
			.append(" inner JOIN produto.fornecedores AS fornecedores ")
			.append(" inner JOIN fornecedores.juridica AS juridica ");
		
		hql.append(" WHERE ( lancamento.dataLancamentoDistribuidor BETWEEN :dataDe AND :dataAte ) and ")
		
		.append("	((estoqueProduto.qtde is not null and estoqueProduto.qtde > 0) or ")
		.append("	(estoqueProduto.qtdeSuplementar is not null and estoqueProduto.qtdeSuplementar > 0) or ")
		.append("	(estoqueProduto.qtdeDevolucaoEncalhe is not null and estoqueProduto.qtdeDevolucaoEncalhe > 0) or ")
		.append("	(estoqueProduto.qtdeDanificado is not null and estoqueProduto.qtdeDanificado > 0)) ");

		
		if (idFornecedor !=  null) {
			hql.append(" AND fornecedores.id = :idFornecedor ");
		}
		
		hql.append(" GROUP BY produtoEdicao.id  ");
		
		
		Query query = this.getSession().createQuery(hql.toString());

    	query.setParameter("dataDe", dataDe);
    	
    	query.setParameter("dataAte", dataAte);

    	if(idFornecedor !=  null){
	    	query.setParameter("idFornecedor", idFornecedor);
	    }		
		
		
		return (BigInteger) query.uniqueResult();
	}

	private void addWhereFromResultadoEdicoesFechadas(StringBuilder hql,HashMap<String, Object> param, Date dataDe, Date dataAte, Long idFornecedor) {
		
		hql.append(" FROM Lancamento AS lancamento ")
		.append(" inner JOIN lancamento.produtoEdicao AS produtoEdicao ")
		.append(" inner JOIN produtoEdicao.estoqueProduto AS estoqueProduto ")
		.append(" inner JOIN produtoEdicao.produto AS produto ")
		.append(" inner JOIN produto.fornecedores AS fornecedores ")
		.append(" inner JOIN fornecedores.juridica AS juridica ");
	
		hql.append(" WHERE lancamento.status=:statusFechado and ( lancamento.dataLancamentoDistribuidor BETWEEN :dataDe AND :dataAte ) ")	
			
			.append(" AND (coalesce(estoqueProduto.qtde, 0) 					+ ")
			.append("	coalesce(estoqueProduto.qtdeSuplementar, 0) 		+ ")
			.append("	coalesce(estoqueProduto.qtdeDevolucaoEncalhe, 0) 	+ ")
			.append("	coalesce(estoqueProduto.qtdeDanificado,	0)) > 0 ");
				
		if (idFornecedor !=  null) {
			hql.append(" AND fornecedores.id = :idFornecedor ");
		}
	
		param.put("dataDe", dataDe);
		
		param.put("dataAte", dataAte);
	
		param.put("statusFechado", StatusLancamento.FECHADO);
		
		if(idFornecedor !=  null){
			param.put("idFornecedor", idFornecedor);
	    }		
		
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long countResultadoEdicoesFechadas(Date dataDe, Date dataAte, Long idFornecedor) {
		
		StringBuilder hql = new StringBuilder();
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		hql.append("	SELECT count(distinct lancamento.id) ");
				
		addWhereFromResultadoEdicoesFechadas(hql, param, dataDe, dataAte, idFornecedor);
						
		Query query = this.getSession().createQuery(hql.toString());

		for (String key : param.keySet()) {
			query.setParameter(key, param.get(key));
		}
    	
		return (Long) query.uniqueResult();
	}	

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe, Date dataAte, Long idFornecedor, String sortorder, String sortname, Integer firstResult, Integer maxResults) {
		
		StringBuilder hql = new StringBuilder();
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		hql.append("	SELECT produtoEdicao.id as idProdutoEdicao, produto.codigo as codigoProduto , ")
			.append("   produto.nome as nomeProduto, ")
			.append("   produtoEdicao.numeroEdicao as edicaoProduto, " )
			.append("   juridica.nomeFantasia as nomeFornecedor, " )
			.append("   (SELECT min(lancamentos.dataRecolhimentoDistribuidor) from Lancamento AS lancamentos WHERE lancamentos.produtoEdicao.id = lancamento.produtoEdicao.id) as dataLancamento , ")
			.append("   produtoEdicao.parcial as parcial, ")
			.append("   (SELECT max(lancamentos.dataRecolhimentoDistribuidor) from Lancamento AS lancamentos WHERE lancamentos.produtoEdicao.id = lancamento.produtoEdicao.id)  as dataRecolhimento, ")
			.append("    cast( ")
			.append("	(coalesce(estoqueProduto.qtde, 0) 					+ ")
			.append("	coalesce(estoqueProduto.qtdeSuplementar, 0) 		+ ")
			.append("	coalesce(estoqueProduto.qtdeDevolucaoEncalhe, 0) 	+ ")
			.append("	coalesce(estoqueProduto.qtdeDanificado,	0)) ")
			.append("	as big_integer ) as saldo ");
		
		
		addWhereFromResultadoEdicoesFechadas(hql, param, dataDe, dataAte, idFornecedor);
		
		hql.append(" GROUP BY lancamento.id  ");
		
		if(sortname != null){
			hql.append("ORDER BY ").append(sortname).append(" ").append(sortorder);
		}

		Query query = this.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RegistroEdicoesFechadasVO.class));
    	    	
    	if (firstResult!=null) {
			query.setFirstResult(firstResult);
		}
		if (maxResults!=null) {
			query.setMaxResults(maxResults);
		}
		
		for (String key : param.keySet()) {
			query.setParameter(key, param.get(key));
		}
		
		return query.list();
	}
	
}
