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
		hql.append("     c.ID, ");
		hql.append("     tsp.ID, ");
		hql.append("     0, ");
		hql.append("     sysdate() ");
		hql.append(" from ");
		hql.append("     cota c, ");
		hql.append("     lancamento l ");
		hql.append("     join produto_edicao pe on l.PRODUTO_EDICAO_ID = pe.ID ");
		hql.append("     join produto p on pe.PRODUTO_ID = p.ID ");
		hql.append("     join tipo_segmento_produto tsp on tsp.ID = p.TIPO_SEGMENTO_PRODUTO_ID ");
		hql.append(" where ");
		hql.append("     not exists(select "); 
		hql.append("             rs.ID ");
		hql.append("         from ");
		hql.append("             ranking_segmento rs ");
		hql.append("         where ");
		hql.append("             rs.COTA_ID = c.id ");
		hql.append("                 and rs.TIPO_SEGMENTO_PRODUTO_ID = tsp.ID) ");
		hql.append("     and C.SITUACAO_CADASTRO IN ('ATIVO' , 'SUSPENSO') ");
		hql.append("     AND l.data_lcto_distribuidor BETWEEN DATE_SUB(NOW(), INTERVAL + 13 week) AND NOW() ");
		hql.append(" group by c.ID, tsp.ID ");
		
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
	
	@Override
	public void gerarRankingSegmento(Long idSegmento) {
		StringBuilder sql = new StringBuilder();

		sql.append(" INSERT INTO ranking_segmento (COTA_ID, TIPO_SEGMENTO_PRODUTO_ID, QTDE, DATA_GERACAO_RANK) ");
		sql.append(" select ");
		sql.append("     innerQuery.cotaId as cota_id, ");
		sql.append("     innerQuery.tspID as tipo_segmento_produto_id, ");
		sql.append("     sum(innerQuery.vd) as qtde, ");
		sql.append("     SYSDATE() ");
		sql.append(" from ");
		sql.append("     ( ");
		sql.append("       SELECT  ");
		sql.append("           tempUnion.cotaId,  ");
		sql.append("           tempUnion.tspId tspID,  ");
		sql.append("           sum(tempUnion.vendaSum) vd ");
		sql.append("       FROM( ");
		sql.append("             (  ");
		sql.append("              Select  ");
		sql.append("                      cast(T.COTA_ID as unsigned) as cotaId, ");
		sql.append("                      T.tspId, ");
		sql.append("                      SUM(T.venda) vendaSum ");
		sql.append("               from  ");
		sql.append("                   (SELECT  ");
		sql.append("                       mecReparte.COTA_ID AS COTA_ID,  ");
		sql.append("                       l.id as lancId,  ");
		sql.append("                       p.TIPO_SEGMENTO_PRODUTO_ID tspId, ");
		sql.append("                       (case  ");
		sql.append("                           when l.status IN ('FECHADO','RECOLHIDO','EM_RECOLHIMENTO') then  ");
		sql.append("                               cast(sum(CASE WHEN tipo.OPERACAO_ESTOQUE = 'ENTRADA' THEN   ");
		sql.append("                                   if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,mecReparte.QTDE,0)  ");
		sql.append("                                   ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,-mecReparte.QTDE,0)  ");
		sql.append("                                         END)  ");
		sql.append("                           - (select  ");
		sql.append("                               sum(mecEncalhe.qtde)  ");
		sql.append("                           from  ");
		sql.append("                               lancamento lanc  ");
		sql.append("                           LEFT JOIN  ");
		sql.append("                               chamada_encalhe_lancamento cel  ");
		sql.append("                                   on cel.LANCAMENTO_ID = lanc.ID  ");
		sql.append("                           LEFT JOIN  ");
		sql.append("                               chamada_encalhe ce  ");
		sql.append("                                   on ce.id = cel.CHAMADA_ENCALHE_ID  ");
		sql.append("                           LEFT JOIN  ");
		sql.append("                               chamada_encalhe_cota cec  ");
		sql.append("                                   on cec.CHAMADA_ENCALHE_ID = ce.ID  ");
		sql.append("                           LEFT JOIN  ");
		sql.append("                               cota cota  ");
		sql.append("                                   on cota.id = cec.COTA_ID  ");
		sql.append("                           LEFT JOIN  ");
		sql.append("                               conferencia_encalhe confEnc  ");
		sql.append("                                   on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID  ");
		sql.append("                           LEFT JOIN  ");
		sql.append("                               movimento_estoque_cota mecEncalhe  ");
		sql.append("                                   on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID  ");
		sql.append("                           WHERE  ");
		sql.append("                               lanc.id = l.id  ");
		sql.append("                               and cota.id = c.id) AS SIGNED INT)  ");
		sql.append("                           else      null  ");
		sql.append("                       end) as venda  ");
		sql.append("                   FROM  ");
		sql.append("                       lancamento l  ");
		sql.append("                   JOIN  ");
		sql.append("                       produto_edicao pe  ");
		sql.append("                           ON pe.id = l.produto_edicao_id   ");
		sql.append("                   LEFT JOIN  ");
		sql.append("                       periodo_lancamento_parcial plp  ");
		sql.append("                           ON plp.id = l.periodo_lancamento_parcial_id  ");
		sql.append("                   JOIN  ");
		sql.append("                       produto p  ");
		sql.append("                           ON p.id = pe.produto_id  ");
		sql.append("                   LEFT JOIN  ");
		sql.append("                       movimento_estoque_cota mecReparte  ");
		sql.append("                           on mecReparte.LANCAMENTO_ID = l.id  ");
		sql.append("                   LEFT JOIN  ");
		sql.append("                       tipo_movimento tipo  ");
		sql.append("                           ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID  ");
		sql.append("                   JOIN  ");
		sql.append("                       cota c  ");
		sql.append("                           ON mecReparte.COTA_ID = c.ID  ");
		sql.append("                   JOIN  ");
		sql.append("                       pessoa pess  ");
		sql.append("                           ON c.PESSOA_ID = pess.ID  ");
		sql.append("                   inner join  ");
		sql.append("                       endereco_cota endCota  ");
		sql.append("                           ON endCota.cota_id = c.id  ");
		sql.append("                           and endCota.principal = true  ");
		sql.append("                   inner join  ");
		sql.append("                       endereco endereco  ");
		sql.append("                           ON endCota.endereco_id = endereco.id  ");
		sql.append("                    JOIN  ");
		sql.append("                       tipo_produto tp  ");
		sql.append("                           ON p.TIPO_PRODUTO_ID = tp.ID ");
		sql.append("                   JOIN   ");
		sql.append("                       tipo_segmento_produto tsp  ");
		sql.append("                           ON tsp.ID = p.TIPO_SEGMENTO_PRODUTO_ID  ");
		sql.append("                   WHERE  ");
		sql.append("                       l.status in ('EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO')  ");
		sql.append("                       AND tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE'  ");
		sql.append("                       AND tsp.ID = :idSegmento  ");
		sql.append("                       AND l.DATA_REC_DISTRIB BETWEEN DATE_SUB(NOW(), INTERVAL + 13 week) AND NOW()   ");
		sql.append("                       AND c.SITUACAO_CADASTRO in ('ATIVO' , 'SUSPENSO') ");
		sql.append("                       AND TP.GRUPO_PRODUTO not in ('CROMO' , 'TALÃO') ");
		sql.append("                   group by  ");
		sql.append("                       pe.numero_edicao,  ");
		sql.append("                       pe.id,  ");
		sql.append("                       mecReparte.cota_id,  ");
		sql.append("                       plp.numero_periodo ");
		sql.append("                   ORDER BY  ");
		sql.append("                       l.ID desc)T ");
		sql.append("                        GROUP BY COTA_ID )");
		sql.append("       UNION ");

		sql.append("       (SELECT  ");
		sql.append("          cast(c.id as unsigned) as cotaId, ");
		sql.append("           :idSegmento as tspId, ");
		sql.append("           0 as vendaSum  ");
		sql.append("         from cota c   ");
		sql.append("           where c.SITUACAO_CADASTRO in ('ATIVO' , 'SUSPENSO')) ");
		sql.append("           order by cotaId ");
		sql.append("       )tempUnion  ");
		sql.append("       group by cotaId ");
		
		sql.append("     ) as innerQuery ");
		
		sql.append(" group by innerQuery.cotaId, innerQuery.tspID ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("idSegmento", idSegmento);
		
		query.executeUpdate();
	}

}
