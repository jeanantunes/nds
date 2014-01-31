package br.com.abril.nds.repository.impl;
 
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
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

	private StringBuilder getHqlSaldoProduto(){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" (select " )

           .append("  sum(case when tm.operacaoEstoque = 'ENTRADA' then me.qtde else (me.qtde * -1) end) " )
				   
           .append("  from MovimentoEstoque me ")
		           
	       .append("  join me.tipoMovimento tm " )

	       .append("  join me.produtoEdicao pe " )
	        
	       .append("  where pe.id = produtoEdicao.id " )
	       
	       .append("  and tm.grupoMovimentoEstoque not in (:gruposExcluidos) ")
	       
	       .append("  and me.status = :statusAprovacao  ")
		   
		   .append(" ) ");
		
		return hql;
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoTotalEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	public BigInteger obterResultadoTotalEdicoesFechadas(Date dataDe, 
			                                             Date dataAte, 
			                                             Long idFornecedor,
				                                         List<GrupoMovimentoEstoque> gruposExcluidos,
				                                         StatusAprovacao statusAprovacao) {
        
	    StringBuilder hql = new StringBuilder();
	    
	    HashMap<String, Object> param = new HashMap<String, Object>();
		
	    hql.append(" SELECT ")
	    
	       .append("        cast( ")
	       
		   .append("	            (" )
		   
	       .append(this.getHqlSaldoProduto())
		   
		   .append("	            ) as big_integer " )
		   
		   .append("            ) as saldo ");
		
		addWhereFromResultadoEdicoesFechadas(hql, 
				                             param, 
				                             dataDe, 
				                             dataAte, 
				                             idFornecedor,
				                             gruposExcluidos,
				                             statusAprovacao);
		
		hql.append(" GROUP BY lancamento.id  ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
        setParameters(query, param);

        query.setParameterList("gruposExcluidos", gruposExcluidos);
        
        BigInteger saldoTotal = BigInteger.ZERO;
        
        for(Object saldo : query.list()){
        	
        	saldoTotal = saldoTotal.add((BigInteger) saldo);
        }
        
		return saldoTotal;
	}

	private void addWhereFromResultadoEdicoesFechadas(StringBuilder hql,
			                                          HashMap<String, Object> param, 
			                                          Date dataDe, 
			                                          Date dataAte, 
			                                          Long idFornecedor,
			                                          List<GrupoMovimentoEstoque> gruposExcluidos,
			                                          StatusAprovacao statusAprovacao) {
		
		hql.append(" FROM Lancamento AS lancamento ")
		
		   .append(" inner JOIN lancamento.produtoEdicao AS produtoEdicao ")
		   
		   .append(" inner JOIN produtoEdicao.estoqueProduto AS estoqueProduto ")
		   
		   .append(" inner JOIN produtoEdicao.produto AS produto ")
		   
		   .append(" inner JOIN produto.fornecedores AS fornecedores ")
		   
		   .append(" inner JOIN fornecedores.juridica AS juridica ")

           .append(" WHERE (  lancamento.dataRecolhimentoDistribuidor BETWEEN :dataDe AND :dataAte  ) " )

		   .append(" AND lancamento.status=:statusFechado ")
		   
		   .append(" AND (")
		   
		   .append(this.getHqlSaldoProduto())
		   
		   .append("     ) > 0 ");
		
		if (idFornecedor !=  null) {
			
			hql.append(" AND fornecedores.id = :idFornecedor ");
		}
	
		param.put("dataDe", dataDe);
		
		param.put("dataAte", dataAte);
	
		param.put("statusFechado", StatusLancamento.FECHADO);
		
		param.put("statusAprovacao", statusAprovacao);
		
		if(idFornecedor !=  null){
			
			param.put("idFornecedor", idFornecedor);
	    }		
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long countResultadoEdicoesFechadas(Date dataDe, 
			                                  Date dataAte, 
			                                  Long idFornecedor,
                                              List<GrupoMovimentoEstoque> gruposExcluidos,
                                              StatusAprovacao statusAprovacao) {
		
		StringBuilder hql = new StringBuilder();
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		hql.append("	SELECT count(distinct lancamento.id) ");
				
		addWhereFromResultadoEdicoesFechadas(hql, 
				                             param, 
				                             dataDe, 
				                             dataAte, 
				                             idFornecedor,
				                             gruposExcluidos,
				                             statusAprovacao);
						
		Query query = this.getSession().createQuery(hql.toString());

		setParameters(query, param);
		
		query.setParameterList("gruposExcluidos", gruposExcluidos);
    	
		return (Long) query.uniqueResult();
	}	

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RegistroEdicoesFechadasVO> obterResultadoEdicoesFechadas(Date dataDe, 
			                                                             Date dataAte, 
			                                                             Long idFornecedor, 
			                                                             String sortorder, 
			                                                             String sortname, 
			                                                             Integer firstResult, 
			                                                             Integer maxResults,
			    				                                         List<GrupoMovimentoEstoque> gruposExcluidos,
			    				                                         StatusAprovacao statusAprovacao) {
		
		StringBuilder hql = new StringBuilder();
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		hql.append(" SELECT produtoEdicao.id as idProdutoEdicao, ")
		
		   .append("        produto.codigo as codigoProduto , ")
		
		   .append("        produto.nome as nomeProduto, ")
		   
		   .append("        produtoEdicao.numeroEdicao as edicaoProduto, " )
		   
		   .append("        juridica.nomeFantasia as nomeFornecedor, " )

		   .append("        lancamento.dataLancamentoDistribuidor as dataLancamento, ")
		   
		   .append("        produtoEdicao.parcial as parcial, ")
		   
		   .append("        lancamento.dataRecolhimentoDistribuidor as dataRecolhimento, ")

		   .append("        cast( ")
	       
		   .append("	            (" )
		   
	       .append(this.getHqlSaldoProduto())
		   
		   .append("	            ) as big_integer " )
		   
		   .append("            ) as saldo ");

		addWhereFromResultadoEdicoesFechadas(hql, 
				                             param, 
				                             dataDe, 
				                             dataAte, 
				                             idFornecedor,
				                             gruposExcluidos,
				                             statusAprovacao);
		
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
		
		setParameters(query, param);
		
		query.setParameterList("gruposExcluidos", gruposExcluidos);
		
		return query.list();
	}
	
}
