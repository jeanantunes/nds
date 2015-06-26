package br.com.abril.nds.repository.impl;
 
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.RegistroEdicoesFechadasVO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
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

	private StringBuilder subSelectGetSaldoProduto(){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("  (select  ");
		sql.append("      sum(case   ");
		sql.append("          when tipoMov.OPERACAO_ESTOQUE='ENTRADA' then mEstq.QTDE   ");
		sql.append("          else mEstq.QTDE*-1   ");
		sql.append("      end)   ");
		sql.append("  from  ");
		sql.append("      MOVIMENTO_ESTOQUE mEstq   ");
		sql.append("  inner join  ");
		sql.append("      TIPO_MOVIMENTO tipoMov   ");
		sql.append("          on mEstq.TIPO_MOVIMENTO_ID=tipoMov.ID   ");
		sql.append("  inner join  ");
		sql.append("      PRODUTO_EDICAO prodEd   ");
		sql.append("          on mEstq.PRODUTO_EDICAO_ID=prodEd.ID   ");
		sql.append("  where  ");
		sql.append("      prodEd.ID=pe.ID   ");
		sql.append("      and tipoMov.GRUPO_MOVIMENTO_ESTOQUE not in  (:gruposExcluidos)  ");
		sql.append("      and mEstq.STATUS = :statusAprovacao )  ");
		
		return sql;
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoTotalEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	public BigInteger obterResultadoTotalEdicoesFechadas(Date dataDe, 
			                                             Date dataAte, 
			                                             Long idFornecedor,
				                                         List<String> gruposExcluidos,
				                                         StatusAprovacao statusAprovacao) {
        
	    StringBuilder sql = new StringBuilder();
	    
	    HashMap<String, Object> param = new HashMap<String, Object>();
		
	    sql.append(" SELECT ")
	    
	       .append("        cast( ")
	       
	       .append( this.subSelectGetSaldoProduto() )
		   
		   .append(" AS UNSIGNED INTEGER) as saldo ");
		
		addWhereFromResultadoEdicoesFechadas(sql, 
				                             param, 
				                             dataDe, 
				                             dataAte, 
				                             idFornecedor,
				                             gruposExcluidos,
				                             statusAprovacao);
		
		Query query = this.getSession().createSQLQuery(sql.toString());
		
		((SQLQuery) query).addScalar("saldo", StandardBasicTypes.BIG_INTEGER);
		
        setParameters(query, param);

        query.setParameterList("gruposExcluidos", gruposExcluidos);
        
        BigInteger saldoTotal = BigInteger.ZERO;
        
        for(Object saldo : query.list()){
        	
        	saldoTotal = saldoTotal.add((BigInteger) saldo);
        }
        
		return saldoTotal;
	}

	private void addWhereFromResultadoEdicoesFechadas(StringBuilder sql,
			                                          HashMap<String, Object> param, 
			                                          Date dataDe, 
			                                          Date dataAte, 
			                                          Long idFornecedor,
			                                          List<String> gruposExcluidos,
			                                          StatusAprovacao statusAprovacao) {
		
		sql.append(" from  ");
		sql.append("         PRODUTO_EDICAO pe   ");
		sql.append("     inner join  ");
		sql.append("         ESTOQUE_PRODUTO estProd   ");
		sql.append("             on pe.ID=estProd.PRODUTO_EDICAO_ID   ");
		sql.append("     inner join  ");
		sql.append("         PRODUTO pd   ");
		sql.append("             on pe.PRODUTO_ID=pd.ID   ");
		sql.append("     inner join  ");
		sql.append("         PRODUTO_FORNECEDOR prodFornc   ");
		sql.append("             on pd.ID=prodFornc.PRODUTO_ID   ");
		sql.append("     inner join  ");
		sql.append("         FORNECEDOR fornc   ");
		sql.append("             on prodFornc.fornecedores_ID=fornc.ID   ");
		sql.append("     inner join  ");
		sql.append("         PESSOA pj   ");
		sql.append("             on fornc.JURIDICA_ID=pj.ID   ");
		sql.append("     WHERE  ");
		sql.append("             	(select l.DATA_REC_DISTRIB from lancamento l where l.PRODUTO_EDICAO_ID = pe.id order by l.DATA_LCTO_DISTRIBUIDOR desc limit 1) between :dataDe and :dataAte  ");
		sql.append("            AND (select l.STATUS from lancamento l where l.PRODUTO_EDICAO_ID = pe.id order by l.DATA_LCTO_DISTRIBUIDOR desc limit 1) = :statusFechado   ");
		sql.append("         	AND (  ");
		
		sql.append( this.subSelectGetSaldoProduto() );
		
		sql.append("         ) > 0  ");
		
		if (idFornecedor !=  null) {
			
			sql.append(" AND fornecedores.id = :idFornecedor ");
		}
	
		param.put("dataDe", dataDe);
		
		param.put("dataAte", dataAte);
	
		param.put("statusFechado", StatusLancamento.FECHADO.getDescricao());
		
		param.put("statusAprovacao", statusAprovacao.getDescricao());
		
		if(idFornecedor !=  null){
			
			param.put("idFornecedor", idFornecedor);
	    }		
	}
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.EdicoesFechadasRepository#obterResultadoEdicoesFechadas(java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	public Long countResultadoEdicoesFechadas(Date dataDe, 
			                                  Date dataAte, 
			                                  Long idFornecedor,
                                              List<String> gruposExcluidos,
                                              StatusAprovacao statusAprovacao) {
		
		StringBuilder sql = new StringBuilder();
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		sql.append("	SELECT count(distinct pe.id) as total ");
				
		addWhereFromResultadoEdicoesFechadas(sql, 
				                             param, 
				                             dataDe, 
				                             dataAte, 
				                             idFornecedor,
				                             gruposExcluidos,
				                             statusAprovacao);
						
		Query query = this.getSession().createSQLQuery(sql.toString());
		
		((SQLQuery) query).addScalar("total", StandardBasicTypes.LONG);

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
			    				                                         List<String> gruposExcluidos,
			    				                                         StatusAprovacao statusAprovacao) {
		
		StringBuilder sql = new StringBuilder();
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		sql.append(" 	SELECT ");
		sql.append(" 	pe.ID as idProdutoEdicao, ");
		sql.append("    pd.CODIGO as codigoProduto, ");
		sql.append("    pd.NOME as nomeProduto, ");
		sql.append("    pe.NUMERO_EDICAO as edicaoProduto, ");
		sql.append("    pj.NOME_FANTASIA as nomeFornecedor, ");
		sql.append("    (select l.DATA_LCTO_DISTRIBUIDOR from lancamento l where l.PRODUTO_EDICAO_ID = pe.id order by l.DATA_LCTO_DISTRIBUIDOR desc limit 1) as dataLancamento, ");
		sql.append("    pe.PARCIAL as parcial, ");
		sql.append("    (select l.DATA_REC_DISTRIB from lancamento l where l.PRODUTO_EDICAO_ID = pe.id order by l.DATA_LCTO_DISTRIBUIDOR desc limit 1) as dataRecolhimento, ");
		sql.append("        cast( ");
	    sql.append( this.subSelectGetSaldoProduto() );
		sql.append(" AS UNSIGNED INTEGER) as saldo ");
		
		addWhereFromResultadoEdicoesFechadas(sql, 
				                             param, 
				                             dataDe, 
				                             dataAte, 
				                             idFornecedor,
				                             gruposExcluidos,
				                             statusAprovacao);
		
		if(sortname != null){
			
			sql.append("ORDER BY ").append(sortname).append(" ").append(sortorder);
		}

		Query query = this.getSession().createSQLQuery(sql.toString());
		
		((SQLQuery) query).addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		((SQLQuery) query).addScalar("codigoProduto", StandardBasicTypes.STRING);
		((SQLQuery) query).addScalar("nomeProduto", StandardBasicTypes.STRING);
		((SQLQuery) query).addScalar("edicaoProduto", StandardBasicTypes.LONG);
		((SQLQuery) query).addScalar("nomeFornecedor", StandardBasicTypes.STRING);
		((SQLQuery) query).addScalar("dataLancamento", StandardBasicTypes.DATE);
		((SQLQuery) query).addScalar("parcial", StandardBasicTypes.BOOLEAN);
		((SQLQuery) query).addScalar("dataRecolhimento", StandardBasicTypes.DATE);
		((SQLQuery) query).addScalar("saldo", StandardBasicTypes.BIG_INTEGER);
		
		
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
