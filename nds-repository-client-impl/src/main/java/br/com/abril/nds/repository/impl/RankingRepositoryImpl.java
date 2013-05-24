package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.RankingRepository;

@Repository
public class RankingRepositoryImpl extends AbstractRepository  implements RankingRepository{

	public Long obterRankingProduto(Long idProduto){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select subRank.rankProduto ")
			
			.append(" from ( ")
				
				.append(" select @rankProduto \\:= @rankProduto+1 as rankProduto, sub.PRODUTO_ID  ")
			
				.append("  from ( ")
					
					.append(" select @rankProduto \\:= 0) as init, ")
									
					.append("  	(select  consolidado.PRODUTO_ID as PRODUTO_ID, sum(consolidado.VALOR_TOTAL_VENDA_COM_DESCONTO) as VALOR ")
				
					.append("    from view_consolidado_movimento_estoque_cota consolidado  ")
				
					.append("    group by consolidado.PRODUTO_ID ")
				
					.append("    order by VALOR desc ")
				
					.append("   ) as sub ") 
				
				.append(" ) as subRank  ")
		
			.append(" where subRank.PRODUTO_ID = :idProduto");

		Query query  = getSession().createSQLQuery(sql.toString());
		query.setParameter("idProduto", idProduto);
		
		Number  retorno = (Number)  query.uniqueResult(); 
		
		return (retorno == null)?0L:retorno.longValue();
	}
	
	public Long obterRankingCota(Long idProduto,Long idCota){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select subRaink.rankCota ")
			
			.append(" from( ")
					
				.append(" select @rankCota\\:=@rankCota+1 as rankCota,sub.PRODUTO_ID,sub.COTA_ID,sub.VALOR ")
					
					.append(" from ( ")
					
						.append(" select @rankCota\\:=0) as init, ")
						
						.append(" (select  consolidado.PRODUTO_ID as PRODUTO_ID, ")
						
						.append("  sum(consolidado.VALOR_TOTAL_VENDA_COM_DESCONTO) as VALOR, ")
						
						.append("  consolidado.COTA_ID as COTA_ID  ")
						
						.append(" from view_consolidado_movimento_estoque_cota consolidado  ")
						
						.append(" group by consolidado.PRODUTO_ID,consolidado.COTA_ID ")
						
						.append(" order by VALOR desc")
						
						.append(" ) as sub ")
					
					.append(" where sub.PRODUTO_ID = :idProduto ")
					
					.append(" ) as subRaink ")
					
			.append(" where subRaink.COTA_ID = :idCota ");
	
		Query query  = getSession().createSQLQuery(sql.toString());
		query.setParameter("idProduto", idProduto);
		query.setParameter("idCota", idCota);
		
		Number  retorno = (Number)  query.uniqueResult(); 
	
		return (retorno == null)?0L:retorno.longValue();
	}
	
	@SuppressWarnings("unchecked")
	public Map<Long, Long> obterRankingProdutoCota(Long idCota){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("  	select  consolidado.PRODUTO_EDICAO_ID as idProdutoEdicao, sum(consolidado.VALOR_TOTAL_VENDA_COM_DESCONTO) as valor ")
		
		.append("    from VIEW_CONSOLIDADO_MOVIMENTO_ESTOQUE_COTA consolidado  ")
		
		.append("    where consolidado.COTA_ID = :idCota ")
	
		.append("    group by consolidado.PRODUTO_EDICAO_ID ")
	
		.append("    order by valor desc ");
		
		SQLQuery query  = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		query.addScalar("valor");
		
		query.setParameter("idCota", idCota);
		
		Map<Long, Long> mapRanking = new HashMap<>();
		
		List<Object[]> resultList = query.list();
		
		long i = 1;
		
		for (Object[] result : resultList) {
			
			mapRanking.put((long) result[0], i++);
		}
		
		return mapRanking;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Long, Long> obterRankingProdutoDistribuidor() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("  	select  consolidado.COTA_ID as idCota, sum(consolidado.VALOR_TOTAL_VENDA_COM_DESCONTO) as valor ")
		
		.append("    from VIEW_CONSOLIDADO_MOVIMENTO_ESTOQUE_COTA consolidado  ")
	
		.append("    group by consolidado.COTA_ID ")
	
		.append("    order by valor desc ");
		
		SQLQuery query  = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("valor");
		
		Map<Long, Long> mapRanking = new HashMap<>();
		
		List<Object[]> resultList = query.list();
		
		long i = 1;
		
		for (Object[] result : resultList) {
			
			mapRanking.put((long) result[0], i++);
		}
		
		return mapRanking;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Long, Long> obterRankingProdutoEditor() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("  	select  consolidado.EDITOR_ID as idEditor, sum(consolidado.VALOR_TOTAL_VENDA_COM_DESCONTO) as valor ")
		
		.append("    from VIEW_CONSOLIDADO_MOVIMENTO_ESTOQUE_COTA consolidado  ")
	
		.append("    group by consolidado.EDITOR_ID ")
	
		.append("    order by valor desc ");
		
		SQLQuery query  = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idEditor", StandardBasicTypes.LONG);
		query.addScalar("valor");
		
		Map<Long, Long> mapRanking = new HashMap<>();
		
		List<Object[]> resultList = query.list();
		
		long i = 1;
		
		for (Object[] result : resultList) {
			
			mapRanking.put((long) result[0], i++);
		}
		
		return mapRanking;
	}
	
	public Long obterRankingCotaDistribuidor(Long idCota){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select subRaink.rankCota ")
			
			.append(" from( ")
					
				.append(" select @rankCota\\:=@rankCota+1 as rankCota,sub.PRODUTO_ID,sub.COTA_ID,sub.VALOR ")
					
					.append(" from ( ")
					
						.append(" select @rankCota\\:=0) as init, ")
						
						.append(" (select  consolidado.PRODUTO_ID as PRODUTO_ID, ")
						
						.append("  sum(consolidado.VALOR_TOTAL_VENDA_COM_DESCONTO) as VALOR, ")
						
						.append("  consolidado.COTA_ID as COTA_ID  ")
						
						.append(" from view_consolidado_movimento_estoque_cota consolidado  ")
						
						.append(" group by consolidado.COTA_ID ")
						
						.append(" order by VALOR desc")
						
						.append(" ) as sub ")
					
					.append(" ) as subRaink ")
					
			.append(" where subRaink.COTA_ID = :idCota ");
		
		Query query  = getSession().createSQLQuery(sql.toString());
		query.setParameter("idCota", idCota);
		
		Number  retorno = (Number)  query.uniqueResult(); 
		
		return (retorno == null)?0L:retorno.longValue();
	}
	
	public Long obterRankingEditor(Long codigoEditor){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select selectRank.rankCota ")
			
			.append(" from ")
			
			.append(" (select  @rankEditor\\:=@rankEditor+1 as rankCota  ,subSelect.faturamentoCapa,subSelect.codigoEditor ")
	 		
			.append("	from (select @rankEditor\\:=0) as init, ")
			
	 		.append("	  	  (select ")
			
			.append("			  editor.CODIGO as codigoEditor,  ")
			
			.append("		     pessoaEditor.RAZAO_SOCIAL as nomeEditor,  ")
			
			.append("		     sum((estoqueProduto.QTDE_RECEBIDA - estoqueProduto.QTDE_DEVOLVIDA) * (movimentos.PRECO_COM_DESCONTO)) as faturamentoCapa   ")
				    
			.append("	     from  ")
			
			.append("	        ESTOQUE_PRODUTO_COTA as estoqueProduto  ")
			
			.append("	     left outer join  MOVIMENTO_ESTOQUE_COTA as movimentos  on estoqueProduto.ID=movimentos.ESTOQUE_PROD_COTA_ID  ")
			
			.append("	     left outer join  PRODUTO_EDICAO as produtoEdicao on estoqueProduto.PRODUTO_EDICAO_ID=produtoEdicao.ID  ")
			
			.append("	     left outer join  PRODUTO as produto  on produtoEdicao.PRODUTO_ID=produto.ID ") 
			
			.append("	     left outer join  PRODUTO_FORNECEDOR as produtoFornecedor on produto.ID=produtoFornecedor.PRODUTO_ID  ")
			
			.append("	     left outer join  FORNECEDOR as fornecedor on produtoFornecedor.fornecedores_ID=fornecedor.ID  ")
			
			.append("	     left outer join  EDITOR as editor on produto.EDITOR_ID=editor.ID  ")
			
			.append("	     left outer join  PESSOA as pessoaEditor  on editor.JURIDICA_ID=pessoaEditor.ID  ")
			
			.append("	     cross join       PESSOA as pessoaJuridica  ")
			
			.append("	     cross join       TIPO_MOVIMENTO as tipoMovimento  ")
			
			.append("	  where  ")
			
			.append("   	 movimentos.TIPO_MOVIMENTO_ID=tipoMovimento.ID  ")	
			
			.append("        and tipoMovimento.GRUPO_MOVIMENTO_ESTOQUE= :grupoMovimentoEstoque  ")
			
			.append("        and editor.JURIDICA_ID=pessoaJuridica.ID   ")
			    	
			.append("	   group by  editor.CODIGO , pessoaJuridica.RAZAO_SOCIAL ")
				        
			.append("	   order by faturamentoCapa desc ")
			   
			.append("	   ) as subSelect	") 
	   
			.append("	)as selectRank ")
	   
			.append("where selectRank.codigoEditor = :codigoEditor ");
		
		Query query  = getSession().createSQLQuery(sql.toString());
		query.setParameter("codigoEditor", codigoEditor);
		query.setParameter("grupoMovimentoEstoque", GrupoMovimentoEstoque.RECEBIMENTO_REPARTE.toString());
		
		Number  retorno = (Number)  query.uniqueResult(); 
		
		return (retorno == null)?0L:retorno.longValue();
		
	}
}