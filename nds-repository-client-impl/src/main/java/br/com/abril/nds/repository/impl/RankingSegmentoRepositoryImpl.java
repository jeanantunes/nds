package br.com.abril.nds.repository.impl;

import java.math.BigInteger;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.repository.RankingSegmentoRepository;

@Repository
public class RankingSegmentoRepositoryImpl extends RankingAbstract implements RankingSegmentoRepository {

	
	@Override
	public void executeJobGerarRankingSegmento() {
		StringBuilder hql = new StringBuilder();

		BigInteger novoId = criarNovoIDRanking();
		
		hql.append(
				" insert into ranking_segmento (COTA_ID,TIPO_SEGMENTO_PRODUTO_ID,SEGMENTO_DESCRICAO,PRODUTO_EDICAO_ID,QTDE,RANKING_SEGMENTOS_GERADOS_ID)( ")
				.append(" select cota_id,tipo_segmento_produto_id,segmento_descricao,produto_edicao_id,sum(qtde) as qtde,").append(novoId).append(" from ")
				.append(" (select mec.cota_id,tsp.id as tipo_segmento_produto_id,	tsp.descricao segmento_descricao, pe.id produto_edicao_id, mec.tipo_movimento_id, ")
				.append(" case when mec.tipo_movimento_id=21 then (mec.qtde) ")
				.append(" when mec.tipo_movimento_id=26 then (mec.qtde*-1) ")
				.append(" else 0 end as qtde ")
				.append(" from movimento_estoque_cota mec ")
				.append(" join estoque_produto_cota EPC ON EPC.ID = mec.ESTOQUE_PROD_COTA_ID ")
				.append(" join produto_edicao PE ON pe.ID = EPC.PRODUTO_EDICAO_ID ")
				.append(" join produto ON produto.ID = PE.PRODUTO_ID ")
				.append(" join TIPO_PRODUTO TP on TP.id=produto.tipo_produto_id ")
				.append(" join tipo_segmento_produto TSP ON TSP.ID = produto.TIPO_SEGMENTO_PRODUTO_ID ")
				.append(" where ")
				.append(" (mec.tipo_movimento_id=21 or mec.tipo_movimento_id=26) and ")
				.append(" (TP.GRUPO_PRODUTO<>'CROMO' and  TP.GRUPO_PRODUTO<>'TALÃO') and ")
				.append(" mec.DATA between DATE_SUB(NOW(),INTERVAL + 13 week) and NOW() ")
				.append(" group by mec.cota_id,tsp.ID,pe.ID ")
				.append(" ) as target ")
				.append(" group by cota_id,tipo_segmento_produto_id ")
				.append(" order by qtde desc) ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		query.executeUpdate();

	}

	@Override
	String getTipoRanking() {
		return "segmento";
	}


}
