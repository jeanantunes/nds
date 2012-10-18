package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

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
		
		Double  retorno = (Double) query.uniqueResult(); 
		
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
		
		Double  retorno = (Double) query.uniqueResult(); 
		
		return (retorno == null)?0L:retorno.longValue();
	}
	
	public Long obterRankingProdutoCota(Long idCota,Long idProduto){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select subRank.rankProduto ")
			
			.append(" from ( ")
				
				.append(" select @rankProduto \\:= @rankProduto+1 as rankProduto, sub.PRODUTO_ID  ")
			
				.append("  from ( ")
					
					.append(" select @rankProduto \\:= 0) as init, ")
									
					.append("  	(select  consolidado.PRODUTO_ID as PRODUTO_ID, consolidado.COTA_ID as COTA_ID ,sum(consolidado.VALOR_TOTAL_VENDA_COM_DESCONTO) as VALOR ")
				
					.append("    from view_consolidado_movimento_estoque_cota consolidado  ")
				
					.append("    group by consolidado.PRODUTO_ID ")
				
					.append("    order by VALOR desc ")
				
					.append("   ) as sub ") 
					
					.append(" where sub.COTA_ID = :idCota ")
				
				.append(" ) as subRank  ")
		
			.append(" where subRank.PRODUTO_ID = :idProduto");
	
		Query query  = getSession().createSQLQuery(sql.toString());
		query.setParameter("idProduto", idProduto);
		query.setParameter("idCota", idCota);
		
		Double  retorno = (Double) query.uniqueResult(); 
		
		return (retorno == null)?0L:retorno.longValue();
		
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
		
		Double  retorno = (Double) query.uniqueResult(); 
		
		return (retorno == null)?0L:retorno.longValue();
	}
}