package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RankingDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDTO;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.RankingRepository;

@Repository
public class RankingRepositoryImpl extends AbstractRepository  implements RankingRepository {
	
	
	
	@SuppressWarnings("unchecked")
	public Map<Long, RankingDTO> obterRankingCota(FiltroCurvaABCDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		
		sql.append(" consolidado.COTA_ID as chave,	");
		sql.append(" consolidado.NUMERO_COTA as numeroCota,	");
		sql.append(" @valorAcumulado\\:=@valorAcumulado + consolidado.valor as valorAcumulado,	");
		sql.append(" @posicaoRanking\\:=@posicaoRanking + 1 as ranking, ");
		sql.append(" consolidado.valor as valor,						");
		sql.append(" consolidado.vendaExemplares as vendaExemplares,	");
		sql.append(" consolidado.faturamentoCapa as faturamentoCapa,	");
		sql.append(" consolidado.reparte as reparte,	");
		sql.append(" consolidado.valorMargemDistribuidor as valorMargemDistribuidor, 			");
		sql.append(" consolidado.porcentagemMargemDistribuidor as porcentagemMargemDistribuidor	");
		
		sql.append(" from ");
		
		sql.append(obterSQLRanking(filtro))
	
		.append(" ,(select @valorAcumulado\\:=0, @posicaoRanking\\:=0) as s ORDER BY faturamentoCapa desc, numeroCota ");
		
		SQLQuery query  = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("chave", StandardBasicTypes.LONG);
		query.addScalar("valor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("valorAcumulado", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("ranking", StandardBasicTypes.LONG);
		query.addScalar("vendaExemplares", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("faturamentoCapa", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("valorMargemDistribuidor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("porcentagemMargemDistribuidor", StandardBasicTypes.BIG_DECIMAL);
		
		
		getFiltroRanking(filtro, query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(RankingDTO.class));
		
		List<RankingDTO> resultList = query.list();
		
		return popularMapRanking(resultList);
	}
	
	private Map<Long, RankingDTO> popularMapRanking(List<RankingDTO> resultList){
		
		Map<Long, RankingDTO> mapRanking = new HashMap<>();
		
		for (RankingDTO result : resultList) {
			
			Long chave = result.getChave();
			
			mapRanking.put(chave, result);
		}
		
		return mapRanking;
		
	}
	
	public StringBuilder obterSQLRanking(FiltroCurvaABCDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" ( select ");
		
		sql.append(" movimento_estoque_cota.COTA_ID AS COTA_ID, "); 
		sql.append(" cota.NUMERO_COTA AS NUMERO_COTA,");
		sql.append(" pessoa.NOME AS NOME_COTA,");
		sql.append(" pessoa.RAZAO_SOCIAL AS RAZAO_SOCIAL_COTA,");
		sql.append(" endereco.CIDADE AS CIDADE_COTA, ");
		sql.append(" produto_edicao.PRODUTO_ID AS PRODUTO_ID, 	");
		sql.append(" produto.EDITOR_ID AS EDITOR_ID, 			");
		sql.append(" pessoaeditor.RAZAO_SOCIAL as RAZAO_SOCIAL_EDITOR,");
		sql.append(" produto.CODIGO as CODIGO_PRODUTO,			");
		sql.append(" produto.nome as NOME_PRODUTO,				");
		sql.append(" movimento_estoque_cota.PRODUTO_EDICAO_ID AS PRODUTO_EDICAO_ID, ");
		sql.append(" produto_edicao.NUMERO_EDICAO AS NUMERO_EDICAO, ");
		sql.append(" movimento_estoque_cota.DATA AS DATA_MOVIMENTO, ");
		sql.append(" produto_edicao.PRECO_VENDA AS PRECO_VENDA,		");
		
		//valor venda com desconto
		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append(" * (produto_edicao.PRECO_VENDA - ((produto_edicao.PRECO_VENDA * coalesce(movimento_estoque_cota.VALOR_DESCONTO,0)) / 100)) ");
		sql.append(" ) as valor, ");

		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append(" ) as vendaExemplares, ");

		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append(" * produto_edicao.PRECO_VENDA  ");
		sql.append(" ) as faturamentoCapa, ");

		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else 0 end");
		sql.append(" ) as reparte, ");
		
		sql.append(" sum( ");
		sql.append(" 	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		
		sql.append(" 	 * (movimento_estoque_cota.PRECO_COM_DESCONTO - (produto_edicao.PRECO_VENDA ");
		sql.append(" 	  		- (produto_edicao.PRECO_VENDA * COALESCE(descontologistica.PERCENTUAL_DESCONTO, 0) / 100) ) ");
		sql.append(" 			) ");
		
		sql.append(" ) as valorMargemDistribuidor, ");

		sql.append(" sum( ");

		sql.append(" (	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		
		sql.append(" 	* (movimento_estoque_cota.PRECO_COM_DESCONTO - (produto_edicao.PRECO_VENDA ");
		sql.append(" 	  		- (produto_edicao.PRECO_VENDA * COALESCE(descontologistica.PERCENTUAL_DESCONTO, 0) / 100) ) ");
		sql.append(" 	  ) ");
		
		sql.append(" ) ");
		
		sql.append(" / ");
		
		sql.append(" (	case when (tipomovimento.OPERACAO_ESTOQUE = 'ENTRADA') then movimento_estoque_cota.qtde else ");
		sql.append("	(movimento_estoque_cota.qtde*-1) end ");
		sql.append(" * produto_edicao.PRECO_VENDA )  ");
		
		sql.append(" ) as porcentagemMargemDistribuidor ");
		
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
		
		sql.append(" 	and tipomovimento.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE' ");
        sql.append(" 	and movimento_estoque_cota.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
		
		sql.append(this.getFiltroRanking(filtro, null));
		
		sql.append("	group by movimento_estoque_cota.COTA_ID	");
		
		sql.append("	) as consolidado ");
		
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
	
}