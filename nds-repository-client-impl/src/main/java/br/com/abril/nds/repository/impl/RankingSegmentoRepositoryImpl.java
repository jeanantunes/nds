package br.com.abril.nds.repository.impl;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.distribuicao.RankingSegmento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RankingSegmentoRepository;

@Repository
public class RankingSegmentoRepositoryImpl extends AbstractRepositoryModel<RankingSegmento, Long> implements RankingSegmentoRepository {

	public RankingSegmentoRepositoryImpl() {
		super(RankingSegmento.class);
	}

	@Override
	public void gerarRankingSegmento() {
		StringBuilder hql = new StringBuilder();

		hql.append(" INSERT INTO ranking_segmento (COTA_ID, TIPO_SEGMENTO_PRODUTO_ID, QTDE, DATA_GERACAO_RANK) ");
		hql.append(" select ");
		hql.append("     innerQuery.cota_id as cota_id, ");
		hql.append("     innerQuery.tipo_segmento_produto_id as tipo_segmento_produto_id, ");
		hql.append("     sum(innerQuery.qtde) as qtde, ");
		hql.append("     SYSDATE() ");
		hql.append(" from ");
		hql.append("     (SELECT ");
		hql.append("         epc.cota_id as cota_id, ");
		hql.append("             tsp.id AS tipo_segmento_produto_id, ");
		hql.append("             epc.qtde_recebida - epc.qtde_devolvida AS qtde ");
		hql.append("     from ");
		hql.append("         estoque_produto_cota epc ");
		hql.append("     JOIN lancamento l ON l.produto_edicao_id = epc.produto_edicao_id ");
		hql.append("     JOIN produto_edicao PE ON pe.ID = EPC.PRODUTO_EDICAO_ID ");
		hql.append("     JOIN produto ON produto.ID = PE.PRODUTO_ID ");
		hql.append("     JOIN TIPO_PRODUTO TP ON TP.id = produto.tipo_produto_id ");
		hql.append("     JOIN tipo_segmento_produto TSP ON TSP.ID = produto.TIPO_SEGMENTO_PRODUTO_ID ");
		hql.append("     JOIN cota c ON epc.COTA_ID = c.ID ");
		hql.append("     WHERE ");
		hql.append("         TP.GRUPO_PRODUTO not in ('CROMO' , 'TALÃO') ");
		hql.append("             AND l.data_lcto_distribuidor BETWEEN DATE_SUB(NOW(), INTERVAL + 13 week) AND NOW() ");
		hql.append("             AND c.SITUACAO_CADASTRO in ('ATIVO' , 'SUSPENSO') ");
		hql.append("     GROUP BY epc.cota_id , pe.ID) as innerQuery ");
		hql.append(" group by innerQuery.cota_id, innerQuery.tipo_segmento_produto_id ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		query.executeUpdate();
	}
	
	//@Override
	public void gerarRankingSegmentoParaCotasSemRanking() {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" INSERT INTO ranking_segmento (COTA_ID, TIPO_SEGMENTO_PRODUTO_ID, QTDE, DATA_GERACAO_RANK) ");
		hql.append(" select ");
		hql.append("     c.ID as cota_id, ");
		hql.append("     ranking.TIPO_SEGMENTO_PRODUTO_ID as tipo_segmento_produto_id, ");
		hql.append("     0 as qtde, ");
		hql.append("     SYSDATE() ");
		hql.append(" from ");
		hql.append("     cota c, ");
		hql.append("     ranking_segmento ranking ");
		hql.append(" where ");
		hql.append("     not exists( select "); 
		hql.append("             rs.ID ");
		hql.append("         from ");
		hql.append("             ranking_segmento rs ");
		hql.append("         where ");
		hql.append("             rs.COTA_ID = c.id ");
		hql.append("                 and rs.TIPO_SEGMENTO_PRODUTO_ID = ranking.TIPO_SEGMENTO_PRODUTO_ID) ");
		hql.append("         AND C.SITUACAO_CADASTRO IN ('ATIVO' , 'SUSPENSO') ");
		hql.append(" group by c.ID, ranking.TIPO_SEGMENTO_PRODUTO_ID ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		query.executeUpdate();
	}
	
	@Override
	public void deletarRankingSegmento() {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" delete from ranking_segmento ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		query.executeUpdate();
	}

}
