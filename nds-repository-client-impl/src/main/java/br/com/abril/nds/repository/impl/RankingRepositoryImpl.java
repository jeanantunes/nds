package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RankingDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDTO;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.RankingRepository;

@Repository
public class RankingRepositoryImpl extends AbstractRepository  implements RankingRepository {
	
	@SuppressWarnings("unchecked")
	public Map<Long, RankingDTO> obterRankingProdutoPorCota(FiltroCurvaABCDTO filtro, Long idCota) {
		
		StringBuilder sql = new StringBuilder();

		sql.append(" select	");
		
		sql.append(" subRnkg.idProdutoEdicao as idProdutoEdicao, 	");
		sql.append(" subRnkg.valor as valor, 	");
		sql.append(" @valorAcumulado\\:=@valorAcumulado + subRnkg.valor as vlrAcumulado, ");
		sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as posicaoRanking ");
		
		sql.append(" from ( ");
		
		sql.append(" select ");
		
		sql.append(" consolidado.PRODUTO_EDICAO_ID as idProdutoEdicao, ");
		sql.append(" consolidado.valor as valor ");
		
		sql.append(" from ");
		
		sql.append(obterSQLRanking(filtro));
		
		sql.append(" where consolidado.COTA_ID = :idCota 	");
		
		sql.append(" order by consolidado.valor desc ) as subRnkg, (select @valorAcumulado\\:=0, @posicaoRanking\\:=0) as s ");
		
		SQLQuery query  = getSession().createSQLQuery(sql.toString());
		
		
		query.addScalar("idProdutoEdicao", StandardBasicTypes.LONG);
		query.addScalar("valor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("vlrAcumulado", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("posicaoRanking", StandardBasicTypes.LONG);
		
		query.setParameter("idCota", idCota);
		
		getFiltroRanking(filtro, query);
		
		List<Object[]> resultList = query.list();
		
		return popularMapRanking(resultList);
	}
	
	
	@SuppressWarnings("unchecked")
	public Map<Long, RankingDTO> obterRankingCota(FiltroCurvaABCDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select	");
		
		sql.append(" subRnkg.idCota as idCota, 	");
		sql.append(" subRnkg.valor as valor, 	");
		sql.append(" @valorAcumulado\\:=@valorAcumulado + subRnkg.valor as vlrAcumulado, ");
		sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as posicaoRanking ");
		
		sql.append(" from ( ");
		
		sql.append(" select ");

		sql.append(" consolidado.COTA_ID as idCota,	");
		sql.append(" consolidado.valor as valor 	");
		
		sql.append(" from ");
		
		sql.append(obterSQLRanking(filtro))
	
		.append("    group by consolidado.COTA_ID ")
	
		.append("    order by consolidado.valor desc ) as subRnkg, (select @valorAcumulado\\:=0, @posicaoRanking\\:=0) as s  ");
		
		SQLQuery query  = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("valor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("vlrAcumulado", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("posicaoRanking", StandardBasicTypes.LONG);
		
		getFiltroRanking(filtro, query);
		
		List<Object[]> resultList = query.list();
		
		return popularMapRanking(resultList);
	}
	
	private Map<Long, RankingDTO> popularMapRanking(List<Object[]> resultList){
		
		Map<Long, RankingDTO> mapRanking = new HashMap<>();
		
		for (Object[] result : resultList) {
			
			long chave = (long) result[0];
			
			BigDecimal valor = (BigDecimal) result[1];
			
			BigDecimal valorAcumulado = (BigDecimal) result[2];
			
			Long posicaoRanking = (Long) result[3];
			
			RankingDTO ranking = new RankingDTO(posicaoRanking, valor, valorAcumulado);
			
			mapRanking.put(chave, ranking);
		}
		
		return mapRanking;
		
	}
	
	@SuppressWarnings("unchecked")
	public StringBuilder obterSQLRanking(FiltroCurvaABCDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" ( select ");
		
		sql.append(" movimento_estoque_cota.COTA_ID AS COTA_ID, "); 
		sql.append(" produto_edicao.PRODUTO_ID AS PRODUTO_ID, 	");
		sql.append(" produto.EDITOR_ID AS EDITOR_ID, 			");
		sql.append(" movimento_estoque_cota.PRODUTO_EDICAO_ID AS PRODUTO_EDICAO_ID, ");
		sql.append(" produto_edicao.NUMERO_EDICAO AS NUMERO_EDICAO, ");
		sql.append(" movimento_estoque_cota.DATA AS DATA_MOVIMENTO, ");
		sql.append(" produto_edicao.PRECO_VENDA AS PRECO_VENDA,		");
		
		sql.append("	sum(( ");
		sql.append("		( ");
		sql.append("			coalesce(( ");
		sql.append("			select sum(mov_sub.QTDE) AS valor_venda ");
		sql.append("			from ((movimento_estoque_cota mov_sub join produto_edicao prod_sub on((mov_sub.PRODUTO_EDICAO_ID = prod_sub.ID))) ");
		sql.append("			join tipo_movimento tipo_mov_sub on((mov_sub.TIPO_MOVIMENTO_ID = tipo_mov_sub.ID))) ");
		sql.append("			where ((tipo_mov_sub.OPERACAO_ESTOQUE = 'ENTRADA') and (mov_sub.COTA_ID = movimento_estoque_cota.COTA_ID)         ");
		sql.append("			and (mov_sub.ID = movimento_estoque_cota.ID) ");
		sql.append("			and (prod_sub.ID = produto_edicao.ID))),0) - ");
		
		sql.append("			coalesce(( ");
		sql.append("			select sum(mov_sub_sd.QTDE) AS valor_venda ");
		sql.append("			from ((movimento_estoque_cota mov_sub_sd join produto_edicao prod_sub_sd on((mov_sub_sd.PRODUTO_EDICAO_ID = prod_sub_sd.ID)))           ");              
		sql.append("			join tipo_movimento tipo_mov_sub_sd on((mov_sub_sd.TIPO_MOVIMENTO_ID = tipo_mov_sub_sd.ID))) ");
		sql.append("			where ((tipo_mov_sub_sd.OPERACAO_ESTOQUE = 'SAIDA')                                                                                     ");
		sql.append("			and (mov_sub_sd.COTA_ID = movimento_estoque_cota.COTA_ID)                                                                               ");
		sql.append("			and (mov_sub_sd.ID = movimento_estoque_cota.ID) and (prod_sub_sd.ID = produto_edicao.ID))),0)                                           ");
		sql.append("		) * (produto_edicao.PRECO_VENDA - ((produto_edicao.PRECO_VENDA * coalesce(movimento_estoque_cota.VALOR_DESCONTO,0)) / 100))                 ");
		sql.append("		)) AS valor                                                                                                        ");
		
		sql.append("	from   ");
		
		sql.append("	movimento_estoque_cota  ");
		
		sql.append("	inner join produto_edicao on movimento_estoque_cota.produto_edicao_id = produto_edicao.id                        ");
		sql.append("	inner join produto on (produto_edicao.produto_id = produto.id)                                                   ");
		sql.append("	inner join tipo_movimento tipomovimento on movimento_estoque_cota.tipo_movimento_id = tipomovimento.id           ");
		sql.append("	inner join produto_fornecedor produtofornecedor on produto.id = produtofornecedor.produto_id                     ");
		sql.append("	inner join fornecedor on produtofornecedor.fornecedores_id = fornecedor.id                                       ");
		sql.append("	inner join cota on movimento_estoque_cota.cota_id = cota.id                                                      ");
		sql.append("	inner join pessoa on pessoa.id = cota.pessoa_id                                                                  ");
		sql.append("	inner join endereco_cota enderecocota on enderecocota.cota_id = cota.id and enderecocota.principal = true        ");
		sql.append("	inner join endereco endereco on enderecocota.endereco_id = endereco.id                                           ");
		sql.append("	inner join editor on editor.id = produto.editor_id                                                               ");
		sql.append("	inner join pessoa pessoaeditor on editor.juridica_id = pessoaeditor.id                                           ");
		sql.append("	left join desconto_logistica descontologistica on descontologistica.id = produto_edicao.desconto_logistica_id    ");
		sql.append("	inner join lancamento on lancamento.id =                                                                         ");
		sql.append("	(                                                                                                                ");
		sql.append("		case when (produto_edicao.parcial)                                                                           ");
		sql.append("				then (select id from lancamento where produto_edicao_id = produto_edicao.id                          ");
		sql.append("							order by id asc limit 1)                                                                 ");
		sql.append("				else (select id from lancamento where produto_edicao_id = produto_edicao.id                          ");
		sql.append("							order by id desc limit 1)                                                                ");
		sql.append("			end                                                                                                      ");
		sql.append("	)                                                                                                                ");
	    sql.append("                                                                                                                     ");
		sql.append("	left join fechamento_encalhe fechamentoencalhe on                                                                ");
		sql.append("		(fechamentoencalhe.data_encalhe = lancamento.data_rec_distrib                                                ");
		sql.append("		and fechamentoencalhe.produto_edicao_id = produto_edicao.id)                                                 ");
		
		sql.append("	WHERE	");
		
		sql.append("	lancamento.status IN ( 'EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO' ) 	   ");
		
		sql.append(this.getFiltroRanking(filtro, null));
		
		sql.append("		group by movimento_estoque_cota.PRODUTO_EDICAO_ID,   ");
		sql.append("		movimento_estoque_cota.COTA_ID, ");
		sql.append("		movimento_estoque_cota.DATA     ");
		
		sql.append("		order by movimento_estoque_cota.DATA desc ) as consolidado ");
		
		return sql;
	}
	

	private String getFiltroRanking(FiltroCurvaABCDTO filtro, Query query) {
		
		StringBuilder hql = null;
		
		if (query == null){
			hql = new StringBuilder();
			hql.append(" and lancamento.DATA_REC_DISTRIB BETWEEN :dataDe AND :dataAte ");
		} else {
			
			query.setParameter("dataDe",  filtro.getDataDe());
			query.setParameter("dataAte", filtro.getDataAte());
		}
		
		if (filtro.getCodigoFornecedor() != null && !filtro.getCodigoFornecedor().isEmpty() && !filtro.getCodigoFornecedor().equals("0")) {
			
			if (query == null){
				
				hql.append("AND fornecedor.ID = :codigoFornecedor ");
			} else {
				
				query.setParameter("codigoFornecedor", Long.parseLong(filtro.getCodigoFornecedor()));
			}
		}

		if (filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()) {
			
			if (query == null){
				
				hql.append(" AND produto.CODIGO = :codigoProduto ");
			} else {
				
				query.setParameter("codigoProduto", filtro.getCodigoProduto());
			}
		}

		if (filtro.getEdicaoProduto() != null && !filtro.getEdicaoProduto().isEmpty()) {
			
			if (query == null){
				
				hql.append(" AND produtoEdicao.NUMERO_EDICAO in (:edicaoProduto) ");
			} else {
				
				query.setParameterList("edicaoProduto", filtro.getEdicaoProduto());
			}
		}

		if (filtro.getCodigoEditor() != null && !filtro.getCodigoEditor().isEmpty() && !filtro.getCodigoEditor().equals("0")) {
			
			if (query == null){
				
				hql.append(" AND produto.EDITOR_ID = :codigoEditor ");
			} else {
				
				query.setParameter("codigoEditor", Long.parseLong(filtro.getCodigoEditor()));
			}
		}

		if (filtro.getCodigoCota() != null) {
			
			if (query == null){
				
				hql.append(" AND cota.NUMERO_COTA = :codigoCota ");
			} else {
				
				query.setParameter("codigoCota", filtro.getCodigoCota());
			}
		}

		if (filtro.getMunicipio() != null && !filtro.getMunicipio().isEmpty() && !filtro.getMunicipio().equalsIgnoreCase("Todos")) {
			
			if (query == null){
				
				hql.append(" AND endereco.CIDADE = :municipio ");
			} else {
				
				query.setParameter("municipio", filtro.getMunicipio());
			}
		}
		
		if (query == null){
			
			return hql.toString();
		}
		
		return null;
	}	
	
	
	@SuppressWarnings("unchecked")
	public Map<Long, RankingDTO> obterRankingEditor(FiltroCurvaABCDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		
		sql.append(" subRnkg.idEditor as idEditor, 	");
		sql.append(" subRnkg.valor as valor, 	");
		sql.append(" @valorAcumulado\\:=@valorAcumulado + subRnkg.valor as vlrAcumulado, ");
		sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as posicaoRanking ")
		
		.append(" from ")
		
		.append(" ( ")
		
		.append(" select ")

		.append(" consolidado.EDITOR_ID as idEditor, ")
		.append(" consolidado.valor as valor ")
		
		.append(" from ")
		
		.append(obterSQLRanking(filtro))
	
		.append("    group by consolidado.EDITOR_ID ")
	
		.append("    order by consolidado.valor desc ) as subRnkg, (select @valorAcumulado\\:=0, @posicaoRanking\\:=0) as s  ");
		
		SQLQuery query  = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idEditor", StandardBasicTypes.LONG);
		query.addScalar("valor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("vlrAcumulado", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("posicaoRanking", StandardBasicTypes.LONG);
		
		getFiltroRanking(filtro, query);
		
		List<Object[]> resultList = query.list();
		
		return popularMapRanking(resultList);
	}
	
	@SuppressWarnings("unchecked")
	public Map<Long, RankingDTO> obterRankingProdutoPorProduto(FiltroCurvaABCDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");

		sql.append(" subRnkg.idProduto as idProduto, 	");
		sql.append(" subRnkg.valor as valor, 	");
		sql.append(" @valorAcumulado\\:=@valorAcumulado + subRnkg.valor as vlrAcumulado, ");
		sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as posicaoRanking ")
		
		.append(" from ")
		
		.append(" ( ");
		
		sql.append(" select consolidado.PRODUTO_ID as idProduto, consolidado.valor as valor ")
		
		.append(" from ")
		
		.append(obterSQLRanking(filtro))
		
		.append("    group by consolidado.PRODUTO_ID ") 	
	
		.append("    order by consolidado.valor desc ) as subRnkg, (select @valorAcumulado\\:=0, @posicaoRanking\\:=0) as s  ");
		
		SQLQuery query  = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idProduto", StandardBasicTypes.LONG);
		query.addScalar("valor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("vlrAcumulado", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("posicaoRanking", StandardBasicTypes.LONG);
		
		getFiltroRanking(filtro, query);
		
		List<Object[]> resultList = query.list();
		
		return popularMapRanking(resultList);

	}
	
	@SuppressWarnings("unchecked")
	public Map<Long, RankingDTO> obterRankingCotaPorProduto(FiltroCurvaABCDTO filtro, Long idProduto) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");

		sql.append(" subRnkg.idCota as idCota, 	");
		sql.append(" subRnkg.valor as valor, 	");
		sql.append(" @valorAcumulado\\:=@valorAcumulado + subRnkg.valor as vlrAcumulado, ");
		sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as posicaoRanking ");
		
		sql.append(" from ");
		
		sql.append(" ( ");
		
		sql.append(" select  consolidado.COTA_ID as idCota, consolidado.valor as valor ")
		
		.append(" from ")
		
		.append(obterSQLRanking(filtro))

		.append("    where consolidado.PRODUTO_ID = :idProduto ")
		
		.append("    group by consolidado.COTA_ID ")
	
		.append("    order by consolidado.valor desc ) as subRnkg, (select @valorAcumulado\\:=0, @posicaoRanking\\:=0) as s  ");
		
		
		SQLQuery query  = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("idCota", StandardBasicTypes.LONG);
		query.addScalar("valor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("vlrAcumulado", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("posicaoRanking", StandardBasicTypes.LONG);
	
		query.setParameter("idProduto", idProduto);
		
		getFiltroRanking(filtro, query);
		
		
		List<Object[]> resultList = query.list();
		
		return popularMapRanking(resultList);

	}
	
}