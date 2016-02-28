package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.distribuicao.RankingFaturamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RankingFaturamentoRepository;

@Repository
public class RankingFaturamentoRepositoryImpl extends AbstractRepositoryModel<RankingFaturamento, Long> implements
		RankingFaturamentoRepository {
	
	public RankingFaturamentoRepositoryImpl(){
		super(RankingFaturamento.class);
	}

	@Override
	public void gerarRankingFaturamento() {
		StringBuilder sql = new StringBuilder();

		sql.append(" INSERT INTO ranking_faturamento (COTA_ID, FATURAMENTO, DATA_GERACAO_RANK) ");
		
		sql.append("  select  ");
		sql.append(" 		     innerQuery.cotaId as cota_id,  ");
		sql.append(" 		     sum(innerQuery.vd) as qtde,  ");
		sql.append(" 		     SYSDATE()  ");
		sql.append(" 		 from  ");
		sql.append(" 		     (  ");
		sql.append(" 		       SELECT   ");
		sql.append(" 		           tempUnion.cotaId,   ");
		sql.append(" 		           sum(tempUnion.vendaSum) vd  ");
		sql.append(" 		       FROM( ");
		sql.append(" 		             (   ");

		sql.append(" 					  Select   ");
		sql.append("                        T2.ctId as cotaId,  ");
		sql.append("                        (SUM(T2.vdSum * T2.PRECO_VENDA)) as vendaSum  ");
		sql.append("                      FROM (   ");
		sql.append("                            Select    ");
		sql.append("                                    cast(T.COTA_ID as unsigned) as ctId,   ");
		sql.append("                                    SUM(T.venda) vdSum,  ");
		sql.append("                                    T.PRECO_VENDA  ");
		sql.append("                            from    ");
		sql.append("                                (SELECT    ");
		sql.append("                                    mecReparte.COTA_ID AS COTA_ID,    ");
		sql.append("                                    l.id as lancId,  ");
		sql.append("                                    pe.PRECO_VENDA AS PRECO_VENDA,  ");
		sql.append("                                    (case    ");
		sql.append("                                        when l.status IN ('FECHADO','RECOLHIDO','EM_RECOLHIMENTO') then    ");
		sql.append("                                            cast(sum(CASE WHEN tipo.OPERACAO_ESTOQUE = 'ENTRADA' THEN     ");
		sql.append("                                                if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,mecReparte.QTDE,0)    ");
		sql.append("                                                ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null,-mecReparte.QTDE,0)    ");
		sql.append("                                                      END)    ");
		sql.append("                                        - (select    ");
		sql.append("                                            sum(mecEncalhe.qtde)    ");
		sql.append("                                        from    ");
		sql.append("                                            lancamento lanc    ");
		sql.append("                                        LEFT JOIN    ");
		sql.append("                                            chamada_encalhe_lancamento cel    ");
		sql.append("                                                on cel.LANCAMENTO_ID = lanc.ID    ");
		sql.append("                                        LEFT JOIN    ");
		sql.append("                                            chamada_encalhe ce    ");
		sql.append("                                                on ce.id = cel.CHAMADA_ENCALHE_ID    ");
		sql.append("                                        LEFT JOIN    ");
		sql.append("                                            chamada_encalhe_cota cec    ");
		sql.append("                                                on cec.CHAMADA_ENCALHE_ID = ce.ID    ");
		sql.append("                                        LEFT JOIN    ");
		sql.append("                                            cota cota    ");
		sql.append("                                                on cota.id = cec.COTA_ID    ");
		sql.append("                                        LEFT JOIN    ");
		sql.append("                                            conferencia_encalhe confEnc    ");
		sql.append("                                                on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID    ");
		sql.append("                                        LEFT JOIN    ");
		sql.append("                                            movimento_estoque_cota mecEncalhe    ");
		sql.append("                                                on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID    ");
		sql.append("                                        WHERE    ");
		sql.append("                                            lanc.id = l.id    ");
		sql.append("                                            and cota.id = c.id) AS SIGNED INT)    ");
		sql.append("                                        else      null    ");
		sql.append("                                    end) as venda    ");
		sql.append("                                FROM    ");
		sql.append("                                    lancamento l    ");
		sql.append("                                JOIN    ");
		sql.append("                                    produto_edicao pe    ");
		sql.append("                                        ON pe.id = l.produto_edicao_id     ");
		sql.append("                                LEFT JOIN    ");
		sql.append("                                    periodo_lancamento_parcial plp    ");
		sql.append("                                        ON plp.id = l.periodo_lancamento_parcial_id    ");
		sql.append("                                JOIN    ");
		sql.append("                                    produto p    ");
		sql.append("                                        ON p.id = pe.produto_id    ");
		sql.append("                                LEFT JOIN    ");
		sql.append("                                    movimento_estoque_cota mecReparte    ");
		sql.append("                                        on mecReparte.LANCAMENTO_ID = l.id    ");
		sql.append("                                LEFT JOIN    ");
		sql.append("                                    tipo_movimento tipo    ");
		sql.append("                                        ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID    ");
		sql.append("                                JOIN    ");
		sql.append("                                    cota c    ");
		sql.append("                                        ON mecReparte.COTA_ID = c.ID    ");
		sql.append("                                JOIN    ");
		sql.append("                                    pessoa pess    ");
		sql.append("                                        ON c.PESSOA_ID = pess.ID    ");
		sql.append("                                inner join    ");
		sql.append("                                    endereco_cota endCota    ");
		sql.append("                                        ON endCota.cota_id = c.id    ");
		sql.append("                                        and endCota.principal = true    ");
		sql.append("                                inner join    ");
		sql.append("                                    endereco endereco    ");
		sql.append("                                        ON endCota.endereco_id = endereco.id    ");
		sql.append("                                  JOIN    ");
		sql.append("                                    tipo_produto tp    ");
		sql.append("                                        ON p.TIPO_PRODUTO_ID = tp.ID   ");
		sql.append("                                JOIN     ");
		sql.append("                                    tipo_segmento_produto tsp    ");
		sql.append("                                        ON tsp.ID = p.TIPO_SEGMENTO_PRODUTO_ID    ");
		sql.append("                                WHERE    ");
		sql.append("                                    l.status in ('EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO')    ");
		sql.append("                                    AND tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE'    ");
		sql.append("                                    AND l.DATA_REC_DISTRIB BETWEEN DATE_SUB(NOW(), INTERVAL + 13 week) AND NOW()     ");
		sql.append("                                    AND c.SITUACAO_CADASTRO in ('ATIVO' , 'SUSPENSO')   ");
		sql.append("                                    AND TP.GRUPO_PRODUTO not in ('CROMO' , 'TALÃO')   ");
		sql.append("                                group by    ");
		sql.append("                                    pe.numero_edicao,    ");
		sql.append("                                    pe.id,    ");
		sql.append("                                    mecReparte.cota_id,    ");
		sql.append("                                    plp.numero_periodo   ");
		sql.append("                                ORDER BY    ");
		sql.append("                                    l.ID desc  ");
		sql.append("                                )T GROUP BY COTA_ID, lancId  ");
		sql.append("                            )T2 GROUP BY ctId  ");
		
		sql.append(" 		             )   ");
		
		sql.append(" 		       UNION  ");

		sql.append(" 		       (SELECT   ");
		sql.append(" 		          cast(c.id as unsigned) as cotaId,  ");
		sql.append(" 		           0 as vendaSum   ");
		sql.append(" 		         from cota c    ");
		sql.append(" 		           where c.SITUACAO_CADASTRO in ('ATIVO' , 'SUSPENSO'))  ");
		sql.append(" 		           order by cotaId  ");
		sql.append(" 		       )tempUnion   ");
		sql.append(" 		       group by cotaId  ");
		sql.append(" 		     ) as innerQuery  ");
		sql.append(" 		 group by innerQuery.cotaId ");
		
		
		
//		sql.append(" select ");
//		sql.append("     innerQuery.cota_id, ");
//		sql.append("     sum(innerQuery.faturamento), ");
//		sql.append("     SYSDATE() ");
//		sql.append(" from ");
//		sql.append("     (select "); 
//		sql.append("         epc.cota_id as cota_id, ");
//		sql.append("             (epc.qtde_recebida - epc.qtde_devolvida) * pe.preco_venda as faturamento ");
//		sql.append("     from ");
//		sql.append("         estoque_produto_cota epc ");
//		sql.append("     join lancamento l ON l.produto_edicao_id = epc.produto_edicao_id ");
//		sql.append("     join produto_edicao PE ON pe.id = epc.produto_edicao_id ");
//		sql.append("     join produto ON produto.ID = PE.PRODUTO_ID ");
//		sql.append("     join tipo_produto tp ON TP.id = produto.tipo_produto_id ");
//		sql.append("     JOIN cota c ON epc.COTA_ID = c.ID ");
//		sql.append("     where ");
//		sql.append("         tp.grupo_produto <> 'CROMO' ");
//		sql.append("             and tp.grupo_produto <> 'TALÃO' ");
//		sql.append("             and l.data_rec_distrib between DATE_SUB(NOW(), INTERVAL + 13 week) and NOW() ");
//		sql.append("             AND c.SITUACAO_CADASTRO in ('ATIVO' , 'SUSPENSO') ");
//		sql.append("     group by epc.cota_id , pe.id) as innerQuery ");
//		sql.append(" group by innerQuery.cota_id ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	}
	
	public void gerarRankingFaturamentoParaCotasSemRanking() {
		StringBuilder sql = new StringBuilder();

		sql.append(" INSERT INTO ranking_faturamento (COTA_ID, FATURAMENTO, DATA_GERACAO_RANK) ");
		sql.append(" select ");
		sql.append("     c.ID, ");
		sql.append("     0, SYSDATE() ");
		sql.append(" from ");
		sql.append("     cota c, ");
		sql.append("     lancamento l ");
		sql.append(" where ");
		sql.append("     not exists(select "); 
		sql.append("             rs.ID ");
		sql.append("         from ");
		sql.append("             ranking_faturamento rs ");
		sql.append("         where ");
		sql.append("             rs.COTA_ID = c.id) ");
		sql.append("         AND C.SITUACAO_CADASTRO IN ('ATIVO' , 'SUSPENSO') ");
		sql.append("         and l.data_rec_distrib between DATE_SUB(NOW(), INTERVAL + 13 week) and NOW() ");
		sql.append(" group by c.ID ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	}
	
	public void deletarRankingFaturamento() {
		StringBuilder sql = new StringBuilder();

		sql.append(" delete from ranking_faturamento; ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.executeUpdate();
	}
	
	@Override
	public void atualizarClassificacaoCota() {
	    StringBuilder sql = new StringBuilder();
	    sql.append("update cota c ");
	    sql.append("  join ranking_faturamento r on r.cota_id = c.id ");
	    sql.append("   set c.classificacao_espectativa_faturamento = ");
	    sql.append("   (case when r.faturamento < (select coalesce(valor_ate, 1000) ");
	    sql.append("                                 from distribuidor_classificacao_cota ");
	    sql.append("                                where cod_classificacao = 'D') then 'D' ");
	    sql.append("         when r.faturamento < (select coalesce(valor_ate, 2000) ");
	    sql.append("                                 from distribuidor_classificacao_cota ");
	    sql.append("                                where cod_classificacao = 'C') then 'C' ");
	    sql.append("         when r.faturamento < (select coalesce(valor_ate, 4000) ");
	    sql.append("                                 from distribuidor_classificacao_cota ");
	    sql.append("                                where cod_classificacao = 'B') then 'B' ");
	    sql.append("         else (case when (select count(p.id) ");
	    sql.append("                            from pdv p ");
	    sql.append("                           where p.cota_id = c.id) < 2 then 'A' else 'AA' end) ");
	    sql.append("   end) ");
	    
	    SQLQuery query = this.getSession().createSQLQuery(sql.toString());
	    query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<RankingFaturamento>  buscarPorCota(Cota cota){
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select rf from RankingFaturamento rf where "); 
		hql.append(" rf.cota = :cota");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("cota", cota);
		
		List<RankingFaturamento> list = query.list();
		return list;
		
	}

}
