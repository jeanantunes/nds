package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.RankingRepository;

@Repository
public class RankingRepositoryImpl extends AbstractRepository  implements RankingRepository{
	
	@SuppressWarnings("unchecked")
	public Map<Long, Long> obterRankingProdutoPorCota(Long idCota){
		
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
	public Map<Long, Long> obterRankingCota() {
		
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
	public Map<Long, Long> obterRankingEditor() {
		
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
	
	@SuppressWarnings("unchecked")
	public Map<Long, Long> obterRankingProdutoPorProduto() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("  	select  consolidado.PRODUTO_ID as idProduto, sum(consolidado.VALOR_TOTAL_VENDA_COM_DESCONTO) as valor ")
		
		.append("    from VIEW_CONSOLIDADO_MOVIMENTO_ESTOQUE_COTA consolidado  ")
		
		.append("    group by consolidado.PRODUTO_ID ")
	
		.append("    order by valor desc ");
		
		SQLQuery query  = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idProduto", StandardBasicTypes.LONG);
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
	public Map<Long, Long> obterRankingCotaPorProduto(Long idProduto) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("  	select  consolidado.COTA_ID as idCota, sum(consolidado.VALOR_TOTAL_VENDA_COM_DESCONTO) as valor ")
		
		.append("    from VIEW_CONSOLIDADO_MOVIMENTO_ESTOQUE_COTA consolidado  ")
	
		.append("    where consolidado.PRODUTO_ID = :idProduto ")
		
		.append("    group by consolidado.COTA_ID ")
	
		.append("    order by valor desc ");
		
		SQLQuery query  = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("valor");
		
		query.setParameter("idProduto", idProduto);
		
		Map<Long, Long> mapRanking = new HashMap<>();
		
		List<Object[]> resultList = query.list();
		
		long i = 1;
		
		for (Object[] result : resultList) {
			
			mapRanking.put((long) result[0], i++);
		}
		
		return mapRanking;
	}
	
}