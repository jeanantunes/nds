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
	public void executeJobGerarRankingSegmento() {
		StringBuilder hql = new StringBuilder();

		hql.append("insert into ranking_segmento (COTA_ID, TIPO_SEGMENTO_PRODUTO_ID, QTDE) ");
		hql.append(" select cota_id, ");
		hql.append("        tipo_segmento_produto_id, ");
		hql.append("        sum(qtde) as qtde ");
		hql.append(" from (select epc.cota_id, ");
		hql.append("              tsp.id as tipo_segmento_produto_id, ");
		hql.append("              epc.qtde_recebida - epc.qtde_devolvida as qtde ");
		hql.append("       from estoque_produto_cota epc ");
		hql.append("       join lancamento l on l.produto_edicao_id = epc.produto_edicao_id ");
		hql.append("       join produto_edicao PE ON pe.ID = EPC.PRODUTO_EDICAO_ID ");
		hql.append("       join produto ON produto.ID = PE.PRODUTO_ID ");
		hql.append("       join TIPO_PRODUTO TP ON TP.id = produto.tipo_produto_id ");
		hql.append("       join tipo_segmento_produto TSP ON TSP.ID = produto.TIPO_SEGMENTO_PRODUTO_ID ");
		hql.append("       where TP.GRUPO_PRODUTO <> 'CROMO' ");
		hql.append(" 		   and TP.GRUPO_PRODUTO <> 'TALÃO' ");
		hql.append("            and l.data_lcto_distribuidor between DATE_SUB(NOW(), INTERVAL + 13 week) and NOW() ");
		hql.append("       group by epc.cota_id , tsp.ID , pe.ID) as target ");
		hql.append(" group by cota_id, tipo_segmento_produto_id ");
		hql.append(" order by qtde desc ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		query.executeUpdate();
	}

}
